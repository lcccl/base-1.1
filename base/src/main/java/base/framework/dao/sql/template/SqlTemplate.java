package base.framework.dao.sql.template;

/**
 * Created by cl on 2017/6/5.
 * SQL模板
 */
public class SqlTemplate {

    /* sqlId */
    private String id;

    /* 模板类型 */
    private String type;

    /* 模板 */
    private String tpl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTpl() {
        return tpl;
    }

    public void setTpl(String tpl) {
        this.tpl = tpl;
    }

}
