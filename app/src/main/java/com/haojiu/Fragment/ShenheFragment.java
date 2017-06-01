package com.haojiu.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haojiu.communitymanager.R;

/**
 * Created by leehom on 2017/4/28.
 */

public class ShenheFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shenhe_layout, container, false);
        setView(view);
        return view;
    }

    public void setView(View view) {

    }
}
