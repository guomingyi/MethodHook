package me.hookmethod;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;

public class HookManager implements IhookManager {
    private final static String TAG = "Hook_HookManager";
    private static HookManager mHookManager;
    private static Object mLock = new Object();
    private Context mContext;

    public HookManager(Context c) {
        HookManager.getInstance(c);
    }

    public HookManager(Context c, boolean flag) {
        mContext = c;
    }

    public static HookManager getInstance(Context c) {
        synchronized (mLock) {
            if (mHookManager == null) {
                Log.i(TAG, "hook new getInstance!");
                mHookManager = new HookManager(c, true);
            }
        }
        return mHookManager;
    }

    public boolean hookMethod(Method origin, Method replace) {
        return Hook.hook(origin, replace);
    }

    @Override
    public boolean hookMethod(Object receiver, String origin, String replace, Class<?>... params) {
        try {
            Method ori = receiver.getClass().getDeclaredMethod(origin, params);
            Method rep = receiver.getClass().getDeclaredMethod(replace, params);
            Log.i(TAG, "origin:"+origin+" replace:"+replace);
            return hookMethod(ori, rep);
        }
        catch (Exception e) {
            Log.i(TAG, "E:"+e);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Object callOriginMethod(Object receiver, String methodName, Object... params) {
        return Hook.callOrigin(receiver, methodName, params);
    }

    @Override
    public Method recoveryMethod(String origin) {
        return Hook.recovery(origin);
    }
}
