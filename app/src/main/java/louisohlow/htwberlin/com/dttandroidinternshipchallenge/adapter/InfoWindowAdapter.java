package louisohlow.htwberlin.com.dttandroidinternshipchallenge.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import louisohlow.htwberlin.com.dttandroidinternshipchallenge.R;

/**
 * @author      Louis Ohlow LouisOhlow@gmail.com>
 * @version     1.0
 */
public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View customWindow;

    @SuppressLint("InflateParams")
    public InfoWindowAdapter(Context context){
        customWindow = LayoutInflater.from(context).inflate(  R.layout.custom_info_window, null);
    }

    /**
     * adds the current title and the snippet to the marker info
     *
     * @param marker the used marker
     * @param view to get the title and the snippet
     */
    private void renderWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView TvTitle = view.findViewById(R.id.tv_title);

        if(!title.equals("")) {
            TvTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView TvSnippet = view.findViewById(R.id.tv_subtitle);

        if (!snippet.equals("")){
            TvSnippet.setText(snippet);
        }
    }

    /**
     * style the info window
     *
     * @param marker the used marker
     * @return the custom window
     */
    @Override
    public View getInfoWindow(Marker marker){
        renderWindowText(marker, customWindow);
        return customWindow;
    }

    /**
     * style the info content
     *
     * @param marker the used marker
     * @return the custom info content
     */
    @Override
    public View getInfoContents(Marker marker){
        renderWindowText(marker, customWindow);
        return customWindow;
    }
}
