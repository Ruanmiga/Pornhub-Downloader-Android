package com.fastsoft.ph.downloader.dl.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.Map;
import java.util.List;
import android.util.*;
import android.text.*;

public class HttpRetriever {
    
    static final String COOKIES_HEADER = "Set-Cookie";
	static final String COOKIE = "Cookie";
	static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.88 Mobile Safari/537.36";
	static CookieManager cookieManager = new CookieManager();
    static String VideoTitle;
	static String VideoThumbnail;

    public static String retrieve(String url) {
        URL targetURL;
        try {
            targetURL = new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
        HttpURLConnection urlConnection = null;
        String response;
        try {
            urlConnection = (HttpURLConnection) targetURL.openConnection();
            urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Accept-Encoding", "gzip");
            urlConnection.setRequestProperty("User-Agent", USER_AGENT);


			if (cookieManager.getCookieStore().getCookies().size() > 0) {
				urlConnection.setRequestProperty(COOKIE ,
												 TextUtils.join(";", cookieManager.getCookieStore().getCookies()));
			}
			Log.i("Cookie",TextUtils.join(";", cookieManager.getCookieStore().getCookies()));
			Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
			List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
			if (cookiesHeader != null) {
				for (String cookie : cookiesHeader) {
					cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
				}
			}

            response = readStream(urlConnection);
        } catch (IOException e) {
            return null;
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
        return response;
    }
	
    public static String getVideoTitle(String Line){
		
	if(Line.contains("og:title"))
	{
		VideoTitle = Line.substring(Line.indexOf("og:title"));
		VideoTitle = VideoTitle.substring(Utils.ordinalIndexOf(VideoTitle,"\"",1)+1,Utils.ordinalIndexOf(VideoTitle,"\"",2));
	}
	return VideoTitle;
	}
	public static String getVideoThumbnail(String Line){
		
		if(Line.contains("og:image")){
		VideoThumbnail = Line.substring(Line.indexOf("og:image"));
		VideoThumbnail = Line.substring(Line.indexOf("content"));
		VideoThumbnail = VideoThumbnail.substring(Utils.ordinalIndexOf(VideoThumbnail,"\"",0)+1,Utils.ordinalIndexOf(VideoThumbnail,"\"",1));
		}
		return VideoThumbnail;
	}
	public static String getTitle(){
		return VideoTitle;
	}
	public static String getThumbnail(){
		return VideoThumbnail;
	}
    private static String readStream(HttpURLConnection connection) {
        BufferedReader br;
        StringBuilder builder = new StringBuilder();
        String line;
        try {
			if ("gzip".equals(connection.getContentEncoding())) {
				br = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
			}
			else {
				br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			}
            while ((line = br.readLine()) != null) {
                builder.append(line);
				getVideoTitle(line);
				getVideoThumbnail(line);
            }
        } catch (IOException e) {
            //Unable to read from the stream
            return null;
        }


        return builder.toString();
    }
    
}
