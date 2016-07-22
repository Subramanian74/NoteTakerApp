package com.example.subramanian.notetakerapp;

import java.util.Random;

/**
 * Model class for NoteTakerApp
 * Declares 3 protected class variables: noteId, noteName and noteContent.
 * Contains getters and setters only.
 * Generates the noteId through a random integer, with value betwee 0 and 999.
 *
 * @author Subramanian Arunachalam
 * @version 1.0
 * @since 28th March, 2016
 */
public class NoteModel {
    /*Declaring class variables*/
    protected int noteId;
    protected String noteName;
    protected String noteContent;

    /*Default constructor to generate a random integer between 0 and 999; and allocate it to noteId*/
    public NoteModel(){
        Random randomInteger =  new Random();
        this.noteId=randomInteger.nextInt(1000);
    }

    /*Parametrized constructor to initialize noteName and noteContent*/
    public NoteModel(String noteName, String content){
        this.noteName=noteName;
        this.noteContent=content;
    }

    /*Getters and Setters for noteId, noteName and noteContent*/
    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }
}
/*NoteModel class ends here*/