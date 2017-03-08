package yuya.bounceball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {

    OpenCvCameraView Cam = new OpenCvCameraView(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 画面にカメラ画像を描画
        Cam.showCameraView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Cam.saveBitmap();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        Cam.deleteCameraBridgeViewBase();
    }

    @Override
    public void onResume() {
        super.onResume();
        Cam.loadOpenCvLibrary();
    }

    public void onDestroy() {
        super.onDestroy();
        Cam.deleteCameraBridgeViewBase();
    }
}
