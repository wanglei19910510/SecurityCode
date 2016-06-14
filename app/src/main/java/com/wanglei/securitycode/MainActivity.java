package com.wanglei.securitycode;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final int CODE = 1;
    private EditText et;

    private SmsObserver smsObserver;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE:
                    String code = (String) msg.obj;

                    et.setText(code);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.et);
        smsObserver = new SmsObserver(this, mHandler);
        Uri uri = Uri.parse("content://sms");
        //注册内容观察者
        getContentResolver().registerContentObserver(uri, true, smsObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //解除内容观察者
        getContentResolver().unregisterContentObserver(smsObserver);
    }
}
