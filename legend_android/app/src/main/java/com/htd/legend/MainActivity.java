package com.htd.legend;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import org.egret.runtime.launcherInterface.INativePlayer;
import org.egret.egretnativeandroid.EgretNativeAndroid;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jiguang.analytics.android.api.LoginEvent;
import cn.jiguang.analytics.android.api.RegisterEvent;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

//Android项目发布设置详见doc目录下的README_ANDROID.md

public class MainActivity extends Activity {
    public final static String appError = "error";
    // 加载首页失败
    public final static String errorIndexLoadFailed = "load";
    // 启动引擎失败 
    public final static String errorJSLoadFailed = "start";
    // 引擎停止运行
    public final static String errorJSCorrupted = "stopRunning";
    public final static String appState = "state";
    // 正在启动引擎
    public final static String stateEngineStarted = "starting";
    // 引擎正在运行
    public final static String stateEngineRunning = "running";
    
    private final String TAG = "MainActivity";
    private EgretNativeAndroid nativeAndroid;

    private ImageView launchScreenImageView = null;
    private FrameLayout rootLayout = null;
    public Activity instance = this;
    public static boolean isForeground = false;

    public static final String MESSAGE_RECEIVED_ACTION = "com.htd.legend.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nativeAndroid = new EgretNativeAndroid(this);
        if (!nativeAndroid.checkGlEsVersion()) {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        nativeAndroid.config.showFPS = false;
        nativeAndroid.config.fpsLogTime = 30;
        nativeAndroid.config.disableNativeRender = false;
        nativeAndroid.config.clearCache = false;
        nativeAndroid.config.loadingTimeout = 0;




//        http://192.168.50.190/dashboard/

//        http://damete.com/App/index.html
//        http://qsdate.com/App/index.html
//        http://192.168.50.190/dashboard/app//index.html



        setExternalInterfaces();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

//       http://cq.jiyuzulin.com/index.html
//        http://cq.wisship.com/index.html
//        http://cq.techan0812.com/index.html
//        http://cq.fjxmigc.com/index.html


//        if(checkUrl("http://192.168.50.190/dashboard/app/index.html")){
//            Log.d(TAG, "地址：http://cq.jiyuzulin.com/index.html");
//            if (!nativeAndroid.initialize("http://192.168.50.190/dashboard/app/index.html")) {
//                Toast.makeText(this, "Initialize native failed.",
//                        Toast.LENGTH_LONG).show();
//                return;
//            }
//        }else

        if(checkUrl("http://cq.scmyxs.com/index.html")){
            Log.d(TAG, "地址：http://cq.scmyxs.com/index.html");
            if (!nativeAndroid.initialize("http://cq.scmyxs.com/index.html")) {
                Toast.makeText(this, "Initialize native failed.",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }else
        if(checkUrl("http://cq.jiyuzulin.com/index.html")){
            Log.d(TAG, "地址：http://cq.jiyuzulin.com/index.html");
            if (!nativeAndroid.initialize("http://cq.jiyuzulin.com/index.html")) {
                Toast.makeText(this, "Initialize native failed.",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }else if(checkUrl("http://cq.wisship.com/index.html")){
            Log.d(TAG, "地址：http://cq.wisship.com/index.html ");
            if (!nativeAndroid.initialize("http://cq.wisship.com/index.html")) {
                Toast.makeText(this, "Initialize native failed.",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }else if(checkUrl("http://cq.techan0812.com/index.html")){
            Log.d(TAG, "地址：http://cq.techan0812.com/index.html");
            if (!nativeAndroid.initialize("http://cq.techan0812.com/index.html")) {
                Toast.makeText(this, "Initialize native failed.",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }else if(checkUrl("http://cq.fjxmigc.com/index.html")){
            Log.d(TAG, "地址：http://cq.fjxmigc.com/index.html");
            if (!nativeAndroid.initialize("http://cq.fjxmigc.com/index.html")) {
                Toast.makeText(this, "Initialize native failed.",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }else{
            Toast.makeText(this, "网络请求失败 请稍后再试",
                    Toast.LENGTH_LONG).show();
        }



        setContentView(nativeAndroid.getRootFrameLayout());

        rootLayout = nativeAndroid.getRootFrameLayout();
        showLoadingView();

//        JAnalyticsInterface.setDebugMode(true);
        // 统计初始化
        JAnalyticsInterface.init(this);
        JAnalyticsInterface.initCrashHandler(this);
        //  推送初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        jpushT_init();

    }
        public boolean  checkUrl(String urlStr) {

            URL url;
            try {
                url = new URL(urlStr);
                InputStream in = url.openStream();
                System.out.println("连接可用");
                return true;
            } catch (Exception e1) {
                System.out.println(e1);
                System.out.println("连接打不开!");
                return false;
            }



        }

    private void jpushT_init() {
        // 设置提示时间段
        Set<Integer> days = new HashSet<Integer>();
        days.add(0);
        days.add(1);
        days.add(2);
        days.add(3);
        days.add(4);
        days.add(5);
        days.add(6);
        JPushInterface.setPushTime(getApplicationContext(), days, 0, 23);


        // 设置通知样式
        BasicPushNotificationBuilder _builder = new BasicPushNotificationBuilder(this);
        _builder.statusBarDrawable = R.drawable.ic_launcher;
        _builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        _builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setPushNotificationBuilder(1, _builder);
//        Toast.makeText(this, "有新的消息", Toast.LENGTH_SHORT).show();

//        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(this, R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
//        builder.layoutIconDrawable = R.drawable.ic_launcher;
//        builder.developerArg0 = "developerArg2";
//        JPushInterface.setPushNotificationBuilder(2, builder);
//        Toast.makeText(this, "Custom Builder - 2", Toast.LENGTH_SHORT).show();
    }


    private void showLoadingView() {

//        Resources resources = getContext().getResources();
//        Drawable btnDrawable = resources.getDrawable(R.drawable.layout_bg);
//        layout.setBackgroundDrawable(btnDrawable);

        launchScreenImageView = new ImageView(this);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.bg);
        launchScreenImageView.setImageDrawable(drawable);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        rootLayout.addView(launchScreenImageView, params);
    }
    private void hideLoadingView() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rootLayout.removeView(launchScreenImageView);
                Drawable drawable = launchScreenImageView.getDrawable();
                launchScreenImageView.setImageDrawable(null);
                drawable.setCallback(null);
                launchScreenImageView = null;
            }
        });
    }
    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
        nativeAndroid.pause();
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
        nativeAndroid.resume();
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            nativeAndroid.exitGame();
        }

        return super.onKeyDown(keyCode, keyEvent);
    }

    private void setExternalInterfaces() {
        nativeAndroid.setExternalInterface("sendToNative", new INativePlayer.INativeInterface() {
            @Override
            public void callback(String message) {
                String str = "Native get message: ";
                str += message;
                Log.d(TAG, str);
                nativeAndroid.callExternalInterface("sendToJS", str);
            }
        });

        // 登录统计
        nativeAndroid.setExternalInterface("loginStatistics", new INativePlayer.INativeInterface() {
            @Override
            public void callback(String message) {
                Log.d(TAG,"获取的渠道名 ："+ message);
                LoginEvent lEvent = new LoginEvent(message,true);
//                lEvent.addKeyValue("name","赵子龙").addKeyValue("id","10086");
                JAnalyticsInterface.onEvent(instance, lEvent);

            }
        });
        // 注册统计
        nativeAndroid.setExternalInterface("registerStatistics", new INativePlayer.INativeInterface() {
            @Override
            public void callback(String message) {
                Log.d(TAG,"获取的渠道名 ："+ message);
                RegisterEvent rEvent = new RegisterEvent(message,true);
//                rEvent.addKeyValue("name","赵子龙").addKeyValue("id","10086");
                JAnalyticsInterface.onEvent(instance, rEvent);

//                nativeAndroid.callExternalInterface("sendToJS", str);
            }
        });

        nativeAndroid.setExternalInterface("getChannel", new INativePlayer.INativeInterface() {
            @Override

            public void callback(String message) {
                try {
                    PackageManager pm = instance.getPackageManager();

                    ApplicationInfo appInfo = pm.getApplicationInfo(instance.getPackageName(), PackageManager.GET_META_DATA);
                    nativeAndroid.callExternalInterface("backChannel", appInfo.metaData.getString("channel"));
                    Log.d(TAG, "获取的渠道 ： ");
                    Log.d(TAG, appInfo.metaData.getString("channel"));



                    return;

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                nativeAndroid.callExternalInterface("backChannel", null);
            }
        });



        nativeAndroid.setExternalInterface("openURL", new INativePlayer.INativeInterface() {
            @Override
            public void callback(String url) {
                Intent intent = new Intent();
                intent.setData(Uri.parse(url));//Url 就是你要打开的网址
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent); //启动浏览器
            }
        });

            // handle the error Event during the running
            nativeAndroid.setExternalInterface("@onError", new INativePlayer.INativeInterface(){
                @Override
                public void callback(String message) {
                    String str = "Native get onError message: ";
            
                    try{
                        JSONObject jsonObject = new JSONObject(message);
                        String error = jsonObject.getString(appError);
                        handleErrorEvent(error);
                    }
                    catch (JSONException e) {
                        Log.e(TAG, "onError message failed to analyze");
                        return;
                    }
            
                    str += message;
                    Log.e(TAG, str);
                }
            });


        nativeAndroid.setExternalInterface("@onState", new INativePlayer.INativeInterface() {
            @Override
            public void callback(String message) {
                String str = "Native get onState message: ";
        
                try{
                    JSONObject jsonObject = new JSONObject(message);
                    String state = jsonObject.getString(appState);
                    handleStateEvent(state);
                }
                catch (JSONException e) {
                    Log.e(TAG, " onState message failed to analyze");
                }
        
                str += message;
                Log.e(TAG, str);
            }
        });
    }
    
    private void handleErrorEvent(String error) {

        switch (error) {
            case MainActivity.errorIndexLoadFailed:
                Log.e(TAG, "errorIndexLoadFailed");
//                Log.d(TAG, "没有地址啊 啊啊啊 啊啊 ： ");
//                nativeAndroid.initialize("http://47.112.41.33/App/index.html");

                break;
            case MainActivity.errorJSLoadFailed:
                Log.e(TAG, "errorJSLoadFailed");
                break;
            case MainActivity.errorJSCorrupted:
                Log.e(TAG, "errorJSCorrupted");
                break;
            default:
                break;
        }

    }
        private void handleStateEvent(String state) {
            switch (state) {
                case MainActivity.stateEngineStarted:
                    Log.e(TAG, "stateEngineStarted");
                    break;
                case MainActivity.stateEngineRunning:
                    Log.e(TAG, "stateEngineRunning");
                    hideLoadingView();
                    break;
                default:
                    break;
            }
        }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
