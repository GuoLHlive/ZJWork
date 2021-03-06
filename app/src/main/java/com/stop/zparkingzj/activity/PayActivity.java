package com.stop.zparkingzj.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.stop.zparkingzj.R;
import com.stop.zparkingzj.api.lmpl.BaseSubscriber;
import com.stop.zparkingzj.api.lmpl.ParkingOrderInteractor;
import com.stop.zparkingzj.bean.Config;
import com.stop.zparkingzj.bean.ParkingOrderDetailBean;
import com.stop.zparkingzj.bean.ReadyPayDialogBean;
import com.stop.zparkingzj.bean.TakePhotoBean;
import com.stop.zparkingzj.bean.UIsBean;
import com.stop.zparkingzj.databinding.ActivityPayBinding;
import com.stop.zparkingzj.dev.IDevService;
import com.stop.zparkingzj.dev.msm8909.Msm8909DevService;
import com.stop.zparkingzj.module.DaggerDevComponent;
import com.stop.zparkingzj.module.DevComponent;
import com.stop.zparkingzj.util.BntNotRepeatClick;
import com.stop.zparkingzj.util.LongTimeOrString;
import com.stop.zparkingzj.util.SharedPreferencesUtils;
import com.stop.zparkingzj.util.StringForJson;
import com.stop.zparkingzj.util.showToast;
import com.stop.zparkingzj.view.CarNumberDialog;
import com.stop.zparkingzj.view.Pay_Dialog_View;
import com.stop.zparkingzj.view.ReadyPay_Dialog_View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/14.
 */
public class PayActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PayActivity--";
    private ActivityPayBinding binding;
    private BaseActivity activity;

    private ParkingOrderInteractor parkingOrderInteractor;
    private UIsBean uIsBean;
    private Map<Integer, String> payState;
    private Resources resources;

    //当前Activity 支付状态
    private String currentState = "";

    //打印依赖
    private DevComponent devComponent;
    private IDevService iDevService = null;


    private int parkingOrderId;
    private ParkingOrderDetailBean.DataBean data;
    private String stopSection; //路段

    //打印数据

    private String userName = "";//用户名字 用于打印
    private String stopSectionSeatNo = "";//路段名字+车位号 用于打印

    //车牌数据
    private String carCount;
    private String carNumber;
    private double carMoney;

    private String[] carTypeItems;
    private String[] carCounts;
    private String[] carNumberTypes;


    private AlertDialog carTypeDialog;
    private AlertDialog carNumberDialog;
    private AlertDialog carMsgDiaglog;

    private CarNumberDialog carNumberWrite;
    private String dialogMoney;//收费提示框该收费金额
    private boolean isReadyPay = true;//是否预交状态

    private boolean isCarType = false;
    private String isSpecial = "";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void initData(Intent intent) {
        devComponent = DaggerDevComponent.builder().build();
        devComponent.inject(this);
        iDevService = devComponent.getIDevService();
        binding = (ActivityPayBinding) view;
        activity = this;
        activitys.add(activity);

        //数据 接口
        parkingOrderInteractor = appComponent.getParkingOrderInteractor();
        //UI数据
        uIsBean = appComponent.getUIsBean();


        carTypeItems = new String[]{"1小型汽车", "2中小货车", "3超大型车", "4免费车辆", "5军医车"};
        carCounts = new String[]{"02", "01", "99", "98"};
        carNumberTypes = new String[]{"1拍照识别", "2无车牌", "3手动输入"};
        //对话框
        carTypeDialog = createCarTypeDialog();
        carNumberDialog = createCarNumberDialog();
        carNumberWrite = createCarNumberWrite();
        carMsgDiaglog = createMesDialog();
        resources = getResources();

        //实体数据已有
        parkingOrderId = intent.getIntExtra("parkingOrderId", 0);
        stopSection = "停车路段：" + SharedPreferencesUtils.getParam(activity, Config.STOPTITLE, "");
        UIsBean.UserInfo userInfo = uIsBean.getUserInfo();
        UIsBean.UserInfo.DataBean data = userInfo.getData();
        if (data != null) {
            userName = data.getName();
        }


        //调用打印服务Service
        if (iDevService instanceof Msm8909DevService) {
            iDevService.openDev();
        }

        if (parkingOrderId == 0) {
            showToast.showToastTxt(activity, "订单号有误");
            onDestroy();
        }

    }

    @Override
    protected void initView() {
        //修改标题栏

        ArrayList<UIsBean.UIBean> upTimeData = uIsBean.getUpTimeData();
        for (int i = 0; i < upTimeData.size(); i++) {
            UIsBean.UIBean uiBean = upTimeData.get(i);
            int parkingOrderId = uiBean.getParkingOrderId();
            if (parkingOrderId == this.parkingOrderId) {
                binding.payTitle.setText("车位编号：" + uiBean.getSeatNo());
                stopSectionSeatNo = stopSection + uiBean.getSeatNo();
            }
        }

        //更新数据 写入UI
        downOrderData();
        //按钮监听
        addOnClick();

    }

    private void downOrderData() {
        parkingOrderInteractor.parkingOrderInfo("Detail.do", StringForJson.OneDataForJson("parkingOrderId", parkingOrderId), new BaseSubscriber<String>(this) {
            @Override
            protected void onSuccess(String result) {
                Log.i("Bean", "parkingOrderId:" + parkingOrderId);
                Gson gson = new Gson();
                ParkingOrderDetailBean parkingOrderDetailBean = gson.fromJson(result, ParkingOrderDetailBean.class);
                data = parkingOrderDetailBean.getData();
                initDataView();
            }
        });


    }

    //写入UI
    private void initDataView() {
        //当前时间
        long thisTime = new Date().getTime();

        if (data != null) {
            Log.i(TAG, data.toString());
            double realFare = data.getRealFare();
            double dueFare = data.getDueFare();
            //应收 -  实收 = 需交
            double dr = dueFare - realFare;
            //欠费 = 实收 - 应收
            double rd = realFare - dueFare;
            binding.payStopMoney.setText("停车费用：" + dueFare + "元");
            binding.payMoney.setText("已预交：" + realFare + "元");

            carNumber = data.getVehicleNo();
            carCount = data.getVehicleType();
            isSpecial = data.getIsSpecial();
            if ("null".equals(isSpecial) || isSpecial == null) {
                isSpecial = "no";
            }
            carMoney = dueFare;
            long parkingTime = data.getParkingTime();

            if (carNumber == null || "null".equals(carNumber)) {
                carNumber = "";
            } else {
                //修改车牌
                ArrayList<UIsBean.UIBean> lists = uIsBean.getLists();
                if (lists != null && lists.size() != 0) {
                    for (int i = 0; i < lists.size(); i++) {
                        UIsBean.UIBean uiBean = lists.get(i);
                        int stopId = uiBean.getParkingOrderId();
                        if (this.parkingOrderId == stopId) {
                            uiBean.setVehicleNo(carNumber);
                            uiBean.setIsVisual(View.GONE);
                            uiBean.setIsParking("yes_photo");
                        }
                    }
                }
            }

            if (carCount == null || "null".equals(carCount)) {
                carCount = "0";
            }

            //判断当前页面状态（预收费、还是欠费）
            boolean overTimeS = LongTimeOrString.isOverTimeS(parkingTime, thisTime);
            if (dueFare==0.0&&overTimeS){
                isReadyPay = true;//预收费状态
            }else {
                isReadyPay = false;//欠费状态
            }


//            页面数据
            if (dr >= 0.0) {
                String s = String.valueOf(dr);
                binding.payOweMoney.setText("需缴费用：" + s + "元");
                String money = s.substring(0, s.indexOf("."));
                dialogMoney = money;
            } else {
                binding.payOweMoney.setText("需缴费用：" + 0 + "元");
                dialogMoney = "0";
            }

            binding.payStopNumber.setText(stopSection);
            binding.payLongTime.setText("入场时间：" + LongTimeOrString.longTimeOrString(parkingTime));
//            binding.payComeTime.setText("停车时间：" + LongTimeOrString.stringStopTime(parkingTime, thisTime));
            binding.payComeTime.setText("现在时间：" + LongTimeOrString.longTimeOrString(thisTime));
            //车牌号码
            String orderType = data.getOrderType();
            //	订单类型，normal普通订单，free_special特殊车辆，free_rent年/月/季租车免费订单

            switch (orderType){
                case "free_rent_month"://月租车free_rent_month
                    CarStyleTxt("月租车");
                    break;
                case "free_rent_season"://季租车
                    CarStyleTxt("季租车");
                    break;
                case "free_rent_year"://年租车
                    CarStyleTxt("年租车");
                    break;
                default:
                    binding.payLicensePlate.setBackgroundColor(Color.alpha(0));
                    binding.payLicensePlate.setTextColor(Color.BLACK);
                    break;
            }

            binding.payLicensePlate.setText(carNumber);
            binding.payTxtCar.setText(getCarTypeTxt());

            String isParking = data.getIsParking();
            if ("no".equals(isParking)) {
                binding.payBntSix.setVisibility(View.INVISIBLE);
            }


        }

    }

    private void CarStyleTxt(String txt) {
        binding.payLicensePlate.setBackgroundColor(Color.BLUE);
        binding.payLicensePlate.setTextColor(Color.WHITE);
        //提示框
        carMsgDiaglog.setTitle("本车辆为"+txt+"。");
        carMsgDiaglog.show();
    }

    //添加按钮事件
    private void addOnClick() {
        binding.payBntOne.setOnClickListener(this);
        binding.payBntTwo.setOnClickListener(this);
        binding.payBntThree.setOnClickListener(this);
        binding.payBntFour.setOnClickListener(this);
        binding.payBntFive.setOnClickListener(this);
        binding.payBntSix.setOnClickListener(this);
        binding.payBack.setOnClickListener(this);
        binding.payBntCarInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.pay_bnt_one:
                //缴费
                if ("0".equals(carCount)) {
                    ChoiceCarType();
                } else {
                    if ("".equals(carNumber)) {
                        ChoiceCarNumber();
                    } else {
                        PayDo();
                    }
                }
                break;
            case R.id.pay_bnt_two:
                //退还
                RefundDo();
                break;

            case R.id.pay_bnt_three:
                //打印凭条
                if ("0".equals(carCount)) {
                    ChoiceCarType();
                 } else {
                    if ("".equals(carNumber)) {
                        ChoiceCarNumber();
                    } else {
                        Print(1);
                    }
                }
                break;
            case R.id.pay_bnt_four:
                //打印收据
                if ("0".equals(carCount)) {
                    ChoiceCarType();
                } else {
                    if ("".equals(carNumber)) {
                        ChoiceCarNumber();
                    } else {
                        Print(2);
                    }
                }
                break;
            case R.id.pay_bnt_five:
                //取消订单
                CancelDo();
                break;
            case R.id.pay_bnt_six:
                //确认车辆离开
                CarOut();
                break;
            case R.id.pay_bnt_carInfo:
                //选择车牌
                ChoiceCarType();
                break;
            case R.id.pay_back:
                //返回
                BackActivity();
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case TakePhotoBean.TACKPHOTO_RESULTCODE:
                Bundle extras = data.getExtras();
                isCarType = false;
                TakePhotoBean takePhotoBean = (TakePhotoBean) extras.getSerializable("TakePhotoBean");
                if (takePhotoBean != null) {
                    if (parkingOrderId == takePhotoBean.getParkingOrderId()) {
                        carNumber = takePhotoBean.getCarNumber();
                        PostCarInfo();
                        break;
                    }
                    showToast.showToastTxt(activity, "拍照出现异常请退出重试!");
                }
                break;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_1) {//交钱
            if (binding.payBntOne.getVisibility() != View.INVISIBLE) {
                binding.payBntOne.callOnClick();
                return true;
            }
        }

        if (keyCode == KeyEvent.KEYCODE_2) {//退钱
            if (binding.payBntTwo.getVisibility() != View.INVISIBLE) {
                binding.payBntTwo.callOnClick();
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_3) {
            //选择车牌
            ChoiceCarType();
        }
        if (keyCode == KeyEvent.KEYCODE_4) {//打印凭条
            if (binding.payBntThree.getVisibility() != View.INVISIBLE) {
                binding.payBntThree.callOnClick();
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_5) {//打印收据
            if (binding.payBntFour.getVisibility() != View.INVISIBLE) {
                binding.payBntFour.callOnClick();
                return true;
            }
        }

        if (keyCode == KeyEvent.KEYCODE_7) {//取消订单
            if (binding.payBntFive.getVisibility() != View.INVISIBLE) {
                binding.payBntFive.callOnClick();
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_8) {//车辆离开
            if (binding.payBntSix.getVisibility() != View.INVISIBLE) {
                binding.payBntSix.callOnClick();
                return true;
            }
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            BackActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iDevService instanceof Msm8909DevService) {
            iDevService.closeDev();
        }
    }


    //确认车辆离开
    private void CarOut() {
        new AlertDialog.Builder(activity).setTitle("确认车辆离开").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                parkingOrderInteractor.parkingOrderInfo("ConfirmLeave.do", StringForJson.OneDataForJson("parkingOrderId", parkingOrderId), new BaseSubscriber<String>(activity) {
                    @Override
                    protected void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String success = jsonObject.getString("success");
                            if ("true".equals(success)) {
                                SetMainUI();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast.showToastTxt(activity, "支付失败或者json解析错误");
                        }
                    }
                });
            }
        }).setNegativeButton("取消", null).create().show();
    }

    //封装支付数据 支付
    private String stringOrJsonPay(double realFare) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("parkingOrderId", parkingOrderId);
            jsonObject.put("realFare", realFare);
            jsonObject.put("payType", "cash");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //封装支付数据 支付
    private String stringOrJsonRefun(double realFare) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("parkingOrderId", parkingOrderId);
            jsonObject.put("refundDue", realFare);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //封装提交方法
    private void PayDo() {
        if (!"".equals(dialogMoney)){

            ReadyPayDialogBean readPayBean = new ReadyPayDialogBean(isReadyPay,carCount,dialogMoney);
            ReadyPay_Dialog_View readyPayDialogView = new ReadyPay_Dialog_View(activity,readPayBean, new ReadyPay_Dialog_View.getUserInput() {
                @Override
                public void Input(String userInput) {
                    if (!"".equals(userInput)) {
                        final Double money = Double.valueOf(userInput);
                        if (money != 0.0) {
                            PostMoney(stringOrJsonPay(money));
                        }
                    }

                }
            });
            readyPayDialogView.setCanceledOnTouchOutside(true);
            readyPayDialogView.show();
        }
    }

    //Pay界面的车类型
    private String getCarTypeTxt() {
        if ("02".equals(carCount)) {
            return "小型汽车：";
        }
        if ("01".equals(carCount)) {
            return "中小货车:";
        }
        if ("99".equals(carCount)) {
            return "超大型车：";
        }
        if ("98".equals(carCount)) {
            return "军医车辆：";
        }
        return "车 牌：";
    }

    //选择车牌类型
    private void ChoiceCarType() {
        if (carTypeDialog != null) {
            carTypeDialog.show();
        }
    }

    private void ChoiceCarNumber() {
        if (carNumberDialog != null) {
            if ("0".equals(carCount)) {
                ChoiceCarType();
            } else {
                carNumberDialog.show();
            }
        }
    }


    private void TackPhoto() {
        Intent intent = new Intent(activity, TakeOcrPhotoActivity.class);
        intent.putExtra("parkingOrderId", parkingOrderId);
        startActivityForResult(intent, TakePhotoBean.TACKPHOTO_REQUESTCODE);
    }

    //支付提交
    private void PostMoney(String postBody) {
        parkingOrderInteractor.parkingOrderInfo("Pay.do", postBody, new BaseSubscriber<String>(this) {
            @Override
            protected void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    if ("true".equals(success)) {
                        downOrderData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast.showToastTxt(activity, "支付失败或者json解析错误");
                }
            }
        });
    }

    //封装退款的方法
    private void RefundDo() {
        final double realFare = data.getRealFare();
        if (realFare == 0.0) {
            showToast.showToastTxt(activity, "预缴金额为0元，不可退还!");
        } else {
            Pay_Dialog_View dialogView = new Pay_Dialog_View(activity, new Pay_Dialog_View.getUserInput() {
                @Override
                public void Input(String userInput) {
                    if (!"".equals(userInput)) {
                        final Double money = Double.valueOf(userInput);
                        if (money != 0.0) {
                            if (money <= realFare) {
                                showToast.showToastTxt(activity, "输入了:" + userInput);
                                MoneyRefun(stringOrJsonRefun(money));
                            } else {
                                showToast.showToastTxt(activity, "退钱金额不能超过停车费用!");
                            }
                        }
                    }
                }
            });
            dialogView.setCanceledOnTouchOutside(true);
            dialogView.show();
        }
    }

    //退还信息提交
    private void MoneyRefun(String postBody) {
        parkingOrderInteractor.parkingOrderInfo("Refund.do", postBody, new BaseSubscriber<String>(this) {
            @Override
            protected void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    if ("true".equals(success)) {
                        downOrderData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast.showToastTxt(activity, "支付失败或者json解析错误");
                }
            }
        });
    }

    //封装取消订单方法
    private void CancelDo() {
        new AlertDialog.Builder(activity).setTitle("是否撤销这张订单？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CancelOrder();
            }
        }).setNegativeButton("取消", null).create().show();
    }

    //撤销订单提交
    private void CancelOrder() {
        parkingOrderInteractor.parkingOrderInfo("Cancel.do", StringForJson.OneDataForJson("parkingOrderId", parkingOrderId), new BaseSubscriber<String>(this) {
            @Override
            protected void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    if ("true".equals(success)) {
                        SetMainUI();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast.showToastTxt(activity, "支付失败或者json解析错误");
                }
            }
        });
    }

    //删除数据 修改UI界面信息
    private void SetMainUI() {
        ArrayList<UIsBean.UIBean> lists = uIsBean.getLists();
        for (int i = 0; i < lists.size(); i++) {
            UIsBean.UIBean uiBean = lists.get(i);
            int parkingOrderId = uiBean.getParkingOrderId();
            if (parkingOrderId == this.parkingOrderId) {
                uiBean.setIsParking("no");//背景
                uiBean.setIsVisual(View.GONE);//小提示显示
                uiBean.setVehicleNo("");//车牌
                uiBean.setStringParkingTime("空 闲");//时间
                uiBean.setOrder(false);//订单信息
                uiBean.setParkingTime(0L);
                //删除当前状态
//                delState(parkingOrderId);
                uIsBean.getUpTimeData().remove(uiBean);
                //清除数据
                uiBean.setParkingOrderId(1000000);
                BackActivity();
            }
        }
    }

//    private void delState(int parkingOrderId) {
//        Iterator<Map.Entry<Integer, String>> iterator = payState.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<Integer, String> entry = iterator.next();
//            Integer key = entry.getKey();
//            Log.i("Bean", "Key:" + key + "");
//            if (key == parkingOrderId) {
//                iterator.remove();
//                Log.i("Bean", "payStateSize" + payState.size() + "");
//            }
//        }
//    }

    //提交车牌
    private void PostCarInfo() {
        parkingOrderInteractor.parkingOrderInfo("RegisterInfo.do", stringOrJson(), new BaseSubscriber<String>(this) {
            @Override
            protected void onSuccess(String result) {
                Log.i("Bean", "提交车牌:parkingOrderId:" + parkingOrderId);
//                Gson gson = new Gson();
//                ParkingOrderDetailBean parkingOrderDetailBean = gson.fromJson(result, ParkingOrderDetailBean.class);
//                data = parkingOrderDetailBean.getData();
//                initDataView();
                downOrderData();
            }
        });
    }

    //请求 转为json（post请求body） 查询订单
    //isSpecial 是否为特殊车辆
    private String stringOrJson() {
        JSONObject jsonObject = new JSONObject();
        Log.i("Bean", "isSpecial" + isSpecial);
        if ("".equals(isSpecial) || "null".equals(isSpecial)) {
            isSpecial = "no";
        }
        try {
            jsonObject.put("parkingOrderId", parkingOrderId);
            jsonObject.put("vehicleNo", carNumber);
            jsonObject.put("vehicleType", carCount);
            jsonObject.put("isSpecial", isSpecial);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, jsonObject.toString());
        return jsonObject.toString();
    }

    //封装打印方法
    private void Print(int state) {
        if (BntNotRepeatClick.isFastClick()) {
            showToast.showToastTxt(activity, "正在打印中，请稍等!");
            PayPrint(state);
        }
    }

    //打印
    private void PayPrint(int state) {
        if (!iDevService.supportPrint()) {
            Toast.makeText(activity, "不支付打印机功能", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject jsonObject = new JSONObject();
        String s = binding.payTxtCar.getText().toString() + binding.payLicensePlate.getText().toString();
        try {
            jsonObject.put("state", state);
            jsonObject.put("license_plate", s);
            jsonObject.put("stop_number", stopSectionSeatNo);
            jsonObject.put("long_time", binding.payLongTime.getText());//入场时间
            jsonObject.put("come_time", binding.payComeTime.getText());//停车时长
            jsonObject.put("money", data.getDueFare());
            jsonObject.put("realFare", data.getRealFare());
            jsonObject.put("userName", userName);

            if (data == null) {
                jsonObject.put("qrcode", "www.baidu.com");
            } else {
                if (data.getMobileUrl() == null) {
                    jsonObject.put("qrcode", "www.baidu.com");
                } else {
                    jsonObject.put("qrcode", data.getMobileUrl());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
//            Log.i("Print",jsonObject.toString());
        iDevService.print(jsonObject.toString());
    }

    //返回按钮
    private void BackActivity() {
        BaseActivity baseActivity = activitys.get(activitys.size() - 1);
        baseActivity.finish();
        activitys.remove(activitys.size() - 1);
    }

    //当前支付状态
//    private void queryState() {
//        Iterator<Map.Entry<Integer, String>> iterator = payState.entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry<Integer, String> entry = iterator.next();
//            Integer key = entry.getKey();
//            if (key==parkingOrderId){
//                currentState = entry.getValue();
//            }
//        }
//        if ("".equals(currentState)){
//            currentState = Config.READPAY;
//            upDataPayState(currentState);
//        }
//
//
//        Log.i("currentState:",currentState);
//    }
    //修改PayActivity状态
    private void upDataPayState(String state) {
        if (payState == null) {
            Toast.makeText(activity, "当前状态发生未知错误", Toast.LENGTH_SHORT).show();
            return;
        }
        payState.put(parkingOrderId, state);
        Log.i("修改currentState:", state);

    }

    private String getSourString(int id) {
        return resources.getString(id);
    }

    //    private void SavePayPrint(String mTitle) {
//        new AlertDialog.Builder(activity).setTitle(mTitle).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                upDataPayState(Config.OUTPRINT);
//                initDataView();
//            }
//        }).setNegativeButton("取消",null).create().show();
//    }
    //逃费
    private void EscapeDo() {
        new AlertDialog.Builder(activity).setTitle("确认逃费？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                parkingOrderInteractor.parkingOrderInfo("Escape.do", StringForJson.OneDataForJson("parkingOrderId", parkingOrderId), new BaseSubscriber<String>(activity) {
                    @Override
                    protected void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String success = jsonObject.getString("success");
                            if ("true".equals(success)) {
                                SetMainUI();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast.showToastTxt(activity, "支付失败或者json解析错误");
                        }
                    }
                });
            }
        }).setNegativeButton("取消", null).create().show();
    }

    //创建对话框 车牌类型
    private AlertDialog createCarTypeDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).setTitle("车牌类型").setItems(carTypeItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 3) {
                    isSpecial = "yes";
                    carCount = carCounts[0];
                } else if (i == 4) {
                    isSpecial = "yes";
                    carCount = carCounts[3];
                } else {
                    isSpecial = "no";
                    carCount = carCounts[i];
                }


                Log.i(TAG, "选择了" + carTypeItems[i]);
                ChoiceCarNumber();

            }
        }).setNegativeButton("返回", null).
                setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                        int action = keyEvent.getAction();
                        switch (action) {
                            case KeyEvent.ACTION_DOWN:
                                Log.i(TAG, "Key:ACTION_DOWN");
                                break;
                            case KeyEvent.ACTION_UP:
                                Log.i(TAG, "Key:ACTION_UP");
                                switch (keyCode) {
                                    case KeyEvent.KEYCODE_1:
                                        carCount = carCounts[0];
                                        dialogInterface.dismiss();
                                        isSpecial = "no";
                                        if (!carTypeDialog.isShowing()) {
                                            //防止外部按键再次触发
                                            ChoiceCarNumber();
                                        }
                                        break;
                                    case KeyEvent.KEYCODE_2:
                                        carCount = carCounts[1];
                                        isSpecial = "no";
                                        dialogInterface.dismiss();
                                        if (!carTypeDialog.isShowing()) {
                                            //防止外部按键再次触发
                                            ChoiceCarNumber();
                                        }
                                        break;
                                    case KeyEvent.KEYCODE_3:
                                        carCount = carCounts[2];
                                        isSpecial = "no";
                                        dialogInterface.dismiss();
                                        if (!carTypeDialog.isShowing()) {
                                            //防止外部按键再次触发
                                            ChoiceCarNumber();
                                        }
                                        break;
                                    case KeyEvent.KEYCODE_4:
                                        carCount = carCounts[0];
                                        isSpecial = "yes";
                                        dialogInterface.dismiss();
                                        if (!carTypeDialog.isShowing()) {
                                            //防止外部按键再次触发
                                            ChoiceCarNumber();
                                        }
                                        break;
                                    case KeyEvent.KEYCODE_5:
                                        carCount = carCounts[3];
                                        isSpecial = "yes";
                                        dialogInterface.dismiss();
                                        if (!carTypeDialog.isShowing()) {
                                            //防止外部按键再次触发
                                            ChoiceCarNumber();
                                        }

                                        break;
                                    case KeyEvent.KEYCODE_BACK:
                                        dialogInterface.dismiss();
                                        break;
                                    default:
                                        break;
                                }
                            default:
                                break;
                        }
                        return true;
                    }
                }).create();
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    private AlertDialog createCarNumberDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setTitle("车牌号").setItems(carNumberTypes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    //拍照识别
                    TackPhoto();
                    isCarType = false;
                    return;
                }
                if (i == 1) {
                    //无车牌
                    carNumber = "无车牌";
                    PostCarInfo();
                    isCarType = false;
                    return;
                }
                if (i == 2) {
                    carNumberWrite.show();
                    isCarType = false;
                }

            }
        }).setNegativeButton("不操作", null).setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (carNumberDialog.isShowing()) {
                    int action = keyEvent.getAction();
                    switch (action) {
                        case KeyEvent.ACTION_DOWN:
                            Log.i(TAG, "Key:ACTION_DOWN");
                            break;
                        case KeyEvent.ACTION_UP:
                            Log.i(TAG, "Key:ACTION_UP");
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_1:
                                    TackPhoto();
                                    dialogInterface.dismiss();
                                    break;
                                case KeyEvent.KEYCODE_2:
                                    carNumber = "无车牌";
                                    PostCarInfo();
                                    dialogInterface.dismiss();
                                    break;
                                case KeyEvent.KEYCODE_3:
                                    carNumberWrite.show();
                                    dialogInterface.dismiss();
                                    break;
                                case KeyEvent.KEYCODE_BACK:
                                    dialogInterface.dismiss();
                                    break;
                                default:
                                    break;
                            }
                        default:
                            break;
                    }
                }
                return true;

            }
        }).create();
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    //月租车提示框
    private AlertDialog createMesDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create();
        return alertDialog;
    }

    //创建手动输入车牌对话框
    private CarNumberDialog createCarNumberWrite() {
        CarNumberDialog dialog = new CarNumberDialog(activity, new CarNumberDialog.getUserInput() {
            @Override
            public void Input(String s) {
                if (!"".equals(s)) {
                    carNumber = s;
                    PostCarInfo();
                }
            }
        });
        return dialog;
    }


}
