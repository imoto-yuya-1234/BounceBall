package yuya.bounceball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {

    OpenCvCamera cam;
    AnimationManager anime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 画面にカメラ画像を描画
        cam = new OpenCvCamera(this);
        cam.showCameraView(this);
        anime = new AnimationManager(this);
        anime.showAnimationView(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ImageManager.INSTANCE.saveImage();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        cam.deleteCameraBridgeViewBase();
    }

    @Override
    public void onResume() {
        super.onResume();
        cam.loadOpenCvLibrary();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cam.deleteCameraBridgeViewBase();
        anime.deleteHandler();
    }
}
