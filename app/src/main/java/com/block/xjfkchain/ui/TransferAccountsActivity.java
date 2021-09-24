package com.block.xjfkchain.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;

import butterknife.BindView;

public class TransferAccountsActivity extends BusinessBaseActivity {


    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.edt_account)
    EditText edt_account;
    @BindView(R.id.edt_amount)
    EditText edt_amount;
    @BindView(R.id.btn_commit)
    Button btn_commit;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_transfer_accounts;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("转账");
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edt_account.toString())) {
                    ToastUtils.showShort("信息不全");
                    return;
                }
                if (TextUtils.isEmpty(edt_amount.toString())) {
                    ToastUtils.showShort("信息不全");
                    return;
                }
                HashMap<String, String> maps = new HashMap<>();
                maps.put("amount", edt_amount.getText().toString());
                maps.put("mobile", edt_account.getText().toString());
                showLoadding();
                EasyHttp.post("/api/cashout/transfer")
                        .params(maps)
                        .headers("Authorization", "Bearer " + App.getApplication().getUserEntity().token)
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onError(ApiException e) {
                                dismissLoadding();
                                e.printStackTrace();
                                ToastUtils.showShort("获取信息错误");
                            }

                            @Override
                            public void onSuccess(String string) {
                                dismissLoadding();
                                CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                                if (returnResponse.isSuc()) {
                                    ToastUtils.showShort("提交成功");
                                    finish();
                                } else {
                                    ToastUtils.showShort(returnResponse.msg);
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}