package com.stop.zparkingzj.module;




import com.stop.zparkingzj.activity.BaseActivity;

import java.util.ArrayList;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/12/15.
 */

@Module
public class AppModule {

    @Singleton
    @Provides
    public ArrayList<BaseActivity> provideBackActivity() {
        return new ArrayList<>();
    }

}
