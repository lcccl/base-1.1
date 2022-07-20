package demo.model.po;

import base.framework.model.po.BaseIdEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by cl on 2017/4/6.
 * 测试表
 */
@Entity
@Table(name = "test_tab")
public class TestTab extends BaseIdEntity implements Serializable {

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "remark")
    private String remark;

    /*
     * Getters and Setters
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
