package com.example.myapplicationtest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.SSLSocket;

public class MainActivity extends AppCompatActivity {
    TextView tvData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvData = (TextView)findViewById(R.id.textView);
        Button btn = (Button)findViewById(R.id.httpTest);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {      //버튼이 클릭되면 여기 리스너로 옴
                new JSONTask().execute("http://13.125.3.60:3000/post");//AsyncTask 시작시킴
            }
        });
    }

    public class JSONTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("db_test", 21);
                HttpURLConnection con = null;
                BufferedReader reader = null;
                try{
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection(); // 연결

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    OutputStream outStream = con.getOutputStream();  //서버로 보내기위한 스트림. request Body에 Data를 담기위해
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));  //버퍼를 생성하고 넣음
                    writer.write(jsonObject.toString()); //db_tset 21
                    writer.flush(); // 저장된 버퍼를 전송하고 버퍼를 비ㅇ
                    writer.close();//버퍼를 받아줌

                    InputStream stream = con.getInputStream(); //서버로부터 데이터를 받음
                    reader = new BufferedReader(new InputStreamReader(stream)); //효율처리
                    StringBuffer buffer = new StringBuffer();// 문자열 추가 변경
                    String line = "";

                    while((line = reader.readLine()) != null) buffer.append(line);
                    return buffer.toString();//서버로부터 받은 값을 리턴
                }  finally {
                    if(con != null) con.disconnect();
                    if(reader != null) reader.close();
                }
            } catch (Exception e) { e.printStackTrace(); }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tvData.setText(result);//서버로 부터 받은 값을 출력
        }
    }
}