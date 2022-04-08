package com.example.weatherexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public final String apiKey = "ea69b3d4f0a4a724d2c03268919c83a1";
    public final String address =
            "https://api.openweathermap.org/data/2.5/weather";
    OkHttpClient okHttpClient;
    EditText cityName;
    Button requestButton;
    TextView responseText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.city);
        requestButton = findViewById(R.id.request);
        responseText = findViewById(R.id.response);

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsynsWeather asynsWeather = new AsynsWeather();
                asynsWeather.execute(address);
            }
        });

        okHttpClient = new OkHttpClient();
    }

    class AsynsWeather extends AsyncTask<String, Void, Response>{
        String name = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            name = cityName.getText().toString();
        }

        @Override
        protected Response doInBackground(String... strings) {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(strings[0]).newBuilder();
            urlBuilder.addQueryParameter("appid", apiKey);
            urlBuilder.addQueryParameter("q", name);
            urlBuilder.addQueryParameter("units", "metric");
            //создание строки запроса
            String url = urlBuilder.build().toString();
            //создание запроса
            Request request = new Request.Builder().url(url).build();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if(response!=null) {
                try {
                    responseText.setText(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
                responseText.setText("Ответ не получен");
        }
    }
}