package yuya.bounceball;

import android.util.Log;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by yuya on 2017/03/25.
 */

public class CoordinateCal {

    public int x, y, dx, dy;
    private int radius;
    private double velocity, distance, angle;
    private int x1, y1, x2, y2;
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
        this.angle = 0;
        this.collided = this.corner = this.outside = false;
    }

    // ボールと辺が衝突したかを求める
    public void calBallState(final int x1, final int y1, final int x2, final int y2) {
        if (!this.corner) {
            int[] vectorBarrier1 = new int[]{x2 - x1, y2 - y1};
            int[] vectorBall1 = new int[]{x - x1, y - y1};
            int[] vectorBarrier2 = new int[]{x1 - x2, y1 - y2};
            int[] vectorBall2 = new int[]{x - x2, y - y2};
            boolean corner;
            double distance;

            if (calInnerProduct(vectorBarrier1, vectorBall1) < 0) {
                distance = calVectorAbsoluteValue(vectorBall1);
                corner = true;
            } else if (calInnerProduct(vectorBarrier2, vectorBall2) < 0) {
                distance = calVectorAbsoluteValue(vectorBall2);
                corner = true;
            } else {
                distance = abs(calCrossProduct(vectorBarrier1, vectorBall1)) / calVectorAbsoluteValue(vectorBarrier1);
                corner = false;
            }

            Log.d("Distance ", String.valueOf(distance));

            angle += calAngleOfVector(new int[]{x - x1, y - y1}, new int[]{x - x2, y - y2});
            if (distance < radius) {
                this.x1 = x1;
                this.y1 = y1;
                this.x2 = x2;
                this.y2 = y2;
                this.collided = true;
                this.corner = corner;
                this.distance = distance;
                if (corner) {
                    angle = 0;
                }
            }
        }
    }

    public void calBallStatus() {
        if (360 - angle > 1E-3) {
            outside = true;
        } else {
            outside = false;
        }

        if (corner) {
            processCornerReflection();
        } else {
            processReflection(new int[]{x2 - x1, y2 - y1});
        }
    }

    // ボールが角で反射する場合の反射処理
    public void processCornerReflection() {
        int[] n;
        n = new int[]{-dx, -dy};

        double nLength = calVectorAbsoluteValue(n);

        // ボールの位置を補正
        x += (radius - distance)*n[0]/nLength;
        y += (radius - distance)*n[1]/nLength;

        // ボールの方向を修正
        dx = -dx;
        dy = -dy;

        Log.d("Velocity ", String.valueOf(sqrt(pow(dx, 2) + pow(dy, 2))));
    }

    // ボールが辺で反射する場合の反射処理
    public void processReflection(final int[] vectorBarrier) {
        // 法線ベクトルを決定
        int[] n;
        if (outside) {
            n = new int[]{vectorBarrier[1], -vectorBarrier[0]};
        } else {
            n = new int[]{-vectorBarrier[1], vectorBarrier[0]};
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

    // 内積を求める
    public double calInnerProduct(final int[] vector1, final int[] vector2) {
        return vector1[0]*vector2[0] + vector1[1]*vector2[1];
    }

    // 外積を求める
    public double calCrossProduct(final int[] vecotr1, final int[] vector2) {
        return vecotr1[0]*vector2[1] - vecotr1[1]*vector2[0];
    }

    // ベクトルの絶対値を求める
    public int calVectorAbsoluteValue(final int[] vector1) {
        return (int)sqrt(pow(vector1[0], 2) + pow(vector1[1], 2));
    }

    // ベクトルの絶対値を求める
    public double calVectorAbsoluteValue(final double[] vector1) {
        return sqrt(pow(vector1[0], 2) + pow(vector1[1], 2));
    }

    // 2つのベクトルのなす角を求める
    public double calAngleOfVector(int[] vector1, int[] vector2) {
        double cos = calInnerProduct(vector1, vector2)/calVectorAbsoluteValue(vector1)/calVectorAbsoluteValue(vector2);
        double angle = Math.toDegrees(Math.acos(cos));
        return angle;
    }
}
