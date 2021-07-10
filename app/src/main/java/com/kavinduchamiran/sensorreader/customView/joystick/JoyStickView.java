package com.kavinduchamiran.sensorreader.customView.joystick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class JoyStickView extends View implements Runnable {
    // Variables
    private OnJoystickMoveListener onJoystickMoveListener; // Listener
    private int xPosition = 0; // Touch x position
    private int yPosition = 0; // Touch y position
    private double centerX = 0; // Center view x position
    private double centerY = 0; // Center view y position
    public double startRange = 0;
    public double endRange = 0;
    private int partOfBigRange = 0;
    private int startOfBigRange = -32767;

    private Paint mainCircle;
    private Paint secondaryCircle;
    private Paint button;
    private Paint border;
    private Paint mainCircleBorder;
    private Paint horizontalLine;
    private Paint verticalLine;
    private int joystickRadius;
    private int buttonRadius;

    private Boolean inMainCircle = false;
    public boolean justShowTouch = false;

    public JoyStickView(Context context) {
        super(context);
    }

    public JoyStickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initJoystickView();
    }

    public JoyStickView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        initJoystickView();
    }

    protected void initJoystickView() {
        mainCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainCircle.setStyle(Paint.Style.STROKE);
        mainCircle.setColor(Color.WHITE);


        secondaryCircle = new Paint();
        secondaryCircle.setColor(Color.TRANSPARENT);
        secondaryCircle.setStyle(Paint.Style.STROKE);

        verticalLine = new Paint();
        verticalLine.setStrokeWidth(5);
        verticalLine.setColor(Color.TRANSPARENT);

        horizontalLine = new Paint();
        horizontalLine.setStrokeWidth(7);
        horizontalLine.setColor(Color.TRANSPARENT);

        button = new Paint(Paint.ANTI_ALIAS_FLAG);
        button.setColor(Color.parseColor("#303134"));
        button.setStyle(Paint.Style.FILL);

        border = new Paint(Paint.ANTI_ALIAS_FLAG);
        border.setColor(Color.parseColor("#cccccc"));
        border.setStyle(Paint.Style.STROKE);
        border.setStrokeWidth(10);

        mainCircleBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainCircleBorder.setColor(Color.parseColor("#cccccc"));
        mainCircleBorder.setStyle(Paint.Style.STROKE);
        mainCircleBorder.setStrokeWidth(5);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        // before measure, get the center of view
        startRange = getWidth() / 4;
        endRange = startRange * 3;
        partOfBigRange = (int) (65535 / (endRange - startRange));
        xPosition = (int) getWidth() / 2;
        yPosition = (int) getWidth() / 2;
        int d = Math.min(xNew, yNew);
        buttonRadius = (int) (d / 2 * 0.35);
        joystickRadius = (int) (d / 2 * 0.5);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // setting the measured values to resize the view to a certain width and
        // height
        int d = Math.min(measure(widthMeasureSpec), measure(heightMeasureSpec));

        setMeasuredDimension(d, d);

    }

    private int measure(int measureSpec) {
        int result = 0;

        // Decode the measurement specifications.
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.UNSPECIFIED) {
            // Return a default size of 200 if no bounds are specified.
            result = 200;
        } else {
            // As you want to fill the available space
            // always return the full available bounds.
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        centerX = (getWidth()) / 2;
        centerY = (getHeight()) / 2;

        // painting the main circle
        canvas.drawCircle((int) centerX, (int) centerY, joystickRadius,
                mainCircleBorder);
        // painting the secondary circle
        canvas.drawCircle((int) centerX, (int) centerY, joystickRadius / 2,
                secondaryCircle);
        // paint lines
        canvas.drawLine((float) centerX, (float) centerY, (float) centerX,
                (float) (centerY - joystickRadius), verticalLine);
        canvas.drawLine((float) (centerX - joystickRadius), (float) centerY,
                (float) (centerX + joystickRadius), (float) centerY,
                horizontalLine);
        canvas.drawLine((float) centerX, (float) (centerY + joystickRadius),
                (float) centerX, (float) centerY, horizontalLine);

        // painting the move button
        canvas.drawCircle(xPosition, yPosition, buttonRadius, button);
        canvas.drawCircle(xPosition, yPosition, buttonRadius, border);
    }

    public void muckTouch(MotionEvent event) {
        justShowTouch = true;
        dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        xPosition = (int) event.getX();
        yPosition = (int) event.getY();
        double abs = Math.sqrt((xPosition - centerX) * (xPosition - centerX)
                + (yPosition - centerY) * (yPosition - centerY));
        if (abs > joystickRadius) {
            xPosition = (int) ((xPosition - centerX) * joystickRadius / abs + centerX);
            yPosition = (int) ((yPosition - centerY) * joystickRadius / abs + centerY);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            xPosition = (int) centerX;
            yPosition = (int) centerY;
            convertPositionToPeriod(false);
            justShowTouch = false;
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (abs <= joystickRadius) {
                inMainCircle = true;
            } else {
                inMainCircle = false;
            }
            convertPositionToPeriod(true);
        } else {
            convertPositionToPeriod(true);
        }
//        if (inMainCircle)
        invalidate();

        return true;
    }

    public void setOnJoystickMoveListener(OnJoystickMoveListener listener) {
        this.onJoystickMoveListener = listener;
    }

    @Override
    public void run() {

    }

    public interface OnJoystickMoveListener {
        void onRangeChanged(int rangeX, int rangeY, boolean stickerIsDown);
    }

    private void convertPositionToPeriod(boolean isDown) {
        double differentX = xPosition - startRange;
        double differentY = yPosition - startRange;

        if (onJoystickMoveListener != null && !justShowTouch)
            onJoystickMoveListener.onRangeChanged((int) ((differentX * partOfBigRange) + startOfBigRange), (int) (((differentY * partOfBigRange) + startOfBigRange) * (-1)), isDown);
    }
}