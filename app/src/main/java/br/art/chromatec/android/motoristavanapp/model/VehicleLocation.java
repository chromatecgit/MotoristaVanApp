package br.art.chromatec.android.motoristavanapp.model;

/**
 * Created by Cesar on 22/01/2017.
 */

public class VehicleLocation {

    private final Double latitude;
    private final Double longitude;

    public VehicleLocation(Double latitude, Double longitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return this.latitude + "," + this.longitude;
    }
}
