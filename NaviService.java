package com.autosdk.lbs.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import b.c.b.b.d;
import b.c.b.b.h;
import b.c.b.b.i;
import com.autosdk.lbs.R$string;
import com.autosdk.protocol.constant.ProtocolResultCode;
import com.baidu.naviauto.imaplbs.bean.MapMatchInfo;
import com.baidu.naviauto.imaplbs.bean.RemainInfo;
import com.baidu.naviauto.imaplbs.bean.TurnInfo;
import com.byd.common.guide.model.CrossNaviInfo;
import com.byd.common.guide.model.NaviBroadCastInfo;
import com.byd.common.guide.model.NaviInfoPanel;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes.dex */
public class NaviService extends Service {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static String f1393b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static String f1394c = null;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static int f1395d = 0;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static boolean f1396e = false;
    public Integer f;
    public BroadcastReceiver g;
    public int h = 0;
    public final Timer i = new Timer();
    public final Timer j = new Timer();
    public TimerTask k = new b();
    public TimerTask l = new c();

    public class a extends BroadcastReceiver {
        public a() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            NaviService naviService;
            int i;
            if (b.a.a.a.b().f()) {
                int i2 = b.a.a.e.a.g().i();
                if (i2 == 0 || i2 == 1 || i2 == 2) {
                    naviService = NaviService.this;
                    i = 8;
                } else if (i2 != 3) {
                    naviService = NaviService.this;
                    i = 9;
                } else {
                    naviService = NaviService.this;
                    i = 10;
                }
                naviService.j(i);
            }
        }
    }

    public class b extends TimerTask {
        public b() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (NaviService.f1396e) {
                return;
            }
            NaviService.this.k();
        }
    }

    public class c extends TimerTask {
        public c() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            boolean zF = b.a.a.a.b().f();
            b.c.b.b.c.c("NaviService", "isInit  :" + zF + "  ;isAccOff:" + NaviService.f1396e, new Object[0]);
            if (zF || NaviService.f1396e) {
                return;
            }
            b.a.a.a.b().e(i.b().getApplicationContext());
        }
    }

    public static String q(int i) {
        String[] strArrA = d.a(i);
        return strArrA[0] + strArrA[1];
    }

    public static String r(int i) {
        String[] strArrA = d.a(i);
        if (i <= 10) {
            return h.b(R$string.autonavi_page_now_string);
        }
        return strArrA[0] + strArrA[1];
    }

    public void b() {
        this.i.cancel();
    }

    public void c() {
        this.j.cancel();
    }

    public void d() {
        this.i.schedule(this.k, 0L, 1000L);
    }

    public void e() {
        this.j.schedule(this.l, 0L, 10000L);
    }

    public boolean f() {
        return b.c.b.c.d.d().a().d() == 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0030  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final boolean g(com.baidu.naviauto.imaplbs.bean.TurnInfo r5, com.baidu.naviauto.imaplbs.bean.TurnInfo r6) {
        /*
            r4 = this;
            int r0 = r5.getRoadLevel()
            r1 = 0
            r2 = 1
            if (r0 != 0) goto La
            r0 = r2
            goto Lb
        La:
            r0 = r1
        Lb:
            int r6 = r6.getRemainDistance()
            r3 = 2000(0x7d0, float:2.803E-42)
            if (r6 > r3) goto L30
            int r6 = r5.getDistanceToNextTurn()
            if (r0 == 0) goto L24
            if (r6 < 0) goto L30
            int r5 = r5.getDistanceToNextTurn()
            r6 = 300(0x12c, float:4.2E-43)
            if (r5 > r6) goto L30
            goto L2e
        L24:
            if (r6 < 0) goto L30
            int r5 = r5.getDistanceToNextTurn()
            r6 = 200(0xc8, float:2.8E-43)
            if (r5 > r6) goto L30
        L2e:
            r5 = r2
            goto L31
        L30:
            r5 = r1
        L31:
            r6 = 2
            java.lang.Object[] r6 = new java.lang.Object[r6]
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            r6[r1] = r0
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r5)
            r6[r2] = r0
            java.lang.String r0 = "NaviService"
            java.lang.String r1 = "isHighWay {?} ; isShow {?}"
            b.c.b.b.c.c(r0, r1, r6)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.autosdk.lbs.service.NaviService.g(com.baidu.naviauto.imaplbs.bean.TurnInfo, com.baidu.naviauto.imaplbs.bean.TurnInfo):boolean");
    }

    public final void h() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("AUTONAVI_STANDARD_BROADCAST_REQUEST");
        a aVar = new a();
        this.g = aVar;
        registerReceiver(aVar, intentFilter);
    }

    @SuppressLint({"WrongConstant"})
    public void i(int i) {
        if (f()) {
            return;
        }
        b.c.b.b.c.c("NaviService", "sendMapStateChangedBroadcast state: " + i, new Object[0]);
        Intent intent = new Intent();
        intent.setPackage("com.byd.amapservice");
        intent.setAction("AUTONAVI_STANDARD_BROADCAST_SEND");
        intent.putExtra("KEY_TYPE", ProtocolResultCode.RESULT_NO_NETWORK_NO_DATA);
        intent.putExtra("EXTRA_STATE", i);
        intent.putExtra("IS_BYD_MAP", true);
        intent.putExtra("IS_BYD_BAIDU_MAP", true);
        i.b().sendBroadcast(intent);
    }

    public final void j(int i) {
        if (i.c("com.byd.launchermap")) {
            b.c.b.b.c.c("NaviService", "launchermap ", new Object[0]);
            return;
        }
        if (f()) {
            return;
        }
        b.c.b.b.c.c("NaviService", "sendMapStatusBroadcast statusValue :{?}", Integer.valueOf(i));
        Intent intent = new Intent();
        intent.setAction("AUTONAVI_STANDARD_BROADCAST_SEND");
        intent.putExtra("KEY_TYPE", ProtocolResultCode.RESULT_NO_NETWORK_NO_DATA);
        intent.putExtra("EXTRA_STATE", i);
        intent.putExtra("IS_BYD_MAP", true);
        intent.putExtra("IS_BYD_BAIDU_MAP", true);
        intent.putExtra("EXTRA_IS_FOREGROUND", 1 ^ (b.a.a.d.a.e().f() ? 1 : 0));
        sendBroadcast(intent);
        if (8 != i) {
            b.a.a.e.c.d().i();
            return;
        }
        b.c.b.b.c.c("NaviService", "isNavi =" + b.a.a.e.a.g().q(), new Object[0]);
        b.a.a.e.c.d().h();
    }

    public void k() {
        if (i.c("com.byd.launchermap")) {
            b.c.b.b.c.c("NaviService", "launchermap ", new Object[0]);
            return;
        }
        if (f()) {
            return;
        }
        if (b.a.a.e.a.g().p()) {
            NaviBroadCastInfo naviBroadCastInfo = new NaviBroadCastInfo();
            s(naviBroadCastInfo);
            l(naviBroadCastInfo, Integer.valueOf(b.a.a.e.a.g().i()).intValue(), !Boolean.valueOf(b.a.a.d.a.e().f()).booleanValue() ? 1 : 0);
            b.a.a.e.c.d().p(2);
        } else if (!b.a.a.e.a.g().o()) {
            return;
        } else {
            b.a.a.e.c.d().p(1);
        }
        o(1);
        n();
    }

    @SuppressLint({"WrongConstant"})
    public void l(NaviBroadCastInfo naviBroadCastInfo, int i, int i2) {
        if (f()) {
            return;
        }
        b.c.b.b.c.c("NaviService", "sendNaviInfoBroadcast  mNaviType :{?}  isForGround :{?}", Integer.valueOf(i), Integer.valueOf(i2));
        b.c.b.b.c.c("NaviService", "sendNaviInfoBroadcast:" + naviBroadCastInfo.toString(), new Object[0]);
        Intent intent = new Intent();
        intent.setPackage("com.byd.amapservice");
        intent.setAction("AUTONAVI_STANDARD_BROADCAST_SEND");
        intent.putExtra("KEY_TYPE", ProtocolResultCode.RESULT_PARAM_ERROR);
        intent.putExtra("TYPE", i);
        intent.putExtra("EXTRA_IS_FOREGROUND", i2);
        intent.putExtra("EXTRA_STATE", i);
        intent.putExtra("IS_BYD_MAP", true);
        intent.putExtra("IS_BYD_BAIDU_MAP", true);
        intent.putExtra("NEXT_ROAD_NAME", naviBroadCastInfo.nextRoadName);
        intent.putExtra("ROUTE_REMAIN_DIS", naviBroadCastInfo.routeRemainDis);
        intent.putExtra("ROUTE_REMAIN_TIME", naviBroadCastInfo.routeRemainTime);
        intent.putExtra("TRAFFIC_LIGHT_NUM", naviBroadCastInfo.trafficLightNum);
        String strR = r(naviBroadCastInfo.remainDistance);
        intent.putExtra("SEG_REMAIN_DIS_AUTO", strR);
        String strC = b.c.b.b.a.c(i.b(), naviBroadCastInfo.routeRemainTime);
        intent.putExtra("ETA_TEXT", strC);
        String strQ = q(naviBroadCastInfo.routeRemainDis);
        intent.putExtra("ROUTE_REMAIN_DIS_AUTO", strQ);
        String strB = d.b(naviBroadCastInfo.routeRemainTime);
        intent.putExtra("ROUTE_REMAIN_TIME_AUTO", strB);
        intent.putExtra("NEXT_SEG_CURROAD_CLASS", naviBroadCastInfo.curRoadClass);
        int i3 = naviBroadCastInfo.NaviInfoFlag;
        int i4 = (i3 < 0 || i3 >= naviBroadCastInfo.NaviInfoData.size()) ? 0 : naviBroadCastInfo.NaviInfoData.get(naviBroadCastInfo.NaviInfoFlag).maneuverID;
        b.c.b.b.c.c("NaviService", "sendNaviInfoBroadcast etaText: " + strC + " routeRemainDisAuto: " + strQ + " routeRemainTimeAuto: " + strB + " segRemainDis: " + strR + " NaviInfoPanel.maneuverID: " + i4, new Object[0]);
        if (i4 > 0) {
            intent.putExtra("NEW_ICON", i4);
            f1395d = i4;
        } else {
            int i5 = f1395d;
            if (i5 > 0) {
                intent.putExtra("NEW_ICON", i5);
            }
        }
        if (naviBroadCastInfo.nextCrossInfo.size() > 0) {
            CrossNaviInfo crossNaviInfo = naviBroadCastInfo.nextCrossInfo.get(0);
            intent.putExtra("NEXT_SEG_REMAIN_DIS", crossNaviInfo.curToSegmentDist);
            intent.putExtra("NEXT_NEXT_ROAD_NAME", crossNaviInfo.nextRoadName);
            intent.putExtra("NEXT_NEXT_TURN_ICON", crossNaviInfo.maneuverID);
        }
        if (naviBroadCastInfo.NaviInfoData.size() > 0) {
            intent.putExtra("SEG_REMAIN_DIS", naviBroadCastInfo.NaviInfoData.get(0).segmentRemain.dist);
        }
        String str = f1393b;
        if (str != null && f1394c != null) {
            intent.putExtra("EXIT_NAME_INFO", str);
            intent.putExtra("EXIT_DIRECTION_INFO", f1394c);
        }
        intent.addFlags(16777216);
        b.c.b.b.c.c("NaviService", "intent:" + intent.getExtras(), new Object[0]);
        i.b().sendBroadcast(intent);
    }

    @SuppressLint({"WrongConstant"})
    public final void m() {
        int i;
        int iValueOf;
        if (f()) {
            return;
        }
        b.c.b.b.c.c("NaviService", "sendNaviStopBroadcast", new Object[0]);
        Intent intent = new Intent();
        intent.setAction("AUTONAVI_STANDARD_BROADCAST_SEND");
        intent.putExtra("KEY_TYPE", ProtocolResultCode.RESULT_NO_NETWORK_NO_DATA);
        if (this.f.intValue() == 0) {
            iValueOf = 9;
        } else {
            if (this.f.intValue() != 1) {
                i = this.f.intValue() == 2 ? 25 : 12;
                intent.putExtra("EXTRA_STATE", 9);
                intent.putExtra("IS_BYD_MAP", true);
                intent.putExtra("IS_BYD_BAIDU_MAP", true);
                intent.addFlags(16777216);
                sendBroadcast(intent);
                b.a.a.e.c.d().i();
            }
            iValueOf = Integer.valueOf(i);
        }
        this.f = iValueOf;
        intent.putExtra("EXTRA_STATE", 9);
        intent.putExtra("IS_BYD_MAP", true);
        intent.putExtra("IS_BYD_BAIDU_MAP", true);
        intent.addFlags(16777216);
        sendBroadcast(intent);
        b.a.a.e.c.d().i();
    }

    public final void n() {
        int iM = b.a.a.e.a.g().m();
        if (iM == -1) {
            iM = 0;
        }
        b.c.b.b.c.c("NaviService", "sendSpeedLimit : " + iM, new Object[0]);
        b.a.a.e.c.d().s(iM);
    }

    public final void o(int i) {
        b.c.b.b.c.c("NaviService", "sendZoneCode :" + i, new Object[0]);
        b.a.a.e.c.d().t(i);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        int i;
        super.onCreate();
        p();
        h();
        Integer numValueOf = Integer.valueOf(b.a.a.e.a.g().i());
        this.f = numValueOf;
        if (numValueOf != null) {
            if (numValueOf.intValue() == 0 || this.f.intValue() == 1 || this.f.intValue() == 2) {
                i = 8;
            } else if (this.f.intValue() == 3) {
                i = 10;
            } else if (this.f.intValue() == -1) {
                i = 9;
            }
            i(i);
        }
        d();
        e();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.g);
        b();
        c();
        m();
        b.a.a.a.b().g();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        p();
        return super.onStartCommand(intent, i, i2);
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        return false;
    }

    public final void p() {
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        int i = Build.VERSION.SDK_INT;
        if (i >= 26) {
            notificationManager.createNotificationChannel(new NotificationChannel("service_location", "百度地图中转", 4));
        }
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        if (i >= 26) {
            builder.setChannelId("service_location");
        }
        startForeground(1, builder.build());
        b.c.b.b.c.a("NaviService", "启动前台服务 for 百度地图 ", new Object[0]);
    }

    public final void s(NaviBroadCastInfo naviBroadCastInfo) {
        if (b.a.a.e.a.g().p()) {
            MapMatchInfo mapMatchInfoH = b.a.a.e.a.g().h();
            List<TurnInfo> listN = b.a.a.e.a.g().n();
            RemainInfo remainInfoK = b.a.a.e.a.g().k();
            b.c.b.b.c.c("NaviService", "updateNaviInfo:matchInfo=" + mapMatchInfoH + "\nturnInfos=" + listN + "\nremainInfo=" + remainInfoK, new Object[0]);
            b.a.a.e.c.d().g();
            if (remainInfoK != null) {
                naviBroadCastInfo.routeRemainTime = remainInfoK.getRemainTime();
                naviBroadCastInfo.routeRemainDis = remainInfoK.getRemainDistance();
                if (remainInfoK.getRemainTrafficLight() != null) {
                    naviBroadCastInfo.trafficLightNum = String.valueOf(remainInfoK.getRemainTrafficLight().length);
                }
            }
            if (listN == null || listN.size() <= 0) {
                return;
            }
            TurnInfo turnInfo = listN.get(0);
            naviBroadCastInfo.nextRoadName = turnInfo.getNextRoadName();
            naviBroadCastInfo.curRoadClass = turnInfo.getRoadLevel();
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            NaviInfoPanel naviInfoPanel = new NaviInfoPanel();
            if (turnInfo.isStraight()) {
                b.c.b.b.c.c("NaviService", "isStraight 顺行", new Object[0]);
                naviInfoPanel.maneuverID = 1077;
            } else {
                naviInfoPanel.maneuverID = turnInfo.getTurnKind();
            }
            naviInfoPanel.segmentRemain.dist = turnInfo.getRemainDistance();
            b.c.b.b.c.c("NaviService", "dist :" + naviInfoPanel.segmentRemain.dist + " ;turnInfos.size() :" + listN.size(), new Object[0]);
            naviInfoPanel.nextRouteName = turnInfo.getNextRoadName();
            if (listN.size() > 1) {
                TurnInfo turnInfo2 = listN.get(1);
                CrossNaviInfo crossNaviInfo = new CrossNaviInfo();
                crossNaviInfo.curToSegmentDist = turnInfo2.getDistanceToPreTurn();
                if (!g(turnInfo, turnInfo2)) {
                    crossNaviInfo.curToSegmentDist = -1;
                }
                if (turnInfo2.isStraight()) {
                    b.c.b.b.c.c("NaviService", "isCrossNaviInfoStraight 顺行", new Object[0]);
                    crossNaviInfo.maneuverID = 1077;
                } else {
                    crossNaviInfo.maneuverID = turnInfo2.getTurnKind();
                }
                crossNaviInfo.nextRoadName = turnInfo2.getNextRoadName();
                arrayList.add(crossNaviInfo);
            }
            arrayList2.add(naviInfoPanel);
            naviBroadCastInfo.nextCrossInfo.clear();
            naviBroadCastInfo.nextCrossInfo.addAll(arrayList);
            naviBroadCastInfo.NaviInfoData.clear();
            naviBroadCastInfo.NaviInfoData.addAll(arrayList2);
        }
    }
}
