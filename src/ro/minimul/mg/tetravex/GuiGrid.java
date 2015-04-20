package ro.minimul.mg.tetravex;

import ro.minimul.mg.TObject;
import ro.minimul.mg.Util;

public class GuiGrid extends TObject {
    private final int triangles;
    
    public GuiGrid(TetravexSettings settings, Positioning pos) {
        int boards = 2;
        int sides = 4; // The rectangles making up the border of board.
        int paitedSquares = (settings.getLines() * settings.getColumns()) / 2;
        int rectsPerBoard = sides + paitedSquares;
        int rectsTotal = boards * rectsPerBoard;
        vertices = new float[rectsTotal * Util.RECT_TRI_VERT];
        
        putBoard(vertices, settings, pos, 0, pos.srcX, pos.srcY);
        int middlePoint = rectsPerBoard * Util.RECT_TRI_VERT;
        putBoard(vertices, settings, pos, middlePoint, pos.dstX, pos.dstY);
            
        triangles = rectsTotal * 2;
        colors = new float[triangles * 3 * 4];
        
        long color = settings.getTheme().boardForeground;
        for (int i = 0; i < triangles; i++) {
            Util.putLastColor(colors, i, color);
        }
        
        verticesBuffer = Util.createFloatBuffer(vertices);
        colorsBuffer = Util.createFloatBuffer(colors);
    }
    
    public void changeTheme(GuiTheme theme) {
        changeColors(theme);
        refreshColors();
    }
    
    private void changeColors(GuiTheme theme) {
        long color = theme.boardForeground;
        for (int i = 0; i < triangles; i++) {
            Util.putLastColor(colors, i, color);
        }
    }
    
    private static void putBoard(float[] v, TetravexSettings settings,
            Positioning pos, int off, int x, int y) {
        float z = -0.9f;
        int rtv = Util.RECT_TRI_VERT;
        int lin = settings.getLines();
        int col = settings.getColumns();
        int b = pos.border;
        int size = pos.size;
        
        int baW = col * size;
        int baH = lin * size;
        
        Util.putRectangle(v, x-b, y-b, x+baW+b, y, z, off+0*rtv);
        Util.putRectangle(v, x-b, y+baH, x+baW+b, y+baH+b, z, off+1*rtv);
        Util.putRectangle(v, x-b, y, x, y+baH, z, off+2*rtv);
        Util.putRectangle(v, x+baW, y, x+baW+b, y+baH, z, off+3*rtv);
        
        int k = 4;
        
        for (int i = 0; i < lin; i++) {
            for (int j = 0; j < col; j++) {
                if ((j * col + i + ((col%2!=0)?0:((j%2!=0)?0:1))) % 2 == 1) {
                    Util.putRectangle(v, x+j*size, y+i*size,
                            x+j*size+size, y+i*size+size, z, off+k*rtv);
                    k++;
                }
            }
        }
    }
}