package com.densha.kimi.kiminodensha;


import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        id = "loginId:nooti";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


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
                    out.write("loginId:nooti".getBytes("UTF-8"));
                    out.flush();
                    out.close();

                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

                        InputStreamReader in = new InputStreamReader(con.getInputStream());
                        int ch;
                        while ((ch = in.read()) != -1) {
                            sb.append((char) ch);
                        }
                        in.close();
                        Log.d("ip주소 ", "203.233.196.139");
                        JSONObject jsonObject = new JSONObject(sb.toString());
                        String result = jsonObject.getString("result");
                        Log.d("로그인결과", result);
                        //서버로부터 수신된 데이터 학인
                        //1. 전송실패
                        switch (result) {
                            case "null":
                                Log.d("Favorite", "불일치");
                                break;
                            case "ok":

                                //로그인된 아이디, 비밀번호를 SharedPreferences에 저장
                                SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
                                txt = (TextView) findViewById(R.id.textBox);
                                txt.setText(result);
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
