package com.aviraxp.xpblocker.hook;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.aviraxp.xpblocker.helper.PreferencesHelper;
import com.aviraxp.xpblocker.util.ContextUtils;
import com.aviraxp.xpblocker.util.LogUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

class ServicesHook {

    public void hook(final XC_LoadPackage.LoadPackageParam lpparam) {

        XC_MethodHook servicesStartHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                Intent intent = (Intent) param.args[1];
                handleServiceStart(param, intent);
            }
        };

        XC_MethodHook servicesBindHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                Intent intent = (Intent) param.args[2];
                handleServiceStart(param, intent);
            }
        };

        if (PreferencesHelper.isServicesHookEnabled() && lpparam.packageName.equals("android")) {
            XposedBridge.hookAllMethods(XposedHelpers.findClass("com.android.server.am.ActiveServices", lpparam.classLoader), "startServiceLocked", servicesStartHook);
            XposedBridge.hookAllMethods(XposedHelpers.findClass("com.android.server.am.ActiveServices", lpparam.classLoader), "bindServiceLocked", servicesBindHook);
        }
    }

    private void handleServiceStart(XC_MethodHook.MethodHookParam param, Intent serviceIntent) {
        if (serviceIntent != null) {
            ComponentName serviceName = serviceIntent.getComponent();
            if (serviceName != null) {
                String packageName = serviceName.getPackageName();
                String splitServicesName = serviceName.getClassName();
                if (HookLoader.servicesList.contains(splitServicesName) && !PreferencesHelper.isWhitelisted(packageName) && !PreferencesHelper.whiteListElements().contains(splitServicesName)) {
                    if (!PreferencesHelper.isDisableSystemApps()) {
                        param.setResult(null);
                    } else {
                        try {
                            ApplicationInfo info = ContextUtils.getSystemContext().getPackageManager().getApplicationInfo(packageName, 0);
                            if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                                param.setResult(null);
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            return;
                        }
                    }
                    LogUtils.logRecord("Service Block Success: " + serviceName.flattenToShortString());
                }
            }
        }
    }
}