package com.example.yumfood.customer.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.yumfood.R;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;

//public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    // Call gg map
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private SearchView mapSearchView;
    ImageView btnBack;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        EditText etSource = findViewById(R.id.source);
        EditText etDes = findViewById(R.id.destination);
        Button btnGo = findViewById(R.id.btn_go);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String source = etSource.getText().toString();
                String des = etDes.getText().toString();
                if (source.equals("")&&des.equals("")){
                    Toast.makeText(MapActivity.this, "Enter source and description", Toast.LENGTH_SHORT).show();
                }
                else {
                    Uri uri = Uri.parse("https://www.google.com/maps/dir/"+source+"/"+des);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.google.android.apps.maps");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fusedLocationProviderClient = (FusedLocationProviderClient) LocationServices.getFusedLocationProviderClient(this);

        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(
                new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getCurrentLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }
        ).check();

        mapSearchView = (SearchView) findViewById(R.id.mapSearch);
        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = mapSearchView.getQuery().toString();
                List<Address> addressList = null;

                if (location!=null){
                    Geocoder geocoder = new Geocoder(MapActivity.this);
                try {
                    addressList = geocoder.getFromLocationName(location,1);
                }
                catch (IOException e){
                    e.printStackTrace();
                }

                Address address = addressList.get(0);
                LatLng latLng = new LatLng( address.getLatitude(), address.getLongitude() );
                googleMap.addMarker(new MarkerOptions().position(latLng).title(location));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        supportMapFragment.getMapAsync(MapActivity.this);

        btnBack = (ImageView) findViewById(R.id.btn_map_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, HomeActivity.class);
                startActivity(intent);
                finishActivity(1);
            }
        });
//        // Lay location cua mt
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        // Map on ready and wait to call.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        // Lib gg map
//        mapFragment.getMapAsync(this);
//        // Map dang trong trang thai cho dc call
    }

    public void getCurrentLocation() {
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
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        if (location!=null)
                        {
                            LatLng latLng = new LatLng( location.getLatitude(), location.getLongitude() );
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Current location!");
                            googleMap.addMarker(markerOptions);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                        }
                        else {
                            Toast.makeText(MapActivity.this, "Please on your location App Permission", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

//    @Override
//    public void onMapReady(GoogleMap map) {
//        googleMap = map;
//        //Add a marker in HoChiMinh City (UIT) and move the camera
//        LatLng UIT = new LatLng( 10.870395631962573, 106.8037320187609 );
//        googleMap.addMarker( new MarkerOptions().position(UIT).title( "Trường Đại Học Công Nghệ Thông Tin" ) );
//        googleMap.moveCamera( CameraUpdateFactory.newLatLng(UIT) );
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UIT, (float) 17));
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(UIT, (float)17));
//    }
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        }
}
