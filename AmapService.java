package com.example.amapservice;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.AVMCameraEvent;
import android.hardware.bydauto.BYDAutoConstants;
import android.hardware.bydauto.BYDAutoEventValue;
import android.hardware.bydauto.BYDAutoFeatureIds;
import android.hardware.bydauto.instrument.BYDAutoInstrumentDevice;
import android.hardware.bydauto.sensor.AbsBYDAutoSensorListener;
import android.hardware.bydauto.sensor.BYDAutoSensorDevice;
import android.hardware.bydauto.setting.BYDAutoSettingDevice;
import android.hardware.bydauto.statistic.BYDAutoStatisticDevice;
import android.media.AudioFormat;
import android.os.AutoContainerManager;
import android.os.BootBusinessManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import androidx.appcompat.widget.ActivityChooserModel;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.baidu.iovsdk.common.account.ExternalServices;
import flatbuffers.FlatBufferBuilder;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AmapService extends Service {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static boolean f1444b = true;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static String f1445c = "userdebug";

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static boolean f1446d = false;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static boolean f1447e = false;
    public static boolean f = false;
    public static boolean g = false;
    public static boolean h = false;
    public static boolean i = false;
    public static boolean j = false;
    public static boolean k = false;
    public static final String[] l = {" ", "自车图标", "左转图标", "右转图标", "左前方图标", "右前方图标", "左后方图标", "右后方图标", "左转掉头图标", "直行图标", "到达途经点图标", "进入环岛图标，右侧通行地区的逆时针环岛", "驶出环岛图标，右侧通行地区的逆时针环岛", "到达服务区图标", "到达收费站图标", "到达目的地图标", "进入隧道图标", "进入环岛图标，左侧通行地区的顺时针环岛", "驶出环岛图标，左侧通行地区的顺时针环岛", "右转掉头图标，左侧通行地区的掉头", "顺行图标", "绕环岛左转，右侧通行地区的逆时针环岛", "绕环岛右转，右侧通行地区的逆时针环岛", "绕环岛直行，右侧通行地区的逆时针环岛", "绕环岛调头，右侧通行地区的逆时针环岛", "绕环岛左转，左侧通行地区的顺时针环岛", "绕环岛右转，左侧通行地区的顺时针环岛", "绕环岛直行，左侧通行地区的顺时针环岛", "绕环岛调头，左侧通行地区的顺时针环岛"};
    public static final int[] m = {0, 0, 1, 2, 3, 5, 7, 8, 9, 11, 45, 13, 24, 46, 47, 48, 49, 14, 23, 10, 12, 15, 18, 20, 22, 16, 17, 19, 21};
    public static final String[] n = {" ", "直行", "右前方转弯", "右转", "右后方转弯", "掉头", "左后方转弯", "左转", "左前方转弯", "环岛", "环岛出口", "普通/JCT①/SAPA②二分歧靠左", "普通/JCT/SAPA二分歧靠右", "左侧走本线", "靠最左走本线", "右侧走本线", "靠最右走本线", "中间走本线", "普通三分歧/JCT/SAPA靠中间", "IC二分歧右侧走IC", "普通三分歧/JCT/SAPA靠最左", "普通三分歧/JCT/SAPA靠最右", "普通三分歧/JCT/SAPA靠中间", "起始地", "目的地", "途经点1", "途经点2", "途经点3", "途经点4", "进入渡口", "脱出渡口", "收费站", "IC二分歧左侧直行走IC", "IC二分歧右侧直行走IC", "普通/JCT/SAPA二分歧左侧直行", "普通/JCT/SAPA二分歧右侧直行", "普通/JCT/SAPA三分歧左侧直行", "普通/JCT/SAPA三分歧中央直行", "普通/JCT/SAPA三分歧右侧直行", "IC三分歧左侧走IC", "IC三分歧中央走IC", "IC三分歧右侧走IC", "IC三分歧左侧直行", "IC三分歧中间直行", "IC三分歧右侧直行", "八方向靠左直行", "八方向靠右直行", "八方向靠最左侧直行", "八方向沿中间直行", "八方向靠最右侧直行", "八方向左转+随后靠左", "八方向左转+随后靠右", "八方向左转+随后靠最左", "八方向左转+随后沿中间", "八方向左转+随后靠最右", "八方向右转+随后靠左", "八方向右转+随后靠右", "八方向右转+随后靠最左", "八方向右转+随后沿中间", "八方向右转+随后靠最右", "八方向左前方靠左侧", "八方向左前方靠右侧", "八方向右前方靠左侧", "八方向右前方靠右侧", "八方向掉头+随后靠左", "八方向掉头+随后靠右", "八方向掉头+随后靠最左", "八方向掉头+随后沿中间", "八方向掉头+随后靠最右", "多岔路", "室内停车位导航 楼层机动点", "左前方不是左转", "右前方不是右转", "左后方不是掉头", "八方向左前方+随后靠左", "八方向左前方+随后沿中间", "八方向左前方+随后靠右", "八方向右前方+随后靠左", "八方向右前方+随后沿中间", "八方向右前方+随后靠右", "八方向左后方+随后靠左", "八方向左后方+随后靠右", "八方向左后方+随后靠最左", "八方向左后方+随后沿中间", "八方向左后方+随后靠最右", "八方向右后方+随后靠左", "八方向右后方+随后靠右", "八方向右后方+随后靠最左", "八方向右后方+随后沿中间", "八方向右后方+随后靠最右", "环岛向前", "环岛向右前", "环岛向右", "环岛向右后", "环岛向后（掉头）", "环岛向左后", "环岛向左", "环岛向左前", "右后方掉头", "右后方", "辅助直行（用于复杂路口放大图）", "辅助直行二分歧靠左", "辅助直行二分歧靠右", "辅助直行三分歧靠左", "辅助直行三分歧靠右", "辅助直行三分歧靠中间", "收费站", "顺行", "无效值"};
    public static final int[] o = {0, 1001, 1002, AVMCameraEvent.EVT_TYPE_FIRST_FRAME, 1004, 1005, 1006, BYDAutoConstants.BYDAUTO_DEVICE_INSTRUMENT, BYDAutoConstants.BYDAUTO_DEVICE_PM2P5, BYDAutoConstants.BYDAUTO_DEVICE_CHARGING, BYDAutoConstants.BYDAUTO_DEVICE_SECURITY, BYDAutoConstants.BYDAUTO_DEVICE_GEARBOX, BYDAutoConstants.BYDAUTO_DEVICE_ENGINE, BYDAutoConstants.BYDAUTO_DEVICE_SPEED, BYDAutoConstants.BYDAUTO_DEVICE_STATISTIC, BYDAutoConstants.BYDAUTO_DEVICE_COLLISION, BYDAutoConstants.BYDAUTO_DEVICE_TYRE, BYDAutoConstants.BYDAUTO_DEVICE_LOCATION, BYDAutoConstants.BYDAUTO_DEVICE_VIDEO, BYDAutoConstants.BYDAUTO_DEVICE_AUX, 1020, BYDAutoConstants.BYDAUTO_DEVICE_RADIO, BYDAutoConstants.BYDAUTO_DEVICE_TEST, 1023, 1024, BYDAutoConstants.BYDAUTO_DEVICE_RADAR, BYDAutoConstants.BYDAUTO_DEVICE_REMINDER, BYDAutoConstants.BYDAUTO_DEVICE_VERSION, BYDAutoConstants.BYDAUTO_DEVICE_FUNCNOTICE, BYDAutoConstants.BYDAUTO_DEVICE_PHONE, BYDAutoConstants.BYDAUTO_DEVICE_CPUTEMPRATURE, BYDAutoConstants.BYDAUTO_DEVICE_PANORAMA, BYDAutoConstants.BYDAUTO_DEVICE_OTA, BYDAutoConstants.BYDAUTO_DEVICE_MQTT, BYDAutoConstants.BYDAUTO_DEVICE_YUN, BYDAutoConstants.BYDAUTO_DEVICE_PROPERTY, BYDAutoConstants.BYDAUTO_DEVICE_QCFS, BYDAutoConstants.BYDAUTO_DEVICE_SIGNAL, BYDAutoConstants.BYDAUTO_DEVICE_ADAS, BYDAutoConstants.BYDAUTO_DEVICE_GB, BYDAutoConstants.BYDAUTO_DEVICE_RESCUE, BYDAutoConstants.BYDAUTO_DEVICE_DOOR_LOCK, BYDAutoConstants.BYDAUTO_DEVICE_SAFETY_BELT, BYDAutoConstants.BYDAUTO_DEVICE_SENSOR, 1044, BYDAutoConstants.BYDAUTO_DEVICE_DTC, BYDAutoConstants.BYDAUTO_DEVICE_WIPER, BYDAutoConstants.BYDAUTO_DEVICE_REAR_VIEW_MIRROR, BYDAutoConstants.BYDAUTO_DEVICE_VEHICLE_DATA, BYDAutoConstants.BYDAUTO_DEVICE_SPECIAL, 1050, 1051, AudioFormat.CHANNEL_OUT_SURROUND, 1053, 1054, 1055, 1056, 1057, 1058, 1059, 1060, BYDAutoConstants.BYDAUTO_DEVICE_BIG_DATA, BYDAutoConstants.BYDAUTO_DEVICE_RSE, 1063, 1064, 1065, 1066, 1067, 1068, 1079, 1080, 1081, 1082, 1083, 1084, 1085, 1086, 1087, 1088, 1089, 1090, 1091, 1092, 1093, 1094, 1095, 1096, 1097, 1098, 1099, 1069, 1070, 1071, 1072, 1073, 1074, 1075, 1076, 1100, 1101, 1102, 1103, 1104, 1105, 1106, 1107, 1108, 1077, 1078};
    public c A;
    public int B;
    public e q;
    public FlatBufferBuilder r;
    public double s;
    public BYDAutoInstrumentDevice t;
    public BYDAutoSensorDevice u;
    public BYDAutoSettingDevice v;
    public Context w;
    public BYDAutoEventValue x;
    public double y;
    public double z;
    public boolean p = false;
    public String C = ExternalServices.ERR_BLANK;
    public String D = ExternalServices.ERR_BLANK;
    public int[] E = {573571116};
    public int F = 0;
    public int G = 0;
    public int H = 0;
    public int I = 0;
    public int J = 0;
    public int K = 0;
    public int L = 0;
    public int M = 0;
    public AutoContainerManager N = null;
    public final AbsBYDAutoSensorListener O = new a();
    public final BroadcastReceiver P = new b();

    public class a extends AbsBYDAutoSensorListener {
        public a() {
        }

        @Override // android.hardware.bydauto.sensor.AbsBYDAutoSensorListener
        public void onSlopeValueChanged(int i) {
            int i2;
            b.c.b.b.c.c(AbsBYDAutoSensorListener.TAG, "onSlopeValueChanged value is " + i, new Object[0]);
            if (AmapService.this.o(i) || i == AmapService.this.B) {
                i2 = -1000;
            } else {
                AmapService.this.B = i;
                double degrees = Math.toDegrees(Math.atan(((double) i) / 100.0d));
                i2 = (int) degrees;
                if (AmapService.f1444b) {
                    b.c.b.b.c.c(AbsBYDAutoSensorListener.TAG, "getArctanDegree value is " + i + " degree is " + degrees, new Object[0]);
                }
            }
            AmapService.this.s(6, i2, ExternalServices.ERR_BLANK);
        }
    }

    public class b extends BroadcastReceiver {
        public b() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            b.c.b.b.c.c("AmapService", "receive action:" + action, new Object[0]);
            if ("android.gpsinfo.fix.action.ALTITUDE_CHANGED".equals(action)) {
                AmapService.this.z = intent.getDoubleExtra("android.gpsinfo.fix.extra.LONGITUDE", 0.0d);
                AmapService.this.y = intent.getDoubleExtra("android.gpsinfo.fix.extra.LATITUDE", 0.0d);
                AmapService.this.s = intent.getDoubleExtra("android.gpsinfo.fix.extra.ALTITUDE", 0.0d);
                b.c.b.b.c.c("AmapService", "mLongitude is " + AmapService.this.z + " mLatitude is " + AmapService.this.y + " mAltitude is " + AmapService.this.s, new Object[0]);
                AmapService.this.x();
            }
        }
    }

    public class c extends BroadcastReceiver {
        public c() {
        }

        public /* synthetic */ c(AmapService amapService, a aVar) {
            this();
        }

        public final byte[] a(String str) {
            if (str == null) {
                b.c.b.b.c.c("AmapService", "string is null!", new Object[0]);
                return null;
            }
            try {
                byte[] bytes = str.getBytes("UnicodeLittleUnmarked");
                b.c.b.b.c.c("AmapService", "unicodes length is " + bytes.length, new Object[0]);
                return bytes;
            } catch (UnsupportedEncodingException e2) {
                throw new AssertionError(e2);
            }
        }

        public int b(StringBuffer stringBuffer) {
            try {
                if (stringBuffer.length() > 0) {
                    return Integer.parseInt(stringBuffer.toString());
                }
                return -1;
            } catch (Exception unused) {
                b.c.b.b.c.c("AmapService", "Integer.parseInt", new Object[0]);
                return -1;
            }
        }

        public int c(String str) {
            AmapService amapService;
            int i;
            AmapService amapService2 = AmapService.this;
            amapService2.F = 0;
            amapService2.G = 0;
            amapService2.H = 0;
            amapService2.I = 0;
            amapService2.J = 0;
            amapService2.K = 0;
            amapService2.L = 0;
            amapService2.M = 0;
            if (str != null && str.length() != 0) {
                StringBuffer stringBuffer = new StringBuffer();
                char[] charArray = str.toCharArray();
                char c2 = 20998;
                boolean z = true;
                if (str.indexOf("预计") < 0 || str.indexOf("到达") < 0 || str.indexOf(58) < 0) {
                    if (str.indexOf(22825) < 0 && str.indexOf(26102) < 0 && str.indexOf(20998) < 0) {
                        b.c.b.b.c.c("AmapService", "解析错误", new Object[0]);
                        return -1;
                    }
                    z = false;
                } else if (str.indexOf("今天") >= 0) {
                    AmapService.this.J = 1;
                } else {
                    if (str.indexOf("明天") >= 0) {
                        amapService = AmapService.this;
                        i = 2;
                    } else if (str.indexOf("后天") >= 0) {
                        amapService = AmapService.this;
                        i = 3;
                    } else if (str.indexOf("周一") >= 0) {
                        amapService = AmapService.this;
                        i = 4;
                    } else if (str.indexOf("周二") >= 0) {
                        amapService = AmapService.this;
                        i = 5;
                    } else if (str.indexOf("周三") >= 0) {
                        amapService = AmapService.this;
                        i = 6;
                    } else if (str.indexOf("周四") >= 0) {
                        amapService = AmapService.this;
                        i = 7;
                    } else if (str.indexOf("周五") >= 0) {
                        amapService = AmapService.this;
                        i = 8;
                    } else if (str.indexOf("周六") >= 0) {
                        amapService = AmapService.this;
                        i = 9;
                    } else if (str.indexOf("周日") >= 0) {
                        amapService = AmapService.this;
                        i = 10;
                    } else {
                        AmapService.this.J = 1;
                        b.c.b.b.c.c("AmapService", str, new Object[0]);
                    }
                    amapService.J = i;
                }
                int length = charArray.length;
                int i2 = 0;
                while (i2 < length) {
                    char c3 = charArray[i2];
                    if (c3 < '0' || c3 > '9') {
                        if (c3 == ':') {
                            int iB = b(stringBuffer);
                            if (iB < 0 || iB >= 24) {
                                return -1;
                            }
                            AmapService.this.K = iB;
                        } else if (c3 == 22825 && !z) {
                            int iB2 = b(stringBuffer);
                            if (iB2 <= 0) {
                                return -1;
                            }
                            AmapService.this.I = iB2;
                        } else if (c3 == 26102) {
                            int iB3 = b(stringBuffer);
                            if (iB3 < 0 || iB3 >= 24) {
                                return -1;
                            }
                            AmapService.this.F = iB3;
                        } else {
                            if (c3 == c2) {
                                int iB4 = b(stringBuffer);
                                b.c.b.b.c.c("AmapService", "temp=" + iB4, new Object[0]);
                                if (iB4 < 0 || iB4 >= 60) {
                                    return -1;
                                }
                                AmapService.this.G = iB4;
                            } else if (c3 == 21040) {
                                int iB5 = b(stringBuffer);
                                if (iB5 < 0 || iB5 >= 60) {
                                    return -1;
                                }
                                AmapService.this.L = iB5;
                            } else {
                                continue;
                            }
                            stringBuffer.delete(0, stringBuffer.length());
                        }
                        stringBuffer.delete(0, stringBuffer.length());
                    } else {
                        stringBuffer.append(c3);
                    }
                    i2++;
                    c2 = 20998;
                }
            }
            return 0;
        }

        public final void d(int i) {
            int i2;
            b.c.b.b.c.c("AmapService", "sendNaviToCluster mClusterType=" + AmapService.this.C + ",mIsBydMap=" + AmapService.f + ",mIsBYDMapNaving=" + AmapService.f1447e + ",mIsGAODENaving=" + AmapService.h, new Object[0]);
            e eVar = AmapService.this.q;
            int i3 = eVar.f1458c;
            if (i3 > 0 && (i2 = eVar.o) > i3) {
                eVar.o = i2 - i3;
                b.c.b.b.c.c("AmapService", "GuideInfo.nextToSegmentDist :" + AmapService.this.q.o, new Object[0]);
            }
            AmapService.this.C.equals("1");
            e();
            if (i > 0) {
                try {
                    b.c.b.b.c.c("AmapService", "延时" + i + "毫秒", new Object[0]);
                    Thread.sleep((long) i);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }

        public final void e() {
            int i;
            int i2;
            int i3;
            int i4;
            b.c.b.b.c.c("AmapService", "********send to independent CAN  start********", new Object[0]);
            e eVar = AmapService.this.q;
            int i5 = eVar.f1456a;
            if (i5 == 0 || i5 == 1) {
                eVar.f1456a = 2;
            } else {
                if (i5 != 9 && i5 != 12) {
                    b.c.b.b.c.c("AmapService", "error  naviState: " + AmapService.this.q.f1456a, new Object[0]);
                    return;
                }
                eVar.f1456a = 1;
            }
            b.c.b.b.c.c("AmapService", "ToCAN naviState : " + AmapService.this.q.f1456a, new Object[0]);
            e eVar2 = AmapService.this.q;
            eVar2.q = eVar2.f1456a;
            BYDAutoEventValue bYDAutoEventValue = new BYDAutoEventValue();
            AmapService amapService = AmapService.this;
            bYDAutoEventValue.intValue = amapService.q.f1456a;
            BYDAutoInstrumentDevice.getInstance(amapService.w).set(new int[]{BYDAutoFeatureIds.INSTRUMENT_SEND_NAVI_STATUS_SET}, bYDAutoEventValue);
            e eVar3 = AmapService.this.q;
            String str = eVar3.f1457b;
            if (str != "-1" && !str.equals(eVar3.r)) {
                b.c.b.b.c.c("AmapService", "nextRouteName:" + AmapService.this.q.f1457b, new Object[0]);
                e eVar4 = AmapService.this.q;
                eVar4.r = eVar4.f1457b;
                BYDAutoEventValue bYDAutoEventValue2 = new BYDAutoEventValue();
                byte[] bArrA = a(AmapService.this.q.f1457b);
                bYDAutoEventValue2.bufferDataValue = bArrA;
                if (bArrA == null) {
                    b.c.b.b.c.c("AmapService", "nextRouteName  null", new Object[0]);
                    return;
                }
                BYDAutoInstrumentDevice.getInstance(AmapService.this.w).set(new int[]{BYDAutoFeatureIds.INSTRUMENT_TARGET_NEXT_PATHNAME_INFO_SET}, bYDAutoEventValue2);
            }
            e eVar5 = AmapService.this.q;
            int i6 = eVar5.f1458c;
            if (i6 != -1 && i6 != eVar5.s) {
                b.c.b.b.c.c("AmapService", "curToSegmentDist:" + AmapService.this.q.f1458c, new Object[0]);
                e eVar6 = AmapService.this.q;
                eVar6.s = eVar6.f1458c;
                BYDAutoEventValue bYDAutoEventValue3 = new BYDAutoEventValue();
                AmapService amapService2 = AmapService.this;
                bYDAutoEventValue3.intValue = amapService2.q.f1458c;
                BYDAutoInstrumentDevice.getInstance(amapService2.w).set(new int[]{BYDAutoFeatureIds.INSTRUMENT_FRONT_CROSSING_DISTANCE_SET}, bYDAutoEventValue3);
            }
            if (AmapService.g) {
                e eVar7 = AmapService.this.q;
                int i7 = eVar7.f1460e;
                if (i7 != -1) {
                    eVar7.t = i7;
                    b.c.b.b.c.c("AmapService", "BAIDU nextTurnIcon:" + AmapService.this.q.f1460e, new Object[0]);
                    e eVar8 = AmapService.this.q;
                    int i8 = eVar8.f1460e;
                    if (i8 >= 0 && i8 < 107) {
                        eVar8.f1460e = AmapService.o[i8];
                    }
                    b.c.b.b.c.c("AmapService", "BAIDU nextTurnIcon later:" + AmapService.this.q.f1460e, new Object[0]);
                    BYDAutoEventValue bYDAutoEventValue4 = new BYDAutoEventValue();
                    AmapService amapService3 = AmapService.this;
                    bYDAutoEventValue4.intValue = amapService3.q.f1460e;
                    BYDAutoInstrumentDevice.getInstance(amapService3.w).set(new int[]{BYDAutoFeatureIds.Instrument.INSTRUMENT_GUIDE_INFO_AND_ROAD_AHEAD_DISTANCE_SET}, bYDAutoEventValue4);
                }
            } else {
                b.c.b.b.c.c("AmapService", "GAODE nextTurnIcon :{?} ; preNextTurnIcon :{?}" + AmapService.this.q.f1460e, Integer.valueOf(AmapService.this.q.t));
                e eVar9 = AmapService.this.q;
                int i9 = eVar9.f1460e;
                if (i9 != -1 && i9 >= 0 && i9 < 29) {
                    eVar9.t = i9;
                    int i10 = eVar9.C;
                    if (i10 <= 0 || i10 > 10) {
                        i = AmapService.m[i9];
                    } else {
                        if (11 == i9 || 12 == i9) {
                            i2 = i10 + 24;
                        } else if (17 == i9 || 18 == i9) {
                            i2 = i10 + 34;
                        } else {
                            i = AmapService.m[i9];
                        }
                        eVar9.f1460e = i2;
                        b.c.b.b.c.c("AmapService", "GAODE nextTurnIcon:" + AmapService.this.q.f1460e, new Object[0]);
                        BYDAutoEventValue bYDAutoEventValue5 = new BYDAutoEventValue();
                        AmapService amapService4 = AmapService.this;
                        bYDAutoEventValue5.intValue = amapService4.q.f1460e;
                        BYDAutoInstrumentDevice.getInstance(amapService4.w).set(new int[]{BYDAutoFeatureIds.INSTRUMENT_GUIDE_INFO_SIMPLE_SET}, bYDAutoEventValue5);
                        BYDAutoInstrumentDevice.getInstance(AmapService.this.w).set(new int[]{BYDAutoFeatureIds.Instrument.INSTRUMENT_GUIDE_INFO_AND_ROAD_AHEAD_DISTANCE_SET}, bYDAutoEventValue5);
                    }
                    eVar9.f1460e = i;
                    b.c.b.b.c.c("AmapService", "GAODE nextTurnIcon:" + AmapService.this.q.f1460e, new Object[0]);
                    BYDAutoEventValue bYDAutoEventValue52 = new BYDAutoEventValue();
                    AmapService amapService42 = AmapService.this;
                    bYDAutoEventValue52.intValue = amapService42.q.f1460e;
                    BYDAutoInstrumentDevice.getInstance(amapService42.w).set(new int[]{BYDAutoFeatureIds.INSTRUMENT_GUIDE_INFO_SIMPLE_SET}, bYDAutoEventValue52);
                    BYDAutoInstrumentDevice.getInstance(AmapService.this.w).set(new int[]{BYDAutoFeatureIds.Instrument.INSTRUMENT_GUIDE_INFO_AND_ROAD_AHEAD_DISTANCE_SET}, bYDAutoEventValue52);
                } else if (i9 == 65) {
                    eVar9.f1460e = 3;
                    b.c.b.b.c.c("AmapService", "65 turn to 3" + AmapService.this.q.f1460e, new Object[0]);
                    BYDAutoEventValue bYDAutoEventValue6 = new BYDAutoEventValue();
                    AmapService amapService5 = AmapService.this;
                    bYDAutoEventValue6.intValue = amapService5.q.f1460e;
                    BYDAutoInstrumentDevice.getInstance(amapService5.w).set(new int[]{BYDAutoFeatureIds.INSTRUMENT_GUIDE_INFO_SIMPLE_SET}, bYDAutoEventValue6);
                    BYDAutoInstrumentDevice.getInstance(AmapService.this.w).set(new int[]{BYDAutoFeatureIds.Instrument.INSTRUMENT_GUIDE_INFO_AND_ROAD_AHEAD_DISTANCE_SET}, bYDAutoEventValue6);
                }
            }
            e eVar10 = AmapService.this.q;
            String str2 = eVar10.l;
            if (str2 != "-1" && str2 != eVar10.y) {
                eVar10.y = str2;
                int iC = c(str2);
                b.c.b.b.c.c("AmapService", "routrRemainTime : " + iC, new Object[0]);
                if (iC == 0) {
                    b.c.b.b.c.c("AmapService", "routrRemainTimeAuto: " + AmapService.this.I + "天 " + AmapService.this.F + "时 " + AmapService.this.G + "分", new Object[0]);
                    BYDAutoEventValue bYDAutoEventValue7 = new BYDAutoEventValue();
                    int[] iArr = new int[1];
                    AmapService amapService6 = AmapService.this;
                    int i11 = amapService6.I;
                    if (i11 >= 0 && i11 < 11) {
                        bYDAutoEventValue7.intValue = i11;
                        iArr[0] = BYDAutoFeatureIds.INSTRUMENT_REMAIN_DRIVING_TIME_DAY_SET;
                        BYDAutoInstrumentDevice.getInstance(amapService6.w).set(iArr, bYDAutoEventValue7);
                    }
                    AmapService amapService7 = AmapService.this;
                    int i12 = amapService7.F;
                    if (i12 >= 0) {
                        bYDAutoEventValue7.intValue = i12;
                        iArr[0] = BYDAutoFeatureIds.INSTRUMENT_NAVI_TRIP_INFO_HOUR_SET;
                        BYDAutoInstrumentDevice.getInstance(amapService7.w).set(iArr, bYDAutoEventValue7);
                    }
                    AmapService amapService8 = AmapService.this;
                    int i13 = amapService8.G;
                    if (i13 > 0) {
                        bYDAutoEventValue7.intValue = i13;
                        iArr[0] = BYDAutoFeatureIds.INSTRUMENT_NAVI_TRIP_INFO_MINUTE_SET;
                        BYDAutoInstrumentDevice.getInstance(amapService8.w).set(iArr, bYDAutoEventValue7);
                    }
                    AmapService amapService9 = AmapService.this;
                    if (amapService9.I == 0 && amapService9.F == 0 && amapService9.G == 1 && (!AmapService.g ? amapService9.q.f >= 30 : amapService9.q.f >= 60)) {
                        b.c.b.b.c.c("AmapService", "小于1分钟", new Object[0]);
                        bYDAutoEventValue7.intValue = 1;
                        iArr[0] = BYDAutoFeatureIds.INSTRUMENT_NAVI_TRIP_REMAINING_SECOND_SET;
                        amapService9 = AmapService.this;
                    } else {
                        bYDAutoEventValue7.intValue = 0;
                        iArr[0] = BYDAutoFeatureIds.INSTRUMENT_NAVI_TRIP_REMAINING_SECOND_SET;
                    }
                    BYDAutoInstrumentDevice.getInstance(amapService9.w).set(iArr, bYDAutoEventValue7);
                }
            }
            e eVar11 = AmapService.this.q;
            int i14 = eVar11.g;
            if (i14 != -1 && i14 != eVar11.v) {
                b.c.b.b.c.c("AmapService", "routeRemainDist:" + AmapService.this.q.g, new Object[0]);
                e eVar12 = AmapService.this.q;
                eVar12.v = eVar12.g;
                BYDAutoEventValue bYDAutoEventValue8 = new BYDAutoEventValue();
                AmapService amapService10 = AmapService.this;
                bYDAutoEventValue8.intValue = amapService10.q.g;
                BYDAutoInstrumentDevice.getInstance(amapService10.w).set(new int[]{BYDAutoFeatureIds.INSTRUMENT_NAVI_TRIP_INFO_MILEAGE_SET}, bYDAutoEventValue8);
            }
            b.c.b.b.c.c("AmapService", "GuideInfo.preStringEtaArrivalTime :" + AmapService.this.q.x, new Object[0]);
            e eVar13 = AmapService.this.q;
            String str3 = eVar13.h;
            if (str3 != "-1" && !str3.equals(eVar13.x)) {
                e eVar14 = AmapService.this.q;
                String str4 = eVar14.h;
                eVar14.x = str4;
                int iC2 = c(str4);
                b.c.b.b.c.c("AmapService", "EtaArrivalTime : " + iC2, new Object[0]);
                if (iC2 == 0) {
                    b.c.b.b.c.c("AmapService", "ETA_TEXT: " + AmapService.this.J + " " + AmapService.this.K + "时 " + AmapService.this.L + "分 ", new Object[0]);
                    BYDAutoEventValue bYDAutoEventValue9 = new BYDAutoEventValue();
                    int[] iArr2 = new int[1];
                    AmapService amapService11 = AmapService.this;
                    int i15 = amapService11.J;
                    if (i15 > 0 && i15 < 11) {
                        bYDAutoEventValue9.intValue = i15;
                        iArr2[0] = BYDAutoFeatureIds.INSTRUMENT_EXPECTED_ARRIVE_DAY_SET;
                        BYDAutoInstrumentDevice.getInstance(amapService11.w).set(iArr2, bYDAutoEventValue9);
                    }
                    b.c.b.b.c.c("AmapService", "time_interval:" + AmapService.this.q.h, new Object[0]);
                    if (AmapService.this.q.h.contains("下午")) {
                        AmapService.this.t(4);
                    }
                    if (AmapService.this.q.h.contains("晚上")) {
                        AmapService.this.t(5);
                    }
                    if (AmapService.this.q.h.contains("上午")) {
                        AmapService.this.t(2);
                    }
                    if (AmapService.this.q.h.contains("中午")) {
                        AmapService.this.t(3);
                    }
                    if (AmapService.this.q.h.contains("凌晨")) {
                        AmapService.this.t(1);
                    }
                    AmapService amapService12 = AmapService.this;
                    bYDAutoEventValue9.intValue = amapService12.K;
                    iArr2[0] = BYDAutoFeatureIds.INSTRUMENT_EXPECTED_ARRIVE_HOUR_SET;
                    BYDAutoInstrumentDevice.getInstance(amapService12.w).set(iArr2, bYDAutoEventValue9);
                    AmapService amapService13 = AmapService.this;
                    bYDAutoEventValue9.intValue = amapService13.L;
                    iArr2[0] = BYDAutoFeatureIds.INSTRUMENT_EXPECTED_ARRIVE_MINUTE_SET;
                    BYDAutoInstrumentDevice.getInstance(amapService13.w).set(iArr2, bYDAutoEventValue9);
                    bYDAutoEventValue9.intValue = 0;
                    iArr2[0] = BYDAutoFeatureIds.INSTRUMENT_EXPECTED_ARRIVE_SECOND_SET;
                    BYDAutoInstrumentDevice.getInstance(AmapService.this.w).set(iArr2, bYDAutoEventValue9);
                }
            }
            b.c.b.b.c.c("AmapService", "GuideInfo.preNextToSegmentDist :" + AmapService.this.q.A, new Object[0]);
            e eVar15 = AmapService.this.q;
            int i16 = eVar15.o;
            if (i16 == -1 || i16 != eVar15.A) {
                b.c.b.b.c.c("AmapService", "nextToSegmentDist:" + AmapService.this.q.o + "米", new Object[0]);
                e eVar16 = AmapService.this.q;
                eVar16.A = eVar16.o;
                BYDAutoEventValue bYDAutoEventValue10 = new BYDAutoEventValue();
                int i17 = AmapService.this.q.o;
                bYDAutoEventValue10.intValue = i17;
                if (i17 == -1) {
                    bYDAutoEventValue10.intValue = 16777215;
                    b.c.b.b.c.c("AmapService", "0x00ffffff", new Object[0]);
                }
                BYDAutoInstrumentDevice.getInstance(AmapService.this.w).set(new int[]{BYDAutoFeatureIds.INSTRUMENT_DISTANCE_OF_TARGET_AHEAD_ADVANCED_SET}, bYDAutoEventValue10);
            }
            if (AmapService.g) {
                e eVar17 = AmapService.this.q;
                int i18 = eVar17.n;
                if (i18 != -1 && i18 != eVar17.B) {
                    eVar17.B = i18;
                    b.c.b.b.c.c("AmapService", "BAIDU nextNextTurnIcon:" + AmapService.this.q.n, new Object[0]);
                    e eVar18 = AmapService.this.q;
                    int i19 = eVar18.n;
                    if (i19 >= 0 && i19 < 107) {
                        eVar18.n = AmapService.o[i19];
                    }
                    b.c.b.b.c.c("AmapService", "BAIDU nextNextTurnIcon later:" + AmapService.this.q.n, new Object[0]);
                    BYDAutoEventValue bYDAutoEventValue11 = new BYDAutoEventValue();
                    AmapService amapService14 = AmapService.this;
                    bYDAutoEventValue11.intValue = amapService14.q.n;
                    BYDAutoInstrumentDevice.getInstance(amapService14.w).set(new int[]{BYDAutoFeatureIds.Instrument.INSTRUMENT_GUIDE_INFO_ADVANCED_ACTION_SET}, bYDAutoEventValue11);
                }
            } else {
                b.c.b.b.c.c("AmapService", "GAODE nextNextTurnIcon :{?} ; preNextNextTurnIcon :{?}" + AmapService.this.q.n, Integer.valueOf(AmapService.this.q.B));
                e eVar19 = AmapService.this.q;
                int i20 = eVar19.n;
                if (i20 != -1 && i20 >= 0 && i20 < 29) {
                    eVar19.B = i20;
                    int i21 = eVar19.D;
                    if (i21 <= 0 || i21 > 10) {
                        i3 = AmapService.m[i20];
                    } else {
                        if (11 == i20 || 12 == i20) {
                            i4 = i21 + 24;
                        } else if (17 == i20 || 18 == i20) {
                            i4 = i21 + 34;
                        } else {
                            i3 = AmapService.m[i20];
                        }
                        eVar19.n = i4;
                        b.c.b.b.c.c("AmapService", "GAODE nextNextTurnIcon:" + AmapService.this.q.n, new Object[0]);
                        BYDAutoEventValue bYDAutoEventValue12 = new BYDAutoEventValue();
                        AmapService amapService15 = AmapService.this;
                        bYDAutoEventValue12.intValue = amapService15.q.n;
                        BYDAutoInstrumentDevice.getInstance(amapService15.w).set(new int[]{BYDAutoFeatureIds.INSTRUMENT_NAVI_LEAD_MSG_ADVANCED_SET}, bYDAutoEventValue12);
                        BYDAutoInstrumentDevice.getInstance(AmapService.this.w).set(new int[]{BYDAutoFeatureIds.Instrument.INSTRUMENT_GUIDE_INFO_ADVANCED_ACTION_SET}, bYDAutoEventValue12);
                    }
                    eVar19.n = i3;
                    b.c.b.b.c.c("AmapService", "GAODE nextNextTurnIcon:" + AmapService.this.q.n, new Object[0]);
                    BYDAutoEventValue bYDAutoEventValue122 = new BYDAutoEventValue();
                    AmapService amapService152 = AmapService.this;
                    bYDAutoEventValue122.intValue = amapService152.q.n;
                    BYDAutoInstrumentDevice.getInstance(amapService152.w).set(new int[]{BYDAutoFeatureIds.INSTRUMENT_NAVI_LEAD_MSG_ADVANCED_SET}, bYDAutoEventValue122);
                    BYDAutoInstrumentDevice.getInstance(AmapService.this.w).set(new int[]{BYDAutoFeatureIds.Instrument.INSTRUMENT_GUIDE_INFO_ADVANCED_ACTION_SET}, bYDAutoEventValue122);
                }
            }
            b.c.b.b.c.c("AmapService", "********send to independent CAN  end********", new Object[0]);
        }

        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        /* JADX WARN: Removed duplicated region for block: B:16:0x00bc  */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void onReceive(android.content.Context r13, android.content.Intent r14) {
            /*
                Method dump skipped, instruction units count: 1486
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.example.amapservice.AmapService.c.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    public class d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public long f1451a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public long f1452b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public String f1453c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public String f1454d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public int f1455e;
        public boolean f;

        public d() {
        }

        public /* synthetic */ d(AmapService amapService, a aVar) {
            this();
        }

        public long a() {
            return this.f1451a;
        }

        public long b() {
            return this.f1452b;
        }

        public String c() {
            return this.f1453c;
        }

        public void d(long j) {
            this.f1451a = j;
        }

        public void e(long j) {
            this.f1452b = j;
        }

        public void f(String str) {
            this.f1453c = str;
        }

        public void g(String str) {
            this.f1454d = str;
        }

        public void h(int i) {
            this.f1455e = i;
        }

        public void i(boolean z) {
            this.f = z;
        }
    }

    public class e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public int f1456a = -1;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public String f1457b = "-1";

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public int f1458c = -1;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public String f1459d = "-1";

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public int f1460e = -1;
        public int f = -1;
        public int g = -1;
        public String h = "-1";
        public String i = "-1";
        public String j = "-1";
        public String k = "-1";
        public String l = "-1";
        public String m = "-1";
        public int n = -1;
        public int o = -1;
        public String p = "-1";
        public int q = -1;
        public String r = "-1";
        public int s = -1;
        public int t = -1;
        public int u = -1;
        public int v = -1;
        public int w = -1;
        public String x = "-1";
        public String y = "-1";
        public String z = "-1";
        public int A = -1;
        public int B = -1;
        public int C = -1;
        public int D = -1;

        public e() {
        }
    }

    public static boolean q(double d2, double d3, double d4) {
        return d2 < d3 || d2 > d4;
    }

    @SuppressLint({"WrongConstant"})
    public List<d> l() {
        b.c.b.b.c.c("AmapService", "getRunningServiceInfo()", new Object[0]);
        ArrayList arrayList = new ArrayList();
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) getSystemService(ActivityChooserModel.ATTRIBUTE_ACTIVITY)).getRunningServices(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION)) {
            d dVar = new d(this, null);
            dVar.g(runningServiceInfo.service.toString());
            dVar.h(runningServiceInfo.pid);
            dVar.f(runningServiceInfo.process);
            dVar.i(runningServiceInfo.started);
            dVar.d(runningServiceInfo.activeSince);
            dVar.e(runningServiceInfo.lastActivityTime);
            arrayList.add(dVar);
        }
        return arrayList;
    }

    public String m(String str) {
        String str2;
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            str2 = (String) cls.getMethod("get", String.class).invoke(cls, str);
        } catch (Throwable th) {
            th.printStackTrace();
            b.c.b.b.c.b("AmapService", "getSystemPropertiesByName is error:", th, new Object[0]);
            str2 = BootBusinessManager.SUB_BUSINESS_NULL;
        }
        b.c.b.b.c.c("AmapService", "properties:" + str2, new Object[0]);
        return str2;
    }

    public boolean n(String str) {
        b.c.b.b.c.c("AmapService", "str :" + str, new Object[0]);
        for (d dVar : l()) {
            if (dVar.c() != null && dVar.c().equals(str) && Math.abs((dVar.a() - dVar.b()) - (SystemClock.elapsedRealtime() - SystemClock.uptimeMillis())) < 100) {
                b.c.b.b.c.c("AmapService", "isServiceRestart", new Object[0]);
                return true;
            }
        }
        return false;
    }

    public boolean o(int i2) {
        return i2 < -60 || i2 > 60;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override // android.app.Service
    @SuppressLint({"WrongConstant"})
    public void onCreate() {
        int i2;
        w();
        this.p = f1445c.equals(m("ro.vendor.build.type"));
        this.D = m("ro.product.name");
        b.c.b.b.c.c("AmapService", "onCreate isDebug=" + this.p + "  , mProductName=" + this.D, new Object[0]);
        try {
            this.N = this.D.equals("DiLink5.0") ? (AutoContainerManager) getSystemService("auto_container") : (AutoContainerManager) getSystemService("AutoContainer");
        } catch (RuntimeException e2) {
            b.c.b.b.c.b("AmapService", "RuntimeException  :", e2, new Object[0]);
        }
        this.A = new c(this, null);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("AUTONAVI_STANDARD_BROADCAST_SEND");
        intentFilter.addAction("DEBUG_CASE");
        intentFilter.addAction("byd.intent.action.KILL_BydAutoMap");
        intentFilter.addAction("byd.intent.action.ACC_OFF");
        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        registerReceiver(this.A, intentFilter);
        this.C = m("ro.build.system.fission_single_os");
        this.C = "1";
        this.t = BYDAutoInstrumentDevice.getInstance(this);
        this.v = BYDAutoSettingDevice.getInstance(this);
        this.x = new BYDAutoEventValue();
        this.r = new FlatBufferBuilder(0);
        this.q = new e();
        if (this.D.equals("DiLink5.0")) {
            b.c.b.b.c.c("AmapService", "mProductName=DiLink5.0 LONGITUDE_ALTITUDE", new Object[0]);
            BYDAutoSensorDevice bYDAutoSensorDevice = BYDAutoSensorDevice.getInstance(this);
            this.u = bYDAutoSensorDevice;
            int slope = bYDAutoSensorDevice.getSlope();
            this.B = 30;
            if (o(slope)) {
                i2 = -1000;
            } else {
                double degrees = Math.toDegrees(Math.atan(((double) this.B) / 100.0d));
                b.c.b.b.c.c("AmapService", "degree=" + degrees, new Object[0]);
                i2 = (int) degrees;
            }
            b.c.b.b.c.c("AmapService", "mSlopeValue= " + this.B + ", degreeValue= " + i2, new Object[0]);
        }
        super.onCreate();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.A);
        this.D.equals("DiLink5.0");
        this.A = null;
        k = false;
        b.c.b.b.c.c("AmapService", "AmapService onDestroy", new Object[0]);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i2, int i3) {
        if (k) {
            b.c.b.b.c.c("AmapService", "BYD AmapService has started", new Object[0]);
            return super.onStartCommand(intent, i2, i3);
        }
        k = true;
        b.c.b.b.c.c("AmapService", "BYD AmapService start", new Object[0]);
        if (n("com.byd.amapservice")) {
            b.c.b.b.c.c("AmapService", "isServiceRestart: true", new Object[0]);
            u(BYDAutoFeatureIds.SET_NAVI_SCREEN_STATUS_SET, 3);
            v(BYDAutoFeatureIds.INSTRUMENT_SEND_NAVI_STATUS_SET, 4);
            this.q.f1456a = 9;
        } else {
            b.c.b.b.c.c("AmapService", "isServiceRestart false", new Object[0]);
        }
        return super.onStartCommand(intent, i2, i3);
    }

    public final boolean p() {
        b.c.b.b.c.c("AmapService", "isValidNegativeRange mLongitude is " + this.z + " mLatitude is " + this.y, new Object[0]);
        double d2 = this.z;
        if (d2 > 88.7d && d2 < 89.91666667d) {
            double d3 = this.y;
            if (d3 > 42.48333333d && d3 < 42.91666667d) {
                b.c.b.b.c.e("AmapService", "Longitude is in [88.7,89.91666667] and Latitude is in [42.48333333,42.91666667]", new Object[0]);
                return true;
            }
        }
        return false;
    }

    public void r() {
        b.c.b.b.c.c("AmapService", "reSetGuideInfo()", new Object[0]);
        e eVar = this.q;
        eVar.f1457b = "-1";
        eVar.f1458c = -1;
        eVar.f1459d = "-1";
        eVar.f1460e = -1;
        eVar.f = -1;
        eVar.g = -1;
        eVar.h = "-1";
        eVar.i = "-1";
        eVar.j = "-1";
        eVar.k = "-1";
        eVar.l = "-1";
        eVar.m = "-1";
        eVar.n = -1;
        eVar.o = -1;
        eVar.p = "-1";
        eVar.r = "-1";
        eVar.s = -1;
        eVar.t = -1;
        eVar.u = -1;
        eVar.v = -1;
        eVar.w = -1;
        eVar.x = "-1";
        eVar.z = "-1";
        eVar.A = -1;
        eVar.B = -1;
        eVar.C = -1;
        eVar.D = -1;
    }

    public void s(int i2, int i3, String str) {
        AutoContainerManager autoContainerManager = this.N;
        if (autoContainerManager == null) {
            b.c.b.b.c.c("AmapService", "mAutoContainerManager is null", new Object[0]);
            return;
        }
        try {
            b.c.b.b.c.c("AmapService", "sendInfo  ", new Object[0]);
            autoContainerManager.sendInfo(i2, i3, str);
        } catch (RuntimeException unused) {
            b.c.b.b.c.c("AmapService", "sendInfo error !!!!!", new Object[0]);
        }
    }

    public final void t(int i2) {
        b.c.b.b.c.c("AmapService", "sendTimeInterval: " + i2, new Object[0]);
        BYDAutoEventValue bYDAutoEventValue = new BYDAutoEventValue();
        bYDAutoEventValue.intValue = i2;
        BYDAutoStatisticDevice.getInstance(this.w).set(new int[]{BYDAutoFeatureIds.Statistic.STATISTICS_MAP_PERIOD_INFOR}, bYDAutoEventValue);
    }

    public final void u(int i2, int i3) {
        b.c.b.b.c.c("AmapService", "setNaviScreenStatus: " + i3, new Object[0]);
        BYDAutoEventValue bYDAutoEventValue = this.x;
        bYDAutoEventValue.intValue = i3;
        this.v.set(new int[]{i2}, bYDAutoEventValue);
    }

    public void v(int i2, int i3) {
        b.c.b.b.c.c("AmapService", "setNaviStatus: " + i3, new Object[0]);
        BYDAutoEventValue bYDAutoEventValue = this.x;
        bYDAutoEventValue.intValue = i3;
        this.t.set(new int[]{i2}, bYDAutoEventValue);
    }

    public final void w() {
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 26) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "定制版置服务", 4));
        }
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        if (i2 >= 26) {
            builder.setChannelId("default");
        }
        startForeground(2, builder.setSmallIcon(R$mipmap.ic_launcher).setContentTitle("AmapService").setContentText("AmapService Text").build());
    }

    public void x() {
        b.c.b.b.c.c("AmapService", "updateAltitude  ", new Object[0]);
        double d2 = this.s;
        if (f1444b) {
            b.c.b.b.c.c("AmapService", "altitude is " + d2, new Object[0]);
        }
        if (q(d2, -999.0d, 8000.0d)) {
            b.c.b.b.c.e("AmapService", "the altitude is out of [-999,8000]", new Object[0]);
        } else {
            if (d2 <= -10.0d || d2 >= 0.0d) {
                int i2 = (d2 >= 0.0d || p()) ? (int) d2 : (int) (-d2);
                b.c.b.b.c.c("AmapService", "curAltitude =  " + i2, new Object[0]);
                s(7, i2, ExternalServices.ERR_BLANK);
                return;
            }
            b.c.b.b.c.e("AmapService", "the altitude is in of [-10,0]", new Object[0]);
        }
        s(7, -1000, ExternalServices.ERR_BLANK);
    }
}
