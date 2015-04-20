package ro.minimul.mg.rivers;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import ro.minimul.mg.Game;
import ro.minimul.mg.GameSettings;

public class RiversGame implements Game {
    @Override
    public void onDrawFrame(GL10 arg0) {
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int arg1, int arg2) {
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        return false;
    }

    @Override
    public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
        return false;
    }

    @Override
    public void onGameCreated(SharedPreferences prefs, Context context) {
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
    public GameSettings getSettings() {
        // TODO Auto-generated method stub
        return null;
    }

}
