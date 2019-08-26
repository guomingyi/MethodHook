package me.hookmethod;

import java.lang.reflect.Method;

/** 备份方法结构体,由于Method实例化接口被hide不方便,目前使用桩函数来实现备份方法,临时占了30个坑 */
public class BackUpObj {
    private String origin_declarName;
    private Method method;

    public BackUpObj(String s, Method m) {
        origin_declarName = s;
        method = m;
    }
    public String getOriginDeclarName() {
        return origin_declarName;
    }
    public Method getMethod() {
        return method;
    }
}
