package com.intravita.android.rss.model;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.intravita.android.rss.R;
import com.intravita.android.rss.widget.BebasneueTextView;
import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PostAdapter extends ArrayAdapter<Post> {
	private Context mContext;
	private int layoutResourceId;
	
	ArrayList<Post> data = null;

	public PostAdapter(Context context, int layoutResourceId, ArrayList<Post> holder) {
		super(context, layoutResourceId, holder);
		this.layoutResourceId = layoutResourceId;
		mContext = context;
		this.data = holder;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		PostHolder holder = null;
		
		if (row == null) {
			LayoutInflater inflater = ((SherlockActivity)mContext).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			holder = new PostHolder();
			holder.thumbnail = (SmartImageView)row.findViewById(R.id.thumbnail);
			holder.title = (TextView)row.findViewById(R.id.title);
			holder.author = (BebasneueTextView)row.findViewById(R.id.author);
			
			row.setTag(holder);
		} else {
			holder = (PostHolder)row.getTag();
		}
		
		Post post = data.get(position);
		holder.title.setText(post.title);
		holder.thumbnail.setImageUrl(post.thumbnail, R.drawable.ic_action_warning);
		holder.author.setText("@" + post.author);
		
		return row;
	}
	
	static class PostHolder {
		SmartImageView thumbnail;
		TextView title;
		BebasneueTextView author;
	}
}