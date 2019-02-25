package com.example.distributor_batterent;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.distributor_batterent.Model.Distributor;
import com.example.distributor_batterent.Util.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {

    EditText edtNewUser, edtNewFirstName, edtNewLastName, edtNewPassword, edtNewEmail, edtNewPhone, edtNewOTP; //for sign up
    FirebaseDatabase database;
    DatabaseReference distributors;
    EditText edtUser,edtPassword;
    private ProgressBar progressBar;
    Button getOTP;
    AlertDialog dialog;
    boolean check = false;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = FirebaseDatabase.getInstance();
        distributors = database.getReference("Distributors");
        edtUser=findViewById(R.id.edtUser);
        edtPassword=findViewById(R.id.edtPassword);


        findViewById(R.id.log_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUser.getText().length()>0&&edtPassword.getText().length()>0){
                    signIn(edtUser.getText().toString(), edtPassword.getText().toString());

                }else {
                    Toast.makeText(LoginActivity.this, "Enter data properly.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.log_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp();
            }
        });




    }

    private void showPopUp() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
        //     alertDialog.setTitle("Sign Up");
        //     alertDialog.setMessage("Please fill full information");
        checkforRegisteredNumber();
        LayoutInflater inflater = this.getLayoutInflater();
        final View sign_up_layout = inflater.inflate(R.layout.signup_layout, null);
        edtNewUser = sign_up_layout.findViewById(R.id.edtNewUserName);
        edtNewPassword = sign_up_layout.findViewById(R.id.edtNewPassword);
        edtNewEmail = sign_up_layout.findViewById(R.id.edtNewEmail);
        edtNewFirstName = sign_up_layout.findViewById(R.id.edtNewFirstName);
        edtNewLastName = sign_up_layout.findViewById(R.id.edtNewLastName);
        edtNewPhone = sign_up_layout.findViewById(R.id.editNewPhone);
        edtNewOTP = sign_up_layout.findViewById(R.id.edtNewOTP);
        progressBar = sign_up_layout.findViewById(R.id.progressBar2);
        getOTP = sign_up_layout.findViewById(R.id.getOTP);
        alertDialog.setView(sign_up_layout);
        alertDialog.setCancelable(true);
        dialog = alertDialog.create();
        dialog.show();
        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.setCancelable(false);


                for (int i = 0; i < Common.registederPhone.size(); i++) {

                    Log.d(TAG, "onDataChange: Test two..." + Common.registederPhone.get(i) + "---" + Common.registederUsername.get(i));

                    if (edtNewPhone.getText().toString().equals(Common.registederPhone.get(i)) || edtNewUser.getText().toString().equals(Common.registederUsername.get(i))) {
                        check = true;
                    }

                }

                if (check == true) {
                    Snackbar.make(sign_up_layout, "Phone Number Or Username Already Registered !", Snackbar.LENGTH_LONG).show();
                    Toast.makeText(LoginActivity.this, "Phone Number Or Username Already Registered !", Toast.LENGTH_SHORT).show();
                    check = false;
                    Common.registederPhone.clear();
                    Common.registederUsername.clear();
                    dialog.dismiss();
                } else if (check == false) {
                    Common.registederPhone.clear();
                    Common.registederUsername.clear();
                    createAccount();
                    alertDialog.setCancelable(false);

                    progressBar.setVisibility(View.VISIBLE);

                }



            }
        });



    }

    private void createAccount() {

        final Distributor distributor = new Distributor(edtNewUser.getText().toString(), edtNewPassword.getText().toString(), edtNewEmail.getText().toString()

                , edtNewFirstName.getText().toString(), edtNewLastName.getText().toString(), edtNewPhone.getText().toString(),edtNewOTP.getText().toString());

        getOTP.setBackgroundColor(Color.parseColor("#4264fb"));
        getOTP.setTextColor(Color.WHITE);
        getOTP.setText("Registered !!!");
        progressBar.setVisibility(View.INVISIBLE);

        distributors.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(distributor.getUserName()).exists()) {
                    Toast.makeText(LoginActivity.this, "Username Already exists !", Toast.LENGTH_SHORT).show();
                } else {
                    distributors.child(distributor.getUserName()).setValue(distributor);

                    Toast.makeText(LoginActivity.this, "Registration Successful !", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 1500);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void checkforRegisteredNumber() {

        distributors.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {


                    Log.d(TAG, "onDataChange: " + dataSnapshot.getChildrenCount());
                    Iterator<DataSnapshot> iterable = dataSnapshot.getChildren().iterator();

                    while (iterable.hasNext()) {


                        DataSnapshot tempItem = iterable.next();
                        Common.registederPhone.add(tempItem.child("phoneNumber").getValue().toString());
                        Common.registederUsername.add(tempItem.child("userName").getValue().toString());
                    }

                } else {
                    check = false;
                }


                for (int i = 0; i < Common.registederPhone.size(); i++) {
                    Log.d(TAG, "onDataChange: Test one...." + Common.registederPhone.get(i) + "-----" + Common.registederUsername.get(i));
                }
                Log.d(TAG, "onDataChange: Test one..." + check);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void signIn(final String user, final String password) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Logging in...");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);


        distributors.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user).exists()) {

                    if (!user.isEmpty()) {

                        Distributor login = dataSnapshot.child(user).getValue(Distributor.class);

                        if (login.getPassword().equals(password)) {

                            Common.currentDistributor = login;

                            dialog.dismiss();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                            finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Please enter your user name", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "User does not exist ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
