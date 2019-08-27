package me.hookmethod;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Hook {
    private static final String TAG = "Hook_";
    private static HashMap<String, BackUpObj> sBackupMap = new HashMap<String, BackUpObj>();

    /** Hook方法入口,原方法保存在sBackupMap中 */
    public static boolean hook(Method origin, Method replace) {
        if (!ArtMethodNative.init()) {
            return false;
        }

        try {
            /** index 用于拿到桩方法索引 */
            int index = sBackupMap.size();
            Method backUp = BackUpStub.class.getDeclaredMethod("bak_"+index);
            if (ArtMethodNative.backupMethod(backUp, origin)) {
                backUp.setAccessible(true);
                /** put到哈希表保存 */
                sBackupMap.put(origin.getName(), new BackUpObj(replace.getDeclaringClass().getName(), backUp));
                /** 底层替换 */
                return ArtMethodNative.hookMethod(origin, replace);
            }
        }
        catch (Exception e) {
            Log.i(TAG, "hook.:"+e);
            e.printStackTrace();
        }
        return false;
    }

    /** 调用原方法 */
    public static Object callOrigin(Object receiver, String methodName, Object...params) {
        try {
            Method method = sBackupMap.get(methodName).getMethod();
            return method.invoke(receiver, params);
        } catch (Exception e) {
            Log.i(TAG, "callOrigin.:"+e);
            e.printStackTrace();
        }
        return null;
    }
}
