package yuya.bounceball;

import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

/**
 * Created by yuya on 2017/02/22.
 */

public class OpenCvCamera implements CvCameraViewListener2 {

    OpenCvPolygon polygon = new OpenCvPolygon();

    private static final String TAG = "OpenCV::Camera";
    private MainActivity main;
    private CameraBridgeViewBase mOpenCvCameraView;
    static public int width, height;

    public OpenCvCamera(MainActivity main) {
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
    public void showCameraView(MainActivity main) {
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

    // カメラ画像を解析
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat inMat = inputFrame.rgba();
        Mat rgbaMat = inMat.clone();
        this.width = (int)inMat.size().width;
        this.height = (int)inMat.size().height;
        ImageManager.INSTANCE.clearId();

        polygon.findPolygon(rgbaMat);
        polygon.drawPolygon(rgbaMat);

        ImageManager.INSTANCE.setMat(inMat);
        ImageManager.INSTANCE.setMat(rgbaMat);
        return rgbaMat;
    }
}