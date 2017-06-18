package com.example.jirka.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Vector;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;

    private Vector<Marker> markers;

    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private BitmapDescriptor closedBook;
    private BitmapDescriptor openBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // closedBook = ;
        // openBook = BitmapDescriptorFactory.fromResource(R.mipmap.book_single_lines);

        markers = new Vector<Marker>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    updatePosition(location);
                }
            };
        };
    }

    public static double distance(LatLng StartP, LatLng EndP) {
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6366000 * c;
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null);
    }*/

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public void updatePosition(Location location) {
        if (location != null) {
            //LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
            LatLng here = new LatLng(location.getLatitude(), location.getLongitude());
            // Marker m = mMap.addMarker(new MarkerOptions().position(here).title("now"));
            // // TODO: 18.6.17 fix only on asdfafds
            // mMap.moveCamera(CameraUpdateFactory.newLatLng(here));
/*
            for (Marker m :
                    markers) {
                double dist = distance(m.getPosition(), here);
                if (dist < 100) {
                    m.setIcon(openBook);
                }
                else {
                    m.setIcon(closedBook);
                }
                }*/
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        JSONArray array;
        String input = "";
        Resources raw = this.getResources();
        try {
            InputStream stream = raw.openRawResource(R.raw.poems_json);
            BufferedInputStream bis = new BufferedInputStream(stream);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result = bis.read();
            while (result != -1) {
                buf.write((byte) result);
                result = bis.read();
            }
            // StandardCharsets.UTF_8.name() > JDK 7
            input = buf.toString("UTF-8");

        } catch (Throwable t) {
            t.printStackTrace();
        }

        try {
            array = new JSONArray(input);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                LatLng pos = new LatLng(obj.getDouble("lat"), obj.getDouble("lng"));
                String name = obj.getString("name");
                Marker m = mMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(name)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.book_closed)));
                m.setTag(new Poem(obj));

                markers.add(m);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e("MapsActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Can't find style. Error: ", e);
        }

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(50, 14);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

        mMap.setOnMarkerClickListener(this);

        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    static int ID = 0;
    private LocationCallback mLocationCallback;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            updatePosition(location);
                }
            });

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(2000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
        }

        /*

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            mMap.setMyLocationEnabled(true);
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }*/
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(this, PoemActivity.class);
        // EditText editText = (EditText) findViewById(R.id.editText);
        // String message = editText.getText().toString();
        // intent.putExtra(EXTRA_MESSAGE, message);
        ID++;
        PoemActivity.activePoem = (Poem) marker.getTag();
        startActivity(intent);

        return false;
    }
}
