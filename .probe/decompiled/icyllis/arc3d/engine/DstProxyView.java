package icyllis.arc3d.engine;

@Deprecated
public class DstProxyView {

    public static final int REQUIRES_TEXTURE_BARRIER_FLAG = 2;

    public static final int AS_INPUT_ATTACHMENT_FLAG = 4;

    SurfaceView mProxyView;

    int mOffsetX;

    int mOffsetY;

    int mFlags;
}