package com.murdoch.fitnessapp.controllers.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.murdoch.fitnessapp.R;
import com.murdoch.fitnessapp.callbackinterfaces.IFragmentReadyListener;
import com.murdoch.fitnessapp.models.implementations.WorkoutSession;

import java.util.Locale;


/**
 * A fragment that displays information regarding a workout
 * in the middle of a workout session
 *
 * Note: The calling activity must implement IFragmentReadyListener interface
 *
 *
 * Use the {@link DuringWorkoutInfoFragment#newInstance} factory method to
 *  *  * create an instance of this fragment.
 *
 * */
public class DuringWorkoutInfoFragment extends Fragment
{
    private IFragmentReadyListener fragmentReadyListener;

    private TextView distanceTravelledTextView;
    private TextView caloriesConsumedTextView;
    private TextView workoutDurationTextView;
    private TextView numberOfStepsTakenTextView;


    public DuringWorkoutInfoFragment()
    {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment instead of the constructor
     *
     * @return A new instance of fragment DuringWorkoutInfoFragment.
     */
    public static DuringWorkoutInfoFragment newInstance()
    {
       return new DuringWorkoutInfoFragment();

    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_during_workout_info,
                container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);
        this.initializeAllViews();

        this.fragmentReadyListener.onFragmentReady(this);
    }



    /**
     * Initialize the various views in the fragment
     * */
    private void initializeAllViews()
    {
        this.distanceTravelledTextView = getView().findViewById(R.id.distanceTravelledTextView);
        this.caloriesConsumedTextView = getView().findViewById(R.id.caloriesConsumedTextView);
        this.workoutDurationTextView = getView().findViewById(R.id.workoutDurationTextView);
        this.numberOfStepsTakenTextView = getView().findViewById(R.id.numberOfStepsTakenTextView);
    }


    /**
     * Throws ClassCastException if context does not implement IFragmentReadyListener interface
     * */
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.fragmentReadyListener = (IFragmentReadyListener) context;
    }


    /**
     *
     * Note: Only call this method when
     * */
    public void displayWorkoutSessionDetails(WorkoutSession workoutSession)
    {
        this.displayWorkoutDuration(workoutSession);

        this.displayDistanceTravelled(workoutSession);

        this.displayCaloriesConsumed(workoutSession);

        this.displayNumberOfSteps(workoutSession);
    }



    /**
     * Displays the workout duration (how long the user has engaged in the
     * workout session)
     *
     * @param workoutSession workout session of the user is engaged in
     * */
    private void displayWorkoutDuration(WorkoutSession workoutSession)
    {
        String workoutDurationString = convertSecondsToHourMinuteSeconds(
                                        workoutSession.getDurationInSeconds());

        this.workoutDurationTextView.setText(workoutDurationString);
    }



    /**
     * Displays the distance that the user has travelled
     *
     * @param workoutSession workout session of the user is engaged in
     * */
    private void displayDistanceTravelled(WorkoutSession workoutSession)
    {
        String distanceTravelledString = String.format(Locale.ENGLISH,
                "%.2f km", workoutSession.getDistanceTravelledInKilometers());

        distanceTravelledTextView.setText(distanceTravelledString);
    }


    /**
     * Displays the amount of calories that the user has consumed
     *
     * @param workoutSession workout session of the user is engaged in
     * */
    private void displayCaloriesConsumed(WorkoutSession workoutSession)
    {
        String caloriesConsumedString = String.format(Locale.ENGLISH, "%.0f",
                                        workoutSession.getCaloriesConsumed());

        this.caloriesConsumedTextView.setText(caloriesConsumedString);
    }


    /**
     * Displays the number of steps that the user has taken
     *
     * @param workoutSession workout session of the user is engaged in
     * */
    private void displayNumberOfSteps(WorkoutSession workoutSession)
    {
        String numberOfStepsString = String.valueOf(workoutSession.getNumberOfStepsTaken());

        this.numberOfStepsTakenTextView.setText(numberOfStepsString);
    }



    /**
     * Convert duration in seconds to a string that is in the format of "HH:MM:SS"
     * (hour:minute:seconds)
     *
     * @param durationInSeconds duration in seconds
     *
     * @return a string in format of "HH:MM:SS"
     * */
    private String convertSecondsToHourMinuteSeconds(int durationInSeconds)
    {
        int numberOfHours = durationInSeconds/3600;

        int numberOfRemainingSeconds = durationInSeconds - numberOfHours* 3600;

        int numberOfMinutes = numberOfRemainingSeconds/60;

        numberOfRemainingSeconds = numberOfRemainingSeconds - numberOfMinutes * 60;

        int numberOfSeconds = numberOfRemainingSeconds;

        return String.format(Locale.ENGLISH, "%02d:%02d:%02d", numberOfHours,
                numberOfMinutes, numberOfSeconds);
    }

}