package com.nessy.coronaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterStat extends RecyclerView.Adapter<AdapterStat.HolderStat> implements Filterable {
    private Context context;
    public ArrayList<ModelStat> statArrayList, filterList;
    private FilterStat filterStat;
    //UI Views


    public AdapterStat(Context context, ArrayList<ModelStat> statArrayList) {
        this.context = context;
        this.statArrayList = statArrayList;
        this.filterList = statArrayList;
    }

    @NonNull
    @Override
    public HolderStat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_stat.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_stat, parent, false);

        return new HolderStat(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderStat holder, int position) {
        //get data
        ModelStat modelStat =  statArrayList.get(position);

        String country = modelStat.getCountry();
        String totalConfirmed = modelStat.getTotalConfirmed();
        String totalDeaths = modelStat.getTotalDeaths();
        String totalRecovered = modelStat.getTotalRecovered();
        String newConfirmed = modelStat.getNewConfirmed();
        String newDeaths = modelStat.getNewDeaths();
        String newRecovered = modelStat.getNewRecovered();

        //set data
        holder.countryTv.setText(country);
        holder.todayRecoveredTv.setText(newRecovered);
        holder.todayDeathsTv.setText(newDeaths);
        holder.todayCasesTv.setText(newConfirmed);
        holder.recoveredTv.setText(totalRecovered);
        holder.deathsTv.setText(totalDeaths);
        holder.casesTv.setText(totalConfirmed);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public Filter getFilter() {
        if (filterStat == null){
            filterStat= new FilterStat(this, filterList);
        }
        return filterStat;
    }

    class HolderStat extends RecyclerView.ViewHolder {
        TextView countryTv, casesTv, todayCasesTv, deathsTv, todayDeathsTv, recoveredTv, todayRecoveredTv;

        public HolderStat(@NonNull View itemView) {
            super(itemView);

            //init UI Views
            countryTv = itemView.findViewById(R.id.countryTv);
            casesTv = itemView.findViewById(R.id.casesTv);
            todayCasesTv = itemView.findViewById(R.id.todayCasesTv);
            deathsTv = itemView.findViewById(R.id.deathsTv);
            todayDeathsTv = itemView.findViewById(R.id.todayDeathsTv);
            recoveredTv = itemView.findViewById(R.id.recoveredTv);
            todayRecoveredTv = itemView.findViewById(R.id.todayRecoveredTv);
        }
    }

}

