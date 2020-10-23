package com.morgan.make_kots_great_again;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list;
    private ArrayList<String> items_quantity;
    private Context context;


    public MyCustomAdapter(ArrayList<String> list, ArrayList<String> items_quantity,  Context context) {
        this.list = list;
        this.items_quantity = items_quantity;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_format, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        final TextView quantity = (TextView)view.findViewById(R.id.quantity);
        quantity.setText(items_quantity.get(position));

        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.delete_btn);
        ImageButton addBtn = (ImageButton)view.findViewById(R.id.add_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("PAGE2-TEST", (String) quantity.getText());
                Log.d("PAGE2-TEST", (String) quantity.getText());
                quantity.setText(remove_one_from_string_number((String) quantity.getText()));
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                quantity.setText(add_one_from_string_number((String) quantity.getText()));
            }
        });

        return view;
    }

    //--------------------------------------------------------
    // Function that takes a String representing a number,
    // converts it into a number and then subtract one to it.
    // Returns the number-1 in a String
    //--------------------------------------------------------

    /* @param (String) => Takes a String that represents a number
    *  @return (String) => Returns number-1 in form of a String */

    public static String remove_one_from_string_number(String number_in_string){
        int number = Integer.parseInt(number_in_string);
        int number_minus_one = number -1;
        return Integer.toString(number_minus_one);
    }

    //--------------------------------------------------------
    // Function that takes a String representing a number,
    // converts it into a number and then adds one to it.
    // Returns the number+1 in a String
    //--------------------------------------------------------

    /* @param (String) => Takes a String that represents a number
     *  @return (String) => Returns number+1 in form of a String */

    public static String add_one_from_string_number(String number_in_string){
        int number = Integer.parseInt(number_in_string);
        int number_plus_one = number +1;
        return Integer.toString(number_plus_one);
    }
}