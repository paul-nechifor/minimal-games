package ro.minimul.mg;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class MinimalRenderer implements Renderer {
    private Game game;
    
    public MinimalRenderer() {
    }
    
    public void setGame(Game game) {
        this.game = game;
    }
    
    public Game getGame() {
        return this.game;
    }
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        gl.glClearColor(1, 1, 1, 1);
        gl.glClearDepthf(1);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        
        game.onSurfaceCreated(gl, eglConfig);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        
        // Normal view where top-left is 0,0 and bottom-right is height,width.
        GLU.gluOrtho2D(gl, 0, width, height, 0);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glFrontFace(GL10.GL_CW);
        gl.glShadeModel(GL10.GL_FLAT);
        
        game.onSurfaceChanged(gl, width, height);
    }
    
    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        game.onDrawFrame(gl);
    }
}
