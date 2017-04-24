package com.stop.zparkingzj.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/3/31.
 * 统计demo
 */
public class StatisticsBean {


    /**
     * success : true
     * date : 2017-03-31 10:41:40
     * type : data
     * code : 0
     * data : {"sumAmt":0,"ordeCount":0,"parkingTimes":0,"parkingSeatCount":0,"sumSeatCount":3,"escapeCount":0,"escapeAmt":0,"carCount":{"99":0,"01":0,"02":0}}
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
         * sumAmt : 0.0
         * ordeCount : 0
         * parkingTimes : 0
         * parkingSeatCount : 0
         * sumSeatCount : 3
         * escapeCount : 0
         * escapeAmt : 0.0
         * carCount : {"99":0,"01":0,"02":0}
         */

        private double sumAmt;
        private int ordeCount;
        private int parkingTimes;
        private int parkingSeatCount;
        private int sumSeatCount;
        private int escapeCount;
        private double escapeAmt;
        private CarCountBean carCount;

        public double getSumAmt() {
            return sumAmt;
        }

        public void setSumAmt(double sumAmt) {
            this.sumAmt = sumAmt;
        }

        public int getOrdeCount() {
            return ordeCount;
        }

        public void setOrdeCount(int ordeCount) {
            this.ordeCount = ordeCount;
        }

        public int getParkingTimes() {
            return parkingTimes;
        }

        public void setParkingTimes(int parkingTimes) {
            this.parkingTimes = parkingTimes;
        }

        public int getParkingSeatCount() {
            return parkingSeatCount;
        }

        public void setParkingSeatCount(int parkingSeatCount) {
            this.parkingSeatCount = parkingSeatCount;
        }

        public int getSumSeatCount() {
            return sumSeatCount;
        }

        public void setSumSeatCount(int sumSeatCount) {
            this.sumSeatCount = sumSeatCount;
        }

        public int getEscapeCount() {
            return escapeCount;
        }

        public void setEscapeCount(int escapeCount) {
            this.escapeCount = escapeCount;
        }

        public double getEscapeAmt() {
            return escapeAmt;
        }

        public void setEscapeAmt(double escapeAmt) {
            this.escapeAmt = escapeAmt;
        }

        public CarCountBean getCarCount() {
            return carCount;
        }

        public void setCarCount(CarCountBean carCount) {
            this.carCount = carCount;
        }

        public static class CarCountBean {
            /**
             * 99 : 0
             * 01 : 0
             * 02 : 0
             */

            @SerializedName("99")
            private int value99;
            @SerializedName("01")
            private int value01;
            @SerializedName("02")
            private int value02;

            public int getValue99() {
                return value99;
            }

            public void setValue99(int value99) {
                this.value99 = value99;
            }

            public int getValue01() {
                return value01;
            }

            public void setValue01(int value01) {
                this.value01 = value01;
            }

            public int getValue02() {
                return value02;
            }

            public void setValue02(int value02) {
                this.value02 = value02;
            }
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "sumAmt=" + sumAmt +
                    ", ordeCount=" + ordeCount +
                    ", parkingTimes=" + parkingTimes +
                    ", parkingSeatCount=" + parkingSeatCount +
                    ", sumSeatCount=" + sumSeatCount +
                    ", escapeCount=" + escapeCount +
                    ", escapeAmt=" + escapeAmt +
                    ", carCount=" + carCount +
                    '}';
        }
    }
}
