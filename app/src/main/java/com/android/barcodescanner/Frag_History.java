package com.android.barcodescanner;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.barcodescanner.Adapter.Adapter_Medicine;
import com.android.barcodescanner.Model.Model_Items;
import com.android.barcodescanner.Sqlite_Db.Constants;
import com.android.barcodescanner.Sqlite_Db.Items_Db;

import java.util.ArrayList;
import java.util.List;


public class Frag_History extends Fragment {


    ListView list_history;
    static Items_Db mDatabase;
    List<Model_Items> model_items = new ArrayList<>();
    Adapter_Medicine adapter_medicine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootview = inflater.inflate(R.layout.fragment_frag__history, container, false);

        list_history = rootview.findViewById(R.id.list_history);

        mDatabase = new Items_Db(getContext());


        Get_local_db_items();


        return rootview;

    }
    private void Get_local_db_items() {

        Cursor cursor = mDatabase.fetchJSONObject();

        if (cursor.getCount() == 0) {
            try {
                Toast.makeText(getContext(), "No Item!", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Log.d("resss::", e.getMessage());
            }
        } else {

            while (cursor.moveToNext()) {
                Log.i("message", "Data got");
                Model_Items mdl = new Model_Items();

                mdl.setBar_code(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.bar_code)));
                mdl.setName(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.PRODUCT_NAME)));
                mdl.setBatch(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.batch)));
                mdl.setCompany(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.company)));
                mdl.setExpiry_date(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.expiry_date)));
                mdl.setGtin_no(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.gtin_no)));
                mdl.setSerial_no(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.serial_no)));
                mdl.setSupply_chain(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.supply_chain)));

                model_items.add(mdl);
                adapter_medicine = new Adapter_Medicine(getActivity(),model_items);
                list_history.setAdapter(adapter_medicine);



            }

        }
    }
}
