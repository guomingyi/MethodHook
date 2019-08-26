package me.hookmethod;

/**
 * 备份桩函数, 由于Method new instance接口受限制与hide有点问题,
 * 从jni去malloc又有内存释放的风险麻烦,所以没有更合适方法前
 * 先用这个笨办法顶着,有更灵活方法再考虑.
 * github有开源方案解决这个问题,后面挪过来试试.(https://github.com/tiann/FreeReflection)
 * */
public class BackUpStub {
    private static void bak_0() {}

    private static void bak_1() {}
    private static void bak_2() {}
    private static void bak_3() {}
    private static void bak_4() {}
    private static void bak_5() {}
    private static void bak_6() {}
    private static void bak_7() {}
    private static void bak_8() {}
    private static void bak_9() {}
    private static void bak_10() {}

    private static void bak_11() {}
    private static void bak_12() {}
    private static void bak_13() {}
    private static void bak_14() {}
    private static void bak_15() {}
    private static void bak_16() {}
    private static void bak_17() {}
    private static void bak_18() {}
    private static void bak_19() {}
    private static void bak_20() {}

    private static void bak_21() {}
    private static void bak_22() {}
    private static void bak_23() {}
    private static void bak_24() {}
    private static void bak_25() {}
    private static void bak_26() {}
    private static void bak_27() {}
    private static void bak_28() {}
    private static void bak_29() {}
    private static void bak_30() {}

}
