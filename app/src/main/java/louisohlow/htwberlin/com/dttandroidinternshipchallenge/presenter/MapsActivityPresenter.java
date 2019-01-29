package louisohlow.htwberlin.com.dttandroidinternshipchallenge.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import louisohlow.htwberlin.com.dttandroidinternshipchallenge.R;
import louisohlow.htwberlin.com.dttandroidinternshipchallenge.model.LocationModel;
import louisohlow.htwberlin.com.dttandroidinternshipchallenge.model.PermissionsModel;
import louisohlow.htwberlin.com.dttandroidinternshipchallenge.views.MainActivity;

/**
 * @author      Louis Ohlow LouisOhlow@gmail.com>
 * @version     1.0
 */
public class MapsActivityPresenter {

    /**
     * maps parameters to access the MapsActivity
     */
    private View mapsView;
    private Context mapsContext;
    private Activity mapsActivity;

    private PermissionsModel permissionModel;
    private LocationModel locationModel;

    private LocationManager mLocationManager;

    private boolean firstMarker=true;

    /**
     *  Tag to log into the console
     */
    private static String TAG = "MapsActivityPresenter";

    /**
     * Handler to ask for location and internet connection every few seconds
     */
    private Handler mHandler = new Handler();

    /**
     * The entry points to the Places API.
     */
    private PlaceDetectionClient mPlaceDetectionClient;

    /**
     * request codes for the permissions
     */
    private static final int REQUEST_CALL = 2222;
    private static final int REQUEST_LOCATION = 1111;


    /**
     * global variables for the permission Strings
     */
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String CALL_PHONE = Manifest.permission.CALL_PHONE;


    /**
     * constantly checking the connections and initializes the GPS location once
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            checkNetworkLocationSettings();
            if(!locationModel.getLocationInitialized()){
                getGPSLocation();
            }
            mHandler.postDelayed(this, 1000);
        }
    };
    /**
     * initializes the View, Context and Activity from MapsActivity to access it
     *
     * @param view of the MapsActivity
     * @param context of the MapsActivity
     */
    public MapsActivityPresenter(View view, Context context){
        mapsView=view;
        mapsContext=context;
        mapsActivity=mapsView.getViewActivity();
    }

    /**
     * creates the necessary variables for the location and place access
     *
     * creates a model to store information
     */
    public void start(){
        permissionModel = new PermissionsModel();
        locationModel = new LocationModel();
        mPlaceDetectionClient = Places.getPlaceDetectionClient(mapsContext);
        mLocationManager = (LocationManager) mapsContext.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * setup to get the GPS location
     *
     * Define a listener that responds to location updates
     * update variables
     */
    private void getGPSLocation(){

        LocationListener mLocationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, mapsContext.getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.getGPSLocationLogUpdate) + locationModel.getCurLatLng());
                locationModel.setCurLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
                if(networkAvailable()){
                   showCurrentPlace();
                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
                mapsView.hideDialog("location");
            }
            @Override
            public void onProviderDisabled(String provider) {
                mapsView.showDialog("location");
            }
        };

        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 3, mLocationListener);
            Log.d(TAG, mapsContext.getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.getGPSLocationLogSetup));
            locationModel.setLocationInitialized(true);
        }
        catch(SecurityException e){
            e.printStackTrace();
        }
    }


    /**
     * checks the connection settings and hides/shows the
     * depending dialogs on the Mapsactivity view
     *
     */
    private void checkNetworkLocationSettings() {
        if (networkAvailable()) {
            mapsView.hideDialog("network");
            if (locationAvailable()) {
                mapsView.hideDialog("gps");
            } else {
                mapsView.showDialog("gps");
            }
        }
        else mapsView.showDialog("network");
    }

    /**
     * checks if there is a network connection with wifi or mobile data
     *
     * @return true if there is a connection and false if there is not
     */
    private boolean networkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mapsContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnectedOrConnecting());
        } else {
            return false;
        }
    }

    /**
     * checks if there is a gps connection
     *
     * @return true if there is a connection and false if there is not
     */
    private boolean locationAvailable(){
        Log.d(TAG, mapsContext.getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.locationAvailableLogCheck));
        boolean gps_enabled = false;
        try {
            gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {
            Log.d(TAG, mapsContext.getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.locationAvailableLogDisabled));
        }
        if (gps_enabled){
            Log.d(TAG, mapsContext.getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.locationAvailableLogEnabled));
        }

        return gps_enabled;
    }



    /**
     * finds the current place which is the nearest to the device
     */
    private void showCurrentPlace(){
        if (permissionModel.getLocationPermissionGranted() ) {
            //suppresses the error of a missing permission
            @SuppressWarnings("MissingPermission") final Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                    PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                    if(likelyPlaces!=null) {
                        double i = 0.00;
                        /*selects the place with the highest rating*/
                        for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                            if (placeLikelihood.getLikelihood() > i) {
                                i = placeLikelihood.getLikelihood();
                                if(placeLikelihood.getPlace().getAddress()!=null)
                                    locationModel.setPlaceAdress(placeLikelihood.getPlace().getAddress().toString());
                            }
                        }
                        if (firstMarker) {
                            mapsView.moveCamera(locationModel.getCurLatLng());
                        } else {
                            mapsView.removeMarker();
                        }
                        mapsView.setMarker(locationModel.getAdress(), locationModel.getCurLatLng());
                        firstMarker = false;
                        Log.d(TAG, mapsContext.getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.showCurrentPlaceLogMarkerAdded));

                        likelyPlaces.release();
                    }
                    Log.d(TAG, mapsContext.getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.showCurrentPlaceLogPlaceFound));
                    Log.d(TAG, mapsContext.getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.showCurrentPlaceLogPlaceName) + locationModel.getAdress());
                }
            });
        }
    }

    /**
     * decides what to do if buttons get pressed on the MapsActivity View
     *
     * @param buttonName of the button which is pressed
     */
    public void buttonPressed(String buttonName){
        switch(buttonName){
            case "callRSRButton":
                getPhoneCallPermission();
                makePhoneCall();
                break;
            case "locationDisabledDlgPos":
                removeCallback();
                mapsContext.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                break;
            case "locationDisabledDlgNeg":
                removeCallback();
                Intent intent = new Intent(mapsContext, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mapsContext.startActivity(intent);
                break;
            case "connectionsDisabledDlgPos":
                removeCallback();
                mapsContext.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                break;
            case "connectionsDisabledDlgNeg":
                removeCallback();
                Intent intent2 = new Intent(mapsContext, MainActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mapsContext.startActivity(intent2);
                break;
            case "backToMainButton":
                removeCallback();
                Intent intent3 = new Intent(mapsContext, MainActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mapsContext.startActivity(intent3);
                break;

        }
    }

    /**
     * stops the runnable
     */
    public void removeCallback(){
        mHandler.removeCallbacks(mRunnable);
    }

    /**
     * continues the runnable
     */
    public void continueCallback(){
        checkNetworkLocationSettings();
        mHandler.postDelayed(mRunnable, 2000);
    }

    /**
     * Makes the phone call
     *
     * checks if the permission is granted just before calling
     * if it's not, it asks for it
     */
    private void makePhoneCall() {
        if(permissionModel.getPhoneCallPermissionGranted()){
            String dial = mapsContext.getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.makePhoneCallTel)+mapsContext.getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.makePhoneCallDialNumber);
            mapsContext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
        else{
            getPhoneCallPermission();
        }
    }

    /**
     * asks for the phone call permission
     */
    private void getPhoneCallPermission(){
        if (ContextCompat.checkSelfPermission(mapsContext, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, mapsContext.getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.getPhoneCallPermissionLogGranted));
            permissionModel.setPhoneCallPermissionGranted(true);
        } else {
            ActivityCompat.requestPermissions(mapsActivity, new String[]{CALL_PHONE}, REQUEST_CALL);
            Log.d(TAG, mapsContext.getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.getPhoneCallPermissionLogDenied));
        }
    }

    /**
     * checks if the permission is granted
     *
     * for both, the Fine locationa and the Coarse location
     * asks for it, if it's not granted
     */
    public void getLocationPermission() {
        String[] permissions = {FINE_LOCATION};
        if (ContextCompat.checkSelfPermission(mapsContext, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            permissionModel.setLocationPermissionGranted(true);
            Log.d(TAG, mapsContext.getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.getLocationPermissionLogGranted));
        } else {
            ActivityCompat.requestPermissions(mapsActivity, permissions, REQUEST_LOCATION);
            Log.d(TAG, mapsContext.getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.getLocationPermissionLogDenied));
        }
    }

    /**
     * descides what to do on the permission result
     *
     * @param permission is the code to identify the permission type
     * @param granted is true when the permission is granted, false if it is not
     */
    public void permissionResult(int permission, boolean granted){
        switch(permission){
            case REQUEST_LOCATION :
                permissionModel.setLocationPermissionGranted(granted);
                if(!granted){
                    Log.d(TAG, mapsContext.getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.onRequestPermissionsResultLogLocationDenied));
                    removeCallback();
                    Intent intent = new Intent(mapsContext, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mapsView.getViewActivity().startActivity(intent);
                    return;
                }
                else{
                    Log.d(TAG, mapsContext.getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.onRequestPermissionsResultLogLocationGranted));
                    return;
                }
            case REQUEST_CALL:
                permissionModel.setPhoneCallPermissionGranted(granted);
                if(!granted){
                    Log.d(TAG, mapsContext.getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.onRequestPermissionsResultLogCallDenied));
                }
                else{
                    makePhoneCall();
                    Log.d(TAG, mapsContext.getResources().getString(R.string.onRequestPermissionsResultLogCallGranted));
                }
        }
    }


    /**
     * View Interface for the MapsActivity class to call it's methods
     */
    public interface View{
        void showDialog(String dialogName);
        void hideDialog(String dialogName);

        void setMarker(String adresse, LatLng curLatLng);
        void removeMarker();

        void moveCamera(LatLng latlng);

        Activity getViewActivity();
    }

}
