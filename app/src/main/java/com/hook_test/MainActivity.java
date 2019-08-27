package com.hook_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static final String TAG = "Hook_test-MainActivity";
    Button do_hook, do_unhook, origin_method, hook_public_method;
    Button hook_private_method, hook_protected_method, hook_static_method;
    EditText sample_text;
    Context mContext;
    HookTestDemo mTestDemo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        do_hook = (Button)findViewById(R.id.do_hook);
        do_unhook = (Button)findViewById(R.id.do_unhook);
        origin_method = (Button)findViewById(R.id.origin_method);
        origin_method.setVisibility(View.GONE);

        hook_public_method = (Button)findViewById(R.id.hook_public_method);
        hook_static_method = (Button)findViewById(R.id.hook_static_method);
        hook_private_method = (Button)findViewById(R.id.hook_private_method);
        hook_protected_method = (Button)findViewById(R.id.hook_protected_method);

        sample_text = (EditText)findViewById(R.id.sample_text);

        do_hook.setOnClickListener(this);
        do_unhook.setOnClickListener(this);
        origin_method.setOnClickListener(this);
        hook_public_method.setOnClickListener(this);
        hook_private_method.setOnClickListener(this);
        hook_protected_method.setOnClickListener(this);
        hook_static_method.setOnClickListener(this);

        mTestDemo = new HookTestDemo();
        mTestDemo.setTextView(sample_text);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.do_unhook:
                mTestDemo.recovery();
                break;
            case R.id.do_hook:
                Log.i(TAG, "执行hook.");
                try {
                    mTestDemo.hook(MainActivity.this);
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
                mTestDemo.test_public("public");
                break;
            case R.id.hook_protected_method:
                mTestDemo.test_protect_ext("protect");
                break;
            case R.id.hook_private_method:
                mTestDemo.test_private_ext("private");
                break;
            case R.id.hook_static_method:
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
