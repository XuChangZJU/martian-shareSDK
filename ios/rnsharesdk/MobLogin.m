//
//  MobLogin.m
//  rnsharesdk
//
//  Created by cc on 2017/2/1.
//  Copyright © 2017年 qq. All rights reserved.
//
#import "MobLogin.h"

@implementation MobLogin


RCT_EXPORT_MODULE();
- (instancetype)init
{
    if(self = [super init]){
        NSLog(@"initShareSdk()!");
        /**
         *  设置ShareSDK的appKey，如果尚未在ShareSDK官网注册过App，请移步到http://mob.com/login 登录后台进行应用注册，
         *  在将生成的AppKey传入到此方法中。我们Demo提供的appKey为内部测试使用，可能会修改配置信息，请不要使用。
         *  方法中的第二个参数用于指定要使用哪些社交平台，以数组形式传入。第三个参数为需要连接社交平台SDK时触发，
         *  在此事件中写入连接代码。第四个参数则为配置本地社交平台时触发，根据返回的平台类型来配置平台信息。
         *  如果您使用的时服务端托管平台信息时，第二、四项参数可以传入nil，第三项参数则根据服务端托管平台来决定要连接的社交SDK。
         */
        
    }
    return self;
}


RCT_EXPORT_METHOD(showShare:(NSString *)title :(NSString *)content :(NSString *)url :(NSString *)imgUrl) {
    dispatch_async(dispatch_get_main_queue(), ^{
        
        NSString* string;
        NSString* string1 = content;
        NSString* string2 = url;
        NSArray* imageArray = @[imgUrl];
        NSLog(@"%@",imageArray);
        string = [string1 stringByAppendingString:string2];
        NSMutableDictionary *shareParams = [NSMutableDictionary dictionary];
        //NSArray* imageArray = @[imgUrl];
        [shareParams SSDKSetupShareParamsByText:string
                                         images:[NSURL URLWithString:imgUrl]
                                            url:[NSURL URLWithString:url]
                                          title:title
                                           type:SSDKContentTypeAuto];
        //优先使用平台客户端分享
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

@end
