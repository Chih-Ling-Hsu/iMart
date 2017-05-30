package com.mysampleapp.demo;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mysampleapp.R;
import com.mysampleapp.model.ProductItem;
import com.mysampleapp.service.HttpURLConnectionHandler;
import com.mysampleapp.service.ProductItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/5/30.
 */

public class HistoryFragment extends DemoFragmentBase {
    private static final String ARGUMENT_DEMO_FEATURE_NAME = "demo_feature_name";
    private static final double maxVisibleDemos = 3.5;
    private List<ProductItem> productItemList;
    private static int pos;
    private static ProductItemAdapter adapter;
    private ListView mListView;
    private TextView resultTxt;

    public static HistoryFragment newInstance(final String demoFeatureName) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(HistoryFragment.ARGUMENT_DEMO_FEATURE_NAME, demoFeatureName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle args = getArguments();
        final String demoFeatureName = args.getString(ARGUMENT_DEMO_FEATURE_NAME);
        final DemoConfiguration.DemoFeature demoFeature = DemoConfiguration.getDemoFeatureByName(demoFeatureName);

        // Set the title for the instruction fragment.
        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(demoFeature.titleResId);
        }
        resultTxt = (TextView) view.findViewById(R.id.txt_result);
        mListView = (ListView) view.findViewById(R.id.list_message);

        new RequestTask("https://pbqcetfbzl.execute-api.us-east-1.amazonaws.com/develop/consumer-record").execute("0");

        /*productItemList = new ArrayList<ProductItem>();
        productItemList.add(new ProductItem("Apple", "2017/5/30", "100", "https://s3.amazonaws.com/rekognition-image-102062329/image1.jpg", "30", "80"));
        productItemList.add(new ProductItem("Apple", "2017/5/30", "100", "https://s3.amazonaws.com/rekognition-image-102062329/image1.jpg", "30", "80"));

        // Set items to the list view
        adapter = new ProductItemAdapter(getActivity(), productItemList);
        mListView.setAdapter(adapter);*/


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
                httpUtil.sendPost(api_url, "{\"consumer_id\":"+postMsg+"}");
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
            for(int j=0; j<item.getJSONArray("detail").length(); j++){
                JSONObject product = item.getJSONArray("detail").getJSONObject(j);
                productItemList.add(new ProductItem(product.getString("product_name"), item.getString("time"), product.getString("price")));
            }

        }
        // Set items to the list view
        adapter = new ProductItemAdapter(getActivity(), productItemList);
        mListView.setAdapter(adapter);
    }
}