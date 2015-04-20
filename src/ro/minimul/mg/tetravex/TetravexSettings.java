package ro.minimul.mg.tetravex;

import ro.minimul.mg.GameSettings;
import android.content.SharedPreferences;

public class TetravexSettings implements GameSettings {
    private int lines = 3;
    private int columns = 3;
    private GuiTheme theme = GuiTheme.WHITE_SATURATED;
    
    private transient boolean modified = true;
    
    public int getLines() {
        return lines;
    }
    
    public int getColumns() {
        return columns;
    }
    
    public GuiTheme getTheme() {
        return theme;
    }
    
    public void setLinAndCol(int lin, int col) {
        this.lines = lin;
        this.columns = col;
        this.modified = true;
    }
    
    public void setTheme(GuiTheme theme) {
        this.theme = theme;
        this.modified = true;
    }

    @Override
    public void maybeCommit(SharedPreferences prefs) {
        if (modified) {
            SharedPreferences.Editor editor = prefs.edit();
            
            editor.putInt("tetravex.lines", lines);
            editor.putInt("tetravex.columns", columns);
            editor.putInt("tetravex.theme", theme.ordinal());
            
            editor.commit();
        }
    }

    @Override
    public void get(SharedPreferences prefs) {
        lines = prefs.getInt("tetravex.lines", lines);
        columns = prefs.getInt("tetravex.columns", columns);
        theme = GuiTheme.values()[prefs.getInt("tetravex.theme",
                theme.ordinal())];
    }
}
