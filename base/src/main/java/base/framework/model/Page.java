package base.framework.model;


import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cl on 2017/4/12.
 * 分页对象
 */
public class Page<T extends Serializable> implements Serializable {

    /**
     * 当前页数
     */
    private int pageNum;

    /**
     * 每页的记录数
     */
    private int pageSize;

    /**
     * 总记录数
     */
    private int totalCount;

    /**
     * 总页数
     */
    private int pageCount;

    /**
     * 当前分页的结果列表
     */
    private List<T> resultList;

    public Page() {
    }

    /**
     * 克隆分页对象，替换分页存储的实体为其他类型
     *
     * @param clazz
     * @return
     */
    public <V extends Serializable> Page<V> cloneTo(Class<V> clazz) {
        try {
            Page<V> page = new Page<V>();
            page.setPageNum(pageNum);
            page.setPageSize(pageSize);
            page.setTotalCount(totalCount);

            if (null != resultList) {
                List<V> rsList = new ArrayList<V>();
                for (T po : resultList) {
                    V vo = clazz.newInstance();
                    BeanUtils.copyProperties(po, vo);
                    rsList.add(vo);
                }
                page.setResultList(rsList);
            }

            return page;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /*===================== Getters and Setters - begin =====================*/
    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }
    /*===================== Getters and Setters - end =====================*/

}
