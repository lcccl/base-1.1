package base.framework.dao.sql.support.impl;

import base.framework.dao.sql.support.SqlSupport;

/**
 * Created by cl on 2017/4/12.
 */
public class MySQLSqlSupport implements SqlSupport {

    @Override
    public String getPageSql(String sql, int pageNum, int pageSize) {
        int startRow = (pageNum - 1) * pageSize;
        return "select * from (" + sql + ") a limit " + startRow + ", " + pageSize;
    }

    @Override
    public String getTotalSql(String sql) {
        return "select count(1) from (" + sql + ") a";
    }

}
