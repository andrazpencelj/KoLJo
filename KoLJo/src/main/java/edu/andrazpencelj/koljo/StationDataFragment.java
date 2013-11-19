package edu.andrazpencelj.koljo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Andraž on 11.11.2013.
 */
public class StationDataFragment extends Fragment implements View.OnClickListener{

    OnFragmentClickListener mCallback;

    public interface OnFragmentClickListener{
        public void onFragmentClick();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_station_data_layout,container,false);
        view.setOnClickListener(this);
        TextView name = (TextView)view.findViewById(R.id.station_name);
        name.setText(getArguments().getString("name"));
        int free_bikes = getArguments().getInt("free_bikes");
        int free_spaces = getArguments().getInt("free_spaces");
        int capacity = getArguments().getInt("capacity");
        TextView free_bikes_view = (TextView)view.findViewById(R.id.station_free_bikes);
        free_bikes_view.setText(""+free_bikes);
        TextView free_spaces_view = (TextView)view.findViewById(R.id.station_free_spaces);
        free_spaces_view.setText(""+free_spaces);
        ImageView image = (ImageView)view.findViewById(R.id.station_image);
        //izberemo sliko glede na stanje na postaji,
        if (free_bikes > 0){
            image.setBackgroundResource(R.drawable.info_icon);
        }
        else{
            image.setBackgroundResource(R.drawable.info_icon_red);
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnFragmentClickListener) activity;
        } catch (ClassCastException e) {
            Log.d("ERROR",e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        mCallback.onFragmentClick();
    }
}
