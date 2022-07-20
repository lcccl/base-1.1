package demo.web.controller

import base.framework.web.mvc.annotation.MappingStrategy
import base.utils.CommonUtils
import base.utils.ObjectUtils
import base.utils.SpringUtils
import base.utils.command.Command
import base.utils.command.CommandCallbackAdapter
import base.utils.phantomjs.PhantomJs4J
import demo.service.TestTabService
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

/**
 * Created by cl on 2017/6/13.
 */
@RestController
@MappingStrategy("byName")
class DemoController {

    Object index() {
        return [
                code: "1",
                msg : "success"
        ];
    }

    Object testSpringUtils() {
        TestTabService service = SpringUtils.getBean("testTabService", TestTabService.class);
        return [
                code   : "1",
                content: service.findAll()
        ];
    }

    /**
     * 测试ObjectUtils对象转Map
     */
    Object testObjectUtils() {
        TestTabService service = SpringUtils.getBean("testTabService", TestTabService.class);
        def list = service.findAll();
        def rs = [
                code   : "1",
                content: service.findAll()
        ];

        rs = ObjectUtils.object2Map(rs);

        return rs;
    }

    /**
     * 测试CommonUtils
     */
    Object testCommonUtils() {
        return CommonUtils.getClassPathText("forward.json", "UTF-8");
    }

    /**
     * 测试PhantomJs4J
     */
    Object testPhantomJs4J(HttpServletRequest req) {
        PhantomJs4J phantomJs4j = PhantomJs4J.create();
        Command cmd = phantomJs4j.execute("console.log('hello'); phantom.exit();");
        println cmd.getResult();

        def imagePath = "D:\\demo.jpg";
        cmd = phantomJs4j.renderWebPage("http://localhost:8080/base/test/demo", imagePath, new CommandCallbackAdapter() {
            @Override
            void onComplete() {
                println "图片生成完成，图片路径：${imagePath}";
            }
        });
        println "render /base/test/demo " + cmd.getResult();

        return "success";
    }

}
