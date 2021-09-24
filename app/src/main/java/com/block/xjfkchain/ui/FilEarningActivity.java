package com.block.xjfkchain.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.UserEntity;
import com.block.xjfkchain.data.UserResponse;
import com.block.xjfkchain.data.ddListEntity;
import com.block.xjfkchain.data.ddResponse;
import com.block.xjfkchain.utils.ClipboardUtils;
import com.google.gson.reflect.TypeToken;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2020, Relx
 * FilEarningActivity
 * <p>
 * Description
 *
 * @author muwenlei
 * @version 1.0
 * <p>
 * Ver 1.0, 2020/10/20, muwenlei, Create file
 */
public class FilEarningActivity extends BusinessBaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.tv_fil)
    TextView mTvFil;
    @BindView(R.id.tv_wallet)
    TextView mTvWallet;
    @BindView(R.id.tv_cz)
    TextView mTvCz;
    @BindView(R.id.tv_tx)
    TextView mTvTx;
    @BindView(R.id.tv_fil_2)
    TextView tv_fil_2;
    @BindView(R.id.tv_usdt_2)
    TextView tv_usdt_2;
    @BindView(R.id.tv_bzz_2)
    TextView tv_bzz_2;
    @BindView(R.id.tv_fil_4)
    TextView tv_fil_4;
    @BindView(R.id.tv_usdt_4)
    TextView tv_usdt_4;
    @BindView(R.id.tv_bzz_4)
    TextView tv_bzz_4;
    @BindView(R.id.tv_fil_3)
    TextView tv_fil_3;
    @BindView(R.id.tv_usdt_3)
    TextView tv_usdt_3;
    @BindView(R.id.tv_bzz_3)
    TextView tv_bzz_3;
    @BindView(R.id.tv_fil_1)
    TextView tv_fil_1;
    @BindView(R.id.tv_fil_5)
    TextView tv_fil_5;
    @BindView(R.id.tv_usdt_1)
    TextView tv_usdt_1;
    @BindView(R.id.tv_bzz_1)
    TextView tv_bzz_1;
    @BindView(R.id.banner)
    MZBannerView banner;
    @BindView(R.id.btn_fil_tx)
    Button btn_fil_tx;
    @BindView(R.id.btn_usdt_tx)
    Button btn_usdt_tx;
    @BindView(R.id.btn_bzz_tx)
    Button btn_bzz_tx;
    @BindView(R.id.btn_usdt_cz)
    Button btn_usdt_cz;
    @BindView(R.id.btn_bzz_cz)
    Button btn_bzz_cz;

    private UserEntity  entity;
    public List<ddListEntity> list;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_fil_earning;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("我的钱包");
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText("收益明细");
        btn_usdt_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilEarningActivity.this, FilWithDrawActivity.class);
                intent.putExtra("entity", entity);
                startActivity(intent);
            }
        });
        btn_fil_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilEarningActivity.this, FilWithDrawActivity.class);
                intent.putExtra("entity", entity);
                startActivity(intent);
            }
        });
        btn_bzz_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilEarningActivity.this, FilWithDrawActivity.class);
                intent.putExtra("entity", entity);
                startActivity(intent);
            }
        });
        btn_usdt_cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilEarningActivity.this, RechargeActivity.class);
                intent.putExtra("address", wallet_addr);
                intent.putExtra("address_code", address_code);
                intent.putExtra("symbol", "USDT");
                startActivity(intent);
            }
        });
        btn_bzz_cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilEarningActivity.this, TransferAccountsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        getNewData();
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void onMTvRightClicked() {
//        Intent intent = new Intent(this, FileEarnListActivity.class);
//        startActivity(intent);
        Intent intent = new Intent(this, IncomeDetailsActivity.class);
        intent.putExtra("list", (Serializable) list);
        startActivity(intent);
    }

    @OnClick(R.id.tv_cz)
    public void onMTvCzClicked() {
        if (entity == null) {
            return;
        }
        Intent intent = new Intent(this, RechargeActivity.class);
        intent.putExtra("address", entity.wallet);
        intent.putExtra("symbol", "USDT");
        startActivity(intent);

    }

    @OnClick(R.id.tv_tx)
    public void onMTvTxClicked() {
        Intent intent = new Intent(FilEarningActivity.this, FilWithDrawActivity.class);
        intent.putExtra("entity", entity);
        startActivity(intent);
    }

    private void getData() {
        HashMap<String, String> maps = new HashMap<>();
        showLoadding();
        LogUtils.e("Bearer " + App.getApplication().getUserEntity().token);
        EasyHttp.post("/api/profile")
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
                            UserEntity userEntity = JSONObject.parseObject(string, UserResponse.class).data;
                            if (userEntity != null) {
                                App.getApplication().setUserEntity(userEntity);
                                showDataView(userEntity);
                            }
                        } else if (returnResponse.code >= 9000 || returnResponse.code == 401) {
                            Intent intent = new Intent(FilEarningActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });

        EasyHttp.post("/api/cashout/usdtWalletInfo")
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
                            UserEntity userEntity = JSONObject.parseObject(string, UserResponse.class).data;
                            wallet_addr= userEntity.wallet_addr;
                            address_code= userEntity.wallet_addr_qrcode;
                        } else if (returnResponse.code >= 9000 || returnResponse.code == 401) {
                            Intent intent = new Intent(FilEarningActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }
    String wallet_addr="";
    String address_code="";
    private void getNewData() {
        HashMap<String, String> maps = new HashMap<>();
        showLoadding();
        LogUtils.e("Bearer " + App.getApplication().getUserEntity().token);
        EasyHttp.post("/api/dayincome/capitalCount")
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
                        list = new ArrayList<>();
                        CommonResponse returnResponse = JSONObject.parseObject(string, CommonResponse.class);
                        if (returnResponse.isSuc()) {
                            List<ddListEntity> userEntity = GsonUtils.fromJson(String.valueOf(returnResponse.data), new TypeToken<List<ddListEntity>>() {
                            }.getType());
                            if (userEntity != null) {
                                list.addAll(userEntity);
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getSymbol().equals("USDT")){
                                        tv_usdt_2.setText(list.get(i).getTotal_income());
                                        tv_usdt_4.setText(list.get(i).getAvail());
                                        tv_usdt_3.setText(list.get(i).getToday_income());
                                        tv_usdt_1.setText(list.get(i).getSymbol()+"总收益");
                                    }else if (list.get(i).getSymbol().equals("FIL")) {
                                        tv_fil_2.setText(list.get(i).getTotal_income());
                                        tv_fil_4.setText(list.get(i).getAvail());
                                        tv_fil_3.setText(list.get(i).getToday_income());
                                        tv_fil_5.setText(list.get(i).getPledge_fil());
                                        tv_fil_1.setText(list.get(i).getSymbol()+"总收益");
                                    }else {
                                        tv_bzz_2.setText(list.get(i).getTotal_income());
                                        tv_bzz_4.setText(list.get(i).getAvail());
                                        tv_bzz_3.setText(list.get(i).getToday_income());
                                        tv_bzz_1.setText(list.get(i).getSymbol()+"总收益");
                                    }

                                }
//                                banner.setPages(list, new MZHolderCreator() {
//                                    @Override
//                                    public BannerViewHolder createViewHolder() {
//                                        return new BannerViewHolder();
//                                    }
//                                });
//                                banner.setIndicatorVisible(false);
                            }
                        } else if (returnResponse.code >= 9000 || returnResponse.code == 401) {
                            Intent intent = new Intent(FilEarningActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }


    public class BannerViewHolder implements MZViewHolder {
        private TextView tv_1;
        private TextView tv_2;//总资产
        private TextView tv_4;//总收益
        private TextView tv_3;//昨日收益
        private Button btn_tx;
        private Button btn_cz;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.item_banner1, null);
            tv_1 = (TextView) view.findViewById(R.id.tv_1);
            tv_2 = (TextView) view.findViewById(R.id.tv_2);
            tv_3 = (TextView) view.findViewById(R.id.tv_3);
            tv_4 = (TextView) view.findViewById(R.id.tv_4);
            btn_tx = (Button) view.findViewById(R.id.btn_tx);
            btn_cz = (Button) view.findViewById(R.id.btn_cz);
            btn_tx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FilEarningActivity.this, FilWithDrawActivity.class);
                    intent.putExtra("entity", entity);
                    startActivity(intent);
                }
            });

            return view;
        }

        @Override
        public void onBind(Context context, int i, Object o) {
            ddListEntity data = (ddListEntity) o;
            if (data.getSymbol().equals("GBZZ")) {
                tv_1.setText("BZZ总资产");
                btn_cz.setVisibility(View.GONE);
            } else if (data.getSymbol().equals("FIL")) {
                tv_1.setText("FIL总资产");
                btn_cz.setVisibility(View.GONE);
            } else {
                tv_1.setText("USDT总资产");
                btn_cz.setVisibility(View.VISIBLE);
            }
            tv_2.setText(data.getAvail());//总资产
            tv_4.setText(data.getTotal_income());//总收益
            tv_3.setText(data.getToday_income());//昨日收益
            btn_cz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FilEarningActivity.this, RechargeActivity.class);
                    intent.putExtra("address", entity.wallet);
                    intent.putExtra("symbol", data.getSymbol());
                    startActivity(intent);
                }
            });
        }
    }

    private void showDataView(UserEntity entity) {
        this.entity = entity;
        mTvFil.setText(entity.fil + "");
        mTvWallet.setText(entity.wallet + "");
    }


    @OnClick(R.id.tv_wallet)
    public void onViewClicked() {
        if (entity != null) {
            ClipboardUtils.copyText(entity.wallet);
            ToastUtils.showShort("复制成功");
        }
    }
}
