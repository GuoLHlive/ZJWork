package com.stop.zparkingzj.module;

import android.app.Activity;


import com.stop.zparkingzj.dev.IDevService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2016/12/28.
 */

@Singleton
@Component(modules = {DevServiceModule.class})
public interface DevComponent {
    void inject(Activity needPrint);
    IDevService getIDevService();
}
