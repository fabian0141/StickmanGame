package schroeti.fabian.stickman;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class JoyStick extends SurfaceView {

    private float sx,sy;
    public float rot = 0;

    public JoyStick(Context context, AttributeSet attrs) {
        super(context, attrs);

        setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        sx = getMeasuredWidth();
        sy = getMeasuredHeight();
    }

    public boolean changed;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                x -= sx / 2;
                y -= sy / 2;

                float rot = (float)Math.toDegrees(Math.atan(x / y)) + 90;

                if(y > 0)
                    rot += 180;

                if(this.rot != rot)
                    changed = true;
                this.rot = rot;


                break;
        }

        return true;
    }
}
