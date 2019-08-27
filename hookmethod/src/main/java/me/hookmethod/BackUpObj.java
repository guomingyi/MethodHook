package me.hookmethod;

import java.lang.reflect.Method;

/**
 * 备份方法结构体.
 *
 * backup : 桩方法
 * recovery: 原方法,但是hook后会被改变为replace的内容,用于恢复使用
 * access_flags : 原方法的访问权限,被hook后被改变,这里备份用于恢复.
 * used : 使用中1,被恢复后归0.
 */
public class BackUpObj {
    private Method backup;
    private Method recovery;
    private int access_flags;
    private int used;

    public BackUpObj(int a, Method b, Method r) {
        access_flags = a;
        backup = b;
        recovery = r;
        used = 1;
    }
    public Method getBackup() {
        return backup;
    }
    public Method getRecovery() { return recovery; }
    public int getAccess_flags() { return access_flags; }
    public int getUsed() {
        return used;
    }
    public void setUsed(int u) {
        used = u;
    }
}
