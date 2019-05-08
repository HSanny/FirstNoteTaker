package com.example.u6490813.myapplication;

import android.view.MotionEvent;
import android.view.View;

import java.util.logging.Logger;

public class SwipeDetector implements View.OnTouchListener{

    public static enum Action{
        Left_To_Right,
        Right_To_Left,
        Top_To_Bottom,
        Bottom_To_Top,
        None // no action detected
    }

    private static final String LogTag = "SwipeDetector";
    private static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;
    private Action action = Action.None;

    public boolean swipeDetected(){
        return action != Action.None;
    }

    public Action getAction(){
        return action;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:{
                downX = motionEvent.getX();
                downY = motionEvent.getY();
                action = Action.None;
                return false; // allow other events like Click to proceed
            }
            case MotionEvent.ACTION_MOVE:{
                upX = motionEvent.getX();
                upY = motionEvent.getY();
                float deltaX = downX - upX;
                float deltaY = downY - upX;
                // horizontal swipe detection
                if(Math.abs(deltaX) > MIN_DISTANCE){
                    // left or right
                    if(deltaX < 0){
                        action = Action.Right_To_Left;
                        return true;
                    }
                    if(deltaX > 0){
                        action = Action.Left_To_Right;
                        return true;
                    }
                }
                // vertical swipe detection
                else {
                    if(Math.abs(deltaY) > MIN_DISTANCE){
                        if(deltaY < 0){
                            action = Action.Top_To_Bottom;
                            return false;
                        }
                        if(deltaX > 0){
                            action = Action.Bottom_To_Top;
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
}
