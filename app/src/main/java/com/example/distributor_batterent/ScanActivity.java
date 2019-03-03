package com.example.distributor_batterent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.distributor_batterent.Model.Data;
import com.example.distributor_batterent.Model.NotificationData;
import com.example.distributor_batterent.Model.ResponseClass;
import com.example.distributor_batterent.Util.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;
import com.marcoscg.dialogsheet.DialogSheet;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ScanActivity extends BaseScannerActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    Call<String> sendStatus;
    FCMService service;
    DatabaseReference reference;
    private static final String TAG = "ScanActivity";
    Call<ResponseClass> notify;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);

        reference = FirebaseDatabase.getInstance().getReference("number");
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://fcm.googleapis.com/fcm/").build();
        service = retrofit.create(FCMService.class);



    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Common.productNumber = rawResult.getText();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScanActivity.this);
            }
        }, 2000);
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(ScanActivity.this, notification);
            r.play();
        } catch (Exception e) {
        }


        final DialogSheet dialogSheet = new DialogSheet(this);
        dialogSheet.setTitle("Confirmation !");
        dialogSheet.setMessage("Product number " + rawResult.getText() + " is alloted to " + Common.recievedOrder.getUserName()
                + " with UID (" + Common.recievedOrder.getUserId() + ") for " + Common.recievedOrder.getMonth() + " month ."
                + "\n" + "Transaction ID: " + Common.recievedOrder.getOrderId() + "\n" +
                "Amount : " + Common.recievedOrder.getAmount()
                + "\n" + "Ship to : " + Common.recievedOrder.getAddress());
        dialogSheet.setColoredNavigationBar(true);
        dialogSheet.setCancelable(true);

        Data data = new Data("Hello "+Common.recievedOrder.getUserName(), "You have been alloted battery with Serial Number "+ rawResult.getText());

        notify = service.sendNotification(new NotificationData(data, "dI1Bpg2uAsU:APA91bH7Jrvk2DOB8_zdfdDCfpII6at0zVQgJxwlPQRPqQ93q6Ina08txkiPUgwoQ6lb5MmGOyjseMvF6jk83aseCpNBDSPRoO-NtE7Hoy547QdibFiVVU0gEmNdfPninX4kEg4LHBcx"));



        dialogSheet.setPositiveButton("Confirm", new DialogSheet.OnPositiveClickListener() {
            @Override
            public void onClick(View v) {
                // Your action

                sendYesToCloud();


            }
        });
        dialogSheet.setNegativeButton("Change Battery", new DialogSheet.OnNegativeClickListener() {
            @Override
            public void onClick(View v) {
                // Your action
                sendMessage();
               // dialogSheet.dismiss();
            }
        });
        dialogSheet.setRoundedCorners(true);
        dialogSheet.setBackgroundColor(Color.WHITE); // Your custom background color
        dialogSheet.setButtonsColorRes(R.color.colorPrimaryDark);  // Default color is accent
        dialogSheet.show();
    }

    private void sendYesToCloud() {

        reference.setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                notify.enqueue(new Callback<ResponseClass>() {
                    @Override
                    public void onResponse(Call<ResponseClass> call, Response<ResponseClass> response) {
                        startActivity(new Intent(ScanActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseClass> call, Throwable t) {

                    }
                });
            }
        });


    }

    private void sendMessage() {
        final ProgressDialog dialog = new ProgressDialog(ScanActivity.this);
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("hritikgupta722@gmail.com", "hritikgupta@722");
                    sender.sendMail("EmailSender App",
                            "I am Smart.",
                            "hritikgupta722@gmail.com",
                            "hritikgupta723@gmail.com");
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }

}
