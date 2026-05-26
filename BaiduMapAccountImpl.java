package com.byd.map.account.lbs;

import android.content.Context;
import android.os.BootBusinessManager;
import android.text.TextUtils;
import android.util.Log;
import com.baidu.iovsdk.bridge.AccountClientSdk;
import com.baidu.iovsdk.bridge.account.InitListener;
import com.baidu.iovsdk.common.account.BaiDuAccountInfo;
import com.baidu.iovsdk.common.account.OnServerCallListener;
import com.byd.map.account.IMapAccount;
import com.byd.map.account.MapAccountCallback;
import com.byd.map.accountsdk.bean.MapAccountInfo;
import com.google.gson.Gson;

/* JADX INFO: loaded from: classes.dex */
public class BaiduMapAccountImpl implements IMapAccount {
    private static final String FLAG_LOGIN_OR_LOGOUT_TOGETHER = "bind";
    private volatile boolean isInit;
    private Context mContext;
    private MapAccountCallback mMapAccountCallback;
    private final String TAG = "BaiduMapAccountImpl";
    private InitListener mInitListener = new InitListener() { // from class: com.byd.map.account.lbs.BaiduMapAccountImpl.1
        public AnonymousClass1() {
        }

        @Override // com.baidu.iovsdk.bridge.account.InitListener
        public void initFailed(int i, String str) {
            Log.e("BaiduMapAccountImpl", "baidu SDK initFailed... code : " + i + "     s : " + str);
            BaiduMapAccountImpl.this.isInit = false;
            if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                BaiduMapAccountImpl.this.mMapAccountCallback.initFailed(i, str);
            }
        }

        @Override // com.baidu.iovsdk.bridge.account.InitListener
        public void initSuccess() {
            Log.e("BaiduMapAccountImpl", "baidu SDK initSuccess...");
            BaiduMapAccountImpl.this.isInit = true;
            if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                BaiduMapAccountImpl.this.mMapAccountCallback.initSuccess();
            }
            AccountClientSdk.setOnServerCallListener(BaiduMapAccountImpl.this.mServerCallListener);
        }
    };
    private OnServerCallListener mServerCallListener = new OnServerCallListener() { // from class: com.byd.map.account.lbs.BaiduMapAccountImpl.2
        public AnonymousClass2() {
        }

        @Override // com.baidu.iovsdk.common.account.OnServerCallListener
        public void onServerCall(String str) {
            Log.e("BaiduMapAccountImpl", "byd_test onServerCall() called with: s = [" + str + "]");
            ExternalBaiduInfo externalBaiduInfo = (ExternalBaiduInfo) new Gson().fromJson(str, ExternalBaiduInfo.class);
            if (externalBaiduInfo != null) {
                BaiduBindInfo baiduBindInfo = (BaiduBindInfo) new Gson().fromJson(externalBaiduInfo.getData(), BaiduBindInfo.class);
                if (baiduBindInfo == null) {
                    Log.e("BaiduMapAccountImpl", "bindInfo is null");
                }
                String funtype = externalBaiduInfo.getFuntype();
                funtype.hashCode();
                switch (funtype) {
                    case "LOGOUT":
                        Log.e("BaiduMapAccountImpl", "百度登出成功 且对应关系: oemid = [" + baiduBindInfo.getOemUid() + "],bduid = " + baiduBindInfo.getBdUid() + "  extra = " + externalBaiduInfo.getExtra());
                        if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                            BaiduMapAccountImpl.this.mMapAccountCallback.logoutSuccess(BaiduMapAccountImpl.FLAG_LOGIN_OR_LOGOUT_TOGETHER.equals(externalBaiduInfo.getExtra()) ? 1 : 0);
                            break;
                        }
                        break;
                    case "UNBIND":
                        if (externalBaiduInfo.getCode() == 0) {
                            Log.e("BaiduMapAccountImpl", "解绑成功 且对应关系: oemid = [" + baiduBindInfo.getOemUid() + "],bduid = " + baiduBindInfo.getBdUid() + "  code = " + externalBaiduInfo.getCode());
                            if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                                BaiduMapAccountImpl.this.mMapAccountCallback.unbindSuccess();
                            }
                            break;
                        } else {
                            Log.e("BaiduMapAccountImpl", "解绑失败 且对应关系: oemid = [" + baiduBindInfo.getOemUid() + "],bduid = " + baiduBindInfo.getBdUid() + "  code = " + externalBaiduInfo.getCode());
                            if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                                BaiduMapAccountImpl.this.mMapAccountCallback.unbindFailure("解绑失败");
                            }
                            break;
                        }
                        break;
                    case "BIND":
                        if (externalBaiduInfo.getCode() == 0) {
                            Log.e("BaiduMapAccountImpl", "绑定成功 且对应关系: oemid = [" + baiduBindInfo.getOemUid() + "],bduid = " + baiduBindInfo.getBdUid() + "  code = " + externalBaiduInfo.getCode());
                            if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                                BaiduMapAccountImpl.this.mMapAccountCallback.bindSuccess();
                            }
                            break;
                        } else {
                            Log.e("BaiduMapAccountImpl", "绑定失败 且对应关系: oemid = [" + baiduBindInfo.getOemUid() + "],bduid = " + baiduBindInfo.getBdUid() + "  code = " + externalBaiduInfo.getCode());
                            if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                                BaiduMapAccountImpl.this.mMapAccountCallback.bindFailure("绑定失败");
                            }
                            break;
                        }
                        break;
                    case "LOGIN":
                        Log.e("BaiduMapAccountImpl", "百度登录成功 且对应关系: oemid = [" + baiduBindInfo.getOemUid() + "],bduid = " + baiduBindInfo.getBdUid() + "  extra = " + externalBaiduInfo.getExtra());
                        if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                            BaiduMapAccountImpl.this.mMapAccountCallback.loginSuccess(BaiduMapAccountImpl.FLAG_LOGIN_OR_LOGOUT_TOGETHER.equals(externalBaiduInfo.getExtra()) ? 1 : 0);
                            break;
                        }
                        break;
                    case "CHNANGE":
                        if (externalBaiduInfo.getCode() == 0) {
                            Log.e("BaiduMapAccountImpl", "换绑成功 且对应关系: oemid = [" + baiduBindInfo.getOemUid() + "],bduid = " + baiduBindInfo.getBdUid() + "  code = " + externalBaiduInfo.getCode());
                            if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                                BaiduMapAccountImpl.this.mMapAccountCallback.changeSuccess();
                            }
                            break;
                        } else {
                            Log.e("BaiduMapAccountImpl", "换绑失败 且对应关系: oemid = [" + baiduBindInfo.getOemUid() + "],bduid = " + baiduBindInfo.getBdUid() + "  code = " + externalBaiduInfo.getCode());
                            if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                                BaiduMapAccountImpl.this.mMapAccountCallback.changeFailure("解绑失败");
                            }
                            break;
                        }
                        break;
                }
            }
        }
    };
    private Gson mGson = new Gson();

    /* JADX INFO: renamed from: com.byd.map.account.lbs.BaiduMapAccountImpl$1 */
    public class AnonymousClass1 implements InitListener {
        public AnonymousClass1() {
        }

        @Override // com.baidu.iovsdk.bridge.account.InitListener
        public void initFailed(int i, String str) {
            Log.e("BaiduMapAccountImpl", "baidu SDK initFailed... code : " + i + "     s : " + str);
            BaiduMapAccountImpl.this.isInit = false;
            if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                BaiduMapAccountImpl.this.mMapAccountCallback.initFailed(i, str);
            }
        }

        @Override // com.baidu.iovsdk.bridge.account.InitListener
        public void initSuccess() {
            Log.e("BaiduMapAccountImpl", "baidu SDK initSuccess...");
            BaiduMapAccountImpl.this.isInit = true;
            if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                BaiduMapAccountImpl.this.mMapAccountCallback.initSuccess();
            }
            AccountClientSdk.setOnServerCallListener(BaiduMapAccountImpl.this.mServerCallListener);
        }
    }

    /* JADX INFO: renamed from: com.byd.map.account.lbs.BaiduMapAccountImpl$2 */
    public class AnonymousClass2 implements OnServerCallListener {
        public AnonymousClass2() {
        }

        @Override // com.baidu.iovsdk.common.account.OnServerCallListener
        public void onServerCall(String str) {
            Log.e("BaiduMapAccountImpl", "byd_test onServerCall() called with: s = [" + str + "]");
            ExternalBaiduInfo externalBaiduInfo = (ExternalBaiduInfo) new Gson().fromJson(str, ExternalBaiduInfo.class);
            if (externalBaiduInfo != null) {
                BaiduBindInfo baiduBindInfo = (BaiduBindInfo) new Gson().fromJson(externalBaiduInfo.getData(), BaiduBindInfo.class);
                if (baiduBindInfo == null) {
                    Log.e("BaiduMapAccountImpl", "bindInfo is null");
                }
                String funtype = externalBaiduInfo.getFuntype();
                funtype.hashCode();
                switch (funtype) {
                    case "LOGOUT":
                        Log.e("BaiduMapAccountImpl", "百度登出成功 且对应关系: oemid = [" + baiduBindInfo.getOemUid() + "],bduid = " + baiduBindInfo.getBdUid() + "  extra = " + externalBaiduInfo.getExtra());
                        if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                            BaiduMapAccountImpl.this.mMapAccountCallback.logoutSuccess(BaiduMapAccountImpl.FLAG_LOGIN_OR_LOGOUT_TOGETHER.equals(externalBaiduInfo.getExtra()) ? 1 : 0);
                            break;
                        }
                        break;
                    case "UNBIND":
                        if (externalBaiduInfo.getCode() == 0) {
                            Log.e("BaiduMapAccountImpl", "解绑成功 且对应关系: oemid = [" + baiduBindInfo.getOemUid() + "],bduid = " + baiduBindInfo.getBdUid() + "  code = " + externalBaiduInfo.getCode());
                            if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                                BaiduMapAccountImpl.this.mMapAccountCallback.unbindSuccess();
                            }
                            break;
                        } else {
                            Log.e("BaiduMapAccountImpl", "解绑失败 且对应关系: oemid = [" + baiduBindInfo.getOemUid() + "],bduid = " + baiduBindInfo.getBdUid() + "  code = " + externalBaiduInfo.getCode());
                            if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                                BaiduMapAccountImpl.this.mMapAccountCallback.unbindFailure("解绑失败");
                            }
                            break;
                        }
                        break;
                    case "BIND":
                        if (externalBaiduInfo.getCode() == 0) {
                            Log.e("BaiduMapAccountImpl", "绑定成功 且对应关系: oemid = [" + baiduBindInfo.getOemUid() + "],bduid = " + baiduBindInfo.getBdUid() + "  code = " + externalBaiduInfo.getCode());
                            if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                                BaiduMapAccountImpl.this.mMapAccountCallback.bindSuccess();
                            }
                            break;
                        } else {
                            Log.e("BaiduMapAccountImpl", "绑定失败 且对应关系: oemid = [" + baiduBindInfo.getOemUid() + "],bduid = " + baiduBindInfo.getBdUid() + "  code = " + externalBaiduInfo.getCode());
                            if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                                BaiduMapAccountImpl.this.mMapAccountCallback.bindFailure("绑定失败");
                            }
                            break;
                        }
                        break;
                    case "LOGIN":
                        Log.e("BaiduMapAccountImpl", "百度登录成功 且对应关系: oemid = [" + baiduBindInfo.getOemUid() + "],bduid = " + baiduBindInfo.getBdUid() + "  extra = " + externalBaiduInfo.getExtra());
                        if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                            BaiduMapAccountImpl.this.mMapAccountCallback.loginSuccess(BaiduMapAccountImpl.FLAG_LOGIN_OR_LOGOUT_TOGETHER.equals(externalBaiduInfo.getExtra()) ? 1 : 0);
                            break;
                        }
                        break;
                    case "CHNANGE":
                        if (externalBaiduInfo.getCode() == 0) {
                            Log.e("BaiduMapAccountImpl", "换绑成功 且对应关系: oemid = [" + baiduBindInfo.getOemUid() + "],bduid = " + baiduBindInfo.getBdUid() + "  code = " + externalBaiduInfo.getCode());
                            if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                                BaiduMapAccountImpl.this.mMapAccountCallback.changeSuccess();
                            }
                            break;
                        } else {
                            Log.e("BaiduMapAccountImpl", "换绑失败 且对应关系: oemid = [" + baiduBindInfo.getOemUid() + "],bduid = " + baiduBindInfo.getBdUid() + "  code = " + externalBaiduInfo.getCode());
                            if (BaiduMapAccountImpl.this.mMapAccountCallback != null) {
                                BaiduMapAccountImpl.this.mMapAccountCallback.changeFailure("解绑失败");
                            }
                            break;
                        }
                        break;
                }
            }
        }
    }

    public BaiduMapAccountImpl(Context context) {
        this.mContext = context;
    }

    @Override // com.byd.map.account.IMapAccount
    public void bind(String str) {
        Log.e("BaiduMapAccountImpl", "[bind] baidu sdk init： " + this.isInit + "  oemId: " + str);
        if (!isInit()) {
            Log.e("BaiduMapAccountImpl", "[bind] baidu sdk not init");
            return;
        }
        ExternalBaiduInfo externalBaiduInfo = new ExternalBaiduInfo();
        externalBaiduInfo.setFuntype(State.BIND);
        externalBaiduInfo.setData(str);
        String json = this.mGson.toJson(externalBaiduInfo);
        Log.e("BaiduMapAccountImpl", "[bind] send baidu sdk massage ： " + json);
        AccountClientSdk.sendMessageToServer(json);
    }

    @Override // com.byd.map.account.IMapAccount
    public void change(String str) {
        Log.e("BaiduMapAccountImpl", "[change] baidu sdk init： " + this.isInit + "  oemId: " + str);
        if (!isInit()) {
            Log.e("BaiduMapAccountImpl", "[change] baidu sdk not init");
            return;
        }
        ExternalBaiduInfo externalBaiduInfo = new ExternalBaiduInfo();
        externalBaiduInfo.setFuntype(State.CHNANGE);
        externalBaiduInfo.setData(str);
        String json = this.mGson.toJson(externalBaiduInfo);
        Log.e("BaiduMapAccountImpl", "[change] send baidu sdk massage ： " + json);
        AccountClientSdk.sendMessageToServer(json);
    }

    @Override // com.byd.map.account.IMapAccount
    public MapAccountInfo getBindInfoByMapId(String str) {
        Log.e("BaiduMapAccountImpl", "[getBindInfoByMapId] baidu sdk init： " + this.isInit + " mapId: " + str);
        MapAccountInfo mapAccountInfo = null;
        if (isInit()) {
            ExternalBaiduInfo externalBaiduInfo = new ExternalBaiduInfo();
            externalBaiduInfo.setFuntype(State.BAIDU_QUERY);
            externalBaiduInfo.setData(str);
            String strSendMessageToServer = AccountClientSdk.sendMessageToServer(this.mGson.toJson(externalBaiduInfo));
            BaiduBindInfo baiduBindInfo = !TextUtils.isEmpty(strSendMessageToServer) ? (BaiduBindInfo) this.mGson.fromJson(strSendMessageToServer, BaiduBindInfo.class) : null;
            if (baiduBindInfo != null) {
                MapAccountInfo mapAccountInfo2 = new MapAccountInfo();
                mapAccountInfo2.setDisplayName(baiduBindInfo.getBdDisplayName());
                mapAccountInfo2.setPortrait(baiduBindInfo.getBdPortrait());
                mapAccountInfo2.setUip(baiduBindInfo.getBdUid());
                mapAccountInfo2.setOemUid(baiduBindInfo.getOemUid());
                mapAccountInfo = mapAccountInfo2;
            }
        } else {
            Log.e("BaiduMapAccountImpl", "[getBindInfoByMapId] baidu sdk not init");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[getBindInfoByMapId] mapAccountInfo : ");
        sb.append(mapAccountInfo == null ? BootBusinessManager.SUB_BUSINESS_NULL : mapAccountInfo.toString());
        Log.e("BaiduMapAccountImpl", sb.toString());
        return mapAccountInfo;
    }

    @Override // com.byd.map.account.IMapAccount
    public MapAccountInfo getBindInfoByOemId(String str) {
        Log.e("BaiduMapAccountImpl", "[getBindInfoByOemId] baidu sdk init： " + this.isInit + "  oemId: " + str);
        MapAccountInfo mapAccountInfo = null;
        if (isInit()) {
            ExternalBaiduInfo externalBaiduInfo = new ExternalBaiduInfo();
            externalBaiduInfo.setFuntype(State.OEM_QUERY);
            externalBaiduInfo.setData(str);
            String strSendMessageToServer = AccountClientSdk.sendMessageToServer(this.mGson.toJson(externalBaiduInfo));
            BaiduBindInfo baiduBindInfo = !TextUtils.isEmpty(strSendMessageToServer) ? (BaiduBindInfo) this.mGson.fromJson(strSendMessageToServer, BaiduBindInfo.class) : null;
            if (baiduBindInfo != null) {
                MapAccountInfo mapAccountInfo2 = new MapAccountInfo();
                mapAccountInfo2.setDisplayName(baiduBindInfo.getBdDisplayName());
                mapAccountInfo2.setPortrait(baiduBindInfo.getBdPortrait());
                mapAccountInfo2.setUip(baiduBindInfo.getBdUid());
                mapAccountInfo2.setOemUid(baiduBindInfo.getOemUid());
                mapAccountInfo = mapAccountInfo2;
            }
        } else {
            Log.e("BaiduMapAccountImpl", "[getBindInfoByOemId] baidu sdk not init");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[getBindInfoByOemId] mapAccountInfo : ");
        sb.append(mapAccountInfo == null ? BootBusinessManager.SUB_BUSINESS_NULL : mapAccountInfo.toString());
        Log.e("BaiduMapAccountImpl", sb.toString());
        return mapAccountInfo;
    }

    @Override // com.byd.map.account.IMapAccount
    public MapAccountInfo getMapAccountInfo() {
        Log.e("BaiduMapAccountImpl", "[getMapAccountInfo] baidu sdk init： " + this.isInit);
        if (!isInit()) {
            Log.e("BaiduMapAccountImpl", "[getMapAccountInfo] baidu sdk not init");
            return null;
        }
        BaiDuAccountInfo baiDuAccountInfo = AccountClientSdk.getBaiDuAccountInfo();
        StringBuilder sb = new StringBuilder();
        sb.append("baiDuAccountInfo : ");
        sb.append(baiDuAccountInfo == null ? BootBusinessManager.SUB_BUSINESS_NULL : this.mGson.toJson(baiDuAccountInfo));
        Log.e("BaiduMapAccountImpl", sb.toString());
        if (baiDuAccountInfo == null || baiDuAccountInfo.getErrorCode() != 0) {
            return null;
        }
        MapAccountInfo mapAccountInfo = new MapAccountInfo();
        mapAccountInfo.setDisplayName(baiDuAccountInfo.getDisplayname());
        mapAccountInfo.setPortrait(baiDuAccountInfo.getPortrait());
        mapAccountInfo.setUip(baiDuAccountInfo.getUid());
        return mapAccountInfo;
    }

    @Override // com.byd.map.account.IMapAccount
    public void init(MapAccountCallback mapAccountCallback) {
        Log.e("BaiduMapAccountImpl", "baidu SDK init...");
        Log.e("BaiduMapAccountImpl", "baidu SDK init mContext : " + this.mContext);
        this.mMapAccountCallback = mapAccountCallback;
        AccountClientSdk.init(this.mContext, this.mInitListener);
    }

    @Override // com.byd.map.account.IMapAccount
    public boolean isInit() {
        return this.isInit;
    }

    @Override // com.byd.map.account.IMapAccount
    public boolean isLoggedIn() {
        Log.e("BaiduMapAccountImpl", "[isLoggedIn] baidu sdk init： " + this.isInit);
        BaiDuAccountInfo baiDuAccountInfo = AccountClientSdk.getBaiDuAccountInfo();
        return (!isInit() || baiDuAccountInfo == null || TextUtils.isEmpty(baiDuAccountInfo.getUid())) ? false : true;
    }

    @Override // com.byd.map.account.IMapAccount
    public void login() {
        String str;
        Log.e("BaiduMapAccountImpl", "[login] baidu sdk init： " + this.isInit);
        if (!isInit()) {
            str = "[login] baidu sdk not init";
        } else {
            if (!AccountClientSdk.isBaiduAccountLogged()) {
                ExternalBaiduInfo externalBaiduInfo = new ExternalBaiduInfo();
                externalBaiduInfo.setFuntype(State.BAIDU_LOGIN);
                String json = this.mGson.toJson(externalBaiduInfo);
                Log.e("BaiduMapAccountImpl", "[login] send baidu sdk massage ： " + json);
                AccountClientSdk.sendMessageToServer(json);
                return;
            }
            str = "[login] baidu sdk has already logged in";
        }
        Log.e("BaiduMapAccountImpl", str);
    }

    @Override // com.byd.map.account.IMapAccount
    public void loginTogether(String str) {
        Log.e("BaiduMapAccountImpl", "[loginTogether] baidu sdk init： " + this.isInit + "  oemId : " + str);
        if (!isInit()) {
            Log.e("BaiduMapAccountImpl", "[loginTogether] baidu sdk not init");
            return;
        }
        ExternalBaiduInfo externalBaiduInfo = new ExternalBaiduInfo();
        externalBaiduInfo.setFuntype(State.LOGIN);
        externalBaiduInfo.setData(str);
        String json = this.mGson.toJson(externalBaiduInfo);
        Log.e("BaiduMapAccountImpl", "[loginTogether] send baidu sdk massage ： " + json);
        AccountClientSdk.sendMessageToServer(json);
    }

    @Override // com.byd.map.account.IMapAccount
    public void logoutTogether(String str) {
        Log.e("BaiduMapAccountImpl", "[logoutTogether] baidu sdk init： " + this.isInit + "   oemId: " + str);
        if (!isInit()) {
            Log.e("BaiduMapAccountImpl", "[logoutTogether] baidu sdk not init");
            return;
        }
        ExternalBaiduInfo externalBaiduInfo = new ExternalBaiduInfo();
        externalBaiduInfo.setFuntype(State.LOGOUT);
        externalBaiduInfo.setData(str);
        String json = this.mGson.toJson(externalBaiduInfo);
        Log.e("BaiduMapAccountImpl", "[logoutTogether] send baidu sdk massage ： " + json);
        AccountClientSdk.sendMessageToServer(json);
    }

    @Override // com.byd.map.account.IMapAccount
    public void unbind(String str) {
        Log.e("BaiduMapAccountImpl", "[unbind] baidu sdk init： " + this.isInit + "   oemId：" + str);
        if (!isInit()) {
            Log.e("BaiduMapAccountImpl", "[unbind] baidu sdk not init");
            return;
        }
        ExternalBaiduInfo externalBaiduInfo = new ExternalBaiduInfo();
        externalBaiduInfo.setFuntype(State.UNBIND);
        externalBaiduInfo.setData(str);
        String json = this.mGson.toJson(externalBaiduInfo);
        Log.e("BaiduMapAccountImpl", "[unbind] send baidu sdk massage ： " + json);
        AccountClientSdk.sendMessageToServer(json);
    }
}
