package com.example.subramanian.notetakerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

/**
 * This class acts as the Database controller class, to handle all database operations of NoteTakerApp.
 * Processes an insert query for creation of database, add a new note, fetch all stored notes,
 * fetch a single note, update an existing note and delete an existing note.
 * Uses SQLite database.
 *
 * @author Subramanian Arunachalam
 * @version 1.0
 * @see android.database.sqlite
 * @see android.content
 * @since 28th March, 2016
 */
public class DbController extends SQLiteOpenHelper {
    /*Declaring class variables and constants*/
    /*** Database setup and variables have been referred from http://developer.android.com/training/basics/data-storage/databases.html***/
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "noteTakerApp.db";
    private static final String TABLE_NAME = "notes";
    private static String TABLE_COLUMN_ID = "id";
    private static final String TABLE_COLUMN_NAME = "name";
    private static final String TABLE_COLUMN_BODY = "text";

    public static final String DB_CREATE_QUERY = "create table "+TABLE_NAME+" ( "+TABLE_COLUMN_ID+
            " integer primary key, "+TABLE_COLUMN_NAME+" text not null, "+TABLE_COLUMN_BODY+
            " text not null );";

    public static final String DB_SELECT_QUERY = "Select * from "+TABLE_NAME;

    /**
     * Parametrized constructor to set the context
     *
     * @param context of type Context
     */
    public DbController (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Method to execute the database create query, to create a new database on first-time use of the app.
     *
     * @param db of type SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_QUERY);
    }

    /**
     * Method to upgrade the database to a new version.
     * Currently unused.
     *
     * @param db of type SQLiteDatabase
     * @param oldVersion of type int
     * @param newVersion of type int
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    /**
     * Method to add a new note to the database.
     *
     * @param noteObject of type NoteModel
     * @return boolean value for success or failure
     */
    public boolean addNote(NoteModel noteObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        /*Fetching values from noteObject and populating it ion ContentValues*/
        ContentValues insertValues = new ContentValues();
        insertValues.put(TABLE_COLUMN_ID, noteObject.getNoteId());
        insertValues.put(TABLE_COLUMN_NAME, noteObject.getNoteName());
        insertValues.put(TABLE_COLUMN_BODY, noteObject.getNoteContent());

        /*Inserting value to the database*/
        long result=db.insert(TABLE_NAME, null, insertValues);
        db.close();

        /*Checking for success or failure*/
        if(result==-1)
            return false;
        else
            return true;
    }

    /**
     * Method to fetch a note from the database, based on the noteId.
     *
     * @param id of type int
     * @return NoteModel object
     */
    public NoteModel fetchNote(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        /*Fetching value from database*/
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+TABLE_COLUMN_ID+" = "+id+";", null);

        /*Moving to the first position in the cursor*/
        if(cursor != null){
            cursor.moveToFirst();
        }

        /*Populating the NoteModel object with the cursor's values*/
        NoteModel noteObject = new NoteModel(cursor.getString(1), cursor.getString(2));
        noteObject.setNoteId(id);
        return  noteObject;
    }

    /**
     * Method to fetch all notes from the database. Called when the app starts or a note is added/modified/deleted.
     *
     * @return ArrayList of type NoteModel
     */
    public List<NoteModel> fetchAllNotes(){
        List<NoteModel> notes = new ArrayList<NoteModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(DB_SELECT_QUERY, null);

        /*Traversing the cursor to fetch all values and adding it to a NodeModel object*/
        if(cursor.moveToFirst()){
            while(cursor.moveToNext()){
                NoteModel tempNote = new NoteModel();

                tempNote.setNoteId(Integer.parseInt(cursor.getString(0)));
                tempNote.setNoteName(cursor.getString(1));
                tempNote.setNoteContent(cursor.getString(2));

                /*Adding note object to the array list*/
                notes.add(tempNote);
            }
        }
        return notes;
    }

    /**
     * Method to update an existing note's name and content in the database, based on the noteId.
     *
     * @param noteObject of type NoteModel
     * @return int for value returned from database
     */
    public int updateNote(NoteModel noteObject){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_COLUMN_NAME, noteObject.getNoteName());
        values.put(TABLE_COLUMN_BODY, noteObject.getNoteContent());

        return db.update(TABLE_NAME, values, TABLE_COLUMN_ID+" =? ", new String[]{String.valueOf(noteObject.getNoteId())});
    }

    /**
     * Method to delete an existing note from the database, based on noteId
     *
     * @param noteId of type int
     * @return int for value returned from database
     */
    public int deleteNote(int noteId){
        SQLiteDatabase db = getWritableDatabase();
        int result=db.delete(TABLE_NAME, TABLE_COLUMN_ID+" =? ", new String[]{String.valueOf(noteId)});
        db.close();
        return result;
    }
}
/*DbController class ends here*/