package base.framework.dao;

import base.framework.model.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by cl on 2017/6/2.
 * 数据访问接口
 */
public interface Dao {

    /**
     * 保存
     */
    <T> T save(T entity);

    /**
     * 更新
     */
    <T> T update(T entity);

    /**
     * 删除
     */
    <T> T delete(T entity);

    /**
     * 根据主键查询一个实体
     */
    <T> T findById(Class<T> clazz, Serializable id);

    /**
     * 根据字段值为查询条件，查询一个实体
     */
    <T> T findBy(Class<T> clazz, Map<String, Object> fieldValues);

    /**
     * 根据字段值为查询条件，查询所有的实体
     */
    <T> List<T> findAllBy(Class<T> clazz, Map<String, Object> fieldValues);

    /**
     * 根据sqlId查询一个结果（sql保存在xml配置文件中，根据sqlId查找对应的sql语句）
     */
    Object queryOne(String sqlId, Map<String, Object> params);

    /**
     * 根据sqlId查询一个结果（sql保存在xml配置文件中，根据sqlId查找对应的sql语句）
     */
    <T> T queryOne(String sqlId, Map<String, Object> params, Class<T> clazz);

    /**
     * 根据sqlId查询一个列表（sql保存在xml配置文件中，根据sqlId查找对应的sql语句）
     */
    List<Object[]> query(String sqlId, Map<String, Object> params);

    /**
     * 根据sqlId查询一个列表（sql保存在xml配置文件中，根据sqlId查找对应的sql语句）
     */
    <T> List<T> query(String sqlId, Map<String, Object> params, Class<T> clazz);

    /**
     * 根据sqlId查询一个列表（sql保存在xml配置文件中，根据sqlId查找对应的sql语句）
     */
    <T> List<T> query(String sqlId, Map<String, Object> params, RowMapper<T> mapper);

    /**
     * 根据sqlId分页查询（sql保存在xml配置文件中，根据sqlId查找对应的sql语句）
     */
    Page<Object[]> queryPage(String sqlId, Map<String, Object> params, int pageNum, int pageSize);

    /**
     * 根据sqlId分页查询（sql保存在xml配置文件中，根据sqlId查找对应的sql语句）
     */
    <T extends Serializable> Page<T> queryPage(String sqlId, Map<String, Object> params, Class<T> clazz, int pageNum, int pageSize);

    /**
     * 根据sqlId分页查询（sql保存在xml配置文件中，根据sqlId查找对应的sql语句）
     */
    <T extends Serializable> Page<T> queryPage(String sqlId, Map<String, Object> params, RowMapper<T> mapper, int pageNum, int pageSize);

    /**
     * 根据sqlId执行sql语句
     */
    int execute(String sqlId, Map<String, Object> params);

    /**
     * 根据sql语句查询一个结果
     */
    Object queryOneBySql(String sql, Map<String, Object> params);

    /**
     * 根据sql语句查询一个结果
     */
    <T> T queryOneBySql(String sql, Map<String, Object> params, Class<T> clazz);

    /**
     * 根据sql语句查询一个列表
     */
    List<Object[]> queryBySql(String sql, Map<String, Object> params);

    /**
     * 根据sql语句查询一个列表
     */
    <T> List<T> queryBySql(String sql, Map<String, Object> params, Class<T> clazz);

    /**
     * 根据sql语句查询一个列表
     */
    <T> List<T> queryBySql(String sql, Map<String, Object> params, RowMapper<T> mapper);

    /**
     * 根据sql语句分页查询
     */
    Page<Object[]> queryPageBySql(String sql, Map<String, Object> params, int pageNum, int pageSize);

    /**
     * 根据sql语句分页查询
     */
    <T extends Serializable> Page<T> queryPageBySql(String sql, Map<String, Object> params, Class<T> clazz, int pageNum, int pageSize);

    /**
     * 根据sql语句分页查询
     */
    <T extends Serializable> Page<T> queryPageBySql(String sql, Map<String, Object> params, RowMapper<T> mapper, int pageNum, int pageSize);

    /**
     * 执行sql语句
     */
    int executeBySql(String sql, Map<String, Object> params);
}
