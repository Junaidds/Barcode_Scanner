package com.android.barcodescanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyPreferences {
    private static MyPreferences instance = null;
    private SharedPreferences mSharedPreferences;

    private static final String sUser_Id = "sUser_Id";
    private static final String sUser_Fullname = "sUser_Fullname";
    private static final String sUser_Email = "sUser_Email";
    private static final String sUser_Address = "sUser_Address";
    private static final String sUser_PhoneNo = "sUser_PhoneNo";




    public static void init(Context context)
    {
        instance = new MyPreferences(context);
    }

    public static MyPreferences getInstance()
    {
        return instance;
    }

    private MyPreferences(Context c) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
    }

    ////////////////////////////////

    public void setsUser_Id(String user_id) {
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(sUser_Id, user_id);
        mEditor.apply();
    }

    public String getsUser_Id() {
        return mSharedPreferences.getString(sUser_Id, "");
    }



    public void setsUser_Email(String user_email) {
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(sUser_Email, user_email);
        mEditor.apply();
    }

    public String getsUser_Email() {
        return mSharedPreferences.getString(sUser_Email, "");
    }


    public void setsUser_Address(String user_address) {
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(sUser_Address, user_address);
        mEditor.apply();
    }

    public String getsUser_Address() {
        return mSharedPreferences.getString(sUser_Address, "");
    }


    public void setsUser_PhoneNo(long phoneNo) {
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putLong(sUser_PhoneNo, phoneNo);
        mEditor.apply();
    }

    public long getsUser_PhoneNo() {
        return mSharedPreferences.getLong(sUser_PhoneNo, 0);
    }

    public void setsUser_Fullname(String fullname) {
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(sUser_Fullname, fullname);
        mEditor.apply();
    }

    public String getsUser_Fullname()
    {
        return mSharedPreferences.getString(sUser_Fullname, "");
    }


}
