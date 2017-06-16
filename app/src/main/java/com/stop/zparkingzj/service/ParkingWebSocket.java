package com.stop.zparkingzj.service;


import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;


import com.stop.zparkingzj.MainActivity;
import com.stop.zparkingzj.MyApp;
import com.stop.zparkingzj.activity.BaseActivity;
import com.stop.zparkingzj.bean.UIsBean;
import com.stop.zparkingzj.http.RetrofitHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;


/**
 * Created by Administrator on 2017/2/8.
 */
public class ParkingWebSocket implements WebSocketListener {

    private WebSocket mWebSocket = null;
    private OkHttpClient client;
    private UIsBean uIsBean;


    private static final String url = "ws://zhanjiang.zparking.cn:80/ZparkingWorkerManagerWeb/common.do";

//    private static final String url = "ws://192.168.1.48:8091/ZparkingWorkerManagerWeb/common.do";
    private static final String TAG = "ParkingWebSocket";


    private BaseActivity activity;

    public ParkingWebSocket(OkHttpClient client) {
        this.client = client;

    }

    public void openWebSocket(UIsBean uIsBean,BaseActivity activity){
        Request request = new Request.Builder().url(url).build();
        WebSocketCall.create(client,request).enqueue(this);
        Log.i(TAG,"openWebSocket:");
        this.uIsBean = uIsBean;
        this.activity = activity;

    }

    public void closeWebSocket(){
        try {
            mWebSocket.close(1000,"");
            //重置
            RetrofitHttp.ResetWebSocket();
        } catch (Exception e) {
            Log.e(TAG,"close:"+
                    e.getMessage());
        }
    }


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
            mWebSocket = webSocket;
    }

    @Override
    public void onFailure(IOException e, Response response) {
        Log.e(TAG,"IOException:"+e.getMessage());
        this.closeWebSocket();
    }

    @Override
    public void onMessage(ResponseBody message) throws IOException {
        String string = message.string().trim();
        Log.i(TAG,"onMessage:"+string);
        if (!"".equals(string)){
            //默认初始停车场Id

            try {
                JSONObject json = new JSONObject(string);
                String routerStr = json.getString("routerStr");

                //先判断消息体属于地磁还是订单
                if ("detector.status".equals(routerStr)){
                    Log.i(TAG,"--地磁信息进入--");
//                    ArrayList<PartSeatBean.DatasBean> datas = partSeatBean.getDatas();
//                    Log.i(TAG,"onMessage:"+datas.toString());

//                    Log.i(TAG,"数据长度:"+datas.size());
//                    //地磁
//                    //根据seatId 修改
                    String seatId = json.getString("seatId");
                    int stopId = Integer.valueOf(seatId);
                    String detect = json.getString("detect");
                    //车位界面全部数据
                    ArrayList<UIsBean.UIBean> lists = uIsBean.getLists();
                   if (lists!=null&&lists.size()!=0){
                       for (int i=lists.size()-1;i>=0;i--){
                           UIsBean.UIBean uiBean = lists.get(i);
                           int parkSeatId = uiBean.getParkSeatId();
//                           Log.i(TAG,"sectionId:"+parkSeatId);
                            if (parkSeatId==stopId){
                                //修改当前状态
                                String isParking = isParkingOrString(detect);
                                uiBean.setIsParking(isParking);
                                Log.i(TAG,"seatId:"+stopId);
                                if ("yes".equals(isParking)){
                                    uiBean.setOrder(true);
                                    int count = 0;
                                    ArrayList<UIsBean.UIBean> upTimeData = uIsBean.getUpTimeData();
                                    for (int k = 0; k < upTimeData.size(); k++) {
                                        UIsBean.UIBean uiBean1 = upTimeData.get(k);
                                        if (uiBean1.getParkSeatId() == stopId){
                                            count++;
                                        }
                                    }
                                    Log.i(TAG,"count"+count);
                                    if (count==0){
                                        uIsBean.getUpTimeData().add(uiBean);
                                        //铃声
                                        soundRing(activity);
                                    }

                                }else if ("no".equals(isParking)){
                                    uiBean.setOrder(false);
                                    uIsBean.getUpTimeData().remove(uiBean);
                                }
                            }
                       }

                   }



                }else if ("parking_order".equals(routerStr)){
                    //订单
                    Log.i(TAG,"--订单信息进入--");
                    String isParking = json.getString("isParking");
                    int parkingOrderId = json.getInt("parkingOrderId");
                    String payStatus = json.getString("payStatus");
                    Long parkingTime = json.getLong("parkingTime");
                    String vehicleNo = json.getString("vehicleNo");
                    int seatId = json.getInt("seatId");
                    Long leaveTime = 0L;



                    ArrayList<UIsBean.UIBean> lists = uIsBean.getLists();
                    if (lists!=null&&lists.size()!=0){
                        for (int i=0;i<lists.size();i++){
                            UIsBean.UIBean uiBean = lists.get(i);
                            int parkSeatId = uiBean.getParkSeatId();
//                            Log.i(TAG,"parkSeatId:"+parkSeatId);
                            if (parkSeatId==seatId){
                                //修改当前状态
                                Log.i(TAG,isParking+"isParking");
                                switch (isParking){
                                    case "no":
                                        leaveTime = json.getLong("leaveTime");
                                        parkingTime = 0L;
                                        uiBean.setIsVisual(View.GONE);
                                        uiBean.setStringParkingTime("空 闲");
                                        vehicleNo = "";
//                                        Log.i("Bean","payStateSize"+payState.size()+"");
//                                        //删除payState里面的数据
//                                        Iterator<Map.Entry<Integer, String>> iterator = payState.entrySet().iterator();
//                                        while (iterator.hasNext()) {
//                                            Map.Entry<Integer, String> entry = iterator.next();
//                                            Integer key = entry.getKey();
//                                            Log.i("Bean", "Key:" + key + "");
//                                            if (key == parkingOrderId) {
//                                                iterator.remove();
//                                                Log.i("Bean", "payStateSize" + payState.size() + "");
//                                            }
//                                        }

                                        break;
                                    case "yes":

                                        if ("null".equals(vehicleNo)||"".equals(vehicleNo)){
                                            uiBean.setIsVisual(View.VISIBLE);
                                            vehicleNo = "";
                                        }else {
                                            uiBean.setIsVisual(View.GONE);
                                            uiBean.setIsParking("yes_photo");
                                        }
                                        Context context = uiBean.getContext();
                                        if (context instanceof MainActivity) {
                                            MainActivity mainActivity = (MainActivity) context;
                                            if (!mainActivity.isTask) {
                                                if (mainActivity.mTime == null) {
                                                    mainActivity.mTime = new Timer();
                                                    mainActivity.mTime.schedule(mainActivity.mTimerTask, 1000, 1000);
                                                }
                                            }
                                        }
                                        break;
                                }
                                uiBean.setParkingTime(parkingTime);
                                uiBean.setParkingOrderId(parkingOrderId);
                                uiBean.setVehicleNo(vehicleNo);
                                uiBean.setPayStatus(payStatus);
                                uiBean.setLeaveTime(leaveTime);
                            }
                        }

                    }


                }


            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG,"解析json失败:"+e.getMessage());
            }

        }

    }

    @Override
    public void onPong(Buffer payload) {
        Log.i(TAG,"onPong:"+payload.size());
    }

    @Override
    public void onClose(int code, String reason) {
        Log.i(TAG,"onClose:"+code+"*"+reason);
    }

    private String isParkingOrString(String detect){
        if ("exsist".equals(detect)){
            return "yes";
        }else {
            return "no";
        }
    }

    private void soundRing(Context context) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{

//        MediaPlayer mp = new MediaPlayer();
        Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, defaultUri);
        r.play();
        Log.i(TAG,defaultUri.toString());
        Log.i(TAG,"进车铃声~~~~");
//        mp.reset();
//        mp.setDataSource(context,
//                defaultUri);
//        mp.prepare();
//        mp.start();

    }

}
