package me.hookmethod;

import android.content.Context;
import android.util.Log;

public class RefsManager {
    private static final String TAG = "RefsManager-hook";
    public static final String JAR_PATH = "/vendor/framework/hookmethod_dex.jar";
    public static final String CLASS_PATH = "me.hookmethod.HookManager";

    private static dalvik.system.PathClassLoader sPcLoader = null;
    private static IhookManager mIhookManager = null;

    public static Object getInstance(Context context, final String jarPath, final String classPath) {
            try {
                if (sPcLoader == null) {
                    sPcLoader = new dalvik.system.PathClassLoader(jarPath, context.getClassLoader());
                }

                Class mferclass = sPcLoader.loadClass(classPath);
                java.lang.reflect.Constructor clazzConstructfunc = mferclass.getConstructor(new Class[] {Context.class});
                clazzConstructfunc.setAccessible(true);
                Object obj = clazzConstructfunc.newInstance(context);
                Log.i(TAG, "instance success:" + obj+" path: "+jarPath+" class: "+classPath);
                return obj;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            Log.i(TAG, "instance failed:" +" path: "+jarPath+" class: "+classPath);
            return null;
    }

    public static Object getInstance(Context context) {
        try {
            String jar = JAR_PATH;
            String cls = CLASS_PATH;
            return getInstance(context, jar, cls);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static IhookManager instance(Context c) {
        if (mIhookManager == null) {
            mIhookManager = (IhookManager)getInstance(c);
        }
        return mIhookManager;
    }
}
