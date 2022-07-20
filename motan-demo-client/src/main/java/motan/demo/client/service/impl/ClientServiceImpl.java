package motan.demo.client.service.impl;

import motan.demo.service.ClientService;
import org.springframework.stereotype.Service;

/**
 * Created by cl on 2017/12/1.
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Override
    public String testClient(String str) {
        return "Client returns " + str;
    }

}
