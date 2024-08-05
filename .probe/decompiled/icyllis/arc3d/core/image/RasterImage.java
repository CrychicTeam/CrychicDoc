package icyllis.arc3d.core.image;

import icyllis.arc3d.core.Image;
import icyllis.arc3d.core.PixelMap;
import icyllis.arc3d.core.PixelRef;
import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.engine.RecordingContext;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RasterImage extends Image {

    final PixelMap mPixelMap;

    PixelRef mPixelRef;

    public RasterImage(@Nonnull PixelMap pixelMap, @Nonnull PixelRef pixelRef) {
        super(pixelMap.getInfo());
        this.mPixelMap = pixelMap;
        this.mPixelRef = RefCnt.create(pixelRef);
    }

    @Override
    protected void deallocate() {
        this.mPixelRef = RefCnt.move(this.mPixelRef);
    }

    @Override
    public boolean isValid(@Nullable RecordingContext context) {
        return true;
    }

    @Override
    public boolean isRasterBacked() {
        return true;
    }
}