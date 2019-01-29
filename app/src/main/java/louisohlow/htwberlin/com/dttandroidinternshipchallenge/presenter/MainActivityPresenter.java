package louisohlow.htwberlin.com.dttandroidinternshipchallenge.presenter;

import android.content.Context;
import android.content.Intent;

import louisohlow.htwberlin.com.dttandroidinternshipchallenge.R;
import louisohlow.htwberlin.com.dttandroidinternshipchallenge.views.MapsActivity;

/**
 * @author      Louis Ohlow LouisOhlow@gmail.com>
 * @version     1.0
 */
public class MainActivityPresenter {

    private Context mainContext;
    private MainView mainView;


    /**
     * initializes the View, Context and Activity from MainActivity to access it
     *
     * @param view of the MainActivity
     * @param context of the MainActivity
     */
    public MainActivityPresenter(MainView view, Context context) {
        mainView = view;
        mainContext = context;
    }

    /**
     * method to invoke potential initializes
     */
    public void start() {
    }

    /**
     * decides what to do if buttons get pressed on the MapsActivity View
     *
     * @param buttonName of the button which is pressed
     */
    public void buttonPressed(String buttonName) {
        switch (buttonName) {
            case "infoDlgPos":
                mainView.hideDialog(mainContext.getString(R.string.infoDialogName));
                break;
            case "infoButton":
                mainView.showDialog(mainContext.getString(R.string.infoDialogName));
                break;
            case "openMapButton":
                Intent gotToMapActivity = new Intent(mainContext, MapsActivity.class);
                mainContext.startActivity(gotToMapActivity);

        }
    }
    /**
     * View Interface for the MapsActivity class to call it's methods
     */
    public interface MainView {
        void showDialog(String dialogName);

        void hideDialog(String dialogName);

    }

}



