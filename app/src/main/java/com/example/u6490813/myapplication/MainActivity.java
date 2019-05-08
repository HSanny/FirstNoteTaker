package com.example.u6490813.myapplication;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.u6490813.myapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * MainActivity.java - a simple class for demonstrating the main activity for a note application
 * @ author - Sanny,Hong Jiajia
 * @ uniID - u6490813
 */

public class MainActivity extends AppCompatActivity {

    private ListView mListViewNotes;

    /*private String mNoteToDelName;
    private Note mLoadNoteToDel;*/

    /*
     * call super class onCreate to recover state information
     * set Use Interface layout
     * @param savedInstanceState a Bundle of state information
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListViewNotes = (ListView) findViewById(R.id.main_listView_notes);
    }

    /*
     * obtain references to Button widget and set up listener
     * @param view the basic building block of GUI */
    public void buttonPress(View view){
        TextView tv = (TextView) findViewById(R.id.textView);
        EditText et = (EditText) findViewById(R.id.editText);
        String text = et.getText().toString().trim();
        if(!TextUtils.isEmpty(text)){
            // if input text is not empty, set text into textView and clear editText
            tv.setText(text);
            et.getText().clear();
        }
        else{
            // else set textView to empty and pop-up feedback bar to user
            tv.setText(null);
            Snackbar.make(view,"Fill in empty field",Snackbar.LENGTH_SHORT).show();
        }
    /* to populate the listVie is that when the app is under our resume
     * because if have seen the diagram for the Android activity life cycle
     * our resume is always called then you search it in app and everything goes up */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // find the menu from the resources and inflate it in the activity
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    // basically same function as the add Button but by another means
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_main_create_new_note:
                // start noteActivity in newNote mode
                Intent newNoteActivity = new Intent(this,NoteActivity.class);
                startActivity(newNoteActivity);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void addButton(View view){
        // call up activity with intent
        // @param: 1st = context already in it, 2nd = name of the class
        Intent newNoteActivity = new Intent(this, NoteActivity.class);
        startActivity(newNoteActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // listView work with concept ```Adapters   ```
        // first set adapter to null because currently don't have any adapter
        // next is to implement this adapter for this listView of nodes
        mListViewNotes.setAdapter(null);
        // load notes from the private storage area that we saved them
        // context : past `this`
        // because in Android, activity and context are basically the same thing in most area
        ArrayList<Note> notes = Utilities.loadAllSavedNotes(getApplicationContext());
        // then check if we have any note
        // because in Utilities.loadAllSavedNotes catch statement
        // if something is going wrong then return null
        // so check it here in the mainActivity otherwise getting null reference exception
        // check if notes is null or have no note at all
        if (notes == null || notes.size() == 0) {
            Toast.makeText(this, "There Is No Saved Note", Toast.LENGTH_LONG).show();
            //return; // just return cos there's nothing to do here
        } else {
            // if have notes ArrayList that get from the Utilities class
            // context: this == getApplicationContext == getBaseContext
            // int: which layout to fill up for us, rmb this is the convert view
            // last object is the object that we loaded from Utilities
            // here when first apply, need to change 3rd param from Note[] to ArrayList<Note> Objects
            final NoteAdapter na = new NoteAdapter(this, R.layout.item_note, notes);
            // last thing to do is to set the Adapter to the mListViewsNotes
            mListViewNotes.setAdapter(na);
            mListViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // since we store the file as dateTime with ".bin"
                    String fileName = ((Note) mListViewNotes.getItemAtPosition(position)).getmDateTime() + Utilities.FILE_EXTENSION;
                    // now need a method that loads a file and cast it to a note and gives it back to ues
                    // so go back to utilities to create loadOneNote method
                    // however it's better to load the note in the activity that we are going to open when the note is clicked
                    // since inside an anonymous method, then don't use `this` to refer to context
                    // if say this, then this will refer to this will refer to the onItemClick function
                    Intent viewNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                    // then add some data, pass data between activity
                    // then later in the new activity that we go into we can see what is the name of the file
                    // and startActivity that we want to create
                    viewNoteIntent.putExtra(Utilities.EXTRAS_NOTE_FILENAME, fileName);
                    startActivity(viewNoteIntent);
                    // then go to NoteActivity to create `showNote` facility
                }
            });
            final SwipeDetector swipeDetector = new SwipeDetector();
            mListViewNotes.setOnTouchListener(swipeDetector);
            mListViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id){
                    if (swipeDetector.swipeDetected()){
                        if (swipeDetector.getAction() == SwipeDetector.Action.Right_To_Left || swipeDetector.getAction() == SwipeDetector.Action.Left_To_Right) {
                            final String mNoteToDelName = ((Note) mListViewNotes.getItemAtPosition(position)).getmDateTime()
                                    + Utilities.FILE_EXTENSION;
                            AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Confirmed To Delete File ?")
                                    .setMessage("About To Delete " + mNoteToDelName)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            Utilities.deleteNoteByName(getApplicationContext(),mNoteToDelName);
                                            Toast.makeText(MainActivity.this,mNoteToDelName+ "Deleted Successfully",Toast.LENGTH_SHORT).show();
                                            Intent newMainActivity = new Intent(MainActivity.this,MainActivity.class);
                                            startActivity(newMainActivity);
                                        }
                                    })
                                    // just set to null, don't want to handle null case, for no button just do nothing
                                    .setNegativeButton("No",null);
                            // make this dialog not cancellable
                            //.setCancelable(false);
                            dialogDelete.show();
                            //Utilities.deleteNoteByName(getApplicationContext(), mNoteToDelName);
                            //Toast.makeText(getApplicationContext(), "successfully deleted file" + mNoteToDelName, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        // double click to edit notes
                        mListViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // since we store the file as dateTime with ".bin"
                            String fileName = ((Note) mListViewNotes.getItemAtPosition(position)).getmDateTime() + Utilities.FILE_EXTENSION;
                                // now need a method that loads a file and cast it to a note and gives it back to ues
                                // so go back to utilities to create loadOneNote method
                                // however it's better to load the note in the activity that we are going to open when the note is clicked
                                // since inside an anonymous method, then don't use `this` to refer to context
                                // if say this, then this will refer to this will refer to the onItemClick function
                            Intent viewNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                                // then add some data, pass data between activity
                                // then later in the new activity that we go into we can see what is the name of the file
                                // and startActivity that we want to create
                            viewNoteIntent.putExtra(Utilities.EXTRAS_NOTE_FILENAME, fileName);
                            startActivity(viewNoteIntent);
                                // then go to NoteActivity to create `showNote` facility
                        }
                    });
                    }
                }
            });
        }
    }
}

    /*// next is to edit notes on the listView Screen
    // make event handler for notes
    protected void onDelete(){
        final SwipeDetector swipeDetector = new SwipeDetector();
        mListViewNotes.setOnTouchListener(swipeDetector);
        mListViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                if (swipeDetector.swipeDetected()) {
                    if (swipeDetector.getAction() == SwipeDetector.Action.Right_To_Left || swipeDetector.getAction() == SwipeDetector.Action.Left_To_Right) {
                        String mNoteToDelName = ((Note) mListViewNotes.getItemAtPosition(position)).getmDateTime()
                                + Utilities.FILE_EXTENSION;
                        Utilities.deleteNoteByName(getApplicationContext(), mNoteToDelName);
                        Toast.makeText(getApplicationContext(), "successfully deleted file" + mNoteToDelName, Toast.LENGTH_SHORT).show();
                    } else {
                    }
                }
            }
        });
    }
} }*/
