package com.murdoch.fitnessapp.models.interfaces;

/**
 * An interface for classes that store geo-location data
 *
 * */
public interface IGPSLocation
{
    /**
     * Sets the value of the latitude
     *
     * @param latitudeValue the value of latitude to be set
     *
     * @throws IllegalArgumentException if latitudeValue is
     * outside valid range
     * */
    void setLatitude(double latitudeValue );


    /**
     * Returns the latitude of the IGPSLocation
     * */
    double getLatitude();


    /**
     * Sets the value of the longitude
     *
     * @param longitudeValue the value of longitude to be set
     *
     * @throws IllegalArgumentException if longitudeValue is
     * outside valid range
     * */
    void setLongitude(double longitudeValue);


    /**
     * Returns the latitude of the IGPSLocation
     * */
    double getLongitude();
}
