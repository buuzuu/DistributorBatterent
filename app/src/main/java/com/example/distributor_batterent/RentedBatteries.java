package com.example.distributor_batterent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.distributor_batterent.Model.Order;
import com.example.distributor_batterent.Util.Common;
import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.victor.ringbutton.RingButton;

import java.util.Iterator;

import ng.max.slideview.SlideView;

public class RentedBatteries extends Fragment {

    RingButton ringButton;
    SlideView slideView;
    private static final int ZBAR_CAMERA_PERMISSION = 1;

    View view ;
    DatabaseReference reference;
    TextView requestMsg,orderId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.battery_rented,container,false);
        ringButton =view.findViewById(R.id.ringButton);
        slideView = view.findViewById(R.id.acceptReq);
        requestMsg = view.findViewById(R.id.requestMsg);
        orderId = view.findViewById(R.id.orderId);
        ringButton.setOnClickListener(new RingButton.OnClickListener() {
            @Override
            public void clickUp() {
                startActivity(new Intent(getActivity(),RentedList.class));
            }

            @Override
            public void clickDown() {
            }
        });


        slideView.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {

                getPermission();


            }
        });

        return view;

    }

    private void getPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            startActivity(new Intent(getActivity(),ScanActivity.class));


        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressLoadingJIGB.startLoadingJIGB(getActivity(),R.raw.waiting,"Waiting for Incoming Request...",0,500,500);
        reference = FirebaseDatabase.getInstance().getReference("Orders");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressLoadingJIGB.finishLoadingJIGB(getActivity());
            }
        },3000);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        Iterator<DataSnapshot> iterable = dataSnapshot.getChildren().iterator();
                        while (iterable.hasNext()){
                            DataSnapshot tempItem = iterable.next();
                            Common.recievedOrder =tempItem.getValue(Order.class);
                        }

                        requestMsg.setTextSize(20);
                        requestMsg.setText(Common.recievedOrder.getUserName()+
                                            " with UID "+Common.recievedOrder.getUserId()+" paid ₹"
                                            +Common.recievedOrder.getAmount()+" for battery with model number "+
                                            Common.recievedOrder.getProduct()+" for "+Common.recievedOrder.getMonth()+" month to be shipped at "
                                            +Common.recievedOrder.getAddress());
                        orderId.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        orderId.setText("Transaction Number: "+Common.recievedOrder.getOrderId());






                    }else {
                        requestMsg.setTextSize(40);
                        requestMsg.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        requestMsg.setText("Sorry");
                        orderId.setText("No order available to show.");
                    }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
