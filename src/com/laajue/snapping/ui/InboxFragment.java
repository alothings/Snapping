package com.laajue.snapping.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.laajue.snapping.R;
import com.laajue.snapping.R.layout;
import com.laajue.snapping.adapters.MessageAdapter;
import com.laajue.snapping.utils.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class InboxFragment extends ListFragment{

	protected List<ParseObject> mMessages;  //messages variable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getActivity().setProgressBarIndeterminateVisibility(true);
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);//may need to correct < >
		query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
		query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> messages, ParseException e) {
				getActivity().setProgressBarIndeterminateVisibility(false);
				if (e == null ) {
					//We found messages
					mMessages = messages;
			
					String[] usernames = new String[mMessages.size()];
					int i = 0;
					for (ParseObject message : mMessages){
						usernames[i] = message.getString(ParseConstants.KEY_SENDER_NAME);
						i++;					
					}
					if ( getListView().getAdapter() == null){
						MessageAdapter adapter = new MessageAdapter(
								getListView().getContext(),
								mMessages);
						setListAdapter(adapter);
					}
				}
				else {
					((MessageAdapter) getListView().getAdapter()).refill(mMessages);
				}
			}
		});
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ParseObject message = mMessages.get(position);
		String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
		ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
		
		Uri fileUri = Uri.parse(file.getUrl());
		
		if (messageType.equals(ParseConstants.TYPE_IMAGE)) {
			//view the image
			Intent intent = new Intent(getActivity(), ViewImageActivity.class);
			intent.setData(fileUri);
			startActivity(intent);
		}
		else {
			//view the video
			Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
			intent.setDataAndType(fileUri, "video/*");
			startActivity(intent);
		}
		
		// Delete the message!
		List<String> ids = message.getList(ParseConstants.KEY_RECIPIENT_IDS);
		
		if (ids.size() == 1){
			//the its the last recipient so we can delete the entire message
			message.deleteInBackground();
		}
		else{
			//only remove the message from the user
			ids.remove(ParseUser.getCurrentUser().getObjectId());
			
			ArrayList<String> idsToRemove = new ArrayList<String>();
			idsToRemove.add(ParseUser.getCurrentUser().getObjectId());
			message.removeAll(ParseConstants.KEY_RECIPIENT_IDS, idsToRemove);
			message.saveInBackground();
		}
	}
}