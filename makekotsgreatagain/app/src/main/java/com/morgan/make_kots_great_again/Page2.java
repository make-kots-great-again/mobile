package com.morgan.make_kots_great_again;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;

public class Page2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page2);

        String lists[] = {"Ma liste Perso","Kot linux"};
        String items[] = {"Banane", "Nutella", "Noisettes", "M&Ms", "Doritos", "Chocolat", "Glaces choco", "Frites"};

        Spinner spinner = findViewById(R.id.dropdown_list);
        ListView listView = findViewById(R.id.listview);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lists);
        spinner.setAdapter(spinnerArrayAdapter);

        //generate list
        ArrayList<String> list = new ArrayList<String>();
        Collections.addAll(list, items);

        //instantiate custom adapter
        MyCustomAdapter adapter = new MyCustomAdapter(list, this);

        //handle listview and assign adapter
        listView.setAdapter(adapter);
    }
}