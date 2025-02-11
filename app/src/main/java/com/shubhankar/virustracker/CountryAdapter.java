package com.shubhankar.virustracker;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> implements Filterable {
    public Context context;
    public static class CountryViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout containerView;
        public TextView textView;
        public ImageView flag;
        
        CountryViewHolder(View view) {
            super(view);
            
            containerView = view.findViewById(R.id.country_row);
            textView = view.findViewById(R.id.country_row_text_view);
            flag = view.findViewById(R.id.imageFlag);
            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), CountryActivity.class);
                    intent.putExtra("position", getAdapterPosition());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
    private static List<Country> countryList = new ArrayList<>();
    private RequestQueue requestQueue;
    public static List<Country> filtered = countryList;
    private List<Country> filteredCountry;

    CountryAdapter(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        this.context = context;
        fetchdata();
    }
    @Override
    public Filter getFilter() {
        return new CountryFilter();
    }
    private class CountryFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredCountry = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredCountry.addAll(countryList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Country item : countryList) {
                    if (item.getCountry().toLowerCase().contains(filterPattern)) {
                        filteredCountry.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredCountry; // you need to create this variable!
            results.count = filteredCountry.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered = (List<Country>) results.values;
            notifyDataSetChanged();
        }

    }
    public void fetchdata(){
        String url  = "https://corona.lmao.ninja/v2/countries/";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String countryName = jsonObject.getString("country");
                                String cases = jsonObject.getString("cases");
                                String todayCases = jsonObject.getString("todayCases");
                                String deaths = jsonObject.getString("deaths");
                                String todayDeaths = jsonObject.getString("todayDeaths");
                                String recovered = jsonObject.getString("recovered");
                                String active = jsonObject.getString("active");
                                String critical = jsonObject.getString("critical");

                                JSONObject object = jsonObject.getJSONObject("countryInfo");
                                String flagUrl = object.getString("flag");

                                Country countryModel = new Country(flagUrl,countryName,cases,todayCases,deaths,todayDeaths,recovered,active,critical);
                                countryList.add(countryModel);


                            }
                            notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e("API", "data load error", e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API", "API error", error);

            }
        });
        requestQueue.add(request);
    }
    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_row, parent, false);
        
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Country current = filtered.get(position);
        holder.textView.setText(current.getCountry());
        Glide.with(context).load(current.getFlag()).into(holder.flag);
        holder.containerView.setTag(current);
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }
}
