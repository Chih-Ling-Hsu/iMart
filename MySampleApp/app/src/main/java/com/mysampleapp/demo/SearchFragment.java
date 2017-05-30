package com.mysampleapp.demo;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobilehelper.auth.IdentityHandler;
import com.amazonaws.mobilehelper.auth.IdentityManager;
import com.amazonaws.mobilehelper.auth.SignInStateChangeListener;
import com.amazonaws.mobilehelper.auth.user.IdentityProfile;
import com.mysampleapp.R;
import com.mysampleapp.model.ProductItem;
import com.mysampleapp.service.HttpURLConnectionHandler;
import com.mysampleapp.service.ProductItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by User on 2017/5/30.
 */

public class SearchFragment extends DemoFragmentBase {
    private static final String ARGUMENT_DEMO_FEATURE_NAME = "demo_feature_name";
    private static final double maxVisibleDemos = 3.5;
    private List<ProductItem> productItemList;
    private static int pos;
    private static ProductItemAdapter adapter;
    private ListView mListView;
    private TextView resultTxt;

    public static SearchFragment newInstance(final String demoFeatureName) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(SearchFragment.ARGUMENT_DEMO_FEATURE_NAME, demoFeatureName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
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

        mListView = (ListView) view.findViewById(R.id.list_message);
        resultTxt = (TextView) view.findViewById(R.id.search_result);
        final ImageView searchBtn = (ImageView) view.findViewById(R.id.btn_search);
        final EditText editSearch = (EditText) view.findViewById(R.id.text_search);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = String.valueOf(editSearch.getText());
                Log.e("SEARCH", searchText);

                new RequestTask("https://pbqcetfbzl.execute-api.us-east-1.amazonaws.com/develop/search").execute(searchText);
            }
        });


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
                    resultTxt.setText("無符合項目");
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