package com.murdoch.fitnessapp.models.interfaces;

import java.time.LocalDateTime;
import java.util.List;


/**
 * This interface is used for classes that represent a workout session
 *
 * */
public interface IWorkoutSession
{
    /**
     * An enum containing various possible activity types in a workout session
     * */
    enum ActivityType
    {
        WALKING, RUNNING
    }

    /**
     * Sets the activity type of the workout sessions
     *
     * @param activityType the activity type of the workout session
     *
     * @throws NullPointerException if the argument is null
     * */
    void setActivityType(ActivityType activityType);


    /**
     * Returns the activity type of the workout session
     *
     * */
    ActivityType getActivityType();


    /**
     * Sets the start date time of the workout session
     *
     * @param startDateTime the start date time of the workout session
     *
     * @throws NullPointerException if the argument is null
     * */
    void setStartDateTime(LocalDateTime startDateTime);


    /**
     * Returns the start date time of the workout session
     *
     * */
    LocalDateTime getStartDateTime();


    /**
     * Sets the duration of the workout session in seconds
     *
     * @param durationInSeconds the duration of the workout in seconds
     *
     * @throws IllegalArgumentException if the argument is a negative number
     * */
    void setDurationInSeconds(int durationInSeconds);


    /**
     * Add duration of workout session to the current workout duration
     *
     * @param durationInSeconds the additional duration in seconds to
     *                                    add to the current workout duration
     *
     * @throws IllegalArgumentException if the argument is a negative number
     * */
    void addDurationInSeconds(int durationInSeconds);


    /**
     * Returns the duration of the workout in seconds
     *
     * */
    int getDurationInSeconds();


    /**
     * Sets the number of steps taken during the workout session
     *
     * @param numberOfSteps the number of steps taken during the workout
     *                           session
     *
     * @throws IllegalArgumentException if the argument is a negative number
     * */
    void setNumberOfStepsTaken(int numberOfSteps);


    /**
     * Add number of steps taken to the current number of steps taken
     *
     * @param numberOfSteps the additional number of steps taken
     *
     * @throws IllegalArgumentException if the argument is a negative number
     * */
    void addNumberOfStepsTaken(int numberOfSteps);


    /**
     * Returns the number of steps taken during the workout session
     *
     * */
    int getNumberOfStepsTaken();


    /**
     * Calculate the calories that the user consumed based on activity type and duration of
     * workout session. The amount of calories consumed can then be retrieved via the method
     * getCaloriesConsumed()
     *
     * @see #setActivityType(ActivityType)
     * @see #setDurationInSeconds(int)
     * @see #getCaloriesConsumed()
     * */
    void calculateCaloriesConsumed();


    /**
     * Returns the number of calories consumed
     *
     * Note: The amount of calories consumed cannot be directly set. It has
     * to be calculated through the method calculateCaloriesConsumed()
     *
     * @see #calculateCaloriesConsumed()
     * */
    double getCaloriesConsumed();


    /**
     * Adds a GPSLocation of a position where the user was present during the workout
     * The GPSLocations will be stored in a list, which will be used to calculate the
     * distance the user has travelled during the workout session
     *
     * GPSLocations between two successive (consecutive) locations will be assumed to be continuous,
     * and will go towards the calculation of distance travelled. For example,
     * if GPSLocationA, GPSLocationB and GPSLocationC are added in such order, then
     * the distance calculated will be via the distance between GPSLocationA to GPSLocationB,
     * and from GPSLocationB to GPSLocationC
     *
     * Null references can be added between two GPSLocations so that the distance will not
     * be calculated between two points
     * For example, if GPSLocationA, GPSLocationB, null, GPSLocationC, GPSLocationD are added,
     * then the distance calculated will be only between GPSLocationA to GPSLocationB, and
     * between GPSLocationC and GPSLocationD
     *
     * @param gpsLocation the GPSLocation to be added. Can be null
     *
     * @see #calculateDistanceTravelled()
     *
     * */
    void addGPSLocation(IGPSLocation gpsLocation);


    /**
     * Returns an unmodifiable list of GPSLocations (coordinates)
     * added in the order from the method addGPSLocation(IGPSLocation)
     *
     * @see #addGPSLocation(IGPSLocation)
     * */
    List<IGPSLocation> getListOfGPSLocations();


    /**
     * Empties the list of GPSLocations
     * */
    void clearListOfGPSLocations();


    /**
     * Calculate the distance travelled by the user based on the list of GPSLocations
     * added by the user.
     * The distance calculated can then be retrieved via the method
     * getDistanceTravelledInKilometers()
     *
     * @see #addGPSLocation(IGPSLocation)
     * @see #getDistanceTravelledInKilometers()
     * */
    void calculateDistanceTravelled();


    /**
     * Returns the distance travelled in kilometers
     *
     * Note: The distance travelled cannot be directly set. It has
     * to be calculated through the method calculateDistanceTravelled()
     *
     * @see #calculateDistanceTravelled()
     * */
    double getDistanceTravelledInKilometers();


    /**
     * Add an image to the list of images taken. Does not check for
     * duplicates
     *
     * @param imagePath an image path of the image taken
     *
     * @throws NullPointerException if the argument is null
     * @throws IllegalArgumentException if the argument is an empty string/
     * string with only whitespaces
     * */
    void addImageTaken(String imagePath);


    /**
     * Returns a list of images taken in the order added through
     * the method addImageTaken
     *
     * @return an unmodifiable list of images taken
     *
     * @see #addImageTaken(String)
     * */
    List<String> getListOfImagesTaken();


    /**
     * Empties out the list of images taken
     * */
    void clearListOfImagesTaken();

}
