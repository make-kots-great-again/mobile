package com.morgan.make_kots_great_again;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SpinnerCustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<List> shopping_lists;
    LayoutInflater inflter;

    public SpinnerCustomAdapter(Context applicationContext, ArrayList<List> lists) {
        this.context = applicationContext;
        this.shopping_lists = lists;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return shopping_lists.size();
    }

    @Override
    public Object getItem(int pos) {
        return shopping_lists.get(pos);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_format, null);
        TextView spinner_text = view.findViewById(R.id.spinner_label);
        List current_list = shopping_lists.get(position);

        spinner_text.setText(current_list.getList_name());
        if (current_list.is_list_personal()){
            spinner_text.setTextColor(Color.RED);
        }

        return view;
    }
}
