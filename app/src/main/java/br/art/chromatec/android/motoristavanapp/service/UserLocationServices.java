package br.art.chromatec.android.motoristavanapp.service;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import br.art.chromatec.android.motoristavanapp.model.VehicleLocation;

/**
 * Created by Cesar on 17/04/2017.
 */

public class UserLocationServices extends AsyncTask<Location, Void, Boolean> {
    //TODO: Enviar um código criptografado e a placa do veículo para o serviço (questão de segurança)
    private static final String ADDRESS_VEHICLE_LOCATION_SERVICE = "http://192.168.15.16:8080/vanapp-web/vehicle_location_receiver";
    private static final String TAG_SERVICE_ANSWERED = "SERVICE_ANSWERED";
    private static final String TAG_REST_SERVICE_URL = "REST_SERVICE_URL";
    private static final String TAG_CONNECTION_CODE = "CONNECTION_CODE";
    private static final String TAG_LOG_MESSAGES = "LOG:";
    private static final Charset charset = StandardCharsets.UTF_8;

    public UserLocationServices() {

    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(Location... userLocations) {
        HttpURLConnection connection = null;
        boolean success = false;
        try {

            URL url = new URL(ADDRESS_VEHICLE_LOCATION_SERVICE);
            Log.i(TAG_REST_SERVICE_URL, url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset.displayName());
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset.displayName());

            OutputStream os = connection.getOutputStream();
            String params;
            for (Location userLocation : userLocations) {
                String latitude = String.valueOf(userLocation.getLatitude());
                String longitude = String.valueOf(userLocation.getLongitude());
                params = String.format("coordinates=%s,%s",
                        URLEncoder.encode(latitude, charset.displayName()),
                        URLEncoder.encode(longitude, charset.displayName()));

                Log.i(TAG_LOG_MESSAGES, params);
                os.write(params.getBytes(charset));
            }
            os.flush();
            os.close();
            Log.i(TAG_CONNECTION_CODE, String.valueOf(connection.getResponseCode()));

            if (connection.getResponseCode() == 200) {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return success;
    }

    @Override
    protected void onPostExecute(Boolean booleanResult) {
        Log.i(TAG_SERVICE_ANSWERED, "Servico VehicleLocationServices foi finalizado");
    }
}
