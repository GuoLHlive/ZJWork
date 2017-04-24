package com.stop.zparkingzj.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stop.zparkingzj.R;

/**
 * Created by Administrator on 2017/3/28.
 */
public class CarNumberDialog extends AlertDialog {

    //控件
    private LinearLayout mLinear; //按钮位置
    private Button mSave,mEsc;
    private ImageView mDel;
    private GridView mProvince;//省份


    private String[] mProvince_Name = {"京","津","沪","渝","冀","豫","云","辽","黑","湘","皖","鲁","新","苏","浙","赣","桂","甘","晋","陕","吉",
            "闽","贵","粤","青","藏","琼"};
    private String[] mEnglish = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8","9","0"};
    private StringAdapter stringAdapter;
    private int index = 0;//当前下标

    private Context context;
    private getUserInput input;
    public interface getUserInput{
        void Input(String s);
    }

    public CarNumberDialog(Context context, getUserInput input) {
        super(context);
        this.input = input;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_number_layout);
        initView();
        initData();
        int childCount = mLinear.getChildCount();
        for (int i = 0;i<childCount;i++){
            TextView mtv = (TextView) mLinear.getChildAt(i);
            mtv.setText("粤");
            if (i == 1){
                mtv.setText("G");
                index = 2;
                stringAdapter.setMlist(mEnglish);
                stringAdapter.notifyDataSetChanged();
                break;
            }
        }

    }

    private void initData() {
        final int childCount = mLinear.getChildCount();
        stringAdapter = new StringAdapter(context, mProvince_Name);
        mProvince.setAdapter(stringAdapter);
        mProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (index>=0){
                    Log.i("Bean","index+:"+index);
                    if (index<7){
                        TextView mtv = (TextView) mLinear.getChildAt(index);
                        String mTvString = mtv.getText().toString();
                        if (index == 0){
                            if ("".equals(mTvString)){
                                mtv.setText(mProvince_Name[position]);
                                stringAdapter.setMlist(mEnglish);
                                stringAdapter.notifyDataSetChanged();
                            }
                        }else {
                            mtv.setText(mEnglish[position]);
                        }
                    }
                    if (index<childCount){
                        index++;
                    }
                }
            }
        });
        mDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index!=0){
                    if (index>0&&index<8){
                        index--;
                    }
                    Log.i("Bean","index-:"+index);
                    TextView mtv = (TextView) mLinear.getChildAt(index);
                    mtv.setText("");


                }
                if (index == 0){
                    stringAdapter.setMlist(mProvince_Name);
                    stringAdapter.notifyDataSetChanged();
                }

            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s ="";
                for (int i=0;i<childCount;i++){
                    s = s +((TextView) mLinear.getChildAt(i)).getText().toString();
                }
                int carNumberLength = s.length();
                String carNumber = s.toString();
                Log.i("Bean", carNumber +"|"+ carNumberLength);
                if (carNumberLength==7){
                    input.Input(carNumber);
                    dismiss();
                }else {
                    showToast("请输入正确车位号!");
                }
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

    private void initView() {
        mLinear = (LinearLayout) this.findViewById(R.id.carNumber_linear);
        mSave = (Button) this.findViewById(R.id.mSave);
        mEsc = (Button) this.findViewById(R.id.mEsc);
        mDel = (ImageView) this.findViewById(R.id.carNumber_del);
        mProvince = (GridView) this.findViewById(R.id.carNumber_province);
    }


    private class StringAdapter extends BaseAdapter{

        private Context context;
        private String[] mlist;


        public StringAdapter(Context context, String[] mlist) {
            this.context = context;
            this.mlist = mlist;
        }

        public void setMlist(String[] mlist) {
            this.mlist = mlist;
        }

        @Override
        public int getCount() {
            return mlist.length;
        }

        @Override
        public Object getItem(int i) {
            return mlist[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            TextView mTv;
            if (convertView == null){
                mTv = new TextView(context);
                mTv.setLayoutParams(new GridView.LayoutParams(55, 55));
                mTv.setBackgroundResource(R.color.white);
                mTv.setTextSize(20);
                mTv.setTextColor(Color.BLACK);
                mTv.setPadding(3,3,3,3);
                mTv.setGravity(Gravity.CENTER);
            }else {
                mTv = (TextView) convertView;
            }
            mTv.setText(mlist[position]);
            return mTv;
        }
    }

    public  void showToast(String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }

}
