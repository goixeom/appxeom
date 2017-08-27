package goixeom.com.maputils;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import goixeom.com.CustomTextView;
import goixeom.com.R;

/**
 * Created by DuongKK on 6/20/2017.
 */

public class DirectionAsynTaskBooking extends AsyncTask<String, Void, String> {

    WeakReference<DirectionCustomModel> directionCustomModelWeakReference;
    WeakReference<GoogleMap> mapWeakReference;
    WeakReference<CustomTextView> tvTimeReference;
    WeakReference<Context> contextWeakReference;
    Polyline polyline;
    public DirectionAsynTaskBooking(Context context, DirectionCustomModel directionCustomModel, GoogleMap googleMap, CustomTextView textViewTime) {
        directionCustomModelWeakReference = new WeakReference<DirectionCustomModel>(directionCustomModel);
        mapWeakReference = new WeakReference<GoogleMap>(googleMap);
        tvTimeReference = new WeakReference<CustomTextView>(textViewTime);
        contextWeakReference = new WeakReference<Context>(context);
    }
    public DirectionAsynTaskBooking(Context context, DirectionCustomModel directionCustomModel, GoogleMap googleMap, CustomTextView textViewTime, Polyline polyline) {
        directionCustomModelWeakReference = new WeakReference<DirectionCustomModel>(directionCustomModel);
        mapWeakReference = new WeakReference<GoogleMap>(googleMap);
        tvTimeReference = new WeakReference<CustomTextView>(textViewTime);
        contextWeakReference = new WeakReference<Context>(context);
        this.polyline = polyline;
    }
    // Downloading data in non-ui thread
    @Override
    protected String doInBackground(String... url) {

        // For storing data from web service
        String data = "";

        try {
            // Fetching the data from web service
            data = downloadUrl(url[0]);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    // Executes in UI thread, after the execution of
    // doInBackground()
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        ParserTask parserTask = new ParserTask();

        // Invokes the thread for parsing the JSON data
        parserTask.execute(result);
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                // Starts parsing data
                DirectionCustomModel directionCustomModel = parser.parse(jObject);
                routes = directionCustomModel.getMapPolyline();
                directionCustomModelWeakReference.get().setDistanceTxt(directionCustomModel.getDistanceTxt());
                directionCustomModelWeakReference.get().setDistance(directionCustomModel.getDistance());
                directionCustomModelWeakReference.get().setDuration(directionCustomModel.getDuration());
                directionCustomModelWeakReference.get().setMapPolyline(directionCustomModel.getMapPolyline());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(15);
                if (contextWeakReference.get() != null)
                    lineOptions.color(ContextCompat.getColor(contextWeakReference.get(),R.color.green));
            }

            // Drawing polyline in the Google Map for the i-th route
            if (mapWeakReference.get() != null && lineOptions!=null)
//                directionCustomModelWeakReference.get().setPolyline(mapWeakReference.get().addPolyline(lineOptions));
            polyline = mapWeakReference.get().addPolyline(lineOptions);
            if (tvTimeReference.get() != null)
                tvTimeReference.get().setText(String.format(contextWeakReference.get().getString(R.string.t_i_xe_ang_n_trong_00_00), directionCustomModelWeakReference.get().getDuration()));
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

}