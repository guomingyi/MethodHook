package me.hookmethod;

import android.util.Log;
import java.lang.reflect.Method;

public class Reflection {
    private static final String TAG = "Reflection";

    private static Object sVmRuntime;
    private static Method setHiddenApiExemptions;

    static {
        try {
            Method forName = Class.class.getDeclaredMethod("forName", String.class);
            Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);

            Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
            Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
            setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
            sVmRuntime = getRuntime.invoke(null);
        }
        catch (Throwable e) {
            Log.e(TAG, "reflect bootstrap failed:", e);
        }
    }

    /**
     * make specific methods exempted from hidden API check.
     *
     * @param methods the method signature prefix, such as "Ldalvik/system", "Landroid" or even "L"
     * @return true if success
     */
    public static boolean exempt(String... methods) {
        if (sVmRuntime == null || setHiddenApiExemptions == null) {
            return false;
        }

        try {
            setHiddenApiExemptions.invoke(sVmRuntime, new Object[]{methods});
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * Make all hidden API exempted.
     *
     * @return true if success.
     */
    public static boolean exemptAll() {
        return exempt(new String[]{"L"});
    }
}
