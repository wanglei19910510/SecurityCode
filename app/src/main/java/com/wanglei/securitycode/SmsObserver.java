package com.wanglei.securitycode;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wanglei on 16/6/14.
 * email wanglei19910510@gmail.com
 * The greatest test of courage on earth is to bear defeat without losing heart.
 */
public class SmsObserver extends ContentObserver {
    private static final String TAG = "SmsObserver";
    private Handler mHandler;
    private Context mContext;

    public SmsObserver(Context context, Handler handler) {
        super(handler);
        mHandler = handler;
        mContext = context;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        Log.i(TAG, uri.toString());
        //onchange方法会执行两次  当第二次执行时再取出短信内容
        if (uri.toString().equals("content://sms/raw")) {
            return;
        }
            //当短信内容存储到数据库时再取出短信内容
        Uri inboxUri = Uri.parse("content://sms/inbox");
        //按日期倒序排列
        Cursor cursor = mContext.getContentResolver().query(inboxUri, null, null, null, "date desc");
        if (cursor != null) {
            if (cursor.moveToFirst()) {

                String address = cursor.getString(cursor.getColumnIndex("address"));
                String body = cursor.getString(cursor.getColumnIndex("body"));
                Log.i(TAG, "发件人:" + address + ",内容:" + body);
               //正则表达式  如果是连续6个数字
                Pattern pattern = Pattern.compile("(\\d{6})");
                Matcher matcher = pattern.matcher(body);
                if (matcher.find()){
                    //去除验证码
                    String group = matcher.group(0);
                    Log.i(TAG,group);
                    //发送消息给指定对象
                    mHandler.obtainMessage(MainActivity.CODE,group).sendToTarget();

                }
            }
        }
    }
}
