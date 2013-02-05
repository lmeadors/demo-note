package com.elm.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.elm.R;
import com.elm.bean.Note;
import com.elm.utility.DateUtility;

import java.util.List;

public class NoteListAdapter extends ArrayAdapter<Note> {

	private static final String TAG = NoteListAdapter.class.getName();

	private final Context context;
	private final DateUtility dateUtility = new DateUtility();

	public NoteListAdapter(Context context, int textViewResourceId, List<Note> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final View view;
		final ViewHolder viewHolder;

		if (null == convertView) {

			Log.d(TAG, "creating new view and holder for position " + position);

			view = LayoutInflater.from(context).inflate(R.layout.note_list_item, null);
			viewHolder = new ViewHolder((TextView) view.findViewById(R.id.date), (TextView) view.findViewById(R.id.title));
			view.setTag(viewHolder);

		} else {

			Log.d(TAG, "reusing view and holder for position " + position);

			view = convertView;
			viewHolder = (ViewHolder) convertView.getTag();

		}

		final Note note = getItem(position);
		viewHolder.dateView.setText(dateUtility.dateString(note.getDate()));
		viewHolder.titleView.setText(note.getTitle());

		return view;

	}

	@Override
	public long getItemId(int position) {
		return super.getItem(position).getId();
	}

	private class ViewHolder {

		private final TextView dateView;
		private final TextView titleView;

		private ViewHolder(TextView dateView, TextView titleView) {
			this.dateView = dateView;
			this.titleView = titleView;
		}

	}

}
