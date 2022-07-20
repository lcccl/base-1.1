package base.utils.groovy.shell;

import base.utils.codec.Md5Utils;
import groovy.lang.GroovyClassLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cl on 2018/4/4.
 * GroovyShell执行器
 */
public class ShellExecutor {

    /* 缓存动态创建的ShellRunner实例 */
    private static Map<String, ShellRunner> shellCache = new HashMap<String, ShellRunner>();

    private static GroovyClassLoader loader = new GroovyClassLoader();

    private ShellExecutor() {
    }

    /**
     * 执行脚本
     *
     * @param shell 脚本
     * @param args  参数
     * @return 脚本执行结果
     */
    public static Object run(String shell, Map<String, Object> args) {
        String key = Md5Utils.encode(shell);

        ShellRunner runner = shellCache.get(key);
        if (null == runner) {
            synchronized (ShellExecutor.class) {
                runner = shellCache.get(key);
                if (null == runner) {
                    StringBuilder classScript = new StringBuilder();
                    classScript.append("import base.utils.groovy.shell.ShellRunner;\n");
                    classScript.append("class ShellRunner$").append(key).append(" implements ShellRunner {\n");
                    classScript.append("Object run(Map<String, Object> args) {\n");
                    classScript.append("def argsDelegate = new Expando(args?: [:]);\n");
                    classScript.append("argsDelegate.with { _delegate -> \n");
                    classScript.append(shell).append("\n");
                    classScript.append("};\n");
                    classScript.append("}\n");
                    classScript.append("}");

                    try {
                        Class<?> clazz = loader.parseClass(classScript.toString());
                        runner = (ShellRunner) clazz.newInstance();
                        shellCache.put(key, runner);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to create ShellRunner instance, reason: " +
                                e.getLocalizedMessage(), e);
                    }
                }
            }
        }

        return runner.run(args);
    }

    /**
     * 执行脚本，无参数
     *
     * @param shell 脚本
     * @return 脚本执行结果
     */
    public static Object run(String shell) {
        return run(shell, null);
    }

}

