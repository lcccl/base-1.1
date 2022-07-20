package base.admin.service.impl

import base.admin.model.po.User
import base.admin.service.UserService
import base.framework.dao.Dao
import base.framework.exception.CommonBusinessException
import base.framework.security.shiro.PasswordEncryptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/**
 * Created by cl on 2017/11/17.
 */
@Service("userService")
class UserServiceImpl implements UserService {

    @Autowired
    Dao dao;

    @Autowired
    PasswordEncryptor passwordEncryptor;

    @Override
    User findByUserName(String userName) {
        return dao.findBy(User.class, [
                userName: userName
        ]);
    }

    @Transactional(readOnly = false)
    @Override
    User saveUser(User user) {
        // 密码加密
        user.passwordSalt = new Date().getTime() + "_" + new Random().nextInt(100000);
        user.password = passwordEncryptor.encrypt(user.password, user.passwordSalt);

        user.dateCreated = new Date();
        user.validInd = "1";

        return dao.save(user);
    }

    @Transactional(readOnly = false)
    @Override
    User updateUser(User user) {
        return dao.update(user);
    }

    @Transactional(readOnly = false)
    @Override
    User updatePassword(String userName, String oldPassword, String password) {
        def user = this.findByUserName(userName);

        // 校验旧密码
        def pwd = passwordEncryptor.encrypt(oldPassword, user.passwordSalt);
        if (user.password != pwd) {
            throw new CommonBusinessException("旧密码输入错误");
        }

        // 密码更新
        user.passwordSalt = new Date().getTime() + "_" + new Random().nextInt(100000);
        user.password = passwordEncryptor.encrypt(password, user.passwordSalt);

        return dao.update(user);
    }

}
