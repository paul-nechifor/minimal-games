package ro.minimul.mg.tetravex;

import static ro.minimul.mg.tetravex.Piece.BOTTOM;
import static ro.minimul.mg.tetravex.Piece.LEFT;
import static ro.minimul.mg.tetravex.Piece.RIGHT;
import static ro.minimul.mg.tetravex.Piece.TOP;
import android.graphics.Point;
import android.graphics.PointF;

public class Board {
    private final GuiPiece[][] pieces;
    private final GuiPiece[][] reservations;
    private int lines, columns;
    private float x, y;
    private float pSize;
    private final boolean checkNeighbors;
    private int total = 0;
    
    public Board(int lines, int columns, float x, float y, float pSize,
            boolean checkNeighbors) {
        this.pieces = new GuiPiece[lines][columns];
        this.reservations = new GuiPiece[lines][columns];
        this.lines = lines;
        this.columns = columns;
        this.x = x;
        this.y = y;
        this.pSize = pSize;
        this.checkNeighbors = checkNeighbors;
    }
    
    public Point getCellFor(GuiPiece p) {
        float dx = p.getCenterX() - x;
        float dy = p.getCenterY() - y;
        
        if (dx < 0 || dy < 0 || dx >= columns*pSize || dy >= lines*pSize) {
            return null;
        }
        
        int i = (int)(dy / pSize);
        int j = (int)(dx / pSize);
        
        return new Point(j, i);
    }
    
    public boolean doesItFit(GuiPiece p, Point cell) {
        int i = cell.y;
        int j = cell.x;
        
        if (pieces[i][j] != null) {
            return false;
        }
        
        GuiPiece reserved = reservations[i][j];
        if (reserved != null && reserved != p) {
            return false;
        }

        if (checkNeighbors) {
            Piece c = p.getPiece();
            
            // Checking left piece.
            if (i > 0 && notFit(i-1, j, BOTTOM, c.getValue(TOP))) {
                return false;
            }
            // Checking top piece.
            if (j > 0 && notFit(i, j-1, RIGHT, c.getValue(LEFT))) {
                return false;
            }
            // Checking right piece.
            if (i < lines-1 && notFit(i+1, j, TOP, c.getValue(BOTTOM))) {
                return false;
            }
            // Checking bottom piece.
            if (j < columns-1 && notFit(i, j+1, LEFT, c.getValue(RIGHT))) {
                return false;
            }
        }
        
        return true;
    }
    
    public Point getFreeCell() {
        assert checkNeighbors : "Only source board can adopt.";
        
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                if (pieces[i][j] == null && reservations[i][j] == null) {
                    return new Point(j, i);
                }
            }
        }
        
        throw new AssertionError("Not reachable.");
    }
    
    public void putPiece(GuiPiece p, Point cell) {
        pieces[cell.y][cell.x] = p;
        p.placeOnBoard(this, cell);
        total++;
    }
    
    public void clearCell(Point cell) {
        int i = cell.y;
        int j = cell.x;
        
        if (pieces[i][j] != null) {
            pieces[i][j] = null;
            total--;
        }
    }
    
    public void placeReservation(GuiPiece p, Point cell) {
        int i = cell.y;
        int j = cell.x;
        
        assert reservations[i][j] == null;
        
        reservations[i][j] = p;
    }
    
    public void clearReservation(Point cell) {
        int i = cell.y;
        int j = cell.x;
        
        assert reservations[i][j] != null;
        
        reservations[i][j] = null;
    }
    
    public PointF getCellPosition(Point cell) {
        return new PointF(x + cell.x * pSize, y + cell.y * pSize);
    }
    
    public boolean isFull() {
        return total == (lines * columns);
    }
    
    private boolean notFit(int i, int j, int side, int value) {
        GuiPiece p = pieces[i][j];
        if (p == null) {
            return false;
        }
        return p.getPiece().getValue(side) != value;
    }
}
