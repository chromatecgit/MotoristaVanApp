package br.art.chromatec.android.motoristavanapp.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import br.art.chromatec.android.motoristavanapp.service.UserLocationServices;

/**
 * Created by Cesar on 17/04/2017.
 */

public class UserLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {
        UserLocationServices userLocationServices = new UserLocationServices();
        userLocationServices.execute(location);
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
}
