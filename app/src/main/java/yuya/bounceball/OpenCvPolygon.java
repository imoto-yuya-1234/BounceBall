package yuya.bounceball;

import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuya on 2017/02/26.
 */

public class OpenCvPolygon {

    // 図形の頂点座標を保持
    static public ArrayList<ArrayList<Integer>> cornerPoints = new ArrayList<ArrayList<Integer>>();

    // 図形を検出
    public void findPolygon(final Mat inMat) {
        Mat tempMat = new Mat();
        Mat save1Mat, save2Mat;

        // 入力画像をグレースケール変換
        Imgproc.cvtColor(inMat, tempMat, Imgproc.COLOR_RGBA2GRAY);
        // 輝度値の高い箇所を抽出
        Imgproc.threshold(tempMat, tempMat, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        //save1Mat = tempMat.clone();

        // 輪郭を抽出
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(tempMat, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_TC89_L1);
        //save2Mat = inMat.clone();
        //Imgproc.drawContours(save2Mat, contours, -1, new Scalar(0, 255, 0), 1);

        //ImageManager.INSTANCE.setMat(save1Mat);
        //ImageManager.INSTANCE.setMat(save2Mat);

        tempMat.release();

        // 得られた全ての輪郭から四角形を抽出
        cornerPoints.clear();
        for (int i = 0; i < contours.size(); i++) {

            // 輪郭の面積を代入
            double contourArea = Imgproc.contourArea(contours.get(i));
            // 面積の大きさを制限
            if (contourArea > 200000 && contourArea < 400000) {
                // 輪郭のポリゴン近似
                MatOfPoint2f contours2f = new MatOfPoint2f(contours.get(i).toArray());
                MatOfPoint2f approx2f = new MatOfPoint2f();
                Imgproc.approxPolyDP(contours2f, approx2f, 0.05*Imgproc.arcLength(contours2f, true), true);

                // 凸包の取得
                MatOfPoint approx = new MatOfPoint(approx2f.toArray());
                MatOfInt hull = new MatOfInt();
                Imgproc.convexHull(approx, hull);

                // 四角形の座標を取得
                if (hull.size().height == 4) {
                    ArrayList<Integer> srcPoints = new ArrayList<Integer>();
                    for (int k = 0; k < hull.size().height; k++) {
                        int hullIndex = (int)hull.get(k, 0)[0];
                        double[] m = approx.get(hullIndex, 0);
                        srcPoints.add((int)m[0]);
                        srcPoints.add((int)m[1]);
                    }
                    cornerPoints.add(srcPoints);
                }
            }
        }
    }

    // 図形を描画
    public void drawPolygon(Mat inMat) {
        for (int i = 0; i < cornerPoints.size(); i++) {
            for (int j = 0; j < cornerPoints.get(i).size()/2; j++) {
                Point pt1, pt2;
                pt1 = new Point(cornerPoints.get(i).get(2*j), cornerPoints.get(i).get(2*j + 1));
                if (j < cornerPoints.get(i).size()/2 - 1) {
                    pt2 = new Point(cornerPoints.get(i).get(2*(j + 1)), cornerPoints.get(i).get(2*(j + 1) + 1));
                } else {
                    pt2 = new Point(cornerPoints.get(i).get(0), cornerPoints.get(i).get(1));
                }
                Imgproc.line(inMat, pt1, pt2, new Scalar(255, 0, 0), 2);
            }
        }
    }
}
