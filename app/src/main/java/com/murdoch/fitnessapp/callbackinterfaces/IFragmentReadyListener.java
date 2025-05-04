package com.murdoch.fitnessapp.callbackinterfaces;

import androidx.fragment.app.Fragment;

/**
 * A listener interface that is fires the onFragmentReady when fragments are ready
 *
 * To be implemented by activity controllers that host fragments
 *
 *
 * */
public interface IFragmentReadyListener
{
    /**
     * The method that is called when the fragment is ready
     *
     * @param fragment the fragment that is ready
     * */
    void onFragmentReady(Fragment fragment);
}
