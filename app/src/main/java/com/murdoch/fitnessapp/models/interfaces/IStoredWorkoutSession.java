package com.murdoch.fitnessapp.models.interfaces;

/**
 * A interface for classes representing workout sessions that has been stored in a database.
 *
 * Difference between this interface and IWorkoutSession is that this interface
 * has setters and getters for the Id of the workout session stored in the database
 *
 * */
public interface IStoredWorkoutSession extends IWorkoutSession
{
    /**
     * Sets the workout session Id
     *
     * @param workoutSessionId the workout session Id
     *
     * @throws IllegalArgumentException if the argument is a negative number
     * */
    void setWorkoutSessionId(long workoutSessionId);


    /**
     * Returns the workout session Id
     *
     * */
    long getWorkoutSessionId();
}
