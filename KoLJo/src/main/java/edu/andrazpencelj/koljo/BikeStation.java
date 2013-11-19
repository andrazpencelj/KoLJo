package edu.andrazpencelj.koljo;

/**
 * Created by Andraž on 7.11.2013.
 */
public class BikeStation {

    /*
        mStationAddress - naslov postaje
        mStationName - ime postaje
        mStationNumber - številka postaje
        mStationLatitude - geografska širina postaje
        mStationLongitude - geografska dolzina postaje
        mStationData - podatki o prostih kolesih, prostih mestih
        mStationOpen - ali postaja deluje
     */

    private String mStationAddress;
    private Station mStationData;
    private double mStationLatitude;
    private double mStationLongitude;
    private String mStationName;
    private int mStationNumber;
    private int mStationOpen;

    public BikeStation(String stationAddress,Station stationData, double stationLatitude, double stationLongitude, String stationName, int stationNumber, int stationOpen){
        this.mStationAddress = stationAddress;
        this.mStationData = stationData;
        this.mStationLatitude = stationLatitude;
        this.mStationLongitude = stationLongitude;
        this.mStationName = stationName;
        this.mStationNumber = stationNumber;
        this.mStationOpen = stationOpen;
    }

    public String getStationAddress(){
        return this.mStationAddress;
    }

    public Station getStationData(){
        return this.mStationData;
    }

    public double getStationLatitude(){
        return this.mStationLatitude;
    }

    public double getStationLongitude(){
        return this.mStationLongitude;
    }

    public String getStationName(){
        return this.mStationName;
    }

    public int getStationNumber(){
        return this.mStationNumber;
    }

    public int getStationOpen(){
        return this.mStationOpen;
    }


}
