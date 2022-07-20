package base.framework.dao.sql.template;

/**
 * Created by cl on 2017/6/9.
 * SQL模板处理结果
 */
public class SqlResult {

    /* 处理后的SQL语句 */
    private String sql;

    /* SQL参数Map */
    private ParamMap paramMap;

    public SqlResult() {
    }

    public SqlResult(String sql, ParamMap paramMap) {
        this.sql = sql;
        this.paramMap = paramMap;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public ParamMap getParamMap() {
        return paramMap;
    }

    public void setParamMap(ParamMap paramMap) {
        this.paramMap = paramMap;
    }

}
