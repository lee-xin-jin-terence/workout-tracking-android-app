package com.murdoch.fitnessapp.models.interfaces;


/**
 * This interface is used for classes that represent a workout session summary,
 * like a summary of details after aggregating various workout session data
 *
 * */
public interface IWorkoutSessionSummary
{
    /**
     * Sets the total duration of the workout session
     *
     * @param totalDurationInSeconds the total duration of the workout sessions, in seconds
     *
     * @throws IllegalArgumentException if argument is a negative number
     * */
    void setTotalDurationInSeconds(int totalDurationInSeconds);


    /**
     * Return the total duration of all workout sessions, in seconds
     * */
    int getTotalDurationInSeconds();


    /**
     * Sets the total number of steps taken
     *
     * @param totalNumberOfStepsTaken the total number of steps taken
     *
     * @throws IllegalArgumentException if argument is a negative number
     * */
    void setTotalNumberOfStepsTaken(int totalNumberOfStepsTaken);


    /**
     * Return the total number of steps taken
     * */
    int getTotalNumberOfStepsTaken();


    /**
     * Sets the total distance travelled in kilometers
     *
     * @param totalDistanceTravelledInKilometers the total distance travelled in kilometers
     *
     * @throws IllegalArgumentException if argument is a negative number
     * */
    void setTotalDistanceTravelledInKilometers(double totalDistanceTravelledInKilometers);


    /**
     * Returns the total distance travelled in kilometers
     * */
    double getTotalDistanceTravelledInKilometers();


    /**
     * Sets the total calories consumed
     *
     * @param totalCaloriesConsumed the total calories consumed from the workout sessions
     *
     * @throws IllegalArgumentException if argument is a negative number
     * */
    void setTotalCaloriesConsumed(double totalCaloriesConsumed);


    /**
     * Returns the total calories consumed
     * */
    double getTotalCaloriesConsumed();

}
