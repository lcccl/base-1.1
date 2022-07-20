package base.admin.security

import base.framework.model.JsonResult
import base.utils.ServletUtils
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter

import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by cl on 2017/11/17.
 * 扩展用户身份认证filter，ajax请求结果特殊处理
 */
class ShiroAuthcFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (!ServletUtils.isAjaxRequest(req) || req.getRequestURI().endsWith(".html")) {
            return super.onAccessDenied(request, response);
        } else {
            // ajax请求接口，返回json
            JsonResult json = new JsonResult(JsonResult.FAIL, "用户未登录或会话超时");
            ServletUtils.writeJson(resp, json.toJsonString());
            return false;
        }
    }

}
