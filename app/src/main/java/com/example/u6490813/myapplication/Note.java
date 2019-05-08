package com.example.u6490813.myapplication;

import android.content.Context;

import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/* to put save functionality into the app
*  new concept: serialisation
*  Serialisation, in many programming language, when you have a object which is a class, usually do a serialisation
*  Which means that saving the current state of the object as a binary file and save it on the disk for later use
*  or sending it to our network.
*  The serialisation is just a concept of saving the state of the object to a usually binary file
*  How to do in java: first implements "Serializable" interface
*  this interface does not provide any method, just here for Java compiler or runtime would know that this object
*  is supposed to be serializable. -> Eg: save game during game playing
* */
public class Note implements Serializable {
    private long mDateTime;
    private String mTitle;
    private String mContent;

    public Note(long mDateTime, String mTitle, String mContent) {
        this.mDateTime = mDateTime;
        this.mTitle = mTitle;
        this.mContent = mContent;
    }
    public long getmDateTime() {
        return mDateTime;
    }
    public String getmTitle() {
        return mTitle;
    }
    public String getmContent() {
        return mContent;
    }

    public void setmDateTime(long mDateTime) {
        this.mDateTime = mDateTime;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getDateTimeAsString(Context context){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",context.getResources().getConfiguration().locale);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(mDateTime));
    }
}
