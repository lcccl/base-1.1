package base.framework.dao.sql.support;

/**
 * Created by cl on 2017/4/12.
 * SQL语句支持
 */
public interface SqlSupport {

    /**
     * 获取分页查询SQL
     */
    String getPageSql(String sql, int pageNum, int pageSize);

    /**
     * 获取总记录数SQL
     */
    String getTotalSql(String sql);

}
