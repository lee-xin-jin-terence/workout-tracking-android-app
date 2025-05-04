package com.murdoch.fitnessapp;

import static org.junit.Assert.assertEquals;


import com.murdoch.fitnessapp.models.implementations.WorkoutSessionSummary;


import org.junit.Test;


/**
 * Contains the unit test of various methods of the WorkoutSessionSummary class
 */
public class WorkoutSessionSummaryUnitTest
{
    @Test
    public void defaultConstructor_NoException()
    {
        WorkoutSessionSummary workoutSessionSummary = new WorkoutSessionSummary();

        final int EXPECTED_TOTAL_DURATION_IN_SECONDS = 0;
        final int EXPECTED_TOTAL_NUMBER_OF_STEPS = 0;
        final double EXPECTED_TOTAL_DISTANCE_TRAVELLED = 0;
        final double EXPECTED_TOTAL_CALORIES_CONSUMED = 0;

        final double MAXIMUM_DELTA = 0;

        assertEquals(EXPECTED_TOTAL_DURATION_IN_SECONDS,
                    workoutSessionSummary.getTotalDurationInSeconds());

        assertEquals(EXPECTED_TOTAL_NUMBER_OF_STEPS,
                    workoutSessionSummary.getTotalNumberOfStepsTaken());

        assertEquals(EXPECTED_TOTAL_DISTANCE_TRAVELLED,
                workoutSessionSummary.getTotalDistanceTravelledInKilometers(), MAXIMUM_DELTA);

        assertEquals(EXPECTED_TOTAL_CALORIES_CONSUMED,
                workoutSessionSummary.getTotalCaloriesConsumed(), MAXIMUM_DELTA);
    }


    /**
     * Test the setTotalDurationInSeconds(int) method correctly sets total workout duration
     *
     * */
    @Test
    public void setTotalDurationInSeconds_ValidTotalDuration_NoException()
    {
        WorkoutSessionSummary workoutSessionSummary = new WorkoutSessionSummary();

        final int RANDOM_TOTAL_DURATION_IN_SECONDS = 200;

        workoutSessionSummary.setTotalDurationInSeconds(RANDOM_TOTAL_DURATION_IN_SECONDS);

        assertEquals(RANDOM_TOTAL_DURATION_IN_SECONDS,
                     workoutSessionSummary.getTotalDurationInSeconds());
    }


    /**
     * Test the setTotalNumberOfSteps(int) method correctly sets total number of steps
     *
     * */
    @Test
    public void setTotalNumberOfStepsTaken_ValidTotalNumberOfSteps_NoException()
    {
        WorkoutSessionSummary workoutSessionSummary = new WorkoutSessionSummary();

        final int RANDOM_TOTAL_NUMBER_OF_STEPS_TAKEN = 300;

        workoutSessionSummary.setTotalNumberOfStepsTaken(RANDOM_TOTAL_NUMBER_OF_STEPS_TAKEN);

        assertEquals(RANDOM_TOTAL_NUMBER_OF_STEPS_TAKEN,
                     workoutSessionSummary.getTotalNumberOfStepsTaken());
    }


    /**
     * Test the setTotalDistanceTravelledInKilometers(double) method correctly sets
     * total distance travelled
     *
     * */
    @Test
    public void setTotalDistanceTravelledInKilometers_ValidTotalDistance_NoException()
    {
        WorkoutSessionSummary workoutSessionSummary = new WorkoutSessionSummary();

        final double RANDOM_TOTAL_DISTANCE_TRAVELLED_IN_KILOMETERS = 500.66;

        final double MAXIMUM_DELTA = 0;

        workoutSessionSummary.setTotalDistanceTravelledInKilometers(
                                        RANDOM_TOTAL_DISTANCE_TRAVELLED_IN_KILOMETERS);

        assertEquals(RANDOM_TOTAL_DISTANCE_TRAVELLED_IN_KILOMETERS,
                    workoutSessionSummary.getTotalDistanceTravelledInKilometers(), MAXIMUM_DELTA);
    }



    /**
     * Test the setTotalCaloriesConsumed(double) method correctly sets
     * total calories consumed
     *
     * */
    @Test
    public void setTotalCaloriesConsumed_ValidTotalCalories_NoException()
    {
        WorkoutSessionSummary workoutSessionSummary = new WorkoutSessionSummary();

        final double RANDOM_TOTAL_CALORIES_CONSUMED = 66.56;

        final double MAXIMUM_DELTA = 0;

        workoutSessionSummary.setTotalCaloriesConsumed(RANDOM_TOTAL_CALORIES_CONSUMED);

        assertEquals(RANDOM_TOTAL_CALORIES_CONSUMED,
                    workoutSessionSummary.getTotalCaloriesConsumed(), MAXIMUM_DELTA);
    }
}
