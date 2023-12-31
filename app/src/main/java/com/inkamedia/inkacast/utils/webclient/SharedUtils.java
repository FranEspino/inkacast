package com.inkamedia.inkacast.utils.webclient;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SharedUtils {

    private static Pattern video_regex = Pattern.compile("\\.(mp4|mp4v|mpv|m1v|m4v|mpg|mpg2|mpeg|xvid|webm|3gp|avi|mov|mkv|ogg|ogv|ogm|m3u8|mpd|ism(?:[vc]|/manifest)?)(?:[\\?#]|$)");

    public static String getVideoMimeType(String uri) {
        String mimeType = null;

        if (uri == null) return mimeType;

        Matcher matcher = video_regex.matcher(uri.toLowerCase());
        String file_ext;

        if (matcher.find()) {
            file_ext = matcher.group(1);

            switch (file_ext) {
                case "mp4":
                case "mp4v":
                case "m4v":
                    mimeType = "video/mp4";
                    break;
                case "mpv":
                    mimeType = "video/MPV";
                    break;
                case "m1v":
                case "mpg":
                case "mpg2":
                case "mpeg":
                    mimeType = "video/mpeg";
                    break;
                case "xvid":
                    mimeType = "video/x-xvid";
                    break;
                case "webm":
                    mimeType = "video/webm";
                    break;
                case "3gp":
                    mimeType = "video/3gpp";
                    break;
                case "avi":
                    mimeType = "video/x-msvideo";
                    break;
                case "mov":
                    mimeType = "video/quicktime";
                    break;
                case "mkv":
                    mimeType = "video/x-mkv";
                    break;
                case "ogg":
                case "ogv":
                case "ogm":
                    mimeType = "video/ogg";
                    break;
                case "m3u8":
                    mimeType = "application/x-mpegURL";
                    break;
                case "mpd":
                    mimeType = "application/dash+xml";
                    break;
                case "ism":
                case "ism/manifest":
                case "ismv":
                case "ismc":
                    mimeType = "application/vnd.ms-sstr+xml";
                    break;
                default:
                    break;
            }
        }
        return mimeType;
    }

}
