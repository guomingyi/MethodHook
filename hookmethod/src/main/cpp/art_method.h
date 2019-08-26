//
// Created by android on 19-8-23.
//

#ifndef ART_METHOD_H_
#define ART_METHOD_H_

#include <jni.h>
#include <string>
#include <android/log.h>

#ifndef ALOG
#define ALOG(priority, tag, ...) __android_log_print(ANDROID_##priority, tag, __VA_ARGS__)
#endif
#ifndef ALOGD
#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#endif
#ifndef ALOGE
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#endif
#ifndef ALOGI
#define ALOGI(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#endif


#ifdef __cplusplus
extern "C" {
#endif

typedef uint8_t  u1;
typedef uint16_t u2;
typedef uint32_t u4;
typedef uint64_t u8;

typedef enum {
    ACC_PUBLIC = 0x1,
    ACC_PRIVATE = 0x2,
    ACC_PROTECTED = 0x4,
    ACC_STATIC = 0x8,
    ACC_FINAL = 0x10,
    ACC_VOLATILE = 0x40,
    ACC_TRANSIENT = 0x80,
    ACC_NATIVE = 0x100,
    ACC_SYNTHETIC = 0x1000,
    ACC_ENUM = 0x4000,
} access_flag_t;

/**
 ArtMethod 结构
  // method 所属类，是 GCRoot，Class 类是可以 Moving GC 的
  // 这点需要特别关注，影响实现
  GcRoot<mirror::Class> declaring_class_;

  // java 层的 Modifier 只有其高 16 位
  // 低 16 位用作 ART 的内部运行，在 java 层被隐藏了
  std::atomic<std::uint32_t> access_flags_;

  // 方法的 CodeItem 在 Dex 中的偏移
  uint32_t dex_code_item_offset_;

  // 方法在 Dex 中的 index
  uint32_t dex_method_index_;

  // 虚方法则为实现方法在 VTable 中的 index
  // 非虚方法则是方法在 DexCodeCache 中的 index
  uint16_t method_index_;

  // 方法的热度，JIT 的重要参考
  uint16_t hotness_count_;

  struct PtrSizedFields {
    // 公共存储区域，用不到
    void* data_;

    // 非常重要！
    // 方法的 Code 入口
    // 如果没有编译，则
    // art_quick_to_interpreter_bridge
    // art_quick_generic_jni_trampoline
    // 如果 JIT/AOT 则为编译后的 native ß入口
    void* entry_point_from_quick_compiled_code_;
  } ptr_sized_fields_;
* */

typedef struct {
    u4 declaring_class_;
    u4 access_flags_;
    u4 dex_code_item_offset_;
    u4 dex_method_index_;
    u2 method_index_;
    u2 hotness_count_;
    struct PtrSizedFields {
        void *data_;
        void *entry_point_from_quick_compiled_code_;
    } ptr_sized_fields_;
} ArtMethod_t_9;

typedef struct {
    u4 declaring_class_;
    u4 access_flags_;
    u4 dex_code_item_offset_;
    u4 dex_method_index_;
    union {
        u2 hotness_count_;
        u2 imt_index_;
    };
    struct PtrSizedFields {
        void *data_;
        void *entry_point_from_quick_compiled_code_;
    } ptr_sized_fields_;
} ArtMethod_t_10;

typedef struct {
    u2 android_version;
    u2 sdk_api;
    u4 size;
    u4 access_flags_offset;
} Method_Info;

#ifdef __cplusplus
};
#endif

#endif
