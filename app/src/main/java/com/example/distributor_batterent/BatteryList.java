package com.example.distributor_batterent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.distributor_batterent.Adapter.BatteryAdapter;
import com.example.distributor_batterent.Model.BatteryModel;

import java.util.ArrayList;
import java.util.List;

public class BatteryList extends AppCompatActivity {
    RecyclerView recyclerView;
    List<BatteryModel> list = new ArrayList<>();
    BatteryAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setdata();
        setContentView(R.layout.battery_list);
        recyclerView = findViewById(R.id.batteryList);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new BatteryAdapter(BatteryList.this,list);
        recyclerView.setAdapter(adapter);
    }


    private void setdata() {

        list.add(new BatteryModel("A1B", "R.drawable.ba1"));
        list.add(new BatteryModel("A2B", "R.drawable.ba2"));
        list.add(new BatteryModel("A3B", "R.drawable.ba3"));
        list.add(new BatteryModel("A4B", "R.drawable.ba4"));
        list.add(new BatteryModel("A5B", "R.drawable.ba5"));
        list.add(new BatteryModel("A6B", "R.drawable.ba6"));
        list.add(new BatteryModel("A7B", "R.drawable.ba7"));
        list.add(new BatteryModel("A8B", "R.drawable.ba8"));
        list.add(new BatteryModel("A9B", "R.drawable.ba9"));
        list.add(new BatteryModel("A10B", "R.drawable.ba10"));
        list.add(new BatteryModel("A11B", "R.drawable.ba11"));
        list.add(new BatteryModel("A12B", "R.drawable.ba12"));
        list.add(new BatteryModel("A13B", "R.drawable.ba13"));





    }
}
