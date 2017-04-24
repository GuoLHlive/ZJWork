package com.stop.zparkingzj.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;

import com.google.gson.Gson;
import com.stop.zparkingzj.R;
import com.stop.zparkingzj.adapter.EscaperListAdapter;
import com.stop.zparkingzj.api.lmpl.BaseSubscriber;
import com.stop.zparkingzj.api.lmpl.ParkingOrderInteractor;
import com.stop.zparkingzj.bean.EscaperListBean;
import com.stop.zparkingzj.databinding.ActivityEscaperecordBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/12/16.
 */

/**
 * 逃票记录界面
 *
 */
public class EscapeRecordActivity extends BaseActivity{


    private BaseActivity activity;
    private static final String TAG = "EscapeRecordActivity:";

    private int visibleLastIndex = 0;   //最后的可视项索引
    private int visibleItemCount;       // 当前窗口可见项总数
    private int page = 1; //页数

    private View loadMoreView;
    private View HeaderView;
    private Button loadMoreButton;
    private ParkingOrderInteractor parkingOrderInteractor;
    private EscaperListAdapter adapter;
    private ActivityEscaperecordBinding binding;
    private List<EscaperListBean.DatasBean> list;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_escaperecord;
    }

    @Override
    protected void initData(Intent intent) {
        binding = (ActivityEscaperecordBinding) view;
        activity = this;
        activitys.add(activity);

        parkingOrderInteractor = appComponent.getParkingOrderInteractor();
        list = new ArrayList<>();

        //UI
        loadMoreView = getLayoutInflater().inflate(R.layout.escaper_footer_layout, null);
        loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);
        HeaderView = getLayoutInflater().inflate(R.layout.escaper_header_layout, null);

    }

    @Override
    protected void initView() {
        binding.escaperList.addHeaderView(HeaderView);
        binding.escaperList.addFooterView(loadMoreView);
        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page++;
                downEscaperList();
            }
        });

        //下载数据

        downEscaperList();

    }

    private void downEscaperList() {
        Log.i(TAG,"拿取逃费数据");
        parkingOrderInteractor.parkingOrderInfo("EscapeList.do", stringforJson(), new BaseSubscriber<String>(activity) {
            @Override
            protected void onSuccess(String result) {
                Gson gson = new Gson();
                EscaperListBean escaperListBean = gson.fromJson(result, EscaperListBean.class);
                List<EscaperListBean.DatasBean> datas = escaperListBean.getDatas();
                Log.i(TAG,"数据长度："+ datas.size());
                int totalPage = escaperListBean.getTotalPage();//总页数
                if (page>1){
                    //第二页以后
                    if (totalPage>page){
                        //第三页
                        list.addAll(datas);
                    }
                }else {
                    //第一页
                    list.clear();
                    list.addAll(datas);
                }
                //加载数据
                initViewData();
            }
        });

    }

    private void initViewData() {
        if (adapter == null){
            adapter = new EscaperListAdapter(list,activity);
            binding.escaperList.setAdapter(adapter);
//            binding.escaperList.setOnScrollListener(this);
        }else {
            adapter.notifyDataSetChanged();
        }

    }

    private String stringforJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page",page);
            jsonObject.put("pageSize","10");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG,jsonObject.toString());
        return jsonObject.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        downEscaperList();
    }
}
