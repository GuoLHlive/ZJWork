package com.stop.zparkingzj.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stop.zparkingzj.R;
import com.stop.zparkingzj.bean.UIsBean;
import com.stop.zparkingzj.databinding.MainItemLayoutBinding;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/12/12.
 *  主界面adapter
 *
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>{


    private UIsBean uIsBean;
    private ArrayList<UIsBean.UIBean> lists;
    public MainRecyclerAdapter(UIsBean uIsBean) {
        this.uIsBean = uIsBean;
        this.lists= uIsBean.getLists();
    }

    @Override
    public MainRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainRecyclerAdapter.ViewHolder holder, int position) {
        holder.setPostion(position);
        holder.setData(uIsBean);
    }

    @Override
    public int getItemCount() {
        return this.lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MainItemLayoutBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setPostion(int postion){
            binding.setPostion(postion);
        }

        public void setData(UIsBean uIsBean){
            binding.setUisBean(uIsBean);
        }

    }
}
