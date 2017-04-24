package com.stop.zparkingzj.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stop.zparkingzj.R;
import com.stop.zparkingzj.activity.BaseActivity;
import com.stop.zparkingzj.activity.TakeOcrPhotoActivity;
import com.stop.zparkingzj.bean.TakePhotoBean;

/**
 * Created by Administrator on 2017/3/14.
 */
public class TakePhoto_Dialog_View extends AlertDialog implements View.OnClickListener{

    private BaseActivity activity;

    private Button bnt_takePhoto,bnt_null,bnt_esc;
    private LinearLayout linearLayout;
    private String carCount;
    private String[] carCounts;

    public TakePhoto_Dialog_View(BaseActivity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carnumber_dialog_layout);
        initData();
        addOnClick();
    }

    private void initData() {
        carCounts = new String[]{"02","01","99"};
        bnt_takePhoto = (Button) this.findViewById(R.id.take_photo);
        bnt_null = (Button) this.findViewById(R.id.take_null);
        bnt_esc = (Button) this.findViewById(R.id.take_esc);
        linearLayout = (LinearLayout) this.findViewById(R.id.take_text);

    }

    private void addOnClick() {
        bnt_takePhoto.setOnClickListener(this);
        bnt_null.setOnClickListener(this);
        bnt_esc.setOnClickListener(this);

        final int childCount = linearLayout.getChildCount();
        for (int i=0;i<childCount;i++){
            TextView textView = (TextView) linearLayout.getChildAt(i);
            textView.setSelected(false);
            textView.setTag(i);
            if (i == 0){
                textView.setSelected(true);
                carCount = carCounts[i];
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0;j<childCount;j++){
                        TextView textView = (TextView) linearLayout.getChildAt(j);
                        textView.setSelected(false);
                        int tag = (int) textView.getTag();
                        if (j == tag){
                            textView.setSelected(true);
                            carCount = carCounts[j];
                        }
                    }
                }
            });

        }


    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.take_photo:
                dismiss();
                break;
            case R.id.take_null:
                dismiss();
                break;
            case R.id.take_esc:
                dismiss();
                break;
        }
    }
}
