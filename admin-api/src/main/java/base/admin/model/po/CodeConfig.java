package base.admin.model.po;

import base.framework.model.po.AbstractIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by cl on 2017/11/17.
 * 参数配置表
 */
@Entity
@Table(name = "c_codeconfig")
public class CodeConfig extends AbstractIdEntity implements Serializable {

    /**
     * 配置类型代码
     */
    @Column(name = "configtype")
    private String configType;

    /**
     * 配置代码
     */
    @Column(name = "configcode")
    private String configCode;

    /**
     * 配置描述
     */
    @Column(name = "configdesc")
    private String configDesc;

    /**
     * 配置值
     */
    @Column(name = "configvalue")
    private String configValue;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 有效标志
     */
    @Column(name = "validind", columnDefinition = "char(1)")
    private Boolean validInd = true;

    /**
     * 创建日期
     */
    @Column(name = "date_created")
    protected Date dateCreated;

    /**
     * 最后更新日期
     */
    @Column(name = "last_updated")
    protected Date lastUpdated;

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public String getConfigDesc() {
        return configDesc;
    }

    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getValidInd() {
        return validInd;
    }

    public void setValidInd(Boolean validInd) {
        this.validInd = validInd;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
