package com.august.common;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common
 * Author: August
 * Update: August(2015/9/24)
 * Description:响应信息类，用于封装响应信息到前台
 * 临时使用
 */
public class RetObj {
    private boolean flag = true;
    private String msg;
    private Object obj;


    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public RetObj() {
    }

    public RetObj(boolean flag, String msg, Object obj) {
        this.flag = flag;
        this.msg = msg;
        this.obj = obj;
    }

    public RetObj(String msg, boolean flag) {
        this.msg = msg;
        this.flag = flag;
    }
}
