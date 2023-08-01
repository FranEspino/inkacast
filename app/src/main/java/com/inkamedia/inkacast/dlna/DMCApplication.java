package com.inkamedia.inkacast.dlna;

import android.app.Activity;
import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.inkamedia.inkacast.utils.SharedPreferenceApp;

import org.cybergarage.upnp.ControlPoint;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class DMCApplication extends Application {

	private static DMCApplication mDmcApplication;
	private static SharedPreferenceApp mContextApp;

	private List<Activity> activities;
	private ControlPoint mControlPoint;

	@Override
	public void onCreate() {
		super.onCreate();
		if (activities != null) {
			activities = null;
		}
		activities = new ArrayList<Activity>();
		mDmcApplication = this;
		mContextApp = new SharedPreferenceApp(getApplicationContext());

		if (mContextApp.getDarkMode()) {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
		} else {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
		}
	}

	public void addActivity(Activity activity) {
		if (activities != null) {
			if (activities.contains(activity)) {
				activities.remove(activity);
			}
			activities.add(activity);
		}
	}

	public void removeActivity(Activity activity) {
		if (activities != null && activities.contains(activity)) {
			activities.remove(activity);
		}
	}

	public static DMCApplication getInstance() {
		return mDmcApplication;
	}

	public static SharedPreferenceApp getContext() {
		return mContextApp;
	}

	public void quit() {
		if (activities != null) {
			for (Activity activity : activities) {
				activity.finish();
			}
			activities = null;
		}
	}

	public void setControlPoint(ControlPoint controlPoint) {
		mControlPoint = controlPoint;
	}

	public ControlPoint getControlPoint() {
		return mControlPoint;
	}
}