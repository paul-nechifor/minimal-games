package ro.minimul.mg;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    private SharedPreferences sharedPreferences;
    private GlobalSettings globalSettings;
    private GLSurfaceView glSurfaceView;
    private MinimalRenderer minimalRenderer;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set full screen view.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        // Hide title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Lock screen in portrait orientation.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        sharedPreferences = getPreferences(MODE_PRIVATE);
        globalSettings = new GlobalSettings();
        globalSettings.get(sharedPreferences);
        
        glSurfaceView = new GLSurfaceView(this);
        minimalRenderer = new MinimalRenderer();
        initalizeGameType();
        glSurfaceView.setRenderer(minimalRenderer);
        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
        minimalRenderer.getGame().getSettings().maybeCommit(sharedPreferences);
        minimalRenderer.getGame().onGamePaused();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
        minimalRenderer.getGame().onGameResumed();
    }
    
    private void initalizeGameType() {
        Game lastGame = minimalRenderer.getGame();
        if (lastGame != null) {
            lastGame.onGameTerminated();
        }
        
        Game game = globalSettings.getActiveGameNewInstance();
        game.onGameCreated(sharedPreferences, this);
        minimalRenderer.setGame(game);
        glSurfaceView.setOnTouchListener(game);
        glSurfaceView.setOnKeyListener(game);
        glSurfaceView.requestFocus();
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return minimalRenderer.getGame().onKey(glSurfaceView, keyCode, event);
    }
}
