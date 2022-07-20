package demo.service.impl;

import base.framework.dao.Dao;
import demo.service.TestTabService;
import base.framework.model.Page;
import demo.model.po.TestTab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by cl on 2017/4/6.
 * 测试服务接口实现类
 */
@Service("testTabService")
class TestTabServiceImpl implements TestTabService {

    @Autowired
    Dao dao;

    @Transactional(readOnly = false)
    @Override
    void save(TestTab t) {
        if (t.id == "") {
            t.id = null;
            dao.save(t);
        } else {
            dao.update(t);
        }
    }

    @Transactional(readOnly = false)
    @Override
    void save(String code, String name, String remark) {
        TestTab po = new TestTab();
        po.setCode(code);
        po.setName(name);
        po.setRemark(remark);
        this.save(po);
    }

    @Transactional(readOnly = false)
    @Override
    void deleteById(String id) {
        TestTab po = dao.findById(TestTab.class, id);
        dao.delete(po);
    }

    @Override
    List<TestTab> findAll() {
        return dao.findAllBy(TestTab.class, null);
    }

    @Override
    List<TestTab> query(String code, String name) {
        def params = [
                code: code,
                name: name
        ];
        def list = dao.query("TestTab.query", params, TestTab.class);
        return list;
    }

    @Override
    TestTab findById(String id) {
        return dao.findById(TestTab.class, id);
    }

    @Override
    Page<TestTab> findByPage(TestTab po, int pageNo, int pageSize) {
        // 测试分页查询方法
        def params = [
                po: po
        ];
        Page<TestTab> page = dao.queryPage("TestTab.findByPage", params, TestTab.class, pageNo, pageSize);
//        Page<Object[]> objPage = dao.queryPage("TestTab.findByPage", params, pageNo, pageSize);

        return page;
    }

    @Override
    TestTab findByCodeAndName(String code, String name) {
        return dao.findBy(TestTab.class, [
                code: code,
                name: name
        ]);
    }

    @Override
    List<TestTab> findAllByCodeAndName(String code, String name) {
        return dao.findAllBy(TestTab.class, [
                code: code,
                name: name
        ]);
    }

    @Override
    List<TestTab> findByCodes(List<String> codes) {
        // 测试groovy模板
        def list = dao.query("TestTab.testQuery1", [
                codes: codes
        ], TestTab.class);
        // 测试freemarker模板
        list = dao.query("TestTab.testQuery2", [
                codes  : codes,
                extData: [
                        names: ["afe", "cc"]
                ],
                fields : [
                        code: "abc",
                        name: "bbc"
                ]
        ], TestTab.class);

        return list;
    }
}
