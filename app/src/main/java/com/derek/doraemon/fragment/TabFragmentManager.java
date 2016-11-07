package com.derek.doraemon.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * author wayde <br/>
 * Date 16/3/4 <br/>
 * Description:
 */
public class TabFragmentManager {
    private static TabFragmentManager sInstance;
    private static FragmentManager sFragmentManager;
    private int mCurrentSelectTabId;
    private static Map<Integer, TabFragmentAdapter> mFragmentAdapterMap = new HashMap<>();
    private static Map<Integer, HomeTabFragment> mFragmentsMap;
    private static final String TAG = TabFragmentManager.class.getName();

    private TabFragmentManager(FragmentActivity fragmentActivity) {
        sFragmentManager = fragmentActivity.getSupportFragmentManager();
        mFragmentsMap = new HashMap<>();
    }

    public static TabFragmentManager getInstance(FragmentActivity fragmentActivity) {
        if (sInstance == null) {
            sInstance = new TabFragmentManager(fragmentActivity);
        }
        return sInstance;
    }

    public static void popAllFragments() {
        try {
            for (int i = 0, count = sFragmentManager.getBackStackEntryCount(); i < count; i++) {
                sFragmentManager.popBackStack();
            }
        } catch (Exception e) {
            Log.d(TAG, "", e);
        }
    }

    private static void addTabFragmentToContainner(HomeTabFragment titleBarFragment,
        int containerViewId, int tabId, String tag) {
        try {
            FragmentTransaction fragmentTransaction = sFragmentManager.beginTransaction();
            fragmentTransaction.add(containerViewId, titleBarFragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
            mFragmentsMap.put(tabId, titleBarFragment);
        } catch (Exception e) {
            Log.d(TAG, "", e);
        }
    }

    public void setFragmentVisibleById(final int tabId) {
        final FragmentTransaction fragmentTrans = sFragmentManager.beginTransaction();
        final HomeTabFragment preFragment = getCurrentFragment();
        hideOtherFragment(tabId, preFragment);
        showCurrentFragment(tabId, fragmentTrans, preFragment);
    }

    public HomeTabFragment getFragmentById(int tabId) {
        return mFragmentsMap.get(tabId);
    }

    private void hideOtherFragment(int tabId, HomeTabFragment preFragment) {
        Set<Map.Entry<Integer, HomeTabFragment>> entrySet = mFragmentsMap.entrySet();
        for (Map.Entry<Integer, HomeTabFragment> entry : entrySet) {
            int fragmentTabId = entry.getKey();
            if (fragmentTabId == tabId) {
                continue;
            }
            hideFragmentByTabId(fragmentTabId);
        }
        if (preFragment != null) {
            preFragment.onPageLeave();
        }
    }

    private void showCurrentFragment(int tabId, FragmentTransaction fragmentTrans,
        HomeTabFragment preFragment) {
        HomeTabFragment toFragment = mFragmentsMap.get(tabId);

        if (toFragment != null) {
            fragmentTrans.show(toFragment);
            toFragment.setUserVisibleHint(true);
            toFragment.setMenuVisibility(true);
            toFragment.onPageInto(preFragment);
            mCurrentSelectTabId = tabId;
            fragmentTrans.commitAllowingStateLoss();
        }
    }

    private void hideFragmentByTabId(int tabId) {
        Fragment fragment = mFragmentsMap.get(tabId);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = sFragmentManager.beginTransaction();
            fragmentTransaction.hide(fragment);
            fragment.setUserVisibleHint(false);
            fragment.setMenuVisibility(false);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public HomeTabFragment getCurrentFragment() {
        return mFragmentsMap.get(mCurrentSelectTabId);
    }

    public void performTabSelected(View containerView, int onClickId) {
        TabFragmentAdapter adapter = mFragmentAdapterMap.get(containerView.getId());
        if (mFragmentsMap.get(onClickId) == null) {
            adapter.createFragment(onClickId);
        } else if (onClickId == mCurrentSelectTabId) {
            getCurrentFragment().onCurrentTabClicked();
        }
        setFragmentVisibleById(onClickId);
        setTabSelected(onClickId);
    }

    public void setTabSelected(int onClickId) {
        Set<Map.Entry<Integer, HomeTabFragment>> entrySet = mFragmentsMap.entrySet();
        for (Map.Entry<Integer, HomeTabFragment> entry : entrySet) {
            int fragmentTabId = entry.getKey();
            HomeTabFragment fragment = mFragmentsMap.get(fragmentTabId);
            if (fragment == null) {
                continue;
            }
            if (onClickId == fragmentTabId) {
                fragment.onTabSelect(true);
            } else {
                fragment.onTabSelect(false);
            }
        }
    }

    public static abstract class TabFragmentAdapter {
        private Map<Integer, HomeTabFragment> mTabFragmentMap;
        private int mContainerViewId;

        public TabFragmentAdapter(int containerViewId) {
            this.mTabFragmentMap = new HashMap<>();
            mFragmentAdapterMap.put(containerViewId, this);
            mContainerViewId = containerViewId;
        }

        public void createFragment(int tabId) {
            HomeTabFragment fragment = getItem(tabId);
            if (fragment != null) {
                mTabFragmentMap.put(tabId, fragment);
                addTabFragmentToContainner(fragment, mContainerViewId, tabId,
                    getFragmentTagsById(tabId));
            }
        }

        public abstract HomeTabFragment getItem(int tabId);
    }

    public static String getFragmentTagsById(int id) {
        return "fragments:" + id;
    }

    public static void onDestroy() {
        mFragmentAdapterMap.clear();
        mFragmentsMap.clear();
        sInstance = null;
        popAllFragments();
        sFragmentManager = null;
        sInstance = null;
    }
}
