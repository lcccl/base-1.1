package base.admin.model.po;

import base.framework.model.po.AbstractIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by cl on 2017/11/17.
 * 数据字典表
 */
@Entity
@Table(name = "c_codetrans")
public class CodeTrans extends AbstractIdEntity implements Serializable {

    /**
     * 代码类型
     */
    private String codeType;

    /**
     * 代码值
     */
    private String code;

    /**
     * 父节点代码
     */
    @Column(name = "parentcode")
    private String parentCode;

    /**
     * 名称
     */
    @Column(name = "codename")
    private String codeName;

    /**
     * 标志
     */
    @Column(name = "flag")
    private String flag;

    /**
     * 代码层级
     */
    @Column(name = "codelevel")
    private Integer codeLevel;

    /**
     * 显示序号
     */
    @Column(name = "display_no")
    private Integer displayNo;

    /**
     * 有效标志
     */
    @Column(name = "validind", columnDefinition = "char(1)")
    private Boolean validInd = true;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getCodeLevel() {
        return codeLevel;
    }

    public void setCodeLevel(Integer codeLevel) {
        this.codeLevel = codeLevel;
    }

    public Integer getDisplayNo() {
        return displayNo;
    }

    public void setDisplayNo(Integer displayNo) {
        this.displayNo = displayNo;
    }

    public Boolean getValidInd() {
        return validInd;
    }

    public void setValidInd(Boolean validInd) {
        this.validInd = validInd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
