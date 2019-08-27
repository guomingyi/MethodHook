package com.hook_test;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

import me.hookmethod.IhookManager;
import me.hookmethod.RefsManager;

public class HookTestDemo {
    static final String TAG = "Hook_TestDemo";
    private Context mContext;
    private EditText mTextview;
    private StringBuilder stringBuilder = new StringBuilder();
    private static IhookManager mIhookManager;

    public HookTestDemo() {

    }

    public void hook(Context context) {
        mContext = context;

        try {
           // mIhookManager = RefsManager.instance(context);
            if (mIhookManager == null) {
                mIhookManager = new me.hookmethod.HookManager(context);
            }

            // public
            mIhookManager.hookMethod(this,"test_public", "public_hook", String.class);

            // protect
            mIhookManager.hookMethod(this,"test_protect", "protect_hook", String.class);

            // private
            mIhookManager.hookMethod(this,"test_private", "private_hook", String.class);

            // static
            mIhookManager.hookMethod(this,"test_static", "static_hook", String.class);

            setText("hook 完成");
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "-e:"+e);
        }
    }

    public void recovery() {
        try {
            String origin = "test_public";
            mIhookManager.recoveryMethod(origin);
            Method method = this.getClass().getDeclaredMethod(origin, String.class);
            method.invoke(this, "RECOVERYED!");
            setText("恢复:"+origin+" success!");
        }
        catch (Exception e) {
            Log.i(TAG, "-e:"+e);
            e.printStackTrace();
        }
    }

    public void setTextView(EditText tv) {
       mTextview = tv;
    }

    private void setText(String s) {
        stringBuilder.append(s+"\n");
        mTextview.setText(stringBuilder.toString());
        mTextview.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTextview.setSelection(mTextview.getText().length(), mTextview.getText().length());

    }

    public void test_public(String tag) {
        Log.i(TAG, "-test_public:"+tag);
        if (tag.equals("from hook")) {
            Toast.makeText(mContext, "test_public hook success", Toast.LENGTH_SHORT).show();
            setText("test_public hook 执行 success");
        }
    }
    public void public_hook(String tag) {
        Log.i(TAG, "-public_hook:"+tag);
        mIhookManager.callOriginMethod(this, "test_public", "from hook");
    }


    public void test_protect_ext(String tag) {
        test_protect(tag);
    }
    protected void test_protect(String tag) {
        Log.i(TAG, "-test_protect:"+tag);
        if (tag.equals("from hook")) {
            Toast.makeText(mContext, "test_protect hook success", Toast.LENGTH_SHORT).show();
            setText("test_protect hook 执行 success");
        }
    }
    protected void protect_hook(String tag) {
        Log.i(TAG, "-protect_hook:"+tag);
        mIhookManager.callOriginMethod(this, "test_protect", "from hook");
    }


    public void test_private_ext(String tag) {
        test_private(tag);
    }
    private void test_private(String tag) {
        Log.i(TAG, "-test_private:"+tag);
        if (tag.equals("from hook")) {
            Toast.makeText(mContext, "test_private hook success", Toast.LENGTH_SHORT).show();
            setText("test_private hook 执行 success");
        }
    }
    private void private_hook(String tag) {
        Log.i(TAG, "-private_hook:"+tag);
        mIhookManager.callOriginMethod(this, "test_private", "from hook");
    }

    public void test_static_ext(String tag) {
        int ret = test_static(tag);
        if (ret == 1) {
            Toast.makeText(mContext, "test_static hook success", Toast.LENGTH_SHORT).show();
            setText("test_static hook 执行 success");
        }
    }

    private static int test_static(String tag) {
        Log.i(TAG, "-test_static:"+tag);
        return 0;
    }
    private static int static_hook(String tag) {
        Log.i(TAG, "-static_hook:"+tag);
        mIhookManager.callOriginMethod(null, "test_static", "from hook");
        return 1;
    }
}
