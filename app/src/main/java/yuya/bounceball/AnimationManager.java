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
        this.ball = new Ball(context);

        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        android.graphics.Point point = new android.graphics.Point();
        display.getSize(point);
        this.width = point.x; //画面の幅
        this.height = point.y; //画面の高さ

        this.ball.x = this.width / 2;
        this.ball.y = this.height / 2;
    }

    public void showAnimationView(MainActivity main) {
        this.handler = new Handler();
        this.handler.postDelayed(this, this.ball.time);
        main.addContentView(this.ball, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void run() {
        int offsetX = (this.width - OpenCvCamera.width) / 2;
        int offsetY = (this.height - OpenCvCamera.height) / 2;
        int endX = OpenCvCamera.width + offsetX;
        int endY = OpenCvCamera.height + offsetY;
        ball.moveBall(offsetX, offsetY, endX, endY);

        // ballの再描画
        this.ball.invalidate();
        handler.postDelayed(this, this.ball.time);
    }

    public void deleteHandler() {
        handler.removeCallbacks(this);
    }
}
