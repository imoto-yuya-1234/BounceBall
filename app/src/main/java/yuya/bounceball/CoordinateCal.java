package yuya.bounceball;

import android.util.Log;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static yuya.bounceball.Vector.calCrossProduct;
import static yuya.bounceball.Vector.calInnerProduct;
import static yuya.bounceball.Vector.calVectorAbsoluteValue;

/**
 * Created by yuya on 2017/03/25.
 */

public class CoordinateCal {

    public int x, y, dx, dy;
    private int radius;
    private double velocity, distance;
    private int x1, y1, x2, y2;
    private int[] cornerVector1, cornerVector2;
    public boolean collided, corner, outside;

    public CoordinateCal(int radius, double velocity) {
        this.radius = radius;
        this.velocity = velocity;
    }

    public void setBallInfo(int x, int y, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.distance = -1;
        this.collided = this.corner = this.outside = false;
    }

    // ボールと辺が衝突したかを求める
    public void checkCollided(final int x1, final int y1, final int x2, final int y2) {
        int[] vectorBarrier1 = new int[]{x2 - x1, y2 - y1};
        int[] vectorBall1 = new int[]{x - x1, y - y1};
        int[] vectorBarrier2 = new int[]{x1 - x2, y1 - y2};
        int[] vectorBall2 = new int[]{x - x2, y - y2};
        boolean corner;
        double distance;

        if (calInnerProduct(vectorBarrier1, vectorBall1) < 0) {
            distance = calVectorAbsoluteValue(vectorBall1);
            corner = true;
            cornerVector1 = new int[]{10*vectorBarrier2[0]/calVectorAbsoluteValue(vectorBarrier2), 10*vectorBarrier2[1]/calVectorAbsoluteValue(vectorBarrier2)};
        } else if (calInnerProduct(vectorBarrier2, vectorBall2) < 0) {
            distance = calVectorAbsoluteValue(vectorBall2);
            corner = true;
            cornerVector2 = new int[]{10*vectorBarrier1[0]/calVectorAbsoluteValue(vectorBarrier1), 10*vectorBarrier1[1]/calVectorAbsoluteValue(vectorBarrier1)};
        } else {
            distance = abs(calCrossProduct(vectorBarrier1, vectorBall1)) / calVectorAbsoluteValue(vectorBarrier1);
            corner = false;
        }

        Log.d("Distance ", String.valueOf(distance));

        // ボールが辺に接触したとき
        if (distance < radius) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.collided = true;
            this.distance = distance;
            this.corner = corner;
        }
        // ボールの位置が多角形の内か外かを調べる
        // 辺ベクトルに対して一つでも右側にボールがあれば外にあるといえる
        if (calCrossProduct(vectorBarrier1, vectorBall1) < 0 || this.outside) {
            this.outside = true;
        }
    }

    // ボールの反射処理
    public void processReflection() {
        // 法線ベクトルを決定
        int[] n;
        if (corner) {
            n = new int[]{cornerVector1[0] + cornerVector2[0], cornerVector1[1] + cornerVector2[1]};
        } else {
            if (outside) {
                n = new int[]{y2 - y1, -(x2 - x1)};
            } else {
                n = new int[]{-(y2 - y1), x2 - x1};
            }
        }

        double nLength = calVectorAbsoluteValue(n);

        // ボールの位置を補正
        x += (radius - distance)*n[0]/nLength;
        y += (radius - distance)*n[1]/nLength;

        // ボールの方向を計算
        // ボールの進行ベクトル
        int[] v = new int[]{dx, dy};
        // ボールの反射ベクトルを算出
        double a = - 2*(calInnerProduct(v, n))/pow(nLength, 2);
        double[] ReflectionVector = new double[]{v[0] + a*n[0], v[1] + a*n[1]};
        // ボールの反射ベクトルの大きさを補正する係数を算出
        double k = velocity/calVectorAbsoluteValue(ReflectionVector);
        // ボールの方向を決定
        dx = (int)(k*ReflectionVector[0]);
        dy = (int)(k*ReflectionVector[1]);

        Log.d("Velocity ", String.valueOf(sqrt(pow(dx, 2) + pow(dy, 2))));
    }
}
