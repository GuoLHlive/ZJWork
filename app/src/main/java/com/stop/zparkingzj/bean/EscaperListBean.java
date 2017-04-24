package com.stop.zparkingzj.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/1.
 */
public class EscaperListBean {

    /**
     * success : true
     * date : 2017-04-01 14:45:40
     * type : data
     * code : 0
     * datas : [{"parkingOrderId":347,"recordNo":"PAA-20170331164727-10002","parkId":0,"parkSectionId":0,"parkSeatId":-4,"vehicleNo":"无车牌","vehicleType":"02","isParking":"no","parkingTime":1490950047000,"leaveTime":1491014366000,"payStatus":"escape","payType":null,"dueFare":0,"realFare":0,"payTime":null},{"parkingOrderId":346,"recordNo":"PAA-20170331160341-10002","parkId":0,"parkSectionId":0,"parkSeatId":-4,"vehicleNo":"无车牌","vehicleType":"01","isParking":"no","parkingTime":1490947421000,"leaveTime":1490949344000,"payStatus":"escape","payType":null,"dueFare":0,"realFare":0,"payTime":null},{"parkingOrderId":348,"recordNo":"PAA-20170401112020-10002","parkId":0,"parkSectionId":0,"parkSeatId":-4,"vehicleNo":"浙KNT22Q","vehicleType":"02","isParking":"yes","parkingTime":1491016820000,"leaveTime":null,"payStatus":"no_pay","payType":null,"dueFare":0,"realFare":0,"payTime":null}]
     * page : 1
     * pageSize : 10
     * total : 3
     * totalPage : 1
     */

    private boolean success;
    private String date;
    private String type;
    private String code;
    private int page;
    private int pageSize;
    private int total;
    private int totalPage;
    private List<DatasBean> datas;

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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * parkingOrderId : 347
         * recordNo : PAA-20170331164727-10002
         * parkId : 0
         * parkSectionId : 0
         * parkSeatId : -4
         * vehicleNo : 无车牌
         * vehicleType : 02
         * isParking : no
         * parkingTime : 1490950047000
         * leaveTime : 1491014366000
         * payStatus : escape
         * payType : null
         * dueFare : 0.0
         * realFare : 0.0
         * payTime : null
         */

        private int parkingOrderId;
        private String recordNo;
        private int parkId;
        private int parkSectionId;
        private int parkSeatId;
        private String vehicleNo;
        private String vehicleType;
        private String isParking;
        private long parkingTime;
        private long leaveTime;
        private String payStatus;
        private Object payType;
        private double dueFare;
        private double realFare;
        private Object payTime;

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

        public long getParkingTime() {
            return parkingTime;
        }

        public void setParkingTime(long parkingTime) {
            this.parkingTime = parkingTime;
        }

        public long getLeaveTime() {
            return leaveTime;
        }

        public void setLeaveTime(long leaveTime) {
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

        public Object getPayTime() {
            return payTime;
        }

        public void setPayTime(Object payTime) {
            this.payTime = payTime;
        }
    }
}
