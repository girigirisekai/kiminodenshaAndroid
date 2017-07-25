package com.densha.kimi.kiminodensha;

import android.app.LauncherActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Fuego on 2017-07-20.
 */

public class SearchStationActivity extends AppCompatActivity {

    String id;
    EditText edtxt;
    TextView txt;
    ListView lists;
    ArrayList <String> asd  = new ArrayList<>();
    ArrayList<String> lineNameArray = new ArrayList<>();
    ArrayAdapter adapter;
    String selectedItem;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // searchStationName_favorite xml 사용
        setContentView(R.layout.activity_searchstation);

        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        Log.d("아이디",id);
        //id = "sunnygtx@daum.net";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // search Btn link
        Button btn = (Button)findViewById(R.id.searchBtn);
        edtxt = (EditText) findViewById(R.id.searchEditBox);
        txt = (TextView) findViewById(R.id.SearchResult);
        lists = (ListView) findViewById(R.id.stationList);



        // Station 정보 버튼
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 역정보 Search 메소드 실행
                // searchEditBox link
                getFavoriteStationName();
                lineNameArray = asd;
                adapter.notifyDataSetChanged();


            }
        });

        lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = (String) adapter.getItem(i); // 아이템 나오는 부분
                Toast.makeText(SearchStationActivity.this, selectedItem, Toast.LENGTH_SHORT).show();
                saveSelectedItem();
            }
        });


    }

    // 역정보 Search 메소드 실행
    public void getFavoriteStationName(){

        URL url = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();

        //JSON형태로 보내기 준비
        String name = edtxt.getText().toString();

        JSONObject makeJson = new JSONObject();
        try{

            makeJson.put("id",id);
            makeJson.put("STATIONNAME",name);
        }catch(Exception c){

        }

        try{

            Log.d("ip주소 ","203.233.196.139");
            //http://10.0.2.2:8888
            url = new URL("http://203.233.196.139:8888/densha/androiStationNameSearch");
            // 나중에 ip 주소 바꿔주세요

        }catch(Exception e){
            Toast.makeText(this, "잘못된 주소입니다!", Toast.LENGTH_SHORT).show();
        }

        Log.d("id", id);
        try{
            con = (HttpURLConnection)url.openConnection();

            if(con != null){
                con.setConnectTimeout(10000);
                con.setUseCaches(false);
                con.setRequestMethod("POST");

                //JSON형태로 보내기 설정
                con.setRequestProperty("Content-type", "application/json");

                con.setDoOutput(true);
                con.setDoInput(true);

                OutputStream out = con.getOutputStream();

                out.write(makeJson.toString().getBytes("UTF-8"));
                out.flush();
                out.close();

                if(con.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStreamReader in = new InputStreamReader(con.getInputStream());
                    int ch;
                    while ((ch = in.read()) != -1){
                        sb.append((char)ch);
                    }
                    in.close();
                    String jsonResult=sb.toString();

                    Log.d("결과", jsonResult);

                    JSONObject resultJson = new JSONObject(jsonResult);
                    JSONArray resultArray = resultJson.getJSONArray("item");

                    String result = resultJson.getString("RESULT"); //RESULT 분석

                    //서버로부터 수신된 데이터 학인
                    //1. 전송실패
                    switch (result) {
                        case "False": // 실패
                            Log.d("Favorite", "False");
                            txt.setText("실패");
                            break;
                        case "True": //성공
                            SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);

                            int resultArraylength = resultArray.length();

                            String texts ="";
                            asd = new ArrayList<>();
                            for(int i = 0 ; i < resultArray.length();i++){
                                JSONObject bufferobj = new JSONObject(resultArray.get(i).toString());
                                String linei = bufferobj.getString("line");
                                String namei = bufferobj.getString("name");
                                asd.add(linei +","+namei);
                                texts+= linei +", "+namei;
                            }
                            txt.setText(asd.toString());
                            lineNameArray = asd;
                            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, lineNameArray);
                            lists.setAdapter(adapter);


                            break;
                    }
                }
            }
        }
        catch (Exception e){

            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void saveSelectedItem(){

        URL url = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();

        //JSON형태로 보내기 준비
        String [] getSelectedItem = selectedItem.split(",");
        Log.d("LINE",getSelectedItem[0]);
        Log.d("STATIONNAME",getSelectedItem[1]);
        // 아이디 라인 이름 넘기기
        JSONObject makeJson = new JSONObject();
        try{
            makeJson.put("ID",id);
            makeJson.put("LINE",getSelectedItem[0]);
            makeJson.put("STATIONNAME",getSelectedItem[1]);

        }catch(Exception c){

        }

        try{

            Log.d("ip주소 ","203.233.196.139");
            //http://10.0.2.2:8888
            url = new URL("http://203.233.196.139:8888/densha/androidNewFavorite");
            // 나중에 ip 주소 바꿔주세요

        }catch(Exception e){
            Toast.makeText(this, "잘못된 주소입니다!", Toast.LENGTH_SHORT).show();
        }

        Log.d("id", id);
        try{
            con = (HttpURLConnection)url.openConnection();

            if(con != null){
                con.setConnectTimeout(10000);
                con.setUseCaches(false);
                con.setRequestMethod("POST");
                //JSON형태로 보내기 설정
                con.setRequestProperty("Content-type", "application/json");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream out = con.getOutputStream();
                out.write(makeJson.toString().getBytes("UTF-8"));
                out.flush();
                out.close();

                if(con.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStreamReader in = new InputStreamReader(con.getInputStream());
                    int ch;
                    while ((ch = in.read()) != -1){
                        sb.append((char)ch);
                    }
                    in.close();
                    String jsonResult=sb.toString();
                    Log.d("결과", jsonResult);
                    JSONObject resultJson = new JSONObject(jsonResult);
                    String result = resultJson.getString("RESULT"); //RESULT 분석
                    //서버로부터 수신된 데이터 학인
                    //1. 전송실패
                    switch (result) {
                        case "False": // 실패
                            Log.d("Favorite", "False");
                            txt.setText("실패" + result);
                            break;
                        case "True": //성공
                            Toast.makeText(this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
                            txt.setText("성공" + result);
                            break;
                    }
                }
            }
        }
        catch (Exception e){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
}