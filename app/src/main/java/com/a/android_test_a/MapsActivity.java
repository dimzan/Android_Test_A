package com.a.android_test_a;

import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.MainThread;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.*;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener, LocationListener, OnCameraMoveCanceledListener, OnCameraIdleListener, Fragment_Rectangle_3.OnFragmentInteractionListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    TextView myStreet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        Fragment app_bar_bg=(Fragment)getSupportFragmentManager().findFragmentById(R.id.fragment2);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            Log.d("setup", "location request granted");
        } else {
            Log.d("setup", "location request FAIL");
        }
        myStreet=(TextView)findViewById(R.id.textView_Street);




    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setBuildingsEnabled(false);


        // Add a marker in Sydney and move the camera
//        LatLng MyPlace = new LatLng(38.1, 23.8);
//        mMap.addMarker(new MarkerOptions().position(MyPlace).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(MyPlace));

    }




    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        String    myLocation=String.valueOf(location.getLatitude())+"  "+String.valueOf(location.getLongitude());
            Log.d("location",myLocation);
//        Toast.makeText(this, myLocation, Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ) {
            locationManager.removeUpdates(this);
            Toast.makeText(this, "Gps turning off", Toast.LENGTH_SHORT).show();
            LatLng MyPlace = new LatLng(location.getLatitude(),location.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(MyPlace).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(MyPlace));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onCameraMoveCanceled() {


    }

    @Override
    public void onCameraIdle() {
        Log.d("new location",String.valueOf(mMap.getCameraPosition().target.latitude)+"  "+String.valueOf(mMap.getCameraPosition().target.longitude));

//        Toast.makeText(this, "new location"+String.valueOf(mMap.getCameraPosition().target.latitude)+"  "+String.valueOf(mMap.getCameraPosition().target.longitude),Toast.LENGTH_SHORT).show();
        get_my_address();
    }


    private void get_my_address(){
        String cityName=null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        double myLatitude=mMap.getCameraPosition().target.latitude;
        double myLongitude=mMap.getCameraPosition().target.longitude;

        try {
            addresses = gcd.getFromLocation(myLatitude, myLongitude, 1);
            if (addresses.size() > 0) {
                String StreetName = addresses.get(0).getThoroughfare();
//                String s = myLongitude + "\n" + myLatitude +"\n\nΗ οδός είναι: " + StreetName;
//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                myStreet.setText(StreetName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
