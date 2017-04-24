package com.stop.zparkingzj.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.stop.zparkingzj.R;
import com.stop.zparkingzj.api.lmpl.BaseSubscriber;
import com.stop.zparkingzj.api.lmpl.LoginNfcInteractor;
import com.stop.zparkingzj.api.lmpl.ParkingOrderInteractor;
import com.stop.zparkingzj.bean.StatisticsBean;
import com.stop.zparkingzj.bean.UIsBean;
import com.stop.zparkingzj.bean.post.LogoutPostBean;
import com.stop.zparkingzj.databinding.ActivityAdmin2Binding;
import com.stop.zparkingzj.util.LongTimeOrString;

import java.util.Date;

/**
 * Created by Administrator on 2016/12/13.
 * 用户界面(正式)
 *
 */
public class Admin2Activity extends BaseActivity {

    private ActivityAdmin2Binding binding;

    private LoginNfcInteractor loginNfcInteractor;
    private ParkingOrderInteractor parkingOrderInteractor;
    private BaseActivity activity;
    private UIsBean uIsBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_admin2;
    }

    @Override
    protected void initData(Intent intent) {
        binding = (ActivityAdmin2Binding) view;
        activity =this;
        activitys.add(this);
        loginNfcInteractor = appComponent.getLoginNfcInteractor();
        //停车场信息
        parkingOrderInteractor = appComponent.getParkingOrderInteractor();
        uIsBean = appComponent.getUIsBean();
    }

    @Override
    protected void initView() {
        //返回按钮
        BackMain();
        //注销按钮
        LogOut();
        //UI信息
        downUserInfo();
        //统计
        downOrder();

    }

    private void downOrder() {
        parkingOrderInteractor.parkingOrderInfo("Statistics.do","", new BaseSubscriber<String>(activity) {
            @Override
            protected void onSuccess(String result) {
                Gson gson = new Gson();

                StatisticsBean statisticsBean = null;
                    statisticsBean = gson.fromJson(result, StatisticsBean.class);
                    writeInUi(statisticsBean.getData());
            }


        });

    }
    //写入统计信息
    private void writeInUi(StatisticsBean.DataBean data) {
        //车辆类型
//        StatisticsBean.DataBean.CarCountBean carCount = data.getCarCount();
        double sumAmt = data.getSumAmt();//总金额
        int parkingSeatCount = data.getParkingSeatCount();//停车次数
        int parkingTimes = data.getParkingTimes();//停车车次
        int sumSeatCount = data.getSumSeatCount();//总车位
        int escapeCount = data.getEscapeCount();//逃费次数
        Log.i("Bean",data.toString());
        int ordeCount = data.getOrdeCount();
        binding.adminFull.setText(sumAmt+"");
        binding.adminStopNumber.setText(parkingTimes+"");
        binding.adminThisStop.setText(parkingSeatCount+"/"+sumSeatCount);
        binding.adminOrder.setText(ordeCount+"");
        binding.adminFlee.setText(escapeCount+"");


    }

    private void downUserInfo() {
        UIsBean.UserInfo userInfo = uIsBean.getUserInfo();
        Long time = userInfo.getTime();
        UIsBean.UserInfo.DataBean data = userInfo.getData();
        if (data!=null){
            String name = data.getName();
            String workerNo = data.getWorkerNo();
            String mUiName = name+" 编号："+workerNo;
            String mTime = "登录时长："+ LongTimeOrString.stringStopTime_MainActivity(time,new Date().getTime());
            binding.adminNameNumber.setText(mUiName);
            binding.adminLoginTime.setText(mTime);
        }

    }

    private void LogOut() {

        binding.adminEsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutPostBean postBean = new LogoutPostBean(0.0,0.0);
                loginNfcInteractor.loginFnc("Logout.do", postBean.toString(), new BaseSubscriber<String>(null) {
                    @Override
                    protected void onSuccess(String result) {
                    }
                });
                int size = activitys.size();
                if (size!= 0){
                    for (Activity activity:activitys){
                        activity.finish();
                    }
                    activitys.clear();
                }
                Intent intent = new Intent(Admin2Activity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void BackMain() {
        binding.adminBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity baseActivity = activitys.get(activitys.size() - 1);
                baseActivity.finish();
                activitys.remove(activitys.size()-1);
            }
        });



    }
}
