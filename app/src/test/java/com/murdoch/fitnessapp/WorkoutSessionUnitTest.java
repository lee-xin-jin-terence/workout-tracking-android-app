package com.murdoch.fitnessapp;


import org.junit.Test;

import static org.junit.Assert.*;

import com.murdoch.fitnessapp.models.implementations.GPSLocation;
import com.murdoch.fitnessapp.models.implementations.WorkoutSession;
import com.murdoch.fitnessapp.models.interfaces.IGPSLocation;
import com.murdoch.fitnessapp.models.interfaces.IWorkoutSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the unit test of various methods of the WorkoutSession class
 */
public class WorkoutSessionUnitTest
{
    /**
     * Test the default constructor initializes the various instance variables to
     * the correct values
     * */
    @Test
    public void defaultConstructor_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final IWorkoutSession.ActivityType EXPECTED_ACTIVITY_TYPE =
                IWorkoutSession.ActivityType.WALKING;

        final int EXPECTED_DURATION_IN_SECONDS = 0;

        final int EXPECTED_NUMBER_OF_STEPS_TAKEN = 0;

        final double EXPECTED_CALORIES_CONSUMED = 0;

        final double EXPECTED_DISTANCE_TRAVELLED_IN_KILOMETERS = 0;

        final double DELTA = 0;

        assertEquals(EXPECTED_ACTIVITY_TYPE, workoutSession.getActivityType());
        assertNull(workoutSession.getStartDateTime());
        assertEquals(EXPECTED_DURATION_IN_SECONDS, workoutSession.getDurationInSeconds());
        assertEquals(EXPECTED_NUMBER_OF_STEPS_TAKEN, workoutSession.getNumberOfStepsTaken());
        assertEquals(EXPECTED_CALORIES_CONSUMED, workoutSession.getCaloriesConsumed(), DELTA);
        assertEquals(EXPECTED_DISTANCE_TRAVELLED_IN_KILOMETERS,
                workoutSession.getDistanceTravelledInKilometers(), DELTA);
    }


    /**
     * Test the setActivityType(enum) method correctly sets the workout activity type
     * if the argument is a valid non-null enum
     *
     * */
    @Test
    public void setActivityType_ValidNonNullActivityType_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        workoutSession.setActivityType(IWorkoutSession.ActivityType.RUNNING);

        assertEquals(IWorkoutSession.ActivityType.RUNNING,
                     workoutSession.getActivityType());
    }


    /**
     * Test the setActivityType(enum) method correctly throws a NullPointerException
     * if the argument is a null reference
     *
     * */
    @Test
    public void setActivityType_InvalidNullActivityType_NullPointerException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        assertThrows(NullPointerException.class,
                ()-> workoutSession.setActivityType(null));
    }


    /**
     * Test the setStartDateTime(LocalDateTime) method correctly sets the start date time
     * if the argument is a valid non-null LocalDateTime reference
     *
     * */
    @Test
    public void setStartDateTime_ValidNonNullStartDateTime_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final int RANDOM_YEAR = 2015;
        final int RANDOM_MONTH = 4;
        final int RANDOM_DAY_OF_MONTH = 12;
        final int RANDOM_HOUR = 22;
        final int RANDOM_MINUTE = 16;
        final int RANDOM_SECOND = 55;

        final LocalDateTime RANDOM_START_DATE_TIME =
                LocalDateTime.of(RANDOM_YEAR, RANDOM_MONTH, RANDOM_DAY_OF_MONTH,
                                 RANDOM_HOUR, RANDOM_MINUTE, RANDOM_SECOND);


        workoutSession.setStartDateTime(RANDOM_START_DATE_TIME);

        assertEquals(RANDOM_START_DATE_TIME,
                    workoutSession.getStartDateTime());
    }



    /**
     * Test the setStartDateTime(LocalDateTime) method correctly throws a NullPointerException
     * if the argument is a null reference
     *
     * */
    @Test
    public void setStartDateTime_InvalidNullStartDateTime_NullPointerException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        assertThrows(NullPointerException.class,
                ()-> workoutSession.setStartDateTime(null));
    }


    /**
     * Test the setDurationInSeconds(int) method correctly sets workout duration
     * if the argument is a valid positive value
     *
     * */
    @Test
    public void setDurationInSeconds_SetValidPositiveDuration_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final int RANDOM_POSITIVE_DURATION_IN_SECONDS = 67;

        workoutSession.setDurationInSeconds(RANDOM_POSITIVE_DURATION_IN_SECONDS);

        assertEquals(RANDOM_POSITIVE_DURATION_IN_SECONDS,
                workoutSession.getDurationInSeconds());
    }



    /**
     * Test the setDurationInSeconds(int) method correctly sets workout duration
     * if the argument is a valid zero-value number
     *
     * */
    @Test
    public void setDurationInSeconds_SetValidZeroDuration_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final int ZERO_DURATION_IN_SECONDS = 0;

        workoutSession.setDurationInSeconds(ZERO_DURATION_IN_SECONDS);

        assertEquals(ZERO_DURATION_IN_SECONDS,
                workoutSession.getDurationInSeconds());
    }


    /**
     * Test the setDurationInSeconds(int) method correctly throws IllegalArgumentException
     * if the argument is a invalid negative value
     *
     * */
    @Test
    public void setDurationInSeconds_SetInvalidNegativeDuration_IllegalArgumentException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final int RANDOM_NEGATIVE_DURATION_IN_SECONDS = -82;

        assertThrows(IllegalArgumentException.class,
                ()-> workoutSession.setDurationInSeconds(
                        RANDOM_NEGATIVE_DURATION_IN_SECONDS));
    }


    /**
     * Test the addDurationInSeconds(int) method correctly adds up two valid
     * positive duration values
     *
     * */
    @Test
    public void addDurationInSeconds_AddValidPositiveDurationTwice_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final int RANDOM_DURATION_IN_SECONDS_1 = 40;
        final int RANDOM_DURATION_IN_SECONDS_2 = 60;

        workoutSession.addDurationInSeconds(RANDOM_DURATION_IN_SECONDS_1);
        workoutSession.addDurationInSeconds(RANDOM_DURATION_IN_SECONDS_2);

        final int EXPECTED_DURATION_IN_SECONDS = 100;

        assertEquals(EXPECTED_DURATION_IN_SECONDS,
                    workoutSession.getDurationInSeconds());
    }


    /**
     * Test the addDurationInSeconds(int) method correctly adds up one positive
     * and one zero-value duration values
     *
     * */
    @Test
    public void addDurationInSeconds_AddValidPositiveAndZeroDurationTwice_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final int RANDOM_DURATION_IN_SECONDS = 40;
        final int ZERO_DURATION_IN_SECONDS = 0;

        workoutSession.addDurationInSeconds(RANDOM_DURATION_IN_SECONDS);
        workoutSession.addDurationInSeconds(ZERO_DURATION_IN_SECONDS);


        final int EXPECTED_DURATION_IN_SECONDS = 40;

        assertEquals(EXPECTED_DURATION_IN_SECONDS,
                workoutSession.getDurationInSeconds());
    }


    /**
     * Test the addDurationInSeconds(int) method correctly throws an IllegalArgumentException
     * if the argument is an invalid negative value
     *
     * */
    @Test
    public void addDurationInSeconds_AddInvalidNegativeDuration_IllegalArgumentException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final int RANDOM_NEGATIVE_DURATION_IN_SECONDS = -54;

        assertThrows(IllegalArgumentException.class,
                ()-> workoutSession.addDurationInSeconds(RANDOM_NEGATIVE_DURATION_IN_SECONDS));
    }


    /**
     * Test the setNumberOfStepsTaken(int) method correctly sets number of steps taken
     * if the argument is a valid positive value
     *
     * */
    @Test
    public void setNumberOfStepsTaken_SetValidPositiveNumberOfStepsTaken_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final int RANDOM_POSITIVE_NUMBER_OF_STEPS_TAKEN = 200;

        workoutSession.setNumberOfStepsTaken(RANDOM_POSITIVE_NUMBER_OF_STEPS_TAKEN);

        assertEquals(RANDOM_POSITIVE_NUMBER_OF_STEPS_TAKEN,
                workoutSession.getNumberOfStepsTaken());
    }


    /**
     * Test the setNumberOfStepsTaken(int) method correctly sets number of steps taken
     * if the argument is a valid zero-value integer
     *
     * */
    @Test
    public void setNumberOfStepsTaken_SetValidZeroNumberOfStepsTaken_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final int ZERO_NUMBER_OF_STEPS_TAKEN = 0;

        workoutSession.setNumberOfStepsTaken(ZERO_NUMBER_OF_STEPS_TAKEN);

        assertEquals(ZERO_NUMBER_OF_STEPS_TAKEN,
                workoutSession.getNumberOfStepsTaken());
    }


    /**
     * Test the setNumberOfStepsTaken(int) method correctly throws an IllegalArgumentException
     * if the argument is a invalid negative number
     *
     * */
    @Test
    public void setNumberOfStepsTaken_SetInvalidZeroNumberOfStepsTaken_IllegalArgumentException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final int RANDOM_NEGATIVE_NUMBER_OF_STEPS_TAKEN = -100;

        assertThrows(IllegalArgumentException.class,
                ()-> workoutSession.setNumberOfStepsTaken(
                        RANDOM_NEGATIVE_NUMBER_OF_STEPS_TAKEN));
    }


    /**
     * Test the addNumberOfStepsTaken(int) method correctly adds up two valid
     * positive values
     *
     * */
    @Test
    public void addNumberOfStepsTaken_AddValidPositiveNumberOfStepsTakenTwice_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final int RANDOM_POSITIVE_NUMBER_OF_STEPS_TAKEN_1 = 100;
        final int RANDOM_POSITIVE_NUMBER_OF_STEPS_TAKEN_2 = 200;

        workoutSession.addNumberOfStepsTaken(RANDOM_POSITIVE_NUMBER_OF_STEPS_TAKEN_1);
        workoutSession.addNumberOfStepsTaken(RANDOM_POSITIVE_NUMBER_OF_STEPS_TAKEN_2);

        final int EXPECTED_NUMBER_OF_STEPS_TAKEN = 300;

        assertEquals(EXPECTED_NUMBER_OF_STEPS_TAKEN,
                workoutSession.getNumberOfStepsTaken());
    }


    /**
     * Test the addNumberOfStepsTaken(int) method correctly adds up a valid
     * positive integer and a zero-value integer
     *
     * */
    @Test
    public void addNumberOfStepsTaken_AddValidPositiveAndZeroNumberOfStepsTakenTwice_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final int RANDOM_POSITIVE_NUMBER_OF_STEPS_TAKEN = 100;
        final int ZERO_NUMBER_OF_STEPS_TAKEN = 0;

        workoutSession.addNumberOfStepsTaken(RANDOM_POSITIVE_NUMBER_OF_STEPS_TAKEN);
        workoutSession.addNumberOfStepsTaken(ZERO_NUMBER_OF_STEPS_TAKEN);

        final int EXPECTED_NUMBER_OF_STEPS = 100;

        assertEquals(EXPECTED_NUMBER_OF_STEPS,
                workoutSession.getNumberOfStepsTaken());

    }


    /**
     * Test the addNumberOfStepsTaken(int) method correctly throws an IllegalArgumentException
     * if the argument is an invalid negative integer
     *
     * */
    @Test
    public void addNumberOfStepsTaken_AddInvalidNegativeNumberOfStepsTaken_IllegalArgumentException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final int RANDOM_NEGATIVE_NUMBER_OF_STEPS_TAKEN = -200;

        assertThrows(IllegalArgumentException.class,
                ()-> workoutSession.addNumberOfStepsTaken(RANDOM_NEGATIVE_NUMBER_OF_STEPS_TAKEN));

    }


    /**
     * Test the calculateCaloriesConsumed() method correctly calculates that zero calories is
     * consumed if the workout duration is zero
     *
     * Note: The calories consumed calculated is directly proportional to the duration spent
     *
     * */
    @Test
    public void calculateCaloriesConsumed_ValidZeroDuration_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final IWorkoutSession.ActivityType ANY_ACTIVITY_TYPE = IWorkoutSession.ActivityType.WALKING;
        final int ZERO_DURATION_IN_SECONDS = 0;

        workoutSession.setActivityType(ANY_ACTIVITY_TYPE);
        workoutSession.setDurationInSeconds(ZERO_DURATION_IN_SECONDS);

        workoutSession.calculateCaloriesConsumed();

        final double EXPECTED_CALORIES_CONSUMED = 0;
        final double MAXIMUM_DELTA = 0;

        assertEquals(EXPECTED_CALORIES_CONSUMED,
                workoutSession.getCaloriesConsumed(), MAXIMUM_DELTA);
    }


    /**
     * Test the calculateCaloriesConsumed() method correctly calculates the calories consumed
     * if the activity type is walking, and the workout duration is not zero
     *
     * Note: The calories consumed calculated is directly proportional to the duration spent
     *  and activity type
     * */
    @Test
    public void calculateCaloriesConsumed_ValidPositiveDurationWalking_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

       final IWorkoutSession.ActivityType WALKING_ACTIVITY = IWorkoutSession.ActivityType.WALKING;
        final int RANDOM_DURATION_IN_SECONDS = 120;

        workoutSession.setActivityType(WALKING_ACTIVITY);
        workoutSession.setDurationInSeconds(RANDOM_DURATION_IN_SECONDS);

        workoutSession.calculateCaloriesConsumed();

        final double EXPECTED_CALORIES_CONSUMED = 7.595;
        final double DELTA = 0;

        assertEquals(EXPECTED_CALORIES_CONSUMED,
                workoutSession.getCaloriesConsumed(), DELTA);
    }



    /**
     * Test the calculateCaloriesConsumed() method correctly calculates the calories consumed
     * if the activity type is running, and the workout duration is not zero
     *
     * Note: The calories consumed calculated is directly proportional to the duration spent
     *  and activity type
     * */
    @Test
    public void calculateCaloriesConsumed_ValidPositiveDurationRunning_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final IWorkoutSession.ActivityType RUNNING_ACTIVITY = IWorkoutSession.ActivityType.RUNNING;
        final int RANDOM_DURATION_IN_SECONDS = 180;

        workoutSession.setActivityType(RUNNING_ACTIVITY);
        workoutSession.setDurationInSeconds(RANDOM_DURATION_IN_SECONDS);

        workoutSession.calculateCaloriesConsumed();

        final double EXPECTED_CALORIES_CONSUMED = 22.785;
        final double DELTA = 0;

        assertEquals(EXPECTED_CALORIES_CONSUMED,
                workoutSession.getCaloriesConsumed(), DELTA);
    }



    /**
     * Test the addGPSLocation(IGPSLocation) method correctly adds two valid non-null
     * GPSLocation to the list of GPSLocations
     * */
    @Test
    public void addGPSLocation_AddTwoValidNonNullGPSLocation_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final IGPSLocation RANDOM_GPS_LOCATION_1 = getRandomGPSLocation1();

        final IGPSLocation RANDOM_GPS_LOCATION_2 = getRandomGPSLocation2();

        workoutSession.addGPSLocation(RANDOM_GPS_LOCATION_1);
        workoutSession.addGPSLocation(RANDOM_GPS_LOCATION_2);


        List<IGPSLocation> listOfGPSLocation = workoutSession.getListOfGPSLocations();

        final int EXPECTED_SIZE_OF_LIST = 2;
        final int INDEX_OF_RANDOM_GPS_LOCATION_1 = 0;
        final int INDEX_OF_RANDOM_GPS_LOCATION_2 = 1;

        assertEquals(EXPECTED_SIZE_OF_LIST, listOfGPSLocation.size());
        assertEquals(RANDOM_GPS_LOCATION_1,
                listOfGPSLocation.get(INDEX_OF_RANDOM_GPS_LOCATION_1));

        assertEquals(RANDOM_GPS_LOCATION_2,
                listOfGPSLocation.get(INDEX_OF_RANDOM_GPS_LOCATION_2));
    }


    /**
     * Returns a fixed random GPSLocation
     * */
    private IGPSLocation getRandomGPSLocation1()
    {
        final double RANDOM_LATITUDE_1 = 1.363189;
        final double RANDOM_LONGITUDE_1 = 103.926002;

        return new GPSLocation(RANDOM_LATITUDE_1,RANDOM_LONGITUDE_1);
    }

    /**
     * Returns a fixed random GPSLocation
     * */
    private IGPSLocation getRandomGPSLocation2()
    {
        final double RANDOM_LATITUDE_2 = 1.354952;
        final double RANDOM_LONGITUDE_2 = 103.936988;

        return new GPSLocation(RANDOM_LATITUDE_2, RANDOM_LONGITUDE_2);
    }

    /**
     * Returns a fixed random GPSLocation
     * */
    private IGPSLocation getRandomGPSLocation3()
    {
        final double RANDOM_LATITUDE_3 = 1.343968;
        final double RANDOM_LONGITUDE_3 = 103.946601;

        return new GPSLocation(RANDOM_LATITUDE_3, RANDOM_LONGITUDE_3);
    }






    /**
     * Test the addGPSLocation(IGPSLocation) method correctly adds a valid null reference
     * GPSLocation to the list of GPSLocations
     *
     * Note: Null references are considered valid when added to list of GPSLocations, to
     * signify a pause during the workout session, and affects distance calculation
     * */
    @Test
    public void addGPSLocation_AddValidNulLGPSLocation_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final IGPSLocation NULL_GPS_LOCATION = null;


        workoutSession.addGPSLocation(NULL_GPS_LOCATION);


        List<IGPSLocation> listOfGPSLocation = workoutSession.getListOfGPSLocations();

        final int EXPECTED_SIZE_OF_LIST = 1;
        final int INDEX_OF_NULL_GPS_LOCATION = 0;

        assertEquals(EXPECTED_SIZE_OF_LIST, listOfGPSLocation.size());

        assertEquals(NULL_GPS_LOCATION,
                listOfGPSLocation.get(INDEX_OF_NULL_GPS_LOCATION));

    }


    /**
     * Test that clearListOfGPSLocations() can clear the list of locations without
     * error if the list of GPSLocations is not empty (no exceptions)
     *
     * */
    @Test
    public void clearListOfGPSLocations_ClearNonEmptyList_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final IGPSLocation RANDOM_GPS_LOCATION_1 = getRandomGPSLocation1();

        final IGPSLocation RANDOM_GPS_LOCATION_2 = getRandomGPSLocation2();

        workoutSession.addGPSLocation(RANDOM_GPS_LOCATION_1);
        workoutSession.addGPSLocation(RANDOM_GPS_LOCATION_2);

        workoutSession.clearListOfGPSLocations();

        final int EXPECTED_SIZE_OF_LIST = 0;

        assertEquals(EXPECTED_SIZE_OF_LIST,
                workoutSession.getListOfGPSLocations().size());
    }



    /**
     * Test that clearListOfGPSLocations() can clear the list of locations without
     * error if the list of GPSLocations is empty (no exceptions)
     *
     * */
    @Test
    public void clearListOfGPSLocations_ClearEmptyList_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        workoutSession.clearListOfGPSLocations();

        final int EXPECTED_SIZE_OF_LIST = 0;

        assertEquals(EXPECTED_SIZE_OF_LIST,
                workoutSession.getListOfGPSLocations().size());
    }



    /**
     * Test that calculateDistanceTravelled() can correctly calculate the distance travelled
     * if there are no GPSLocations added to the list of GPSLocations
     *
     * Note: The distance travelled in the workout session is calculated based on all the
     * GPSLocations (coordinates) that the user has travelled to
     * */
    @Test
    public void calculateDistanceTravelled_ZeroGPSLocationInList_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        workoutSession.calculateDistanceTravelled();

        final double EXPECTED_DISTANCE_TRAVELLED_IN_KILOMETERS = 0;

        final double MAXIMUM_DELTA = 0;

        assertEquals(EXPECTED_DISTANCE_TRAVELLED_IN_KILOMETERS,
                workoutSession.getDistanceTravelledInKilometers(),MAXIMUM_DELTA);

    }



    /**
     * Test that calculateDistanceTravelled() can correctly calculate the distance travelled
     * if there is only one GPSLocation (coordinate) tracked/stored in the workout session
     *
     * Note: The distance travelled in the workout session is calculated based on all the
     * GPSLocations (coordinates) that the user has travelled to
     * */
    @Test
    public void calculateDistanceTravelled_OneNonNullGPSLocationInList_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        IGPSLocation RANDOM_GPS_LOCATION_1 = getRandomGPSLocation1();

        workoutSession.addGPSLocation(RANDOM_GPS_LOCATION_1);


        workoutSession.calculateDistanceTravelled();

        final double EXPECTED_DISTANCE_TRAVELLED_IN_KILOMETERS = 0;
        final double DELTA = 0.0;

        assertEquals(EXPECTED_DISTANCE_TRAVELLED_IN_KILOMETERS,
                workoutSession.getDistanceTravelledInKilometers(),DELTA);

    }



    /**
     * Test that calculateDistanceTravelled() can correctly calculate the distance travelled
     * if there are some GPSLocations tracked/stored in the workout session
     *
     * Note: The distance travelled in the workout session is calculated based on all the
     * GPSLocations (coordinates) that the user has travelled to
     * */
    @Test
    public void calculateDistanceTravelled_ThreeNonNullGPSLocationInList_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        IGPSLocation RANDOM_GPS_LOCATION_1 = getRandomGPSLocation1();
        IGPSLocation RANDOM_GPS_LOCATION_2 = getRandomGPSLocation2();
        IGPSLocation RANDOM_GPS_LOCATION_3 = getRandomGPSLocation3();

        workoutSession.addGPSLocation(RANDOM_GPS_LOCATION_1);
        workoutSession.addGPSLocation(RANDOM_GPS_LOCATION_2);
        workoutSession.addGPSLocation(RANDOM_GPS_LOCATION_3);

        workoutSession.calculateDistanceTravelled();

        final double EXPECTED_DISTANCE_TRAVELLED_IN_KILOMETERS = 3.149404;
        final double DELTA = 0.000001;

        assertEquals(EXPECTED_DISTANCE_TRAVELLED_IN_KILOMETERS,
                workoutSession.getDistanceTravelledInKilometers(),DELTA);

    }


    /**
     * Test that calculateDistanceTravelled() can correctly calculate the distance travelled
     * if there are some GPSLocations tracked/stored in the workout session
     *
     * Note: The distance travelled in the workout session is calculated based on all the
     * GPSLocations (coordinates) that the user has travelled to
     * */
    @Test
    public void calculateDistanceTravelled_GPSLocationWithNullInBetweenInList_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        IGPSLocation RANDOM_GPS_LOCATION_1 = getRandomGPSLocation1();
        IGPSLocation RANDOM_GPS_LOCATION_2 = getRandomGPSLocation2();
        IGPSLocation NULL_GPS_LOCATION = null;
        IGPSLocation RANDOM_GPS_LOCATION_3 = getRandomGPSLocation3();

        workoutSession.addGPSLocation(RANDOM_GPS_LOCATION_1);
        workoutSession.addGPSLocation(RANDOM_GPS_LOCATION_2);
        workoutSession.addGPSLocation(NULL_GPS_LOCATION);
        workoutSession.addGPSLocation(RANDOM_GPS_LOCATION_3);

        workoutSession.calculateDistanceTravelled();

        final double EXPECTED_DISTANCE_TRAVELLED_IN_KILOMETERS = 1.5265426;
        final double MAXIMUM_DELTA = 0.000001;

        assertEquals(EXPECTED_DISTANCE_TRAVELLED_IN_KILOMETERS,
                workoutSession.getDistanceTravelledInKilometers(),MAXIMUM_DELTA);

    }



    /**
     * Test that addImageTaken(String) can correctly add two valid non-null non-empty
     * string to the list of images taken
     *
     * */
    @Test
    public void addImageTaken_AddTwoValidImagePath_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final String RANDOM_IMAGE_PATH_1 = "someImage1.png";
        final String RANDOM_IMAGE_PATH_2 = "someImage2.png";

        workoutSession.addImageTaken(RANDOM_IMAGE_PATH_1);
        workoutSession.addImageTaken(RANDOM_IMAGE_PATH_2);

        final int EXPECTED_SIZE_OF_LIST = 2;

        final int INDEX_OF_RANDOM_IMAGE_PATH_1 = 0;
        final int INDEX_OF_RANDOM_IMAGE_PATH_2 = 1;

        assertEquals(EXPECTED_SIZE_OF_LIST,
                workoutSession.getListOfImagesTaken().size());

        assertEquals(RANDOM_IMAGE_PATH_1,
                workoutSession.getListOfImagesTaken().get(INDEX_OF_RANDOM_IMAGE_PATH_1));

        assertEquals(RANDOM_IMAGE_PATH_2,
                workoutSession.getListOfImagesTaken().get(INDEX_OF_RANDOM_IMAGE_PATH_2));
    }



    /**
     * Test that addImageTaken(String) can correctly throw a NullPointerException if the
     * argument is a null reference
     *
     * */
    @Test
    public void addImageTaken_AddInvalidNullString_NullPointerException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        assertThrows(NullPointerException.class,
                ()-> workoutSession.addImageTaken(null));
    }



    /**
     * Test that addImageTaken(String) can correctly throw a IllegalArgumentException if the
     * argument is an empty string
     *
     * */
    @Test
    public void addImageTaken_AddInvalidEmptyString_IllegalArgumentException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final String EMPTY_STRING = "";

        assertThrows(IllegalArgumentException.class,
                ()-> workoutSession.addImageTaken(EMPTY_STRING));
    }



    /**
     * Test that addImageTaken(String) can correctly throw a IllegalArgumentException if the
     * argument is a whitespace-only string
     *
     * */
    @Test
    public void addImageTaken_AddInvalidWhitespaceOnlyString_IllegalArgumentException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final String WHITESPACE_ONLY_STRING = "   ";

        assertThrows(IllegalArgumentException.class,
                ()-> workoutSession.addImageTaken(WHITESPACE_ONLY_STRING));
    }


    /**
     * Test that clearListOfImagesTaken() can correctly empty the list of images for a
     * non-empty list of images
     *
     * */
    @Test
    public void clearListOfImagesTaken_ClearNonEmptyList_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final String RANDOM_IMAGE_PATH_1 = "someImage1.png";
        final String RANDOM_IMAGE_PATH_2 = "someImage2.png";

        workoutSession.addImageTaken(RANDOM_IMAGE_PATH_1);
        workoutSession.addImageTaken(RANDOM_IMAGE_PATH_2);

        workoutSession.clearListOfImagesTaken();

        final int EXPECTED_SIZE_OF_LIST = 0;

        assertEquals(EXPECTED_SIZE_OF_LIST,
                workoutSession.getListOfImagesTaken().size());
    }



    /**
     * Test that clearListOfImagesTaken() can correctly empty the list of images for an
     * empty list of images
     *
     * */
    @Test
    public void clearListOfImagesTaken_ClearEmptyList_NoException()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        workoutSession.clearListOfImagesTaken();

        final int EXPECTED_SIZE_OF_LIST = 0;

        assertEquals(EXPECTED_SIZE_OF_LIST, workoutSession.getListOfImagesTaken().size());
    }



    /**
     * Test that the equals(Object) method correct returns true when two workout
     * sessions are equal, where the start date time is not null
     *
     * Note: the start date time is null by default
     * */
    @Test
    public void equals_TwoExactWorkoutSessionWithNonNullStartDateTime_NoException()
    {

        final IWorkoutSession.ActivityType RANDOM_ACTIVITY_TYPE =
                IWorkoutSession.ActivityType.RUNNING;

        final LocalDateTime RANDOM_START_DATE_TIME = LocalDateTime.now();

        final int RANDOM_DURATION_IN_SECONDS = 140;

        final int RANDOM_NUMBER_OF_STEPS_TAKEN = 1234;

        final IGPSLocation RANDOM_GPS_LOCATION_1 = getRandomGPSLocation1();
        final IGPSLocation RANDOM_GPS_LOCATION_2 = getRandomGPSLocation2();


        final String RANDOM_IMAGE_1 = "image1.png";
        final String RANDOM_IMAGE_2 = "image2.png";


        WorkoutSession workoutSession1 = new WorkoutSession();

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


        WorkoutSession workoutSession2 = new WorkoutSession();

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
     * Test that the equals(Object) method correct returns true when two workout
     * sessions are equal, where the start date time is null
     *
     * Note: the start date time is null by default
     * */
    @Test
    public void equals_TwoExactWorkoutSessionWithNullStartDateTime_NoException()
    {

        final IWorkoutSession.ActivityType RANDOM_ACTIVITY_TYPE =
                IWorkoutSession.ActivityType.RUNNING;

        final int RANDOM_DURATION_IN_SECONDS = 140;

        final int RANDOM_NUMBER_OF_STEPS_TAKEN = 1234;

        final IGPSLocation RANDOM_GPS_LOCATION_1 = getRandomGPSLocation1();
        final IGPSLocation RANDOM_GPS_LOCATION_2 = getRandomGPSLocation2();


        final String RANDOM_IMAGE_1 = "image1.png";
        final String RANDOM_IMAGE_2 = "image2.png";


        WorkoutSession workoutSession1 = new WorkoutSession();

        //not setting start date time as null as it is null by default
        //and does not allow setting of null
        workoutSession1.setActivityType(RANDOM_ACTIVITY_TYPE);
        workoutSession1.setDurationInSeconds(RANDOM_DURATION_IN_SECONDS);
        workoutSession1.setNumberOfStepsTaken(RANDOM_NUMBER_OF_STEPS_TAKEN);
        workoutSession1.calculateCaloriesConsumed();
        workoutSession1.calculateDistanceTravelled();
        workoutSession1.addGPSLocation(RANDOM_GPS_LOCATION_1);
        workoutSession1.addGPSLocation(RANDOM_GPS_LOCATION_2);
        workoutSession1.addImageTaken(RANDOM_IMAGE_1);
        workoutSession1.addImageTaken(RANDOM_IMAGE_2);


        WorkoutSession workoutSession2 = new WorkoutSession();

        //not setting start date time as null as it is null by default
        //and does not allow setting of null
        workoutSession2.setActivityType(RANDOM_ACTIVITY_TYPE);
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
     * Test that the equals(Object) method correct returns false if both start date
     * time are different
     * */
    @Test
    public void equals_DifferentStartDateTime_NoException()
    {
        final LocalDateTime RANDOM_START_DATE_TIME_1 = LocalDateTime.now();

        final int RANDOM_YEAR = 2021;
        final int RANDOM_MONTH = 1;
        final int RANDOM_DAY = 22;
        final int RANDOM_HOUR = 15;
        final int RANDOM_MINUTE = 59;
        final int RANDOM_SECOND = 1;

        final LocalDateTime RANDOM_START_DATE_TIME_2 = LocalDateTime.of(RANDOM_YEAR,RANDOM_MONTH,
                RANDOM_DAY,RANDOM_HOUR, RANDOM_MINUTE, RANDOM_SECOND );

        WorkoutSession workoutSession1 = new WorkoutSession();
        workoutSession1.setStartDateTime(RANDOM_START_DATE_TIME_1);

        WorkoutSession workoutSession2 = new WorkoutSession();
        workoutSession2.setStartDateTime(RANDOM_START_DATE_TIME_2);

        assertFalse(workoutSession1.equals(workoutSession2));
    }


    /**
     * Test that the equals(Object) method correct returns false if both activity type
     * is different
     * */
    @Test
    public void equals_DifferentActivityType_NoException()
    {
        final IWorkoutSession.ActivityType RANDOM_ACTIVITY_TYPE_1 =
                IWorkoutSession.ActivityType.WALKING;

        final IWorkoutSession.ActivityType RANDOM_ACTIVITY_TYPE_2 =
                IWorkoutSession.ActivityType.RUNNING;

        WorkoutSession workoutSession1 = new WorkoutSession();
        workoutSession1.setActivityType(RANDOM_ACTIVITY_TYPE_1);

        WorkoutSession workoutSession2 = new WorkoutSession();
        workoutSession2.setActivityType(RANDOM_ACTIVITY_TYPE_2);


        assertFalse(workoutSession1.equals(workoutSession2));

    }



    /**
     * Test that the equals(Object) method correct returns false if duration in seconds
     * is different
     * */
    @Test
    public void equals_DifferentDurationInSeconds_NoException()
    {
        final int RANDOM_DURATION_IN_SECONDS_1 = 100;

        final int RANDOM_DURATION_IN_SECONDS_2 = 600;

        WorkoutSession workoutSession1 = new WorkoutSession();
        workoutSession1.setDurationInSeconds(RANDOM_DURATION_IN_SECONDS_1);

        WorkoutSession workoutSession2 = new WorkoutSession();
        workoutSession2.setDurationInSeconds(RANDOM_DURATION_IN_SECONDS_2);


        assertFalse(workoutSession1.equals(workoutSession2));

    }


    /**
     * Test that the equals(Object) method correct returns false if number of steps
     * taken is different
     * */
    @Test
    public void equals_DifferentNumberOfStepsTaken_NoException()
    {
        final int RANDOM_NUMBER_OF_STEPS_TAKEN_1 = 100;

        final int RANDOM_NUMBER_OF_STEPS_TAKEN_2 = 600;

        WorkoutSession workoutSession1 = new WorkoutSession();
        workoutSession1.setNumberOfStepsTaken(RANDOM_NUMBER_OF_STEPS_TAKEN_1);

        WorkoutSession workoutSession2 = new WorkoutSession();
        workoutSession2.setNumberOfStepsTaken(RANDOM_NUMBER_OF_STEPS_TAKEN_2);


        assertFalse(workoutSession1.equals(workoutSession2));
    }


    /**
     * Test that the equals(Object) method correct returns false if different image paths
     * are stored in list of images taken
     * */
    @Test
    public void equals_DifferentImageTaken_NoException()
    {
        final String RANDOM_IMAGE_TAKEN_1 = "image1.png";

        final String RANDOM_IMAGE_TAKEN_2 = "image2.png";

        WorkoutSession workoutSession1 = new WorkoutSession();
        workoutSession1.addImageTaken(RANDOM_IMAGE_TAKEN_1);

        WorkoutSession workoutSession2 = new WorkoutSession();
        workoutSession2.addImageTaken(RANDOM_IMAGE_TAKEN_2);


        assertFalse(workoutSession1.equals(workoutSession2));
    }


    /**
     * Test that the equals(Object) method correct returns false if different
     * GPSLocations (coordinates) are stored in the list of GPSLocations
     * */
    @Test
    public void equals_DifferentGPSLocations_NoException()
    {

        final IGPSLocation RANDOM_GPS_LOCATION_1 = getRandomGPSLocation1();

        final IGPSLocation RANDOM_GPS_LOCATION_2 = getRandomGPSLocation2();


        WorkoutSession workoutSession1 = new WorkoutSession();
        workoutSession1.addGPSLocation(RANDOM_GPS_LOCATION_1);

        WorkoutSession workoutSession2 = new WorkoutSession();
        workoutSession2.addGPSLocation(RANDOM_GPS_LOCATION_2);


        assertFalse(workoutSession1.equals(workoutSession2));
    }


    /**
     * Test that the equals(Object) method correct returns false if different
     * distance have been travelled
     *
     * Note: The distance travelled is based on the GPSLocations (coordinates)
     * that they have travelled to
     *
     * If the method calculateDistanceTravelled() is not invoked,
     *  the distance will not be calculated
     * */
    @Test
    public void equals_DifferentDistanceTravelled_NoException()
    {
        final IGPSLocation RANDOM_GPS_LOCATION_1 = getRandomGPSLocation1();

        final IGPSLocation RANDOM_GPS_LOCATION_2 = getRandomGPSLocation2();

        WorkoutSession workoutSession1 = new WorkoutSession();
        workoutSession1.addGPSLocation(RANDOM_GPS_LOCATION_1);
        workoutSession1.addGPSLocation(RANDOM_GPS_LOCATION_2);


        //only difference here is that distance is calculated
        WorkoutSession workoutSession2 = new WorkoutSession();
        workoutSession2.addGPSLocation(RANDOM_GPS_LOCATION_1);
        workoutSession2.addGPSLocation(RANDOM_GPS_LOCATION_2);
        workoutSession2.calculateDistanceTravelled();


        assertFalse(workoutSession1.equals(workoutSession2));
    }



    /**
     * Test that the equals(Object) method correct returns false the calories
     * consumed is different
     *
     * Calories consumed is directly proportional to the activity type and duration in seconds
     *
     * It needs to be calculated via the method calculateCaloriesConsumed()
     * */
    @Test
    public void equals_DifferentCaloriesConsumed_NoException()
    {
        final IWorkoutSession.ActivityType RANDOM_ACTIVITY_TYPE = IWorkoutSession.ActivityType.RUNNING;

        final int RANDOM_DURATION_IN_SECONDS = 200;

        WorkoutSession workoutSession1 = new WorkoutSession();
        workoutSession1.setActivityType(RANDOM_ACTIVITY_TYPE);
        workoutSession1.setDurationInSeconds(RANDOM_DURATION_IN_SECONDS);


        //only difference here is that the method calculateCaloriesConsumed() is invoked
        WorkoutSession workoutSession2 = new WorkoutSession();
        workoutSession2.setActivityType(RANDOM_ACTIVITY_TYPE);
        workoutSession2.setDurationInSeconds(RANDOM_DURATION_IN_SECONDS);
        workoutSession2.calculateCaloriesConsumed();

        assertFalse(workoutSession1.equals(workoutSession2));
    }



    /**
     * Test that the equals(Object) method correct returns false if the argument is null
     * */
    @Test
    public void equals_NullParameter_NoException()
    {

        WorkoutSession RANDOM_WORKOUT_SESSION = new WorkoutSession();


        assertFalse(RANDOM_WORKOUT_SESSION.equals(null));
    }


    /**
     * Test that the equals(Object) method correct returns false if the argument is of
     * a different unrelated class
     * */
    @Test
    public void equals_UnrelatedClassParameter_NoException()
    {

        WorkoutSession RANDOM_WORKOUT_SESSION = new WorkoutSession();

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
        final WorkoutSession RANDOM_WORKOUT_SESSION = new WorkoutSession();

        final int EXPECTED_DESCRIPTION = 0;

        assertEquals(EXPECTED_DESCRIPTION, RANDOM_WORKOUT_SESSION.describeContents());

    }
}