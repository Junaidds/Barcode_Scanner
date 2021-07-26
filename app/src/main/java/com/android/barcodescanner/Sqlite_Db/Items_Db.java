package com.android.barcodescanner.Sqlite_Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.android.barcodescanner.Sqlite_Db.Constants.DATABASE.ID_Medicine;
import static com.android.barcodescanner.Sqlite_Db.Constants.DATABASE.Name_Symptom;
import static com.android.barcodescanner.Sqlite_Db.Constants.DATABASE.TABLE_Medicines;
import static com.android.barcodescanner.Sqlite_Db.Constants.DATABASE.TABLE_NAME;


public class Items_Db extends SQLiteOpenHelper {
    private static final String TAG = Items_Db.class.getSimpleName();


    public Items_Db(Context context) {
        super(context, Constants.DATABASE.DB_NAME, null, Constants.DATABASE.DB_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
//            db.execSQL(Constants.DATABASE.CREATE_TABLE_QUERY);

            db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                    + Constants.DATABASE.ID + " INTEGER primary key, "
                    + Constants.DATABASE.bar_code + " TEXT not null, "
                    + Constants.DATABASE.PRODUCT_NAME + " TEXT not null, "
                    + Constants.DATABASE.batch + " TEXT not null, "
                    + Constants.DATABASE.company + " TEXT not null, "
                    + Constants.DATABASE.expiry_date + " TEXT not null, "
                    + Constants.DATABASE.gtin_no + " TEXT not null, "
                    + Constants.DATABASE.serial_no + " TEXT not null, "
                    + Constants.DATABASE.supply_chain + " TEXT not null);");

            db.execSQL("CREATE TABLE " + TABLE_Medicines + " ("
                    + Constants.DATABASE.ID_Medicine + " INTEGER primary key, "
                    + Constants.DATABASE.Name_Symptom + " TEXT not null, "
                    + Constants.DATABASE.Formula + " TEXT not null, "
                    + Constants.DATABASE.Symptom_Medicines + " TEXT not null);");


        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(Constants.DATABASE.DROP_QUERY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Medicines);
        // create new tables
        this.onCreate(db);

    }

    public void add_items(String bar_code, String product_Name, String batch, String company, String expiry_date,
                          String gtin_no, String serial_no, String supply_chain) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.DATABASE.bar_code, bar_code);
        values.put(Constants.DATABASE.PRODUCT_NAME, product_Name);
        values.put(Constants.DATABASE.batch, batch);
        values.put(Constants.DATABASE.company, company);
        values.put(Constants.DATABASE.expiry_date, expiry_date);
        values.put(Constants.DATABASE.gtin_no, gtin_no);
        values.put(Constants.DATABASE.serial_no, serial_no);
        values.put(Constants.DATABASE.supply_chain, supply_chain);

        try {

            db.insert(TABLE_NAME, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }


    public void add_syptoms(String syptom_name,String formula, String medicines) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.DATABASE.Name_Symptom, syptom_name);
        values.put(Constants.DATABASE.Formula, formula);
        values.put(Constants.DATABASE.Symptom_Medicines, medicines);

        try {

            db.insert(TABLE_Medicines, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }


  /*  public Cursor fetchNamesByConstraint(String filter) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.query(true, TABLE_Medicines, new String[] { ID_Medicine,
                        Name_Symptom }, Name_Symptom + " LIKE ?",
                new String[] { filter }, null, null, null,
                null);

        return mCursor;
    }*/


    public Cursor fetchJSONObject() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor;

        String query = "Select * from " + TABLE_NAME;
        cursor = sqLiteDatabase.rawQuery(query, null);


        return cursor;
    }

    public Cursor fetch_Medicines() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor;

        String query = "Select * from " + TABLE_Medicines;
        cursor = sqLiteDatabase.rawQuery(query, null);


        return cursor;
    }

    public void delete_data() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
        db.close();

    }

    public void delete_Medicines() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_Medicines);
        db.close();

    }

    public void delete_by_id(String str_barcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, Constants.DATABASE.bar_code + "=" + str_barcode, null);
        db.close();
    }
}
