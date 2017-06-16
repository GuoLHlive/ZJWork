package com.stop.zparkingzj.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */
public class ParkingOrderDetailBean {


    /**
     * success : true
     * date : 2017-05-19 15:53:59
     * type : data
     * code : 0
     * data : {"parkingOrderId":443,"recordNo":"PAA-20170510163059-10002","parkId":-1,"parkSectionId":-1,"parkSeatId":-4,"vehicleNo":"辽AU9595","vehicleType":"99","isParking":"yes","photoDir":null,"isSpecial":"no","parkingTime":1494405059000,"leaveTime":null,"payStatus":"no_pay","payType":null,"dueFare":600,"realFare":0,"status":"normal","payTime":null,"remark":null,"createTime":1494405059000,"updateTime":1494405059000,"orderType":"normal","vehicleTypeText":null,"parkName":null,"seatNo":null,"payStatusText":null,"payTypeText":null,"createUserName":null,"registerParkingUserName":null,"confirmLeaveUserName":null,"orderStatusText":null,"imgIds":[],"mobileUrl":null}
     */

    private boolean success;
    private String date;
    private String type;
    private String code;
    private DataBean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * parkingOrderId : 443
         * recordNo : PAA-20170510163059-10002
         * parkId : -1
         * parkSectionId : -1
         * parkSeatId : -4
         * vehicleNo : 辽AU9595
         * vehicleType : 99
         * isParking : yes
         * photoDir : null
         * isSpecial : no
         * parkingTime : 1494405059000
         * leaveTime : null
         * payStatus : no_pay
         * payType : null
         * dueFare : 600.0
         * realFare : 0.0
         * status : normal
         * payTime : null
         * remark : null
         * createTime : 1494405059000
         * updateTime : 1494405059000
         * orderType : normal
         * vehicleTypeText : null
         * parkName : null
         * seatNo : null
         * payStatusText : null
         * payTypeText : null
         * createUserName : null
         * registerParkingUserName : null
         * confirmLeaveUserName : null
         * orderStatusText : null
         * imgIds : []
         * mobileUrl : null
         */

        private int parkingOrderId;
        private String recordNo;
        private int parkId;
        private int parkSectionId;
        private int parkSeatId;
        private String vehicleNo;
        private String vehicleType;
        private String isParking;
        private Object photoDir;
        private String isSpecial;
        private long parkingTime;
        private Object leaveTime;
        private String payStatus;
        private Object payType;
        private double dueFare;
        private double realFare;
        private String status;
        private Object payTime;
        private Object remark;
        private long createTime;
        private long updateTime;
        private String orderType;
        private Object vehicleTypeText;
        private Object parkName;
        private Object seatNo;
        private Object payStatusText;
        private Object payTypeText;
        private Object createUserName;
        private Object registerParkingUserName;
        private Object confirmLeaveUserName;
        private Object orderStatusText;
        private Object mobileUrl;
        private List<?> imgIds;

        public int getParkingOrderId() {
            return parkingOrderId;
        }

        public void setParkingOrderId(int parkingOrderId) {
            this.parkingOrderId = parkingOrderId;
        }

        public String getRecordNo() {
            return recordNo;
        }

        public void setRecordNo(String recordNo) {
            this.recordNo = recordNo;
        }

        public int getParkId() {
            return parkId;
        }

        public void setParkId(int parkId) {
            this.parkId = parkId;
        }

        public int getParkSectionId() {
            return parkSectionId;
        }

        public void setParkSectionId(int parkSectionId) {
            this.parkSectionId = parkSectionId;
        }

        public int getParkSeatId() {
            return parkSeatId;
        }

        public void setParkSeatId(int parkSeatId) {
            this.parkSeatId = parkSeatId;
        }

        public String getVehicleNo() {
            return vehicleNo;
        }

        public void setVehicleNo(String vehicleNo) {
            this.vehicleNo = vehicleNo;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public String getIsParking() {
            return isParking;
        }

        public void setIsParking(String isParking) {
            this.isParking = isParking;
        }

        public Object getPhotoDir() {
            return photoDir;
        }

        public void setPhotoDir(Object photoDir) {
            this.photoDir = photoDir;
        }

        public String getIsSpecial() {
            return isSpecial;
        }

        public void setIsSpecial(String isSpecial) {
            this.isSpecial = isSpecial;
        }

        public long getParkingTime() {
            return parkingTime;
        }

        public void setParkingTime(long parkingTime) {
            this.parkingTime = parkingTime;
        }

        public Object getLeaveTime() {
            return leaveTime;
        }

        public void setLeaveTime(Object leaveTime) {
            this.leaveTime = leaveTime;
        }

        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
        }

        public Object getPayType() {
            return payType;
        }

        public void setPayType(Object payType) {
            this.payType = payType;
        }

        public double getDueFare() {
            return dueFare;
        }

        public void setDueFare(double dueFare) {
            this.dueFare = dueFare;
        }

        public double getRealFare() {
            return realFare;
        }

        public void setRealFare(double realFare) {
            this.realFare = realFare;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getPayTime() {
            return payTime;
        }

        public void setPayTime(Object payTime) {
            this.payTime = payTime;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public Object getVehicleTypeText() {
            return vehicleTypeText;
        }

        public void setVehicleTypeText(Object vehicleTypeText) {
            this.vehicleTypeText = vehicleTypeText;
        }

        public Object getParkName() {
            return parkName;
        }

        public void setParkName(Object parkName) {
            this.parkName = parkName;
        }

        public Object getSeatNo() {
            return seatNo;
        }

        public void setSeatNo(Object seatNo) {
            this.seatNo = seatNo;
        }

        public Object getPayStatusText() {
            return payStatusText;
        }

        public void setPayStatusText(Object payStatusText) {
            this.payStatusText = payStatusText;
        }

        public Object getPayTypeText() {
            return payTypeText;
        }

        public void setPayTypeText(Object payTypeText) {
            this.payTypeText = payTypeText;
        }

        public Object getCreateUserName() {
            return createUserName;
        }

        public void setCreateUserName(Object createUserName) {
            this.createUserName = createUserName;
        }

        public Object getRegisterParkingUserName() {
            return registerParkingUserName;
        }

        public void setRegisterParkingUserName(Object registerParkingUserName) {
            this.registerParkingUserName = registerParkingUserName;
        }

        public Object getConfirmLeaveUserName() {
            return confirmLeaveUserName;
        }

        public void setConfirmLeaveUserName(Object confirmLeaveUserName) {
            this.confirmLeaveUserName = confirmLeaveUserName;
        }

        public Object getOrderStatusText() {
            return orderStatusText;
        }

        public void setOrderStatusText(Object orderStatusText) {
            this.orderStatusText = orderStatusText;
        }

        public Object getMobileUrl() {
            return mobileUrl;
        }

        public void setMobileUrl(Object mobileUrl) {
            this.mobileUrl = mobileUrl;
        }

        public List<?> getImgIds() {
            return imgIds;
        }

        public void setImgIds(List<?> imgIds) {
            this.imgIds = imgIds;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "parkingOrderId=" + parkingOrderId +
                    ", recordNo='" + recordNo + '\'' +
                    ", parkId=" + parkId +
                    ", parkSectionId=" + parkSectionId +
                    ", parkSeatId=" + parkSeatId +
                    ", vehicleNo='" + vehicleNo + '\'' +
                    ", vehicleType='" + vehicleType + '\'' +
                    ", isParking='" + isParking + '\'' +
                    ", photoDir=" + photoDir +
                    ", isSpecial='" + isSpecial + '\'' +
                    ", parkingTime=" + parkingTime +
                    ", leaveTime=" + leaveTime +
                    ", payStatus='" + payStatus + '\'' +
                    ", payType=" + payType +
                    ", dueFare=" + dueFare +
                    ", realFare=" + realFare +
                    ", status='" + status + '\'' +
                    ", payTime=" + payTime +
                    ", remark=" + remark +
                    ", createTime=" + createTime +
                    ", updateTime=" + updateTime +
                    ", orderType='" + orderType + '\'' +
                    ", vehicleTypeText=" + vehicleTypeText +
                    ", parkName=" + parkName +
                    ", seatNo=" + seatNo +
                    ", payStatusText=" + payStatusText +
                    ", payTypeText=" + payTypeText +
                    ", createUserName=" + createUserName +
                    ", registerParkingUserName=" + registerParkingUserName +
                    ", confirmLeaveUserName=" + confirmLeaveUserName +
                    ", orderStatusText=" + orderStatusText +
                    ", mobileUrl=" + mobileUrl +
                    ", imgIds=" + imgIds +
                    '}';
        }
    }
}
