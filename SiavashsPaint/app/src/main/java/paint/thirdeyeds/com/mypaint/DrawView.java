package paint.thirdeyeds.com.mypaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by siavash on 9/21/15.
 */
public class DrawView extends View {

    private static final float DEFAULT_BRUSH_THICKNESS = 20;


    private Path currentPath;
    private Paint drawPaint;
    private float brushThickness = DEFAULT_BRUSH_THICKNESS;

    private ArrayList<ColoredPath> paths = new ArrayList<>();
    private ArrayList<ColoredPath> undonePaths = new ArrayList<>();
    private boolean drawInProgress = false;


    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        currentPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(Color.BLACK);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushThickness);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        //respond to down, move and up events
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawInProgress = true;
                currentPath = new Path();
                currentPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                currentPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                drawInProgress = false;
                currentPath.lineTo(x, y);
                paths.add(new ColoredPath(new Path(currentPath), new Paint(drawPaint)));
                currentPath.reset();
                break;
            default:
                return false;
        }

        //redraw
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        //canvas.drawPath(currentPath, drawPaint);
        if(!paths.isEmpty()) {
            for (ColoredPath cp : paths) {
                canvas.drawPath(cp.path, cp.paint);
            }
        } else {
            canvas.drawColor(Color.WHITE);
        }
        if(drawInProgress) {
            canvas.drawPath(currentPath, drawPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //drawCanvas = new Canvas(canvasBitmap);
    }



    public void undo(){
        if(!paths.isEmpty()) {
            undonePaths.add(paths.remove(paths.size() - 1));
            invalidate();
        }
    }
    /**
     *
     * I purposely allowed redo to work even after new paths were added becuase it allows for new
     * possibilites for usability, and does not take anything away or cause any issues. Any unwanted
     * paths added by redo can be just undone.
     * */
    public void redo(){
        if(!undonePaths.isEmpty()) {
            paths.add(undonePaths.remove(undonePaths.size() - 1));
            invalidate();
        }
    }

    public void setDrawingPaint(int color) {
        drawPaint.setColor(color);
    }
    public void setBrushThickness(float newThickness) {
        brushThickness = newThickness;
        drawPaint.setStrokeWidth(brushThickness);
    }
}
