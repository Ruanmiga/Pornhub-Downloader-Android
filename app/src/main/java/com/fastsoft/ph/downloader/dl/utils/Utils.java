package com.fastsoft.ph.downloader.dl.utils;
import android.content.Context;
import android.util.DisplayMetrics;
import android.app.DownloadManager;
import android.os.Environment;
import java.io.File;
import android.net.Uri;

public class Utils {
    
    public static int ordinalIndexOf(String str, String substr, int n) {
        int pos = -1;
        do {
            pos = str.indexOf(substr, pos + 1);
        } while (n-- > 0 && pos != -1);
        return pos;
    }
    
    public static String removeBackSlash(String str)
	{
		return str.replaceAll("\\\\","");
	}
	public static int calculateNoOfColumns(Context context,float columnWidthDp){
        {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
            return (int)Math.round(screenWidthDp / columnWidthDp +  0.5);
        }
	}
	public static void downloadFile(Context context,String url, String fileName,String format)
	{
		DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() +"/"+ fileName + "." + format);
		DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url))
			.setTitle(fileName)
			.setDescription("Pinche Puerco Descargando Videos XD")
			.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
			.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
			.setDestinationUri(Uri.fromFile(file))
			.setAllowedOverMetered(true)
			.setAllowedOverRoaming(true);

		downloadManager.enqueue(request);
	}
}
