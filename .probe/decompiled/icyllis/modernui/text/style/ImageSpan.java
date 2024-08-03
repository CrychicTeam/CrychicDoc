package icyllis.modernui.text.style;

import icyllis.modernui.graphics.Image;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import javax.annotation.Nonnull;

public class ImageSpan extends DynamicDrawableSpan {

    @Nonnull
    private final Drawable mDrawable;

    public ImageSpan(@Nonnull Image image) {
        this(image, 0);
    }

    public ImageSpan(@Nonnull Image image, int verticalAlignment) {
        super(verticalAlignment);
        this.mDrawable = new ImageDrawable(image);
        int width = this.mDrawable.getIntrinsicWidth();
        int height = this.mDrawable.getIntrinsicHeight();
        this.mDrawable.setBounds(0, 0, Math.max(width, 0), Math.max(height, 0));
    }

    public ImageSpan(@Nonnull Drawable drawable) {
        this(drawable, 0);
    }

    public ImageSpan(@Nonnull Drawable drawable, int verticalAlignment) {
        super(verticalAlignment);
        this.mDrawable = drawable;
    }

    @Nonnull
    @Override
    public Drawable getDrawable() {
        return this.mDrawable;
    }
}