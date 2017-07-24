package com.densha.kimi.kiminodensha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

        //서버 전송을 위한 설정
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void mainClick(View view){
        //DB에 접근해서 계정 데이터 유무 확인
        Log.d("DB데이터확인","실행");
        SharedPreferences preferences=getSharedPreferences("login_prefs",MODE_PRIVATE);
        String loginId=preferences.getString("loginId","");
        String loginPassword=preferences.getString("loginPassword","");
        Log.d("DB아이디",": "+loginId);
        Log.d("DB비밀번호",": "+loginPassword);
        Log.d("DB데이터확인","완료");
        //있으면 로그인화면에 가져와서 아이디 비밀번호 넣고 자동 로그인버튼 실행
        if(!loginId.equals("")&&!loginPassword.equals("")){
            //자동로그인해주는 메소드 실행
            Log.d("로그인아이디",loginId);
            //clearPreferences();   //SharedPreferences에 있는 아이디, 비밀번호 값 지울 때 사용
            autoLogin(loginId,loginPassword);
            finish();
        }
        // 없으면 빈 로그인화면으로 전환
        else{
            Log.d("일반로그인","실행");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
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

    public void autoLogin(String id, String password){
        Log.d("자동로그인","실행");
        URL url = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();
        String autoLoginInfo="";
        JSONObject autoJson=null;

        if ((!id.equals("")) && (!password.equals(""))) {
            Log.d("아이디",id);
            Log.d("비밀번호",password);
            //JSON타입으로 값을 담음
            try{
                autoJson=new JSONObject("{'id':"+id+",'password':"+password+"}");
            }catch (JSONException e){
                Log.d("json","error");
            }
            //String형태로 변환
            autoLoginInfo = autoJson.toString();
            Log.d("login", autoLoginInfo);
        }
        //url설정
        try{
            Log.d("ip주소 ","203.233.196.139");
            url = new URL("http://203.233.196.139:8888/densha/loginandroid");
        }catch(MalformedURLException e){
            Toast.makeText(this, "잘못된 주소입니다!", Toast.LENGTH_SHORT).show();
        }
        try{
            Log.d("서버연결","실행");
            con = (HttpURLConnection)url.openConnection();
            if(con != null){
                con.setConnectTimeout(10000);
                con.setUseCaches(false);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);
                con.setDoInput(true);
                Log.d("서버연결","완료");
                //logInfo송신
                Log.d("서버송신","실행");
                OutputStream out2 = con.getOutputStream();
                out2.write(autoLoginInfo.getBytes("utf-8"));
                out2.flush();
                out2.close();
                Log.d("서버송신","완료");
                Log.d("서버수신","실행");
                if(con.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStreamReader in = new InputStreamReader(con.getInputStream());
                    int ch;
                    while ((ch = in.read()) != -1){
                        sb.append((char)ch);
                    }
                    in.close();
                    Log.d("서버수신","완료");
                    //수신된 데이터 학인
                    JSONObject jsonObject=new JSONObject(sb.toString());
                    String loginResult=jsonObject.getString("result");
                    Log.d("자동로그인 결과", loginResult);
                    switch (loginResult) {
                        //1. 전송된 데이터 없음
                        case "notdata":
                            Log.d("자동로그인", "전송실패");
                            Toast.makeText(getApplicationContext(), "Login Fail!", Toast.LENGTH_LONG).show();
                            break;
                        //일치하는 아이디나 비밀번호 없음
                        case "fail":
                            Log.d("자동로그인", "불일치");
                            Toast.makeText(getApplicationContext(), "Login Fail!", Toast.LENGTH_LONG).show();
                            break;
                        //일치
                        case "success":
                            Log.d("자동로그인", "완료");
                            //즐겨찾기 엑티비티로 이동
                            Intent intent = new Intent(this, FavActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                            finish();
                            break;
                        default:
                            Log.d("자동로그인", "실패");
                            Toast.makeText(getApplicationContext(), "Login Fail!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        catch (Exception e){
            e.getStackTrace();
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
