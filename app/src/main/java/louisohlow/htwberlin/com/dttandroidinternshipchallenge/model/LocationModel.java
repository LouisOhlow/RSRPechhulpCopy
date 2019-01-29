package louisohlow.htwberlin.com.dttandroidinternshipchallenge.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author      Louis Ohlow LouisOhlow@gmail.com>
 * @version     1.0
 */
public class LocationModel {
    /**
     *   global boolean to check if the permission got granted
     */
    private LatLng curLatLng;
    private String curAddress;
    private boolean locationInitialized = false;

    /**
     * setter for the place adresse
     * @param address to set
     */
    public void setPlaceAdress(String address){
        curAddress = address;
    }

    /**
     * setter for the position LatLng
     * @param latLng to set
     */
    public void setCurLatLng(LatLng latLng){
        curLatLng = latLng;
    }

    /**
     * getter for the location address
     * @return the addresse
     */
    public String getAdress(){
        return curAddress;
    }

    /**
     * getter for the position LatLng
     * @return the curLatLng
     */
    public LatLng getCurLatLng(){
        return curLatLng;
    }

    /**
     * setter for the locationInitialized boolean
     * @param isInitialized to set boolean
     */
    public void setLocationInitialized(boolean isInitialized){
        locationInitialized = isInitialized;
    }

    /**
     * getter for the locationInitialized boolean
     * @return locationInitialized to get
     */
    public boolean getLocationInitialized(){
        return locationInitialized;
    }

}