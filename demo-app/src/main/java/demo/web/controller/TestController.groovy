package demo.web.controller;

import base.framework.cache.CacheService;
import base.framework.cache.CacheServiceManager
import base.framework.dao.sql.template.SqlTemplateManager
import base.framework.model.Page
import base.framework.service.util.PacketTransferService
import base.utils.ObjectUtils
import base.utils.captcha.Captcha
import base.utils.captcha.CaptchaUtils
import base.utils.ptu.PacketListener;
import base.utils.ptu.PacketTransferUtils;
import base.utils.ptu.model.PacketConfig;
import base.utils.ptu.model.PacketResult;
import demo.service.TestTabService;
import demo.model.po.TestTab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession;

/**
 * Created by cl on 2017/4/6.
 */
@Controller
@RequestMapping("/test")
class TestController {

    @Autowired
    TestTabService testTabService;

    @Autowired
    PacketTransferService packetTransferService;

    @ResponseBody
    @RequestMapping("/index")
    Object index() {
        // 测试findAll方法
        def list = testTabService.findAll();
        // 测试query方法
        def tabList = testTabService.query("test", null);
        // 测试findBy和findAllBy方法
        def po = testTabService.findByCodeAndName("test", "test001");
        def poList = testTabService.findAllByCodeAndName("test001", "测试111");

        return [
                list   : list,
                tabList: tabList,
                po     : po,
                poList : poList
        ];
    }

    @RequestMapping("/demo")
    String demo() {
        return "/test/demo";
    }

    @ResponseBody
    @RequestMapping("/ajaxQuery")
    Object ajaxQuery(TestTab t,
                     @RequestParam(value = "pageNo") int pageNo,
                     @RequestParam(value = "pageSize") int pageSize) {
        Page<TestTab> page = testTabService.findByPage(t, pageNo, pageSize);

        def list = page.getResultList();
        def voList = [];
        list.each({ it ->
            voList << ObjectUtils.object2Map(it);
        });

        return [
                pageNo    : pageNo,
                pageSize  : pageSize,
                totalCount: page.getTotalCount(),
                pageCount : page.getPageCount(),
                list      : voList
        ];
    }

    @ResponseBody
    @RequestMapping("/ajaxDelete")
    public Object ajaxDelete(@RequestParam(value = "id") String id) {
        testTabService.deleteById(id);
        return "success";
    }


    @ResponseBody
    @RequestMapping("/ajaxSave")
    public Object ajaxSave(TestTab t) {
        testTabService.save(t);
        return "success";
    }

    @ResponseBody
    @RequestMapping("/ajaxGet")
    public Object ajaxGet(@RequestParam(value = "id") String id) {
        def po = testTabService.findById(id);
        def vo = [:] << po.properties;
        vo.remove("class");
        return vo;
    }

    @RequestMapping("/testSendPacket")
    @ResponseBody
    public Object testSendPacket() {
        // 报文传输工具测试
        PacketTransferUtils ptu = new PacketTransferUtils();
        ptu.init();

        def conf = new PacketConfig([
                sendEncode    : "UTF-8",
                backEncode    : "UTF-8",
                packetHandler : "xmlTemplate",
                packetTransfer: "http",
                url           : "http://localhost:8080/base/test/testReceivePacket",
                template      : "${'<Packet>${content}</Packet>'}"
        ]);

        conf.setListener(new PacketListener() {
            @Override
            public String beforeSend(String sendPacket) {
                System.out.println("报文发送前");
                return sendPacket;
            }

            @Override
            public String afterSend(String backPacket) {
                System.out.println("报文发送后");
                return backPacket;
            }
        });

        PacketResult rs = ptu.sendPacket(conf, [content: "Hello"]);

        return rs.getBackPacket();
    }

    @RequestMapping("/testReceivePacket")
    public void testReceivePacket(@RequestBody String packet, HttpServletResponse resp)
            throws IOException {
        println "=================== 报文接口测试 =======================";
        println "接收报文：${packet}";

        resp.setContentType("application/xml");
        resp.getWriter().write("<Packet><Head><Status>Success</Status></Head></Packet>");
    }

    @RequestMapping("/testSendPacketService")
    @ResponseBody
    public Object testSendPacketService() {
        // 报文传输服务测试
        def params = [
                userName: "admin",
                password: "admin",
                testTab : [
                        code  : "Test001",
                        name  : "测试",
                        remark: "Test123_bfefe"
                ]
        ];

        // Spring Boot开发时取不到WEB-INF下的模板，所以测试时模板暂时放在classpath下
        PacketResult rs = packetTransferService.send("test001", params);

        return rs.getBackPacket();
    }

    @ResponseBody
    @RequestMapping("/testSendJsonPacket")
    Object testSendJsonPacket() {
        // 报文传输工具初始化
        PacketTransferUtils ptu = new PacketTransferUtils();
        ptu.init();

        // 测试发送JSON报文
        def json = [
                code: "test",
                name: "张三"
        ];
        PacketConfig config = new PacketConfig([
                sendEncode    : "UTF-8",
                backEncode    : "UTF-8",
                packetHandler : "json",
                packetTransfer: "http",
                url           : "http://localhost:8080/base/test/testReceiveJsonPacket"
        ]);

        def rs = ptu.sendPacket(config, json);

        return rs.data;
    }

    @ResponseBody
    @RequestMapping("/testReceiveJsonPacket")
    Object testReceiveJsonPacket(@RequestBody String packet) {
        println "=================== JSON接口测试 =======================";
        println "接收报文：${packet}";

        return [
                code: "success",
                msg : "成功"
        ];
    }

    @ResponseBody
    Object testSendFormPacket() {
        // 报文传输工具初始化
        PacketTransferUtils ptu = new PacketTransferUtils();
        ptu.init();

        // 表单请求测试
        def params = [
                code  : "test001",
                name  : "test_tab_001",
                remark: "测试123"
        ];

        PacketConfig config = new PacketConfig([
                sendEncode    : "UTF-8",
                backEncode    : "UTF-8",
                packetHandler : "form",
                packetTransfer: "httpForm",
                url           : "http://localhost:8080/base/test/testReceiveFormPacket"
        ]);



        def rs = ptu.sendPacket(config, params);

        return rs.getData();
    }

    @ResponseBody
    Object testReceiveFormPacket(TestTab testTab) {
        println "=================== Form接口测试 =======================";

        return [
                code: "success",
                data: testTab
        ];
    }

    @RequestMapping("/testSendRawPacket")
    @ResponseBody
    public Object testSendRawPacket() {
        // 报文传输工具测试
        PacketTransferUtils ptu = new PacketTransferUtils();
        ptu.init();

        def conf = new PacketConfig([
                sendEncode    : "UTF-8",
                backEncode    : "UTF-8",
                packetHandler : "raw",
                packetTransfer: "http",
                url           : "http://localhost:8080/base/test/testReceivePacket"
        ]);

        PacketResult rs = ptu.sendPacket(conf, [packet: "<Packet><Head></Head></Packet>"]);

        return rs.getBackPacket();
    }

    /**
     * 测试代理转发
     */
    @RequestMapping("/testSendPacketByProxy")
    @ResponseBody
    public Object testSendPacketByProxy() {
        PacketTransferUtils ptu = new PacketTransferUtils();
        ptu.init();

        def conf = new PacketConfig([
                sendEncode    : "UTF-8",
                backEncode    : "UTF-8",
                packetHandler : "raw",
                packetTransfer: "http",
                url           : "http://localhost:8080/base/proxy/forward/test"
        ]);

        PacketResult rs = ptu.sendPacket(conf, [packet: "<Packet><Head></Head></Packet>"]);

        return rs.getBackPacket();
    }

    @RequestMapping("/testClearCache")
    @ResponseBody
    Object testClearCache() {
        CacheService cacheService = CacheServiceManager.getInstance().getCacheService();
        cacheService.clear("dataCache");
        return "缓存清理成功";
    }

    /**
     * 验证码测试
     */
    void testCaptcha(HttpServletResponse resp, HttpSession session) {
        // 禁止图像缓存。
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);

        resp.setContentType("image/jpeg");

        // 创建验证码，5个字符，验证码图片尺寸85*30
        Captcha captcha = CaptchaUtils.generateCaptcha(5, 85, 30);
        println "验证码：${captcha.getCode()}";
        // 输出验证码
        captcha.writeTo(resp.getOutputStream(), "jpeg");
    }

    /**
     * 测试验证码生成并保存到缓存
     */
    @ResponseBody
    Object testVerifyCode(HttpSession session) {
        CacheService cacheService = CacheServiceManager.getInstance().getCacheService();
        def cacheName = "dataCache",
            key = "captcha_${session.getId()}";
        def rs = cacheService.get(cacheName, key);
        if (null == rs) {
            rs = [
                    code      : CaptchaUtils.generateCode(5),
                    createTime: new Date()
            ];
            cacheService.put(cacheName, key, rs);
        }

        return rs;
    }

    String testProxy() {
        return "/test/proxy";
    }

    /**
     * 测试接收代理请求转发参数
     */
    @ResponseBody
    Object testReceiveProxyParam(HttpServletRequest req) {
        return [
                code: req.getParameter("code")
        ];
    }

    @ResponseBody
    Object testForEach() {
        def list = testTabService.findByCodes(["test001", "test002", "c"]);
        return [
                list: list
        ];
    }

    @ResponseBody
    Object clearTemplateCache() {
        SqlTemplateManager.reload();
        return [
                message: "SQL模板重新加载成功"
        ];
    }

}
