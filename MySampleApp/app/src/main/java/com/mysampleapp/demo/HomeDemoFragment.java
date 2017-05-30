package com.mysampleapp.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mysampleapp.R;

public class HomeDemoFragment extends DemoFragmentBase {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_demo_home, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final LinearLayout searchBtn = (LinearLayout) view.findViewById(R.id.btn_search);
        final LinearLayout historyBtn = (LinearLayout) view.findViewById(R.id.btn_history);

        searchBtn.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v) {
                        final Fragment fragment = SearchFragment.newInstance("search");

                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_fragment_container, fragment, "search")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
                    }
                }
        );

        historyBtn.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v) {
                        final Fragment fragment = HistoryFragment.newInstance("history");

                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_fragment_container, fragment, "history")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
                    }
                }
        );
    }
}
