package com.inkamedia.inkacast.utils.webclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.inkamedia.inkacast.presentation.MainActivity;
import com.inkamedia.inkacast.R;

public class BrowserWebViewClient_VideoDetector extends WebViewClient {

    private boolean isFisrtTime, findFile = false;
    private MainActivity browserActivity;
    private boolean touchedScreen = false;

    public BrowserWebViewClient_VideoDetector(MainActivity browserActivity) {
        super();

        this.browserActivity = browserActivity;
    }


    @Override
    public void onLoadResource(WebView view, String url) {
        process_URL(url, view);
    }


    public void setTouchedScreen(boolean touched) {
        touchedScreen = touched;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        String mimeType = SharedUtils.getVideoMimeType(url);
//        if(url.contains(MainActivity.currentUrl ) &&  !isFisrtTime){
//            isFisrtTime = false;
//            return  false;
//        }

        if (mimeType != null) {
            process_URL(url, view);
            return true;
        }
        if (touchedScreen) {
            touchedScreen = false;
            return true;
        }
        return findFile;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
//            if(url.contains(BrowserActivity.currentUrl ) && !isFisrtTime ){
//                isFisrtTime = false;
//                return false;
//            }



        String mimeType = SharedUtils.getVideoMimeType(url);
        if (mimeType != null) {
            process_URL(url, view);
            return true;
        }
        if (touchedScreen) {
            touchedScreen = false;
            return true;
        }
        return findFile;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        process_URL(url, view);
        return null;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        process_URL(url, view);
        return null;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        String behavior = BrowserUtils.getBadSslPageloadBehavior(browserActivity);

        if (behavior.equals(browserActivity.getString(R.string.pref_pageloadbehavior_array_value_ask)))
            askBadSslPageloadBehavior(handler, error);
        else if (behavior.equals(browserActivity.getString(R.string.pref_pageloadbehavior_array_value_proceed)))
            handler.proceed();
        else if (behavior.equals(browserActivity.getString(R.string.pref_pageloadbehavior_array_value_cancel)))
            handler.cancel();
        else
            handler.cancel();
    }

    protected void process_URL(String uri) {
        process_URL(uri, null);
    }

    private void process_URL(String uri, WebView view) {
        String mimeType = SharedUtils.getVideoMimeType(uri);
        if (mimeType != null) {
            findFile = true;
            browserActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String referer = (view == null) ? null : view.getUrl();
                    browserActivity.addSavedVideo(uri, mimeType, referer);
                }
            });
        }
    }

    private void askBadSslPageloadBehavior(SslErrorHandler handler, SslError error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(browserActivity);

        StringBuilder sb = new StringBuilder();

        int error_code = error.getPrimaryError();
        switch (error_code) {
            case SslError.SSL_DATE_INVALID:
                sb.append(browserActivity.getString(R.string.alertdialog_badssl_pageloadbehavior_reason_ssl_date_invalid));
                break;
            case SslError.SSL_EXPIRED:
                sb.append(browserActivity.getString(R.string.alertdialog_badssl_pageloadbehavior_reason_ssl_expired));
                break;
            case SslError.SSL_IDMISMATCH:
                sb.append(browserActivity.getString(R.string.alertdialog_badssl_pageloadbehavior_reason_ssl_idmismatch));
                break;
            case SslError.SSL_INVALID:
                sb.append(browserActivity.getString(R.string.alertdialog_badssl_pageloadbehavior_reason_ssl_invalid));
                break;
            case SslError.SSL_NOTYETVALID:
                sb.append(browserActivity.getString(R.string.alertdialog_badssl_pageloadbehavior_reason_ssl_notyetvalid));
                break;
            case SslError.SSL_UNTRUSTED:
                sb.append(browserActivity.getString(R.string.alertdialog_badssl_pageloadbehavior_reason_ssl_untrusted));
                break;
        }

        String url = error.getUrl();
        if ((url != null) && !url.isEmpty()) {
            if (sb.length() > 0) {
                sb.append("\n\n");
            }

            sb.append(browserActivity.getString(R.string.alertdialog_badssl_pageloadbehavior_label_url));
            sb.append("\n");
            sb.append(url);
        }

        if (sb.length() > 0) {
            builder.setMessage(sb.toString());
        }

        builder.setTitle(
                        R.string.alertdialog_badssl_pageloadbehavior_title
                )
                .setPositiveButton(
                        R.string.alertdialog_badssl_pageloadbehavior_label_button_positive,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handler.proceed();
                            }
                        }
                )
                .setNegativeButton(
                        R.string.alertdialog_badssl_pageloadbehavior_label_button_negative,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handler.cancel();
                            }
                        }
                )
                .show();
    }
}