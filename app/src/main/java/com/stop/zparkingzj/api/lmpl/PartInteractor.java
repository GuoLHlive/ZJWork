package com.stop.zparkingzj.api.lmpl;

import rx.Subscription;

/**
 * Created by Administrator on 2017/1/16.
 */
public interface PartInteractor {

    //订阅动作
    Subscription partInfo(String url, String body, BaseSubscriber<String> subscriber);

    //订阅动作
    Subscription partInfo(String url, String path, String body, BaseSubscriber<String> subscriber);


}
