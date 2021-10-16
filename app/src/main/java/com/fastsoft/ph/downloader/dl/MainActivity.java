package com.fastsoft.ph.downloader.dl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.fastsoft.ph.downloader.dl.utils.PornhubExtractor;
import java.util.ArrayList;
import com.fastsoft.ph.downloader.dl.models.VideoData;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.List;
import com.fastsoft.ph.downloader.dl.adapters.VideoAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import com.fastsoft.ph.downloader.dl.utils.Utils;
import com.fastsoft.ph.downloader.dl.utils.ItemClickInterface;
import android.content.Intent;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Build;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.DialogInterface;
import android.provider.Settings;
import android.net.Uri;

public class MainActivity extends AppCompatActivity {
    
	PornhubExtractor ph;
	TextView txtTitle;
	ImageView thumbnail;
	VideoAdapter videoAdapter;
	RecyclerView rv;
	String fileName;
	private Bundle savedInstanceState;
	BottomSheet dialog;
	int PERMISSION_CODE = 304;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
        
        
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			if(CheckAndRequestPermission())
			{
				intializeDialog();
				dialog.show();
				getIntent(getIntent());
			}

		}else
		{
			intializeDialog();
			dialog.show();
			getIntent(getIntent());
		}

    
	
	}

	@Override
	protected void onStop()
	{
	    super.onStop();
		if(dialog != null)
		{
			dialog.dismiss();
		}
	}
	
	
    private void getIntent(Intent intent){
		if (savedInstanceState == null && Intent.ACTION_SEND.equals(intent.getAction())
			&& intent.getType() != null && "text/plain".equals(intent.getType())) {

            String url = getIntent().getStringExtra(Intent.EXTRA_TEXT);

            if (url != null
				&& (url.contains("pornhub.com/view_video.php?viewkey="))) {

				sendRequest(url);
			}else{
				Toast.makeText(MainActivity.this,"Esta Url no es Compatible",Toast.LENGTH_LONG).show();
			}
	  }
	}
	private void sendRequest(String url){
		ph = new PornhubExtractor(url);
		ph.start().extract().setOnExtractListener(new PornhubExtractor.OnExtractListener(){

				@Override
				public void onExtractSuccess(List<VideoData> data,String videoTitle,String videoThumbnail)
				{
					setDetails(videoTitle,videoThumbnail,data);
				}

				@Override
				public void onError()
				{
				}
			});
	}
	private void intializeDialog(){
		BottomSheet.Builder sheet = new BottomSheet.Builder(MainActivity.this);
		sheet.setContentView(intializeView());
		dialog = sheet.build();
	}
	private View intializeView(){
	  View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main,(ViewGroup)findViewById(R.id.activity));
	  
		txtTitle = v.findViewById(R.id.video_title);
		thumbnail = v.findViewById(R.id.video_thumbnail);
		rv = v.findViewById(R.id.recyclerView);

		
	  return v;
	}
	private void setDetails(String title,String thumbnailUrl,List<VideoData> data){
		txtTitle.setText(title);
		Glide.with(getApplicationContext())
		.load(thumbnailUrl)
		.crossFade()
		.into(thumbnail);
		
		videoAdapter = new VideoAdapter(MainActivity.this, data, new ItemClickInterface(){

				@Override
				public void itemClick(String title, String url, String format) {
					fileName = txtTitle.getText().toString();
					if(fileName == null || fileName.equals("")){
						fileName = title + "_" + System.currentTimeMillis();
					}
					
					Utils.downloadFile(getApplicationContext(),url,fileName,format.toLowerCase());
					dialog.dismiss();
					finish();
					Toast.makeText(MainActivity.this,"La descarga A Comenzado",Toast.LENGTH_LONG).show();
				}
		});
		rv.setHasFixedSize(true);
		rv.setLayoutManager(new GridLayoutManager(getApplicationContext(),Utils.calculateNoOfColumns(getApplicationContext(),100f)));
		rv.setAdapter(videoAdapter);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ph.destroy();
	}
	public boolean CheckAndRequestPermission()
	{
		String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

		if(checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED)
		{
			requestPermissions(new String[]{permission}, PERMISSION_CODE);

			return false;
		}else
		if
		(checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
		{
			return true;
		}

		return false;
    }

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode == PERMISSION_CODE)
		{
			if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_DENIED)
			{
				if(shouldShowRequestPermissionRationale(permissions[0]))
				{
					dialogPerm("", "This app need Storage Permission to downloading files.",
						"Grant", new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialogInterface, int i)
							{
								dialogInterface.dismiss();
								CheckAndRequestPermission();
							}


						},"Deny/Exit", new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialogInterface, int i)
							{
								dialogInterface.dismiss();
								finish();
							}


						},false);
				}
				else
				{
					dialogPerm("","You have denied permission, Allow permission at Application settings > Permissions",
						"Go to settings",
						new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialogInterface, int i)
							{
								dialogInterface.dismiss();

								Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
								finish();
							}


						},
						"Deny/Exit",
						new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialogInterface, int i)
							{
								dialogInterface.dismiss();
								finish();
							}


						}, false);


				}
			}else{
				intializeDialog();
				dialog.show();
				getIntent(getIntent());
			}

		}
	}

	private AlertDialog dialogPerm(String title, String msg, String posiviteLabel, DialogInterface.OnClickListener positiveClick,
								   String negativeLabel, DialogInterface.OnClickListener negativeClick,
								   boolean isCancelAble)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this,android.R.style.Theme_Material_Light_Dialog)
			.setTitle(title)
			.setMessage(msg)
			.setCancelable(isCancelAble)
			.setPositiveButton(posiviteLabel, positiveClick)
			.setNegativeButton(negativeLabel, negativeClick);

		AlertDialog alert = builder.create();
		alert.show();
		return alert;
	}
	
}

