package com.myq.flyvideo.mainfly.adapter;

import androidx.recyclerview.widget.RecyclerView;


import com.myq.flyvideo.R;
import com.myq.flyvideo.mainfly.getdata.TVListVo;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
* Create by guorui on 2020/9/21
* Last update 2020/9/21
* Description:
**/
public class TVListAdapter extends BGARecyclerViewAdapter<TVListVo> {

    public TVListAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.listitem_tvlist);
    }

    @Override
    protected void fillData(BGAViewHolderHelper viewHolderHelper, int position, TVListVo model) {
        viewHolderHelper.setText(R.id.tvlist_item_tv_name, model.getName());
    }

    @Override
    public void setItemChildListener(BGAViewHolderHelper viewHolderHelper, int viewType) {
    }

}