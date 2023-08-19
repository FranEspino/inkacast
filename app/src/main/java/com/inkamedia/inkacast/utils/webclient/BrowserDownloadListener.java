package com.inkamedia.inkacast.utils.webclient;

import android.util.Log;
import android.webkit.DownloadListener;

public class BrowserDownloadListener implements DownloadListener {

    private BrowserWebViewClient client;

    public BrowserDownloadListener(BrowserWebViewClient client) {
        Log.d("BrowserDownloadListener", "BrowserDownloadListener"+client.toString());
        this.client = client;
    }

    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        Log.d("BrowserDownloadListener", "onDownloadStart"+url);
        client.process_URL(url);
    }
}
