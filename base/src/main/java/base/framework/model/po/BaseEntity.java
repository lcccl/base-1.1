package base.framework.model.po;


import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by cl on 2017/4/6.
 * 实体类基类，包含dateCreated、lastUpdated、version等字段
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

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

    @Column(name = "version")
    protected Integer version;

    @PrePersist
    public void prePersist() {
        Date nowDate = new Date();
        dateCreated = nowDate;
        lastUpdated = nowDate;
        version = 1;
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdated = new Date();
        if (null != version) {
            version++;
        } else {
            version = 1;
        }
    }

    /**
     * 将属性克隆到另外一个对象中
     *
     * @param clazz
     * @return
     */
    public <T> T cloneTo(Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            BeanUtils.copyProperties(this, t);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /*
     * Getters and Setters
     */
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
