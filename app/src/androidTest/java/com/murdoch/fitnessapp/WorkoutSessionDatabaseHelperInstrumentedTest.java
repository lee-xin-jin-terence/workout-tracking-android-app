package com.murdoch.fitnessapp;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.murdoch.fitnessapp.databases.exceptions.RecordInsertionException;
import com.murdoch.fitnessapp.databases.exceptions.RecordNotFoundException;
import com.murdoch.fitnessapp.databases.helpers.WorkoutSessionDatabaseHelper;
import com.murdoch.fitnessapp.models.implementations.StoredWorkoutSession;
import com.murdoch.fitnessapp.models.implementations.WorkoutSessionSummary;
import com.murdoch.fitnessapp.models.implementations.WorkoutSession;
import com.murdoch.fitnessapp.models.interfaces.IWorkoutSessionSummary;
import com.murdoch.fitnessapp.models.interfaces.IStoredWorkoutSession;
import com.murdoch.fitnessapp.models.interfaces.IWorkoutSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Instrumental test that tests the SQLite helper class WorkoutSessionDatabaseHelper
 * */
@RunWith(AndroidJUnit4.class)
public class WorkoutSessionDatabaseHelperInstrumentedTest
{
    Context context;
    WorkoutSessionDatabaseHelper databaseHelper;


    /**
     * Setup the test before running the various instrumented tests
     * */
    @Before
    public void setupTest()
    {
        this.context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        this.databaseHelper = new WorkoutSessionDatabaseHelper(context);

    }


    /**
     * Testing the method insertWorkoutSessionRecord(IWorkoutSession)
     *
     * Test that the helper can successfully insert a valid non-null WorkoutSession data
     * into the sqlite database
     *
     * */
    @Test
    public void insertWorkoutSessionRecord_InsertValidNonNullWorkoutSession_NoException()
            throws RecordInsertionException
    {
        this.databaseHelper.deleteAllWorkoutSessionRecords();

        final WorkoutSession RANDOM_WORKOUT_SESSION = getRandomWorkoutSession1();

        IStoredWorkoutSession storedWorkoutSession =
                    this.databaseHelper.insertWorkoutSessionRecord(RANDOM_WORKOUT_SESSION);

        long storedWorkoutSessionId = storedWorkoutSession.getWorkoutSessionId();


        // the id of the workout session id should not be 0
        assertTrue(storedWorkoutSessionId != 0);

        //the only difference between a WorkoutSession and StoredWorkoutSession is
        // the setWorkoutSessionId(long) and getWorkoutSessionId
        //therefore can use the overriden equals to test for equality
        assertTrue(RANDOM_WORKOUT_SESSION.equals(
                    (WorkoutSession) storedWorkoutSession));

        this.databaseHelper.deleteAllWorkoutSessionRecords();
    }


    /**
     * Testing the method insertWorkoutSessionRecord(IWorkoutSession)
     *
     * Test that the helper can successfully will throw a null pointer exception if
     * we try to insert a workout session record that is null (invalid)
     *
     * for the method insertWorkoutSessionRecord(IWorkoutSession)
     * */
    @Test
    public void insertWorkoutSessionRecord_InsertInvalidNullWorkoutSession_NullPointerException()
            throws RecordInsertionException
    {
        this.databaseHelper.deleteAllWorkoutSessionRecords();

        boolean nullPointerExceptionIsThrown = false;

        //there is no assertThrows() for instrumented test, so have to do it in
        // such a try-catch way

        try
        {
            this.databaseHelper.insertWorkoutSessionRecord(null);
        }
        catch (NullPointerException e)
        {
            nullPointerExceptionIsThrown = true;
        }

        assertTrue(nullPointerExceptionIsThrown);

    }


    /**
     * Testing the method queryWorkoutSessionSummary()
     *
     * Test that if 2 workout session records exist in the database, all the various
     * workout summary data will be correct when queried when the workout session
     * records fall within the stated start and end date arguments
     * */
    @Test
    public void queryWorkoutSessionSummary_QueryWhenTwoRecordsExist_NoException()
            throws RecordInsertionException
    {

        final IWorkoutSession RANDOM_WORKOUT_SESSION_1 = getRandomWorkoutSession1();
        final IWorkoutSession RANDOM_WORKOUT_SESSION_2 = getRandomWorkoutSession2();

        this.databaseHelper.insertWorkoutSessionRecord(RANDOM_WORKOUT_SESSION_1);
        this.databaseHelper.insertWorkoutSessionRecord(RANDOM_WORKOUT_SESSION_2);


        //this start date comes before the date of RANDOM_WORKOUT_SESSION_1 and
        // RANDOM_WORKOUT_SESSION_2
        LocalDate startDateInclusive = getStartDateInclusiveOfRandomWorkoutSession1And2();

        //this end date comes after the date of RANDOM_WORKOUT_SESSION_1 and
        // RANDOM_WORKOUT_SESSION_2
        LocalDate endDateExclusive = getEndDateExclusiveOfRandomWorkoutSession1And2();

        IWorkoutSessionSummary queryResult =
                this.databaseHelper.queryWorkoutSessionSummary(startDateInclusive, endDateExclusive);

        final double MAXIMUM_DELTA = 0;

        final int EXPECTED_TOTAL_NUMBER_OF_STEPS_TAKEN =
                RANDOM_WORKOUT_SESSION_1.getNumberOfStepsTaken() +
                        RANDOM_WORKOUT_SESSION_2.getNumberOfStepsTaken();

        final int EXPECTED_TOTAL_DURATION_IN_SECONDS =
                RANDOM_WORKOUT_SESSION_1.getDurationInSeconds() +
                        RANDOM_WORKOUT_SESSION_2.getDurationInSeconds();

        final double EXPECTED_TOTAL_DISTANCE_TRAVELLED_IN_KILOMETERS =
                RANDOM_WORKOUT_SESSION_1.getDistanceTravelledInKilometers() +
                        RANDOM_WORKOUT_SESSION_2.getDistanceTravelledInKilometers();

        final double EXPECTED_TOTAL_CALORIES_CONSUMED =
                RANDOM_WORKOUT_SESSION_1.getCaloriesConsumed() +
                        RANDOM_WORKOUT_SESSION_2.getCaloriesConsumed();

        assertEquals(EXPECTED_TOTAL_NUMBER_OF_STEPS_TAKEN, queryResult.getTotalNumberOfStepsTaken());

        assertEquals(EXPECTED_TOTAL_DURATION_IN_SECONDS, queryResult.getTotalDurationInSeconds());

        assertEquals(EXPECTED_TOTAL_DISTANCE_TRAVELLED_IN_KILOMETERS,
                queryResult.getTotalDistanceTravelledInKilometers(), MAXIMUM_DELTA);

        assertEquals(EXPECTED_TOTAL_CALORIES_CONSUMED, queryResult.getTotalCaloriesConsumed(),
                MAXIMUM_DELTA);


        this.databaseHelper.deleteAllWorkoutSessionRecords();
    }


    /**
     * Testing the method queryWorkoutSessionSummary()
     *
     * Test that if 3 workout session records exist in the database, and the third
     * workout record fall outside the specified start and end date range, the aggregated
     * workout summary will not include the third workout record when aggregating/summing data
     * */
    @Test
    public void queryWorkoutSessionSummary_QueryWhenThreeRecordsExistWithOneOutsideQueryRange_NoException() throws RecordInsertionException {

        this.databaseHelper.deleteAllWorkoutSessionRecords();


        final IWorkoutSession RANDOM_WORKOUT_SESSION_1 = getRandomWorkoutSession1();
        final IWorkoutSession RANDOM_WORKOUT_SESSION_2 = getRandomWorkoutSession2();
        final IWorkoutSession RANDOM_WORKOUT_SESSION_3 = getRandomWorkoutSession3();

        this.databaseHelper.insertWorkoutSessionRecord(RANDOM_WORKOUT_SESSION_1);
        this.databaseHelper.insertWorkoutSessionRecord(RANDOM_WORKOUT_SESSION_2);
        this.databaseHelper.insertWorkoutSessionRecord(RANDOM_WORKOUT_SESSION_3);

        //this start date comes before the date of RANDOM_WORKOUT_SESSION_1 and
        // RANDOM_WORKOUT_SESSION_2
        LocalDate startDateInclusive = getStartDateInclusiveOfRandomWorkoutSession1And2();

        //this end date comes after the date of RANDOM_WORKOUT_SESSION_1 and
        // RANDOM_WORKOUT_SESSION_2, excluding RANDOM_WORKOUT_SESSION_3
        LocalDate endDateExclusive = getEndDateExclusiveOfRandomWorkoutSession1And2();


        IWorkoutSessionSummary queryResult =
                this.databaseHelper.queryWorkoutSessionSummary(startDateInclusive, endDateExclusive);

        final double MAXIMUM_DELTA = 0;

        final int EXPECTED_TOTAL_NUMBER_OF_STEPS_TAKEN =
                RANDOM_WORKOUT_SESSION_1.getNumberOfStepsTaken() +
                        RANDOM_WORKOUT_SESSION_2.getNumberOfStepsTaken();

        final int EXPECTED_TOTAL_DURATION_IN_SECONDS =
                RANDOM_WORKOUT_SESSION_1.getDurationInSeconds() +
                        RANDOM_WORKOUT_SESSION_2.getDurationInSeconds();

        final double EXPECTED_TOTAL_DISTANCE_TRAVELLED_IN_KILOMETERS =
                RANDOM_WORKOUT_SESSION_1.getDistanceTravelledInKilometers() +
                        RANDOM_WORKOUT_SESSION_2.getDistanceTravelledInKilometers();

        final double EXPECTED_TOTAL_CALORIES_CONSUMED =
                RANDOM_WORKOUT_SESSION_1.getCaloriesConsumed() +
                        RANDOM_WORKOUT_SESSION_2.getCaloriesConsumed();


        assertEquals(EXPECTED_TOTAL_NUMBER_OF_STEPS_TAKEN, queryResult.getTotalNumberOfStepsTaken());

        assertEquals(EXPECTED_TOTAL_DURATION_IN_SECONDS, queryResult.getTotalDurationInSeconds());

        assertEquals(EXPECTED_TOTAL_DISTANCE_TRAVELLED_IN_KILOMETERS,
                queryResult.getTotalDistanceTravelledInKilometers(), MAXIMUM_DELTA);

        assertEquals(EXPECTED_TOTAL_CALORIES_CONSUMED, queryResult.getTotalCaloriesConsumed(),
                MAXIMUM_DELTA);


        this.databaseHelper.deleteAllWorkoutSessionRecords();
    }



    /**
     * Testing the method queryWorkoutSessionSummary()
     *
     * Test that if no workout records exist in the database, all the various
     * workout summary data will be at default (zero) when queried
     * */
    @Test
    public void queryWorkoutSessionSummary_QueryWhenNoRecordsExist_NoException()
    {
        this.databaseHelper.deleteAllWorkoutSessionRecords();

        final double EXPECTED_TOTAL_CALORIES_CONSUMED = 0;
        final double EXPECTED_TOTAL_DISTANCED_TRAVELLED_IN_KILOMETERS = 0;
        final int EXPECTED_TOTAL_NUMBER_OF_STEPS = 0;
        final int EXPECTED_TOTAL_DURATION_IN_SECONDS = 0;

        IWorkoutSessionSummary expectedResult = new WorkoutSessionSummary();

        expectedResult.setTotalCaloriesConsumed(EXPECTED_TOTAL_CALORIES_CONSUMED);

        expectedResult.setTotalDistanceTravelledInKilometers(
                EXPECTED_TOTAL_DISTANCED_TRAVELLED_IN_KILOMETERS);
        expectedResult.setTotalNumberOfStepsTaken(EXPECTED_TOTAL_NUMBER_OF_STEPS);
        expectedResult.setTotalDurationInSeconds(EXPECTED_TOTAL_DURATION_IN_SECONDS);

        LocalDate startDateInclusive = LocalDate.of(2020, 10, 10);
        LocalDate endDateExclusive = LocalDate.of(2020,10,13);

        IWorkoutSessionSummary queryResult =
                this.databaseHelper.queryWorkoutSessionSummary(startDateInclusive, endDateExclusive);

        assertTrue(expectedResult.equals(queryResult));

    }



    /**
     * Testing the method queryAllWorkoutSessionRecords()
     *
     * Test that if 2 workout session records exist in the database, then querying
     * a list of all records should return a list of that two records, in
     * descending order with respect to the of the workout sessions
     * */
    @Test
    public void queryAllWorkoutSessionRecords_QueryWhenTwoRecordsExist_NoException()
            throws RecordInsertionException
    {
        this.databaseHelper.deleteAllWorkoutSessionRecords();


        final IWorkoutSession RANDOM_WORKOUT_SESSION_1 = getRandomWorkoutSession1();
        final IWorkoutSession RANDOM_WORKOUT_SESSION_2 = getRandomWorkoutSession2();

        this.databaseHelper.insertWorkoutSessionRecord(RANDOM_WORKOUT_SESSION_1);
        this.databaseHelper.insertWorkoutSessionRecord(RANDOM_WORKOUT_SESSION_2);


        List<IStoredWorkoutSession> listOfStoredWorkoutSessions =
                                    this.databaseHelper.queryAllWorkoutSessionRecords();

        IStoredWorkoutSession firstStoredWorkoutSession = listOfStoredWorkoutSessions.get(0);
        IStoredWorkoutSession secondStoredWorkoutSession = listOfStoredWorkoutSessions.get(1);

        final int EXPECTED_LIST_SIZE = 2;

        assertEquals(EXPECTED_LIST_SIZE, listOfStoredWorkoutSessions.size());

        //second workout session will be the first in list
        assertTrue(RANDOM_WORKOUT_SESSION_2.equals(
                (WorkoutSession) firstStoredWorkoutSession)
        );

        //second workout session will be the second in list
        assertTrue(RANDOM_WORKOUT_SESSION_1.equals(
                (WorkoutSession) secondStoredWorkoutSession
        ));

        this.databaseHelper.deleteAllWorkoutSessionRecords();
    }


    /**
     * Testing the method queryAllWorkoutSessionRecords()
     *
     * Test that if no workout session records exist in the database, then querying
     * a list of all records should return an empty list
     *
     * */
    @Test
    public void queryAllWorkoutSessionRecords_QueryWhenNoRecordsExist_NoException()
    {
        this.databaseHelper.deleteAllWorkoutSessionRecords();


        List<IStoredWorkoutSession> listOfStoredWorkoutSessions =
                this.databaseHelper.queryAllWorkoutSessionRecords();

        final int EXPECTED_LIST_SIZE = 0;

        assertEquals(EXPECTED_LIST_SIZE, listOfStoredWorkoutSessions.size());


        this.databaseHelper.deleteAllWorkoutSessionRecords();
    }


    /**
     * Testing the method deleteWorkoutSessionRecord(long)
     *
     * Test that there will be no exceptions if we try to delete an existing workout
     * session record
     * */
    @Test
    public void deleteWorkoutSessionRecord_DeleteExistingRecord_NoException()
            throws RecordInsertionException, RecordNotFoundException
    {
        this.databaseHelper.deleteAllWorkoutSessionRecords();

        final WorkoutSession RANDOM_WORKOUT_SESSION = getRandomWorkoutSession1();

        IStoredWorkoutSession insertedWorkoutSession =
                    this.databaseHelper.insertWorkoutSessionRecord(RANDOM_WORKOUT_SESSION);

        long insertedWorkoutSessionId = insertedWorkoutSession.getWorkoutSessionId();


        //deleting workout record now
        final int AFTER_DELETION_EXPECTED_LIST_SIZE = 0;

        this.databaseHelper.deleteWorkoutSessionRecord(insertedWorkoutSessionId);

        List<IStoredWorkoutSession> listOfStoredWorkoutSessions =
                        this.databaseHelper.queryAllWorkoutSessionRecords();

        assertEquals(AFTER_DELETION_EXPECTED_LIST_SIZE, listOfStoredWorkoutSessions.size());

        this.databaseHelper.deleteAllWorkoutSessionRecords();
    }



    /**
     * Testing the method deleteWorkoutSessionRecord(long)
     *
     * Test that there will be RecordNotFoundException thrown if we try to delete a
     * non-existent workout session record
     * */
    @Test
    public void
            deleteWorkoutSessionRecord_DeleteNonExistentWorkoutSession_RecordNotFoundException()
    {
        this.databaseHelper.deleteAllWorkoutSessionRecords();

        final long RANDOM_NON_EXISTENT_WORKOUT_SESSION_ID = 999999999;

        boolean isRecordNotFoundExceptionThrown = false;

        try
        {
            this.databaseHelper.deleteWorkoutSessionRecord(
                            RANDOM_NON_EXISTENT_WORKOUT_SESSION_ID);
        }
        catch (RecordNotFoundException e)
        {
            isRecordNotFoundExceptionThrown = true;
        }

        assertTrue(isRecordNotFoundExceptionThrown);


        this.databaseHelper.deleteAllWorkoutSessionRecords();
    }


    /**
     * Testing the method deleteAllWorkoutSessionRecords()
     *
     * Test that the operation to delete all workout session records will be successful
     * when the database contain two such workout session records
     * */
    @Test
    public void deleteAllWorkoutSessionRecords_DeleteWithSomeRecordsExisting_NoException()
            throws RecordInsertionException
    {
        this.databaseHelper.deleteAllWorkoutSessionRecords();

        final WorkoutSession RANDOM_WORKOUT_SESSION_1 = getRandomWorkoutSession1();
        final WorkoutSession RANDOM_WORKOUT_SESSION_2 = getRandomWorkoutSession2();

        this.databaseHelper.insertWorkoutSessionRecord(RANDOM_WORKOUT_SESSION_1);
        this.databaseHelper.insertWorkoutSessionRecord(RANDOM_WORKOUT_SESSION_2);

        this.databaseHelper.deleteAllWorkoutSessionRecords();

        final int AFTER_DELETION_EXPECTED_LIST_SIZE = 0;

        List<IStoredWorkoutSession> listOfStoredWorkoutSessions =
                            this.databaseHelper.queryAllWorkoutSessionRecords();

        assertEquals(AFTER_DELETION_EXPECTED_LIST_SIZE, listOfStoredWorkoutSessions.size());

        this.databaseHelper.deleteAllWorkoutSessionRecords();
    }


    /**
     * Returns a random fixed workout session instance
     * */
    private WorkoutSession getRandomWorkoutSession1()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final IWorkoutSession.ActivityType RANDOM_ACTIVITY_TYPE =
                IWorkoutSession.ActivityType.WALKING;


        final int RANDOM_NUMBER_OF_STEPS_TAKEN = 1000;
        final int RANDOM_DURATION_IN_SECONDS = 500;

        final int RANDOM_YEAR = 2021;
        final int RANDOM_MONTH = 10;
        final int RANDOM_DAY = 22;
        final int RANDOM_HOUR = 15;
        final int RANDOM_MINUTE = 12;
        final int RANDOM_SECOND = 45;
        final LocalDateTime RANDOM_START_DATE_TIME =
                LocalDateTime.of(RANDOM_YEAR, RANDOM_MONTH, RANDOM_DAY,
                        RANDOM_HOUR, RANDOM_MINUTE, RANDOM_SECOND);

        workoutSession.setActivityType(RANDOM_ACTIVITY_TYPE);
        workoutSession.setNumberOfStepsTaken(RANDOM_NUMBER_OF_STEPS_TAKEN);
        workoutSession.setDurationInSeconds(RANDOM_DURATION_IN_SECONDS);
        workoutSession.setStartDateTime(RANDOM_START_DATE_TIME);

        workoutSession.calculateCaloriesConsumed();
        workoutSession.calculateDistanceTravelled();

        return workoutSession;
    }


    /**
     * Returns a random fixed workout session instance
     * */
    private WorkoutSession getRandomWorkoutSession2()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final IWorkoutSession.ActivityType RANDOM_ACTIVITY_TYPE =
                IWorkoutSession.ActivityType.WALKING;


        final int RANDOM_NUMBER_OF_STEPS_TAKEN = 2000;
        final int RANDOM_DURATION_IN_SECONDS = 200;

        final int RANDOM_YEAR = 2021;
        final int RANDOM_MONTH = 10;
        final int RANDOM_DAY = 23;
        final int RANDOM_HOUR = 15;
        final int RANDOM_MINUTE = 12;
        final int RANDOM_SECOND = 45;
        final LocalDateTime RANDOM_START_DATE_TIME =
                LocalDateTime.of(RANDOM_YEAR, RANDOM_MONTH, RANDOM_DAY,
                        RANDOM_HOUR, RANDOM_MINUTE, RANDOM_SECOND);

        workoutSession.setActivityType(RANDOM_ACTIVITY_TYPE);
        workoutSession.setNumberOfStepsTaken(RANDOM_NUMBER_OF_STEPS_TAKEN);
        workoutSession.setDurationInSeconds(RANDOM_DURATION_IN_SECONDS);
        workoutSession.setStartDateTime(RANDOM_START_DATE_TIME);

        workoutSession.calculateCaloriesConsumed();
        workoutSession.calculateDistanceTravelled();

        return workoutSession;
    }


    /**
     * Returns a random fixed workout session instance
     * */
    private WorkoutSession getRandomWorkoutSession3()
    {
        WorkoutSession workoutSession = new WorkoutSession();

        final IWorkoutSession.ActivityType RANDOM_ACTIVITY_TYPE =
                IWorkoutSession.ActivityType.WALKING;


        final int RANDOM_NUMBER_OF_STEPS_TAKEN = 1300;
        final int RANDOM_DURATION_IN_SECONDS = 1900;

        final int RANDOM_YEAR = 2022;
        final int RANDOM_MONTH = 11;
        final int RANDOM_DAY = 15;
        final int RANDOM_HOUR = 20;
        final int RANDOM_MINUTE = 12;
        final int RANDOM_SECOND = 45;
        final LocalDateTime RANDOM_START_DATE_TIME =
                LocalDateTime.of(RANDOM_YEAR, RANDOM_MONTH, RANDOM_DAY,
                        RANDOM_HOUR, RANDOM_MINUTE, RANDOM_SECOND);

        workoutSession.setActivityType(RANDOM_ACTIVITY_TYPE);
        workoutSession.setNumberOfStepsTaken(RANDOM_NUMBER_OF_STEPS_TAKEN);
        workoutSession.setDurationInSeconds(RANDOM_DURATION_IN_SECONDS);
        workoutSession.setStartDateTime(RANDOM_START_DATE_TIME);

        workoutSession.calculateCaloriesConsumed();
        workoutSession.calculateDistanceTravelled();

        return workoutSession;
    }


    /**
     * Returns a start date that comes before random workout session 1
     * and random workout session 2
     *
     * @see #getRandomWorkoutSession1()
     * @see #getRandomWorkoutSession2()
     * */
    private LocalDate getStartDateInclusiveOfRandomWorkoutSession1And2()
    {
        final int RANDOM_DATE_YEAR = 2021;
        final int RANDOM_DATE_MONTH = 10;
        final int RANDOM_DATE_START_DAY_INCLUSIVE = 22;

        return LocalDate.of(RANDOM_DATE_YEAR, RANDOM_DATE_MONTH,
                RANDOM_DATE_START_DAY_INCLUSIVE);
    }


    /**
     * Returns a end date that comes after random workout session 1
     * and random workout session 2
     *
     * @see #getRandomWorkoutSession1()
     * @see #getRandomWorkoutSession2()
     * */
    private LocalDate getEndDateExclusiveOfRandomWorkoutSession1And2()
    {
        final int RANDOM_DATE_YEAR = 2021;
        final int RANDOM_DATE_MONTH = 10;

        final int RANDOM_DATE_END_DAY_EXCLUSIVE = 24;

        return LocalDate.of(RANDOM_DATE_YEAR,RANDOM_DATE_MONTH,
                    RANDOM_DATE_END_DAY_EXCLUSIVE);
    }








}