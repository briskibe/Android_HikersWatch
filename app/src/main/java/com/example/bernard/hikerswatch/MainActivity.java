package com.example.bernard.hikerswatch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locMng;
    LocationListener locLst;
    TextView txtLat;
    TextView txtLng;
    TextView txtAcc;
    TextView txtAlt;
    TextView txtAdr;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locMng.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locLst);
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLat = (TextView) findViewById(R.id.txtLat);
        txtLng = (TextView) findViewById(R.id.txtLng);
        txtAcc = (TextView) findViewById(R.id.txtAcc);
        txtAlt = (TextView) findViewById(R.id.txtAlt);
        txtAdr = (TextView) findViewById(R.id.txtAddr);


        locMng = (LocationManager) getSystemService(LOCATION_SERVICE);
        locLst = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateTexts(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            locMng.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locLst);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locMng.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locLst);
            }
        }
    }

    private void updateTexts(Location loc) {
        txtLat.setText("Latitude: " + loc.getLatitude());
        txtLng.setText("Longitude: " + loc.getLongitude());
        txtAcc.setText("Accuracy: " + loc.getAccuracy());
        txtAlt.setText("Altitude: " + loc.getAltitude());

        Geocoder gcdr = new Geocoder(getApplicationContext(), Locale.US);
        try {
            List<Address> addressList = gcdr.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            if (addressList != null && addressList.size() > 0) {
                Address addr = addressList.get(0);
                Log.i("ADDRESS", addr.toString());
                String addrText = "Address: \n";
                if (addr.getThoroughfare() != null) addrText += addr.getThoroughfare() + "\n";
                if (addr.getLocality() != null) addrText += addr.getLocality() + "\n";
                if (addr.getAdminArea() != null) addrText += addr.getAdminArea();
                txtAdr.setText(addrText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
