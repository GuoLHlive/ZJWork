package com.stop.zparkingzj.module;

import android.content.Context;


import com.stop.zparkingzj.api.LoginApi;
import com.stop.zparkingzj.api.ParkingOrderApi;
import com.stop.zparkingzj.api.PartApi;
import com.stop.zparkingzj.api.lmpl.LoginNfcInteractor;
import com.stop.zparkingzj.api.lmpl.LoginNfcInteractorImpl;
import com.stop.zparkingzj.api.lmpl.ParkingOrderInteractor;
import com.stop.zparkingzj.api.lmpl.ParkingOrderInteractorImpl;
import com.stop.zparkingzj.api.lmpl.PartInteractor;
import com.stop.zparkingzj.api.lmpl.PartInteractorImpl;
import com.stop.zparkingzj.http.RetrofitHttp;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2017/1/16.
 */


@Module
public class RetrofitUtilModule {

    private final Context context;

    public RetrofitUtilModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    public Retrofit provideRetrofit(){
        return RetrofitHttp.getnstance(context);
    }

    @Provides
    public LoginApi provideLoginApi(Retrofit retrofit){
        return retrofit.create(LoginApi.class);
    }

    @Provides
    public LoginNfcInteractor provideLoginNfcInteractor(LoginApi loginApi){
        return new LoginNfcInteractorImpl(loginApi);
    }

    @Provides
    public PartApi providePartApi(Retrofit retrofit){
        return retrofit.create(PartApi.class);
    }

    @Provides
    public PartInteractor providePartInteractor(PartApi partApi){
        return new PartInteractorImpl(partApi);
    }

    @Provides
    public ParkingOrderApi provideParkingOrderApi(Retrofit retrofit){
        return retrofit.create(ParkingOrderApi.class);
    }

    @Provides
    public ParkingOrderInteractor provideParkingOrderInteractor(ParkingOrderApi parkingOrderApi){
        return new ParkingOrderInteractorImpl(parkingOrderApi);

    }


}
