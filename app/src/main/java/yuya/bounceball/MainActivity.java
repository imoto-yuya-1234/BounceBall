package yuya.bounceball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    OpenCvCamera Cam = new OpenCvCamera(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_view);
        // 画面にカメラ画像を描画
        Cam.showCameraView(findViewById(R.id.camera_view));
    }

    @Override
    public void onPause() {
        super.onPause();
        Cam.deleteCameraBridgeViewBase();
    }

    @Override
    public void onResume() {
        super.onResume();
        Cam.onResumeProcess();
    }

    public void onDestroy() {
        super.onDestroy();
        Cam.deleteCameraBridgeViewBase();
    }
}
