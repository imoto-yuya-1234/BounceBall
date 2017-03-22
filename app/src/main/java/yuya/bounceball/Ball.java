package yuya.bounceball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by yuya on 2017/03/10.
 */

public class Ball extends View {

    public int dx, dy, time, x, y, radius;
    private double velocity;
    private Paint paint;
    private int beforeX, beforeY;
    private int offsetX, offsetY;

    public Ball(Context context) {
        super(context);
        dx = dy = 10;
        velocity = sqrt(pow(dx, 2) + pow(dy, 2));
        time = 100;
        x = y = 0;
        radius = 30;
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        beforeX = x;
        beforeY = y;
        offsetX = offsetY = 0;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawCircle(x, y, radius, paint);
    }

    public void moveBall(final int startX, final int startY, final int endX, final int endY) {
        offsetX = startX;
        offsetY = startY;

        x += dx;
        y += dy;

        if (x <= startX + radius) {
            x = startX + radius;
            dx = -dx;
        } else if (x >= endX - radius) {
            x = endX - radius;
            dx = -dx;
        }

        if (y <= startY + radius) {
            y = startY + radius;
            dy = -dy;
        } else if (y >= endY - radius) {
            y = endY - radius;
            dy = -dy;
        }

        checkCollided();
    }

    public boolean checkCollided() {
        ArrayList<ArrayList<Integer>> cornerPoints = new ArrayList<ArrayList<Integer>>(OpenCvPolygon.cornerPoints);
        for (int i = 0; i < cornerPoints.size(); i++) {
            for (int j = 0; j < cornerPoints.get(i).size()/2; j++) {
                int[] pt1 = new int[]{cornerPoints.get(i).get(2*j) + offsetX, cornerPoints.get(i).get(2*j + 1) + offsetY};
                int[] pt2;
                if (j < cornerPoints.get(i).size()/2 - 1) {
                    pt2 = new int[]{cornerPoints.get(i).get(2*(j + 1)) + offsetX, cornerPoints.get(i).get(2*(j + 1) + 1) + offsetY};
                } else {
                    pt2 = new int[]{cornerPoints.get(i).get(0) + offsetX, cornerPoints.get(i).get(1) + offsetY};
                }

                if (isCollided(pt1[0], pt1[1], pt2[0], pt2[1])) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCollided(final int x1, final int y1, final int x2, final int y2) {
        int[] vectorBarrier = new int[]{x2 - x1, y2 - y1};
        int[] vectorBall = new int[]{x - x1, y - y1};
        if (calInnerProduct(vectorBarrier, vectorBall) < 0.0F) {
            return false;
        }

        vectorBarrier = new int[]{x1 - x2, y1 - y2};
        vectorBall = new int[]{x - x2, y - y2};
        if (calInnerProduct(vectorBarrier, vectorBall) < 0.0F) {
            return false;
        }

        int d = abs(vectorBarrier[0]*vectorBall[1] - vectorBarrier[1]*vectorBall[0])/(int)calVectorAbsoluteValue(vectorBarrier);
        Log.d("Distance ", String.valueOf(d));
        if (d <= radius) {
            int[] n = new int[]{-vectorBarrier[1], vectorBarrier[0]};
            double nLength = calVectorAbsoluteValue(n);
            x += (radius - d)*n[0]/nLength;
            y += (radius - d)*n[1]/nLength;

            int[] v = new int[]{dx, dy};
            double a = - 2*(calInnerProduct(v, n))/pow(nLength, 2);
            double[] tempReflectionVector = new double[]{v[0] + a*n[0], v[1] + a*n[1]};
            double k = velocity/calVectorAbsoluteValue(tempReflectionVector);
            dx = (int)(k*tempReflectionVector[0]);
            dy = (int)(k*tempReflectionVector[1]);
            Log.d("Velocity ", String.valueOf(sqrt(pow(dx, 2) + pow(dy, 2))));

            return true;
        } else {
            return false;
        }
    }

    public double calInnerProduct(final int[] vector1, final int[] vector2) {
        return vector1[0]*vector2[0] + vector1[1]*vector2[1];
    }

    public int calVectorAbsoluteValue(final int[] vector1) {
        return (int)sqrt(pow(vector1[0], 2) + pow(vector1[1], 2));
    }

    public double calVectorAbsoluteValue(final double[] vector1) {
        return sqrt(pow(vector1[0], 2) + pow(vector1[1], 2));
    }
}
