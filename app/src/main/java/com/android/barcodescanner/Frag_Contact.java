package com.android.barcodescanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.barcodescanner.Mail_Helper.GMailSender;

import java.security.AccessController;

public class Frag_Contact extends Fragment implements View.OnClickListener {

    EditText ed_email,ed_msg;
    CardView crd_send;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootview = inflater.inflate(R.layout.fragment_frag__contact, container, false);
        ed_email = rootview.findViewById(R.id.ed_email);
        ed_msg = rootview.findViewById(R.id.ed_msg);
        crd_send = rootview.findViewById(R.id.crd_send);

        crd_send.setOnClickListener(this);


        return rootview;
    }

    @Override
    public void onClick(View view) {
        if (view == crd_send){
            sendEmail();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    /*private void sendMessage() {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("mna25867@gmail.com", "526tayy2222");
                    sender.sendMail("EmailSender App",
                            ed_msg.getText().toString(),
                            "mna25867@gmail.com",
                            ed_email.getText().toString());
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }*/

        private void sendEmail() {
            //Getting content for email
            String email = ed_email.getText().toString().trim();
            String subject = ed_email.getText().toString().trim();
            String message = ed_msg.getText().toString().trim();

            //Creating SendMail object
            GMailSender sm = new GMailSender(getContext(), email, subject, message);

            //Executing sendmail to send email
            sm.execute();
        }
    }
