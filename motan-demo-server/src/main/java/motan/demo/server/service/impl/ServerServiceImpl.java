package motan.demo.server.service.impl;

import com.weibo.api.motan.config.springsupport.annotation.MotanService;
import motan.demo.service.ServerService;
import org.springframework.stereotype.Service;

/**
 * Created by cl on 2017/12/1.
 */
@Service
@MotanService
public class ServerServiceImpl implements ServerService {

    @Override
    public String testServer(String str) {
        return "Server returns " + str;
    }

}
