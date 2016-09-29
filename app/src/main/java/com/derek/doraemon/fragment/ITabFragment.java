package com.derek.doraemon.fragment;

/**
 * author  wayde <br/>
 * Date 16/3/4  <br/>
 * Description:
 */
public interface ITabFragment {
    void onTabSelect(boolean selected);

    void onCurrentTabClicked();

    void onPageLeave();

    void onPageInto(BaseFragment fromFragment);

    void reload();
}
