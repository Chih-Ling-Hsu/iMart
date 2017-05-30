package com.mysampleapp.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mysampleapp.R;
import com.mysampleapp.model.ProductItem;

import java.util.List;

/**
 * Created by User on 2017/5/30.
 */

public class ProductItemAdapter extends BaseAdapter {
    private List<ProductItem> mMessageList;
    private LayoutInflater mMyInflater;

    public ProductItemAdapter(Context c, List<ProductItem> list) {
        this.mMessageList = list;
        mMyInflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return mMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setList(List<ProductItem> list) {
        this.mMessageList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mMyInflater.inflate(R.layout.list_item_product, null);
        ProductItem message = mMessageList.get(position);

        TextView itemTxt = (TextView) convertView.findViewById(R.id.txt_item);
        TextView priceTxt = (TextView) convertView.findViewById(R.id.txt_price);
        TextView locationTxt = (TextView) convertView.findViewById(R.id.txt_location);

        itemTxt.setText(message.getName());
        priceTxt.setText("$"+message.getPrice());
        if(message.getLocation().length()==1) {
            locationTxt.setText("Section " + message.getLocation());
        }
        else{
            locationTxt.setText(message.getLocation());
        }

        return convertView;
    }
}