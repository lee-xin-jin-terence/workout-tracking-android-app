package com.murdoch.fitnessapp.models.implementations;

import com.murdoch.fitnessapp.models.interfaces.IWorkoutSessionSummary;



/**
 * This class represents a workout session summary,
 * like a summary of details after aggregating various workout session data
 *
 * */
public class WorkoutSessionSummary implements IWorkoutSessionSummary
{
    public static final int TOTAL_DURATION_IN_SECONDS_DEFAULT_VALUE = 0;
    public static final int TOTAL_NUMBER_OF_STEPS_TAKEN_DEFAULT_VALUE = 0;
    public static final double TOTAL_DISTANCE_TRAVELLED_IN_KILOMETERS_DEFAULT_VALUE = 0;
    public static final double TOTAL_CALORIES_CONSUMED_DEFAULT_VALUE = 0;


    private int totalDurationInSeconds = TOTAL_DURATION_IN_SECONDS_DEFAULT_VALUE;
    private int totalNumberOfStepsTaken = TOTAL_NUMBER_OF_STEPS_TAKEN_DEFAULT_VALUE;
    private double totalDistanceTravelledInKilometers =
                                TOTAL_DISTANCE_TRAVELLED_IN_KILOMETERS_DEFAULT_VALUE;
    private double totalCaloriesConsumed = TOTAL_CALORIES_CONSUMED_DEFAULT_VALUE;


    /**
     * Initializes an instance of WorkoutSessionSummary with the default values
     *
     * @see #TOTAL_DURATION_IN_SECONDS_DEFAULT_VALUE
     * @see #TOTAL_NUMBER_OF_STEPS_TAKEN_DEFAULT_VALUE
     * @see #TOTAL_DISTANCE_TRAVELLED_IN_KILOMETERS_DEFAULT_VALUE
     * @see #TOTAL_CALORIES_CONSUMED_DEFAULT_VALUE
     * */
    public WorkoutSessionSummary()
    {

    }


    /**
     * Sets the total duration of the workout session
     *
     * @param totalDurationInSeconds the total duration of the workout sessions, in seconds
     *
     * @throws IllegalArgumentException if argument is a negative number
     * */
    @Override
    public void setTotalDurationInSeconds(int totalDurationInSeconds)
    {
        this.totalDurationInSeconds = totalDurationInSeconds;
    }


    /**
     * Return the total duration of all workout sessions, in seconds
     * */
    @Override
    public int getTotalDurationInSeconds()
    {
        return this.totalDurationInSeconds;
    }


    /**
     * Sets the total number of steps taken
     *
     * @param totalNumberOfStepsTaken the total number of steps taken
     *
     * @throws IllegalArgumentException if argument is a negative number
     * */
    @Override
    public void setTotalNumberOfStepsTaken(int totalNumberOfStepsTaken)
    {
        this.totalNumberOfStepsTaken = totalNumberOfStepsTaken;
    }


    /**
     * Return the total number of steps taken
     * */
    @Override
    public int getTotalNumberOfStepsTaken()
    {
        return this.totalNumberOfStepsTaken;
    }


    /**
     * Sets the total distance travelled in kilometers
     *
     * @param totalDistanceTravelledInKilometers the total distance travelled in kilometers
     *
     * @throws IllegalArgumentException if argument is a negative number
     * */
    @Override
    public void setTotalDistanceTravelledInKilometers(double totalDistanceTravelledInKilometers)
    {
        this.totalDistanceTravelledInKilometers = totalDistanceTravelledInKilometers;
    }


    /**
     * Returns the total distance travelled in kilometers
     * */
    @Override
    public double getTotalDistanceTravelledInKilometers()
    {
        return totalDistanceTravelledInKilometers;
    }


    /**
     * Sets the total calories consumed
     *
     * @param totalCaloriesConsumed the total calories consumed from the workout sessions
     *
     * @throws IllegalArgumentException if argument is a negative number
     * */
    @Override
    public void setTotalCaloriesConsumed(double totalCaloriesConsumed)
    {
        this.totalCaloriesConsumed = totalCaloriesConsumed;
    }


    /**
     * Returns the total calories consumed
     * */
    @Override
    public double getTotalCaloriesConsumed()
    {
        return this.totalCaloriesConsumed;
    }

    @Override
    public boolean equals(Object otherObject)
    {
        if (otherObject == this)
        {
            return true;
        }

        if (!(otherObject instanceof WorkoutSessionSummary))
        {
            return false;
        }

        WorkoutSessionSummary otherObjectSummary = (WorkoutSessionSummary) otherObject;

        return this.totalCaloriesConsumed == otherObjectSummary.totalCaloriesConsumed &&
                this.totalDistanceTravelledInKilometers ==
                        otherObjectSummary.totalDistanceTravelledInKilometers &&
                this.totalDurationInSeconds == otherObjectSummary.totalDurationInSeconds &&
                this.totalNumberOfStepsTaken == otherObjectSummary.totalNumberOfStepsTaken;
    }

}
