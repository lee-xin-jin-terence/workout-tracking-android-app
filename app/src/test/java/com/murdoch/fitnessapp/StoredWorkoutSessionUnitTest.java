package com.murdoch.fitnessapp;

import org.junit.Test;

import static org.junit.Assert.*;

import com.murdoch.fitnessapp.models.implementations.GPSLocation;
import com.murdoch.fitnessapp.models.implementations.StoredWorkoutSession;
import com.murdoch.fitnessapp.models.implementations.WorkoutSession;
import com.murdoch.fitnessapp.models.interfaces.IGPSLocation;
import com.murdoch.fitnessapp.models.interfaces.IWorkoutSession;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Contains the unit test of various methods of the StoredWorkoutSession class
 */
public class StoredWorkoutSessionUnitTest
{

    /**
     * Test the default constructor initializes the workout session id to the default values
     * */
    @Test
    public void defaultConstructor_NoException()
    {
        StoredWorkoutSession storedWorkoutSession = new StoredWorkoutSession();

        assertEquals(StoredWorkoutSession.WORKOUT_SESSION_ID_DEFAULT_VALUE,
                     storedWorkoutSession.getWorkoutSessionId());
    }


    /**
     * Test the setWorkoutSessionId(long)  correctly sets the workout session id
     * value is valid positive workout session id (valid workout session id is >= 0 )
     *
     * */
    @Test
    public void setWorkoutSessionId_ValidPositiveWorkoutSessionId_NoException()
    {
        final long RANDOM_VALID_POSITIVE_WORKOUT_SESSION_ID = 7;

        StoredWorkoutSession storedWorkoutSession = new StoredWorkoutSession();

        storedWorkoutSession.setWorkoutSessionId(RANDOM_VALID_POSITIVE_WORKOUT_SESSION_ID);

        assertEquals(RANDOM_VALID_POSITIVE_WORKOUT_SESSION_ID,
                     storedWorkoutSession.getWorkoutSessionId());
    }


    /**
     * Test the setWorkoutSessionId(long)  correctly sets the workout session id
     * value is valid zero-value workout session id (valid workout session id is  >= 0 )
     *
     * */
    @Test
    public void setWorkoutSessionId_ValidZeroWorkoutSessionId_NoException() {
        final long RANDOM_VALID_ZERO_WORKOUT_SESSION_ID = 0;

        StoredWorkoutSession storedWorkoutSession = new StoredWorkoutSession();

        storedWorkoutSession.setWorkoutSessionId(RANDOM_VALID_ZERO_WORKOUT_SESSION_ID);

        assertEquals(RANDOM_VALID_ZERO_WORKOUT_SESSION_ID,
                storedWorkoutSession.getWorkoutSessionId());
    }



    /**
     * Test the setWorkoutSessionId(long) correctly throws an IllegalArgumentException when the
     * workout session id value is invalid negative value (valid workout session id is >= 0)
     *
     * */
    @Test
    public void setWorkoutSessionId_InvalidNegativeWorkoutSessionId_IllegalArgumentException()
    {
        final long RANDOM_INVALID_NEGATIVE_WORKOUT_SESSION_ID = -7;

        StoredWorkoutSession storedWorkoutSession = new StoredWorkoutSession();

        assertThrows(IllegalArgumentException.class,
                () -> {
                    storedWorkoutSession.setWorkoutSessionId(
                            RANDOM_INVALID_NEGATIVE_WORKOUT_SESSION_ID);
                });

    }


    /**
     * Test the overridden equals(Object) correctly returns true when both StoredWorkoutSession
     * instances are exactly the same
     * */
    @Test
    public void equals_TwoStoredExactWorkoutSession_AreEqual()
    {
        final long RANDOM_WORKOUT_SESSION_ID = 4;

        final IWorkoutSession.ActivityType RANDOM_ACTIVITY_TYPE =
                IWorkoutSession.ActivityType.RUNNING;

        final LocalDateTime RANDOM_START_DATE_TIME = LocalDateTime.now();

        final int RANDOM_DURATION_IN_SECONDS = 140;

        final int RANDOM_NUMBER_OF_STEPS_TAKEN = 1234;

        final IGPSLocation RANDOM_GPS_LOCATION_1 = getRandomGPSLocation1();

        final IGPSLocation RANDOM_GPS_LOCATION_2 = getRandomGPSLocation2();

        String RANDOM_IMAGE_1 = "image1.png";
        String RANDOM_IMAGE_2 = "image2.png";


        StoredWorkoutSession workoutSession1 = new StoredWorkoutSession();

        workoutSession1.setWorkoutSessionId(RANDOM_WORKOUT_SESSION_ID);
        workoutSession1.setActivityType(RANDOM_ACTIVITY_TYPE);
        workoutSession1.setStartDateTime(RANDOM_START_DATE_TIME);
        workoutSession1.setDurationInSeconds(RANDOM_DURATION_IN_SECONDS);
        workoutSession1.setNumberOfStepsTaken(RANDOM_NUMBER_OF_STEPS_TAKEN);
        workoutSession1.calculateCaloriesConsumed();
        workoutSession1.calculateDistanceTravelled();
        workoutSession1.addGPSLocation(RANDOM_GPS_LOCATION_1);
        workoutSession1.addGPSLocation(RANDOM_GPS_LOCATION_2);
        workoutSession1.addImageTaken(RANDOM_IMAGE_1);
        workoutSession1.addImageTaken(RANDOM_IMAGE_2);


        StoredWorkoutSession workoutSession2 = new StoredWorkoutSession();

        workoutSession2.setWorkoutSessionId(RANDOM_WORKOUT_SESSION_ID);
        workoutSession2.setActivityType(RANDOM_ACTIVITY_TYPE);
        workoutSession2.setStartDateTime(RANDOM_START_DATE_TIME);
        workoutSession2.setDurationInSeconds(RANDOM_DURATION_IN_SECONDS);
        workoutSession2.setNumberOfStepsTaken(RANDOM_NUMBER_OF_STEPS_TAKEN);
        workoutSession2.calculateCaloriesConsumed();
        workoutSession2.calculateDistanceTravelled();
        workoutSession2.addGPSLocation(RANDOM_GPS_LOCATION_1);
        workoutSession2.addGPSLocation(RANDOM_GPS_LOCATION_2);
        workoutSession2.addImageTaken(RANDOM_IMAGE_1);
        workoutSession2.addImageTaken(RANDOM_IMAGE_2);


        assertTrue(workoutSession1.equals(workoutSession2));
    }


    /**
     * Returns a fixed gps location with random latitude and longitude
     * */
    private GPSLocation getRandomGPSLocation1()
    {
        final double RANDOM_VALID_LATITUDE = 12.34;
        final double RANDOM_VALID_LONGITUDE = 56.78;

        return new GPSLocation(RANDOM_VALID_LATITUDE, RANDOM_VALID_LONGITUDE);
    }

    /**
     * Returns a fixed gps location with random latitude and longitude
     * */
    private GPSLocation getRandomGPSLocation2()
    {
        final double RANDOM_VALID_LATITUDE = -43.21;
        final double RANDOM_VALID_LONGITUDE = -87.65;

        return new GPSLocation(RANDOM_VALID_LATITUDE, RANDOM_VALID_LONGITUDE);
    }

    /**
     * Test the overridden equals(Object) correctly returns false when the workout session id
     * is different
     * */
    @Test
    public void equals_DifferentWorkoutSessionId_NotEqual()
    {
        final long RANDOM_WORKOUT_SESSION_ID_1 = 4;
        final long RANDOM_WORKOUT_SESSION_ID_2 = 22;

        StoredWorkoutSession workoutSession1 = new StoredWorkoutSession();
        workoutSession1.setWorkoutSessionId(RANDOM_WORKOUT_SESSION_ID_1);

        StoredWorkoutSession workoutSession2 = new StoredWorkoutSession();
        workoutSession1.setWorkoutSessionId(RANDOM_WORKOUT_SESSION_ID_2);

        assertFalse(workoutSession1.equals(workoutSession2));
    }


    /**
     * Test the overridden equals(Object) correctly returns false when a random base class
     * property is different
     * */
    @Test
    public void equals_DifferentRandomBaseClassPropertyValue_NoException()
    {
        final IWorkoutSession.ActivityType RANDOM_ACTIVITY_TYPE_1 =
                IWorkoutSession.ActivityType.WALKING;

        final IWorkoutSession.ActivityType RANDOM_ACTIVITY_TYPE_2 =
                IWorkoutSession.ActivityType.RUNNING;

        StoredWorkoutSession workoutSession1 = new StoredWorkoutSession();
        workoutSession1.setActivityType(RANDOM_ACTIVITY_TYPE_1);

        StoredWorkoutSession workoutSession2 = new StoredWorkoutSession();
        workoutSession1.setActivityType(RANDOM_ACTIVITY_TYPE_2);

        assertFalse(workoutSession1.equals(workoutSession2));
    }



    /**
     * Test the overridden equals(Object) correctly returns false when the argument is null
     * */
    @Test
    public void equals_NullParameter_NoException()
    {
        StoredWorkoutSession RANDOM_WORKOUT_SESSION = new StoredWorkoutSession();

        assertFalse(RANDOM_WORKOUT_SESSION.equals(null));
    }

    /**
     * Test the overridden equals(Object) correctly returns false when the argument is not
     * an instance of StoredWorkoutSession
     * */
    @Test
    public void equals_UnrelatedClassParameter_NoException()
    {
        StoredWorkoutSession RANDOM_WORKOUT_SESSION = new StoredWorkoutSession();

        final ArrayList<String> UNRELATED_CLASS_OBJECT = new ArrayList<>();

        assertFalse(RANDOM_WORKOUT_SESSION.equals(UNRELATED_CLASS_OBJECT));
    }


    /**
     * Test the describeContents() method of the Parcelable interface correctly returns its
     * default value
     * */
    @Test
    public void describeContents_DefaultDescription_NoException()
    {
        final int EXPECTED_DESCRIPTION = 0;

        final WorkoutSession RANDOM_WORKOUT_SESSION = new WorkoutSession();

        assertEquals(EXPECTED_DESCRIPTION,
                RANDOM_WORKOUT_SESSION.describeContents());
    }
}
