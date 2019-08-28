package me.hookmethod;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Hook {
    private static final String TAG = "Hook_";
    private static HashMap<String, BackUpObj> sBackupMap = new HashMap<String, BackUpObj>();
    private static ArrayList<Method> stubMethodList = new ArrayList<Method>();

    private static ArrayList<Method> getBakStubMethod() {
        final ArrayList<Method> s = stubMethodList;
        if (s.size() == 0) {
            try {
                Method[] methods = BackUpStub.class.getDeclaredMethods();
                for (int i = 0; i < methods.length; i++) {
                    s.add(methods[i]);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "E:"+e);
            }
        }
        return s;
    }

    /** Hook方法入口,原方法保存在sBackupMap中 */
    public static boolean hook(Method origin, Method replace) {
        if (!ArtMethodNative.init()) {
            return false;
        }

        try {
            /** index 用于拿到桩方法索引 */
            Method backUp = getBakStubMethod().get(0);
            int access_flags = ArtMethodNative.backupMethod(backUp, origin);
            if (access_flags > 0) {
                backUp.setAccessible(true);
                /** 哈希表保存 */
                sBackupMap.put(origin.getName(), new BackUpObj(access_flags, backUp, origin));
                getBakStubMethod().remove(backUp);
                Log.i(TAG, "stubList:"+getBakStubMethod().size());
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
    public static Object callOrigin(Object receiver, String method, Object...params) {
        try {
            if (sBackupMap.get(method).getUsed() > 0) {
                Method backup = sBackupMap.get(method).getBackup();
                return backup.invoke(receiver, params);
            }
            Log.i(TAG, "callOrigin: failure.");
        } catch (Exception e) {
            Log.i(TAG, "callOrigin.:"+e);
            e.printStackTrace();
        }
        return null;
    }

    public static Method recovery(String origin) {
        try {
            Method backup = sBackupMap.get(origin).getBackup();
            Method recovery = sBackupMap.get(origin).getRecovery();
            int access = sBackupMap.get(origin).getAccess_flags();
            ArtMethodNative.recoveryMethod(recovery, backup, access);
            getBakStubMethod().add(backup);
            sBackupMap.remove(origin);
            Log.i(TAG, "stubList:"+getBakStubMethod().size());
            return recovery;
        }
        catch (Exception e) {
            Log.i(TAG, "e:"+e);
            e.printStackTrace();
        }
        return null;
    }
}
