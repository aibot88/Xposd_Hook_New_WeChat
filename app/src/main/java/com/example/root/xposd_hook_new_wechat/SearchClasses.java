package com.example.root.xposd_hook_new_wechat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.gson.Gson;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import static de.robv.android.xposed.XposedBridge.log;

public class SearchClasses {
    private static XSharedPreferences preferencesInstance = null;

    public static void init(Context context, XC_LoadPackage.LoadPackageParam lparam, String versionName) {

        if (loadConfig(lparam, versionName))
            return;

        log("failed to load config, start finding...");

        generateConfig(lparam.appInfo.sourceDir, lparam.classLoader, versionName);

        saveConfig(context);
    }

    public static void generateConfig(String wechatApk, ClassLoader classLoader, String versionName) {

        HookParams hp = HookParams.getInstance();
        hp.versionName = versionName;
        hp.versionCode = HookParams.VERSION_CODE;
        int versionNum = getVersionNum(versionName);
        if (versionNum >= getVersionNum("6.5.6") && versionNum <= getVersionNum("6.5.23"))
            hp.LuckyMoneyReceiveUIClassName = "com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f";
        if (versionNum < getVersionNum("6.5.8"))
            hp.SQLiteDatabaseClassName = "com.tencent.mmdb.database.SQLiteDatabase";
        if (versionNum < getVersionNum("6.5.4"))
            hp.hasTimingIdentifier = false;
        if (versionNum >= getVersionNum("7.0.0"))
            hp.LuckyMoneyReceiveUIClassName = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
        if (versionNum >= getVersionNum("7.0.0"))
            hp.ChatroomInfoUIClassName = "com.tencent.mm.chatroom.ui.ChatroomInfoUI";
    }

    private static int getVersionNum(String version) {
        String[] v = version.split("\\.");
        if (v.length == 3)
            return Integer.valueOf(v[0]) * 100 * 100 + Integer.valueOf(v[1]) * 100 + Integer.valueOf(v[2]);
        else
            return 0;
    }

    private static boolean loadConfig(XC_LoadPackage.LoadPackageParam lpparam, String curVersionName) {

        try {
            SharedPreferences pref = getPreferencesInstance();
            HookParams hp = new Gson().fromJson(pref.getString("params", ""), HookParams.class);
            log("HookParams.VERSION_CODE: "+hp.versionCode);

            if (hp == null
                    || !hp.versionName.equals(curVersionName)
                    || hp.versionCode != HookParams.VERSION_CODE) {
                log("HookParams.VERSION_CODE: "+hp.versionCode);
                return false;
            }

            HookParams.setInstance(hp);
            log("load config successful");
            return true;
        } catch (Error | Exception e) {
            log("load config failed!");
        }
        return false;
    }

    private static void saveConfig(Context context) {
        try {
            Intent saveConfigIntent = new Intent();
            saveConfigIntent.setAction(HookParams.SAVE_WECHAT_ENHANCEMENT_CONFIG);
            saveConfigIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            saveConfigIntent.putExtra("params", new Gson().toJson(HookParams.getInstance()));
            context.sendBroadcast(saveConfigIntent);
            log("saving config...");
        } catch (Error | Exception e) {
            log("saving config failed!");
        }
    }

    private static XSharedPreferences getPreferencesInstance() {
        if (preferencesInstance == null) {
            preferencesInstance = new XSharedPreferences(HookTest.class.getPackage().getName(), HookParams.WECHAT_ENHANCEMENT_CONFIG_NAME);
            preferencesInstance.makeWorldReadable();
        } else {
            preferencesInstance.reload();
        }
        return preferencesInstance;
    }
}
