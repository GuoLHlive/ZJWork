package com.stop.zparkingzj.bean;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;


import com.stop.zparkingzj.BR;
import com.stop.zparkingzj.MyApp;
import com.stop.zparkingzj.activity.PayActivity;
import com.stop.zparkingzj.api.lmpl.BaseSubscriber;
import com.stop.zparkingzj.api.lmpl.ParkingOrderInteractor;
import com.stop.zparkingzj.module.AppComponent;
import com.stop.zparkingzj.util.LongTimeOrString;
import com.stop.zparkingzj.util.StringForJson;
import com.stop.zparkingzj.util.showToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/9.
 */
public class UIsBean {

    public UIsBean() {
    }



    //用户数据
    private UserInfo userInfo;
    public UserInfo getUserInfo() {
        return userInfo;
    }
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    public static class UserInfo{

        /**
         * success : true
         * date : 2017-02-25 17:16:45
         * type : data
         * code : 0
         * data : {"workerId":0,"workerNo":"100000","name":"张三","workCardNo":"048c39b2412d80","lastLoginTime":1488014195351}
         */

        private boolean success;
        private String date;
        private String type;
        private String code;
        private DataBean data;
        private Long time;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public static class DataBean {
            /**
             * workerId : 0
             * workerNo : 100000
             * name : 张三
             * workCardNo : 048c39b2412d80
             * lastLoginTime : 1488014195351
             */

            private int workerId;
            private String workerNo;
            private String name;
            private String workCardNo;
            private long lastLoginTime;

            public int getWorkerId() {
                return workerId;
            }

            public void setWorkerId(int workerId) {
                this.workerId = workerId;
            }

            public String getWorkerNo() {
                return workerNo;
            }

            public void setWorkerNo(String workerNo) {
                this.workerNo = workerNo;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getWorkCardNo() {
                return workCardNo;
            }

            public void setWorkCardNo(String workCardNo) {
                this.workCardNo = workCardNo;
            }

            public long getLastLoginTime() {
                return lastLoginTime;
            }

            public void setLastLoginTime(long lastLoginTime) {
                this.lastLoginTime = lastLoginTime;
            }
        }
    }


    //UI数据
    private ArrayList<UIBean> lists;
    public ArrayList<UIBean> getLists() {
        return lists;
    }
    public void setLists(ArrayList<UIBean> lists) {
        this.lists = lists;
    }

    //实体数据 有订单的数据
    private ArrayList<UIBean> upTimeData;

    public ArrayList<UIBean> getUpTimeData() {
        if (upTimeData == null){
            upTimeData = new ArrayList<>();
        }
        return upTimeData;
    }

    public void setUpTimeData(ArrayList<UIBean> upTimeData) {
        this.upTimeData = upTimeData;
    }

    public static class UIBean extends BaseObservable{

        //对应车位Id
        private int parkSeatId;
        //订单号Id
        private int parkingOrderId;
        //车位号
        private String seatNo;
        //车牌
        private String vehicleNo;
        //当前状态（小红点）
        private int isVisual;
        //小红点里面文字
        private String dotText;
        //背景颜色（是否在停）
        private String isParking;
        //上下文
        private Context context;
        //是否支付
        private String payStatus;
        //驻车时间
        private Long parkingTime;
        //离开时间
        private Long leaveTime;
        //Txt时间
        private String stringParkingTime;
        //离开时间
        private String stringLeaveTime;
        //是否已经创建订单
        private boolean isOrder = false;

        private PayOnClick payOnClick;


        public UIBean(int parkSeatId, String seatNo, int parkingOrderId, Context context, int isVisual, String dotText, String isParking, String vehicleNo, String payStatus, Long parkingTime, Long leaveTime) {
            this.parkSeatId = parkSeatId;
            this.parkingOrderId = parkingOrderId;
            this.context = context;
            this.isVisual = isVisual;
            this.dotText = dotText;
            this.isParking = isParking;
            this.vehicleNo = vehicleNo;
            this.payStatus = payStatus;
            this.parkingTime = parkingTime;
            this.leaveTime = leaveTime;
            this.seatNo = seatNo;
        }

        public int getParkSeatId() {
            return parkSeatId;
        }

        public void setParkSeatId(int parkSeatId) {
            this.parkSeatId = parkSeatId;
        }

        public int getParkingOrderId() {
            return parkingOrderId;
        }

        public void setParkingOrderId(int parkingOrderId) {
            this.parkingOrderId = parkingOrderId;
        }

        public String getSeatNo() {
            return seatNo;
        }

        public void setSeatNo(String seatNo) {
            this.seatNo = seatNo;
        }

        @Bindable
        public String getVehicleNo() {
            return vehicleNo;
        }

        public void setVehicleNo(String vehicleNo) {
            this.vehicleNo = vehicleNo;
            notifyPropertyChanged(BR.vehicleNo);
        }

        @Bindable
        public int getIsVisual() {
            return isVisual;
        }

        public void setIsVisual(int isVisual) {
            this.isVisual = isVisual;
            notifyPropertyChanged(BR.isVisual);
        }

        public String getDotText() {
            if ("".equals(dotText)){
                dotText = "拍";
            }
            return dotText;
        }

        public void setDotText(String dotText) {
            this.dotText = dotText;
        }
        @Bindable
        public String getIsParking() {
            return isParking;
        }

        public void setIsParking(String isParking) {
            this.isParking = isParking;
            notifyPropertyChanged(BR.isParking);
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
        }

        public Long getParkingTime() {
            return parkingTime;
        }

        public void setParkingTime(Long parkingTime) {
            this.parkingTime = parkingTime;

        }

        public Long getLeaveTime() {
            return leaveTime;
        }

        public void setLeaveTime(Long leaveTime) {
            this.leaveTime = leaveTime;
        }
        @Bindable
        public String getStringParkingTime() {
            if (parkingTime == 0L){
                stringParkingTime = "空 闲";
            }
            return stringParkingTime;
        }

        public void setStringParkingTime(String stringParkingTime) {
            this.stringParkingTime = stringParkingTime;
            notifyPropertyChanged(BR.stringParkingTime);
        }

        public String getStringLeaveTime() {
            return stringLeaveTime;
        }

        public void setStringLeaveTime(String stringLeaveTime) {
            this.stringLeaveTime = stringLeaveTime;
        }

        public boolean getIsOrder() {
            return isOrder;
        }

        public void setOrder(boolean order) {
            isOrder = order;
        }

        @Bindable
        public PayOnClick getPayOnClick() {
            if (payOnClick == null){
                payOnClick = new PayOnClick(getThisDemo());
            }
            return payOnClick;
        }

        public void setPayOnClick(PayOnClick payOnClick) {
            this.payOnClick = payOnClick;
        }
        public UIBean getThisDemo(){
            return this;
        }
    }


    public static class PayOnClick implements View.OnClickListener{

        private UIBean uiBean;
        public PayOnClick(UIBean uiBean) {
            this.uiBean = uiBean;
        }
        @Override
        public void onClick(View view) {
            boolean isOrder = uiBean.getIsOrder();
            Context context = uiBean.getContext();
            if (isOrder){
                //存在订单
                Intent intent = new Intent(context,PayActivity.class);
                intent.putExtra("parkingOrderId",uiBean.getParkingOrderId());
                context.startActivity(intent);
            }else {
                Listener listener = new Listener(uiBean);
                EscLinstener escLinstener = new EscLinstener(listener);
                new AlertDialog.Builder(context).setTitle("是否创建订单").setPositiveButton("确定",listener).setNegativeButton("取消",escLinstener).create().show();
            }

        }
    }


    public static class Listener implements DialogInterface.OnClickListener{


        private AppComponent appComponent;
        private ParkingOrderInteractor parkingOrderInteractor;
        private UIBean uiBean;
        private Context context;
        private Map<Integer,String> payState;
        public Listener(UIBean uiBean) {
            this.uiBean = uiBean;
            this.context = uiBean.getContext();
            this.appComponent = MyApp.getApp(context).component();
            this.parkingOrderInteractor = appComponent.getParkingOrderInteractor();
            this.payState = MyApp.getPayState();
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (parkingOrderInteractor !=null){
                parkingOrderInteractor.parkingOrderInfo("Create.do", StringForJson.OneDataForJson("parkSeatId", uiBean.getParkSeatId()), new BaseSubscriber<String>(context) {
                    @Override
                    protected void onSuccess(String result) {
                        try {
                            JSONObject json = new JSONObject(result);
                            JSONObject json_data = json.getJSONObject("data");
                            int parkingOrderId = json_data.getInt("parkingOrderId");
                            long parkingTime = json_data.getLong("parkingTime");
                            String payStatus = json_data.getString("payStatus");


                            uiBean.setParkingTime(parkingTime);
                            uiBean.setOrder(true);
                            uiBean.setIsParking("yes");
                            uiBean.setPayStatus(payStatus);
                            uiBean.setParkingOrderId(parkingOrderId);
                            uiBean.setIsVisual(View.VISIBLE);
                            uiBean.setStringParkingTime(LongTimeOrString.stringStopTime_MainActivity(parkingTime,new Date().getTime()));

                            UIsBean uIsBean = appComponent.getUIsBean();
                            ArrayList<UIBean> upTimeData = uIsBean.getUpTimeData();
                            upTimeData.add(uiBean);
                            payState.put(parkingOrderId,Config.READPAY);

                            Intent intent = new Intent(context,PayActivity.class);
                            intent.putExtra("parkingOrderId",uiBean.getParkingOrderId());
                            context.startActivity(intent);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast.showToastTxt(context,"创建订单失败");
                        }

                    }
                });
            }
        }
    }
    public static class EscLinstener implements DialogInterface.OnClickListener{
        private Listener listener;

        public EscLinstener(Listener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (listener!=null){
                listener = null;
            }

        }
    }
}
