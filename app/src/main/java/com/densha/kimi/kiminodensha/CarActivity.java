package com.densha.kimi.kiminodensha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    Intent intent;
    String subwayNum="";
    JSONObject json=null;
    String subwayNumInfo="";
    JSONArray jsonArray=null;
    ArrayList<String> subwayInfoArray=null;
    JSONObject subwayInfo=null;
    TextView[] carCongestionArray=new TextView[10];

    //메소드
    //onCreate
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        // 화면을 가로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        //차량번호 로드
        intent=getIntent();
        subwayNum=Integer.toString(intent.getIntExtra("num",0));
        Log.d("차량번호",subwayNum);

        //서버 전송을 위한 설정
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        carCongestionArray[0]=(TextView)findViewById(R.id.tText1Con);
        carCongestionArray[1]=(TextView)findViewById(R.id.tText2Con);
        carCongestionArray[2]=(TextView)findViewById(R.id.tText3Con);
        carCongestionArray[3]=(TextView)findViewById(R.id.tText4Con);
        carCongestionArray[4]=(TextView)findViewById(R.id.tText5Con);
        carCongestionArray[5]=(TextView)findViewById(R.id.tText6Con);
        carCongestionArray[6]=(TextView)findViewById(R.id.tText7Con);
        carCongestionArray[7]=(TextView)findViewById(R.id.tText8Con);
        carCongestionArray[8]=(TextView)findViewById(R.id.tText9Con);
        carCongestionArray[9]=(TextView)findViewById(R.id.tText10Con);

        carCongestionLoad();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_car);

        // 화면을 가로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //차량번호 로드
        intent=getIntent();
        subwayNum=Integer.toString(intent.getIntExtra("num",0));
        Log.d("차량번호",subwayNum);

        //서버 전송을 위한 설정
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        carCongestionArray[0]=(TextView)findViewById(R.id.tText1Con);
        carCongestionArray[1]=(TextView)findViewById(R.id.tText2Con);
        carCongestionArray[2]=(TextView)findViewById(R.id.tText3Con);
        carCongestionArray[3]=(TextView)findViewById(R.id.tText4Con);
        carCongestionArray[4]=(TextView)findViewById(R.id.tText5Con);
        carCongestionArray[5]=(TextView)findViewById(R.id.tText6Con);
        carCongestionArray[6]=(TextView)findViewById(R.id.tText7Con);
        carCongestionArray[7]=(TextView)findViewById(R.id.tText8Con);
        carCongestionArray[8]=(TextView)findViewById(R.id.tText9Con);
        carCongestionArray[9]=(TextView)findViewById(R.id.tText10Con);

        carCongestionLoad();
    }

    //열개의 량의 데이터를 가져옴
    //데이터는 사람수, 혼잡도율
    //혼잡율에 따라 칸의 색깔 다름  ---> 빨강
    public void carCongestionLoad(){
        Log.d("량 데이터 로드", "실행");

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
            url = new URL("http://203.233.196.139:8888/densha/apiservice/json?subwaynum="+subwayNum);
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
                    Log.d("수신데이터들",result);
                    jsonArray=new JSONArray(result);
                    subwayInfo=new JSONObject();

                    for(int i=0;i<jsonArray.length();i++){
                        Log.d("수신데이터"+(i+1),jsonArray.get(i).toString());
                        subwayInfo=(JSONObject) jsonArray.get(i);
                        carCongestionArray[i].setText(subwayInfo.getString("humanNum")+"명");
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("오류",e.toString());
        }
    }
}
