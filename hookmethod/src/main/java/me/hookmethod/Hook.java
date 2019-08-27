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
            int access_flags = ArtMethodNative.backupMethod(backUp, origin);
            if (access_flags > 0) {
                backUp.setAccessible(true);
                /** 哈希表保存 */
                sBackupMap.put(origin.getName(), new BackUpObj(access_flags, backUp, origin));
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
            sBackupMap.get(origin).setUsed(0); // 标记为已经不再使用.
            return recovery;
        }
        catch (Exception e) {
            Log.i(TAG, "e:"+e);
            e.printStackTrace();
        }
        return null;
    }
}
