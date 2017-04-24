package com.stop.zparkingzj.module;



import com.stop.zparkingzj.MyApp;
import com.stop.zparkingzj.activity.BaseActivity;
import com.stop.zparkingzj.api.lmpl.LoginNfcInteractor;
import com.stop.zparkingzj.api.lmpl.ParkingOrderInteractor;
import com.stop.zparkingzj.api.lmpl.PartInteractor;
import com.stop.zparkingzj.bean.UIsBean;

import java.util.ArrayList;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2016/12/14.
 * dagger2
 */


@Singleton
@Component( modules = {AppModule.class,RetrofitUtilModule.class,BeanModule.class})
public interface AppComponent {
    void inject(MyApp myApp);
    LoginNfcInteractor getLoginNfcInteractor();
    ArrayList<BaseActivity> getAppActivitys();
    PartInteractor getPartInteractor();
    ParkingOrderInteractor getParkingOrderInteractor();
    UIsBean getUIsBean();

}
