package base.framework.distributed;

/**
 * Created by cl on 2018/4/8.
 * 分布式队列
 */
public interface Queue<T> {

    /**
     * 将元素放入到队列末尾
     *
     * @param t
     */
    void push(T t);

    /**
     * 将队列的首元素弹出
     *
     * @return
     */
    T pop();

    /**
     * 返回队列的长度
     *
     * @return
     */
    int size();

    /**
     * 清空队列
     */
    void clear();

}
