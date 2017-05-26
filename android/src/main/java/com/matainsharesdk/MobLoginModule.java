package com.matainsharesdk;

import android.content.Context;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import android.widget.Toast;
import com.facebook.react.bridge.WritableMap;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * Created by cc on 2017/1/29.
 */
class Foo implements PlatformActionListener {

    void foo() {
    }
     @Override
     public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap ) {
                       WritableMap params = Arguments.createMap();
                        params.putInt("type", ShareSDK.platformNameToId(platform.getName()));
                        params.putString("platformName", platform.getName());
                        params.putInt("action", action);
                        params.putString("MSG","成功");
                        foo();
                        promise.resol
     }

                @Override
                public void onError(Platform platform, int action, Throwable throwable) {
                }

                @Override
                public void onCancel(Platform platform, int i) {
                }
}

public class MobLoginModule extends ReactContextBaseJavaModule {
    private Context mContext;
    private Promise mPromise;

    public MobLoginModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.mContext = reactContext.getApplicationContext();
    }

    @Override
    public String getName() {
        return "MobLogin";
    }

    @Override
    public void initialize() {
        super.initialize();
        //初始化Mob
        ShareSDK.initSDK(mContext);
    }


    @ReactMethod
    public void showShare(String title, String text, String url, String imageUrl, final Promise promise) {
        OnekeyShare oks = new OnekeyShare();
        oks.setSilent(true);
    oks.setTheme(OnekeyShareTheme.CLASSIC);
        // 令编辑页面显示为Dialog模式
        oks.setDialogMode();
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();
        oks.setTitle(title);
        oks.setTitleUrl(url);
        oks.setText(text);
        //oks.setImagePath("/sdcard/test-pic.jpg");  //分享sdcard目录下的图片
        oks.setImageUrl(imageUrl);
        oks.setUrl(url); //微信不绕过审核分享链接
        oks.setSite("ShareSDK");  //QZone分享完之后返回应用时提示框上显示的名称
        oks.setVenueName("ShareSDK");
        oks.setVenueDescription("This is a beautiful place!");
           // 启动分享GUI
           oks.setCallback(new PlatformActionListener ()
           {
             @Override
               public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap ) {
                       Toast.makeText(mContext, platform.getName(), Toast.LENGTH_SHORT).show();
                      WritableMap params = Arguments.createMap();
                       params.putInt("type", ShareSDK.platformNameToId(platform.getName()));
                       params.putString("platformName", platform.getName());
                       params.putInt("action", action);
                       params.putString("MSG","成功");
                       promise.resolve(params);
               }

               @Override
               public void onError(Platform platform, int action, Throwable throwable) {
   Toast.makeText(mContext, "失败", Toast.LENGTH_SHORT).show();
               }

               @Override
               public void onCancel(Platform platform, int i) {
   Toast.makeText(mContext, "取消", Toast.LENGTH_SHORT).show();
               }
           }
           );
           oks.show(mContext);
       }
