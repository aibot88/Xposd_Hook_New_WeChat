package com.example.root.xposd_hook_new_wechat;


import static com.example.root.xposd_hook_new_wechat.ReflectionUtil.log;

import android.app.Activity;
import android.widget.Button;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class AutoLogin{
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        log("hook autoLogin 开始啦, android.app.Activity.class: "+android.app.Activity.class);
        XposedHelpers.findAndHookMethod(android.app.Activity.class, "onStart", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                try {
                    if (!PreferencesUtils.isAutoLogin())
                        return;
                    if (!(param.thisObject instanceof Activity)) {
                        return;
                    }
                    log("onStart方法");
                    Activity activity = (Activity) param.thisObject;
                    log("onStart方法中的 activity"+activity);
                    if (activity.getClass().getName().equals(HookParams.getInstance().WebWXLoginUIClassName)) {
                        Class clazz = activity.getClass();
                        log("onStart方法中的 clazz "+ clazz);
                        Field field = XposedHelpers.findFirstFieldByExactType(clazz, Button.class);
                        log("onStart方法中的 field "+ field);
                        Button button = (Button) field.get(activity);
                        log("onStart方法中的 button "+ button);
                        if (button != null) {
                            log("onStart方法中的 button.performClick() "+ button.performClick());
                        }
                    }

                } catch (Error | Exception e) {
                    log(""+e);
                }
            }
        });

    }

}
