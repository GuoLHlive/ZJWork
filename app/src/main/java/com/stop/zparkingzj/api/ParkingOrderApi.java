package com.stop.zparkingzj.api;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2017/1/18.
 * 订单号
 *
 */
public interface ParkingOrderApi {

    @POST("ParkingOrder/{stringPath}")
    Observable<String> ParkingOrderInfo(@Path("stringPath") String path, @Body String body);

}
