package yuya.bounceball;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yuya on 2017/02/22.
 */

public class OpenCvCameraView implements CvCameraViewListener2 {

    public static Mat mat1, mat2;
    public static Bitmap bmp1, bmp2;

    OpenCvPolygon polygon = new OpenCvPolygon();

    private static final String TAG = "OpenCV::Camera";
    private MainActivity main;
    private CameraBridgeViewBase mOpenCvCameraView;

    public OpenCvCameraView(MainActivity main) {
        this.main = main;
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    // ライブラリ初期化完了後に呼ばれるコールバック
    public BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(main) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    public void deleteCameraBridgeViewBase() {
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void loadOpenCvLibrary() {
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, main, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onCameraViewStarted(int width, int height) {

    }

    public void onCameraViewStopped() {

    }

    // カメラ画像を描画
    public void showCameraView() {
        main.setContentView(R.layout.camera_view);
        // 画面を横向きに固定
        main.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // フルスクリーン設定
        main.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // タイトルバー非表示
        main.getSupportActionBar().hide();
        mOpenCvCameraView = (CameraBridgeViewBase)main.findViewById(R.id.camera_view);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgbaMat = inputFrame.rgba();
        Mat outMat = rgbaMat.submat(0, (int)rgbaMat.size().height, 0, (int)rgbaMat.size().width);
        mat1 = rgbaMat.clone();
        polygon.findPolygon(rgbaMat);
        polygon.drawPolygon(outMat);
        return outMat;
    }

    public void saveBitmap() {
        try {
            bmp1 = Bitmap.createBitmap(mat1.width(), mat1.height(), Bitmap.Config.ARGB_8888);
            bmp2 = Bitmap.createBitmap(mat2.width(), mat2.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat1, bmp1);
            Utils.matToBitmap(mat2, bmp2);

            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

            // 日付でファイル名を作成
            Date mDate = new Date();
            SimpleDateFormat fileName = new SimpleDateFormat("yyyyMMdd_HHmmss");

            // 保存処理開始
            FileOutputStream fos1 = new FileOutputStream(new File(root, fileName.format(mDate) + "_bmp1.jpg"));
            FileOutputStream fos2 = new FileOutputStream(new File(root, fileName.format(mDate) + "_bmp2.jpg"));

            // jpegで保存
            bmp1.compress(Bitmap.CompressFormat.JPEG, 100, fos1);
            bmp2.compress(Bitmap.CompressFormat.JPEG, 100, fos2);
        } catch (Exception e) {
            Log.e("Error", "" + e.toString());
        }
    }
}