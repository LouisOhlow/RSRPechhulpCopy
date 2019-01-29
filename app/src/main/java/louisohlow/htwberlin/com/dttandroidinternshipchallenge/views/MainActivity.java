package louisohlow.htwberlin.com.dttandroidinternshipchallenge.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import louisohlow.htwberlin.com.dttandroidinternshipchallenge.R;
import louisohlow.htwberlin.com.dttandroidinternshipchallenge.presenter.MainActivityPresenter;

/**
 * @author      Louis Ohlow LouisOhlow@gmail.com>
 * @version     1.0
 */
public class MainActivity extends AppCompatActivity implements MainActivityPresenter.MainView {

    /**
     * Description of the variable here.
     */
    AlertDialog infoDialog;
    /**
     * Info Button on the top right of the main activity
     */
    ImageButton infoButton;
    /**
     * RSR Pechhulp Button which leads to the map activity
     */
    Button openMapButton;
    /**
     * Info Button for the tablet view in the center
     */
    Button infoButtonTablet;

    MainActivityPresenter presenter;

    /**
     * method which gets called when the activity is build
     *
     * sets the content View and calls all important
     * methods for starting the activity
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainActivityPresenter(this, this);

        assignDialogs();
        assignButtons();
    }

    /**
     * assigns all the necessary dialogs
     */
    private void assignDialogs() {
        final SpannableString s = new SpannableString(getResources().getString(R.string.infoLink));
        final TextView tx1 = new TextView(this);
        tx1.setText(s);
        tx1.setGravity(Gravity.CENTER);
        tx1.setAutoLinkMask(RESULT_OK);
        tx1.setMovementMethod(LinkMovementMethod.getInstance());
        Linkify.addLinks(s, Linkify.WEB_URLS);
        infoDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(getResources().getString(R.string.infoDialogTitle))
                .setMessage(getResources().getString(R.string.infoDialogMessage))
                .setPositiveButton(getResources().getString(R.string.infoDialogPositiveButton), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.buttonPressed("infoDlgPos");
                    }
                })
                .setView(tx1).create();
        infoDialog.setCanceledOnTouchOutside(false);
        infoDialog.setOnShowListener(new DialogInterface.OnShowListener(){
            @Override
            public void onShow(DialogInterface arg0) {
                infoDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
            }
        });
    }

    /**
     * assigns all the necessary buttons
     */
    private void assignButtons() {

        infoButton = findViewById(R.id.infoImageButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.buttonPressed("infoButton");
            }
        });

        openMapButton = findViewById(R.id.openMapButton);
        openMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.buttonPressed("openMapButton");
            }
        });

        infoButtonTablet = findViewById(R.id.infoButtonTablet);
        infoButtonTablet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDialog.show();
            }
        });
    }

    /**
     * shows the dialog based on the DialogName
     *
     * @param dialogName selects the specific dialog
     */
    @Override
    public void showDialog(String dialogName) {
        switch(dialogName){
            case "infoDialog":
                infoDialog.show();
        }
    }

    /**
     * hides the dialog based on the DialogName
     *
     * @param dialogName selects the specific dialog
     */
    @Override
    public void hideDialog(String dialogName) {
        switch(dialogName){
            case "infoDialog":
                infoDialog.dismiss();
        }

    }

}




