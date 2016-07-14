package com.example.android.bike;





import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;



public class RoundProgressBar2 extends View implements  ProgressLis2{
	/**
	 * Brush object reference
	 */
	
	public int oldprogress = 0;
	public int nowprogress = 0;
	private Paint paint;
	private Paint paint1;
	
	/**
	 * The colors of the rings
	 */
	private int roundColor;
	
	/**
	 * The progress of the color ring
	 */
	private int roundProgressColor;
	
	/**
	 * The middle of the string progress percentage color
	 */
	private int textColor;
	
	/**
	 * The middle of the string progress percentage font
	 */
	private float textSize;
	
	/**
	 * Ring width
	 */
	private float roundWidth;
	
	/**
	 * If the cover ring, the only effective use when filling is filled, whether has the outer ring, 1 expressed reservations, 0 said they did not keep
	 */
	private int keepRoundType;
	public static final int KEEP = 1;
	public static final int NOT_KEEP = 0;
	
	
	/**
	 * The biggest progress
	 */
	private int max;
	
	/**
	 * The current progress
	 */
	private int progress;
	/**
	 * Display intermediate schedule
	 */
	private boolean textIsDisplayable;
	
	/**
	 * The progress of the style, solid or hollow
	 */
	private int style  ;
	
	public static final int STROKE = 0;
	public static final int FILL = 1;
	
	public RoundProgressBar2(Context context) {
		this(context, null);
	}

	public RoundProgressBar2(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public RoundProgressBar2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		paint = new Paint();
		paint1 = new Paint();

		
		TypedArray mTypedArray1 = context.obtainStyledAttributes(attrs,
				R.styleable.RoundProgressBar2);
		
		//Get a custom attribute and the default value
		roundColor = mTypedArray1.getColor(R.styleable.RoundProgressBar2_roundColor1, Color.RED);
		roundProgressColor = mTypedArray1.getColor(R.styleable.RoundProgressBar2_roundProgressColor1, Color.GREEN);
		textColor = mTypedArray1.getColor(R.styleable.RoundProgressBar2_textColor1, Color.GREEN);
		textSize = mTypedArray1.getDimension(R.styleable.RoundProgressBar2_textSize1, 15);
		roundWidth = mTypedArray1.getDimension(R.styleable.RoundProgressBar2_roundWidth1, 5);
		keepRoundType = mTypedArray1.getInt(R.styleable.RoundProgressBar2_keepRoundType1, 0);//Keep the outer ring is filled circle
		max = mTypedArray1.getInteger(R.styleable.RoundProgressBar2_max1, 100);
		textIsDisplayable = mTypedArray1.getBoolean(R.styleable.RoundProgressBar2_textIsDisplayable1, true);
		style = mTypedArray1.getInt(R.styleable.RoundProgressBar2_style1, 1);
		
		mTypedArray1.recycle();
	}
	

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		
		if(progress>30&&progress<101){
			int color1 = Color.parseColor("#22cd5e");
			roundProgressColor = color1	;	
		}  if (progress<70&&progress>30){
			int color3 = Color.parseColor("#FF8000");
			roundProgressColor = color3;
		}
			if(progress<30){
		
			int color2 = Color.parseColor("#ed1c24");
			roundProgressColor = color2	;	
		}
		
		
		int centre = getWidth()/2; //X coordinates acquisition center
		int radius = (int) (centre - roundWidth/2); //The radius of the ring
		paint.setColor(roundColor); //Set the colors of the rings
		paint.setStyle(Paint.Style.STROKE); //A hollow
		paint.setStrokeWidth(roundWidth); //Set the ring width
		paint.setAntiAlias(true);  //Antialiasing 
		canvas.drawCircle(centre, centre, radius, paint); //Draw a circle
		
		Log.e("log", centre + "");
		

		paint.setStrokeWidth(0); 
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD); //Set the font
		int percent = (int)(((float)progress / (float)max) * 100);  //Percentage of completion in the middle, first converted to float in a division operation, or 0
		float textWidth = paint.measureText(percent + "%");   //Measure the font width, we need according to the font width in the middle ring
		
		if(textIsDisplayable && percent != 0 && style == STROKE){
			canvas.drawText(percent + "%", centre - textWidth / 2, centre + textSize/2, paint); //Draw the percentage
		}
		
		

		
		//Setup schedule is solid or hollow
		paint1.setStrokeWidth(1); //Set the ring width
		paint1.setColor(roundProgressColor);  //The progress of the color settings
		
		
		RectF oval = null;
		if(keepRoundType == KEEP){//Retaining ring
			radius -= roundWidth;
			
			
		}
		
		
		
		radius = (int) (radius - roundWidth + 5);
		
		oval = new RectF(centre - radius, centre - radius, centre
			+ radius, centre + radius);  //Used to define the shape and the size of the circular boundaries
		
		switch (style) {
		case STROKE:{
		
			paint1.setStyle(Paint.Style.STROKE);
			canvas.drawArc(oval, 90-progress*18/10, 360 * progress / max, false, paint1);  //According to the progress of an arc
			
			
			break;
			
			
		}
		case FILL:{
			
			paint1.setStyle(Paint.Style.FILL_AND_STROKE);
			if(progress !=0)
				canvas.drawArc(oval, 90-progress*18/10, 360 * progress / max, false, paint1);  //According to the progress of an arc
			
			
			break;
		}
		}
		
		
		
		
	}
	
	
	public synchronized int getMax() {
		
		return max;
	}

	/**
	 * Set the maximum schedule
	 * @param max
	 */
	public synchronized void setMax(int max) {
		if(max <0){
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}


	public int getProgress() {
		return progress;
	}


	public  void setProgress(int progress) {
		if(progress <0){
			throw new IllegalArgumentException("progress not less than 0");
		}
		if(progress > max){
			progress = max;
		}
		
		if(progress <= max){
			this.progress = progress;
			
		}
		
		
		
		
		
	}
	
	
	public int getCricleColor() {
		return roundColor;
	}

	public void setCricleColor(int cricleColor) {
		
		
		this.roundColor = cricleColor;
	}

	public int getCricleProgressColor() {
		return roundProgressColor;
	}

	public void setCricleProgressColor(int cricleProgressColor) {
		this.roundProgressColor = cricleProgressColor;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	public float getRoundWidth() {
		return roundWidth;
	}

	public void setRoundWidth(float roundWidth) {
		this.roundWidth = roundWidth;
	}

	public void onProgressChange1(int progress) {
		// TODO Auto-generated method stub
		this.setProgress(progress);
		this.invalidate();
	}
	public void onProgressColor(String color){
		int color1 = Color.parseColor(color);
		this.setCricleColor(color1);
		this.invalidate();
	}

}