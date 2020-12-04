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
    protected String current_list_name;
    protected String current_list_groupId;
    protected ArrayList<Product> products = new ArrayList<>();
    protected ArrayList<Product> products_modified = new ArrayList<>(); // copy of products, used to know if quantities have been modified

    protected ArrayList<List> lists = new ArrayList<>(); // ArrayList comprenant toutes les listes auquels l'utilisateurs à accés (stocké en tant qu'objet liste)
    protected List list_selected; // Liste actuellement selectionner dans le menu deroulant.

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
        apiRequest.Get_Shopping_Lists(lists); // Updates ArrayList "lists"

        try {
            TimeUnit.MILLISECONDS.sleep(500);
            set_spinner();
        } catch (InterruptedException ignored) { }

        final Button btn_add_product = findViewById(R.id.button_new_product);
        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        btn_mode_achat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                /*
                if(quantitiesHaveBeenChanged(products, products_modified)) {
                    getModifiedQuantities(products, products_modified, apiRequest);
                }

                launch_page3();*/
                refresh_user_vue();
            }
        });
    }
    /*

    @Override
    protected void onResume() {
        super.onResume();
        refresh_user_vue();
    }*/

    // Dropdown menu with list "onChange functions"
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l)
    {
        //Lors d'un choix de liste, requete GET avec le nom de liste pour choper les elements de cette liste
        reset_selected_lists(lists);
        list_selected = (List) parent.getItemAtPosition(position);
        list_selected.set_is_selected(true);

        current_list_name = list_selected.getList_name();
        current_list_groupId = list_selected.getList_id();

        Log.d("CURRENT-LIST-NAME", current_list_name);
        Log.d("CURRENT-LIST-GroupID", current_list_groupId);

        /*
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("current_list_name", current_list_name);
        editor.putString("current_list_groupId", current_list_groupId);
        editor.commit();*/

        refresh_user_vue();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }

    private void set_spinner(){
        SpinnerCustomAdapter spinnerCustomAdapter = new SpinnerCustomAdapter(getBaseContext(), lists);
        spinner.setAdapter(spinnerCustomAdapter);
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

    protected void refresh_user_vue() {
        ApiRequest apiRequest = new ApiRequest(Page2.this);
        reset_arrayLists(products, products_modified);
        apiRequest.Get_Shopping_Lists_items(products, products_modified, list_selected, Page2.this);
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
    public void getModifiedQuantities(final ArrayList<Product> products, final ArrayList<Product> products_modified, ApiRequest apiRequest) {
        for (int i = 0; i<products.size(); i++) {
            if(!products.get(i).equals(products_modified.get(i))) {
                apiRequest.updateProductRequest(Page2.this, products.get(i).getProduct_uid(), products_modified.get(i).getProduct_quantity());
            }
        }
    }
    private void reset_selected_lists (ArrayList<List> list){
        for (int z =0; z<list.size(); z++){
            list.get(z).set_is_selected(false);
        }
    }

    private List get_the_current_selected_list (ArrayList<List> list){
        for (int z =0; z<list.size(); z++){
            if (list.get(z).get_selected_list()){
                return list.get(z);
            }
        }
        return null;
    }
}