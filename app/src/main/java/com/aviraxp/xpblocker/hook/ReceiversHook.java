package com.aviraxp.xpblocker.hook;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

import com.aviraxp.xpblocker.helper.PreferencesHelper;
import com.aviraxp.xpblocker.util.ContextUtils;
import com.aviraxp.xpblocker.util.LogUtils;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

class ReceiversHook {

    public void hook(final XC_LoadPackage.LoadPackageParam lpparam) {

        if (!PreferencesHelper.isReceiversHookEnabled()) {
            return;
        }

        try {
            ActivityInfo[] receiverInfo = ContextUtils.getSystemContext().getPackageManager().getPackageInfo(lpparam.packageName, PackageManager.GET_RECEIVERS).receivers;
            if (receiverInfo != null) {
                for (ActivityInfo info : receiverInfo) {
                    String className = info.getClass().getName();
                    if (HookLoader.receiversList.contains(className) && !PreferencesHelper.whiteListElements().contains(className)) {
                        XposedHelpers.findAndHookMethod(info.name, lpparam.classLoader, "onReceive", Context.class, Intent.class, XC_MethodReplacement.DO_NOTHING);
                        LogUtils.logRecord("Receiver Block Success: " + lpparam.packageName + "/" + info.name);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
    }
}
