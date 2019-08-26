package com.hook_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import me.hookmethod.HookTestDemo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static final String TAG = "Hook_test-MainActivity";
    Button do_hook, origin_method, hook_public_method;
    Button hook_private_method, hook_protected_method, hook_static_method;
    TextView sample_text;
    Context mContext;
    HookTestDemo mTestDemo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        do_hook = (Button)findViewById(R.id.do_hook);
        origin_method = (Button)findViewById(R.id.origin_method);
        origin_method.setVisibility(View.GONE);

        hook_public_method = (Button)findViewById(R.id.hook_public_method);
        hook_static_method = (Button)findViewById(R.id.hook_static_method);
        hook_private_method = (Button)findViewById(R.id.hook_private_method);
        hook_protected_method = (Button)findViewById(R.id.hook_protected_method);

        sample_text = (TextView)findViewById(R.id.sample_text);

        do_hook.setOnClickListener(this);
        origin_method.setOnClickListener(this);
        hook_public_method.setOnClickListener(this);
        hook_private_method.setOnClickListener(this);
        hook_protected_method.setOnClickListener(this);
        hook_static_method.setOnClickListener(this);

        mTestDemo = new HookTestDemo();
        mTestDemo.setTextView(sample_text);
    }

    public TextView getTextView() {
        return sample_text;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.do_hook:
                Log.i(TAG, "执行hook.");
                try {
                    //Log.i(TAG, "CLICK HOOK TEST");
                    mTestDemo.hook(MainActivity.this);
                    //Toast.makeText(mContext, "hook ok!", Toast.LENGTH_SHORT).show();
                    hook_public_method.performClick();
                    hook_private_method.performClick();
                    hook_protected_method.performClick();
                    hook_static_method.performClick();

                } catch (Throwable e) {
                    Log.i(TAG, "E:"+e);
                    e.printStackTrace();
                }
                break;
            case R.id.origin_method:
                break;
            case R.id.hook_public_method:
                //Log.i(TAG, "hook_public_method:");
                //Toast.makeText(mContext, "public", Toast.LENGTH_SHORT).show();
                mTestDemo.test_public("public");
                break;
            case R.id.hook_protected_method:
               // Log.i(TAG, "hook_protected_method:");
               // Toast.makeText(mContext, "protect", Toast.LENGTH_SHORT).show();
                mTestDemo.test_protect_ext("protect");
                break;
            case R.id.hook_private_method:
               // Log.i(TAG, "hook_private_method:");
                //Toast.makeText(mContext, "private", Toast.LENGTH_SHORT).show();
                mTestDemo.test_private_ext("private");
                break;
            case R.id.hook_static_method:
               // Log.i(TAG, "hook_static_method:");
               // Toast.makeText(mContext, "static", Toast.LENGTH_SHORT).show();
                mTestDemo.test_static_ext("static");
                break;
        }
    }

    public  void helloWorld(String tag) {
        Log.i(TAG, "helloWorld:"+tag);
        Toast.makeText(mContext, "helloWorld:"+tag, Toast.LENGTH_SHORT).show();
    }

    public void hello_hook(String tag) {
        Log.i(TAG, "hello_hook:"+tag);
        Toast.makeText(mContext, "hello_hook:"+tag, Toast.LENGTH_SHORT).show();
    }
}
