package com.densha.kimi.kiminodensha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by haleylovespurple on 7/19/17.
 */
public class LoginActivity extends AppCompatActivity {

    EditText idInput, passwordInput;    //로그인, 비밀번호
    CheckBox autoLogin;     //자동 로그인 체크박스
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String loginInfo = "";
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        autoLogin = (CheckBox) findViewById(R.id.checkBox);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void btnClick(View view){

        switch (view.getId()){
            case R.id.loginButton:
                if ((!idInput.getText().toString().equals("")) && (!idInput.getText().toString().equals(""))) {
                    Log.d("아이디",idInput.getText().toString());
                    Log.d("비밀번호",passwordInput.getText().toString());
                    try{
                        json=new JSONObject("{'id':"+idInput.getText().toString()+",'password':"+passwordInput.getText().toString()+"}");
                    }catch (JSONException e){
                        Log.d("json","error");
                    }
                    loginInfo = json.toString();
                    Log.d("login", loginInfo);
                }

                URL url = null;
                HttpURLConnection con = null;
                StringBuilder sb = new StringBuilder();

                try{
                    url = new URL("http://10.10.1.143:8888/densha/loginandroid");
                }catch(MalformedURLException e){
                    Toast.makeText(this, "잘못된 주소입니다!", Toast.LENGTH_SHORT).show();
                }
                try{
                    con = (HttpURLConnection)url.openConnection();

                    if(con != null){
                        con.setConnectTimeout(10000);
                        con.setUseCaches(false);
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Content-Type", "application/json");
                        con.setDoOutput(true);
                        con.setDoInput(true);
                        OutputStream out = con.getOutputStream();
                        out.write(loginInfo.getBytes("utf-8"));
                        out.flush();
                        out.close();

                        if(con.getResponseCode() == HttpURLConnection.HTTP_OK){
                            InputStreamReader in = new InputStreamReader(con.getInputStream());
                            int ch;
                            while ((ch = in.read()) != -1){
                                sb.append((char)ch);
                            }
                            in.close();
                        }

                        //로그인된 아이디, 비밀번호를 SharedPreferences에 저장
                        SharedPreferences preferences=getSharedPreferences("login_prefs",MODE_PRIVATE);
                    }
                }
                catch (Exception e){
                    Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.joinButton:
                Intent intent = new Intent(this, JoinMemberActivity.class);
                startActivity(intent);
        }
    }

    //자동로그인 메소드
    public void autoLogin(){

    }
}
