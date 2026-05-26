package com.autosdk.protocol.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.SparseArray;
import b.a.a.b.a;
import b.a.a.b.c;
import b.b.a.a.a.b;
import b.b.a.a.d.c0;
import b.b.a.a.d.j0;
import b.b.a.a.d.r;
import b.b.a.a.d.x;
import b.c.b.b.f;
import b.c.b.b.g;
import b.c.b.b.i;
import b.c.b.b.j;
import com.autosdk.protocol.AutoProtocolManager;
import com.autosdk.protocol.IProtocolAidlInterface;
import com.autosdk.protocol.constant.ProtocolID;
import com.autosdk.protocol.constant.ProtocolResultCode;
import com.autosdk.protocol.listener.IProtocolCallback;
import com.autosdk.protocol.model.base.ProtocolBaseModel;
import com.baidu.iovsdk.common.account.ExternalServices;
import com.baidu.naviauto.imaplbs.bean.AlongRouteSearchPoi;
import com.baidu.naviauto.imaplbs.bean.AlongRouteSearchResult;
import com.baidu.naviauto.imaplbs.bean.AreaSearchPoi;
import com.baidu.naviauto.imaplbs.bean.BaseSearchPoi;
import com.baidu.naviauto.imaplbs.bean.GeoPoint;
import com.baidu.naviauto.imaplbs.bean.Location;
import com.baidu.naviauto.imaplbs.bean.MapAutoResult;
import com.baidu.naviauto.imaplbs.bean.NearbySearchPoi;
import com.baidu.naviauto.imaplbs.bean.ReverseGeocodingResult;
import com.baidu.naviauto.imaplbs.bean.RoutePosition;
import com.baidu.naviauto.imaplbs.bean.TurnInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ProtocolService extends Service {
    private static final String BLANK = " ";
    private static final String BYD_AUTO_VOICE = "com.byd.autovoice";
    private static final String COLON = "：";
    private static final String KEY_ALONG_POIS = "alongPois";
    private static final String KEY_RESULT_POIS = "resultPois";
    private static final String KEY_VIA_POIS = "viaPois";
    private static final int NAVI_CANCEL_NAVI = 0;
    private static final int NAVI_CAR_MODE_DIRECTION_ACTION = 5;
    private static final int NAVI_COMPANY_ACTION = 1;
    private static final int NAVI_DIRECT_QUERY = 1;
    private static final int NAVI_HOME_ACTION = 0;
    private static final int NAVI_NIGHT_MODE_ACTION = 6;
    private static final int NAVI_SET_PERFER = 8;
    private static final int NAVI_SET_PREVIEW = 1;
    private static final int NAVI_SET_TTS_PLAY_PRARM = 7;
    private static final String PROTOCOL_TTS_ALONG_SEARCH_STRING = "已为您找到如下沿途的";
    private static final String PROTOCOL_TTS_CAR_MODE_2DCARFRONT = "已切换为2D车头朝上";
    private static final String PROTOCOL_TTS_CAR_MODE_2DNORTH = "已切换为2D正北朝上";
    private static final String PROTOCOL_TTS_CAR_MODE_3DCARFRONT = "已切换为3D车头朝上";
    private static final String PROTOCOL_TTS_CLOSE_SETTINGS = "好的，已关闭";
    private static final String PROTOCOL_TTS_COLLECTION = "好的，地址已收藏";
    private static final String PROTOCOL_TTS_EXIT_PREVIEW = "已为您退出全览";
    private static final String PROTOCOL_TTS_MAP_OP_ROAD_CONDITION_OFF = "已为您关闭路况";
    private static final String PROTOCOL_TTS_MAP_OP_ROAD_CONDITION_ON = "已为您打开路况";
    private static final String PROTOCOL_TTS_MAP_OP_TTS_MODE_DETAIL = "已使用详细播报模式";
    private static final String PROTOCOL_TTS_MAP_OP_TTS_MODE_SIMPLE = "已使用简洁播报模式";
    private static final String PROTOCOL_TTS_NAVI_CANCEL = "即将退出导航，下次再见";
    private static final String PROTOCOL_TTS_OK = "好的";
    private static final String PROTOCOL_TTS_OPENED_MAP = "地图已打开";
    private static final String PROTOCOL_TTS_OPEN_SETTINGS = "好的，已打开";
    private static final String PROTOCOL_TTS_ROUTE_PERFER_CHANGE_SUCCESS = "好的";
    private static final String PROTOCOL_TTS_SEARCH_NO_RESULT = "未搜索到结果";
    private static final String PROTOCOL_TTS_SHOW_PREVIEW = "已为您展示全览";
    private static final String PROTOCOL_TTS_VIA_PASS_DEL_SUCCESS = "好的，已删除途径点";
    private static final String PROTOCOL_TTS_VOLUME_MAX = "当前音量已经最大";
    private static final String PROTOCOL_TTS_VOLUME_MIN = "当前音量已经最小";
    private static final String PROTOCOL_TTS_ZOOM_IN = "已为您放大地图";
    private static final String PROTOCOL_TTS_ZOOM_OUT = "已为您缩小地图";
    private static final int SELECT_ROUTE_CANCEL_ACTION = 0;
    private static final int SELECT_ROUTE_JUST_NAVI_ACTION = 3;
    private static final int SELECT_ROUTE_NAVI_ACTION = 1;
    private static final int SET_MAP_ZOOM = 1;
    private static final int SET_ROAD_CONDITION = 2;
    private static final String SPLIT = ";";
    private static final int STATUS_QUERY_APP_FORGROUND = 2;
    private static final int STATUS_QUERY_NAVI_ACTION = 1;
    private static final int STATUS_QUERY_ROUTE_ACTION = 0;
    private IProtocolCallback activeCallback;
    public int errResultCode;
    private ArrayList<AlongRouteSearchPoi> mAlongRouteSearchPois;
    public String naviInfoStr;
    public ProtocolBaseModel protocolModel;
    public String volumeStr;
    public boolean zoomResult;
    private static final String TAG = ProtocolService.class.getSimpleName();
    public static final String MAP_PACKAGE_NAME = i.b().getPackageName();
    public static int STREAM_NAVI = 15;
    private final SparseArray<IProtocolCallback> mDiTrainerCallbackArray = new SparseArray<>();
    private String mCurrentPosition = ExternalServices.ERR_BLANK;
    private String mCurrentAddress = ExternalServices.ERR_BLANK;
    private int mRoutePlanPref = 99;
    private String callerPackageName = ExternalServices.ERR_BLANK;
    private int mInstrumentNaviType = 0;
    private int mSearchResultCount = 0;
    public MapAutoResult<Void> result = null;
    private int mActionHCType = Integer.MIN_VALUE;
    public c serviceCallBack = new c() { // from class: com.autosdk.protocol.service.ProtocolService.1
        public void activeCallBack(int i) {
            b.c.b.b.c.a(ProtocolService.TAG, "activeCallBack:" + i, new Object[0]);
        }

        public void giveACallBack(boolean z, int i) {
            b.c.b.b.c.a(ProtocolService.TAG, "giveACallBack:" + z + "," + i, new Object[0]);
            ProtocolService.this.callback(z, i);
        }

        @Override // b.a.a.b.c
        public void updateActionDis(int i) {
            IProtocolCallback iProtocolCallback = (IProtocolCallback) ProtocolService.this.mDiTrainerCallbackArray.get(ProtocolID.PROTOCOL_REGISTER_ACTION_DIS);
            if (iProtocolCallback != null) {
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("actiondis", i);
                    iProtocolCallback.onJSONResult(jSONObject.toString());
                } catch (RemoteException | JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }

        @Override // b.a.a.b.c
        public void updateNaviStatus(int i) {
            IProtocolCallback iProtocolCallback = (IProtocolCallback) ProtocolService.this.mDiTrainerCallbackArray.get(ProtocolID.PROTOCOL_REGISTER_NAVI_STATUS);
            if (iProtocolCallback != null) {
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("navistatus", i);
                    iProtocolCallback.onJSONResult(jSONObject.toString());
                } catch (RemoteException | JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }

        @Override // b.a.a.b.c
        public void updateRestCardStatus(int i) {
            IProtocolCallback iProtocolCallback = (IProtocolCallback) ProtocolService.this.mDiTrainerCallbackArray.get(ProtocolID.PROTOCOL_SHOW_REST_CARD_AND_REGISTER_CALLBACK);
            if (iProtocolCallback != null) {
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("resultCode", i);
                    jSONObject.put("resultMsg", ProtocolResultCode.RESULT_MESSAGE.get(Integer.valueOf(i)));
                    iProtocolCallback.onJSONResult(jSONObject.toString());
                } catch (RemoteException | JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }

        @Override // b.a.a.b.c
        public void updateRoadSpeedLimit(int i) {
            IProtocolCallback iProtocolCallback = (IProtocolCallback) ProtocolService.this.mDiTrainerCallbackArray.get(ProtocolID.PROTOCOL_REGISTER_ROAD_SPEED_LIMIT);
            if (iProtocolCallback != null) {
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("roadlimit", i);
                    iProtocolCallback.onJSONResult(jSONObject.toString());
                } catch (RemoteException | JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }

        @Override // b.a.a.b.c
        public void updateRoadType(int i) {
            IProtocolCallback iProtocolCallback = (IProtocolCallback) ProtocolService.this.mDiTrainerCallbackArray.get(ProtocolID.PROTOCOL_REGISTER_ROAD_TYPE);
            if (iProtocolCallback != null) {
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("roadtype", i);
                    iProtocolCallback.onJSONResult(jSONObject.toString());
                } catch (RemoteException | JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }

        @Override // b.a.a.b.c
        public void updateTbtInfo(int i) {
            IProtocolCallback iProtocolCallback = (IProtocolCallback) ProtocolService.this.mDiTrainerCallbackArray.get(ProtocolID.PROTOCOL_REGISTER_TBT_INFO);
            if (iProtocolCallback != null) {
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("tbtinfo", i);
                    iProtocolCallback.onJSONResult(jSONObject.toString());
                } catch (RemoteException | JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }
    };
    private final IProtocolAidlInterface.Stub mProtocolServiceManager = new AnonymousClass2();
    private BroadcastReceiver mAutoVoiceReceiver = new BroadcastReceiver() { // from class: com.autosdk.protocol.service.ProtocolService.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            b.c.b.b.c.c(ProtocolService.TAG, "action:{?}, state:{?}", intent.getAction(), Integer.valueOf(intent.getIntExtra("autovoice_state", -1)));
            if (intent.getIntExtra("autovoice_state", -1) != 0 && intent.getIntExtra("autovoice_state", -1) == 1) {
                ProtocolService.this.mActionHCType = Integer.MIN_VALUE;
            }
        }
    };
    private j0 mOnTurnInfoListener = new j0() { // from class: com.autosdk.protocol.service.ProtocolService.5
        @Override // b.b.a.a.d.j0
        public void onTurnInfoUpdated(List<TurnInfo> list) {
            if (list == null || list.size() <= 0) {
                return;
            }
            b.c.b.b.c.a(ProtocolService.TAG, "mOnTurnInfoListener TBTInfo isStraight: {?}, 前方{?}米{?}", Boolean.valueOf(list.get(0).isStraight()), Integer.valueOf(list.get(0).getRemainDistance()), list.get(0).getDirectionName());
        }
    };
    private final RemoteCallbackList<IProtocolCallback> mListenerList = new RemoteCallbackList<>();

    /* JADX INFO: renamed from: com.autosdk.protocol.service.ProtocolService$2, reason: invalid class name */
    public class AnonymousClass2 extends IProtocolAidlInterface.Stub {
        public AnonymousClass2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: lambda$setProtocolModelData$0, reason: merged with bridge method [inline-methods] */
        public /* synthetic */ void a() {
            ProtocolService protocolService = ProtocolService.this;
            protocolService.doOperate(protocolService.protocolModel.getProtocolID());
        }

        @Override // com.autosdk.protocol.IProtocolAidlInterface.Stub, android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            ProtocolService.this.callerPackageName = i.a(Binder.getCallingPid());
            b.c.b.b.c.c(ProtocolService.TAG, "callerPackageName: " + ProtocolService.this.callerPackageName, new Object[0]);
            return super.onTransact(i, parcel, parcel2, i2);
        }

        @Override // com.autosdk.protocol.IProtocolAidlInterface
        public void registCallBack(IProtocolCallback iProtocolCallback) {
            b.c.b.b.c.a(ProtocolService.TAG, "registCallBack::" + ProtocolService.this.mListenerList.getRegisteredCallbackCount(), new Object[0]);
            if (ProtocolService.this.mListenerList.getRegisteredCallbackCount() > 0) {
                for (int i = 0; i < ProtocolService.this.mListenerList.getRegisteredCallbackCount(); i++) {
                    ProtocolService.this.mListenerList.unregister((IProtocolCallback) ProtocolService.this.mListenerList.getRegisteredCallbackItem(i));
                }
            }
            if (ProtocolService.this.activeCallback == null) {
                ProtocolService.this.setActiveCallback(iProtocolCallback);
            }
            ProtocolService.this.activeCallback = iProtocolCallback;
            ProtocolService.this.mListenerList.register(iProtocolCallback);
        }

        @Override // com.autosdk.protocol.IProtocolAidlInterface
        public void registerCallBack(IProtocolCallback iProtocolCallback, int i) {
            b.c.b.b.c.a(ProtocolService.TAG, "registerCallBack iProtocolCallback: " + iProtocolCallback + " id: " + i, new Object[0]);
            ProtocolService.this.mDiTrainerCallbackArray.put(i, iProtocolCallback);
            iProtocolCallback.onSuccess("注册成功");
            ProtocolService.this.callbackImmediate(i);
        }

        @Override // com.autosdk.protocol.IProtocolAidlInterface
        public void setProtocolModelData(ProtocolBaseModel protocolBaseModel) {
            b.c.b.b.c.a(ProtocolService.TAG, "protocolModel:{?}, BaiduMap:{?}, GaodeMap:{?}", protocolBaseModel, Boolean.valueOf(ProtocolService.this.isBaiduMapInstalled()), Boolean.valueOf(ProtocolService.this.isGaodeMapInstalled()));
            if (protocolBaseModel != null) {
                b.c.b.b.c.c(ProtocolService.TAG, "setProtocolModelData::" + protocolBaseModel.getProtocolID(), new Object[0]);
                if (ProtocolService.this.mActionHCType != Integer.MIN_VALUE && protocolBaseModel.getProtocolID() != 34008) {
                    ProtocolService.this.mActionHCType = Integer.MIN_VALUE;
                }
                if (ProtocolService.this.mAlongRouteSearchPois != null && !ProtocolService.this.mAlongRouteSearchPois.isEmpty() && protocolBaseModel.getProtocolID() != 31003) {
                    ProtocolService.this.mAlongRouteSearchPois.clear();
                    ProtocolService.this.mAlongRouteSearchPois = null;
                }
                ProtocolService protocolService = ProtocolService.this;
                protocolService.protocolModel = protocolBaseModel;
                if (!protocolService.isBaiduMapInstalled()) {
                    if (ProtocolService.this.isGaodeMapInstalled() && AutoProtocolManager.getInstance(ProtocolService.this).isServiceConnected()) {
                        AutoProtocolManager.getInstance(ProtocolService.this).getAPI().performMapOperation(protocolBaseModel, ProtocolService.this.activeCallback);
                        return;
                    }
                    return;
                }
                if (i.d()) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: b.a.b.a.a
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f1141b.a();
                        }
                    });
                    return;
                }
                ProtocolService protocolService2 = ProtocolService.this;
                protocolService2.errResultCode = ProtocolResultCode.RESULT_APP_NOT_PERMIT;
                protocolService2.callback(false, ProtocolID.PROTOCOL_PERMIT_STATUS);
            }
        }
    }

    public ProtocolService() {
        a.i().u(this.serviceCallBack);
    }

    private void addMarker(List<BaseSearchPoi> list) {
        b.R().d(list);
    }

    private void addToCollection(ProtocolBaseModel protocolBaseModel) {
        int i;
        if (isNetworkConnected()) {
            String searchKey = protocolBaseModel.getSearchKey();
            String destPoiName = protocolBaseModel.getDestPoiName();
            String destLatitude = protocolBaseModel.getDestLatitude();
            String destLongitude = protocolBaseModel.getDestLongitude();
            int actionType = protocolBaseModel.getActionType();
            String str = TAG;
            b.c.b.b.c.a(str, "addToCollection poiName:" + searchKey + ", actionType:" + actionType, new Object[0]);
            StringBuilder sb = new StringBuilder();
            sb.append("destPoiName: {?} - {?} - {?}");
            sb.append(destPoiName);
            b.c.b.b.c.a(str, sb.toString(), destLongitude, destLatitude);
            backToMap();
            if (actionType == 0) {
                final Location data = b.R().P().getData();
                b.R().N0(data.getPoint(), new c0() { // from class: com.autosdk.protocol.service.ProtocolService.12
                    @Override // b.b.a.a.d.c0
                    public void onReverseGeoSearchResult(int i2, ReverseGeocodingResult reverseGeocodingResult) {
                        b.c.b.b.c.a(ProtocolService.TAG, "addToCollection - {?}", reverseGeocodingResult.toString());
                        BaseSearchPoi baseSearchPoi = new BaseSearchPoi();
                        baseSearchPoi.setLocation(data.getPoint());
                        baseSearchPoi.setName(reverseGeocodingResult.getAddress());
                        ProtocolService.this.callback(b.R().b(baseSearchPoi).getErrorCode() == 0);
                    }
                });
                return;
            } else {
                if (actionType != 1) {
                    b.a.a.a.b().c().h(searchKey, new x() { // from class: com.autosdk.protocol.service.ProtocolService.13
                        @Override // b.b.a.a.d.x
                        public void onNearbySearchResult(int i2, List<NearbySearchPoi> list) {
                            boolean zA = false;
                            if (list != null && !list.isEmpty()) {
                                zA = b.a.a.a.b().c().a(list.get(0));
                            }
                            ProtocolService.this.callback(zA);
                        }
                    });
                    return;
                }
                if (b.R().i0().getData().booleanValue()) {
                    RoutePosition data2 = b.R().T().getData();
                    BaseSearchPoi baseSearchPoi = new BaseSearchPoi();
                    baseSearchPoi.setLocation(data2.getPosition());
                    baseSearchPoi.setName(data2.getName());
                    callback(b.R().b(baseSearchPoi).getErrorCode() == 0);
                    return;
                }
                i = ProtocolResultCode.RESULT_NO_LAUNCH_UNSUPPORT;
            }
        } else {
            i = ProtocolResultCode.RESULT_NO_NETWORK_NO_DATA;
        }
        this.errResultCode = i;
        callback(false);
    }

    private void addToCollection(String str, String str2, String str3, int i) {
        b.c.b.b.c.a(TAG, "addToCollection type:" + i, new Object[0]);
    }

    private void addViaPoi(String str, String str2, String str3) {
        b.c.b.b.c.a(TAG, "addViaPoi", new Object[0]);
    }

    private String addVolume() {
        int streamVolume = -1;
        try {
            streamVolume = ((AudioManager) getApplication().getSystemService("audio")).getStreamVolume(STREAM_NAVI);
            b.c.b.b.c.a(TAG, "getVolume = " + streamVolume, new Object[0]);
        } catch (Exception e2) {
            b.c.b.b.c.a(TAG, "getVolume = " + e2.getMessage(), new Object[0]);
        }
        if (streamVolume == 10) {
            return PROTOCOL_TTS_VOLUME_MAX;
        }
        setVolume(streamVolume + 1);
        return "好的";
    }

    private void alongSearch(int i) {
        if (!b.a.a.a.b().d().q()) {
            this.errResultCode = ProtocolResultCode.RESULT_NO_LAUNCH_UNSUPPORT;
            callback(false);
            return;
        }
        b.c.b.b.c.c(TAG, "alongSearch:" + i, new Object[0]);
        String alongWaySearchType = getAlongWaySearchType(i);
        if (g.c(alongWaySearchType)) {
            this.result = b.R().l0(alongWaySearchType);
            b.R().H0(alongWaySearchType, ExternalServices.ERR_BLANK, new b.b.a.a.d.c() { // from class: com.autosdk.protocol.service.ProtocolService.14
                @Override // b.b.a.a.d.c
                public void onAlongRouteSearchResult(int i2, AlongRouteSearchResult alongRouteSearchResult) {
                    boolean z = false;
                    b.c.b.b.c.c(ProtocolService.TAG, "onAlongRouteSearchResult : {?} --- {?}", Integer.valueOf(i2), alongRouteSearchResult);
                    if (alongRouteSearchResult != null) {
                        b.c.b.b.c.c(ProtocolService.TAG, "onAlongRouteSearchResult : {?} --- {?} --- {?}", Integer.valueOf(i2), Integer.valueOf(alongRouteSearchResult.getPois().size()), Integer.valueOf(alongRouteSearchResult.getTotalCount()));
                        ProtocolService.this.mSearchResultCount = alongRouteSearchResult.getTotalCount();
                        ProtocolService.this.mAlongRouteSearchPois = alongRouteSearchResult.getPois();
                    }
                    MapAutoResult<Void> mapAutoResult = ProtocolService.this.result;
                    if (mapAutoResult == null || mapAutoResult.getErrorCode() != 0) {
                        ProtocolService.this.errResultCode = ProtocolResultCode.RESULT_NO_FOUND;
                    }
                    ProtocolService protocolService = ProtocolService.this;
                    MapAutoResult<Void> mapAutoResult2 = protocolService.result;
                    if (mapAutoResult2 != null && mapAutoResult2.getErrorCode() == 0) {
                        z = true;
                    }
                    protocolService.callback(z);
                    ProtocolService.this.result = null;
                }
            });
        } else {
            this.errResultCode = ProtocolResultCode.RESULT_UNSUPPORT;
            callback(false);
        }
    }

    private void avoidRoadName(String str) {
        b.R().K(0, str);
    }

    private void backToBackground() {
        if (i.d()) {
            b.R().F();
        }
    }

    private boolean backToMap() {
        String str = TAG;
        b.c.b.b.c.a(str, "open BaiduMap, isInit :{?}, isMapFront : {?}", Boolean.valueOf(b.a.a.a.b().f()), b.R().j0().getData());
        if (!b.a.a.a.b().f()) {
            b.c.b.b.c.a(str, "openMap failure : MapAutoService is not init", new Object[0]);
        } else {
            if (!b.R().j0().getData().booleanValue()) {
                MapAutoResult<Void> mapAutoResultO0 = b.R().o0();
                b.c.b.b.c.a(str, "backToMap code:{?},msg:{?}", Integer.valueOf(mapAutoResultO0.getErrorCode()), mapAutoResultO0.getErrorMessage());
                return mapAutoResultO0.getErrorCode() == 0;
            }
            b.c.b.b.c.a(str, "BaiduMap is foreground!!!", new Object[0]);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:217)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:68)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.addCases(SwitchRegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:71)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:125)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public void callback(boolean r25) {
        /*
            Method dump skipped, instruction units count: 966
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.autosdk.protocol.service.ProtocolService.callback(boolean):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callback(boolean z, int i) {
        ProtocolBaseModel protocolBaseModel = this.protocolModel;
        if (protocolBaseModel == null) {
            b.c.b.b.c.a(TAG, "need no callback, finish.", new Object[0]);
            return;
        }
        if (i != 31004 || protocolBaseModel.getProtocolID() == 31004) {
            this.protocolModel.setProtocolID(i);
        }
        if (i == 31008 && !z) {
            this.errResultCode = ProtocolResultCode.RESULT_DEL_MIDDLE_POINT_MULTIPLE;
        }
        callback(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callbackImmediate(int i) {
        c cVar;
        int i2;
        int iM;
        int iJ;
        if (i == 50001) {
            if (a.i().o()) {
                return;
            }
            this.serviceCallBack.updateRestCardStatus(ProtocolResultCode.RESULT_DMS_REST_NOT_SHOW_NO_NAVI);
        }
        switch (i) {
            case ProtocolID.PROTOCOL_REGISTER_ROAD_TYPE /* 32001 */:
                int iK = a.i().k();
                if (iK != -1) {
                    this.serviceCallBack.updateRoadType(iK);
                }
                break;
            case ProtocolID.PROTOCOL_REGISTER_ROAD_SPEED_LIMIT /* 32002 */:
                int iL = a.i().l();
                if (iL != -1) {
                    this.serviceCallBack.updateRoadSpeedLimit(iL);
                }
                break;
            case ProtocolID.PROTOCOL_REGISTER_NAVI_STATUS /* 32003 */:
                if (a.i().n()) {
                    cVar = this.serviceCallBack;
                    i2 = 2;
                } else if (a.i().o()) {
                    cVar = this.serviceCallBack;
                    i2 = 1;
                } else {
                    cVar = this.serviceCallBack;
                    i2 = 0;
                }
                cVar.updateNaviStatus(i2);
                break;
            case ProtocolID.PROTOCOL_REGISTER_TBT_INFO /* 32004 */:
                if (a.i().o() && (iM = a.i().m()) != -1) {
                    this.serviceCallBack.updateTbtInfo(iM);
                    break;
                }
                break;
            case ProtocolID.PROTOCOL_REGISTER_ACTION_DIS /* 32005 */:
                if (a.i().o() && (iJ = a.i().j()) != -1) {
                    this.serviceCallBack.updateActionDis(iJ);
                    break;
                }
                break;
        }
    }

    private boolean cancelNavi() {
        return false;
    }

    private void changeMapShowMode() {
        b.R().H();
    }

    private void changeNaviType(ProtocolBaseModel protocolBaseModel) {
        if (protocolBaseModel.getActionType() == 1) {
            f.a().c(protocolBaseModel.getOperaType());
        } else if (protocolBaseModel.getActionType() != 2) {
            return;
        } else {
            this.mInstrumentNaviType = f.a().b();
        }
        callback(true);
    }

    private boolean checkAndSavePrefer(int i) {
        return false;
    }

    private void checkHomeOrCompanySetted(int i) {
        backToMap();
        BaseSearchPoi baseSearchPoiF = b.a.a.a.b().d().f(i);
        callback((baseSearchPoiF == null || baseSearchPoiF.getLocation() == null || baseSearchPoiF.getLocation().getLat() <= 0.0d) ? false : true);
    }

    private void clearAllSerachFragment() {
    }

    private boolean closePage(int i) {
        b.c.b.b.c.a(TAG, "closePage", new Object[0]);
        if (!isBaiduMapInstalled()) {
            return true;
        }
        this.errResultCode = ProtocolResultCode.RESULT_UNSUPPORT;
        return false;
    }

    private boolean continueNaviOpera(int i) {
        int i2;
        String str = TAG;
        b.c.b.b.c.a(str, "continueNaviOpera:" + i, new Object[0]);
        if (b.a.a.a.b().d().q()) {
            MapAutoResult<Void> mapAutoResultI = b.R().I();
            b.c.b.b.c.a(str, "continueNaviOpera ErrorCode : {?}", Integer.valueOf(mapAutoResultI.getErrorCode()));
            if (mapAutoResultI.getErrorCode() == 0) {
                return mapAutoResultI.getErrorCode() == 0;
            }
            i2 = ProtocolResultCode.RESULT_UNSUPPORT;
        } else {
            i2 = ProtocolResultCode.RESULT_UNSUPPORT_VIEW;
        }
        this.errResultCode = i2;
        return false;
    }

    private void continueNaviTask() {
        b.c.b.b.c.a(TAG, "continueNaviTask", new Object[0]);
    }

    private void correctNaviRoute(int i) {
        if (i.d()) {
            b.R().J(i);
        }
    }

    private void customNaviRoute(int i, String str) {
        int i2;
        boolean z = false;
        b.c.b.b.c.c(TAG, "customNaviRoute: {?} --- {?}", Integer.valueOf(i), str);
        if (!i.d()) {
            i2 = ProtocolResultCode.RESULT_UNINIT;
        } else {
            if (b.a.a.e.a.g().p()) {
                if (b.R().K(i, str).getErrorCode() == 0) {
                    z = true;
                }
                callback(z);
            }
            i2 = ProtocolResultCode.RESULT_NO_LAUNCH_UNSUPPORT;
        }
        this.errResultCode = i2;
        callback(z);
    }

    private void delViaPass(ProtocolBaseModel protocolBaseModel) {
        int i;
        if (!isNetworkConnected()) {
            i = ProtocolResultCode.RESULT_NO_NETWORK_NO_DATA;
        } else {
            if (b.a.a.e.a.g().p()) {
                int actionType = protocolBaseModel.getActionType();
                String str = TAG;
                b.c.b.b.c.c(str, "delViaPass:" + actionType, new Object[0]);
                b.R().U0(true);
                if (actionType > 0) {
                    MapAutoResult<Void> mapAutoResultL = b.R().L(actionType);
                    b.c.b.b.c.c(str, mapAutoResultL.getErrorCode() + "--" + mapAutoResultL.getErrorMessage(), new Object[0]);
                    if (mapAutoResultL.getErrorCode() == 0) {
                        callback(true);
                        return;
                    } else {
                        this.errResultCode = -30031;
                        ProtocolResultCode.RESULT_MESSAGE.put(-30031, mapAutoResultL.getErrorMessage());
                    }
                } else {
                    this.errResultCode = actionType == -1 ? ProtocolResultCode.RESULT_UNSUPPORT : ProtocolResultCode.RESULT_DEL_MIDDLE_POINT_MULTIPLE;
                }
                callback(false);
                return;
            }
            i = ProtocolResultCode.RESULT_NO_LAUNCH_UNSUPPORT;
        }
        this.errResultCode = i;
        callback(false);
    }

    private boolean delViaPass(int i) {
        b.c.b.b.c.a(TAG, "delViaPass:" + i, new Object[0]);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:149:0x03fe  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x01ac A[Catch: Exception -> 0x0543, TryCatch #0 {Exception -> 0x0543, blocks: (B:47:0x00b7, B:51:0x00cd, B:52:0x00d2, B:53:0x00d9, B:54:0x00e3, B:55:0x00e8, B:57:0x011b, B:59:0x0127, B:61:0x0133, B:63:0x0157, B:65:0x0193, B:66:0x01a4, B:69:0x01ac, B:71:0x01b1, B:72:0x01f0, B:73:0x0224, B:75:0x0232, B:77:0x023c, B:80:0x0244, B:82:0x024c, B:84:0x027a, B:85:0x028b, B:89:0x0296, B:92:0x02a0, B:93:0x02a4, B:94:0x02a9, B:95:0x02ac, B:96:0x02b2, B:98:0x02ba, B:99:0x02bf, B:101:0x02c7, B:102:0x02cc, B:104:0x02d4, B:105:0x02d9, B:107:0x02e1, B:108:0x02e6, B:110:0x02ee, B:111:0x02f3, B:113:0x02fb, B:114:0x0300, B:117:0x030c, B:121:0x031b, B:123:0x0349, B:124:0x0358, B:128:0x0362, B:131:0x0373, B:136:0x03af, B:138:0x03c2, B:142:0x03d4, B:144:0x03dc, B:146:0x03ea, B:148:0x03f8, B:150:0x0401, B:153:0x040b, B:154:0x0416, B:156:0x041c, B:157:0x0422, B:159:0x042c, B:160:0x0430, B:161:0x0437, B:162:0x0442, B:163:0x0453, B:164:0x045a, B:166:0x046e, B:168:0x0472, B:171:0x047b, B:173:0x0485, B:174:0x048b, B:175:0x048f, B:177:0x0497, B:180:0x04a2, B:182:0x04aa, B:186:0x04be, B:190:0x04c7, B:191:0x04da, B:192:0x04df, B:193:0x04e5, B:195:0x04ed, B:199:0x0530, B:201:0x0538, B:204:0x0546, B:205:0x0562, B:206:0x056d, B:207:0x0579, B:208:0x057d, B:209:0x0583, B:210:0x0587, B:211:0x058f, B:212:0x0595, B:213:0x05a1, B:214:0x05b1, B:215:0x05bb, B:216:0x05c1), top: B:220:0x0069 }] */
    @android.annotation.SuppressLint({"WrongConstant"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void doOperate(int r12) {
        /*
            Method dump skipped, instruction units count: 1566
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.autosdk.protocol.service.ProtocolService.doOperate(int):void");
    }

    @TargetApi(21)
    private boolean exitAPP() {
        String str = TAG;
        b.c.b.b.c.a(str, "exit BaiduMap: " + b.a.a.a.b().f(), new Object[0]);
        if (b.a.a.a.b().d().p()) {
            b.a.a.a.b().d().t();
        }
        if (!b.a.a.a.b().f()) {
            b.c.b.b.c.a(str, "exitMap failure : MapAutoService is not init", new Object[0]);
            return false;
        }
        MapAutoResult<Void> mapAutoResultM = b.R().M();
        b.c.b.b.c.a(str, "exitAPP code:{?}, msg:{?}", Integer.valueOf(mapAutoResultM.getErrorCode()), mapAutoResultM.getErrorMessage());
        return mapAutoResultM.getErrorCode() == 0;
    }

    private boolean exitMapList() {
        b.c.b.b.c.a(TAG, "exitMapList", new Object[0]);
        return false;
    }

    private String getAlongWaySearchType(int i) {
        return i == 0 ? "加油站" : i == 1 ? "充电站" : i == 2 ? "ATM" : i == 3 ? "厕所" : i == 4 ? "维修站" : i == 5 ? "服务区" : i == 6 ? "餐饮" : i == 7 ? "风景名胜" : i == 8 ? "酒店" : i == 9 ? "加气站" : ExternalServices.ERR_BLANK;
    }

    private int getNaviTTSMode() {
        return b.R().U().getData().intValue();
    }

    private int getTTSVolume() {
        return b.R().a0().getData().intValue();
    }

    private void gotoNaviHistory() {
        if (isBaiduMapInstalled()) {
            this.errResultCode = ProtocolResultCode.RESULT_UNSUPPORT;
            callback(false);
        } else {
            backToMap();
            MapAutoResult<Void> mapAutoResultQ0 = b.R().q0();
            b.c.b.b.c.a(TAG, "gotoNaviHistory code:{?}, msg:{?}", Integer.valueOf(mapAutoResultQ0.getErrorCode()), mapAutoResultQ0.getErrorMessage());
            callback(mapAutoResultQ0.getErrorCode() == 0);
        }
    }

    private void gotoUserFavorites() {
        backToMap();
        MapAutoResult<Void> mapAutoResultM0 = b.R().m0();
        b.c.b.b.c.a(TAG, "gotoUserFavorites code:{?} , msg:{?}", Integer.valueOf(mapAutoResultM0.getErrorCode()), mapAutoResultM0.getErrorMessage());
        if (mapAutoResultM0.getErrorCode() == 0) {
            callback(true);
            return;
        }
        this.errResultCode = -30023;
        ProtocolResultCode.RESULT_MESSAGE.put(-30023, mapAutoResultM0.getErrorMessage());
        callback(false);
    }

    private void gotoWallpaper() {
        try {
            Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
            intent.addCategory("android.intent.category.WALLPAPER_HOME");
            intent.addFlags(270532608);
            startActivity(intent);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private boolean isAppActived() {
        b.c.b.b.c.a(TAG, "isAppActived", new Object[0]);
        return false;
    }

    private boolean isAutoLaunched() {
        return false;
    }

    private boolean isBLAosServiceInit() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isBaiduMapInstalled() {
        return i.c("com.byd.naviauto");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isGaodeMapInstalled() {
        return i.c("com.byd.launchermap");
    }

    private boolean isNetworkConnected() {
        return j.i();
    }

    private boolean isValidPOINameAddr(String str) {
        if (TextUtils.isEmpty(str) || "地图选点".equals(str)) {
            return false;
        }
        return !"在地图选点附近".equals(str);
    }

    private void jumpSearchHome() {
    }

    private void jumpSettingsPage() {
        backToMap();
        if (b.R().i0().getData().booleanValue()) {
            this.errResultCode = ProtocolResultCode.RESULT_UNSUPPORT_VIEW;
            callback(false);
            return;
        }
        MapAutoResult<Void> mapAutoResultP0 = b.R().p0();
        b.c.b.b.c.a(TAG, "jumpSettingsPage code :{?}, msg :{?}", Integer.valueOf(mapAutoResultP0.getErrorCode()), mapAutoResultP0.getErrorMessage());
        if (mapAutoResultP0.getErrorCode() == 0) {
            callback(true);
            return;
        }
        this.errResultCode = -30022;
        ProtocolResultCode.RESULT_MESSAGE.put(-30022, mapAutoResultP0.getErrorMessage());
        callback(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: lambda$setMapZoom$0, reason: merged with bridge method [inline-methods] */
    public /* synthetic */ void a(boolean z) {
        if (b.a.a.a.b().c().i(!z)) {
            return;
        }
        this.errResultCode = z ? ProtocolResultCode.RESULT_SCALE_IS_SMALLEST : ProtocolResultCode.RESULT_SCALE_IS_LARGEST;
        this.zoomResult = false;
    }

    private void moveMap(int i, int i2) {
        if (i.d()) {
            b.R().k0(i, i2);
        }
    }

    private void naviHomeOrCompany(ProtocolBaseModel protocolBaseModel) {
        int i;
        if (isNetworkConnected()) {
            backToMap();
            int actionType = protocolBaseModel.getActionType();
            int operaType = protocolBaseModel.getOperaType();
            String str = TAG;
            b.c.b.b.c.a(str, "naviHomeOrCompany action:" + actionType + ", opera:" + operaType, new Object[0]);
            if (actionType < 30000) {
                boolean zBooleanValue = b.R().g0(actionType).getData().booleanValue();
                if (operaType == 1) {
                    callback(zBooleanValue);
                    return;
                }
                if (operaType == 0) {
                    if (!zBooleanValue) {
                        b.R().n0(actionType);
                        return;
                    }
                    MapAutoResult<Void> mapAutoResultP0 = b.R().P0(b.a.a.a.b().d().f(actionType));
                    b.c.b.b.c.a(str, "naviHomeOrCompany --- {?}, {?}", Integer.valueOf(mapAutoResultP0.getErrorCode()), mapAutoResultP0.getErrorMessage());
                    if (mapAutoResultP0.getErrorCode() != 0) {
                        this.errResultCode = -30026;
                        ProtocolResultCode.RESULT_MESSAGE.put(-30026, mapAutoResultP0.getErrorMessage());
                    }
                    callback(mapAutoResultP0.getErrorCode() == 0);
                    return;
                }
                return;
            }
            int i2 = (actionType != 60002 && actionType == 60003) ? 1 : 0;
            b.c.b.b.c.a(str, "isHCSet : {?}", b.R().g0(i2).getData());
            if (actionType == 60002 && !b.R().g0(0).getData().booleanValue()) {
                this.mActionHCType = actionType;
                i = ProtocolResultCode.RESULT_NO_SET_HOME;
            } else {
                if (actionType != 60003 || b.R().g0(1).getData().booleanValue()) {
                    if (operaType >= 0) {
                        refreshRouteWithPerfer(operaType);
                    }
                    MapAutoResult<Void> mapAutoResultQ0 = b.R().Q0(b.a.a.a.b().d().f(i2), this.mRoutePlanPref);
                    b.c.b.b.c.a(str, "naviHomeOrCompany --- {?}", Integer.valueOf(mapAutoResultQ0.getErrorCode()));
                    if (mapAutoResultQ0.getErrorCode() != 0) {
                        this.errResultCode = -30026;
                        ProtocolResultCode.RESULT_MESSAGE.put(-30026, mapAutoResultQ0.getErrorMessage());
                    }
                    callback(mapAutoResultQ0.getErrorCode() == 0);
                    return;
                }
                this.mActionHCType = actionType;
                i = ProtocolResultCode.RESULT_NO_SET_COMPANY;
            }
        } else {
            i = ProtocolResultCode.RESULT_NO_NETWORK_NO_DATA;
        }
        this.errResultCode = i;
        callback(false);
    }

    private void openHCSettingPage(int i) {
        MapAutoResult<Void> mapAutoResultN0 = b.R().n0(i);
        b.c.b.b.c.a(TAG, "openHCSettingPage code:{?}, msg:{?}", Integer.valueOf(mapAutoResultN0.getErrorCode()), mapAutoResultN0.getErrorMessage());
        callback(mapAutoResultN0.getErrorCode() == 0);
    }

    private boolean refreshRoute() {
        if (!b.a.a.a.b().d().p()) {
            this.errResultCode = ProtocolResultCode.RESULT_NO_LAUNCH_UNSUPPORT;
            return false;
        }
        MapAutoResult<Void> mapAutoResultS0 = b.R().s0();
        b.c.b.b.c.a(TAG, "errorCode : " + mapAutoResultS0.getErrorCode(), new Object[0]);
        return mapAutoResultS0.getErrorCode() == 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:29:0x00dc. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0110  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0113  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String requestFrontTrafficInfo(int r10) {
        /*
            Method dump skipped, instruction units count: 792
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.autosdk.protocol.service.ProtocolService.requestFrontTrafficInfo(int):java.lang.String");
    }

    private void routePlan(BaseSearchPoi baseSearchPoi) {
        b.R().P0(baseSearchPoi);
    }

    private void routePlan(BaseSearchPoi baseSearchPoi, int i) {
        b.R().Q0(baseSearchPoi, i);
    }

    private void searchAround(int i, GeoPoint geoPoint, String str) {
        boolean z = false;
        b.c.b.b.c.a(TAG, "searchAround  type:" + i + " ,poi:" + geoPoint + " ,keyword:" + str, new Object[0]);
        b.R().G();
        backToMap();
        b.R().L0(str, geoPoint, new x() { // from class: com.autosdk.protocol.service.ProtocolService.7
            @Override // b.b.a.a.d.x
            public void onNearbySearchResult(int i2, List<NearbySearchPoi> list) {
                if (list == null || list.size() <= 0) {
                    return;
                }
                b.c.b.b.c.a(ProtocolService.TAG, "onNearbySearchResult : {?} {?} {?}", Integer.valueOf(i2), Integer.valueOf(list.size()), list.get(0).getAreaName());
            }
        });
        if (i == 2) {
            this.errResultCode = ProtocolResultCode.RESULT_UNSUPPORT;
        } else {
            if (i != 1 || !BYD_AUTO_VOICE.equals(this.callerPackageName)) {
                return;
            }
            if (b.R().r0(str).getErrorCode() == 0) {
                z = true;
            }
        }
        callback(z);
    }

    private void searchAround(int i, String str) {
        if (!isNetworkConnected()) {
            this.errResultCode = ProtocolResultCode.RESULT_NO_NETWORK_NO_DATA;
            callback(false);
            return;
        }
        GeoPoint geoPointD = null;
        if (i == 2) {
            geoPointD = b.a.a.a.b().d().d();
        } else if (i == 1) {
            geoPointD = b.a.a.a.b().c().d();
        }
        searchAround(i, geoPointD, str);
    }

    private void searchAroundResultSelected(int i) {
    }

    private void searchKeyword(ProtocolBaseModel protocolBaseModel) {
        b bVarR;
        r rVar;
        backToMap();
        if (isNetworkConnected()) {
            String searchKey = protocolBaseModel.getSearchKey();
            String destPoiName = protocolBaseModel.getDestPoiName();
            String passPoiName = protocolBaseModel.getPassPoiName();
            boolean zIsMainCab = protocolBaseModel.isMainCab();
            boolean zIsNavi = protocolBaseModel.isNavi();
            boolean zIsWaypoint = protocolBaseModel.isWaypoint();
            int actionType = protocolBaseModel.getActionType();
            int operaType = protocolBaseModel.getOperaType();
            String str = TAG;
            b.c.b.b.c.a(str, "searchKeyword   keyword:" + searchKey + " ,isMainCab:" + zIsMainCab + ", isNavi =" + zIsNavi + " , actionType = " + actionType + ", operaType = " + operaType, new Object[0]);
            StringBuilder sb = new StringBuilder();
            sb.append("searchKeyword   destPoiName:");
            sb.append(destPoiName);
            sb.append(" ,passPoiName:");
            sb.append(passPoiName);
            sb.append(", waypoint =");
            sb.append(zIsWaypoint);
            b.c.b.b.c.a(str, sb.toString(), new Object[0]);
            if ((!zIsWaypoint && TextUtils.isEmpty(passPoiName)) || !isBaiduMapInstalled()) {
                if (operaType >= 0) {
                    if (!isBaiduMapInstalled()) {
                        checkAndSavePrefer(operaType);
                    }
                }
                if (TextUtils.isEmpty(searchKey)) {
                    return;
                }
                if (b.a.a.a.b().d().q()) {
                    if (g.c(searchKey)) {
                        this.result = b.R().l0(searchKey);
                        b.R().H0(searchKey, ExternalServices.ERR_BLANK, new b.b.a.a.d.c() { // from class: com.autosdk.protocol.service.ProtocolService.8
                            @Override // b.b.a.a.d.c
                            public void onAlongRouteSearchResult(int i, AlongRouteSearchResult alongRouteSearchResult) {
                                boolean z = false;
                                b.c.b.b.c.c(ProtocolService.TAG, "onAlongRouteSearchResult : {?} --- {?}", Integer.valueOf(i), alongRouteSearchResult);
                                if (alongRouteSearchResult != null) {
                                    b.c.b.b.c.c(ProtocolService.TAG, "onAlongRouteSearchResult : {?} --- {?} --- {?}", Integer.valueOf(i), Integer.valueOf(alongRouteSearchResult.getPois().size()), Integer.valueOf(alongRouteSearchResult.getTotalCount()));
                                    ProtocolService.this.mSearchResultCount = alongRouteSearchResult.getTotalCount();
                                    ProtocolService.this.mAlongRouteSearchPois = alongRouteSearchResult.getPois();
                                }
                                MapAutoResult<Void> mapAutoResult = ProtocolService.this.result;
                                if (mapAutoResult == null || mapAutoResult.getErrorCode() != 0) {
                                    ProtocolService.this.errResultCode = ProtocolResultCode.RESULT_NO_FOUND;
                                }
                                ProtocolService protocolService = ProtocolService.this;
                                MapAutoResult<Void> mapAutoResult2 = protocolService.result;
                                if (mapAutoResult2 != null && mapAutoResult2.getErrorCode() == 0) {
                                    z = true;
                                }
                                protocolService.callback(z);
                                ProtocolService.this.result = null;
                            }
                        });
                        return;
                    } else {
                        if (b.R().e1().getErrorCode() != 0 || !BYD_AUTO_VOICE.equals(this.callerPackageName)) {
                            return;
                        }
                        this.result = b.R().r0(searchKey);
                        bVarR = b.R();
                        rVar = new r() { // from class: com.autosdk.protocol.service.ProtocolService.9
                            @Override // b.b.a.a.d.r
                            public void onKeywordSearchResult(int i, List<AreaSearchPoi> list) {
                                if (list != null && list.size() > 0) {
                                    b.c.b.b.c.c(ProtocolService.TAG, "onKeywordSearchResult : {?} --- {?} --- {?} --- {?} --- {?}", Integer.valueOf(i), Integer.valueOf(list.size()), list.get(0).getName(), list.get(0).getAreaName(), list.get(0).getAddress());
                                    ProtocolService.this.mSearchResultCount = list.size();
                                }
                                MapAutoResult<Void> mapAutoResult = ProtocolService.this.result;
                                if (mapAutoResult == null || mapAutoResult.getErrorCode() != 0) {
                                    ProtocolService.this.errResultCode = ProtocolResultCode.RESULT_NO_FOUND;
                                }
                                ProtocolService protocolService = ProtocolService.this;
                                MapAutoResult<Void> mapAutoResult2 = protocolService.result;
                                protocolService.callback(mapAutoResult2 != null && mapAutoResult2.getErrorCode() == 0);
                                ProtocolService.this.result = null;
                            }
                        };
                    }
                } else {
                    if (!BYD_AUTO_VOICE.equals(this.callerPackageName)) {
                        return;
                    }
                    this.result = b.R().r0(searchKey);
                    bVarR = b.R();
                    rVar = new r() { // from class: com.autosdk.protocol.service.ProtocolService.10
                        @Override // b.b.a.a.d.r
                        public void onKeywordSearchResult(int i, List<AreaSearchPoi> list) {
                            if (list != null && list.size() > 0) {
                                b.c.b.b.c.c(ProtocolService.TAG, "onKeywordSearchResult : {?} --- {?} --- {?} --- {?} --- {?}", Integer.valueOf(i), Integer.valueOf(list.size()), list.get(0).getName(), list.get(0).getAreaName(), list.get(0).getAddress());
                                ProtocolService.this.mSearchResultCount = list.size();
                            }
                            MapAutoResult<Void> mapAutoResult = ProtocolService.this.result;
                            if (mapAutoResult == null || mapAutoResult.getErrorCode() != 0) {
                                ProtocolService.this.errResultCode = ProtocolResultCode.RESULT_NO_FOUND;
                            }
                            ProtocolService protocolService = ProtocolService.this;
                            MapAutoResult<Void> mapAutoResult2 = protocolService.result;
                            protocolService.callback(mapAutoResult2 != null && mapAutoResult2.getErrorCode() == 0);
                            ProtocolService.this.result = null;
                        }
                    };
                }
                bVarR.J0(searchKey, rVar);
                return;
            }
            this.errResultCode = ProtocolResultCode.RESULT_UNSUPPORT;
        } else {
            this.errResultCode = ProtocolResultCode.RESULT_NO_NETWORK_NO_DATA;
        }
        callback(false);
    }

    private boolean sendToCarOpera(int i) {
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0030  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00e1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void setHomeOrCompany(com.autosdk.protocol.model.base.ProtocolBaseModel r7) {
        /*
            Method dump skipped, instruction units count: 246
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.autosdk.protocol.service.ProtocolService.setHomeOrCompany(com.autosdk.protocol.model.base.ProtocolBaseModel):void");
    }

    private void setMapLineData(List<GeoPoint> list, float f, float f2, float f3, float f4, float f5) {
        b.R().W0(list, f, f2, f3, f4, f5);
    }

    private void setMapZoom(final boolean z) {
        this.zoomResult = true;
        backToMap();
        b.c.b.a.a.a(new Runnable() { // from class: b.a.b.a.b
            @Override // java.lang.Runnable
            public final void run() {
                this.f1142b.a(z);
            }
        });
        b.c.b.a.a.b(new Runnable() { // from class: com.autosdk.protocol.service.ProtocolService.6
            @Override // java.lang.Runnable
            public void run() {
                ProtocolService protocolService = ProtocolService.this;
                protocolService.callback(protocolService.zoomResult);
            }
        }, 300L);
    }

    private String setMute(boolean z) {
        try {
            ((AudioManager) getApplication().getSystemService("audio")).setStreamMute(STREAM_NAVI, z);
            return "好的";
        } catch (Exception e2) {
            b.c.b.b.c.a(TAG, "setMute = " + e2.getMessage(), new Object[0]);
            return "好的";
        }
    }

    private void setTTSOpen(boolean z) {
        b.R().a1(z);
    }

    private void setTTSVolume(int i) {
        b.R().b1(i);
    }

    private String setVolume(int i) {
        if (i > 10 || i < 0) {
            this.errResultCode = ProtocolResultCode.RESULT_UNSUPPORT;
            callback(false);
            return null;
        }
        try {
            ((AudioManager) getApplication().getSystemService("audio")).setStreamVolume(STREAM_NAVI, i, 4);
            b.c.b.b.c.a(TAG, "Volume = " + i, new Object[0]);
            return "好的";
        } catch (Exception e2) {
            b.c.b.b.c.a(TAG, "setVolume = " + e2.getMessage(), new Object[0]);
            return "好的";
        }
    }

    private void showCurrentLocation() {
        b.R().c1();
    }

    private void showMyLocation() {
        if (!isNetworkConnected()) {
            this.errResultCode = ProtocolResultCode.RESULT_NO_NETWORK_NO_DATA;
            callback(false);
        } else {
            this.mCurrentPosition = ExternalServices.ERR_BLANK;
            this.mCurrentAddress = ExternalServices.ERR_BLANK;
            b.a.a.a.b().c().getCurrentLocationAddress(new c0() { // from class: com.autosdk.protocol.service.ProtocolService.11
                @Override // b.b.a.a.d.c0
                public void onReverseGeoSearchResult(int i, ReverseGeocodingResult reverseGeocodingResult) {
                    b.c.b.b.c.a(ProtocolService.TAG, "onReverseGeoSearchResult: {?}", reverseGeocodingResult);
                    if (reverseGeocodingResult != null) {
                        if (reverseGeocodingResult.getPoiList().size() > 0) {
                            ProtocolService.this.mCurrentPosition = reverseGeocodingResult.getPoiList().get(0).getAddress();
                        }
                        ProtocolService.this.mCurrentAddress = reverseGeocodingResult.getAddress();
                    }
                    ProtocolService.this.callback(true);
                }
            });
        }
    }

    private boolean showPreview(int i) {
        b.c.b.b.c.a(TAG, "showPreview:" + i, new Object[0]);
        return true;
    }

    private void startNavi(int i) {
        b.R().d1(i);
    }

    private void startUpMap(String str) {
        b.c.b.b.c.a(TAG, "startUpMap:" + str, new Object[0]);
        new Intent();
    }

    private void startUpMap(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        b.c.b.b.c.a(TAG, "startUpMap", new Object[0]);
    }

    private void statusQuery(final int i) {
        b.c.b.b.c.a(TAG, "statusQuery:" + i, new Object[0]);
        b.c.b.a.a.a(new Runnable() { // from class: com.autosdk.protocol.service.ProtocolService.15
            @Override // java.lang.Runnable
            public void run() {
                boolean zF;
                int i2 = i;
                if (i2 == 0) {
                    ProtocolService.this.callback(false);
                    return;
                }
                if (i2 == 1) {
                    zF = b.a.a.a.b().d().q();
                } else if (i2 != 2) {
                    return;
                } else {
                    zF = b.a.a.a.b().c().f();
                }
                ProtocolService.this.callback(zF);
            }
        });
    }

    private void stopNavi() {
        b.R().e1();
    }

    private String subVolume() {
        int streamVolume = -1;
        try {
            streamVolume = ((AudioManager) getApplication().getSystemService("audio")).getStreamVolume(STREAM_NAVI);
            b.c.b.b.c.a(TAG, "getVolume = " + streamVolume, new Object[0]);
        } catch (Exception e2) {
            b.c.b.b.c.a(TAG, "getVolume = " + e2.getMessage(), new Object[0]);
        }
        if (streamVolume == 0) {
            return PROTOCOL_TTS_VOLUME_MIN;
        }
        setVolume(streamVolume - 1);
        return "好的";
    }

    private void viaRoadName(String str) {
        b.R().K(1, str);
    }

    private String volumeOprea(int i, int i2) {
        b.c.b.b.c.a(TAG, "volumeOprea type: {?}, opera: {?}", Integer.valueOf(i), Integer.valueOf(i2));
        if (i == 0) {
            return setMute(true);
        }
        if (i == 1) {
            return setMute(false);
        }
        if (i == 2) {
            return setVolume(i2);
        }
        if (i == 3) {
            return addVolume();
        }
        if (i == 4) {
            return subVolume();
        }
        return null;
    }

    private void zoomMapMax(int i) {
        if (i.d()) {
            b.R().i1(i);
        }
    }

    public void closeRouteResult() {
    }

    public long getRoutePathCount() {
        return 0L;
    }

    public void justClickNavi() {
        b.c.b.b.c.a(TAG, "justClickNavi", new Object[0]);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        b.c.b.b.c.a(TAG, "mProtocolServiceManager:" + this.mProtocolServiceManager, new Object[0]);
        return this.mProtocolServiceManager;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        b.c.b.b.c.a(TAG, "onCreate", new Object[0]);
        b.R().A(this.mOnTurnInfoListener);
        registerReceiver(this.mAutoVoiceReceiver, new IntentFilter("com.byd.intent.action.AUTOVOICE_STATE"));
    }

    @Override // android.app.Service
    public void onDestroy() {
        b.c.b.b.c.a(TAG, "onDestroy", new Object[0]);
        synchronized (this) {
            this.mListenerList.kill();
        }
        this.mDiTrainerCallbackArray.clear();
        a.i().w();
        super.onDestroy();
        b.R().E0(this.mOnTurnInfoListener);
        unregisterReceiver(this.mAutoVoiceReceiver);
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        b.c.b.b.c.a("ProtocolService", "onUnbind", new Object[0]);
        return super.onUnbind(intent);
    }

    public boolean refreshRouteWithPerfer(int i) {
        int i2 = 8;
        if (i == 0) {
            i2 = 0;
        } else if (i == 1) {
            i2 = 4;
        } else if (i == 2) {
            i2 = 3;
        } else if (i == 4) {
            i2 = 2;
        } else if (i == 8) {
            i2 = 9;
        } else if (i != 32) {
            i2 = i != 64 ? 99 : 15;
        }
        this.mRoutePlanPref = i2;
        MapAutoResult<Void> mapAutoResultG1 = b.R().g1(i2);
        if (mapAutoResultG1.getErrorCode() != 0) {
            this.errResultCode = -30027;
            ProtocolResultCode.RESULT_MESSAGE.put(-30027, mapAutoResultG1.getErrorMessage());
        }
        return mapAutoResultG1.getErrorCode() == 0;
    }

    public void roadConditionChange(int i) {
        if (i != 0 && !isNetworkConnected()) {
            this.errResultCode = ProtocolResultCode.RESULT_NO_NETWORK_NO_DATA;
            callback(false);
            return;
        }
        backToMap();
        MapAutoResult<Void> mapAutoResultZ0 = b.R().Z0(i == 1);
        b.c.b.b.c.c(TAG, "roadConditionChange : {?} {?}", Integer.valueOf(mapAutoResultZ0.getErrorCode()), mapAutoResultZ0.getErrorMessage());
        if (mapAutoResultZ0.getErrorCode() != 0) {
            this.errResultCode = -30030;
            ProtocolResultCode.RESULT_MESSAGE.put(-30030, mapAutoResultZ0.getErrorMessage());
        }
        callback(mapAutoResultZ0.getErrorCode() == 0);
    }

    public boolean selectRoute(int i) {
        b.c.b.b.c.a(TAG, "selectRoute:" + i, new Object[0]);
        b.R().R0(i, true);
        return true;
    }

    public void setActiveCallback(final IProtocolCallback iProtocolCallback) {
        new Timer().schedule(new TimerTask() { // from class: com.autosdk.protocol.service.ProtocolService.3
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                ProtocolBaseModel protocolBaseModel = ProtocolService.this.protocolModel;
                if (protocolBaseModel == null || protocolBaseModel.getProtocolID() != 31015) {
                    return;
                }
                b.c.b.b.c.a(ProtocolService.TAG, "setActiveCallback ok.", new Object[0]);
                ProtocolService.this.activeCallback = iProtocolCallback;
            }
        }, 1000L);
    }

    @SuppressLint({"WrongConstant"})
    public boolean setCarModeDirection(int i) {
        backToMap();
        MapAutoResult<Void> mapAutoResultX0 = i == 0 ? b.R().X0(0) : i == 1 ? b.R().X0(2) : b.R().X0(1);
        b.c.b.b.c.c(TAG, "setMapShowMode :" + mapAutoResultX0.getErrorCode() + "--" + mapAutoResultX0.getErrorMessage(), new Object[0]);
        if (mapAutoResultX0.getErrorCode() != 0) {
            this.errResultCode = -30024;
            ProtocolResultCode.RESULT_MESSAGE.put(-30024, mapAutoResultX0.getErrorMessage());
        }
        return mapAutoResultX0.getErrorCode() == 0;
    }

    public boolean setTtsPlayParam(int i) {
        return true;
    }

    public void startNavi(String str, String str2, String str3, int i) {
        String str4 = TAG;
        b.c.b.b.c.a(str4, "startNavi   poiName:" + str + ",lat:" + str2 + ",lng:" + str3, new Object[0]);
        backToMap();
        if (!b.a.a.a.b().f()) {
            b.a.a.a.b().e(i.b());
        }
        refreshRouteWithPerfer(i);
        BaseSearchPoi baseSearchPoi = new BaseSearchPoi();
        if (!TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str2) && g.d(str3) && g.d(str2)) {
            baseSearchPoi.setLocation(new GeoPoint(Double.parseDouble(str3), Double.parseDouble(str2)));
        }
        baseSearchPoi.setName(str);
        MapAutoResult<Void> mapAutoResultP0 = b.R().P0(baseSearchPoi);
        b.c.b.b.c.c(str4, "result : {?}, {?}", Integer.valueOf(mapAutoResultP0.getErrorCode()), mapAutoResultP0.getErrorMessage());
        if (mapAutoResultP0.getErrorCode() == 0) {
            callback(true);
            return;
        }
        this.errResultCode = -30026;
        ProtocolResultCode.RESULT_MESSAGE.put(-30026, mapAutoResultP0.getErrorMessage());
        callback(false);
    }

    public void startNavi(String str, String str2, String str3, String str4, String str5, String str6, int i) {
        b.c.b.b.c.a(TAG, "startNavi   poiName:" + str + ",lat:" + str2 + ",lng:" + str3 + ",passPoiName:" + str4 + ",pasLat:" + str5 + ",pasLng:" + str6 + ",perfer:" + i, new Object[0]);
        backToMap();
        if (!b.a.a.a.b().f()) {
            b.a.a.a.b().e(i.b());
        }
        this.errResultCode = ProtocolResultCode.RESULT_UNSUPPORT;
        callback(false);
    }
}
