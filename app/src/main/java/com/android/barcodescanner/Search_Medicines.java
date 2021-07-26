package com.android.barcodescanner;

import android.database.Cursor;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.barcodescanner.Model.Model_Items;
import com.android.barcodescanner.Model.Model_Sypmtoms;
import com.android.barcodescanner.Sqlite_Db.Constants;
import com.android.barcodescanner.Sqlite_Db.Items_Db;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static com.android.barcodescanner.Sqlite_Db.Constants.DATABASE.bar_code;

public class Search_Medicines extends Fragment implements View.OnClickListener {

    CardView crd_search;
    TextView txt_relatedMedicines;
    EditText ed_search;
    static Items_Db mDatabase;
    List<Model_Sypmtoms> model_sypmtoms = new ArrayList<>();
    String str_symptom;
    ArrayList<String> arry_lst = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootview = inflater.inflate(R.layout.fragment_search__medicines, container, false);

        crd_search = rootview.findViewById(R.id.crd_search);
        txt_relatedMedicines = rootview.findViewById(R.id.txt_relatedMedicines);
        ed_search = rootview.findViewById(R.id.ed_search);

        /*Initialize database*/
        mDatabase = new Items_Db(getContext());

        /*Delete Medicines from Database*/
        mDatabase.delete_Medicines();


        /*Insert Records in Medicine table*/
        mDatabase.add_syptoms("Headache", "","Migraine & Headache Medicines - Drugs for Headache Pain Relief, Generic Name Brand Name, Aspirin Bayer, Bufferin, Ecotrin, Fenoprofen, Nalfon, Flurbiprofen, Ansaid, Ibuprofen, Advil, otrin IB, Nuprin, ");
        mDatabase.add_syptoms("Cough", "","List of affected medicines, Brand Name, Ingredient intended to treat cold symptoms, Robitussin Cough & Chest Congestion, guaifenesin dextromethorphan, Robitussin DMP Extra Strength, pseudoephedrine dextromethorphan, Robitussin Dry Cough, dextromethorphan, Robitussin Dry Cough Forte, dextromethorphan");
        mDatabase.add_syptoms("Fever", "temperature","Medications for Fever \nParacetamol, acetaminophen, Tylenol, \nibuprofen, Advil, aspirin, Motrin, naproxen, \tAleve");

        mDatabase.add_syptoms("Hepatitis", "","A Full List of Hepatitis C Medications: \nEpclusa, Harvoni, Zepatier, and More\n" +
                "Ribavirin\n" +
                "Direct-acting antivirals\n" +
                "Combination drugs\n" +
                "Harvoni\n" +
                "Viekira Pak \n" +
                "Hepatitis C virus (HCV) infection causes liver inflammation that can lead to liver problems, including cancer. People who have chronic hepatitis C need medication to treat it. These drugs can ease symptoms of HCV.");

        mDatabase.add_syptoms("Corona", "","Key takeaways:\n" +
                "\n" +
                "There are no approved coronavirus treatments at this time.\n" +
                "The drug that’s furthest along in clinical trials for treating COVID-19 is remdesivir, a new intravenous antiviral that the FDA has not yet approved, though they did grant an emergency use authorization for it to make it more accessible.\n" +
                "Researchers are also testing older medications (that are typically used to treat other conditions) to see if they are also effective in treating COVID-19.\n"
        );

        mDatabase.add_syptoms("Paracetamol","","NSAIDs such as ibuprofen, naproxen, and diclofenac are more effective than paracetamol for controlling dental pain or pain arising from dental procedures; combinations of NSAIDs and acetaminophen are more effective than either alone");

        mDatabase.add_syptoms("Covid-19","","Key takeaways:\n" +
                "\n" +
                "There are no approved coronavirus treatments at this time.\n" +
                "The drug that’s furthest along in clinical trials for treating COVID-19 is remdesivir, a new intravenous antiviral that the FDA has not yet approved, though they did grant an emergency use authorization for it to make it more accessible.\n" +
                "Researchers are also testing older medications (that are typically used to treat other conditions) to see if they are also effective in treating COVID-19.\n"
        );


        crd_search.setOnClickListener(this);


        return rootview;

    }

    @Override
    public void onClick(View view) {
        if (view == crd_search) {

            get_Medicines();
        }
    }

    /*Fetch Medicines*/

    public void get_Medicines() {
        String ed_text = ed_search.getText().toString().trim();

        if (!ed_text.equals("")){

            str_symptom = ed_text.substring(0, 1).toUpperCase() + ed_text.substring(1).toLowerCase();

            Cursor cursor = mDatabase.fetch_Medicines();

            if (cursor.getCount() == 0) {
                try {
                    //  mDatabase.add_items(bar_code,name,batch,company,expiry_date,gtin_no,serial_no,supply_chain);
                    txt_relatedMedicines.setText("No Medicine available");
                    Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
                    Log.d("resss::", e.getMessage());
                }
            } else {

                while (cursor.moveToNext()) {
                    Log.i("message", "Data got");

                    /*Add data in Model Class*/

                    Model_Sypmtoms mdl = new Model_Sypmtoms();

                    mdl.setId(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.ID_Medicine)));
                    mdl.setSymtomName(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.Name_Symptom)));
                    mdl.setMedicines(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.Symptom_Medicines)));
                    mdl.setFormula(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.Formula)));

                    model_sypmtoms.add(mdl);
                    String symtomName = mdl.getSymtomName();
                    arry_lst.add(symtomName);


                }


                if (arry_lst.contains(str_symptom)) {

                    for (int i = 0; i < model_sypmtoms.size(); i++) {
                        Model_Sypmtoms md = model_sypmtoms.get(i);
                        if (md.getSymtomName().equals(str_symptom)) {

                            txt_relatedMedicines.setText(md.getMedicines());

                        }
                    }

                } else {
                    try {

                        txt_relatedMedicines.setText("No Medicine available!");


                    } catch (Exception e) {
                        Log.d("reeeess:", e.getMessage());
                    }
                }
            }
        }
    }
}
