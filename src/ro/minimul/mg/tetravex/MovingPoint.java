package ro.minimul.mg.tetravex;

import android.graphics.PointF;

public class MovingPoint {
    private boolean moving = false;
    private PointF point = new PointF();
    private float speedX, speedY;
    private float accX, accY;
    private float stopX, stopY;
    private boolean cmpX, cmpY;
    
    public MovingPoint() {
    }
    
    public void setMovement(float x, float y, float stopAtX, float stopAtY, 
            float speed) {
        moving = true;
        point.x = x;
        point.y = y;
        stopX = stopAtX;
        stopY = stopAtY;
        accX = 0;
        accY = 0;
        
        float dx = stopX - x;
        float dy = stopY - y;
        
        speedX = dx * speed;
        speedY = dy * speed;
        
        cmpX = dx > 0;
        cmpY = dy > 0;
    }
    
    public boolean isMoving() {
        return moving;
    }
    
    public PointF getNewPosition(float delta) {
        if (moving) {
            speedX += accX;
            speedY += accY;
            point.x += speedX * delta;
            point.y += speedY * delta;
            
            if (cmp(cmpX, point.x, stopX) || cmp(cmpY, point.y, stopY)) {
                stop();
            }
        }
        
        return point;
    }
    
    public void stop() {
        moving = false;
        accX = 0;
        accY = 0;
        speedX = 0;
        speedY = 0;
        point.x = stopX;
        point.y = stopY;
    }

    private static boolean cmp(boolean positive, float a, float b) {
        if (positive) {
            return a - b >= 0;
        } else {
            return a - b <= 0;
        }
    }
}