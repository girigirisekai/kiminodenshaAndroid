package com.densha.kimi.kiminodensha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/*
* 폰트는 assets패키지 생성해서 거기에 fonts폴더 생성하고 저장함
* 애니메이션 설정은 res패키지의 anim폴더에 xml을 생성해서 설정
* 전체 화면 전환은 androidManifest.xml과 MainActivity클래스에서 설정
* 메인화면 어디라도 클릭 시 로그인화면으로 전환
* */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //레이아웃을 풀스크린으로 전환
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //메인엑티비티 실행
        setContentView(R.layout.activity_main);

        //폰트 로드
        Typeface dynalight=Typeface.createFromAsset(getAssets(),"fonts/Dynalight-Regular.ttf");
        Typeface dalbit=Typeface.createFromAsset(getAssets(),"fonts/dalbit.ttf");

        //메인레이아웃의 뷰들 로드
        ImageView titleLogo=(ImageView)findViewById(R.id.titleLogo);
        TextView tvTitleTxt=(TextView)findViewById(R.id.titleTxt);
        TextView tvClickTxt=(TextView)findViewById(R.id.clickTxt);

        //폰트 설정
        tvTitleTxt.setTypeface(dalbit);
        tvClickTxt.setTypeface(dynalight);

        //애니메이션 로드
        Animation alpha_title= AnimationUtils.loadAnimation( this,R.anim.alpha_title);
        Animation alpha= AnimationUtils.loadAnimation( this,R.anim.alpha);

        //에니메이션 설정
        titleLogo.startAnimation(alpha_title);
        tvTitleTxt.startAnimation(alpha_title);
        tvClickTxt.startAnimation(alpha);
    }

    public void mainClick(View view){
        //DB에 접근해서 계정 데이터 유무 확인
        SharedPreferences preferences=getSharedPreferences("login_prefs",MODE_PRIVATE);
        String loginId=preferences.getString("loginId","");
        String loginPassword=preferences.getString("lgoinPassword","");
        Log.d("에","ㅇ안뜨나");
        //있으면 로그인화면에 가져와서 아이디 비밀번호 넣고 자동 로그인버튼 실행
        if(!loginId.equals("")&&!loginPassword.equals("")){
            //자동로그인해주는 메소드 실행
        }
        // 없으면 빈 로그인화면으로 전환
        else{
            Log.d("dd", "로그");
            Intent intent = new Intent(this, LoginActivity.class); // 다음 넘어갈 클래스 지정
            startActivity(intent);
            Log.d("ㅇㅇ", "ㅇㅇ");
        }




    }
}
