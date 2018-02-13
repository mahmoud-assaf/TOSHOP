package com.appssquare.mahmoud.myshoppinglist;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//import com.google.android.gms.location.FusedLocationProviderClient;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback {
    GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
   // FusedLocationProviderClient mFusedLocationClient;
    private long UPDATE_INTERVAL = 60 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private LocationCallback mLocationCallback;
    FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mMap;
    Button doneBtn;
    FloatingActionButton getCurrentLocFAB;
    Marker tempMarker;
    MarkerOptions marker;
    Location shopLoc;
    LatLng shopLocation;
    String shopAddress;
    boolean isShowLocation=false;
    LatLng shownLocation;
    String shownShopName;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().hasExtra("SHOP_LOCATION")){
            isShowLocation=true;

            String loc=getIntent().getStringExtra("SHOP_LOCATION");
            shownShopName=getIntent().getStringExtra("SHOP_NAME");
            String[]cords=loc.split(":");
            shownLocation=new LatLng(Double.parseDouble(cords[0]),Double.parseDouble(cords[1]));
            Log.e("editted  shop: ",loc);

        }
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        doneBtn = (Button) findViewById(R.id.setthisLocationbtn);
        doneBtn.setOnClickListener(this);
        getCurrentLocFAB = (FloatingActionButton) findViewById(R.id.fabCurrentLoc);
        getCurrentLocFAB.setOnClickListener(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        bar=findViewById(R.id.progressBargetlocation);
        //mGoogleApiClient.connect();
       // createLocationRequest();
       // mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
       /* mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    // status.append(location.toString()+ "\n");
                    // ...
                    shopLoc=location;
                    shopLocation=new LatLng(shopLoc.getLatitude(),shopLoc.getLongitude());
                    Address address=getAddress(shopLoc.getLatitude(),shopLoc.getLongitude());
                    if(address==null)
                        shopAddress="Unkonwn";
                    else
                        shopAddress=getAddress(shopLoc.getLatitude(),shopLoc.getLongitude()).getAddressLine(0);
                    if (tempMarker!=null)
                        tempMarker.remove();
                    tempMarker=  mMap.addMarker(new MarkerOptions().position(shopLocation).title("Shop"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(shopLocation));
                }
            };
        };*/

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

        //here we will let user point to location or button to get current location
        //TO-DO

        mMap = googleMap;

        if(isShowLocation){
            mMap.addMarker(new MarkerOptions().position(shownLocation).title("shownShopName"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shownLocation,14));
        }
        // Add a marker in Sydney and move the camera
      //  LatLng random = new LatLng(34, 33);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
      //  mMap.moveCamera(CameraUpdateFactory.newLatLng(random));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng position) {

                //new click
                if (tempMarker != null)
                    tempMarker.remove();

                marker = new MarkerOptions().position(position).title("Shop");
                tempMarker = mMap.addMarker(marker);
                shopLocation = position;
                Address address=getAddress(position.latitude,position.longitude);
                if(address==null)
                    shopAddress="Unkonwn";
                else
                    shopAddress=getAddress(position.latitude,position.longitude).getAddressLine(0);


            }
        });
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setthisLocationbtn:               //marker put use it

                if(tempMarker==null)
                    return;

                Intent BackIntent = new Intent();
                BackIntent.putExtra("LOCATION_LAT",shopLocation.latitude);
                BackIntent.putExtra("LOCATION_LNG",shopLocation.longitude);
                BackIntent.putExtra("ADDRESS",shopAddress);


                setResult(RESULT_OK,BackIntent);
                finish();



                break;
            case R.id.fabCurrentLoc:               //get current location and use

                bar.setVisibility(View.VISIBLE);
              //  mGoogleApiClient.connect();
                createLocationRequest();
                startLocationUpdates();

                //mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallback, null /* Looper */);

                break;

        }

    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            Log.e("permissions: ","noooooooooo");
            Toast.makeText(this, R.string.permissions_denied,Toast.LENGTH_LONG).show();
            return;
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    for (Location location : locationResult.getLocations()) {
                      //  status.append(location.toString()+ "\n");
                        // ...
                        double lat = location.getLatitude(), lon = location.getLongitude();

                        shopLocation=new LatLng(location.getLatitude(),location.getLongitude());
                        Address address=getAddress(location.getLatitude(),location.getLongitude());
                        if(address==null)
                            shopAddress="Unkonwn";
                        else
                            shopAddress=getAddress(location.getLatitude(),location.getLongitude()).getAddressLine(0);
                        if (tempMarker!=null)
                            tempMarker.remove();
                        bar.setVisibility(View.INVISIBLE);
                        tempMarker=  mMap.addMarker(new MarkerOptions().position(shopLocation).title("Shop"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shopLocation,14));
                    }
                };
            };
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public Address getAddress(double latitude, double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {

        /*if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            Log.e("permissions: ","noooooooooo");
            return;
        }
        Log.e("permissions: ","granted");
        Location shopLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (shopLoc==null){
            Log.e("fused location: ","non");
            return;}
        double lat = shopLoc.getLatitude(), lon = shopLoc.getLongitude();

        shopLocation=new LatLng(shopLoc.getLatitude(),shopLoc.getLongitude());
        Address address=getAddress(shopLoc.getLatitude(),shopLoc.getLongitude());
        if(address==null)
            shopAddress="Unkonwn";
        else
            shopAddress=getAddress(shopLoc.getLatitude(),shopLoc.getLongitude()).getAddressLine(0);
        if (tempMarker!=null)
            tempMarker.remove();
        tempMarker=  mMap.addMarker(new MarkerOptions().position(shopLocation).title("Shop"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shopLocation,14));
*/
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"Connection Failed",Toast.LENGTH_LONG).show();

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        Log.e("mainactivity attatch", "attachBaseContext");
    }
}
