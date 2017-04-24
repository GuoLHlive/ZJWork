package com.stop.zparkingzj.util;

import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2016/12/14.
 */
public class StringResponseBody {

    public static ResponseBody postResponseBody(String json){
        ResponseBody responseBody = ResponseBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8")
                ,json);
        return responseBody;
    }

}
