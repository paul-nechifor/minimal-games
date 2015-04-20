package ro.minimul.mg;

import ro.minimul.mg.tetravex.TetravexGame;
import android.content.SharedPreferences;

public class GlobalSettings implements GameSettings {
    public static final int TETRAVEX_GAME = 0;
    
    private int activeGameIndex = 0;
    
    @Override
    public void maybeCommit(SharedPreferences prefs) {
    }

    @Override
    public void get(SharedPreferences prefs) {
    }
    
    public void setActiveGameIndex(int index) {
        this.activeGameIndex = index;
    }
    
    public Game getActiveGameNewInstance() {
        switch (activeGameIndex) {
        case 0:
            return new TetravexGame();
        default:
            throw new AssertionError();
        }
    }
}
