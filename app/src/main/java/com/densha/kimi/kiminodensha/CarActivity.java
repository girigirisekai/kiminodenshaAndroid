package com.densha.kimi.kiminodensha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
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
    TextView[] carHumanNumArray=new TextView[10];
    String[] carCongestionArray=new String[10];
    ImageView[] carImageArray=new ImageView[10];
    String elderlySeats="";
    String[] elderlySeatsArray=new String[10];

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

        //차량 혼잡도 Textview 로드
        carHumanNumArray[0]=(TextView)findViewById(R.id.tText1Con);
        carHumanNumArray[1]=(TextView)findViewById(R.id.tText2Con);
        carHumanNumArray[2]=(TextView)findViewById(R.id.tText3Con);
        carHumanNumArray[3]=(TextView)findViewById(R.id.tText4Con);
        carHumanNumArray[4]=(TextView)findViewById(R.id.tText5Con);
        carHumanNumArray[5]=(TextView)findViewById(R.id.tText6Con);
        carHumanNumArray[6]=(TextView)findViewById(R.id.tText7Con);
        carHumanNumArray[7]=(TextView)findViewById(R.id.tText8Con);
        carHumanNumArray[8]=(TextView)findViewById(R.id.tText9Con);
        carHumanNumArray[9]=(TextView)findViewById(R.id.tText10Con);

        //차량 Imageview 로드
        carImageArray[0]=(ImageView)findViewById(R.id.car1);
        carImageArray[1]=(ImageView)findViewById(R.id.car2);
        carImageArray[2]=(ImageView)findViewById(R.id.car3);
        carImageArray[3]=(ImageView)findViewById(R.id.car4);
        carImageArray[4]=(ImageView)findViewById(R.id.car5);
        carImageArray[5]=(ImageView)findViewById(R.id.car6);
        carImageArray[6]=(ImageView)findViewById(R.id.car7);
        carImageArray[7]=(ImageView)findViewById(R.id.car8);
        carImageArray[8]=(ImageView)findViewById(R.id.car9);
        carImageArray[9]=(ImageView)findViewById(R.id.car10);

        //차량 혼잡도 로드
        carCongestionLoad();

    }

    //
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

        //차량 혼잡도 Textview 로드
        carHumanNumArray[0]=(TextView)findViewById(R.id.tText1Con);
        carHumanNumArray[1]=(TextView)findViewById(R.id.tText2Con);
        carHumanNumArray[2]=(TextView)findViewById(R.id.tText3Con);
        carHumanNumArray[3]=(TextView)findViewById(R.id.tText4Con);
        carHumanNumArray[4]=(TextView)findViewById(R.id.tText5Con);
        carHumanNumArray[5]=(TextView)findViewById(R.id.tText6Con);
        carHumanNumArray[6]=(TextView)findViewById(R.id.tText7Con);
        carHumanNumArray[7]=(TextView)findViewById(R.id.tText8Con);
        carHumanNumArray[8]=(TextView)findViewById(R.id.tText9Con);
        carHumanNumArray[9]=(TextView)findViewById(R.id.tText10Con);

        //차량 Imageview 로드
        carImageArray[0]=(ImageView)findViewById(R.id.car1);
        carImageArray[1]=(ImageView)findViewById(R.id.car2);
        carImageArray[2]=(ImageView)findViewById(R.id.car3);
        carImageArray[3]=(ImageView)findViewById(R.id.car4);
        carImageArray[4]=(ImageView)findViewById(R.id.car5);
        carImageArray[5]=(ImageView)findViewById(R.id.car6);
        carImageArray[6]=(ImageView)findViewById(R.id.car7);
        carImageArray[7]=(ImageView)findViewById(R.id.car8);
        carImageArray[8]=(ImageView)findViewById(R.id.car9);
        carImageArray[9]=(ImageView)findViewById(R.id.car10);

        //차량 혼잡도 로드
        carCongestionLoad();
    }

    //열개의 량의 데이터를 가져옴
    //데이터는 사람수, 혼잡도율 사용
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

                    //인원수와 혼잡도, 노약자석
                    for(int i=0;i<jsonArray.length();i++) {
                        Log.d("수신데이터" + (i + 1), jsonArray.get(i).toString());
                        subwayInfo = (JSONObject) jsonArray.get(i);
                        carHumanNumArray[i].setText(subwayInfo.getString("humanNum") + "명");
                        carCongestionArray[i]=subwayInfo.getString("humanPercent");
                        for (int j = 1; j <= 12; j++) {
                            elderlySeats += subwayInfo.getString("elderlySeat" +j);
                        }
                        elderlySeatsArray[i]=elderlySeats;
                        elderlySeats="";
                    }

                    //칸 색상
                    //0~10% = 그대로, 11~30% = 연분홍, 31~70% = 진한 분홍, 71~100% = 빨강
                    Log.d("칸 색상 변화","실행");
                    for(int i=0;i<jsonArray.length();i++){
                        int congestion=Integer.parseInt(carCongestionArray[i]);
                        if(congestion>=0&&congestion<=10) {
                            Log.d("칸 색상","변화없음");
                        }else if(congestion>=11&&congestion<=30){
                            carImageArray[i].setColorFilter(0XFFDBC1);
                            Log.d("칸 색상","연분홍");
                        }else if(congestion>=31&&congestion<=70){
                            carImageArray[i].setColorFilter(0XFF88A7);
                            Log.d("칸 색상","진한 분홍");
                        }else if(congestion>=71&&congestion<=100){
                            carImageArray[i].setColorFilter(0XFF5675);
                            Log.d("칸 색상","빨간색");
                        }
                    }
                    Log.d("칸 색상 변화","완료");
                    Log.d("칸 클릭 설정","실행");
                    //칸 클릭 설정
                    for(int i=0;i<carImageArray.length;i++) {
                        final int j=i;
                        carImageArray[j].setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                Log.d("노약자석", "실행");
                                //버튼 눌릴 시
                                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                    String elderlySeatstoElderly=elderlySeatsArray[j];
                                    String carNum=Integer.toString(j);
                                    intent = new Intent(CarActivity.this, ElderlySeatsActivity.class);
                                    Log.d("노약자석 데이터",elderlySeatstoElderly);
                                    Log.d("차량번호",carNum);
                                    intent.putExtra("elderlySeats",elderlySeatstoElderly);
                                    intent.putExtra("carNum",carNum);
                                    startActivity(intent);
                                    Log.d("노약자석","완료");
                                }
                                return true;
                            }
                        });
                    }
                    Log.d("칸 클릭 설정","완료");
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("오류",e.toString());
        }
    }
}
