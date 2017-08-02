package com.densha.kimi.kiminodensha;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * ElderlySeats
 */
public class ElderlySeatsActivity extends AppCompatActivity {

    //변수
    Intent intent=null;
    JSONObject json=null;
    String subwayNum="";
    String subwayNumInfo="";
    JSONObject subwayInfo=null;
    JSONArray jsonArray=null;
    String carNum="";
    static int elderlySeatsLength=12;
    ImageView[] imageViewArray=new ImageView[12];
    TextView txtViewSubwayNum;
    TextView txtViewCarNum;
    String elderlySeats="";
    String[] elderlySeatsArray=new String[10];
    SwipeRefreshLayout swipeRefreshLayout;

    //메소드
    //onCreate
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);

        // 화면을 가로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //pull to Refresh
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.elderlySeatsSwiftLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                Log.d("리프레쉬","실행");
                elderlySeatsLoad();
                elderlySeatsUpdate();
                swipeRefreshLayout.setRefreshing(false);
                Log.d("리프레쉬","완료");
            }
        });

        //서버 전송을 위한 설정
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //칸번호와 노약자석 정보 로드
        intent=getIntent();
        subwayNum=intent.getStringExtra("subwayNum");
        carNum=intent.getStringExtra("carNum");
        elderlySeatsLoad();
        Log.d("지하철번호",subwayNum);
        Log.d("칸번호",carNum);

        //텍스트뷰 로드
        Log.d("텍스트뷰로드","실행");
        txtViewSubwayNum=(TextView)findViewById(R.id.subwayNumElderly);
        txtViewCarNum=(TextView)findViewById(R.id.carNumElderly);
        txtViewSubwayNum.setText("열차번호: "+subwayNum);
        txtViewCarNum.setText("칸번호: "+carNum);
        txtViewSubwayNum.setTextColor(Color.WHITE);
        txtViewCarNum.setTextColor(Color.WHITE);
        Log.d("열차번호텍스트",txtViewSubwayNum.getText().toString());
        Log.d("칸번호텍스트",txtViewCarNum.getText().toString());
        Log.d("텍스트뷰로드","완료");

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

        elderlySeatsUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_seat);

        // 화면을 가로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //pull to Refresh
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.elderlySeatsSwiftLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                Log.d("리프레쉬","실행");
                elderlySeatsLoad();
                elderlySeatsUpdate();
                swipeRefreshLayout.setRefreshing(false);
                Log.d("리프레쉬","완료");
            }
        });

        //칸번호와 노약자석 정보 로드
        intent=getIntent();
        subwayNum=intent.getStringExtra("subwayNum");
        carNum=intent.getStringExtra("carNum");
        elderlySeatsLoad();
        Log.d("지하철번호",subwayNum);
        Log.d("칸번호",carNum);

        //텍스트뷰 로드
        Log.d("텍스트뷰로드","실행");
        txtViewSubwayNum=(TextView)findViewById(R.id.subwayNumElderly);
        txtViewCarNum=(TextView)findViewById(R.id.carNumElderly);
        txtViewSubwayNum.setText("열차번호: "+subwayNum);
        txtViewCarNum.setText("칸번호: "+carNum);
        txtViewSubwayNum.setTextColor(Color.WHITE);
        txtViewCarNum.setTextColor(Color.WHITE);
        Log.d("열차번호텍스트",txtViewSubwayNum.getText().toString());
        Log.d("칸번호텍스트",txtViewCarNum.getText().toString());
        Log.d("텍스트뷰로드","완료");

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

        elderlySeatsUpdate();
    }

    public void elderlySeatsUpdate(){
        Log.d("노약자석업데이트","실행");
        //이미지 색 초기화
        for(int i=0;i<imageViewArray.length;i++){
            imageViewArray[i].setColorFilter(Color.GRAY);
        }

        //사람 유무로 이미지 색 변경
        for(int i=0;i<elderlySeatsLength;i++){
            if(elderlySeats.charAt(i)=='1'){
                imageViewArray[i].setColorFilter(null);
            }if(elderlySeats.charAt(i)=='9'){
                //imageViewArray[i].setColorFilter(null);
                imageViewArray[i].setVisibility(View.INVISIBLE);
            }
        }
        elderlySeats="";
        Log.d("노약자석업데이트","완료");
    }

    public void elderlySeatsLoad(){
        Log.d("량 데이터 로드", "실행");

        URL url = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();

        //JSON타입으로 값을 담음
        try {
            json = new JSONObject("{'subwayNum':"+subwayInfo+"}");
        } catch (JSONException e) {
            Log.d("json", "error");
        }
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
                    Log.d("수신데이터" + (carNum), jsonArray.get(Integer.parseInt(carNum)-1).toString());
                    subwayInfo = (JSONObject) jsonArray.get(Integer.parseInt(carNum)-1);
                    for (int j = 1; j <= 12; j++) {
                        elderlySeats += subwayInfo.getString("elderlySeat" + j);
                    }
                    Log.d("노약자석",elderlySeats);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("오류",e.toString());
        }
    }
}
