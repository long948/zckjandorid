package com.block.xjfkchain.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.block.xjfkchain.ui.fragment.FinancialFragment;
import com.block.xjfkchain.ui.fragment.HomeFragment;
import com.block.xjfkchain.ui.fragment.MarketFragment;
import com.block.xjfkchain.ui.fragment.MineFragment;
import com.block.xjfkchain.ui.fragment.ProductFragment;


class ViewPagerAdapter extends FragmentPagerAdapter {
    private Fragment[] mFragments = new Fragment[]{new HomeFragment(), new FinancialFragment(), new ProductFragment(), new MarketFragment(), new MineFragment()};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }
}
