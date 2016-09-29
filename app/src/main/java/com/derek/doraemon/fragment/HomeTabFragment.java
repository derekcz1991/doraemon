package com.derek.doraemon.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import com.derek.doraemon.activity.BaseActivity;
import java.lang.reflect.Field;

/**
 * Created by wayde on 15/9/18.
 */

public abstract class HomeTabFragment extends BaseFragment implements ITabFragment {
    protected boolean isTabSelected;
    public BaseActivity mBaseActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBaseActivity = (BaseActivity) context;
    }

    @Override
    public void setMenuVisibility(boolean menuVisibile) {
        super.setMenuVisibility(menuVisibile);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisibile ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onTabSelect(boolean selected) {
        isTabSelected = selected;
    }

    /**
     * current page is already selected, click the tab again
     */
    @Override
    public void onCurrentTabClicked() {
    }

    @Override
    public void onPageLeave() {

    }

    @Override
    public void reload() {
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBaseActivity = null;
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
