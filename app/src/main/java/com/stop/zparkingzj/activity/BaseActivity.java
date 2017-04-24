package com.stop.zparkingzj.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;

import com.stop.zparkingzj.MainActivity;
import com.stop.zparkingzj.MyApp;
import com.stop.zparkingzj.module.AppComponent;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/9.
 */
public abstract class BaseActivity extends AutoLayoutActivity{

    protected ViewDataBinding view;
    protected ArrayList<BaseActivity> activitys;
    protected AppComponent appComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(MainActivity.FLAG_HOMEKEY_DISPATCHED, MainActivity.FLAG_HOMEKEY_DISPATCHED);

        view = DataBindingUtil.setContentView(this, getLayoutId());
        appComponent = MyApp.getApp(getApplicationContext()).component();
        activitys = appComponent.getAppActivitys();

        initData(getIntent());
        initView();

    }


    protected abstract int getLayoutId();
    protected abstract void initData(Intent intent);
    protected abstract void initView();

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK){
            Log.i("RetrofitLog","activitys:"+activitys.size());
            try {
                if (activitys.size()==1){
                    activitys.get(0).finish();
                    activitys.clear();
                }else {
                    activitys.get(activitys.size()-1).finish();
                    activitys.remove(activitys.size()-1);
                }
            } catch (Exception e) {
                finish();
            }
        }
        return true;
    }
}
