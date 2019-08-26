package me.hookmethod;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

public class HookTestDemo {
    static final String TAG = "Hook_TestDemo";
    private Context mContext;
    private TextView mTextview;
    private StringBuilder stringBuilder = new StringBuilder();

    public void hook(Context context) {
        mContext = context;
        Method origin;
        Method replace;

        try {
            // public
            origin = this.getClass().getDeclaredMethod("test_public", String.class);
            replace = this.getClass().getDeclaredMethod("public_hook", String.class);
            Hook.hook(origin, replace);

            // protect
            origin = this.getClass().getDeclaredMethod("test_protect", String.class);
            replace = this.getClass().getDeclaredMethod("protect_hook", String.class);
            Hook.hook(origin, replace);

            // private
            origin = this.getClass().getDeclaredMethod("test_private", String.class);
            replace = this.getClass().getDeclaredMethod("private_hook", String.class);
            Hook.hook(origin, replace);

            // static
            origin = this.getClass().getDeclaredMethod("test_static", String.class);
            replace = this.getClass().getDeclaredMethod("static_hook", String.class);
            Hook.hook(origin, replace);

            setText("hook 完成");
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "-e:"+e);
        }
    }

    public void setTextView(TextView tv) {
       mTextview = tv;
    }

    private void setText(String s) {
        stringBuilder.append(s+"\n");
        mTextview.setText(stringBuilder.toString());
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
        Hook.callOrigin(this, "test_public", "from hook");
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
        Hook.callOrigin(this, "test_protect", "from hook");
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
        Hook.callOrigin(this, "test_private", "from hook");
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
        Hook.callOrigin(null, "test_static", "from hook");
        return 1;
    }
}
