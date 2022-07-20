package base.framework.model.po;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Created by cl on 2017/4/7.
 * ID主键实体基类
 */
@MappedSuperclass
public abstract class AbstractIdEntity implements Serializable {

    @Id
    @GenericGenerator(name = "PKUUID", strategy = "uuid")
    @GeneratedValue(generator = "PKUUID")
    @Column(name = "id")
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (null == o || this.getClass() != o.getClass()) {
            return false;
        }
        return null != id && id.equals(((AbstractIdEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return null != id ? id.hashCode() : super.hashCode();
    }

}
