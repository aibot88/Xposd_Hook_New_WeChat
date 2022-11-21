package com.example.root.xposd_hook_new_wechat;

import static de.robv.android.xposed.XposedBridge.log;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import de.robv.android.xposed.IXposedHookLoadPackage;

import de.robv.android.xposed.XC_MethodHook;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;

import de.robv.android.xposed.XposedHelpers;

import de.robv.android.xposed.callbacks.XC_LoadPackage;



public class HookTest implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
//        log("hookTest 加载中...");
        if (lpparam.packageName.equals(HookParams.WECHAT_PACKAGE_NAME)) {
            log("进入微信hook程序");
            try {
                XposedHelpers.findAndHookMethod(ContextWrapper.class, "attachBaseContext", Context.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Context context = (Context) param.args[0];
                        String processName = lpparam.processName;
                        //Only hook important process
                        if (!processName.equals(HookParams.WECHAT_PACKAGE_NAME) &&
                                !processName.equals(HookParams.WECHAT_PACKAGE_NAME + ":tools")
                        ) {
                            return;
                        }
                        String versionName = getVersionName(context, HookParams.WECHAT_PACKAGE_NAME);
                        log("Found wechat version:" + versionName);
                        if (!HookParams.hasInstance()) {
                            SearchClasses.init(context, lpparam, versionName);
                            log("进入loadPlugins, 终于要开始执行autoLogin了.");
                            loadPlugins(lpparam);
                        }
//                        SearchClasses.init(context, lpparam, versionName);
//                        log("进入loadPlugins, 终于要开始执行autoLogin了.");
//                        loadPlugins(lpparam);
                    }
                });
            } catch (Error | Exception e) {
            }
        }
    }

    private String getVersionName(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(packageName, 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return "";
    }


    private void loadPlugins(XC_LoadPackage.LoadPackageParam lpparam) {
        AutoLogin autoLoginInstance = new AutoLogin();
        autoLoginInstance.hook(lpparam);
    }

}
