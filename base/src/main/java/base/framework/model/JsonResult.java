package base.framework.model;

import base.framework.exception.CommonBusinessException;
import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by cl on 2017/9/18.
 * JSON接口返回结果
 */
public class JsonResult implements Serializable {
    /* 状态常量：成功 */
    public final static int SUCCESS = 0;
    /* 状态常量：失败 */
    public final static int FAIL = 1;

    /* 返回代码 */
    private int code;

    /* 返回消息 */
    private String message;

    /* 返回的数据 */
    private Object data;

    public JsonResult() {
        this(SUCCESS, "", null);
    }

    public JsonResult(Object data) {
        this(SUCCESS, "", data);
    }

    public JsonResult(int status, String message) {
        this(status, message, null);
    }

    public JsonResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public JsonResult(CommonBusinessException e) {
        this(FAIL, e.getMessage(), null);
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }

    /*========================== Getters and Setters ==========================*/
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
