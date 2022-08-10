package com.nunmsc.bcisng.nafisa.sec;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class GEO extends AppCompatActivity implements LocationListener {

    TextView address,location;
    Button getLocation;
    LocationTracker tracker;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static final String PREFS_NAME = "MyPrefsFile";
    static double zLati, zLongi;
    SharedPreferences sharedpreferences;
    static String status="Valid", sta,sto;
    TextView time,lati,longi ;
    int t1=0,t2;
    LocationManager locationManager ;
    String provider, Stamp;
    SimpleDateFormat df1 = new SimpleDateFormat("MMM MM dd, yyyy h:mm");
    long start, stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo);

        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        final SharedPreferences.Editor editor = settings.edit();
        time = (TextView) findViewById(R.id.time);

        //sta = settings.getString("TimeStamp", "");

        stop = System.currentTimeMillis();
        SimpleDateFormat df1 = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
        Stamp = df1.format(stop);

        // Getting LocationManager object
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);

        if(provider!=null && !provider.equals("")){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                    // MY_PERMISSIONS_REQUEST_FINE_LOCATION is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }

/*            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if ( checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to ACCESS_FINE_LOCATION - requesting it");
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                //String[] permission = {Manifest.permission.ACCESS_COARSE_LOCATION};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }*/
            Location location = locationManager.getLastKnownLocation(provider);
            // Get the location from the given provider
            locationManager.requestLocationUpdates(provider, 10000, 1, this);
            if(location!=null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }

        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.removeUpdates(GEO.this);
            }
        }

        time.setText(Stamp);

        //***********************************

       // address=(TextView)findViewById(R.id.txt);
        //location=(TextView)findViewById(R.id.location);
        getLocation=(Button)findViewById(R.id.get);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create LocationTracker Object
                tracker=new LocationTracker(GEO.this);
                // check if location is available
                if(tracker.isLocationEnabled)
                {
                    double latitude=tracker.getLatitude();
                    double longitude=tracker.getLongitude();
                    String loc_msg="Your Location is Latitude= " + latitude + " Longitude= " + longitude;
                    //location.setText(loc_msg);
                    String addres= getCompleteAddressString(latitude,longitude);
                    //address.setText(addres);
                    Toast.makeText(getBaseContext(), addres, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getBaseContext(), loc_msg, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // show dialog box to user to enable location
                    tracker.askToOnLocation();
                }
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        lati = (TextView) findViewById(R.id.la);// Getting reference to TextView tv_latitude
        longi = (TextView) findViewById(R.id.lo); // Getting reference to TextView tv_latitude

        // Setting Current Longitude
        longi.setText( ""+location.getLongitude());
        zLongi = location.getLongitude();
        // Setting Current Latitude
        lati.setText(""+location.getLatitude());
        zLati = location.getLatitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
    //**********************
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder
                    .getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                android.location.Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            "\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w(" location address", ""+ strReturnedAddress.toString());
            } else {
                Log.w(" location address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(" location address", "Cannot get Address!");
        }
        return strAdd;
    }


}
