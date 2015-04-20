package ro.minimul.mg.tetravex;

import ro.minimul.mg.TObject;
import ro.minimul.mg.Util;
import android.graphics.Point;
import android.graphics.PointF;

public class GuiPiece extends TObject {
    private final Piece piece;
    private final MovingPoint movingPoint = new MovingPoint();
    private float size;
    private boolean touched;
    private float touchedX, touchedY;
    private Board board;
    private Point boardCell;
    
    private Board futureBoard;
    private Point futureBoardCell;
    
    public GuiPiece(float size, float space, GuiTheme theme, Piece piece) {
        this.piece = piece;
        this.size = size;
        
        vertices = createVertices(size, space);
        colors = createInitialColors(theme, piece);
        verticesBuffer = Util.createFloatBuffer(vertices);
        colorsBuffer = Util.createFloatBuffer(colors);
    }
    
    public Piece getPiece() {
        return piece;
    }
    
    public float getCenterX() {
        return x + size / 2;
    }
    
    public float getCenterY() {
        return y + size / 2;
    }
    
    public Board getBoard() {
        return board;
    }
    
    public Point getBoardCell() {
        return boardCell;
    }
    
    public void setTouched(float px, float py) {
        touched = true;
        touchedX = px - x;
        touchedY = py - y;
        
        if (movingPoint.isMoving()) {
            movingPoint.stop();
        }
        
        if (futureBoard != null) {
            clearArrival();
        }
    }
    
    public void setUntouched() {
        touched = false;
    }
    
    public boolean amITouching(float px, float py) {
        return px > x && px < x+size && py > y && py < y+size && !touched;
    }
    
    public void moveToPointer(float px, float py) {
        x = px - touchedX;
        y = py - touchedY;
    }
    
    public void placeOnBoard(Board board, Point cell) {
        this.board = board;
        this.boardCell = cell;
        
        PointF point = board.getCellPosition(cell);
        x = point.x;
        y = point.y;
    }
    
    public void tic(float delta) {
        if (movingPoint.isMoving()) {
            PointF point = movingPoint.getNewPosition(delta);
            x = point.x;
            y = point.y;
            
            if (!movingPoint.isMoving() && futureBoard != null) {
                onArrival();
            }
        }
    }
    
    public void moveToBoard(Board board, Point cell) {
        futureBoard = board;
        futureBoardCell = cell;
        
        board.placeReservation(this, cell);
        
        PointF stop = board.getCellPosition(cell);

        float speed = size / 80;
        
        movingPoint.setMovement(x, y, stop.x, stop.y, speed);
    }
    
    public void changeTheme(GuiTheme theme) {
        changeColors(theme, piece, colors);
        refreshColors();
    }
    
    private void onArrival() {
        futureBoard.putPiece(this, futureBoardCell);
        clearArrival();
    }
    
    private void clearArrival() {
        futureBoard.clearReservation(futureBoardCell);
        futureBoard = null;
        futureBoardCell = null;
    }
    
    private static float[] createInitialColors(GuiTheme theme, Piece piece) {
        float[] colors = new float[6 * 3 * 4];
        
        Util.putLastColor(colors, 0, theme.pieceBackground);
        Util.putLastColor(colors, 1, theme.pieceBackground);
        
        changeColors(theme, piece, colors);
        
        return colors;
    }
    
    private static void changeColors(GuiTheme theme, Piece piece,
            float[] colors) {
        for (int i = 0; i < Piece.N_SIDES; i++) {
            Util.putLastColor(colors, i+2, theme.colors[piece.getValue(i)]);
        }
    }
    
    private static float[] createVertices(float size, float space) {
        float sp = space + 0.5f;
        
        return new float[] {
            // Top left background.
            0, 0, 0,
            size, 0, 0,
            0, size, 0,
            
            // Bottom right background.
            0, size, 0,
            size, size, 0,
            size, 0, 0,
                
            // Top.
            sp, 0, 0,
            size - sp, 0, 0,
            size/2, size/2 - sp, 0,
            
            // Right.
            size, sp, 0,
            size, size - sp, 0,
            size/2 + sp, size/2, 0,
            
            // Bottom.
            sp, size, 0,
            size/2, size/2 + sp, 0,
            size - sp, size, 0,
            
            // Left.
            0, sp, 0,
            0, size - sp, 0,
            size/2 - sp, size/2, 0,
        };
    }
    
    /*
    private float[][] hsv = new float[4][3];
    
    private void setColors(Piece piece) {
        for (int i = 0; i < Piece.N_SIDES; i++) {
            hsv[i][0] = piece.getValue(i) / ((float) Piece.N_VALUES);
            hsv[i][1] = 1f;
            hsv[i][2] = 1f;
        }
    }
    
    public void updateColors(float time) {
        for (int i = 0; i < Piece.N_SIDES; i++) {
            float hue = (hsv[i][0] + time * 0.01f) % 1.0f;
            int offset = (i+2) * 3 * 4 + 2 * 4;
            Util.fromHsvPutRgb(hue, hsv[i][1], hsv[i][2], colors, offset);
        }
        colorsBuffer.clear();
        colorsBuffer.put(colors);
        colorsBuffer.rewind();
    }
    */
}
