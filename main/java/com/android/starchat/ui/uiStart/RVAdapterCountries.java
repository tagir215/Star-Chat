package com.android.starchat.ui.uiStart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.starchat.R;

public class RVAdapterCountries extends RecyclerView.Adapter<RVAdapterCountries.CountryHolder> {

    private final LayoutInflater inflater;
    private final Country[] countries;
    public RVAdapterCountries(Context context, Country[] countries) {
        inflater = LayoutInflater.from(context);
        this.countries = countries;
    }

    @NonNull
    @Override
    public RVAdapterCountries.CountryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.l_countries,parent,false);
        return new CountryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapterCountries.CountryHolder holder, int position) {
        holder.name.setText(countries[position].getName());
        holder.code.setText(countries[position].getCode());
        holder.region.setText("("+countries[position].getRegion()+")");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countries[holder.getAdapterPosition()].getAction().setCountry();
            }
        });
    }

    @Override
    public int getItemCount() {
        return countries.length;
    }

    protected class CountryHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView code;
        TextView region;
        ConstraintLayout layout;

        public CountryHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.countryName);
            code = itemView.findViewById(R.id.countryCode);
            region = itemView.findViewById(R.id.countryRegion);
            layout = itemView.findViewById(R.id.countriesLayout);
        }
    }
}
