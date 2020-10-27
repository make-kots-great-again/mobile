package com.morgan.make_kots_great_again;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Page2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String current_user_name;
    private String current_user_token;
    private Spinner spinner;
    private ListView listView;
    private ArrayAdapter<String> spinnerArrayAdapter;
    private String current_list_selected;

    final ArrayList<String> lists = new ArrayList<>();
    final ArrayList<String> items = new ArrayList<>();
    final ArrayList<String> items_quantity = new ArrayList<>();

    private final String get_url_route = "https://kotsapp.herokuapp.com/server/api/shoppingList/";
    //private final String get_url_route = "http://172.18.0.3:8000/server/api/shoppingList/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page2);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        current_user_name = pref.getString("username", null);
        current_user_token = pref.getString("token", null);

        spinner = findViewById(R.id.dropdown_list);
        spinner.setOnItemSelectedListener(this);
        listView = findViewById(R.id.listview);
        TextView welcome_user = findViewById(R.id.label_welcome);

        welcome_user.setText(Html.fromHtml("Welcome back <span style=\"color:blue\">" + current_user_name + "</span> !"));

        Get_Shopping_Lists(get_url_route, lists);
        try {
            TimeUnit.MILLISECONDS.sleep(500);
            set_spinner();
        } catch (InterruptedException ignored) { }
    }

    public void Get_Shopping_Lists(String url, final ArrayList<String> arrayList) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder().header("Authorization", current_user_token).url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
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

                    Iterator<String> iter = Jobject2.keys();
                    while(iter.hasNext()){
                        String key = iter.next();
                        arrayList.add(key);
                    }
                } catch (JSONException ignored) { }
            }
        });
    }

    // Dropdown menu with list "onChange functions"
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        //Lors d'un choix de liste, requete GET avec le nom de liste pour choper les elements de cette liste
        current_list_selected = parent.getItemAtPosition(position).toString();
        Log.d("SPINNER",current_list_selected);

        Get_Shopping_Lists_items(get_url_route, items, items_quantity);
        try {
            TimeUnit.MILLISECONDS.sleep(500);
            set_listview();
        } catch (InterruptedException ignored) { }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do Nothing
    }

    public void Get_Shopping_Lists_items(String url, final ArrayList<String> items, final ArrayList<String> quantity) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder().header("Authorization", current_user_token).url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("GET", "Error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String responseBody = response.body().string();
                Log.d("GET", "Ok");
                try {
                    JSONObject Jobject = new JSONObject(responseBody);
                    JSONObject Jobject2 = Jobject.getJSONObject("shoppingList");
                    JSONArray Jarray = Jobject2.getJSONArray(current_list_selected);

                    items.clear();
                    quantity.clear();

                    for(int i = 0; i < Jarray.length(); i++) {
                        JSONObject object = Jarray.getJSONObject(i);
                        String product_name = object.getString("product_name");
                        String product_quantity = object.getString("quantity");
                        items.add(product_name);
                        quantity.add(product_quantity);
                    }

                } catch (JSONException ignored) { }
            }
        });
    }
    private void set_listview(){
        listView.setAdapter(new MyCustomAdapter(items, items_quantity, getBaseContext()));
    }
    private void set_spinner(){
        spinnerArrayAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, lists);
        spinner.setAdapter(spinnerArrayAdapter);
    }
}