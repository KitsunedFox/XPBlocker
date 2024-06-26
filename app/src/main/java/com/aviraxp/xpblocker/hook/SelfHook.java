package com.aviraxp.xpblocker.hook;

import com.aviraxp.xpblocker.BuildConfig;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

class SelfHook {
    public void hook(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.equals(BuildConfig.APPLICATION_ID)) {
            Class<?> SettingsActivity = XposedHelpers.findClass(BuildConfig.APPLICATION_ID + ".ui.SettingsActivity", lpparam.classLoader);
            XposedHelpers.setStaticBooleanField(SettingsActivity, "isActivated", true);
        }
    }
}
