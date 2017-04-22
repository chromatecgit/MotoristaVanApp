package br.art.chromatec.android.motoristavanapp.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.art.chromatec.android.motoristavanapp.listener.VehicleDataServiceResponseListener;
import br.art.chromatec.android.motoristavanapp.model.Vehicle;


/**
 * Created by Chromatec on 15/12/2016.
 */
public class VehicleDataServices
        extends AsyncTask<String, Void, List<Vehicle>> {

    private static final String ADDRESS_VEHICLE_DATA_SERVICES = "http://192.168.15.16:8080/vanapp-web/vehicles";
    private static final String TAG_CONNECTION_CODE = "CONNECTION_CODE";
    private static final String TAG_RESPONSE_MESSAGE = "RESPONSE_MESSAGE";
    private static final String TAG_SERVICE_ANSWERED = "SERVICE_ANSWERED";
    private static final String TAG_SERVICE_FINISHED = "SERVICE EXEC FINISHED";
    private static final String TAG_REST_SERVICE_URL = "REST_SERVICE_URL";
    private static final String TAG_JSON_OK = "JSON_OK_MESSAGE";
    private static final String TAG_LOG_MESSAGES = "LOG:";
    private static final String TAG_JSON_ERROR = "JSON_ERROR_MESSAGE";
    private static final String PROGRESS_MESSAGE = "Buscando informações";
    private VehicleDataServiceResponseListener vehicleDataListener;
    private ProgressDialog progressDialog;
    private Context mainContext;

    public VehicleDataServices(Context context) {
        this.mainContext = context;
        this.progressDialog = new ProgressDialog(mainContext);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setCancelable(false);
        progressDialog.setMessage(PROGRESS_MESSAGE);
        progressDialog.show();
    }

    @Override
    public List<Vehicle> doInBackground(String... paramVarArgs) {
        HttpURLConnection connection = null;
        JSONArray jsonArray = null;
        try {
            URL url = new URL(ADDRESS_VEHICLE_DATA_SERVICES);
            Log.i(TAG_REST_SERVICE_URL, url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader response;

            StringBuilder sb = new StringBuilder();
            Log.i(TAG_CONNECTION_CODE, String.valueOf(connection.getResponseCode()));
            if (connection.getResponseCode() == 200) {
                Log.i(TAG_RESPONSE_MESSAGE, connection.getResponseMessage());
                response = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String line;
                while((line = response.readLine()) != null) {
                    sb.append(line);
                }
                jsonArray = new JSONArray(sb.toString());
                Log.i(TAG_JSON_OK, jsonArray.toString());
                response.close();

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
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (JSONException jsone) {
            jsone.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return parseJSONArray(jsonArray);
    }

    public void onPostExecute(List<Vehicle> vehicleList) {
        //TODO: exibir uma mensagem de atualizacao
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        this.vehicleDataListener.onVehicleDataFetchComplete(vehicleList);
        Log.i(TAG_SERVICE_ANSWERED, "Servico VehicleLocationServices foi finalizado");
    }

    private List<Vehicle> parseJSONArray(JSONArray jsonArray) {
        Log.i(TAG_LOG_MESSAGES, jsonArray.toString());
        List<Vehicle> vehicleList = new ArrayList<>();
        //TODO: Colocar num metodo de parse separado ou talvez em outra classe
        try {
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    Log.i(TAG_LOG_MESSAGES, "Logging Vehicle");
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    Vehicle vehicle = new Vehicle(
                            (String) jsonObject.get("id"),
                            (String) jsonObject.get("state"),
                            (String) jsonObject.get("city"),
                            (String) jsonObject.get("destination"),
                            (int) jsonObject.get("passengerCapacity"),
                            (boolean) jsonObject.get("isAvailable"));

                    vehicleList.add(vehicle);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vehicleList;
    }

    public void setVehicleDataListener(
            VehicleDataServiceResponseListener vehicleDataListener) {
        this.vehicleDataListener = vehicleDataListener;
        Log.i(TAG_LOG_MESSAGES, "Registrou VehicleDataListener");
    }
}
