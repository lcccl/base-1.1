package demo.service;

import base.framework.model.Page;
import demo.model.po.TestTab;

import java.util.List;

/**
 * Created by cl on 2017/4/6.
 * 测试服务接口
 */
public interface TestTabService {

    /**
     * 保存测试表数据
     */
    void save(TestTab t);

    /**
     * 保存测试表数据
     */
    void save(String code, String name, String remark);

    /**
     * 根据ID删除
     */
    void deleteById(String id);

    /**
     * 查询所有的测试表数据
     */
    List<TestTab> findAll();

    /**
     * 根据对象查询
     */
    List<TestTab> query(String code, String name);

    /**
     * 根据ID查询
     */
    TestTab findById(String id);

    /**
     * 分页查询
     */
    Page<TestTab> findByPage(TestTab t, int pageNo, int pageSize);

    /**
     * 根据代码和名称查询
     */
    TestTab findByCodeAndName(String code, String name);

    /**
     * 根据代码和名称查询
     */
    List<TestTab> findAllByCodeAndName(String code, String name);

    /**
     * 根据多个代码查询
     */
    List<TestTab> findByCodes(List<String> codes);

}
