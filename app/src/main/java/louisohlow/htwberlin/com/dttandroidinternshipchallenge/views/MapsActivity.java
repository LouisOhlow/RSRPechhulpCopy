package louisohlow.htwberlin.com.dttandroidinternshipchallenge.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import louisohlow.htwberlin.com.dttandroidinternshipchallenge.adapter.InfoWindowAdapter;
import louisohlow.htwberlin.com.dttandroidinternshipchallenge.R;
import louisohlow.htwberlin.com.dttandroidinternshipchallenge.presenter.MapsActivityPresenter;

/**
 * @author      Louis Ohlow LouisOhlow@gmail.com>
 * @version     1.0
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, MapsActivityPresenter.View {

    private GoogleMap mMap;

    Marker curLocationMarker;

    /**
     * the necessary Dialogs on the activity
     */
    AlertDialog connectionDisabledDialog;
    View callDialogView;
    AlertDialog callDialog;
    AlertDialog locationDisabledDialog;

    /**
     * the necessary Buttons on the activity
     */
    Button backToMainButton;
    Button callDialogButton;
    Button callRSRButton;
    Button leaveDialogButton;

    MapsActivityPresenter presenter;
    /**
     * request codes for the permissions
     */
    private static final int REQUEST_CALL = 2222;
    private static final int REQUEST_LOCATION = 1111;

    /**
     * method which gets called when the activity is build
     *
     * sets the content View and calls all important
     * methods for starting the activity
     *
     * Construct a PlaceDetectionClient and obtains the SupportMapFragment
     * to get notified when the map is ready to be used.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new MapsActivityPresenter(this, this);
        presenter.start();

        setContentView(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.id.map);

        if(mapFragment!=null)
            mapFragment.getMapAsync(this);

        assignDialogs();
        assignButtons();
    }


    /**
     * assigns all Dialogs
     */
    @SuppressLint("InflateParams")
    private void assignDialogs(){
        connectionDisabledDialog = new AlertDialog.Builder(MapsActivity.this)
                .setTitle(getResources().getString(R.string.connectionDisabledDlgTitle))
                .setMessage(getResources().getString(R.string.connectionDisabledDlgMessage))
                .setPositiveButton(getResources().getString(R.string.buttonAanzetten), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.buttonPressed(getString(R.string.connectionsDisabledDlgPos));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.buttonAnnuleren), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.buttonPressed(getString(R.string.connectionsDisabledDlgNeg));
                    }
                })
                .create();
        connectionDisabledDialog.setOnShowListener(new DialogInterface.OnShowListener(){
            @Override
            public void onShow(DialogInterface arg0) {
                connectionDisabledDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getApplicationContext().getResources().getColor(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.color.colorPrimary));
            }
        });
        connectionDisabledDialog.setCanceledOnTouchOutside(false);

        locationDisabledDialog = new AlertDialog.Builder(MapsActivity.this)
                .setTitle(getResources().getString(R.string.locationDisabledDlgTitle))
                .setMessage(getResources().getString(R.string.locationDisabledDlgMessage))
                .setPositiveButton(getResources().getString(R.string.buttonAanzetten), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.buttonPressed(getString(R.string.locationDisabledDlgPos));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.buttonAnnuleren), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.buttonPressed(getString(R.string.locationDisabledDlgNeg));
                    }
                })
                .create();
        locationDisabledDialog.setOnShowListener(new DialogInterface.OnShowListener(){
            @Override
            public void onShow(DialogInterface arg0) {
                locationDisabledDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getApplicationContext().getResources().getColor(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.color.colorPrimary));
            }
        });
        locationDisabledDialog.setCanceledOnTouchOutside(false);

        callDialogView = LayoutInflater.from(this).inflate(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.layout.call_dialog, null);
        callDialog = new AlertDialog.Builder(MapsActivity.this)
                .setView(callDialogView)
                .create();
        Window window = callDialog.getWindow();
        if(window!=null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
        }
        callDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * assigns all Buttons
     */
    private void assignButtons(){
        backToMainButton = findViewById(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.id.backToMainButton);
        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.buttonPressed("backToMainButton");
            }
        });

        callDialogButton = findViewById(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.id.openCallDialog);
        callDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialog.show();
                callDialogButton.setVisibility(View.GONE);
                findViewById(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.id.openCallDialogView).setVisibility(View.GONE);

            }
        });

        callRSRButton = callDialogView.findViewById(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.id.callButton);
        callRSRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.buttonPressed("callRSRButton");
            }
        });


        leaveDialogButton = callDialogView.findViewById(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.id.leaveDialogButton);
        leaveDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialog.dismiss();
                callDialogButton.setVisibility(View.VISIBLE);
                findViewById(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.id.openCallDialogView).setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * gets called when the application pauses
     *
     * pauses the Runnable to prevent background errors
     */
    @Override
    protected void onPause() {
        super.onPause();
        presenter.removeCallback();
    }

    /**
     * gets called when the application resumes
     *
     * checks immediately the network and location settings
     * continues the Runnable
     */
    @Override
    protected void onResume() {
        super.onResume();
        presenter.continueCallback();
    }

    /** gets invoked when the map is ready
     *  gets the location permission
     *  moves the camera to 0/0
     *  starts the Runnable
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        presenter.getLocationPermission();

        moveCamera(new LatLng(0.0, 0.0));

        presenter.continueCallback();
    }

    /**
     * sets the marker to the last known position
     *
     * prevents the appearing from the marker when the map is launching and at 0/0
     *
     * @param addresse gets displayed on the marker info snippet
     */
    @Override
    public void setMarker (String addresse, LatLng curLatLng) {
        MarkerOptions options = new MarkerOptions()
                .position(curLatLng)
                .icon(BitmapDescriptorFactory.fromResource(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.mipmap.ic_map_marker))
                .title(getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.markerTitle))
                .snippet(addresse
                        + "\n" + "\n" + getResources().getString(louisohlow.htwberlin.com.dttandroidinternshipchallenge.R.string.markerSnippet));

        mMap.setInfoWindowAdapter(new InfoWindowAdapter(MapsActivity.this));
        curLocationMarker = mMap.addMarker(options);
        curLocationMarker.showInfoWindow();
    }

    /**
     * gets called from the presenter to show a dialog
     *
     * @param dialogName specifies the dialog to show
     */
    @Override
    public void showDialog(String dialogName) {
        switch (dialogName) {
            case "gps":
                if (!locationDisabledDialog.isShowing()) {
                    locationDisabledDialog.show();
                }
                break;
            case "network":
                if (!connectionDisabledDialog.isShowing()) {
                    connectionDisabledDialog.show();
                }
                break;
        }
    }

    /**
     * gets called from the presenter to hide a dialog
     *
     * @param dialogName specifies the dialog to hide
     */
    @Override
    public void hideDialog(String dialogName) {
        switch (dialogName) {
            case "gps":
                if (locationDisabledDialog.isShowing()) {
                    locationDisabledDialog.dismiss();
                }
                break;
            case "network":
                if (connectionDisabledDialog.isShowing()) {
                    connectionDisabledDialog.dismiss();
                }
                break;
        }
    }


    /**
     * moves the camera on the map
     *
     * @param latlng of the positin it moves to
     */
    @Override
    public void moveCamera(LatLng latlng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16f));
    }

    /**
     * handles the reactions to the permissions
     *
     * leads back to the main activity if they are denied
     *
     * calls the phone if the Call permission is granted
     *
     * @param requestCode identifites which permission is asked for
     * @param permissions contains all permission results
     * @param grantResults contains all granted permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        presenter.permissionResult(requestCode, false);
                        return;
                    }
                    presenter.permissionResult(requestCode, true);
                    return;

                }
            }
            case REQUEST_CALL:
                if (grantResults.length > 0) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        presenter.permissionResult(requestCode, false);

                    }
                    else{
                        presenter.permissionResult(requestCode, true);
                    }
                }
        }
    }

    /**
     * removes the marker from the map
     */
    @Override
    public void removeMarker(){
        curLocationMarker.remove();
    }

    /**
     * returns the activity for the presenter
     *
     * @return Activity from the Map
     */
    @Override
    public Activity getViewActivity() {
        return this;
    }
}