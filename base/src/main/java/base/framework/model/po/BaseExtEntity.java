package base.framework.model.po;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.io.Serializable;

/**
 * Created by cl on 2017/6/8.
 * 扩展的基础实体，提供ID、dateCreated、lastUpdated、version、deleteFlag、validFlag等字段
 */
@MappedSuperclass
public abstract class BaseExtEntity extends BaseIdEntity implements Serializable {

    /**
     * 删除标志
     */
    @Column(name = "delete_flag", columnDefinition = "char(1)")
    protected Boolean deleteFlag = false;

    /**
     * 有效标志
     */
    @Column(name = "valid_flag", columnDefinition = "char(1)")
    protected Boolean validFlag = true;

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Boolean getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(Boolean validFlag) {
        this.validFlag = validFlag;
    }
}
