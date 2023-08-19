package com.inkamedia.inkacast.utils.webclient;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.webkit.WebView;
import android.widget.Toast;

import com.inkamedia.inkacast.BuildConfig;

public class BrowserDebugUtils {

    private static boolean isWebContentsDebuggingEnabled = false;

    public static boolean configWebView(Context context) {
        boolean didChange = false;

        if (Build.VERSION.SDK_INT >= 19) {  // Build.VERSION_CODES.KITKAT
            if (
                    (BuildConfig.DEBUG)
                            || (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE))
                            || (BrowserUtils.getEnableRemoteDebuggerPreference(context))
            ) {
                if (!isWebContentsDebuggingEnabled) {
                    isWebContentsDebuggingEnabled = true;
                    WebView.setWebContentsDebuggingEnabled(true);
                    didChange = true;
                }
            }
            else {
                if (isWebContentsDebuggingEnabled) {
                    isWebContentsDebuggingEnabled = false;
                    WebView.setWebContentsDebuggingEnabled(false);

                    Toast.makeText(context, "WebView remote debugging disabled", Toast.LENGTH_SHORT).show();
                    didChange = true;
                }
            }
        }

        return didChange;
    }

}