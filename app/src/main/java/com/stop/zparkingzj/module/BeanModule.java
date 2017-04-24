package com.stop.zparkingzj.module;



import com.stop.zparkingzj.bean.UIsBean;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/2/13.
 */

@Module
public class BeanModule {

    @Singleton
    @Provides
    public UIsBean providesUIsBean(){
        return new UIsBean();
    }



}
