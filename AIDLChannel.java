package com.baidu.naviauto.imaplbs.inner;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import b.b.a.a.b.d;
import b.b.a.a.b.e;
import com.baidu.naviauto.imaplbs.IEventListener;
import com.baidu.naviauto.imaplbs.IMapAutoAPIService;
import com.baidu.naviauto.imaplbs.inner.bean.APIRequest;
import com.baidu.naviauto.imaplbs.inner.bean.APIResponse;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
public class AIDLChannel extends b.b.a.a.c.a {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static volatile AIDLChannel f1417c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public WeakReference<Context> f1418d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public IMapAutoAPIService f1419e;
    public IBinder f;
    public d g;
    public final String h;
    public final String i;
    public ServiceConnection j;
    public IBinder.DeathRecipient k;
    public IEventListener l;

    public class a implements ServiceConnection {
        public a() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            b.b.a.a.f.b.a("LBS-AIDLChannel", "onServiceConnected", new Object[0]);
            AIDLChannel.this.f1419e = IMapAutoAPIService.Stub.asInterface(iBinder);
            AIDLChannel.this.f = iBinder;
            try {
                AIDLChannel.this.f.linkToDeath(AIDLChannel.this.k, 0);
            } catch (Exception unused) {
            }
            try {
                AIDLChannel.this.f1419e.setSDKEventListener(AIDLChannel.this.l, String.valueOf(Process.myPid()));
            } catch (Exception e2) {
                b.b.a.a.f.b.a("LBS-AIDLChannel", "onServiceConnected setEventListener error:%s", e2);
            }
            AIDLChannel.this.f();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            b.b.a.a.f.b.a("LBS-AIDLChannel", "onServiceConnected, component:%s", componentName);
            AIDLChannel.this.f1419e = null;
            try {
                AIDLChannel.this.f.unlinkToDeath(AIDLChannel.this.k, 0);
            } catch (Exception unused) {
            }
            AIDLChannel.this.f = null;
            AIDLChannel.this.g(componentName == null ? 6 : 3, componentName == null ? "map is dead" : "aidl channel disconnected");
        }
    }

    public class b implements IBinder.DeathRecipient {
        public b() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            b.b.a.a.f.b.a("LBS-AIDLChannel", "binderDied, map is dead", new Object[0]);
        }
    }

    public AIDLChannel(Context context, d dVar, String str, String str2) {
        this.j = new a();
        this.k = new b();
        this.l = new IEventListener.Stub() { // from class: com.baidu.naviauto.imaplbs.inner.AIDLChannel.3
            @Override // com.baidu.naviauto.imaplbs.IEventListener
            public void onEvent(String str3) {
                b.b.a.a.f.b.a("LBS-AIDLChannel", "onEvent:%s", str3);
                AIDLChannel.this.t(AIDLChannel.this.g.a(str3));
            }
        };
        this.f1418d = new WeakReference<>(context.getApplicationContext());
        this.g = dVar;
        this.h = str;
        this.i = str2;
    }

    public AIDLChannel(Context context, String str, String str2) {
        this(context, new e(), str, str2);
    }

    public static AIDLChannel q(Context context, String str, String str2) {
        if (f1417c == null) {
            synchronized (AIDLChannel.class) {
                if (f1417c == null) {
                    f1417c = new AIDLChannel(context, str, str2);
                }
            }
        }
        return f1417c;
    }

    @Override // b.b.a.a.a.a
    public APIResponse b(APIRequest aPIRequest) {
        if (!s()) {
            APIResponse aPIResponse = new APIResponse(aPIRequest);
            aPIResponse.setErrorCode(3);
            aPIResponse.setErrorMessage("aidl channel disconnected");
            b.b.a.a.f.b.a("LBS-AIDLChannel", "send fail, aidl channel disconnected", new Object[0]);
            return aPIResponse;
        }
        try {
            String strB = this.g.b(aPIRequest);
            b.b.a.a.f.b.a("LBS-AIDLChannel", "send data:%s", strB);
            String strSend = this.f1419e.send(strB);
            b.b.a.a.f.b.a("LBS-AIDLChannel", "send data response:%s", strSend);
            return this.g.a(strSend);
        } catch (Exception e2) {
            APIResponse aPIResponse2 = new APIResponse(aPIRequest);
            aPIResponse2.setErrorCode(4);
            aPIResponse2.setErrorMessage(e2.getMessage());
            b.b.a.a.f.b.a("LBS-AIDLChannel", "send data error:%s", e2);
            return aPIResponse2;
        }
    }

    @Override // b.b.a.a.a.a
    public void c() {
        Context context = this.f1418d.get();
        if (context == null) {
            g(5, "channel context is null");
            return;
        }
        if (r()) {
            IMapAutoAPIService iMapAutoAPIService = this.f1419e;
            if (iMapAutoAPIService != null) {
                try {
                    iMapAutoAPIService.removeSDKEventListener(String.valueOf(Process.myPid()));
                } catch (RemoteException e2) {
                    e2.printStackTrace();
                }
            }
            try {
                IBinder iBinder = this.f;
                if (iBinder != null) {
                    iBinder.unlinkToDeath(this.k, 0);
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            context.unbindService(this.j);
        }
        this.f1419e = null;
        this.f = null;
    }

    @Override // b.b.a.a.a.a
    public void d() {
        Context context = this.f1418d.get();
        if (context == null) {
            g(5, "channel context is null");
            return;
        }
        if (r()) {
            c();
        }
        Intent intent = new Intent("com.baidu.naviauto.imaplbs.lbsservice");
        intent.setPackage(this.i);
        intent.putExtra("package", this.h);
        context.bindService(intent, this.j, 1);
    }

    public boolean r() {
        return s();
    }

    public boolean s() {
        return this.f1419e != null;
    }

    public final void t(APIResponse aPIResponse) {
        b.b.a.a.d.e eVarH = h();
        if (eVarH != null) {
            eVarH.a(aPIResponse);
        }
    }
}
