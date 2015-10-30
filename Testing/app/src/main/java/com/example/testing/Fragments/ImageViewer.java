/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.testing.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.widget.Scroller;

import com.example.testing.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class ImageViewer extends View
{
	private Bitmap image = null;
	
	private final GestureDetector gestureDetector;
	private final ScaleGestureDetector scaleGestureDetector;
	private final Scroller scroller;
	private final Paint paint = new Paint();
	private static final int MAX_CLICK_DURATION = 200;
	private long startClickTime;
	private float scaleFactor;

	Rect clipBounds_canvas;

	private Bitmap bitmapStars;
	private Random random;
	private ArrayList<ArrayList<Integer>> coordinatesStars;
	private int getScaledWidth()
	{
		return (int)(image.getWidth() * scaleFactor);
	}
	
	private int getScaledHeight()
	{
		return (int)(image.getHeight() * scaleFactor);
	}

	private int getScaledWidthStars()
	{
		return (int)(100 * scaleFactor);
	}

	private int getScaledHeightStars()
	{
		return (int)(100 * scaleFactor);
	}
	
	public ImageViewer(Context context)
	{
		super(context);
		random = new Random();
		gestureDetector = new GestureDetector(context, new MyGestureListener());
		scaleGestureDetector = new ScaleGestureDetector(context, new MyScaleGestureListener());
		scroller = new Scroller(context);

		coordinatesStars = new ArrayList<ArrayList<Integer>>();

		paint.setFilterBitmap(true);
		paint.setDither(false);
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		Rect dst = new Rect(0, 0, getScaledWidth(), getScaledHeight());

		canvas.drawBitmap(image, null, dst, paint);

		for(int i = 0; i < 10; i++)
			canvas.drawBitmap(bitmapStars, null, new Rect(
							(int)(coordinatesStars.get(i).get(0)* scaleFactor),
							(int)(coordinatesStars.get(i).get(1)* scaleFactor),
							(int)(coordinatesStars.get(i).get(0)* scaleFactor) + getScaledWidthStars(),
							(int)(coordinatesStars.get(i).get(1)* scaleFactor) +getScaledHeightStars()),
					paint);

		clipBounds_canvas  = canvas.getClipBounds();

	}

	@Override
	protected int computeHorizontalScrollRange()
	{
		return getScaledWidth();
	}

	@Override
	protected int computeVerticalScrollRange()
	{
		return getScaledHeight();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN)
		{
			if (!scroller.isFinished()) scroller.abortAnimation();
		}

		float x = event.getX() / scaleFactor + clipBounds_canvas.left;
		float y = event.getY() / scaleFactor + clipBounds_canvas.top;

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				startClickTime = Calendar.getInstance().getTimeInMillis();
				break;
			}
			case MotionEvent.ACTION_UP: {
				long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
				if(clickDuration < MAX_CLICK_DURATION) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
					builder.setTitle("Координаты")
							.setMessage("x = " + String.valueOf(x) + "\n" +"y = " + String.valueOf(y))
							.setCancelable(false)
							.setNegativeButton("Ок",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		}
		// handle pinch zoom gesture
		// don't check return value since it is always true
		scaleGestureDetector.onTouchEvent(event);

		// check for scroll gesture
		if (gestureDetector.onTouchEvent(event)) return true;

		// check for pointer release
		if ((event.getPointerCount() == 1) && ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP))
		{
			int newScrollX = getScrollX();
			if (getScaledWidth() < getWidth()) newScrollX = -(getWidth() - getScaledWidth()) / 2;
			else if (getScrollX() < 0) newScrollX = 0;
			else if (getScrollX() > getScaledWidth() - getWidth()) newScrollX = getScaledWidth() - getWidth();

			int newScrollY = getScrollY();
			if (getScaledHeight() < getHeight()) newScrollY = -(getHeight() - getScaledHeight()) / 2;
			else if (getScrollY() < 0) newScrollY = 0;
			else if (getScrollY() > getScaledHeight() - getHeight()) newScrollY = getScaledHeight() - getHeight();

			if ((newScrollX != getScrollX()) || (newScrollY != getScrollY()))
			{
				scroller.startScroll(getScrollX(), getScrollY(), newScrollX - getScrollX(), newScrollY - getScrollY());
				awakenScrollBars(scroller.getDuration());
			}
		}

		return true;
	}

	@Override
	public void computeScroll()
	{
		if (scroller.computeScrollOffset())
		{
			int oldX = getScrollX();
			int oldY = getScrollY();
			int x = scroller.getCurrX();
			int y = scroller.getCurrY();
			scrollTo(x, y);
			if (oldX != getScrollX() || oldY != getScrollY())
			{
				onScrollChanged(getScrollX(), getScrollY(), oldX, oldY);
			}

			postInvalidate();
		}
	}
	
	@Override
	protected void onScrollChanged(int x, int y, int oldX, int oldY)
	{
	}
	
	@Override
	protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight)
	{
		int scrollX = (getScaledWidth() < width ? -(width - getScaledWidth()) / 2 : getScaledWidth() / 2);
		int scrollY = (getScaledHeight() < height ? -(height - getScaledHeight()) / 2 : getScaledHeight() / 2);
		scrollTo(scrollX, scrollY);
	}
	
	public void loadImage(int drawable)
	{
		/*
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		*/

		bitmapStars = BitmapFactory.decodeResource(getResources(), R.drawable.alivestar);
		//bitmapStars = Bitmap.createScaledBitmap(bitmapStarsExtra, 100, 100, true);

		image = BitmapFactory.decodeResource(getResources(), drawable);
		if (bitmapStars == null) throw new NullPointerException("The image can't be decoded.");
		
		scaleFactor = 1;

		// center image on the screen
		int width = getWidth();
		int height = getHeight();
		if ((width != 0) || (height != 0))
		{
			int scrollX = (image.getWidth() < width ? -(width - image.getWidth()) / 2 : image.getWidth() / 2);
			int scrollY = (image.getHeight() < height ? -(height - image.getHeight()) / 2 : image.getHeight() / 2);
			scrollTo(scrollX, scrollY);
		}

		ArrayList<Integer> exIntegers;
		for(int i = 0; i < 10; i ++){
			exIntegers = new ArrayList<Integer>();
			exIntegers.add(random.nextInt(getScaledWidth() - 110));
			exIntegers.add(random.nextInt(getScaledHeight() - 110));
			coordinatesStars.add(exIntegers);
		}
	}
	
	private class MyGestureListener extends SimpleOnGestureListener
	{
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
		{
			scrollBy((int)distanceX, (int)distanceY);
			return true;
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
		{
			int fixedScrollX = 0, fixedScrollY = 0;
			int maxScrollX = getScaledWidth(), maxScrollY = getScaledHeight();
			
			if (getScaledWidth() < getWidth())
			{
				fixedScrollX = -(getWidth() - getScaledWidth()) / 2;
				maxScrollX = fixedScrollX + getScaledWidth();
			}
			
			if (getScaledHeight() < getHeight())
			{
				fixedScrollY = -(getHeight() - getScaledHeight()) / 2;
				maxScrollY = fixedScrollY + getScaledHeight();
			}

			boolean scrollBeyondImage = (fixedScrollX < 0) || (fixedScrollX > maxScrollX) || (fixedScrollY < 0) || (fixedScrollY > maxScrollY);
			if (scrollBeyondImage) return false;
			
			scroller.fling(getScrollX(), getScrollY(), -(int)velocityX, -(int)velocityY, 0, getScaledWidth() - getWidth(), 0, getScaledHeight() - getHeight());
			awakenScrollBars(scroller.getDuration());
			
			return true;
		}
	}

	private class MyScaleGestureListener implements OnScaleGestureListener
	{
		public boolean onScale(ScaleGestureDetector detector)
		{
			scaleFactor *= detector.getScaleFactor();

			int newScrollX = (int)((getScrollX() + detector.getFocusX()) * detector.getScaleFactor() - detector.getFocusX());
			int newScrollY = (int)((getScrollY() + detector.getFocusY()) * detector.getScaleFactor() - detector.getFocusY());
			scrollTo(newScrollX, newScrollY);
			
			invalidate();
			
			return true;
		}

		public boolean onScaleBegin(ScaleGestureDetector detector)
		{
			return true;
		}

		public void onScaleEnd(ScaleGestureDetector detector)
		{
		}
	}
}
