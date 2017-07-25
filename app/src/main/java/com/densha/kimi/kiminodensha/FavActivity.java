package com.densha.kimi.kiminodensha;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Favorite
 */

public class FavActivity extends AppCompatActivity{

    //변수
    Intent intent;
    LinearLayout favMain;
    Animation alpha=null;
    String id;
    ImageView pikachu;

    //메소드
    //onCreate
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        //아이디 가져오기
        intent=getIntent();
        id=intent.getExtras().getString("id");
        Log.d("아이디",id);

        //서버 전송을 위한 설정
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //객체 설정
        favMain = (LinearLayout) findViewById(R.id.favMain);
        pikachu=(ImageView)findViewById(R.id.pikachu);
        LinearLayout favLinearLayout=(LinearLayout)findViewById(R.id.favMain);
        alpha= AnimationUtils.loadAnimation(this, R.anim.alpha_pikachu);

        //피카츄 버튼 터치리스너
        pikachu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("피카츄","실행");
                //버튼 눌릴 시
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    //투명 애니메이션 실행
                    pikachu.startAnimation(alpha);

                    //피카츄 소리 실행
                    MediaPlayer mediaPlayer=MediaPlayer.create(FavActivity.this,R.raw.pikachuuu);
                    mediaPlayer.setVolume(0.8f, 0.8f);
                    mediaPlayer.setLooping(false);
                    mediaPlayer.start();
                    //mediaPlayer.stop();
//
                    Log.d("팝업메뉴","실행");
                    PopupMenu popup= new PopupMenu(FavActivity.this, view);
                    popup.getMenuInflater().inflate(R.menu.option_menu,popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //팝업메뉴에서 눌린 버튼에 따라 선택 사항
                            switch (item.getItemId()){
                                case R.id.searchStation:
                                    Log.d("검색하기","실행");
                                    //Toast.makeText(getApplication(),"역검색하기",Toast.LENGTH_SHORT).show();
                                    intent=new Intent(FavActivity.this, SearchStationActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                    Log.d("검색하기","완료");
                                    break;
                                case R.id.logout:
                                    Log.d("로그아웃","실행");
                                    //Toast.makeText(getApplication(),"로그아웃",Toast.LENGTH_SHORT).show();
                                    logOUt();
                                    Log.d("로그아웃","완료");
                                    break;
                                default:
                                    Log.d("이상한 버튼","실행됨");
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                    Log.d("팝업메뉴","완료");
                }
                Log.d("피카츄","완료");
                return true;
            }
        });

        favoriteList();
    }

    // id로 json읽는 부분
    public void favoriteList (){

        String getId = id;
        URL url = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();
        Log.d("id", getId);
        try {
            Log.d("ip주소 ", "203.233.196.139");
            //http://10.0.2.2:8888
            url = new URL("http://203.233.196.139:8888/densha/Androidfavorite");
        } catch (MalformedURLException e) {
            Toast.makeText(this, "잘못된 주소입니다!", Toast.LENGTH_SHORT).show();
        }
        try {
            con = (HttpURLConnection) url.openConnection();

            if (con != null) {
                con.setConnectTimeout(10000);
                con.setUseCaches(false);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream out = con.getOutputStream();
                out.write(id.getBytes("UTF-8"));
                out.flush();
                out.close();

                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    InputStreamReader in = new InputStreamReader(con.getInputStream());
                    int ch;
                    while ((ch = in.read()) != -1) {
                        sb.append((char) ch);
                    }
                    in.close();
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    String result = jsonObject.getString("RESULT");
                    Log.d("로그인결과", result);
                    //서버로부터 수신된 데이터 학인
                    //1. 전송실패
                    switch (result) {
                        case "NULL":
                            Log.d("Favorite", "불일치");
                            break;
                        case "OK":
                            JSONObject item = null;

                            JSONArray jarray = jsonObject.getJSONArray("item");
                            StringBuilder sb2 = new StringBuilder();
                            for (int i = 0; i < jarray.length(); i++) {
                                item = jarray.getJSONObject(i);

                                LinearLayout linearLayout = new LinearLayout(this);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                                        (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);
                                linearLayout.setOrientation(LinearLayout.VERTICAL);

                                TextView textView1 = new TextView(this);
                                textView1.setText("FAVORITENAME : "+item.getString("FAVORITENAME"));

                                TextView textView2 = new TextView(this);
                                textView2.setText("STATIONCODE : "+item.getString("STATIONCODE"));

                                TextView textView3 = new TextView(this);
                                textView3.setText("LINE : "+item.getString("LINE"));

                                TextView textView4 = new TextView(this);
                                textView4.setText("FCODE : "+item.getString("FCODE"));

                                linearLayout.addView(textView1);
                                linearLayout.addView(textView2);
                                linearLayout.addView(textView3);
                                linearLayout.addView(textView4);

                                favMain.addView(linearLayout);

                            }
                            Log.d("Favorite", "완료");
                            break;
                        default:
                            Log.d("로그인", "실패");
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void logOUt(){
        Log.d("로그아웃","실행");
        new AlertDialog.Builder(this)
                .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("로그아웃", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton) {
                        clearPreferences();
                        Intent intent = new Intent(FavActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
        Log.d("로그아웃","완료");
    }

    //Preferences 로그인 데이터 클리어 메소드
    public void clearPreferences(){
        Log.d("Preferences클리어","실행");
        SharedPreferences preferences=getSharedPreferences("login_prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("loginId");
        editor.remove("loginPassword");
        editor.commit();
        Log.d("Preferences클리어","완료");
    }
}
