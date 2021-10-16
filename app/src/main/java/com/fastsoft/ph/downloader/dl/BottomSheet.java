package com.fastsoft.ph.downloader.dl;

	import android.app.Activity;
	import android.app.Dialog;
	import android.os.Bundle;
	import android.util.Log;
	import android.view.Display;
	import android.view.Gravity;
	import android.view.View;
	import android.view.Window;
	import android.view.WindowManager;
	import android.widget.ImageView;
	import android.widget.LinearLayout;
	import android.widget.RelativeLayout;
	import android.widget.TextView;

	
	import java.util.ArrayList;
	import java.util.List;

	public class BottomSheet extends Dialog{

		private static final String TAG = "BottomSheet";

		private static final int MAX_ITEM_COUNT = 8;

		private LinearLayout container;
		private Activity context;
		private View contentView;
		
		public BottomSheet(Activity context,View contentView){
			super(context);

			this.context = context;
			this.contentView = contentView;
			
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);

			configSheet();

		}

		@Override
		public void cancel() {
			super.cancel();
			context.finish();
		}
  
		private void configSheet() {
			setContentView(R.layout.bottom_dialog);

		    container = findViewById(R.id.container);
			container.addView(contentView);
			

			Window window = getWindow();
			WindowManager m = window.getWindowManager();
			Display d = m.getDefaultDisplay();
			WindowManager.LayoutParams p = window.getAttributes();
			p.width = d.getWidth();
			window.setAttributes(p);
			window.setGravity(Gravity.BOTTOM);
			window.setBackgroundDrawableResource(android.R.color.transparent);
			window.setWindowAnimations(R.style.bottomSheetAnim);

			findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						BottomSheet.this.dismiss();
						context.finish();
					}
				});
}

				


		public static class Builder{

			
			private Activity context;
			private View view;

			public Builder(Activity context) {
				this.context = context;
			}
			public Builder setContentView(View view){
				this.view = view;
				return this;
			}

			public static Builder newBuilder(Activity context){
				return new Builder(context);
			}

			public BottomSheet build(){
				return new BottomSheet(context,view);
			}

			
		}

		
	
    
}
