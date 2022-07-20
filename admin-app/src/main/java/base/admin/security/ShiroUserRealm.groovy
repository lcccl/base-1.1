package base.admin.security

import base.admin.model.po.User
import base.admin.service.UserService
import org.apache.shiro.authc.*
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import org.apache.shiro.util.ByteSource

/**
 * Created by cl on 2017/11/17.
 * Shiro的用户权限Realm
 */
class ShiroUserRealm extends AuthorizingRealm {

    UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 身份验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = token.getUsername();
        User user = userService.findByUserName(userName);
        if (null != user) {
            return new SimpleAuthenticationInfo(
                    user.getUserName(),
                    user.getPassword(),
                    ByteSource.Util.bytes(user.getPasswordSalt()), getName()
            );
        }

        return null;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
