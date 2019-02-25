package com.example.distributor_batterent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.distributor_batterent.Util.Common;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RentedList extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    TextView modelNumber,productNumber,userName,productAmount,userAddress;
    private GoogleMap mMap;
    Button track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented_list);
        track=findViewById(R.id.track);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        modelNumber=findViewById(R.id.modelNumber);
        productNumber=findViewById(R.id.productNumber);
        userName=findViewById(R.id.userName);
        productAmount=findViewById(R.id.productAmount);
        userAddress=findViewById(R.id.userAddress);

        modelNumber.setText( "Model "+Common.recievedOrder.getProduct());
        productAmount.setText("Amount "+Common.recievedOrder.getAmount());
        productNumber.setText("ProductNumber "+Common.productNumber);
        userName.setText("Name "+Common.recievedOrder.getUserName());
        userAddress.setText("Address "+Common.recievedOrder.getAddress());
        track.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        30.704649
//        76.717873
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.track){
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(new LatLng(30.704,76.717));
            circleOptions.fillColor(R.color.actionbar_opacity);
            circleOptions.radius(100);
            mMap.addMarker(new MarkerOptions().position(new LatLng(30.704,76.717)).title("Your Battery is Here"));
            mMap.addCircle(circleOptions);

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.704,76.717),15f));

        }
    }
}
