package ro.minimul.mg.tetravex;

import ro.minimul.mg.Util;

/**
 * Calculates all the positioning variables based on the screen size.
 * 
 * @author Paul Nechifor
 */
public class Positioning {
    private static final int[] BORDERS = new int[] {1, 200, 2, 400, 4, 700, 6};
    private static final float PADDING_RATIO = 0.02f;

    public final int border;
    public final int size;
    public final int srcX, srcY;
    public final int dstX, dstY;
    public final int midway;
    public final int width;
    public final int height;
    
    public Positioning(int width, int height, int lin, int col) {
        this.width = width;
        this.height = height;
        
        int padding = (int)(PADDING_RATIO * width);
        border = Util.chooseLTE(width, BORDERS);

        // What is the size of the pieces that will fit. Pick the smallest.
        int fitInWidth = (width - 2 * padding - 2 * border) / col;
        int fitInHeight = (height - 3 * padding - 4 * border) / (2 * lin);
        int pieceSize = (fitInWidth < fitInHeight) ? fitInWidth : fitInHeight;
        
        // The piece size must be an odd number.
        if (pieceSize % 2 == 0) {
            pieceSize--;
        }
        size = pieceSize;
        
        srcX = (width - col * size - border) / 2;
        float vPad = (height - 2*lin*size - 4 * border) / 3.0f;
        srcY = (int)vPad + border;
        
        dstX = srcX;
        dstY = (int)(2 * vPad) + lin * size + 3 * border;
        
        midway = height / 2;
    }
}
