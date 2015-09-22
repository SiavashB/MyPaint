package paint.thirdeyeds.com.mypaint;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by siavash on 9/22/15.
 */
public class ColoredPath {
    public Path path;
    public Paint paint;

    public ColoredPath(Path path, Paint paint) {
        this.path = path;
        this.paint = paint;
    }
}
