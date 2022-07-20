package base.framework.dao.impl;

import base.framework.dao.Dao;
import base.framework.dao.RowMapper;
import base.framework.dao.sql.support.SqlSupport;
import base.framework.dao.sql.support.SqlSupportFactory;
import base.framework.dao.sql.template.SqlResult;
import base.framework.dao.sql.template.SqlTemplateManager;
import base.framework.model.Page;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by cl on 2017/6/2.
 * Dao的Jpa实现
 */
public class JpaDao implements Dao {

    private EntityManager entityManager;

    /* SQL模板管理器 */
    private SqlTemplateManager sqlTemplateManager;

    /* 数据库类型 */
    private String dbType;

    public JpaDao(EntityManager entityManager, String dbType, String sqlPath) {
        this.entityManager = entityManager;
        this.dbType = dbType;

        // 初始化SQL模板引擎，加载配置
        sqlTemplateManager = new SqlTemplateManager();
        sqlTemplateManager.load(sqlPath);
    }

    public JpaDao(EntityManager entityManager, SqlTemplateManager sqlTemplateManager, String dbType) {
        this.entityManager = entityManager;
        this.sqlTemplateManager = sqlTemplateManager;
        this.dbType = dbType;
    }

    @Override
    public <T> T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public <T> T update(T entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public <T> T delete(T entity) {
        entityManager.remove(entity);
        return entity;
    }

    @Override
    public <T> T findById(Class<T> clazz, Serializable id) {
        try {
            return entityManager.find(clazz, id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T> T findBy(Class<T> clazz, Map<String, Object> fieldValues) {
        Query query = this.createConditionQuery(clazz, fieldValues);
        return (T) getSingleResult(query);
    }

    @Override
    public <T> List<T> findAllBy(Class<T> clazz, Map<String, Object> fieldValues) {
        Query query = this.createConditionQuery(clazz, fieldValues);
        return query.getResultList();
    }

    /*======================= 通过sqlId对应的sql模板查询 - begin  =======================*/
    @Override
    public Object queryOne(String sqlId, Map<String, Object> params) {
        SqlResult rs = sqlTemplateManager.getSqlResult(sqlId, params);
        return this.queryOneBySql(rs.getSql(), rs.getParamMap());
    }

    @Override
    public <T> T queryOne(String sqlId, Map<String, Object> params, Class<T> clazz) {
        SqlResult rs = sqlTemplateManager.getSqlResult(sqlId, params);
        return this.queryOneBySql(rs.getSql(), rs.getParamMap(), clazz);
    }

    @Override
    public List<Object[]> query(String sqlId, Map<String, Object> params) {
        SqlResult rs = sqlTemplateManager.getSqlResult(sqlId, params);
        return this.queryBySql(rs.getSql(), rs.getParamMap());
    }

    @Override
    public <T> List<T> query(String sqlId, Map<String, Object> params, Class<T> clazz) {
        SqlResult rs = sqlTemplateManager.getSqlResult(sqlId, params);
        return this.queryBySql(rs.getSql(), rs.getParamMap(), clazz);
    }

    @Override
    public <T> List<T> query(String sqlId, Map<String, Object> params, RowMapper<T> mapper) {
        SqlResult rs = sqlTemplateManager.getSqlResult(sqlId, params);
        return this.queryBySql(rs.getSql(), rs.getParamMap(), mapper);
    }

    @Override
    public Page<Object[]> queryPage(String sqlId, Map<String, Object> params, int pageNum, int pageSize) {
        SqlResult rs = sqlTemplateManager.getSqlResult(sqlId, params);
        return this.queryPageBySql(rs.getSql(), rs.getParamMap(), pageNum, pageSize);
    }

    @Override
    public <T extends Serializable> Page<T> queryPage(String sqlId, Map<String, Object> params, Class<T> clazz, int pageNum, int pageSize) {
        SqlResult rs = sqlTemplateManager.getSqlResult(sqlId, params);
        return this.queryPageBySql(rs.getSql(), rs.getParamMap(), clazz, pageNum, pageSize);
    }

    @Override
    public <T extends Serializable> Page<T> queryPage(String sqlId, Map<String, Object> params, RowMapper<T> mapper, int pageNum, int pageSize) {
        SqlResult rs = sqlTemplateManager.getSqlResult(sqlId, params);
        return this.queryPageBySql(rs.getSql(), rs.getParamMap(), mapper, pageNum, pageSize);
    }

    @Override
    public int execute(String sqlId, Map<String, Object> params) {
        SqlResult rs = sqlTemplateManager.getSqlResult(sqlId, params);
        return this.executeBySql(rs.getSql(), rs.getParamMap());
    }
    /*======================= 通过sqlId对应的sql模板查询 - end  =======================*/


    /*======================= 通过sql语句查询 - begin  =======================*/
    @Override
    public Object queryOneBySql(String sql, Map<String, Object> params) {
        Query query = this.createNativeQuery(sql, params, null);
        return getSingleResult(query);
    }

    @Override
    public <T> T queryOneBySql(String sql, Map<String, Object> params, Class<T> clazz) {
        Query query = this.createNativeQuery(sql, params, clazz);
        return (T) getSingleResult(query);
    }

    @Override
    public List<Object[]> queryBySql(String sql, Map<String, Object> params) {
        Query query = this.createNativeQuery(sql, params, null);
        return query.getResultList();
    }

    @Override
    public <T> List<T> queryBySql(String sql, Map<String, Object> params, Class<T> clazz) {
        Query query = this.createNativeQuery(sql, params, clazz);
        return query.getResultList();
    }

    @Override
    public <T> List<T> queryBySql(String sql, Map<String, Object> params, RowMapper<T> mapper) {
        List<Object[]> list = this.queryBySql(sql, params);

        // 通过RowMapper组装结果集
        List<T> targetList = new ArrayList<T>();
        for (Object[] objArr : list) {
            T t = mapper.mapRow(objArr);
            targetList.add(t);
        }

        return targetList;
    }

    @Override
    public Page<Object[]> queryPageBySql(String sql, Map<String, Object> params, int pageNum, int pageSize) {
        return this.queryPageBySql(sql, params, pageNum, pageSize, new QueryRunner<Object[]>() {
            @Override
            public List<Object[]> query(String sql, Map<String, Object> params) {
                return queryBySql(sql, params);
            }
        });
    }

    @Override
    public <T extends Serializable> Page<T> queryPageBySql(String sql, Map<String, Object> params, final Class<T> clazz, int pageNum, int pageSize) {
        return this.queryPageBySql(sql, params, pageNum, pageSize, new QueryRunner<T>() {
            @Override
            public List<T> query(String sql, Map<String, Object> params) {
                return queryBySql(sql, params, clazz);
            }
        });
    }

    @Override
    public <T extends Serializable> Page<T> queryPageBySql(String sql, Map<String, Object> params, final RowMapper<T> mapper, int pageNum, int pageSize) {
        return this.queryPageBySql(sql, params, pageNum, pageSize, new QueryRunner<T>() {
            @Override
            public List<T> query(String sql, Map<String, Object> params) {
                return queryBySql(sql, params, mapper);
            }
        });
    }

    @Override
    public int executeBySql(String sql, Map<String, Object> params) {
        Query query = this.createNativeQuery(sql, params, null);
        return query.executeUpdate();
    }
    /*======================= 通过sql语句查询 - end  =======================*/


    /**
     * 根据字段值创建Query对象
     */
    private Query createConditionQuery(Class<?> clazz, Map<String, Object> fieldValues) {
        // 组装条件语句
        StringBuilder condition = new StringBuilder();
        if (null != fieldValues && fieldValues.size() > 0) {
            int count = 0;
            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                String fieldName = entry.getKey();
                Object value = entry.getValue();
                if (null != value) {
                    if (count++ > 0) {
                        condition.append(" and ");
                    }
                    condition.append("t.").append(fieldName).append(" = :").append(fieldName);
                }
            }
        }

        // 组装jpql语句
        String jpql = "select t from " + clazz.getName() + " t";
        if (condition.length() > 0) {
            jpql += " where " + condition.toString();
        }

        Query query = this.createQuery(jpql, fieldValues, clazz);

        return query;
    }

    /**
     * 根据jpql语句和参数创建Query对象
     */
    private Query createQuery(String jpql, Map<String, Object> params, Class<?> clazz) {
        Query query = null;
        if (null != clazz) {
            query = entityManager.createQuery(jpql, clazz);
        } else {
            query = entityManager.createQuery(jpql);
        }
        this.setQueryParameters(query, params);

        return query;
    }

    /**
     * 根据sql语句创建查询对象
     */
    private Query createNativeQuery(String sql, Map<String, Object> params, Class<?> clazz) {
        Query query = null;
        if (null != clazz) {
            query = entityManager.createNativeQuery(sql, clazz);
        } else {
            query = entityManager.createNativeQuery(sql);
        }
        this.setQueryParameters(query, params);

        return query;
    }

    /**
     * 设置Query对象的参数
     */
    private void setQueryParameters(Query query, Map<String, Object> params) {
        Set<Parameter<?>> sqlParams = query.getParameters();
        if (null != params && params.size() > 0) {
            for (Parameter<?> p : sqlParams) {
                String paramName = p.getName();
                Object paramValue = params.get(paramName);
                query.setParameter(paramName, paramValue);
            }
        }
    }

    /**
     * 用来替代Query.getSingleResult方法，防止查询不到数据报错
     */
    private Object getSingleResult(Query query) {
        try {
            List list = query.getResultList();
            return null != list && list.size() > 0 ? list.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据sql语句分页查询，通过QueryRunner来查询结果集
     */
    private <T extends Serializable> Page<T> queryPageBySql(String sql, Map<String, Object> params, int pageNum, int pageSize, QueryRunner<T> runner) {
        SqlSupport sqlSupport = SqlSupportFactory.getSupport(dbType);
        String listSql = sqlSupport.getPageSql(sql, pageNum, pageSize);
        String countSql = sqlSupport.getTotalSql(sql);

        // 查询分页
        List<T> resultList = runner.query(listSql, params);
        // 查询总记录数
        int totalCount = Integer.valueOf(this.queryOneBySql(countSql, params).toString());
        // 总页数
        int pageCount = totalCount / pageSize + (totalCount % pageSize > 0 ? 1 : 0);

        Page<T> page = new Page<T>();
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setTotalCount(totalCount);
        page.setPageCount(pageCount);
        page.setResultList(resultList);

        return page;
    }

    /**
     * 结果集查询器
     */
    private interface QueryRunner<T extends Serializable> {

        List<T> query(String sql, Map<String, Object> params);

    }

}
