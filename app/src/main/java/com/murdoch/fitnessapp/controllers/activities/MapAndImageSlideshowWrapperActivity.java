package com.murdoch.fitnessapp.controllers.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.murdoch.fitnessapp.R;
import com.murdoch.fitnessapp.controllers.fragments.MapAndImageSlideshowFragment;
import com.murdoch.fitnessapp.models.implementations.WorkoutSession;


/**
 * A class that acts as the wrapper for the fragment MapAndImageSlideshowFragment
 *
 * It contains two extra additions:
 *      (a) a "close" button that allows the user to close the activity
 *      (b) requests for "READ_EXTERNAL_STORAGE" permissions on behalf of the
 *              MapAndImageSlideshowFragment, as the fragment needs this
 *              permission
 * */
public class MapAndImageSlideshowWrapperActivity extends AppCompatActivity
{
    private static final String EXTRAS_WORKOUT_SESSION_KEY = "workoutSession";

    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION_CODE = 1;

    private WorkoutSession workoutSession;


    /**
     * Create an intent to start the MapAndImageSlideshowWrapperActivity
     *
     * @param packageContext context of the application package
     * @param workoutSession a WorkoutSession instance with all the various details regarding
     *                       the workout session stored within the instance
     *
     * @throws NullPointerException if the argument 'workoutSession' is null
     * */
    public static Intent createIntent(Context packageContext, WorkoutSession workoutSession)
    {
        Intent intent = new Intent(packageContext, MapAndImageSlideshowWrapperActivity.class);

        intent.putExtra(EXTRAS_WORKOUT_SESSION_KEY, workoutSession);

        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map_and_image_slideshow_wrapper);

        initializeWorkoutSessionFromIntent();

        initializeAllViews();
    }


    /**
     * Initialize the workout session instance variable that was passed to
     * this activity via an intent
     * */
    private void initializeWorkoutSessionFromIntent()
    {

        this.workoutSession = getIntent().getExtras().getParcelable(EXTRAS_WORKOUT_SESSION_KEY);
    }


    /**
     * Initialize all the various views within the activity
     *
     * Note: The this.workoutSession instance variable should be initialized
     * before calling this method
     * */
    private void initializeAllViews()
    {
        initializeCloseButton();
        initializeMapAndImageSlideshowFragmentContainerView();
    }




    private void initializeMapAndImageSlideshowFragmentContainerView()
    {
        if (!hasReadExternalStoragePermission())
        {
            requestForReadExternalStoragePermission();

            return;
        }

        MapAndImageSlideshowFragment mapAndImageSlideshowFragment =
                                    MapAndImageSlideshowFragment.newInstance(this.workoutSession);

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.mapAndImageSlideshowFragmentContainerView,
                                    mapAndImageSlideshowFragment);

        fragmentTransaction.commit();
    }


    /**
     * Initialize the close button to close the activity upon the button being clicked
     * */
    private void initializeCloseButton()
    {
        ImageButton closeButton = findViewById(R.id.closeButton);

        closeButton.setOnClickListener(view -> finish());
    }



    /**
     * Checks if the device has permissions to read from external storage
     *
     * @return true if it has read external storage permissions, and false if
     * otherwise
     * */
    private boolean hasReadExternalStoragePermission()
    {
        return ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * Request the app to have the permissions to read from external storage
     * */
    private void requestForReadExternalStoragePermission()
    {
        String [] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        ActivityCompat.requestPermissions(this, permissions,
                REQUEST_READ_EXTERNAL_STORAGE_PERMISSION_CODE);
    }



    /**
     * Determine what is to be done after user grants various permissions to the app
     *
     * The callback (defined by Android) that is called whenever user grants/
     * rejects permissions requested by the app
     **/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE_PERMISSION_CODE)
        {
            handleRequestForReadExternalStoragePermissionResults(grantResults);
        }
    }


    /**
     * Handles the results of permission requests for accessing read  storage
     * permissions.
     * Only be called from the method onRequestPermissionsResult(int, String[], int[])
     *
     * If appropriate location access permissions are granted, then will initialize
     * fragment to display the map and image slideshow. Otherwise, display an error
     * message (toast)
     * and finish() the activity
     *
     * @param grantResults results of permissions granted
     *
     * */
    private void handleRequestForReadExternalStoragePermissionResults(int [] grantResults)
    {
        if (grantResults.length == 1 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            initializeMapAndImageSlideshowFragmentContainerView();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Cannot launch slideshow. Failed to "
                    + "obtain permission to read external storage", Toast.LENGTH_LONG).show();

            finish();
        }
    }
}