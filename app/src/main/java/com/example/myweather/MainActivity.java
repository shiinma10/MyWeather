package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    TextView txtTemp, txtFeels_like, txtTemp_min,txtTemp_max, txtPressure, txtHumidity, txtDescription, txtClouds, txtCountry, txtSunrise, txtSunset, txtName;
    EditText search;
    GifImageView gifImageView, weather;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTemp=findViewById(R.id.txt_temp);
        txtTemp.setText("");
        txtFeels_like=findViewById(R.id.txt_fl);
        txtFeels_like.setText("");
        txtTemp_min=findViewById(R.id.txt_temp_min);
        txtTemp_min.setText("");
        txtTemp_max=findViewById(R.id.txt_temp_max);
        txtTemp_max.setText("");
        txtPressure=findViewById(R.id.txt_pressure);
        txtPressure.setText("");
        txtHumidity=findViewById(R.id.txt_humidity);
        txtHumidity.setText("");
        txtDescription=findViewById(R.id.txt_desc);
        txtDescription.setText("");
        txtClouds=findViewById(R.id.txt_clouds);
        txtClouds.setText("");
        txtCountry=findViewById(R.id.txt_country);
        txtCountry.setText("");
        txtSunrise=findViewById(R.id.txt_sunrise);
        txtSunrise.setText("");
        txtSunset=findViewById(R.id.txt_sunset);
        txtSunset.setText("");
        txtName=findViewById(R.id.txt_name);
        txtName.setText("");

        search=findViewById(R.id.edt_search);
        gifImageView=findViewById(R.id.gifView);
        weather=findViewById(R.id.weather_image);
        floatingActionButton=findViewById(R.id.fb_search);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm=(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                api_key(String.valueOf(search.getText()));
            }
        });

    }

    private void api_key(final String City) {
        final OkHttpClient client=new OkHttpClient();

        Request request=new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?q="+City+"&appid=2575931d1735ba1c4ccc756c7073aea6&units=metric")
                .get()
                .build();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response = client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData=  response.body().string();
                    try {
                        JSONObject json=new JSONObject(responseData);

                        JSONArray array=json.getJSONArray("weather");
                        JSONObject object=array.getJSONObject(0);
                        String description= object.getString("description");
                        String icons = object.getString("icon");

                        JSONObject temp1= json.getJSONObject("main");
                        Double Temperature=temp1.getDouble("temp");
                        int FeelsLike=temp1.getInt("feels_like");
                        Double TempMin=temp1.getDouble("temp_min");
                        Double TempMax=temp1.getDouble("temp_max");
                        int Pressure=temp1.getInt("pressure");
                        int Humidity=temp1.getInt("humidity");

                        JSONObject sys= json.getJSONObject("sys");
                        String country =  sys.getString("country");
                        String sunrise = convertDate (sys.getLong("sunrise"));
                        String sunset = convertDate(sys.getLong("sunset"));

                        JSONObject clouds= json.getJSONObject("clouds");
                        int All=clouds.getInt("all");

                        setText(txtName, City);

                        String temps=Math.round(Temperature)+" °C";
                        setText(txtTemp, temps);

                        String temp2=Math.round(TempMin)+" °C";
                        setText(txtTemp_min, temp2);

                        String temp3=Math.round(TempMax)+" °C";
                        setText(txtTemp_max, temp3);

                        String temp4=Math.round(FeelsLike)+" °C";
                        setText(txtFeels_like, temp4);

                        String cloud=Math.round(All)+" %";
                        setText(txtClouds, cloud);

                        String pressure=Math.round(Pressure)+" hpa";
                        setText(txtPressure, pressure);

                        String humidity=Math.round(Humidity)+"%";
                        setText(txtHumidity, humidity);

                        setText(txtDescription, description);
                        setText(txtCountry, country);
                        setText(txtSunrise, sunrise);
                        setText(txtSunset, sunset);
                        setGifImageView(weather, icons);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String convertDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        if (minutes<10){
            return (hours+":0"+minutes+":"+seconds);
        }else {
            return (hours + ":" + minutes + ":" + seconds);
        }
    }

    private void setText(final TextView txtName, final String city) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtName.setText(city);
            }
        });
    }


    private void setGifImageView (final GifImageView gifImageView, final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (value) {
                    case "01d":

                        break;
                    case "01n":
                        gifImageView.setImageDrawable(getResources().getDrawable(R.drawable.d01n));
                        break;
                    case "02d":
                        gifImageView.setImageDrawable(getResources().getDrawable(R.drawable.d02n));
                        break;
                    case "02n":
                        gifImageView.setImageDrawable(getResources().getDrawable(R.drawable.d02n));
                        break;
                    case "03d":
                        gifImageView.setImageDrawable(getResources().getDrawable(R.drawable.d03n));
                        break;
                    case "03n":
                        gifImageView.setImageDrawable(getResources().getDrawable(R.drawable.d03n));
                        break;
                    case "04d":
                        gifImageView.setImageDrawable(getResources().getDrawable(R.drawable.d04n));
                        break;
                    case "04n":
                        gifImageView.setImageDrawable(getResources().getDrawable(R.drawable.d04n));
                        break;
                    case "09d":
                        gifImageView.setImageDrawable(getResources().getDrawable(R.drawable.d09n));
                        break;
                    case "09n":
                        gifImageView.setImageDrawable(getResources().getDrawable(R.drawable.d09n));
                        break;
                    case "10d":
                        gifImageView.setImageDrawable(getResources().getDrawable(R.drawable.d10n));
                        break;
                    case "10n":
                        gifImageView.setImageDrawable(getResources().getDrawable(R.drawable.d10n));
                        break;
                    case "11d":
                        gifImageView.setImageDrawable(getResources().getDrawable(R.drawable.d11n));
                        break;
                    case "11n":
                        gifImageView.setImageDrawable(getResources().getDrawable(R.drawable.d11n));
                        break;
                    case "13d":
                        gifImageView.setImageDrawable(getResources().getDrawable(R.drawable.d13n));
                        break;
                    case "13n":
                        gifImageView.setImageDrawable(getResources().getDrawable(R.drawable.d13n));
                        break;
                }
            }
        });
    }

}
