package com.murdoch.fitnessapp;

import org.junit.Test;

import static org.junit.Assert.*;

import com.murdoch.fitnessapp.models.implementations.GPSLocation;

import java.util.ArrayList;

/**
 * Contains the unit test of various methods of the GPSLocation class
 */
public class GPSLocationUnitTest
{

    /**
     * Test the default constructor initializes the latitude and longitude to the default values
     * */
    @Test
    public void defaultConstructor_NoException()
    {
        GPSLocation gpsLocation = new GPSLocation();

        final int MAXIMUM_DELTA = 0;

        assertEquals(GPSLocation.LATITUDE_DEFAULT_VALUE, gpsLocation.getLatitude(), MAXIMUM_DELTA);

        assertEquals(GPSLocation.LONGITUDE_DEFAULT_VALUE, gpsLocation.getLongitude(), MAXIMUM_DELTA);
    }


    /**
     * Test the parameterized constructor correctly initializes random valid positive latitude
     *  and random valid positive longitude to the values provided in the arguments
     * */
    @Test
    public void parameterizedConstructor_ValidPositiveLatitudeAndPositiveLongitude_NoException()
    {
        final double RANDOM_VALID_POSITIVE_LATITUDE = 85.65;
        final double RANDOM_VALID_POSITIVE_LONGITUDE = 152.23;

        GPSLocation gpsLocation = new GPSLocation(RANDOM_VALID_POSITIVE_LATITUDE,
                RANDOM_VALID_POSITIVE_LONGITUDE);

        final int MAXIMUM_DELTA = 0;

        assertEquals(RANDOM_VALID_POSITIVE_LATITUDE, gpsLocation.getLatitude(), MAXIMUM_DELTA);
        assertEquals(RANDOM_VALID_POSITIVE_LONGITUDE, gpsLocation.getLongitude(), MAXIMUM_DELTA);
    }


    /**
     * Test the parameterized constructor correctly initializes random valid negative latitude
     *  and random valid negative longitude to the values provided in the arguments
     * */
    @Test
    public void parameterizedConstructor_ValidNegativeLatitudeAndNegativeLongitude_NoException()
    {
        final double RANDOM_VALID_NEGATIVE_LATITUDE = -85.65;
        final double RANDOM_VALID_NEGATIVE_LONGITUDE = -152.23;

        GPSLocation gpsLocation = new GPSLocation(RANDOM_VALID_NEGATIVE_LATITUDE,
                RANDOM_VALID_NEGATIVE_LONGITUDE);

        final int MAXIMUM_DELTA = 0;

        assertEquals(RANDOM_VALID_NEGATIVE_LATITUDE, gpsLocation.getLatitude(), MAXIMUM_DELTA);
        assertEquals(RANDOM_VALID_NEGATIVE_LONGITUDE, gpsLocation.getLongitude(), MAXIMUM_DELTA);
    }


    /**
     * Test the parameterized constructor correctly initializes valid zero-value latitude
     *  and valid zero-value negative longitude to the values provided in the arguments
     * */
    @Test
    public void parameterizedConstructor_ValidZeroLatitudeAndZeroLongitude_NoException()
    {
        final double RANDOM_VALID_ZERO_LATITUDE = 0.00;
        final double RANDOM_VALID_ZERO_LONGITUDE = 0.00;

        GPSLocation gpsLocation = new GPSLocation(RANDOM_VALID_ZERO_LATITUDE,
                RANDOM_VALID_ZERO_LONGITUDE);

        final int MAXIMUM_DELTA = 0;

        assertEquals(RANDOM_VALID_ZERO_LATITUDE, gpsLocation.getLatitude(), MAXIMUM_DELTA);
        assertEquals(RANDOM_VALID_ZERO_LONGITUDE, gpsLocation.getLongitude(), MAXIMUM_DELTA);
    }


    /**
     * Test the parameterized constructor correctly throws an IllegalArgumentException
     *  when an invalid positive latitude (outside range of -90 <= latitude <= 90 is
     *  used
     * */
    @Test
    public void parameterizedConstructor_InvalidPositiveLatitude_IllegalArgumentException()
    {
        final double RANDOM_INVALID_POSITIVE_LATITUDE = 102.65;
        final double RANDOM_VALID_LONGITUDE = -152.23;

        assertThrows(IllegalArgumentException.class,
                ()-> new GPSLocation(RANDOM_INVALID_POSITIVE_LATITUDE,
                             RANDOM_VALID_LONGITUDE));

    }



    /**
     * Test the parameterized constructor correctly throws an IllegalArgumentException
     *  when an invalid negative latitude (outside range of -90 <= latitude <= 90 is
     *  used
     * */
    @Test
    public void parameterizedConstructor_InvalidNegativeLatitude_IllegalArgumentException()
    {
        final double RANDOM_INVALID_NEGATIVE_LATITUDE = -99.65;
        final double RANDOM_VALID_LONGITUDE = -152.23;

        assertThrows(IllegalArgumentException.class,
                ()-> new GPSLocation(RANDOM_INVALID_NEGATIVE_LATITUDE,
                        RANDOM_VALID_LONGITUDE));

    }



    /**
     * Test the parameterized constructor correctly throws an IllegalArgumentException
     *  when an invalid positive longitude (outside range of -180 <= longitude <= 180 is
     *  used
     * */
    @Test
    public void parameterizedConstructor_InvalidPositiveLongitude_IllegalArgumentException()
    {
        final double RANDOM_VALID_LATITUDE = 23.65;
        final double RANDOM_INVALID_POSITIVE_LONGITUDE = 199.23;


        assertThrows(IllegalArgumentException.class,
                ()-> new GPSLocation(RANDOM_VALID_LATITUDE,
                        RANDOM_INVALID_POSITIVE_LONGITUDE));

    }



    /**
     * Test the parameterized constructor correctly throws an IllegalArgumentException
     *  when an invalid negative longitude (outside range of -180 <= longitude <= 180 is
     *  used
     * */
    @Test
    public void parameterizedConstructor_InvalidNegativeLongitude_IllegalArgumentException()
    {
        final double RANDOM_VALID_LATITUDE = 23.65;
        final double RANDOM_INVALID_NEGATIVE_LONGITUDE = -199.23;

        assertThrows(IllegalArgumentException.class,
                ()-> new GPSLocation(RANDOM_VALID_LATITUDE,
                        RANDOM_INVALID_NEGATIVE_LONGITUDE));

    }



    /**
     * Test the setLatitude(double)  correctly sets the latitude when the latitude
     * value is valid positive latitude (valid latitude is in range of -90 <= latitude <= 90)
     *
     * */
    @Test
    public void setLatitude_ValidPositiveLatitude_NoException()
    {
        final double RANDOM_VALID_POSITIVE_LATITUDE = 25.55;

        GPSLocation gpsLocation = new GPSLocation();

        gpsLocation.setLatitude(RANDOM_VALID_POSITIVE_LATITUDE);

        final int MAXIMUM_DELTA = 0;

        assertEquals(RANDOM_VALID_POSITIVE_LATITUDE, gpsLocation.getLatitude(), MAXIMUM_DELTA);
    }


    /**
     * Test the setLatitude(double)  correctly sets the latitude when the latitude
     * value is valid negative latitude (valid latitude is in range of -90 <= latitude <= 90)
     *
     * */
    @Test
    public void setLatitude_ValidNegativeLatitude_NoException()
    {
        final double RANDOM_VALID_NEGATIVE_LATITUDE = -77.66;

        GPSLocation gpsLocation = new GPSLocation();

        gpsLocation.setLatitude(RANDOM_VALID_NEGATIVE_LATITUDE);

        final int MAXIMUM_DELTA = 0;

        assertEquals(RANDOM_VALID_NEGATIVE_LATITUDE, gpsLocation.getLatitude(), MAXIMUM_DELTA);
    }



    /**
     * Test the setLatitude(double)  correctly sets the latitude when the latitude
     * value is valid zero-value latitude (valid latitude is in range of -90 <= latitude <= 90)
     *
     * */
    @Test
    public void setLatitude_ValidZeroLatitude_NoException()
    {
        final double RANDOM_VALID_ZERO_LATITUDE = 0.000;

        GPSLocation gpsLocation = new GPSLocation();

        gpsLocation.setLatitude(RANDOM_VALID_ZERO_LATITUDE);


        final int MAXIMUM_DELTA = 0;

        assertEquals(RANDOM_VALID_ZERO_LATITUDE, gpsLocation.getLatitude(), MAXIMUM_DELTA);
    }



    /**
     * Test the setLatitude(double) correctly throws an IllegalArgumentException when the latitude
     * value is invalid positive latitude (valid latitude is in range of -90 <= latitude <= 90)
     *
     * */
    @Test
    public void setLatitude_InvalidPositiveLatitude_IllegalArgumentException()
    {
        GPSLocation gpsLocation = new GPSLocation();

        final double RANDOM_INVALID_POSITIVE_LATITUDE = 129.22;

        assertThrows(IllegalArgumentException.class,
                ()-> gpsLocation.setLatitude(RANDOM_INVALID_POSITIVE_LATITUDE));
    }


    /**
     * Test the setLatitude(double) correctly throws an IllegalArgumentException when the latitude
     * value is invalid negative latitude (valid latitude is in range of -90 <= latitude <= 90)
     *
     * */
    @Test
    public void setLatitude_InvalidNegativeLatitude_IllegalArgumentException()
    {
        GPSLocation gpsLocation = new GPSLocation();

        final double RANDOM_INVALID_NEGATIVE_LATITUDE = -102.34;

        assertThrows(IllegalArgumentException.class,
                ()-> gpsLocation.setLatitude(RANDOM_INVALID_NEGATIVE_LATITUDE));
    }


    /**
     * Test the setLongitude(double)  correctly sets the longitude when the longitude
     * value is valid positive longitude (valid latitude is in range of -180 <= longitude <= 180)
     *
     * */
    @Test
    public void setLongitude_ValidPositiveLongitude_NoException()
    {
        final double RANDOM_VALID_POSITIVE_LONGITUDE = 111.55;

        GPSLocation gpsLocation = new GPSLocation();

        gpsLocation.setLongitude(RANDOM_VALID_POSITIVE_LONGITUDE);

        final int MAXIMUM_DELTA = 0;

        assertEquals(RANDOM_VALID_POSITIVE_LONGITUDE, gpsLocation.getLongitude(), MAXIMUM_DELTA);
    }


    /**
     * Test the setLongitude(double)  correctly sets the longitude when the longitude
     * value is valid negative longitude (valid latitude is in range of -180 <= longitude <= 180)
     *
     * */
    @Test
    public void setLongitude_ValidNegativeLongitude_NoException()
    {
        final double RANDOM_VALID_NEGATIVE_LONGITUDE = -144.32;

        GPSLocation gpsLocation = new GPSLocation();

        gpsLocation.setLongitude(RANDOM_VALID_NEGATIVE_LONGITUDE);


        final int MAXIMUM_DELTA = 0;

        assertEquals(RANDOM_VALID_NEGATIVE_LONGITUDE, gpsLocation.getLongitude(), MAXIMUM_DELTA);
    }



    /**
     * Test the setLongitude(double)  correctly sets the longitude when the longitude
     * value is valid zero-value longitude (valid latitude is in range of -180 <= longitude <= 180)
     *
     * */
    @Test
    public void setLongitude_ValidZeroLongitude_NoException()
    {
        final double RANDOM_VALID_ZERO_LONGITUDE = 0;

        GPSLocation gpsLocation = new GPSLocation();

        gpsLocation.setLongitude(RANDOM_VALID_ZERO_LONGITUDE);


        final int MAXIMUM_DELTA = 0;

        assertEquals(RANDOM_VALID_ZERO_LONGITUDE, gpsLocation.getLongitude(), MAXIMUM_DELTA);
    }


    /**
     * Test the setLongitude(double) correctly throws an IllegalArgumentException when the longitude
     * value is invalid positive longitude (valid latitude is in range of -180 <= longitude <= 180)
     *
     * */
    @Test
    public void setLongitude_InvalidPositiveLongitude_IllegalArgumentException()
    {
        GPSLocation gpsLocation = new GPSLocation();

        final double RANDOM_INVALID_POSITIVE_LONGITUDE = 202.22;

        assertThrows(IllegalArgumentException.class,
                ()-> gpsLocation.setLongitude(RANDOM_INVALID_POSITIVE_LONGITUDE));
    }


    /**
     * Test the setLongitude(double) correctly throws an IllegalArgumentException when the longitude
     * value is invalid negative longitude (valid latitude is in range of -180 <= longitude <= 180)
     *
     * */
    @Test
    public void setLongitude_InvalidNegativeLongitude_IllegalArgumentException()
    {
        GPSLocation gpsLocation = new GPSLocation();

        final double RANDOM_INVALID_NEGATIVE_LONGITUDE = -299.22;

        assertThrows(IllegalArgumentException.class,
                ()-> gpsLocation.setLongitude(RANDOM_INVALID_NEGATIVE_LONGITUDE));
    }


    /**
     * Test the overridden equals(Object) correctly returns true when both latitude and
     * longitude are exactly the same
     * */
    @Test
    public void equals_TwoExactSameGPSLocation_AreEqual()
    {
        final double RANDOM_LATITUDE_1 = 12.34;
        final double RANDOM_LONGITUDE_1 = 45.67;

        final GPSLocation RANDOM_GPS_LOCATION_1 = new GPSLocation(RANDOM_LATITUDE_1,
                RANDOM_LONGITUDE_1);

        final GPSLocation RANDOM_GPS_LOCATION_2 = new GPSLocation(RANDOM_LATITUDE_1,
                RANDOM_LONGITUDE_1);


        assertTrue(RANDOM_GPS_LOCATION_1.equals(RANDOM_GPS_LOCATION_2));
    }


    /**
     * Test the overridden equals(Object) correctly returns false when the latitude is different,
     * while keeping the longitude the same
     * */
    @Test
    public void equals_DifferentLatitude_NotEqual()
    {
        final double RANDOM_LATITUDE_1 = 12.34;
        final double RANDOM_LONGITUDE = 45.67;

        final GPSLocation RANDOM_GPS_LOCATION_1 = new GPSLocation(RANDOM_LATITUDE_1,
                                                                  RANDOM_LONGITUDE);

        final double RANDOM_LATITUDE_2 = 87.65;

        GPSLocation RANDOM_GPS_LOCATION_2 = new GPSLocation(RANDOM_LATITUDE_2,
                                                            RANDOM_LONGITUDE);


        assertFalse(RANDOM_GPS_LOCATION_1.equals(RANDOM_GPS_LOCATION_2));
    }


    /**
     * Test the overridden equals(Object) correctly returns false when the longitude is different,
     * while keeping the latitude the same
     * */
    @Test
    public void equals_DifferentLongitude_NotEqual()
    {
        final double RANDOM_LATITUDE = 12.34;
        final double RANDOM_LONGITUDE_1 = 45.67;

        final GPSLocation RANDOM_GPS_LOCATION_1 = new GPSLocation(RANDOM_LATITUDE,
                                                                  RANDOM_LONGITUDE_1);

        final double RANDOM_LONGITUDE_2 = 112.85;

        final GPSLocation RANDOM_GPS_LOCATION_2 = new GPSLocation(RANDOM_LATITUDE,
                                                                  RANDOM_LONGITUDE_2);

        assertFalse(RANDOM_GPS_LOCATION_1.equals(RANDOM_GPS_LOCATION_2));
    }



    /**
     * Test the overridden equals(Object) correctly returns false when the argument is null
     * */
    @Test
    public void equals_NullArgument_NotEqual()
    {
        final double RANDOM_LATITUDE_1 = 12.34;
        final double RANDOM_LONGITUDE_1 = 45.67;

        final GPSLocation RANDOM_GPS_LOCATION_1 = new GPSLocation(RANDOM_LATITUDE_1,
                                                                  RANDOM_LONGITUDE_1);

        assertFalse(RANDOM_GPS_LOCATION_1.equals(null));
    }


    /**
     * Test the overridden equals(Object) correctly returns false when the argument is not
     * an instance of GPSLocation
     * */
    @Test
    public void equals_UnrelatedClassArgument_NotEqual()
    {
        final double RANDOM_LATITUDE_1 = 12.34;
        final double RANDOM_LONGITUDE_1 = 45.67;

        final GPSLocation RANDOM_GPS_LOCATION_1 = new GPSLocation(RANDOM_LATITUDE_1,
                RANDOM_LONGITUDE_1);


        final ArrayList<String> UNRELATED_CLASS_OBJECT = new ArrayList<>();

        assertFalse(RANDOM_GPS_LOCATION_1.equals(UNRELATED_CLASS_OBJECT));
    }



    /**
     * Test the describeContents() method of the Parcelable interface correctly returns its
     * default value
     * */
    @Test
    public void describeContents_DefaultDescription_NoException()
    {
        GPSLocation gpsLocation = new GPSLocation();

        final int EXPECTED_DESCRIPTION_VALUE = 0;

        assertEquals(EXPECTED_DESCRIPTION_VALUE,
                    gpsLocation.describeContents());
    }


}