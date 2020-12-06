package com.morgan.make_kots_great_again;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Page3 extends AppCompatActivity {

    private String current_list_name;
    private String current_list_id;

    private ListView listview2;
    private Button btn_deleteProducts;

    protected final ArrayList<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3);

        SharedPreferences pref = Page3.this.getSharedPreferences("MyPref", 0);
        current_list_name = pref.getString("current_list_name", null);
        current_list_id = pref.getString("current_list_groupId", null);

        ApiRequest apiRequest = new ApiRequest(Page3.this);
        apiRequest.Get_items_page3(products, current_list_name, Page3.this);

        listview2 = findViewById(R.id.listview2);

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
            listview2.setAdapter(new MyCustomAdapter2(products, Page3.this));
        } catch (InterruptedException ignored) { }


        btn_deleteProducts = findViewById(R.id.btn_delete_products);
        btn_deleteProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDeletePopup();
            }
        });
        is_there_some_product();
    }

    public void createDeletePopup() {
        AlertDialog.Builder popup = new AlertDialog.Builder(Page3.this);

        if (is_there_some_product()) {
            popup.setMessage("Êtes-vous sûr de vouloir supprimer TOUS les éléments de la liste ?");

            popup.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    wipeList();
                    refreshView();
                    refresh_user_vue_page2();
                }
            });

            popup.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    refreshView();
                }
            });

            popup.show();
        }
    }

    public void wipeList() {
        ApiRequest apiRequest = new ApiRequest(Page3.this);

        for(int i=0; i<products.size(); i++) {
            apiRequest.deleteProductRequest(Page3.this, products.get(i));
        }
    }

    private void refreshView() {
        ApiRequest apiRequest = new ApiRequest(Page3.this);
        products.clear();
        apiRequest.Get_items_page3(products, current_list_name, Page3.this);

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
            listview2.setAdapter(new MyCustomAdapter2(products, Page3.this));
            is_there_some_product();
        } catch (InterruptedException ignored) { }
    }

    private void refresh_user_vue_page2() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("page2_needs_refresh", "true");
        editor.commit();
    }

    private boolean is_there_some_product (){
        if (listview2.getCount() >=1){
            btn_deleteProducts.setVisibility(View.VISIBLE);
            return true;
        }
        else {
            btn_deleteProducts.setVisibility(View.INVISIBLE);
            return false;
        }
    }

}