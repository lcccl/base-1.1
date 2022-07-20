package base.framework.dao;

/**
 * Created by cl on 2017/4/12.
 */
public interface RowMapper<T> {

    T mapRow(Object[] objArray);

}
