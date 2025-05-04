package com.murdoch.fitnessapp.controllers.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.murdoch.fitnessapp.R;
import com.murdoch.fitnessapp.databases.helpers.WorkoutSessionDatabaseHelper;
import com.murdoch.fitnessapp.models.implementations.StoredWorkoutSession;
import com.murdoch.fitnessapp.models.interfaces.IGPSLocation;
import com.murdoch.fitnessapp.models.interfaces.IStoredWorkoutSession;
import com.murdoch.fitnessapp.models.interfaces.IWorkoutSession;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * UI controller for the activity_view_past_workout.xml layout
 *
 * Provides an activity that allows user to view details regarding
 * the workout sessions that were previously tracked
 *
 * The activity result code of this activity has two possible values:
 *      (a) RESULT_HAS_NOT_DELETED_WORKOUT_SESSION_RECORDS
 *              if the user has not deleted any workout session records
 *
 *      (b) RESULT_HAS_DELETED_WORKOUT_SESSION_RECORDS
 *              if the user has deleted one or more workout session records
 * */
public class ViewPastWorkoutActivity extends AppCompatActivity {

    private LinearLayout pastWorkoutSessionsRootContainer;

    /**
     * Activity result code if the user has not deleted any workout records
     * */
    public static final int RESULT_HAS_NOT_DELETED_WORKOUT_SESSION_RECORDS =
                                                                    Activity.RESULT_FIRST_USER + 1;

    /**
     * Activity result code if the user has deleted one or more workout records
     * */
    public static final int RESULT_HAS_DELETED_WORKOUT_SESSION_RECORDS =
                                                                    Activity.RESULT_FIRST_USER + 2;


    /**
     * Create an intent to start the HomePageActivity
     *
     * @param packageContext context of the application package
     *
     * @return an intent to start this application
     * */
    public static Intent createIntent(Context packageContext)
    {
        return new Intent(packageContext, ViewPastWorkoutActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_past_workout);

        initializeActivityResultCode();
        initializeAllViews();

    }


    /**
     * Initialize the activity result code to RESULT_HAS_NOT_DELETED_WORKOUT_SESSION_RECORDS,
     * meaning that the user has not deleted any workout session records
     *
     * This activity result code may change if the user has deleted one or more workout
     * session records
     * */
    private void initializeActivityResultCode()
    {
        setResult(RESULT_HAS_NOT_DELETED_WORKOUT_SESSION_RECORDS);
    }


    /**
     * Initialize all the views in this activity
     * */
    private void initializeAllViews()
    {
        initializeBackArrowNavigationImageButton();
        initializePastWorkoutSessionsRootContainer();

        //hide the "No records available" message if there is at least one text
        initializeOrUpdateNoWorkoutRecordsAvailableTextView();
    }




    /**
     * Initialize the back arrow navigation button to return to
     * the home page
     * */
    private void initializeBackArrowNavigationImageButton()
    {
        ImageButton backArrowNavigationImageButton =
                findViewById(R.id.backArrowNavigationImageButton);

        backArrowNavigationImageButton.setOnClickListener(view -> finish());
    }


    /**
     * Initialize the past workout sessions root container to display a list of past workout
     * session records
     * */
    private void initializePastWorkoutSessionsRootContainer()
    {
        this.pastWorkoutSessionsRootContainer =
                                findViewById(R.id.pastWorkoutSessionsRootContainer);

        List<IStoredWorkoutSession> listOfPastWorkoutSessions =
                                    queryAndGetListOfPastWorkoutSessions();

        displayListOfWorkoutSessions(listOfPastWorkoutSessions);
    }



    /**
     * Query the database to get a list of all past workout session records
     *
     * @return list of all past workout session records
     * */
    private List<IStoredWorkoutSession> queryAndGetListOfPastWorkoutSessions()
    {
        WorkoutSessionDatabaseHelper databaseHelper =
                                    new WorkoutSessionDatabaseHelper(getApplicationContext());

        return databaseHelper.queryAllWorkoutSessionRecords();
    }


    /**
     * Display a list of past workout sessions records. The workout session records are grouped
     * together by their dates
     * */
    private void displayListOfWorkoutSessions(List<IStoredWorkoutSession> listOfPastWorkoutSessionRecords)
    {

        LocalDate previousWorkoutSessionDate = null;
        LinearLayout groupOfSameDateWorkoutSessionsParentView = null;

        for (IStoredWorkoutSession workoutSession : listOfPastWorkoutSessionRecords)
        {
            LocalDate currentWorkoutSessionDate = workoutSession.getStartDateTime().toLocalDate();

            if (previousWorkoutSessionDate == null  ||
                    !previousWorkoutSessionDate.equals(currentWorkoutSessionDate))
            {
                groupOfSameDateWorkoutSessionsParentView = getLinearLayoutWithDateSubHeader(currentWorkoutSessionDate);

                this.pastWorkoutSessionsRootContainer.addView(groupOfSameDateWorkoutSessionsParentView);
            }


            createWorkoutSessionViewAndAddToParentView(
                        groupOfSameDateWorkoutSessionsParentView, workoutSession);

            previousWorkoutSessionDate = currentWorkoutSessionDate;
        }


    }


    /**
     * Get a linear layout (vertical orientation) with a textview as a single child
     *
     * The textview contains a date designed as a sub-header
     * */
    private LinearLayout getLinearLayoutWithDateSubHeader(LocalDate localDate)
    {
        LinearLayout linearLayout = getEmptyLinearLayoutWithVerticalOrientation();
        TextView dateSubHeaderTextView = getDateSubHeaderTextView(localDate);

        linearLayout.addView(dateSubHeaderTextView);

        return linearLayout;
    }


    /**
     * Return a linear layout with vertical orientation, along with
     * center + top gravity
     * */
    private LinearLayout getEmptyLinearLayoutWithVerticalOrientation()
    {
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER | Gravity.TOP);

        return linearLayout;
    }



    /**
     * Return a text view containing a date. This text view will act as a subheader
     *
     * @param localDate date of interest
     * */
    private TextView getDateSubHeaderTextView(LocalDate localDate)
    {
        LayoutInflater layoutInflater =
                (LayoutInflater) getApplicationContext().getSystemService(
                                                        Context.LAYOUT_INFLATER_SERVICE);

        final ViewGroup NO_PARENT_ROOT = null;

        //had to inflate an xml layout as it is too difficult to create one programmatically
        TextView textView = (TextView) layoutInflater.inflate(
                                        R.layout.custom_subheader_title_text_view, NO_PARENT_ROOT);


        String localDateString = localDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));

        textView.setText(localDateString);

        return textView;
    }



    /**
     * Create and initialize the view that displays a single workout session view. The view
     * is then add to the parent view
     *
     * @param groupOfSameDateWorkoutSessionsParentView the parent view containing the workout
     *                                                 session view. This parent view is used
     *                                                 to store a list of workout session views
     *                                                 with the same dates
     *
     * @param workoutSession the workout session instance containing the workout session data
     *                       to be displayed
     * */
    private void createWorkoutSessionViewAndAddToParentView(LinearLayout groupOfSameDateWorkoutSessionsParentView,
                                                            IStoredWorkoutSession workoutSession)
    {
        LinearLayout workoutSessionView = getUninitializedWorkoutSessionView();


        initializeStartTimeTextView(workoutSessionView, workoutSession);
        initializeActivityTypeImageViewAndTextView(workoutSessionView,workoutSession);

        initializeWorkoutDurationTextView(workoutSessionView, workoutSession);
        initializeDistanceTravelledTextView(workoutSessionView, workoutSession);

        initializeNumberOfStepsTakenTextView(workoutSessionView, workoutSession);
        initializeCaloriesConsumedTextView(workoutSessionView, workoutSession);

        initializeMapIconImageView(workoutSessionView, workoutSession);
        initializePhotoIconImageView(workoutSessionView, workoutSession);

        initializeTripleDotMoreImageButton(workoutSessionView, groupOfSameDateWorkoutSessionsParentView, workoutSession);
        initializeWorkoutSessionViewOnClickEventHandler(workoutSessionView, workoutSession);


        groupOfSameDateWorkoutSessionsParentView.addView(workoutSessionView);
    }


    /**
     * Return a view of a workout session record, where the view has not yet been initialized
     * */
    private LinearLayout getUninitializedWorkoutSessionView()
    {
        LayoutInflater layoutInflater =
                (LayoutInflater) getApplicationContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

        final ViewGroup NO_PARENT_ROOT = null;

        return (LinearLayout) layoutInflater.inflate(
                R.layout.linear_layout_single_workout_record, NO_PARENT_ROOT);
    }


    /**
     * Initialize the textview displaying the start time travelled of the
     * workout session record
     *
     * @param workoutSessionView the view representing a single workout session
     * @param workoutSession the workout session of interest
     * */
    private void initializeStartTimeTextView(LinearLayout workoutSessionView,
                                             IStoredWorkoutSession workoutSession)
    {
        LocalDateTime startDateTime = workoutSession.getStartDateTime();

        LocalTime startTime = startDateTime.toLocalTime();

        String startTimeString = convertLocalTimeToString(startTime);

        TextView startTimeTextView = workoutSessionView.findViewById(R.id.startTimeTextView);

        startTimeTextView.setText(startTimeString);

    }


    /**
     * Initialize the textview and image view displaying the activity type of
     * the workout session record
     * */
    private void initializeActivityTypeImageViewAndTextView(LinearLayout workoutSessionView,
                                                            IStoredWorkoutSession workoutSession)
    {
        TextView activityTypeTextView = workoutSessionView.findViewById(R.id.activityTypeTextView);
        ImageView activityTypeImageView = workoutSessionView.findViewById(
                                                                    R.id.activityTypeImageView);

        if (workoutSession.getActivityType() == IWorkoutSession.ActivityType.WALKING)
        {
            activityTypeTextView.setText(R.string.linear_layout_single_workout_record_walk_text);
            activityTypeImageView.setImageResource(R.drawable.walking_icon);
        }
        else
        {
            activityTypeTextView.setText(R.string.linear_layout_single_workout_record_run_text);
            activityTypeImageView.setImageResource(R.drawable.running_icon);
        }
    }


    /**
     * Extract the time within the LocalDateTime instance and convert it
     * to time with the format of "hh:mm aa"
     *
     * e.g. a string such as "12:16 AM"
     * */
    private String convertLocalTimeToString(LocalTime localTime)
    {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        return localTime.format(dateTimeFormatter);
    }



    /**
     * Initialize the textview displaying the distance travelled of the
     * workout session record
     *
     * @param workoutSessionView the view representing a single workout session
     * @param workoutSession the workout session of interest
     * */
    private void initializeDistanceTravelledTextView(LinearLayout workoutSessionView,
                                                     IStoredWorkoutSession workoutSession)
    {
        double distanceTravelledInKilometers =
                                        workoutSession.getDistanceTravelledInKilometers();

        String distanceTravelledInKilometersString =
                    String.format(Locale.ENGLISH, "%.2f km", distanceTravelledInKilometers);


        TextView distanceTravelledTextView = workoutSessionView.findViewById(
                                                    R.id.distanceTravelledTextView);

        distanceTravelledTextView.setText(distanceTravelledInKilometersString);
    }



    /**
     * Initialize the textview displaying the workout duration of the
     * workout session record
     *
     * @param workoutSessionView the view representing a single workout session
     * @param workoutSession the workout session of interest
     * */
    private void initializeWorkoutDurationTextView(LinearLayout workoutSessionView,
                                                   IStoredWorkoutSession workoutSession)
    {
        int workoutDurationInSeconds = workoutSession.getDurationInSeconds();

        String workoutDurationInHourMinuteSeconds =
                                    convertSecondsToHourMinuteSecond(workoutDurationInSeconds);

        TextView durationTextView = workoutSessionView.findViewById(R.id.workoutDurationTextView);

        durationTextView.setText(workoutDurationInHourMinuteSeconds);
    }


    /**
     * Initialize the textview displaying the number of steps taken for the particular
     * workout session record
     *
     * @param workoutSessionView the view representing a single workout session
     * @param workoutSession the workout session of interest
     * */
    private void initializeNumberOfStepsTakenTextView(LinearLayout workoutSessionView,
                                                      IStoredWorkoutSession workoutSession)
    {

        int numberOfStepsTaken = workoutSession.getNumberOfStepsTaken();

        String numberOfStepsTakenString = String.valueOf(numberOfStepsTaken);


        TextView stepsTakenTextView = workoutSessionView.findViewById(R.id.numberOfStepsTakenTextView);

        stepsTakenTextView.setText(numberOfStepsTakenString);

    }


    /**
     * Initialize the textview displaying the calories consumed for the particular
     * workout session record
     *
     * @param workoutSessionView the view representing a single workout session
     * @param workoutSession the workout session of interest
     * */
    private void initializeCaloriesConsumedTextView(LinearLayout workoutSessionView,
                                                    IStoredWorkoutSession workoutSession)
    {
        double caloriesConsumed = workoutSession.getCaloriesConsumed();

        String caloriesConsumedString = String.format(Locale.ENGLISH, "%.0f",
                                                      caloriesConsumed);

        TextView caloriesConsumedTextView = workoutSessionView.findViewById(
                                                                R.id.caloriesConsumedTextView);

        caloriesConsumedTextView.setText(caloriesConsumedString);
    }



    /**
     * Initialize and display the map icon image view only if the workout session
     * has at least one non-null coordinates (location) in it
     *
     * Note: WorkoutSessions are allowed to contain null coordinates to signify a pause
     *      in the workout session
     *
     * @param workoutSessionView the view representing a single workout session
     * @param workoutSession the workout session of interest
     * */
    private void initializeMapIconImageView(LinearLayout workoutSessionView,
                                            IStoredWorkoutSession workoutSession)
    {
        ImageView mapIconImageView = workoutSessionView.findViewById(R.id.mapIconImageView);


        int numberOfNonNullMapLocations = countNumberOfNonNullMapLocations(workoutSession);

        if (numberOfNonNullMapLocations == 0)
        {
            mapIconImageView.setVisibility(View.GONE);
        }
        else
        {
            mapIconImageView.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Count and return the number of non-null map locations (coordinates) stored within
     * the workout session
     *
     * @param workoutSession the workout session of interest
     * */
    private int countNumberOfNonNullMapLocations(IStoredWorkoutSession workoutSession)
    {
        int count = 0;

        for (IGPSLocation gpsLocation: workoutSession.getListOfGPSLocations())
        {
            if (gpsLocation != null)
            {
                count++;
            }
        }

        return count;
    }


    /**
     * Initialize and display the photo icon image view only if the workout session
     * has photos associated with the workout session
     *
     * @param workoutSessionView the view representing a single workout session
     * @param workoutSession the workout session of interest
     * */
    private void initializePhotoIconImageView(LinearLayout workoutSessionView,
                                              IStoredWorkoutSession workoutSession)
    {
        ImageView photoIconImageView = workoutSessionView.findViewById(R.id.photoIconImageView);

        if (workoutSession.getListOfImagesTaken().isEmpty())
        {
            photoIconImageView.setVisibility(View.GONE);
        }
        else
        {
            photoIconImageView.setVisibility(View.VISIBLE);
        }
    }



    /**
     * Initialize the triple dot image button that creates a pop-up menu.
     *
     * The pop-up menu will allow users to
     *      (a) Share the workout session record
     *      (b) Delete the workout session record
     *
     *
     * @param workoutSessionView the view containing the workout session record
     * @param groupOfSameDateWorkoutSessionsParentView the parent view containing the workout
     *                                                       session view. This parent view is used
     *                                                      to store a list of workout session views
     *                                                       with the same dates
     * @param workoutSession the workout session to be deleted
     * */
    private void initializeTripleDotMoreImageButton(LinearLayout workoutSessionView,
                                            LinearLayout groupOfSameDateWorkoutSessionsParentView,
                                            IStoredWorkoutSession workoutSession)
    {

        ImageButton tripleDotMoreImageButton = workoutSessionView.findViewById(
                                            R.id.tripleDotMoreImageButton);


        tripleDotMoreImageButton.setOnClickListener(view -> {

            PopupMenu popupMenu = new PopupMenu(ViewPastWorkoutActivity.this,
                                                tripleDotMoreImageButton);

            popupMenu.getMenuInflater().inflate(R.menu.past_workout_record_popup_menu,
                                                popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {

                if (menuItem.getItemId() == R.id.shareWorkoutRecordMenuItem)
                {
                    shareWorkoutSessionWithOtherApps(workoutSession);
                    return true;
                }
                else if (menuItem.getItemId() == R.id.deleteWorkoutRecordMenuItem)
                {
                    promptDeleteWorkoutSessionConfirmation(workoutSessionView,
                            groupOfSameDateWorkoutSessionsParentView, workoutSession);
                    return true;
                }

                return false;
            });

            popupMenu.show();
        });
    }


    /**
     * Initialize the click event for the workout session view to display the
     * map-and-image slideshow
     *
     * The slideshow displays
     *      (a) a map showing the path of where the user has travelled in a workout session
     *      (b) photos taken during the workout session (if any)
     *
     *  @param workoutSessionView the view representing a single workout session
     *  @param workoutSession the workout session of interest
     * */
    private void initializeWorkoutSessionViewOnClickEventHandler(LinearLayout workoutSessionView,
                                                                 IStoredWorkoutSession workoutSession)
    {
        workoutSessionView.setOnClickListener(
                view -> {
                    Intent intent = MapAndImageSlideshowWrapperActivity.createIntent(
                            this, (StoredWorkoutSession) workoutSession);

                    startActivity(intent);
                });
    }


    /**
     * Share the workout session data with other apps.
     *
     * As many apps lack the ability to properly access images, this application
     * will only share plain text with other apps
     * */
    private void shareWorkoutSessionWithOtherApps(IStoredWorkoutSession workoutSession)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_TEXT, getShareTextString(workoutSession));

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
    private String getShareTextString(IStoredWorkoutSession workoutSession)
    {
        String startDateTimeString = getFormattedDateTimeString(
                workoutSession.getStartDateTime());

        String distanceTravelledString = String.format(Locale.ENGLISH,
                "%.2f km", workoutSession.getDistanceTravelledInKilometers());

        String workoutDurationInHoursMinutesSeconds =
                convertSecondsToHourMinuteSecond(workoutSession.getDurationInSeconds());

        String caloriesConsumedString = String.format(Locale.ENGLISH,
                "%.0f cal", workoutSession.getCaloriesConsumed());



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
     * Convert duration in seconds to a string that is in the format of "HH:MM:SS"
     * (hour:minute:seconds)
     *
     * @param durationInSeconds duration in seconds
     *
     * @return a string in format of "HH:MM:SS"
     * */
    private String convertSecondsToHourMinuteSecond(int durationInSeconds)
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
     * Display a dialog to prompt the user whether the user truly wishes to delete the
     * workout session record
     *
     * If so, then delete the workout session record and update the view
     *
     * @param workoutSessionView the view containing the workout session record
     *
     * @param groupOfSameDateWorkoutSessionsParentView the parent view containing the workout
*    *                                                session view. This parent view is used
     *                                                 to store a list of workout session views
     *                                                 with the same dates
     *
     * @param workoutSession the workout session to be deleted
     * */
    private void promptDeleteWorkoutSessionConfirmation(LinearLayout workoutSessionView,
                                            LinearLayout groupOfSameDateWorkoutSessionsParentView,
                                            IStoredWorkoutSession workoutSession)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(R.string.activity_view_past_workout_prompt_delete_title_text);

        alertDialogBuilder.setMessage(
                R.string.activity_view_past_workout_prompt_delete_confirmation_text);

        alertDialogBuilder.setNegativeButton(R.string.activity_view_past_workout_no_text,
                (dialogInterface, i) -> {
                    //do nothing
                });


        alertDialogBuilder.setPositiveButton(R.string.activity_view_past_workout_yes_text,
                (dialogInterface, i) ->
                                deleteWorkoutSession(workoutSessionView,
                                        groupOfSameDateWorkoutSessionsParentView, workoutSession));


        alertDialogBuilder.show();
    }



    /**
     * Delete the workout session from the database. The corresponding workout session
     * view will also be removed from the display
     *
     * @param workoutSessionView the view containing the workout session record
     *
     * @param groupOfSameDateWorkoutSessionsParentView the parent view containing the workout
     *                                                  session view. This parent view is used
     *                                                  to store a list of workout session views
     *                                                  with the same dates
     *
     * @param workoutSession the workout session to be deleted
     * */
    private void deleteWorkoutSession(LinearLayout workoutSessionView,
                                      LinearLayout groupOfSameDateWorkoutSessionsParentView,
                                      IStoredWorkoutSession workoutSession)
    {

        deleteWorkoutSessionFromDatabase(workoutSession);
        deleteWorkoutSessionImageFiles(workoutSession);
        removeWorkoutSessionViewFromParentView(workoutSessionView, groupOfSameDateWorkoutSessionsParentView);
        initializeOrUpdateNoWorkoutRecordsAvailableTextView();


        setResult(RESULT_HAS_DELETED_WORKOUT_SESSION_RECORDS);

    }


    /**
     * Delete the workout session record from the database
     *
     * @param workoutSession the workout session to be deleted from the database
     * */
    private void deleteWorkoutSessionFromDatabase(IStoredWorkoutSession workoutSession)
    {
        WorkoutSessionDatabaseHelper databaseHelper =
                            new WorkoutSessionDatabaseHelper(getApplicationContext());

        try
        {
            long workoutSessionId = workoutSession.getWorkoutSessionId();
            databaseHelper.deleteWorkoutSessionRecord(workoutSessionId);
            Toast.makeText(getApplicationContext(),"Workout session deleted",
                    Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error occurred while trying" +
                    "to delete workout session", Toast.LENGTH_LONG).show();

        }

    }


    /**
     * Delete the image files associated with the workout from the app directory
     *
     * @param workoutSession the workout session of interest, whose images are to
     *                       to be deleted from the app directory
     * */
    private void deleteWorkoutSessionImageFiles(IStoredWorkoutSession workoutSession)
    {
        if (workoutSession.getListOfImagesTaken().isEmpty())
        {
            return;
        }

        //list of image paths of images
        List<String> listOfImages =  workoutSession.getListOfImagesTaken();

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
            Toast.makeText(getApplicationContext(), "Workout session image files failed to " +
                    "be deleted", Toast.LENGTH_LONG).show();

        }
    }


    /**
     * Removes the workout session view from the parent view. If the workout session view
     * is the very last view in the parent view, then also remove the parent view
     * from the root container
     *
     * @param workoutSessionView the view containing the workout session record
     * @param groupOfSameDateWorkoutSessionsParentView the parent view containing the workout
     *                                                 session view. This parent view is used
     *                                                 to store a list of workout session views
     *                                                 with the same dates
     * */
    private void removeWorkoutSessionViewFromParentView(LinearLayout workoutSessionView,
                                            LinearLayout groupOfSameDateWorkoutSessionsParentView)
    {
        groupOfSameDateWorkoutSessionsParentView.removeView(workoutSessionView);

        int numberOfRemainingWorkoutSessionViewsInParent =
                getNumberOfWorkoutSessionViewsInParent(groupOfSameDateWorkoutSessionsParentView);


        if (numberOfRemainingWorkoutSessionViewsInParent == 0)
        {
            this.pastWorkoutSessionsRootContainer.removeView(groupOfSameDateWorkoutSessionsParentView);
        }
    }



    /**
     * Returns the number of workout session views (number of children) within thin this
     * parent view, excluding the date sub-header
     *
     * @param groupOfSameDateWorkoutSessionsParentView the parent view containing the workout
     *                                               a list of workout session views
     *                                               with the same dates
     *
     * @return the number of workout session views within the parent view
     * */
    private int getNumberOfWorkoutSessionViewsInParent(LinearLayout
                                                           groupOfSameDateWorkoutSessionsParentView)
    {
        //The parent will contain the date subheader as a child,
        // along with other workout session views
        // minus 1 to not include the date subheader

        return groupOfSameDateWorkoutSessionsParentView.getChildCount() - 1;
    }


    /**
     * Initialize the "No Workout Records Available" message by showing or
     * hiding it whether there are workout session records available
     *
     * Note: Must be called after the all the workout session records
     * have been displayed
     * */
    private void initializeOrUpdateNoWorkoutRecordsAvailableTextView()
    {
        TextView noWorkoutRecordsMessageTextView =
                        findViewById(R.id.noWorkoutRecordsAvailableTextView);

        if (this.pastWorkoutSessionsRootContainer.getChildCount() == 0)
        {
            noWorkoutRecordsMessageTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            noWorkoutRecordsMessageTextView.setVisibility(View.GONE);
        }
    }
}