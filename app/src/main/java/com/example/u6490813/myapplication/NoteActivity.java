package com.example.u6490813.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class NoteActivity extends AppCompatActivity {

    private Button btnAdd;
    private EditText etTitle,etContent;

    //private Animation in = new AlphaAnimation(0.0f, 1.0f); // text fades in, fades out is reversed value
    //private Animation out = new AlphaAnimation(1.0f,0.0f)
    //AnimationSet as = new AnimationSet(true);

    // a variable to hold the fileName we getting from the main Activity and send to this noteActivity
    private String mFileName;
    // create this because using the same NoteActivity to create a new note to show also the previously created note
    private Note mLoadedNote = null;

    private boolean mIsViewingOrUpdating; // statement of activity
    private long mNoteCreationTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        //btnAdd = (Button) findViewById(R.id.saveNote);
        etTitle = (EditText) findViewById(R.id.note_et_title);
        etContent = (EditText) findViewById(R.id.note_et_content);

        // check if we have any anything in the bundle that created this activity
        // pass stringExtra passed from MainActivity noteHandlers
        // now know that have a file to be loaded and displayed at the activity
        mFileName = getIntent().getStringExtra(Utilities.EXTRAS_NOTE_FILENAME);
        // then safety check if we really have this file
        if(mFileName != null && !mFileName.isEmpty() && mFileName.endsWith(Utilities.FILE_EXTENSION)){
            // then load this note = mLoadNoteFromFileName
            mLoadedNote = Utilities.loadNoteByName(getApplicationContext(),mFileName);
            // check if really can load the note
            if(mLoadedNote != null){
                etTitle.setText(mLoadedNote.getmTitle());
                etContent.setText(mLoadedNote.getmContent());
                mNoteCreationTime = mLoadedNote.getmDateTime();
                mIsViewingOrUpdating = true;
                // since there's two possibilities
                // go to save note, save a new note or save a already loaded note
            }else{
                // user wants to create new note
                mNoteCreationTime = System.currentTimeMillis();
                mIsViewingOrUpdating = false;
            }
        }
        //out.setDuration(3000);
        //in.setDuration(2000);
        //as.addAnimation(out);
        //in.setStartOffset(3000);
        //as.addAnimation(in);
        //out.setRepeatCount(Animation.INFINITE);
        //out.setRepeatMode(Animation.REVERSE);
    }
    public void note_cancel_button(View view){
        finish();
       // cancel();
    }

    public void note_save_button(View view){
        saveNote();
    }
    public void note_delete_button(View view) {
        deleteNote();
    }
    public void note_clear_button(View view){
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        Animation out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        //Animation in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)){
            etTitle.startAnimation(out);
            etContent.startAnimation(out);
            etTitle.setText(null);
            etContent.setText(null);
        }
        else if(!TextUtils.isEmpty(title)){
            etTitle.startAnimation(out);
            etTitle.setText(null);
        }
        else if(!TextUtils.isEmpty(content)){
            etContent.startAnimation(out);
            etContent.setText(null);
            //etContent.setText(null);
        }
        else{
            Snackbar.make(view,"Fields Are Already Empty",Snackbar.LENGTH_SHORT).show();
        }
    }
    private void cancel() {
        // To-Do
    }

    private void deleteNote() {
        // some more need to warn the user if he really wants to delete this file
        // so need a dialect that has a yes or no button
         AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this)
                 .setTitle("Confirmed To Delete File ?")
                 .setMessage("About To Delete " + mLoadedNote.getmTitle())
                 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int which) {
                         if(mLoadedNote != null){
                             Utilities.deleteNoteByName(getApplicationContext(),mFileName);
                             Toast.makeText(NoteActivity.this,mLoadedNote.getmTitle()+ "Deleted Successfully",Toast.LENGTH_SHORT).show();
                         }else{
                             Toast.makeText(NoteActivity.this,mLoadedNote.getmTitle() + "File Cannot Be Deleted",Toast.LENGTH_SHORT) .show();
                         }
                         finish();
                     }
                 })
                 // just set to null, don't want to handle null case, for no button just do nothing
                 .setNegativeButton("No",null);
                 // make this dialog not cancellable
                 //.setCancelable(false);
         dialogDelete.show();
            // same as loadNoteByName
            //Utilities.deleteNoteByName(getApplicationContext(),mLoadNoteFromFileName.getmDateTime() + Utilities.FILE_EXTENSION);

    }
    private void saveNote(){
        Note note;
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        // note object being saved will be either a new note or a old existing note
        // new note = get Current time as new note's name
        // old note = dateTime of the old note itself
        if(title.isEmpty()){
            Toast.makeText(NoteActivity.this,"Please Enter A Title",Toast.LENGTH_SHORT).show();
            return;
        }
        if(content.isEmpty()){
            Toast.makeText(NoteActivity.this,"Please Enter Some Content",Toast.LENGTH_SHORT).show();
            return;
        }
        //set the creation time, if new note, now, otherwise the loaded note's creation time
        if(mLoadedNote == null){
            // if loaded note is null, then user wants to create new note
            //note = new Note(System.currentTimeMillis(),title,content);
            mNoteCreationTime = System.currentTimeMillis();
        }
        else{
            // user wants to create a new note but he wants to either view or edit a already saved note
            // since we are using time as fileName
            // for the note to be saved, if the loaded note is not done
            // pass first argument from the getDateTime of the old note
            // such that the fileName will be in sync with the old note itself
           // note = new Note(mLoadedNote.getmDateTime(),title,content);
            mNoteCreationTime = mLoadedNote.getmDateTime();
        }

        if(Utilities.saveNote(this,new Note(mNoteCreationTime,title,content))){ // cos activity is the context in some way...
            // if result is true, make a Toast: toast is a simple notification for user
            Toast.makeText(this,"Note Is Successfully Saved!",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"Note Saved Failed, Please Make Sure Device Has Enough Space", Toast.LENGTH_SHORT).show();
        }
        // in anyway we want to finish the activity
        finish();
    }
}



    /*
     * obtain references to Button widget and set up listener
     * @param view the basic building block of GUI
      *
      * btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = etContent.getText().toString().trim();

                if (!TextUtils.isEmpty(content)){
                    tvContent.setText(content);
                }
                else{
                    // else set textView to empty and pop-up feedback bar to user
                    //tv.setText(null);
                    Snackbar.make(view,"Fill in empty fields",Snackbar.LENGTH_SHORT).show();
                }
            }
        });*/
