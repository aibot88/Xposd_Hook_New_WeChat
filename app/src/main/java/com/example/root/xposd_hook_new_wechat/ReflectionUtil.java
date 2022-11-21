package com.example.root.xposd_hook_new_wechat;
import android.util.Log;
import de.robv.android.xposed.XposedBridge;

public final class ReflectionUtil {
    private static boolean xposedExist;
    static {
        try {
            Class.forName("de.robv.android.xposed.XposedBridge");
            xposedExist = true;
        } catch (ClassNotFoundException e) {
            xposedExist = false;
        }
    }
    public static void log(String msg) {
        if (msg == null) {
            return;
        }
        if (xposedExist) {
            XposedBridge.log(msg);
        } else {
            Log.i("Xposed", "[WechatEnhancement] " + msg);
        }
    }
}
