package com.densha.kimi.kiminodensha;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by 이민호 on 2017-07-19.
 */

public class JoinMemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
    }

    public void JoinButton(View v){
        switch (v.getId()){
            case 1:
                break;
            case 2:
                break;

        }
    }


}
