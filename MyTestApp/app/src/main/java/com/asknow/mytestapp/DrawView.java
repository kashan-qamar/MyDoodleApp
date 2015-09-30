package com.asknow.mytestapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

//other features
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.asknow.mytestapp.MainActivity;

import java.io.File;

/**
 * Created by kashanqamar on 9/25/15.
 */
public class DrawView extends View {

    private float brushSize, lastBrushSize;

    //eraser flag
    private boolean erase=false;

    //drawing path
    private Path drawPath;

    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;

    //initial color
    private int paintColor = 0xFF000000;

    //canvas
    private Canvas drawCanvas;

    //canvas bitmap
    private Bitmap canvasBitmap;
    private int mBackgroundColor = android.R.color.background_dark;





    public DrawView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }



    private void setupDrawing()
    {
        //get drawing area setup for interaction


        brushSize=getResources().getInteger(R.integer.medium_size);
        lastBrushSize=brushSize;

        //initial objects
        drawPath=new Path();
        drawPaint =new Paint();

        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        //instantiating a canvas paint object
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void setBrushSize(float newSize)
    {
        //update size for each brush
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());

        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize)
    {
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize()
    {
        return lastBrushSize;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        // override a couple of methods to make
        //the custom View function as a drawing View



        //view given size
        super.onSizeChanged(w,h,oldw,oldh);

        canvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        //To allow the class to function as a custom drawing View,
        //we also need to override the onDraw method
        //draw view

       // canvas.drawColor(getResources().getColor(mBackgroundColor));
        canvas.drawBitmap(canvasBitmap, 0,0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //detect user touch


        float touchX=event.getX();
        float touchY=event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;

            default:
                return false;
        }
        //redraw - Calling invalidate will cause the onDraw method to execute.
        invalidate();
        return true;

    }

    public void setColor(String newColor)
    {
        //set color and start by invalidating the view
        invalidate();

        //parse and set the color for drawing
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);

    }

    public void setErase (Boolean isErase)
    {
        //set erase true or false
        erase=isErase;
        if(erase)
            drawPaint.setXfermode(new PorterDuffXfermode (PorterDuff.Mode.CLEAR));
        else
            drawPaint.setXfermode(null);
    }

    public void startNew()
    {
        //set a new draw
        drawCanvas.drawColor(0,PorterDuff.Mode.CLEAR);
        invalidate();
    }




}
