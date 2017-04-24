package com.stop.zparkingzj;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.GridLayoutManager;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.View;
        import android.widget.Toast;

        import com.google.gson.Gson;
        import com.stop.zparkingzj.activity.Admin2Activity;
        import com.stop.zparkingzj.activity.BaseActivity;
        import com.stop.zparkingzj.activity.EscapeRecordActivity;
        import com.stop.zparkingzj.adapter.MainRecyclerAdapter;
        import com.stop.zparkingzj.api.lmpl.BaseSubscriber;
        import com.stop.zparkingzj.api.lmpl.LoginNfcInteractor;
        import com.stop.zparkingzj.api.lmpl.ParkingOrderInteractor;
        import com.stop.zparkingzj.api.lmpl.PartInteractor;
        import com.stop.zparkingzj.bean.Config;
        import com.stop.zparkingzj.bean.ParkingOrderListBean;
        import com.stop.zparkingzj.bean.PartBaseInfoBean;
        import com.stop.zparkingzj.bean.PartSeatBean;
        import com.stop.zparkingzj.bean.UIsBean;
        import com.stop.zparkingzj.databinding.ActivityMainBinding;
        import com.stop.zparkingzj.http.RetrofitHttp;
        import com.stop.zparkingzj.util.LongTimeOrString;
        import com.stop.zparkingzj.util.SharedPreferencesUtils;

        import java.sql.Time;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.Iterator;
        import java.util.List;
        import java.util.Map;
        import java.util.Timer;
        import java.util.TimerTask;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    //屏蔽home键
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    private ActivityMainBinding binding;
    private BaseActivity activity;
    private MainRecyclerAdapter adapter;

    //网络工具类
    private PartInteractor partInteractor;
    private ParkingOrderInteractor parkingOrderInteractor;
    private LoginNfcInteractor loginNfcInteractor;

    //网络通信拿取数据
    private PartBaseInfoBean baseInfoBean;
    private PartSeatBean partSeatBean;
    private ParkingOrderListBean parkingOrderListBean;
    private UIsBean uIsBean;


    //界面需要刷新的时间key
    private static final String UPDATAUI_TIME = "updata_time";
    private boolean isTimeNotify = false;//界面刷新要更新adapter

    //时间
    private ArrayList<UIsBean.UIBean> upTimeData;
    public Timer mTime;
    private Long time;
    public boolean isTask = false;
    public TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (upTimeData!=null&&upTimeData.size()!=0){
                for (int i=0;i<upTimeData.size();i++){
                    UIsBean.UIBean uiBean = upTimeData.get(i);
                    Long parkingTime = uiBean.getParkingTime();
                    if (parkingTime==0L){
                        uiBean.setStringParkingTime("空 闲");
                    }else {
                        uiBean.setStringParkingTime(LongTimeOrString.stringStopTime_MainActivity(parkingTime,time));
                    }

                }
                time = time +1000;
            }
        }
    };;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Intent intent) {
        binding = (ActivityMainBinding) view;
        activity = this;
        activitys.add(activity);


        //停车项目信息
        partInteractor = appComponent.getPartInteractor();
        //停车场信息
        parkingOrderInteractor = appComponent.getParkingOrderInteractor();
        //用户信息
        loginNfcInteractor = appComponent.getLoginNfcInteractor();

        //UI数据
        uIsBean = appComponent.getUIsBean();
        time = new Date().getTime();
        //需要计时的车位数据存储
        upTimeData = uIsBean.getUpTimeData();
        //第一次进入
        isTimeNotify = false;
        //加入当前时间
        SharedPreferencesUtils.setParam(activity,UPDATAUI_TIME,time);

    }

    @Override
    protected void initView() {
        Gson gson = new Gson();
        //拿取地区信息 BaseInfo.do
        downBaseInfo(gson);
        initOnClick();

    }

    private void downBaseInfo(final Gson gson) {

        partInteractor.partInfo("BaseInfo.do", "", new BaseSubscriber<String>(this) {
            @Override
            protected void onSuccess(String result) {
                baseInfoBean = gson.fromJson(result,PartBaseInfoBean.class);
                String title = baseInfoBean.getData().getName();
                SharedPreferencesUtils.setParam(activity, Config.STOPTITLE,title);
                binding.mainTitle.setText(title);
                //拿取车位信息 Seat/BaseInfo.do
                downSeatBaseInfo(gson);
            }
        });

    }

    private void downSeatBaseInfo(final Gson gson) {
        partInteractor.partInfo("Seat","BaseInfo.do", "", new BaseSubscriber<String>(this) {
            @Override
            protected void onSuccess(String result) {
                partSeatBean = gson.fromJson(result, PartSeatBean.class);
                Log.i("Bean","partSeatBean加载成功!");
                //没有处理的订单信息
                downList(gson);
            }
        });
    }

    private void downList(final Gson gson) {
        parkingOrderInteractor.parkingOrderInfo("List.do", "", new BaseSubscriber<String>(this) {
            @Override
            protected void onSuccess(String result) {
                Log.i("Bean","parkingOrderListBean加载成功!");
                parkingOrderListBean = gson.fromJson(result, ParkingOrderListBean.class);
                downUserInfo(gson);
            }
        });
    }

    private void downUserInfo(final Gson gson) {
        loginNfcInteractor.loginFnc("LoginWorkerDetail.do", "", new BaseSubscriber<String>(this) {
            @Override
            protected void onSuccess(String result) {
                UIsBean.UserInfo userInfo = gson.fromJson(result, UIsBean.UserInfo.class);
                userInfo.setTime(new Date().getTime());
                if (uIsBean.getUserInfo()!=null){
                    uIsBean.setUserInfo(null);
                }
                uIsBean.setUserInfo(userInfo);
                if (partSeatBean.getDatas() != null){
                    UpDataUI();
                    return;
                }
                Toast.makeText(activity,"网络发生未知错误，请把程序退出，重新登录!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpDataUI() {
        Log.i("Bean","加载UI!");
        ArrayList<UIsBean.UIBean> lists = new ArrayList<>();
        //停车场车位信息(先写入数据)
        ArrayList<PartSeatBean.DatasBean> partDatas = partSeatBean.getDatas();
        //未完成订单信息(后修改数据)
        List<ParkingOrderListBean.DatasBean> orderDatas = parkingOrderListBean.getDatas();

        Log.i("Bean","未处理数据数量："+orderDatas.size());

        upTimeData.clear();
        if (uIsBean.getLists()!=null){
            uIsBean.setLists(null);
        }

        //把UI的模型定制好
        for (int i=0;i<partDatas.size();i++){
            PartSeatBean.DatasBean partData = partDatas.get(i);
            UIsBean.UIBean uiBean = new UIsBean.UIBean(partData.getParkSeatId(),
                    partData.getSeatNo(),1000000,
                    activity, View.GONE,"",partData.getIsParking(),
                    "","",0L,0L);
            lists.add(uiBean);
        }

        //UI的数据加入(未完成的订单)
        Map<Integer,ParkingOrderListBean.DatasBean> map = new HashMap<>();
        if (orderDatas!=null&&orderDatas.size()>0) {
            //消息体为空 订单号为0 不处理删除
            ParkingOrderListBean.DatasBean datasBean = orderDatas.get(0);
            if (datasBean.getParkingOrderId() == 0){
                orderDatas.remove(0);
            }

            for (int i = 0; i < orderDatas.size(); i++) {
                ParkingOrderListBean.DatasBean orderData = orderDatas.get(i);
                int orderId = orderData.getParkSeatId();
                //筛选 取最新的
                map.put(orderId, orderData);
            }
            //对比 把未处理的订单数据加入list
            Iterator<Map.Entry<Integer, ParkingOrderListBean.DatasBean>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, ParkingOrderListBean.DatasBean> next = iterator.next();
                Integer orderId = next.getKey();
                ParkingOrderListBean.DatasBean orderData = next.getValue();
                for (int i = 0; i < lists.size(); i++) {
                    UIsBean.UIBean uiBean = lists.get(i);
                    int parkSeatId = uiBean.getParkSeatId();
                    //利用车位Id对比
                    if (orderId == parkSeatId) {
                        Log.i("ParkingWebSocket", "orderId:" + orderId + "");
                        uiBean.setParkingOrderId(orderData.getParkingOrderId());
                        uiBean.setPayStatus(orderData.getPayStatus());
                        uiBean.setVehicleNo(orderData.getVehicleNo());
                        uiBean.setParkingTime(orderData.getParkingTime());
                        //存在订单
                        uiBean.setOrder(true);
                        if (orderData.getVehicleNo() == null){
                            uiBean.setIsParking("yes");
                            uiBean.setVehicleNo("");
                            uiBean.setIsVisual(View.VISIBLE);
                        }else {
                            uiBean.setIsParking("yes_photo");
                            uiBean.setIsVisual(View.GONE);
                        }
                        upTimeData.add(uiBean);
                        isTask = true;
                        //时间
                        if (mTime == null){
                            mTime = new Timer();
                            mTime.schedule(mTimerTask,1000,1000);
                        }
                    }
                }
            }
        }
        uIsBean.setLists(lists);
        Log.i("Bean","list:"+uIsBean.getLists().size());
        //把数据写入adapter
        initAdapter();
        Log.i("Bean","数据写入成功!");
        if (RetrofitHttp.isWebSocketState()){
            Log.i("Bean","打开Socket!");
            RetrofitHttp.openWebSocket(uIsBean,activity);
        }

    }

    private void initAdapter() {
        if (isTimeNotify) {
            adapter.notifyDataSetChanged();
        }
        else {
            binding.mRecyclerView.setLayoutManager(new GridLayoutManager(activity,2));
            adapter = new MainRecyclerAdapter(uIsBean);
            binding.mRecyclerView.setAdapter(adapter);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Bean","onResume:");
        Long outTime = (Long) SharedPreferencesUtils.getParam(activity, UPDATAUI_TIME, 0L);
        long thisTime = new Date().getTime();
        long timeDifference = thisTime - outTime;
        int ss = (int) (timeDifference / 1000);//总共的秒数
        Log.i("Bean","界面离开时间:"+ss);
        if (ss>=1800){
            //重新写入界面刷新时间
            SharedPreferencesUtils.setParam(activity,UPDATAUI_TIME,thisTime);
            Gson gson = new Gson();
            //拿取地区信息 BaseInfo.do
            //清除数据
            isTimeNotify = true;
            downBaseInfo(gson);

        }

    }

//    public void startTimer(){
//        if (mTime == null){
//            mTime = new Timer();
//        }
//        if (mTimerTask == null){
//            mTimerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    if (upTimeData!=null&&upTimeData.size()!=0){
//                        for (int i=0;i<upTimeData.size();i++){
//                            UIsBean.UIBean uiBean = upTimeData.get(i);
//                            Long parkingTime = uiBean.getParkingTime();
//                            if (parkingTime==0L){
//                                uiBean.setStringParkingTime("空 闲");
//                            }else {
//                                uiBean.setStringParkingTime(LongTimeOrString.stringStopTime_MainActivity(parkingTime,time));
//                            }
//
//                        }
//                        time = time +1000;
//                    }
//                }
//            };
//        }
//
//        if(mTime!= null && mTimerTask != null ){
//            mTime.schedule(mTimerTask,1000,1000);
//        }
//
//    }
//    public void stopTime(){
//        if (mTimerTask!=null){
//            mTimerTask.cancel();
//            mTimerTask = null;
//        }
//        if (mTime!=null){
//            mTime.cancel();
//            mTime = null;
//        }
//    }

    private void initOnClick() {
        binding.mainEscapeRecord.setOnClickListener(this);
        binding.mainMenu.setOnClickListener(this);
        binding.mainUpdata.setOnClickListener(this);
    }

    //数字键盘监听
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_HOME){
            Intent intent = new Intent(this,Admin2Activity.class);
            startActivity(intent);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK){
        }
        return true;
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.main_menu:
                intent = new Intent(this,Admin2Activity.class);
                break;
            case R.id.main_escapeRecord:
                intent = new Intent(this,EscapeRecordActivity.class);
                break;
            case R.id.main_updata:
                SharedPreferencesUtils.setParam(activity,UPDATAUI_TIME,new Date().getTime());
                Gson gson = new Gson();
                //拿取地区信息 BaseInfo.do
                //清除数据
                isTimeNotify = true;
                time = new Date().getTime();
                downBaseInfo(gson);
                break;

        }
        if (intent!=null){
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //      关闭通信
        if (mTime!=null){
            mTime.cancel();
            mTimerTask.cancel();
            mTime = null;
            mTimerTask = null;
        }
        RetrofitHttp.stopWebSocket();
    }
}
