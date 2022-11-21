package com.example.root.xposd_hook_new_wechat;

import static de.robv.android.xposed.XposedBridge.log;

public class HookParams {
    public static final String SAVE_WECHAT_ENHANCEMENT_CONFIG = "wechat.intent.action.SAVE_WECHAT_ENHANCEMENT_CONFIG";
    public static final String WECHAT_ENHANCEMENT_CONFIG_NAME = "wechat_enhancement_config";
    public static final String WECHAT_PACKAGE_NAME = "com.tencent.mm";
//    public static final int VERSION_CODE = 46; //大版本变动时候才需要修改
    public static final int VERSION_CODE = 2260; //大版本变动时候才需要修改

    public String SQLiteDatabaseClassName = "com.tencent.wcdb.database.SQLiteDatabase";

    public String ChatroomInfoUIClassName = "com.tencent.mm.plugin.chatroom.ui.ChatroomInfoUI";
    public String WebWXLoginUIClassName = "com.tencent.mm.plugin.webwx.ui.ExtDeviceWXLoginUI";
    public String LuckyMoneyReceiveUIClassName = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
    public boolean hasTimingIdentifier = true;
    public String versionName;
    public int versionCode;

    private static HookParams instance = null;

    private HookParams() {
    }

    public static HookParams getInstance() {
        if (instance == null)
            instance = new HookParams();
        return instance;
    }

    public static void setInstance(HookParams i) {
        instance = i;
    }

    public static boolean hasInstance() {
        log("hasInstance的结果是: "+instance);
        return instance != null;
    }

}
