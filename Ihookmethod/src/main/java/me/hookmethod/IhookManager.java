package me.hookmethod;

import java.lang.reflect.Method;

public interface IhookManager {
    Method recoveryMethod(String origin);
    boolean hookMethod(Object receiver, String origin, String replace, Class<?>... params);
    Object callOriginMethod(Object receiver, String methodName, Object... params);
}
