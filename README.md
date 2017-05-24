

# 码天科技 5月23 日 创作 matian-sharesdk
share sdk for react-native
集成了 微信，朋友圈，以及新浪微博 3大最主流的 ios 以及android 端的 分享功能

## install
 在package.json 加入 "martian-shareSDK": "XuChangZJU/martian-shareSDK#dev",

`npm i martian-shareSDK`

## link

`react-native link`

### IOS


 1. 打开Xcode app项目文件夹, 找到路径为`node_modules/react-native-sharesdk/ios/rnsharesdk/ShareSDK`的目录
    并且拖动到`Libraries`目录,不要勾选`copy items if needed`。

 2. 在项目中找到`Build Settings`这一栏, 继续往下找到`Framework Search Paths`这一节，加入以下这些路径值：
    `$(SRCROOT)/../node_modules/react-native-sharesdk/ios/rnsharesdk/ShareSDK`,
    `$(SRCROOT)/../node_modules/react-native-sharesdk/ios/rnsharesdk/ShareSDK/Support/Optional`,
    `$(SRCROOT)/../node_modules/react-native-sharesdk/ios/rnsharesdk/ShareSDK/Support/PlatformConnector`,
    `$(SRCROOT)/../node_modules/react-native-sharesdk/ios/rnsharesdk/ShareSDK/Support/Required`,
    继续找到`Library Search Paths`这一节，加入以下这些路径值：
    `$(SRCROOT)/../node_modules/react-native-sharesdk/ios/rnsharesdk/ShareSDK/Support/PlatformSDK/WeChatSDK`,
    `$(SRCROOT)/../node_modules/react-native-sharesdk/ios/rnsharesdk/ShareSDK/Support/PlatformSDK/SinaWeiboSDK`,

 3.在项目中找到`Build Phases`这一栏, 继续往下找到`Link Binary With Libraries`这一节，加入以下这些库：
   `码天已帮您link 所有库 这里可以跳过`
   具体依赖如下图
   ![tbdimg](https://github.com/lihaodeveloper/React-Native-ShareSdk/blob/master/asset/tbdimg.png)

 4.在项目中找到`Info`这一栏, 往下找到`App Transport Security Settings`这一栏，添加新的一行`Allow Arbitrary Loads`，设置为`YES`
   ![arbitrary](https://github.com/lihaodeveloper/React-Native-ShareSdk/blob/master/asset/arbitrary.png)
   继续往下找到`URL Types`这一节，点击`+`号添加一栏数据，填入weibo appid,微信app key
   例如:`weibo100371282`, 100371282 是weibo appid.
   ![urltype](https://github.com/lihaodeveloper/React-Native-ShareSdk/blob/master/asset/urltype.png)
   在`URL Types`中添加WEIBO的AppID，其格式为：”weibo” ＋ AppId的16进制（如果appId转换的16进制数不够8位则在前面补0，如转换的是：5FB8B52，
   则最终填入为：QQ05FB8B52 注意：转换后的字母要大写） 转换16进制的方法：echo ‘ibase=10;obase=16;801312852′|bc，其中801312852为QQ的AppID
   码天 温馨提示 这里一定要去参考
   ![urltype16](http://wiki.mob.com/wp-content/uploads/2015/09/9406F13D-F78B-4261-A52B-CFBC7ECF4890.png)

   ### 具体细节可参考：[ios简洁版快速集成](http://wiki.mob.com/ios简洁版快速集成/) 


 5.在项目中找到`Info`这一栏, 添加 `LSApplicationQueriesSchemes` 设置类型 `Array`.

   ![aqs](https://github.com/lihaodeveloper/React-Native-ShareSdk/blob/master/asset/aqs.png)

   ![lsqschemesimg](https://github.com/lihaodeveloper/React-Native-ShareSdk/blob/master/asset/lsqschemes.png)
   
   ### 具体细节可参考： [ios9-对sharesdk的影响](http://wiki.mob.com/ios9-对sharesdk的影响（适配ios-9必读）/) 
   
 6.添加你的 sharesdk appkey, qq appid 和 appkey 在 `node_modules/react-native-sharesdk/ios/rnsharesdk/MobLogin.m`
   ```objectiv-c
      [ShareSDK registerApp:@"iosv1101"
   ```

   ```objectiv-c
RCT_EXPORT_METHOD(showShare:(NSString *)title :(NSString *)content :(NSString *)url :(NSString *)imgUrl) {
    dispatch_async(dispatch_get_main_queue(), ^{
        
        NSString* string;
        NSString* string1 = content;
        NSString* string2 = url;
        string = [string1 stringByAppendingString:string2];
        NSMutableDictionary *shareParams = [NSMutableDictionary dictionary];
        //NSArray* imageArray = @[imgUrl];
        [shareParams SSDKSetupShareParamsByText:string
                                         images:[NSURL URLWithString:imgUrl]
                                            url:[NSURL URLWithString:url]
                                          title:title
                                           type:SSDKContentTypeAuto];        //优先使用平台客户端分享
        [shareParams SSDKEnableUseClientShare];
        //设置微博使用高级接口
        //2017年6月30日后需申请高级权限
        [shareParams SSDKEnableAdvancedInterfaceShare];
        //设置显示平台 只能分享视频的YouTube MeiPai 不显示
        NSArray *items = @[
                           @(SSDKPlatformTypeWechat),
                           @(SSDKPlatformTypeSinaWeibo)
                           ];
        
        SSUIShareActionSheetController *sheet = [ShareSDK showShareActionSheet:nil
                                                                         items:items
                                                                   shareParams:shareParams
                                                           onShareStateChanged:^(SSDKResponseState state, SSDKPlatformType platformType, NSDictionary *userData, SSDKContentEntity *contentEntity, NSError *error, BOOL end) {
                                                               
                                                               switch (state) {
                                                                       
                                                                   case SSDKResponseStateBegin:
                                                                   {
                                                                       //设置UI等操作
                                                                       break;
                                                                   }
                                                                   case SSDKResponseStateSuccess:
                                                                   {
                                                                       //Instagram、Line等平台捕获不到分享成功或失败的状态，最合适的方式就是对这些平台区别对待
                                                                       if (platformType == SSDKPlatformTypeInstagram)
                                                                       {
                                                                           break;
                                                                       }
                                                                       
                                                                       UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"分享成功"
                                                                                                                           message:nil
                                                                                                                          delegate:nil
                                                                                                                 cancelButtonTitle:@"确定"
                                                                                                                 otherButtonTitles:nil];
                                                                       [alertView show];
                                                                       break;
                                                                   }
                                                                   case SSDKResponseStateFail:
                                                                   {
                                                                       UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"分享失败"
                                                                                                                       message:[NSString stringWithFormat:@"%@",error]
                                                                                                                      delegate:nil
                                                                                                             cancelButtonTitle:@"OK"
                                                                                                             otherButtonTitles:nil, nil];
                                                                       [alert show];
                                                                       break;
                                                                   }
                                                                   case SSDKResponseStateCancel:
                                                                   {
                                                                       UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"分享已取消"
                                                                                                                           message:nil
                                                                                                                          delegate:nil
                                                                                                                 cancelButtonTitle:@"确定"
                                                                                                                 otherButtonTitles:nil];
                                                                       [alertView show];
                                                                       break;
                                                                   }
                                                                   default:
                                                                       break;
                                                               }
                                                           }];
        [sheet.directSharePlatforms addObject:@(SSDKPlatformTypeSinaWeibo)];
    });
}
   ```


### ANDROID

 **qq 微信登录 分享**

1. 添加你的 qq AppId 在 `node_modules/react-native-sharesdk/android/build.gradle`

  ```xml
  defaultConfig {
        ...
        manifestPlaceholders = [
                QQ_APP_ID: "100371282", //qq AppId
        ]
    }
  ```

2. 添加你的 qq sharesdk appkey 在 `node_modules/react-native-sharesdk/android/src/main/assets/ShareSDK.xml`

  ```xml
  <ShareSDK
        AppKey = "androidv1101"/> <!-- 修改成你在sharesdk后台注册的应用的appkey"-->
  ```

3. 添加你的 qq appid 和 appkey 在 `node_modules/react-native-sharesdk/android/src/main/assets/ShareSDK.xml`

  ```xml
  <!-- ShareByAppClient标识是否使用微博客户端分享，默认是false -->
	<QQ
        Id="7"
        SortId="7"
        AppId="100371282"
        AppKey="aed9b0303e3ed1e27bae87c33761161d"
        ShareByAppClient="true"
        Enable="true" />
  ```

## usage

```
...
import { NativeModules } from 'react-native'
const {MobLogin} = NativeModules

...

  _onPressShare() {
    MobLogin.showShare('我是标题', '分享什么内容')
  }

render() {
    return (
      <View style={styles.container}>
        <TouchableOpacity style={styles.qqlogin} onPress={()=>this._onPressLogin()}>
          <Text style={{fontSize: 18, color: 'black'}}>QQLogin</Text>
        </TouchableOpacity>
        ....
      </View>
    )
}
