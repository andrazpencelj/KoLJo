package edu.andrazpencelj.koljo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

public class MapActivity extends ActionBarActivity implements StationMapFragment.OnMapMarkerClickListener, StationDataFragment.OnFragmentClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_layout);
        //preverimo ce ima aplikacija do spleta
        if (checkConnection()){
            //naprava je povezana zato prikazemo karto
            if (getSupportFragmentManager().findFragmentByTag("MAP") == null){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new StationMapFragment(),"MAP")
                        .commit();
            }
        }
        else{
            //naprava ni povezana zato prikazemo obvestilo
            ConnectionDialogFragment dialog = new ConnectionDialogFragment();
            dialog.show(getSupportFragmentManager(),"CONNECTION");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onMapMarkerClickListener(String name,int free_bikes,int free_spaces,int capacity) {
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        bundle.putInt("free_bikes",free_bikes);
        bundle.putInt("free_spaces",free_spaces);
        bundle.putInt("capacity",capacity);
        StationDataFragment stationDataFragment = new StationDataFragment();
        stationDataFragment.setArguments(bundle);
        StationDataFragment oldFragment = (StationDataFragment)getSupportFragmentManager().findFragmentByTag("INFO_DATA");
        if (oldFragment == null){
            //fragment se ne obstaja, ko ga dodamo ga dodamo tudi na back sklad
            getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right,android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                                        .add(R.id.container, stationDataFragment, "INFO_DATA")
                                        .addToBackStack(null)
                                        .commit();
        }
        else {
            //fragment ze obstaja zato ga samo nadomestimo
            getSupportFragmentManager().beginTransaction()
                                        .remove(oldFragment)
                                        .commit();
            //stari fragment odstranimo s sklada
            getSupportFragmentManager().popBackStack();

            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right,android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                    .add(R.id.container, stationDataFragment, "INFO_DATA")
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onFragmentClick() {
        /* odstranimo fragment (ze obstaja) */
        StationDataFragment stationDataFragment = (StationDataFragment)getSupportFragmentManager().findFragmentByTag("INFO_DATA");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(stationDataFragment);
        getSupportFragmentManager().popBackStack();
        fragmentTransaction.commit();
    }

    private boolean checkConnection(){
        //preverimo ce je naprava povezana s spletom
        ConnectivityManager manager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        boolean connetion = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return  connetion;
    }

}
