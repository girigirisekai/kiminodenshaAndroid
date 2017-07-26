package com.densha.kimi.kiminodensha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 이민호 on 2017-07-19.
 */

public class JoinMemberActivity extends AppCompatActivity {
        JSONObject json;
        EditText editId;
        EditText editPassword;
        EditText editAnswer;
        EditText editCheckCode;
        Spinner spinner1;
        ArrayAdapter adapter;
        String logininfo;
        String checkNum;
        Button checkBtn;
        Button checkBtnOk;
        Button joinButton;
        String androidresult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

            editId= (EditText) findViewById(R.id.editId);
            editPassword= (EditText) findViewById(R.id.editPassword);
            editAnswer= (EditText) findViewById(R.id.editAnswer);
            editCheckCode= (EditText) findViewById(R.id.editCheckCode);
            spinner1 =(Spinner)findViewById(R.id.answerSpinner);
            checkBtn = (Button)findViewById(R.id.checkCode);
            checkBtnOk = (Button)findViewById(R.id.checkCodeOk);
            joinButton = (Button)findViewById(R.id.joinButton);

            adapter = ArrayAdapter.createFromResource(
                    this, R.array.text_pw, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapter);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    public void JoinButton(View v){
        switch (v.getId()){
            case R.id.joinButton:
                Log.d("editId",editId.getText().toString());
                Log.d("editPassword",editPassword.getText().toString());
                Log.d("editAnswer",editAnswer.getText().toString());
                Log.d("editCheckCode",editCheckCode.getText().toString());
                Log.d("spinner1",spinner1.getSelectedItem().toString());

                String question = spinner1.getSelectedItem().toString().replace(" ", "_");
                Log.d("spinner1",question);

               if(!editId.getText().toString().equals("")&&!editPassword.getText().toString().equals("")&&!editAnswer.getText().toString().equals("")
                    &&!editCheckCode.getText().toString().equals("")&&!spinner1.getSelectedItem().equals("")){
                  try {
                      json = new JSONObject("{'id':"+editId.getText().toString()+",'password':"+editPassword.getText().toString()
                              + ",'question':"+question+ ",'answer':"+editAnswer.getText().toString()
                              +",'type':"+editCheckCode.getText().toString()+"}");



                  }catch (JSONException e){
                      Log.d("json","error");
                  }
                   logininfo = json.toString();
                   Log.d("login", logininfo);

                }

                URL url = null;
                HttpURLConnection con = null;
                StringBuffer sb = new StringBuffer();

                try{
                    url = new URL("http://203.233.196.147:8888/densha/joinandroid");
                }catch (MalformedURLException e){
                    Toast.makeText(this, "잘못된 URL입니다.", Toast.LENGTH_SHORT).show();
                }try {
                con = (HttpURLConnection) url.openConnection();
                if (con != null) {
                    con.setConnectTimeout(10000);

                    //연결제한시간. 0은 무한대기.
                    con.setUseCaches(false);
                    //캐쉬 사용여부
                    con.setRequestMethod("POST");
                    //요청방식 선택 (GET, POST)
                    con.setRequestProperty("Content-Type","application/json");
                    con.setDoOutput(true);
                    //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                    con.setDoInput(true);  //InputStream으로 서버로 부터 응답을 받겠다는 옵션.
                    OutputStream os = con.getOutputStream();
                    //Request Body에 Data를 담기위해 OutputStream 객체를 생성.
                    os.write(logininfo.getBytes("utf-8"));
                    os.flush();
                    os.close();
                    Log.d("trasforCheck : ", "ok");
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStreamReader in = new InputStreamReader(con.getInputStream());
                        int ch;
                        while ((ch = in.read()) != -1) {
                            sb.append((char) ch);
                        }
                        in.close();
                        JSONObject jsonObject=new JSONObject(sb.toString());
                        androidresult=jsonObject.getString("androidresult");
                        Intent intent = new Intent(JoinMemberActivity.this,LoginActivity.class);
                        Log.d(androidresult,androidresult);
                        Log.d("조인확인","조인확인");
                        Toast.makeText(this, "회원가입 성공 ♥♥♥♥♥♥" , Toast.LENGTH_SHORT).show();
                        startActivity(intent);


                    }
                }
            }catch (Exception e) {
                Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            }

                 break;

            case R.id.checkCode:

                if(!editId.getText().toString().equals("")&&!editPassword.getText().toString().equals("")&&!editAnswer.getText().toString().equals("")
                       &&!spinner1.getSelectedItem().equals("")){
                    question = spinner1.getSelectedItem().toString().replace(" ", "_");
                    try {
                        json = new JSONObject("{'id':"+editId.getText().toString()+",'password':"+editPassword.getText().toString()
                                + ",'question':"+question+ ",'answer':"+editAnswer.getText().toString()
                               +"}");

                    }catch (JSONException e){
                        Log.d("json","error");
                    }
                    logininfo = json.toString();
                    Log.d("login", logininfo);

                }

                url = null;
                con = null;
                sb = new StringBuffer();

                try{
                    url = new URL("http://203.233.196.147:8888/densha/checkcodeandroid");
                }catch (MalformedURLException e){
                    Toast.makeText(this, "잘못된 URL입니다.", Toast.LENGTH_SHORT).show();
                }try {
                con = (HttpURLConnection) url.openConnection();
                if (con != null) {
                    con.setConnectTimeout(10000);

                    //연결제한시간. 0은 무한대기.
                    con.setUseCaches(false);
                    //캐쉬 사용여부
                    con.setRequestMethod("POST");
                    //요청방식 선택 (GET, POST)
                    con.setRequestProperty("Content-Type","application/json");
                    con.setDoOutput(true);
                    //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                    con.setDoInput(true);  //InputStream으로 서버로 부터 응답을 받겠다는 옵션.
                    OutputStream os = con.getOutputStream();
                    //Request Body에 Data를 담기위해 OutputStream 객체를 생성.
                    os.write(logininfo.getBytes("utf-8"));
                    os.flush();
                    os.close();
                   if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStreamReader in = new InputStreamReader(con.getInputStream());
                        int ch;
                        while ((ch = in.read()) != -1) {
                            sb.append((char) ch);
                        }
                        in.close();

                        //서버로부터 수신된 데이터 학인
                        JSONObject jsonObject=new JSONObject(sb.toString());
                        checkNum=jsonObject.getString("code");
                        Log.d("인증번호 결과", checkNum);
                       checkBtn.setVisibility(View.GONE);
                       checkBtnOk.setVisibility(View.VISIBLE);
                    }
                }
            }catch (Exception e) {
                Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            }
                break;
            case R.id.checkCodeOk:
                Log.d(checkNum,checkNum);
                if(editCheckCode.getText().toString().equals(checkNum)){
                    checkBtnOk.setVisibility(View.GONE);
                    joinButton.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

}
