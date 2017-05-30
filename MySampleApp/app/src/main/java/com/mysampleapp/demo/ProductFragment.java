package com.mysampleapp.demo;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobilehelper.auth.ImageDownloader;
import com.mysampleapp.R;
import com.mysampleapp.model.ProductItem;
import com.mysampleapp.service.HttpURLConnectionHandler;
import com.mysampleapp.service.ProductItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/5/30.
 */

public class ProductFragment extends DemoFragmentBase{
    private static final String PRODUCT_NAME = "product_name";
    private static final String PRODUCT_LOCATION = "product_location";
    private static final String PRODUCT_IMAGE_URL = "product_image_url";
    private static final String PRODUCT_PRICE = "product_price";
    private static final String PRODUCT_CONSUME = "product_consume";
    private static final String PRODUCT_REPURCHASE = "product_repurchase";
    private List<ProductItem> productItemList;
    private static int pos;
    private static ProductItemAdapter adapter;
    private ListView mListView;
    private TextView resultTxt;

    /** This fragment's view. */
    private View mFragmentView;

    public static ProductFragment newInstance(final ProductItem item) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ProductFragment.PRODUCT_NAME, item.getName());
        args.putString(ProductFragment.PRODUCT_LOCATION, item.getLocation());
        args.putString(ProductFragment.PRODUCT_PRICE, item.getPrice());
        args.putString(ProductFragment.PRODUCT_IMAGE_URL, item.getImgUrl());
        args.putString(ProductFragment.PRODUCT_CONSUME, item.getMonthConsume());
        args.putString(ProductFragment.PRODUCT_REPURCHASE, item.getRepurchaseRate());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mFragmentView = inflater.inflate(R.layout.fragment_product, container, false);
        return mFragmentView;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the title for the instruction fragment.
        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Product Info");
        }

        ProductItem item = new ProductItem(this.getArguments().getString(PRODUCT_NAME),
                    this.getArguments().getString(PRODUCT_LOCATION),
                    this.getArguments().getString(PRODUCT_PRICE),
                    this.getArguments().getString(PRODUCT_IMAGE_URL),
                    this.getArguments().getString(PRODUCT_CONSUME),
                    this.getArguments().getString(PRODUCT_REPURCHASE)
                );

        final TextView nameTxt = (TextView) view.findViewById(R.id.txt_name);
        final TextView priceTxt = (TextView) view.findViewById(R.id.txt_price);
        final TextView locTxt = (TextView) view.findViewById(R.id.txt_location);
        final TextView comsumeTxt = (TextView) view.findViewById(R.id.txt_comsume);
        final TextView repurchaseTxt = (TextView) view.findViewById(R.id.txt_repurchase);
        final ImageView itemImg = (ImageView) view.findViewById(R.id.img_product);
        resultTxt = (TextView) view.findViewById(R.id.txt_result);
        mListView = (ListView) view.findViewById(R.id.list_message);

        nameTxt.setText(item.getName());
        priceTxt.setText("\n$"+item.getPrice());
        locTxt.setText("商品位置: Section "+item.getLocation());
        comsumeTxt.setText("當月賣出: "+item.getMonthConsume());
        repurchaseTxt.setText("回購率: "+item.getRepurchaseRate()+"%");

        new DownloadImageTask(itemImg).execute(item.getImgUrl());
        new RequestTask("https://pbqcetfbzl.execute-api.us-east-1.amazonaws.com/develop/recommend").execute(item.getName());

        /*productItemList = new ArrayList<ProductItem>();
        productItemList.add(new ProductItem("Apple", "Section A", "100", "https://s3.amazonaws.com/rekognition-image-102062329/image1.jpg", "30", "80"));
        productItemList.add(new ProductItem("Apple", "Section A", "100", "https://s3.amazonaws.com/rekognition-image-102062329/image1.jpg", "30", "80"));

        // Set items to the list view
        adapter = new ProductItemAdapter(getActivity(), productItemList);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.e("CLICK", String.valueOf(position));
                ProductItem item = productItemList.get(position);
                final ProductFragment fragment = ProductFragment.newInstance(item);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment_container, fragment, "Product Info")
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                //getActivity().getSupportActionBar().setTitle(item.titleResId);
            }
        });*/


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            ImageDownloader imgUtil = new ImageDownloader();
            imgUtil.loadImageFromUrl(imageUrl);
            while(imgUtil.getImage()==null){

            }
            Log.e("Async OK", "OKKKKKKKKKKKKK");
            return imgUtil.getImage();
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private class RequestTask extends AsyncTask<String, Void, String> {
        String api_url;

        public RequestTask(String api_url) {
            this.api_url = api_url;
        }

        protected String doInBackground(String... urls) {
            String postMsg = urls[0];
            String response = "[]";

            try {
                HttpURLConnectionHandler httpUtil = new HttpURLConnectionHandler();
                httpUtil.sendPost(api_url, "{\"product\":\""+postMsg+"\"}");
                while(httpUtil.complete==false){

                }
                response = httpUtil.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Asynk Error", e.toString());
            }

            return response;
        }

        protected void onPostExecute(String response) {
            Log.e("Async OK", response);
            try {
                if(response.equals("[]")){
                    resultTxt.setText("");
                }
                else {
                    rendarProductList(response);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void rendarProductList(String response) throws JSONException {
        JSONArray productArray = new JSONArray(response);
        Log.e("JSONNNNN", String.valueOf(productArray.length()));
        productItemList = new ArrayList<ProductItem>();
        for (int i = 0; i < productArray.length(); i++) {
            JSONObject item = productArray.getJSONObject(i);
            productItemList.add(new ProductItem(item.getString("product_name"), item.getString("location"), item.getString("price"), item.getString("image_url"), item.getString("sell_amount"), item.getString("repurchase_rate")));
        }
        // Set items to the list view
        adapter = new ProductItemAdapter(getActivity(), productItemList);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.e("CLICK", String.valueOf(position));
                ProductItem item = productItemList.get(position);
                final ProductFragment fragment = ProductFragment.newInstance(item);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment_container, fragment, "Product Info")
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                //getActivity().getSupportActionBar().setTitle(item.titleResId);
            }
        });
    }
}
