package base.admin.model.po;

import base.framework.model.po.AbstractIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by cl on 2017/11/17.
 * 用户表
 */
@Entity
@Table(name = "t_user")
public class User extends AbstractIdEntity implements Serializable {

    /**
     * 用户名
     */
    @Column(name = "username")
    private String userName;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 密码salt
     */
    @Column(name = "password_salt")
    private String passwordSalt;

    /**
     * 姓名
     */
    @Column(name = "name")
    private String name;

    /**
     * 生日（格式：yyyy-MM-dd）
     */
    @Column(name = "birthday")
    private String birthday;

    /**
     * 性别
     */
    @Column(name = "sex")
    private String sex;

    /**
     * 证件类型
     */
    @Column(name = "identify_type")
    private String identifyType;

    /**
     * 证件号
     */
    @Column(name = "identify_no")
    private String identifyNo;

    /**
     * 电子邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 电话号码
     */
    @Column(name = "tel")
    private String tel;

    /**
     * 手机号码
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 头像
     */
    @Column(name = "head_img")
    private String headImg;

    /**
     * 创建日期
     */
    @Column(name = "date_created")
    private Date dateCreated;

    /**
     * 最后登录日期
     */
    @Column(name = "last_login_time")
    private Date lastLoginTime;

    /**
     * 有效标志
     */
    @Column(name = "validind", columnDefinition = "char(1)")
    private String validInd;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }

    public String getIdentifyNo() {
        return identifyNo;
    }

    public void setIdentifyNo(String identifyNo) {
        this.identifyNo = identifyNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getValidInd() {
        return validInd;
    }

    public void setValidInd(String validInd) {
        this.validInd = validInd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
