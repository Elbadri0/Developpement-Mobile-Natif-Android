package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextVille;
    private ListView listViewMeteo;
    private ImageButton buttonOK;
    List<MeteoItem> data = new ArrayList<>();
    private MeteoListModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextVille = findViewById(R.id.editTextVille);
        listViewMeteo = findViewById(R.id.listViewMeteo);
        buttonOK = findViewById(R.id.imageButton3);

        model = new MeteoListModel(getApplicationContext(), R.layout.list_item_layout, data);
        listViewMeteo.setAdapter(model);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyLog", ".....");
                data.clear();
                model.notifyDataSetChanged();
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                String ville = editTextVille.getText().toString();
                Log.i("MyLog", ville);
                String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + ville +
                        "&appid=9d6480c227693d97a6ef4b80fd715e6f&units=metric";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("MyLog", ".........");
                            Log.i("MyLog", response);
                            List<MeteoItem> meteoItems = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                MeteoItem meteoItem = new MeteoItem();
                                JSONObject d = jsonArray.getJSONObject(i);
                                Date date = new Date(d.getLong("dt") * 1000);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                String dateString = simpleDateFormat.format(date);
                                JSONObject main = d.getJSONObject("main");
                                JSONArray weather = d.getJSONArray("weather");
                                int tempMin = (int) main.getDouble("temp_min");
                                int tempMax = (int) main.getDouble("temp_max");
                                int pression = main.getInt("pressure");
                                int humidite = main.getInt("humidity");
                                meteoItem.tempMin = tempMin;
                                meteoItem.tempMax = tempMax;
                                meteoItem.pression = pression;
                                meteoItem.humidite = humidite;
                                meteoItem.date = dateString;
                                meteoItem.image = weather.getJSONObject(0).getString("main");
                                meteoItems.add(meteoItem);
                                data.add(meteoItem);
                            }
                            model.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("MyLog", "Connection Problem");
                            }
                        });
                requestQueue.add(stringRequest);
            }
        });
    }
}
