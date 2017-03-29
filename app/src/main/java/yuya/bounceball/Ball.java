package yuya.bounceball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by yuya on 2017/03/10.
 */

public class Ball extends View {

    public int dx, dy, time, x, y, radius;
    public double velocity;
    private Paint paint;
    private int offsetX, offsetY;
    private CoordinateCal cCal;

    public Ball(Context context) {
        super(context);
        dx = dy = 20;
        velocity = sqrt(pow(dx, 2) + pow(dy, 2));
        time = 100;
        x = y = 0;
        radius = 30;
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        offsetX = offsetY = 0;
        cCal = new CoordinateCal(radius, velocity);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawCircle(x, y, radius, paint);
    }

    // ボールの動きを決める
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

        // 検出した多角形に衝突したか調べる
        ArrayList<ArrayList<Integer>> cornerPoints = new ArrayList<ArrayList<Integer>>(OpenCvPolygon.cornerPoints);
        for (int i = 0; i < cornerPoints.size(); i++) {
            int[] pt = new int[4];
            cCal.setBallInfo(x, y, dx, dy);
            for (int j = 0; j < cornerPoints.get(i).size()/2; j++) {
                pt[0] = cornerPoints.get(i).get(2 * j) + offsetX;
                pt[1] = cornerPoints.get(i).get(2 * j + 1) + offsetY;
                if (j < cornerPoints.get(i).size() / 2 - 1) {
                    pt[2] = cornerPoints.get(i).get(2 * (j + 1)) + offsetX;
                    pt[3] = cornerPoints.get(i).get(2 * (j + 1) + 1) + offsetY;
                } else {
                    pt[2] = cornerPoints.get(i).get(0) + offsetX;
                    pt[3] = cornerPoints.get(i).get(1) + offsetY;
                }

                // ボールと辺が衝突したかを調べる
                cCal.checkCollided(pt[0], pt[1], pt[2], pt[3]);
            }
            if (cCal.collided) {
                cCal.processReflection();
                x = cCal.x;
                y = cCal.y;
                dx = cCal.dx;
                dy = cCal.dy;
                break;
            }
        }
    }
}
