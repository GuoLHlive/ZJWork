package com.stop.zparkingzj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/14.
 */
public class TakePhotoBean implements Serializable {

    //返回码
    public static final int TACKPHOTO_RESULTCODE = 301;
    //请求吗
    public static final int TACKPHOTO_REQUESTCODE = 300;

    private String carNumber;//车牌
    private int parkingOrderId;

    public TakePhotoBean(String carNumber, int parkingOrderId) {
        this.carNumber = carNumber;
        this.parkingOrderId = parkingOrderId;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public int getParkingOrderId() {
        return parkingOrderId;
    }

    public void setParkingOrderId(int parkingOrderId) {
        this.parkingOrderId = parkingOrderId;
    }
}
