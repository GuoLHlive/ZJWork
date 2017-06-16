package com.stop.zparkingzj.activity;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;


import com.google.gson.Gson;
import com.pgyersdk.update.PgyUpdateManager;
import com.stop.zparkingzj.MainActivity;
import com.stop.zparkingzj.R;
import com.stop.zparkingzj.api.lmpl.BaseSubscriber;
import com.stop.zparkingzj.api.lmpl.LoginNfcInteractor;
import com.stop.zparkingzj.bean.LoginReturnBean;
import com.stop.zparkingzj.bean.LoginUserBean;
import com.stop.zparkingzj.bean.post.LoginPostBean;
import com.stop.zparkingzj.bean.post.LogoutPostBean;
import com.stop.zparkingzj.databinding.ActivityLoginBinding;
import com.stop.zparkingzj.file.SystemService;
import com.stop.zparkingzj.util.showToast;
import com.stop.zparkingzj.view.Login_Dialog_View;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 2016/12/15.
 * 登录界面
 *
 */
public class LoginActivity extends BaseActivity {

    private LoginNfcInteractor loginNfcInteractor;
    private BaseActivity activity;
    private SystemService systemService;
    private ActivityLoginBinding binding;
    private static boolean ISUPDATA = false;//更新状态

    @Override
    protected int getLayoutId() {
        return  R.layout.activity_login;
    }

    @Override
    protected void initData(Intent intent) {
        activity = this;
        activitys.add(activity);
        loginNfcInteractor = appComponent.getLoginNfcInteractor();
        systemService  = new SystemService();
        binding = (ActivityLoginBinding) view;
    }

    @Override
    protected void initView() {
        PgyUpdateManager.register(this);
        //账号密码登录
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login_Dialog_View dialogView = new Login_Dialog_View(activity, new Login_Dialog_View.getUserInput() {
                    @Override
                    public void Input(LoginUserBean s) {
                        if (s != null){
//                            Toast.makeText(activity,s.toString(),Toast.LENGTH_SHORT).show();
                        loginNfcInteractor.loginFnc("Login.do", demoForPostJson(s), new BaseSubscriber<String>(activity) {
                            @Override
                            protected void onSuccess(String result) {
                                Log.i("Bean",result);
                                Gson gson = new Gson();
                                LoginReturnBean loginReturnBean = gson.fromJson(result, LoginReturnBean.class);
                                String code = loginReturnBean.getCode();
//                    Log.i("Bean","code"+code);
                                if ("0".equals(code)){
                                    activitys.remove(activity);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    activity.startActivity(intent);
                                    activity.finish();
                                    outUpdata();
                                }else {
                                    showToast.showToastTxt(activity,loginReturnBean.getMsg());
                                }

                            }
                        });

                    }
                    }
                });
                dialogView.setCanceledOnTouchOutside(true);
                dialogView.show();
            }
        });



    }


    private String demoForPostJson(LoginUserBean demo){

        JSONObject jsonObject = new JSONObject();

        String password = demo.getPassword();
        String workerNo = demo.getWorkerNo();

        try {
            jsonObject.put("deviceId",  systemService.getAndroidID());
            jsonObject.put("longitude","0.0");
            jsonObject.put("latitude","0.0");
            jsonObject.put("workerNo",workerNo);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Bean",jsonObject.toString());
        return jsonObject.toString();
    }



    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getEventTime() - event.getDownTime() > 5000 && keyCode == KeyEvent.KEYCODE_0) {
            // TODO 长按输入密码并进入调试设置界面
//            Log.i("MainActivityA","进入设置模式");
            Intent intent = new Intent(LoginActivity.this,SettingActivity.class);
            startActivity(intent);
        }
        return super.onKeyUp(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //得到是否检测到ACTION_TECH_DISCOVERED触发
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
            //处理该intent
            final String Rid = systemService.getAndroidID();
            final String Uid = systemService.receiveNFC(getIntent());

            final LoginPostBean loginPostBean = new LoginPostBean(Rid,0.0,0.0,Uid);

            loginNfcInteractor.loginFnc("LoginByCard.do", loginPostBean.toString(), new BaseSubscriber<String>(this) {
                @Override
                protected void onSuccess(String result) {
                    Log.i("Bean",result);
                    Gson gson = new Gson();
                    LoginReturnBean loginReturnBean = gson.fromJson(result, LoginReturnBean.class);
                    String code = loginReturnBean.getCode();
//                    Log.i("Bean","code"+code);
                    if ("0".equals(code)) {
                        activitys.remove(activity);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }else {
                        activitys.remove(activity);
                        activity.finish();
                    }
                    outUpdata();
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    showToast.showToastTxt(activity,"网络连接失败...");
                    int size = activitys.size();
                    if (size>1){
                        BaseActivity baseActivity = activitys.get(0);
                        baseActivity.finish();
                        activitys.remove(baseActivity);
                        outUpdata();

                    }
                }
            });

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
            int size = activitys.size();
            if (size>1){
                for (Activity activity:activitys){
                    activity.finish();
                }
                activitys.clear();
                logout();
            }
        startActivity(getIntent());

    }

    private void logout() {
        //登出
        LogoutPostBean postBean = new LogoutPostBean(0.0,0.0);
        loginNfcInteractor.loginFnc("Logout.do",postBean.toString(), new BaseSubscriber<String>(this) {
            @Override
            protected void onSuccess(String result) {
                Log.i("RetrofitLog","result:"+result);
                outUpdata();
            }});
    }

    private void outUpdata(){
            PgyUpdateManager.unregister();
    }



}
