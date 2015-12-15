package com.quantum.app.mycourse.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.quantum.app.mycourse.R;
import com.quantum.app.mycourse.activity.LoginActivity;
import com.quantum.app.mycourse.util.HttpUtil;
import com.quantum.app.mycourse.util.MyApplication;

/**
 * Created by hua on 2015/10/29.
 */
public class NotLoginFragment extends Fragment {

    private Button notLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_not_login, container, false);

        notLogin = (Button) view.findViewById(R.id.not_login_button);
        notLogin.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hyperspace_jump));
        notLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
