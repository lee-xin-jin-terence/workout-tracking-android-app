package com.murdoch.fitnessapp.controllers.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.murdoch.fitnessapp.R;
import com.murdoch.fitnessapp.controllers.fragments.MapAndImageSlideshowFragment;
import com.murdoch.fitnessapp.databases.helpers.WorkoutSessionDatabaseHelper;
import com.murdoch.fitnessapp.models.implementations.WorkoutSession;
import com.murdoch.fitnessapp.models.interfaces.IStoredWorkoutSession;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;


/**
 * UI controller for the activity_complete_workout.xml layout
 *
 * Provides an activity that allows view post-workout summary
 * after the completion of a workout session
 *
 * */
public class CompleteWorkoutActivity extends AppCompatActivity
{
    private IStoredWorkoutSession storedWorkoutSession;
    private WorkoutSession workoutSession;

    private ImageButton deleteWorkoutSessionImageButton;



    /**
     * Create an intent to start the DuringWorkoutActivity. The intent contains the
     * Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK flags
     * Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK flags
     *
     * @param packageContext context of the application package
     * @param workoutSession a WorkoutSession instance with the activity type and start date time
     *                      already set
     *
     * @throws NullPointerException if the argument 'workoutSession' is null
     * */
    public static Intent createIntent(Context packageContext, WorkoutSession workoutSession)
    {
        if (workoutSession == null)
        {
            throw new NullPointerException("Workout Session cannot be null");
        }


        Intent intent = new Intent(packageContext, CompleteWorkoutActivity.class);

        final String EXTRAS_WORKOUT_SESSION_KEY = "workoutSession";

        intent.putExtra(EXTRAS_WORKOUT_SESSION_KEY, workoutSession);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_workout);


        initializeWorkoutSessionFromIntent();

        initializeAllViews();

        saveWorkoutSessionRecordToDatabase();
    }



    /**
     * Initialize the workout session instance variable that was passed to
     * this activity via an intent
     * */
    private void initializeWorkoutSessionFromIntent()
    {
        final String EXTRAS_WORKOUT_SESSION_KEY = "workoutSession";

        this.workoutSession = getIntent().getExtras().getParcelable(EXTRAS_WORKOUT_SESSION_KEY);
    }


    /**
     * Initialize all the various views in the activity
     * */
    private void initializeAllViews()
    {
        initializePostWorkoutSummaryViews();
        initializeFragmentContainerView();
        initializeBackToHomeImageButton();
        initializeDeleteImageButton();
        initializeShareImageButton();
    }


    /**
     * Initialize the fragment container view to allow it to display a map/image
     * slideshow
     * */
    private void initializeFragmentContainerView()
    {
        MapAndImageSlideshowFragment mapAndImageSlideshowFragment =
                            MapAndImageSlideshowFragment.newInstance(this.workoutSession);

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainerView, mapAndImageSlideshowFragment);

        fragmentTransaction.commit();
    }


    /**
     * Initialize the views that display the post-workout summary
     *
     * Note: The this.workoutSession instance variable needs to be initialized
     * before calling this method
     * */
    private void initializePostWorkoutSummaryViews()
    {
        initializeDistanceTravelledTextView();
        initializeWorkoutDurationTextView();

        initializeNumberOfStepsTakenTextView();
        initializeCaloriesConsumedTextView();

    }


    /**
     * Initialize the text view that displays the distance travelled
     *
     * Note: The this.workoutSession instance variable needs to be initialized
     *      * before calling this method
     * */
    private void initializeDistanceTravelledTextView()
    {
        TextView distanceTravelledTextView = findViewById(R.id.distanceTravelledTextView);

        double distanceTravelledInKilometers =
                this.workoutSession.getDistanceTravelledInKilometers();

        distanceTravelledTextView.setText(
                String.format(Locale.ENGLISH,"%.2f km", distanceTravelledInKilometers));
    }



    /**
     * Initialize the text view that displays the workout duration
     *
     * Note: The this.workoutSession instance variable needs to be initialized
     *      * before calling this method
     * */
    private void initializeWorkoutDurationTextView()
    {
        TextView workoutDurationTextView = findViewById(R.id.workoutDurationTextView);

        String durationInHourMinuteSecond = getWorkoutDurationStringInHoursMinutesSeconds(
                this.workoutSession.getDurationInSeconds());

        workoutDurationTextView.setText(durationInHourMinuteSecond);
    }


    /**
     * Initialize the text view that displays the number of steps taken
     *
     * Note: The this.workoutSession instance variable needs to be initialized
     *      * before calling this method
     * */
    private void initializeNumberOfStepsTakenTextView()
    {
        int numberOfSteps = this.workoutSession.getNumberOfStepsTaken();

        TextView numberOfStepsTakenTextView = findViewById(R.id.numberOfStepsTakenTextView);

        numberOfStepsTakenTextView.setText(String.valueOf(numberOfSteps));
    }


    /**
     * Initialize the text view that displays the amount of calories consumed
     *
     * Note: The this.workoutSession instance variable needs to be initialized
     *      * before calling this method
     * */
    private void initializeCaloriesConsumedTextView()
    {
        double caloriesConsumed = this.workoutSession.getCaloriesConsumed();

        TextView caloriesConsumedTextView = findViewById(R.id.caloriesConsumedTextView);

        caloriesConsumedTextView.setText(String.format(Locale.ENGLISH,
                            "%.0f", caloriesConsumed));
    }


    /**
     * Initialize the delete workout session image button
     *
     * Upon clicking, the workout session will be prompted a confirmation. If confirmed,
     * the workout session will be deleted and the user will be redirected to the home page
     * */
    private void initializeDeleteImageButton()
    {
        this.deleteWorkoutSessionImageButton = findViewById(R.id.deleteImageButton);

        this.deleteWorkoutSessionImageButton.setOnClickListener(view -> promptDeleteConfirmation());
    }


    /**
     * Initialize the button that allows users to return to the home page
     * */
    private void initializeBackToHomeImageButton()
    {
        ImageButton backToHomeImageButton = findViewById(R.id.backToHomeImageButton);

        Intent intent = HomePageActivity.createIntent(getApplicationContext());

        backToHomeImageButton.setOnClickListener(view -> startActivity(intent));
    }


    /**
     * Initialize the share button so that it allows users to share workout session data
     * (text-only) with other apps
     *
     * */
    private void initializeShareImageButton()
    {
        ImageButton shareImageButton = findViewById(R.id.shareImageButton);

        shareImageButton.setOnClickListener(view -> shareWorkoutSessionWithOtherApps());
    }



    /**
     * Save the workout session record to the device database
     *
     * If successfully saved, then the this.storedWorkoutSession instance
     * will contain the instance of the saved workout session
     *
     * If unsuccessful, the delete button will be disabled
     *
     *
     * Note: The delete button need to be initialized before calling this method
     * */
    private void saveWorkoutSessionRecordToDatabase()
    {
        WorkoutSessionDatabaseHelper helper =
                new WorkoutSessionDatabaseHelper(getApplicationContext());

        try
        {
            this.storedWorkoutSession = helper.insertWorkoutSessionRecord(this.workoutSession);

            Toast.makeText(getApplicationContext(), "Workout session data successfully saved",
                    Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            this.storedWorkoutSession = null;

            this.deleteWorkoutSessionImageButton.setEnabled(false);

            Toast.makeText(getApplicationContext(), "Failed to save workout session data ",
                    Toast.LENGTH_LONG).show();
        }

    }


    /**
     * Display a dialog to prompt the user whether the user truly wishes to delete the
     * workout session.
     *
     * If so, then delete the workout session and direct the user back to the home page.
     * */
    private void promptDeleteConfirmation()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(R.string.activity_complete_workout_prompt_delete_title_text);

        alertDialogBuilder.setMessage(
                            R.string.activity_complete_workout_prompt_delete_confirmation_text);

        alertDialogBuilder.setNegativeButton(R.string.activity_complete_workout_no_text,
                (dialogInterface, selectedButtonId) -> {
                    //do nothing
                });

        alertDialogBuilder.setPositiveButton(R.string.activity_complete_workout_yes_text,
                (dialogInterface, selectedButtonId) -> deleteWorkoutSession());


        alertDialogBuilder.show();
    }


    /**
     * Delete the workout session from the database and also delete the image files
     * from the app directory.
     *
     * Once done, redirect the user back to the app home page
     * */
    private void deleteWorkoutSession()
    {
        deleteWorkoutSessionFromDatabase();
        deleteWorkoutSessionImageFiles();
        startActivity(HomePageActivity.createIntent(getApplicationContext()));
    }


    /**
     * Delete the workout session record from the database
     * */
    private void deleteWorkoutSessionFromDatabase()
    {
        WorkoutSessionDatabaseHelper databaseHelper =
                    new WorkoutSessionDatabaseHelper(getApplicationContext());

        try
        {
            long workoutSessionId = this.storedWorkoutSession.getWorkoutSessionId();
            databaseHelper.deleteWorkoutSessionRecord(workoutSessionId);
            Toast.makeText(getApplicationContext(),"Workout session deleted",
                    Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error occurred while trying" +
                            "to delete workout Session", Toast.LENGTH_LONG).show();

        }
    }


    /**
     * Delete the image files associated with the workout from the app directory
     * */
    private void deleteWorkoutSessionImageFiles()
    {
        if (this.workoutSession.getListOfImagesTaken().isEmpty()) {
            return;
        }


        List <String> listOfImages = this.workoutSession.getListOfImagesTaken();

        try {
            for (String imagePath: listOfImages)
            {
                File imageFileToDelete = new File(imagePath);

                boolean deletionSuccessful = imageFileToDelete.delete();

                if (!deletionSuccessful)
                {
                    throw new Exception("Failed to delete image");
                }
            }

            Toast.makeText(getApplicationContext(), "Workout session image files " +
                            "successfully deleted", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Workout image files failed to " +
                            "be deleted", Toast.LENGTH_LONG).show();
        }

    }


    /**
     * Share the workout session data with other apps.
     *
     * As many apps lack the ability to properly access images, this application
     * will only share plain text with other apps
     * */
    private void shareWorkoutSessionWithOtherApps()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_TEXT, getShareTextString());
        intent.setType("text/plain");

        startActivity(intent);
    }


    /**
     * Converts the workout session start date time to the format of "dd MMM yyyy, hh:mm:ss a"
     *
     * E.g. 27 July 2020, 12:23:14 PM
     *
     * Note: The this.workoutSession instance variable must first be initialized
     *
     * */
    private String getShareTextString()
    {
        String startDateTimeString = getFormattedDateTimeString(
                this.workoutSession.getStartDateTime());

        String distanceTravelledString = String.format(Locale.ENGLISH,
                    "%.2f km", this.workoutSession.getDistanceTravelledInKilometers());

        String workoutDurationInHoursMinutesSeconds =
                getWorkoutDurationStringInHoursMinutesSeconds(
                        this.workoutSession.getDurationInSeconds());

        String caloriesConsumedString = String.format(Locale.ENGLISH,
                "%.0f cal", this.workoutSession.getCaloriesConsumed());



        return "I have completed a workout session!" +
                "\n\nDate Time: " + startDateTimeString +
                "\nActivity Type: " + workoutSession.getActivityType().name() +
                "\nDistance: " + distanceTravelledString +
                "\nDuration: " + workoutDurationInHoursMinutesSeconds +
                "\nNumber of steps: " + workoutSession.getNumberOfStepsTaken() +
                "\nCalories Consumed: " + caloriesConsumedString;
    }


    /**
     * Converts the date time to the format of "dd MMM yyyy, hh:mm:ss a"
     *
     * E.g. 27 July 2020, 12:23:14 PM
     *
     * @param localDateTime the date time to be converted to formatted string
     *
     * @return formatted date time string
     * */
    private String getFormattedDateTimeString(LocalDateTime localDateTime)
    {
        DateTimeFormatter dateTimeFormatter =
               DateTimeFormatter.ofPattern ("dd MMM yyyy, hh:mm:ss a", Locale.ENGLISH);

        return localDateTime.format(dateTimeFormatter);

    }


    /**
     * Convert the workout duration in seconds to a string that is in the format of "hh:mm:ss"
     * (hour:minute:seconds)
     *
     * @param durationInSeconds the duration to be converted to hours minutes seconds
     *
     * @return a string in format of "HH:MM:SS"
     * */
    private static String getWorkoutDurationStringInHoursMinutesSeconds(int durationInSeconds)
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