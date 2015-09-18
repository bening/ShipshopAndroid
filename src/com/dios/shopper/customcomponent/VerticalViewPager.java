package com.dios.shopper.customcomponent;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

/**
* Uses a combination of a PageTransformer and onTouchEvent to create the
* illusion of a vertically scrolling ViewPager. 
* 
* Requires API 11+
* 
*/
//@SuppressLint("NewApi")
public class VerticalViewPager extends ViewPager {
	
	private boolean enabled;
	private float xDistance, yDistance, lastX, lastY;
	private GestureDetector mGestureDetector;
    View.OnTouchListener mGestureListener;

	public VerticalViewPager(Context context) {
	    super(context);
	    mGestureDetector = new GestureDetector(context, new YScrollDetector());
	    init();
	}
	
	public VerticalViewPager(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    mGestureDetector = new GestureDetector(context, new YScrollDetector());
	    init();
	}
	
	private void init() {
	    // The majority of the magic happens here
		this.enabled = true;
	    setPageTransformer(true, new VerticalPageTransformer());
	    // The easiest way to get rid of the overscroll drawing that happens on the left and right
	    setOverScrollMode(OVER_SCROLL_ALWAYS);
	}
	
	private class VerticalPageTransformer implements ViewPager.PageTransformer {
	
	    @Override
	    public void transformPage(View view, float position) {
	        int pageWidth = view.getWidth();
	        int pageHeight = view.getHeight();
	
	        if (position < -1) { // [-Infinity,-1)
	            // This page is way off-screen to the left.
	            view.setAlpha(0);
	
	        } else if (position <= 1) { // [-1,1]
	            view.setAlpha(1);
	
	            // Counteract the default slide transition
	            view.setTranslationX(pageWidth * -position);
	
	            //set Y position to swipe in from top
	            float yPosition = position * pageHeight;
	            view.setTranslationY(yPosition);
	
	        } else { // (1,+Infinity]
	            // This page is way off-screen to the right.
	            view.setAlpha(0);
	        }
	    }

	}
	
	/**
	 * Swaps the X and Y coordinates of your touch event
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
	    //swap the x and y coords of the touch event
		Log.e("VerticalTouch", "x: "+ev.getX()+" y: "+ev.getY());
		if (this.enabled) {
			ev.setLocation(ev.getY(), ev.getX());
			
		    return super.onTouchEvent(ev);
	    }
		return false;
	    
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		getParent().requestDisallowInterceptTouchEvent(true);
		
		Log.e("VerticalInterceptTouch", "x: "+ev.getX()+" y: "+ev.getY());
	    if (this.enabled) {
	    	//ev.setLocation(ev.getY(), ev.getX());
	        //return super.onInterceptTouchEvent(ev);
	    	switch (ev.getAction()) {
		        case MotionEvent.ACTION_DOWN:
		            xDistance = yDistance = 0f;
		            lastX = ev.getX();
		            lastY = ev.getY();
		            Log.e("VerticalInterceptTouch", "action down, last x: "+lastX+", last y: "+lastY);
		            break;
		        case MotionEvent.ACTION_MOVE:
		            final float curX = ev.getX();
		            final float curY = ev.getY();
		            xDistance += Math.abs(curX - lastX);
		            yDistance += Math.abs(curY - lastY);
		            lastX = curX;
		            lastY = curY;
		            Log.e("VerticalInterceptTouch", "action move, last x: "+lastX+", last y: "+lastY+", xDistance: "+xDistance+", yDistance: "+yDistance);
		            if(xDistance > yDistance){
		            	Log.e("VerticalInterceptTouch", "xDistance > yDistance");
		            	getParent().requestDisallowInterceptTouchEvent(false);
		                return false;
		            }
		            else{
		            	Log.e("VerticalInterceptTouch", "xDistance <= yDistance");
		            	getParent().requestDisallowInterceptTouchEvent(true);
		            	return true;
		            }
			}
	
			return super.onInterceptTouchEvent(ev);
	    }
	
	    return false;
	}
	
	public void setPagingEnabled(boolean enabled) {
	    this.enabled = enabled;
	}
	
	// Return false if we're scrolling in the x direction  
    class YScrollDetector extends SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {             
            return Math.abs(distanceY) > Math.abs(distanceX);
        }
    }
}
