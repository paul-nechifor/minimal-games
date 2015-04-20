package ro.minimul.mg;

import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

/**
 * An object made out of triangles.
 * @author Paul Nechifor
 */
public abstract class TObject implements Renderable {    
    protected float[] vertices;
    protected float[] colors;
    protected FloatBuffer verticesBuffer;
    protected FloatBuffer colorsBuffer;
    
    public float x;
    public float y;
    public float z;
    
    public void draw(GL10 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorsBuffer);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vertices.length / 3);

        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glPopMatrix();
    }
    
    protected final void refreshColors() {
        colorsBuffer.clear();
        colorsBuffer.put(colors);
        colorsBuffer.rewind();
    }
}
