package com.derek.doraemon.fragment;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.OnClick;
import com.derek.doraemon.R;

/**
 * Created by derek on 16/8/18.
 */
public class HeartFragment extends HomeTabFragment {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.cameraFab) FloatingActionButton cameraFab;

    @Override
    public void onPageInto(BaseFragment fromFragment) {

    }

    @OnClick(R.id.adoptBtn)
    public void checkAdopt() {

    }

    @OnClick(R.id.breedBtn)
    public void checkBreed() {

    }

    @OnClick(R.id.findBtn)
    public void checkFind() {

    }

    @OnClick(R.id.qaBtn)
    public void checkQa() {

    }
}
