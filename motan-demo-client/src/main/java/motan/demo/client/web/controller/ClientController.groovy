package motan.demo.client.web.controller

import com.weibo.api.motan.config.springsupport.annotation.MotanReferer
import motan.demo.service.ClientService
import motan.demo.service.ServerService
import motan.demo.service.TestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

/**
 * Created by cl on 2017/12/1.
 */
@RestController
class ClientController {

    @Autowired
    ClientService clientService;

    @MotanReferer
    ServerService serverService;

    @MotanReferer
    TestService testService;

    Object test() {
        def rs = [
                client: clientService.testClient("test"),
                server: serverService.testServer("test"),
                test  : testService.test("test")
        ];

        return rs;
    }

}
