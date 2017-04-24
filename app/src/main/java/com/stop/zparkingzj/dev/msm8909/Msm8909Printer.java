package com.stop.zparkingzj.dev.msm8909;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.smartdevice.aidl.IZKCService;
import com.stop.zparkingzj.bean.Config;
import com.stop.zparkingzj.dev.IDevService;
import com.stop.zparkingzj.util.LongTimeOrString;
import com.stop.zparkingzj.util.printer.IPrinter;
import com.stop.zparkingzj.util.printer.content.JsonContent;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/2/23.
 */
public class Msm8909Printer implements IPrinter {

    private IDevService iDevService;
    private Context context;

    public Msm8909Printer(Context context,IDevService iDevService) {
        this.iDevService = iDevService;
        this.context = context;
    }
    @Override
    public void print(String content) throws Exception {
        Gson gson = new Gson();
        JsonContent jsonContent = gson.fromJson(content, JsonContent.class);
        Log.i("Print",jsonContent.toString());
        Log.i("Print",jsonContent.getQrcode());
        Observable.just(jsonContent)
                .delay(3, TimeUnit.SECONDS)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<JsonContent>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Print",e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(JsonContent jsonContent) {
//                            byte[] json = jsonContent.toString().getBytes("GBK");


                        if (iDevService instanceof  Msm8909DevService){
                            IZKCService mIzkcService = ((Msm8909DevService) iDevService).mIzkcService;

                            int state = jsonContent.getState();//打印类型
                            String userName = jsonContent.getUserName();//用户
                            String license_plate = jsonContent.getLicense_plate();//车牌
                            String stop_number = jsonContent.getStop_number();//停车路段
                            String qrcode = jsonContent.getQrcode();//2维码
                            String long_time = jsonContent.getLong_time();//入场时间
                            String come_time = jsonContent.getCome_time();//停车时长
                            String realFare = jsonContent.getRealFare();//预交
                            String money = jsonContent.getMoney();//停车费用
                            String title = "";
                            String thisTime = LongTimeOrString.longTimeOrString(new Date().getTime());
                            String s = "";
                            //state == 1 打印凭条
                            if (state == 1){
                            title = "湛江路边停车凭证";
                            s = "\n"+license_plate+"\n"+stop_number+"\n"+long_time+"\n"+"收费员："+userName+"\n"
                                +"预交费用："+realFare+"元"+"\n"+"打印时间："+thisTime+"\n"+"-------------------------"+"\n"+
                                Config.PRINTTEXT1;
                            }else
                            //收据
                            if (state ==2){
                                title = "湛江路边停车收据";
                                s = "\n收费单位：湛江交投城市停车服务经营有限公司\n"+"收费员："+userName+""
                                        +"\n"+license_plate+"\n"+stop_number+"\n"+long_time+"\n"+"出场时间："
                                        +thisTime+"\n"+come_time+"\n"+"停车费用："+money+"元"+"\n"+"已付款："+
                                        realFare+"元"+"\n"+"打印时间："+thisTime+"\n"+"-------------------------"+"\n"+
                                        Config.PRINTTEXT2;
                            }
                            try {
                                mIzkcService.printGBKText("\n");
                                mIzkcService.printGBKText("--------"+title+"--------"+"\n");
                                mIzkcService.printGBKText(s);
                                mIzkcService.printGBKText("\n\n");
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                });

    }


}
