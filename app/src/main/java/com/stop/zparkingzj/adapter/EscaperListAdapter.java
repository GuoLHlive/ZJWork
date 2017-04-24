package com.stop.zparkingzj.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stop.zparkingzj.R;
import com.stop.zparkingzj.activity.PayActivity;
import com.stop.zparkingzj.bean.EscaperListBean;
import com.stop.zparkingzj.util.LongTimeOrString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/1.
 */
public class EscaperListAdapter extends BaseAdapter {
    private static Map<Integer,View> m=new HashMap<Integer,View>();
    private List<EscaperListBean.DatasBean> list;

    private Context context;

    public EscaperListAdapter(List<EscaperListBean.DatasBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {
        EscaperListBean.DatasBean datasBean = list.get(position);
        contentView=m.get(position);
        ViewHolder viewHolder = null;
        if(contentView==null){
            viewHolder = new ViewHolder();
            contentView= LayoutInflater.from(context).inflate(R.layout.escaper_text_layout,null);
            viewHolder.mCarNumber = (TextView) contentView.findViewById(R.id.escaper_carNumber);
            viewHolder.mTime = (TextView) contentView.findViewById(R.id.escaper_stopTime);
            viewHolder.mOnClick = (LinearLayout) contentView.findViewById(R.id.escaper_onClick);
            contentView.setTag(viewHolder);
            m.put(position,contentView);
        }else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.mCarNumber.setText(datasBean.getVehicleNo());
        viewHolder.mTime.setText(LongTimeOrString.longTimeOrString(datasBean.getLeaveTime()));
        final int parkingOrderId = datasBean.getParkingOrderId();
        viewHolder.mOnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,PayActivity.class);
                intent.putExtra("parkingOrderId",parkingOrderId);
                context.startActivity(intent);
            }
        });
        return contentView;
    }

    public void addItem(List<EscaperListBean.DatasBean> data) {
        list.addAll(data);
    }

    private class ViewHolder{
        public TextView mCarNumber,mTime;
        public LinearLayout mOnClick;
    }


}
