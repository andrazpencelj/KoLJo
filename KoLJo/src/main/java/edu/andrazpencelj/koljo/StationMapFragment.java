package edu.andrazpencelj.koljo;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Andraž on 8.11.2013.
 */
public class StationMapFragment extends Fragment implements GoogleMap.InfoWindowAdapter, GoogleMap.OnMarkerClickListener{

    private HashMap<String,BikeStation> mBikeStationData = null;
    private GoogleMap mGoogleMap;
    private RequestQueue mRequestQueue;
    private ProgressBar mProgressBar;
    private final String REQUEST_TAG = "KOLJO_REQUEST";

    /* implementiramo interface za ugotavljanje dogodkov */
    OnMapMarkerClickListener mCallback;

    public interface OnMapMarkerClickListener{
        public void onMapMarkerClickListener(String name,int free_bikes,int free_spaces,int capacity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_map_layout,container,false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map_menu, menu);
    }

    @Override
    public void onStart(){
        super.onStart();
        mRequestQueue = Volley.newRequestQueue(getActivity());
        mProgressBar = (ProgressBar)getActivity().findViewById(R.id.prograss_bar);
        mGoogleMap = ((SupportMapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);
        setStartLocationOfMap();
    }

    @Override
    public void onPause(){
        super.onPause();
        mRequestQueue.cancelAll(REQUEST_TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.bringToFront();
                getServerData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnMapMarkerClickListener) activity;
        } catch (ClassCastException e) {
            Log.d("ERROR",e.toString());
        }
    }

    public void setStartLocationOfMap(){
        /* dolocimo zacetno pozicijo zemljevida */
        LatLng ljubljana = new LatLng(46.056451,14.508070);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ljubljana,13));
    }

    public void setMarkers(){
        /* prikazemo markerje postaj */
        mGoogleMap.setInfoWindowAdapter(this);
        mGoogleMap.setOnMarkerClickListener(this);
        for (String key : mBikeStationData.keySet()){
            BikeStation station = (BikeStation)mBikeStationData.get(key);
            LatLng markerLocation = new LatLng(station.getStationLatitude(),station.getStationLongitude());
            //pretvorimo podatke  v JSON da jih lahko posredujemo markerju
            JSONObject json = new JSONObject();
            try{
                json.put("name",station.getStationName());
                json.put("free_bikes",Integer.toString(station.getStationData().getNumberOfFreeBikes()));
                json.put("free_spaces", Integer.toString(station.getStationData().getNumberOfFreeSpaces()));
                json.put("capacity",Integer.toString(station.getStationData().getCapacity()));
            }
            catch (Exception e){
                Log.d("ERROR","aba "+e.toString());
            }
            if (station.getStationData().getNumberOfFreeBikes()>0){
                mGoogleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)).snippet(json.toString()).position(markerLocation));
            }
            else{
                mGoogleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_red)).snippet(json.toString()).position(markerLocation));

            }
        }
    }

    public void getServerData(){
        /*  prenesejo se podatki iz streznika */
        String mUrl = "http://opendata.si/promet/bicikelj/list/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,mUrl,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                mBikeStationData = parseJSON(jsonObject);
                setMarkers();
                Log.d("MESSAGE","END SERVER DATA");
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Log.d("ERROR", volleyError.toString());
            }
        });
        jsonObjectRequest.setTag(REQUEST_TAG);
        mRequestQueue.add(jsonObjectRequest);
    }

    public HashMap parseJSON(JSONObject jsonObject){
        HashMap<String,BikeStation> data = new HashMap<String, BikeStation>();
        try{
            JSONObject markers = jsonObject.getJSONObject("markers");
            for (int i=1;i<markers.length();i++){
                JSONObject bikeStationItem = markers.getJSONObject(Integer.toString(i));
                JSONObject stationItem = bikeStationItem.getJSONObject("station");

                int total = Integer.parseInt(stationItem.getString("total"));
                int available = Integer.parseInt(stationItem.getString("available"));
                int free = Integer.parseInt(stationItem.getString("free"));
                Station station = new Station(total,available,free);

                String address = bikeStationItem.getString("fullAddress");
                double latitude = Double.parseDouble(bikeStationItem.getString("lat"));
                double longitude = Double.parseDouble(bikeStationItem.getString("lng"));
                String name = bikeStationItem.getString("name");
                int number = i;
                int open = Integer.parseInt(bikeStationItem.getString("open"));
                BikeStation bikeStation = new BikeStation(address,station,latitude,longitude,name,number,open);

                data.put(""+i,bikeStation);
            }
        }
        catch (Exception e){
            Log.d("ERROR",e.toString());
        }
        return data;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //pretvorimo podatke iz marker.snippet, ki so v obliki dolgega stringa
        try{
            JSONObject json = new JSONObject(marker.getSnippet());
            String name = json.getString("name");
            int free_bikes = Integer.parseInt(json.getString("free_bikes"));
            int free_spaces = Integer.parseInt(json.getString("free_spaces"));
            int capacity = Integer.parseInt(json.getString("capacity"));
            mCallback.onMapMarkerClickListener(name,free_bikes,free_spaces,capacity);
        }
        catch (Exception e){
            Log.d("ERROR",e.toString());
        }
        return false;
    }
}
