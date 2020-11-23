package com.morgan.make_kots_great_again;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

    private Spinner spinner;
    private ArrayAdapter<String> spinnerArrayAdapter;
    protected String current_list_selected;
    protected ImageButton btn_refresh;

    protected final ArrayList<String> lists = new ArrayList<>(); // list contenant les noms des shoppinglist du user
    protected final ArrayList<String> items = new ArrayList<>(); // items names
    protected final ArrayList<String> items_owner = new ArrayList<>();
    protected final ArrayList<String> items_quantity = new ArrayList<>();
    protected final ArrayList<String> items_uid = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page2);

        spinner = findViewById(R.id.dropdown_list);
        spinner.setOnItemSelectedListener(this);
        TextView welcome_user = findViewById(R.id.label_welcome);

        //----------------------------------------------------------
        // Fix bug of having to type 3 times username and password
        try {
            TimeUnit.MILLISECONDS.sleep(500);
            set_spinner();
        } catch (InterruptedException ignored) { }
        // ---------------------------------------------------------

        ApiRequest apiRequest = new ApiRequest(Page2.this);

        welcome_user.setText(Html.fromHtml("Welcome back <span style=\"color:blue\">" + apiRequest.user + "</span> !"));
        apiRequest.Get_Shopping_Lists(lists);

        try {
            TimeUnit.MILLISECONDS.sleep(500);
            set_spinner();
        } catch (InterruptedException ignored) { }

        final Button btn_add_product = findViewById(R.id.button_new_product);
        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                addProductPopup popup = new addProductPopup(Page2.this);
                popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        refresh_user_vue();
                    }
                });
                popup.build();
            }
        });

        Button btn_mode_achat = findViewById(R.id.button_mode_achat);
        btn_mode_achat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_page3();
            }
        });

        // A CHANGER !!!!!!!
        // ---------------------------------------------------------------------------------------------------------------------------------------------
        btn_refresh = findViewById(R.id.button_refresh);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiRequest apiRequest = new ApiRequest(Page2.this);
                reset_arrayLists(items, items_owner, items_quantity, items_uid);
                apiRequest.Get_Shopping_Lists_items(items, items_owner, items_quantity, items_uid, current_list_selected, Page2.this);
            }
        });
        // ---------------------------------------------------------------------------------------------------------------------------------------------
    }

    // Dropdown menu with list "onChange functions"
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        //Lors d'un choix de liste, requete GET avec le nom de liste pour choper les elements de cette liste
        current_list_selected = parent.getItemAtPosition(position).toString();
        Log.d("SPINNER",current_list_selected);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("list", current_list_selected);
        editor.commit();

        ApiRequest apiRequest = new ApiRequest(Page2.this);
        reset_arrayLists(items, items_owner, items_quantity, items_uid);
        apiRequest.Get_Shopping_Lists_items(items, items_owner, items_quantity, items_uid, current_list_selected, Page2.this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }

    private void set_spinner(){
        spinnerArrayAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, lists);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    /**
     * ---------------------------------------------------
     *  Method that clears all elements from 4 arraylist
     * ---------------------------------------------------
     * @param items
     * @param owner
     * @param quantity
     * @param uid
     */
    private void reset_arrayLists(final ArrayList<String> items, final ArrayList<String> owner, final ArrayList<String> quantity, final ArrayList<String> uid){
        items.clear(); owner.clear();
        quantity.clear(); uid.clear();
    }

    /**
     * ---------------------------------------------------
     *  Method that destroy current activity and launch "Page3"
     * ---------------------------------------------------
     */
    private void launch_page3(){
        Intent intent = new Intent(this, Page3.class);
        startActivity(intent);
        finish();// Kills curent activity
    }
    private void refresh_user_vue(){
        ApiRequest apiRequest = new ApiRequest(Page2.this);
        reset_arrayLists(items, items_owner, items_quantity, items_uid);
        apiRequest.Get_Shopping_Lists_items(items, items_owner, items_quantity, items_uid, current_list_selected, Page2.this);
    }
    protected void refresh_user_vue2(ArrayList<String> items, ArrayList<String> owner, ArrayList<String> quantity, ArrayList<String> uid, String current_list_selected, Activity activity){
        ApiRequest apiRequest = new ApiRequest(Page2.this);
        reset_arrayLists(items, owner, quantity, uid);
        apiRequest.Get_Shopping_Lists_items(items, owner, quantity, uid, current_list_selected, activity);
    }
}