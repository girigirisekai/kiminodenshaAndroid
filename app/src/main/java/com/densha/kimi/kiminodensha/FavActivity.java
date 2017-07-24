package com.densha.kimi.kiminodensha;


import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
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
 * Created by haleylovespurple on 7/20/17.
 */

public class FavActivity extends AppCompatActivity{

    String id;
    TextView txt;
    LinearLayout favMain;
    Animation alpha=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        id = "nooti";

        favMain = (LinearLayout) findViewById(R.id.favMain);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        final ImageView pikachu=(ImageView)findViewById(R.id.pikachu);
        LinearLayout favLinearLayout=(LinearLayout)findViewById(R.id.favMain);
        alpha= AnimationUtils.loadAnimation(this, R.anim.alpha_pikachu);

        pikachu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("피카츄","실행");
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    pikachu.startAnimation(alpha);
                    MediaPlayer mediaPlayer=MediaPlayer.create(FavActivity.this,R.raw.pikachuuu);
                    mediaPlayer.setVolume(0.8f, 0.8f);
                    mediaPlayer.setLooping(false);
                    mediaPlayer.start();
                    //mediaPlayer.stop();
                    Log.d("팝업메뉴","실행");
                    PopupMenu popup= new PopupMenu(FavActivity.this, view);
                    popup.getMenuInflater().inflate(R.menu.option_menu,popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.searchStation:
                                    Log.d("검색하기","실행");
                                    Toast.makeText(getApplication(),"역검색하기",Toast.LENGTH_SHORT).show();
                                    Log.d("검색하기","완료");
                                    break;
                                case R.id.logout:
                                    Log.d("로그아웃","실행");
                                    Toast.makeText(getApplication(),"로그아웃",Toast.LENGTH_SHORT).show();
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
    }

    // id로 json읽는 부분
    public void favBtn (View v){

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
                    out.write("nooti".getBytes("UTF-8"));
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
                                txt = (TextView) findViewById(R.id.textBox);
                                txt.setText(result);
                                txt.append("\n");
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

                                    //favMain.addView(linearLayout);

                                }
                                txt.setText(sb2.toString());
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




}
