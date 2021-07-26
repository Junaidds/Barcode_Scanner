package com.android.barcodescanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.barcodescanner.Model.Model_Items;
import com.android.barcodescanner.Sqlite_Db.Constants;
import com.android.barcodescanner.Sqlite_Db.Items_Db;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.android.barcodescanner.Bottom_Sheet_Screen.str_Username;

public class Get_FB_Data extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    FirebaseFirestore db;

    TextView txt_gtn,txt_batch,txt_expiry,txt_serial,txt_product_name,txt_company,txt_supply,txt_barcode;

    ArrayList<String> aray_codes = new ArrayList<>();
    ImageView img_back;
    static Items_Db mDatabase;
    String bar_code,batch,company,expiry_date,gtin_no,name,serial_no,supply_chain;
    List<Model_Items> model_items = new ArrayList<>();
    ArrayList<String> arry_lst = new ArrayList<>();

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    String city_name,country_name;

    String str_time,str_date;

    FloatingActionsMenu right_labels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__f_b__data);

        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);

        mDatabase = new Items_Db(this);

        txt_gtn = findViewById(R.id.txt_gtn);
        txt_batch = findViewById(R.id.txt_batch);
        txt_expiry = findViewById(R.id.txt_expiry);
        txt_serial = findViewById(R.id.txt_serial);
        txt_product_name = findViewById(R.id.txt_product_name);
        txt_company = findViewById(R.id.txt_company);
        txt_supply = findViewById(R.id.txt_supply);
        txt_barcode = findViewById(R.id.txt_barcode);
        img_back = findViewById(R.id.img_back);

      //  Date currentTime = Calendar.getInstance().getTime();

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());


        str_time = currentTime;

        str_date = currentDate;

        Log.e("str_time", "onCreate:"+str_time+"str_date:"+str_date );


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        right_labels = findViewById(R.id.right_labels);
        FloatingActionButton fab_screenshot = findViewById(R.id.fab_screenshot);

        fab_screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getScreenShot(rootView);
                takeScreenshot();

            }
        });
        FloatingActionButton btn_share = findViewById(R.id.btn_share);

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shareImage();
                share_screenshot();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            final String str_val = bundle.getString("bar_code_val");
            db = FirebaseFirestore.getInstance();

            db.collection("medicine")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("Doccc", document.getId() + " => " + document.getData());

                                     bar_code = String.valueOf(document.get("bar_code"));

                                    aray_codes.add(bar_code);

                                    if (str_val.equals(bar_code)){
                                        Log.d("bar_code:", "onComplete:"+bar_code);
                                         batch = String.valueOf(document.get("batch"));
                                         company = String.valueOf(document.get("company"));
                                         expiry_date = String.valueOf(document.get("expiry_date"));
                                         gtin_no = String.valueOf(document.get("gtin_no"));
                                         name = String.valueOf(document.get("name"));
                                         serial_no = String.valueOf(document.get("serial_no"));
                                         supply_chain = String.valueOf(document.get("supply_chain"));

                                        txt_supply.setText(supply_chain);
                                        txt_batch.setText(batch);
                                        txt_company.setText(company);
                                        txt_expiry.setText(expiry_date);
                                        txt_gtn.setText(gtin_no);
                                        txt_product_name.setText(name);
                                        txt_serial.setText(serial_no);
                                        txt_barcode.setText(bar_code);

                                        local_db_items();

                                        String location = city_name+","+country_name;

                                        search_history(name,str_Username,location,str_time,str_date);

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


        }
    }

    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static void store(Bitmap bm, String fileName){
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "No App Available", Toast.LENGTH_SHORT).show();
        }
    }



    private void local_db_items() {

        Cursor cursor = mDatabase.fetchJSONObject();

        if (cursor.getCount() == 0) {
            try {
                mDatabase.add_items(bar_code,name,batch,company,expiry_date,gtin_no,serial_no,supply_chain);

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
                String bar_cde = mdl.getBar_code();
                arry_lst.add(bar_cde);

            }
            if (arry_lst.contains(bar_code)) {

                for (int i = 0; i < model_items.size(); i++) {
                    Model_Items md = model_items.get(i);
                    if (md.getBar_code().equals(bar_code)) {

                        Toast.makeText(this, "Already added", Toast.LENGTH_SHORT).show();

                    }
                }

            } else {
                try {
                    mDatabase.add_items(bar_code,name,batch,company,expiry_date,
                            gtin_no,serial_no,supply_chain);


                } catch (Exception e) {
                    Log.d("reeeess:", e.getMessage());
                }
            }
        }
    }


    public void search_history(String medicine_name,String user_name,String location,String str_time,String str_date) {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("medicine_name", medicine_name);
        user.put("user_name", user_name);
        user.put("location", location);
        user.put("Time",str_time);
        user.put("Date",str_date);
//        user.put("phone_no", phone);
//        user.put("email", email);

// Add a new document with a generated ID
        db.collection("search_history")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AddUser:", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error:", "Error adding document", e);
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
            mGoogleApiClient.disconnect();
        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            Log.e("crtttt:", "locaaaa"+currentLatitude );



            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);

                int maxAddressLine = addresses.get(0).getMaxAddressLineIndex();

                // String countryName = addresses.get(0).getAddressLine(maxAddressLine);
                city_name = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String cityName = addresses.get(0).getAddressLine(maxAddressLine);
                country_name = addresses.get(0).getCountryName();

                Log.d("city:", "city name:"+cityName);
                Log.d("city_name", "city_name::"+city_name);
                Log.d("country", "country::"+country_name);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

    }

    private Bitmap takeScreenshot() {

        right_labels.setVisibility(View.GONE);
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        Bitmap bitmap = null;
        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

           // openScreenshot(imageFile);
            right_labels.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Screen shot Saved!", Toast.LENGTH_SHORT).show();
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
        return bitmap;
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    public void share_screenshot(){
        Bitmap bm = takeScreenshot();
        File file = saveBitmap(bm, "Fyp_image.png");
        Log.i("chase", "filepath: "+file.getAbsolutePath());
        Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out my app.");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "share via"));

    }

    private Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private static File saveBitmap(Bitmap bm, String fileName){
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
