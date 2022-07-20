package test

import base.utils.date.DateUtils
import base.utils.groovy.shell.ShellExecutor
import org.junit.Test

/**
 * Created by cl on 2018/4/4.
 */
class TestShell {

    @Test
    void test() {
        def shell = "user.name + ', ' + formatDate(user.birthday) + ', ' + DateUtils.format(new Date(), 'yyyy-mm-dd HH:mm:ss');",
            params = [
                    user      : [
                            name    : "test_abc",
                            birthday: new Date()
                    ],
                    formatDate: { date ->
                        return DateUtils.format(date, "yyyy-mm-dd");
                    },
                    ".bc[]"   : 20,
                    DateUtils : DateUtils
            ];

        ShellExecutor.run(shell, params);
        new GroovyShell(new Binding(params)).evaluate(shell);

        def count = 1000;

        def cost = testCost(count, {
            ShellExecutor.run(shell, params);
        });
        println "ShellExecutor测试，执行次数：${count}，耗时：${cost}ms";

        cost = testCost(count, {
            new GroovyShell(new Binding(params)).evaluate(shell);
        });
        println "GroovyShell测试，执行次数：${count}，耗时：${cost}ms";
    }

    private int testCost(def count, def closure) {
        def startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            closure();
        }
        def endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

}
