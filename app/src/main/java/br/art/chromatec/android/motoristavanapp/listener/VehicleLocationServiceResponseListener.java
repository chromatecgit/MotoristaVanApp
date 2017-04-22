package br.art.chromatec.android.motoristavanapp.listener;


import br.art.chromatec.android.motoristavanapp.model.VehicleLocation;

/**
 * Created by Cesar on 25/02/2017.
 */

public interface VehicleLocationServiceResponseListener {
    void onVehicleLocationFetchComplete(VehicleLocation vehicleLocation);
}
