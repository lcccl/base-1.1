package base.admin.web.controller

import base.admin.common.SysConstants
import base.admin.service.UserService
import base.framework.exception.CommonBusinessException
import base.framework.model.JsonResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

/**
 * Created by cl on 2017/11/17.
 * 用户Controller
 */
@RestController
class UserController {

    @Autowired
    UserService userService;

    /**
     * 获取当前登录用户信息
     */
    Object getSessionUserInfo(HttpServletRequest req) {
        def user = req.getSession().getAttribute(SysConstants.SESSION_USER);
        return new JsonResult(user);
    }

    /**
     * 修改密码
     */
    Object modifyPassword(HttpServletRequest req,
                          @RequestParam(name = "oldPwd") String oldPwd, @RequestParam(name = "newPwd") String newPwd) {
        def user = req.getSession().getAttribute(SysConstants.SESSION_USER);
        try {
            userService.updatePassword(user.userName, oldPwd, newPwd);
            return new JsonResult();
        } catch (CommonBusinessException e) {
            return new JsonResult(e);
        } catch (Exception e) {
            return new JsonResult(JsonResult.FAIL, e.getLocalizedMessage());
        }
    }

}
