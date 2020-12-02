package com.morgan.make_kots_great_again;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Page2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private ArrayAdapter<String> spinnerArrayAdapter;
    protected String current_list_selected;
    protected final ArrayList<String> lists = new ArrayList<>(); // list contenant les noms des shoppinglist du user
    protected final ArrayList<Product> products = new ArrayList<>();
    protected final ArrayList<Product> products_modified = new ArrayList<>(); // copy of products, used to know if quantities have been modified

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
                        //refresh_user_vue();
                    }
                });
                popup.build();
            }
        });

        Button btn_mode_achat = findViewById(R.id.button_mode_achat);
        btn_mode_achat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(quantitiesHaveBeenChanged(products, products_modified))
                {
                    getModifiedQuantities(products, products_modified, apiRequest);
                }

                launch_page3();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh_user_vue();
    }

    // Dropdown menu with list "onChange functions"
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l)
    {
        //Lors d'un choix de liste, requete GET avec le nom de liste pour choper les elements de cette liste
        current_list_selected = parent.getItemAtPosition(position).toString();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("list", current_list_selected);
        editor.commit();

        ApiRequest apiRequest = new ApiRequest(Page2.this);
        reset_arrayLists(products, products_modified);
        apiRequest.Get_Shopping_Lists_items(products, products_modified, current_list_selected, Page2.this);

        //refresh_user_vue();
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
    private void reset_arrayLists(final ArrayList<Product> products, final ArrayList<Product> products_modified)
    {
        products.clear();
        products_modified.clear();
    }

    /**
     * ---------------------------------------------------
     *  Method that destroy current activity and launch "Page3"
     * ---------------------------------------------------
     */
    private void launch_page3(){
        Intent intent = new Intent(this, Page3.class);
        startActivity(intent);
    }

    protected void refresh_user_vue()
    {
        ApiRequest apiRequest = new ApiRequest(Page2.this);
        reset_arrayLists(products, products_modified);
        apiRequest.Get_Shopping_Lists_items(products, products_modified, current_list_selected, Page2.this);
    }

    /**
     * Compare both Arraylists (products & products_modified) in ordre to detect if we have modified the quantities
     *
     * @param products
     * @param products_modified
     * @return true if any quantity has been changed or false if nothing has been changed
     */
    public boolean quantitiesHaveBeenChanged(final ArrayList<Product> products, final ArrayList<Product> products_modified)
    {
        boolean qty_changed = false;

        for(int i = 0; i<products.size(); i++)
        {
            if(!products.get(i).equals(products_modified.get(i)))
            {
                qty_changed = true;
                break;
            }
        }

        return qty_changed;
    }

    /**
     * Loop on the Arraylists to find which products quantities have been modified
     *
     * @param products
     * @param products_modified
     * @param apiRequest
     */
    public void getModifiedQuantities(final ArrayList<Product> products, final ArrayList<Product> products_modified, ApiRequest apiRequest)
    {
        for(int i = 0; i<products.size(); i++)
        {
            if(!products.get(i).equals(products_modified.get(i)))
            {
                apiRequest.updateProductRequest(Page2.this, products.get(i).product_uid, products_modified.get(i).product_quantity);
            }
        }
    }
}