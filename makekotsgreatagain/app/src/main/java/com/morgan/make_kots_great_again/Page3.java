package com.morgan.make_kots_great_again;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Page3 extends AppCompatActivity {

    private String current_list_selected;

    private ListView listview2;
    private Button addReserve_btn, noAddReserve_btn;

    protected final ArrayList<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3);

        SharedPreferences pref = Page3.this.getSharedPreferences("MyPref", 0);
        current_list_selected = pref.getString("list", null);

        ApiRequest apiRequest = new ApiRequest(Page3.this);
        apiRequest.Get_items_page3(products, current_list_selected, Page3.this);

        listview2 = findViewById(R.id.listview2);

        try {
            TimeUnit.MILLISECONDS.sleep(500);
            listview2.setAdapter(new MyCustomAdapter2(products, Page3.this));
        } catch (InterruptedException ignored) { }

        noAddReserve_btn = findViewById(R.id.button_noAddReserve);
        noAddReserve_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createNoAddReservePopup();
            }
        });
    }

    public void createNoAddReservePopup()
    {
        AlertDialog.Builder addReservePopup = new AlertDialog.Builder(Page3.this);

        if(allItemsChecked())
        {
            addReservePopup.setMessage("Êtes-vous sûr de vouloir supprimer TOUS les éléments de la liste ?");
        }
        else
        {
            addReservePopup.setMessage("Êtes-vous sûr de vouloir supprimer ces éléments de la liste ? Il reste des éléments non-cochés.");
        }

        addReservePopup.setPositiveButton("OUI", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                wipeList();
            }
        });

        addReservePopup.setNegativeButton("NON", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {}
        });

        addReservePopup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                refreshView();
            }
        });

        addReservePopup.show();
    }

    public boolean allItemsChecked()
    {
        for(int i = 0; i<listview2.getChildCount(); i++)
        {
            CheckBox checkBoxStatus = listview2.getChildAt(i).findViewById(R.id.product_checkbox);

            if(!checkBoxStatus.isChecked())
            {
                return false;
            }
        }

        return true;
    }

    public void wipeList()
    {
        ApiRequest apiRequest = new ApiRequest(Page3.this);

        for(int i=0; i<products.size(); i++)
        {
            apiRequest.deleteProductRequest(Page3.this, products.get(i).product_uid);
        }
    }

    public void refreshView()
    {
        ApiRequest apiRequest = new ApiRequest(Page3.this);
        products.clear();
        apiRequest.Get_items_page3(products, current_list_selected, Page3.this);

        try {
            TimeUnit.MILLISECONDS.sleep(500);
            listview2.setAdapter(new MyCustomAdapter2(products, Page3.this));
        } catch (InterruptedException ignored) { }
    }

}