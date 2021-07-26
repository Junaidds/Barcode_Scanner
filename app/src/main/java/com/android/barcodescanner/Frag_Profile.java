package com.android.barcodescanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.android.barcodescanner.Bottom_Sheet_Screen.str_Username;

public class Frag_Profile extends Fragment {

    TextView txt_name,txt_email,txt_contact,txt_country;

    FirebaseFirestore db;
    String email;

    ArrayList<String> aray_emails = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootview = inflater.inflate(R.layout.fragment_frag__profile, container, false);

        txt_name = rootview.findViewById(R.id.txt_name);
        txt_email = rootview.findViewById(R.id.txt_email);
        txt_contact = rootview.findViewById(R.id.txt_contact);
        txt_country = rootview.findViewById(R.id.txt_country);


        MyPreferences.init(getContext());

        email = MyPreferences.getInstance().getsUser_Email();

        db = FirebaseFirestore.getInstance();

        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Doccc", document.getId() + " => " + document.getData());

                               String str_email = String.valueOf(document.get("email"));

                                aray_emails.add(str_email);

                                if (email.equals(str_email)){
                                    Log.d("email:", "onComplete:"+email);
                                    String s_email = String.valueOf(document.get("email"));
                                    String userName = String.valueOf(document.get("name"));
                                   String contact = String.valueOf(document.get("phone_no"));
                                   String country = String.valueOf(document.get("country"));

                                    txt_name.setText(userName);
                                    txt_email.setText(s_email);
                                    txt_contact.setText(contact);
                                    txt_country.setText(country);

                                }

                                else {
                                 //   Toast.makeText(getContext(), "No user", Toast.LENGTH_SHORT).show();
                                }

                            }

                        } else {
                            Log.w("Erroorrr:", "Error getting documents.", task.getException());
                        }
                    }
                });


        return rootview;

    }
}
