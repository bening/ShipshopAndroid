package com.dios.shopper.customcomponent;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class ViewPagerDisable extends ViewPager {

	private boolean enabled;
	private GestureDetector mGestureDetector;
	private float xDistance, yDistance, lastX, lastY;
    View.OnTouchListener mGestureListener;
	private boolean mIsLockOnHorizontalAxis = false;
	
	public ViewPagerDisable(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    this.enabled = true;
	    mGestureDetector = new GestureDetector(context, new XScrollDetector());
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/*switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.e("HorizontalTouch", "action down");
			break;
		case MotionEvent.ACTION_UP:
			Log.e("HorizontalTouch", "action up");
			break;	
		default:
			break;
		}*/
		Log.e("HorizontalTouch", "x: "+event.getX()+" y: "+event.getY());
	    if (this.enabled) {
	        return super.onTouchEvent(event);
	    }
	
	    return false;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		/*switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.e("HorizontalInterceptTouch", "action down");
			break;
		case MotionEvent.ACTION_UP:
			Log.e("HorizontalInterceptTouch", "action up");
			break;	
		default:
			break;
		}*/
		Log.e("HorizontalInterceptTouch", "x: "+event.getX()+" y: "+event.getY());
	    if (this.enabled) {
	    	return super.onInterceptTouchEvent(event);
	    }
	
	    return false;
	}
	
	public void setPagingEnabled(boolean enabled) {
	    this.enabled = enabled;
	}
	
	// Return false if we're scrolling in the y direction  
    class XScrollDetector extends SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {             
            return Math.abs(distanceX) > Math.abs(distanceY);
        }
    }

}
