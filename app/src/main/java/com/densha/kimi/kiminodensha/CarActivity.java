package com.densha.kimi.kiminodensha;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Car
 */
public class CarActivity extends AppCompatActivity {

    //메소드
    //onCreate
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);

        //열개의 량의 데이터를 가져옴
        //데이터는 사람수, 혼잡도율
        //혼잡율에 따라 칸의 색깔 다름  ---> 빨강
    }
}
