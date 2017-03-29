package yuya.bounceball;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by yuya on 2017/03/29.
 */

public final class Vector {
    // 内積を求める
    public static double calInnerProduct(final int[] vector1, final int[] vector2) {
        return vector1[0]*vector2[0] + vector1[1]*vector2[1];
    }

    // 外積を求める
    public static double calCrossProduct(final int[] vecotr1, final int[] vector2) {
        return vecotr1[0]*vector2[1] - vecotr1[1]*vector2[0];
    }

    // ベクトルの絶対値を求める
    public static int calVectorAbsoluteValue(final int[] vector1) {
        return (int)sqrt(pow(vector1[0], 2) + pow(vector1[1], 2));
    }

    // ベクトルの絶対値を求める
    public static double calVectorAbsoluteValue(final double[] vector1) {
        return sqrt(pow(vector1[0], 2) + pow(vector1[1], 2));
    }

    // 2つのベクトルのなす角を求める
    public static double calAngleOfVector(int[] vector1, int[] vector2) {
        double cos = calInnerProduct(vector1, vector2)/calVectorAbsoluteValue(vector1)/calVectorAbsoluteValue(vector2);
        double angle = Math.toDegrees(Math.acos(cos));
        return angle;
    }
}
