package com.android.barcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Bottom_Sheet_Screen extends AppCompatActivity {
    BottomNavigationView navigation;
    ImageView img_logout;
    private FirebaseAuth auth;
    TextView txt_title;
    String user_email;
    FirebaseFirestore db;
    ArrayList<String> aray_emails = new ArrayList<>();
    public static String str_Username;
    String str_val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom__sheet__screen);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_Book);
        txt_title = findViewById(R.id.txt_title);

        MyPreferences.init(getApplicationContext());
        str_val = MyPreferences.getInstance().getsUser_Email();


//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();

      //  if (bundle != null) {
          //  str_val = bundle.getString("user_email");
            db = FirebaseFirestore.getInstance();

            db.collection("user")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("Doccc", document.getId() + " => " + document.getData());

                                   String email = String.valueOf(document.get("email"));

                                    aray_emails.add(email);

                                    if (str_val.equals(email)){
                                        Log.d("emailllll:", "onComplete:"+email);
                                        str_Username = String.valueOf(document.get("name"));
                                        Log.d("User_name::", "onComplete:"+str_Username);
                                        MyPreferences.getInstance().setsUser_Fullname(str_Username);

                                    }

                                    else {
//                                        Toast.makeText(Get_FB_Data.this, "Not Match", Toast.LENGTH_SHORT).show();
                                        // onBackPressed();
                                    }

                                }

                            } else {
                                Log.w("Erroorrr:", "Error getting documents.", task.getException());
                            }
                        }
                    });


    //    }


        loadFragment(new Frag_Main());

        img_logout = findViewById(R.id.img_logout);

        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth = FirebaseAuth.getInstance();
                if (auth.getCurrentUser() != null) {
                    // User is logged in
                    auth.signOut();
                    startActivity(new Intent(Bottom_Sheet_Screen.this, LoginActivity.class));
                    finish();
                }
            }
        });


        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_Book:
                        txt_title.setText("Scan");
                        loadFragment(new Frag_Main());
                        break;
                    case R.id.navigation_Profile:
                        txt_title.setText("History");
                        loadFragment(new Frag_History());
                        break;
                    case R.id.navigation_Bookings:
                        txt_title.setText("Contact");
                        loadFragment(new Frag_Contact());
                        break;
                    case R.id.navigation_Settings:
                        txt_title.setText("Search Medicines");
                        loadFragment(new Search_Medicines());

                        break;
                    case R.id.navigation_User_Profile:
                        txt_title.setText("User Profile");
                        loadFragment(new Frag_Profile());

                        break;
                }
                return true;
            }
        });

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
