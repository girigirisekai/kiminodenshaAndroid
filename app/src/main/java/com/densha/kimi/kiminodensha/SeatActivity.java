package com.densha.kimi.kiminodensha;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by haleylovespurple on 7/27/17.
 */

public class SeatActivity extends AppCompatActivity{


    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;
    ImageView img6;
    ImageView img7;
    ImageView img8;
    ImageView img9;
    ImageView img10;
    ImageView img11;
    ImageView img12;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);


        img1 = (ImageView)findViewById(R.id.pokemon1);
        img2 = (ImageView)findViewById(R.id.pokemon2);
        img3 = (ImageView)findViewById(R.id.pokemon3);
        img4 = (ImageView)findViewById(R.id.pokemon4);
        img5 = (ImageView)findViewById(R.id.pokemon5);
        img6 = (ImageView)findViewById(R.id.pokemon6);
        img7 = (ImageView)findViewById(R.id.pokemon7);
        img8 = (ImageView)findViewById(R.id.pokemon8);
        img9 = (ImageView)findViewById(R.id.pokemon9);
        img10 = (ImageView)findViewById(R.id.pokemon10);
        img11 = (ImageView)findViewById(R.id.pokemon11);
        img12 = (ImageView)findViewById(R.id.pokemon12);

        setSat();
        setSat2();

    }


    //무채색으로 만드는 코드
    public void setSat(){

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);                        //0이면 grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        img1.setColorFilter(cf);
        img2.setColorFilter(cf);
        img3.setColorFilter(cf);
        img4.setColorFilter(cf);
        img5.setColorFilter(cf);
        img6.setColorFilter(cf);
        img7.setColorFilter(cf);
        img8.setColorFilter(cf);
        img9.setColorFilter(cf);
        img10.setColorFilter(cf);
        img11.setColorFilter(cf);
        img12.setColorFilter(cf);

    }


    //색 다시 활성화시키는 코드
    public void setSat2(){

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(10);                        //0이면 grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        img1.setColorFilter(cf);
        img2.setColorFilter(cf);
        img3.setColorFilter(cf);
        img4.setColorFilter(cf);
        img5.setColorFilter(cf);
        img6.setColorFilter(cf);
        img7.setColorFilter(cf);
        img8.setColorFilter(cf);
        img9.setColorFilter(cf);
        img10.setColorFilter(cf);
        img11.setColorFilter(cf);
        img12.setColorFilter(cf);

    }


}
