package com.example.subramanian.notetakerapp;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Testing class for NoteTakerApp.
 * This class acts as the source of all test cases for the NoteTakerApp.
 * Its primary purpose is to execute the mentioned test cases and return success for all.
 *
 * Logic for test cases is within comments
 *
 * @author Subramanian Arunachalam
 * @version 1.0
 * @since 28th March, 2016
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    /*Method to check if note ID generated is within range*/
    public void testIdGeneratedRange(){
        NoteModel object = new NoteModel();
        boolean check=false;

        if(object.getNoteId()>=0 && object.getNoteId()<=999)
            check=true;

        assertTrue(check);
    }

    /*Method to check if two NoteModel objects have the same generated Id*/
    public void testIdGeneratedEquality(){
        NoteModel object1 = new NoteModel();
        NoteModel object2 = new NoteModel();
        boolean check=true;

        if(object1.getNoteId()== object2.getNoteId())
            check=false;

        assertTrue(check);
    }

    /*Method to check if two note colors generated one after another are not the same*/
    public void testNoteColorEquality(){
        List<NoteModel> dummyList = new ArrayList<>();
        NoteAdaptor object = new NoteAdaptor(dummyList);
        int color1=object.generateColor();
        int color2=object.generateColor();

        assertTrue(color1!=color2);
    }

    public void testInputFieldValidation(){
        /*I planned to run the app, try to create a new note and press the submit button without
        entering note name and note content*/
        /*The test case should pass if the db returns an error*/
    }

    public void testNoNotesPresent(){
        /*I planned to run the app with an empty database and check if the default message
        * "No Notes Available" gets displayed. */
        /*The test case should pass if this message is displayed*/
    }
    public void testNoteColors(){
        /*I planned to check if two consecutive notes have the same background color*/
        /*The test should pass if all consecutive notes have different colors*/
    }

    public void testButtonDisabledMainScreen(){
        /*Once the app starts and all the notes available are displayed, the View Note,
        * Edit Note and Delete Note buttons should be disabled.*/
        /*They should get enabled once a note is selected and disabled when de-selected*/
        /*The test case should pass if this behavior is correct*/
    }

    public void testButtonDisabledSecondScreen(){
        /*Once a note is selected and the View Note button is pressed, a new screen with the
        * note details must be shown, with Submit and Reset buttons disabled*/
        /*The Edit Text fields cannot be edited at this stage*/
        /*The test case should pass if this behavior is correct*/
    }

    public void testDatabaseUpdateOperations(){
        /*All database edit and delete operations must return the integer 1 as the result, which
        * indicates that only one row was affected in the database*/
        /*The test case should pass if exactly 1 is returned*/
    }

    public void testEmailAlternative(){
        /*On view note screen, if the email button at the bottom is clicked, the device's default
        * email client must open and populate the 'To', 'Subject' and 'Body' fields*/
        /*If this option is not available, the device must prompt the user if instead a text
        * message can be sent*/
        /*The test case should pass if this behavior is observed*/
    }
}
/*ApplicationTest class ends here*/