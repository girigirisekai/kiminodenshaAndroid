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
 * Login
 */
public class LoginActivity extends AppCompatActivity {

    //변수
    EditText idInput, passwordInput;    //아이디, 비밀번호
    SharedPreferences pref;             //DB처럼 복잡한 자료가 아닌 간단한 설정 값을 파일 형태로 저장하는 객체, key, value형태로 저장
    SharedPreferences.Editor editor;    //SharedPreferences에 값을 넣고 빼기 위한 editor
    String loginInfo = "";              //서버로 보낼 로그인정보
    JSONObject json;                    //서버로 보낼 데이터를 먼저 json형태로 만들기 위한 객체

    //메소드
    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //아이디, 패스워드 가져오기
        idInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);

        //서버 전송을 위한 설정
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    //버튼 클릭 시 액션들
    public void btnClick(View view) {
        switch (view.getId()) {
            //로그인 버튼 클릭 시
            case R.id.loginButton:
                Log.d("로그인", "실행");

                URL url = null;
                HttpURLConnection con = null;
                StringBuilder sb = new StringBuilder();

                if ((!idInput.getText().toString().equals("")) && (!passwordInput.getText().toString().equals(""))) {
                    Log.d("아이디", idInput.getText().toString());
                    Log.d("비밀번호", passwordInput.getText().toString());
                    //JSON타입으로 값을 담음
                    try {
                        json = new JSONObject("{'id':" + idInput.getText().toString() + ",'password':" + passwordInput.getText().toString() + "}");
                    } catch (JSONException e) {
                        Log.d("json", "error");
                    }
                    //String형태로 변환
                    loginInfo = json.toString();
                    Log.d("login", loginInfo);
                }
                //url설정
                try {
                    Log.d("ip주소 ", "203.233.196.139");
                    url = new URL("http://203.233.196.139:8888/densha/loginandroid");
                } catch (MalformedURLException e) {
                    Toast.makeText(this, "잘못된 주소입니다!", Toast.LENGTH_SHORT).show();
                }
                try {
                    //서버와 연결
                    con = (HttpURLConnection) url.openConnection();
                    if (con != null) {
                        con.setConnectTimeout(10000);
                        con.setUseCaches(false);
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Content-Type", "application/json");
                        con.setDoOutput(true);
                        con.setDoInput(true);
                        //logInfo송신
                        OutputStream out = con.getOutputStream();
                        out.write(loginInfo.getBytes("utf-8"));
                        out.flush();
                        out.close();
                        //수신
                        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            InputStreamReader in = new InputStreamReader(con.getInputStream());
                            int ch;
                            while ((ch = in.read()) != -1) {
                                sb.append((char) ch);
                            }
                            in.close();

                            //수신된 데이터 학인
                            JSONObject jsonObject = new JSONObject(sb.toString());
                            String loginResult = jsonObject.getString("result");
                            Log.d("로그인 결과", loginResult);
                            switch (loginResult) {
                                //1. 전송된 데이터 없음
                                case "notdata":
                                    Log.d("로그인", "전송실패");
                                    Toast.makeText(getApplicationContext(), "Login Fail!", Toast.LENGTH_LONG).show();
                                    break;
                                //일치하는 아이디나 비밀번호 없음
                                case "fail":
                                    Log.d("로그인", "불일치");
                                    Toast.makeText(getApplicationContext(), "Login Fail!", Toast.LENGTH_LONG).show();
                                    break;
                                //일치
                                case "success":
                                    Log.d("로그인", "완료");
                                    Log.d("DB저장", "실행");
                                    //로그인된 아이디, 비밀번호를 SharedPreferences에 저장
                                    SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("loginId", idInput.getText().toString());
                                    editor.putString("loginPassword", passwordInput.getText().toString());
                                    editor.commit();
                                    Log.d("DB저장", "완료");
                                    //즐겨찾기 엑티비티로 이동
                                    Intent intent = new Intent(this, FavActivity.class);
                                    intent.putExtra("id", idInput.getText().toString());
                                    startActivity(intent);
                                    finish();
                                    break;
                                default:
                                    Log.d("로그인", "실패");
                                    Toast.makeText(getApplicationContext(), "Login Fail!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.signupButton:
                Intent intent = new Intent(this, JoinMemberActivity.class);
                Log.d("a", "b");
                startActivity(intent);
                finish();
        }
    }
}
