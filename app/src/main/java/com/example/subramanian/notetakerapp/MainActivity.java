package com.example.subramanian.notetakerapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * View class for NoteTakerApp.
 * This class acts as the entry point for the NoteTakerApp.
 * The content view is set to activity_main.xml.
 * Its primary purpose is to display stored notes to the user, once the app is started; and to
 * provide 4 buttons to the user, for creating, editing, viewing and deleting a note.
 *
 * @author Subramanian Arunachalam
 * @see android.app.Activity
 * @see android.support.v7.widget.CardView
 * @see android.support.v7.widget.LinearLayoutManager
 * @see android.support.v7.widget.RecyclerView
 * @version 1.0
 * @since 28th March, 2016
 */
public class MainActivity extends AppCompatActivity {
    /* Declaring the Class variables */
    /*** Lists, card layout and recycler view taken from http://developer.android.com/training/material/lists-cards.html ***/
    Button createNoteButton;
    Button viewNoteButton;
    Button editNoteButton;
    Button deleteNoteButton;
    RecyclerView recycler;
    Button viewNoteCountButton; //New Addition for lab demo
    LinearLayoutManager manager;
    int clickPosition;
    CardView card;
    Boolean noteSelected=false;
    int existingColor;
    int selectPosition=-1;
    List<NoteModel> savedNotes;
    NoteAdaptor adapter;
    TextView defMessage;

    /**
     * On create method for MainActivity
     * Handles all MainActivity operations.
     * Calls methods of NoteController class for all note operations.
     *
     * @param savedInstanceState of type Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Mapping class variables to xml fields*/
        createNoteButton=(Button) findViewById(R.id.create);
        viewNoteButton=(Button) findViewById(R.id.view);
        editNoteButton=(Button) findViewById(R.id.edit);
        deleteNoteButton=(Button) findViewById(R.id.delete);
        defMessage=(TextView) findViewById(R.id.defaultMessage);
        viewNoteCountButton=(Button) findViewById(R.id.viewCount);

        /*By default, view, edit and delete note buttons would be disabled. They will be enabled when user selects a note*/
        viewNoteButton.setEnabled(false);
        editNoteButton.setEnabled(false);
        deleteNoteButton.setEnabled(false);
        defMessage.setEnabled(false);

        /*Fetching all notes*/
        savedNotes = loadNotes();

        /*Mapping recycler view to xml*/
        recycler=(RecyclerView) findViewById(R.id.noteDisplay);
        recycler.setHasFixedSize(true);

        /*Mapping card view to xml*/
        card=(CardView) findViewById(R.id.card_view);

        /*Creating a linear layout manager and assigning it to the recycler*/
        manager = new LinearLayoutManager(this);
        recycler.setLayoutManager(manager);

        /*Calling the parametrized constructor of NoteAdapter class with savedNotes*/
        adapter = new NoteAdaptor(savedNotes);
        recycler.setAdapter(adapter);

        /**
         * Item touch listener for recycler view.
         * Determines which note is selected, sets border color to black.
         * On clicking a selected note, the border color and position values are reset.
         */
        recycler.addOnItemTouchListener(new RecyclerListener(getApplicationContext(), recycler, new RecyclerListener.OnNoteClickListener() {
            @Override
            public void onSingleClick(View view, int position) {
                /*When a note is selected. view, edit and delete note buttons are enabled*/
                clickPosition = position;
                editNoteButton.setEnabled(true);
                deleteNoteButton.setEnabled(true);
                viewNoteButton.setEnabled(true);

                /*On selecting a note, the border color is set to black and position is noted */
                if (!noteSelected) {
                    existingColor = view.getDrawingCacheBackgroundColor();
                    view.setBackgroundColor(Color.BLACK);
                    noteSelected = true;
                    selectPosition = position;
                } else {
                    /*On de-selecting a note, the border color and position are reset*/
                    if (position == selectPosition) {
                        view.setBackgroundColor(existingColor);
                        noteSelected = false;
                        selectPosition = -1;
                        editNoteButton.setEnabled(false);
                        deleteNoteButton.setEnabled(false);
                        viewNoteButton.setEnabled(false);
                    }
                }
            }
        }));

        /**
         * On click listener for View Note button
         * Creates a new intent, adds request type as '1' and starts the activity
         */
        viewNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteController.class);
                int listId = savedNotes.get(clickPosition).getNoteId();
                intent.putExtra("ID", listId);
                intent.putExtra("TYPE", 1);

                MainActivity.this.startActivity(intent);
            }
        });

        /**
         * On click listener for Create Note button
         * Creates a new intent and starts the activity
         */
        createNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteController.class);
                MainActivity.this.startActivity(intent);
            }
        });

        /**
         * On click listener for Edit Note button
         * Creates a new intent, adds request type as '2' and starts the activity
         */
        editNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteController.class);
                int listId = savedNotes.get(clickPosition).getNoteId();
                intent.putExtra("ID", listId);
                intent.putExtra("TYPE", 2);

                MainActivity.this.startActivity(intent);
            }
        });

        /**
         * On click listener for Delete Note button
         * Fetches required note from list
         * Calls deleteNote() method
         * Resets selected note position and disables edit, view and delete buttons
         * Refreshes the recycler view
         */
        deleteNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteController controllerObject = new NoteController();
                Context context = getApplicationContext();

                int listId = savedNotes.get(clickPosition).getNoteId();

                noteSelected = false;
                selectPosition = -1;
                editNoteButton.setEnabled(false);
                deleteNoteButton.setEnabled(false);
                viewNoteButton.setEnabled(false);

                int result=controllerObject.deleteNote(listId, context);

                /*Checking result*/
                if(result==1){
                    Toast toast = Toast.makeText(context, "Note Deleted Successfully", Toast.LENGTH_SHORT);
                    toast.show();

                    /*Refreshing the recycler view and notifies the adaptor of a data set change*/
                    savedNotes=loadNotes();
                    adapter=new NoteAdaptor(savedNotes);
                    recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                else{
                    Toast toast = Toast.makeText(context, "Error in deleted note", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        /**
         * New addition for lab demo
         * On click, displays a toast with the available notes
         */
        viewNoteCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Calling on resume method to refresh notes list*/
                onResume();

                /*Obtaining note size*/
                int noteSize = savedNotes.size();

                /*Displaying it in a toast*/
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Number of notes available: " + noteSize, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    /**
     * This method is currently not used
     *
     * @param menu of type Meny
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * This method is currently not used
     *
     * @param item of type MenuItem
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to load notes from database to savedNotes class variable
     * Enables or disables the default message based on number of notes returned
     *
     * @return Array List of type NoteModel
     */
    public List<NoteModel> loadNotes(){
        List<NoteModel> savedNotes = new ArrayList<NoteModel>();

        Context context = getApplicationContext();
        DbController dbObject = new DbController(context);

        savedNotes=dbObject.fetchAllNotes();

        /*Reversing list to display the most recent note first*/
        Collections.reverse(savedNotes);

        if(savedNotes.isEmpty()){
            defMessage.setEnabled(true);
        }
        else{
            defMessage.setEnabled(false);
        }
        return savedNotes;
    }

    /**
     * This method is called when the recycler view's data set needs to be refreshed
     */
    @Override
    protected void onResume() {
        super.onResume();

        savedNotes=loadNotes();
        adapter=new NoteAdaptor(savedNotes);
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
/*MainActivity class ends here*/