package base.utils.xml.handler;

import org.dom4j.Element;

import java.util.List;

/**
 * Created by cl on 2017/3/22.
 * 键值对处理器
 */
public abstract class KeyValHandler implements XmlNodeHandler {

    @Override
    public void handle(Element e) {
        String nodeName = e.getName();
        String nodeText = e.getText();
        List<?> childs = e.elements();
        boolean hasChild = null != childs && childs.size() > 0;

        Object[] rs = handle(nodeName, hasChild ? null : nodeText);
        if (null != rs[0]) {
            e.setName((String) rs[0]);
        }
        if (!hasChild && null != rs[1]) {
            e.setText((String) rs[1]);
        }
    }

    /**
     * 处理节点的key,val
     */
    public abstract Object[] handle(String key, Object val);

}
