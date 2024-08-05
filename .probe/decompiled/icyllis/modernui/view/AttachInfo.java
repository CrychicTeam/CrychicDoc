package icyllis.modernui.view;

import icyllis.modernui.core.Handler;
import icyllis.modernui.graphics.Matrix;
import icyllis.modernui.graphics.Point;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.RectF;

final class AttachInfo {

    final AttachInfo.Callbacks mRootCallbacks;

    View mRootView;

    final KeyEvent.DispatcherState mKeyDispatchState = new KeyEvent.DispatcherState();

    boolean mHasWindowFocus = true;

    int mWindowVisibility;

    boolean mInTouchMode = true;

    boolean mViewVisibilityChanged;

    boolean mViewScrollChanged;

    final ViewTreeObserver mTreeObserver;

    final ViewRoot mViewRoot;

    final Handler mHandler;

    final float[] mTmpTransformLocation = new float[2];

    final Rect mTmpInvalRect = new Rect();

    final RectF mTmpTransformRect = new RectF();

    final Matrix mTmpMatrix = new Matrix();

    boolean mDebugLayout = false;

    final Point mPoint = new Point();

    View mViewRequestingLayout;

    View mTooltipHost;

    AttachInfo(ViewRoot viewRoot, Handler handler, AttachInfo.Callbacks callbacks) {
        this.mViewRoot = viewRoot;
        this.mHandler = handler;
        this.mRootCallbacks = callbacks;
        this.mTreeObserver = new ViewTreeObserver();
    }

    interface Callbacks {

        void playSoundEffect(int var1);

        boolean performHapticFeedback(int var1, boolean var2);
    }
}