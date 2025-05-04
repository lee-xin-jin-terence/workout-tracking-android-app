package com.murdoch.fitnessapp;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import android.Manifest;
import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.murdoch.fitnessapp.controllers.activities.HomePageActivity;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

/**
 * Automated UI tests for the app
 * */
@LargeTest
public class AutomatedUITest
{
    //cannot use the recommended ActivityScenarioRule as it has a high chance of throwing errors
    // if the internal resources are not properly cleaned up
    @Rule
    public ActivityTestRule<HomePageActivity> activityRule
            = new ActivityTestRule<>(HomePageActivity.class);


    @Rule
    public GrantPermissionRule grantCoarseLocationRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

    @Rule
    public GrantPermissionRule grantFineLocationRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);


    @Rule
    public GrantPermissionRule grantPhysicalActivityRule =
            GrantPermissionRule.grant(Manifest.permission.ACTIVITY_RECOGNITION);

    @Rule
    public GrantPermissionRule grantReadExternalStorageActivityRule =
            GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);


    /**
     * Start a test that involves tracking the user doing a "Walking" workout
     *
     * The workout will last for 10 seconds, and then check that the workout details
     * are displayed correctly in the
     *      (a) post-workout summary page and
     *      (b) Past workout records page
     *
     * Finally, delete the workout session and return to the home page
     **/
    @Test
    public void walkingWorkoutSessionTest() throws InterruptedException
    {
        final String WALKING_ACTIVITY = "Walk";

        startWorkoutSession(WALKING_ACTIVITY);

        String [] trackedResults = trackWorkoutSessionAndProceedToPostWorkoutSummary();

        checkIfPostWorkoutSummaryDetailsAreCorrectAndReturnToHomePage(trackedResults);


        viewPastWorkoutRecordsToCheckIfWorkoutSessionDetailsCorrect(WALKING_ACTIVITY,
                                                                   trackedResults);
    }



    /**
     * Start a test that involves tracking the user doing a "Running" workout
     *
     * The workout will last for 10 seconds, and then check that the workout details
     * are displayed correctly in the
     *      (a) post-workout summary page and
     *      (b) Past workout records page
     *
     * Finally, delete the workout session and return to the home page
     **/
    @Test
    public void runningWorkoutSessionTest() throws InterruptedException {

        final String RUNNING_ACTIVITY = "Run";

        startWorkoutSession(RUNNING_ACTIVITY);

        String [] trackedResults = trackWorkoutSessionAndProceedToPostWorkoutSummary();

        checkIfPostWorkoutSummaryDetailsAreCorrectAndReturnToHomePage(trackedResults);

        viewPastWorkoutRecordsToCheckIfWorkoutSessionDetailsCorrect(RUNNING_ACTIVITY,
                                                                    trackedResults);
    }


    /**
     * Helper Method
     *
     * Select the workout type and then start the workout session
     *
     * @param activityType the activity type
     *                     There are two acceptable values:
     *                          (a) "Walk"
     *                          (b) "Run"
     * */
    private void startWorkoutSession(String activityType)
    {
        //click on the HomePageActivity
        onView(withId(R.id.startWorkoutImageButton)).perform(click());

        //start the workout session
        onView(withId(R.id.selectWorkoutTypeSpinner)).perform(click());


        if (activityType.equals("Walk"))
        {
            //click on "Walk" option
            onData(anything()).atPosition(1).perform(click());
        }
        else
        {
            //click on "Run" option
            onData(anything()).atPosition(2).perform(click());
        }

        //click on the StartWorkoutActivity
        onView(withId(R.id.startWorkoutImageButton)).perform(click());

    }



    /**
     * Helper Method
     *
     * Track the workout session for 10 seconds and then end the workout session
     *
     * @return a String array of exactly 4 elements, in the order of
     *          index [0] -> workout duration (e.g. "00:10:35")
     *          index [1] -> distance travelled (e.g. "25.43 km")
     *          index [2] -> number of steps taken (e.g. "34")
     *          index [3] -> calories consumed (e.g. "99")
     * */
    private String[] trackWorkoutSessionAndProceedToPostWorkoutSummary() throws InterruptedException
    {

        //test toggling between map and info mode
        onView(withId(R.id.infoModeOrMapModeImageButton)).perform(click());
        onView(withId(R.id.infoModeOrMapModeImageButton)).perform(click());


        //sleep the thread to allow the workout to run for ten seconds
        final int TEN_SECONDS_IN_MILLI_SECONDS = 10000;
        Thread.sleep(TEN_SECONDS_IN_MILLI_SECONDS);


        //pause the workout session so that workout values don't change anymore
        // ensure that data can be safely extracted without errors
        onView(withId(R.id.pauseOrContinueImageButton)).perform(click());


        //extract the various tracked workout values from their respective text views
        String workoutDurationString = getTextFromTextView(withId(R.id.workoutDurationTextView));
        String distanceTravelledString = getTextFromTextView(withId(R.id.distanceTravelledTextView));

        String numberOfStepsTakenString = getTextFromTextView(withId(R.id.numberOfStepsTakenTextView));
        String caloriesConsumedString = getTextFromTextView(withId(R.id.caloriesConsumedTextView));

        //finish the workout session
        onView(withId(R.id.finishImageButton)).perform(click());

        //click on the 'yes' button of the alert dialog to confirm the ending of
        // the workout session to proceed to the workout summary page
        onView(withText("Yes")).perform(click());


        return new String[]{
                workoutDurationString, distanceTravelledString,
                numberOfStepsTakenString, caloriesConsumedString
        };
    }



    /**
     * Helper Method
     *
     * On the workout summary page (CompleteWorkoutActivity), check if the details/values displayed
     * are correct.
     *
     * Once done checking, return to the home page
     *
     * @param expectedResults a string array with exactly 4 elements, containing the expected
     *                        values of the workout session to be checked against the
     *                        post-workout details displayed
     *
     *                        The elements are in the order of:
     *                          index [0] -> workout duration (e.g. "00:10:35")
     *                          index [1] -> distance travelled (e.g. "25.43 km")
     *                          index [2] -> number of steps taken (e.g. "34")
     *                          index [3] -> calories consumed (e.g. "99")
     * */
    private void checkIfPostWorkoutSummaryDetailsAreCorrectAndReturnToHomePage(
            String [] expectedResults)
    {
        //check if various post workout summary details are correct

        String expectedWorkoutDuration = expectedResults[0];
        onView(withId(R.id.workoutDurationTextView)).check(matches(withText(expectedWorkoutDuration)));

        String expectedDistanceTravelled = expectedResults[1];
        onView(withId(R.id.distanceTravelledTextView)).check(matches(withText(expectedDistanceTravelled)));

        String expectedNumberOfStepsTaken = expectedResults[2];
        onView(withId(R.id.numberOfStepsTakenTextView)).check(matches(withText(expectedNumberOfStepsTaken)));

        String expectedCaloriesConsumed = expectedResults[3];
        onView(withId(R.id.caloriesConsumedTextView)).check(matches(withText(expectedCaloriesConsumed)));


        //return back to the home page
        onView(withId(R.id.backToHomeImageButton)).perform(click());
    }



    /**
     * Helper Method
     *
     * On the View Past Workout Records page, check if the most recently added record is
     * displayed correct with the correct workout details
     *
     * Next click on the workout record to see if can launch the image slideshow. Close the
     * slideshow immediately
     *
     * Once done checking, delete the workout record and return to the home page
     *
     * @param expectedActivityType the expected activity type of the workout session
     *                             There are only two acceptable values:
     *                                  (a) "Walk"
     *                                  (b) "Run"
     *
     * @param expectedResults a string array with exactly 4 elements, containing the expected
     *                        values of the workout session to be checked against the
     *                        post-workout details displayed
     *
     *                        The elements are in the order of:
     *                          index [0] -> workout duration (e.g. "00:10:35")
     *                          index [1] -> distance travelled (e.g. "25.43 km")
     *                          index [2] -> number of steps taken (e.g. "34")
     *                          index [3] -> calories consumed (e.g. "99")
     * */
    private void viewPastWorkoutRecordsToCheckIfWorkoutSessionDetailsCorrect(
            String expectedActivityType, String [] expectedResults)
    {
        //click on the view past workout records button in the home page
        onView(withId(R.id.viewPastWorkoutRecordsImageButton)).perform(click());


        //check the activity type is correct
        onView(firstMatch(withId(R.id.activityTypeTextView))).check(matches(withText(expectedActivityType)));


        //check if various workout record details are correct
        String expectedWorkoutDuration = expectedResults[0];
        onView(firstMatch(withId(R.id.workoutDurationTextView))).check(matches(withText(expectedWorkoutDuration)));

        String expectedDistanceTravelled = expectedResults[1];
        onView(firstMatch(withId(R.id.distanceTravelledTextView))).check(matches(withText(expectedDistanceTravelled)));

        String expectedNumberOfStepsTaken = expectedResults[2];
        onView(firstMatch(withId(R.id.numberOfStepsTakenTextView))).check(matches(withText(expectedNumberOfStepsTaken)));

        String expectedCaloriesConsumed = expectedResults[3];
        onView(firstMatch(withId(R.id.caloriesConsumedTextView))).check(matches(withText(expectedCaloriesConsumed)));


        //click on the workout record to launch the map and image slideshow
        // then close it
        onView(firstMatch(withId(R.id.singleWorkoutRecord))).perform(click());
        onView(withId(R.id.closeButton)).perform(click());


        //delete the workout session on the pop-up menu
        onView(firstMatch(withId(R.id.tripleDotMoreImageButton))).perform(click());
        onView(withText("Delete")).perform(click());


        //click on the 'yes' button of the alert dialog to confirm deletion
        onView(withText("Yes")).perform(click());


        //go back to the home page
        onView(withId(R.id.backArrowNavigationImageButton)).perform(click());
    }




    /**
     * Returns a matcher that matches only the first view in a list of matched views
     * */
    private <T> Matcher<T> firstMatch(final Matcher<T> matcher)
    {
        return new BaseMatcher<T>() {
            boolean isFirst = true;

            @Override
            public boolean matches(final Object item) {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false;
                    return true;
                }

                return false;
            }

            @Override
            public void describeTo(final Description description) {
                //do nothing
            }
        };
    }


    /**
     * Returns a matcher returns the string a textview
     *
     * Pre-requisites: The argument must be matcher that matches only textviews
     * */
    private String getTextFromTextView(final Matcher<View> matcher)
    {
        final String[] text = new String[1];

        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                text[0] = tv.getText().toString();
            }
        });

        return text[0];
    }
}
