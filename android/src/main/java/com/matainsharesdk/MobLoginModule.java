package com.matainsharesdk;

import android.content.Context;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
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

public class MobLoginModule extends ReactContextBaseJavaModule implements PlatformActionListener {
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
    public void showShare(String title, String text, String url, String imageUrl) {

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
        oks.setLatitude(23.169f);
        oks.setLongitude(112.908f);
        // 启动分享GUI
        oks.show(mContext);
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        if (action == Platform.ACTION_USER_INFOR) {
            PlatformDb platDB = platform.getDb();
            WritableMap map = Arguments.createMap();
            map.putString("token", platDB.getToken());
            map.putString("user_id", platDB.getUserId());
            map.putString("user_name", platDB.getUserName());
            map.putString("user_gender", platDB.getUserGender());
            map.putString("user_icon", platDB.getUserIcon());
            mPromise.resolve(map);
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        mPromise.reject("LoginError", throwable.getMessage());
    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
