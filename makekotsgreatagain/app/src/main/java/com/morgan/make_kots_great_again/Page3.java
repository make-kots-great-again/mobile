package com.morgan.make_kots_great_again;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;

public class Page3 extends AppCompatActivity {

    private final ArrayList<String> product_name = new ArrayList<>();
    private final ArrayList<String> product_owner = new ArrayList<>();
    private final ArrayList<String> product_quantity = new ArrayList<>();
    private final ArrayList<String> product_uid = new ArrayList<>();

    private String current_list_selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3);

        SharedPreferences pref = Page3.this.getSharedPreferences("MyPref", 0);
        current_list_selected = pref.getString("list", null);

        ApiRequest apiRequest = new ApiRequest(Page3.this);
        apiRequest.Get_items_page3(product_name, product_owner, product_quantity, product_uid, current_list_selected, Page3.this);
    }
}