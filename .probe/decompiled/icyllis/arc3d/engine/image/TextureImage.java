package icyllis.arc3d.engine.image;

import icyllis.arc3d.core.ColorSpace;
import icyllis.arc3d.core.Image;
import icyllis.arc3d.core.ImageInfo;
import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.RecordingContext;
import icyllis.arc3d.engine.TextureProxy;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TextureImage extends Image {

    RecordingContext mContext;

    @SharedPtr
    TextureProxy mProxy;

    short mSwizzle;

    int mOrigin;

    public TextureImage(@Nonnull RecordingContext rContext, @Nonnull TextureProxy proxy, short swizzle, int origin, int colorType, int alphaType, @Nullable ColorSpace colorSpace) {
        super(ImageInfo.make(proxy.getBackingWidth(), proxy.getBackingHeight(), colorType, alphaType, colorSpace));
        this.mContext = rContext;
        this.mProxy = RefCnt.create(proxy);
        this.mSwizzle = swizzle;
        this.mOrigin = origin;
    }

    @Override
    protected void deallocate() {
        this.mProxy = RefCnt.move(this.mProxy);
    }

    @Override
    public RecordingContext getContext() {
        return this.mContext;
    }

    @Override
    public boolean isValid(@Nullable RecordingContext context) {
        if (this.mContext.isDiscarded()) {
            return false;
        } else {
            return context == null ? true : !context.isDiscarded() && this.mContext.matches(context);
        }
    }

    @Override
    public boolean isTextureBacked() {
        return true;
    }

    @Override
    public long getTextureMemorySize() {
        return this.mProxy.getMemorySize();
    }
}