package br.art.chromatec.android.motoristavanapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import br.art.chromatec.android.motoristavanapp.R;
import br.art.chromatec.android.motoristavanapp.listener.UserLocationListener;
import br.art.chromatec.android.motoristavanapp.listener.VehicleDataServiceResponseListener;
import br.art.chromatec.android.motoristavanapp.listener.VehicleLocationServiceResponseListener;
import br.art.chromatec.android.motoristavanapp.model.Vehicle;
import br.art.chromatec.android.motoristavanapp.model.VehicleLocation;
import br.art.chromatec.android.motoristavanapp.service.UserLocationServices;

/**
 * Created by Chromatec on 15/12/2016.
 */
public class MainActivity
        extends AppCompatActivity
        implements VehicleLocationServiceResponseListener,
                    VehicleDataServiceResponseListener {


    private static final String[] PERMISSIONS_BASIC = {
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    private final int REQUEST_CODE_BASIC_PERMS = 10;

    private static final String TAG_LOG_MESSAGES = "LOG:";
    private UserLocationListener userLocationListener;
    private LocationManager locationManager;

    public MainActivity() {
        userLocationListener = new UserLocationListener();
    }

    @Override
    protected  void onPause() {
        super.onPause();
        Log.i(TAG_LOG_MESSAGES, "Chamou onPause em MainActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (canAccessLocation()) {
            this.locationManager = (LocationManager) getApplicationContext()
                    .getSystemService(Context.LOCATION_SERVICE);

            /** Recupera a localizacao do usuario */
            this.getUserLocation();

        } else {
            this.askForLocationPermission(MainActivity.this);
        }

    }

    // LISTENERS
    @Override
    public void onVehicleLocationFetchComplete(final VehicleLocation vehicleLocation) {

        Log.i(TAG_LOG_MESSAGES, "Chamou onVehicleLocationFetchComplete");
    }

    @Override
    public void onVehicleDataFetchComplete(List<Vehicle> vehicleList) {
        Log.i(TAG_LOG_MESSAGES, "Chamou onVehicleDataFetchComplete");
    }


    // CALLBACKS
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_BASIC_PERMS:
                if (this.canAccessLocation()) {
                    Toast.makeText(MainActivity.this, "Acessar Localização: Concedido", Toast.LENGTH_SHORT).show();
                    getUserLocation();
                }
                break;
            default:
                Log.i(TAG_LOG_MESSAGES, "Default");
        }
    }

    // LOCATION METHODS

    /**
     * Metodo responsavel por pedir a permissao
     *
     * @param activity A atividade que precisa da permissao.
     * */
    private void askForLocationPermission(final Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE_BASIC_PERMS);
    }

    private Location getUserLocation() {
        Location location = null;
        try {
            String provider = checkProvidersAvailability(locationManager);
            locationManager.requestLocationUpdates(provider, 1000, 10, userLocationListener);
            location = locationManager.getLastKnownLocation(provider);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return location;
    }

    private String checkProvidersAvailability(LocationManager locationManager) {
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return LocationManager.NETWORK_PROVIDER;
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        }
        return "";
    }


    //INTENT
    public static Intent newIntent(Context packageContext, boolean isUserAllowed) {
        if (isUserAllowed) {
            return (new Intent(packageContext, MainActivity.class));
        }
        return null;
    }

    // PERMISSION CHECKING HELPER METHODS
    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
        } else {
            return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));
        }
    }

}
