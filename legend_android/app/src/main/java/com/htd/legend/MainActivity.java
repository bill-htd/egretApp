package com.htd.legend;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import org.egret.runtime.launcherInterface.INativePlayer;
import org.egret.egretnativeandroid.EgretNativeAndroid;
import org.json.JSONException;
import org.json.JSONObject;

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
        setExternalInterfaces();
        if (!nativeAndroid.initialize("http://damete.com/App/index.html")) {
            Toast.makeText(this, "Initialize native failed.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        setContentView(nativeAndroid.getRootFrameLayout());

        rootLayout = nativeAndroid.getRootFrameLayout();
        showLoadingView();
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
        super.onPause();
        nativeAndroid.pause();
    }

    @Override
    protected void onResume() {
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
