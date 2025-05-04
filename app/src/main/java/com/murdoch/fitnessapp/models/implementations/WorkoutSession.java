package com.murdoch.fitnessapp.models.implementations;

import android.os.Parcel;
import android.os.Parcelable;

import com.murdoch.fitnessapp.models.interfaces.IWorkoutSession;
import com.murdoch.fitnessapp.models.interfaces.IGPSLocation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * This class represents a workout session, and stores data such as activity type,
 * start date time, workout duration, number of steps taken, calories consumed
 * and distance travelled
 *
 * */
public class WorkoutSession implements IWorkoutSession, Parcelable
{

    public static final ActivityType ACTIVITY_TYPE_DEFAULT_VALUE = ActivityType.WALKING;
    public static final LocalDateTime START_DATE_TIME_DEFAULT_VALUE = null;
    public static final int DURATION_IN_SECONDS_DEFAULT_VALUE = 0;
    public static final int NUMBER_OF_STEPS_TAKEN_DEFAULT_VALUE = 0;
    public static final int CALORIES_CONSUMED_DEFAULT_VALUE = 0;
    public static final double DISTANCE_TRAVELLED_DEFAULT_VALUE = 0;

    private LocalDateTime startDateTime = START_DATE_TIME_DEFAULT_VALUE;
    private ActivityType activityType = ACTIVITY_TYPE_DEFAULT_VALUE;
    private int durationInSeconds = DURATION_IN_SECONDS_DEFAULT_VALUE;
    private int numberOfStepsTaken = NUMBER_OF_STEPS_TAKEN_DEFAULT_VALUE;
    private double caloriesConsumed = CALORIES_CONSUMED_DEFAULT_VALUE;
    private double distanceTravelledInKilometers = DISTANCE_TRAVELLED_DEFAULT_VALUE;


    private ArrayList<IGPSLocation> listOfGPSLocations = new ArrayList<>();
    private ArrayList<String> listOfImagesTaken = new ArrayList<>();


    /**
     * Initializes an instance of WorkoutSession with the default values
     *
     * @see #ACTIVITY_TYPE_DEFAULT_VALUE
     * @see #START_DATE_TIME_DEFAULT_VALUE
     * @see #DURATION_IN_SECONDS_DEFAULT_VALUE
     * @see #NUMBER_OF_STEPS_TAKEN_DEFAULT_VALUE
     * @see #CALORIES_CONSUMED_DEFAULT_VALUE
     * @see #DISTANCE_TRAVELLED_DEFAULT_VALUE
     * */
    public WorkoutSession()
    {

    }


    /**
     * Sets the activity type of the workout sessions
     *
     * @param activityTypeValue the activity type of the workout session
     *
     * @throws NullPointerException if the argument is null
     * */
    public void setActivityType(ActivityType activityTypeValue)
    {
        if(activityTypeValue == null)
        {
            throw new NullPointerException("Activity Type cannot be null");
        }

        this.activityType = activityTypeValue;
    }


    /**
     * Returns the activity type of the workout session
     *
     * @see #ACTIVITY_TYPE_DEFAULT_VALUE
     * */
    public ActivityType getActivityType()
    {
        return this.activityType;
    }



    /**
     * Sets the start date time of the workout session
     *
     * @param startDateTimeValue the start date time of the workout session
     *
     * @throws NullPointerException if the argument is null
     * */
    @Override
    public void setStartDateTime(LocalDateTime startDateTimeValue)
    {
        if (startDateTimeValue == null)
        {
            throw new NullPointerException("Start Date Time cannot be null");
        }

        this.startDateTime = startDateTimeValue;
    }


    /**
     * Returns the start date time of the workout session
     *
     * @see #START_DATE_TIME_DEFAULT_VALUE
     * */
    @Override
    public LocalDateTime getStartDateTime()
    {
        return this.startDateTime;
    }


    /**
     * Sets the duration of the workout session in seconds
     *
     * @param durationInSecondsValue the duration of the workout in seconds
     *
     * @throws IllegalArgumentException if the argument is a negative number
     * */
    @Override
    public void setDurationInSeconds(int durationInSecondsValue)
    {
        if (durationInSecondsValue < 0)
        {
            throw new IllegalArgumentException("Duration cannot be negative value");
        }

        this.durationInSeconds = durationInSecondsValue;
    }


    /**
     * Add duration of workout session to the current workout duration
     *
     * @param additionalDurationInSeconds the additional duration in seconds to
     *                                    add to the current workout duration
     *
     * @throws IllegalArgumentException if the argument is a negative number
     * */
    @Override
    public void addDurationInSeconds(int additionalDurationInSeconds)
    {
        if (additionalDurationInSeconds < 0)
        {
            throw new IllegalArgumentException("Additional Duration cannot be " +
                    "negative value");
        }

        this.durationInSeconds += additionalDurationInSeconds;
    }


    /**
     * Returns the duration of the workout in seconds
     *
     * @see #DURATION_IN_SECONDS_DEFAULT_VALUE
     * */
    @Override
    public int getDurationInSeconds()
    {
        return this.durationInSeconds;
    }



    /**
     * Sets the number of steps taken during the workout session
     *
     * @param numberOfStepsValue the number of steps taken during the workout
     *                           session
     *
     * @throws IllegalArgumentException if the argument is a negative number
     * */
    @Override
    public void setNumberOfStepsTaken(int numberOfStepsValue)
    {
        if (numberOfStepsValue < 0)
        {
            throw new IllegalArgumentException("Number of steps cannot be negative");
        }

        this.numberOfStepsTaken = numberOfStepsValue;
    }



    /**
     * Add number of steps taken to the current number of steps taken
     *
     * @param additionalNumberOfStepsTaken the additional number of steps taken
     *
     * @throws IllegalArgumentException if the argument is a negative number
     * */
    @Override
    public void addNumberOfStepsTaken(int additionalNumberOfStepsTaken)
    {
        if (additionalNumberOfStepsTaken < 0)
        {
            throw new IllegalArgumentException("Number of steps cannot be negative");
        }

        this.numberOfStepsTaken += additionalNumberOfStepsTaken;
    }



    /**
     * Returns the number of steps taken during the workout session
     *
     * @see #NUMBER_OF_STEPS_TAKEN_DEFAULT_VALUE
     * */
    @Override
    public int getNumberOfStepsTaken()
    {
        return this.numberOfStepsTaken;
    }



    /**
     * Calculate the calories that the user consumed based on activity type and duration of
     * workout session. The amount of calories consumed can then be retrieved via the method
     * getCaloriesConsumed()
     *
     * @see #setActivityType(ActivityType) 
     * @see #setDurationInSeconds(int)
     * @see #getCaloriesConsumed()
     * */
    @Override
    public void calculateCaloriesConsumed()
    {
        double METValue = getMETValueFromActivityType();

        final double AVERAGE_HUMAN_WEIGHT_IN_KILOGRAMS = 62;

        final double OXYGEN_CONSUMED_PER_KILOGRAM_WEIGHT = 3.5;

        final double QUOTIENT_CONSTANT = 200;

        double durationInMinutes = this.durationInSeconds/60.0;

        this.caloriesConsumed = (durationInMinutes * METValue *
                    OXYGEN_CONSUMED_PER_KILOGRAM_WEIGHT * AVERAGE_HUMAN_WEIGHT_IN_KILOGRAMS)/
                    QUOTIENT_CONSTANT;

    }


    /**
     * Return the MET value from the activity type of the workout session.
     *
     * Note: MET refers to 'Metabolic Equivalents'. It refers to the metabolic
     * rate of a particular activity
     * */
    private double getMETValueFromActivityType()
    {
        double METValue;

        if (this.activityType == ActivityType.WALKING)
        {
            METValue = 3.5;
        }
        else
        {
            //for ActivityType.RUNNING
            METValue = 7;
        }

        return METValue;
    }


    /**
     * Returns the number of calories consumed
     *
     * Note: The amount of calories consumed cannot be directly set. It has
     * to be calculated through the method calculateCaloriesConsumed()
     *
     * @see #calculateCaloriesConsumed()
     * @see #CALORIES_CONSUMED_DEFAULT_VALUE
     * */
    @Override
    public double getCaloriesConsumed()
    {
        return this.caloriesConsumed;
    }


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
    @Override
    public void addGPSLocation(IGPSLocation gpsLocation)
    {

        this.listOfGPSLocations.add(gpsLocation);
    }


    /**
     * Returns an unmodifiable list of GPSLocations (coordinates)
     * added in the order from the method addGPSLocation(IGPSLocation)
     * 
     * @see #addGPSLocation(IGPSLocation)
     * */
    @Override
    public List<IGPSLocation> getListOfGPSLocations()
    {
        return Collections.unmodifiableList(this.listOfGPSLocations);
    }



    /**
     * Empties the list of GPSLocations
     * */
    @Override
    public void clearListOfGPSLocations()
    {
        this.listOfGPSLocations.clear();
    }


    /**
     * Calculate the distance travelled by the user based on the list of GPSLocations
     * added by the user.
     * The distance calculated can then be retrieved via the method
     * getDistanceTravelledInKilometers()
     * 
     * @see #addGPSLocation(IGPSLocation)
     * @see #getDistanceTravelledInKilometers()
     * */
    @Override
    public void calculateDistanceTravelled()
    {
        if (this.listOfGPSLocations.size() <= 1)
        {
            this.distanceTravelledInKilometers = 0;
            return;
        }

        double totalDistanceInKilometers = 0;

        
        for (int index = 0; index < this.listOfGPSLocations.size() - 1; index++)
        {
            IGPSLocation currentPoint = this.listOfGPSLocations.get(index);
            IGPSLocation nextPoint = this.listOfGPSLocations.get(index + 1);

            if (currentPoint != null && nextPoint != null)
            {
                double distanceBetweenTwoPointsInKilometers =
                        calculateAndGetDistanceBetweenTwoPointsInKilometers(currentPoint,
                                                                            nextPoint);

                totalDistanceInKilometers += distanceBetweenTwoPointsInKilometers;
            }
        }

        this.distanceTravelledInKilometers = totalDistanceInKilometers;
    }


    /**
     * Calculate and return the distance between two points (GPSLocation). The return value
     * will be distance represented in kilometers
     *
     * @param firstPoint the first point of interest
     * @param secondPoint the second point of interest
     *
     * The distance will be calculated using the Haversine formula, where the Earth's radius
     * used is 6371 km.
     * */
    private static double calculateAndGetDistanceBetweenTwoPointsInKilometers(IGPSLocation firstPoint,
                                                                              IGPSLocation secondPoint)
    {
        final int EARTH_RADIUS_IN_KILOMETERS = 6371;

        double firstPointLatitude = firstPoint.getLatitude();
        double firstPointLongitude = firstPoint.getLongitude();

        double secondPointLatitude = secondPoint.getLatitude();
        double secondPointLongitude = secondPoint.getLongitude();

        double firstPointLatitudeRadius = firstPointLatitude * Math.PI / 180;
        double secondPointLatitudeRadius = secondPointLatitude * Math.PI / 180;

        double latitudeDifference = (secondPointLatitude - firstPointLatitude) * Math.PI / 180;
        double longitudeDifference = (secondPointLongitude - firstPointLongitude) * Math.PI / 180;


        double chordLength = Math.sin(latitudeDifference / 2) * Math.sin(latitudeDifference / 2) +
                Math.cos(firstPointLatitudeRadius) * Math.cos(secondPointLatitudeRadius) *
                        Math.sin(longitudeDifference / 2) * Math.sin(longitudeDifference / 2);


        double angularDistance = 2 * Math.atan2(Math.sqrt(chordLength),
                                                Math.sqrt(1 - chordLength));


        double distance = (EARTH_RADIUS_IN_KILOMETERS * angularDistance);


        return distance;
    }


    /**
     * Returns the distance travelled in kilometers
     *
     * Note: The distance travelled cannot be directly set. It has
     * to be calculated through the method calculateDistanceTravelled()
     *
     * @see #calculateDistanceTravelled()
     * @see #DISTANCE_TRAVELLED_DEFAULT_VALUE
     * */
    @Override
    public double getDistanceTravelledInKilometers()
    {
        return this.distanceTravelledInKilometers;
    }


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
    @Override
    public void addImageTaken(String imagePath)
    {
        if (imagePath == null)
        {
            throw new NullPointerException("Image path cannot be null");
        }

        imagePath = imagePath.trim();

        if (imagePath.equals(""))
        {
            throw new IllegalArgumentException("Image path cannot be empty string");
        }


        this.listOfImagesTaken.add(imagePath);
    }


    /**
     * Returns a list of images taken in the order added through
     * the method addImageTaken
     *
     * @return an unmodifiable list of images taken
     * 
     * @see #addImageTaken(String)
     * */
    @Override
    public List<String> getListOfImagesTaken()
    {
        return Collections.unmodifiableList(this.listOfImagesTaken);
    }


    /**
     * Empties out the list of images taken
     * */
    @Override
    public void clearListOfImagesTaken()
    {
        this.listOfImagesTaken.clear();
    }


    @Override
    public boolean equals(Object otherObject)
    {

        if (otherObject == this)
        {
            return true;
        }

        if (!(otherObject instanceof WorkoutSession))
        {
            return false;
        }


        WorkoutSession otherWorkoutSession = (WorkoutSession) otherObject;

        boolean startDateTimeIsEqual = isLocalDateTimeEqual(this.startDateTime,
                                        otherWorkoutSession.startDateTime);


        return this.activityType == otherWorkoutSession.activityType &&
                startDateTimeIsEqual &&
                this.distanceTravelledInKilometers ==
                        otherWorkoutSession.distanceTravelledInKilometers &&
                this.caloriesConsumed == otherWorkoutSession.caloriesConsumed &&
                this.durationInSeconds == otherWorkoutSession.durationInSeconds &&
               this.numberOfStepsTaken == otherWorkoutSession.numberOfStepsTaken &&
                this.listOfGPSLocations.equals(otherWorkoutSession.listOfGPSLocations) &&
                this.listOfImagesTaken.equals(otherWorkoutSession.listOfImagesTaken);
    }



    private static boolean isLocalDateTimeEqual(LocalDateTime localDateTime1,
                                             LocalDateTime localDateTime2)
    {
        boolean isEqual = false;

        if (localDateTime1 == null && localDateTime1 == null)
        {
            isEqual = true;
        }
        else if (localDateTime1 != null &&
                localDateTime1.equals(localDateTime2))
        {
            isEqual = true;
        }

        return isEqual;
    }


    /**
     * Interface that must be implemented and provided as a public
     * CREATOR field that generates instances of this Parcelable
     * class from a Parcel.
     *
     *  Note: This is part of the Parcelable interface
     * */
    public static final Creator<WorkoutSession> CREATOR = new Creator<WorkoutSession>()
    {
        @Override
        public WorkoutSession createFromParcel(Parcel in)
        {
            return new WorkoutSession(in);
        }

        @Override
        public WorkoutSession[] newArray(int size)
        {
            return new WorkoutSession[size];
        }
    };



    /**
     * Constructor required for the Parcelable interface
     * */
    protected WorkoutSession(Parcel in)
    {

        this.durationInSeconds = in.readInt();
        this.numberOfStepsTaken = in.readInt();
        this.caloriesConsumed = in.readDouble();
        this.distanceTravelledInKilometers = in.readDouble();

        this.listOfImagesTaken = new ArrayList<>();
        this.listOfGPSLocations = new ArrayList<>();

        in.readList(this.listOfImagesTaken, String.class.getClassLoader());
        in.readList(this.listOfGPSLocations, GPSLocation.class.getClassLoader());


        String startDateTimeString = in.readString();
        this.startDateTime = convertStringToStartDateTime(startDateTimeString);


        String activityTypeString = in.readString();
        this.activityType = convertStringToActivityType(activityTypeString);

    }


    /**
     * Converts a string to its corresponding LocalDateTime instance
     * Helper method for the WorkoutSession(Parcel) constructor
     *
     * @param startDateTimeString - start date time represented as a string
     *                            Expects either
     *                            (a) null reference
     *                            (b) string representing a local date time,
     *                            which is obtained from LocalDateTime.toString()
     *
     * @return null if argument is null, or a LocalDateTime instance if argument
     *          is not null
     *
     * @see #WorkoutSession(Parcel)
     * */
    private LocalDateTime convertStringToStartDateTime(String startDateTimeString)
    {
        if (startDateTimeString == null)
        {
            return null;
        }
        else
        {
            return LocalDateTime.parse(startDateTimeString);
        }
    }


    /**
     * Converts a string to its corresponding activity type
     * Helper method for the WorkoutSession(Parcel) constructor
     * 
     * @param activityTypeString - activity type represented as a string
     *                           Expecting only two possible values:
     *                              (a) "WALKING"
     *                              (b) "RUNNING"
     *
     * @return corresponding ActivityType of either ActivityType.WALKING or
     *          ActivityType.RUNNING
     *
     * @see #WorkoutSession(Parcel)
     * */
    private ActivityType convertStringToActivityType(String activityTypeString)
    {
        if (activityTypeString.equals("WALKING"))
        {
            return ActivityType.WALKING;
        }
        else
        {
            return ActivityType.RUNNING;
        }
    }


    /**
     * Flatten this object in to a Parcel.
     *
     * Note: This is part of the Parcelable interface
     * */
    @Override
    public void writeToParcel(Parcel parcel, int i)
    {

        parcel.writeInt(this.durationInSeconds);
        parcel.writeInt(this.numberOfStepsTaken);
        parcel.writeDouble(this.caloriesConsumed);
        parcel.writeDouble(this.distanceTravelledInKilometers);
        parcel.writeList(this.listOfImagesTaken);
        parcel.writeList(this.listOfGPSLocations);

        if (this.startDateTime == null)
        {
            parcel.writeString(null);
        }
        else
        {
            parcel.writeString(this.startDateTime.toString());
        }


        if (activityType == ActivityType.WALKING)
        {
            parcel.writeString("WALKING");
        }
        else
        {
            parcel.writeString("RUNNING");
        }
    }



    /**
     * Describe the kinds of special objects contained in this Parcelable instance's
     * marshaled representation.
     *
     * Note: This is part of the Parcelable interface
     * */
    @Override
    public int describeContents()
    {
        final int DEFAULT_DESCRIPTION = 0;

        return DEFAULT_DESCRIPTION;
    }
}
