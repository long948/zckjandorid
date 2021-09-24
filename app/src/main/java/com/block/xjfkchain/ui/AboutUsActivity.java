package com.block.xjfkchain.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.block.xjfkchain.R;
import com.block.xjfkchain.adapter.AboutUsRecycleAdapter;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.BannerEntity;
import com.block.xjfkchain.utils.ImageLoaderManager;
import com.block.xjfkchain.widget.autoscrollviewpager.BGABanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutUsActivity extends BusinessBaseActivity {
    private AboutUsRecycleAdapter aboutUsRecycleAdapter;
    private List<Integer> list;


    @BindView(R.id.tv_title)
    TextView mTvTitle;
//    @BindView(R.id.about_us_recyclerView)
//    RecyclerView mAbout_us_recyclerView;
    @BindView(R.id.about_us_banner)
    BGABanner mBanner;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("关于我们");
        initBanner();
//        aboutUsRecycleAdapter=new AboutUsRecycleAdapter(this,list);
//        LinearLayoutManager manager=new LinearLayoutManager(this);
//        manager.setOrientation(LinearLayoutManager.VERTICAL);
//        mAbout_us_recyclerView.setLayoutManager(manager);
//        mAbout_us_recyclerView.setAdapter(aboutUsRecycleAdapter);

    }

    private void initBanner() {
        list=new ArrayList<>();
        list.add(R.mipmap.about1);
        list.add(R.mipmap.about2);
        list.add(R.mipmap.about3);
       mBanner.setAdapter(new BGABanner.Adapter<View,Integer>() {

           @Override
           public void fillBannerItem(BGABanner banner, View itemView, Integer model, int position) {
               ImageView imageView=itemView.findViewById(R.id.iv_image);
               imageView.setBackgroundResource(model);
           }
       });
        mBanner.setData(R.layout.item_banner, list, null);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        finish();
    }
}
