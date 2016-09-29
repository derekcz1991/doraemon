package com.derek.doraemon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.derek.doraemon.R;
import com.derek.doraemon.fragment.HomeFragment;
import com.derek.doraemon.fragment.HomeTabFragment;
import com.derek.doraemon.fragment.TabFragmentManager;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private static final String SAVE_PAGE_INDEX = "current_page_index";
    private static final int[] FRAGMENT_IDS =
        { R.id.tab_home, R.id.tab_heart, R.id.tab_pet, R.id.tab_chat, R.id.tab_me };
    private TabFragmentManager.TabFragmentAdapter mTabFragmentAdapter;
    private RadioGroup mBottomNavigationBar;
    private FrameLayout mContent;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView(savedInstanceState);
        setTabSelected(R.id.tab_home);
    }

    private void initView(Bundle savedInstanceState) {
        mTabFragmentAdapter = new HomeTabFragmentAdapter(R.id.home_content);
        mBottomNavigationBar = (RadioGroup) findViewById(R.id.bottom_tab);
        mContent = (FrameLayout) findViewById(R.id.home_content);
        findViewById(R.id.tab_home).setOnClickListener(this);
        findViewById(R.id.tab_heart).setOnClickListener(this);
        findViewById(R.id.tab_pet).setOnClickListener(this);
        findViewById(R.id.tab_chat).setOnClickListener(this);
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
        int clickedTableIndex = getTabIndexByResId(selectedResId);

        if (isMyAccountTab(clickedTableIndex)) {
            // 点击我的页面时，如果未登录就跳到登录页面
            /*Intent intent = IntentHelper.getLoginIntent();
            startActivityForResult(intent, IntentConstants.REQUEST_CODE_LOGIN);
            overridePendingTransition(R.anim.slide_bottom_in, R.anim.no);*/
            return;
        }
        // 通知一下tab的点击事件
        TabFragmentManager.getInstance(this).performTabSelected(mContent, selectedResId);
        mBottomNavigationBar.check(selectedResId);
    }

    private int getTabIndexByResId(int selectedResId) {
        for (int i = 0; i < FRAGMENT_IDS.length; i++) {
            if (FRAGMENT_IDS[i] == selectedResId) {
                return i;
            }
        }
        return 0;
    }

    private boolean isMyAccountTab(int clickedTableIndex) {
        return clickedTableIndex == 4;// && !ManagerFactory.getAccountManager().isLogin();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SAVE_PAGE_INDEX, mBottomNavigationBar.getCheckedRadioButtonId());
        super.onSaveInstanceState(outState);
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
                /*case R.id.tab_loan:
                    fragment = new BaseWebViewFragment();
                    initWebViewFragment(fragment,Url.TAB_LOAN_URL,getString(R.string.loan));
                    break;
                case R.id.tab_discovery:
                    fragment = new BaseWebViewFragment();
                    initWebViewFragment(fragment, Url.TAB_DISCOVERY_URL, getString(R.string.discovery));
                    break;
                case R.id.tab_me:
                    fragment = new MyAccountFragment();
                    break;*/
            }
            return fragment;
        }
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
