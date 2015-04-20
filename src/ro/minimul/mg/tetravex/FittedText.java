package ro.minimul.mg.tetravex;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class FittedText {
    private final static float LINE_HEIGHT = 0.5f;
	private final String[] lines;
	private final Paint paint;
	private final int[] xs;
	private final int[] ys;
	private final int topText;
	private final int bottomText;
	
    public FittedText(String text, Paint textPaint, int left, int top, int right,
			int bottom) {
		lines = text.split("\n");
		paint = new Paint(textPaint);
		
		int width = right - left;
		int height = bottom - top;
        Rect bounds = new Rect();
        int size;
        int textHeight;
        int space;
        int totalHeight;
		
        all:
		for (size = 7; size <= 100; size++) {
			paint.setTextSize(size);
			paint.getTextBounds(lines[0], 0, lines[0].length(), bounds);
			
			// Computing the height of all the lines with the spacing.
	        textHeight = bounds.height();
	        space = (int)(textHeight * LINE_HEIGHT);
	        totalHeight = lines.length * textHeight + (lines.length-1) * space;
			
			if (totalHeight > height || bounds.width() > width) {
			    break all;
			}
			
			// Checking the widths of all the other lines.
			for (int i = 1; i < lines.length; i++) {
	            paint.getTextBounds(lines[i], 0, lines[i].length(), bounds);
	            if (bounds.width() > width) {
	                break all;
	            }
			}
		}
		
		size--; // Going back to the last correct one.
		paint.setTextSize(size);
		
		// Computing the height and spacing for every line.
		paint.getTextBounds(lines[0], 0, lines[0].length(), bounds);
		textHeight = bounds.height();
        space = (int)(textHeight * LINE_HEIGHT);
        totalHeight = lines.length * textHeight + (lines.length-1) * space;
        
		topText = top + (height - totalHeight) / 2;
		bottomText = topText + totalHeight;
		
		xs = new int[lines.length];
		ys = new int[lines.length];
		
		for (int i = 0; i < lines.length; i++) {
		    paint.getTextBounds(lines[i], 0, lines[i].length(), bounds);
		    xs[i] = left + (width - bounds.width()) / 2;
		    ys[i] = topText + (i+1) * textHeight;
		    if (i > 0) {
		        ys[i] += space;
		    }
		}
	}
	
	public final void draw(Canvas canvas) {
	    for (int i = 0; i < lines.length; i++) {
	        canvas.drawText(lines[i], xs[i], ys[i], paint);
	    }
	}
	
	public final int getTop() {
		return topText;
	}
	
	public final int getBottom() {
		return bottomText;
	}
}
