package com.android.barcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.barcodescanner.Adapter.Adapter_Medicine;
import com.android.barcodescanner.Model.Model_Items;
import com.android.barcodescanner.Sqlite_Db.Constants;
import com.android.barcodescanner.Sqlite_Db.Items_Db;

import java.util.ArrayList;
import java.util.List;

public class History extends Bottom_Sheet_Screen {
    ListView list_history;
    static Items_Db mDatabase;
    List<Model_Items> model_items = new ArrayList<>();
    Adapter_Medicine adapter_medicine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_history, contentFrameLayout);
        // setContentView(R.layout.activity_history);

        list_history = findViewById(R.id.list_history);

        mDatabase = new Items_Db(this);


        /*Fetch medicines From local database*/

        Get_local_db_items();

    }


    private void Get_local_db_items() {

        Cursor cursor = mDatabase.fetchJSONObject();

        if (cursor.getCount() == 0) {
            try {
                Toast.makeText(this, "No Item!", Toast.LENGTH_SHORT).show();

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
                adapter_medicine = new Adapter_Medicine(this,model_items);
                list_history.setAdapter(adapter_medicine);




            }

        }
    }
}
