package com.murdoch.fitnessapp.databases.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.murdoch.fitnessapp.databases.exceptions.RecordInsertionException;
import com.murdoch.fitnessapp.databases.exceptions.RecordNotFoundException;
import com.murdoch.fitnessapp.models.implementations.WorkoutSessionSummary;
import com.murdoch.fitnessapp.models.implementations.GPSLocation;
import com.murdoch.fitnessapp.models.implementations.StoredWorkoutSession;
import com.murdoch.fitnessapp.models.interfaces.IWorkoutSessionSummary;
import com.murdoch.fitnessapp.models.interfaces.IGPSLocation;
import com.murdoch.fitnessapp.models.interfaces.IWorkoutSession;
import com.murdoch.fitnessapp.models.interfaces.IStoredWorkoutSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


/**
 * A database helper that allows for inserting, querying and deleting
 * of workout session data
 *
 * */
public class WorkoutSessionDatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "WORKOUT_DB.db";
    private static final int VERSION_NUMBER = 3;

    private static final String WORKOUT_SESSION_TABLE_NAME = "WorkoutSession";
    private static final String WORKOUT_SESSION_COLUMN_SESSION_ID = "sessionId";
    private static final String WORKOUT_SESSION_COLUMN_ACTIVITY_TYPE = "activityType";

    private static final String WORKOUT_SESSION_COLUMN_NUMBER_OF_STEPS_TAKEN = "numberOfStepsTaken";
    private static final String WORKOUT_SESSION_COLUMN_START_DATE_TIME = "startDateTime";
    private static final String WORKOUT_SESSION_COLUMN_DURATION_IN_SECONDS = "durationInSeconds";
    private static final String WORKOUT_SESSION_COLUMN_CALORIES_CONSUMED = "caloriesConsumed";
    private static final String WORKOUT_SESSION_COLUMN_DISTANCE_TRAVELLED_IN_KILOMETERS
                                                                            = "distanceTravelled";


    private static final String WORKOUT_SESSION_GPS_LOCATION_TABLE_NAME
            = "WorkoutSessionGPSLocation";
    private static final String WORKOUT_SESSION_GPS_LOCATION_COLUMN_SESSION_ID
            = WORKOUT_SESSION_COLUMN_SESSION_ID;
    private static final String WORKOUT_SESSION_GPS_LOCATION_COLUMN_LOCATION_INDEX = "locationIndex";
    private static final String WORKOUT_SESSION_GPS_LOCATION_COLUMN_IS_LOCATION_NULL =
                                                                                "isLocationNull";
    private static final String WORKOUT_SESSION_GPS_LOCATION_COLUMN_LATITUDE = "latitude";
    private static final String WORKOUT_SESSION_GPS_LOCATION_COLUMN_LONGITUDE = "longitude";


    private static final String WORKOUT_SESSION_IMAGE_TABLE_NAME = "WorkoutSessionImage";
    private static final String WORKOUT_SESSION_IMAGE_COLUMN_IMAGE_PATH = "imagePath";
    private static final String WORKOUT_SESSION_IMAGE_COLUMN_IMAGE_PATH_INDEX = "imagePathIndex";
    private static final String WORKOUT_SESSION_IMAGE_COLUMN_SESSION_ID
                                                        = WORKOUT_SESSION_COLUMN_SESSION_ID;



    private static final String COLUMN_ALIAS_TOTAL_NUMBER_OF_STEPS_TAKEN = "totalNumberOfStepsTaken";
    private static final String COLUMN_ALIAS_TOTAL_DURATION_IN_SECONDS = "totalDurationInSeconds";
    private static final String COLUMN_ALIAS_TOTAL_CALORIES_CONSUMED = "totalCaloriesConsumed";
    private static final String COLUMN_ALIAS_TOTAL_DISTANCE_TRAVELLED = "totalDistanceTravelled";


    /**
     * Create an instance of WorkoutSessionDatabaseHelper
     *
     * @param context The context used for locating paths to the database.
     *                May be null
     * */
    public WorkoutSessionDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }


    /**
     * Foreign key constraints are not enabled by default, therefore
     * need to set them during the onConfigure() phase
     * */
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

        db.setForeignKeyConstraintsEnabled(true);

    }


    /**
     * Call when the database is created for the first time. Creation
     * of tables take place here
     *
     * @param sqLiteDatabase   The database handle
     * */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        this.createWorkoutSessionTable(sqLiteDatabase);
        this.createWorkoutSessionGPSLocationTable(sqLiteDatabase);
        this.createWorkoutSessionImageTable(sqLiteDatabase);

    }


    /**
     * Called when the database needs to be upgraded
     *
     * Note: This method executes within a transaction. If an exception is thrown,
     * all changes will automatically be rolled back.
     *
     * @param sqLiteDatabase   The database handle
     * @param oldVersionNumber the old database version number
     * @param newVersionNumber the new database version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersionNumber,
                          int newVersionNumber) {
        this.dropAllTables(sqLiteDatabase);

        this.onCreate(sqLiteDatabase);

    }


    /**
     * Called when the database needs to be downgraded
     * <p>
     * Note: This method executes within a transaction. If an exception is thrown,
     * all changes will automatically be rolled back.
     *
     * @param sqLiteDatabase   The database handle
     * @param oldVersionNumber the old database version number
     * @param newVersionNumber the new database version number
     */
    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersionNumber,
                            int newVersionNumber)
    {
        this.dropAllTables(sqLiteDatabase);

        this.onCreate(sqLiteDatabase);

    }


    /**
     * Drop all existing tables if they exists
     *
     * @param sqLiteDatabase the sqliteDatabase handle used to create a table
     */
    private void dropAllTables(SQLiteDatabase sqLiteDatabase)
    {
        String dropWorkoutSessionTableQuery =
                "DROP TABLE IF EXISTS " + WORKOUT_SESSION_TABLE_NAME;

        String dropWorkoutSessionImageTableQuery =
                "DROP TABLE IF EXISTS " + WORKOUT_SESSION_IMAGE_TABLE_NAME;

        String dropWorkoutSessionGPSLocationTableQuery =
                "DROP TABLE IF EXISTS " + WORKOUT_SESSION_GPS_LOCATION_TABLE_NAME;

        sqLiteDatabase.execSQL(dropWorkoutSessionImageTableQuery);
        sqLiteDatabase.execSQL(dropWorkoutSessionGPSLocationTableQuery);
        sqLiteDatabase.execSQL(dropWorkoutSessionTableQuery);
    }


    /**
     * Create a table various details regarding the workout session
     * (WORKOUT_SESSION table)
     *
     * @param sqLiteDatabase the sqliteDatabase handle used to create a table
     */
    private void createWorkoutSessionTable(SQLiteDatabase sqLiteDatabase)
    {
        String createTableString =
                "CREATE TABLE " + WORKOUT_SESSION_TABLE_NAME + " ( " +
                        WORKOUT_SESSION_COLUMN_SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        WORKOUT_SESSION_COLUMN_ACTIVITY_TYPE + " TEXT NOT NULL, " +
                        WORKOUT_SESSION_COLUMN_START_DATE_TIME + " INTEGER NOT NULL, " +
                        WORKOUT_SESSION_COLUMN_NUMBER_OF_STEPS_TAKEN + " INTEGER NOT NULL, " +
                        WORKOUT_SESSION_COLUMN_DURATION_IN_SECONDS + " INTEGER NOT NULL, " +
                        WORKOUT_SESSION_COLUMN_CALORIES_CONSUMED + " REAL NOT NULL, " +
                        WORKOUT_SESSION_COLUMN_DISTANCE_TRAVELLED_IN_KILOMETERS + " REAL NOT NULL );";

        sqLiteDatabase.execSQL(createTableString);
    }


    /**
     * Create a table storing lists of gps locations representing the geo-coordinates
     * of the places travelled
     * <p>
     * This table can only be created after WORKOUT_SESSION table is created, therefore
     * must call the method createWorkoutSessionTable(SQLiteDatabase) first
     *
     * @param sqLiteDatabase the sqliteDatabase handle used to create a table
     * @see #createWorkoutSessionTable(SQLiteDatabase)
     */
    private void createWorkoutSessionGPSLocationTable(SQLiteDatabase sqLiteDatabase)
    {
        String createTableString =
                "CREATE TABLE " + WORKOUT_SESSION_GPS_LOCATION_TABLE_NAME + " ( " +
                        WORKOUT_SESSION_GPS_LOCATION_COLUMN_SESSION_ID + " INTEGER NOT NULL, " +
                        WORKOUT_SESSION_GPS_LOCATION_COLUMN_LOCATION_INDEX + " INTEGER NOT NULL, " +
                        WORKOUT_SESSION_GPS_LOCATION_COLUMN_IS_LOCATION_NULL + " INTEGER NOT NULL, " +
                        WORKOUT_SESSION_GPS_LOCATION_COLUMN_LATITUDE + " REAL, " +
                        WORKOUT_SESSION_GPS_LOCATION_COLUMN_LONGITUDE + " REAL, " +
                        "PRIMARY KEY ( " + WORKOUT_SESSION_GPS_LOCATION_COLUMN_SESSION_ID +
                        ", " + WORKOUT_SESSION_GPS_LOCATION_COLUMN_LOCATION_INDEX + " ), " +
                        "FOREIGN KEY ( " + WORKOUT_SESSION_GPS_LOCATION_COLUMN_SESSION_ID +
                        " ) REFERENCES " + WORKOUT_SESSION_TABLE_NAME +
                        " ( " + WORKOUT_SESSION_COLUMN_SESSION_ID + " ) " +
                        "ON UPDATE NO ACTION ON DELETE CASCADE);";

        sqLiteDatabase.execSQL(createTableString);
    }


    /**
     * Create a table storing lists of images taken during each workout session
     * <p>
     * This table can only be created after WORKOUT_SESSION table is created, therefore
     * must call the method createWorkoutSessionTable(SQLiteDatabase) first
     *
     * @param sqLiteDatabase the sqliteDatabase handle used to create a table
     * @see #createWorkoutSessionTable(SQLiteDatabase)
     */
    private void createWorkoutSessionImageTable(SQLiteDatabase sqLiteDatabase)
    {

        String createTableString =
                "CREATE TABLE " + WORKOUT_SESSION_IMAGE_TABLE_NAME + " ( " +
                        WORKOUT_SESSION_IMAGE_COLUMN_IMAGE_PATH + " TEXT PRIMARY KEY, " +
                        WORKOUT_SESSION_IMAGE_COLUMN_IMAGE_PATH_INDEX + " INTEGER NOT NULL, " +
                        WORKOUT_SESSION_IMAGE_COLUMN_SESSION_ID + " INTEGER NOT NULL, " +
                        "FOREIGN KEY ( " + WORKOUT_SESSION_IMAGE_COLUMN_SESSION_ID +
                        " ) REFERENCES " + WORKOUT_SESSION_TABLE_NAME +
                        " ( " + WORKOUT_SESSION_COLUMN_SESSION_ID + " ) " +
                        " ON UPDATE NO ACTION ON DELETE CASCADE);";

        sqLiteDatabase.execSQL(createTableString);
    }





    /**
     * Inserts the workout session as a record into the database
     *
     * @param workoutSession the workout session to be inserted into the database
     *
     * @throws NullPointerException if the argument workoutSession is null
     * @throws RecordInsertionException if the workout session fails to be inserted into
     * the database
     * */
    public IStoredWorkoutSession insertWorkoutSessionRecord(IWorkoutSession workoutSession)
            throws RecordInsertionException
    {
        if (workoutSession == null)
        {
            throw new NullPointerException("Workout session cannot be null");
        }

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.beginTransaction();


        //throws RecordInsertionException if failure
        long sessionId = insertWorkoutSessionRecordAndGetSessionId(workoutSession, sqLiteDatabase);

        //throws RecordInsertionException if failure
        insertListOfWorkoutSessionGPSLocations(workoutSession.getListOfGPSLocations(),
                sessionId, sqLiteDatabase);

        //throws RecordInsertionException if failure
        insertListOfWorkoutSessionImagePaths(workoutSession.getListOfImagesTaken(), sessionId,
                sqLiteDatabase);

        IStoredWorkoutSession storedWorkoutSession =
                convertWorkoutSessionToStoredWorkoutSession(sessionId, workoutSession);

        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();


        return storedWorkoutSession;
    }




    /**
     * Inserts a workout session record and returns the workout session id of the record
     *
     * @param workoutSession the workout session to be inserted into the database
     * @param sqLiteDatabase the database handle with write permission, which is
     *                       part of a transaction
     *
     * @throws RecordInsertionException if the database fails to insert the record
     * */
    private static long insertWorkoutSessionRecordAndGetSessionId(IWorkoutSession workoutSession,
                                                           SQLiteDatabase sqLiteDatabase)
            throws RecordInsertionException
    {
        ContentValues contentValues = convertIWorkoutSessionToContentValues(workoutSession);

        final String NO_NULL_COLUMN_HACK = null;

        long sessionId = sqLiteDatabase.insert(WORKOUT_SESSION_TABLE_NAME,
                NO_NULL_COLUMN_HACK, contentValues);

        if (sessionId == -1)
        {
            throw new RecordInsertionException("Failed to insert WorkoutSession Record");
        }


        return sessionId;
    }


    /**
     * Convert a IWorkoutSession instance into ContentValues (key-value pairs)
     *
     * @param workoutSession workout session to be converted
     * @return ContentValues the corresponding ContentValues
     */
    private static ContentValues convertIWorkoutSessionToContentValues(
            IWorkoutSession workoutSession)
    {
        ContentValues contentValues = new ContentValues();


        contentValues.put(WORKOUT_SESSION_COLUMN_ACTIVITY_TYPE,
                workoutSession.getActivityType().name());

        contentValues.put(WORKOUT_SESSION_COLUMN_NUMBER_OF_STEPS_TAKEN,
                workoutSession.getNumberOfStepsTaken());

        contentValues.put(WORKOUT_SESSION_COLUMN_DURATION_IN_SECONDS,
                workoutSession.getDurationInSeconds());


        contentValues.put(WORKOUT_SESSION_COLUMN_START_DATE_TIME,
                workoutSession.getStartDateTime().toEpochSecond(ZoneOffset.UTC));

        contentValues.put(WORKOUT_SESSION_COLUMN_CALORIES_CONSUMED,
                workoutSession.getCaloriesConsumed());

        contentValues.put(WORKOUT_SESSION_COLUMN_DISTANCE_TRAVELLED_IN_KILOMETERS,
                workoutSession.getDistanceTravelledInKilometers());

        return contentValues;
    }


    /**
     * Convert a IWorkoutSession instance into a IStoredWorkoutSession instance
     *
     * @param workoutSessionId the id of the workout session
     * @param workoutSession   the workout session
     * @return a corresponding IStoredWorkoutSession instance
     */
    private static IStoredWorkoutSession convertWorkoutSessionToStoredWorkoutSession(
            long workoutSessionId,
            IWorkoutSession workoutSession)
    {
        IStoredWorkoutSession storedWorkoutSession = new StoredWorkoutSession();

        storedWorkoutSession.setWorkoutSessionId(workoutSessionId);
        storedWorkoutSession.setActivityType(workoutSession.getActivityType());

        storedWorkoutSession.setNumberOfStepsTaken(workoutSession.getNumberOfStepsTaken());
        storedWorkoutSession.setStartDateTime(workoutSession.getStartDateTime());
        storedWorkoutSession.setDurationInSeconds(workoutSession.getDurationInSeconds());

        storedWorkoutSession.calculateCaloriesConsumed();
        storedWorkoutSession.calculateDistanceTravelled();

        return storedWorkoutSession;
    }


    /**
     * Insert a list of GPSLocations associated with the workout session into the
     * table WORKOUT_SESSION_GPS_LOCATION_TABLE_NAME
     *
     * @param listOfGPSLocations list of GPSLocations
     * @param workoutSessionId   the id of the workout session
     * @param sqLiteDatabase     the sqliteDatabase, which is part of a transaction
     */
    private static void insertListOfWorkoutSessionGPSLocations(List<IGPSLocation> listOfGPSLocations,
                                                        long workoutSessionId,
                                                        SQLiteDatabase sqLiteDatabase)
            throws RecordInsertionException {

        final String NO_NULL_COLUMN_HACK = null;

        try {
            for (int index = 0; index < listOfGPSLocations.size(); index++) {
                IGPSLocation currentGPSLocation = listOfGPSLocations.get(index);

                ContentValues contentValues = convertIGPSLocationToContentValues(
                        currentGPSLocation, index, workoutSessionId);

                sqLiteDatabase.insertOrThrow(WORKOUT_SESSION_GPS_LOCATION_TABLE_NAME,
                        NO_NULL_COLUMN_HACK, contentValues);

            }
        } catch (Exception exception)
        {
            throw new RecordInsertionException("Failed to insert WorkoutSessionGPSLocation Record");
        }
    }


    /**
     * Convert a IGPSLocation instance into ContentValues (key-value pairs)
     *
     * @param gpsLocation              GPSLocation instance to be converted
     * @param indexOfGPSLocationInList index of the GPSLocation instance within a list
     * @param workoutSessionId         the id of the workout session record
     * @return ContentValues the corresponding ContentValues
     */
    private static ContentValues convertIGPSLocationToContentValues(IGPSLocation gpsLocation,
                                                                    int indexOfGPSLocationInList,
                                                                    long workoutSessionId) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(WORKOUT_SESSION_GPS_LOCATION_COLUMN_SESSION_ID,
                workoutSessionId);

        contentValues.put(WORKOUT_SESSION_GPS_LOCATION_COLUMN_LOCATION_INDEX,
                indexOfGPSLocationInList);

        final int TRUE = 1;
        final int FALSE = 0;

        //gps may be null as null is used to represent a pause during a workout session
        if (gpsLocation != null)
        {
            contentValues.put(WORKOUT_SESSION_GPS_LOCATION_COLUMN_IS_LOCATION_NULL, TRUE);

            contentValues.put(WORKOUT_SESSION_GPS_LOCATION_COLUMN_LATITUDE,
                    gpsLocation.getLatitude());

            contentValues.put(WORKOUT_SESSION_GPS_LOCATION_COLUMN_LONGITUDE,
                    gpsLocation.getLongitude());
        }
        else
        {
            contentValues.put(WORKOUT_SESSION_GPS_LOCATION_COLUMN_IS_LOCATION_NULL, FALSE);

            contentValues.putNull(WORKOUT_SESSION_GPS_LOCATION_COLUMN_LATITUDE);

            contentValues.putNull(WORKOUT_SESSION_GPS_LOCATION_COLUMN_LONGITUDE);
        }



        return contentValues;
    }


    /**
     * Insert a list of image paths associated with the workout session into the
     * table WORKOUT_SESSION_IMAGE_TABLE_NAME
     *
     * @param listOfImagePaths list of image paths
     * @param workoutSessionId the id of the workout session
     * @param sqLiteDatabase   the sqliteDatabase, which is part of a transaction
     */
    private void insertListOfWorkoutSessionImagePaths(List<String> listOfImagePaths,
                                                      long workoutSessionId,
                                                      SQLiteDatabase sqLiteDatabase)
            throws RecordInsertionException
    {

        final String NO_NULL_COLUMN_HACK = null;

        try {
            for (int index = 0; index < listOfImagePaths.size(); index++) {
                String imagePath = listOfImagePaths.get(index);

                ContentValues contentValues = convertImagePathToContentValues(imagePath,
                        index, workoutSessionId);

                sqLiteDatabase.insertOrThrow(WORKOUT_SESSION_IMAGE_TABLE_NAME,
                        NO_NULL_COLUMN_HACK, contentValues);

            }
        } catch (Exception exception) {
            throw new RecordInsertionException("Failed to insert WorkoutSessionImage Record");
        }
    }


    /**
     * Convert an image path into ContentValues (key-value pairs)
     *
     * @param imagePath              image path of the image
     * @param indexOfImagePathInList index of the image path within a list
     * @param workoutSessionId       the id of the workout session record
     * @return ContentValues the corresponding ContentValues
     */
    private static ContentValues convertImagePathToContentValues(String imagePath,
                                                                 int indexOfImagePathInList,
                                                                 long workoutSessionId) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(WORKOUT_SESSION_IMAGE_COLUMN_IMAGE_PATH, imagePath);
        contentValues.put(WORKOUT_SESSION_IMAGE_COLUMN_IMAGE_PATH_INDEX, indexOfImagePathInList);
        contentValues.put(WORKOUT_SESSION_COLUMN_SESSION_ID, workoutSessionId);

        return contentValues;
    }



    /**
     * Returns a summary of the overall aggregated workout session summary based on
     * the date range give
     *
     * @param startDateInclusive  the start date inclusive of the date range of interest
     * @param endDateExclusive the end date exclusive of the date range of interest
     *
     * @throws NullPointerException if either of the arguments startDateInclusive or
     * endDateExclusive are null
     *
     * @return a IWorkoutSessionSummary instance containing the aggregated workout session
     * data
     * */
    public IWorkoutSessionSummary queryWorkoutSessionSummary(LocalDate startDateInclusive,
                                                             LocalDate endDateExclusive)
    {
        if (startDateInclusive == null || endDateExclusive == null)
        {
            throw new NullPointerException(
                    "Start date inclusive or end date exclusive cannot be null");
        }

        long startDateTimeInclusiveEpochSeconds =
                convertLocalDateToEpochSeconds(startDateInclusive);

        long endDateTimeExclusiveEpochSeconds =
                convertLocalDateToEpochSeconds(endDateExclusive);


        Cursor resultCursor = queryWorkoutSessionSummaryAndGetCursor(
                startDateTimeInclusiveEpochSeconds, endDateTimeExclusiveEpochSeconds);

        return convertCursorToWorkoutSessionSummary(resultCursor);

    }


    /**
     * Convert a LocalDate instance to epoch seconds
     *
     * @param localDate the LocalDate instance to be converted
     *
     * @return the equivalent of the argument in epoch seconds
     * */
    private static long convertLocalDateToEpochSeconds(LocalDate localDate)
    {
        LocalDateTime localDateTime =
                LocalDateTime.of(localDate, getMidnightLocalTime());

        return localDateTime.toEpochSecond(ZoneOffset.UTC);
    }


    /**
     * Return a LocalDate instance representing midnight time
     * */
    private static LocalTime getMidnightLocalTime()
    {
        final int HOUR = 0;
        final int MINUTE = 0;
        final int SECOND = 0;

        return LocalTime.of(HOUR, MINUTE, SECOND);
    }



    /**
     * Makes an aggregate query that queries the table WORKOUT_SESSION_TABLE_NAME for
     * a workout session summary. This method then returns a cursor.
     *
     * @param startDateTimeInclusiveEpochSeconds the start date time inclusive of the
     *                                           range of interest in epoch seconds
     *
     * @param endDateTimeExclusiveEpochSeconds the end date time exclusive of the
     *                                         range of interest in epoch seconds
     *
     * @return a cursor of the SQLiteDatabase query
     * */
    private Cursor queryWorkoutSessionSummaryAndGetCursor(
            long startDateTimeInclusiveEpochSeconds, long endDateTimeExclusiveEpochSeconds)
    {
        String[] columns = new String[]{
                "SUM( " + WORKOUT_SESSION_COLUMN_NUMBER_OF_STEPS_TAKEN + " ) AS "
                                    + COLUMN_ALIAS_TOTAL_NUMBER_OF_STEPS_TAKEN,

                "SUM( " + WORKOUT_SESSION_COLUMN_DURATION_IN_SECONDS + " ) AS "
                                    + COLUMN_ALIAS_TOTAL_DURATION_IN_SECONDS,

                "SUM( " + WORKOUT_SESSION_COLUMN_CALORIES_CONSUMED + " ) AS "
                                    + COLUMN_ALIAS_TOTAL_CALORIES_CONSUMED,

                "SUM( " + WORKOUT_SESSION_COLUMN_DISTANCE_TRAVELLED_IN_KILOMETERS + " ) AS "
                                    + COLUMN_ALIAS_TOTAL_DISTANCE_TRAVELLED
        };

        String selection =
                WORKOUT_SESSION_COLUMN_START_DATE_TIME + " >= ? AND " +
                        WORKOUT_SESSION_COLUMN_START_DATE_TIME + " < ?";

        String[] selectionArgs = new String[]{
                String.valueOf(startDateTimeInclusiveEpochSeconds),
                String.valueOf(endDateTimeExclusiveEpochSeconds),
        };

        final String NO_GROUP_BY_CLAUSE = null;
        final String NO_HAVING_CLAUSE = null;
        final String NO_ORDER_BY_CLAUSE = null;

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        return sqLiteDatabase.query(WORKOUT_SESSION_TABLE_NAME,
                columns, selection, selectionArgs, NO_GROUP_BY_CLAUSE, NO_HAVING_CLAUSE,
                NO_ORDER_BY_CLAUSE);
    }




    /**
     * Reads through the data pointed by the cursor and returns a corresponding
     * IWorkoutSessionSummary instance containing the data retrieved
     *
     * Note: This method moves the position of the cursor only once
     *
     * @param resultCursor the cursor of the result from an aggregate query of the
     *                     WORKOUT_SESSION_TABLE_NAME table
     *
     * @return a corresponding IStoredWorkoutSession instance based on the data retrieved
     * from the record pointed by the cursor
     * */
    private static IWorkoutSessionSummary convertCursorToWorkoutSessionSummary(Cursor resultCursor)
    {
        IWorkoutSessionSummary workoutSessionSummary = new WorkoutSessionSummary();

        //if no records are found, return the  default summary instance with all values set to 0
        if (!resultCursor.moveToNext())
        {
            return workoutSessionSummary;
        }

        setTotalNumberOfStepsTakenFromCursor(resultCursor, workoutSessionSummary);
        setTotalDurationInSecondsFromCursor(resultCursor, workoutSessionSummary);
        setTotalCaloriesConsumedFromCursor(resultCursor, workoutSessionSummary);
        setTotalDistanceTravelledInKilometersFromCursor(resultCursor, workoutSessionSummary);


        resultCursor.close();

        return workoutSessionSummary;
    }



    /**
     * Sets the total number of steps taken of the workout session summary based on the record
     * currently pointed by the cursor. This method does not move the position of the cursor
     *
     * @param resultCursor the cursor of the result from an aggregate query of the
     *                      WORKOUT_SESSION_TABLE_NAME table
     *
     * @param workoutSessionSummary This is the return value of the method. When this method
     *                             finishes executing, this argument will contain the total
     *                            number of steps taken from the record pointed by the cursor.
     *                              Cannot be null.
     * */
    private static void setTotalNumberOfStepsTakenFromCursor(Cursor resultCursor,
                                             IWorkoutSessionSummary workoutSessionSummary)
    {
        int totalNumberOfStepsTaken = resultCursor.getInt(
                resultCursor.getColumnIndexOrThrow(COLUMN_ALIAS_TOTAL_NUMBER_OF_STEPS_TAKEN));

        workoutSessionSummary.setTotalNumberOfStepsTaken(totalNumberOfStepsTaken);
    }



    /**
     * Sets the total exercise duration of the workout session summary based on the record
     * currently pointed by the cursor. This method does not move the position of the cursor
     *
     * @param resultCursor the cursor of the result from an aggregate query of the
     *                      WORKOUT_SESSION_TABLE_NAME table
     *
     * @param workoutSessionSummary This is the return value of the method. When this method
     *                             finishes executing, this argument will contain the total
     *                             exercise duration from the record pointed by the cursor.
     *                              Cannot be null.
     * */
    private static void setTotalDurationInSecondsFromCursor(Cursor resultCursor,
                                           IWorkoutSessionSummary workoutSessionSummary)
    {
        int totalDurationInSeconds = resultCursor.getInt(
                resultCursor.getColumnIndexOrThrow(COLUMN_ALIAS_TOTAL_DURATION_IN_SECONDS));

        workoutSessionSummary.setTotalDurationInSeconds(totalDurationInSeconds);
    }


    /**
     * Sets the total calories consumed of the workout session summary based on the record
     * currently pointed by the cursor. This method does not move the position of the cursor
     *
     * @param resultCursor the cursor of the result from an aggregate query of the
     *                      WORKOUT_SESSION_TABLE_NAME table
     *
     * @param workoutSessionSummary This is the return value of the method. When this method
     *                             finishes executing, this argument will contain the workout
     *                             total calories consumed from the record pointed by the cursor.
     *                              Cannot be null.
     * */
    private static void setTotalCaloriesConsumedFromCursor(Cursor resultCursor,
                                           IWorkoutSessionSummary workoutSessionSummary)
    {
        double totalCaloriesConsumed = resultCursor.getDouble(
                resultCursor.getColumnIndexOrThrow(COLUMN_ALIAS_TOTAL_CALORIES_CONSUMED));

        workoutSessionSummary.setTotalCaloriesConsumed(totalCaloriesConsumed);
    }



    /**
     * Sets the total distance travelled of the workout session summary based on the record
     * currently pointed by the cursor. This method does not move the position of the cursor
     *
     * @param resultCursor the cursor of the result from an aggregate query of the
     *                      WORKOUT_SESSION_TABLE_NAME table
     *
     * @param workoutSessionSummary This is the return value of the method. When this method
     *                             finishes executing, this argument will contain the workout
     *                             total distance travelled from the record pointed by the cursor.
     *                              Cannot be null.
     * */
    private static void setTotalDistanceTravelledInKilometersFromCursor(Cursor resultCursor,
                                            IWorkoutSessionSummary workoutSessionSummary)
    {
        double totalDistanceTravelledInKilometers = resultCursor.getDouble(
                resultCursor.getColumnIndexOrThrow(COLUMN_ALIAS_TOTAL_DISTANCE_TRAVELLED));


        workoutSessionSummary.setTotalDistanceTravelledInKilometers(
                                                totalDistanceTravelledInKilometers);
    }



    /**
     * Returns a list of all workout session records stored in the database, order in descending
     * order according to start date time. This means the the list begins with the most recent
     * record and ends with the oldest record.
     *
     * @return a list containing all workout session records
     * */
    public List<IStoredWorkoutSession> queryAllWorkoutSessionRecords()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        sqLiteDatabase.beginTransaction();

        Cursor resultCursor = queryAllWorkoutSessionRecordsAndGetCursor(sqLiteDatabase);

        List<IStoredWorkoutSession> listOfStoredWorkSessions =
                    iterateThroughWorkoutSessionCursorAndAddStoredWorkoutSessions(
                                                        sqLiteDatabase,
                                                        resultCursor);

        resultCursor.close();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();


        return listOfStoredWorkSessions;

    }


    /**
     * Makes a query that queries the table WORKOUT_SESSION_TABLE_NAME for
     * all workout session records (in descending order according to start date time).
     * This method then returns a cursor.
     *
     * @param sqLiteDatabase the database handle with read access permission.Part of
     *                       a transaction
     *
     * @return a cursor of the SQLiteDatabase query
     * */
    private static Cursor queryAllWorkoutSessionRecordsAndGetCursor(SQLiteDatabase sqLiteDatabase)
    {


        String[] columns = new String[]{
                WORKOUT_SESSION_COLUMN_SESSION_ID,
                WORKOUT_SESSION_COLUMN_ACTIVITY_TYPE,
                WORKOUT_SESSION_COLUMN_START_DATE_TIME,
                WORKOUT_SESSION_COLUMN_NUMBER_OF_STEPS_TAKEN,
                WORKOUT_SESSION_COLUMN_DURATION_IN_SECONDS
        };

        final String NO_SELECTION = null;
        final String[] NO_SELECTION_ARGS = null;

        final String NO_GROUP_BY_CLAUSE = null;
        final String NO_HAVING_CLAUSE = null;

        String orderByClause = WORKOUT_SESSION_COLUMN_SESSION_ID + " DESC";

        return sqLiteDatabase.query(WORKOUT_SESSION_TABLE_NAME,
                columns, NO_SELECTION, NO_SELECTION_ARGS,
                NO_GROUP_BY_CLAUSE, NO_HAVING_CLAUSE, orderByClause);

    }


    /**
     * Iterate through a cursor that queries the WORKOUT_SESSION_TABLE_NAME table,
     * and convert the resulting that into a list of workout sessions
     *
     * Note: This method will make further queries to access related data from other tables.
     *
     * @param sqLiteDatabase database handle with read permission. Part of a transaction.
     *
     * @param resultCursor a cursor pointing to the results from a query of the
     *              WORKOUT_SESSION_TABLE_NAME table, with read-access
     *
     * */
    private static List<IStoredWorkoutSession> iterateThroughWorkoutSessionCursorAndAddStoredWorkoutSessions(
                                                        SQLiteDatabase sqLiteDatabase,
                                                        Cursor resultCursor)
    {
        List<IStoredWorkoutSession> listOfStoredWorkSessions = new ArrayList<>();

        while (resultCursor.moveToNext())
        {

            IStoredWorkoutSession storedWorkoutSession =
                    convertCursorToStoredWorkoutSession(resultCursor);

            queryListOfWorkoutSessionImage(sqLiteDatabase, storedWorkoutSession);
            queryListOfWorkoutSessionGPSLocation(sqLiteDatabase, storedWorkoutSession);


            storedWorkoutSession.calculateCaloriesConsumed();
            storedWorkoutSession.calculateDistanceTravelled();

            listOfStoredWorkSessions.add(storedWorkoutSession);
        }

        return listOfStoredWorkSessions;
    }






    /**
     * Reads through the data pointed by the cursor and returns a corresponding
     * IStoredWorkoutSession instance containing the data retrieved
     *
     * Note: This method does not move the position of the cursor. It merely reads
     * through the data pointed by the cursor's current position
     *
     * @param resultCursor the cursor of the result from a query of the
     *                     WORKOUT_SESSION_TABLE_NAME table
     *
     * @return a corresponding IStoredWorkoutSession instance based on the data retrieved
     * from the record pointed by the cursor
     * */
    private static IStoredWorkoutSession convertCursorToStoredWorkoutSession(Cursor resultCursor)
    {
        IStoredWorkoutSession storedWorkoutSession = new StoredWorkoutSession();


        setWorkoutSessionIdFromCursor(resultCursor, storedWorkoutSession);
        setWorkoutActivityTypeFromCursor(resultCursor, storedWorkoutSession);
        setStartDateTimeFromCursor(resultCursor, storedWorkoutSession);

        setNumberOfStepsTakenFromCursor(resultCursor, storedWorkoutSession);
        setDurationInSecondsFromCursor(resultCursor, storedWorkoutSession);


        return storedWorkoutSession;
    }



    /**
     * Sets the workout session id of the workout session based on the record currently
     * pointed by the cursor. This method does not move the position of the cursor
     *
     * @param resultCursor the cursor of the result from a query of the
     *                      WORKOUT_SESSION_TABLE_NAME table
     *
     * @param storedWorkoutSession This is the return value of the method. When this method
     *                             finishes executing, this argument will contain the workout
     *                             session id from the record pointed by the cursor. Cannot be
     *                             null.
     * */
    private static void setWorkoutSessionIdFromCursor(Cursor resultCursor,
                                              IStoredWorkoutSession storedWorkoutSession)
    {
        long sessionId = resultCursor.getLong(resultCursor.getColumnIndexOrThrow(
                WORKOUT_SESSION_COLUMN_SESSION_ID));

        storedWorkoutSession.setWorkoutSessionId(sessionId);
    }



    /**
     * Sets the workout activity type of the workout session based on the record currently
     * pointed by the cursor. This method does not move the position of the cursor
     *
     * @param resultCursor the cursor of the result from a query of the
     *                      WORKOUT_SESSION_TABLE_NAME table
     *
     * @param storedWorkoutSession This is the return value of the method. When this method
     *                             finishes executing, this argument will contain the workout
     *                             activity type from the record pointed by the cursor. Cannot be
     *                             null.
     * */
    private static void setWorkoutActivityTypeFromCursor(Cursor resultCursor,
                                                     IStoredWorkoutSession storedWorkoutSession)
    {
        String activityTypeString = resultCursor.getString(resultCursor.getColumnIndexOrThrow(
                WORKOUT_SESSION_COLUMN_ACTIVITY_TYPE));


        switch (activityTypeString) {
            case "WALKING":
                storedWorkoutSession.setActivityType(IWorkoutSession.ActivityType.WALKING);
                break;

            case "RUNNING":
                storedWorkoutSession.setActivityType(IWorkoutSession.ActivityType.RUNNING);
                break;
        }

    }



    /**
     * Sets the number of steps of the workout session based on the record currently
     * pointed by the cursor. This method does not move the position of the cursor
     *
     * @param resultCursor the cursor of the result from a query of the
     *                      WORKOUT_SESSION_TABLE_NAME table
     *
     * @param storedWorkoutSession This is the return value of the method. When this method
     *                             finishes executing, this argument will contain the number of
     *                            steps from the record pointed by the cursor. Cannot be
     *                             null.
     * */
    private static void setNumberOfStepsTakenFromCursor(Cursor resultCursor,
                                                    IStoredWorkoutSession storedWorkoutSession)
    {
        int numberOfSteps = resultCursor.getInt(resultCursor.getColumnIndexOrThrow(
                                            WORKOUT_SESSION_COLUMN_NUMBER_OF_STEPS_TAKEN));

        storedWorkoutSession.setNumberOfStepsTaken(numberOfSteps);
    }



    /**
     * Sets the workout duration of the workout session based on the record currently
     * pointed by the cursor. This method does not move the position of the cursor
     *
     * @param resultCursor the cursor of the result from a query of the
     *                      WORKOUT_SESSION_TABLE_NAME table
     *
     * @param storedWorkoutSession This is the return value of the method. When this method
     *                             finishes executing, this argument will contain the workout
     *                            duration from the record pointed by the cursor. Cannot be
     *                             null.
     * */
    private static void setDurationInSecondsFromCursor(Cursor resultCursor,
                                                   IStoredWorkoutSession storedWorkoutSession)
    {
        int durationInSeconds = resultCursor.getInt(
                resultCursor.getColumnIndexOrThrow(WORKOUT_SESSION_COLUMN_DURATION_IN_SECONDS));

        storedWorkoutSession.setDurationInSeconds(durationInSeconds);
    }



    /**
     * Sets the start date time of the workout session based on the record currently
     * pointed by the cursor. This method does not move the position of the cursor
     *
     * @param resultCursor the cursor of the result from a query of the
     *                      WORKOUT_SESSION_TABLE_NAME table
     *
     * @param storedWorkoutSession This is the return value of the method. When this method
     *                             finishes executing, this argument will contain the workout
     *                            start date time from the record pointed by the cursor. Cannot be
     *                             null.
     * */
    private static void setStartDateTimeFromCursor(Cursor resultCursor,
                                            IStoredWorkoutSession storedWorkoutSession)
    {
        long startDateTimeEpochSecond = resultCursor.getLong(
                resultCursor.getColumnIndexOrThrow(WORKOUT_SESSION_COLUMN_START_DATE_TIME));


        final int ZERO_NANO_SECONDS = 0;

        LocalDateTime startDateTime = LocalDateTime.ofEpochSecond(startDateTimeEpochSecond,
                                        ZERO_NANO_SECONDS, ZoneOffset.UTC);

        storedWorkoutSession.setStartDateTime(startDateTime);
    }



    /**
     * Queries the database for list of images associated with the stored workout session
     *
     * @param sqLiteDatabase the database handle with read permission. Part of a transaction
     * @param storedWorkoutSession the stored workout session instance to store the list of
     *                             images. This is the return value of the method.
     *
     *                              Note: This argument must already contain the workout
     *                             session id
     *
     * */
    private static void queryListOfWorkoutSessionImage(SQLiteDatabase sqLiteDatabase,
                                                IStoredWorkoutSession storedWorkoutSession)
    {

        String [] columns = new String[]{WORKOUT_SESSION_IMAGE_COLUMN_IMAGE_PATH};

        String selectionClause = WORKOUT_SESSION_COLUMN_SESSION_ID + " = ?";

        long sessionId = storedWorkoutSession.getWorkoutSessionId();

        String sessionIdString = Long.toString(sessionId);
        String [] selectionArgs = new String[]{sessionIdString};

        final String NO_GROUP_BY_CLAUSE = null;
        final String NO_HAVING_CLAUSE = null;

        String orderByClause = WORKOUT_SESSION_IMAGE_COLUMN_IMAGE_PATH_INDEX + " ASC";

        Cursor resultCursor = sqLiteDatabase.query(WORKOUT_SESSION_IMAGE_TABLE_NAME,
                            columns, selectionClause, selectionArgs,
                            NO_GROUP_BY_CLAUSE, NO_HAVING_CLAUSE, orderByClause);

        iterateThroughWorkoutSessionImageCursorAndAddListOfImages(resultCursor,storedWorkoutSession );
    }



    /**
     * Iterate through a cursor that queries the WORKOUT_SESSION_IMAGE_TABLE_NAME table,
     * and convert the resulting that into a list of image paths.
     *
     * Each of these image paths will be added to the storedWorkoutSession argument
     *
     * Note: Expects each record pointed by the cursor to contain the following attributes:
     *      (a) WORKOUT_SESSION_GPS_LOCATION_COLUMN_LATITUDE
     *      (b) WORKOUT_SESSION_GPS_LOCATION_COLUMN_LONGITUDE
     *
     *
     * @param resultCursor a cursor pointing to the results from a query of the
     *              WORKOUT_SESSION_GPS_LOCATION table, with read-access
     *
     * @param storedWorkoutSession a instance of storedWorkoutSession to stored the results
     *                             from the cursor. Cannot be null
     * */
    private static void iterateThroughWorkoutSessionImageCursorAndAddListOfImages(
                                                    Cursor resultCursor,
                                                    IStoredWorkoutSession storedWorkoutSession)
    {
        String currentImagePath;

        while(resultCursor.moveToNext())
        {
            currentImagePath = resultCursor.getString(
                    resultCursor.getColumnIndexOrThrow(WORKOUT_SESSION_IMAGE_COLUMN_IMAGE_PATH));

            storedWorkoutSession.addImageTaken(currentImagePath);
        }

        resultCursor.close();
    }



    /**
     * Queries the database for list of GPSLocations (coordinates) associated with the
     * stored workout session
     *
     * @param sqLiteDatabase the database handle with read permission. Part of a transaction
     * @param storedWorkoutSession the stored workout session instance to store the list of
     *                             GPSLocations. This is the return value of the method.
     *
     *                              Note: This argument must already contain the workout
     *                             session id
     *
     * */
    private static void queryListOfWorkoutSessionGPSLocation(SQLiteDatabase sqLiteDatabase,
                                                IStoredWorkoutSession storedWorkoutSession )
    {
        long sessionId = storedWorkoutSession.getWorkoutSessionId();


        String [] columns = new String[]{
                                        WORKOUT_SESSION_GPS_LOCATION_COLUMN_IS_LOCATION_NULL,
                                        WORKOUT_SESSION_GPS_LOCATION_COLUMN_LATITUDE,
                                        WORKOUT_SESSION_GPS_LOCATION_COLUMN_LONGITUDE};


        String selectionClause = WORKOUT_SESSION_COLUMN_SESSION_ID + " = ?";

        String sessionIdString = Long.toString(sessionId);
        String [] selectionArgs = new String[]{sessionIdString};


        final String NO_GROUP_BY_CLAUSE = null;
        final String NO_HAVING_CLAUSE = null;

        String orderByClause = WORKOUT_SESSION_GPS_LOCATION_COLUMN_LOCATION_INDEX + " ASC";

        Cursor cursor = sqLiteDatabase.query(WORKOUT_SESSION_GPS_LOCATION_TABLE_NAME,
                                columns, selectionClause, selectionArgs, NO_GROUP_BY_CLAUSE,
                                NO_HAVING_CLAUSE, orderByClause);

        iterateThroughWorkoutSessionGPSLocationCursorAndAddListOfGPSLocations(cursor,
                                                            storedWorkoutSession);

        cursor.close();

    }


    /**
     * Iterate through a cursor that queries the WORKOUT_SESSION_GPS_LOCATION table,
     * and convert the resulting that into IGPSLocation instances.
     *
     * Each of these IGPSLocation instances will be added to the storedWorkoutSession argument
     *
     * Note: Each record pointed by the cursor may contain the following attributes:
     *      (a) WORKOUT_SESSION_GPS_LOCATION_COLUMN_LATITUDE
     *      (b) WORKOUT_SESSION_GPS_LOCATION_COLUMN_LONGITUDE
     *
     * The record may not contain these two attributes if these two values are null, which
     * is an acceptable value to represent a pause during the workout session
     *
     * @param resultCursor a cursor pointing to the results from a query of the
     *              WORKOUT_SESSION_GPS_LOCATION table, with read-access
     *
     * @param storedWorkoutSession a instance of storedWorkoutSession to stored the results
     *                             from the cursor. Cannot be null
     * */
    private static void iterateThroughWorkoutSessionGPSLocationCursorAndAddListOfGPSLocations(
                                            Cursor resultCursor,
                                            IStoredWorkoutSession storedWorkoutSession)
    {
        final GPSLocation EMPTY_MARKER = null;

        final long TRUE = 1;

        while(resultCursor.moveToNext())
        {

            //check if latitude and longitude is null
            //it is acceptable to have null latitude and longitude to represent
            // a pause during the workout session
            long isLocationNull = resultCursor.getLong(
                        resultCursor.getColumnIndexOrThrow(
                                WORKOUT_SESSION_GPS_LOCATION_COLUMN_IS_LOCATION_NULL));

            if (isLocationNull == TRUE)
            {
                double currentLatitude = resultCursor.getDouble(
                        resultCursor.getColumnIndexOrThrow(
                                WORKOUT_SESSION_GPS_LOCATION_COLUMN_LATITUDE));

                double currentLongitude = resultCursor.getDouble(
                        resultCursor.getColumnIndexOrThrow(
                                WORKOUT_SESSION_GPS_LOCATION_COLUMN_LONGITUDE));

                GPSLocation currentGPSLocation = new GPSLocation(currentLatitude, currentLongitude);

                storedWorkoutSession.addGPSLocation(currentGPSLocation);
            }
            else
            {

                storedWorkoutSession.addGPSLocation(EMPTY_MARKER);
            }
        }

    }


    /**
     * Delete the workout session record based on the id of the workout session record
     *
     * @param workoutSessionId the id of the workout session record
     *
     * @throws RecordNotFoundException if the workout session record to be deleted does
     * not exist
     * */
    public void deleteWorkoutSessionRecord(long workoutSessionId) throws RecordNotFoundException
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String whereClause = WORKOUT_SESSION_COLUMN_SESSION_ID + " = ?";


        String workoutSessionIdString = String.valueOf(workoutSessionId);
        String [] whereArgs = new String [] { workoutSessionIdString};


        int numOfRowsDeleted = sqLiteDatabase.delete(WORKOUT_SESSION_TABLE_NAME,
                                                    whereClause, whereArgs);

        if (numOfRowsDeleted != 1)
        {
            throw new RecordNotFoundException("WorkoutSessionRecord to be deleted not found");
        }

    }

    /**
     * Delete all workout session records
     * */
    public void deleteAllWorkoutSessionRecords()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        final String NO_WHERE_CLAUSE = null;

        final String [] NO_WHERE_ARGS = null;

        sqLiteDatabase.delete(WORKOUT_SESSION_TABLE_NAME, NO_WHERE_CLAUSE, NO_WHERE_ARGS);
    }


}

