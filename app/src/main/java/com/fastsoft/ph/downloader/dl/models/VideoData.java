package com.fastsoft.ph.downloader.dl.models;

public class VideoData {
    

	private String format;
	private String videoUrl;
	private String quality;


	public VideoData(String format, String videoUrl, String quality)
	{
		this.format = format;
		this.videoUrl = videoUrl;
		this.quality = quality;
	}


	public String getFormat()
	{
		return format;
	}


	public String getVideoUrl()
	{
		return videoUrl;
	}


	public String getQuality()
	{
		return quality;
	}
	
}
