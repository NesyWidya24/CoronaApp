package com.nessy.coronaapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class StatsFragment extends Fragment {
    private static final  String STATS_URL = "https://api.covid19api.com/summary";
    Context context;

    private ProgressBar progressBar;
    private EditText searchEt;
    private ImageButton sortBtn;
    private RecyclerView statsRv;

    ArrayList<ModelStat> statArrayList;
    AdapterStat adapterStat;

    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        searchEt = view.findViewById(R.id.searchEt);
        sortBtn = view.findViewById(R.id.sortBtn);
        statsRv = view.findViewById(R.id.statRv);

        progressBar.setVisibility(View.GONE);



        //search
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //called as and when user typing or remove letter
                try {
                    adapterStat.getFilter().filter(charSequence);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //popup menu to show sorting options
        PopupMenu popupMenu = new PopupMenu(context, sortBtn);
        popupMenu.getMenu().add(Menu.NONE, 0, 0 , "Ascending");//1st param is id of item, 2nd param is position in list of item, 3rd param is title
        popupMenu.getMenu().add(Menu.NONE, 1, 1 , "Descending");
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            //handle item clicks
             int id = menuItem.getItemId();
             if (id == 0){
                 Collections.sort(statArrayList, new SortStatCountryAsc());
                 adapterStat.notifyDataSetChanged();
             }else if (id == 0){
                 Collections.sort(statArrayList, new SorrtStatCountryDesc());
                 adapterStat.notifyDataSetChanged();
             }
            return false;
        });

        //sort
        sortBtn.setOnClickListener(view1 ->{
            popupMenu.show();
        });
        loadStatsData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStatsData();
    }

    private void loadStatsData(){
        progressBar.setVisibility(View.VISIBLE);

        //api call
        StringRequest stringRequest = new StringRequest(Request.Method.GET, STATS_URL, this::handleResponse, error -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        //add request to queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void handleResponse(String response) {
        statArrayList = new ArrayList<>();
        statArrayList.clear();

        try {
            //json object as response
            JSONObject jsonObject =  new JSONObject(response);
            //and then array of record
            JSONArray jsonArray = jsonObject.getJSONArray("Countries");

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("dd/MM/yyyy hh:mm a");
            Gson gson = gsonBuilder.create();

            //start getting data
            for (int i=0; i<jsonArray.length(); i++){
                ModelStat modelStat = gson.fromJson(jsonArray.getJSONObject(i).toString(), ModelStat.class);
                statArrayList.add(modelStat);
            }

            //setup adapter
            adapterStat = new AdapterStat(context, statArrayList);
            statsRv.setAdapter(adapterStat);//set adapter to rv

            progressBar.setVisibility(View.GONE);
        }catch (Exception e){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

//    sort countries as ascending order
    public class SortStatCountryAsc implements Comparator<ModelStat>{
        @Override
        public int compare(ModelStat left, ModelStat right) {

            return left.getCountry().compareTo(right.getCountry());
        }
    }
    //sort countries as descending order
    public class SorrtStatCountryDesc implements Comparator<ModelStat>{

        @Override
        public int compare(ModelStat left, ModelStat right) {
            return right.getCountry().compareTo(left.getCountry());
        }
    }
}