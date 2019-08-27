//
// Created by android on 19-8-23.
//
#define LOG_TAG "Hook_test-jni"

#include "art_method.h"

#define NELEM(x) (sizeof(x)/sizeof(*(x)))
#define PACKAGE_CLASS_NAME "me/hookmethod/ArtMethodNative"

static Method_Info mMethodInfo = {0};

/**
 * ArtMethod 结构在内存中属于线性布局所以可以
 * 使用计算两个artmethod地址偏移方法求artmethod结构体大小.
 * 用于跟直接sizeof结构大小做对比,如果相等那么是正常的,否则有问题.
 * */
static jint init_art_method_size(JNIEnv* env, jclass cl) {
    jmethodID java_m1 = NULL;
    jmethodID java_m2 = NULL;
    size_t method_width = 0;

    jclass clazz = env->FindClass(PACKAGE_CLASS_NAME);
    if (!clazz) {
        ALOGE("[-] Error: Find class %s failed", PACKAGE_CLASS_NAME);
        goto exit;
    }

    java_m1 = env->GetStaticMethodID(clazz, "m1", "()V");
    java_m2 = env->GetStaticMethodID(clazz, "m2", "()V");
    if (!java_m1 || !java_m2) {
        ALOGE("[-] Error: Find class %s method %s failed", PACKAGE_CLASS_NAME, "m1/m2");
        goto exit;
    }

    method_width = (reinterpret_cast<size_t>(java_m2) - reinterpret_cast<size_t>(java_m1));
    mMethodInfo.size = method_width;

exit:
    if(clazz) {
        env->DeleteLocalRef(clazz);
    }

    ALOGI("[-] method_width:%d", method_width);
    return method_width;
}

/**
 * 设置access_flags_的偏移offset
 */
static void set_access_flags_offset(JNIEnv* env, jclass cl, jint offset) {
    mMethodInfo.access_flags_offset = (u4)offset;
}

static char *mem_to_str(uint8_t *data, int length) {
    static char buf[1024*2];
    char tmp[8] = {0};
    char *p = buf;

    memset(buf, 0 ,sizeof(buf));
    for (int i = 0; i < length; i++) {
        sprintf(tmp, "%02x,", data[i]);
        strcat(p, tmp);
        p += strlen(tmp);
    }
    return buf;
}

/**
 * ori: 原方法
 * dst: 目标方法
 * 返回状态
 */
static jboolean hook_method(JNIEnv* env, jclass cl, jobject origin, jobject replace) {
    jmethodID jorigin = NULL;
    jmethodID jreplace    = NULL;
    int length = mMethodInfo.size;

    jorigin = env->FromReflectedMethod(origin);
    jreplace = env->FromReflectedMethod(replace);
    if (!jorigin || !jreplace) {
        ALOGE("find class %s method failed", PACKAGE_CLASS_NAME);
        return false;
    }

    u1 *p_origin = reinterpret_cast<u1 *>(jorigin);
    u1 *p_replace = reinterpret_cast<u1 *>(jreplace);

    ALOGI("origin[0]: %s\n", mem_to_str(p_origin, length));
    *(p_origin + mMethodInfo.access_flags_offset) &= ~ACC_PUBLIC;
    *(p_origin + mMethodInfo.access_flags_offset) |= ACC_PRIVATE;
    memcpy(p_origin, p_replace, length);
    ALOGI("origin[1]: %s\n", mem_to_str(p_origin, length));

    ALOGI("hook method finish.\n");
    return true;
}

/**
 * ori: 待备份的原方法
 * stub: 备份原方法到一个"桩"方法上.
*/
static jboolean backup_method(JNIEnv* env, jclass cls, jobject stub, jobject ori) {
    jmethodID jorigin = NULL;
    jmethodID jstub = NULL;

    jorigin = env->FromReflectedMethod(ori);
    jstub = env->FromReflectedMethod(stub);
    if (!jorigin || !jstub) {
        ALOGE("find class %s method failed", PACKAGE_CLASS_NAME);
        return false;
    }

    u1 *p_ori = reinterpret_cast<u1 *>(jorigin);
    u1 *p_stub = reinterpret_cast<u1 *>(jstub);

    memcpy(p_stub, p_ori, mMethodInfo.size);
    *(p_stub + mMethodInfo.access_flags_offset) &= ~ACC_PUBLIC;
    *(p_stub + mMethodInfo.access_flags_offset) |= ACC_PRIVATE;
    ALOGI("backStub:  %s\n", mem_to_str(p_stub, mMethodInfo.size));
    return true;
}

static JNINativeMethod g_methods[] ={
    { "initArtMethodSize",   "()I",                                                     (void *)init_art_method_size },
    { "setAccessFlagOffset", "(I)V",                                                    (void *)set_access_flags_offset },
    { "hookMethod",          "(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;)Z", (void *)hook_method },
    { "backupMethod",        "(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;)Z", (void *)backup_method },
};


static bool register_jni_method(JNIEnv* env) {
    jclass clazz = env->FindClass(PACKAGE_CLASS_NAME);
    bool r = false;
    if (!clazz) {
        ALOGE("[-] find class %s failed", PACKAGE_CLASS_NAME);
        goto exit;
    }

    if (env->RegisterNatives(clazz, g_methods, NELEM(g_methods)) < 0) {
        ALOGE("[-] register class method failed");
        goto exit;
    }
    r = true;

exit:
    if (clazz) {
        env->DeleteLocalRef(clazz);
    }
    return r;
}

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv*	env = NULL;

    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        ALOGE("[-] Error: GetEnv failed");
        return JNI_ERR;
    }
    if (!register_jni_method(env)) {
        ALOGI("[*] Error >>> register_art_method");
        return JNI_ERR;
    }
    return JNI_VERSION_1_4;
}
