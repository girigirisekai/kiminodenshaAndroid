package com.densha.kimi.kiminodensha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Car
 */
public class CarActivity extends AppCompatActivity {

    //변수
    String subwayNum="2002";
    JSONObject json=null;
    String subwayNumInfo="";
    String[] subwayInfo=new String[10];

    //메소드
    //onCreate
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        Intent intent=getIntent();
        intent.getStringExtra("num");

        //서버 전송을 위한 설정
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //열개의 량의 데이터를 가져옴
        //데이터는 사람수, 혼잡도율
        //혼잡율에 따라 칸의 색깔 다름  ---> 빨강
        Log.d("칸인원수로드", "실행");

        URL url = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();

        //JSON타입으로 값을 담음
        try {
            json = new JSONObject("{'subwayNum':"+subwayNum+"}");
        } catch (JSONException e) {
            Log.d("json", "error");
        }
        //String형태로 변환
        subwayNumInfo = json.toString();
        Log.d("지하철번호데이터", subwayNumInfo);
        //url설정
        try {
            Log.d("ip주소 ", "203.233.196.139");
            url = new URL("http://203.233.196.139:8888/densha/apiservice/json?subwaynum=2002");
        } catch (MalformedURLException e) {
            Toast.makeText(this, "잘못된 주소입니다!", Toast.LENGTH_SHORT).show();
        }
        try {
            //서버와 연결
            Log.d("서버연결", "실행");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setUseCaches(false);
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            Log.d("서버연결", "완료");
            if (con != null) {
                //수신
                Log.d("수신", "실행");
                if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader in = new InputStreamReader(con.getInputStream());
                    int ch;
                    while ((ch = in.read()) != -1) {
                        sb.append((char) ch);
                    }
                    in.close();
                    Log.d("수신", "완료");
                    //수신된 데이터 학인
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    String result = jsonObject.getString("item");
                    Log.d("수신데이터",result);
                   /* result=result.replace("[","");
                    result=result.replace("]","");
                    Log.d("수신데이터2",result);*/
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("오류",e.toString());
        }
    }
}
