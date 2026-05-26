package com.byd.launchermap;

import android.app.Application;
import android.content.Intent;
import android.hardware.bydauto.BYDAutoEventValue;
import android.hardware.bydauto.BYDAutoFeatureIds;
import android.hardware.bydauto.bodywork.AbsBYDAutoBodyworkListener;
import android.hardware.bydauto.bodywork.BYDAutoBodyworkDevice;
import android.os.Build;
import android.util.Log;
import b.c.b.b.c;
import b.c.b.b.i;
import b.c.b.b.j;
import com.autosdk.lbs.service.NaviService;
import com.byd.common.tts.NaviTTSPlayer;
import com.byd.map.account.MapAccountManager;
import com.example.amapservice.AmapService;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public class AdapterApplication extends Application {

    /* JADX INFO: renamed from: b */
    public static Application f1433b;

    /* JADX INFO: renamed from: c */
    public String f1434c = "BaiduMap";

    /* JADX INFO: renamed from: d */
    public String f1435d = "GaoDeMap";

    /* JADX INFO: renamed from: e */
    public final AbsBYDAutoBodyworkListener f1436e = new a();

    public class a extends AbsBYDAutoBodyworkListener {
        public a() {
        }

        @Override // android.hardware.bydauto.bodywork.AbsBYDAutoBodyworkListener, android.hardware.IBYDAutoListener
        public void onDataEventChanged(int i, BYDAutoEventValue bYDAutoEventValue) {
            super.onDataEventChanged(i, bYDAutoEventValue);
            int i2 = bYDAutoEventValue.intValue;
            if (i == BYDAutoFeatureIds.Bodywork.BODYWORK_POWER_LEVEL) {
                if (2 == i2) {
                    if (!b.a.a.a.b().f()) {
                        b.a.a.a.b().e(AdapterApplication.this);
                    }
                    AmapService.j = false;
                    NaviService.f1396e = false;
                    AmapService.f1446d = false;
                    c.c(AbsBYDAutoBodyworkListener.TAG, "BODYWORK_POWER_LEVEL_ON !!!mIsShutdown = false", new Object[0]);
                    return;
                }
                if (i2 == 0) {
                    if (b.a.a.a.b().f()) {
                        b.a.a.a.b().g();
                    }
                    NaviService.f1396e = true;
                    AmapService.f1446d = true;
                    c.c(AbsBYDAutoBodyworkListener.TAG, "BODYWORK_POWER_LEVEL_OFF !!!", new Object[0]);
                    Intent intent = new Intent();
                    intent.setPackage("com.byd.amapservice");
                    intent.setAction("byd.intent.action.ACC_OFF");
                    i.b().sendBroadcast(intent);
                }
            }
        }
    }

    public static String a() {
        StringBuilder sb = new StringBuilder();
        String str = File.separator;
        sb.append(str);
        sb.append("BydAutoMap");
        sb.append(str);
        sb.append("bllog");
        sb.append(str);
        sb.append("BydMapLog");
        sb.append(str);
        return sb.toString();
    }

    public void b() {
        Log.e("AdapterApplication", "onCreate() NaviService");
        Intent intent = new Intent();
        intent.setClass(this, NaviService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    public void c() {
        Log.e("AdapterApplication", "onCreate() AmapService:" + AmapService.k);
        if (AmapService.k) {
            return;
        }
        Intent intent = new Intent(this, (Class<?>) AmapService.class);
        intent.setAction("com.byd.amapservice");
        if (Build.VERSION.SDK_INT < 26) {
            startService(intent);
        } else {
            Log.e("AdapterApplication", "startForegroundService");
            startForegroundService(intent);
        }
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        Log.e("AdapterApplication", "V1.0.27.20240725 AdapterApplication create...");
        f1433b = this;
        c.f(this, a());
        i.e(this);
        NaviTTSPlayer.f().c();
        j.m();
        if (i.c("com.byd.naviauto")) {
            c.c("AdapterApplication", "start NaviService", new Object[0]);
            b();
        }
        c();
        MapAccountManager.getInstance().init(this);
        b.a.a.a.b().e(this);
        BYDAutoBodyworkDevice.getInstance(this).registerListener(this.f1436e, new int[]{BYDAutoFeatureIds.Bodywork.BODYWORK_POWER_LEVEL});
    }
}
