package edu.andrazpencelj.koljo;

/**
 * Created by Andraž on 7.11.2013.
 */
public class Station {

    /*
        mCapacity - stevilo vseh mest na tej postaji
        mFreeBike - stevilo prostih koles na tej postaji
        mFreeSpace - stevilo prostih mesta za kolesa na tej postaji
     */

    private int mCapacity;
    private int mFreeBike;
    private int mFreeSpace;


    public Station(int capacity,int freeBike, int freeSpace){
        this.mCapacity = capacity;
        this.mFreeBike = freeBike;
        this.mFreeSpace = freeSpace;
    }

    public void setCapacity(int capacity){
        this.mCapacity = capacity;
    }

    public void setFreeBikes(int freeBike){
        this.mFreeBike = freeBike;
    }

    public void setFreeSpaces(int freeSpace){
        this.mFreeSpace = freeSpace;
    }

    public int getCapacity(){
        return this.mCapacity;
    }

    public  int getNumberOfFreeBikes(){
        return  this.mFreeBike;
    }

    public int getNumberOfFreeSpaces(){
        return  this.mFreeSpace;
    }

}
