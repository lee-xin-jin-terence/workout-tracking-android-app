package com.murdoch.fitnessapp.controllers.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.murdoch.fitnessapp.R;
import com.murdoch.fitnessapp.models.implementations.WorkoutSession;
import com.murdoch.fitnessapp.models.interfaces.IWorkoutSession;

import java.time.LocalDateTime;

/**
 * UI controller for the activity_start_workout.xml layout
 *
 * Provides an activity that allows user to select the workout
 * activity type, and also to start the workout session
 *
 * */
public class StartWorkoutActivity extends AppCompatActivity
{
    private final WorkoutSession workoutSession = new WorkoutSession();

    private TextView instructionTextView;
    private FrameLayout startWorkoutButtonAndTextViewContainer;



    /**
     * Create an intent to start the StartWorkoutActivity
     *
     * @param packageContext context of the application package
     *
     * @return an intent to start this application
     * */
    public static Intent createIntent(Context packageContext)
    {
        return new Intent(packageContext, StartWorkoutActivity.class);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);

        initializeAllViews();
    }


    /**
     * Initialize all the various views in this activity
     * */
    private void initializeAllViews()
    {
        initializeInstructionTextView();
        initializeStartWorkoutButtonAndContainer();
        initializeSelectWorkoutTypeSpinner();
    }


    /**
     * Initialize the start workout button and container
     * */
    private void initializeInstructionTextView()
    {
        this.instructionTextView = findViewById(R.id.instructionTextView);
    }


    /**
     * Initialize the start workout button and container
     * */
    private void initializeStartWorkoutButtonAndContainer()
    {
        this.startWorkoutButtonAndTextViewContainer =
                findViewById(R.id.startWorkoutButtonAndTextViewContainer);

        ImageButton startWorkoutImageButton = findViewById(R.id.startWorkoutImageButton);

        startWorkoutImageButton.setOnClickListener(view -> {

            this.workoutSession.setStartDateTime(LocalDateTime.now());

            Intent intent = DuringWorkoutActivity.createIntent(getApplicationContext(),
                                                                this.workoutSession);
            StartWorkoutActivity.this.startActivity(intent);
        });
    }


    /**
     * Initialize the spinner that allows user to select the activity type
     * */
    private void initializeSelectWorkoutTypeSpinner()
    {
        Spinner selectWorkoutTypeSpinner = findViewById(R.id.selectWorkoutTypeSpinner);

        ArrayAdapter<CharSequence> workoutTypesArrayAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.activity_start_workout_activity_types_array,
                        R.layout.custom_spinner_text_view);

        workoutTypesArrayAdapter.setDropDownViewResource(
                R.layout.custom_spinner_checked_text_view);

        selectWorkoutTypeSpinner.setAdapter(workoutTypesArrayAdapter);

        selectWorkoutTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int selectedSpinnerOptionIndex, long rowId)
            {
                setActivityType(selectedSpinnerOptionIndex);
                setInstructionText(selectedSpinnerOptionIndex);
                hideOrShowStartButtonContainer(selectedSpinnerOptionIndex);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //do nothing
                //abstract method that needs to be implemented
            }
        });
    }


    /**
     * Sets the text of the instruction textview based on the option on the spinner
     * that the user has selected
     *
     * @param selectedSpinnerOptionIndex the index of the spinner option
     *                                   Expected Values: '0', '1' or '2'
     *
     *                                  (a) '0' means the user has not selected anything
     *                                  (b) '1' means the user selected 'WALKING'
     *                                  (b) '2' means the user selected 'RUNNING'
     * */
    private void setInstructionText(int selectedSpinnerOptionIndex)
    {
        final int NO_ACTIVITY_TYPE_SELECTED = 0;

        if (selectedSpinnerOptionIndex == NO_ACTIVITY_TYPE_SELECTED)
        {
            this.instructionTextView.setText(
                    R.string.activity_start_workout_select_workout_activity_type_instruction_text);
        }
        else
        {
            this.instructionTextView.setText(R.string.activity_start_workout_click_on_start_to_begin_instruction_text);
        }
    }


    /**
     * Sets the activity type of this.workoutSession based on the option on the spinner
     * that the user has selected
     *
     * @param selectedSpinnerOptionIndex the index of the spinner option
     *                                   Expected Values: '0', '1' or '2'
     *
     *                                  (a) '0' means the user has not selected anything
     *                                  (b) '1' means the user selected 'WALKING'
     *                                  (b) '2' means the user selected 'RUNNING'
     *
     * If the user selected '0', nothing happens
     * If the user selected '1' or '2', then the activity type in this.workoutSession
     * will be set accordingly
     * */
    private void setActivityType(int selectedSpinnerOptionIndex)
    {
        final int WALKING = 1;
        final int RUNNING = 2;

        if (selectedSpinnerOptionIndex == WALKING)
        {
            this.workoutSession.setActivityType(IWorkoutSession.ActivityType.WALKING);
        }
        else if (selectedSpinnerOptionIndex == RUNNING)
        {
            this.workoutSession.setActivityType(IWorkoutSession.ActivityType.RUNNING);
        }

    }



    /**
     * Hide or show the start button container based on the option on the spinner
     * that the user has selected
     *
     * @param selectedSpinnerOptionIndex the index of the spinner option
     *                                   Expected Values: '0', '1' or '2'
     *
     *                                  (a) '0' means the user has not selected anything
     *                                  (b) '1' means the user selected 'WALKING'
     *                                  (b) '2' means the user selected 'RUNNING'
     *
     * If the user selected '0', the start button container is hidden (VIEW.GONE)
     * If the user selected '1' or '2', then the start button container will become
     *  visible (View.VISIBLE)
     * */
    private void hideOrShowStartButtonContainer(int selectedSpinnerOptionIndex)
    {
        final int NO_ACTIVITY_TYPE_SELECTED = 0;

        if (selectedSpinnerOptionIndex == NO_ACTIVITY_TYPE_SELECTED)
        {
            this.startWorkoutButtonAndTextViewContainer.setVisibility(View.GONE);
        }
        else
        {
            this.startWorkoutButtonAndTextViewContainer.setVisibility(View.VISIBLE);
        }
    }


}