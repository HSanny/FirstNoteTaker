package com.example.u6490813.myapplication;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Utilities {
    // for saving a file ,need a file name
    // make the file a constant with an extension `.bin` for later possible edition of the extension of the file
    public static final String FILE_EXTENSION = ".bin";
    public static final String EXTRAS_NOTE_FILENAME = "EXTRAS_NOTE_FILENAME";

    /* @param: context is provided from activity, note is get from activit
     *  then create a file name, name part will be the datetime(11digits, the time in milliseconds)
     *  then the .bin for the extension for the file
     *  then use fos and oos to write the file to a private storage area for application
     * */
    public static boolean saveNote(Context context, Note note) { // save is successful or a failure
        // for saving a file ,need a file name
        // let unique file name be date time of the creation of the note
        String fileName = String.valueOf(note.getmDateTime()) + FILE_EXTENSION;

        /* now to write sth on the Android device(internal) storage
        the Android has internal and external
        internal: install an app and the Android API will give a unique space to the app
        hence don't need permission to write to ur own app area as storage*/

        FileOutputStream fos;
        ObjectOutputStream oos;  // serialization to binary

        try {
            // fos equals from the context, we can have the fos function or method
            // want a file output with a fileName and
            // a context(let the API know that we want this in the Private Mode) == in the private storage area of the app
            fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
            // use the oos and make a new object `oos` by passing the fos to it
            oos = new ObjectOutputStream(fos);
            oos.writeObject(note); // the object we want to write is `note`
            oos.close(); // close the oos
            //fos.close(); // closes them because they will consume resources on device

        } catch (Exception e) {
            // usual exception while doing this kind of stuff is I/O exception
            // usually nothing will go wrong unless not enough space on device
            e.printStackTrace(); // simply print the stack trace of the problem
            return false; // when sth happen just return false to nail down problem and tell users sth went wrong
        }

        // a method, let's return true, if something happens in the middle just return false
        return true;
    }

    /* Do the reverse of the `saveNote` method to load the note */

    public static ArrayList<Note> loadAllSavedNotes(Context context) {
        // to reverse the saveNote
        // first need a empty ArrayList
        ArrayList<Note> notes = new ArrayList<>();
        // get where the files are being saved, so need a file object and get it from the context
        File filesDir = context.getFilesDir();
        // need another list for the files that we have saved, fileNames are of type String
        ArrayList<String> noteFiles = new ArrayList<>();
        // go over all the files in the folder, if they're ended with .bin extension , then there're note files
        for (String file : filesDir.list()) {
            if (file.endsWith(FILE_EXTENSION)) {
                // add to notes ArrayList else do nothing with it
                noteFiles.add(file);
            }
        }
        // now that have a list of FileName, then individually load back
        // or deserialize them back to a note = oppose thing to saveNote
        // fileOutStream then fileInputStream
        // == ObjectAfterString and need ObjectInputStrem

        FileInputStream fis;
        ObjectInputStream ois;
        // then go in for all items in the list
        for (int i = 0; i < noteFiles.size(); i++) {// no. of elem in the arrayList
            // need a try-catch block because it's an I/O operation
            // and I/O operation always tend to fail
            try {
                // instantiate the fis and ois object
                fis = context.openFileInput(noteFiles.get(i));
                ois = new ObjectInputStream(fis);
                // then in the arrayList of the `notes` up there
                // just add a new item to this list and it will be the ois that read object
                // and it should be casted to Note
                // otherwise it does not have clue that what is the type of the object
                // always close the streams
                // and this goes forever until we have covered all the files and convert them back to a Note Object
                notes.add((Note) ois.readObject());
                fis.close();
                ois.close();
            }
            // another thing that can fail during this operation is `classNotFound Exception
            // because if the note class is not being found,
            // then the object input the string cannot cast back the content of the file to a note Class/note Object
            // catch them and name i
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace(); // can see what's going wrong in the debugger
                return null;
            }
        }
    // at the end, will have to return ArrayList notes Object
    // then go back to mainAct
        return notes;
}

    // get one note back by its name
    public static Note loadNoteByName(Context context,String fileName){
        // roughly same thing as loadAllSavedNotes
        File file = new File(context.getFilesDir(), fileName);
        // then check if the file exist (safety check)
        if(file.exists() && !file.isDirectory()){
            Log.v("UTILITIES","File Exist =" + fileName);
            FileInputStream fis;
            ObjectInputStream ois;
            try{// load the file
                fis = context.openFileInput(fileName);
                ois = new ObjectInputStream(fis);
                Note note = (Note) ois.readObject();
                fis.close();
                ois.close();
                return  note;
            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
                return null;
            }
        }else{
        return null;
        }
    }

    public static boolean deleteNoteByName(Context applicationContext, String fileName) {
        File dirFiles = applicationContext.getFilesDir();
        File fileToDel = new File(dirFiles, fileName);
        // check if the file actually exist
        if(fileToDel.exists() && !fileToDel.isDirectory()){
            fileToDel.delete();
        }
        return false;
    }
}
