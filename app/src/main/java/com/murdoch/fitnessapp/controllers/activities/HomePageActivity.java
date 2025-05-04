package com.murdoch.fitnessapp.controllers.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.murdoch.fitnessapp.R;
import com.murdoch.fitnessapp.databases.helpers.WorkoutSessionDatabaseHelper;
import com.murdoch.fitnessapp.models.interfaces.IWorkoutSessionSummary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.Locale;


/**
 * UI controller for the activity_home_page.xml layout
 *
 * Provides an activity that allows user view the workout summary for past week,
 * start tracking workout and view past workout records
 *
 * */
public class HomePageActivity extends AppCompatActivity
{

    private ActivityResultLauncher<Intent> viewPastWorkoutActivityIntentLauncher;

    /**
     * Create an intent to start the HomePageActivity. The intent contains the
     * Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK flags
     *
     * @param packageContext context of the application package
     *
     * @return an intent to start this application
     * */
    public static Intent createIntent(Context packageContext)
    {
        Intent intent = new Intent(packageContext, HomePageActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initializeViewPastWorkoutActivityIntentLauncher();
        initializeAllViews();

    }


    /**
     * Initialize the intent launcher that will launch the ViewPastWorkoutActivity,
     * which upon activity result (completion), will update the workout summary
     * in the event that the user deleted workout one or more records
     *
     * Note: This launcher can only be initialized within the onCreate() lifecycle phase,
     * and initializing it outside this phase will cause an error to be thrown
     * */
    private void initializeViewPastWorkoutActivityIntentLauncher()
    {
        this.viewPastWorkoutActivityIntentLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() ==
                                    ViewPastWorkoutActivity.RESULT_HAS_DELETED_WORKOUT_SESSION_RECORDS)
                            {
                                initializeOrUpdateWorkoutSummaryForTheLastSevenDays();
                            }
                        });
    }


    /**
     * Initialize all views on the HomePageActivity
     * */
    private void initializeAllViews()
    {
        initializeOrUpdateWorkoutSummaryForTheLastSevenDays();
        initializeStartWorkoutImageButton();
        initializeViewPastWorkoutRecordsImageButton();
    }


    /**
     * Initializes the workout summary data for the last 7 days
     * */
    private void initializeOrUpdateWorkoutSummaryForTheLastSevenDays()
    {
        WorkoutSessionDatabaseHelper databaseHelper =
                new WorkoutSessionDatabaseHelper(getApplicationContext());

        LocalDate sixDaysAgoDateStartDateInclusive = getSixDaysAgoDateStartDateInclusive();
        LocalDate tomorrowsDateEndDateExclusive = getTomorrowsDateEndDateExclusive();

        //query workout session summary data for the last 7 days including today
        //and excluding tmr
        IWorkoutSessionSummary workoutSessionSummary =
                databaseHelper.queryWorkoutSessionSummary(sixDaysAgoDateStartDateInclusive,
                                                          tomorrowsDateEndDateExclusive);

        this.initializeDistanceTravelledTextView(workoutSessionSummary);
        this.initializeWorkoutDurationTextView(workoutSessionSummary);
        this.initializeNumberOfStepsTextView(workoutSessionSummary);
        this.initializeCaloriesBurntTextView(workoutSessionSummary);

    }

    /**
     * Get the date for six days ago, to represent the start date (inclusive)
     * for the last 7 days
     *
     * @return a LocalDate representing date for six days ago
     * */
    private static LocalDate getSixDaysAgoDateStartDateInclusive()
    {
        LocalDate todayDate = LocalDate.now();

        return todayDate.minusDays(6);
    }


    /**
     * Get the date for tomorrow, to represent the end date (exclusive)
     * for the last 7 days
     *
     * @return a LocalDate representing tomorrows day
     * */
    private static LocalDate getTomorrowsDateEndDateExclusive()
    {
        LocalDate todayDate = LocalDate.now();

        return todayDate.plusDays(1);
    }


    /**
     * Initializes the textview that displays the distance the user travelled for the
     * last 7 days
     *
     * @param workoutSessionSummary workout summary for the last 7 days
     * */
    private void initializeDistanceTravelledTextView(IWorkoutSessionSummary workoutSessionSummary)
    {
        TextView distanceTravelledTextView = findViewById(R.id.distanceTravelledTextView);

        String stringToDisplay = String.format(Locale.ENGLISH,"%.2f km",
                workoutSessionSummary.getTotalDistanceTravelledInKilometers());

        distanceTravelledTextView.setText(stringToDisplay);
    }


    /**
     * Initializes the textview that displays the duration the user exercised for the
     * last 7 days
     *
     * @param workoutSessionSummary workout summary for the last 7 days
     * */
    private void initializeWorkoutDurationTextView(IWorkoutSessionSummary workoutSessionSummary)
    {
        TextView workoutDurationTextView = findViewById(R.id.durationTextView);

        String stringToDisplay = convertSecondsToHourMinuteSeconds(
                                    workoutSessionSummary.getTotalDurationInSeconds());

        workoutDurationTextView.setText(stringToDisplay);
    }


    /**
     * Initializes the textview that displays the number of steps the user has taken for the
     * last 7 days
     *
     * @param workoutSessionSummary workout summary for the last 7 days
     * */
    private void initializeNumberOfStepsTextView(IWorkoutSessionSummary workoutSessionSummary)
    {
        TextView numberOfStepsTakenTextView = findViewById(R.id.numberOfStepsTextView);

        String stringToDisplay = String.format(Locale.ENGLISH,"%d steps",
                            workoutSessionSummary.getTotalNumberOfStepsTaken());

        numberOfStepsTakenTextView.setText(stringToDisplay);

    }


    /**
     * Initializes the textview that displays the amount of calories the user has consumed
     * (burnt) for the last 7 days
     *
     * @param workoutSessionSummary workout summary for the last 7 days
     * */
    private void initializeCaloriesBurntTextView(IWorkoutSessionSummary workoutSessionSummary)
    {
        TextView caloriesBurntTextView = findViewById(R.id.caloriesConsumedTextView);

        String stringToDisplay = String.format(Locale.ENGLISH, "%.0f cal",
                                    workoutSessionSummary.getTotalCaloriesConsumed());

        caloriesBurntTextView.setText(stringToDisplay);
    }


    /**
     * Convert duration in seconds to a string that is in the format of "HH:MM:SS"
     * (hour:minute:seconds)
     *
     * @param durationInSeconds duration in seconds
     *
     * @return a string in format of "HH:MM:SS"
     * */
    private static String convertSecondsToHourMinuteSeconds(int durationInSeconds)
    {
        int numberOfHours = durationInSeconds/3600;

        int numberOfRemainingSeconds = durationInSeconds - numberOfHours* 3600;

        int numberOfMinutes = numberOfRemainingSeconds/60;

        numberOfRemainingSeconds = numberOfRemainingSeconds - numberOfMinutes * 60;

        int numberOfSeconds = numberOfRemainingSeconds;

        return String.format(Locale.ENGLISH, "%02d:%02d:%02d", numberOfHours,
                numberOfMinutes, numberOfSeconds);
    }

    /**
     * Initialize the start workout image button
     * */
    private void initializeStartWorkoutImageButton()
    {
        ImageButton startWorkoutButton = findViewById(R.id.startWorkoutImageButton);

        startWorkoutButton.setOnClickListener(view -> {

            Intent myStart = StartWorkoutActivity.createIntent(getApplicationContext());
            HomePageActivity.this.startActivity(myStart);
        });

    }


    /**
     * Initialize the view past workout records image button.
     *
     * upon return from this activity, update the workout summary in case
     * of deletion of records by the user
     * */
    private void initializeViewPastWorkoutRecordsImageButton()
    {
        ImageButton viewPastWorkoutRecordsImageButton =
                findViewById(R.id.viewPastWorkoutRecordsImageButton);

        viewPastWorkoutRecordsImageButton.setOnClickListener(view -> {

            Intent intent = ViewPastWorkoutActivity.createIntent(getApplicationContext());
            HomePageActivity.this.viewPastWorkoutActivityIntentLauncher.launch(intent);
        });
    }

}