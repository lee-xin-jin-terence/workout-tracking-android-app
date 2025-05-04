package com.murdoch.fitnessapp.controllers.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.murdoch.fitnessapp.R;
import com.murdoch.fitnessapp.callbackinterfaces.IFragmentReadyListener;
import com.murdoch.fitnessapp.controllers.fragments.DuringWorkoutInfoFragment;
import com.murdoch.fitnessapp.controllers.fragments.DuringWorkoutMapFragment;
import com.murdoch.fitnessapp.models.implementations.GPSLocation;
import com.murdoch.fitnessapp.models.implementations.WorkoutSession;
import com.murdoch.fitnessapp.models.interfaces.IGPSLocation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * UI controller for the activity_during_workout.xml layout
 *
 * Provides an activity that allows user to track the
 * progress of the workout in the middle of the workout
 *
 * */
public class DuringWorkoutActivity extends AppCompatActivity
        implements IFragmentReadyListener
{
    private static final int REQUEST_ACCESS_LOCATION_PERMISSION_CODE = 1;
    private static final int REQUEST_ACTIVITY_RECOGNITION_PERMISSION_CODE = 2;
    private static final int REQUEST_READ_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 3;


    private DuringWorkoutMapFragment duringWorkoutMapFragment;
    private DuringWorkoutInfoFragment duringWorkoutInfoFragment;

    private boolean duringWorkoutMapFragmentIsReady = false;
    private boolean duringWorkoutInfoFragmentIsReady = false;


    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;

    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    private SensorEventListener stepCountEventListener;
    private long previousTotalStepCountSinceBoot = 0;


    private Handler repeatEverySecondHandler;
    private Runnable repeatEverySecondRunnable;


    private ActivityResultLauncher<Intent> takePhotoIntentResultLauncher;
    private String currentPhotoPath;


    private enum WorkoutSessionMode {
        PLAY, PAUSE
    }

    private enum FragmentDisplayMode {
        INFO, MAP
    }


    private WorkoutSessionMode workoutSessionMode = WorkoutSessionMode.PLAY;
    private FragmentDisplayMode fragmentDisplayMode = FragmentDisplayMode.INFO;

    private WorkoutSession workoutSession;



    /**
     * Create an intent to start the DuringWorkoutActivity
     *
     * @param packageContext context of the application package
     * @param workoutSession a WorkoutSession instance with the activity type and start date time
     *                      already set
     *
     * @throws NullPointerException if the argument 'workoutSession' is null
     * */
    public static Intent createIntent(Context packageContext, WorkoutSession workoutSession) {

        if (workoutSession == null)
        {
            throw new NullPointerException("WorkoutSession cannot be null");
        }

        final String EXTRAS_WORKOUT_SESSION_KEY = "workoutSession";

        Intent intent = new Intent(packageContext, DuringWorkoutActivity.class);

        intent.putExtra(EXTRAS_WORKOUT_SESSION_KEY, workoutSession);

        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_during_workout);


        initializeWorkoutSessionFromIntent();

        initializeAllViews();
        initializeTakePhotoIntentResultLauncher();

    }


    /**
     * Initialize the workout session instance variable that was passed to
     * this activity via an intent
     * */
    private void initializeWorkoutSessionFromIntent()
    {
        final String EXTRAS_WORKOUT_SESSION_KEY = "workoutSession";

        this.workoutSession = getIntent().getExtras().
                getParcelable(EXTRAS_WORKOUT_SESSION_KEY);
    }




    /**
     * Initialize the various views
     * */
    private void initializeAllViews()
    {
        initializeFragmentContainerView();
        initializePauseOrContinueImageButtonAndTextView();
        initializeFinishImageButton();
        initializeTakePhotoImageButton();
        initializeInfoModeOrMapModeImageButtonAndTextView();
    }


    /**
     * Initialize the fragment container view, with the this.duringWorkoutInfoFragment
     * being displayed
     *
     * At the same time initialize the following instance variables (fragments):
     *      (a) this.duringWorkoutInfoFragment
     *      (b) this.duringWorkoutMapFragment
     * */
    private void initializeFragmentContainerView() {

        this.duringWorkoutInfoFragment = DuringWorkoutInfoFragment.newInstance();

        this.duringWorkoutMapFragment = DuringWorkoutMapFragment.newInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragmentContainerView, this.duringWorkoutMapFragment);
        fragmentTransaction.add(R.id.fragmentContainerView, this.duringWorkoutInfoFragment);

        fragmentTransaction.show(this.duringWorkoutInfoFragment);

        fragmentTransaction.commit();

    }


    /**
     * Initialize the PauseOrContinue Image Button and Text View
     *
     * */
    private void initializePauseOrContinueImageButtonAndTextView()
    {
        ImageButton pauseOrContinueImageButton =  findViewById(R.id.pauseOrContinueImageButton);
        pauseOrContinueImageButton.setImageResource(R.drawable.pause_icon);

        pauseOrContinueImageButton.setOnClickListener(view ->
                DuringWorkoutActivity.this.toggleBetweenPlayModeAndPauseMode());


        TextView pauseOrContinueTextView = findViewById(R.id.pauseOrContinueTextView);
        pauseOrContinueTextView.setText(R.string.activity_during_workout_pause_text);

    }


    /**
     * Initialize the Finish workout image button
     *
     * When the button is clicked, it will direct the user to the CompleteWorkoutActivity
     * */
    private void initializeFinishImageButton() {

        ImageButton finishImageButton =  findViewById(R.id.finishImageButton);

        finishImageButton.setOnClickListener(view ->
                promptFinishWorkoutConfirmationDialog());
    }


    /**
     * Initialize the image button that allows user to take photos when it is clicked
     *
     * If the device does not have a built-in camera, disable and hide the button
     * */
    private void initializeTakePhotoImageButton()
    {
        LinearLayout takePhotoImageButtonContainer = findViewById(R.id.takePhotoImageButtonContainer);
        ImageButton takePhotoImageButton = findViewById(R.id.takePhotoImageButton);


        if (!hasBuiltInCameraInDevice())
        {
            takePhotoImageButtonContainer.setVisibility(View.GONE);
            takePhotoImageButton.setEnabled(false);
            return;
        }


        takePhotoImageButton.setOnClickListener(view ->
        {
            if (!hasReadWriteExternalStoragePermissions())
            {
                requestForReadWriteExternalStoragePermissions();
            }
            else
            {
                launchCameraToTakePhoto();
            }
        });
    }


    /**
     * Initialize the button and text view that helps user to toggle between
     * info view and map view
     * */
    private void initializeInfoModeOrMapModeImageButtonAndTextView()
    {

        TextView infoModeOrMapModeTextView = findViewById(R.id.infoModeOrMapModeTextView);

        infoModeOrMapModeTextView.setText(R.string.activity_during_workout_map_text);



        ImageButton infoModeOrMapModeImageButton = findViewById(R.id.infoModeOrMapModeImageButton);

        infoModeOrMapModeImageButton.setImageResource(R.drawable.map_icon_black_white);

        infoModeOrMapModeImageButton.setOnClickListener(view ->
                this.toggleBetweenInfoModeAndMapMode());
    }



    /**
     * Initializes the launcher that launches the built-in camera
     *
     * This is the replacement for onActivityResult method, which has since been
     * deprecated
     *
     * Note: This method must be called during the .onCreate() lifecycle, else will
     * throw IllegalStateException
     * 
     * @see AppCompatActivity#onActivityResult(int, int, Intent)
     * */
    private void initializeTakePhotoIntentResultLauncher()
    {
        this.takePhotoIntentResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == RESULT_OK)
                    {
                        DuringWorkoutActivity.this.handleOnCameraActivityCompleted();
                    }
                });
    }



    /**
     * This is a custom callback method which is fired when a fragment attached is fully ready.
     *
     * When both this.duringWorkoutInfoFragment and this.duringWorkoutMapFragment
     * are ready, initialize the location and step count sensors
     *
     * Note: This method is necessary to ensure that the app does not crash when
     * by only allowing the activity to call the methods of fragments when the fragments are ready
     *
     * @param fragment the fragment that is attached
     *
     * @see IFragmentReadyListener
     * */
    @Override
    public void onFragmentReady(Fragment fragment)
    {
        if (fragment == this.duringWorkoutInfoFragment)
        {
            this.duringWorkoutInfoFragmentIsReady = true;
        }
        else if (fragment == this.duringWorkoutMapFragment)
        {
            this.duringWorkoutMapFragmentIsReady = true;
        }

        if (this.duringWorkoutInfoFragmentIsReady &&
                this.duringWorkoutMapFragmentIsReady)
        {
            this.initializeLocationSensorAndCallbacks();
            this.initializeStepCountSensor();
            this.initializeRepeatEverySecondHandlerAndRunnable();
        }
    }


    /**
     * Initialize the location sensor such as the it will provide location updates every
     * few seconds
     * */
    private void initializeLocationSensorAndCallbacks()
    {
        if (!hasAccessLocationPermissions())
        {
            requestForAccessLocationPermissions();
            return;
        }

        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest locationRequest = this.createLocationRequest();

        this.locationCallback = this.createLocationCallback();

        final Looper RUN_ON_CURRENT_THREAD = null;

        this.fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                this.locationCallback, RUN_ON_CURRENT_THREAD);

    }


    /**
     * Check whether the app has access to location permissions
     *
     * @return true is has location permissions, and false if otherwise
     * */
    private boolean hasAccessLocationPermissions()
    {
        return (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }


    /**
     * Request the app to have the ability to access location permissions, if the
     * location permissions have not been granted yet
     * */
    private void requestForAccessLocationPermissions()
    {

        String[] permissions = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        ActivityCompat.requestPermissions(this, permissions,
                DuringWorkoutActivity.REQUEST_ACCESS_LOCATION_PERMISSION_CODE);

    }


    /**
     * Create a location request that is made to the device every 5 seconds
     *
     * @return a location request (part of FusedLocationProviderClient API
     * */
    private LocationRequest createLocationRequest()
    {
        final long FIVE_SECONDS_IN_MILLISECONDS = 5000;

        LocationRequest locationRequest = LocationRequest.create();

        locationRequest.setInterval(FIVE_SECONDS_IN_MILLISECONDS);

        locationRequest.setFastestInterval(FIVE_SECONDS_IN_MILLISECONDS);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return locationRequest;
    }


    /**
     * Define and return a callback method that performs the following actions
     * whenever a location update is received
     *      (a) Add a gpslocation to the this.workoutSession instance
     *      (b) Update the this.duringWorkoutMapFragment map by adding a new map marker
     *
     * @return a location request (part of FusedLocationProviderClient API
     * */
    private LocationCallback createLocationCallback() {

        return new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location lastLocation = locationResult.getLastLocation();

                GPSLocation gpsLocation = new GPSLocation(lastLocation.getLatitude(),
                        lastLocation.getLongitude());

                DuringWorkoutActivity.this.workoutSession.addGPSLocation(gpsLocation);

                DuringWorkoutActivity.this.duringWorkoutMapFragment.addMapMarker(gpsLocation);

            }
        };
    }


    /**
     * Initialize the step count sensor
     *
     * If no sensor is found within the device, display an error message
     * */
    private void initializeStepCountSensor()
    {
        if (!hasActivityRecognitionPermission())
        {
            requestForActivityRecognitionPermission();
            return;
        }

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (this.stepCountSensor == null)
        {
            Toast.makeText(getApplicationContext(), "No step count sensor found in device",
                    Toast.LENGTH_LONG).show();
            return;
        }

        this.stepCountEventListener = getStepCountEventListener();

        this.sensorManager.registerListener(this.stepCountEventListener,
                this.stepCountSensor, SensorManager.SENSOR_DELAY_FASTEST);

    }


    /**
     * Define and return the event listener each time a step count
     * update is received
     * */
    private SensorEventListener getStepCountEventListener()
    {
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                updateNumberOfStepsToWorkoutSession(sensorEvent);
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //an abstract method that must be overridden
                //do nothing
            }
        };
    }


    /**
     * Check whether the app has access to activity recognition permission
     *
     * @return true is has activity recognition permission, and false if otherwise
     * */
    private boolean hasActivityRecognitionPermission()
    {
        return ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * Request the app to have the ability to access activity recognition (to detect step
     * count), if the activity recognition permission have not been granted yet
     * */
    private void requestForActivityRecognitionPermission()
    {

        String [] permissions = new String[]{
                Manifest.permission.ACTIVITY_RECOGNITION
        };

        ActivityCompat.requestPermissions(this, permissions,
                DuringWorkoutActivity.REQUEST_ACTIVITY_RECOGNITION_PERMISSION_CODE);

    }


    /**
     * Update the number of steps that the user has made to the
     * this.workoutSession instance
     *
     * @param sensorEvent the event that is fired each time a step is
     *                    detected by the device
     * */
    private void updateNumberOfStepsToWorkoutSession(SensorEvent sensorEvent)
    {
        long totalStepCountSinceBoot = (long) sensorEvent.values[0];


        if (this.previousTotalStepCountSinceBoot == 0)
        {
            this.previousTotalStepCountSinceBoot = totalStepCountSinceBoot;
        }

        long currentNumberOfStepsTaken = totalStepCountSinceBoot -
                this.previousTotalStepCountSinceBoot;


        this.workoutSession.addNumberOfStepsTaken((int) currentNumberOfStepsTaken);

        this.previousTotalStepCountSinceBoot = totalStepCountSinceBoot;
    }



    /**
     * Initialize the handler which updates the UI every second
     *
     * */
    private void initializeRepeatEverySecondHandlerAndRunnable()
    {
        final int ONE_SECOND_IN_SECONDS = 1;
        final int ONE_SECOND_IN_MILLISECONDS = 1000;

        this.repeatEverySecondRunnable = () -> runOnUiThread(() -> {

            DuringWorkoutActivity.this.workoutSession.addDurationInSeconds(ONE_SECOND_IN_SECONDS);
            DuringWorkoutActivity.this.workoutSession.calculateCaloriesConsumed();
            DuringWorkoutActivity.this.workoutSession.calculateDistanceTravelled();

            DuringWorkoutActivity.this.repeatEverySecondHandler.postDelayed(
                    DuringWorkoutActivity.this.repeatEverySecondRunnable,
                    ONE_SECOND_IN_MILLISECONDS);

            DuringWorkoutActivity.this.duringWorkoutInfoFragment.displayWorkoutSessionDetails(
                    DuringWorkoutActivity.this.workoutSession);
        });

        this.repeatEverySecondHandler = new Handler();
        this.repeatEverySecondHandler.postDelayed(this.repeatEverySecondRunnable,
                ONE_SECOND_IN_MILLISECONDS);

    }


    /**
     * Checks whether the device has a built-in camera
     *
     * @return true if have a built-in camera, and false if otherwise
     * */
    private boolean hasBuiltInCameraInDevice()
    {
        PackageManager packageManager = getPackageManager();

        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }



    /**
     * Checks if the device has permissions to read and write from/to external storage
     *
     * @return true if it has read/write external storage permissions, and false if
     * otherwise
     * */
    private boolean hasReadWriteExternalStoragePermissions()
    {
        return (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                                &&
                ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }



    /**
     * Request the app to have the permissions to read/write from/to external storage
     * */
    private void requestForReadWriteExternalStoragePermissions()
    {

        String [] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };


        ActivityCompat.requestPermissions(this, permissions,
                REQUEST_READ_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
    }


    /**
     * Launches the built-in camera to take a photo
     * */
    private void launchCameraToTakePhoto()
    {
        Intent takePhotoIntent;

        try {
            takePhotoIntent = createTakePhotoIntent();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Failed to create a file to store" +
                    " photo. " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }


        // Ensure that there's a camera activity to handle the intent
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null)
        {
            this.takePhotoIntentResultLauncher.launch(takePhotoIntent);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No suitable application " +
                    "to access the camera", Toast.LENGTH_LONG).show();
        }

    }


    /**
     * Create an intent to launch the camera to take a photo
     *
     * @throws IOException if it fails to create an in-memory
     * File to store the photo before launching the camera
     * */
    private Intent createTakePhotoIntent() throws IOException
    {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = createImageFile();

        final String FILE_PROVIDER_AUTHORITY = "com.murdoch.fitnessapp.fileprovider";

        Uri currentPhotoUri = FileProvider.getUriForFile(getApplicationContext(),
                FILE_PROVIDER_AUTHORITY, photoFile);

        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);

        return takePhotoIntent;
    }





    /**
     * Create an in-memory File with a unique  collision-resistant file name,
     * to be used for saving images to the external storage later
     *
     * Also sets the this.currentPhotoPath variable to the path of the created
     * in-memory file
     *
     * @return in-memory file with unique file name (directory pointing to the app's
     * designated directory)
     * */
    private File createImageFile() throws IOException {
        String timeStamp =  new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).
                                    format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        this.currentPhotoPath = image.getAbsolutePath();

        return image;
    }


    /**
     * Adds the path of the image taken by the camera to the this.workoutSession
     * instance
     *
     * The method that is called whenever the launched camera has completed
     * */
    private void handleOnCameraActivityCompleted()
    {
        this.workoutSession.addImageTaken(this.currentPhotoPath);

        Toast.makeText(getApplicationContext(), "Image saved to device",
                Toast.LENGTH_LONG).show();
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

        switch (requestCode)
        {
            case REQUEST_ACCESS_LOCATION_PERMISSION_CODE:
                handleRequestForAccessLocationPermissionsResults(grantResults);
                break;

            case REQUEST_ACTIVITY_RECOGNITION_PERMISSION_CODE:
                handleRequestForActivityRecognitionPermissionResult(grantResults);
                break;

            case REQUEST_READ_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE:
                handleRequestForReadWriteExternalStoragePermissionsResult(grantResults);
                break;
        }

    }


    /**
     * Handles the results of permission requests for accessing location.
     * Only be called from the method onRequestPermissionsResult(int, String[], int[])
     *
     * If appropriate location access permissions are granted, then will initialize
     * location sensors and callbacks. Otherwise, display an error message (toast)
     *
     * @param grantResults results of permissions granted
     *
     * */
    private void handleRequestForAccessLocationPermissionsResults(int[] grantResults)
    {
        if (grantResults.length == 2 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED)
        {

            initializeLocationSensorAndCallbacks();

        } else
        {
            Toast.makeText(getApplicationContext(), "Failed to obtain permissions " +
                    "for accessing location", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Handles the results of permission requests for accessing activity recognition.
     * Only be called from the method onRequestPermissionsResult(int, String[], int[])
     *
     * If appropriate activity sensor permissions are granted, then will initialize
     * step count sensors and callbacks. Otherwise, display an error message (toast)
     *
     * @param grantResults results of permissions granted
     *
     * */
    private void handleRequestForActivityRecognitionPermissionResult(int[] grantResults)
    {
        if (grantResults.length == 1 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            this.initializeStepCountSensor();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Failed to obtain permissions " +
                    "for activity recognition (step counting)", Toast.LENGTH_LONG).show();
        }
    }



    /**
     * Handles the results of permission requests for accessing read and write storage
     * permissions.
     * Only be called from the method onRequestPermissionsResult(int, String[], int[])
     *
     * If appropriate read external storage  permissions are granted, then will launch
     * camera to take a photo. Otherwise, display an error message (toast)
     *
     * @param grantResults results of permissions granted
     *
     * */
    private void handleRequestForReadWriteExternalStoragePermissionsResult(int [] grantResults)
    {

        if (grantResults.length == 2 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED)
        {
            launchCameraToTakePhoto();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Failed to obtain permissions " +
                    "for external storage read/write and camera", Toast.LENGTH_LONG)
                    .show();
        }
    }



    /**
     * Toggles between PLAY mode and PAUSE Mode. Upon toggling,
     * updates the following
     *      (a) The this.workoutSessionMode flag
     *      (b) Remove/ Restore Handlers and Callbacks
     *      (c) updates the UI to change the image/text of
     *      PauseOrContinue ImageButton and TextView
     * */
    private void toggleBetweenPlayModeAndPauseMode()
    {
        if (this.workoutSessionMode == WorkoutSessionMode.PLAY)
        {
            this.workoutSessionMode = WorkoutSessionMode.PAUSE;
            removeHandlerLocationAndSensorCallbacks();

        } else {
            this.workoutSessionMode = WorkoutSessionMode.PLAY;
            restoreHandlerLocationAndSensorCallbacks();
        }

        updateContinueOrPauseImageButtonAndTextView();
    }


    /**
     * Remove the various callbacks (upon pausing of the workout session)
     *
     *  (a) this.repeatEverySecondHandler (timing + UI updates)
     *  (b) this.fusedLocationProviderClient (for receiving location updates)
     *  (c) this.sensorManager (for receiving step count updates)
     * */
    private void removeHandlerLocationAndSensorCallbacks()
    {

        this.repeatEverySecondHandler.removeCallbacks(this.repeatEverySecondRunnable);


        // only remove location service callback if GPSLocation service is available and
        // been granted access
        if (this.fusedLocationProviderClient != null)
        {
            this.fusedLocationProviderClient.removeLocationUpdates(this.locationCallback);
            this.previousTotalStepCountSinceBoot = 0;

            final IGPSLocation EMPTY_MARKER = null;

            //so that will not take previous marker and next marker into distance calculation
            this.workoutSession.addGPSLocation(EMPTY_MARKER);
        }


        //only remove step count callback if step count sensor is available
        // and been granted access
        if (this.stepCountSensor != null)
        {
            this.sensorManager.unregisterListener(this.stepCountEventListener, this.stepCountSensor);
        }
    }


    /**
     * Restore the following callbacks (upon continuing of workout session)
     *
     * (a) this.repeatEverySecondHandler (timing + UI updates)
     * (b) this.fusedLocationProviderClient (for receiving location updates)
     * (c) this.sensorManager (for receiving step count updates)
     *
     * */
    private void restoreHandlerLocationAndSensorCallbacks()
    {
        final int ONE_SECOND_IN_MILLISECONDS = 1000;

        this.repeatEverySecondHandler.postDelayed(this.repeatEverySecondRunnable,
                ONE_SECOND_IN_MILLISECONDS);


        if (this.fusedLocationProviderClient != null)
        {
            //callbacks and requests must be re-created in order to work
            this.locationCallback = createLocationCallback();
            LocationRequest locationRequest = createLocationRequest();

            final Looper RUN_ON_CURRENT_THREAD = null;

            this.fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                    this.locationCallback, RUN_ON_CURRENT_THREAD);
        }

        if (this.stepCountSensor != null)
        {
            this.sensorManager.registerListener(this.stepCountEventListener, this.stepCountSensor,
                    SensorManager.SENSOR_DELAY_FASTEST);
        }
    }


    /**
     * Update the ContinueOrPause ImageButton and TextView to reflect the changes in
     * mode of the app. Call this method only after the this.workoutSessionMode
     * flag has been updated
     *
     * If is PLAY mode-> show the "Pause" icon + "Pause" label text
     * If is PAUSE mode-> show the "Play" icon + "Continue" label text
     * */
    private void updateContinueOrPauseImageButtonAndTextView()
    {
        ImageButton pauseOrContinueImageButton = findViewById(R.id.pauseOrContinueImageButton);
        TextView pauseOrContinueTextView = findViewById(R.id.pauseOrContinueTextView);


        if (this.workoutSessionMode == WorkoutSessionMode.PLAY) {

            pauseOrContinueImageButton.setImageResource(R.drawable.pause_icon);

            pauseOrContinueTextView.setText(R.string.activity_during_workout_pause_text);

        } else {

            pauseOrContinueImageButton.setImageResource(R.drawable.play_icon);

            pauseOrContinueTextView.setText(R.string.activity_during_workout_continue_text);

        }
    }


    /**
     * Prompt the user whether he/she truly wants to finish the workout session.
     *
     * If yes, then move on to start the CompleteWorkoutActivity
     * */
    private void promptFinishWorkoutConfirmationDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(R.string.activity_during_workout_prompt_finish_title_text);

        alertDialogBuilder.setMessage(
                    R.string.activity_during_workout_prompt_finish_confirmation_text);

        alertDialogBuilder.setNegativeButton(R.string.activity_during_workout_no_text,
                (dialogInterface, buttonId) -> { /*do nothing*/ });


        alertDialogBuilder.setPositiveButton(R.string.activity_during_workout_yes_text,
                (dialogInterface, buttonId) -> {

                    Intent intent = CompleteWorkoutActivity.createIntent(getApplicationContext(),
                            this.workoutSession);
                    startActivity(intent);
                });

        alertDialogBuilder.show();
    }


    /**
     * Toggles between INFO mode and MAP Mode. Upon toggling,
     * updates the following
     *      (a) The this.fragmentDisplayMode flag
     *      (b) updates the UI to change the image/text of
     *      infoModeOrMapMode ImageButton and TextView
     *      (c) updates the fragment container view to show
     *       the appropriate fragment
     * */
    private void toggleBetweenInfoModeAndMapMode()
    {
        if (this.fragmentDisplayMode == FragmentDisplayMode.INFO)
        {
            this.fragmentDisplayMode = FragmentDisplayMode.MAP;
        }
        else
        {
            this.fragmentDisplayMode = FragmentDisplayMode.INFO;
        }

        updateInfoModeOrMapModeImageButtonAndTextView();
        updateFragmentContainerView();
    }


    /**
     * Update the InfoModeOrMapMode ImageButton and TextView to reflect the changes in
     * mode of the app. Call this method only after the this.fragmentDisplayMode
     * flag has been updated
     *
     * If is INFO mode-> show the "map" icon + "map" label text
     * If is MAP mode-> show the "info" icon + "info" label text
     * */
    private void updateInfoModeOrMapModeImageButtonAndTextView()
    {
        ImageButton infoModeOrMapModeImageButton = findViewById(R.id.infoModeOrMapModeImageButton);

        TextView infoModeOrMapModeTextView = findViewById(R.id.infoModeOrMapModeTextView);

        if (this.fragmentDisplayMode == FragmentDisplayMode.INFO)
        {
            infoModeOrMapModeImageButton.setImageResource(R.drawable.map_icon_black_white);

            infoModeOrMapModeTextView.setText(R.string.activity_during_workout_map_text);
        }
        else
        {
            infoModeOrMapModeImageButton.setImageResource(R.drawable.info_icon);

            infoModeOrMapModeTextView.setText(R.string.activity_during_workout_info_text);
        }
    }


    /**
     * Update the fragment container view to show the appropriate fragment.
     * Call this method only after the this.fragmentDisplayMode flag has been updated
     *
     * If is INFO mode-> show the this.duringWorkoutInfoFragment
     * If is MAP mode-> show the this.duringWorkoutMapFragment
     * */
    private void updateFragmentContainerView()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //only works when both .show() and .hide() methods are called
        if (fragmentDisplayMode == FragmentDisplayMode.INFO)
        {
            fragmentTransaction.show(this.duringWorkoutInfoFragment);
            fragmentTransaction.hide(this.duringWorkoutMapFragment);
        }
        else
        {
            fragmentTransaction.show(this.duringWorkoutMapFragment);
            fragmentTransaction.hide(this.duringWorkoutInfoFragment);
        }

        fragmentTransaction.commit();
    }

}