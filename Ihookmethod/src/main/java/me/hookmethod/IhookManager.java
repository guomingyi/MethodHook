package me.hookmethod;

import java.lang.reflect.Method;

public interface IhookManager {
    boolean hookMethod(Method origin, Method replace);
    boolean hookMethod(Class<?> clz, String origin, String replace, Class<?>... params);
    Method recoveryMethod(String origin);
    Object callOriginMethod(Object receiver, String methodName, Object... params);
}
