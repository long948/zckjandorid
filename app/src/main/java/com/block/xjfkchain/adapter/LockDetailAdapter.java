package com.block.xjfkchain.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;

import com.block.xjfkchain.R;
import com.block.xjfkchain.data.EarningEntity;
import com.block.xjfkchain.data.NodeEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


public class LockDetailAdapter extends BaseQuickAdapter<EarningEntity, BaseViewHolder> {

    public LockDetailAdapter(@Nullable List<EarningEntity> data) {
        super(R.layout.item_lock_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EarningEntity item) {
        helper.setText(R.id.tv_name, "+"+item.income_fil);
        helper.setText(R.id.tv_time, item.created_at);
//        helper.setText(R.id.tv_hashrate, item.team_power + "T");
//        helper.setText(R.id.tv_level, item.level);
//        if (!item.mount) {
//            helper.setTextColor(R.id.tv_tag, Color.parseColor("#333333"));
//            helper.setText(R.id.tv_tag, "已满");
//        } else {
//            helper.setTextColor(R.id.tv_tag, Color.parseColor("#ffce3d3a"));
//            helper.setText(R.id.tv_tag, "可挂载");
//        }
//        helper.addOnClickListener(R.id.tv_tag);
//        helper.getView(R.id.tv_tag).setVisibility(showStatus ? View.VISIBLE : View.GONE);
    }
}
