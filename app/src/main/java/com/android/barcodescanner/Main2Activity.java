package com.android.barcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main2Activity extends Bottom_Sheet_Screen {

    ImageView img_logout;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_main2, contentFrameLayout);
        // setContentView(R.layout.activity_main2);

        CardView crd_scan = findViewById(R.id.crd_scan);
        img_logout = findViewById(R.id.img_logout);

        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth = FirebaseAuth.getInstance();
                if (auth.getCurrentUser() != null) {
                    // User is logged in
                    auth.signOut();
                    startActivity(new Intent(Main2Activity.this, LoginActivity.class));
                    finish();

                }


            }
        });

        crd_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
