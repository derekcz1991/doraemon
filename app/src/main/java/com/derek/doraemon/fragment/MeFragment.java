package com.derek.doraemon.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.derek.doraemon.R;
import com.derek.doraemon.view.CircleImageView;

/**
 * Created by derek on 16/8/28.
 */
public class MeFragment extends HomeTabFragment {
    CircleImageView userImageView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onPageInto(BaseFragment fromFragment) {

    }
}
