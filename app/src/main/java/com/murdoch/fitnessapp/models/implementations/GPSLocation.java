package com.murdoch.fitnessapp.models.implementations;

import android.os.Parcel;
import android.os.Parcelable;

import com.murdoch.fitnessapp.models.interfaces.IGPSLocation;

/**
 * This class is used for storing geo-location data
 *
 * */
public class GPSLocation implements IGPSLocation, Parcelable
{
    public static final double LATITUDE_MIN_INCLUSIVE = -90;
    public static final double LATITUDE_MAX_INCLUSIVE = 90;

    public static final double LONGITUDE_MIN_INCLUSIVE = -180;
    public static final double LONGITUDE_MAX_INCLUSIVE = 180;

    public static final double LATITUDE_DEFAULT_VALUE = 0;
    public static final double LONGITUDE_DEFAULT_VALUE = 0;


    private double latitude = LATITUDE_DEFAULT_VALUE;
    private double longitude = LONGITUDE_DEFAULT_VALUE;


    /**
     * Initializes a GPSLocation with latitude and longitude set to
     * LATITUDE_DEFAULT_VALUE and LONGITUDE_DEFAULT_VALUE respectively
     *
     * @see #LATITUDE_DEFAULT_VALUE
     * @see #LONGITUDE_DEFAULT_VALUE
     * */
    public GPSLocation()
    {

    }


    /**
     * Initializes a GPSLocation with latitude and longitude set to
     * the values provided in the argument
     *
     * @param latitudeValue the initial value of the latitude
     * @param longitudeValue the initial value of the longitude
     *
     * @throws IllegalArgumentException if latitudeValue or longitudeValue
     * is outside the range of acceptable values
     *
     * @see #setLatitude(double) 
     * @see #setLongitude(double)
     * */
    public GPSLocation(double latitudeValue, double longitudeValue)
    {
        this.setLatitude(latitudeValue);
        this.setLongitude(longitudeValue);
    }


    /**
     * Sets the value of the latitude
     *
     * @param latitudeValue the value of latitude to be set
     *
     * @throws IllegalArgumentException if latitudeValue is
     * less than LATITUDE_MIN_INCLUSIVE or more than
     * LATITUDE_MAX_INCLUSIVE
     *
     * @see #LATITUDE_MIN_INCLUSIVE
     * @see #LATITUDE_MAX_INCLUSIVE
     * */
    @Override
    public void setLatitude(double latitudeValue)
    {
        if (latitudeValue < LATITUDE_MIN_INCLUSIVE ||
                latitudeValue > LATITUDE_MAX_INCLUSIVE)
        {
            throw new IllegalArgumentException("Latitude outside valid range");
        }

        this.latitude = latitudeValue;
    }


    /**
     * Returns the latitude of the GPSLocation
     * */
    @Override
    public double getLatitude()
    {
        return this.latitude;
    }



    /**
     * Sets the value of the longitude
     *
     * @param longitudeValue the value of longitude to be set
     *
     * @throws IllegalArgumentException if longitudeValue is
     * less than LONGITUDE_MIN_INCLUSIVE or more than
     * LONGITUDE_MAX_INCLUSIVE
     *
     * @see #LONGITUDE_MIN_INCLUSIVE
     * @see #LONGITUDE_MAX_INCLUSIVE
     * */
    @Override
    public void setLongitude(double longitudeValue)
    {
        if (longitudeValue < LONGITUDE_MIN_INCLUSIVE ||
                longitudeValue > LONGITUDE_MAX_INCLUSIVE)
        {
            throw new IllegalArgumentException("Longitude outside valid range");
        }

        this.longitude = longitudeValue;
    }


    /**
     * Returns the longitude of the GPSLocation
     * */
    @Override
    public double getLongitude()
    {
        return this.longitude;
    }


    /**
     * Only returns true if both objects are instances of
     * GPSLocation, and their latitude and longitudes are
     * exactly the same
     *
     * Otherwise, returns false.
     * */
    @Override
    public boolean equals(Object otherObject)
    {
        if (otherObject == this)
        {
            return true;
        }

        if (!(otherObject instanceof GPSLocation))
        {
            return false;
        }


        return this.latitude == ((GPSLocation) otherObject).latitude &&
                this.longitude == ((GPSLocation) otherObject).longitude;
    }



    /**
     * Interface that must be implemented and provided as a public
     * CREATOR field that generates instances of this Parcelable
     * class from a Parcel.
     *
     *  Note: This is part of the Parcelable interface
     * */
    public static final Creator<GPSLocation> CREATOR = new Creator<GPSLocation>()
    {
        @Override
        public GPSLocation createFromParcel(Parcel in)
        {
            return new GPSLocation(in);
        }

        @Override
        public GPSLocation[] newArray(int size)
        {
            return new GPSLocation[size];
        }
    };


    /**
     * Constructor required for the Parcelable interface
     * */
    protected GPSLocation(Parcel in)
    {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }


    /**
     * Flatten this object in to a Parcel.
     *
     * Note: This is part of the Parcelable interface
     * */
    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeDouble(this.latitude);
        parcel.writeDouble(this.longitude);
    }


    /**
     * Describe the kinds of special objects contained in this Parcelable instance's
     * marshaled representation.
     *
     * Note: This is part of the Parcelable interface
     * */
    @Override
    public int describeContents()
    {
        final int DEFAULT_DESCRIPTION = 0;

        return DEFAULT_DESCRIPTION;
    }
}
