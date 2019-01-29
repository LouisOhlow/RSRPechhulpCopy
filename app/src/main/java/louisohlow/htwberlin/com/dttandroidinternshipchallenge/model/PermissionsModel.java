package louisohlow.htwberlin.com.dttandroidinternshipchallenge.model;

/**
 * @author      Louis Ohlow LouisOhlow@gmail.com>
 * @version     1.0
 */
public class PermissionsModel {

    /**
     *   global boolean to check if the permission got granted
     */
    private Boolean mLocationPermissionGranted = false;
    private Boolean mPhoneCallPermissionGranted = false;

    public void setLocationPermissionGranted(boolean granted){
        mLocationPermissionGranted = granted;
    }

    public void setPhoneCallPermissionGranted(boolean granted){
        mPhoneCallPermissionGranted = granted;
    }

    public boolean getLocationPermissionGranted(){
        return mLocationPermissionGranted;
    }

    public boolean getPhoneCallPermissionGranted(){
        return mPhoneCallPermissionGranted;
    }

}
