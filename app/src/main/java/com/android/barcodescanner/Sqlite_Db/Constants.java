package com.android.barcodescanner.Sqlite_Db;

public class Constants {

    public static final class DATABASE {

        public static final String DB_NAME = "items.db";
        public static final int DB_VERSION = 4;

        public static final String TABLE_NAME = "items_data";
        public static final String ID = "id";
        public static final String bar_code = "bar_code";
        public static final String PRODUCT_NAME="product_name";
        public static final String batch = "batch";
        public static final String company = "company";
        public static final String expiry_date = "expiry_date";
        public static final String gtin_no = "gtin_no";
        public static final String serial_no = "serial_no";
        public static final String supply_chain = "supply_chain";

        public static final String TABLE_Medicines = "items_medicines";
        public static final String ID_Medicine = "id_medicine";
        public static final String Name_Symptom = "sypmtom_name";
        public static final String Symptom_Medicines = "sypmtom_medicines";
        public static final String Formula = "fomula";


        public static final String DROP_QUERY = "DROP TABLE IF EXIST " + TABLE_NAME;
        public static final String GET_JSON_OBJECT_QUERY = "SELECT * FROM " + TABLE_NAME;
        public static final String DELETE_TABLE_ROW = "DELETE FROM "+TABLE_NAME;

    }

    public static final class REFERENCE {
        public static final String FLOWER = Config.PACKAGE_NAME + "flower";
    }

    public static final class Config {
        public static final String PACKAGE_NAME = "org.dalol.retrofit2_restapidemo.";
    }

}
