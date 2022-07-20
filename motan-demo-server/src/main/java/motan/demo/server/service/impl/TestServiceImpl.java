package motan.demo.server.service.impl;

import com.weibo.api.motan.config.springsupport.annotation.MotanService;
import motan.demo.service.TestService;
import org.springframework.stereotype.Service;

/**
 * Created by cl on 2017/12/7.
 */
@Service
@MotanService
public class TestServiceImpl implements TestService {

    @Override
    public String test(String str) {
        return "Test returns " + str;
    }

}
