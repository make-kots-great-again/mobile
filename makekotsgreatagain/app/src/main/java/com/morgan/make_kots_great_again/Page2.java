package com.morgan.make_kots_great_again;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Iterator;

public class Page2 extends AppCompatActivity {

    private String current_user_name;
    private String current_user_token;
    Spinner spinner;
    ArrayAdapter<String> spinnerArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page2);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        current_user_name = pref.getString("username", null);
        current_user_token = pref.getString("token", null);

        final ArrayList<String> lists = new ArrayList<>();


        String items[] = {"Banane", "Nutella", "Noisettes", "M&Ms", "Doritos", "Chocolat", "Glaces choco", "Frites"};
        ArrayList<String> quantity = new ArrayList<>();
        String items_quantity[] = {"1", "3", "5", "7", "2", "3", "1", "2"};
        Collections.addAll(quantity, items_quantity);

        spinner = findViewById(R.id.dropdown_list);
        ListView listView = findViewById(R.id.listview);
        Button new_product = findViewById(R.id.button_new_product);
        TextView welcome_user = findViewById(R.id.label_welcome);

        welcome_user.setText(Html.fromHtml("Welcome back <span style=\"color:blue\">" + current_user_name + "</span> !"));

        try {
            Get_Shopping_Lists_Request("http://172.18.0.3:8000/server/api/shoppingList/", lists);
        } catch (Exception ignored) { }


        new_product.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO
            }
        });


        ArrayList<String> list = new ArrayList<String>();
        Collections.addAll(list, items);

        MyCustomAdapter adapter = new MyCustomAdapter(list, quantity, this);
        listView.setAdapter(adapter);
    }

    public void Get_Shopping_Lists_Request(String url, final ArrayList<String> arrayList)  throws Exception {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder().header("Authorization", current_user_token).url(url).build();

        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e) //Ok
            {
                Log.d("GET", "Error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String responseBody = response.body().string();
                Log.d("Get-Ok",responseBody);
                try {
                    JSONObject Jobject = new JSONObject(responseBody);
                    JSONObject Jobject2 = Jobject.getJSONObject("shoppingList");

                    Iterator<String> iter = Jobject2.keys(); //This should be the iterator you want.
                    while(iter.hasNext()){
                        String key = iter.next();
                        arrayList.add(key);
                        spinnerArrayAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
                        spinner.setAdapter(spinnerArrayAdapter);
                        Log.d("ITER",key);
                    }
                } catch (JSONException ignored) { }

                if(!responseBody.equals("Unauthorized")) {
                    //OK
                }
                else {
                    //ERROR
                }
            }
        });
    }
}