package demo.web.controller

import base.utils.forward.HttpForwardManager
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by cl on 2017/7/3.
 * Http请求转发Controller（代理）
 */
@Controller
class ProxyController {

    @RequestMapping(value = "/forward/{code}")
    public void forward(HttpServletRequest req, HttpServletResponse resp, @PathVariable("code") String code) {
        HttpForwardManager.forward(req, resp, code);
    }

}
