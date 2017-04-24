package com.stop.zparkingzj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/14.
 */
public class PayUIBean implements Serializable {

    private int parkingOrderId; //订单号
    private String seatNo; //车位编号

    public PayUIBean(int parkingOrderId) {
        this.parkingOrderId = parkingOrderId;
    }

    public int getParkingOrderId() {
        return parkingOrderId;
    }

    public void setParkingOrderId(int parkingOrderId) {
        this.parkingOrderId = parkingOrderId;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }


    @Override
    public String toString() {
        return "PayUIBean{" +
                "parkingOrderId=" + parkingOrderId +
                ", seatNo='" + seatNo + '\'' +
                '}';
    }
}
