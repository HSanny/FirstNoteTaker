package com.example.u6490813.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {
    // alt + enter to create constructors
//    public NoteAdapter(@NonNull Context context, int resource) {
//        super(context, resource);
//    }

//    public NoteAdapter(@NonNull Context context, int resource, int textViewResourceId) {
//        super(context, resource, textViewResourceId);
//    }

    // cos we need to work with 3 arguments so choose only this one
    public NoteAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Note> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        // first check that `convertView` is null or not
        if(convertView == null){
            // create convertView ourselves
            // second param: null at this point because not doing advanced stuff
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note,null);
        }
        // put each note applicate into position in the listView
        Note note = getItem(position);
        if(note != null){
            // continue building up listView
            TextView tvDate = (TextView) convertView.findViewById(R.id.list_note_date);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.list_note_title);
            TextView tvContent = (TextView) convertView.findViewById(R.id.list_note_content);
            // setUp title text, from the Note item is passed to this editor
            tvDate.setText(note.getDateTimeAsString(getContext()));
            tvTitle.setText(note.getmTitle());
            // need edition cos if content is long, substring is needed
            tvContent.setText(note.getmContent());
            if(note.getmContent().length() > 50){
                // if long content, it will not populate the whole area of the listView, only 50 char
                tvContent.setText(note.getmContent().substring(0,50));
            }
            else{
                tvContent.setText(note.getmContent());
            }
        }
        return convertView;
    }


       //    public NoteAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull Note[] objects) {
//        super(context, resource, textViewResourceId, objects);
//    }

//    public NoteAdapter(@NonNull Context context, int resource, @NonNull List<Note> objects) {
//        super(context, resource, objects);
//    }

//    public NoteAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Note> objects) {
//        super(context, resource, textViewResourceId, objects);
//    }
}
