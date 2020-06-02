package com.yun.nls.form;

/**
 * ajax返回实体
 * @author wyq
 */
public class AjaxResultDto {
    /**
     * 状态码
     */
    private String code;
    /**
     * 内容
     */
    private String msg;
    /**
     * 数据
     */
    private Object data;

    public AjaxResultDto() {
    }

    public AjaxResultDto(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public AjaxResultDto(String code, Object data) {
        this.code = code;
        this.data = data;
    }

    public AjaxResultDto(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
