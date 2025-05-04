package com.murdoch.fitnessapp.models.implementations;

import android.os.Parcel;
import android.os.Parcelable;

import com.murdoch.fitnessapp.models.interfaces.IStoredWorkoutSession;

import java.util.Objects;


/**
 * A class representing a workout session that has been stored in a database.
 *
 * The difference between this class and the WorkoutSession class is that this
 * class has an additional setter and getter for the ID of the workout session record
 * stored in the database
 *
 * */
public class StoredWorkoutSession extends WorkoutSession
    implements IStoredWorkoutSession, Parcelable
{

    public static final long WORKOUT_SESSION_ID_DEFAULT_VALUE = 0;

    private long workoutSessionId = WORKOUT_SESSION_ID_DEFAULT_VALUE;


    /**
     * Initializes an instance of StoredWorkoutSession with the default values
     *
     * @see #WORKOUT_SESSION_ID_DEFAULT_VALUE
     * */
    public StoredWorkoutSession()
    {
        super();
    }


    /**
     * Sets the workout session Id
     *
     * @param workoutSessionIdValue the workout session Id
     *
     * @throws IllegalArgumentException if the argument is a negative number
     * */
    @Override
    public void setWorkoutSessionId(long workoutSessionIdValue)
    {
        if (workoutSessionIdValue < 0)
        {
            throw new IllegalArgumentException("Fitness Activity Id cannot be negative");
        }

        this.workoutSessionId = workoutSessionIdValue;
    }


    /**
     * Returns the workout session Id
     *
     * @see #WORKOUT_SESSION_ID_DEFAULT_VALUE
     * */
    @Override
    public long getWorkoutSessionId()
    {
        return this.workoutSessionId;
    }

    @Override
    public boolean equals(Object otherObject)
    {
        if (otherObject == this)
        {
            return true;
        }

        if (!(otherObject instanceof StoredWorkoutSession))
        {
            return false;
        }

        return super.equals(otherObject) &&
                this.workoutSessionId == ((StoredWorkoutSession) otherObject).workoutSessionId;
    }



    /**
     * Interface that must be implemented and provided as a public
     * CREATOR field that generates instances of this Parcelable
     * class from a Parcel.
     *
     *  Note: This is part of the Parcelable interface
     * */
    public static final Creator<StoredWorkoutSession> CREATOR = new Creator<StoredWorkoutSession>()
    {
        @Override
        public StoredWorkoutSession createFromParcel(Parcel in)
        {
            return new StoredWorkoutSession(in);
        }

        @Override
        public StoredWorkoutSession[] newArray(int size) {
            return new StoredWorkoutSession[size];
        }
    };


    /**
     * Constructor required for the Parcelable interface
     * */
    protected StoredWorkoutSession(Parcel in)
    {
        super(in);
        this.workoutSessionId = in.readLong();
    }



    /**
     * Flatten this object in to a Parcel.
     *
     * Note: This is part of the Parcelable interface
     * */
    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        super.writeToParcel(parcel, i);
        parcel.writeLong(this.workoutSessionId);
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
