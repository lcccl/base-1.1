package base.framework.dao.sql.support.impl;

import base.framework.dao.sql.support.SqlSupport;

/**
 * Created by cl on 2017/11/17.
 */
public class PostgreSQLSqlSupport implements SqlSupport {

    @Override
    public String getPageSql(String sql, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return "select * from (" + sql + ") a limit " + pageSize + ", " + offset;
    }

    @Override
    public String getTotalSql(String sql) {
        return "select count(1) from (" + sql + ") a";
    }

}
