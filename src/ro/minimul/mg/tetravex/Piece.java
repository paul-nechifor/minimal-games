package ro.minimul.mg.tetravex;

import android.graphics.Point;

public class Piece {
    public static final int N_VALUES = 10;
    public static final int N_SIDES = 4;
    public static final int TOP = 0;
    public static final int RIGHT = 1;
    public static final int BOTTOM = 2;
    public static final int LEFT = 3;
    
    private final int[] values = new int[N_SIDES];
    private final Point correctCell;
    
    public Piece(int posI, int posJ, int c0, int c1, int c2, int c3) {
        this.values[0] = c0;
        this.values[1] = c1;
        this.values[2] = c2;
        this.values[3] = c3;
        correctCell = new Point(posJ, posI);
    }
    
    /**
     * Get the color corresponding the the side.
     * @param side      TOP, RIGHT, BOTTOM or LEFT.
     * @return          The value of the color.
     */
    public int getValue(int side) {
        return values[side];
    }
    
    public int[] getValues() {
        return values;
    }
    
    public Point getCorrectCell() {
        return correctCell;
    }
}