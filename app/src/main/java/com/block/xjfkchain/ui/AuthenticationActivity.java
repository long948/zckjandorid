package com.block.xjfkchain.ui;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.data.CommonResponse;
import com.block.xjfkchain.data.ImageUploadResponse;
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

public class AuthenticationActivity extends BusinessBaseActivity implements TakePhoto.TakeResultListener, InvokeListener {

    private String id_card_front;
    private String id_card_back;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;


    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.edt_xm)
    EditText edt_xm;
    @BindView(R.id.edt_sfz)
    EditText edt_sfz;
    @BindView(R.id.img_card_front)
    ImageView img_card_front;
    @BindView(R.id.img_card_back)
    ImageView img_card_back;
    @BindView(R.id.img_sh_zt)
    ImageView img_sh_zt;
    @BindView(R.id.btn_commit)
    Button btn_commit;
    @BindView(R.id.btn_reset_commit)
    Button btn_reset_commit;
    @BindView(R.id.no_pw_linear)
    LinearLayout no_pw_linear;
    @BindView(R.id.ll_user_show)
    LinearLayout ll_user_show;
    @BindView(R.id.tv_tip_msg)
    TextView tv_tip_msg;

    int imgType = 0;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_authentication;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        mTvTitle.setText("实名认证");
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             finish();
            }
        });
        img_card_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgType = 0;
                getTakePhoto().onPickFromGallery();
            }
        });
        img_card_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgType = 1;
                getTakePhoto().onPickFromGallery();
            }
        });
        btn_reset_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no_pw_linear.setVisibility( View.VISIBLE);
                ll_user_show.setVisibility(  View.GONE);
            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edt_xm.getText().toString())) {
                    ToastUtils.showShort("信息不全");
                    return;
                }
                if (TextUtils.isEmpty(edt_sfz.getText().toString())) {
                    ToastUtils.showShort("信息不全");
                    return;
                }
                if (TextUtils.isEmpty(id_card_front.toString())) {
                    ToastUtils.showShort("信息不全");
                    return;
                }
                if (TextUtils.isEmpty(id_card_back.toString())) {
                    ToastUtils.showShort("信息不全");
                    return;
                }
                HashMap<String, String> maps = new HashMap<>();
                maps.put("id_card", edt_sfz.getText().toString());
                maps.put("id_card_name", edt_xm.getText().toString());
                maps.put("id_card_front", id_card_front);
                maps.put("id_card_back", id_card_back);
                showLoadding();
                EasyHttp.post("/api/idCardAuth")
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
                                    ToastUtils.showShort("提交成功，请等待审核");
                                    finish();
                                } else {
                                    ToastUtils.showShort(returnResponse.msg);
                                }
                            }
                        });
            }
        });

        String is_id_card_auth = getIntent().getStringExtra("is_id_card_auth");
        //0：未实名，1，已实名，2：待审核，-1：审核未通过
        switch (is_id_card_auth) {
            case "0":
                no_pw_linear.setVisibility(View.VISIBLE);
                ll_user_show.setVisibility(View.GONE);
                break;
            case "1":
                img_sh_zt.setImageResource(R.drawable.ic_check_circle_black_24dp);
                btn_reset_commit.setVisibility(View.GONE);
                no_pw_linear.setVisibility(View.GONE);
                ll_user_show.setVisibility(View.VISIBLE);
                tv_tip_msg.setText("实名认证成功");
                break;
            case "2":
                img_sh_zt.setImageResource(R.drawable.ic_info_black_24dp);
                no_pw_linear.setVisibility(View.GONE);
                btn_reset_commit.setVisibility(View.GONE);
                ll_user_show.setVisibility(View.VISIBLE);
                tv_tip_msg.setText("待审核");
                break;
            case "-1":
                img_sh_zt.setImageResource(R.drawable.ic_info_black_24dp);
                no_pw_linear.setVisibility(View.GONE);
                ll_user_show.setVisibility(View.VISIBLE);
                btn_reset_commit.setVisibility(View.VISIBLE);
                tv_tip_msg.setText("审核未通过");
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
                            if (imgType == 0) {
                                Glide.with(AuthenticationActivity.this).load(filePath).into(img_card_front);
                                id_card_front = imageUploadResponse.data.src;
                            } else {
                                Glide.with(AuthenticationActivity.this).load(filePath).into(img_card_back);
                                id_card_back = imageUploadResponse.data.src;
                            }
                        } else {
                            ToastUtils.showShort(returnResponse.msg);
                        }
                    }
                });

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}