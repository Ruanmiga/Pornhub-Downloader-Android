package com.fastsoft.ph.downloader.dl.utils;
import android.os.AsyncTask;
import java.net.URL;
import com.fastsoft.ph.downloader.dl.models.VideoData;
import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.util.Log;
import java.util.List;
import android.os.Handler;
import android.app.Activity;

public class PornhubExtractor {

	private String url;
	private OnExtractListener listener = null;
	private List<VideoData> pornhub;
	private Handler handler;
	
	
	public PornhubExtractor(String url){
		this.url = url;
	}
	public PornhubExtractor setOnExtractListener(OnExtractListener listener){
		this.listener = listener;
	    
		return this;
	}
	public PornhubExtractor start(){
		handler = new Handler();
		return this;
	}
	
	public void destroy(){
		if(handler != null){
			handler = null;
		}
	}
	public PornhubExtractor extract()
	{
		new Thread()
		{
			public void run()
			{
				final String html = HttpRetriever.retrieve(url);

				pornhub = PornhubParser.getGeneralList(html);
				
				if(html!=null)
				{
					handler.post(new Runnable()
						{

							@Override
							public void run()
							{
								listener.onExtractSuccess(pornhub,HttpRetriever.getTitle(),HttpRetriever.getThumbnail());
							}


						});

				}else
				{
					handler.post(new Runnable()
						{

							@Override
							public void run()
							{
								listener.onError();
							}


						});

				}
			}
		}.start();
		return this;
	}

	
    public interface OnExtractListener{
		void onExtractSuccess(List<VideoData> data,String videoTitle,String videoThumbnail);

		void onError();
	}
    
    
    
}
