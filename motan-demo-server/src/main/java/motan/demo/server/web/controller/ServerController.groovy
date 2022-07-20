package motan.demo.server.web.controller

import motan.demo.service.ServerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

/**
 * Created by cl on 2017/12/1.
 */
@RestController
class ServerController {

    @Autowired
    ServerService serverService;

    Object test() {
        def rs = [
                server: serverService.testServer("test")
        ];

        return rs;
    }

}
