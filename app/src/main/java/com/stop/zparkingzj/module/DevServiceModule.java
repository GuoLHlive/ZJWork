package com.stop.zparkingzj.module;

import com.stop.zparkingzj.MyApp;
import com.stop.zparkingzj.dev.DefaultDevService;
import com.stop.zparkingzj.dev.IDevService;
import com.stop.zparkingzj.dev.P900.P990DevService;
import com.stop.zparkingzj.dev.msm8909.Msm8909DevService;
import com.stop.zparkingzj.dev.ww808_emmc.WW808EmmcDevService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Tonny on 2016/5/19.
 */
@Module()
public class DevServiceModule {
    @Singleton
    @Provides
    public IDevService providesCheckVersionService(){
        String model = android.os.Build.MODEL;
        IDevService iDevService = null;
        if(model.equals("P990")){
            iDevService = new P990DevService(MyApp.getApp());
        }else if(model.equals("ww808_emmc")){
            iDevService = new WW808EmmcDevService(MyApp.getApp());
        }else if (model.equals("msm8909")){
            iDevService = new Msm8909DevService(MyApp.getApp());
        }else {
            iDevService = new DefaultDevService(MyApp.getApp());
        }
        return iDevService;
    }
}
