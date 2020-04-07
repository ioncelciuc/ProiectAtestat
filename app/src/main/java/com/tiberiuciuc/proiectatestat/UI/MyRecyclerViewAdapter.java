package com.tiberiuciuc.proiectatestat.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tiberiuciuc.proiectatestat.Model.EarthQuake;
import com.tiberiuciuc.proiectatestat.R;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private List<EarthQuake> quakeList;
    private Context context;

    public MyRecyclerViewAdapter(Context context, List<EarthQuake> quakeList) {
        this.context = context;
        this.quakeList = quakeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EarthQuake earthQuake = quakeList.get(position);
        holder.place.setText(earthQuake.getPlace());
        holder.magnitude.setText(String.valueOf(earthQuake.getMagnitude()));
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(earthQuake.getTime()).getTime());
        holder.date.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return (quakeList != null && quakeList.size() != 0) ? quakeList.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView magnitude;
        TextView place;
        TextView date;

        public MyViewHolder(View view) {
            super(view);
            this.magnitude = view.findViewById(R.id.earthquake_magnitude);
            this.place = view.findViewById(R.id.earthquake_place);
            this.date = view.findViewById(R.id.earthquake_date);
        }
    }

    public EarthQuake getEarthquake(int position) {
        return (quakeList != null && quakeList.size() > 0) ? quakeList.get(position) : null;
    }
}
