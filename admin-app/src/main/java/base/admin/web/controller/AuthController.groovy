package base.admin.web.controller

import base.admin.common.SysConstants
import base.admin.service.UserService
import base.utils.ObjectUtils
import base.utils.date.DateUtils
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.subject.Subject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestParam

import javax.servlet.http.HttpServletRequest

/**
 * Created by cl on 2017/11/17.
 * 安全认证Controller
 */
@Controller
class AuthController {

    private final static Log logger = LogFactory.getLog(AuthController.class);

    @Autowired
    UserService userService;

    String login(
            @RequestParam(name = "userName") String userName,
            @RequestParam(name = "password") String password, HttpServletRequest req) {
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            logger.error("用户名密码错误");
            if (logger.isDebugEnabled()) {
                logger.error(e.getLocalizedMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (subject.isAuthenticated()) {
            logger.info("用户[${userName}]登录成功");
            // 获取用户信息，更新最后登录时间
            def user = userService.findByUserName(userName);
            user.lastLoginTime = new Date();
            userService.updateUser(user);

            // 用户信息保存到session
            def userVo = ObjectUtils.object2Map(user);
            userVo.remove("password");
            userVo.remove("passwordSalt");
            userVo.lastLoginTimeStr = DateUtils.format(userVo.lastLoginTime, "yyyy-MM-dd HH:mm:ss");
            req.getSession().setAttribute(SysConstants.SESSION_USER, userVo);
        } else {
            token.clear();
            return "redirect:/login.html?errCode=1";
        }

        return "redirect:/index.html";
    }

    String logout(HttpServletRequest req) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            def session = req.getSession(),
                userName = session.getAttribute(SysConstants.SESSION_USER)?.userName;

            subject.logout();

            logger.info("用户[${userName}]已安全登出");
        }

        return "redirect:/login.html";
    }

}
