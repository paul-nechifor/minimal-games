package ro.minimul.mg;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView.Renderer;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;

public interface Game extends Renderer, OnTouchListener, OnKeyListener {
    public void onGameCreated(SharedPreferences prefs, Context context);
    public void onGamePaused();
    public void onGameResumed();
    public void onGameTerminated();
    public GameSettings getSettings();
}
