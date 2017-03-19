package yuya.bounceball;

import android.content.Context;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by yuya on 2017/03/18.
 */

public class AnimationManager extends View implements Runnable {

    private int width, height;
    private Handler handler;
    private Ball ball;

    public AnimationManager(Context context) {
        super(context);
        // 画面サイズの取得
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        android.graphics.Point point = new android.graphics.Point();
        display.getSize(point);
        width = point.x; //画面の幅
        height = point.y; //画面の高さ

        ball = new Ball(context);
    }
    
    public void showAnimationView(MainActivity main) {
        handler = new Handler();
        handler.postDelayed(this, ball.time);
        main.addContentView(ball, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void run() {
        int offsetX = (width - OpenCvCamera.width) / 2;
        int offsetY = (height - OpenCvCamera.height) / 2;
        int endX = OpenCvCamera.width + offsetX;
        int endY = OpenCvCamera.height + offsetY;

        ball.moveBall(offsetX, offsetY, endX, endY);
        // ballの再描画
        ball.invalidate();
        handler.postDelayed(this, ball.time);
    }

    public void deleteHandler() {
        handler.removeCallbacks(this);
    }
}
