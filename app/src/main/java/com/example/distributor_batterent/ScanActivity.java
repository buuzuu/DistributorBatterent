package com.example.distributor_batterent;

import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.example.distributor_batterent.Services.StatusService;
import com.example.distributor_batterent.Util.Common;
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
    private static final String TAG = "ScanActivity";
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);
        setupToolbar();
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://us-central1-batterent-810a3.cloudfunctions.net/").build();
        StatusService statusService = retrofit.create(StatusService.class);
         sendStatus = statusService.allowBattery("true");
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
        } catch (Exception e) {}


       final DialogSheet dialogSheet = new DialogSheet(this);
                dialogSheet.setTitle("Confirmation !");
                   dialogSheet .setMessage("Product number "+rawResult.getText()+" is alloted to "+ Common.recievedOrder.getUserName()
                            +" with UID ("+Common.recievedOrder.getUserId()+") for "+Common.recievedOrder.getMonth()+" month ."
                            + "\n"+"Transaction ID: "+Common.recievedOrder.getOrderId()+"\n"+
                                "Amount : "+Common.recievedOrder.getAmount()
                            +"\n"+"Ship to : "+Common.recievedOrder.getAddress());
                dialogSheet.setColoredNavigationBar(true);
                dialogSheet.setCancelable(false);
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
                        dialogSheet.dismiss();
                    }
                });
                dialogSheet.setRoundedCorners(true);
                dialogSheet.setBackgroundColor(Color.WHITE); // Your custom background color
                dialogSheet.setButtonsColorRes(R.color.colorPrimaryDark);  // Default color is accent
                dialogSheet.show();
    }

    private void sendYesToCloud() {

        sendStatus.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: "+response.toString());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });

        startActivity(new Intent(ScanActivity.this,MainActivity.class));
        finish();




    }
}
