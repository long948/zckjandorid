package com.block.xjfkchain.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.adapter.LockDetailAdapter;
import com.block.xjfkchain.adapter.NodeAdapter;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.EarnResponse;
import com.block.xjfkchain.data.EarningEntity;
import com.block.xjfkchain.data.NodeEntity;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LockDetailActivity extends BusinessBaseActivity {


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_1)
    TextView tv_1;
    @BindView(R.id.tv_2)
    TextView tv_2;
    @BindView(R.id.tv_3)
    TextView tv_3;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    private List<EarningEntity> mNodeEntities = new ArrayList<>();
    private LockDetailAdapter mLockDetailAdapter;
    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        finish();
    }
    @Override
    protected int getContentViewId() {
        return R.layout.activity_lock_detail;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {

        mLockDetailAdapter = new LockDetailAdapter(mNodeEntities);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mLockDetailAdapter);
        mTvTitle.setText(getIntent().getStringExtra("date"));
        getData();
    }
    private void getData() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("date",getIntent().getStringExtra("date"));
        EasyHttp.post("/api/dayincome/lockDetail")
                .params(maps)
                .headers("Authorization", "Bearer " + App.getApplication().getUserEntity().token)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        e.printStackTrace();
                        ToastUtils.showShort("获取信息错误");
                    }

                    @Override
                    public void onSuccess(String string) {
                        dismissLoadding();
                        mNodeEntities.clear();
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            EarnResponse productListResponse = JSONObject.parseObject(string, EarnResponse.class);
                            if (productListResponse.data != null && productListResponse.data != null) {
                                tv_1.setText(productListResponse.data.start);
                                tv_2.setText(productListResponse.data.end);
                                tv_3.setText(productListResponse.data.days+"(天)");
                                mNodeEntities.addAll(productListResponse.data.release);
                            }
                            mLockDetailAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }

                    }
                });
    }
}