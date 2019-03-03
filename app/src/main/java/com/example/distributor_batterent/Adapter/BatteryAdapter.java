package com.example.distributor_batterent.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.distributor_batterent.Model.BatteryModel;
import com.example.distributor_batterent.R;

import java.util.List;

public class BatteryAdapter extends RecyclerView.Adapter<BatteryAdapter.ViewHolder> {

    Context context;
    List<BatteryModel> batteryModelList;
    private static final String TAG = "BatteryAdapter";

    int imageResource[] = {R.drawable.ba1,R.drawable.ba2,R.drawable.ba3,R.drawable.ba4,R.drawable.ba5,R.drawable.ba6,R.drawable.ba7,
                            R.drawable.ba8,R.drawable.ba9,R.drawable.ba10,R.drawable.ba11,R.drawable.ba12,R.drawable.ba13};

    public BatteryAdapter(Context context, List<BatteryModel> batteryModelList) {
        this.context = context;
        this.batteryModelList = batteryModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.battery_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.textView.setText(batteryModelList.get(i).getBatteryName());
        viewHolder.imageView.setImageResource(imageResource[i]);

    }
    
    @Override
    public int getItemCount() {
        return batteryModelList.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView textView;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.nameBattery);
            imageView = (ImageView) itemView.findViewById(R.id.imageBattery);
        }

    }


}
