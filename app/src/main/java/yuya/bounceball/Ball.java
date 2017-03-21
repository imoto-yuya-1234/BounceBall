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
    private Paint paint;
    private int offsetX, offsetY;

    public Ball(Context context) {
        super(context);
        dx = dy = 10;
        time = 100;
        x = y = 0;
        radius = 30;
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        offsetX = offsetY = 0;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawCircle(x, y, radius, paint);
    }

    public void moveBall(int startX, int startY, int endX, int endY) {
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

        if (isCollided()) {
            dx = -dx;
            dy = -dy;
        }
    }

    public boolean isCollided() {
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
                int d = calDistance(pt1[0], pt1[1], pt2[0], pt2[1]);
                Log.d("Distance ", String.valueOf(d));
                if (d > 0 && d < radius) {
                    return true;
                }
            }
        }
        return false;
    }

    public int calDistance(int x1, int y1, int x2, int y2) {
        int[] vector = new int[]{x2 - x1, y2 - y1};
        int[] vectorBall = new int[]{x - x1, y - y1};
        if (calInnerProduct(vector, vectorBall) < 0.0F) {
            return -1;
        }

        vector = new int[]{x1 - x2, y1 - y2};
        vectorBall = new int[]{x - x2, y - y2};
        if (calInnerProduct(vector, vectorBall) < 0.0F) {
            return -1;
        }

        return abs(vector[0]*vectorBall[1] - vector[1]*vectorBall[0])/(int)sqrt(pow(vector[0], 2) + pow(vector[1], 2));
    }

    public int calInnerProduct(int[] vector1, int[] vector2) {
        return vector1[0]*vector2[0] + vector1[1]*vector2[1];
    }
}
