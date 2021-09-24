package com.block.xjfkchain.ui;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.block.xjfkchain.data.ImageUploadResponse;
import com.block.xjfkchain.data.OrderEntity;
import com.block.xjfkchain.utils.ClipboardUtils;
import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RechargeActivity extends BusinessBaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_icon)
    ImageView mIvCode;
    @BindView(R.id.img_add_pic)
    ImageView img_add_pic;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.et_amount)
    EditText et_amount;
    @BindView(R.id.btn_copy_address)
    Button btn_copy_address;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private String mQrcode;
    private String symbol;
    private String mImageUrl;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();


    @Override
    protected int getContentViewId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mQrcode = getIntent().getStringExtra("address");
        symbol = getIntent().getStringExtra("symbol");
        mTvTitle.setText(symbol+"充值");
        Glide.with(this).load(getIntent().getStringExtra("address_code")).into(mIvCode);
        mTvAddress.setText(mQrcode);
        btn_copy_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtils.copyText(mQrcode);
                ToastUtils.showShort("复制成功");
            }
        });


        img_add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTakePhoto().onPickFromGallery();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitBuy();
            }
        });
    }

    private void commitBuy() {

        HashMap<String, String> maps = new HashMap<>();
        if (TextUtils.isEmpty(mImageUrl)) {
            ToastUtils.showShort("请上传付款凭证");
            return;
        }
        if (TextUtils.isEmpty(et_amount.getText().toString())) {
            ToastUtils.showShort("请填写充值金额");
            return;
        }
        maps.put("symbol", TextUtils.isEmpty(symbol) ? "USDT" :symbol);
        maps.put("amount", et_amount.getText().toString());
        maps.put("pay_screenshot", mImageUrl);
        showLoadding();
        EasyHttp.post("/api/cashout/recharge")
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
                            ToastUtils.showShort("支付成功，请等待审核");
                            finish();
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void takeSuccess(TResult result) {
        if (!TextUtils.isEmpty(result.getImage().getCompressPath())) {
            uploadImage(result.getImage().getCompressPath());
        } else {
            uploadImage(result.getImage().getOriginalPath());
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {
    }

    @Override
    public void takeCancel() {
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    private void uploadImage(final String filePath) {
        mImageUrl = "";
        List<File> files = new ArrayList<>();
        files.add(new File(filePath));
        HashMap<String, String> maps = new HashMap<>();
        maps.put("path", "path");
        maps.put("type", "type");
        showLoadding();
        EasyHttp.post("/api/upload")
                .params(maps)
                .addFileParams("file", files, null)
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
                            ImageUploadResponse imageUploadResponse = JSONObject.parseObject(string, ImageUploadResponse.class);
                            Glide.with(RechargeActivity.this).load(filePath).into(img_add_pic);
                            mImageUrl = imageUploadResponse.data.src;
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });

    }

}
