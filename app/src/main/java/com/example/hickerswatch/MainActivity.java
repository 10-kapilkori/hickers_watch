package com.example.hickerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0 , 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
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

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0 , 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation != null) {

            }
        }

    }
    public void updateLocationInfo(Location location) {
        TextView latTextView = (TextView) findViewById(R.id.latTextView);
        TextView longTextView = (TextView) findViewById(R.id.longTextView);
        TextView accTextView = (TextView) findViewById(R.id.accTextView);
        TextView altTextView = (TextView) findViewById(R.id.altTextView);
        TextView addressTextView = (TextView) findViewById(R.id.addressTextView);

        latTextView.setText("Latitude :" + Double.toString(location.getLatitude()));
        longTextView.setText("Longitude :" + Double.toString(location.getLongitude()));
        accTextView.setText("Accuracy :" + Double.toString(location.getAccuracy()));
        altTextView.setText("Altitude :" + Double.toString(location.getAltitude()));

        String address = "Could not find address!";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(list != null && list.size() > 0) {
                address = "Address: \n";
                if(list.get(0).getThoroughfare() != null) {
                    address += list.get(0).getThoroughfare() + "\n";
                }
                if(list.get(0).getLocality() != null) {
                    address += list.get(0).getLocality() + " ";
                }
                if(list.get(0).getPostalCode() != null) {
                    address += list.get(0).getPostalCode() + " ";
                }
                if(list.get(0).getAdminArea() != null) {
                    address += list.get(0).getAdminArea() + " ";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        addressTextView.setText(address);
    }
}
