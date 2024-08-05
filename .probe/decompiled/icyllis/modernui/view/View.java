package icyllis.modernui.view;

import icyllis.modernui.ModernUI;
import icyllis.modernui.animation.AnimationUtils;
import icyllis.modernui.animation.StateListAnimator;
import icyllis.modernui.annotation.CallSuper;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.annotation.UiThread;
import icyllis.modernui.core.Context;
import icyllis.modernui.core.Core;
import icyllis.modernui.core.Handler;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Matrix;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Point;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.RectF;
import icyllis.modernui.graphics.RenderNode;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.text.TextUtils;
import icyllis.modernui.util.FloatProperty;
import icyllis.modernui.util.IntProperty;
import icyllis.modernui.util.SparseArray;
import icyllis.modernui.util.StateSet;
import icyllis.modernui.view.menu.MenuBuilder;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus.Internal;

@UiThread
public class View implements Drawable.Callback {

    protected static final Marker VIEW_MARKER = MarkerManager.getMarker("View");

    public static final int NO_ID = -1;

    public static final int NOT_FOCUSABLE = 0;

    public static final int FOCUSABLE = 1;

    public static final int FOCUSABLE_AUTO = 16;

    private static final int FOCUSABLE_MASK = 17;

    public static final int VISIBLE = 0;

    public static final int INVISIBLE = 4;

    public static final int GONE = 8;

    static final int VISIBILITY_MASK = 12;

    static final int ENABLED = 0;

    static final int DISABLED = 32;

    static final int ENABLED_MASK = 32;

    static final int WILL_NOT_DRAW = 128;

    static final int DRAW_MASK = 128;

    static final int SCROLLBARS_NONE = 0;

    static final int SCROLLBARS_HORIZONTAL = 256;

    static final int SCROLLBARS_VERTICAL = 512;

    static final int SCROLLBARS_MASK = 768;

    static final int FADING_EDGE_NONE = 0;

    static final int FADING_EDGE_HORIZONTAL = 4096;

    static final int FADING_EDGE_VERTICAL = 8192;

    static final int FADING_EDGE_MASK = 12288;

    static final int CLICKABLE = 16384;

    static final int FOCUSABLE_IN_TOUCH_MODE = 262144;

    static final int LONG_CLICKABLE = 2097152;

    static final int DUPLICATE_PARENT_STATE = 4194304;

    static final int CONTEXT_CLICKABLE = 8388608;

    public static final int SCROLLBARS_INSIDE_OVERLAY = 0;

    public static final int SCROLLBARS_INSIDE_INSET = 16777216;

    public static final int SCROLLBARS_OUTSIDE_OVERLAY = 33554432;

    public static final int SCROLLBARS_OUTSIDE_INSET = 50331648;

    static final int SCROLLBARS_INSET_MASK = 16777216;

    static final int SCROLLBARS_OUTSIDE_MASK = 33554432;

    static final int SCROLLBARS_STYLE_MASK = 50331648;

    public static final int SOUND_EFFECTS_ENABLED = 134217728;

    public static final int HAPTIC_FEEDBACK_ENABLED = 268435456;

    static final int TOOLTIP = 1073741824;

    public static final int FOCUSABLES_ALL = 0;

    public static final int FOCUSABLES_TOUCH_MODE = 1;

    public static final int FOCUS_BACKWARD = 1;

    public static final int FOCUS_FORWARD = 2;

    public static final int FOCUS_LEFT = 17;

    public static final int FOCUS_UP = 33;

    public static final int FOCUS_RIGHT = 66;

    public static final int FOCUS_DOWN = 130;

    public static final int MEASURED_SIZE_MASK = 16777215;

    public static final int MEASURED_STATE_MASK = -16777216;

    public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;

    public static final int MEASURED_STATE_TOO_SMALL = 16777216;

    protected static final int[] EMPTY_STATE_SET = StateSet.WILD_CARD;

    protected static final int[] ENABLED_STATE_SET = StateSet.get(8);

    protected static final int[] FOCUSED_STATE_SET = StateSet.get(4);

    protected static final int[] SELECTED_STATE_SET = StateSet.get(2);

    protected static final int[] PRESSED_STATE_SET = StateSet.get(16);

    protected static final int[] WINDOW_FOCUSED_STATE_SET = StateSet.get(1);

    protected static final int[] ENABLED_FOCUSED_STATE_SET = StateSet.get(12);

    protected static final int[] ENABLED_SELECTED_STATE_SET = StateSet.get(10);

    protected static final int[] ENABLED_WINDOW_FOCUSED_STATE_SET = StateSet.get(9);

    protected static final int[] FOCUSED_SELECTED_STATE_SET = StateSet.get(6);

    protected static final int[] FOCUSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(5);

    protected static final int[] SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(3);

    protected static final int[] ENABLED_FOCUSED_SELECTED_STATE_SET = StateSet.get(14);

    protected static final int[] ENABLED_FOCUSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(13);

    protected static final int[] ENABLED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(11);

    protected static final int[] FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(7);

    protected static final int[] ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(15);

    protected static final int[] PRESSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(17);

    protected static final int[] PRESSED_SELECTED_STATE_SET = StateSet.get(18);

    protected static final int[] PRESSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(19);

    protected static final int[] PRESSED_FOCUSED_STATE_SET = StateSet.get(20);

    protected static final int[] PRESSED_FOCUSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(21);

    protected static final int[] PRESSED_FOCUSED_SELECTED_STATE_SET = StateSet.get(22);

    protected static final int[] PRESSED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(23);

    protected static final int[] PRESSED_ENABLED_STATE_SET = StateSet.get(24);

    protected static final int[] PRESSED_ENABLED_WINDOW_FOCUSED_STATE_SET = StateSet.get(25);

    protected static final int[] PRESSED_ENABLED_SELECTED_STATE_SET = StateSet.get(26);

    protected static final int[] PRESSED_ENABLED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(27);

    protected static final int[] PRESSED_ENABLED_FOCUSED_STATE_SET = StateSet.get(28);

    protected static final int[] PRESSED_ENABLED_FOCUSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(29);

    protected static final int[] PRESSED_ENABLED_FOCUSED_SELECTED_STATE_SET = StateSet.get(30);

    protected static final int[] PRESSED_ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(31);

    private static final ThreadLocal<Rect> sThreadLocal = ThreadLocal.withInitial(Rect::new);

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(65536);

    static final int PFLAG_WANTS_FOCUS = 1;

    static final int PFLAG_FOCUSED = 2;

    static final int PFLAG_SELECTED = 4;

    static final int PFLAG_IS_ROOT_NAMESPACE = 8;

    static final int PFLAG_HAS_BOUNDS = 16;

    static final int PFLAG_DRAWN = 32;

    static final int PFLAG_DRAW_ANIMATION = 64;

    static final int PFLAG_SKIP_DRAW = 128;

    static final int PFLAG_REQUEST_TRANSPARENT_REGIONS = 512;

    static final int PFLAG_DRAWABLE_STATE_DIRTY = 1024;

    static final int PFLAG_MEASURED_DIMENSION_SET = 2048;

    static final int PFLAG_FORCE_LAYOUT = 4096;

    static final int PFLAG_LAYOUT_REQUIRED = 8192;

    private static final int PFLAG_PRESSED = 16384;

    static final int PFLAG_DRAWING_CACHE_VALID = 32768;

    static final int PFLAG_ANIMATION_STARTED = 65536;

    private static final int PFLAG_SAVE_STATE_CALLED = 131072;

    static final int PFLAG_ALPHA_SET = 262144;

    static final int PFLAG_SCROLL_CONTAINER = 524288;

    static final int PFLAG_SCROLL_CONTAINER_ADDED = 1048576;

    static final int PFLAG_DIRTY = 2097152;

    static final int PFLAG_DIRTY_MASK = 2097152;

    static final int PFLAG_OPAQUE_BACKGROUND = 8388608;

    static final int PFLAG_OPAQUE_SCROLLBARS = 16777216;

    static final int PFLAG_OPAQUE_MASK = 25165824;

    private static final int PFLAG_PREPRESSED = 33554432;

    static final int PFLAG_CANCEL_NEXT_UP_EVENT = 67108864;

    private static final int PFLAG_AWAKEN_SCROLL_BARS_ON_ATTACH = 134217728;

    private static final int PFLAG_HOVERED = 268435456;

    static final int PFLAG_ACTIVATED = 1073741824;

    static final int PFLAG_INVALIDATED = Integer.MIN_VALUE;

    static final int PFLAG2_DRAG_CAN_ACCEPT = 1;

    static final int PFLAG2_DRAG_HOVERED = 2;

    @Internal
    public static final int LAYOUT_DIRECTION_UNDEFINED = -1;

    public static final int LAYOUT_DIRECTION_LTR = 0;

    public static final int LAYOUT_DIRECTION_RTL = 1;

    public static final int LAYOUT_DIRECTION_INHERIT = 2;

    public static final int LAYOUT_DIRECTION_LOCALE = 3;

    static final int PFLAG2_LAYOUT_DIRECTION_MASK_SHIFT = 2;

    static final int PFLAG2_LAYOUT_DIRECTION_MASK = 12;

    static final int PFLAG2_LAYOUT_DIRECTION_RESOLVED_RTL = 16;

    static final int PFLAG2_LAYOUT_DIRECTION_RESOLVED = 32;

    static final int PFLAG2_LAYOUT_DIRECTION_RESOLVED_MASK = 48;

    private static final int[] LAYOUT_DIRECTION_FLAGS = new int[] { 0, 1, 2, 3 };

    private static final int LAYOUT_DIRECTION_DEFAULT = 2;

    static final int LAYOUT_DIRECTION_RESOLVED_DEFAULT = 0;

    public static final int TEXT_DIRECTION_INHERIT = 0;

    public static final int TEXT_DIRECTION_FIRST_STRONG = 1;

    public static final int TEXT_DIRECTION_ANY_RTL = 2;

    public static final int TEXT_DIRECTION_LTR = 3;

    public static final int TEXT_DIRECTION_RTL = 4;

    public static final int TEXT_DIRECTION_LOCALE = 5;

    public static final int TEXT_DIRECTION_FIRST_STRONG_LTR = 6;

    public static final int TEXT_DIRECTION_FIRST_STRONG_RTL = 7;

    private static final int TEXT_DIRECTION_DEFAULT = 0;

    static final int TEXT_DIRECTION_RESOLVED_DEFAULT = 1;

    static final int PFLAG2_TEXT_DIRECTION_MASK_SHIFT = 6;

    static final int PFLAG2_TEXT_DIRECTION_MASK = 448;

    private static final int[] PFLAG2_TEXT_DIRECTION_FLAGS = new int[] { 0, 64, 128, 192, 256, 320, 384, 448 };

    static final int PFLAG2_TEXT_DIRECTION_RESOLVED = 512;

    static final int PFLAG2_TEXT_DIRECTION_RESOLVED_MASK_SHIFT = 10;

    static final int PFLAG2_TEXT_DIRECTION_RESOLVED_MASK = 7168;

    static final int PFLAG2_TEXT_DIRECTION_RESOLVED_DEFAULT = 1024;

    public static final int TEXT_ALIGNMENT_INHERIT = 0;

    public static final int TEXT_ALIGNMENT_GRAVITY = 1;

    public static final int TEXT_ALIGNMENT_TEXT_START = 2;

    public static final int TEXT_ALIGNMENT_TEXT_END = 3;

    public static final int TEXT_ALIGNMENT_CENTER = 4;

    public static final int TEXT_ALIGNMENT_VIEW_START = 5;

    public static final int TEXT_ALIGNMENT_VIEW_END = 6;

    private static final int TEXT_ALIGNMENT_DEFAULT = 1;

    static final int TEXT_ALIGNMENT_RESOLVED_DEFAULT = 1;

    static final int PFLAG2_TEXT_ALIGNMENT_MASK_SHIFT = 13;

    static final int PFLAG2_TEXT_ALIGNMENT_MASK = 57344;

    private static final int[] PFLAG2_TEXT_ALIGNMENT_FLAGS = new int[] { 0, 8192, 16384, 24576, 32768, 40960, 49152 };

    static final int PFLAG2_TEXT_ALIGNMENT_RESOLVED = 65536;

    static final int PFLAG2_TEXT_ALIGNMENT_RESOLVED_MASK_SHIFT = 17;

    static final int PFLAG2_TEXT_ALIGNMENT_RESOLVED_MASK = 917504;

    private static final int PFLAG2_TEXT_ALIGNMENT_RESOLVED_DEFAULT = 131072;

    static final int PFLAG2_VIEW_QUICK_REJECTED = 268435456;

    static final int PFLAG2_PADDING_RESOLVED = 536870912;

    static final int PFLAG2_DRAWABLE_RESOLVED = 1073741824;

    static final int PFLAG2_HAS_TRANSIENT_STATE = Integer.MIN_VALUE;

    static final int ALL_RTL_PROPERTIES_RESOLVED = 1610678816;

    static final int PFLAG3_VIEW_IS_ANIMATING_TRANSFORM = 1;

    static final int PFLAG3_VIEW_IS_ANIMATING_ALPHA = 2;

    static final int PFLAG3_IS_LAID_OUT = 4;

    static final int PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT = 8;

    static final int PFLAG3_CALLED_SUPER = 16;

    static final int PFLAG3_APPLYING_INSETS = 32;

    static final int PFLAG3_FITTING_SYSTEM_WINDOWS = 64;

    static final int PFLAG3_NESTED_SCROLLING_ENABLED = 128;

    static final int PFLAG3_SCROLL_INDICATOR_TOP = 256;

    static final int PFLAG3_SCROLL_INDICATOR_BOTTOM = 512;

    static final int PFLAG3_SCROLL_INDICATOR_LEFT = 1024;

    static final int PFLAG3_SCROLL_INDICATOR_RIGHT = 2048;

    static final int PFLAG3_SCROLL_INDICATOR_START = 4096;

    static final int PFLAG3_SCROLL_INDICATOR_END = 8192;

    static final int DRAG_MASK = 3;

    static final int SCROLL_INDICATORS_NONE = 0;

    static final int SCROLL_INDICATORS_PFLAG3_MASK = 16128;

    static final int SCROLL_INDICATORS_TO_PFLAGS3_LSHIFT = 8;

    public static final int SCROLL_INDICATOR_TOP = 1;

    public static final int SCROLL_INDICATOR_BOTTOM = 2;

    public static final int SCROLL_INDICATOR_LEFT = 4;

    public static final int SCROLL_INDICATOR_RIGHT = 8;

    public static final int SCROLL_INDICATOR_START = 16;

    public static final int SCROLL_INDICATOR_END = 32;

    private static final int PFLAG3_CLUSTER = 32768;

    private static final int PFLAG3_FINGER_DOWN = 131072;

    private static final int PFLAG3_FOCUSED_BY_DEFAULT = 262144;

    private static final int PFLAG3_OVERLAPPING_RENDERING_FORCED_VALUE = 8388608;

    private static final int PFLAG3_HAS_OVERLAPPING_RENDERING_FORCED = 16777216;

    static final int PFLAG3_TEMPORARY_DETACH = 33554432;

    private static final int PFLAG3_NO_REVEAL_ON_FOCUS = 67108864;

    private static final int PFLAG3_SCREEN_READER_FOCUSABLE = 268435456;

    private static final int PFLAG3_AGGREGATED_VISIBLE = 536870912;

    private static final int PFLAG4_ALLOW_CLICK_WHEN_DISABLED = 4096;

    private static final int PFLAG4_DETACHED = 8192;

    private static final int PFLAG4_HAS_TRANSLATION_TRANSIENT_STATE = 16384;

    public static final int OVER_SCROLL_ALWAYS = 0;

    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;

    public static final int OVER_SCROLL_NEVER = 2;

    public static final int SCROLL_AXIS_NONE = 0;

    public static final int SCROLL_AXIS_HORIZONTAL = 1;

    public static final int SCROLL_AXIS_VERTICAL = 2;

    public static final int TYPE_TOUCH = 0;

    public static final int TYPE_NON_TOUCH = 1;

    private int mOverScrollMode;

    int mPrivateFlags;

    int mPrivateFlags2;

    int mPrivateFlags3;

    int mPrivateFlags4;

    ViewParent mParent;

    AttachInfo mAttachInfo;

    int mTransientStateCount = 0;

    int mWindowAttachCount;

    ViewGroup.LayoutParams mLayoutParams;

    int mViewFlags;

    private SparseArray<Object> mKeyedTags;

    int mID = -1;

    final RenderNode mRenderNode = new RenderNode();

    private float mAlpha = 1.0F;

    private float mTransitionAlpha = 1.0F;

    int mLeft;

    int mRight;

    int mTop;

    int mBottom;

    protected int mScrollX;

    protected int mScrollY;

    protected int mPaddingLeft = 0;

    protected int mPaddingRight = 0;

    protected int mPaddingTop;

    protected int mPaddingBottom;

    private View.MatchIdPredicate mMatchIdPredicate;

    int mUserPaddingRight;

    int mUserPaddingBottom;

    int mUserPaddingLeft;

    int mUserPaddingStart;

    int mUserPaddingEnd;

    int mUserPaddingLeftInitial;

    int mUserPaddingRightInitial;

    private static final int UNDEFINED_PADDING = Integer.MIN_VALUE;

    private boolean mLeftPaddingDefined = false;

    private boolean mRightPaddingDefined = false;

    int mMeasuredWidth;

    int mMeasuredHeight;

    int mOldWidthMeasureSpec = Integer.MIN_VALUE;

    int mOldHeightMeasureSpec = Integer.MIN_VALUE;

    private Drawable mBackground;

    private boolean mBackgroundSizeChanged;

    private View.ForegroundInfo mForegroundInfo;

    private Drawable mScrollIndicatorDrawable;

    boolean mDefaultFocusHighlightEnabled = true;

    private Drawable mDefaultFocusHighlight;

    private Drawable mDefaultFocusHighlightCache;

    private boolean mDefaultFocusHighlightSizeChanged;

    private String mTransitionName;

    View.ListenerInfo mListenerInfo;

    View.TooltipInfo mTooltipInfo;

    private float mLongClickX = Float.NaN;

    private float mLongClickY = Float.NaN;

    private Context mContext;

    private ScrollCache mScrollCache;

    private int[] mDrawableState = null;

    private StateListAnimator mStateListAnimator;

    private int mNextFocusLeftId = -1;

    private int mNextFocusRightId = -1;

    private int mNextFocusUpId = -1;

    private int mNextFocusDownId = -1;

    int mNextFocusForwardId = -1;

    int mNextClusterForwardId = -1;

    private View.CheckForLongPress mPendingCheckForLongPress;

    private View.CheckForTap mPendingCheckForTap;

    private View.PerformClick mPerformClick;

    private View.UnsetPressedState mUnsetPressedState;

    private boolean mHasPerformedLongPress;

    private boolean mInContextButtonPress;

    private boolean mIgnoreNextUpEvent;

    private int mMinHeight;

    private int mMinWidth;

    private ViewTreeObserver mFloatingTreeObserver;

    private ViewParent mNestedScrollingParentTouch;

    private ViewParent mNestedScrollingParentNonTouch;

    private int[] mTempNestedScrollConsumed;

    private HandlerActionQueue mRunQueue;

    public static final FloatProperty<View> ALPHA = new FloatProperty<View>("alpha") {

        public void setValue(View object, float value) {
            object.setAlpha(value);
        }

        public Float get(View object) {
            return object.getAlpha();
        }
    };

    public static final FloatProperty<View> TRANSLATION_X = new FloatProperty<View>("translationX") {

        public void setValue(View object, float value) {
            object.setTranslationX(value);
        }

        public Float get(View object) {
            return object.getTranslationX();
        }
    };

    public static final FloatProperty<View> TRANSLATION_Y = new FloatProperty<View>("translationY") {

        public void setValue(View object, float value) {
            object.setTranslationY(value);
        }

        public Float get(View object) {
            return object.getTranslationY();
        }
    };

    public static final FloatProperty<View> TRANSLATION_Z = new FloatProperty<View>("translationZ") {

        public void setValue(View object, float value) {
            object.setTranslationZ(value);
        }

        public Float get(View object) {
            return object.getTranslationZ();
        }
    };

    public static final FloatProperty<View> X = new FloatProperty<View>("x") {

        public void setValue(View object, float value) {
            object.setX(value);
        }

        public Float get(View object) {
            return object.getX();
        }
    };

    public static final FloatProperty<View> Y = new FloatProperty<View>("y") {

        public void setValue(View object, float value) {
            object.setY(value);
        }

        public Float get(View object) {
            return object.getY();
        }
    };

    public static final FloatProperty<View> Z = new FloatProperty<View>("z") {

        public void setValue(View object, float value) {
            object.setZ(value);
        }

        public Float get(View object) {
            return object.getZ();
        }
    };

    public static final FloatProperty<View> ROTATION = new FloatProperty<View>("rotation") {

        public void setValue(View object, float value) {
            object.setRotation(value);
        }

        public Float get(View object) {
            return object.getRotation();
        }
    };

    public static final FloatProperty<View> ROTATION_X = new FloatProperty<View>("rotationX") {

        public void setValue(View object, float value) {
            object.setRotationX(value);
        }

        public Float get(View object) {
            return object.getRotationX();
        }
    };

    public static final FloatProperty<View> ROTATION_Y = new FloatProperty<View>("rotationY") {

        public void setValue(View object, float value) {
            object.setRotationY(value);
        }

        public Float get(View object) {
            return object.getRotationY();
        }
    };

    public static final FloatProperty<View> SCALE_X = new FloatProperty<View>("scaleX") {

        public void setValue(View object, float value) {
            object.setScaleX(value);
        }

        public Float get(View object) {
            return object.getScaleX();
        }
    };

    public static final FloatProperty<View> SCALE_Y = new FloatProperty<View>("scaleY") {

        public void setValue(View object, float value) {
            object.setScaleY(value);
        }

        public Float get(View object) {
            return object.getScaleY();
        }
    };

    public static final IntProperty<View> LEFT = new IntProperty<View>("left") {

        public void setValue(View object, int value) {
            object.setLeft(value);
        }

        public Integer get(View object) {
            return object.getLeft();
        }
    };

    public static final IntProperty<View> TOP = new IntProperty<View>("top") {

        public void setValue(View object, int value) {
            object.setTop(value);
        }

        public Integer get(View object) {
            return object.getTop();
        }
    };

    public static final IntProperty<View> RIGHT = new IntProperty<View>("right") {

        public void setValue(View object, int value) {
            object.setRight(value);
        }

        public Integer get(View object) {
            return object.getRight();
        }
    };

    public static final IntProperty<View> BOTTOM = new IntProperty<View>("bottom") {

        public void setValue(View object, int value) {
            object.setBottom(value);
        }

        public Integer get(View object) {
            return object.getBottom();
        }
    };

    public static final IntProperty<View> SCROLL_X = new IntProperty<View>("scrollX") {

        public void setValue(View object, int value) {
            object.setScrollX(value);
        }

        public Integer get(View object) {
            return object.getScrollX();
        }
    };

    public static final IntProperty<View> SCROLL_Y = new IntProperty<View>("scrollY") {

        public void setValue(View object, int value) {
            object.setScrollY(value);
        }

        public Integer get(View object) {
            return object.getScrollY();
        }
    };

    public View(Context context) {
        this.mContext = context;
        this.mViewFlags = 402653200;
        this.mPrivateFlags2 = 140296;
        this.setOverScrollMode(1);
        this.mUserPaddingStart = Integer.MIN_VALUE;
        this.mUserPaddingEnd = Integer.MIN_VALUE;
    }

    public final boolean isShowingLayoutBounds() {
        return this.mAttachInfo != null && this.mAttachInfo.mDebugLayout;
    }

    final void draw(@NonNull Canvas canvas, @NonNull ViewGroup group, boolean clip) {
        boolean identity = this.hasIdentityMatrix();
        if (!clip || !identity || !canvas.quickReject((float) this.mLeft, (float) this.mTop, (float) this.mRight, (float) this.mBottom)) {
            float alpha = this.mAlpha * this.mTransitionAlpha;
            if (!(alpha <= 0.001F)) {
                this.computeScroll();
                int sx = this.mScrollX;
                int sy = this.mScrollY;
                int saveCount = canvas.save();
                canvas.translate((float) this.mLeft, (float) this.mTop);
                if (this.mRenderNode.getAnimationMatrix() != null) {
                    canvas.concat(this.mRenderNode.getAnimationMatrix());
                }
                if (!identity) {
                    canvas.concat(this.getMatrix());
                }
                boolean hasSpace = true;
                if (clip) {
                    hasSpace = canvas.clipRect(0.0F, 0.0F, (float) (this.mRight - this.mLeft), (float) (this.mBottom - this.mTop));
                }
                canvas.translate((float) (-sx), (float) (-sy));
                if (hasSpace) {
                    if (alpha < 0.999F) {
                        canvas.saveLayer((float) sx, (float) sy, (float) (sx + this.mRight - this.mLeft), (float) (sy + this.mBottom - this.mTop), (int) (alpha * 255.0F));
                    }
                    if ((this.mPrivateFlags & 128) == 128) {
                        this.dispatchDraw(canvas);
                    } else {
                        this.draw(canvas);
                    }
                }
                canvas.restoreToCount(saveCount);
            }
        }
    }

    @CallSuper
    public void draw(@NonNull Canvas canvas) {
        this.drawBackground(canvas);
        this.onDraw(canvas);
        this.dispatchDraw(canvas);
        this.onDrawForeground(canvas);
    }

    private void drawBackground(@NonNull Canvas canvas) {
        Drawable background = this.mBackground;
        if (background != null) {
            if (this.mBackgroundSizeChanged) {
                background.setBounds(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
                this.mBackgroundSizeChanged = false;
            }
            int scrollX = this.mScrollX;
            int scrollY = this.mScrollY;
            if ((scrollX | scrollY) == 0) {
                background.draw(canvas);
            } else {
                canvas.translate((float) scrollX, (float) scrollY);
                background.draw(canvas);
                canvas.translate((float) (-scrollX), (float) (-scrollY));
            }
        }
    }

    protected void onDraw(@NonNull Canvas canvas) {
    }

    protected void dispatchDraw(@NonNull Canvas canvas) {
    }

    public void onDrawForeground(@NonNull Canvas canvas) {
        this.onDrawScrollIndicators(canvas);
        this.onDrawScrollBars(canvas);
        Drawable foreground = this.mForegroundInfo != null ? this.mForegroundInfo.mDrawable : null;
        if (foreground != null) {
            if (this.mForegroundInfo.mBoundsChanged) {
                this.mForegroundInfo.mBoundsChanged = false;
                Rect selfBounds = this.mForegroundInfo.mSelfBounds;
                Rect overlayBounds = this.mForegroundInfo.mOverlayBounds;
                if (this.mForegroundInfo.mInsidePadding) {
                    selfBounds.set(0, 0, this.getWidth(), this.getHeight());
                } else {
                    selfBounds.set(this.getPaddingLeft(), this.getPaddingTop(), this.getWidth() - this.getPaddingRight(), this.getHeight() - this.getPaddingBottom());
                }
                int ld = this.getLayoutDirection();
                Gravity.apply(this.mForegroundInfo.mGravity, foreground.getIntrinsicWidth(), foreground.getIntrinsicHeight(), selfBounds, overlayBounds, ld);
                foreground.setBounds(overlayBounds);
            }
            foreground.draw(canvas);
        }
    }

    private void onDrawScrollIndicators(@NonNull Canvas c) {
        if ((this.mPrivateFlags3 & 16128) != 0) {
            Drawable dr = this.mScrollIndicatorDrawable;
            if (dr != null) {
                if (this.mAttachInfo != null) {
                    int h = dr.getIntrinsicHeight();
                    int w = dr.getIntrinsicWidth();
                    Rect rect = this.mAttachInfo.mTmpInvalRect;
                    this.getScrollIndicatorBounds(rect);
                    if ((this.mPrivateFlags3 & 256) != 0) {
                        boolean canScrollUp = this.canScrollVertically(-1);
                        if (canScrollUp) {
                            dr.setBounds(rect.left, rect.top, rect.right, rect.top + h);
                            dr.draw(c);
                        }
                    }
                    if ((this.mPrivateFlags3 & 512) != 0) {
                        boolean canScrollDown = this.canScrollVertically(1);
                        if (canScrollDown) {
                            dr.setBounds(rect.left, rect.bottom - h, rect.right, rect.bottom);
                            dr.draw(c);
                        }
                    }
                    int rightRtl;
                    int leftRtl;
                    if (this.getLayoutDirection() == 1) {
                        leftRtl = 8192;
                        rightRtl = 4096;
                    } else {
                        leftRtl = 4096;
                        rightRtl = 8192;
                    }
                    int leftMask = 1024 | leftRtl;
                    if ((this.mPrivateFlags3 & leftMask) != 0) {
                        boolean canScrollLeft = this.canScrollHorizontally(-1);
                        if (canScrollLeft) {
                            dr.setBounds(rect.left, rect.top, rect.left + w, rect.bottom);
                            dr.draw(c);
                        }
                    }
                    int rightMask = 2048 | rightRtl;
                    if ((this.mPrivateFlags3 & rightMask) != 0) {
                        boolean canScrollRight = this.canScrollHorizontally(1);
                        if (canScrollRight) {
                            dr.setBounds(rect.right - w, rect.top, rect.right, rect.bottom);
                            dr.draw(c);
                        }
                    }
                }
            }
        }
    }

    protected final void onDrawScrollBars(@NonNull Canvas canvas) {
        ScrollCache cache = this.mScrollCache;
        if (cache != null && cache.mState != 0) {
            boolean invalidate = false;
            if (cache.mState == 2) {
                long currentTime = AnimationUtils.currentAnimationTimeMillis();
                float fraction = (float) (currentTime - cache.mFadeStartTime) / (float) cache.mFadeDuration;
                if (fraction >= 1.0F) {
                    cache.mState = 0;
                    return;
                }
                int alpha = 255 - (int) (fraction * 255.0F);
                cache.mScrollBar.mutate().setAlpha(alpha);
                invalidate = true;
            } else {
                cache.mScrollBar.mutate().setAlpha(255);
            }
            boolean drawHorizontalScrollBar = this.isHorizontalScrollBarEnabled();
            boolean drawVerticalScrollBar = this.isVerticalScrollBarEnabled() && !this.isVerticalScrollBarHidden();
            if (drawVerticalScrollBar || drawHorizontalScrollBar) {
                ScrollBar scrollBar = cache.mScrollBar;
                if (drawHorizontalScrollBar) {
                    scrollBar.setParameters(this.computeHorizontalScrollRange(), this.computeHorizontalScrollOffset(), this.computeHorizontalScrollExtent(), false);
                    Rect bounds = cache.mScrollBarBounds;
                    this.getHorizontalScrollBarBounds(bounds, null);
                    scrollBar.setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom);
                    scrollBar.draw(canvas);
                    if (invalidate) {
                        this.invalidate();
                    }
                }
                if (drawVerticalScrollBar) {
                    scrollBar.setParameters(this.computeVerticalScrollRange(), this.computeVerticalScrollOffset(), this.computeVerticalScrollExtent(), true);
                    Rect bounds = cache.mScrollBarBounds;
                    this.getVerticalScrollBarBounds(bounds, null);
                    scrollBar.setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom);
                    scrollBar.draw(canvas);
                    if (invalidate) {
                        this.invalidate();
                    }
                }
            }
        }
    }

    public void layout(int l, int t, int r, int b) {
        int oldL = this.mLeft;
        int oldT = this.mTop;
        int oldB = this.mBottom;
        int oldR = this.mRight;
        boolean changed = this.setFrame(l, t, r, b);
        if (changed || (this.mPrivateFlags & 8192) != 0) {
            this.onLayout(changed, l, t, r, b);
            this.mPrivateFlags &= -8193;
            View.ListenerInfo li = this.mListenerInfo;
            if (li != null && li.mOnLayoutChangeListeners != null) {
                for (View.OnLayoutChangeListener listener : (ArrayList) li.mOnLayoutChangeListeners.clone()) {
                    listener.onLayoutChange(this, l, t, r, b, oldL, oldT, oldR, oldB);
                }
            }
        }
        boolean wasLayoutValid = this.isLayoutValid();
        this.mPrivateFlags &= -4097;
        this.mPrivateFlags3 |= 4;
        if (!wasLayoutValid && this.isFocused()) {
            this.mPrivateFlags &= -2;
            if (this.canTakeFocus()) {
                this.clearParentsWantFocus();
            } else if (this.getViewRoot() == null || !this.getViewRoot().isInLayout()) {
                this.clearFocusInternal(null, true, false);
                this.clearParentsWantFocus();
            } else if (this.hasNoParentWantsFocus()) {
                this.clearFocusInternal(null, true, false);
            }
        } else if ((this.mPrivateFlags & 1) != 0) {
            this.mPrivateFlags &= -2;
            View focused = this.findFocus();
            if (focused != null && !this.restoreDefaultFocus() && this.hasNoParentWantsFocus()) {
                focused.clearFocusInternal(null, true, false);
            }
        }
    }

    private boolean hasNoParentWantsFocus() {
        ViewParent parent = this.mParent;
        while (parent instanceof ViewGroup) {
            ViewGroup pv = (ViewGroup) parent;
            if ((pv.mPrivateFlags & 1) != 0) {
                return false;
            }
            parent = pv.mParent;
        }
        return true;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    protected boolean setFrame(int left, int top, int right, int bottom) {
        if (this.mLeft == left && this.mRight == right && this.mTop == top && this.mBottom == bottom) {
            return false;
        } else {
            int drawn = this.mPrivateFlags & 32;
            int oldWidth = this.mRight - this.mLeft;
            int oldHeight = this.mBottom - this.mTop;
            int newWidth = right - left;
            int newHeight = bottom - top;
            boolean sizeChanged = newWidth != oldWidth || newHeight != oldHeight;
            this.invalidate();
            this.mLeft = left;
            this.mTop = top;
            this.mRight = right;
            this.mBottom = bottom;
            this.mRenderNode.setPosition(this.mLeft, this.mTop, this.mRight, this.mBottom);
            this.mPrivateFlags |= 16;
            if (sizeChanged) {
                this.sizeChange(newWidth, newHeight, oldWidth, oldHeight);
            }
            this.mPrivateFlags |= 32;
            this.mPrivateFlags |= drawn;
            this.mDefaultFocusHighlightSizeChanged = true;
            this.mBackgroundSizeChanged = true;
            if (this.mForegroundInfo != null) {
                this.mForegroundInfo.mBoundsChanged = true;
            }
            return true;
        }
    }

    protected void onSizeChanged(int width, int height, int prevWidth, int prevHeight) {
    }

    public final void measure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean needsLayout = (this.mPrivateFlags & 4096) == 4096;
        if (!needsLayout) {
            boolean specChanged = widthMeasureSpec != this.mOldWidthMeasureSpec || heightMeasureSpec != this.mOldHeightMeasureSpec;
            boolean isSpecExactly = MeasureSpec.getMode(widthMeasureSpec) == 1073741824 && MeasureSpec.getMode(heightMeasureSpec) == 1073741824;
            boolean matchesSpecSize = this.getMeasuredWidth() == MeasureSpec.getSize(widthMeasureSpec) && this.getMeasuredHeight() == MeasureSpec.getSize(heightMeasureSpec);
            needsLayout = specChanged && (!isSpecExactly || !matchesSpecSize);
        }
        if (needsLayout) {
            this.mPrivateFlags &= -2049;
            this.resolveRtlPropertiesIfNeeded();
            this.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if ((this.mPrivateFlags & 2048) == 0) {
                throw new IllegalStateException(this.getClass().getName() + "#onMeasure() did not set the measured dimensionby calling setMeasuredDimension()");
            }
            this.mPrivateFlags |= 8192;
        }
        this.mOldWidthMeasureSpec = widthMeasureSpec;
        this.mOldHeightMeasureSpec = heightMeasureSpec;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(getDefaultSize(this.mMinWidth, widthMeasureSpec), getDefaultSize(this.mMinHeight, heightMeasureSpec));
    }

    protected final void setMeasuredDimension(int measuredWidth, int measuredHeight) {
        this.mMeasuredWidth = measuredWidth;
        this.mMeasuredHeight = measuredHeight;
        this.mPrivateFlags |= 2048;
    }

    public final int getMeasuredWidth() {
        return this.mMeasuredWidth & 16777215;
    }

    public final int getMeasuredWidthAndState() {
        return this.mMeasuredWidth;
    }

    public final int getMeasuredHeight() {
        return this.mMeasuredHeight & 16777215;
    }

    public final int getMeasuredHeightAndState() {
        return this.mMeasuredHeight;
    }

    public final int getMeasuredState() {
        return this.mMeasuredWidth & 0xFF000000 | this.mMeasuredHeight >> 16 & -256;
    }

    public ViewGroup.LayoutParams getLayoutParams() {
        return this.mLayoutParams;
    }

    public void setLayoutParams(@NonNull ViewGroup.LayoutParams params) {
        this.mLayoutParams = (ViewGroup.LayoutParams) Objects.requireNonNull(params);
        this.resolveLayoutParams();
        if (this.mParent instanceof ViewGroup) {
            ((ViewGroup) this.mParent).onSetLayoutParams(this, params);
        }
        this.requestLayout();
    }

    @Internal
    public void resolveLayoutParams() {
        if (this.mLayoutParams != null) {
            this.mLayoutParams.resolveLayoutDirection(this.getLayoutDirection());
        }
    }

    public static int combineMeasuredStates(int curState, int newState) {
        return curState | newState;
    }

    public static int resolveSize(int size, int measureSpec) {
        return resolveSizeAndState(size, measureSpec, 0) & 16777215;
    }

    public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        return switch(specMode) {
            case Integer.MIN_VALUE ->
                {
                    if (specSize < size) {
                        ???;
                    } else {
                        yield size;
                    }
                }
            default ->
                size;
            case 1073741824 ->
                specSize;
        } | childMeasuredState & 0xFF000000;
    }

    public static int getDefaultSize(int size, int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        return switch(specMode) {
            case Integer.MIN_VALUE, 1073741824 ->
                specSize;
            default ->
                size;
        };
    }

    public final int dp(float value) {
        float f = value * this.getContext().getResources().getDisplayMetrics().density;
        int res = (int) (f >= 0.0F ? f + 0.5F : f - 0.5F);
        if (res != 0) {
            return res;
        } else if (value == 0.0F) {
            return 0;
        } else {
            return value > 0.0F ? 1 : -1;
        }
    }

    public final int sp(float value) {
        float f = value * this.getContext().getResources().getDisplayMetrics().scaledDensity;
        int res = (int) (f >= 0.0F ? f + 0.5F : f - 0.5F);
        if (res != 0) {
            return res;
        } else if (value == 0.0F) {
            return 0;
        } else {
            return value > 0.0F ? 1 : -1;
        }
    }

    protected int getSuggestedMinimumHeight() {
        return this.mBackground == null ? this.mMinHeight : Math.max(this.mMinHeight, this.mBackground.getMinimumHeight());
    }

    protected int getSuggestedMinimumWidth() {
        return this.mBackground == null ? this.mMinWidth : Math.max(this.mMinWidth, this.mBackground.getMinimumWidth());
    }

    public int getMinimumWidth() {
        return this.mMinWidth;
    }

    public void setMinimumWidth(int minWidth) {
        this.mMinWidth = minWidth;
        this.requestLayout();
    }

    public int getMinimumHeight() {
        return this.mMinHeight;
    }

    public void setMinimumHeight(int minHeight) {
        this.mMinHeight = minHeight;
        this.requestLayout();
    }

    @Nullable
    public final ViewParent getParent() {
        return this.mParent;
    }

    final void assignParent(@Nullable ViewParent parent) {
        if (this.mParent == null) {
            this.mParent = parent;
        } else {
            if (parent != null) {
                throw new IllegalStateException("The parent of view " + this + " has been assigned");
            }
            this.mParent = null;
        }
    }

    @Internal
    public void setIsRootNamespace(boolean isRoot) {
        if (isRoot) {
            this.mPrivateFlags |= 8;
        } else {
            this.mPrivateFlags &= -9;
        }
    }

    @Internal
    public boolean isRootNamespace() {
        return (this.mPrivateFlags & 8) != 0;
    }

    public int getId() {
        return this.mID;
    }

    public void setId(int id) {
        this.mID = id;
    }

    public static int generateViewId() {
        int result;
        int newValue;
        do {
            result = sNextGeneratedId.get();
            newValue = result + 1;
            if (newValue > 16777215) {
                newValue = 65536;
            }
        } while (!sNextGeneratedId.compareAndSet(result, newValue));
        return result;
    }

    @Nullable
    public Object getTag(int key) {
        return this.mKeyedTags != null ? this.mKeyedTags.get(key) : null;
    }

    public void setTag(int key, Object tag) {
        if ((key & 0xFF000000) == 0) {
            throw new IllegalArgumentException("The key must be a valid resource id.");
        } else {
            if (this.mKeyedTags == null) {
                this.mKeyedTags = new SparseArray<>(2);
            }
            this.mKeyedTags.put(key, tag);
        }
    }

    public int getVisibility() {
        return this.mViewFlags & 12;
    }

    public void setVisibility(int visibility) {
        this.setFlags(visibility, 12);
    }

    public boolean isEnabled() {
        return (this.mViewFlags & 32) == 0;
    }

    public void setEnabled(boolean enabled) {
        if (enabled != this.isEnabled()) {
            this.setFlags(enabled ? 0 : 32, 32);
            this.refreshDrawableState();
            this.invalidate();
            if (!enabled) {
                this.cancelPendingInputEvents();
            }
        }
    }

    public final boolean isFocusable() {
        return (this.mViewFlags & 1) == 1;
    }

    public int getFocusable() {
        return (this.mViewFlags & 16) > 0 ? 16 : this.mViewFlags & 1;
    }

    public void setFocusable(boolean focusable) {
        this.setFocusable(focusable ? 1 : 0);
    }

    public void setFocusable(int focusable) {
        if ((focusable & 17) == 0) {
            this.setFlags(0, 262144);
        }
        this.setFlags(focusable, 17);
    }

    public final boolean isFocusableInTouchMode() {
        return (this.mViewFlags & 262144) == 262144;
    }

    public void setFocusableInTouchMode(boolean focusableInTouchMode) {
        this.setFlags(focusableInTouchMode ? 262144 : 0, 262144);
        if (focusableInTouchMode) {
            this.setFlags(1, 17);
        }
    }

    public boolean isClickable() {
        return (this.mViewFlags & 16384) == 16384;
    }

    public void setClickable(boolean clickable) {
        this.setFlags(clickable ? 16384 : 0, 16384);
    }

    public boolean isLongClickable() {
        return (this.mViewFlags & 2097152) == 2097152;
    }

    public void setLongClickable(boolean longClickable) {
        this.setFlags(longClickable ? 2097152 : 0, 2097152);
    }

    public boolean isContextClickable() {
        return (this.mViewFlags & 8388608) == 8388608;
    }

    public void setContextClickable(boolean contextClickable) {
        this.setFlags(contextClickable ? 8388608 : 0, 8388608);
    }

    private void setPressed(float x, float y) {
        this.drawableHotspotChanged(x, y);
        this.setPressed(true);
    }

    public void setPressed(boolean pressed) {
        boolean needsRefresh = pressed != ((this.mPrivateFlags & 16384) == 16384);
        if (pressed) {
            this.mPrivateFlags |= 16384;
        } else {
            this.mPrivateFlags &= -16385;
        }
        if (needsRefresh) {
            this.refreshDrawableState();
        }
        this.dispatchSetPressed(pressed);
    }

    protected void dispatchSetPressed(boolean pressed) {
    }

    public boolean isPressed() {
        return (this.mPrivateFlags & 16384) == 16384;
    }

    public void setSelected(boolean selected) {
        if ((this.mPrivateFlags & 4) != 0 != selected) {
            this.mPrivateFlags = this.mPrivateFlags & -5 | (selected ? 4 : 0);
            if (!selected) {
                this.resetPressedState();
            }
            this.invalidate();
            this.refreshDrawableState();
            this.dispatchSetSelected(selected);
        }
    }

    protected void dispatchSetSelected(boolean selected) {
    }

    public boolean isSelected() {
        return (this.mPrivateFlags & 4) != 0;
    }

    public void setActivated(boolean activated) {
        if ((this.mPrivateFlags & 1073741824) != 0 != activated) {
            this.mPrivateFlags = this.mPrivateFlags & -1073741825 | (activated ? 1073741824 : 0);
            this.invalidate();
            this.refreshDrawableState();
            this.dispatchSetActivated(activated);
        }
    }

    protected void dispatchSetActivated(boolean activated) {
    }

    public boolean isActivated() {
        return (this.mPrivateFlags & 1073741824) != 0;
    }

    private boolean hasSize() {
        return this.mBottom > this.mTop && this.mRight > this.mLeft;
    }

    private boolean canTakeFocus() {
        return (this.mViewFlags & 12) == 0 && (this.mViewFlags & 1) == 1 && (this.mViewFlags & 32) == 0 && (!this.isLayoutValid() || this.hasSize());
    }

    void setFlags(int flags, int mask) {
        int old = this.mViewFlags;
        this.mViewFlags = this.mViewFlags & ~mask | flags & mask;
        int changed = this.mViewFlags ^ old;
        if (changed != 0) {
            int privateFlags = this.mPrivateFlags;
            boolean shouldNotifyFocusableAvailable = false;
            if ((this.mViewFlags & 16) != 0 && (changed & 16401) != 0) {
                int newFocus;
                if ((this.mViewFlags & 16384) != 0) {
                    newFocus = 1;
                } else {
                    newFocus = 0;
                }
                this.mViewFlags = this.mViewFlags & -2 | newFocus;
                int focusableChangedByAuto = old & 1 ^ newFocus & 1;
                changed = changed & -2 | focusableChangedByAuto;
            }
            if ((changed & 1) != 0 && (privateFlags & 16) != 0) {
                if ((old & 1) == 1 && (privateFlags & 2) != 0) {
                    this.clearFocus();
                } else if ((old & 1) == 0 && (privateFlags & 2) == 0 && this.mParent != null) {
                    shouldNotifyFocusableAvailable = this.canTakeFocus();
                }
            }
            int newVisibility = flags & 12;
            if (newVisibility == 0 && (changed & 12) != 0) {
                this.mPrivateFlags |= 32;
                this.invalidate();
                shouldNotifyFocusableAvailable = this.hasSize();
            }
            if ((changed & 32) != 0) {
                if ((this.mViewFlags & 32) == 0) {
                    shouldNotifyFocusableAvailable = this.canTakeFocus();
                } else if (this.isFocused()) {
                    this.clearFocus();
                }
            }
            if (shouldNotifyFocusableAvailable && this.mParent != null) {
                this.mParent.focusableViewAvailable(this);
            }
            if ((changed & 8) != 0) {
                this.requestLayout();
                if ((this.mViewFlags & 12) == 8) {
                    if (this.hasFocus()) {
                        this.clearFocus();
                    }
                    if (this.mParent instanceof View) {
                        ((View) this.mParent).invalidate();
                    }
                    this.mPrivateFlags |= 32;
                }
                if (this.mAttachInfo != null) {
                    this.mAttachInfo.mViewVisibilityChanged = true;
                }
            }
            if ((changed & 4) != 0) {
                this.mPrivateFlags |= 32;
                if ((this.mViewFlags & 12) == 4 && this.getRootView() != this && this.hasFocus()) {
                    this.clearFocus();
                }
                if (this.mAttachInfo != null) {
                    this.mAttachInfo.mViewVisibilityChanged = true;
                }
            }
            if ((changed & 12) != 0) {
                if (newVisibility != 0 && this.mAttachInfo != null) {
                }
                if (this.mParent instanceof ViewGroup parent) {
                    parent.onChildVisibilityChanged(this, changed & 12, newVisibility);
                    parent.invalidate();
                }
                if (this.mAttachInfo != null) {
                    this.dispatchVisibilityChanged(this, newVisibility);
                    if (this.mParent != null && this.getWindowVisibility() == 0 && (!(this.mParent instanceof ViewGroup) || ((ViewGroup) this.mParent).isShown())) {
                        this.dispatchVisibilityAggregated(newVisibility == 0);
                    }
                }
            }
            if ((changed & 128) != 0) {
                if ((this.mViewFlags & 128) == 0) {
                    this.mPrivateFlags &= -129;
                } else if (this.mBackground == null && this.mDefaultFocusHighlight == null && (this.mForegroundInfo == null || this.mForegroundInfo.mDrawable == null)) {
                    this.mPrivateFlags |= 128;
                } else {
                    this.mPrivateFlags &= -129;
                }
                this.requestLayout();
                this.invalidate();
            }
        }
    }

    public final void invalidate() {
        if ((this.mViewFlags & 12) == 0 || this.mParent instanceof ViewGroup && ((ViewGroup) this.mParent).isViewTransitioning(this)) {
            if (this.mAttachInfo != null) {
                this.mAttachInfo.mViewRoot.invalidate();
            }
        }
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        if (this.verifyDrawable(drawable)) {
            this.invalidate();
        }
    }

    @Override
    public final void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
        if (this.verifyDrawable(who)) {
            long delay = when - Core.timeMillis();
            if (this.mAttachInfo != null) {
                this.mAttachInfo.mViewRoot.mChoreographer.postCallbackDelayed(1, what, who, delay);
            } else {
                this.getRunQueue().postDelayed(what, delay);
            }
        }
    }

    @Override
    public final void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
        if (this.verifyDrawable(who)) {
            if (this.mAttachInfo != null) {
                this.mAttachInfo.mViewRoot.mChoreographer.removeCallbacks(1, what, who);
            }
            if (this.mRunQueue != null) {
                this.mRunQueue.removeCallbacks(what);
            }
        }
    }

    public final void unscheduleDrawable(@Nullable Drawable who) {
        if (this.mAttachInfo != null && who != null) {
            this.mAttachInfo.mViewRoot.mChoreographer.removeCallbacks(1, null, who);
        }
    }

    @Nullable
    View findUserSetNextFocus(View root, int direction) {
        switch(direction) {
            case 1:
                if (this.mID == -1) {
                    return null;
                }
                return root.findViewByPredicateInsideOut(this, t -> this.findViewInsideOutShouldExist(root, t, t.mNextFocusForwardId) == this);
            case 2:
                if (this.mNextFocusForwardId == -1) {
                    return null;
                }
                return this.findViewInsideOutShouldExist(root, this.mNextFocusForwardId);
            case 17:
                if (this.mNextFocusLeftId == -1) {
                    return null;
                }
                return this.findViewInsideOutShouldExist(root, this.mNextFocusLeftId);
            case 33:
                if (this.mNextFocusUpId == -1) {
                    return null;
                }
                return this.findViewInsideOutShouldExist(root, this.mNextFocusUpId);
            case 66:
                if (this.mNextFocusRightId == -1) {
                    return null;
                }
                return this.findViewInsideOutShouldExist(root, this.mNextFocusRightId);
            case 130:
                if (this.mNextFocusDownId == -1) {
                    return null;
                }
                return this.findViewInsideOutShouldExist(root, this.mNextFocusDownId);
            default:
                return null;
        }
    }

    View findUserSetNextKeyboardNavigationCluster(View root, int direction) {
        switch(direction) {
            case 1:
                if (this.mID == -1) {
                    return null;
                }
                int id = this.mID;
                return root.findViewByPredicateInsideOut(this, t -> t.mNextClusterForwardId == id);
            case 2:
                if (this.mNextClusterForwardId == -1) {
                    return null;
                }
                return this.findViewInsideOutShouldExist(root, this.mNextClusterForwardId);
            default:
                return null;
        }
    }

    private View findViewInsideOutShouldExist(View root, int id) {
        return this.findViewInsideOutShouldExist(root, this, id);
    }

    private View findViewInsideOutShouldExist(View root, View start, int id) {
        if (this.mMatchIdPredicate == null) {
            this.mMatchIdPredicate = new View.MatchIdPredicate();
        }
        this.mMatchIdPredicate.mId = id;
        return root.findViewByPredicateInsideOut(start, this.mMatchIdPredicate);
    }

    @NonNull
    public final ArrayList<View> getFocusables(int direction) {
        ArrayList<View> result = new ArrayList();
        this.addFocusables(result, direction);
        return result;
    }

    public final void addFocusables(ArrayList<View> views, int direction) {
        this.addFocusables(views, direction, this.isInTouchMode() ? 1 : 0);
    }

    public void addFocusables(@NonNull ArrayList<View> views, int direction, int focusableMode) {
        if (this.canTakeFocus()) {
            if ((focusableMode & 1) != 1 || this.isFocusableInTouchMode()) {
                views.add(this);
            }
        }
    }

    public void addKeyboardNavigationClusters(@NonNull Collection<View> views, int direction) {
        if (this.isKeyboardNavigationCluster()) {
            if (this.hasFocusable()) {
                views.add(this);
            }
        }
    }

    @NonNull
    public final ArrayList<View> getTouchables() {
        ArrayList<View> result = new ArrayList();
        this.addTouchables(result);
        return result;
    }

    public void addTouchables(@NonNull ArrayList<View> views) {
        int viewFlags = this.mViewFlags;
        if (((viewFlags & 16384) == 16384 || (viewFlags & 2097152) == 2097152 || (viewFlags & 8388608) == 8388608) && (viewFlags & 32) == 0) {
            views.add(this);
        }
    }

    public final boolean requestFocus() {
        return this.requestFocus(130);
    }

    boolean restoreFocusInCluster(int direction) {
        return this.restoreDefaultFocus() ? true : this.requestFocus(direction);
    }

    boolean restoreFocusNotInCluster() {
        return this.requestFocus(130);
    }

    public boolean restoreDefaultFocus() {
        return this.requestFocus(130);
    }

    public final boolean requestFocus(int direction) {
        return this.requestFocus(direction, null);
    }

    public boolean requestFocus(int direction, @Nullable Rect previouslyFocusedRect) {
        return this.requestFocusNoSearch(direction, previouslyFocusedRect);
    }

    private boolean requestFocusNoSearch(int direction, @Nullable Rect previouslyFocusedRect) {
        if (!this.canTakeFocus()) {
            return false;
        } else {
            boolean focusableInTouchMode = this.isFocusableInTouchMode();
            if (this.isInTouchMode() && !focusableInTouchMode) {
                return false;
            } else {
                ViewParent ancestor = this.mParent;
                while (ancestor instanceof ViewGroup vgAncestor) {
                    if (vgAncestor.getDescendantFocusability() == 393216 || !focusableInTouchMode && vgAncestor.shouldBlockFocusForTouchscreen()) {
                        return false;
                    }
                    ancestor = vgAncestor.getParent();
                }
                if (!this.isLayoutValid()) {
                    this.mPrivateFlags |= 1;
                } else {
                    this.clearParentsWantFocus();
                }
                this.handleFocusGainInternal(direction, previouslyFocusedRect);
                return true;
            }
        }
    }

    void clearParentsWantFocus() {
        if (this.mParent instanceof View v) {
            v.mPrivateFlags &= -2;
            v.clearParentsWantFocus();
        }
    }

    void handleFocusGainInternal(int direction, @Nullable Rect previouslyFocusedRect) {
        if ((this.mPrivateFlags & 2) == 0) {
            this.mPrivateFlags |= 2;
            View oldFocus = this.mAttachInfo != null ? this.getRootView().findFocus() : null;
            if (this.mParent != null) {
                this.mParent.requestChildFocus(this, this);
                this.updateFocusedInCluster(oldFocus, direction);
            }
            if (this.mAttachInfo != null) {
                this.mAttachInfo.mTreeObserver.dispatchOnGlobalFocusChange(oldFocus, this);
            }
            this.onFocusChanged(true, direction, previouslyFocusedRect);
            this.refreshDrawableState();
        }
    }

    @Internal
    public final void setFocusedInCluster() {
        this.setFocusedInCluster(this.findKeyboardNavigationCluster());
    }

    private void setFocusedInCluster(View cluster) {
        if (this instanceof ViewGroup) {
            ((ViewGroup) this).mFocusedInCluster = null;
        }
        if (cluster != this) {
            ViewParent parent = this.mParent;
            for (View child = this; parent instanceof ViewGroup; parent = parent.getParent()) {
                ((ViewGroup) parent).mFocusedInCluster = child;
                if (parent == cluster) {
                    break;
                }
                child = (View) parent;
            }
        }
    }

    private void updateFocusedInCluster(View oldFocus, int direction) {
        if (oldFocus != null) {
            View oldCluster = oldFocus.findKeyboardNavigationCluster();
            View cluster = this.findKeyboardNavigationCluster();
            if (oldCluster != cluster) {
                oldFocus.setFocusedInCluster(oldCluster);
                if (!(oldFocus.mParent instanceof ViewGroup)) {
                    return;
                }
                if (direction == 2 || direction == 1) {
                    ((ViewGroup) oldFocus.mParent).clearFocusedInCluster(oldFocus);
                } else if (oldFocus instanceof ViewGroup && ((ViewGroup) oldFocus).getDescendantFocusability() == 262144 && ViewRoot.isViewDescendantOf(this, oldFocus)) {
                    ((ViewGroup) oldFocus.mParent).clearFocusedInCluster(oldFocus);
                }
            }
        }
    }

    public final void setRevealOnFocusHint(boolean revealOnFocus) {
        if (revealOnFocus) {
            this.mPrivateFlags3 &= -67108865;
        } else {
            this.mPrivateFlags3 |= 67108864;
        }
    }

    public final boolean getRevealOnFocusHint() {
        return (this.mPrivateFlags3 & 67108864) == 0;
    }

    public boolean requestRectangleOnScreen(Rect rectangle) {
        return this.requestRectangleOnScreen(rectangle, false);
    }

    public boolean requestRectangleOnScreen(Rect rectangle, boolean immediate) {
        if (this.mParent == null) {
            return false;
        } else {
            View child = this;
            RectF position = this.mAttachInfo != null ? this.mAttachInfo.mTmpTransformRect : new RectF();
            position.set(rectangle);
            ViewParent parent = this.mParent;
            boolean scrolled;
            for (scrolled = false; parent != null; parent = child.getParent()) {
                rectangle.set((int) position.left, (int) position.top, (int) position.right, (int) position.bottom);
                scrolled |= parent.requestChildRectangleOnScreen(child, rectangle, immediate);
                if (!(parent instanceof View)) {
                    break;
                }
                position.offset((float) (child.mLeft - child.getScrollX()), (float) (child.mTop - child.getScrollY()));
                child = (View) parent;
            }
            return scrolled;
        }
    }

    public final boolean isFocusedByDefault() {
        return (this.mPrivateFlags3 & 262144) != 0;
    }

    public void setFocusedByDefault(boolean isFocusedByDefault) {
        if (isFocusedByDefault != ((this.mPrivateFlags3 & 262144) != 0)) {
            if (isFocusedByDefault) {
                this.mPrivateFlags3 |= 262144;
            } else {
                this.mPrivateFlags3 &= -262145;
            }
            if (this.mParent instanceof ViewGroup) {
                if (isFocusedByDefault) {
                    ((ViewGroup) this.mParent).setDefaultFocus(this);
                } else {
                    ((ViewGroup) this.mParent).clearDefaultFocus(this);
                }
            }
        }
    }

    boolean hasDefaultFocus() {
        return this.isFocusedByDefault();
    }

    public View keyboardNavigationClusterSearch(View currentCluster, int direction) {
        if (this.isKeyboardNavigationCluster()) {
            currentCluster = this;
        }
        if (this.isRootNamespace()) {
            return FocusFinder.getInstance().findNextKeyboardNavigationCluster(this, currentCluster, direction);
        } else {
            return this.mParent != null ? this.mParent.keyboardNavigationClusterSearch(currentCluster, direction) : null;
        }
    }

    public void clearFocus() {
        this.clearFocusInternal(null, true, !this.isInTouchMode());
    }

    void clearFocusInternal(View focused, boolean propagate, boolean refocus) {
        if ((this.mPrivateFlags & 2) != 0) {
            this.mPrivateFlags &= -3;
            this.clearParentsWantFocus();
            if (propagate && this.mParent != null) {
                this.mParent.clearChildFocus(this);
            }
            this.onFocusChanged(false, 0, null);
            this.refreshDrawableState();
            if (propagate && (!refocus || !this.rootViewRequestFocus())) {
                this.notifyGlobalFocusCleared(this);
            }
        }
    }

    void notifyGlobalFocusCleared(View oldFocus) {
        if (oldFocus != null && this.mAttachInfo != null) {
            this.mAttachInfo.mTreeObserver.dispatchOnGlobalFocusChange(oldFocus, null);
        }
    }

    boolean rootViewRequestFocus() {
        View root = this.getRootView();
        return root != null && root.requestFocus();
    }

    void unFocus(View focused) {
        this.clearFocusInternal(focused, false, false);
    }

    public boolean hasFocus() {
        return (this.mPrivateFlags & 2) != 0;
    }

    public final boolean hasFocusable() {
        return this.hasFocusable(true, false);
    }

    public final boolean hasExplicitFocusable() {
        return this.hasFocusable(false, true);
    }

    boolean hasFocusable(boolean allowAutoFocus, boolean dispatchExplicit) {
        if (!this.isFocusableInTouchMode()) {
            for (ViewParent p = this.mParent; p instanceof ViewGroup; p = p.getParent()) {
                ViewGroup g = (ViewGroup) p;
                if (g.shouldBlockFocusForTouchscreen()) {
                    return false;
                }
            }
        }
        return (this.mViewFlags & 12) == 0 && (this.mViewFlags & 32) == 0 ? (allowAutoFocus || this.getFocusable() != 16) && this.isFocusable() : false;
    }

    @CallSuper
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        if (!gainFocus) {
            if (this.isPressed()) {
                this.setPressed(false);
            }
            this.resetPressedState();
        }
        this.invalidate();
        View.ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnFocusChangeListener != null) {
            li.mOnFocusChangeListener.onFocusChange(this, gainFocus);
        }
        if (this.mAttachInfo != null) {
            this.mAttachInfo.mKeyDispatchState.reset(this);
        }
    }

    private void resetPressedState() {
        if ((this.mViewFlags & 32) != 32) {
            if (this.isPressed()) {
                this.setPressed(false);
                if (!this.mHasPerformedLongPress) {
                    this.removeLongPressCallback();
                }
            }
        }
    }

    public boolean isFocused() {
        return (this.mPrivateFlags & 2) != 0;
    }

    @Nullable
    public View findFocus() {
        return this.isFocused() ? this : null;
    }

    @Nullable
    public View focusSearch(int direction) {
        return this.mParent != null ? this.mParent.focusSearch(this, direction) : null;
    }

    public final boolean isKeyboardNavigationCluster() {
        return (this.mPrivateFlags3 & 32768) != 0;
    }

    @Nullable
    View findKeyboardNavigationCluster() {
        if (this.mParent instanceof View) {
            View cluster = ((View) this.mParent).findKeyboardNavigationCluster();
            if (cluster != null) {
                return cluster;
            }
            if (this.isKeyboardNavigationCluster()) {
                return this;
            }
        }
        return null;
    }

    public void setKeyboardNavigationCluster(boolean isCluster) {
        if (isCluster) {
            this.mPrivateFlags3 |= 32768;
        } else {
            this.mPrivateFlags3 &= -32769;
        }
    }

    public int getNextFocusLeftId() {
        return this.mNextFocusLeftId;
    }

    public void setNextFocusLeftId(int nextFocusLeftId) {
        this.mNextFocusLeftId = nextFocusLeftId;
    }

    public int getNextFocusRightId() {
        return this.mNextFocusRightId;
    }

    public void setNextFocusRightId(int nextFocusRightId) {
        this.mNextFocusRightId = nextFocusRightId;
    }

    public int getNextFocusUpId() {
        return this.mNextFocusUpId;
    }

    public void setNextFocusUpId(int nextFocusUpId) {
        this.mNextFocusUpId = nextFocusUpId;
    }

    public int getNextFocusDownId() {
        return this.mNextFocusDownId;
    }

    public void setNextFocusDownId(int nextFocusDownId) {
        this.mNextFocusDownId = nextFocusDownId;
    }

    public int getNextFocusForwardId() {
        return this.mNextFocusForwardId;
    }

    public void setNextFocusForwardId(int nextFocusForwardId) {
        this.mNextFocusForwardId = nextFocusForwardId;
    }

    public int getNextClusterForwardId() {
        return this.mNextClusterForwardId;
    }

    public void setNextClusterForwardId(int nextClusterForwardId) {
        this.mNextClusterForwardId = nextClusterForwardId;
    }

    public boolean isShown() {
        current = this;
        while ((current.mViewFlags & 12) == 0) {
            ViewParent parent = current.mParent;
            if (parent == null) {
                return false;
            }
            if (!(parent instanceof View current)) {
                return true;
            }
            if (current == null) {
                return false;
            }
        }
        return false;
    }

    public void setHorizontalScrollBarEnabled(boolean enabled) {
        if (this.isHorizontalScrollBarEnabled() != enabled) {
            this.mViewFlags ^= 256;
            this.resolvePadding();
        }
    }

    public void setVerticalScrollBarEnabled(boolean enabled) {
        if (this.isVerticalScrollBarEnabled() != enabled) {
            this.mViewFlags ^= 512;
            this.resolvePadding();
        }
    }

    public boolean isHorizontalScrollBarEnabled() {
        return (this.mViewFlags & 256) != 0;
    }

    public boolean isVerticalScrollBarEnabled() {
        return (this.mViewFlags & 512) != 0;
    }

    private void initializeScrollIndicatorsInternal() {
        if (this.mScrollIndicatorDrawable == null) {
            this.mScrollIndicatorDrawable = new Drawable() {

                @Override
                public void draw(@NonNull Canvas canvas) {
                    Paint paint = Paint.obtain();
                    paint.setRGBA(0, 0, 0, 31);
                    canvas.drawRect(this.getBounds(), paint);
                    paint.recycle();
                }
            };
        }
    }

    private void initScrollCache() {
        if (this.mScrollCache == null) {
            this.mScrollCache = new ScrollCache(this);
        }
    }

    private void initializeScrollBarDrawable() {
        this.initScrollCache();
        if (this.mScrollCache.mScrollBar == null) {
            this.mScrollCache.mScrollBar = new ScrollBar();
            this.mScrollCache.mScrollBar.setState(this.getDrawableState());
            this.mScrollCache.mScrollBar.setCallback(this);
        }
    }

    public void setScrollbarFadingEnabled(boolean fadeScrollbars) {
        this.initScrollCache();
        ScrollCache scrollCache = this.mScrollCache;
        scrollCache.mFadeScrollBars = fadeScrollbars;
        if (fadeScrollbars) {
            scrollCache.mState = 0;
        } else {
            scrollCache.mState = 1;
        }
    }

    public boolean isScrollbarFadingEnabled() {
        return this.mScrollCache != null && this.mScrollCache.mFadeScrollBars;
    }

    private ScrollCache getScrollCache() {
        this.initScrollCache();
        return this.mScrollCache;
    }

    public int getScrollBarDefaultDelayBeforeFade() {
        return this.mScrollCache == null ? ViewConfiguration.getScrollDefaultDelay() : this.mScrollCache.mDefaultDelayBeforeFade;
    }

    public void setScrollBarDefaultDelayBeforeFade(int scrollBarDefaultDelayBeforeFade) {
        this.getScrollCache().mDefaultDelayBeforeFade = scrollBarDefaultDelayBeforeFade;
    }

    public int getScrollBarFadeDuration() {
        return this.mScrollCache == null ? ViewConfiguration.getScrollBarFadeDuration() : this.mScrollCache.mFadeDuration;
    }

    public void setScrollBarFadeDuration(int scrollBarFadeDuration) {
        this.getScrollCache().mFadeDuration = scrollBarFadeDuration;
    }

    public int getScrollBarSize() {
        return this.mScrollCache == null ? ViewConfiguration.get(this.mContext).getScaledScrollbarSize() : this.mScrollCache.mScrollBarSize;
    }

    public void setScrollBarSize(int scrollBarSize) {
        this.getScrollCache().mScrollBarSize = scrollBarSize;
    }

    public void setScrollBarStyle(int style) {
        if (style != (this.mViewFlags & 50331648)) {
            this.mViewFlags = this.mViewFlags & -50331649 | style & 50331648;
            this.resolvePadding();
        }
    }

    public int getScrollBarStyle() {
        return this.mViewFlags & 50331648;
    }

    public void setVerticalScrollbarThumbDrawable(@Nullable Drawable drawable) {
        this.initializeScrollBarDrawable();
        this.mScrollCache.mScrollBar.setVerticalThumbDrawable(drawable);
    }

    public void setVerticalScrollbarTrackDrawable(@Nullable Drawable drawable) {
        this.initializeScrollBarDrawable();
        this.mScrollCache.mScrollBar.setVerticalTrackDrawable(drawable);
    }

    public void setHorizontalScrollbarThumbDrawable(@Nullable Drawable drawable) {
        this.initializeScrollBarDrawable();
        this.mScrollCache.mScrollBar.setHorizontalThumbDrawable(drawable);
    }

    public void setHorizontalScrollbarTrackDrawable(@Nullable Drawable drawable) {
        this.initializeScrollBarDrawable();
        this.mScrollCache.mScrollBar.setHorizontalTrackDrawable(drawable);
    }

    @Nullable
    public Drawable getVerticalScrollbarThumbDrawable() {
        return this.mScrollCache != null && this.mScrollCache.mScrollBar != null ? this.mScrollCache.mScrollBar.getVerticalThumbDrawable() : null;
    }

    @Nullable
    public Drawable getVerticalScrollbarTrackDrawable() {
        return this.mScrollCache != null && this.mScrollCache.mScrollBar != null ? this.mScrollCache.mScrollBar.getVerticalTrackDrawable() : null;
    }

    @Nullable
    public Drawable getHorizontalScrollbarThumbDrawable() {
        return this.mScrollCache != null && this.mScrollCache.mScrollBar != null ? this.mScrollCache.mScrollBar.getHorizontalThumbDrawable() : null;
    }

    @Nullable
    public Drawable getHorizontalScrollbarTrackDrawable() {
        return this.mScrollCache != null && this.mScrollCache.mScrollBar != null ? this.mScrollCache.mScrollBar.getHorizontalTrackDrawable() : null;
    }

    public void scrollTo(int x, int y) {
        if (this.mScrollX != x || this.mScrollY != y) {
            int oldX = this.mScrollX;
            int oldY = this.mScrollY;
            this.mScrollX = x;
            this.mScrollY = y;
            this.onScrollChanged(this.mScrollX, this.mScrollY, oldX, oldY);
            if (!this.awakenScrollBars()) {
                this.postInvalidateOnAnimation();
            }
        }
    }

    public void scrollBy(int x, int y) {
        this.scrollTo(this.mScrollX + x, this.mScrollY + y);
    }

    protected final boolean awakenScrollBars() {
        return this.mScrollCache != null && this.awakenScrollBars(this.mScrollCache.mDefaultDelayBeforeFade);
    }

    private void initialAwakenScrollBars() {
        if (this.mScrollCache != null) {
            this.awakenScrollBars(this.mScrollCache.mDefaultDelayBeforeFade * 4);
        }
    }

    protected boolean awakenScrollBars(int startDelay) {
        ScrollCache scrollCache = this.mScrollCache;
        if (scrollCache != null && scrollCache.mFadeScrollBars) {
            this.initializeScrollBarDrawable();
            if (!this.isHorizontalScrollBarEnabled() && !this.isVerticalScrollBarEnabled()) {
                return false;
            } else {
                this.postInvalidateOnAnimation();
                if (scrollCache.mState == 0) {
                    startDelay = Math.max(1500, startDelay);
                } else {
                    startDelay = Math.max(0, startDelay);
                }
                long fadeStartTime = AnimationUtils.currentAnimationTimeMillis() + (long) startDelay;
                scrollCache.mFadeStartTime = fadeStartTime;
                scrollCache.mState = 1;
                if (this.mAttachInfo != null) {
                    this.mAttachInfo.mHandler.removeCallbacks(scrollCache);
                    this.mAttachInfo.mHandler.postAtTime(scrollCache, fadeStartTime);
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public void setScrollX(int value) {
        this.scrollTo(value, this.mScrollY);
    }

    public void setScrollY(int value) {
        this.scrollTo(this.mScrollX, value);
    }

    public final int getScrollX() {
        return this.mScrollX;
    }

    public final int getScrollY() {
        return this.mScrollY;
    }

    protected int computeHorizontalScrollRange() {
        return this.getWidth();
    }

    protected int computeHorizontalScrollOffset() {
        return this.mScrollX;
    }

    protected int computeHorizontalScrollExtent() {
        return this.getWidth();
    }

    protected int computeVerticalScrollRange() {
        return this.getHeight();
    }

    protected int computeVerticalScrollOffset() {
        return this.mScrollY;
    }

    protected int computeVerticalScrollExtent() {
        return this.getHeight();
    }

    public boolean canScrollHorizontally(int direction) {
        int offset = this.computeHorizontalScrollOffset();
        int range = this.computeHorizontalScrollRange() - this.computeHorizontalScrollExtent();
        if (range == 0) {
            return false;
        } else {
            return direction < 0 ? offset > 0 : offset < range - 1;
        }
    }

    public boolean canScrollVertically(int direction) {
        int offset = this.computeVerticalScrollOffset();
        int range = this.computeVerticalScrollRange() - this.computeVerticalScrollExtent();
        if (range == 0) {
            return false;
        } else {
            return direction < 0 ? offset > 0 : offset < range - 1;
        }
    }

    void getScrollIndicatorBounds(@NonNull Rect out) {
        out.left = this.mScrollX;
        out.right = this.mScrollX + this.mRight - this.mLeft;
        out.top = this.mScrollY;
        out.bottom = this.mScrollY + this.mBottom - this.mTop;
    }

    private void getHorizontalScrollBarBounds(@Nullable Rect drawBounds, @Nullable Rect touchBounds) {
        Rect bounds = drawBounds != null ? drawBounds : touchBounds;
        if (bounds != null) {
            int inside = (this.mViewFlags & 33554432) == 0 ? -1 : 0;
            boolean drawVerticalScrollBar = this.isVerticalScrollBarEnabled() && !this.isVerticalScrollBarHidden();
            int size = this.getHorizontalScrollbarHeight();
            int verticalScrollBarGap = drawVerticalScrollBar ? this.getVerticalScrollbarWidth() : 0;
            int width = this.mRight - this.mLeft;
            int height = this.mBottom - this.mTop;
            bounds.top = this.mScrollY + height - size - (this.mUserPaddingBottom & inside);
            bounds.left = this.mScrollX + (this.mPaddingLeft & inside);
            bounds.right = this.mScrollX + width - (this.mUserPaddingRight & inside) - verticalScrollBarGap;
            bounds.bottom = bounds.top + size;
            if (touchBounds != null) {
                if (touchBounds != bounds) {
                    touchBounds.set(bounds);
                }
                int minTouchTarget = this.mScrollCache.mScrollBarMinTouchTarget;
                if (touchBounds.height() < minTouchTarget) {
                    int adjust = (minTouchTarget - touchBounds.height()) / 2;
                    touchBounds.bottom = Math.min(touchBounds.bottom + adjust, this.mScrollY + height);
                    touchBounds.top = touchBounds.bottom - minTouchTarget;
                }
                if (touchBounds.width() < minTouchTarget) {
                    int adjust = (minTouchTarget - touchBounds.width()) / 2;
                    touchBounds.left -= adjust;
                    touchBounds.right = touchBounds.left + minTouchTarget;
                }
            }
        }
    }

    private void getVerticalScrollBarBounds(@Nullable Rect drawBounds, @Nullable Rect touchBounds) {
        Rect bounds = drawBounds != null ? drawBounds : touchBounds;
        if (bounds != null) {
            int inside = (this.mViewFlags & 33554432) == 0 ? -1 : 0;
            int size = this.getVerticalScrollbarWidth();
            boolean layoutRtl = this.isLayoutRtl();
            int width = this.mRight - this.mLeft;
            int height = this.mBottom - this.mTop;
            if (layoutRtl) {
                bounds.left = this.mScrollX + (this.mUserPaddingLeft & inside);
            } else {
                bounds.left = this.mScrollX + width - size - (this.mUserPaddingRight & inside);
            }
            bounds.top = this.mScrollY + (this.mPaddingTop & inside);
            bounds.right = bounds.left + size;
            bounds.bottom = this.mScrollY + height - (this.mUserPaddingBottom & inside);
            if (touchBounds != null) {
                if (touchBounds != bounds) {
                    touchBounds.set(bounds);
                }
                int minTouchTarget = this.mScrollCache.mScrollBarMinTouchTarget;
                if (touchBounds.width() < minTouchTarget) {
                    int adjust = (minTouchTarget - touchBounds.width()) / 2;
                    if (layoutRtl) {
                        touchBounds.left = Math.max(touchBounds.left + adjust, this.mScrollX);
                        touchBounds.right = touchBounds.left + minTouchTarget;
                    } else {
                        touchBounds.right = Math.min(touchBounds.right + adjust, this.mScrollX + width);
                        touchBounds.left = touchBounds.right - minTouchTarget;
                    }
                }
                if (touchBounds.height() < minTouchTarget) {
                    int adjust = (minTouchTarget - touchBounds.height()) / 2;
                    touchBounds.top -= adjust;
                    touchBounds.bottom = touchBounds.top + minTouchTarget;
                }
            }
        }
    }

    @Internal
    protected boolean isVerticalScrollBarHidden() {
        return false;
    }

    boolean isOnScrollbar(float x, float y) {
        if (this.mScrollCache == null) {
            return false;
        } else {
            x += (float) this.getScrollX();
            y += (float) this.getScrollY();
            boolean canScrollVertically = this.computeVerticalScrollRange() > this.computeVerticalScrollExtent();
            if (this.isVerticalScrollBarEnabled() && !this.isVerticalScrollBarHidden() && canScrollVertically) {
                Rect touchBounds = this.mScrollCache.mScrollBarTouchBounds;
                this.getVerticalScrollBarBounds(null, touchBounds);
                if (touchBounds.contains((int) x, (int) y)) {
                    return true;
                }
            }
            boolean canScrollHorizontally = this.computeHorizontalScrollRange() > this.computeHorizontalScrollExtent();
            if (this.isHorizontalScrollBarEnabled() && canScrollHorizontally) {
                Rect touchBounds = this.mScrollCache.mScrollBarTouchBounds;
                this.getHorizontalScrollBarBounds(null, touchBounds);
                return touchBounds.contains((int) x, (int) y);
            } else {
                return false;
            }
        }
    }

    boolean isOnScrollbarThumb(float x, float y) {
        return this.isOnVerticalScrollbarThumb(x, y) || this.isOnHorizontalScrollbarThumb(x, y);
    }

    private boolean isOnVerticalScrollbarThumb(float x, float y) {
        if (this.mScrollCache != null && this.isVerticalScrollBarEnabled() && !this.isVerticalScrollBarHidden()) {
            int range = this.computeVerticalScrollRange();
            int extent = this.computeVerticalScrollExtent();
            if (range <= extent) {
                return false;
            } else {
                x += (float) this.getScrollX();
                y += (float) this.getScrollY();
                Rect bounds = this.mScrollCache.mScrollBarBounds;
                Rect touchBounds = this.mScrollCache.mScrollBarTouchBounds;
                this.getVerticalScrollBarBounds(bounds, touchBounds);
                int offset = this.computeVerticalScrollOffset();
                int thumbLength = ScrollCache.getThumbLength(bounds.height(), bounds.width(), extent, range);
                int thumbOffset = ScrollCache.getThumbOffset(bounds.height(), thumbLength, extent, range, offset);
                int thumbTop = bounds.top + thumbOffset;
                int adjust = Math.max(this.mScrollCache.mScrollBarMinTouchTarget - thumbLength, 0) / 2;
                return x >= (float) touchBounds.left && x <= (float) touchBounds.right && y >= (float) (thumbTop - adjust) && y <= (float) (thumbTop + thumbLength + adjust);
            }
        } else {
            return false;
        }
    }

    private boolean isOnHorizontalScrollbarThumb(float x, float y) {
        if (this.mScrollCache != null && this.isHorizontalScrollBarEnabled()) {
            int range = this.computeHorizontalScrollRange();
            int extent = this.computeHorizontalScrollExtent();
            if (range <= extent) {
                return false;
            } else {
                x += (float) this.getScrollX();
                y += (float) this.getScrollY();
                Rect bounds = this.mScrollCache.mScrollBarBounds;
                Rect touchBounds = this.mScrollCache.mScrollBarTouchBounds;
                this.getHorizontalScrollBarBounds(bounds, touchBounds);
                int offset = this.computeHorizontalScrollOffset();
                int thumbLength = ScrollCache.getThumbLength(bounds.width(), bounds.height(), extent, range);
                int thumbOffset = ScrollCache.getThumbOffset(bounds.width(), thumbLength, extent, range, offset);
                int thumbLeft = bounds.left + thumbOffset;
                int adjust = Math.max(this.mScrollCache.mScrollBarMinTouchTarget - thumbLength, 0) / 2;
                return x >= (float) (thumbLeft - adjust) && x <= (float) (thumbLeft + thumbLength + adjust) && y >= (float) touchBounds.top && y <= (float) touchBounds.bottom;
            }
        } else {
            return false;
        }
    }

    boolean isDraggingScrollBar() {
        return this.mScrollCache != null && this.mScrollCache.mScrollBarDraggingState != 0;
    }

    public void setScrollIndicators(int indicators) {
        this.setScrollIndicators(indicators, 63);
    }

    public void setScrollIndicators(int indicators, int mask) {
        mask <<= 8;
        mask &= 16128;
        indicators <<= 8;
        indicators &= mask;
        int updatedFlags = indicators | this.mPrivateFlags3 & ~mask;
        if (this.mPrivateFlags3 != updatedFlags) {
            this.mPrivateFlags3 = updatedFlags;
            if (indicators != 0) {
                this.initializeScrollIndicatorsInternal();
            }
            this.invalidate();
        }
    }

    public int getScrollIndicators() {
        return (this.mPrivateFlags3 & 16128) >>> 8;
    }

    public int getVerticalScrollbarWidth() {
        ScrollCache cache = this.mScrollCache;
        if (cache != null) {
            ScrollBar scrollBar = cache.mScrollBar;
            if (scrollBar != null) {
                int size = scrollBar.getSize(true);
                if (size <= 0) {
                    size = cache.mScrollBarSize;
                }
                return size;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    protected int getHorizontalScrollbarHeight() {
        ScrollCache cache = this.mScrollCache;
        if (cache != null) {
            ScrollBar scrollBar = cache.mScrollBar;
            if (scrollBar != null) {
                int size = scrollBar.getSize(false);
                if (size <= 0) {
                    size = cache.mScrollBarSize;
                }
                return size;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public int getVerticalFadingEdgeLength() {
        if (this.isVerticalFadingEdgeEnabled()) {
            ScrollCache cache = this.mScrollCache;
            if (cache != null) {
                return cache.fadingEdgeLength;
            }
        }
        return 0;
    }

    public void computeScroll() {
    }

    public boolean isHorizontalFadingEdgeEnabled() {
        return (this.mViewFlags & 4096) == 4096;
    }

    public void setHorizontalFadingEdgeEnabled(boolean horizontalFadingEdgeEnabled) {
        if (this.isHorizontalFadingEdgeEnabled() != horizontalFadingEdgeEnabled) {
            if (horizontalFadingEdgeEnabled) {
                this.initScrollCache();
            }
            this.mViewFlags ^= 4096;
        }
    }

    public boolean isVerticalFadingEdgeEnabled() {
        return (this.mViewFlags & 8192) == 8192;
    }

    public void setVerticalFadingEdgeEnabled(boolean verticalFadingEdgeEnabled) {
        if (this.isVerticalFadingEdgeEnabled() != verticalFadingEdgeEnabled) {
            if (verticalFadingEdgeEnabled) {
                this.initScrollCache();
            }
            this.mViewFlags ^= 8192;
        }
    }

    @Internal
    public int getFadingEdge() {
        return this.mViewFlags & 12288;
    }

    @Internal
    public int getFadingEdgeLength() {
        return this.mScrollCache != null && (this.mViewFlags & 12288) != 0 ? this.mScrollCache.fadingEdgeLength : 0;
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.mBackgroundSizeChanged = true;
        this.mDefaultFocusHighlightSizeChanged = true;
        if (this.mForegroundInfo != null) {
            this.mForegroundInfo.mBoundsChanged = true;
        }
        AttachInfo ai = this.mAttachInfo;
        if (ai != null) {
            ai.mViewScrollChanged = true;
        }
        if (this.mListenerInfo != null && this.mListenerInfo.mOnScrollChangeListener != null) {
            this.mListenerInfo.mOnScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }

    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        int overScrollMode = this.mOverScrollMode;
        boolean canScrollHorizontal = this.computeHorizontalScrollRange() > this.computeHorizontalScrollExtent();
        boolean canScrollVertical = this.computeVerticalScrollRange() > this.computeVerticalScrollExtent();
        boolean overScrollHorizontal = overScrollMode == 0 || overScrollMode == 1 && canScrollHorizontal;
        boolean overScrollVertical = overScrollMode == 0 || overScrollMode == 1 && canScrollVertical;
        int newScrollX = scrollX + deltaX;
        if (!overScrollHorizontal) {
            maxOverScrollX = 0;
        }
        int newScrollY = scrollY + deltaY;
        if (!overScrollVertical) {
            maxOverScrollY = 0;
        }
        int left = -maxOverScrollX;
        int right = maxOverScrollX + scrollRangeX;
        int top = -maxOverScrollY;
        int bottom = maxOverScrollY + scrollRangeY;
        boolean clampedX = false;
        if (newScrollX > right) {
            newScrollX = right;
            clampedX = true;
        } else if (newScrollX < left) {
            newScrollX = left;
            clampedX = true;
        }
        boolean clampedY = false;
        if (newScrollY > bottom) {
            newScrollY = bottom;
            clampedY = true;
        } else if (newScrollY < top) {
            newScrollY = top;
            clampedY = true;
        }
        this.onOverScrolled(newScrollX, newScrollY, clampedX, clampedY);
        return clampedX || clampedY;
    }

    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
    }

    public int getOverScrollMode() {
        return this.mOverScrollMode;
    }

    public void setOverScrollMode(int overScrollMode) {
        if (overScrollMode != 0 && overScrollMode != 1 && overScrollMode != 2) {
            throw new IllegalArgumentException("Invalid overscroll mode " + overScrollMode);
        } else {
            this.mOverScrollMode = overScrollMode;
        }
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        if (enabled) {
            this.mPrivateFlags3 |= 128;
        } else if (this.isNestedScrollingEnabled()) {
            this.stopNestedScroll(0);
            this.mPrivateFlags3 &= -129;
        }
    }

    public boolean isNestedScrollingEnabled() {
        return (this.mPrivateFlags3 & 128) == 128;
    }

    public boolean startNestedScroll(int axes, int type) {
        if (this.hasNestedScrollingParent(type)) {
            return true;
        } else {
            if (this.isNestedScrollingEnabled()) {
                ViewParent p = this.getParent();
                for (child = this; p != null; p = p.getParent()) {
                    if (p.onStartNestedScroll(child, this, axes, type)) {
                        if (type == 1) {
                            this.mNestedScrollingParentNonTouch = p;
                        } else {
                            this.mNestedScrollingParentTouch = p;
                        }
                        p.onNestedScrollAccepted(child, this, axes, type);
                        return true;
                    }
                    if (p instanceof View child) {
                        ;
                    }
                }
            }
            return false;
        }
    }

    public void stopNestedScroll(int type) {
        if (type == 1) {
            if (this.mNestedScrollingParentNonTouch != null) {
                this.mNestedScrollingParentNonTouch.onStopNestedScroll(this, type);
                this.mNestedScrollingParentNonTouch = null;
            }
        } else if (this.mNestedScrollingParentTouch != null) {
            this.mNestedScrollingParentTouch.onStopNestedScroll(this, type);
            this.mNestedScrollingParentTouch = null;
        }
    }

    public boolean hasNestedScrollingParent(int type) {
        return type == 1 ? this.mNestedScrollingParentNonTouch != null : this.mNestedScrollingParentTouch != null;
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type, @NonNull int[] consumed) {
        if (this.isNestedScrollingEnabled()) {
            ViewParent parent = type == 1 ? this.mNestedScrollingParentNonTouch : this.mNestedScrollingParentTouch;
            if (parent == null) {
                return false;
            }
            if (dxConsumed != 0 || dyConsumed != 0 || dxUnconsumed != 0 || dyUnconsumed != 0) {
                int startX = 0;
                int startY = 0;
                if (offsetInWindow != null) {
                    this.getLocationInWindow(offsetInWindow);
                    startX = offsetInWindow[0];
                    startY = offsetInWindow[1];
                }
                parent.onNestedScroll(this, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
                if (offsetInWindow != null) {
                    this.getLocationInWindow(offsetInWindow);
                    offsetInWindow[0] -= startX;
                    offsetInWindow[1] -= startY;
                }
                return true;
            }
            if (offsetInWindow != null) {
                offsetInWindow[0] = 0;
                offsetInWindow[1] = 0;
            }
        }
        return false;
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        if (this.isNestedScrollingEnabled()) {
            ViewParent parent = type == 1 ? this.mNestedScrollingParentNonTouch : this.mNestedScrollingParentTouch;
            if (parent == null) {
                return false;
            }
            if (dx != 0 || dy != 0) {
                int startX = 0;
                int startY = 0;
                if (offsetInWindow != null) {
                    this.getLocationInWindow(offsetInWindow);
                    startX = offsetInWindow[0];
                    startY = offsetInWindow[1];
                }
                if (consumed == null) {
                    if (this.mTempNestedScrollConsumed == null) {
                        this.mTempNestedScrollConsumed = new int[2];
                    }
                    consumed = this.mTempNestedScrollConsumed;
                }
                consumed[0] = 0;
                consumed[1] = 0;
                parent.onNestedPreScroll(this, dx, dy, consumed, type);
                if (offsetInWindow != null) {
                    this.getLocationInWindow(offsetInWindow);
                    offsetInWindow[0] -= startX;
                    offsetInWindow[1] -= startY;
                }
                return consumed[0] != 0 || consumed[1] != 0;
            }
            if (offsetInWindow != null) {
                offsetInWindow[0] = 0;
                offsetInWindow[1] = 0;
            }
        }
        return false;
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return this.isNestedScrollingEnabled() && this.mNestedScrollingParentTouch != null ? this.mNestedScrollingParentTouch.onNestedFling(this, velocityX, velocityY, consumed) : false;
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return this.isNestedScrollingEnabled() && this.mNestedScrollingParentTouch != null ? this.mNestedScrollingParentTouch.onNestedPreFling(this, velocityX, velocityY) : false;
    }

    public final int getWidth() {
        return this.mRight - this.mLeft;
    }

    public final int getHeight() {
        return this.mBottom - this.mTop;
    }

    public void getDrawingRect(@NonNull Rect outRect) {
        outRect.left = this.mScrollX;
        outRect.top = this.mScrollY;
        outRect.right = this.mScrollX + (this.mRight - this.mLeft);
        outRect.bottom = this.mScrollY + (this.mBottom - this.mTop);
    }

    public final int getLeft() {
        return this.mLeft;
    }

    public final void setLeft(int left) {
        if (left != this.mLeft) {
            this.invalidate();
            int oldWidth = this.mRight - this.mLeft;
            int height = this.mBottom - this.mTop;
            this.mLeft = left;
            this.mRenderNode.setLeft(left);
            this.sizeChange(this.mRight - this.mLeft, height, oldWidth, height);
            this.mBackgroundSizeChanged = true;
            this.mDefaultFocusHighlightSizeChanged = true;
            if (this.mForegroundInfo != null) {
                this.mForegroundInfo.mBoundsChanged = true;
            }
        }
    }

    public final int getTop() {
        return this.mTop;
    }

    public final void setTop(int top) {
        if (top != this.mTop) {
            this.invalidate();
            int width = this.mRight - this.mLeft;
            int oldHeight = this.mBottom - this.mTop;
            this.mTop = top;
            this.mRenderNode.setTop(this.mTop);
            this.sizeChange(width, this.mBottom - this.mTop, width, oldHeight);
            this.mBackgroundSizeChanged = true;
            this.mDefaultFocusHighlightSizeChanged = true;
            if (this.mForegroundInfo != null) {
                this.mForegroundInfo.mBoundsChanged = true;
            }
        }
    }

    public final int getRight() {
        return this.mRight;
    }

    public final void setRight(int right) {
        if (right != this.mRight) {
            this.invalidate();
            int oldWidth = this.mRight - this.mLeft;
            int height = this.mBottom - this.mTop;
            this.mRight = right;
            this.mRenderNode.setRight(this.mRight);
            this.sizeChange(this.mRight - this.mLeft, height, oldWidth, height);
            this.mBackgroundSizeChanged = true;
            this.mDefaultFocusHighlightSizeChanged = true;
            if (this.mForegroundInfo != null) {
                this.mForegroundInfo.mBoundsChanged = true;
            }
        }
    }

    public final int getBottom() {
        return this.mBottom;
    }

    public final void setBottom(int bottom) {
        if (bottom != this.mBottom) {
            this.invalidate();
            int width = this.mRight - this.mLeft;
            int oldHeight = this.mBottom - this.mTop;
            this.mBottom = bottom;
            this.mRenderNode.setBottom(this.mBottom);
            this.sizeChange(width, this.mBottom - this.mTop, width, oldHeight);
            this.mBackgroundSizeChanged = true;
            this.mDefaultFocusHighlightSizeChanged = true;
            if (this.mForegroundInfo != null) {
                this.mForegroundInfo.mBoundsChanged = true;
            }
        }
    }

    private void sizeChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        if (this.isLayoutValid() && (!(this.mParent instanceof ViewGroup) || !((ViewGroup) this.mParent).isLayoutSuppressed())) {
            if (newWidth > 0 && newHeight > 0) {
                if ((oldWidth <= 0 || oldHeight <= 0) && this.mParent != null && this.canTakeFocus()) {
                    this.mParent.focusableViewAvailable(this);
                }
            } else if (this.hasFocus()) {
                this.clearFocus();
            }
        }
    }

    public final Matrix getMatrix() {
        return this.mRenderNode.getMatrix();
    }

    public final Matrix getInverseMatrix() {
        return this.mRenderNode.getInverseMatrix();
    }

    public final boolean hasIdentityMatrix() {
        Matrix matrix = this.mRenderNode.getMatrix();
        return matrix == null || matrix.isIdentity();
    }

    public float getX() {
        return (float) this.mLeft + this.getTranslationX();
    }

    public void setX(float x) {
        this.setTranslationX(x - (float) this.mLeft);
    }

    public float getY() {
        return (float) this.mTop + this.getTranslationY();
    }

    public void setY(float y) {
        this.setTranslationY(y - (float) this.mTop);
    }

    public float getZ() {
        return this.getElevation() + this.getTranslationZ();
    }

    public void setZ(float z) {
        this.setTranslationZ(z - this.getElevation());
    }

    public float getElevation() {
        return this.mRenderNode.getElevation();
    }

    public void setElevation(float elevation) {
        if (this.mRenderNode.setElevation(elevation)) {
            this.invalidate();
        }
    }

    public float getTranslationX() {
        return this.mRenderNode.getTranslationX();
    }

    public void setTranslationX(float translationX) {
        if (this.mRenderNode.setTranslationX(translationX)) {
            this.invalidate();
        }
    }

    public float getTranslationY() {
        return this.mRenderNode.getTranslationY();
    }

    public void setTranslationY(float translationY) {
        if (this.mRenderNode.setTranslationY(translationY)) {
            this.invalidate();
        }
    }

    public float getTranslationZ() {
        return this.mRenderNode.getTranslationZ();
    }

    public void setTranslationZ(float translationZ) {
        if (this.mRenderNode.setTranslationZ(translationZ)) {
            this.invalidate();
        }
    }

    public float getRotation() {
        return this.mRenderNode.getRotationZ();
    }

    public void setRotation(float rotation) {
        if (this.mRenderNode.setRotationZ(rotation)) {
            this.invalidate();
        }
    }

    public float getRotationY() {
        return this.mRenderNode.getRotationY();
    }

    public void setRotationY(float rotationY) {
        if (this.mRenderNode.setRotationY(rotationY)) {
            this.invalidate();
        }
    }

    public float getRotationX() {
        return this.mRenderNode.getRotationX();
    }

    public void setRotationX(float rotationX) {
        if (this.mRenderNode.setRotationX(rotationX)) {
            this.invalidate();
        }
    }

    public float getScaleX() {
        return this.mRenderNode.getScaleX();
    }

    public void setScaleX(float scaleX) {
        if (this.mRenderNode.setScaleX(scaleX)) {
            this.invalidate();
        }
    }

    public float getScaleY() {
        return this.mRenderNode.getScaleY();
    }

    public void setScaleY(float scaleY) {
        if (this.mRenderNode.setScaleY(scaleY)) {
            this.invalidate();
        }
    }

    public float getPivotX() {
        return this.mRenderNode.getPivotX();
    }

    public void setPivotX(float pivotX) {
        if (this.mRenderNode.setPivotX(pivotX)) {
            this.invalidate();
        }
    }

    public float getPivotY() {
        return this.mRenderNode.getPivotY();
    }

    public void setPivotY(float pivotY) {
        if (this.mRenderNode.setPivotY(pivotY)) {
            this.invalidate();
        }
    }

    public boolean isPivotSet() {
        return this.mRenderNode.isPivotExplicitlySet();
    }

    public void resetPivot() {
        if (this.mRenderNode.resetPivot()) {
            this.invalidate();
        }
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    public void forceHasOverlappingRendering(boolean hasOverlappingRendering) {
        this.mPrivateFlags3 |= 16777216;
        if (hasOverlappingRendering) {
            this.mPrivateFlags3 |= 8388608;
        } else {
            this.mPrivateFlags3 &= -8388609;
        }
    }

    public final boolean getHasOverlappingRendering() {
        return (this.mPrivateFlags3 & 16777216) != 0 ? (this.mPrivateFlags3 & 8388608) != 0 : this.hasOverlappingRendering();
    }

    public boolean hasOverlappingRendering() {
        return true;
    }

    public void setAlpha(float alpha) {
        if (this.mAlpha != alpha) {
            this.mAlpha = alpha;
            this.invalidate();
        }
    }

    public final void setTransitionAlpha(float alpha) {
        if (this.mTransitionAlpha != alpha) {
            this.mTransitionAlpha = alpha;
            this.invalidate();
        }
    }

    public final float getTransitionAlpha() {
        return this.mTransitionAlpha;
    }

    public final void setAnimationMatrix(@Nullable Matrix matrix) {
        this.mRenderNode.setAnimationMatrix(matrix);
    }

    @Nullable
    public final Matrix getAnimationMatrix() {
        return this.mRenderNode.getAnimationMatrix();
    }

    public StateListAnimator getStateListAnimator() {
        return this.mStateListAnimator;
    }

    public void setStateListAnimator(@Nullable StateListAnimator stateListAnimator) {
        if (this.mStateListAnimator != stateListAnimator) {
            if (this.mStateListAnimator != null) {
                this.mStateListAnimator.setTarget(null);
            }
            this.mStateListAnimator = stateListAnimator;
            if (stateListAnimator != null) {
                stateListAnimator.setTarget(this);
                if (this.isAttachedToWindow()) {
                    stateListAnimator.setState(this.getDrawableState());
                }
            }
        }
    }

    int combineVisibility(int vis1, int vis2) {
        return Math.max(vis1, vis2);
    }

    void dispatchAttachedToWindow(AttachInfo info, int visibility) {
        this.mAttachInfo = info;
        this.mWindowAttachCount++;
        this.mPrivateFlags |= 1024;
        if (this.mFloatingTreeObserver != null) {
            info.mTreeObserver.merge(this.mFloatingTreeObserver);
            this.mFloatingTreeObserver = null;
        }
        if (this.mRunQueue != null) {
            this.mRunQueue.executeActions(info.mHandler);
            this.mRunQueue = null;
        }
        this.onAttachedToWindow();
        View.ListenerInfo li = this.mListenerInfo;
        CopyOnWriteArrayList<View.OnAttachStateChangeListener> listeners = li != null ? li.mOnAttachStateChangeListeners : null;
        if (listeners != null && listeners.size() > 0) {
            for (View.OnAttachStateChangeListener listener : listeners) {
                listener.onViewAttachedToWindow(this);
            }
        }
        int vis = info.mWindowVisibility;
        if (vis != 8) {
            this.onWindowVisibilityChanged(vis);
            if (this.isShown()) {
                this.onVisibilityAggregated(vis == 0);
            }
        }
        this.onVisibilityChanged(this, visibility);
        if ((this.mPrivateFlags & 1024) != 0) {
            this.refreshDrawableState();
        }
    }

    void dispatchDetachedFromWindow() {
        AttachInfo info = this.mAttachInfo;
        if (info != null) {
            int vis = info.mWindowVisibility;
            if (vis != 8) {
                this.onWindowVisibilityChanged(8);
                if (this.isShown()) {
                    this.onVisibilityAggregated(false);
                }
            }
        }
        this.onDetachedFromWindow();
        this.mPrivateFlags &= -67108865;
        this.mPrivateFlags3 &= -5;
        this.mPrivateFlags3 &= -33554433;
        this.removeUnsetPressCallback();
        this.removeLongPressCallback();
        this.removePerformClickCallback();
        this.stopNestedScroll(0);
        this.jumpDrawablesToCurrentState();
        if ((this.mViewFlags & 1073741824) == 1073741824) {
            this.hideTooltip();
        }
        View.ListenerInfo li = this.mListenerInfo;
        CopyOnWriteArrayList<View.OnAttachStateChangeListener> listeners = li != null ? li.mOnAttachStateChangeListeners : null;
        if (listeners != null && listeners.size() > 0) {
            for (View.OnAttachStateChangeListener listener : listeners) {
                listener.onViewDetachedFromWindow(this);
            }
        }
        this.mAttachInfo = null;
    }

    @CallSuper
    protected void onAttachedToWindow() {
        this.mPrivateFlags3 &= -5;
        this.jumpDrawablesToCurrentState();
    }

    protected void onDetachedFromWindow() {
    }

    public boolean isAttachedToWindow() {
        return this.mAttachInfo != null;
    }

    protected int getWindowAttachCount() {
        return this.mWindowAttachCount;
    }

    public boolean hasTransientState() {
        return (this.mPrivateFlags2 & -2147483648) == Integer.MIN_VALUE;
    }

    public void setHasTransientState(boolean hasTransientState) {
        boolean oldHasTransientState = this.hasTransientState();
        this.mTransientStateCount = hasTransientState ? this.mTransientStateCount + 1 : this.mTransientStateCount - 1;
        if (this.mTransientStateCount < 0) {
            this.mTransientStateCount = 0;
        } else if (hasTransientState && this.mTransientStateCount == 1 || !hasTransientState && this.mTransientStateCount == 0) {
            this.mPrivateFlags2 = this.mPrivateFlags2 & 2147483647 | (hasTransientState ? Integer.MIN_VALUE : 0);
            boolean newHasTransientState = this.hasTransientState();
            if (this.mParent != null && newHasTransientState != oldHasTransientState) {
                this.mParent.childHasTransientStateChanged(this, newHasTransientState);
            }
        }
    }

    @Internal
    public void setHasTranslationTransientState(boolean hasTranslationTransientState) {
        if (hasTranslationTransientState) {
            this.mPrivateFlags4 |= 16384;
        } else {
            this.mPrivateFlags4 &= -16385;
        }
    }

    @Internal
    public boolean hasTranslationTransientState() {
        return (this.mPrivateFlags4 & 16384) == 16384;
    }

    public final void cancelPendingInputEvents() {
        this.dispatchCancelPendingInputEvents();
    }

    void dispatchCancelPendingInputEvents() {
        this.mPrivateFlags3 &= -17;
        this.onCancelPendingInputEvents();
        if ((this.mPrivateFlags3 & 16) != 16) {
            throw new IllegalStateException("View " + this.getClass().getSimpleName() + " did not call through to super.onCancelPendingInputEvents()");
        }
    }

    public void onCancelPendingInputEvents() {
        this.removePerformClickCallback();
        this.cancelLongPress();
        this.mPrivateFlags3 |= 16;
    }

    public void setDuplicateParentStateEnabled(boolean enabled) {
        this.setFlags(enabled ? 4194304 : 0, 4194304);
    }

    public boolean isDuplicateParentStateEnabled() {
        return (this.mViewFlags & 4194304) == 4194304;
    }

    public boolean isLaidOut() {
        return (this.mPrivateFlags3 & 4) == 4;
    }

    boolean isLayoutValid() {
        return this.isLaidOut() && (this.mPrivateFlags & 4096) == 0;
    }

    public void setWillNotDraw(boolean willNotDraw) {
        this.setFlags(willNotDraw ? 128 : 0, 128);
    }

    public boolean willNotDraw() {
        return (this.mViewFlags & 128) == 128;
    }

    public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
        this.setFlags(soundEffectsEnabled ? 134217728 : 0, 134217728);
    }

    public boolean isSoundEffectsEnabled() {
        return 134217728 == (this.mViewFlags & 134217728);
    }

    public void setHapticFeedbackEnabled(boolean hapticFeedbackEnabled) {
        this.setFlags(hapticFeedbackEnabled ? 268435456 : 0, 268435456);
    }

    public boolean isHapticFeedbackEnabled() {
        return 268435456 == (this.mViewFlags & 268435456);
    }

    public void playSoundEffect(int soundConstant) {
        if (this.mAttachInfo != null && this.mAttachInfo.mRootCallbacks != null && this.isSoundEffectsEnabled()) {
            this.mAttachInfo.mRootCallbacks.playSoundEffect(soundConstant);
        }
    }

    public final boolean performHapticFeedback(int feedbackConstant) {
        return this.performHapticFeedback(feedbackConstant, 0);
    }

    public boolean performHapticFeedback(int feedbackConstant, int flags) {
        if (this.mAttachInfo == null) {
            return false;
        } else {
            return (flags & 1) == 0 && !this.isHapticFeedbackEnabled() ? false : this.mAttachInfo.mRootCallbacks.performHapticFeedback(feedbackConstant, (flags & 2) != 0);
        }
    }

    @Internal
    public final int getRawLayoutDirection() {
        return (this.mPrivateFlags2 & 12) >> 2;
    }

    public void setLayoutDirection(int layoutDirection) {
        if (this.getRawLayoutDirection() != layoutDirection) {
            this.mPrivateFlags2 &= -13;
            this.resetRtlProperties();
            this.mPrivateFlags2 |= layoutDirection << 2 & 12;
            this.resolveRtlPropertiesIfNeeded();
            this.requestLayout();
            this.invalidate();
        }
    }

    public final int getLayoutDirection() {
        return (this.mPrivateFlags2 & 16) == 16 ? 1 : 0;
    }

    @Internal
    public final boolean isLayoutRtl() {
        return this.getLayoutDirection() == 1;
    }

    @Internal
    public boolean resolveRtlPropertiesIfNeeded() {
        if ((this.mPrivateFlags2 & 1610678816) == 1610678816) {
            return false;
        } else {
            if (!this.isLayoutDirectionResolved()) {
                this.resolveLayoutDirection();
                this.resolveLayoutParams();
            }
            if (!this.isTextDirectionResolved()) {
                this.resolveTextDirection();
            }
            if (!this.isTextAlignmentResolved()) {
                this.resolveTextAlignment();
            }
            if (!this.areDrawablesResolved()) {
                this.resolveDrawables();
            }
            if (!this.isPaddingResolved()) {
                this.resolvePadding();
            }
            this.onRtlPropertiesChanged(this.getLayoutDirection());
            return true;
        }
    }

    @Internal
    void resetRtlProperties() {
        this.resetResolvedLayoutDirection();
        this.resetResolvedTextDirection();
        this.resetResolvedTextAlignment();
        this.resetResolvedPadding();
        this.resetResolvedDrawables();
    }

    protected void onRtlPropertiesChanged(int layoutDirection) {
    }

    @Internal
    public boolean resolveLayoutDirection() {
        this.mPrivateFlags2 &= -49;
        if (ModernUI.getInstance().hasRtlSupport()) {
            switch(this.getRawLayoutDirection()) {
                case 1:
                    this.mPrivateFlags2 |= 16;
                    break;
                case 2:
                    if (!this.canResolveLayoutDirection()) {
                        return false;
                    }
                    try {
                        if (!this.mParent.isLayoutDirectionResolved()) {
                            return false;
                        }
                        if (this.mParent.getLayoutDirection() == 1) {
                            this.mPrivateFlags2 |= 16;
                        }
                    } catch (AbstractMethodError var2) {
                        ModernUI.LOGGER.error(VIEW_MARKER, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", var2);
                    }
                    break;
                case 3:
                    if (TextUtils.getLayoutDirectionFromLocale(ModernUI.getSelectedLocale()) == 1) {
                        this.mPrivateFlags2 |= 16;
                    }
            }
        }
        this.mPrivateFlags2 |= 32;
        return true;
    }

    public final boolean canResolveLayoutDirection() {
        if (!this.isLayoutDirectionInherited()) {
            return true;
        } else {
            if (this.mParent != null) {
                try {
                    return this.mParent.canResolveLayoutDirection();
                } catch (AbstractMethodError var2) {
                    ModernUI.LOGGER.error(VIEW_MARKER, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", var2);
                }
            }
            return false;
        }
    }

    @Internal
    void resetResolvedLayoutDirection() {
        this.mPrivateFlags2 &= -49;
    }

    @Internal
    public final boolean isLayoutDirectionInherited() {
        return this.getRawLayoutDirection() == 2;
    }

    public final boolean isLayoutDirectionResolved() {
        return (this.mPrivateFlags2 & 32) == 32;
    }

    @Internal
    public final int getRawTextDirection() {
        return (this.mPrivateFlags2 & 448) >> 6;
    }

    public void setTextDirection(int textDirection) {
        if (this.getRawTextDirection() != textDirection) {
            this.mPrivateFlags2 &= -449;
            this.resetResolvedTextDirection();
            this.mPrivateFlags2 |= textDirection << 6 & 448;
            this.resolveTextDirection();
            this.onRtlPropertiesChanged(this.getLayoutDirection());
            this.requestLayout();
            this.invalidate();
        }
    }

    public final int getTextDirection() {
        return (this.mPrivateFlags2 & 7168) >> 10;
    }

    @Internal
    public boolean resolveTextDirection() {
        this.mPrivateFlags2 &= -7681;
        if (!ModernUI.getInstance().hasRtlSupport()) {
            this.mPrivateFlags2 |= 1024;
        } else {
            int textDirection = this.getRawTextDirection();
            label41: switch(textDirection) {
                case 0:
                    if (!this.canResolveTextDirection()) {
                        this.mPrivateFlags2 |= 1024;
                        return false;
                    }
                    try {
                        if (!this.mParent.isTextDirectionResolved()) {
                            this.mPrivateFlags2 |= 1024;
                            return false;
                        }
                    } catch (AbstractMethodError var5) {
                        ModernUI.LOGGER.error(VIEW_MARKER, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", var5);
                        this.mPrivateFlags2 |= 1536;
                        return true;
                    }
                    int parentResolvedDirection;
                    try {
                        parentResolvedDirection = this.mParent.getTextDirection();
                    } catch (AbstractMethodError var4) {
                        ModernUI.LOGGER.error(VIEW_MARKER, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", var4);
                        parentResolvedDirection = 3;
                    }
                    switch(parentResolvedDirection) {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                            this.mPrivateFlags2 |= parentResolvedDirection << 10;
                            break label41;
                        default:
                            this.mPrivateFlags2 |= 1024;
                            break label41;
                    }
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    this.mPrivateFlags2 |= textDirection << 10;
                    break;
                default:
                    this.mPrivateFlags2 |= 1024;
            }
        }
        this.mPrivateFlags2 |= 512;
        return true;
    }

    public final boolean canResolveTextDirection() {
        if (!this.isTextDirectionInherited()) {
            return true;
        } else {
            if (this.mParent != null) {
                try {
                    return this.mParent.canResolveTextDirection();
                } catch (AbstractMethodError var2) {
                    ModernUI.LOGGER.error(VIEW_MARKER, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", var2);
                }
            }
            return false;
        }
    }

    @Internal
    void resetResolvedTextDirection() {
        this.mPrivateFlags2 &= -7681;
        this.mPrivateFlags2 |= 1024;
    }

    @Internal
    public final boolean isTextDirectionInherited() {
        return this.getRawTextDirection() == 0;
    }

    public final boolean isTextDirectionResolved() {
        return (this.mPrivateFlags2 & 512) == 512;
    }

    @Internal
    public final int getRawTextAlignment() {
        return (this.mPrivateFlags2 & 57344) >> 13;
    }

    public void setTextAlignment(int textAlignment) {
        if (textAlignment != this.getRawTextAlignment()) {
            this.mPrivateFlags2 &= -57345;
            this.resetResolvedTextAlignment();
            this.mPrivateFlags2 |= textAlignment << 13 & 57344;
            this.resolveTextAlignment();
            this.onRtlPropertiesChanged(this.getLayoutDirection());
            this.requestLayout();
            this.invalidate();
        }
    }

    public final int getTextAlignment() {
        return (this.mPrivateFlags2 & 917504) >> 17;
    }

    @Internal
    public boolean resolveTextAlignment() {
        this.mPrivateFlags2 &= -983041;
        if (!ModernUI.getInstance().hasRtlSupport()) {
            this.mPrivateFlags2 |= 131072;
        } else {
            int textAlignment = this.getRawTextAlignment();
            label41: switch(textAlignment) {
                case 0:
                    if (!this.canResolveTextAlignment()) {
                        this.mPrivateFlags2 |= 131072;
                        return false;
                    }
                    try {
                        if (!this.mParent.isTextAlignmentResolved()) {
                            this.mPrivateFlags2 |= 131072;
                            return false;
                        }
                    } catch (AbstractMethodError var5) {
                        ModernUI.LOGGER.error(VIEW_MARKER, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", var5);
                        this.mPrivateFlags2 |= 196608;
                        return true;
                    }
                    int parentResolvedTextAlignment;
                    try {
                        parentResolvedTextAlignment = this.mParent.getTextAlignment();
                    } catch (AbstractMethodError var4) {
                        ModernUI.LOGGER.error(VIEW_MARKER, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", var4);
                        parentResolvedTextAlignment = 1;
                    }
                    switch(parentResolvedTextAlignment) {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                            this.mPrivateFlags2 |= parentResolvedTextAlignment << 17;
                            break label41;
                        default:
                            this.mPrivateFlags2 |= 131072;
                            break label41;
                    }
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    this.mPrivateFlags2 |= textAlignment << 17;
                    break;
                default:
                    this.mPrivateFlags2 |= 131072;
            }
        }
        this.mPrivateFlags2 |= 65536;
        return true;
    }

    public final boolean canResolveTextAlignment() {
        if (!this.isTextAlignmentInherited()) {
            return true;
        } else {
            if (this.mParent != null) {
                try {
                    return this.mParent.canResolveTextAlignment();
                } catch (AbstractMethodError var2) {
                    ModernUI.LOGGER.error(VIEW_MARKER, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", var2);
                }
            }
            return false;
        }
    }

    @Internal
    void resetResolvedTextAlignment() {
        this.mPrivateFlags2 &= -983041;
        this.mPrivateFlags2 |= 131072;
    }

    @Internal
    public final boolean isTextAlignmentInherited() {
        return this.getRawTextAlignment() == 0;
    }

    public final boolean isTextAlignmentResolved() {
        return (this.mPrivateFlags2 & 65536) == 65536;
    }

    @Internal
    boolean isPaddingResolved() {
        return (this.mPrivateFlags2 & 536870912) == 536870912;
    }

    @Internal
    public void resolvePadding() {
        int resolvedLayoutDirection = this.getLayoutDirection();
        if (ModernUI.getInstance().hasRtlSupport()) {
            if (this.mBackground != null && (!this.mLeftPaddingDefined || !this.mRightPaddingDefined)) {
                Rect padding = (Rect) sThreadLocal.get();
                if (padding == null) {
                    padding = new Rect();
                    sThreadLocal.set(padding);
                }
                this.mBackground.getPadding(padding);
                if (!this.mLeftPaddingDefined) {
                    this.mUserPaddingLeftInitial = padding.left;
                }
                if (!this.mRightPaddingDefined) {
                    this.mUserPaddingRightInitial = padding.right;
                }
            }
            if (resolvedLayoutDirection == 1) {
                if (this.mUserPaddingStart != Integer.MIN_VALUE) {
                    this.mUserPaddingRight = this.mUserPaddingStart;
                } else {
                    this.mUserPaddingRight = this.mUserPaddingRightInitial;
                }
                if (this.mUserPaddingEnd != Integer.MIN_VALUE) {
                    this.mUserPaddingLeft = this.mUserPaddingEnd;
                } else {
                    this.mUserPaddingLeft = this.mUserPaddingLeftInitial;
                }
            } else {
                if (this.mUserPaddingStart != Integer.MIN_VALUE) {
                    this.mUserPaddingLeft = this.mUserPaddingStart;
                } else {
                    this.mUserPaddingLeft = this.mUserPaddingLeftInitial;
                }
                if (this.mUserPaddingEnd != Integer.MIN_VALUE) {
                    this.mUserPaddingRight = this.mUserPaddingEnd;
                } else {
                    this.mUserPaddingRight = this.mUserPaddingRightInitial;
                }
            }
            this.mUserPaddingBottom = this.mUserPaddingBottom >= 0 ? this.mUserPaddingBottom : this.mPaddingBottom;
        }
        this.internalSetPadding(this.mUserPaddingLeft, this.mPaddingTop, this.mUserPaddingRight, this.mUserPaddingBottom);
        this.onRtlPropertiesChanged(resolvedLayoutDirection);
        this.mPrivateFlags2 |= 536870912;
    }

    @Internal
    void resetResolvedPadding() {
        this.mPrivateFlags2 &= -536870913;
    }

    public int getBaseline() {
        return -1;
    }

    public final boolean isInLayout() {
        ViewRoot viewRoot = this.getViewRoot();
        return viewRoot != null && viewRoot.isInLayout();
    }

    @CallSuper
    public void requestLayout() {
        if (this.mAttachInfo != null && this.mAttachInfo.mViewRequestingLayout == null) {
            ViewRoot viewRoot = this.getViewRoot();
            if (viewRoot != null && viewRoot.isInLayout() && !viewRoot.requestLayoutDuringLayout(this)) {
                return;
            }
            this.mAttachInfo.mViewRequestingLayout = this;
        }
        this.mPrivateFlags |= 4096;
        this.mPrivateFlags |= Integer.MIN_VALUE;
        if (this.mParent != null && !this.mParent.isLayoutRequested()) {
            this.mParent.requestLayout();
        }
        if (this.mAttachInfo != null && this.mAttachInfo.mViewRequestingLayout == this) {
            this.mAttachInfo.mViewRequestingLayout = null;
        }
    }

    public boolean isLayoutRequested() {
        return (this.mPrivateFlags & 4096) == 4096;
    }

    public void forceLayout() {
        this.mPrivateFlags |= 4096;
        this.mPrivateFlags |= Integer.MIN_VALUE;
    }

    @CallSuper
    public void drawableHotspotChanged(float x, float y) {
        if (this.mBackground != null) {
            this.mBackground.setHotspot(x, y);
        }
        if (this.mDefaultFocusHighlight != null) {
            this.mDefaultFocusHighlight.setHotspot(x, y);
        }
        if (this.mForegroundInfo != null && this.mForegroundInfo.mDrawable != null) {
            this.mForegroundInfo.mDrawable.setHotspot(x, y);
        }
        this.dispatchDrawableHotspotChanged(x, y);
    }

    public void dispatchDrawableHotspotChanged(float x, float y) {
    }

    public void refreshDrawableState() {
        this.mPrivateFlags |= 1024;
        this.drawableStateChanged();
        ViewParent parent = this.mParent;
        if (parent != null) {
            parent.childDrawableStateChanged(this);
        }
    }

    @Internal
    protected void resolveDrawables() {
        if (this.isLayoutDirectionResolved() || this.getRawLayoutDirection() != 2) {
            int layoutDirection = this.isLayoutDirectionResolved() ? this.getLayoutDirection() : this.getRawLayoutDirection();
            if (this.mBackground != null) {
                this.mBackground.setLayoutDirection(layoutDirection);
            }
            if (this.mForegroundInfo != null && this.mForegroundInfo.mDrawable != null) {
                this.mForegroundInfo.mDrawable.setLayoutDirection(layoutDirection);
            }
            if (this.mDefaultFocusHighlight != null) {
                this.mDefaultFocusHighlight.setLayoutDirection(layoutDirection);
            }
            this.mPrivateFlags2 |= 1073741824;
            this.onResolveDrawables(layoutDirection);
        }
    }

    boolean areDrawablesResolved() {
        return (this.mPrivateFlags2 & 1073741824) == 1073741824;
    }

    @Internal
    public void onResolveDrawables(int layoutDirection) {
    }

    @Internal
    protected void resetResolvedDrawables() {
        this.resetResolvedDrawablesInternal();
    }

    void resetResolvedDrawablesInternal() {
        this.mPrivateFlags2 &= -1073741825;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        this.resetResolvedPadding();
        this.mUserPaddingStart = Integer.MIN_VALUE;
        this.mUserPaddingEnd = Integer.MIN_VALUE;
        this.mUserPaddingLeftInitial = left;
        this.mUserPaddingRightInitial = right;
        this.mLeftPaddingDefined = true;
        this.mRightPaddingDefined = true;
        this.internalSetPadding(left, top, right, bottom);
    }

    @Internal
    protected void internalSetPadding(int left, int top, int right, int bottom) {
        this.mUserPaddingLeft = left;
        this.mUserPaddingRight = right;
        this.mUserPaddingBottom = bottom;
        int viewFlags = this.mViewFlags;
        boolean changed = false;
        if ((viewFlags & 768) != 0) {
            if ((viewFlags & 512) != 0) {
                int offset = (viewFlags & 16777216) == 0 ? 0 : this.getVerticalScrollbarWidth();
                if (this.isLayoutRtl()) {
                    left += offset;
                } else {
                    right += offset;
                }
            }
            if ((viewFlags & 256) != 0) {
                bottom += (viewFlags & 16777216) == 0 ? 0 : this.getHorizontalScrollbarHeight();
            }
        }
        if (this.mPaddingLeft != left) {
            changed = true;
            this.mPaddingLeft = left;
        }
        if (this.mPaddingTop != top) {
            changed = true;
            this.mPaddingTop = top;
        }
        if (this.mPaddingRight != right) {
            changed = true;
            this.mPaddingRight = right;
        }
        if (this.mPaddingBottom != bottom) {
            changed = true;
            this.mPaddingBottom = bottom;
        }
        if (changed) {
            this.requestLayout();
        }
    }

    public void setPaddingRelative(int start, int top, int end, int bottom) {
        this.resetResolvedPadding();
        this.mUserPaddingStart = start;
        this.mUserPaddingEnd = end;
        this.mLeftPaddingDefined = true;
        this.mRightPaddingDefined = true;
        if (this.isLayoutRtl()) {
            this.mUserPaddingLeftInitial = end;
            this.mUserPaddingRightInitial = start;
            this.internalSetPadding(end, top, start, bottom);
        } else {
            this.mUserPaddingLeftInitial = start;
            this.mUserPaddingRightInitial = end;
            this.internalSetPadding(start, top, end, bottom);
        }
    }

    public int getPaddingTop() {
        return this.mPaddingTop;
    }

    public int getPaddingBottom() {
        return this.mPaddingBottom;
    }

    public int getPaddingLeft() {
        if (!this.isPaddingResolved()) {
            this.resolvePadding();
        }
        return this.mPaddingLeft;
    }

    public int getPaddingStart() {
        if (!this.isPaddingResolved()) {
            this.resolvePadding();
        }
        return this.getLayoutDirection() == 1 ? this.mPaddingRight : this.mPaddingLeft;
    }

    public int getPaddingRight() {
        if (!this.isPaddingResolved()) {
            this.resolvePadding();
        }
        return this.mPaddingRight;
    }

    public int getPaddingEnd() {
        if (!this.isPaddingResolved()) {
            this.resolvePadding();
        }
        return this.getLayoutDirection() == 1 ? this.mPaddingLeft : this.mPaddingRight;
    }

    public boolean isPaddingRelative() {
        return this.mUserPaddingStart != Integer.MIN_VALUE || this.mUserPaddingEnd != Integer.MIN_VALUE;
    }

    @CallSuper
    protected boolean verifyDrawable(@NonNull Drawable drawable) {
        return drawable == this.mBackground || this.mForegroundInfo != null && this.mForegroundInfo.mDrawable == drawable || this.mDefaultFocusHighlight == drawable;
    }

    @CallSuper
    protected void drawableStateChanged() {
        int[] state = this.getDrawableState();
        boolean changed = false;
        Drawable bg = this.mBackground;
        if (bg != null && bg.isStateful()) {
            changed |= bg.setState(state);
        }
        Drawable hl = this.mDefaultFocusHighlight;
        if (hl != null && hl.isStateful()) {
            changed |= hl.setState(state);
        }
        Drawable fg = this.mForegroundInfo != null ? this.mForegroundInfo.mDrawable : null;
        if (fg != null && fg.isStateful()) {
            changed |= fg.setState(state);
        }
        if (this.mScrollCache != null) {
            Drawable scrollBar = this.mScrollCache.mScrollBar;
            if (scrollBar != null && scrollBar.isStateful()) {
                changed |= scrollBar.setState(state) && this.mScrollCache.mState != 0;
            }
        }
        if (this.mStateListAnimator != null) {
            this.mStateListAnimator.setState(state);
        }
        if (!this.isAggregatedVisible()) {
            this.jumpDrawablesToCurrentState();
        }
        if (changed) {
            this.invalidate();
        }
    }

    public final int[] getDrawableState() {
        if (this.mDrawableState == null || (this.mPrivateFlags & 1024) != 0) {
            this.mDrawableState = this.onCreateDrawableState(0);
            this.mPrivateFlags &= -1025;
        }
        return this.mDrawableState;
    }

    @NonNull
    protected int[] onCreateDrawableState(int extraSpace) {
        if ((this.mViewFlags & 4194304) == 4194304 && this.mParent instanceof View) {
            return ((View) this.mParent).onCreateDrawableState(extraSpace);
        } else {
            int stateMask = 0;
            int privateFlags = this.mPrivateFlags;
            if ((privateFlags & 16384) != 0) {
                stateMask |= 16;
            }
            if ((this.mViewFlags & 32) == 0) {
                stateMask |= 8;
            }
            if (this.isFocused()) {
                stateMask |= 4;
            }
            if ((privateFlags & 4) != 0) {
                stateMask |= 2;
            }
            if (this.hasWindowFocus()) {
                stateMask |= 1;
            }
            if ((privateFlags & 1073741824) != 0) {
                stateMask |= 32;
            }
            if ((privateFlags & 268435456) != 0) {
                stateMask |= 64;
            }
            int privateFlags2 = this.mPrivateFlags2;
            if ((privateFlags2 & 1) != 0) {
                stateMask |= 128;
            }
            if ((privateFlags2 & 2) != 0) {
                stateMask |= 256;
            }
            int[] drawableState = StateSet.get(stateMask);
            if (extraSpace == 0) {
                return drawableState;
            } else {
                int[] fullState;
                if (stateMask != 0) {
                    fullState = new int[drawableState.length + extraSpace];
                    System.arraycopy(drawableState, 0, fullState, 0, drawableState.length);
                } else {
                    fullState = new int[extraSpace];
                }
                return fullState;
            }
        }
    }

    @NonNull
    protected static int[] mergeDrawableStates(@NonNull int[] baseState, @NonNull int[] additionalState) {
        int i = baseState.length - 1;
        while (i >= 0 && baseState[i] == 0) {
            i--;
        }
        System.arraycopy(additionalState, 0, baseState, i + 1, additionalState.length);
        return baseState;
    }

    @CallSuper
    public void jumpDrawablesToCurrentState() {
        if (this.mBackground != null) {
            this.mBackground.jumpToCurrentState();
        }
        if (this.mStateListAnimator != null) {
            this.mStateListAnimator.jumpToCurrentState();
        }
        if (this.mDefaultFocusHighlight != null) {
            this.mDefaultFocusHighlight.jumpToCurrentState();
        }
        if (this.mForegroundInfo != null && this.mForegroundInfo.mDrawable != null) {
            this.mForegroundInfo.mDrawable.jumpToCurrentState();
        }
    }

    public void setBackground(@Nullable Drawable background) {
        if (background != this.mBackground) {
            boolean requestLayout = false;
            if (this.mBackground != null) {
                if (this.isAttachedToWindow()) {
                    this.mBackground.setVisible(false, false);
                }
                this.mBackground.setCallback(null);
                this.unscheduleDrawable(this.mBackground);
            }
            if (background != null) {
                Rect padding = (Rect) sThreadLocal.get();
                if (padding == null) {
                    padding = new Rect();
                    sThreadLocal.set(padding);
                }
                this.resetResolvedDrawablesInternal();
                background.setLayoutDirection(this.getLayoutDirection());
                if (background.getPadding(padding)) {
                    this.resetResolvedPadding();
                    if (background.getLayoutDirection() == 1) {
                        this.mUserPaddingLeftInitial = padding.right;
                        this.mUserPaddingRightInitial = padding.left;
                        this.internalSetPadding(padding.right, padding.top, padding.left, padding.bottom);
                    } else {
                        this.mUserPaddingLeftInitial = padding.left;
                        this.mUserPaddingRightInitial = padding.right;
                        this.internalSetPadding(padding.left, padding.top, padding.right, padding.bottom);
                    }
                    this.mLeftPaddingDefined = false;
                    this.mRightPaddingDefined = false;
                }
                if (this.mBackground == null || this.mBackground.getMinimumHeight() != background.getMinimumHeight() || this.mBackground.getMinimumWidth() != background.getMinimumWidth()) {
                    requestLayout = true;
                }
                this.mBackground = background;
                background.setCallback(this);
                if ((this.mPrivateFlags & 128) != 0) {
                    this.mPrivateFlags &= -129;
                    requestLayout = true;
                }
            } else {
                this.mBackground = null;
                if ((this.mViewFlags & 128) != 0 && this.mDefaultFocusHighlight == null && (this.mForegroundInfo == null || this.mForegroundInfo.mDrawable == null)) {
                    this.mPrivateFlags |= 128;
                }
                requestLayout = true;
            }
            if (requestLayout) {
                this.requestLayout();
            }
            this.mBackgroundSizeChanged = true;
            this.invalidate();
        }
    }

    @Nullable
    public Drawable getBackground() {
        return this.mBackground;
    }

    @Nullable
    public Drawable getForeground() {
        return this.mForegroundInfo != null ? this.mForegroundInfo.mDrawable : null;
    }

    public void setForeground(@Nullable Drawable foreground) {
        if (this.mForegroundInfo == null) {
            if (foreground == null) {
                return;
            }
            this.mForegroundInfo = new View.ForegroundInfo();
        }
        if (foreground != this.mForegroundInfo.mDrawable) {
            if (this.mForegroundInfo.mDrawable != null) {
                if (this.isAttachedToWindow()) {
                    this.mForegroundInfo.mDrawable.setVisible(false, false);
                }
                this.mForegroundInfo.mDrawable.setCallback(null);
                this.unscheduleDrawable(this.mForegroundInfo.mDrawable);
            }
            this.mForegroundInfo.mDrawable = foreground;
            this.mForegroundInfo.mBoundsChanged = true;
            if (foreground != null) {
                if ((this.mPrivateFlags & 128) != 0) {
                    this.mPrivateFlags &= -129;
                }
                foreground.setLayoutDirection(this.getLayoutDirection());
                if (foreground.isStateful()) {
                    foreground.setState(this.getDrawableState());
                }
                if (this.isAttachedToWindow()) {
                    foreground.setVisible(this.getWindowVisibility() == 0 && this.isShown(), false);
                }
                foreground.setCallback(this);
            } else if ((this.mViewFlags & 128) != 0 && this.mBackground == null && this.mDefaultFocusHighlight == null) {
                this.mPrivateFlags |= 128;
            }
            this.requestLayout();
            this.invalidate();
        }
    }

    @Internal
    public boolean isForegroundInsidePadding() {
        return this.mForegroundInfo == null || this.mForegroundInfo.mInsidePadding;
    }

    public int getForegroundGravity() {
        return this.mForegroundInfo != null ? this.mForegroundInfo.mGravity : 8388659;
    }

    public void setForegroundGravity(int gravity) {
        if (this.mForegroundInfo == null) {
            this.mForegroundInfo = new View.ForegroundInfo();
        }
        if (this.mForegroundInfo.mGravity != gravity) {
            if ((gravity & 8388615) == 0) {
                gravity |= 8388611;
            }
            if ((gravity & 112) == 0) {
                gravity |= 48;
            }
            this.mForegroundInfo.mGravity = gravity;
            this.requestLayout();
        }
    }

    @NonNull
    public final ViewTreeObserver getViewTreeObserver() {
        if (this.mAttachInfo != null) {
            return this.mAttachInfo.mTreeObserver;
        } else {
            if (this.mFloatingTreeObserver == null) {
                this.mFloatingTreeObserver = new ViewTreeObserver();
            }
            return this.mFloatingTreeObserver;
        }
    }

    public final View getRootView() {
        if (this.mAttachInfo != null) {
            View v = this.mAttachInfo.mRootView;
            if (v != null) {
                return v;
            }
        }
        View v = this;
        while (v.mParent instanceof View) {
            v = (View) v.mParent;
        }
        return v;
    }

    @Internal
    public boolean toGlobalMotionEvent(@NonNull MotionEvent ev) {
        AttachInfo info = this.mAttachInfo;
        if (info == null) {
            return false;
        } else {
            Matrix m = info.mTmpMatrix;
            m.setIdentity();
            this.transformMatrixToGlobal(m);
            ev.transform(m);
            return true;
        }
    }

    @Internal
    public boolean toLocalMotionEvent(@NonNull MotionEvent ev) {
        AttachInfo info = this.mAttachInfo;
        if (info == null) {
            return false;
        } else {
            Matrix m = info.mTmpMatrix;
            m.setIdentity();
            this.transformMatrixToLocal(m);
            ev.transform(m);
            return true;
        }
    }

    public void transformMatrixToGlobal(@NonNull Matrix matrix) {
        if (this.mParent instanceof View vp) {
            vp.transformMatrixToGlobal(matrix);
            matrix.preTranslate((float) (-vp.mScrollX), (float) (-vp.mScrollY));
        }
        matrix.preTranslate((float) this.mLeft, (float) this.mTop);
        if (!this.hasIdentityMatrix()) {
            matrix.preConcat(this.getMatrix());
        }
    }

    public void transformMatrixToLocal(@NonNull Matrix matrix) {
        if (this.mParent instanceof View vp) {
            vp.transformMatrixToLocal(matrix);
            matrix.postTranslate((float) vp.mScrollX, (float) vp.mScrollY);
        }
        matrix.postTranslate((float) (-this.mLeft), (float) (-this.mTop));
        if (!this.hasIdentityMatrix()) {
            matrix.postConcat(this.getInverseMatrix());
        }
    }

    public void getBoundsOnScreen(@NonNull Rect outRect) {
        this.getBoundsOnScreen(outRect, false);
    }

    public void getBoundsOnScreen(@NonNull Rect outRect, boolean clipToParent) {
        if (this.mAttachInfo != null) {
            RectF position = this.mAttachInfo.mTmpTransformRect;
            position.set(0.0F, 0.0F, (float) (this.mRight - this.mLeft), (float) (this.mBottom - this.mTop));
            this.mapRectFromViewToScreenCoords(position, clipToParent);
            position.round(outRect);
        }
    }

    public void mapRectFromViewToScreenCoords(@NonNull RectF rect, boolean clipToParent) {
        if (!this.hasIdentityMatrix()) {
            this.getMatrix().mapRect(rect);
        }
        rect.offset((float) this.mLeft, (float) this.mTop);
        ViewParent parent = this.mParent;
        while (parent instanceof View) {
            View parentView = (View) parent;
            rect.offset((float) (-parentView.mScrollX), (float) (-parentView.mScrollY));
            if (clipToParent) {
                rect.left = Math.max(rect.left, 0.0F);
                rect.top = Math.max(rect.top, 0.0F);
                rect.right = Math.min(rect.right, (float) parentView.getWidth());
                rect.bottom = Math.min(rect.bottom, (float) parentView.getHeight());
            }
            if (!parentView.hasIdentityMatrix()) {
                parentView.getMatrix().mapRect(rect);
            }
            rect.offset((float) parentView.mLeft, (float) parentView.mTop);
            parent = parentView.mParent;
        }
    }

    public void getLocationOnScreen(int[] outLocation) {
        this.getLocationInWindow(outLocation);
    }

    public void getLocationInWindow(@NonNull int[] outLocation) {
        if (outLocation.length < 2) {
            throw new IllegalArgumentException("outLocation must be an array of two integers");
        } else {
            outLocation[0] = 0;
            outLocation[1] = 0;
            this.transformFromViewToWindowSpace(outLocation);
        }
    }

    @Internal
    public void transformFromViewToWindowSpace(@NonNull int[] inOutLocation) {
        if (inOutLocation.length < 2) {
            throw new IllegalArgumentException("inOutLocation must be an array of two integers");
        } else if (this.mAttachInfo == null) {
            inOutLocation[0] = inOutLocation[1] = 0;
        } else {
            float[] position = this.mAttachInfo.mTmpTransformLocation;
            position[0] = (float) inOutLocation[0];
            position[1] = (float) inOutLocation[1];
            if (!this.hasIdentityMatrix()) {
                this.getMatrix().mapPoint(position);
            }
            position[0] += (float) this.mLeft;
            position[1] += (float) this.mTop;
            ViewParent viewParent = this.mParent;
            while (viewParent instanceof View) {
                View view = (View) viewParent;
                position[0] -= (float) view.mScrollX;
                position[1] -= (float) view.mScrollY;
                if (!view.hasIdentityMatrix()) {
                    view.getMatrix().mapPoint(position);
                }
                position[0] += (float) view.mLeft;
                position[1] += (float) view.mTop;
                viewParent = view.mParent;
            }
            inOutLocation[0] = Math.round(position[0]);
            inOutLocation[1] = Math.round(position[1]);
        }
    }

    @Nullable
    public final <T extends View> T findViewById(int id) {
        return id == -1 ? null : this.findViewTraversal(id);
    }

    @NonNull
    public final <T extends View> T requireViewById(int id) {
        T view = this.findViewById(id);
        if (view == null) {
            throw new IllegalArgumentException("ID does not reference a View inside this View");
        } else {
            return view;
        }
    }

    @Internal
    @Nullable
    protected <T extends View> T findViewTraversal(int id) {
        return (T) (id == this.mID ? this : null);
    }

    @Internal
    public final <T extends View> T findViewByPredicate(@NonNull Predicate<View> predicate) {
        return this.findViewByPredicateTraversal(predicate, null);
    }

    @Internal
    @Nullable
    protected <T extends View> T findViewByPredicateTraversal(@NonNull Predicate<View> predicate, @Nullable View childToSkip) {
        return (T) (predicate.test(this) ? this : null);
    }

    @Internal
    @Nullable
    public final <T extends View> T findViewByPredicateInsideOut(@NonNull View start, Predicate<View> predicate) {
        View childToSkip = null;
        while (true) {
            T view = start.findViewByPredicateTraversal(predicate, childToSkip);
            if (view != null || start == this) {
                return view;
            }
            ViewParent parent = start.getParent();
            if (!(parent instanceof View)) {
                return null;
            }
            childToSkip = start;
            start = (View) parent;
        }
    }

    @Nullable
    public final Handler getHandler() {
        AttachInfo attachInfo = this.mAttachInfo;
        return attachInfo != null ? attachInfo.mHandler : null;
    }

    @NonNull
    private HandlerActionQueue getRunQueue() {
        if (this.mRunQueue == null) {
            this.mRunQueue = new HandlerActionQueue();
        }
        return this.mRunQueue;
    }

    @Nullable
    public final ViewRoot getViewRoot() {
        return this.mAttachInfo != null ? this.mAttachInfo.mViewRoot : null;
    }

    public final boolean post(@NonNull Runnable action) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            return attachInfo.mHandler.post(action);
        } else {
            this.getRunQueue().post(action);
            return true;
        }
    }

    public final boolean postDelayed(@NonNull Runnable action, long delayMillis) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            return attachInfo.mHandler.postDelayed(action, delayMillis);
        } else {
            this.getRunQueue().postDelayed(action, delayMillis);
            return true;
        }
    }

    public final void postOnAnimation(@NonNull Runnable action) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            attachInfo.mViewRoot.mChoreographer.postCallback(1, action, null);
        } else {
            this.getRunQueue().post(action);
        }
    }

    public final void postOnAnimationDelayed(@NonNull Runnable action, long delayMillis) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            attachInfo.mViewRoot.mChoreographer.postCallbackDelayed(1, action, null, delayMillis);
        } else {
            this.getRunQueue().postDelayed(action, delayMillis);
        }
    }

    public final void removeCallbacks(@Nullable Runnable action) {
        if (action != null) {
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo != null) {
                attachInfo.mHandler.removeCallbacks(action);
                attachInfo.mViewRoot.mChoreographer.removeCallbacks(1, action, null);
            }
            if (this.mRunQueue != null) {
                this.mRunQueue.removeCallbacks(action);
            }
        }
    }

    public final void postInvalidate() {
        this.postInvalidateDelayed(0L);
    }

    public final void postInvalidateDelayed(long delayMilliseconds) {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            attachInfo.mViewRoot.dispatchInvalidateDelayed(this, delayMilliseconds);
        }
    }

    public final void postInvalidateOnAnimation() {
        AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null) {
            attachInfo.mViewRoot.dispatchInvalidateOnAnimation(this);
        }
    }

    public final boolean startDragAndDrop(@Nullable Object localState, @Nullable View.DragShadow shadow, int flags) {
        if (this.mAttachInfo == null) {
            ModernUI.LOGGER.error(VIEW_MARKER, "startDragAndDrop called out of a window");
            return false;
        } else {
            return this.mAttachInfo.mViewRoot.startDragAndDrop(this, localState, shadow, flags);
        }
    }

    public boolean onDragEvent(DragEvent event) {
        return true;
    }

    protected void dispatchVisibilityChanged(@NonNull View changedView, int visibility) {
        this.onVisibilityChanged(changedView, visibility);
    }

    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
    }

    public final int getWindowVisibility() {
        return this.mAttachInfo != null ? this.mAttachInfo.mWindowVisibility : 8;
    }

    public void dispatchWindowVisibilityChanged(int visibility) {
        this.onWindowVisibilityChanged(visibility);
    }

    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility == 0) {
            this.initialAwakenScrollBars();
        }
    }

    boolean isAggregatedVisible() {
        return (this.mPrivateFlags3 & 536870912) != 0;
    }

    boolean dispatchVisibilityAggregated(boolean isVisible) {
        boolean thisVisible = this.getVisibility() == 0;
        if (thisVisible || !isVisible) {
            this.onVisibilityAggregated(isVisible);
        }
        return thisVisible && isVisible;
    }

    @CallSuper
    public void onVisibilityAggregated(boolean isVisible) {
        boolean oldVisible = this.isAggregatedVisible();
        this.mPrivateFlags3 = isVisible ? this.mPrivateFlags3 | 536870912 : this.mPrivateFlags3 & -536870913;
        if (isVisible && this.mAttachInfo != null) {
            this.initialAwakenScrollBars();
        }
        Drawable dr = this.mBackground;
        if (dr != null && isVisible != dr.isVisible()) {
            dr.setVisible(isVisible, false);
        }
        Drawable hl = this.mDefaultFocusHighlight;
        if (hl != null && isVisible != hl.isVisible()) {
            hl.setVisible(isVisible, false);
        }
        Drawable fg = this.mForegroundInfo != null ? this.mForegroundInfo.mDrawable : null;
        if (fg != null && isVisible != fg.isVisible()) {
            fg.setVisible(isVisible, false);
        }
    }

    public final boolean dispatchPointerEvent(@NonNull MotionEvent event) {
        return event.isTouchEvent() ? this.dispatchTouchEvent(event) : this.dispatchGenericMotionEvent(event);
    }

    public void dispatchWindowFocusChanged(boolean hasFocus) {
        this.onWindowFocusChanged(hasFocus);
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) {
            if (this.isPressed()) {
                this.setPressed(false);
            }
            this.mPrivateFlags3 &= -131073;
            this.removeLongPressCallback();
            this.removeTapCallback();
            this.resetPressedState();
        }
        this.refreshDrawableState();
    }

    public boolean hasWindowFocus() {
        return this.mAttachInfo != null && this.mAttachInfo.mHasWindowFocus;
    }

    public boolean dispatchGenericMotionEvent(@NonNull MotionEvent event) {
        int action = event.getAction();
        if (action != 9 && action != 7 && action != 10) {
            if (this.dispatchGenericPointerEvent(event)) {
                return true;
            }
        } else if (this.dispatchHoverEvent(event)) {
            return true;
        }
        return this.dispatchGenericMotionEventInternal(event);
    }

    private boolean dispatchGenericMotionEventInternal(MotionEvent event) {
        View.ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnGenericMotionListener != null && (this.mViewFlags & 32) == 0 && li.mOnGenericMotionListener.onGenericMotion(this, event)) {
            return true;
        } else if (this.onGenericMotionEvent(event)) {
            return true;
        } else {
            int actionButton = event.getActionButton();
            switch(event.getAction()) {
                case 11:
                    if (this.isContextClickable() && !this.mInContextButtonPress && !this.mHasPerformedLongPress && actionButton == 2 && this.performContextClick(event.getX(), event.getY())) {
                        this.mInContextButtonPress = true;
                        this.setPressed(event.getX(), event.getY());
                        this.removeTapCallback();
                        this.removeLongPressCallback();
                        return true;
                    }
                    break;
                case 12:
                    if (this.mInContextButtonPress && actionButton == 2) {
                        this.mInContextButtonPress = false;
                        this.mIgnoreNextUpEvent = true;
                    }
            }
            return false;
        }
    }

    protected boolean dispatchHoverEvent(@NonNull MotionEvent event) {
        View.ListenerInfo li = this.mListenerInfo;
        return li != null && li.mOnHoverListener != null && (this.mViewFlags & 32) == 0 && li.mOnHoverListener.onHover(this, event) ? true : this.onHoverEvent(event);
    }

    protected boolean dispatchGenericPointerEvent(@NonNull MotionEvent event) {
        return false;
    }

    public boolean onGenericMotionEvent(@NonNull MotionEvent event) {
        return false;
    }

    public boolean onHoverEvent(@NonNull MotionEvent event) {
        int action = event.getAction();
        if ((action == 9 || action == 7) && this.isOnScrollbar(event.getX(), event.getY())) {
            this.awakenScrollBars();
        }
        if (!this.isHoverable() && !this.isHovered()) {
            return false;
        } else {
            switch(action) {
                case 9:
                    this.setHovered(true);
                    break;
                case 10:
                    this.setHovered(false);
            }
            this.dispatchGenericMotionEventInternal(event);
            return true;
        }
    }

    private boolean isHoverable() {
        int viewFlags = this.mViewFlags;
        return (viewFlags & 32) == 32 ? false : (viewFlags & 16384) == 16384 || (viewFlags & 2097152) == 2097152 || (viewFlags & 8388608) == 8388608;
    }

    public boolean isHovered() {
        return (this.mPrivateFlags & 268435456) != 0;
    }

    public void setHovered(boolean hovered) {
        if (hovered) {
            if ((this.mPrivateFlags & 268435456) == 0) {
                this.mPrivateFlags |= 268435456;
                this.refreshDrawableState();
                this.onHoverChanged(true);
            }
        } else if ((this.mPrivateFlags & 268435456) != 0) {
            this.mPrivateFlags &= -268435457;
            this.refreshDrawableState();
            this.onHoverChanged(false);
        }
    }

    public void onHoverChanged(boolean hovered) {
    }

    @NonNull
    View.ListenerInfo getListenerInfo() {
        if (this.mListenerInfo == null) {
            this.mListenerInfo = new View.ListenerInfo();
        }
        return this.mListenerInfo;
    }

    public void setOnScrollChangeListener(@Nullable View.OnScrollChangeListener l) {
        if (l != null || this.mListenerInfo != null) {
            this.getListenerInfo().mOnScrollChangeListener = l;
        }
    }

    public void setOnFocusChangeListener(@Nullable View.OnFocusChangeListener l) {
        if (l != null || this.mListenerInfo != null) {
            this.getListenerInfo().mOnFocusChangeListener = l;
        }
    }

    public void addOnLayoutChangeListener(@NonNull View.OnLayoutChangeListener listener) {
        View.ListenerInfo li = this.getListenerInfo();
        if (li.mOnLayoutChangeListeners == null) {
            li.mOnLayoutChangeListeners = new ArrayList();
        }
        if (!li.mOnLayoutChangeListeners.contains(listener)) {
            li.mOnLayoutChangeListeners.add(listener);
        }
    }

    public void removeOnLayoutChangeListener(@NonNull View.OnLayoutChangeListener listener) {
        View.ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnLayoutChangeListeners != null) {
            li.mOnLayoutChangeListeners.remove(listener);
        }
    }

    public void addOnAttachStateChangeListener(@NonNull View.OnAttachStateChangeListener listener) {
        View.ListenerInfo li = this.getListenerInfo();
        if (li.mOnAttachStateChangeListeners == null) {
            li.mOnAttachStateChangeListeners = new CopyOnWriteArrayList();
        }
        li.mOnAttachStateChangeListeners.add(listener);
    }

    public void removeOnAttachStateChangeListener(@NonNull View.OnAttachStateChangeListener listener) {
        View.ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnAttachStateChangeListeners != null) {
            li.mOnAttachStateChangeListeners.remove(listener);
        }
    }

    @Nullable
    public View.OnFocusChangeListener getOnFocusChangeListener() {
        View.ListenerInfo li = this.mListenerInfo;
        return li != null ? li.mOnFocusChangeListener : null;
    }

    public void setOnClickListener(@Nullable View.OnClickListener l) {
        if (l != null || this.mListenerInfo != null) {
            if (!this.isClickable()) {
                this.setClickable(true);
            }
            this.getListenerInfo().mOnClickListener = l;
        }
    }

    public boolean hasOnClickListeners() {
        View.ListenerInfo li = this.mListenerInfo;
        return li != null && li.mOnClickListener != null;
    }

    public void setOnLongClickListener(@Nullable View.OnLongClickListener l) {
        if (l != null || this.mListenerInfo != null) {
            if (!this.isLongClickable()) {
                this.setLongClickable(true);
            }
            this.getListenerInfo().mOnLongClickListener = l;
        }
    }

    public boolean hasOnLongClickListeners() {
        View.ListenerInfo li = this.mListenerInfo;
        return li != null && li.mOnLongClickListener != null;
    }

    @Internal
    @Nullable
    public View.OnLongClickListener getOnLongClickListener() {
        View.ListenerInfo li = this.mListenerInfo;
        return li != null ? li.mOnLongClickListener : null;
    }

    public void setOnContextClickListener(@Nullable View.OnContextClickListener l) {
        if (l != null || this.mListenerInfo != null) {
            if (!this.isContextClickable()) {
                this.setContextClickable(true);
            }
            this.getListenerInfo().mOnContextClickListener = l;
        }
    }

    public void setOnCreateContextMenuListener(@Nullable View.OnCreateContextMenuListener l) {
        if (l != null || this.mListenerInfo != null) {
            if (!this.isLongClickable()) {
                this.setLongClickable(true);
            }
            this.getListenerInfo().mOnCreateContextMenuListener = l;
        }
    }

    public void setOnKeyListener(@Nullable View.OnKeyListener l) {
        if (l != null || this.mListenerInfo != null) {
            this.getListenerInfo().mOnKeyListener = l;
        }
    }

    public void setOnTouchListener(@Nullable View.OnTouchListener l) {
        if (l != null || this.mListenerInfo != null) {
            this.getListenerInfo().mOnTouchListener = l;
        }
    }

    public void setOnGenericMotionListener(@Nullable View.OnGenericMotionListener l) {
        if (l != null || this.mListenerInfo != null) {
            this.getListenerInfo().mOnGenericMotionListener = l;
        }
    }

    public void setOnHoverListener(@Nullable View.OnHoverListener l) {
        if (l != null || this.mListenerInfo != null) {
            this.getListenerInfo().mOnHoverListener = l;
        }
    }

    public void setOnDragListener(@Nullable View.OnDragListener l) {
        if (l != null || this.mListenerInfo != null) {
            this.getListenerInfo().mOnDragListener = l;
        }
    }

    protected boolean canReceivePointerEvents() {
        return (this.mViewFlags & 12) == 0;
    }

    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        int action = event.getAction();
        if (action == 0) {
            this.stopNestedScroll(0);
        }
        boolean result = (this.mViewFlags & 32) == 0 && this.handleScrollBarDragging(event);
        View.ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnTouchListener != null && (this.mViewFlags & 32) == 0 && li.mOnTouchListener.onTouch(this, event)) {
            result = true;
        }
        if (!result && this.onTouchEvent(event)) {
            result = true;
        }
        if (action == 1 || action == 3 || action == 0 && !result) {
            this.stopNestedScroll(0);
        }
        return result;
    }

    public boolean onTouchEvent(@NonNull MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int viewFlags = this.mViewFlags;
        int action = event.getAction();
        boolean clickable = (viewFlags & 16384) == 16384 || (viewFlags & 2097152) == 2097152 || (viewFlags & 8388608) == 8388608;
        if ((viewFlags & 32) == 32) {
            if (action == 1 && (this.mPrivateFlags & 16384) != 0) {
                this.setPressed(false);
            }
            this.mPrivateFlags3 &= -131073;
            return clickable;
        } else if (!clickable && (viewFlags & 1073741824) != 1073741824) {
            return false;
        } else {
            switch(action) {
                case 0:
                    this.mHasPerformedLongPress = false;
                    if (!clickable) {
                        this.checkForLongClick((long) ViewConfiguration.getLongPressTimeout(), x, y);
                    } else if (!this.performButtonActionOnTouchDown(event)) {
                        boolean isInScrollingContainer = this.isInScrollingContainer();
                        if (isInScrollingContainer) {
                            this.mPrivateFlags |= 33554432;
                            if (this.mPendingCheckForTap == null) {
                                this.mPendingCheckForTap = new View.CheckForTap();
                            }
                            this.mPendingCheckForTap.x = event.getX();
                            this.mPendingCheckForTap.y = event.getY();
                            this.postDelayed(this.mPendingCheckForTap, (long) ViewConfiguration.getTapTimeout());
                        } else {
                            this.setPressed(x, y);
                            this.checkForLongClick((long) ViewConfiguration.getLongPressTimeout(), x, y);
                        }
                    }
                    break;
                case 1:
                    this.mPrivateFlags3 &= -131073;
                    if ((viewFlags & 1073741824) == 1073741824) {
                        this.handleTooltipUp();
                    }
                    if (!clickable) {
                        this.removeTapCallback();
                        this.removeLongPressCallback();
                        this.mInContextButtonPress = false;
                        this.mHasPerformedLongPress = false;
                        this.mIgnoreNextUpEvent = false;
                    } else {
                        boolean prepressed = (this.mPrivateFlags & 33554432) != 0;
                        if ((this.mPrivateFlags & 16384) != 0 || prepressed) {
                            boolean focusTaken = false;
                            if (this.isFocusable() && this.isFocusableInTouchMode() && !this.isFocused()) {
                                focusTaken = this.requestFocus();
                            }
                            if (prepressed) {
                                this.setPressed(x, y);
                            }
                            if (!this.mHasPerformedLongPress && !this.mIgnoreNextUpEvent) {
                                this.removeLongPressCallback();
                                if (!focusTaken) {
                                    if (this.mPerformClick == null) {
                                        this.mPerformClick = new View.PerformClick();
                                    }
                                    if (!this.post(this.mPerformClick)) {
                                        this.performClick();
                                    }
                                }
                            }
                            if (this.mUnsetPressedState == null) {
                                this.mUnsetPressedState = new View.UnsetPressedState();
                            }
                            if (prepressed) {
                                this.postDelayed(this.mUnsetPressedState, (long) ViewConfiguration.getPressedStateDuration());
                            } else if (!this.post(this.mUnsetPressedState)) {
                                this.mUnsetPressedState.run();
                            }
                            this.removeTapCallback();
                        }
                        this.mIgnoreNextUpEvent = false;
                    }
                    break;
                case 2:
                    if (clickable) {
                        this.drawableHotspotChanged(x, y);
                    }
                    if (!this.pointInView(x, y, (float) ViewConfiguration.get(this.mContext).getScaledTouchSlop())) {
                        this.removeTapCallback();
                        this.removeLongPressCallback();
                        if ((this.mPrivateFlags & 16384) != 0) {
                            this.setPressed(false);
                        }
                        this.mPrivateFlags3 &= -131073;
                    }
                    break;
                case 3:
                    if (clickable) {
                        this.setPressed(false);
                    }
                    this.removeTapCallback();
                    this.removeLongPressCallback();
                    this.mInContextButtonPress = false;
                    this.mHasPerformedLongPress = false;
                    this.mIgnoreNextUpEvent = false;
                    this.mPrivateFlags3 &= -131073;
            }
            return true;
        }
    }

    @Internal
    protected boolean handleScrollBarDragging(MotionEvent event) {
        if (this.mScrollCache == null) {
            return false;
        } else {
            float x = event.getX();
            float y = event.getY();
            int action = event.getAction();
            if ((this.mScrollCache.mScrollBarDraggingState != 0 || action == 0) && event.isButtonPressed(1)) {
                switch(action) {
                    case 2:
                        if (this.mScrollCache.mScrollBarDraggingState == 1) {
                            Rect bounds = this.mScrollCache.mScrollBarBounds;
                            this.getVerticalScrollBarBounds(bounds, null);
                            int range = this.computeVerticalScrollRange();
                            int offset = this.computeVerticalScrollOffset();
                            int extent = this.computeVerticalScrollExtent();
                            int thumbLength = ScrollCache.getThumbLength(bounds.height(), bounds.width(), extent, range);
                            int thumbOffset = ScrollCache.getThumbOffset(bounds.height(), thumbLength, extent, range, offset);
                            float diff = y - this.mScrollCache.mScrollBarDraggingPos;
                            float maxThumbOffset = (float) (bounds.height() - thumbLength);
                            float newThumbOffset = Math.min(Math.max((float) thumbOffset + diff, 0.0F), maxThumbOffset);
                            int height = this.getHeight();
                            if (Math.round(newThumbOffset) != thumbOffset && maxThumbOffset > 0.0F && height > 0 && extent > 0) {
                                int newY = Math.round((float) (range - extent) / ((float) extent / (float) height) * (newThumbOffset / maxThumbOffset));
                                if (newY != this.getScrollY()) {
                                    this.mScrollCache.mScrollBarDraggingPos = y;
                                    this.setScrollY(newY);
                                }
                            }
                            return true;
                        } else if (this.mScrollCache.mScrollBarDraggingState == 2) {
                            Rect bounds = this.mScrollCache.mScrollBarBounds;
                            this.getHorizontalScrollBarBounds(bounds, null);
                            int range = this.computeHorizontalScrollRange();
                            int offset = this.computeHorizontalScrollOffset();
                            int extent = this.computeHorizontalScrollExtent();
                            int thumbLength = ScrollCache.getThumbLength(bounds.width(), bounds.height(), extent, range);
                            int thumbOffset = ScrollCache.getThumbOffset(bounds.width(), thumbLength, extent, range, offset);
                            float diff = x - this.mScrollCache.mScrollBarDraggingPos;
                            float maxThumbOffset = (float) (bounds.width() - thumbLength);
                            float newThumbOffset = Math.min(Math.max((float) thumbOffset + diff, 0.0F), maxThumbOffset);
                            int width = this.getWidth();
                            if (Math.round(newThumbOffset) != thumbOffset && maxThumbOffset > 0.0F && width > 0 && extent > 0) {
                                int newX = Math.round((float) (range - extent) / ((float) extent / (float) width) * (newThumbOffset / maxThumbOffset));
                                if (newX != this.getScrollX()) {
                                    this.mScrollCache.mScrollBarDraggingPos = x;
                                    this.setScrollX(newX);
                                }
                            }
                            return true;
                        }
                    case 0:
                        if (this.mScrollCache.mState == 0) {
                            return false;
                        } else if (this.isOnVerticalScrollbarThumb(x, y)) {
                            this.mScrollCache.mScrollBarDraggingState = 1;
                            this.mScrollCache.mScrollBarDraggingPos = y;
                            return true;
                        } else if (this.isOnHorizontalScrollbarThumb(x, y)) {
                            this.mScrollCache.mScrollBarDraggingState = 2;
                            this.mScrollCache.mScrollBarDraggingPos = x;
                            return true;
                        }
                    default:
                        this.mScrollCache.mScrollBarDraggingState = 0;
                        return false;
                }
            } else {
                this.mScrollCache.mScrollBarDraggingState = 0;
                return false;
            }
        }
    }

    @Internal
    public boolean isInScrollingContainer() {
        for (ViewParent p = this.getParent(); p instanceof ViewGroup; p = p.getParent()) {
            if (((ViewGroup) p).shouldDelayChildPressedState()) {
                return true;
            }
        }
        return false;
    }

    private void removeLongPressCallback() {
        if (this.mPendingCheckForLongPress != null) {
            this.removeCallbacks(this.mPendingCheckForLongPress);
        }
    }

    private boolean hasPendingLongPressCallback() {
        if (this.mPendingCheckForLongPress == null) {
            return false;
        } else {
            AttachInfo attachInfo = this.mAttachInfo;
            return attachInfo == null ? false : attachInfo.mHandler.hasCallbacks(this.mPendingCheckForLongPress);
        }
    }

    private void removePerformClickCallback() {
        if (this.mPerformClick != null) {
            this.removeCallbacks(this.mPerformClick);
        }
    }

    private void removeUnsetPressCallback() {
        if ((this.mPrivateFlags & 16384) != 0 && this.mUnsetPressedState != null) {
            this.setPressed(false);
            this.removeCallbacks(this.mUnsetPressedState);
        }
    }

    private void removeTapCallback() {
        if (this.mPendingCheckForTap != null) {
            this.mPrivateFlags &= -33554433;
            this.removeCallbacks(this.mPendingCheckForTap);
        }
    }

    public void cancelLongPress() {
        this.removeLongPressCallback();
        this.removeTapCallback();
    }

    public boolean performClick() {
        View.ListenerInfo li = this.mListenerInfo;
        boolean result;
        if (li != null && li.mOnClickListener != null) {
            this.playSoundEffect(0);
            li.mOnClickListener.onClick(this);
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean callOnClick() {
        View.ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnClickListener != null) {
            li.mOnClickListener.onClick(this);
            return true;
        } else {
            return false;
        }
    }

    private void checkForLongClick(long delay, float x, float y) {
        if ((this.mViewFlags & 2097152) == 2097152 || (this.mViewFlags & 1073741824) == 1073741824) {
            this.mHasPerformedLongPress = false;
            if (this.mPendingCheckForLongPress == null) {
                this.mPendingCheckForLongPress = new View.CheckForLongPress();
            }
            this.mPendingCheckForLongPress.setAnchor(x, y);
            this.mPendingCheckForLongPress.rememberWindowAttachCount();
            this.mPendingCheckForLongPress.rememberPressedState();
            this.postDelayed(this.mPendingCheckForLongPress, delay);
        }
    }

    public boolean performLongClick() {
        return this.performLongClickInternal(this.mLongClickX, this.mLongClickY);
    }

    public boolean performLongClick(float x, float y) {
        this.mLongClickX = x;
        this.mLongClickY = y;
        boolean handled = this.performLongClick();
        this.mLongClickX = Float.NaN;
        this.mLongClickY = Float.NaN;
        return handled;
    }

    private boolean performLongClickInternal(float x, float y) {
        boolean handled = false;
        View.ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnLongClickListener != null) {
            handled = li.mOnLongClickListener.onLongClick(this);
        }
        if (!handled) {
            boolean isAnchored = !Float.isNaN(x) && !Float.isNaN(y);
            handled = isAnchored ? this.showContextMenu(x, y) : this.showContextMenu();
        }
        if ((this.mViewFlags & 1073741824) == 1073741824 && !handled) {
            handled = this.showLongClickTooltip((int) x, (int) y);
        }
        if (handled) {
            this.performHapticFeedback(0);
        }
        return handled;
    }

    public boolean performContextClick(float x, float y) {
        return this.performContextClick();
    }

    public boolean performContextClick() {
        boolean handled = false;
        View.ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnContextClickListener != null) {
            handled = li.mOnContextClickListener.onContextClick(this);
        }
        if (handled) {
            this.performHapticFeedback(6);
        }
        return handled;
    }

    @Internal
    protected boolean performButtonActionOnTouchDown(@NonNull MotionEvent event) {
        if (this.isLongClickable() && (event.getButtonState() & 2) != 0) {
            this.showContextMenu(event.getX(), event.getY());
            this.mPrivateFlags |= 67108864;
            return true;
        } else {
            return false;
        }
    }

    public final boolean showContextMenu() {
        return this.showContextMenu(Float.NaN, Float.NaN);
    }

    public boolean showContextMenu(float x, float y) {
        ViewParent parent = this.getParent();
        return parent == null ? false : parent.showContextMenuForChild(this, x, y);
    }

    public final ActionMode startActionMode(ActionMode.Callback callback) {
        return this.startActionMode(callback, 0);
    }

    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        ViewParent parent = this.getParent();
        return parent == null ? null : parent.startActionModeForChild(this, callback, type);
    }

    public final void createContextMenu(@NonNull ContextMenu menu) {
        ContextMenu.ContextMenuInfo menuInfo = this.getContextMenuInfo();
        ((MenuBuilder) menu).setCurrentMenuInfo(menuInfo);
        this.onCreateContextMenu(menu);
        View.ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnCreateContextMenuListener != null) {
            li.mOnCreateContextMenuListener.onCreateContextMenu(menu, this, menuInfo);
        }
        ((MenuBuilder) menu).setCurrentMenuInfo(null);
        if (this.mParent != null) {
            this.mParent.createContextMenu(menu);
        }
    }

    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return null;
    }

    protected void onCreateContextMenu(@NonNull ContextMenu menu) {
    }

    public final boolean isTemporarilyDetached() {
        return (this.mPrivateFlags3 & 33554432) != 0;
    }

    @CallSuper
    public void dispatchStartTemporaryDetach() {
        this.mPrivateFlags3 |= 33554432;
        this.onStartTemporaryDetach();
    }

    public void onStartTemporaryDetach() {
        this.removeUnsetPressCallback();
        this.mPrivateFlags |= 67108864;
    }

    @CallSuper
    public void dispatchFinishTemporaryDetach() {
        this.mPrivateFlags3 &= -33554433;
        this.onFinishTemporaryDetach();
    }

    public void onFinishTemporaryDetach() {
    }

    @Nullable
    public final KeyEvent.DispatcherState getKeyDispatcherState() {
        return this.mAttachInfo != null ? this.mAttachInfo.mKeyDispatchState : null;
    }

    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        View.ListenerInfo li = this.mListenerInfo;
        if (li != null && li.mOnKeyListener != null && (this.mViewFlags & 32) == 0 && li.mOnKeyListener.onKey(this, event.getKeyCode(), event)) {
            return true;
        } else {
            return switch(event.getAction()) {
                case 0 ->
                    this.onKeyDown(event.getKeyCode(), event);
                case 1 ->
                    this.onKeyUp(event.getKeyCode(), event);
                default ->
                    false;
            };
        }
    }

    public boolean dispatchKeyShortcutEvent(@NonNull KeyEvent event) {
        return this.onKeyShortcut(event.getKeyCode(), event);
    }

    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == 257 || keyCode == 335) {
            if ((this.mViewFlags & 32) == 32) {
                return true;
            }
            if (event.getRepeatCount() == 0) {
                boolean clickable = (this.mViewFlags & 16384) == 16384 || (this.mViewFlags & 2097152) == 2097152;
                if (clickable || (this.mViewFlags & 1073741824) == 1073741824) {
                    float x = (float) this.getWidth() / 2.0F;
                    float y = (float) this.getHeight() / 2.0F;
                    if (clickable) {
                        this.setPressed(true);
                    }
                    this.checkForLongClick((long) ViewConfiguration.getLongPressTimeout(), x, y);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == 257 || keyCode == 335) {
            if ((this.mViewFlags & 32) == 32) {
                return true;
            }
            if ((this.mViewFlags & 16384) == 16384 && this.isPressed()) {
                this.setPressed(false);
                if (!this.mHasPerformedLongPress) {
                    this.removeLongPressCallback();
                    return this.performClick();
                }
            }
        }
        return false;
    }

    public boolean onKeyShortcut(int keyCode, @NonNull KeyEvent event) {
        return false;
    }

    public void getHitRect(Rect outRect) {
        if (!this.hasIdentityMatrix() && this.mAttachInfo != null) {
            RectF tmpRect = this.mAttachInfo.mTmpTransformRect;
            tmpRect.set(0.0F, 0.0F, (float) this.getWidth(), (float) this.getHeight());
            this.getMatrix().mapRect(tmpRect);
            outRect.set((int) tmpRect.left + this.mLeft, (int) tmpRect.top + this.mTop, (int) tmpRect.right + this.mLeft, (int) tmpRect.bottom + this.mTop);
        } else {
            outRect.set(this.mLeft, this.mTop, this.mRight, this.mBottom);
        }
    }

    final boolean pointInView(float localX, float localY) {
        return this.pointInView(localX, localY, 0.0F);
    }

    @Internal
    public boolean pointInView(float localX, float localY, float slop) {
        return localX >= -slop && localY >= -slop && localX < (float) (this.mRight - this.mLeft) + slop && localY < (float) (this.mBottom - this.mTop) + slop;
    }

    public void getFocusedRect(@NonNull Rect r) {
        this.getDrawingRect(r);
    }

    public boolean getGlobalVisibleRect(@NonNull Rect r, @Nullable Point globalOffset) {
        int width = this.mRight - this.mLeft;
        int height = this.mBottom - this.mTop;
        if (width > 0 && height > 0) {
            r.set(0, 0, width, height);
            if (globalOffset != null) {
                globalOffset.set(-this.mScrollX, -this.mScrollY);
            }
            return this.mParent == null || this.mParent.getChildVisibleRect(this, r, globalOffset);
        } else {
            return false;
        }
    }

    public final boolean getGlobalVisibleRect(@NonNull Rect r) {
        return this.getGlobalVisibleRect(r, null);
    }

    public final boolean getLocalVisibleRect(@NonNull Rect r) {
        Point offset = this.mAttachInfo != null ? this.mAttachInfo.mPoint : new Point();
        if (this.getGlobalVisibleRect(r, offset)) {
            r.offset(-offset.x, -offset.y);
            return true;
        } else {
            return false;
        }
    }

    public void offsetTopAndBottom(int offset) {
        if (offset != 0) {
            this.mTop += offset;
            this.mBottom += offset;
            this.mRenderNode.offsetTopAndBottom(offset);
            this.invalidate();
        }
    }

    public void offsetLeftAndRight(int offset) {
        if (offset != 0) {
            this.mLeft += offset;
            this.mRight += offset;
            this.mRenderNode.offsetLeftAndRight(offset);
            this.invalidate();
        }
    }

    public final Context getContext() {
        return this.mContext;
    }

    public boolean isInTouchMode() {
        return this.mAttachInfo != null ? this.mAttachInfo.mInTouchMode : false;
    }

    public PointerIcon onResolvePointerIcon(@NonNull MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        return !this.isDraggingScrollBar() && !this.isOnScrollbarThumb(x, y) ? null : PointerIcon.getSystemIcon(1000);
    }

    public void setTooltipText(@Nullable CharSequence tooltipText) {
        if (TextUtils.isEmpty(tooltipText)) {
            this.setFlags(0, 1073741824);
            this.hideTooltip();
            this.mTooltipInfo = null;
        } else {
            this.setFlags(1073741824, 1073741824);
            if (this.mTooltipInfo == null) {
                this.mTooltipInfo = new View.TooltipInfo();
                this.mTooltipInfo.mShowTooltipRunnable = this::showHoverTooltip;
                this.mTooltipInfo.mHideTooltipRunnable = this::hideTooltip;
                this.mTooltipInfo.mHoverSlop = ViewConfiguration.get(this.mContext).getScaledHoverSlop();
                this.mTooltipInfo.clearAnchorPos();
            }
            this.mTooltipInfo.mTooltipText = tooltipText;
        }
    }

    @Nullable
    public CharSequence getTooltipText() {
        return this.mTooltipInfo != null ? this.mTooltipInfo.mTooltipText : null;
    }

    private boolean showTooltip(int x, int y, boolean fromLongClick) {
        if (this.mAttachInfo == null || this.mTooltipInfo == null) {
            return false;
        } else if (fromLongClick && (this.mViewFlags & 32) != 0) {
            return false;
        } else if (TextUtils.isEmpty(this.mTooltipInfo.mTooltipText)) {
            return false;
        } else {
            this.hideTooltip();
            this.mTooltipInfo.mTooltipFromLongClick = fromLongClick;
            this.mTooltipInfo.mTooltipPopup = new TooltipPopup(this.getContext());
            boolean fromTouch = (this.mPrivateFlags3 & 131072) == 131072;
            this.mTooltipInfo.mTooltipPopup.show(this, x, y, fromTouch, this.mTooltipInfo.mTooltipText);
            this.mAttachInfo.mTooltipHost = this;
            return true;
        }
    }

    void hideTooltip() {
        if (this.mTooltipInfo != null) {
            this.removeCallbacks(this.mTooltipInfo.mShowTooltipRunnable);
            if (this.mTooltipInfo.mTooltipPopup != null) {
                this.mTooltipInfo.mTooltipPopup.hide();
                this.mTooltipInfo.mTooltipPopup = null;
                this.mTooltipInfo.mTooltipFromLongClick = false;
                this.mTooltipInfo.clearAnchorPos();
                if (this.mAttachInfo != null) {
                    this.mAttachInfo.mTooltipHost = null;
                }
            }
        }
    }

    private boolean showLongClickTooltip(int x, int y) {
        this.removeCallbacks(this.mTooltipInfo.mShowTooltipRunnable);
        this.removeCallbacks(this.mTooltipInfo.mHideTooltipRunnable);
        return this.showTooltip(x, y, true);
    }

    private void showHoverTooltip() {
        this.showTooltip(this.mTooltipInfo.mAnchorX, this.mTooltipInfo.mAnchorY, false);
    }

    boolean dispatchTooltipHoverEvent(@NonNull MotionEvent event) {
        if (this.mTooltipInfo == null) {
            return false;
        } else {
            switch(event.getAction()) {
                case 7:
                    if ((this.mViewFlags & 1073741824) == 1073741824) {
                        if (!this.mTooltipInfo.mTooltipFromLongClick && this.mTooltipInfo.updateAnchorPos(event)) {
                            if (this.mTooltipInfo.mTooltipPopup == null) {
                                this.removeCallbacks(this.mTooltipInfo.mShowTooltipRunnable);
                                this.postDelayed(this.mTooltipInfo.mShowTooltipRunnable, (long) ViewConfiguration.getHoverTooltipShowTimeout());
                            }
                            int timeout = ViewConfiguration.getHoverTooltipHideTimeout();
                            this.removeCallbacks(this.mTooltipInfo.mHideTooltipRunnable);
                            this.postDelayed(this.mTooltipInfo.mHideTooltipRunnable, (long) timeout);
                        }
                        return true;
                    }
                    break;
                case 10:
                    this.mTooltipInfo.clearAnchorPos();
                    if (!this.mTooltipInfo.mTooltipFromLongClick) {
                        this.hideTooltip();
                    }
            }
            return false;
        }
    }

    void handleTooltipKey(KeyEvent event) {
        switch(event.getAction()) {
            case 0:
                if (event.getRepeatCount() == 0) {
                    this.hideTooltip();
                }
                break;
            case 1:
                this.handleTooltipUp();
        }
    }

    private void handleTooltipUp() {
        if (this.mTooltipInfo != null && this.mTooltipInfo.mTooltipPopup != null) {
            this.removeCallbacks(this.mTooltipInfo.mHideTooltipRunnable);
            this.postDelayed(this.mTooltipInfo.mHideTooltipRunnable, (long) ViewConfiguration.getLongPressTooltipHideTimeout());
        }
    }

    @Internal
    public View getTooltipView() {
        return this.mTooltipInfo != null && this.mTooltipInfo.mTooltipPopup != null ? this.mTooltipInfo.mTooltipPopup.getContentView() : null;
    }

    public final void setTransitionVisibility(int visibility) {
        this.mViewFlags = this.mViewFlags & -13 | visibility;
    }

    public final void setTransitionName(String transitionName) {
        this.mTransitionName = transitionName;
    }

    public String getTransitionName() {
        return this.mTransitionName;
    }

    public String toString() {
        StringBuilder out = new StringBuilder(128);
        out.append(this.getClass().getName());
        out.append('{');
        out.append(Integer.toHexString(System.identityHashCode(this)));
        out.append(' ');
        switch(this.mViewFlags & 12) {
            case 0:
                out.append('V');
                break;
            case 4:
                out.append('I');
                break;
            case 8:
                out.append('G');
                break;
            default:
                out.append('.');
        }
        out.append((char) ((this.mViewFlags & 1) == 1 ? 'F' : '.'));
        out.append((char) ((this.mViewFlags & 32) == 0 ? 'E' : '.'));
        out.append((char) ((this.mViewFlags & 128) == 128 ? '.' : 'D'));
        out.append((char) ((this.mViewFlags & 256) != 0 ? 'H' : '.'));
        out.append((char) ((this.mViewFlags & 512) != 0 ? 'V' : '.'));
        out.append((char) ((this.mViewFlags & 16384) != 0 ? 'C' : '.'));
        out.append((char) ((this.mViewFlags & 2097152) != 0 ? 'L' : '.'));
        out.append((char) ((this.mViewFlags & 8388608) != 0 ? 'X' : '.'));
        out.append(' ');
        out.append((char) ((this.mPrivateFlags & 8) != 0 ? 'R' : '.'));
        out.append((char) ((this.mPrivateFlags & 2) != 0 ? 'F' : '.'));
        out.append((char) ((this.mPrivateFlags & 4) != 0 ? 'S' : '.'));
        if ((this.mPrivateFlags & 33554432) != 0) {
            out.append('p');
        } else {
            out.append((char) ((this.mPrivateFlags & 16384) != 0 ? 'P' : '.'));
        }
        out.append((char) ((this.mPrivateFlags & 268435456) != 0 ? 'H' : '.'));
        out.append((char) ((this.mPrivateFlags & 1073741824) != 0 ? 'A' : '.'));
        out.append((char) ((this.mPrivateFlags & -2147483648) != 0 ? 'I' : '.'));
        out.append((char) ((this.mPrivateFlags & 2097152) != 0 ? 'D' : '.'));
        out.append(' ');
        out.append(this.mLeft);
        out.append(',');
        out.append(this.mTop);
        out.append('-');
        out.append(this.mRight);
        out.append(',');
        out.append(this.mBottom);
        int id = this.getId();
        if (id != -1) {
            out.append(" #");
            out.append(Integer.toHexString(id));
        }
        out.append("}");
        return out.toString();
    }

    private final class CheckForLongPress implements Runnable {

        private int mOriginalWindowAttachCount;

        private float mX;

        private float mY;

        private boolean mOriginalPressedState;

        public void run() {
            if (this.mOriginalPressedState == View.this.isPressed() && View.this.mParent != null && this.mOriginalWindowAttachCount == View.this.mWindowAttachCount && View.this.performLongClick(this.mX, this.mY)) {
                View.this.mHasPerformedLongPress = true;
            }
        }

        public void setAnchor(float x, float y) {
            this.mX = x;
            this.mY = y;
        }

        public void rememberWindowAttachCount() {
            this.mOriginalWindowAttachCount = View.this.mWindowAttachCount;
        }

        public void rememberPressedState() {
            this.mOriginalPressedState = View.this.isPressed();
        }
    }

    private final class CheckForTap implements Runnable {

        public float x;

        public float y;

        public void run() {
            View.this.mPrivateFlags &= -33554433;
            View.this.setPressed(this.x, this.y);
            long delay = (long) (ViewConfiguration.getLongPressTimeout() - ViewConfiguration.getTapTimeout());
            View.this.checkForLongClick(delay, this.x, this.y);
        }
    }

    public static class DragShadow {

        private final WeakReference<View> viewRef;

        public DragShadow(View view) {
            this.viewRef = new WeakReference(view);
        }

        public DragShadow() {
            this.viewRef = new WeakReference(null);
        }

        @Nullable
        public final View getView() {
            return (View) this.viewRef.get();
        }

        public void onProvideShadowCenter(@NonNull Point outShadowCenter) {
        }

        public void onDrawShadow(@NonNull Canvas canvas) {
            View view = (View) this.viewRef.get();
            if (view != null) {
                view.onDraw(canvas);
            } else {
                ModernUI.LOGGER.error(View.VIEW_MARKER, "No view found on draw shadow");
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusDirection {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusRealDirection {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Focusable {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusableMode {
    }

    private static class ForegroundInfo {

        private Drawable mDrawable;

        private int mGravity = 119;

        private boolean mInsidePadding = true;

        private boolean mBoundsChanged = true;

        private final Rect mSelfBounds = new Rect();

        private final Rect mOverlayBounds = new Rect();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LayoutDir {
    }

    static class ListenerInfo {

        private View.OnTouchListener mOnTouchListener;

        private View.OnHoverListener mOnHoverListener;

        private View.OnGenericMotionListener mOnGenericMotionListener;

        private View.OnKeyListener mOnKeyListener;

        private View.OnClickListener mOnClickListener;

        private View.OnLongClickListener mOnLongClickListener;

        private View.OnDragListener mOnDragListener;

        private View.OnContextClickListener mOnContextClickListener;

        private View.OnCreateContextMenuListener mOnCreateContextMenuListener;

        protected View.OnScrollChangeListener mOnScrollChangeListener;

        private View.OnFocusChangeListener mOnFocusChangeListener;

        private ArrayList<View.OnLayoutChangeListener> mOnLayoutChangeListeners;

        private CopyOnWriteArrayList<View.OnAttachStateChangeListener> mOnAttachStateChangeListeners;
    }

    private static class MatchIdPredicate implements Predicate<View> {

        public int mId;

        public boolean test(View view) {
            return view.mID == this.mId;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NestedScrollType {
    }

    public interface OnAttachStateChangeListener {

        void onViewAttachedToWindow(View var1);

        void onViewDetachedFromWindow(View var1);
    }

    @FunctionalInterface
    public interface OnClickListener {

        void onClick(View var1);
    }

    @FunctionalInterface
    public interface OnContextClickListener {

        boolean onContextClick(View var1);
    }

    @FunctionalInterface
    public interface OnCreateContextMenuListener {

        void onCreateContextMenu(ContextMenu var1, View var2, ContextMenu.ContextMenuInfo var3);
    }

    @FunctionalInterface
    public interface OnDragListener {

        boolean onDrag(View var1, DragEvent var2);
    }

    @FunctionalInterface
    public interface OnFocusChangeListener {

        void onFocusChange(View var1, boolean var2);
    }

    @FunctionalInterface
    public interface OnGenericMotionListener {

        boolean onGenericMotion(View var1, MotionEvent var2);
    }

    @FunctionalInterface
    public interface OnHoverListener {

        boolean onHover(View var1, MotionEvent var2);
    }

    @FunctionalInterface
    public interface OnKeyListener {

        boolean onKey(View var1, int var2, KeyEvent var3);
    }

    @FunctionalInterface
    public interface OnLayoutChangeListener {

        void onLayoutChange(View var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9);
    }

    @FunctionalInterface
    public interface OnLongClickListener {

        boolean onLongClick(View var1);
    }

    @FunctionalInterface
    public interface OnScrollChangeListener {

        void onScrollChange(View var1, int var2, int var3, int var4, int var5);
    }

    @FunctionalInterface
    public interface OnTouchListener {

        boolean onTouch(View var1, MotionEvent var2);
    }

    private final class PerformClick implements Runnable {

        public void run() {
            View.this.performClick();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ResolvedLayoutDir {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollAxis {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollBarStyle {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollIndicators {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TextAlignment {
    }

    private static class TooltipInfo {

        @Nullable
        CharSequence mTooltipText;

        int mAnchorX;

        int mAnchorY;

        @Nullable
        TooltipPopup mTooltipPopup;

        boolean mTooltipFromLongClick;

        Runnable mShowTooltipRunnable;

        Runnable mHideTooltipRunnable;

        int mHoverSlop;

        private boolean updateAnchorPos(MotionEvent event) {
            int newAnchorX = (int) event.getX();
            int newAnchorY = (int) event.getY();
            if (Math.abs(newAnchorX - this.mAnchorX) <= this.mHoverSlop && Math.abs(newAnchorY - this.mAnchorY) <= this.mHoverSlop) {
                return false;
            } else {
                this.mAnchorX = newAnchorX;
                this.mAnchorY = newAnchorY;
                return true;
            }
        }

        private void clearAnchorPos() {
            this.mAnchorX = Integer.MAX_VALUE;
            this.mAnchorY = Integer.MAX_VALUE;
        }
    }

    private final class UnsetPressedState implements Runnable {

        public void run() {
            View.this.setPressed(false);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {
    }
}