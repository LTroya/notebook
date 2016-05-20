package com.ltroya.notebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteAdapter extends ArrayAdapter<Note> {

    public static class ViewHolder{
        TextView noteTitle;
        TextView noteBody;
        ImageView noteIcon;
    }

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        super(context, 0, notes);
    }
    /**
     *
     * @param position position of the row in the list
     * @param convertView View associate with the row. When it get out of the view, android save it
     * @param parent it's the layout
     * @return convertView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Note note = getItem(position);

        // Create a new view holder
        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate a new
        // view in from custom row layout
        if (convertView == null) {
            // If we don't have a view that is being used create one, and make sure you create
            // a view holder along with it to save our view reference to.
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);

            // Set our views to our view holder so that we no longer have to go back and use find view
            // by id every time we have a new row
            viewHolder.noteTitle = (TextView) convertView.findViewById(R.id.text_item_note_title);
            viewHolder.noteBody = (TextView) convertView.findViewById(R.id.text_item_note_body);
            viewHolder.noteIcon = (ImageView) convertView.findViewById(R.id.image_item_note);

            convertView.setTag(viewHolder);
        } else {
            // We already have a view so just go to our viewHolder and grab the widgets from it
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.noteTitle.setText(note.getTitle());
        viewHolder.noteBody.setText(note.getMessage());
        viewHolder.noteIcon.setImageDrawable(note.getIconResource());

        // Now that we modified the view to display appropiate data,
        // return it so it will be displayed
        return convertView;
    }
}
