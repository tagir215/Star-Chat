package com.android.starchat.glRenderer;

import android.app.Activity;
import android.view.MotionEvent;
import androidx.constraintlayout.widget.ConstraintSet;

public class MotionEventHandler {
    private float oldY;
    private float momentum;
    private volatile boolean interruptMomentum;
    private GLRenderer renderer;

    public MotionEventHandler(GLRenderer renderer) {
        this.renderer = renderer;
    }

    Runnable momentumRunnable = new Runnable() {
        @Override
        public void run() {
            momentumLoop();
        }
    };

    private void momentumLoop(){
        while (Math.abs(momentum)>0.01f){
            if(interruptMomentum)
                break;

            momentum = momentum/1.1f;
            updatePosition(momentum);


            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean handleMotionEvent(MotionEvent event){
        if(event==null)
            return false;

        float y = event.getY();
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            oldY = y;
            interruptMomentum = true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            interruptMomentum = false;
            Thread thread = new Thread(momentumRunnable);
            thread.start();
            return false;
        }

        momentum = y-oldY;
        updatePosition(momentum);
        oldY = y;

        return true;
    }

    private void updatePosition(float momentum){
        renderer.getScrollPosition().scrollDistance(momentum);
        renderer.getScrollPosition().setTimeLineWidth();
    }




}
