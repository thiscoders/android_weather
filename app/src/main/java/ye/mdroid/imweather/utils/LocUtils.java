package ye.mdroid.imweather.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ye on 16-11-29.
 */

public class LocUtils {
    private final String TAG = LocUtils.class.getSimpleName();

    private Context context;
    private double latitude; //纬度
    private double longitude; //经度

    public LocUtils(Context context) {
        this.context = context;
    }

    public void getLoc() {
        LocationManager locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this.context, "请授权以正常使用本软件！", Toast.LENGTH_SHORT).show();
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.i(TAG, "aaa..." + (location == null) + "");
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.i(TAG, "111..." + latitude + "..." + longitude);
            }
        } else {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

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
            };

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Log.i(TAG, "bbb..." + (location == null) + "");
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.i(TAG, "222..." + latitude + "..." + longitude);
            }

        }

    }

}
