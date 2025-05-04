package com.murdoch.fitnessapp.controllers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.murdoch.fitnessapp.R;
import com.murdoch.fitnessapp.callbackinterfaces.IFragmentReadyListener;
import com.murdoch.fitnessapp.models.interfaces.IGPSLocation;


/**
 * A fragment that displays the locations that the user has
 * travelled/ or where the user is currently at during a workout session
 *
 * Note: The calling activity must implement IFragmentReadyListener interface
 *
 * Use the {@link DuringWorkoutMapFragment#newInstance} factory method to
 *  * create an instance of this fragment.
 *
 * */
public class DuringWorkoutMapFragment extends Fragment
{
    private IFragmentReadyListener fragmentReadyListener;


    private MapView mapView;
    private GoogleMap googleMap;

    private Marker previousMarker;
    private Marker firstMarker;

    public DuringWorkoutMapFragment()
    {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment instead of the constructor
     *
     * @return A new instance of fragment DuringWorkoutMapFragment.
     */
    public static DuringWorkoutMapFragment newInstance()
    {
        return new DuringWorkoutMapFragment();

    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_during_workout_map, container,
                false);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initializeMapView(savedInstanceState);
    }


    /**
     * Throws ClassCastException if context does not implement IFragmentReadyListener interface
     * */
    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        this.fragmentReadyListener = (IFragmentReadyListener) context;
    }

    /**
     * Initializes the Map view
     *
     * Note: As stated by Google Map Android documentation, the fragment/activity
     *      * must forward the onViewCreated/onCreate() lifecycle method to mapView.onCreate()
     *
     * @see IFragmentReadyListener#onFragmentReady(Fragment)
     * */
    private void initializeMapView(Bundle savedInstanceState)
    {

        this.mapView = getView().findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);

        this.mapView.getMapAsync(googleMap ->

                googleMap.setOnMapLoadedCallback(() -> DuringWorkoutMapFragment.this.initializeGoogleMap(googleMap)));
    }



    /**
     * Initialize the default settings of the google map
     *
     * Also fires the this.fragmentReadyListener.onFragmentReady(fragment)
     *      * event when the initialization is complete
     *
     * Also sets the instance variable this.googleMap
     * to the argument
     *
     * @param googleMap the google map instance of interest
     * */
    private void initializeGoogleMap(@NonNull GoogleMap googleMap)
    {
        final float MIN_ZOOM_PREFERENCE = 14;

        this.googleMap = googleMap;
        this.googleMap.setMinZoomPreference(MIN_ZOOM_PREFERENCE);

        this.fragmentReadyListener.onFragmentReady(DuringWorkoutMapFragment.this);

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
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);

        this.mapView.onSaveInstanceState(outState);
    }




    /**
     * Adds a map marker to the google map canvas.
     *
     * (a) If this is the very first marker that is added, it adds the "Starting Point"
     * marker to the map
     *
     * (b) If this is the non-first marker that is added, it adds the "Current"/ "You are here"
     * marker to the map. At the same time, it changes the previous non-first marker to
     * the default violet map marker
     *
     * Ensure that this method is only called after the OnFragmentReady(Fragment)
     * event listener is fired
     *
     *
     * @param gpsLocation the location to be plotted to the map
     *
     * @see IFragmentReadyListener#onFragmentReady(Fragment)
     * */
    public void addMapMarker(IGPSLocation gpsLocation)
    {
        final float ZOOM_LEVEL = 18;

        LatLng latLng = convertGPSLocationToLatLng(gpsLocation);


        if (this.firstMarker == null)
        {
            this.addStartPointMapMarker(latLng);
        }
        else
        {
            this.changePreviousMapMarkerToDefaultMapMarker();
            this.addCurrentPointMapMarker(latLng);
        }

        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,ZOOM_LEVEL));
    }


    /**
     * Add a starting point map marker to the google map canvas
     *
     * Also, this method sets the value of the this.firstMarker instance
     * variable to the added marker
     *
     * @param latLng the position to add/plot the marker
     * */
    private void addStartPointMapMarker(LatLng latLng)
    {
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(latLng);

        markerOptions.icon(getMapMarkerIcon("STARTING_POINT"));

        this.firstMarker = this.googleMap.addMarker(markerOptions);
    }



    /**
     * Add a marker representing the current user position to the map
     *
     * Also, this method sets the value of the this.previousMarker instance
     * variable to the added marker
     *
     * @param latLng the position to add/plot the marker
     * */
    private void addCurrentPointMapMarker(LatLng latLng)
    {
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(latLng);

        markerOptions.icon(getMapMarkerIcon("CURRENT_POINT"));

        this.previousMarker = this.googleMap.addMarker(markerOptions);
    }



    /**
     * Changes the previous marker from a "CURRENT_POINT" map marker to
     * the default map marker
     * */
    private void changePreviousMapMarkerToDefaultMapMarker()
    {
        if (this.previousMarker != null)
        {
            this.previousMarker.setIcon(getMapMarkerIcon("DEFAULT"));
        }
    }



    /**
     * Returns a Map Marker icon based on the argument
     *
     * @param mapMarkerType a string describing the type of map marker.
     *                      There are three acceptable values:
     *                      (a) "STARTING_POINT" - the starting point icon
     *                      (b) "CURRENT_POINT" - the current point (You are here) icon
     *                      (c) "DEFAULT" - the default violet icon
     * */
    private static BitmapDescriptor getMapMarkerIcon(String mapMarkerType)
    {
        BitmapDescriptor mapMarkerIcon;

        switch (mapMarkerType)
        {
            case "STARTING_POINT":
                mapMarkerIcon =
                        BitmapDescriptorFactory.fromResource(R.drawable.starting_point_icon);
                break;

            case "CURRENT_POINT":
                mapMarkerIcon = BitmapDescriptorFactory.fromResource(R.drawable.you_are_here_icon);
                break;

            case "DEFAULT":
            default:
                mapMarkerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
                break;
        }


        return mapMarkerIcon;
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


}