package com.example.subramanian.notetakerapp;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class acts as an adaptor to bind the notes fetched from the database, to the RecyclerView
 * in MainActivity class.
 * It primarily uses onCreateViewHolder() and onBindViewHolder() methods of the RecyclerView class
 * to create a new RecyclerView in MainActivity class and populates it with data fetched from Database.
 * The associated xml is note_card_layout.xml, which defines the structure of the recycler view.
 *
 * @author Subramanian Arunachalam
 * @see android.support.v7.widget.RecyclerView.Adapter
 * @see android.view.LayoutInflater
 * @see android.view.ViewGroup
 * @version 1.0
 * @since 28th March, 2016
 */
public class NoteAdaptor extends RecyclerView.Adapter<NoteAdaptor.ViewHolder>{
    /*Declaring class variables*/
    /*** ViewHolder and LayoutInflater code referred from http://developer.android.com/reference/android/support/v7/widget/RecyclerView.ViewHolder.html ***/
    List<NoteModel> notesList = new ArrayList<NoteModel>();

    /**
     * Parametrized constructor for saving notesList
     */
    public NoteAdaptor(List<NoteModel> notesList){
        this.notesList=notesList;
    }

    /**
     * This method creates a new ViewHolder and populates data from a ViewGroup parent
     *
     * @param parent of type ViewGroup
     * @param viewType of type int
     * @return vh of type ViewHolder
     */
    @Override
    public NoteAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card_layout, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    /**
     * This method binds each note fetched from the database to the view holder
     *
     * @param holder of type ViewHolder
     * @param position of type int
     */
    @Override
    public void onBindViewHolder(NoteAdaptor.ViewHolder holder, int position) {
        holder.details.setText(notesList.get(position).noteContent);
        holder.details.setBackgroundColor(generateColor());
    }

    /**
     * Return size of the note array list
     *
     * @return int
     */
    @Override
    public int getItemCount() {
        return notesList.size();
    }

    /**
     * Inner class to populate the child view within a recycler view with note details.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /*Declaring class variables*/
        ImageView imageView;
        TextView details;

        /**
         * Parametrized constructor to bind class variables with note_card_layout xml fields
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.imageview);
            details=(TextView)itemView.findViewById(R.id.note_display);
        }
    }

    /*Static variable to ensure that the next note is not given the same color */
    static int lastNum=-1;

    /**
     * Generates a list of 5 colors and returns a random color to the calling method.
     * lastNum static variable ensures that the same color is not returned twice.
     *
     * @return int
     */
    public int generateColor(){
        int next=0;

        /*Creating and populating array list of colors*/
        List<Integer> colors= new ArrayList<Integer>();
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.LTGRAY);
        colors.add(Color.CYAN);
        colors.add(Color.WHITE);

        /*Generating a random number between 0 and 4*/
        do{
            Random random = new Random();
            next=random.nextInt(5);
        }

        /*Checking if new color is not the same as the previous color*/
        while(next==lastNum);

        lastNum=next;
        return colors.get(next);
    }
}
/*End of NoteAdaptor class*/