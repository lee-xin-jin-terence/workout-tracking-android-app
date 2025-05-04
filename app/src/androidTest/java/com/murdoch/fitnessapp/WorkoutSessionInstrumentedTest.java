package com.murdoch.fitnessapp;

import static org.junit.Assert.assertTrue;

import android.os.Parcel;

import com.murdoch.fitnessapp.models.implementations.GPSLocation;
import com.murdoch.fitnessapp.models.implementations.WorkoutSession;
import com.murdoch.fitnessapp.models.interfaces.IGPSLocation;
import com.murdoch.fitnessapp.models.interfaces.IWorkoutSession;

import org.junit.Test;

import java.time.LocalDateTime;


/**
 * Instrumented tests for the WorkoutSession class, testing methods related
 * to the Parcelable interface
 * */
public class WorkoutSessionInstrumentedTest
{
    /**
     * Test that the writeToParcel and static Creator methods (part of Parcelable interface)
     * are correctly creating flattening an instance of WorkoutSession into a Parcel, and
     * re-creating it back into an instance of WorkoutSession
     * */
    @Test
    public void writeToParcel_and_StaticCreator_ValidValues_NoException()
    {

        final IWorkoutSession.ActivityType RANDOM_ACTIVITY_TYPE =
                IWorkoutSession.ActivityType.RUNNING;

        final LocalDateTime RANDOM_START_DATE_TIME = LocalDateTime.now();

        final int RANDOM_DURATION_IN_SECONDS = 140;

        final int RANDOM_NUMBER_OF_STEPS_TAKEN = 1234;

        final IGPSLocation RANDOM_GPS_LOCATION_1 = new GPSLocation(11.45, 43.2 );

        final IGPSLocation RANDOM_GPS_LOCATION_2 = new GPSLocation(33.44, 55.66);

        String RANDOM_IMAGE_1 = "image1.png";
        String RANDOM_IMAGE_2 = "image2.png";


        WorkoutSession originalWorkoutSession = new WorkoutSession();

        originalWorkoutSession.setActivityType(RANDOM_ACTIVITY_TYPE);
        originalWorkoutSession.setStartDateTime(RANDOM_START_DATE_TIME);
        originalWorkoutSession.setDurationInSeconds(RANDOM_DURATION_IN_SECONDS);
        originalWorkoutSession.setNumberOfStepsTaken(RANDOM_NUMBER_OF_STEPS_TAKEN);
        originalWorkoutSession.calculateCaloriesConsumed();
        originalWorkoutSession.calculateDistanceTravelled();
        originalWorkoutSession.addGPSLocation(RANDOM_GPS_LOCATION_1);
        originalWorkoutSession.addGPSLocation(RANDOM_GPS_LOCATION_2);
        originalWorkoutSession.addImageTaken(RANDOM_IMAGE_1);
        originalWorkoutSession.addImageTaken(RANDOM_IMAGE_2);


        Parcel parcel = Parcel.obtain();


        originalWorkoutSession.writeToParcel(parcel,
                originalWorkoutSession.describeContents());

        final int STARTING_POSITION = 0;

        parcel.setDataPosition(STARTING_POSITION);

        WorkoutSession createdFromParcel =
                WorkoutSession.CREATOR.createFromParcel(parcel);


        assertTrue(createdFromParcel.equals(originalWorkoutSession));

    }


}
