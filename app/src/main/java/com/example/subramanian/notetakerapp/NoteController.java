package com.example.subramanian.notetakerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Controller class for NoteTakerApp
 * Content view is set to note_operations.xml
 * All note operations: create, view, edit and delete are implemented here.
 *
 * @author Subramanian Arunachalam
 * @version 1.0
 * @see android.support.design.widget.FloatingActionButton
 * @since 28th March, 2016
 */
public class NoteController extends AppCompatActivity {
    /*Declaring class variables*/
    /*** Floating action button help referred from http://developer.android.com/reference/android/support/design/widget/FloatingActionButton.html#q=email%20intent ***/
    EditText noteName;
    EditText noteContent;
    Button submitButton;
    Button resetButton;
    Button cancelButton;
    Toast toast;
    final int DURATION=Toast.LENGTH_SHORT;
    DbController dbObject;
    int listPosition;
    NoteModel noteObjectFetched;
    boolean isCreate=true;
    FloatingActionButton emailButton;

    /**
     * onCreate method for NoteController class.
     * Handles all note operations.
     * @param savedInstanceState of type Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_operations);

        /*Mapping class variables to xml fields*/
        noteName=(EditText) findViewById(R.id.name);
        noteContent=(EditText) findViewById(R.id.body);
        submitButton=(Button) findViewById(R.id.submit);
        resetButton=(Button) findViewById(R.id.reset);
        cancelButton=(Button) findViewById(R.id.cancel);
        emailButton = (FloatingActionButton) findViewById(R.id.fab);

        /*Checking the saved instance state*/
        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras!=null){
                /*Getting position of the selected note*/
                listPosition=extras.getInt("ID");
                /*Getting type of request 1 for view note, 2 for edit note*/
                int type=extras.getInt("TYPE");
                if(type==1){
                    fetchNoteProcess();
                    displayNote();
                }
                else{
                    fetchNoteProcess();
                    isCreate=false;
                }
            }
        }

        /*Checking if the request is not a create request*/
        if(!isCreate){
            noteName.setText(noteObjectFetched.getNoteName().toString());
            noteContent.setText(noteObjectFetched.getNoteContent().toString());
        }
        /**
         * On click listener for submit button
         * This method firstly checks if it is a create new note request or update existing note request.
         * Displays a warning if any field is left blank. But will create a blank note nevertheless.
         */
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*Checking if the request is for updating an existing note*/
                if(!isCreate){
                    /*Checking if the field is blank*/
                    if(noteName.getText()!=null && !"".equalsIgnoreCase(noteName.getText().toString().trim())){
                        noteObjectFetched.setNoteName(noteName.getText().toString());
                    }
                    else{
                        /*Displaying a warning message*/
                        Context context = getApplicationContext();
                        toast = Toast.makeText(context, "WARNING. Note name is blank.", DURATION);
                        toast.show();
                    }

                    /*Checking if the field is blank*/
                    if(noteContent.getText()!=null && !"".equalsIgnoreCase(noteContent.getText().toString().trim())){
                        noteObjectFetched.setNoteContent(noteContent.getText().toString());
                    }
                    else{
                        /*Displaying a warning message*/
                        Context context = getApplicationContext();
                        toast = Toast.makeText(context, "WARNING. Note content is blank", DURATION);
                        toast.show();
                    }

                    /*Calling editNote() method*/
                    int result=editNote(noteObjectFetched);

                    Context context = getApplicationContext();
                    /*Checking and displaying success/failure as a toast*/
                    if(result==1){
                        toast = Toast.makeText(context, "Note updated Successfully", DURATION);
                        toast.show();
                        finish();
                    }
                    else{
                        toast = Toast.makeText(context, "Error in saving note", DURATION);
                        toast.show();
                    }
                }
                /*If request is for creating a new note*/
                else{
                    NoteModel noteObject = new NoteModel();

                    /*Checking if the field is blank*/
                    if(noteName.getText()!=null && !"".equalsIgnoreCase(noteName.getText().toString().trim())){
                        noteObject.setNoteName(noteName.getText().toString());
                    }
                    else{
                        /*Displaying a warning message*/
                        Context context = getApplicationContext();
                        toast = Toast.makeText(context, "WARNING. Note name is blank.", DURATION);
                        toast.show();
                    }

                    /*Checking if the field is blank*/
                    if(noteContent.getText()!=null && !"".equalsIgnoreCase(noteContent.getText().toString().trim())){
                        noteObject.setNoteContent(noteContent.getText().toString());
                    }
                    else{
                        /*Displaying a warning message*/
                        Context context = getApplicationContext();
                        toast = Toast.makeText(context, "WARNING. Note content is blank", DURATION);
                        toast.show();
                    }

                    /*Calling createNote() method*/
                    boolean check = createNote(noteObject);

                    Context context = getApplicationContext();

                    /*Checking and displaying success/failure as a toast*/
                    if(check){
                        Toast toast = Toast.makeText(context, "Note Saved Successfully", DURATION);
                        toast.show();
                        finish();
                    }
                    else {
                        Toast toast = Toast.makeText(context, "Error in saving note", DURATION);
                        toast.show();
                    }
                }
            }
        });

        /*On click listener for reset button. Sets note name and note content fields to blank and finishes activity*/
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                noteName.setText("");
                noteContent.setText("");
                finish();
            }
        });

        /*On click listener for reset button. Sets note name and note content fields to blank*/
        resetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                noteName.setText("");
                noteContent.setText("");
            }
        });

        /**
         * OnClickListener for email floating action button
         * Creates a new intent object and populates it with pre-defined email address, subject and body.
         * In case an email cannot be sent, the note can be sent via text message.
         * The device would prompt the user in such a scenario.
         */
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*** Email has been referred from http://developer.android.com/guide/components/intents-common.html#Email ***/
                String address[] = new String[1];
                /*Adding a test email address*/
                address[0] = "u5683507@anu.edu.au";
                Intent intent = new Intent(Intent.ACTION_SEND);
                /*Setting explicit MIME type*/
                intent.setType("*/*");
                /*Adding components for the email message*/
                intent.putExtra(Intent.EXTRA_EMAIL, address);
                intent.putExtra(Intent.EXTRA_SUBJECT, noteObjectFetched.getNoteName());
                intent.putExtra(Intent.EXTRA_TEXT, noteObjectFetched.getNoteContent());

                /*Starting the email activity*/
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Method to add a new note to the database.
     * Calls the addNote() method of the DbController class.
     *
     * @param noteObject of type NoteModel
     * @return boolean for database failure/success message
     */
    public boolean createNote(NoteModel noteObject) {
        Context context = getApplicationContext();
        DbController dbObject = new DbController(context);

        boolean result = dbObject.addNote(noteObject);
        return result;
    }

    /**
     * Method to fetch an existing note
     * Populates the noteObjectFetched class variable with the value from fetchNote() method in DbController class.
     */
    public void fetchNoteProcess(){
        Context context = getApplicationContext();
        dbObject=new DbController(context);

        noteObjectFetched=dbObject.fetchNote(listPosition);
    }

    /**
     * Method to edit an existing note in the database.
     * Calls the updateNote() method in DbControl class
     *
     * @param noteObject of type NoteModel
     * @return int for database success/failure message
     */
    public int editNote(NoteModel noteObject){
        Context context = getApplicationContext();
        dbObject=new DbController(context);

        int result=dbObject.updateNote(noteObject);
        return result;
    }

    /**
     * Method to delete an existing note from the database.
     * Calls the deleteNote() method in DbControl class
     * NOTE: the context has been passed as a parameter from MainActivity.
     *
     * @param listId of type int
     * @param context of type Context
     * @return int for database success/failure message
     */
    public int deleteNote(int listId, Context context){
        DbController dbObject = new DbController(context);

        int result=dbObject.deleteNote(listId);
        return result;
    }

    /**
     * Method to populate note name and note content fields for viewing and editing a note.
     * Also disables edit text fields, reset and submit button for view note.
     */
    public void displayNote(){
        noteName.setText(noteObjectFetched.getNoteName().toString());
        noteContent.setText(noteObjectFetched.getNoteContent().toString());
        noteName.setFocusable(false);
        noteContent.setFocusable(false);
        resetButton.setEnabled(false);
        submitButton.setEnabled(false);
    }
}
/*NoteController ends here*/