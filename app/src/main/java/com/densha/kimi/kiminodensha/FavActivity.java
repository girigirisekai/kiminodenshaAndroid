package com.densha.kimi.kiminodensha;


import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
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
import java.util.ArrayList;

/**
 * Favorite
 */

public class FavActivity extends AppCompatActivity{

    //변수
    Intent intent;
    LinearLayout favMain;
    Animation alpha=null;
    String id;
    ImageView pokemon;
    int randomChar;
    MediaPlayer mediaPlayer;
    JSONObject item = null;
    ArrayList <String> favoriteArray;
    ArrayList <String> codeArray;
    int btnTagNum;

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
        pokemon=(ImageView)findViewById(R.id.pokemon);
        LinearLayout favLinearLayout=(LinearLayout)findViewById(R.id.favMain);
        alpha= AnimationUtils.loadAnimation(this, R.anim.alpha_pokemon);

        //1~7까지 난수 생성
        Log.d("난수", "생성");
        randomChar=(int)(Math.random()*7)+1;
        Log.d("난수", Integer.toString(randomChar));
        //버튼 이미지와 사운드 랜덤으로 선택
        switch(randomChar){
            case 1:
                //피카츄
                pokemon.setImageResource(R.drawable.pikachu);
                mediaPlayer=MediaPlayer.create(FavActivity.this,R.raw.pikachuuu);
                break;
            case 2:
                //이브이
                pokemon.setImageResource(R.drawable.eevee);
                mediaPlayer=MediaPlayer.create(FavActivity.this,R.raw.eevee);
                break;
            case 3:
                //고라파덕
                pokemon.setImageResource(R.drawable.psyduck);
                mediaPlayer=MediaPlayer.create(FavActivity.this,R.raw.gorapaduck);
                break;
            case 4:
                //잠만보
                pokemon.setImageResource(R.drawable.snorlax);
                mediaPlayer=MediaPlayer.create(FavActivity.this,R.raw.zammanbo);
                break;
            case 5:
                //이상해씨
                pokemon.setImageResource(R.drawable.bullbasaur);
                mediaPlayer=MediaPlayer.create(FavActivity.this,R.raw.esanghaesee);
                break;
            case 6:
                //파이리
                pokemon.setImageResource(R.drawable.charmander);
                mediaPlayer=MediaPlayer.create(FavActivity.this,R.raw.pairi);
                break;
            case 7:
                //꼬부기
                pokemon.setImageResource(R.drawable.squirtle);
                mediaPlayer=MediaPlayer.create(FavActivity.this,R.raw.kkobugi);
                break;
            default:
                Log.d("난수","이상함");
        }
        //포켓몬 버튼 터치리스너
        pokemon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("포켓몬","실행");
                //버튼 눌릴 시
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    //울음소리 실행
                    mediaPlayer.setVolume(0.8f, 0.8f);
                    mediaPlayer.setLooping(false);
                    mediaPlayer.start();
                    //mediaPlayer.stop();

                    //포켓몬 투명 애니메이션 실행
                    pokemon.startAnimation(alpha);

                    Log.d("팝업메뉴","실행");
                    PopupMenu popup= new PopupMenu(FavActivity.this, view);
                    popup.getMenuInflater().inflate(R.menu.option_menu,popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //팝업메뉴에서 눌린 버튼에 따라 선택
                            switch (item.getItemId()){
                                case R.id.searchStation:
                                    Log.d("검색하기","실행");
                                    intent=new Intent(FavActivity.this, SearchStationActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                    Log.d("검색하기","완료");
                                    break;
                                case R.id.logout:
                                    Log.d("로그아웃","실행");
                                    logOut();
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
                Log.d("포켓몬","완료");
                return true;
            }
        });
        favoriteArray = new ArrayList<>();
        codeArray = new ArrayList<>();
        btnTagNum = 0;

        //즐겨찾기 리스트 불러오기
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
                    final JSONObject jsonObject = new JSONObject(sb.toString());
                    String result = jsonObject.getString("RESULT");
                    Log.d("로그인결과", result);
                    //서버로부터 수신된 데이터 학인
                    switch (result) {
                        //자료가 없을 시
                        case "NULL":
                            Log.d("Favorite", "불일치");
                            break;
                        //자료가 있을 시
                        case "OK":
                            JSONArray jarray = jsonObject.getJSONArray("item");
                            StringBuilder sb2 = new StringBuilder();
                            for (int i = 0; i < jarray.length(); i++) {
                                item = jarray.getJSONObject(i);

                                final LinearLayout linearLayout = new LinearLayout(this);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);
                                linearLayout.setOrientation(LinearLayout.VERTICAL);

                                //호선 별 색상
                                switch (item.getString("LINE")){
                                    case "1":
                                        linearLayout.setBackgroundColor(Color.rgb(0,0,102));
                                        break;
                                    case "2":
                                        linearLayout.setBackgroundColor(Color.rgb(51,153,51));
                                        break;
                                    case "3":
                                        linearLayout.setBackgroundColor(Color.rgb(255,102,0));
                                        break;
                                    case "4":
                                        linearLayout.setBackgroundColor(Color.rgb(0, 102, 255));
                                        break;
                                    case "5":
                                        linearLayout.setBackgroundColor(Color.rgb(153, 0, 204));
                                        break;
                                    case "6":
                                        linearLayout.setBackgroundColor(Color.rgb(204, 102, 0));
                                        break;
                                    case "7":
                                        linearLayout.setBackgroundColor(Color.rgb(102, 102, 51));
                                        break;
                                    case "8":
                                        linearLayout.setBackgroundColor(Color.rgb(255, 0, 102));
                                        break;
                                    case "9":
                                        linearLayout.setBackgroundColor(Color.rgb(204, 153, 0));
                                        break;
                                    default:
                                        linearLayout.setBackgroundColor(Color.rgb(102, 102, 102));
                                        break;
                                }

                                final TextView textView1 = new TextView(this);
                                textView1.setText("FAVORITENAME : "+item.getString("FAVORITENAME"));
                                textView1.setTextColor(Color.WHITE);
                                favoriteArray.add(item.getString("FAVORITENAME"));

                                TextView textView2 = new TextView(this);
                                textView2.setText("STATIONCODE : "+item.getString("STATIONCODE"));
                                textView2.setTextColor(Color.WHITE);
                                codeArray.add(item.getString("STATIONCODE"));

                                TextView textView3 = new TextView(this);
                                textView3.setText("LINE : "+item.getString("LINE"));
                                textView3.setTextColor(Color.WHITE);

                                TextView textView4 = new TextView(this);
                                textView4.setText("FCODE : "+item.getString("FCODE"));
                                textView4.setTextColor(Color.WHITE);

                                //버튼 생성 및 삭제
                                Button button = new Button(this);
                                button.setTag(btnTagNum);
                                //클릭 시 메소드 작동
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        int keyNum = (int) view.getTag();

                                        URL url = null;
                                        HttpURLConnection con = null;
                                        StringBuilder sb = new StringBuilder();

                                        //삭제 정보 JSON파일 만들기
                                        JSONObject btnJsonObjec = new JSONObject();
                                        try{
                                            btnJsonObjec.put("STATIONNAME", favoriteArray.get(keyNum));
                                            btnJsonObjec.put("STATIONCODE", codeArray.get(keyNum));
                                            btnJsonObjec.put("ID", id);

                                        }catch (JSONException e){
                                            Toast.makeText(getApplicationContext(), e.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        try {
                                            Log.d("ip주소 ", "203.233.196.139");
                                            //http://10.0.2.2:8888
                                            url = new URL("http://203.233.196.139:8888/densha/androidDeleteFavorite");
                                        } catch (MalformedURLException e) {
                                            Toast.makeText(getApplicationContext(), "잘못된 주소입니다!", Toast.LENGTH_SHORT).show();
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
                                                out.write(btnJsonObjec.toString().getBytes("UTF-8"));
                                                out.flush();
                                                out.close();
                                            }
                                            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

                                                InputStreamReader in = new InputStreamReader(con.getInputStream());
                                                int ch;
                                                while ((ch = in.read()) != -1) {
                                                    sb.append((char) ch);
                                                }
                                                in.close();
                                                
                                            }
                                        }catch (Exception e){
                                            Toast.makeText(getApplicationContext(), e.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                                button.setText("삭제");
                                btnTagNum++;

                                linearLayout.addView(textView1);
                                linearLayout.addView(textView2);
                                linearLayout.addView(textView3);
                                linearLayout.addView(textView4);
                                linearLayout.addView(button);

                                favMain.addView(linearLayout);

                            }
                            Log.d("Favorite", "완료");
                            break;
                        default:
                            Log.d("Favorite", "실패");
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //로그아웃
    public void logOut(){
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

    //Preferences 로그인 데이터 클리어
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
