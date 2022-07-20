package base.utils.xml;

import java.io.StringWriter;
import java.util.*;

import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import base.utils.xml.handler.*;

public class XmlUtils {

    private XmlUtils() {
    }

    /**
     * xml解析成Map
     */
    public static Map<String, Object> xml2Map(String xml) throws Exception {
        return xml2Map(xml, null);
    }

    /**
     * xml解析成Map,忽略xml节点的属性,只取节点name和text
     */
    public static Map<String, Object> xml2Map(String xml, KeyValHandler handler) throws Exception {
        Document doc = DocumentHelper.parseText(xml);
        Element root = doc.getRootElement();
        ObjectMap map = new ObjectMap(handler);
        parse2Map(root, map, handler);
        return map;
    }

    /**
     * Map转换成XML
     */
    public static String map2Xml(Map<String, Object> map) throws Exception {
        return map2Xml(map, null);
    }

    /**
     * Map转换成XML
     */
    public static String map2Xml(Map<String, Object> map, KeyValHandler handler) throws Exception {
        return map2Xml(map, true, handler);
    }

    /**
     * Map转换成XML
     *
     * @param map        待转换的Map
     * @param ignoreNull 是否忽略空节点
     * @param handler    键值对处理器
     * @return
     * @throws Exception
     */
    public static String map2Xml(Map<String, Object> map, boolean ignoreNull, KeyValHandler handler) throws Exception {
        Document doc = DocumentHelper.createDocument();
        map2Xml(map, doc, ignoreNull, handler);
        StringWriter sw = new StringWriter();
        doc.write(sw);
        return sw.toString();
    }

    /**
     * xml解析成列表形式的节点信息
     */
    public static List<NodeInfo> xml2List(String xml) throws Exception {
        return xml2List(xml, null);
    }

    /**
     * xml解析成列表形式的节点信息
     */
    public static List<NodeInfo> xml2List(String xml, KeyValHandler handler) throws Exception {
        Map<String, Object> map = xml2Map(xml, handler);
        return map2List(map);
    }

    /**
     * Map解析成列表形式的节点信息
     */
    public static List<NodeInfo> map2List(Map<String, Object> map) throws Exception {
        List<NodeInfo> list = new ArrayList<NodeInfo>();
        parse2List(list, map, "", "");
        return list;
    }

    /**
     * 处理xml
     */
    public static String handleXml(String xml, XmlNodeHandler handler) throws Exception {
        StringWriter sw = new StringWriter();
        Document doc = DocumentHelper.parseText(xml);
        Element root = doc.getRootElement();
        handleXml(root, handler);
        doc.write(sw);

        return sw.toString();
    }

    /**
     * 处理Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> handleMap(Map<String, Object> map, KeyValHandler handler) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();

            if (val instanceof List) {
                List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> list = (List<Map<String, Object>>) val;
                for (Map<String, Object> m : list) {
                    resultList.add(handleMap(m, handler));
                }
                val = resultList;
            } else if (val instanceof Map) {
                val = handleMap((Map<String, Object>) val, handler);
            }

            if (null != handler) {
                Object[] rs = handler.handle(key, val);
                key = (String) rs[0];
                val = rs[1];
            }

            resultMap.put(key, val);
        }

        return resultMap;
    }

    /**
     * Element解析成Map
     */
    @SuppressWarnings("unchecked")
    private static void parse2Map(Element node, ObjectMap parentMap, KeyValHandler handler) throws Exception {
        String name = node.getName();
        if (parentMap.containsKey(name)) {
            Object item = parentMap.get(name);
            List<Object> list = null;
            if (item instanceof List) {
                list = (List<Object>) item;
            } else {
                list = new ArrayList<Object>();
                list.add(item);
                parentMap.put2(name, list);
            }

            ObjectMap map = new ObjectMap(handler);
            parse2Map(node, map, handler);
            list.add(map.get(name));
        } else {
            List<?> childs = node.elements();
            if (null != childs && childs.size() > 0) {
                ObjectMap map = new ObjectMap(handler);
                for (Object o : childs) {
                    Element e = (Element) o;
                    parse2Map(e, map, handler);
                }
                parentMap.put2(name, map);
            } else {
                parentMap.put2(name, node.getText());
            }
        }
    }

    /**
     * Map转换成XML
     *
     * @param map        待转换的Map
     * @param parentNode 父节点
     * @param ignoreNull 是否忽略空节点
     * @param handler    键值对处理器
     */
    @SuppressWarnings("unchecked")
    private static void map2Xml(Map<String, Object> map, Branch parentNode, boolean ignoreNull,
                                KeyValHandler handler) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();

            if (null != handler) {
                Object[] rs = handler.handle(name, value);
                name = (String) rs[0];
                value = rs[1];
            }

            if (value instanceof Collection) {
                Collection<Object> list = (Collection<Object>) value;
                for (Object o : list) {
                    // List中的子元素只能是Map或基本类型数据
                    map2Xml(name, o, parentNode, ignoreNull, handler);
                }
            } else {
                map2Xml(name, value, parentNode, ignoreNull, handler);
            }
        }
    }

    /**
     * Map中的键值对解析成xml节点
     */
    private static void map2Xml(String name, Object value, Branch parentNode, boolean ignoreNull,
                                KeyValHandler handler) {
        if (value instanceof Map) {
            Map<String, Object> valueMap = (Map<String, Object>) value;
            if (valueMap.size() > 0 || (!ignoreNull)) {
                Element node = parentNode.addElement(name);
                map2Xml(valueMap, node, ignoreNull, handler);
            }
        } else {
            String text = null == value ? "" : value.toString();
            if (text.trim().length() > 0 || (!ignoreNull)) {
                Element node = parentNode.addElement(name);
                node.setText(text);
            }
        }
    }

    /**
     * 将Map解析成列表形式的节点信息
     */
    @SuppressWarnings("unchecked")
    private static void parse2List(List<NodeInfo> nodeList, Map<String, Object> map, String parentPath,
                                   String parentNodePath) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String name = entry.getKey();
            Object obj = entry.getValue();

            String path = parentPath + "/" + name;
            String nodePath = parentNodePath + "/" + name;
            String value = null;
            Object rawData = obj;
            if (obj instanceof String) {
                value = (String) obj;
            }

            NodeInfo nodeInfo = new NodeInfo(path, nodePath, value, rawData);
            nodeList.add(nodeInfo);

            if (obj instanceof List) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) obj;
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> m = list.get(i);
                    parse2List(nodeList, m, path, nodePath + "[" + i + "]");
                }
            } else if (obj instanceof Map) {
                parse2List(nodeList, (Map<String, Object>) obj, path, nodePath);
            }
        }
    }

    /**
     * 遍历xml的每个节点，并进行处理
     */
    private static void handleXml(Element e, XmlNodeHandler handler) {
        List<?> childs = e.elements();
        if (null != childs && childs.size() > 0) {
            for (int i = 0; i < childs.size(); i++) {
                Element el = (Element) childs.get(i);
                handleXml(el, handler);
            }
        }

        if (null != handler) {
            handler.handle(e);
        }
    }

    /**
     * XML节点信息
     */
    public static class NodeInfo {

        /**
         * 节点真实路径
         */
        public String path;

        /**
         * 节点路径，大部分情况跟path相同，但list节点时会增加索引，如： .../ITEMKIND[0]/AMOUNT
         */
        public String nodePath;

        /**
         * 节点文本
         */
        public String value;

        /**
         * 节点原始数据
         */
        public Object rawData;

        public NodeInfo() {
        }

        public NodeInfo(String path, String nodePath, String value, Object rawData) {
            this.path = path;
            this.nodePath = nodePath;
            this.value = value;
            this.rawData = rawData;
        }
    }

}
