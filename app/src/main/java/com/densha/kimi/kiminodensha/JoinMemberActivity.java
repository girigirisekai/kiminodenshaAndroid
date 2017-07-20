package com.densha.kimi.kiminodensha;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 이민호 on 2017-07-19.
 */

public class JoinMemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void joinButton(View v){
        switch (v.getId()){
            case R.id.joinButton:
                URL url = null;
                HttpURLConnection con = null;
                StringBuffer sb = new StringBuffer();
                Log.d("1","2");
                try{
                    url = new URL("http://203.233.196.147:8888/densha/joinMember");
                }catch (MalformedURLException e){
                    Toast.makeText(this, "잘못된 URL입니다.", Toast.LENGTH_SHORT).show();
                }try {
                con = (HttpURLConnection) url.openConnection();
                if (con != null) {
                    con.setConnectTimeout(10000);
                    Log.d("3","4");
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
                    os.write("num:01".getBytes("utf-8"));
                    os.flush();
                    os.close();
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStreamReader in = new InputStreamReader(con.getInputStream());
                        int ch;
                        while ((ch = in.read()) != -1) {
                            sb.append((char) ch);
                        }
                        in.close();
                       /* jsontext = sb.toString();
                        textView.setText(jsontext);*/
                    }
                }
            }catch (Exception e) {
                Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            }

                break;
       /*     case 2:
                break;*/

        }
    }


}
