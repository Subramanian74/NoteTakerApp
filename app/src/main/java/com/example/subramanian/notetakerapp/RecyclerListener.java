package com.example.subramanian.notetakerapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * This class acts as a listener to the RecyclerView note list
 * Its primary activity is to find the child view (individual note) when tapped/clicked once by the
 * user and return the position (x and y coordinates). This position is used to determine which
 * note has been clicked, so that it can be edited, viewed or deleted. It implements the
 * OnNoteClickListener and OnItemTouchListener interfaces, to determine the click location.
 *
 * @author Subramanian Arunachalam
 * @see android.support.v7.widget.RecyclerView.OnItemTouchListener
 * @see android.view.GestureDetector
 * @see android.view.MotionEvent
 * @version 1.0
 * @since 28th March, 2016
 */
public class RecyclerListener implements RecyclerView.OnItemTouchListener {
    /*** RecyclerView OnItemTouchListener code referred from http://developer.android.com/reference/android/support/v7/widget/RecyclerView.OnItemTouchListener.html ***/
    /*** Gesture Detector and Motion Event code referred from http://developer.android.com/reference/android/view/GestureDetector.html ***/

    /**
     * Interface containing the onSingleClick() method
     */
    public interface OnNoteClickListener{
        void onSingleClick(View view, int position);
    }

    /*Declaring class variables*/
    private OnNoteClickListener listener;
    private GestureDetector detector;

    /**
     * Method to fetch the child view clicked by the user. Uses a MotionEvent object to determine
     * the coordinates clicked on the screen.
     *
     * @param rv of type RecyclerView
     * @param e of type MotionEvent
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        return childView!=null && detector.onTouchEvent(e);
    }

    /**
     * Parametrized constructor which is called as soon as a click is detected on a note (child view).
     * Determines which note was clicked and actions to be taken.
     *
     * @param context
     * @param recyclerViewObject
     * @param listener
     */
    public RecyclerListener(Context context, final RecyclerView recyclerViewObject, final OnNoteClickListener listener){
        this.listener=listener;
        detector=new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View childView = recyclerViewObject.findChildViewUnder(e.getX(), e.getY());
                listener.onSingleClick(childView, recyclerViewObject.getChildAdapterPosition(childView));
                return true;
            }
        });
    }

    /**
     * Implemented method OnItemTouchListener interface
     * Currently unused
     *
     * @param rv of type RecyclerView
     * @param e of type MotionEvent
     */
    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    /**
     * Implemented method OnItemTouchListener interface
     * Currently unused
     *
     * @param disallowIntercept of type boolean
     */
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
/*End of RecyclerListener class*/