package com.murdoch.fitnessapp.controllers.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.murdoch.fitnessapp.R;
import com.murdoch.fitnessapp.models.implementations.WorkoutSession;
import com.murdoch.fitnessapp.models.interfaces.IGPSLocation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * A fragment that displays slideshow containing a map where the user has travelled during
 * the workout, along with images taken during the workout
 *
 * The user can go to next/previous slides by clicking on the left/right buttons
 *
 * Note: This fragment assumes that it has READ_EXTERNAL_STORAGE permission. The
 *        calling activity should check and request for this permission
 *        before using this fragment, if the workout session has images involved.
 *        If no images are taken during the workout session, then
 *        READ_EXTERNAL_STORAGE permission is not required.
 *
 * Use the {@link MapAndImageSlideshowFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */

public class MapAndImageSlideshowFragment extends Fragment
{
    private WorkoutSession workoutSession;

    private int currentSlideIndex = 0;
    private int totalNumberOfSlides;
    private boolean isMapShownInSlideshow;
    private int numberOfImages;


    private ImageButton leftArrowButton;
    private ImageButton rightArrowButton;

    private MapView mapView;
    private ImageView imageView;

    private GoogleMap googleMap;
    private List<Bitmap> listOfBitmaps;


    public MapAndImageSlideshowFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment instead of the constructor
     *
     * Note: This fragment assumes that it has READ_EXTERNAL_STORAGE permissions. The
     *            calling activity should check and request for this permission
     *           before using this fragment, if the workout session has images involved.
     *          If no images are taken during the workout session, then
     *          READ_EXTERNAL_STORAGE permission is not required.
     *
     * @param workoutSession the workout session containing all the details regarding the
     *                       completed workout session
     *
     * @return A new instance of fragment MapAndImageSlideshowFragment.
     */
    public static MapAndImageSlideshowFragment newInstance(WorkoutSession workoutSession)
    {
        if (workoutSession == null)
        {
            throw new NullPointerException("WorkoutSession cannot be null");
        }

        final String WORKOUT_SESSION_BUNDLE_KEY = "workoutSession";


        MapAndImageSlideshowFragment fragment = new MapAndImageSlideshowFragment();
        Bundle args = new Bundle();

        args.putParcelable(WORKOUT_SESSION_BUNDLE_KEY, workoutSession);
        fragment.setArguments(args);

        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initializeWorkoutSessionFromArguments();

    }


    /**
     * Initialize the instance variable this.workoutSession from the
     * arguments of this fragment
     * */
    private void initializeWorkoutSessionFromArguments()
    {
        final String WORKOUT_SESSION_BUNDLE_KEY = "workoutSession";

        this.workoutSession = getArguments().getParcelable(WORKOUT_SESSION_BUNDLE_KEY);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_and_image_slideshow, container, false);
    }




    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        initializeListOfBitmaps();
        initializeNumberOfImages_IsMapShown_TotalNumberOfSlides();
        initializeAllViews(savedInstanceState);

    }


    /**
     * Initialize the list of bitmaps (images files) from the list of images taken during the workout
     * (converting image paths to bitmaps)
     *
     * Output: the this.listOfBitMaps will contain the list of bitmaps
     * */
    private void initializeListOfBitmaps()
    {
        this.listOfBitmaps = new ArrayList<>();

        for(String imagePath: this.workoutSession.getListOfImagesTaken())
        {

            Bitmap bitmap = BitmapFactory.decodeFile(new File(imagePath).getAbsolutePath());

            if (bitmap != null)
            {
                //if image is present
                this.listOfBitmaps.add(bitmap);
            }

        }
    }


    /**
     * Initialize the following instance variables
     *  (a) this.numberOfImages --> number of images to be displayed
     *
     *  (b) this.isMapShownInSlideshow --> whether the map will be shown in slideshow or not
     *      (if there are no GPSLocations [coordinates] in the list of gpslocations,
     *      then it will be the only reason that map will not be shown, otherwise map
     *      is always shown)
     *
     *  (c) this.totalNumberOfSlides --> the total number of slides, which is
     *          the sum of this.numberOfImages + 1 if map is shown, or
     *          equals to this.numberOfImages if map is not shown
     *
     * Note: This method must be called after initializeListOfBitMaps
     * */
    private void initializeNumberOfImages_IsMapShown_TotalNumberOfSlides()
    {
        int numberOfNonNullMapCoordinates = getNumberOfNonNullGPSLocations();

        this.isMapShownInSlideshow = (numberOfNonNullMapCoordinates != 0);

        this.numberOfImages = this.listOfBitmaps.size();

        if (this.isMapShownInSlideshow)
        {
            this.totalNumberOfSlides = this.numberOfImages + 1;
        }
        else
        {
            this.totalNumberOfSlides = this.numberOfImages;
        }
    }



    /**
     * Count and return the number of non-null gps locations in workout session
     *
     * Note: GPSLocations may contain null-references to indicate a pause/break
     * during the workout session, so as to not take distance calculation
     * into consideration during the pause
     * */
    private int getNumberOfNonNullGPSLocations()
    {
        int numberOfNonNullMapMarkers = 0;

        for (IGPSLocation gpsLocation: this.workoutSession.getListOfGPSLocations())
        {
            if (gpsLocation != null)
            {
                numberOfNonNullMapMarkers++;
            }
        }

        return numberOfNonNullMapMarkers;
    }


    /**
     * Initialize all the various views within the fragment
     *
     * @param savedInstanceState  if the activity is being re-initialized after previously
     *                            being shut down then this Bundle contains the data it most
     *                            recently supplied in
     * */
    private void initializeAllViews(Bundle savedInstanceState)
    {

        this.initializeLeftArrowButton();
        this.initializeRightArrowButton();

        this.initializeNoMapOrImageToDisplayMessageTextView();
        this.initializeImageView();
        this.initializeMapView(savedInstanceState);

    }



    /**
     * Initialize the left arrow button which will proceed to the previous slide
     * when clicked
     * */
    private void initializeLeftArrowButton()
    {
        this.leftArrowButton = getView().findViewById(R.id.leftArrowButton);

        this.leftArrowButton.setOnClickListener(
                view -> MapAndImageSlideshowFragment.this.moveToPreviousSlide());


        // the initial slide to be displayed is the first slide
        // View.GONE because cannot go to previous slide from the first slide
        this.leftArrowButton.setVisibility(View.GONE);
    }


    /**
     * Initialize the right arrow button which will proceed to the next slide
     * when clicked
     * */
    private void initializeRightArrowButton()
    {
        this.rightArrowButton = getView().findViewById(R.id.rightArrowButton);

        this.rightArrowButton.setOnClickListener(
                view -> MapAndImageSlideshowFragment.this.moveToNextSlide());


        // Only show the right arrow button if there are more than one slide
        // otherwise does not make sense to show the right arrow button
        if (this.totalNumberOfSlides <= 1)
        {
            this.rightArrowButton.setVisibility(View.GONE);
        }

    }


    /**
     * Change the slideshow display by going to the previous slide
     * */
    private void moveToPreviousSlide()
    {
        this.currentSlideIndex--;

        updateLeftRightArrowButtons();
        updateMapViewAndImageView();
    }


    /**
     * Change the slideshow display by going to the next slide
     * */
    private void moveToNextSlide()
    {
        this.currentSlideIndex++;

        updateLeftRightArrowButtons();
        updateMapViewAndImageView();

    }


    /**
     * Update the slide display after the this.currentSlideIndex instance variable
     * is updated
     *
     * The first slide is always the map (unless the map is not shown because there
     * are no coordinates available), and then in this case the first slide
     * will be images
     *
     * The second to last slides will always be images (depending on the number of images)
     *
     *
     * Note: This method assumes that the this.totalNumberOfSlides >= 1
     *
     * */
    private void updateMapViewAndImageView()
    {
        if (this.currentSlideIndex == 0 && this.isMapShownInSlideshow)
        {
            this.mapView.setVisibility(View.VISIBLE);
            this.imageView.setVisibility(View.GONE);
        }
        else if (this.currentSlideIndex == 0 && this.numberOfImages >= 1)
        {
            this.mapView.setVisibility(View.GONE);
            this.imageView.setVisibility(View.VISIBLE);
            this.imageView.setImageBitmap(this.listOfBitmaps.get(this.currentSlideIndex));
        }
        else if (this.currentSlideIndex > 0 && this.isMapShownInSlideshow && this.numberOfImages >= 1)
        {
            this.mapView.setVisibility(View.GONE);
            this.imageView.setVisibility(View.VISIBLE);
            this.imageView.setImageBitmap(this.listOfBitmaps.get(this.currentSlideIndex - 1));
        }

    }


    /**
     * Update the left (next) and right (previous) arrow button depending on the instance variable
     * this.currentSlideIndex
     *
     * The left and right arrows are shown based on the following rules:
     *      (a) The first slide will never show the left arrow button
     *      (b) The first slide will only show the right arrow button
     *              if there is more than one slide
     *      (c) The last slide will never show the right arrow button
     *      (d) The slides between the first and the last will show both
     *              the left and right arrow button
     *
     *
     * Note: This method assumes that the this.totalNumberOfSlides >= 1
     * */
    private void updateLeftRightArrowButtons()
    {

        if (this.currentSlideIndex == 0 && this.totalNumberOfSlides == 1)
        {
            this.leftArrowButton.setVisibility(View.GONE);
            this.rightArrowButton.setVisibility(View.GONE);

        }
        if (this.currentSlideIndex == 0 && this.totalNumberOfSlides > 1)
        {

            this.leftArrowButton.setVisibility(View.GONE);
            this.rightArrowButton.setVisibility(View.VISIBLE);

        }
        else if (this.currentSlideIndex > 0 &&
                 this.currentSlideIndex < (this.totalNumberOfSlides -1) )
        {

            this.leftArrowButton.setVisibility(View.VISIBLE);
            this.rightArrowButton.setVisibility(View.VISIBLE);
        }
        else
        {
            // means (this.currentSlideIndex > 0 &&
            //          this.currentSlideIndex == this.totalNumberOfSlides -1)

            this.leftArrowButton.setVisibility(View.VISIBLE);
            this.rightArrowButton.setVisibility(View.GONE);
        }
    }


    private void initializeNoMapOrImageToDisplayMessageTextView()
    {
        TextView noMapOrImageToShowTextView = getView().
                findViewById(R.id.noMapOrImageToDisplayMessageTextView);

        if (this.totalNumberOfSlides == 0)
        {
            noMapOrImageToShowTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            noMapOrImageToShowTextView.setVisibility(View.GONE);
        }
    }


    private void initializeImageView()
    {
        this.imageView = getView().findViewById(R.id.imageView);

        if (this.numberOfImages == 0)
        {
            this.imageView.setVisibility(View.GONE);
        }
        else
        {
            this.imageView.setImageBitmap(this.listOfBitmaps.get(0));
        }

    }


    /**
     * Initialize the map view
     *
     * @param savedInstanceState   if the activity is being re-initialized after previously being
     *                             shut down then this Bundle contains the data it most recently
     *                             supplied in
     *
     * Note: As stated by Google Map Android documentation, the fragment/activity
     *  must forward the onStart() lifecycle method to mapView.onStart()
     * */
    private void initializeMapView(Bundle savedInstanceState)
    {
        this.mapView = getView().findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);

        if (!this.isMapShownInSlideshow)
        {
            this.mapView.setVisibility(View.GONE);
            return;
        }

        this.mapView.getMapAsync(googleMap -> {

            MapAndImageSlideshowFragment.this.googleMap = googleMap;

            googleMap.setOnMapLoadedCallback(
                    MapAndImageSlideshowFragment.this::plotAllMarkersOnMap);

        });
    }


    /**
     * Plots all the markers on the map, with all markers visible within the mapView
     * */
    private void plotAllMarkersOnMap()
    {
        List <LatLng> listOfLatLng = convertListOfGPSLocationsToListOfLatLng();

        if (listOfLatLng.isEmpty())
        {
            return;
        }

        List<MarkerOptions> listOfMarkerOptions = convertListOfLatLngToListOfMarkerOptions(listOfLatLng);

        List<Marker> listOfPlottedMarkers = plotMarkersOnMapAndGetListOfMarkers(listOfMarkerOptions);

        LatLngBounds latLngBounds = getLatLngBoundsOfPlottedMarkers(listOfPlottedMarkers);

        adjustMapZoomWithAllMarkersVisibleToUser(latLngBounds);
    }


    /**
     * Convert a list of GPSLocations to list of LatLng (Google Play's API representation
     * of location information)
     *
     * Filters the list to remove any null references in the list of GPSLocations
     * */
    private List<LatLng> convertListOfGPSLocationsToListOfLatLng()
    {
        List<IGPSLocation> listOfGPSLocations = this.workoutSession.getListOfGPSLocations();

        ArrayList <LatLng> listOfLatLng = new ArrayList<>();

        for (IGPSLocation gpsLocation: listOfGPSLocations)
        {
            if (gpsLocation != null) {

                LatLng latLng = convertGPSLocationToLatLng(gpsLocation);

                listOfLatLng.add(latLng);
            }
        }

        return listOfLatLng;
    }


    /**
     * Converts GPSLocation to Google Maps's LatLng
     *
     * @param gpsLocation geo-location data
     *
     * @return a corresponding LatLng instance also containing geo-location data
     * */
    private static LatLng convertGPSLocationToLatLng(IGPSLocation gpsLocation)
    {
        return new LatLng(gpsLocation.getLatitude(), gpsLocation.getLongitude());
    }



    /**
     * Converts a list of latLng to marker options. MarkerOptions contains information for
     * plotting markers on the map
     *
     * @param listOfLatLng list of positions to be plotted onto the map
     * */
    private List<MarkerOptions> convertListOfLatLngToListOfMarkerOptions(List<LatLng> listOfLatLng)
    {
        List<MarkerOptions> listOfMarkerOptions = new ArrayList<>();


        for(int index=0; index < listOfLatLng.size(); index++)
        {
            LatLng currentLatLng = listOfLatLng.get(index);

            MarkerOptions markerOptions;

            if (index == 0) {

                markerOptions = getMarkerOptions(currentLatLng, "STARTING_POINT");

            } else if (index == listOfLatLng.size() - 1) {

                markerOptions = getMarkerOptions(currentLatLng, "FINISHING_POINT");
            }
            else {
                markerOptions = getMarkerOptions(currentLatLng, "DEFAULT");
            }

            listOfMarkerOptions.add(markerOptions);
        }


        return listOfMarkerOptions;
    }


    /**
     * Returns a MapMarkerOptions based on the argument
     *
     * @param latLng the position of the marker
     *
     * @param mapMarkerIconType a string describing the type of map marker icon.
     *                      There are three acceptable values:
     *                      (a) "STARTING_POINT" - the starting point icon
     *                      (b) "FINISHING_POINT" - the finishing point
     *                      (c) "DEFAULT" - the default red icon
     * */
    private static MarkerOptions getMarkerOptions(LatLng latLng, String mapMarkerIconType)
    {

        BitmapDescriptor mapMarkerIcon;

        switch (mapMarkerIconType)
        {
            case "STARTING_POINT":
                mapMarkerIcon =
                        BitmapDescriptorFactory.fromResource(
                                R.drawable.small_starting_point_icon);
                break;

            case "FINISHING_POINT":
                mapMarkerIcon = BitmapDescriptorFactory.fromResource(
                        R.drawable.small_finishing_point_icon);
                break;

            case "DEFAULT":
            default:
                mapMarkerIcon = BitmapDescriptorFactory.defaultMarker();
                break;
        }

        return new MarkerOptions().position(latLng).icon(mapMarkerIcon);
    }




    /**
     * Plot the markers on the map and return the list of markers that have been plotted
     *
     * @param listOfMarkerOptions a list of MarkerOptions (a marker option corresponds to
     *                            information pf a marker that is going to be plotted)
     *
     * @return a list of plotted Markers
     * */
    private List<Marker> plotMarkersOnMapAndGetListOfMarkers(List<MarkerOptions> listOfMarkerOptions)
    {
        List<Marker> listOfMarkers = new ArrayList<>();

        for (MarkerOptions markerOptions: listOfMarkerOptions)
        {
            Marker plottedMarker = this.googleMap.addMarker(markerOptions);
            listOfMarkers.add(plottedMarker);
        }

        return listOfMarkers;
    }



    /**
     * Get the bounds of all plotted markers (to be used later for dynamically determining
     * the zoom level of the map, so that all markers are encompassed visible within the map)
     *
     * @param listOfPlottedMarkers list of markers that have been plotted on the map
     *
     * @return boundary information of the markers
     * */
    private static LatLngBounds getLatLngBoundsOfPlottedMarkers(List<Marker> listOfPlottedMarkers)
    {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (Marker marker: listOfPlottedMarkers)
        {
            builder.include(marker.getPosition());
        }

        return builder.build();
    }



    /**
     * Adjust the map zoom so that all plotted markers on the map are visible to the user
     *
     * @param latLngBounds contains the boundary information of the markers
     * */
    private void adjustMapZoomWithAllMarkersVisibleToUser(LatLngBounds latLngBounds)
    {
        final int PADDING = 200;

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, PADDING);

        this.googleMap.moveCamera(cameraUpdate);

        this.googleMap.getUiSettings().setAllGesturesEnabled(false);
        this.googleMap.getUiSettings().setZoomControlsEnabled(false);

    }




    /**
     * Note: As stated by Google Map Android documentation, the fragment/activity
     * must forward the onStart() lifecycle method to mapView.onStart()
     * */
    @Override
    public void onStart()
    {
        super.onStart();
        this.mapView.onStart();
    }


    /**
     * Note: As stated by Google Map Android documentation, the fragment/activity
     * must forward the onResume() lifecycle method to mapView.onResume()
     * */
    @Override
    public void onResume()
    {
        super.onResume();
        this.mapView.onResume();
    }



    /**
     * Note: As stated by Google Map Android documentation, the fragment/activity
     * must forward the onPause() lifecycle method to mapView.onPause()
     * */
    @Override
    public void onPause()
    {
        super.onPause();
        this.mapView.onPause();
    }



    /**
     * Note: As stated by Google Map Android documentation, the fragment/activity
     * must forward the onStop() lifecycle method to mapView.onStop()
     * */
    @Override
    public void onStop()
    {
        super.onStop();
        this.mapView.onStop();
    }



    /**
     * Note: As stated by Google Map Android documentation, the fragment/activity
     * must forward the onDestroy() lifecycle method to mapView.onDestroy()
     * */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.mapView.onDestroy();
    }


    /**
     * Note: As stated by Google Map Android documentation, the fragment/activity
     * must forward the onLowMemory() lifecycle method to mapView.onLowMemory()
     * */
    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        this.mapView.onLowMemory();
    }


    /**
     * Note: As stated by Google Map Android documentation, the fragment/activity
     * must forward the View.onSaveInstanceState() lifecycle method to
     * mapView.onSaveInstanceState()
     * */
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        this.mapView.onSaveInstanceState(outState);
    }

}