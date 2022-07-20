package base.utils.forward;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by cl on 2017/7/1.
 * Http请求转发器
 */
public interface HttpForward {

    /**
     * 请求转发
     */
    void forward(HttpServletRequest req, HttpServletResponse resp, Forward forward);

}
