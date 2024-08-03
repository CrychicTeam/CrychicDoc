package icyllis.modernui.graphics.drawable;

import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.BlendMode;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.resources.Resources;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.view.Gravity;
import java.util.Objects;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class LayerDrawable extends Drawable implements Drawable.Callback {

    public static final Marker MARKER = MarkerManager.getMarker("LayerDrawable");

    public static final int PADDING_MODE_NEST = 0;

    public static final int PADDING_MODE_STACK = 1;

    public static final int INSET_UNDEFINED = Integer.MIN_VALUE;

    @NonNull
    LayerDrawable.LayerState mLayerState;

    private int[] mPaddingL;

    private int[] mPaddingT;

    private int[] mPaddingR;

    private int[] mPaddingB;

    private final Rect mTmpRect = new Rect();

    private final Rect mTmpOutRect = new Rect();

    private final Rect mTmpContainer = new Rect();

    private Rect mHotspotBounds;

    private boolean mMutated;

    private boolean mSuspendChildInvalidation;

    private boolean mChildRequestedInvalidation;

    public LayerDrawable(@NonNull Drawable... layers) {
        this(layers, null);
    }

    LayerDrawable(@NonNull Drawable[] layers, @Nullable LayerDrawable.LayerState state) {
        this(state, null);
        Objects.requireNonNull(layers, "layers must be non-null");
        int length = layers.length;
        LayerDrawable.ChildDrawable[] r = new LayerDrawable.ChildDrawable[length];
        for (int i = 0; i < length; i++) {
            r[i] = new LayerDrawable.ChildDrawable(this.mLayerState.mDensity);
            Drawable child = layers[i];
            r[i].mDrawable = child;
            if (child != null) {
                child.setCallback(this);
                this.mLayerState.mChildrenChangingConfigurations = this.mLayerState.mChildrenChangingConfigurations | child.getChangingConfigurations();
            }
        }
        this.mLayerState.mNumChildren = length;
        this.mLayerState.mChildren = r;
        this.ensurePadding();
        this.refreshPadding();
    }

    LayerDrawable() {
        this((LayerDrawable.LayerState) null, null);
    }

    LayerDrawable(@Nullable LayerDrawable.LayerState state, @Nullable Resources res) {
        this.mLayerState = this.createConstantState(state, res);
        if (this.mLayerState.mNumChildren > 0) {
            this.ensurePadding();
            this.refreshPadding();
        }
    }

    LayerDrawable.LayerState createConstantState(@Nullable LayerDrawable.LayerState state, @Nullable Resources res) {
        return new LayerDrawable.LayerState(state, this, res);
    }

    @Override
    public boolean canApplyTheme() {
        return this.mLayerState.canApplyTheme() || super.canApplyTheme();
    }

    int addLayer(@NonNull LayerDrawable.ChildDrawable layer) {
        LayerDrawable.LayerState st = this.mLayerState;
        int N = st.mChildren != null ? st.mChildren.length : 0;
        int i = st.mNumChildren;
        if (i >= N) {
            LayerDrawable.ChildDrawable[] nu = new LayerDrawable.ChildDrawable[N + 10];
            if (i > 0) {
                assert st.mChildren != null;
                System.arraycopy(st.mChildren, 0, nu, 0, i);
            }
            st.mChildren = nu;
        }
        assert st.mChildren != null;
        st.mChildren[i] = layer;
        st.mNumChildren++;
        st.invalidateCache();
        return i;
    }

    LayerDrawable.ChildDrawable addLayer(Drawable dr, int[] themeAttrs, int id, int left, int top, int right, int bottom) {
        LayerDrawable.ChildDrawable childDrawable = this.createLayer(dr);
        childDrawable.mId = id;
        childDrawable.mThemeAttrs = themeAttrs;
        childDrawable.mDrawable.setAutoMirrored(this.isAutoMirrored());
        childDrawable.mInsetL = left;
        childDrawable.mInsetT = top;
        childDrawable.mInsetR = right;
        childDrawable.mInsetB = bottom;
        this.addLayer(childDrawable);
        this.mLayerState.mChildrenChangingConfigurations = this.mLayerState.mChildrenChangingConfigurations | dr.getChangingConfigurations();
        dr.setCallback(this);
        return childDrawable;
    }

    private LayerDrawable.ChildDrawable createLayer(Drawable dr) {
        LayerDrawable.ChildDrawable layer = new LayerDrawable.ChildDrawable(this.mLayerState.mDensity);
        layer.mDrawable = dr;
        return layer;
    }

    public int addLayer(Drawable dr) {
        LayerDrawable.ChildDrawable layer = this.createLayer(dr);
        int index = this.addLayer(layer);
        this.ensurePadding();
        this.refreshChildPadding(index, layer);
        return index;
    }

    public Drawable findDrawableByLayerId(int id) {
        LayerDrawable.ChildDrawable[] layers = this.mLayerState.mChildren;
        for (int i = this.mLayerState.mNumChildren - 1; i >= 0; i--) {
            if (layers[i].mId == id) {
                return layers[i].mDrawable;
            }
        }
        return null;
    }

    public void setId(int index, int id) {
        this.mLayerState.mChildren[index].mId = id;
    }

    public int getId(int index) {
        if (index >= this.mLayerState.mNumChildren) {
            throw new IndexOutOfBoundsException();
        } else {
            return this.mLayerState.mChildren[index].mId;
        }
    }

    public int getNumberOfLayers() {
        return this.mLayerState.mNumChildren;
    }

    public boolean setDrawableByLayerId(int id, Drawable drawable) {
        int index = this.findIndexByLayerId(id);
        if (index < 0) {
            return false;
        } else {
            this.setDrawable(index, drawable);
            return true;
        }
    }

    public int findIndexByLayerId(int id) {
        LayerDrawable.ChildDrawable[] layers = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            LayerDrawable.ChildDrawable childDrawable = layers[i];
            if (childDrawable.mId == id) {
                return i;
            }
        }
        return -1;
    }

    public void setDrawable(int index, Drawable drawable) {
        if (index >= this.mLayerState.mNumChildren) {
            throw new IndexOutOfBoundsException();
        } else {
            LayerDrawable.ChildDrawable[] layers = this.mLayerState.mChildren;
            LayerDrawable.ChildDrawable childDrawable = layers[index];
            if (childDrawable.mDrawable != null) {
                if (drawable != null) {
                    Rect bounds = childDrawable.mDrawable.getBounds();
                    drawable.setBounds(bounds);
                }
                childDrawable.mDrawable.setCallback(null);
            }
            if (drawable != null) {
                drawable.setCallback(this);
            }
            childDrawable.mDrawable = drawable;
            this.mLayerState.invalidateCache();
            this.refreshChildPadding(index, childDrawable);
        }
    }

    public Drawable getDrawable(int index) {
        if (index >= this.mLayerState.mNumChildren) {
            throw new IndexOutOfBoundsException();
        } else {
            return this.mLayerState.mChildren[index].mDrawable;
        }
    }

    public void setLayerSize(int index, int w, int h) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        childDrawable.mWidth = w;
        childDrawable.mHeight = h;
    }

    public void setLayerWidth(int index, int w) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        childDrawable.mWidth = w;
    }

    public int getLayerWidth(int index) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        return childDrawable.mWidth;
    }

    public void setLayerHeight(int index, int h) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        childDrawable.mHeight = h;
    }

    public int getLayerHeight(int index) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        return childDrawable.mHeight;
    }

    public void setLayerGravity(int index, int gravity) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        childDrawable.mGravity = gravity;
    }

    public int getLayerGravity(int index) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        return childDrawable.mGravity;
    }

    public void setLayerInset(int index, int l, int t, int r, int b) {
        this.setLayerInsetInternal(index, l, t, r, b, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public void setLayerInsetRelative(int index, int s, int t, int e, int b) {
        this.setLayerInsetInternal(index, 0, t, 0, b, s, e);
    }

    public void setLayerInsetLeft(int index, int l) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        childDrawable.mInsetL = l;
    }

    public int getLayerInsetLeft(int index) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        return childDrawable.mInsetL;
    }

    public void setLayerInsetRight(int index, int r) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        childDrawable.mInsetR = r;
    }

    public int getLayerInsetRight(int index) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        return childDrawable.mInsetR;
    }

    public void setLayerInsetTop(int index, int t) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        childDrawable.mInsetT = t;
    }

    public int getLayerInsetTop(int index) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        return childDrawable.mInsetT;
    }

    public void setLayerInsetBottom(int index, int b) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        childDrawable.mInsetB = b;
    }

    public int getLayerInsetBottom(int index) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        return childDrawable.mInsetB;
    }

    public void setLayerInsetStart(int index, int s) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        childDrawable.mInsetS = s;
    }

    public int getLayerInsetStart(int index) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        return childDrawable.mInsetS;
    }

    public void setLayerInsetEnd(int index, int e) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        childDrawable.mInsetE = e;
    }

    public int getLayerInsetEnd(int index) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        return childDrawable.mInsetE;
    }

    private void setLayerInsetInternal(int index, int l, int t, int r, int b, int s, int e) {
        LayerDrawable.ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        childDrawable.mInsetL = l;
        childDrawable.mInsetT = t;
        childDrawable.mInsetR = r;
        childDrawable.mInsetB = b;
        childDrawable.mInsetS = s;
        childDrawable.mInsetE = e;
    }

    public void setPaddingMode(int mode) {
        if (this.mLayerState.mPaddingMode != mode) {
            this.mLayerState.mPaddingMode = mode;
        }
    }

    public int getPaddingMode() {
        return this.mLayerState.mPaddingMode;
    }

    private void suspendChildInvalidation() {
        this.mSuspendChildInvalidation = true;
    }

    private void resumeChildInvalidation() {
        this.mSuspendChildInvalidation = false;
        if (this.mChildRequestedInvalidation) {
            this.mChildRequestedInvalidation = false;
            this.invalidateSelf();
        }
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
        if (this.mSuspendChildInvalidation) {
            this.mChildRequestedInvalidation = true;
        } else {
            this.mLayerState.invalidateCache();
            this.invalidateSelf();
        }
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
        this.scheduleSelf(what, when);
    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
        this.unscheduleSelf(what);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.draw(canvas);
            }
        }
    }

    @Override
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mLayerState.getChangingConfigurations();
    }

    @Override
    public boolean getPadding(@NonNull Rect padding) {
        LayerDrawable.LayerState layerState = this.mLayerState;
        if (layerState.mPaddingMode == 0) {
            this.computeNestedPadding(padding);
        } else {
            this.computeStackedPadding(padding);
        }
        int paddingT = layerState.mPaddingTop;
        int paddingB = layerState.mPaddingBottom;
        boolean isLayoutRtl = this.getLayoutDirection() == 1;
        int paddingRtlL = isLayoutRtl ? layerState.mPaddingEnd : layerState.mPaddingStart;
        int paddingRtlR = isLayoutRtl ? layerState.mPaddingStart : layerState.mPaddingEnd;
        int paddingL = paddingRtlL >= 0 ? paddingRtlL : layerState.mPaddingLeft;
        int paddingR = paddingRtlR >= 0 ? paddingRtlR : layerState.mPaddingRight;
        if (paddingL >= 0) {
            padding.left = paddingL;
        }
        if (paddingT >= 0) {
            padding.top = paddingT;
        }
        if (paddingR >= 0) {
            padding.right = paddingR;
        }
        if (paddingB >= 0) {
            padding.bottom = paddingB;
        }
        return padding.left != 0 || padding.top != 0 || padding.right != 0 || padding.bottom != 0;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        LayerDrawable.LayerState layerState = this.mLayerState;
        layerState.mPaddingLeft = left;
        layerState.mPaddingTop = top;
        layerState.mPaddingRight = right;
        layerState.mPaddingBottom = bottom;
        layerState.mPaddingStart = -1;
        layerState.mPaddingEnd = -1;
    }

    public void setPaddingRelative(int start, int top, int end, int bottom) {
        LayerDrawable.LayerState layerState = this.mLayerState;
        layerState.mPaddingStart = start;
        layerState.mPaddingTop = top;
        layerState.mPaddingEnd = end;
        layerState.mPaddingBottom = bottom;
        layerState.mPaddingLeft = -1;
        layerState.mPaddingRight = -1;
    }

    public int getLeftPadding() {
        return this.mLayerState.mPaddingLeft;
    }

    public int getRightPadding() {
        return this.mLayerState.mPaddingRight;
    }

    public int getStartPadding() {
        return this.mLayerState.mPaddingStart;
    }

    public int getEndPadding() {
        return this.mLayerState.mPaddingEnd;
    }

    public int getTopPadding() {
        return this.mLayerState.mPaddingTop;
    }

    public int getBottomPadding() {
        return this.mLayerState.mPaddingBottom;
    }

    private void computeNestedPadding(Rect padding) {
        padding.left = 0;
        padding.top = 0;
        padding.right = 0;
        padding.bottom = 0;
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            this.refreshChildPadding(i, array[i]);
            padding.left = padding.left + this.mPaddingL[i];
            padding.top = padding.top + this.mPaddingT[i];
            padding.right = padding.right + this.mPaddingR[i];
            padding.bottom = padding.bottom + this.mPaddingB[i];
        }
    }

    private void computeStackedPadding(Rect padding) {
        padding.left = 0;
        padding.top = 0;
        padding.right = 0;
        padding.bottom = 0;
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            this.refreshChildPadding(i, array[i]);
            padding.left = Math.max(padding.left, this.mPaddingL[i]);
            padding.top = Math.max(padding.top, this.mPaddingT[i]);
            padding.right = Math.max(padding.right, this.mPaddingR[i]);
            padding.bottom = Math.max(padding.bottom, this.mPaddingB[i]);
        }
    }

    @Override
    public void setHotspot(float x, float y) {
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setHotspot(x, y);
            }
        }
    }

    @Override
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setHotspotBounds(left, top, right, bottom);
            }
        }
        if (this.mHotspotBounds == null) {
            this.mHotspotBounds = new Rect(left, top, right, bottom);
        } else {
            this.mHotspotBounds.set(left, top, right, bottom);
        }
    }

    @Override
    public void getHotspotBounds(@NonNull Rect outRect) {
        if (this.mHotspotBounds != null) {
            outRect.set(this.mHotspotBounds);
        } else {
            super.getHotspotBounds(outRect);
        }
    }

    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = super.setVisible(visible, restart);
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setVisible(visible, restart);
            }
        }
        return changed;
    }

    @Override
    public void setAlpha(int alpha) {
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setAlpha(alpha);
            }
        }
    }

    @Override
    public int getAlpha() {
        Drawable dr = this.getFirstNonNullDrawable();
        return dr != null ? dr.getAlpha() : super.getAlpha();
    }

    @Override
    public void setTintList(ColorStateList tint) {
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setTintList(tint);
            }
        }
    }

    @Override
    public void setTintBlendMode(@NonNull BlendMode blendMode) {
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setTintBlendMode(blendMode);
            }
        }
    }

    private Drawable getFirstNonNullDrawable() {
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                return dr;
            }
        }
        return null;
    }

    @Override
    public void setAutoMirrored(boolean mirrored) {
        this.mLayerState.mAutoMirrored = mirrored;
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setAutoMirrored(mirrored);
            }
        }
    }

    @Override
    public boolean isAutoMirrored() {
        return this.mLayerState.mAutoMirrored;
    }

    @Override
    public void jumpToCurrentState() {
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.jumpToCurrentState();
            }
        }
    }

    @Override
    public boolean isStateful() {
        return this.mLayerState.isStateful();
    }

    @Override
    public boolean hasFocusStateSpecified() {
        return this.mLayerState.hasFocusStateSpecified();
    }

    @Override
    protected boolean onStateChange(@NonNull int[] state) {
        boolean changed = false;
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null && dr.isStateful() && dr.setState(state)) {
                this.refreshChildPadding(i, array[i]);
                changed = true;
            }
        }
        if (changed) {
            this.updateLayerBounds(this.getBounds());
        }
        return changed;
    }

    @Override
    protected boolean onLevelChange(int level) {
        boolean changed = false;
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null && dr.setLevel(level)) {
                this.refreshChildPadding(i, array[i]);
                changed = true;
            }
        }
        if (changed) {
            this.updateLayerBounds(this.getBounds());
        }
        return changed;
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        this.updateLayerBounds(bounds);
    }

    private void updateLayerBounds(Rect bounds) {
        try {
            this.suspendChildInvalidation();
            this.updateLayerBoundsInternal(bounds);
        } finally {
            this.resumeChildInvalidation();
        }
    }

    private void updateLayerBoundsInternal(Rect bounds) {
        int paddingL = 0;
        int paddingT = 0;
        int paddingR = 0;
        int paddingB = 0;
        Rect outRect = this.mTmpOutRect;
        int layoutDirection = this.getLayoutDirection();
        boolean isLayoutRtl = layoutDirection == 1;
        boolean isPaddingNested = this.mLayerState.mPaddingMode == 0;
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int i = 0;
        for (int count = this.mLayerState.mNumChildren; i < count; i++) {
            LayerDrawable.ChildDrawable r = array[i];
            Drawable d = r.mDrawable;
            if (d != null) {
                int insetT = r.mInsetT;
                int insetB = r.mInsetB;
                int insetRtlL = isLayoutRtl ? r.mInsetE : r.mInsetS;
                int insetRtlR = isLayoutRtl ? r.mInsetS : r.mInsetE;
                int insetL = insetRtlL == Integer.MIN_VALUE ? r.mInsetL : insetRtlL;
                int insetR = insetRtlR == Integer.MIN_VALUE ? r.mInsetR : insetRtlR;
                Rect container = this.mTmpContainer;
                container.set(bounds.left + insetL + paddingL, bounds.top + insetT + paddingT, bounds.right - insetR - paddingR, bounds.bottom - insetB - paddingB);
                int intrinsicW = d.getIntrinsicWidth();
                int intrinsicH = d.getIntrinsicHeight();
                int layerW = r.mWidth;
                int layerH = r.mHeight;
                int gravity = resolveGravity(r.mGravity, layerW, layerH, intrinsicW, intrinsicH);
                int resolvedW = layerW < 0 ? intrinsicW : layerW;
                int resolvedH = layerH < 0 ? intrinsicH : layerH;
                Gravity.apply(gravity, resolvedW, resolvedH, container, outRect, layoutDirection);
                d.setBounds(outRect);
                if (isPaddingNested) {
                    paddingL += this.mPaddingL[i];
                    paddingR += this.mPaddingR[i];
                    paddingT += this.mPaddingT[i];
                    paddingB += this.mPaddingB[i];
                }
            }
        }
    }

    private static int resolveGravity(int gravity, int width, int height, int intrinsicWidth, int intrinsicHeight) {
        if (!Gravity.isHorizontal(gravity)) {
            if (width < 0) {
                gravity |= 7;
            } else {
                gravity |= 8388611;
            }
        }
        if (!Gravity.isVertical(gravity)) {
            if (height < 0) {
                gravity |= 112;
            } else {
                gravity |= 48;
            }
        }
        if (width < 0 && intrinsicWidth < 0) {
            gravity |= 7;
        }
        if (height < 0 && intrinsicHeight < 0) {
            gravity |= 112;
        }
        return gravity;
    }

    @Override
    public int getIntrinsicWidth() {
        int width = -1;
        int padL = 0;
        int padR = 0;
        boolean nest = this.mLayerState.mPaddingMode == 0;
        boolean isLayoutRtl = this.getLayoutDirection() == 1;
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            LayerDrawable.ChildDrawable r = array[i];
            if (r.mDrawable != null) {
                int insetRtlL = isLayoutRtl ? r.mInsetE : r.mInsetS;
                int insetRtlR = isLayoutRtl ? r.mInsetS : r.mInsetE;
                int insetL = insetRtlL == Integer.MIN_VALUE ? r.mInsetL : insetRtlL;
                int insetR = insetRtlR == Integer.MIN_VALUE ? r.mInsetR : insetRtlR;
                int minWidth = r.mWidth < 0 ? r.mDrawable.getIntrinsicWidth() : r.mWidth;
                int w = minWidth < 0 ? -1 : minWidth + insetL + insetR + padL + padR;
                if (w > width) {
                    width = w;
                }
                if (nest) {
                    padL += this.mPaddingL[i];
                    padR += this.mPaddingR[i];
                }
            }
        }
        return width;
    }

    @Override
    public int getIntrinsicHeight() {
        int height = -1;
        int padT = 0;
        int padB = 0;
        boolean nest = this.mLayerState.mPaddingMode == 0;
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            LayerDrawable.ChildDrawable r = array[i];
            if (r.mDrawable != null) {
                int minHeight = r.mHeight < 0 ? r.mDrawable.getIntrinsicHeight() : r.mHeight;
                int h = minHeight < 0 ? -1 : minHeight + r.mInsetT + r.mInsetB + padT + padB;
                if (h > height) {
                    height = h;
                }
                if (nest) {
                    padT += this.mPaddingT[i];
                    padB += this.mPaddingB[i];
                }
            }
        }
        return height;
    }

    private boolean refreshChildPadding(int i, LayerDrawable.ChildDrawable r) {
        if (r.mDrawable != null) {
            Rect rect = this.mTmpRect;
            r.mDrawable.getPadding(rect);
            if (rect.left != this.mPaddingL[i] || rect.top != this.mPaddingT[i] || rect.right != this.mPaddingR[i] || rect.bottom != this.mPaddingB[i]) {
                this.mPaddingL[i] = rect.left;
                this.mPaddingT[i] = rect.top;
                this.mPaddingR[i] = rect.right;
                this.mPaddingB[i] = rect.bottom;
                return true;
            }
        }
        return false;
    }

    void ensurePadding() {
        int N = this.mLayerState.mNumChildren;
        if (this.mPaddingL == null || this.mPaddingL.length < N) {
            this.mPaddingL = new int[N];
            this.mPaddingT = new int[N];
            this.mPaddingR = new int[N];
            this.mPaddingB = new int[N];
        }
    }

    void refreshPadding() {
        int N = this.mLayerState.mNumChildren;
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        for (int i = 0; i < N; i++) {
            this.refreshChildPadding(i, array[i]);
        }
    }

    @Override
    public Drawable.ConstantState getConstantState() {
        if (this.mLayerState.canConstantState()) {
            this.mLayerState.mChangingConfigurations = this.getChangingConfigurations();
            return this.mLayerState;
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mLayerState = this.createConstantState(this.mLayerState, null);
            LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
            int N = this.mLayerState.mNumChildren;
            for (int i = 0; i < N; i++) {
                Drawable dr = array[i].mDrawable;
                if (dr != null) {
                    dr.mutate();
                }
            }
            this.mMutated = true;
        }
        return this;
    }

    @Override
    public void clearMutated() {
        super.clearMutated();
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.clearMutated();
            }
        }
        this.mMutated = false;
    }

    @Override
    public boolean onLayoutDirectionChanged(int layoutDirection) {
        boolean changed = false;
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNumChildren;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                changed |= dr.setLayoutDirection(layoutDirection);
            }
        }
        this.updateLayerBounds(this.getBounds());
        return changed;
    }

    static class ChildDrawable {

        public Drawable mDrawable;

        public int[] mThemeAttrs;

        public int mDensity = 72;

        public int mInsetL;

        public int mInsetT;

        public int mInsetR;

        public int mInsetB;

        public int mInsetS = Integer.MIN_VALUE;

        public int mInsetE = Integer.MIN_VALUE;

        public int mWidth = -1;

        public int mHeight = -1;

        public int mGravity = 0;

        public int mId = -1;

        ChildDrawable(int density) {
            this.mDensity = density;
        }

        ChildDrawable(@NonNull LayerDrawable.ChildDrawable orig, @NonNull LayerDrawable owner, @Nullable Resources res) {
            Drawable dr = orig.mDrawable;
            Drawable clone;
            if (dr != null) {
                Drawable.ConstantState cs = dr.getConstantState();
                if (cs == null) {
                    clone = dr;
                    if (dr.getCallback() != null) {
                        ModernUI.LOGGER.warn(LayerDrawable.MARKER, "Invalid drawable added to LayerDrawable! Drawable already belongs to another owner but does not expose a constant state.", new RuntimeException());
                    }
                } else if (res != null) {
                    clone = cs.newDrawable(res);
                } else {
                    clone = cs.newDrawable();
                }
                clone.setLayoutDirection(dr.getLayoutDirection());
                clone.setBounds(dr.getBounds());
                clone.setLevel(dr.getLevel());
                clone.setCallback(owner);
            } else {
                clone = null;
            }
            this.mDrawable = clone;
            this.mThemeAttrs = orig.mThemeAttrs;
            this.mInsetL = orig.mInsetL;
            this.mInsetT = orig.mInsetT;
            this.mInsetR = orig.mInsetR;
            this.mInsetB = orig.mInsetB;
            this.mInsetS = orig.mInsetS;
            this.mInsetE = orig.mInsetE;
            this.mWidth = orig.mWidth;
            this.mHeight = orig.mHeight;
            this.mGravity = orig.mGravity;
            this.mId = orig.mId;
            this.mDensity = Drawable.resolveDensity(res, orig.mDensity);
            if (orig.mDensity != this.mDensity) {
                this.applyDensityScaling(orig.mDensity, this.mDensity);
            }
        }

        public boolean canApplyTheme() {
            return this.mThemeAttrs != null || this.mDrawable != null && this.mDrawable.canApplyTheme();
        }

        public final void setDensity(int targetDensity) {
            if (this.mDensity != targetDensity) {
                int sourceDensity = this.mDensity;
                this.mDensity = targetDensity;
                this.applyDensityScaling(sourceDensity, targetDensity);
            }
        }

        private void applyDensityScaling(int sourceDensity, int targetDensity) {
            this.mInsetL = Drawable.scaleFromDensity(this.mInsetL, sourceDensity, targetDensity, false);
            this.mInsetT = Drawable.scaleFromDensity(this.mInsetT, sourceDensity, targetDensity, false);
            this.mInsetR = Drawable.scaleFromDensity(this.mInsetR, sourceDensity, targetDensity, false);
            this.mInsetB = Drawable.scaleFromDensity(this.mInsetB, sourceDensity, targetDensity, false);
            if (this.mInsetS != Integer.MIN_VALUE) {
                this.mInsetS = Drawable.scaleFromDensity(this.mInsetS, sourceDensity, targetDensity, false);
            }
            if (this.mInsetE != Integer.MIN_VALUE) {
                this.mInsetE = Drawable.scaleFromDensity(this.mInsetE, sourceDensity, targetDensity, false);
            }
            if (this.mWidth > 0) {
                this.mWidth = Drawable.scaleFromDensity(this.mWidth, sourceDensity, targetDensity, true);
            }
            if (this.mHeight > 0) {
                this.mHeight = Drawable.scaleFromDensity(this.mHeight, sourceDensity, targetDensity, true);
            }
        }
    }

    static class LayerState extends Drawable.ConstantState {

        private int[] mThemeAttrs;

        int mNumChildren;

        LayerDrawable.ChildDrawable[] mChildren;

        int mDensity;

        int mPaddingTop = -1;

        int mPaddingBottom = -1;

        int mPaddingLeft = -1;

        int mPaddingRight = -1;

        int mPaddingStart = -1;

        int mPaddingEnd = -1;

        int mChangingConfigurations;

        int mChildrenChangingConfigurations;

        private boolean mCheckedOpacity;

        private int mOpacity;

        private boolean mCheckedStateful;

        private boolean mIsStateful;

        private boolean mAutoMirrored = false;

        private int mPaddingMode = 0;

        LayerState(@Nullable LayerDrawable.LayerState orig, @NonNull LayerDrawable owner, @Nullable Resources res) {
            this.mDensity = Drawable.resolveDensity(res, orig != null ? orig.mDensity : 0);
            if (orig != null) {
                LayerDrawable.ChildDrawable[] origChildDrawable = orig.mChildren;
                int N = orig.mNumChildren;
                this.mNumChildren = N;
                this.mChildren = new LayerDrawable.ChildDrawable[N];
                this.mChangingConfigurations = orig.mChangingConfigurations;
                this.mChildrenChangingConfigurations = orig.mChildrenChangingConfigurations;
                for (int i = 0; i < N; i++) {
                    LayerDrawable.ChildDrawable or = origChildDrawable[i];
                    this.mChildren[i] = new LayerDrawable.ChildDrawable(or, owner, res);
                }
                this.mCheckedOpacity = orig.mCheckedOpacity;
                this.mOpacity = orig.mOpacity;
                this.mCheckedStateful = orig.mCheckedStateful;
                this.mIsStateful = orig.mIsStateful;
                this.mAutoMirrored = orig.mAutoMirrored;
                this.mPaddingMode = orig.mPaddingMode;
                this.mThemeAttrs = orig.mThemeAttrs;
                this.mPaddingTop = orig.mPaddingTop;
                this.mPaddingBottom = orig.mPaddingBottom;
                this.mPaddingLeft = orig.mPaddingLeft;
                this.mPaddingRight = orig.mPaddingRight;
                this.mPaddingStart = orig.mPaddingStart;
                this.mPaddingEnd = orig.mPaddingEnd;
                if (orig.mDensity != this.mDensity) {
                    this.applyDensityScaling(orig.mDensity, this.mDensity);
                }
            } else {
                this.mNumChildren = 0;
                this.mChildren = null;
            }
        }

        public final void setDensity(int targetDensity) {
            if (this.mDensity != targetDensity) {
                int sourceDensity = this.mDensity;
                this.mDensity = targetDensity;
                this.onDensityChanged(sourceDensity, targetDensity);
            }
        }

        protected void onDensityChanged(int sourceDensity, int targetDensity) {
            this.applyDensityScaling(sourceDensity, targetDensity);
        }

        private void applyDensityScaling(int sourceDensity, int targetDensity) {
            if (this.mPaddingLeft > 0) {
                this.mPaddingLeft = Drawable.scaleFromDensity(this.mPaddingLeft, sourceDensity, targetDensity, false);
            }
            if (this.mPaddingTop > 0) {
                this.mPaddingTop = Drawable.scaleFromDensity(this.mPaddingTop, sourceDensity, targetDensity, false);
            }
            if (this.mPaddingRight > 0) {
                this.mPaddingRight = Drawable.scaleFromDensity(this.mPaddingRight, sourceDensity, targetDensity, false);
            }
            if (this.mPaddingBottom > 0) {
                this.mPaddingBottom = Drawable.scaleFromDensity(this.mPaddingBottom, sourceDensity, targetDensity, false);
            }
            if (this.mPaddingStart > 0) {
                this.mPaddingStart = Drawable.scaleFromDensity(this.mPaddingStart, sourceDensity, targetDensity, false);
            }
            if (this.mPaddingEnd > 0) {
                this.mPaddingEnd = Drawable.scaleFromDensity(this.mPaddingEnd, sourceDensity, targetDensity, false);
            }
        }

        @Override
        public boolean canApplyTheme() {
            if (this.mThemeAttrs == null && !super.canApplyTheme()) {
                LayerDrawable.ChildDrawable[] array = this.mChildren;
                int N = this.mNumChildren;
                for (int i = 0; i < N; i++) {
                    LayerDrawable.ChildDrawable layer = array[i];
                    if (layer.canApplyTheme()) {
                        return true;
                    }
                }
                return false;
            } else {
                return true;
            }
        }

        @NonNull
        @Override
        public Drawable newDrawable() {
            return new LayerDrawable(this, null);
        }

        @NonNull
        @Override
        public Drawable newDrawable(@Nullable Resources res) {
            return new LayerDrawable(this, res);
        }

        @Override
        public int getChangingConfigurations() {
            return this.mChangingConfigurations | this.mChildrenChangingConfigurations;
        }

        public final boolean isStateful() {
            if (this.mCheckedStateful) {
                return this.mIsStateful;
            } else {
                int N = this.mNumChildren;
                LayerDrawable.ChildDrawable[] array = this.mChildren;
                boolean isStateful = false;
                for (int i = 0; i < N; i++) {
                    Drawable dr = array[i].mDrawable;
                    if (dr != null && dr.isStateful()) {
                        isStateful = true;
                        break;
                    }
                }
                this.mIsStateful = isStateful;
                this.mCheckedStateful = true;
                return isStateful;
            }
        }

        public final boolean hasFocusStateSpecified() {
            int N = this.mNumChildren;
            LayerDrawable.ChildDrawable[] array = this.mChildren;
            for (int i = 0; i < N; i++) {
                Drawable dr = array[i].mDrawable;
                if (dr != null && dr.hasFocusStateSpecified()) {
                    return true;
                }
            }
            return false;
        }

        public final boolean canConstantState() {
            LayerDrawable.ChildDrawable[] array = this.mChildren;
            int N = this.mNumChildren;
            for (int i = 0; i < N; i++) {
                Drawable dr = array[i].mDrawable;
                if (dr != null && dr.getConstantState() == null) {
                    return false;
                }
            }
            return true;
        }

        void invalidateCache() {
            this.mCheckedOpacity = false;
            this.mCheckedStateful = false;
        }
    }
}