package me.hookmethod;

import android.util.Log;

import java.lang.reflect.Method;

public class ArtMethodNative {
    final static String TAG = "Hook-ArtMethod";
    public static boolean isInit = false;

    public static boolean init() {
        if (isInit) {
            return true;
        }

        try {
            System.loadLibrary("arthook-lib");
            ArtMethodNative.initArtMethodSize();
            /**
             * 这个值是参考access_flags_ 在ArtMethod结构体中的偏移量.
             * 不同sdk版本可能不同需要适配.
             * */
            setAccessFlagOffset(4);
            isInit = true;
            Log.e(TAG, "init success.");
            return true;
        }
        catch (Exception e) {
            Log.e(TAG, "E:"+e);
            e.printStackTrace();
        }
        Log.e(TAG, "init failed!");
        return false;
    }

    /********************************************************************/
    /**
     * ArtMethod 结构在内存中属于线性布局,所以可以通过两个相邻的ArtMethod实例
     * 的差偏移来确定ArtMethod的size,即:
     * size = ArtMethod(实例2 addr) - ArtMethod(实例1 addr)
     * m1,m2将是起这个作用.
     */
    private final static void m1() {}
    private final static void m2() {}
    /********************************************************************/

    /**
     * 备份原函数方法，此方法完成native层method结构数据结构拷贝
     * 原函数方法将会备份到 "桩" 函数上.
     *
     * @param ori 即将被hook的方法
     * @param stub "桩" 方法
     * @return 是否成功
     */
    public static native boolean backupMethod(Method stub, Method ori);

    /**
     * hook的主方法，此方法只完成native层method结构数据结构替换
     * 利用此方法，origin对应的native层method结构将失去和java层method结构的联系
     *
     * @param replace 替换原方法的方法
     * @param origin 即将被hook的方法
     * @return 是否成功
     */
    public static native boolean hookMethod(Method origin, Method replace);

    /**
     * 计算Artmethod的宽度.
     */
    public static native int initArtMethodSize();

    /**
     * 设置access_flag在Artmethod的偏移.
     */
    public static native void setAccessFlagOffset(int offset);
}