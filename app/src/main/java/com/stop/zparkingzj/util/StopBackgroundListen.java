package com.stop.zparkingzj.util;

import android.databinding.BindingAdapter;
import android.widget.RelativeLayout;

import com.stop.zparkingzj.R;


/**
 * Created by Administrator on 2017/2/14.
 */
public class StopBackgroundListen {

    @BindingAdapter("StopBackground")
    public static void StopBackgroundUtil(RelativeLayout relativeLayout, String isParking){
        if ("yes".equals(isParking)){
            relativeLayout.setBackgroundResource(R.color.pig);
        }else if ("no".equals(isParking)){
            relativeLayout.setBackgroundResource(R.color.green);
        }else if ("yes_photo".equals(isParking)){
            relativeLayout.setBackgroundResource(R.color.bule_b);
        }
//        else if ("payed".equals(isParking)||"free".equals(isParking)){
//                relativeLayout.setBackgroundResource(R.color.bule_b);
//            }
    }

}
