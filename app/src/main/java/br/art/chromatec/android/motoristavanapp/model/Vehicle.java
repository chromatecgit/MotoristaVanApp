package br.art.chromatec.android.motoristavanapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chromatec on 15/12/2016.
 */

public class Vehicle implements Parcelable {

    /**
     * A placa do veículo.
     */
    private String id;
    /**
     * O Estado origem do veículo.
     */
    private String state;
    /**
     * A cidade origem do veículo.
     */
    private String city;
    private String destination;
    private int passengerCapacity;
    private boolean isAvailable;

    public Vehicle() {}

    public Vehicle(String id, String state, String city, String destination, int passengerCapacity,
                   boolean isAvailable) {
        super();
        this.id = id;
        this.state = state;
        this.city = city;
        this.destination = destination;
        this.passengerCapacity = passengerCapacity;
        this.isAvailable = isAvailable;
    }

    private Vehicle(Parcel in) {
        id = in.readString();
        state = in.readString();
        city = in.readString();
        destination = in.readString();
        passengerCapacity = in.readInt();
        isAvailable = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(destination);
        dest.writeInt(passengerCapacity);
        dest.writeByte((byte) (isAvailable ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
