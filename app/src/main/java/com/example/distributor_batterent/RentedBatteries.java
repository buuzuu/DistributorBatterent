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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.distributor_batterent.Model.Credentials;
import com.example.distributor_batterent.Model.Order;
import com.example.distributor_batterent.Util.Common;
import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.victor.ringbutton.RingButton;

import java.util.Iterator;

import ng.max.slideview.SlideView;

public class RentedBatteries extends Fragment {

    RingButton ringButton;
    SlideView slideView;
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    View view ;
    TextView schemeName ,customerName
            ,userId
            ,nice_spinner
            ,month,paidAmount
            ,address
            ,shipAddress;
    DatabaseReference reference,reference2;
    ImageView imageView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.battery_rented,container,false);
        ringButton =view.findViewById(R.id.ringButton);
        slideView = view.findViewById(R.id.acceptReq);
        customerName = view.findViewById(R.id.customerName);
        paidAmount = view.findViewById(R.id.paidAmount);
        shipAddress = view.findViewById(R.id.shipAddress);
        month = view.findViewById(R.id.month);
        address = view.findViewById(R.id.address);
        schemeName = view.findViewById(R.id.scheme_name);
        userId = view.findViewById(R.id.userId);
        imageView = view.findViewById(R.id.rcImage);
        ringButton.setOnClickListener(new RingButton.OnClickListener() {
            @Override
            public void clickUp() {

                startActivity(new Intent(getActivity(),MyRentedBatteryList.class));

               // startActivity(new Intent(getActivity(),RentedList.class));
            }

            @Override
            public void clickDown() {

                startActivity(new Intent(getActivity(),BatteryList.class));

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
        reference2 = FirebaseDatabase.getInstance().getReference("credentials");
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

                        schemeName.setText(Common.recievedOrder.getProduct());
                        customerName.setText(Common.recievedOrder.getUserName());

                        userId.setText(Common.recievedOrder.getUserId());
                        month.setText("2 ");
                        paidAmount.setText(Common.recievedOrder.getAmount());
                        address.setText("Transaction Number: "+Common.recievedOrder.getOrderId());
                        shipAddress.setText("Ship to: "+Common.recievedOrder.getAddress());


                        setUpRC(Common.recievedOrder.getUserId());

                    }else {
                        customerName.setVisibility(View.INVISIBLE);
                        userId.setVisibility(View.INVISIBLE);
                      //  nice_spinner.setVisibility(View.INVISIBLE);
                        paidAmount.setVisibility(View.INVISIBLE);
                        address.setVisibility(View.INVISIBLE);
                        shipAddress.setVisibility(View.INVISIBLE);
                        schemeName.setTextSize(35);
                        month.setText("");
                        schemeName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        schemeName.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        schemeName.setText("Sorry"+"\n"+"No order available to show.");
                        customerName.setVisibility(View.INVISIBLE);

                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setUpRC(String userId) {

        reference2.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    Credentials credentials = dataSnapshot.getValue(Credentials.class);
                    Picasso.with(getActivity()).load(credentials.getRcURL()).centerCrop().fit()
                                .into(imageView);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
