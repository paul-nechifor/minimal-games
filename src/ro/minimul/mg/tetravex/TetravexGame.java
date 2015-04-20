package ro.minimul.mg.tetravex;

import java.util.Iterator;
import java.util.LinkedList;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import ro.minimul.mg.Game;
import ro.minimul.mg.GameMenu;
import ro.minimul.mg.GameMenu.Item;
import ro.minimul.mg.GameMenu.StringItem;
import ro.minimul.mg.GameSettings;
import ro.minimul.mg.R;
import ro.minimul.mg.Util;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class TetravexGame implements Game {
    private static final float NANO = 1.0f / 1000000000;
    private TetravexSettings settings;
    private Context context;
    private GameMenu mainMenu;
    private GameMenu boardSizeMenu;
    private GameMenu themeMenu;
    private Positioning pos;
    private Board source, destination;
    private final LinkedList<GuiPiece> allPieces = new LinkedList<GuiPiece>();
    private GuiGrid grid;
    private GuiPiece[] underPointer = new GuiPiece[256];
    private volatile float[] clearColor = null;
    private volatile long lastTime;
    private volatile boolean gameRunning;
    
    private static class SizeItem extends Item {
        public Point size;
        
        public SizeItem(int lines, int columns) {
            super(lines + "×" + columns);
            size = new Point(lines, columns);
        }
    }
    
    @Override
    public void onGameCreated(SharedPreferences prefs, Context context) {
        settings = new TetravexSettings();
        settings.get(prefs);
        this.context = context;
        
        mainMenu = new GameMenu() {
            @Override
            protected Item[] createItems(Context context) {
                return new Item[] {
                    new StringItem(context, R.string.new_game),
                    new StringItem(context, R.string.show_solution),
                    new StringItem(context, R.string.change_game_type),
                    new StringItem(context, R.string.change_theme),
                    new StringItem(context, R.string.how_to_play)
                };
            }

            @Override
            protected void onClick(int index, Item item) {
                onMainMenuClick((StringItem) item);
            }
        };
        
        boardSizeMenu = new GameMenu() {
            @Override
            protected Item[] createItems(Context context) {
                return new Item[] {
                    new SizeItem(2, 3),
                    new SizeItem(3, 3),
                    new SizeItem(3, 4),
                    new SizeItem(4, 4),
                    new SizeItem(5, 5),
                    new SizeItem(5, 6),
                };
            }

            @Override
            protected void onClick(int index, Item item) {
                Point size = ((SizeItem) item).size;
                settings.setLinAndCol(size.x, size.y);
                newGame();
            }
        };
        
        themeMenu = new GameMenu() {
            @Override
            protected Item[] createItems(Context context) {
                Item[] ret = new Item[GuiTheme.values().length];
                
                for (int i = 0; i < ret.length; i++) {
                    ret[i] = new Item(GuiTheme.values()[i].name);
                }
                
                return ret;
            }

            @Override
            protected void onClick(int index, Item item) {
                settings.setTheme(GuiTheme.values()[index]);
                updatePiecesTheme();
            }
        };
    }

    @Override
    public void onGamePaused() {
    }

    @Override
    public void onGameResumed() {
    }

    @Override
    public void onGameTerminated() {
    }
    
    @Override
    public void onDrawFrame(GL10 gl) {
        if (clearColor != null) {
            gl.glClearColor(clearColor[0], clearColor[1], clearColor[2],
                    clearColor[3]);
            clearColor = null;
        }
        synchronized (grid) {
            grid.draw(gl);
        }
        
        synchronized (allPieces) {
            long now = System.nanoTime();
            float delta = (now - lastTime) * NANO;
            lastTime = now;
            
            for (GuiPiece piece : allPieces) {
                piece.tic(delta);
                piece.draw(gl);
            }
        }
        
        if (destination.isFull()) {
            newGame();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Set only once.
        if (pos == null) {
            pos = new Positioning(width, height, settings.getLines(),
                    settings.getColumns());
            newGame();
            
            changeBackground(settings.getTheme());
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }
    
    @Override
    public GameSettings getSettings() {
        return settings;
    }

    @Override
    public boolean onTouch(View view, MotionEvent ev) {
        int action = ev.getAction();
        int index = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) 
                >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        
        switch (action & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_POINTER_DOWN:
            fingerDown(ev.getPointerId(index), ev.getX(index), ev.getY(index));
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP:
            fingerUp(ev.getPointerId(index), ev.getX(index), ev.getY(index));
            break;
        case MotionEvent.ACTION_MOVE:
            int count = ev.getPointerCount();
            for (int i = 0; i < count; i++) {
                fingerMove(ev.getPointerId(i), ev.getX(i), ev.getY(i));
            }
            break;
        }
        return true;
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            mainMenu.buildAlertDialog(context).show();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            //clearGame();
            //return true;
        }
        
        return false;
    }

    private void onMainMenuClick(StringItem item) {
        switch (item.id) {
        case R.string.new_game:
            newGame();
            break;
        case R.string.show_solution:
            showSolution();
            break;
        case R.string.change_game_type:
            boardSizeMenu.buildAlertDialog(context).show();
            break;
        case R.string.change_theme:
            themeMenu.buildAlertDialog(context).show();
            break;
        case R.string.how_to_play:
            break;
        }
    }
    
    private void newGame() {
        int lin = settings.getLines();
        int col = settings.getColumns();
        
        pos = new Positioning(pos.width, pos.height, lin, col);
        
        source = new Board(lin, col, pos.srcX, pos.srcY, pos.size, false);
        destination = new Board(lin, col, pos.dstX, pos.dstY, pos.size, true);
        grid = new GuiGrid(settings, pos);
        
        Piece[][] pieces = PieceSetBuilder.build(lin, col);
        
        synchronized (allPieces) {
            allPieces.clear();
            for (int i = 0; i < lin; i++) {
                for (int j = 0; j < col; j++) {
                    GuiPiece p = new GuiPiece(pos.size, 1, settings.getTheme(),
                            pieces[i][j]);
                    source.putPiece(p, new Point(j, i));
                    allPieces.add(p);
                }
            }
        }
        
        lastTime = System.nanoTime();
        gameRunning = true;
    }
    
    private void fingerDown(int id, float x, float y) {
        if (!gameRunning) {
            return;
        }
        
        GuiPiece p = getPieceAt(x, y);
        if (p != null) {
            underPointer[id] = p;
            p.setTouched(x, y);
            p.moveToPointer(x, y);
            p.getBoard().clearCell(p.getBoardCell());
            
            moveToTop(p);
        }
    }
    
    private void fingerUp(int id, float x, float y) {
        GuiPiece p = underPointer[id];
        if (p != null) {
            underPointer[id] = null;
            p.setUntouched();
            p.moveToPointer(x, y);
            
            Board board = (p.getCenterY() < pos.midway) ? source : destination;
            Point cell = board.getCellFor(p);
            
            if (cell == null || !board.doesItFit(p, cell)) {
                if (p.getBoard().doesItFit(p, p.getBoardCell())) {
                    board = p.getBoard();
                    cell = p.getBoardCell();
                } else {
                    board = source;
                    cell = source.getFreeCell();
                }
            }
            
            p.moveToBoard(board, cell);
        }
    }
    
    private void fingerMove(int id, float x, float y) {
        GuiPiece p = underPointer[id];
        if (p != null) {
            p.moveToPointer(x, y);
        }
    }
    
    private GuiPiece getPieceAt(float x, float y) {
        // Check all the pieces... there aren't that many.
        for (GuiPiece p : allPieces) {
            if (p.amITouching(x, y)) {
                return p; 
            }
        }
        
        return null;
    }
    
    private void moveToTop(GuiPiece piece) {
        synchronized (allPieces) {
            Iterator<GuiPiece> iterator = allPieces.iterator();
            while (iterator.hasNext()) {
                GuiPiece listPiece = iterator.next();
                if (listPiece == piece) {
                    iterator.remove();
                    break;
                }
            }
            allPieces.addLast(piece);
        }
    }
    
    private void showSolution() {
        synchronized (allPieces) {
            for (GuiPiece p : allPieces) {
                p.moveToBoard(destination, p.getPiece().getCorrectCell());
            }
        }
        gameRunning = false;
    }
    
    private void updatePiecesTheme() {
        GuiTheme theme = settings.getTheme();
        
        changeBackground(theme);
        
        synchronized (grid) {
            grid.changeTheme(theme);
        }
        
        synchronized (allPieces) {
            for (GuiPiece p : allPieces) {
                p.changeTheme(theme );
            }
        }
    }
    
    private void changeBackground(GuiTheme theme) {
        clearColor = Util.getColor(theme.boardBackground);
    }
}
