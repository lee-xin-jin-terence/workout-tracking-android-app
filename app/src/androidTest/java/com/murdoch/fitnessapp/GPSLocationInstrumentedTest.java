package com.murdoch.fitnessapp;

import static org.junit.Assert.assertEquals;

import android.os.Parcel;

import com.murdoch.fitnessapp.models.implementations.GPSLocation;

import org.junit.Test;

/**
 * Instrumented tests for the GPSLocation class, testing methods related
 * to the Parcelable interface
 * */
public class GPSLocationInstrumentedTest
{
    /**
     * Test that the writeToParcel and static Creator methods (part of Parcelable interface)
     * are correctly creating flattening an instance of GPSLocation into a Parcel, and
     * re-creating it back into an instance of GPSLocation
     * */
    @Test
    public void writeToParcel_and_StaticCreator_ValidValues_NoException()
    {
        Parcel parcel = Parcel.obtain();

        final double RANDOM_VALID_LATITUDE = 12.34;
        final double RANDOM_VALID_LONGITUDE = 56.78;

        GPSLocation ORIGINAL_GPS_LOCATION = new GPSLocation(RANDOM_VALID_LATITUDE,
                RANDOM_VALID_LONGITUDE);


        ORIGINAL_GPS_LOCATION.writeToParcel(parcel,
                            ORIGINAL_GPS_LOCATION.describeContents());

        final int STARTING_POSITION = 0;

        parcel.setDataPosition(STARTING_POSITION);

        GPSLocation createdFromParcel =
                GPSLocation.CREATOR.createFromParcel(parcel);


        assertEquals(ORIGINAL_GPS_LOCATION, createdFromParcel);

    }
}
