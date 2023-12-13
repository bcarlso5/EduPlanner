package com.example.eduplanner.viewmodel;

import androidx.lifecycle.ViewModel;

/**
 * Class to help keep the app logged in
 */
public class MainActivityViewModel extends ViewModel {
    /**
     * Value for if the user is signed in
     */
    private boolean mIsSigningIn;

    /**
     * Constructor for the new user that is signed in
     */
    public MainActivityViewModel() {
        mIsSigningIn = false;
    }

    /**
     * Get the current value of if the user is signed in
     * @return boolean value
     */
    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    /**
     * Function for setting if the user is currently signed in.
     * @param mIsSigningIn boolean value of if the user is signed in
     */
    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }
}