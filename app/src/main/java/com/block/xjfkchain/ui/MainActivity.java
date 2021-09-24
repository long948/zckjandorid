package com.block.xjfkchain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SPUtils;
import com.block.xjfkchain.R;
import com.block.xjfkchain.app.App;
import com.block.xjfkchain.base.BusinessBaseActivity;
import com.block.xjfkchain.constant.Constants;
import com.block.xjfkchain.data.LoginEntity;
import com.block.xjfkchain.utils.BottomNavigationViewHelper;

import butterknife.BindView;

public class MainActivity extends BusinessBaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationView mBottomNavigationBar;
    @BindView(R.id.viewpager)
    com.block.xjfkchain.utils.NoScrollViewPager viewpager;
    private FragmentManager mFragmentManager;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        LoginEntity userEntity = JSONObject.parseObject(SPUtils.getInstance().getString(Constants.SP_USER), LoginEntity.class);
        if (userEntity == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            App.getApplication().setUserEntity(userEntity);
        }
        mFragmentManager = getSupportFragmentManager();
        initBottomNavigation();
    }

    private void initBottomNavigation() {
        mBottomNavigationBar.setOnNavigationItemSelectedListener(this);
        mBottomNavigationBar.setItemIconTintList(null);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationBar);
        mBottomNavigationBar.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        ViewPagerAdapter mMainAdapter = new ViewPagerAdapter(this.mFragmentManager);
        viewpager.setAdapter(mMainAdapter);
        viewpager.setOffscreenPageLimit(5);
        viewpager.setCurrentItem(0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_home: {
                viewpager.setCurrentItem(0);
                return true;
            }
            case R.id.navigation_licai: {
                viewpager.setCurrentItem(1);
                return true;
            }
            case R.id.navigation_chanpin: {
                viewpager.setCurrentItem(2);
                return true;
            }
            case R.id.navigation_hangqing: {
                viewpager.setCurrentItem(3);
                return true;
            }
            case R.id.navigation_wode: {
                viewpager.setCurrentItem(4);
                return true;
            }
        }
        return false;
    }
}
