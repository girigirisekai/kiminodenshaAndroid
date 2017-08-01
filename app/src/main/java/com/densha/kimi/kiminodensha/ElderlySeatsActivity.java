package com.densha.kimi.kiminodensha;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

/**
 * ElderlySeats
 */
public class ElderlySeatsActivity extends AppCompatActivity {

    //변수
    Intent intent=null;
    String carNum="";
    String elderlySeats="";
    static int elderlySeatsLength=12;
    ImageView[] imageViewArray=new ImageView[12];
    //메소드
    //onCreate
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);

        // 화면을 가로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //칸번호와 노약자석 정보 로드
        intent=getIntent();
        carNum=intent.getStringExtra("carNum");
        elderlySeats=intent.getStringExtra("elderlySeats");
        Log.d("칸번호",carNum);
        Log.d("노약자석",elderlySeats);

        //이미지뷰 로드
        imageViewArray[0]=(ImageView)findViewById(R.id.pokemon1);
        imageViewArray[1]=(ImageView)findViewById(R.id.pokemon2);
        imageViewArray[2]=(ImageView)findViewById(R.id.pokemon3);
        imageViewArray[3]=(ImageView)findViewById(R.id.pokemon4);
        imageViewArray[4]=(ImageView)findViewById(R.id.pokemon5);
        imageViewArray[5]=(ImageView)findViewById(R.id.pokemon6);
        imageViewArray[6]=(ImageView)findViewById(R.id.pokemon7);
        imageViewArray[7]=(ImageView)findViewById(R.id.pokemon8);
        imageViewArray[8]=(ImageView)findViewById(R.id.pokemon9);
        imageViewArray[9]=(ImageView)findViewById(R.id.pokemon10);
        imageViewArray[10]=(ImageView)findViewById(R.id.pokemon11);
        imageViewArray[11]=(ImageView)findViewById(R.id.pokemon12);

        //이미지 색 초기화
        for(int i=0;i<imageViewArray.length;i++){
            imageViewArray[i].setColorFilter(Color.GRAY);

        }

        //사람 유무로 이미지 색 변경
        for(int i=0;i<elderlySeatsLength;i++){
            if(elderlySeats.charAt(i)=='1'){
                imageViewArray[i].setColorFilter(null);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_seat);

        //칸번호와 노약자석 정보 로드
        intent=getIntent();
        carNum=intent.getStringExtra("carNum");
        elderlySeats=intent.getStringExtra("elderlySeats");
        Log.d("칸번호",carNum);
        Log.d("노약자석",elderlySeats);

        //이미지뷰 로드
        imageViewArray[0]=(ImageView)findViewById(R.id.pokemon1);
        imageViewArray[1]=(ImageView)findViewById(R.id.pokemon2);
        imageViewArray[2]=(ImageView)findViewById(R.id.pokemon3);
        imageViewArray[3]=(ImageView)findViewById(R.id.pokemon4);
        imageViewArray[4]=(ImageView)findViewById(R.id.pokemon5);
        imageViewArray[5]=(ImageView)findViewById(R.id.pokemon6);
        imageViewArray[6]=(ImageView)findViewById(R.id.pokemon7);
        imageViewArray[7]=(ImageView)findViewById(R.id.pokemon8);
        imageViewArray[8]=(ImageView)findViewById(R.id.pokemon9);
        imageViewArray[9]=(ImageView)findViewById(R.id.pokemon10);
        imageViewArray[10]=(ImageView)findViewById(R.id.pokemon11);
        imageViewArray[11]=(ImageView)findViewById(R.id.pokemon12);

        //이미지 색 초기화
        for(int i=0;i<imageViewArray.length;i++){
            imageViewArray[i].setColorFilter(Color.GRAY);
        }

        //사람 유무로 이미지 색 변경
        for(int i=0;i<elderlySeatsLength;i++){
            if(elderlySeats.charAt(i)=='1'){
                imageViewArray[i].setColorFilter(null);
            }
        }
    }
}
