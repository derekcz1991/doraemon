package com.derek.doraemon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.derek.doraemon.R;
import com.derek.doraemon.fragment.WelfareFragment;
import com.derek.doraemon.fragment.HomeFragment;
import com.derek.doraemon.fragment.HomeTabFragment;
import com.derek.doraemon.fragment.MeFragment;
import com.derek.doraemon.fragment.MomentFragment;
import com.derek.doraemon.fragment.NearbyFragment;
import com.derek.doraemon.fragment.TabFragmentManager;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private static final String SAVE_PAGE_INDEX = "current_page_index";
    private static final int[] FRAGMENT_IDS =
        {R.id.tab_home, R.id.tab_heart, R.id.tab_nearby, R.id.tab_moment, R.id.tab_me};
    private TabFragmentManager.TabFragmentAdapter mTabFragmentAdapter;
    private RadioGroup mBottomNavigationBar;
    private FrameLayout mContent;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        initView(savedInstanceState);
        setTabSelected(R.id.tab_nearby);

    }

    private void initView(Bundle savedInstanceState) {
        mTabFragmentAdapter = new HomeTabFragmentAdapter(R.id.home_content);
        mBottomNavigationBar = (RadioGroup) findViewById(R.id.bottom_tab);
        mContent = (FrameLayout) findViewById(R.id.home_content);
        findViewById(R.id.tab_home).setOnClickListener(this);
        findViewById(R.id.tab_heart).setOnClickListener(this);
        findViewById(R.id.tab_nearby).setOnClickListener(this);
        findViewById(R.id.tab_moment).setOnClickListener(this);
        findViewById(R.id.tab_me).setOnClickListener(this);
        initFragment(savedInstanceState);
    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreFragment(savedInstanceState);
        } else {
            mBottomNavigationBar.check(R.id.tab_home);
            TabFragmentManager.getInstance(this).performTabSelected(mContent, R.id.tab_home);
        }
    }

    private void restoreFragment(Bundle savedInstanceState) {
        TabFragmentManager.getInstance(this).popAllFragments();
        int checkId = savedInstanceState.getInt(SAVE_PAGE_INDEX);
        if (checkId == 0) {
            checkId = R.id.tab_home;
        }
        mBottomNavigationBar.check(checkId);
        TabFragmentManager.getInstance(this).performTabSelected(mContent, checkId);
    }

    public void setTabSelected(int selectedResId) {
        // 通知一下tab的点击事件
        TabFragmentManager.getInstance(this).performTabSelected(mContent, selectedResId);
        mBottomNavigationBar.check(selectedResId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HomeTabFragment currentFragment = TabFragmentManager.getInstance(this).getCurrentFragment();
        if (currentFragment != null) {
            currentFragment.onPageInto(null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        HomeTabFragment currentFragment = TabFragmentManager.getInstance(this).getCurrentFragment();
        if (currentFragment != null) {
            currentFragment.onPageLeave();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = TabFragmentManager.getInstance(this).getCurrentFragment();
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        TabFragmentManager.getInstance(this).onDestroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        for (int i = 0; i < FRAGMENT_IDS.length; i++) {
            if (FRAGMENT_IDS[i] == id) {
                setTabSelected(id);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 5000) {// 5s内点击2次退出才可以
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class HomeTabFragmentAdapter extends TabFragmentManager.TabFragmentAdapter {

        public HomeTabFragmentAdapter(int containerViewId) {
            super(containerViewId);
        }

        @Override
        public HomeTabFragment getItem(int tabId) {
            HomeTabFragment fragment = null;
            switch (tabId) {
                case R.id.tab_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.tab_heart:
                    fragment = new WelfareFragment();
                    break;
                case R.id.tab_nearby:
                    fragment = new NearbyFragment();
                    break;
                case R.id.tab_moment:
                    fragment = new MomentFragment();
                    break;
                case R.id.tab_me:
                    fragment = new MeFragment();
                    break;
            }
            return fragment;
        }
    }

}
