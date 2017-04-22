package br.art.chromatec.android.motoristavanapp.listener;


import java.util.List;

import br.art.chromatec.android.motoristavanapp.model.Vehicle;

/**
 * Created by Cesar on 25/02/2017.
 */

public interface VehicleDataServiceResponseListener {
    void onVehicleDataFetchComplete(List<Vehicle> vehicleList);
}
