package br.art.chromatec.android.motoristavanapp.service;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import br.art.chromatec.android.motoristavanapp.listener.VehicleLocationServiceResponseListener;
import br.art.chromatec.android.motoristavanapp.model.VehicleLocation;


public class VehicleLocationServices implements Runnable {

    private static final String ADDRESS_VEHICLE_LOCATION_SERVICE = "http://192.168.15.16:8080/vanapp-web/vehicles_location";
    private static final String JSON_MACTIVITY_RESPONSE = "JSON_MACTIVITY_RESPONSE";
    private static final String TAG_SERVICE_ANSWERED = "SERVICE_ANSWERED";
    private static final String TAG_PARSING_VEHICLES = "PARSING_VEHICLES";
    private static final String TAG_VEHICLES_COORDS = "VEHICLES_COORDS";
    private static final String TAG_RESPONSE_MESSAGE = "RESPONSE_MESSAGE";
    private static final String TAG_REST_SERVICE_URL = "REST_SERVICE_URL";
    private static final String TAG_CONNECTION_CODE = "CONNECTION_CODE";
    private static final String TAG_JSON_OK = "JSON_OK_MESSAGE";
    private static final String TAG_LOG_MESSAGES = "LOG:";
    private static final String TAG_JSON_ERROR = "JSON_ERROR_MESSAGE";
    private static final String PROGRESS_MESSAGE = "Buscando informações";
    private VehicleLocationServiceResponseListener vehicleLocationListener;

    private boolean finished = false;

    public VehicleLocationServices() {

    }

    @Override
    public void run() {
        onPreExecute();
        onPostExecute(execute());
    }

    protected void onPreExecute() {

    }

    protected VehicleLocation execute() {
        HttpURLConnection connection = null;
        VehicleLocation vehicleLocation = new VehicleLocation(0.0, 0.0);
        try {
            URL url = new URL(ADDRESS_VEHICLE_LOCATION_SERVICE);
            Log.i(TAG_REST_SERVICE_URL, url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader response;

            StringBuilder sb = new StringBuilder();
            Log.i(TAG_CONNECTION_CODE, String.valueOf(connection.getResponseCode()));

            if (connection.getResponseCode() == 200) {
                // Lendo a resposta
                Log.i(TAG_RESPONSE_MESSAGE, connection.getResponseMessage());
                response = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String line;
                while((line = response.readLine()) != null) {
                    sb.append(line);
                }
                response.close();

                JSONArray jsonArray = new JSONArray(sb.toString());
                Log.i(TAG_JSON_OK, jsonArray.toString());
                // Parse
                vehicleLocation = this.parseJSONArray(jsonArray);

            } else {
                Log.i(TAG_RESPONSE_MESSAGE, connection.getResponseMessage());
                response = new BufferedReader(new InputStreamReader(
                        connection.getErrorStream()));
                String line;
                while((line = response.readLine()) != null) {
                    sb.append(line);
                }
                response.close();
                Log.i(TAG_JSON_ERROR, sb.toString());

            }
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
            return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (JSONException jsone) {
            jsone.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return vehicleLocation;
    }

    private VehicleLocation parseJSONArray(JSONArray jsonArray) {
        Log.i(JSON_MACTIVITY_RESPONSE, jsonArray.toString());
        //TODO: Colocar num metodo de parse separado ou talvez em outra classe
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                Log.i(TAG_PARSING_VEHICLES, "Logging Coordinates");
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                VehicleLocation vehicleLocation = new VehicleLocation(
                        (double) jsonObject.get("latitude"),
                        (double) jsonObject.get("longitude"));
                Log.i(TAG_VEHICLES_COORDS, "Lat:"
                        + vehicleLocation.getLatitude() + " / "
                        + "Lng:" + vehicleLocation.getLongitude());

                return vehicleLocation;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(VehicleLocation vehicleLocation) {
        //TODO: exibir uma mensagem de atualizacao
        this.finished = true;
        vehicleLocationListener.onVehicleLocationFetchComplete(vehicleLocation);
        Log.i(TAG_SERVICE_ANSWERED, "Servico VehicleLocationServices foi finalizado");
    }

    public void setVehicleLocationListener(
            VehicleLocationServiceResponseListener vehicleLocationListener) {
        this.vehicleLocationListener = vehicleLocationListener;
        Log.i(TAG_LOG_MESSAGES, "Registrou Listener");
    }

    public boolean hasFinished() {
        return this.finished;
    }

}