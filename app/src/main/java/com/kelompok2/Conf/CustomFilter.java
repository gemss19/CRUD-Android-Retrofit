package com.kelompok2.Conf;

import android.widget.Filter;

import java.util.ArrayList;

public class CustomFilter extends Filter {

    Adapter adapter;
    ArrayList<Pegawai> filterList;

    public CustomFilter(ArrayList<Pegawai> filterList, Adapter adapter) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if (constraint != null && constraint.length() > 0) {
            //CHANGE TO UPPER
            constraint = constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<Pegawai> filteredPgw = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {
                //CHECK
                if (filterList.get(i).getName().toUpperCase().contains(constraint)){
                    //ADD TO FILTER
                    filteredPgw.add(filterList.get(i));
                }
            }

            results.count = filteredPgw.size();
            results.values = filteredPgw;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.pgws = (ArrayList<Pegawai>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }

}
