package com.teamtreehouse.stormy;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.teamtreehouse.stormy.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private CurrentWeather currentWeather = new CurrentWeather();

    private ImageView iconImageView;

    private FusedLocationProviderClient mFusedLocationClient;

    double latitude = 37.8267;
    double longitude = -122.4233;

    private List<Address> list;

    private String locationName = "Alcatraz Island, CA";

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] permissionsGranted){
        super.onRequestPermissionsResult(requestCode, permissions, permissionsGranted);
        Log.d(TAG, "PASSED THROUGH ONREQUESTPERMISSIONSRESULT\n\n");
        getForecast(); /*Main method*/
        Log.d(TAG, "Main UI thread is running");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if permissions were already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED){
            //Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            //Wait for the permission request
            onRequestPermissionsResult(1, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, new int[] {1});
        }
        else {
            //Skip to the weather
            getForecast();
        }
    }

    private void checkPermissionAndGetLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED){
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_LONG).show();
            Log.i(TAG, "Location permission denied");
        }
        else{
            Log.i(TAG, "Location permission granted");
            //Get location
            try{
                getLocation();
            }catch (SecurityException e) {
                Log.e(TAG, "SecurityException caught: " + e);
            }
        }
    }

    private void getLocation() throws SecurityException{
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        final Geocoder geocoder = new Geocoder(this);

        Log.d(TAG, "Getting ready to get last location");

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d(TAG, "Location is: " + location);
                        if (location != null){
                            Log.d(TAG, "getLastLocation() returned location object, lat: "
                                    + String.valueOf(location.getLatitude()) + " lon: "
                                    + String.valueOf(location.getLongitude()));
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            Log.i(TAG, "Latitude was set to: " + latitude);
                            Log.i(TAG, "Longitude was set to: " + longitude);

                            try{
                                list = geocoder.getFromLocation(latitude, longitude, 1);
                                if (list != null & list.size() > 0) {
                                    Log.d(TAG, "Geocoder returned something");
                                    Address address = list.get(0);
                                    if (address.getLocality() != null) locationName = address.getLocality();
                                    Log.d(TAG, "Address found is: " + address.getLocality());
                                    Log.d(TAG, "Location name is: " + locationName);
                                }
                            }catch(IOException e){
                                Log.e(TAG, "Caught IOException: " + e);
                            }
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "No location could be returned");
                    }
                });
    }

    private void getForecast() {

        checkPermissionAndGetLocation();

        final ActivityMainBinding binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        TextView darkSky = findViewById(R.id.darkSkyAttribution);
        darkSky.setMovementMethod(LinkMovementMethod.getInstance());

        iconImageView = findViewById(R.id.iconImageView);

        String apiKey = "8b061d71cc593f087908cbd369a353b2";

        String forecastUrl = "https://api.darksky.net/forecast/" + apiKey + "/" + latitude + "," + longitude;

        Log.i(TAG, "Forecast URL is: " + forecastUrl);

        if(isNetworkAvailable()) {

            OkHttpClient httpClient = new OkHttpClient();

            Request request = new Request.Builder().url(forecastUrl).build();

            Call call = httpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);

                        currentWeather = getCurrentDetails(jsonData);

                        final CurrentWeather displayWeather = new CurrentWeather(
                                currentWeather.getLocationLabel(),
                                currentWeather.getIcon(),
                                currentWeather.getTime(),
                                currentWeather.getTemperature(),
                                currentWeather.getHumidity(),
                                currentWeather.getPrecipChance(),
                                currentWeather.getSummary(),
                                currentWeather.getTimezone()
                        );

                        binding.setWeather(displayWeather);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Drawable drawable = getResources().getDrawable(displayWeather.getIconID());
                                iconImageView.setImageDrawable(drawable);
                            }
                        });

                        if (!response.isSuccessful()) {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "IOException caught: ", e);
                    } catch (JSONException e){
                        Log.e(TAG, "JSONException caught: ", e);
                    }
                }
            });
        }
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);

        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);

        JSONObject currently = forecast.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.setLocationLabel(locationName);
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setTemperature((currently.getDouble("temperature")-32)/1.8);
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("icon"));
        currentWeather.setTimezone(timezone);
        String currentTime = currentWeather.getFormattedTime();

        Log.i(TAG, "From JSON: " +  currentTime);

        return currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()) isAvailable = true;
        else{
            Toast.makeText(this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
        }

        return isAvailable;
    }

    public void refreshOnClick(View v){
        Toast.makeText(this, "Refreshing data", Toast.LENGTH_LONG).show();
        getForecast();
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialogue");
    }
}
