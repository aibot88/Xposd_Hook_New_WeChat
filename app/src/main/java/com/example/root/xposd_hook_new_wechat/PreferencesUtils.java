package com.example.root.xposd_hook_new_wechat;

import java.util.Objects;

import de.robv.android.xposed.XSharedPreferences;

public class PreferencesUtils {

    private static XSharedPreferences instance = null;

    private static XSharedPreferences getInstance() {
        if (instance == null) {
            instance = new XSharedPreferences(Objects.requireNonNull(PreferencesUtils.class.getPackage()).getName());
            instance.makeWorldReadable();
        } else {
            instance.reload();
        }
        return instance;
    }
    public static boolean isAutoLogin() {
        return getInstance().getBoolean("is_auto_login", true);
    }

}


