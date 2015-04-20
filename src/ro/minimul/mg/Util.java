package ro.minimul.mg;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Util {
    /**
     * The number of vertices necessary to make a rectangle out of two
     * triangles.
     */
    public final static int RECT_TRI_VERT = 18;
    
    private final static int FLOAT_SIZE = 4;
    private final static int COLOR_DIMS = 4;
    
    private Util() {
    }
    
    public static FloatBuffer createFloatBuffer(float[] floats) {
        ByteBuffer bb = ByteBuffer.allocateDirect(floats.length * FLOAT_SIZE);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = bb.asFloatBuffer();
        floatBuffer.put(floats);
        floatBuffer.rewind();
        return floatBuffer;
    }

    /**
     * Only the last point in the triangle gets a color since a flat shading
     * model is used.
     */
    public static void putLastColor(float[] colors, int index, long color) {
        int offset = index * 3 * COLOR_DIMS + 2 * COLOR_DIMS;
        putColor(colors, offset, color);
    }
    
    public static void putColor(float[] colors, int offset, long color) {
        colors[offset + 0] = ((color >> 16) & 0xFF) / 255.0f;
        colors[offset + 1] = ((color >>  8) & 0xFF) / 255.0f;
        colors[offset + 2] = ((color >>  0) & 0xFF) / 255.0f;
        colors[offset + 3] = ((color >> 24) & 0xFF) / 255.0f;
    }
    
    public static float[] getColor(long color) {
        return new float[] {
            ((color >> 16) & 0xFF) / 255.0f,
            ((color >>  8) & 0xFF) / 255.0f,
            ((color >>  0) & 0xFF) / 255.0f,
            ((color >> 24) & 0xFF) / 255.0f,
        };
    }
    
    /**
     * Makes a rectangle out of two triangles (top-right, bottom-left) using
     * RECT_TRI_VERT vertices.
     */
    public static void putRectangle(float[] vert, float x1, float y1, float x2,
            float y2, float z, int offset) {
        vert[offset + 0] = x1;
        vert[offset + 1] = y1;
        vert[offset + 2] = z;
        
        vert[offset + 3] = x2;
        vert[offset + 4] = y1;
        vert[offset + 5] = z;
        
        vert[offset + 6] = x1;
        vert[offset + 7] = y2;
        vert[offset + 8] = z;
        
        vert[offset + 9] = x2;
        vert[offset + 10] = y1;
        vert[offset + 11] = z;
        
        vert[offset + 12] = x2;
        vert[offset + 13] = y2;
        vert[offset + 14] = z;
        
        vert[offset + 15] = x1;
        vert[offset + 16] = y2;
        vert[offset + 17] = z;
    }
    
    /**
     * Choose a result by simulatting a long if-elseif-else chain with <=
     * operator.
     * 
     * @return c[0] if v<=c[1], c[2] if v<=c[3], ..., c[n-1].
     */
    public static int chooseLTE(int value, int[] choices) {
        if (choices.length % 2 == 0) {
            throw new IllegalArgumentException("Must be of odd length.");
        }
        
        for (int i = 0; i < choices.length-1; i += 2) {
            if (value <= choices[i+1]) {
                return choices[i];
            }
        }
        
        return choices[choices.length - 1];
    }
    
    public static void fromHsvPutRgb(float hue, float sat, float val,
            float[] colors, int offset) {
        float r, g, b;
        int h = (int)(hue * 6);
        float f = hue * 6 - h;
        float p = val * (1 - sat);
        float q = val * (1 - f * sat);
        float t = val * (1 - (1 - f) * sat);

        if (h == 0) {
            r = val;
            g = t;
            b = p;
        } else if (h == 1) {
            r = q;
            g = val;
            b = p;
        } else if (h == 2) {
            r = p;
            g = val;
            b = t;
        } else if (h == 3) {
            r = p;
            g = q;
            b = val;
        } else if (h == 4) {
            r = t;
            g = p;
            b = val;
        } else if (h == 5) {
            r = val;
            g = p;
            b = q;
        } else {
            throw new RuntimeException(hue + ", " + sat + ", " + val);
        }
        
        colors[offset + 0] = r;
        colors[offset + 1] = g;
        colors[offset + 2] = b;
    }
}
