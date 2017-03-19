package yuya.bounceball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by yuya on 2017/03/10.
 */

public class Ball extends View {

    public int dx, dy, time, x, y, radius;
    private Paint paint;

    public Ball(Context context) {
        super(context);
        dx = dy = 10;
        time = 100;
        x = y = 0;
        radius = 30;
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawCircle(x, y, radius, paint);
    }

    public void moveBall(int startX, int startY, int endX, int endY) {
        x += dx;
        y -= dy;

        if (x <= startX + radius) {
            x = startX + radius;
            dx = -dx;
        } else if (x >= endX - radius) {
            x = endX - radius;
            dx = -dx;
        }

        if (y <= startY + radius) {
            y = startY + radius;
            dy = -dy;
        } else if (y >= endY - radius) {
            y = endY - radius;
            dy = -dy;
        }
    }
}
