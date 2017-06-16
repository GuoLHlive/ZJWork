package com.stop.zparkingzj.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stop.zparkingzj.R;
import com.stop.zparkingzj.bean.ReadyPayDialogBean;

/**
 * Created by Administrator on 2017/3/16.
 * 预缴框
 */
public class ReadyPay_Dialog_View extends AlertDialog {

    private EditText mMoney;
    private Button mSave;
    private Button mEsc;
    private LinearLayout mTxts;


    //根据车牌类型收费
    private String carCount;
    private String[] mTxtMoney;
    private getUserInput input;
    private String carMoney;//收费金额
    private boolean isReadyPay;
    public interface getUserInput{
        void Input(String s);
    }

    public ReadyPay_Dialog_View(Context context, ReadyPayDialogBean data, getUserInput input) {
        super(context);
        this.carCount = data.getCarCount();
        this.input = input;
        this.carMoney = data.getCarMoney();
        this.isReadyPay = data.isReadyPay();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readypay_dialog_layout);
        initData();
        initView();
    }

    private void initView() {
        int mTxtSize = mTxts.getChildCount();

        mTxtOnClick onClick = new mTxtOnClick();
        //默认价格
        switch (carCount){
            case "02"://小车价格
                mTxtMoney = new String[]{"0","5","8","10"};
                break;
            case "01":
                mTxtMoney = new String[]{"8","12","16"};
                break;
            case "99":
                mTxtMoney = new String[]{"60"};
                break;
            case "98":
                mTxtMoney = new String[]{"0","5","8","10"};
                break;
            default:
                mTxtMoney = new String[]{"价格出现错误!"};
                break;
        }
        //价格的摸
        Log.i("Diag",mTxtMoney.length+"");
        switch (mTxtMoney.length){
            case 1:
                for (int i = 0;i<mTxtSize;i++){
                    TextView mTxt = (TextView) this.mTxts.getChildAt(i);
                    mTxt.setVisibility(View.INVISIBLE);
                    mTxt.setOnClickListener(onClick);
                    mTxt.setTag(i);
                    if (i==1){
                        mTxt.setText(mTxtMoney[0]);
                        mTxt.setVisibility(View.VISIBLE);
                        if (isReadyPay){
                            mMoney.setText(mTxtMoney[0]);
                        }else {
                            mMoney.setText(carMoney);
                        }
                        mMoney.setSelection(mMoney.length());
                    }
                    if (i == 3){
                        mTxt.setVisibility(View.GONE);
                    }
                }
                break;
            case 3:
                for (int i = 0;i<mTxtSize;i++){
                    TextView mTxt = (TextView) this.mTxts.getChildAt(i);
                    if (i!=3){
                        mTxt.setText(mTxtMoney[i]);
                        mTxt.setOnClickListener(onClick);
                        mTxt.setTag(i);
                    }else {
                        mTxt.setVisibility(View.GONE);
                    }

                    if (i==0){
                        if (isReadyPay){
                            mMoney.setText(mTxtMoney[i]);
                        }else {
                            mMoney.setText(carMoney);
                        }
                        mMoney.setSelection(mMoney.length());
                    }
                }
                break;
            case 4:
                for (int i = 0;i<mTxtSize;i++){
                    TextView mTxt = (TextView) this.mTxts.getChildAt(i);
                    mTxt.setText(mTxtMoney[i]);
                    mTxt.setOnClickListener(onClick);
                    mTxt.setTag(i);
                    if (i==1){
                        if (isReadyPay){
                            mMoney.setText(mTxtMoney[i]);
                        }else {
                            mMoney.setText(carMoney);
                        }
                        mMoney.setSelection(mMoney.length());
                    }
                }
                break;
            default:
                break;
        }

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String money = mMoney.getText().toString().trim();
                input.Input(money);
                dismiss();
            }
        });

        mEsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.Input("");
                dismiss();
            }
        });


    }

    private void initData() {
        mEsc = (Button) this.findViewById(R.id.mEsc);
        mSave = (Button) this.findViewById(R.id.mSave);
        mMoney = (EditText) findViewById(R.id.mMoney);
        mTxts = (LinearLayout) this.findViewById(R.id.pay_ReadyTxt);
    }


    private class mTxtOnClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            int tag = (int) view.getTag();
            for (int i = 0;i<mTxts.getChildCount();i++){
                TextView mTxt = (TextView) mTxts.getChildAt(i);
                Log.i("Bean","i"+i);
                if (tag == i){
                    Log.i("Bean","onClickTag"+tag);
                    mTxt.setSelected(true);
                    String txt = mTxt.getText().toString();
                    mMoney.setText(txt);
                    mMoney.setSelection(mMoney.length());
                }
            }
        }
    }


}
