package com.fastsoft.ph.downloader.dl.adapters;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.fastsoft.ph.downloader.dl.R;
import android.content.Context;
import java.util.List;
import com.fastsoft.ph.downloader.dl.models.VideoData;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.LayoutInflater;
import android.view.animation.AnimationUtils;
import android.support.v7.app.AlertDialog;
import com.fastsoft.ph.downloader.dl.utils.ItemClickInterface;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

	Context context;
	List<VideoData> vd;
	ItemClickInterface ic;
	
	int lastPosition = -1;
	public VideoAdapter(Context context,List<VideoData> vd,ItemClickInterface ic){
		this.context = context;
		this.vd = vd;
		this.ic = ic;

	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
		View v = LayoutInflater.from(context).inflate(R.layout.item,p1,false);
		return new ViewHolder(v);
	}
	private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {

		holder.name.setText(vd.get(position).getQuality());
		
		setAnimation(holder.itemView,position);

		setClick(holder.itemView,position);

	}
    @Override
    public void onViewDetachedFromWindow(final ViewHolder holder)
    {
        ((ViewHolder)holder).clearAnimation();
    }
	@Override
	public int getItemCount() {
		return vd.size();
	}
    public void setClick(View v,final int position){
		v.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {

					if(ic != null){
						ic.itemClick(vd.get(position).getQuality(),vd.get(position).getVideoUrl(),vd.get(position).getFormat());
					}
				}

			});
	}
    public class ViewHolder extends RecyclerView.ViewHolder{
		TextView name;
		
		public ViewHolder(View itemView){
			super(itemView);

		 name = itemView.findViewById(R.id.list_item_text);
			
		}
		public void clearAnimation()
		{
			itemView.clearAnimation();
		}
	}
    
}
