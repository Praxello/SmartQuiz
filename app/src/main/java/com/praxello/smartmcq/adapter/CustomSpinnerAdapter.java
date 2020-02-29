package com.praxello.smartmcq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.praxello.smartmcq.R;
import com.praxello.smartmcq.model.categories.GetCategoriesBO;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    public Context context;
    public ArrayList<GetCategoriesBO> getCategoriesBOArrayList;

    public CustomSpinnerAdapter(Context context, ArrayList<GetCategoriesBO> getCategoriesBOArrayList) {
        this.context = context;
        this.getCategoriesBOArrayList = getCategoriesBOArrayList;
    }

    @Override
    public int getCount() {
        return getCategoriesBOArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return getCategoriesBOArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getCategoriesBOArrayList.get(position).getCategoryId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  View.inflate(context, R.layout.layout_spinner_main, null);
        TextView textView =  view.findViewById(R.id.main);
        textView.setText(getCategoriesBOArrayList.get(position).getCategoryTitle());
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view;
        view =  View.inflate(context, R.layout.layout_spinner_row, null);
        final TextView textView = view.findViewById(R.id.dropdown);
        textView.setText(getCategoriesBOArrayList.get(position).getCategoryTitle());
        return view;
    }
}
