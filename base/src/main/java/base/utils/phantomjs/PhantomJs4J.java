package base.utils.phantomjs;

import base.framework.cache.CacheService;
import base.framework.cache.CacheServiceManager;
import base.utils.CommonUtils;
import base.utils.command.Command;
import base.utils.command.CommandCallback;
import base.utils.command.CommandCallbackAdapter;
import base.utils.template.TemplateEngine;
import base.utils.template.TemplateEngineFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cl on 2017/7/7.
 * PhantomJs的Java API
 */
public class PhantomJs4J {

    /* js脚本模板缓存常量 */
    private final static String SCRIPT_CACHE_NAME = "phantomjs";
    private final static String SCRIPT_CACHE_KEY = "renderWebPage_script_tpl";

    private final static Log logger = LogFactory.getLog(PhantomJs4J.class);

    /* phantomjs根目录 */
    private String phantomHome;

    /* 超时时间 */
    private Long timeout;

    /* phantomjs命令路径 */
    private String cmdPath;

    public PhantomJs4J() {
    }

    public PhantomJs4J(String phantomHome, Long timeout) {
        this.phantomHome = phantomHome;
        this.timeout = timeout;

        cmdPath = phantomHome + File.separator + "bin" + File.separator + "phantomjs";
    }

    /**
     * 设置phantomjs根目录
     */
    public void setPhantomHome(String phantomHome) {
        this.phantomHome = phantomHome;
        cmdPath = phantomHome + File.separator + "bin" + File.separator + "phantomjs";
    }

    /**
     * 设置超时时间
     */
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    /**
     * 执行脚本，返回结果
     */
    public Command execute(String script) {
        return execute(script, new String[]{});
    }

    /**
     * 执行脚本、参数，返回结果
     */
    public Command execute(String script, String[] args) {
        return execute(script, args, null);
    }

    /**
     * 执行脚本、参数、回调，返回结果
     */
    public Command execute(String script, String[] args, CommandCallback callback) {
        try {
            // 创建临时js文件
            final File file = File.createTempFile(RandomStringUtils.randomAlphabetic(10), ".js");
            FileUtils.writeStringToFile(file, script, Charset.defaultCharset().displayName());

            // 执行js脚本，执行完成后删除临时js文件
            return execute(file, args, new CommandCallback[]{callback, new CommandCallbackAdapter() {
                @Override
                public void onComplete() {
                    if (null != file && file.exists()) {
                        file.delete();
                    }
                }
            }});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行脚本文件，返回结果
     */
    public Command execute(File scriptFile) {
        return execute(scriptFile, null);
    }

    /**
     * 执行脚本文件、参数，返回结果
     */
    public Command execute(File scriptFile, String[] args) {
        return execute(scriptFile, args, null);
    }

    /**
     * 执行脚本文件、参数、回调，返回结果
     */
    public Command execute(File scriptFile, String[] args, CommandCallback... callbacks) {
        String cmd = this.cmdPath + " " + scriptFile.getAbsolutePath();
        if (null != args) {
            cmd = cmd + " " + StringUtils.join(args, " ");
        }

        Command command = new Command(cmd, timeout);
        if (null != callbacks) {
            for (CommandCallback callback : callbacks) {
                if (null != callback) {
                    command.addCallback(callback);
                }
            }
        }
        command.execute();

        return command;
    }


    /**
     * 执行模板生成js脚本并执行，返回结果
     */
    public Command execute(String scriptTpl, Map<String, Object> params) {
        return execute(scriptTpl, params, null);
    }

    /**
     * 执行模板生成js脚本并执行、回调，返回结果
     */
    public Command execute(String scriptTpl, Map<String, Object> params, CommandCallback callback) {
        TemplateEngine templateEngine = TemplateEngineFactory.getEngine("groovy");
        String script = templateEngine.evaluate(scriptTpl, params, true);
        return execute(script, new String[]{}, callback);
    }

    /**
     * 将Web页面渲染成图片
     */
    public Command renderWebPage(String url, String imagePath) {
        return renderWebPage(url, imagePath, null);
    }

    /**
     * 将Web页面渲染成图片并执行回调
     */
    public Command renderWebPage(String url, String imagePath, CommandCallback callback) {
        CacheService cacheService = CacheServiceManager.getInstance().getCacheService();
        // 通过缓存获取默认的js脚本模板
        String scriptTpl = (String) cacheService.get(SCRIPT_CACHE_NAME, SCRIPT_CACHE_KEY);
        if (StringUtils.isEmpty(scriptTpl)) {
            scriptTpl = CommonUtils.getClassPathText("/base/utils/phantomjs/renderPage.tpl", "UTF-8");
            cacheService.put(SCRIPT_CACHE_NAME, SCRIPT_CACHE_KEY, scriptTpl);
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("url", url);
        params.put("imagePath", imagePath.replace("\\", "/"));
        return execute(scriptTpl, params, callback);
    }

    /*============================ 静态方法创建实例 - begin  ============================*/
    public static PhantomJs4J create() {
        return new PhantomJs4J(CommonUtils.getClassPathResourcePath("/phantomjs"), 15L);
    }

    public static PhantomJs4J create(String phantomHome, Long timeout) {
        return new PhantomJs4J(phantomHome, timeout);
    }
    /*============================ 静态方法创建实例 - end  ============================*/

}
