package com.laajue.snapping;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

public class MessageAdapter extends ArrayAdapter<ParseObject>{
	
	protected Context mContext;
	protected List<ParseObject> mMessages;
	
	public MessageAdapter(Context context, List<ParseObject> messages) {
		super(context, R.layout.message_item, messages);
		mContext = context;
		mMessages = messages;
		
	}
	
	/*View. Creating a custom adaptor. Adaptor calls certain method to get the appropriate Fragment. Then Adapted it and put it in View.
	 Attached to list view. call get view. create view. inflated into layout, ad attached into Listview.*/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//no SUPER because it doesn't matter since we are gonna handle it on our on. Will use VIEW HOLDER. which we create
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
			holder = new ViewHolder();
			holder.iconImageView = (ImageView) convertView.findViewById(R.id.messageIcon);
			holder.nameLabel = (TextView) convertView.findViewById(R.id.senderLabel);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag(); //getTag gets the ViewHolder that was already created
		}
		ParseObject message = mMessages.get(position);
		
		if (message.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_IMAGE)){
			holder.iconImageView.setImageResource(R.drawable.ic_action_picture);
		}
		else {
			holder.iconImageView.setImageResource(R.drawable.ic_action_play_over_video);
		}
		holder.nameLabel.setText(message.getString(ParseConstants.KEY_SENDER_NAME));	
		return convertView;
	}
	
	private static class ViewHolder {
		ImageView iconImageView;
		TextView nameLabel;
	}
	
	public void refill(List<ParseObject> messages){
		mMessages.clear();
		mMessages.addAll(messages);
		notifyDataSetChanged();
	}
	
}
