package icyllis.modernui.widget;

import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Clipboard;
import icyllis.modernui.core.Context;
import icyllis.modernui.core.Core;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.text.FontMetricsInt;
import icyllis.modernui.graphics.text.LineBreakConfig;
import icyllis.modernui.text.BoringLayout;
import icyllis.modernui.text.DynamicLayout;
import icyllis.modernui.text.Editable;
import icyllis.modernui.text.InputFilter;
import icyllis.modernui.text.Layout;
import icyllis.modernui.text.PrecomputedText;
import icyllis.modernui.text.Selection;
import icyllis.modernui.text.SpanWatcher;
import icyllis.modernui.text.Spannable;
import icyllis.modernui.text.Spanned;
import icyllis.modernui.text.SpannedString;
import icyllis.modernui.text.StaticLayout;
import icyllis.modernui.text.TextDirectionHeuristic;
import icyllis.modernui.text.TextDirectionHeuristics;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.text.TextUtils;
import icyllis.modernui.text.TextWatcher;
import icyllis.modernui.text.Typeface;
import icyllis.modernui.text.method.ArrowKeyMovementMethod;
import icyllis.modernui.text.method.MovementMethod;
import icyllis.modernui.text.method.PasswordTransformationMethod;
import icyllis.modernui.text.method.SingleLineTransformationMethod;
import icyllis.modernui.text.method.TextKeyListener;
import icyllis.modernui.text.method.TransformationMethod;
import icyllis.modernui.text.method.WordIterator;
import icyllis.modernui.text.style.CharacterStyle;
import icyllis.modernui.text.style.ClickableSpan;
import icyllis.modernui.text.style.ParagraphStyle;
import icyllis.modernui.text.style.UpdateAppearance;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.view.ContextMenu;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.MeasureSpec;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.PointerIcon;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.view.ViewTreeObserver;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.jetbrains.annotations.VisibleForTesting;

public class TextView extends View implements ViewTreeObserver.OnPreDrawListener {

    static final int ID_SELECT_ALL = 16908319;

    static final int ID_UNDO = 16908338;

    static final int ID_REDO = 16908339;

    static final int ID_CUT = 16908320;

    static final int ID_COPY = 16908321;

    static final int ID_PASTE = 16908322;

    @VisibleForTesting
    public static final BoringLayout.Metrics UNKNOWN_BORING = new BoringLayout.Metrics();

    private static final int LINES = 1;

    private static final int PIXELS = 2;

    private static final int MAX_LENGTH_FOR_SINGLE_LINE_EDIT_TEXT = 5000;

    private InputFilter.LengthFilter mSingleLineLengthFilter = null;

    static final int VERY_WIDE = 1048576;

    private static final InputFilter[] NO_FILTERS = new InputFilter[0];

    private static final Spanned EMPTY_SPANNED = new SpannedString("");

    private static final int CHANGE_WATCHER_PRIORITY = 100;

    private static final int DEFAULT_LINE_BREAK_STYLE = 0;

    private static final int DEFAULT_LINE_BREAK_WORD_STYLE = 0;

    private ColorStateList mTextColor;

    private ColorStateList mHintTextColor;

    private ColorStateList mLinkTextColor;

    private int mCurTextColor;

    private int mCurHintTextColor;

    private Editable.Factory mEditableFactory = Editable.DEFAULT_FACTORY;

    private Spannable.Factory mSpannableFactory = Spannable.DEFAULT_FACTORY;

    private boolean mPreDrawRegistered;

    private boolean mPreDrawListenerDetached;

    private boolean mPreventDefaultMovement;

    @Nullable
    private TextUtils.TruncateAt mEllipsize;

    TextView.Drawables mDrawables;

    private int mLastLayoutDirection = -1;

    @NonNull
    private CharSequence mText = "";

    @Nullable
    private Spannable mSpannable;

    @Nullable
    private PrecomputedText mPrecomputed;

    @NonNull
    private CharSequence mTransformed = "";

    @NonNull
    private TextView.BufferType mBufferType = TextView.BufferType.NORMAL;

    @Nullable
    private CharSequence mHint;

    private Layout mHintLayout;

    @Nullable
    private MovementMethod mMovement;

    @Nullable
    private TransformationMethod mTransformation;

    private TextView.ChangeWatcher mChangeWatcher;

    private ArrayList<TextWatcher> mListeners;

    private final TextPaint mTextPaint = new TextPaint();

    private Layout mLayout;

    private int mLineBreakStyle = 0;

    private int mLineBreakWordStyle = 0;

    boolean mUseFallbackLineSpacing = true;

    private int mGravity = 8388659;

    private boolean mHorizontallyScrolling;

    private int mAutoLinkMask;

    private boolean mLinksClickable = true;

    private int mMaximum = Integer.MAX_VALUE;

    private int mMaxMode = 1;

    private int mMinimum = 0;

    private int mMinMode = 1;

    private int mOldMaximum = this.mMaximum;

    private int mOldMaxMode = this.mMaxMode;

    private int mMaxWidth = Integer.MAX_VALUE;

    private int mMinWidth = 0;

    private boolean mSingleLine;

    private int mDesiredHeightAtMeasure = -1;

    private boolean mIncludePad = true;

    private int mDeferScroll = -1;

    private Rect mTempRect;

    private BoringLayout.Metrics mBoring;

    private BoringLayout.Metrics mHintBoring;

    private BoringLayout mSavedLayout;

    private BoringLayout mSavedHintLayout;

    private TextDirectionHeuristic mTextDir;

    @NonNull
    private InputFilter[] mFilters = NO_FILTERS;

    int mHighlightColor = 1714664933;

    private FloatArrayList mHighlightPath;

    private boolean mHighlightPathBogus = true;

    private Editor mEditor;

    public TextView(Context context) {
        super(context);
        this.setTextSize(16.0F);
        this.setTextColor(-1);
    }

    @Nullable
    public final MovementMethod getMovementMethod() {
        return this.mMovement;
    }

    public final void setMovementMethod(@Nullable MovementMethod movement) {
        if (this.mMovement != movement) {
            this.mMovement = movement;
            if (movement != null && this.mSpannable == null) {
                this.setText(this.mText);
            }
            if (this.mMovement == null && (this.mEditor == null || this.mBufferType != TextView.BufferType.EDITABLE)) {
                this.setFocusable(16);
                this.setClickable(false);
                this.setLongClickable(false);
            } else {
                this.setFocusable(1);
                this.setClickable(true);
                this.setLongClickable(true);
            }
        }
    }

    @Nullable
    public final TransformationMethod getTransformationMethod() {
        return this.mTransformation;
    }

    public final void setTransformationMethod(@Nullable TransformationMethod method) {
        if (method != this.mTransformation) {
            if (this.mTransformation != null && this.mSpannable != null) {
                this.mSpannable.removeSpan(this.mTransformation);
            }
            this.mTransformation = method;
            this.setText(this.mText);
            this.mTextDir = this.getTextDirectionHeuristic();
        }
    }

    public int getCompoundPaddingTop() {
        TextView.Drawables dr = this.mDrawables;
        return dr != null && dr.mShowing[1] != null ? this.mPaddingTop + dr.mDrawablePadding + dr.mDrawableSizeTop : this.mPaddingTop;
    }

    public int getCompoundPaddingBottom() {
        TextView.Drawables dr = this.mDrawables;
        return dr != null && dr.mShowing[3] != null ? this.mPaddingBottom + dr.mDrawablePadding + dr.mDrawableSizeBottom : this.mPaddingBottom;
    }

    public int getCompoundPaddingLeft() {
        TextView.Drawables dr = this.mDrawables;
        return dr != null && dr.mShowing[0] != null ? this.mPaddingLeft + dr.mDrawablePadding + dr.mDrawableSizeLeft : this.mPaddingLeft;
    }

    public int getCompoundPaddingRight() {
        TextView.Drawables dr = this.mDrawables;
        return dr != null && dr.mShowing[2] != null ? this.mPaddingRight + dr.mDrawablePadding + dr.mDrawableSizeRight : this.mPaddingRight;
    }

    public int getCompoundPaddingStart() {
        this.resolveDrawables();
        return this.isLayoutRtl() ? this.getCompoundPaddingRight() : this.getCompoundPaddingLeft();
    }

    public int getCompoundPaddingEnd() {
        this.resolveDrawables();
        return this.isLayoutRtl() ? this.getCompoundPaddingLeft() : this.getCompoundPaddingRight();
    }

    public int getExtendedPaddingTop() {
        if (this.mMaxMode != 1) {
            return this.getCompoundPaddingTop();
        } else {
            if (this.mLayout == null) {
                this.assumeLayout();
            }
            if (this.mLayout.getLineCount() <= this.mMaximum) {
                return this.getCompoundPaddingTop();
            } else {
                int top = this.getCompoundPaddingTop();
                int bottom = this.getCompoundPaddingBottom();
                int viewht = this.getHeight() - top - bottom;
                int layoutht = this.mLayout.getLineTop(this.mMaximum);
                if (layoutht >= viewht) {
                    return top;
                } else {
                    int gravity = this.mGravity & 112;
                    if (gravity == 48) {
                        return top;
                    } else {
                        return gravity == 80 ? top + viewht - layoutht : top + (viewht - layoutht) / 2;
                    }
                }
            }
        }
    }

    public int getExtendedPaddingBottom() {
        if (this.mMaxMode != 1) {
            return this.getCompoundPaddingBottom();
        } else {
            if (this.mLayout == null) {
                this.assumeLayout();
            }
            if (this.mLayout.getLineCount() <= this.mMaximum) {
                return this.getCompoundPaddingBottom();
            } else {
                int top = this.getCompoundPaddingTop();
                int bottom = this.getCompoundPaddingBottom();
                int viewht = this.getHeight() - top - bottom;
                int layoutht = this.mLayout.getLineTop(this.mMaximum);
                if (layoutht >= viewht) {
                    return bottom;
                } else {
                    int gravity = this.mGravity & 112;
                    if (gravity == 48) {
                        return bottom + viewht - layoutht;
                    } else {
                        return gravity == 80 ? bottom : bottom + (viewht - layoutht) / 2;
                    }
                }
            }
        }
    }

    public int getTotalPaddingLeft() {
        return this.getCompoundPaddingLeft();
    }

    public int getTotalPaddingRight() {
        return this.getCompoundPaddingRight();
    }

    public int getTotalPaddingStart() {
        return this.getCompoundPaddingStart();
    }

    public int getTotalPaddingEnd() {
        return this.getCompoundPaddingEnd();
    }

    public int getTotalPaddingTop() {
        return this.getExtendedPaddingTop() + this.getVerticalOffset(true);
    }

    public int getTotalPaddingBottom() {
        return this.getExtendedPaddingBottom() + this.getBottomVerticalOffset(true);
    }

    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        TextView.Drawables dr = this.mDrawables;
        if (dr != null) {
            if (dr.mDrawableStart != null) {
                dr.mDrawableStart.setCallback(null);
            }
            dr.mDrawableStart = null;
            if (dr.mDrawableEnd != null) {
                dr.mDrawableEnd.setCallback(null);
            }
            dr.mDrawableEnd = null;
            dr.mDrawableSizeStart = dr.mDrawableHeightStart = 0;
            dr.mDrawableSizeEnd = dr.mDrawableHeightEnd = 0;
        }
        boolean drawables = left != null || top != null || right != null || bottom != null;
        if (!drawables) {
            if (dr != null) {
                if (!dr.hasMetadata()) {
                    this.mDrawables = null;
                } else {
                    for (int i = dr.mShowing.length - 1; i >= 0; i--) {
                        if (dr.mShowing[i] != null) {
                            dr.mShowing[i].setCallback(null);
                        }
                        dr.mShowing[i] = null;
                    }
                    dr.mDrawableSizeLeft = dr.mDrawableHeightLeft = 0;
                    dr.mDrawableSizeRight = dr.mDrawableHeightRight = 0;
                    dr.mDrawableSizeTop = dr.mDrawableWidthTop = 0;
                    dr.mDrawableSizeBottom = dr.mDrawableWidthBottom = 0;
                }
            }
        } else {
            if (dr == null) {
                this.mDrawables = dr = new TextView.Drawables();
            }
            this.mDrawables.mOverride = false;
            if (dr.mShowing[0] != left && dr.mShowing[0] != null) {
                dr.mShowing[0].setCallback(null);
            }
            dr.mShowing[0] = left;
            if (dr.mShowing[1] != top && dr.mShowing[1] != null) {
                dr.mShowing[1].setCallback(null);
            }
            dr.mShowing[1] = top;
            if (dr.mShowing[2] != right && dr.mShowing[2] != null) {
                dr.mShowing[2].setCallback(null);
            }
            dr.mShowing[2] = right;
            if (dr.mShowing[3] != bottom && dr.mShowing[3] != null) {
                dr.mShowing[3].setCallback(null);
            }
            dr.mShowing[3] = bottom;
            Rect compoundRect = dr.mCompoundRect;
            int[] state = this.getDrawableState();
            if (left != null) {
                left.setState(state);
                left.copyBounds(compoundRect);
                left.setCallback(this);
                dr.mDrawableSizeLeft = compoundRect.width();
                dr.mDrawableHeightLeft = compoundRect.height();
            } else {
                dr.mDrawableSizeLeft = dr.mDrawableHeightLeft = 0;
            }
            if (right != null) {
                right.setState(state);
                right.copyBounds(compoundRect);
                right.setCallback(this);
                dr.mDrawableSizeRight = compoundRect.width();
                dr.mDrawableHeightRight = compoundRect.height();
            } else {
                dr.mDrawableSizeRight = dr.mDrawableHeightRight = 0;
            }
            if (top != null) {
                top.setState(state);
                top.copyBounds(compoundRect);
                top.setCallback(this);
                dr.mDrawableSizeTop = compoundRect.height();
                dr.mDrawableWidthTop = compoundRect.width();
            } else {
                dr.mDrawableSizeTop = dr.mDrawableWidthTop = 0;
            }
            if (bottom != null) {
                bottom.setState(state);
                bottom.copyBounds(compoundRect);
                bottom.setCallback(this);
                dr.mDrawableSizeBottom = compoundRect.height();
                dr.mDrawableWidthBottom = compoundRect.width();
            } else {
                dr.mDrawableSizeBottom = dr.mDrawableWidthBottom = 0;
            }
        }
        if (dr != null) {
            dr.mDrawableLeftInitial = left;
            dr.mDrawableRightInitial = right;
        }
        this.resetResolvedDrawables();
        this.resolveDrawables();
        this.invalidate();
        this.requestLayout();
    }

    public void setCompoundDrawablesWithIntrinsicBounds(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        if (left != null) {
            left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
        }
        if (right != null) {
            right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
        }
        if (top != null) {
            top.setBounds(0, 0, top.getIntrinsicWidth(), top.getIntrinsicHeight());
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, bottom.getIntrinsicWidth(), bottom.getIntrinsicHeight());
        }
        this.setCompoundDrawables(left, top, right, bottom);
    }

    public void setCompoundDrawablesRelative(@Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        TextView.Drawables dr = this.mDrawables;
        if (dr != null) {
            if (dr.mShowing[0] != null) {
                dr.mShowing[0].setCallback(null);
            }
            dr.mShowing[0] = dr.mDrawableLeftInitial = null;
            if (dr.mShowing[2] != null) {
                dr.mShowing[2].setCallback(null);
            }
            dr.mShowing[2] = dr.mDrawableRightInitial = null;
            dr.mDrawableSizeLeft = dr.mDrawableHeightLeft = 0;
            dr.mDrawableSizeRight = dr.mDrawableHeightRight = 0;
        }
        boolean drawables = start != null || top != null || end != null || bottom != null;
        if (!drawables) {
            if (dr != null) {
                if (!dr.hasMetadata()) {
                    this.mDrawables = null;
                } else {
                    if (dr.mDrawableStart != null) {
                        dr.mDrawableStart.setCallback(null);
                    }
                    dr.mDrawableStart = null;
                    if (dr.mShowing[1] != null) {
                        dr.mShowing[1].setCallback(null);
                    }
                    dr.mShowing[1] = null;
                    if (dr.mDrawableEnd != null) {
                        dr.mDrawableEnd.setCallback(null);
                    }
                    dr.mDrawableEnd = null;
                    if (dr.mShowing[3] != null) {
                        dr.mShowing[3].setCallback(null);
                    }
                    dr.mShowing[3] = null;
                    dr.mDrawableSizeStart = dr.mDrawableHeightStart = 0;
                    dr.mDrawableSizeEnd = dr.mDrawableHeightEnd = 0;
                    dr.mDrawableSizeTop = dr.mDrawableWidthTop = 0;
                    dr.mDrawableSizeBottom = dr.mDrawableWidthBottom = 0;
                }
            }
        } else {
            if (dr == null) {
                this.mDrawables = dr = new TextView.Drawables();
            }
            this.mDrawables.mOverride = true;
            if (dr.mDrawableStart != start && dr.mDrawableStart != null) {
                dr.mDrawableStart.setCallback(null);
            }
            dr.mDrawableStart = start;
            if (dr.mShowing[1] != top && dr.mShowing[1] != null) {
                dr.mShowing[1].setCallback(null);
            }
            dr.mShowing[1] = top;
            if (dr.mDrawableEnd != end && dr.mDrawableEnd != null) {
                dr.mDrawableEnd.setCallback(null);
            }
            dr.mDrawableEnd = end;
            if (dr.mShowing[3] != bottom && dr.mShowing[3] != null) {
                dr.mShowing[3].setCallback(null);
            }
            dr.mShowing[3] = bottom;
            Rect compoundRect = dr.mCompoundRect;
            int[] state = this.getDrawableState();
            if (start != null) {
                start.setState(state);
                start.copyBounds(compoundRect);
                start.setCallback(this);
                dr.mDrawableSizeStart = compoundRect.width();
                dr.mDrawableHeightStart = compoundRect.height();
            } else {
                dr.mDrawableSizeStart = dr.mDrawableHeightStart = 0;
            }
            if (end != null) {
                end.setState(state);
                end.copyBounds(compoundRect);
                end.setCallback(this);
                dr.mDrawableSizeEnd = compoundRect.width();
                dr.mDrawableHeightEnd = compoundRect.height();
            } else {
                dr.mDrawableSizeEnd = dr.mDrawableHeightEnd = 0;
            }
            if (top != null) {
                top.setState(state);
                top.copyBounds(compoundRect);
                top.setCallback(this);
                dr.mDrawableSizeTop = compoundRect.height();
                dr.mDrawableWidthTop = compoundRect.width();
            } else {
                dr.mDrawableSizeTop = dr.mDrawableWidthTop = 0;
            }
            if (bottom != null) {
                bottom.setState(state);
                bottom.copyBounds(compoundRect);
                bottom.setCallback(this);
                dr.mDrawableSizeBottom = compoundRect.height();
                dr.mDrawableWidthBottom = compoundRect.width();
            } else {
                dr.mDrawableSizeBottom = dr.mDrawableWidthBottom = 0;
            }
        }
        this.resetResolvedDrawables();
        this.resolveDrawables();
        this.invalidate();
        this.requestLayout();
    }

    public void setCompoundDrawablesRelativeWithIntrinsicBounds(@Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        if (start != null) {
            start.setBounds(0, 0, start.getIntrinsicWidth(), start.getIntrinsicHeight());
        }
        if (end != null) {
            end.setBounds(0, 0, end.getIntrinsicWidth(), end.getIntrinsicHeight());
        }
        if (top != null) {
            top.setBounds(0, 0, top.getIntrinsicWidth(), top.getIntrinsicHeight());
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, bottom.getIntrinsicWidth(), bottom.getIntrinsicHeight());
        }
        this.setCompoundDrawablesRelative(start, top, end, bottom);
    }

    @NonNull
    public Drawable[] getCompoundDrawables() {
        TextView.Drawables dr = this.mDrawables;
        return dr != null ? (Drawable[]) dr.mShowing.clone() : new Drawable[] { null, null, null, null };
    }

    @NonNull
    public Drawable[] getCompoundDrawablesRelative() {
        TextView.Drawables dr = this.mDrawables;
        return dr != null ? new Drawable[] { dr.mDrawableStart, dr.mShowing[1], dr.mDrawableEnd, dr.mShowing[3] } : new Drawable[] { null, null, null, null };
    }

    public void setCompoundDrawablePadding(int pad) {
        TextView.Drawables dr = this.mDrawables;
        if (pad == 0) {
            if (dr != null) {
                dr.mDrawablePadding = pad;
            }
        } else {
            if (dr == null) {
                this.mDrawables = dr = new TextView.Drawables();
            }
            dr.mDrawablePadding = pad;
        }
        this.invalidate();
        this.requestLayout();
    }

    public int getCompoundDrawablePadding() {
        TextView.Drawables dr = this.mDrawables;
        return dr != null ? dr.mDrawablePadding : 0;
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        if (left != this.mPaddingLeft || right != this.mPaddingRight || top != this.mPaddingTop || bottom != this.mPaddingBottom) {
            this.nullLayouts();
        }
        super.setPadding(left, top, right, bottom);
        this.invalidate();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        if (start != this.getPaddingStart() || end != this.getPaddingEnd() || top != this.mPaddingTop || bottom != this.mPaddingBottom) {
            this.nullLayouts();
        }
        super.setPaddingRelative(start, top, end, bottom);
        this.invalidate();
    }

    public void setFirstBaselineToTopHeight(int firstBaselineToTopHeight) {
        if (firstBaselineToTopHeight < 0) {
            firstBaselineToTopHeight = 0;
        }
        FontMetricsInt fontMetrics = this.getPaint().getFontMetricsInt();
        int fontMetricsTop = -fontMetrics.ascent;
        if (firstBaselineToTopHeight > fontMetricsTop) {
            int paddingTop = firstBaselineToTopHeight - fontMetricsTop;
            this.setPadding(this.getPaddingLeft(), paddingTop, this.getPaddingRight(), this.getPaddingBottom());
        }
    }

    public void setLastBaselineToBottomHeight(int lastBaselineToBottomHeight) {
        if (lastBaselineToBottomHeight < 0) {
            lastBaselineToBottomHeight = 0;
        }
        FontMetricsInt fontMetrics = this.getPaint().getFontMetricsInt();
        int fontMetricsBottom = fontMetrics.descent;
        if (lastBaselineToBottomHeight > fontMetricsBottom) {
            int paddingBottom = lastBaselineToBottomHeight - fontMetricsBottom;
            this.setPadding(this.getPaddingLeft(), this.getPaddingTop(), this.getPaddingRight(), paddingBottom);
        }
    }

    public int getFirstBaselineToTopHeight() {
        return this.getPaddingTop() - this.getPaint().getFontMetricsInt().ascent;
    }

    public int getLastBaselineToBottomHeight() {
        return this.getPaddingBottom() + this.getPaint().getFontMetricsInt().descent;
    }

    @NonNull
    public Locale getTextLocale() {
        return this.mTextPaint.getTextLocale();
    }

    public void setTextLocale(@NonNull Locale locale) {
        this.mTextPaint.setTextLocale(locale);
        if (this.mLayout != null) {
            this.nullLayouts();
            this.requestLayout();
            this.invalidate();
        }
    }

    public float getTextSize() {
        return this.mTextPaint.getTextSize();
    }

    public void setTextSize(float size) {
        int s = this.sp(size);
        if ((float) s != this.mTextPaint.getTextSize()) {
            this.mTextPaint.setTextSize((float) s);
            if (this.mLayout != null) {
                this.nullLayouts();
                this.requestLayout();
                this.invalidate();
            }
        }
    }

    public void setTextStyle(int style) {
        if (style != this.mTextPaint.getTextStyle()) {
            this.mTextPaint.setTextStyle(style);
            if (this.mLayout != null) {
                this.nullLayouts();
                this.requestLayout();
                this.invalidate();
            }
        }
    }

    public int getTextStyle() {
        return this.mTextPaint.getTextStyle();
    }

    public void setTypeface(@NonNull Typeface tf) {
        if (this.mTextPaint.getTypeface() != tf) {
            this.mTextPaint.setTypeface(tf);
            if (this.mLayout != null) {
                this.nullLayouts();
                this.requestLayout();
                this.invalidate();
            }
        }
    }

    @NonNull
    public Typeface getTypeface() {
        return this.mTextPaint.getTypeface();
    }

    public int getLineHeight() {
        return this.mTextPaint.getFontMetricsInt(null);
    }

    public void setFallbackLineSpacing(boolean enabled) {
        if (this.mUseFallbackLineSpacing != enabled) {
            this.mUseFallbackLineSpacing = enabled;
            if (this.mLayout != null) {
                this.nullLayouts();
                this.requestLayout();
                this.invalidate();
            }
        }
    }

    public boolean isFallbackLineSpacing() {
        return this.mUseFallbackLineSpacing;
    }

    public void setLineBreakStyle(int lineBreakStyle) {
        if (this.mLineBreakStyle != lineBreakStyle) {
            this.mLineBreakStyle = lineBreakStyle;
            if (this.mLayout != null) {
                this.nullLayouts();
                this.requestLayout();
                this.invalidate();
            }
        }
    }

    public void setLineBreakWordStyle(int lineBreakWordStyle) {
        if (this.mLineBreakWordStyle != lineBreakWordStyle) {
            this.mLineBreakWordStyle = lineBreakWordStyle;
            if (this.mLayout != null) {
                this.nullLayouts();
                this.requestLayout();
                this.invalidate();
            }
        }
    }

    public int getLineBreakStyle() {
        return this.mLineBreakStyle;
    }

    public int getLineBreakWordStyle() {
        return this.mLineBreakWordStyle;
    }

    @NonNull
    public PrecomputedText.Params getTextMetricsParams() {
        return new PrecomputedText.Params(new TextPaint(this.mTextPaint), LineBreakConfig.getLineBreakConfig(this.mLineBreakStyle, this.mLineBreakWordStyle), this.getTextDirectionHeuristic());
    }

    public void setTextMetricsParams(@NonNull PrecomputedText.Params params) {
        this.mTextPaint.set(params.getTextPaint());
        this.mTextDir = params.getTextDirection();
        LineBreakConfig lineBreakConfig = params.getLineBreakConfig();
        this.mLineBreakStyle = lineBreakConfig.getLineBreakStyle();
        this.mLineBreakWordStyle = lineBreakConfig.getLineBreakWordStyle();
        if (this.mLayout != null) {
            this.nullLayouts();
            this.requestLayout();
            this.invalidate();
        }
    }

    public void setTextColor(int color) {
        this.mTextColor = ColorStateList.valueOf(color);
        this.updateTextColors();
    }

    public void setTextColor(ColorStateList colors) {
        if (colors == null) {
            throw new NullPointerException();
        } else {
            this.mTextColor = colors;
            this.updateTextColors();
        }
    }

    public final ColorStateList getTextColors() {
        return this.mTextColor;
    }

    public final int getCurrentTextColor() {
        return this.mCurTextColor;
    }

    public void setHighlightColor(int color) {
        if (this.mHighlightColor != color) {
            this.mHighlightColor = color;
            this.invalidate();
        }
    }

    public int getHighlightColor() {
        return this.mHighlightColor;
    }

    @NonNull
    public TextPaint getPaint() {
        return this.mTextPaint;
    }

    public final Layout getLayout() {
        return this.mLayout;
    }

    public final void setAutoLinkMask(int mask) {
        this.mAutoLinkMask = mask;
    }

    public final int getAutoLinkMask() {
        return this.mAutoLinkMask;
    }

    public final void setLinksClickable(boolean whether) {
        this.mLinksClickable = whether;
    }

    public final boolean getLinksClickable() {
        return this.mLinksClickable;
    }

    public final void setHintTextColor(int color) {
        this.mHintTextColor = ColorStateList.valueOf(color);
        this.updateTextColors();
    }

    public final void setHintTextColor(ColorStateList colors) {
        this.mHintTextColor = colors;
        this.updateTextColors();
    }

    public final ColorStateList getHintTextColors() {
        return this.mHintTextColor;
    }

    public final int getCurrentHintTextColor() {
        return this.mCurHintTextColor;
    }

    public final void setLinkTextColor(int color) {
        this.mLinkTextColor = ColorStateList.valueOf(color);
        this.updateTextColors();
    }

    public final void setLinkTextColor(ColorStateList colors) {
        this.mLinkTextColor = colors;
        this.updateTextColors();
    }

    public final ColorStateList getLinkTextColors() {
        return this.mLinkTextColor;
    }

    public void setGravity(int gravity) {
        if ((gravity & 8388615) == 0) {
            gravity |= 8388611;
        }
        if ((gravity & 112) == 0) {
            gravity |= 48;
        }
        boolean newLayout = (gravity & 8388615) != (this.mGravity & 8388615);
        if (gravity != this.mGravity) {
            this.invalidate();
        }
        this.mGravity = gravity;
        if (this.mLayout != null && newLayout) {
            int want = this.mLayout.getWidth();
            int hintWant = this.mHintLayout == null ? 0 : this.mHintLayout.getWidth();
            this.makeNewLayout(want, hintWant, UNKNOWN_BORING, UNKNOWN_BORING, this.getWidth() - this.getCompoundPaddingLeft() - this.getCompoundPaddingRight(), true);
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setHorizontallyScrolling(boolean whether) {
        if (this.mHorizontallyScrolling != whether) {
            this.mHorizontallyScrolling = whether;
            if (this.mLayout != null) {
                this.nullLayouts();
                this.requestLayout();
                this.invalidate();
            }
        }
    }

    public final boolean isHorizontallyScrollable() {
        return this.mHorizontallyScrolling;
    }

    public void setMinLines(int minLines) {
        this.mMinimum = minLines;
        this.mMinMode = 1;
        this.requestLayout();
        this.invalidate();
    }

    public int getMinLines() {
        return this.mMinMode == 1 ? this.mMinimum : -1;
    }

    public void setMinHeight(int minPixels) {
        this.mMinimum = minPixels;
        this.mMinMode = 2;
        this.requestLayout();
        this.invalidate();
    }

    public int getMinHeight() {
        return this.mMinMode == 2 ? this.mMinimum : -1;
    }

    public void setMaxLines(int maxLines) {
        this.mMaximum = maxLines;
        this.mMaxMode = 1;
        this.requestLayout();
        this.invalidate();
    }

    public int getMaxLines() {
        return this.mMaxMode == 1 ? this.mMaximum : -1;
    }

    public void setMaxHeight(int maxPixels) {
        this.mMaximum = maxPixels;
        this.mMaxMode = 2;
        this.requestLayout();
        this.invalidate();
    }

    public int getMaxHeight() {
        return this.mMaxMode == 2 ? this.mMaximum : -1;
    }

    public void setLines(int lines) {
        this.mMaximum = this.mMinimum = lines;
        this.mMaxMode = this.mMinMode = 1;
        this.requestLayout();
        this.invalidate();
    }

    public void setHeight(int pixels) {
        this.mMaximum = this.mMinimum = pixels;
        this.mMaxMode = this.mMinMode = 2;
        this.requestLayout();
        this.invalidate();
    }

    public void setMinWidth(int minPixels) {
        this.mMinWidth = minPixels;
        this.requestLayout();
        this.invalidate();
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public void setMaxWidth(int maxPixels) {
        this.mMaxWidth = maxPixels;
        this.requestLayout();
        this.invalidate();
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    public void setWidth(int pixels) {
        this.mMaxWidth = this.mMinWidth = pixels;
        this.requestLayout();
        this.invalidate();
    }

    @NonNull
    public CharSequence getText() {
        return this.mText;
    }

    public Editable getEditableText() {
        return this.mText instanceof Editable ? (Editable) this.mText : null;
    }

    public final void setEditableFactory(@NonNull Editable.Factory factory) {
        this.mEditableFactory = factory;
        this.setText(this.mText);
    }

    public final void setSpannableFactory(@NonNull Spannable.Factory factory) {
        this.mSpannableFactory = factory;
        this.setText(this.mText);
    }

    public final void setText(@NonNull CharSequence text) {
        this.setText(text, this.mBufferType);
    }

    public final void setTextKeepState(@NonNull CharSequence text) {
        this.setTextKeepState(text, this.mBufferType);
    }

    public final void setTextKeepState(@NonNull CharSequence text, @NonNull TextView.BufferType type) {
        int start = this.getSelectionStart();
        int end = this.getSelectionEnd();
        int len = text.length();
        this.setText(text, type);
        if ((start >= 0 || end >= 0) && this.mSpannable != null) {
            Selection.setSelection(this.mSpannable, Math.max(0, Math.min(start, len)), Math.max(0, Math.min(end, len)));
        }
    }

    public void setText(@NonNull CharSequence text, @NonNull TextView.BufferType type) {
        for (InputFilter filter : this.mFilters) {
            CharSequence out = filter.filter(text, 0, text.length(), EMPTY_SPANNED, 0, 0);
            if (out != null) {
                text = out;
            }
        }
        int oldLength = this.mText.length();
        this.sendBeforeTextChanged(this.mText, 0, oldLength, text.length());
        boolean needEditableForNotification = this.mListeners != null && this.mListeners.size() > 0;
        if (type == TextView.BufferType.EDITABLE || needEditableForNotification) {
            this.createEditorIfNeeded();
            this.mEditor.forgetUndoRedo();
            Editable t = this.mEditableFactory.newEditable(text);
            text = t;
            this.setFilters(t, this.mFilters);
        } else if (text instanceof PrecomputedText precomputed) {
            if (this.mTextDir == null) {
                this.mTextDir = this.getTextDirectionHeuristic();
            }
            int checkResult = precomputed.getParams().checkResultUsable(this.getPaint(), this.mTextDir, LineBreakConfig.getLineBreakConfig(this.mLineBreakStyle, this.mLineBreakWordStyle));
            switch(checkResult) {
                case 0:
                    throw new IllegalArgumentException("PrecomputedText's Parameters don't match the parameters of this TextView.Consider using setTextMetricsParams(precomputedText.getParams()) to override the settings of this TextView: PrecomputedText: " + precomputed.getParams() + "TextView: " + this.getTextMetricsParams());
                case 1:
                    text = PrecomputedText.create(precomputed, this.getTextMetricsParams());
                case 2:
            }
        } else if (type != TextView.BufferType.SPANNABLE && this.mMovement == null) {
            text = TextUtils.stringOrSpannedString(text);
        } else {
            text = this.mSpannableFactory.newSpannable(text);
        }
        if (this.mAutoLinkMask != 0) {
        }
        this.mBufferType = type;
        this.setTextInternal(text);
        if (this.mTransformation == null) {
            this.mTransformed = text;
        } else {
            this.mTransformed = this.mTransformation.getTransformation(text, this);
        }
        int textLength = text.length();
        if (text instanceof Spannable sp) {
            for (TextView.ChangeWatcher watcher : sp.getSpans(0, sp.length(), TextView.ChangeWatcher.class)) {
                sp.removeSpan(watcher);
            }
            if (this.mChangeWatcher == null) {
                this.mChangeWatcher = new TextView.ChangeWatcher();
            }
            sp.setSpan(this.mChangeWatcher, 0, textLength, 6553618);
            if (this.mEditor != null) {
                this.mEditor.addSpanWatchers(sp);
            }
            if (this.mTransformation != null) {
                sp.setSpan(this.mTransformation, 0, textLength, 18);
            }
            if (this.mMovement != null) {
                this.mMovement.initialize(this, (Spannable) text);
                if (this.mEditor != null) {
                    this.mEditor.mSelectionMoved = false;
                }
            }
        }
        if (this.mLayout != null) {
            this.checkForRelayout();
        }
        this.sendOnTextChanged(text, 0, oldLength, textLength);
        this.onTextChanged(text, 0, oldLength, textLength);
        if (needEditableForNotification) {
            this.sendAfterTextChanged((Editable) text);
        }
    }

    private void setTextInternal(@NonNull CharSequence text) {
        this.mText = text;
        this.mSpannable = text instanceof Spannable ? (Spannable) text : null;
        this.mPrecomputed = text instanceof PrecomputedText ? (PrecomputedText) text : null;
    }

    @Nullable
    public CharSequence getHint() {
        return this.mHint;
    }

    public final void setHint(@Nullable CharSequence hint) {
        this.mHint = TextUtils.stringOrSpannedString(hint);
        if (this.mLayout != null) {
            this.checkForRelayout();
        }
        if (this.mText.length() == 0) {
            this.invalidate();
        }
    }

    private boolean hasPasswordTransformationMethod() {
        return this.mTransformation instanceof PasswordTransformationMethod;
    }

    public void setFilters(@NonNull InputFilter... filters) {
        this.mFilters = (InputFilter[]) Objects.requireNonNull(filters);
        if (this.mText instanceof Editable) {
            this.setFilters((Editable) this.mText, filters);
        }
    }

    private void setFilters(Editable e, InputFilter[] filters) {
        if (this.mEditor != null) {
            boolean undoFilter = this.mEditor.mUndoInputFilter != null;
            int num = 0;
            if (undoFilter) {
                num++;
            }
            if (num > 0) {
                InputFilter[] nf = new InputFilter[filters.length + num];
                System.arraycopy(filters, 0, nf, 0, filters.length);
                num = 0;
                if (undoFilter) {
                    nf[filters.length] = this.mEditor.mUndoInputFilter;
                    num++;
                }
                e.setFilters(nf);
                return;
            }
        }
        e.setFilters(filters);
    }

    @NonNull
    public InputFilter[] getFilters() {
        return this.mFilters;
    }

    private int getBoxHeight(Layout l) {
        int padding = l == this.mHintLayout ? this.getCompoundPaddingTop() + this.getCompoundPaddingBottom() : this.getExtendedPaddingTop() + this.getExtendedPaddingBottom();
        return this.getMeasuredHeight() - padding;
    }

    private int getVerticalOffset(boolean forceNormal) {
        int voffset = 0;
        int gravity = this.mGravity & 112;
        Layout l = this.mLayout;
        if (!forceNormal && this.mText.length() == 0 && this.mHintLayout != null) {
            l = this.mHintLayout;
        }
        if (gravity != 48) {
            int boxHeight = this.getBoxHeight(l);
            int textHeight = l.getHeight();
            if (textHeight < boxHeight) {
                if (gravity == 80) {
                    voffset = boxHeight - textHeight;
                } else {
                    voffset = boxHeight - textHeight >> 1;
                }
            }
        }
        return voffset;
    }

    private int getBottomVerticalOffset(boolean forceNormal) {
        int voffset = 0;
        int gravity = this.mGravity & 112;
        Layout l = this.mLayout;
        if (!forceNormal && this.mText.length() == 0 && this.mHintLayout != null) {
            l = this.mHintLayout;
        }
        if (gravity != 80) {
            int boxht = this.getBoxHeight(l);
            int textht = l.getHeight();
            if (textht < boxht) {
                if (gravity == 48) {
                    voffset = boxht - textht;
                } else {
                    voffset = boxht - textht >> 1;
                }
            }
        }
        return voffset;
    }

    void invalidateCursorPath() {
        if (this.mHighlightPathBogus) {
            if (this.getSelectionEnd() >= 0) {
                this.invalidate();
            }
        } else {
            this.invalidate();
        }
    }

    private void registerForPreDraw() {
        if (!this.mPreDrawRegistered) {
            this.getViewTreeObserver().addOnPreDrawListener(this);
            this.mPreDrawRegistered = true;
        }
    }

    private void unregisterForPreDraw() {
        this.getViewTreeObserver().removeOnPreDrawListener(this);
        this.mPreDrawRegistered = false;
        this.mPreDrawListenerDetached = false;
    }

    @Override
    public boolean onPreDraw() {
        if (this.mLayout == null) {
            this.assumeLayout();
        }
        if (this.mMovement != null) {
            int curs = this.getSelectionEnd();
            if (curs < 0 && (this.mGravity & 112) == 80) {
                curs = this.mText.length();
            }
            if (curs >= 0) {
                this.bringPointIntoView(curs);
            }
        } else {
            this.bringTextIntoView();
        }
        this.unregisterForPreDraw();
        return true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mEditor != null) {
            this.mEditor.onAttachedToWindow();
        }
        if (this.mPreDrawListenerDetached) {
            this.getViewTreeObserver().addOnPreDrawListener(this);
            this.mPreDrawListenerDetached = false;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (this.mPreDrawRegistered) {
            this.getViewTreeObserver().removeOnPreDrawListener(this);
            this.mPreDrawListenerDetached = true;
        }
        this.resetResolvedDrawables();
        if (this.mEditor != null) {
            this.mEditor.onDetachedFromWindow();
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        boolean verified = super.verifyDrawable(who);
        if (!verified && this.mDrawables != null) {
            for (Drawable dr : this.mDrawables.mShowing) {
                if (who == dr) {
                    return true;
                }
            }
        }
        return verified;
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mDrawables != null) {
            for (Drawable dr : this.mDrawables.mShowing) {
                if (dr != null) {
                    dr.jumpToCurrentState();
                }
            }
        }
    }

    public boolean isTextSelectable() {
        return this.mEditor != null && this.mEditor.mTextIsSelectable;
    }

    public void setTextIsSelectable(boolean selectable) {
        if (selectable || this.mEditor != null) {
            this.createEditorIfNeeded();
            if (this.mEditor.mTextIsSelectable != selectable) {
                this.mEditor.mTextIsSelectable = selectable;
                this.setFocusableInTouchMode(selectable);
                this.setFocusable(16);
                this.setClickable(selectable);
                this.setLongClickable(selectable);
                this.setMovementMethod(selectable ? ArrowKeyMovementMethod.getInstance() : null);
                this.setText(this.mText, selectable ? TextView.BufferType.SPANNABLE : TextView.BufferType.NORMAL);
            }
        }
    }

    private void updateTextColors() {
        boolean inval = false;
        int[] drawableState = this.getDrawableState();
        int color = this.mTextColor.getColorForState(drawableState, 0);
        if (color != this.mCurTextColor) {
            this.mCurTextColor = color;
            inval = true;
        }
        if (this.mHintTextColor != null) {
            color = this.mHintTextColor.getColorForState(drawableState, 0);
            if (color != this.mCurHintTextColor) {
                this.mCurHintTextColor = color;
                if (this.mText.length() == 0) {
                    inval = true;
                }
            }
        }
        if (inval) {
            this.invalidate();
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mTextColor != null && this.mTextColor.isStateful() || this.mHintTextColor != null && this.mHintTextColor.isStateful() || this.mLinkTextColor != null && this.mLinkTextColor.isStateful()) {
            this.updateTextColors();
        }
        if (this.mDrawables != null) {
            int[] state = this.getDrawableState();
            for (Drawable dr : this.mDrawables.mShowing) {
                if (dr != null && dr.isStateful() && dr.setState(state)) {
                    this.invalidateDrawable(dr);
                }
            }
        }
    }

    @NonNull
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace);
        if (this.isTextSelectable()) {
            int length = drawableState.length;
            for (int i = 0; i < length; i++) {
                if (drawableState[i] == 16842919) {
                    int[] nonPressedState = new int[length - 1];
                    System.arraycopy(drawableState, 0, nonPressedState, 0, i);
                    System.arraycopy(drawableState, i + 1, nonPressedState, i, length - i - 1);
                    return nonPressedState;
                }
            }
        }
        return drawableState;
    }

    private void drawHighlight(@NonNull Canvas canvas, int cursorOffsetVertical) {
        int selStart = this.getSelectionStart();
        if (this.mMovement != null && (this.isFocused() || this.isPressed()) && selStart >= 0) {
            int selEnd = this.getSelectionEnd();
            Paint paint = Paint.obtain();
            paint.setStrokeWidth((float) Math.max(1, this.dp(0.75F)));
            if (selStart == selEnd) {
                if (this.mEditor != null && this.mEditor.shouldRenderCursor()) {
                    if (this.mHighlightPathBogus) {
                        if (this.mHighlightPath == null) {
                            this.mHighlightPath = new FloatArrayList();
                        }
                        this.mLayout.getCursorPath(selStart, this.mHighlightPath, this.mText);
                        this.mHighlightPathBogus = false;
                    }
                    paint.setColor(this.mTextPaint.getColor());
                    paint.setStyle(0);
                    if (cursorOffsetVertical != 0) {
                        canvas.translate(0.0F, (float) cursorOffsetVertical);
                    }
                    canvas.drawRoundLines(this.mHighlightPath.elements(), 0, this.mHighlightPath.size(), false, paint);
                    if (cursorOffsetVertical != 0) {
                        canvas.translate(0.0F, (float) (-cursorOffsetVertical));
                    }
                }
            } else {
                if (this.mHighlightPathBogus) {
                    if (this.mHighlightPath == null) {
                        this.mHighlightPath = new FloatArrayList();
                    }
                    this.mLayout.getSelectionPath(selStart, selEnd, this.mHighlightPath);
                    this.mHighlightPathBogus = false;
                }
                paint.setColor(this.mHighlightColor);
                paint.setStyle(0);
                if (cursorOffsetVertical != 0) {
                    canvas.translate(0.0F, (float) cursorOffsetVertical);
                }
                float[] src = this.mHighlightPath.elements();
                int len = this.mHighlightPath.size();
                int i = 0;
                while (i < len) {
                    canvas.drawRect(src[i++], src[i++], src[i++], src[i++], paint);
                }
                if (cursorOffsetVertical != 0) {
                    canvas.translate(0.0F, (float) (-cursorOffsetVertical));
                }
            }
            paint.recycle();
        }
    }

    public int getHorizontalOffsetForDrawables() {
        return 0;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        int compoundPaddingLeft = this.getCompoundPaddingLeft();
        int compoundPaddingTop = this.getCompoundPaddingTop();
        int compoundPaddingRight = this.getCompoundPaddingRight();
        int compoundPaddingBottom = this.getCompoundPaddingBottom();
        int scrollX = this.mScrollX;
        int scrollY = this.mScrollY;
        TextView.Drawables dr = this.mDrawables;
        if (dr != null) {
            int vspace = this.getHeight() - compoundPaddingBottom - compoundPaddingTop;
            int hspace = this.getWidth() - compoundPaddingRight - compoundPaddingLeft;
            boolean isLayoutRtl = this.isLayoutRtl();
            int offset = this.getHorizontalOffsetForDrawables();
            int leftOffset = isLayoutRtl ? 0 : offset;
            int rightOffset = isLayoutRtl ? offset : 0;
            if (dr.mShowing[0] != null) {
                canvas.save();
                offset = (vspace - dr.mDrawableHeightLeft) / 2;
                canvas.translate((float) (scrollX + this.mPaddingLeft + leftOffset), (float) (scrollY + compoundPaddingTop + offset));
                dr.mShowing[0].draw(canvas);
                canvas.restore();
            }
            if (dr.mShowing[2] != null) {
                canvas.save();
                offset = (vspace - dr.mDrawableHeightRight) / 2;
                canvas.translate((float) (scrollX + this.getWidth() - this.mPaddingRight - dr.mDrawableSizeRight - rightOffset), (float) (scrollY + compoundPaddingTop + offset));
                dr.mShowing[2].draw(canvas);
                canvas.restore();
            }
            if (dr.mShowing[1] != null) {
                canvas.save();
                offset = (hspace - dr.mDrawableWidthTop) / 2;
                canvas.translate((float) (scrollX + compoundPaddingLeft + offset), (float) (scrollY + this.mPaddingTop));
                dr.mShowing[1].draw(canvas);
                canvas.restore();
            }
            if (dr.mShowing[3] != null) {
                canvas.save();
                offset = (hspace - dr.mDrawableWidthBottom) / 2;
                canvas.translate((float) (scrollX + compoundPaddingLeft + offset), (float) (scrollY + this.getHeight() - this.mPaddingBottom - dr.mDrawableSizeBottom));
                dr.mShowing[3].draw(canvas);
                canvas.restore();
            }
        }
        int color = this.mCurTextColor;
        if (this.mLayout == null) {
            this.assumeLayout();
        }
        Layout layout = this.mLayout;
        if (this.mHint != null && this.mText.length() == 0) {
            if (this.mHintTextColor != null) {
                color = this.mCurHintTextColor;
            }
            layout = this.mHintLayout;
        }
        if (layout.getLineCount() != 0) {
            this.mTextPaint.setColor(color);
            int extendedPaddingTop = this.getExtendedPaddingTop();
            int extendedPaddingBottom = this.getExtendedPaddingBottom();
            int vspacex = this.getHeight() - compoundPaddingBottom - compoundPaddingTop;
            int maxScrollY = this.mLayout.getHeight() - vspacex;
            float clipLeft = (float) (compoundPaddingLeft + scrollX);
            float clipTop = scrollY == 0 ? 0.0F : (float) (extendedPaddingTop + scrollY);
            float clipRight = (float) (this.getWidth() - compoundPaddingRight + scrollX);
            float clipBottom = (float) (this.getHeight() + scrollY - (scrollY == maxScrollY ? 0 : extendedPaddingBottom));
            canvas.save();
            canvas.clipRect(clipLeft, clipTop, clipRight, clipBottom);
            int vOffsetText = 0;
            int vOffsetCursor = 0;
            if ((this.mGravity & 112) != 48) {
                vOffsetText = this.getVerticalOffset(false);
                vOffsetCursor = this.getVerticalOffset(true);
            }
            canvas.translate((float) compoundPaddingLeft, (float) (extendedPaddingTop + vOffsetText));
            long range = layout.getLineRangeForDraw(canvas);
            if (range >= 0L) {
                int firstLine = (int) (range >>> 32);
                int lastLine = (int) (range & 4294967295L);
                layout.drawBackground(canvas, firstLine, lastLine);
                this.drawHighlight(canvas, vOffsetCursor - vOffsetText);
                layout.drawText(canvas, firstLine, lastLine);
            }
            canvas.restore();
        }
    }

    @Override
    public void getFocusedRect(@NonNull Rect r) {
        if (this.mLayout == null) {
            super.getFocusedRect(r);
        } else {
            int selEnd = this.getSelectionEnd();
            if (selEnd < 0) {
                super.getFocusedRect(r);
            } else {
                int selStart = this.getSelectionStart();
                if (selStart >= 0 && selStart < selEnd) {
                    int lineStart = this.mLayout.getLineForOffset(selStart);
                    int lineEnd = this.mLayout.getLineForOffset(selEnd);
                    r.top = this.mLayout.getLineTop(lineStart);
                    r.bottom = this.mLayout.getLineBottom(lineEnd);
                    if (lineStart == lineEnd) {
                        r.left = (int) this.mLayout.getPrimaryHorizontal(selStart);
                        r.right = (int) this.mLayout.getPrimaryHorizontal(selEnd);
                    } else {
                        r.left = 0;
                        r.right = this.mLayout.getWidth();
                    }
                } else {
                    int line = this.mLayout.getLineForOffset(selEnd);
                    r.top = this.mLayout.getLineTop(line);
                    r.bottom = this.mLayout.getLineBottom(line);
                    r.left = (int) this.mLayout.getPrimaryHorizontal(selEnd) - 2;
                    r.right = r.left + 4;
                }
                int paddingLeft = this.getCompoundPaddingLeft();
                int paddingTop = this.getExtendedPaddingTop();
                if ((this.mGravity & 112) != 48) {
                    paddingTop += this.getVerticalOffset(false);
                }
                r.offset(paddingLeft, paddingTop);
                int paddingBottom = this.getExtendedPaddingBottom();
                r.bottom += paddingBottom;
            }
        }
    }

    public int getLineCount() {
        return this.mLayout != null ? this.mLayout.getLineCount() : 0;
    }

    public int getLineBounds(int line, @Nullable Rect bounds) {
        if (this.mLayout == null) {
            if (bounds != null) {
                bounds.set(0, 0, 0, 0);
            }
            return 0;
        } else {
            int baseline = this.mLayout.getLineBounds(line, bounds);
            int voffset = this.getExtendedPaddingTop();
            if ((this.mGravity & 112) != 48) {
                voffset += this.getVerticalOffset(true);
            }
            if (bounds != null) {
                bounds.offset(this.getCompoundPaddingLeft(), voffset);
            }
            return baseline + voffset;
        }
    }

    @Override
    public int getBaseline() {
        return this.mLayout == null ? super.getBaseline() : this.getBaselineOffset() + this.mLayout.getLineBaseline(0);
    }

    int getBaselineOffset() {
        int vOffset = 0;
        if ((this.mGravity & 112) != 48) {
            vOffset = this.getVerticalOffset(true);
        }
        return this.getExtendedPaddingTop() + vOffset;
    }

    @VisibleForTesting
    public final void nullLayouts() {
        if (this.mLayout instanceof BoringLayout && this.mSavedLayout == null) {
            this.mSavedLayout = (BoringLayout) this.mLayout;
        }
        if (this.mHintLayout instanceof BoringLayout && this.mSavedHintLayout == null) {
            this.mSavedHintLayout = (BoringLayout) this.mHintLayout;
        }
        this.mLayout = this.mHintLayout = null;
        this.mBoring = this.mHintBoring = null;
    }

    private void assumeLayout() {
        int width = this.getWidth() - this.getCompoundPaddingLeft() - this.getCompoundPaddingRight();
        if (width < 1) {
            width = 0;
        }
        int physicalWidth = width;
        if (this.mHorizontallyScrolling) {
            width = 1048576;
        }
        this.makeNewLayout(width, physicalWidth, UNKNOWN_BORING, UNKNOWN_BORING, physicalWidth, false);
    }

    private Layout.Alignment getLayoutAlignment() {
        return switch(this.getTextAlignment()) {
            case 1 ->
                {
                    switch(this.mGravity & 8388615) {
                        case 1:
                            ???;
                        case 3:
                            ???;
                        case 5:
                            ???;
                        case 8388613:
                            ???;
                        default:
                            ???;
                    }
                }
            default ->
                Layout.Alignment.ALIGN_NORMAL;
            case 3 ->
                Layout.Alignment.ALIGN_OPPOSITE;
            case 4 ->
                Layout.Alignment.ALIGN_CENTER;
            case 5 ->
                this.isLayoutRtl() ? Layout.Alignment.ALIGN_RIGHT : Layout.Alignment.ALIGN_LEFT;
            case 6 ->
                this.isLayoutRtl() ? Layout.Alignment.ALIGN_LEFT : Layout.Alignment.ALIGN_RIGHT;
        };
    }

    @VisibleForTesting
    public final void makeNewLayout(int wantWidth, int hintWidth, BoringLayout.Metrics boring, BoringLayout.Metrics hintBoring, int ellipsisWidth, boolean bringIntoView) {
        this.mOldMaximum = this.mMaximum;
        this.mOldMaxMode = this.mMaxMode;
        this.mHighlightPathBogus = true;
        if (wantWidth < 0) {
            wantWidth = 0;
        }
        if (hintWidth < 0) {
            hintWidth = 0;
        }
        Layout.Alignment alignment = this.getLayoutAlignment();
        boolean testDirChange = !bringIntoView && this.mSingleLine && this.mLayout != null;
        bringIntoView |= testDirChange && this.mLayout.getAlignment() != alignment;
        int oldDir = 0;
        if (testDirChange && !bringIntoView) {
            oldDir = this.mLayout.getParagraphDirection(0);
        }
        boolean shouldEllipsize = this.mEllipsize != null && this.mBufferType != TextView.BufferType.EDITABLE;
        TextUtils.TruncateAt effectiveEllipsize = this.mEllipsize;
        if (this.mTextDir == null) {
            this.mTextDir = this.getTextDirectionHeuristic();
        }
        this.mLayout = this.makeSingleLayout(wantWidth, boring, ellipsisWidth, alignment, shouldEllipsize, effectiveEllipsize, effectiveEllipsize == this.mEllipsize);
        shouldEllipsize = this.mEllipsize != null;
        this.mHintLayout = null;
        if (this.mHint != null) {
            if (shouldEllipsize) {
                hintWidth = wantWidth;
            }
            if (hintBoring == UNKNOWN_BORING) {
                hintBoring = BoringLayout.isBoring(this.mHint, this.mTextPaint, this.mTextDir, this.mHintBoring);
                if (hintBoring != null) {
                    this.mHintBoring = hintBoring;
                }
            }
            if (hintBoring != null) {
                if (hintBoring.width <= hintWidth && (!shouldEllipsize || hintBoring.width <= ellipsisWidth)) {
                    if (this.mSavedHintLayout != null) {
                        this.mHintLayout = this.mSavedHintLayout.replaceOrMake(this.mHint, this.mTextPaint, hintWidth, alignment, hintBoring, this.mIncludePad);
                    } else {
                        this.mHintLayout = BoringLayout.make(this.mHint, this.mTextPaint, hintWidth, alignment, hintBoring, this.mIncludePad);
                    }
                    this.mSavedHintLayout = (BoringLayout) this.mHintLayout;
                } else if (shouldEllipsize && hintBoring.width <= hintWidth) {
                    if (this.mSavedHintLayout != null) {
                        this.mHintLayout = this.mSavedHintLayout.replaceOrMake(this.mHint, this.mTextPaint, hintWidth, alignment, hintBoring, this.mIncludePad, this.mEllipsize, ellipsisWidth);
                    } else {
                        this.mHintLayout = BoringLayout.make(this.mHint, this.mTextPaint, hintWidth, alignment, hintBoring, this.mIncludePad, this.mEllipsize, ellipsisWidth);
                    }
                }
            }
            if (this.mHintLayout == null) {
                StaticLayout.Builder builder = StaticLayout.builder(this.mHint, 0, this.mHint.length(), this.mTextPaint, hintWidth).setAlignment(alignment).setTextDirection(this.mTextDir).setIncludePad(this.mIncludePad).setFallbackLineSpacing(this.mUseFallbackLineSpacing).setMaxLines(this.mMaxMode == 1 ? this.mMaximum : Integer.MAX_VALUE).setLineBreakConfig(LineBreakConfig.getLineBreakConfig(this.mLineBreakStyle, this.mLineBreakWordStyle));
                if (shouldEllipsize) {
                    builder.setEllipsize(this.mEllipsize).setEllipsizedWidth(ellipsisWidth);
                }
                this.mHintLayout = builder.build();
            }
        }
        if (bringIntoView || testDirChange && oldDir != this.mLayout.getParagraphDirection(0)) {
            this.registerForPreDraw();
        }
    }

    @VisibleForTesting
    public final boolean useDynamicLayout() {
        return this.isTextSelectable() || this.mSpannable != null && this.mPrecomputed == null;
    }

    @NonNull
    private Layout makeSingleLayout(int wantWidth, BoringLayout.Metrics boring, int ellipsisWidth, Layout.Alignment alignment, boolean shouldEllipsize, TextUtils.TruncateAt effectiveEllipsize, boolean useSaved) {
        Layout result = null;
        if (this.useDynamicLayout()) {
            DynamicLayout.Builder builder = DynamicLayout.builder(this.mText, this.mTextPaint, wantWidth).setDisplayText(this.mTransformed).setAlignment(alignment).setTextDirection(this.mTextDir).setIncludePad(this.mIncludePad).setFallbackLineSpacing(this.mUseFallbackLineSpacing).setEllipsize(this.mBufferType != TextView.BufferType.EDITABLE ? effectiveEllipsize : null).setEllipsizedWidth(ellipsisWidth);
            result = builder.build();
        } else {
            if (boring == UNKNOWN_BORING) {
                boring = BoringLayout.isBoring(this.mTransformed, this.mTextPaint, this.mTextDir, this.mBoring);
                if (boring != null) {
                    this.mBoring = boring;
                }
            }
            if (boring != null) {
                if (boring.width > wantWidth || effectiveEllipsize != null && boring.width > ellipsisWidth) {
                    if (shouldEllipsize && boring.width <= wantWidth) {
                        if (useSaved && this.mSavedLayout != null) {
                            result = this.mSavedLayout.replaceOrMake(this.mTransformed, this.mTextPaint, wantWidth, alignment, boring, this.mIncludePad, effectiveEllipsize, ellipsisWidth);
                        } else {
                            result = BoringLayout.make(this.mTransformed, this.mTextPaint, wantWidth, alignment, boring, this.mIncludePad, effectiveEllipsize, ellipsisWidth);
                        }
                    }
                } else {
                    if (useSaved && this.mSavedLayout != null) {
                        result = this.mSavedLayout.replaceOrMake(this.mTransformed, this.mTextPaint, wantWidth, alignment, boring, this.mIncludePad);
                    } else {
                        result = BoringLayout.make(this.mTransformed, this.mTextPaint, wantWidth, alignment, boring, this.mIncludePad);
                    }
                    if (useSaved) {
                        this.mSavedLayout = (BoringLayout) result;
                    }
                }
            }
        }
        if (result == null) {
            StaticLayout.Builder builder = StaticLayout.builder(this.mTransformed, 0, this.mTransformed.length(), this.mTextPaint, wantWidth).setAlignment(alignment).setTextDirection(this.mTextDir).setIncludePad(this.mIncludePad).setFallbackLineSpacing(this.mUseFallbackLineSpacing).setMaxLines(this.mMaxMode == 1 ? this.mMaximum : Integer.MAX_VALUE).setLineBreakConfig(LineBreakConfig.getLineBreakConfig(this.mLineBreakStyle, this.mLineBreakWordStyle));
            if (shouldEllipsize) {
                builder.setEllipsize(effectiveEllipsize).setEllipsizedWidth(ellipsisWidth);
            }
            result = builder.build();
        }
        return result;
    }

    private static int desired(@NonNull Layout layout) {
        int n = layout.getLineCount();
        CharSequence text = layout.getText();
        float max = 0.0F;
        for (int i = 0; i < n - 1; i++) {
            if (text.charAt(layout.getLineEnd(i) - 1) != '\n') {
                return -1;
            }
        }
        for (int ix = 0; ix < n; ix++) {
            max = Math.max(max, layout.getLineMax(ix));
        }
        return (int) Math.ceil((double) max);
    }

    public void setIncludeFontPadding(boolean includePad) {
        if (this.mIncludePad != includePad) {
            this.mIncludePad = includePad;
            if (this.mLayout != null) {
                this.nullLayouts();
                this.requestLayout();
                this.invalidate();
            }
        }
    }

    public boolean getIncludeFontPadding() {
        return this.mIncludePad;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        BoringLayout.Metrics boring = UNKNOWN_BORING;
        BoringLayout.Metrics hintBoring = UNKNOWN_BORING;
        if (this.mTextDir == null) {
            this.mTextDir = this.getTextDirectionHeuristic();
        }
        int des = -1;
        boolean fromexisting = false;
        float widthLimit = widthMode == Integer.MIN_VALUE ? (float) widthSize : Float.MAX_VALUE;
        int width;
        if (widthMode == 1073741824) {
            width = widthSize;
        } else {
            if (this.mLayout != null && this.mEllipsize == null) {
                des = desired(this.mLayout);
            }
            if (des < 0) {
                boring = BoringLayout.isBoring(this.mTransformed, this.mTextPaint, this.mTextDir, this.mBoring);
                if (boring != null) {
                    this.mBoring = boring;
                }
            } else {
                fromexisting = true;
            }
            if (boring != null && boring != UNKNOWN_BORING) {
                width = boring.width;
            } else {
                if (des < 0) {
                    des = (int) Math.ceil((double) Layout.getDesiredWidthWithLimit(this.mTransformed, 0, this.mTransformed.length(), this.mTextPaint, this.mTextDir, widthLimit));
                }
                width = des;
            }
            TextView.Drawables dr = this.mDrawables;
            if (dr != null) {
                width = Math.max(width, dr.mDrawableWidthTop);
                width = Math.max(width, dr.mDrawableWidthBottom);
            }
            if (this.mHint != null) {
                int hintDes = -1;
                if (this.mHintLayout != null && this.mEllipsize == null) {
                    hintDes = desired(this.mHintLayout);
                }
                if (hintDes < 0) {
                    hintBoring = BoringLayout.isBoring(this.mHint, this.mTextPaint, this.mTextDir, this.mHintBoring);
                    if (hintBoring != null) {
                        this.mHintBoring = hintBoring;
                    }
                }
                int hintWidth;
                if (hintBoring != null && hintBoring != UNKNOWN_BORING) {
                    hintWidth = hintBoring.width;
                } else {
                    if (hintDes < 0) {
                        hintDes = (int) Math.ceil((double) Layout.getDesiredWidthWithLimit(this.mHint, 0, this.mHint.length(), this.mTextPaint, this.mTextDir, widthLimit));
                    }
                    hintWidth = hintDes;
                }
                if (hintWidth > width) {
                    width = hintWidth;
                }
            }
            width += this.getCompoundPaddingLeft() + this.getCompoundPaddingRight();
            width = Math.min(width, this.mMaxWidth);
            width = Math.max(width, this.mMinWidth);
            width = Math.max(width, this.getSuggestedMinimumWidth());
            if (widthMode == Integer.MIN_VALUE) {
                width = Math.min(widthSize, width);
            }
        }
        int want = width - this.getCompoundPaddingLeft() - this.getCompoundPaddingRight();
        int unpaddedWidth = want;
        if (this.mHorizontallyScrolling) {
            want = 1048576;
        }
        int hintWidthx = this.mHintLayout == null ? want : this.mHintLayout.getWidth();
        if (this.mLayout == null) {
            this.makeNewLayout(want, want, boring, hintBoring, width - this.getCompoundPaddingLeft() - this.getCompoundPaddingRight(), false);
        } else {
            boolean layoutChanged = this.mLayout.getWidth() != want || hintWidthx != want || this.mLayout.getEllipsizedWidth() != width - this.getCompoundPaddingLeft() - this.getCompoundPaddingRight();
            boolean widthChanged = this.mHint == null && this.mEllipsize == null && want > this.mLayout.getWidth() && (this.mLayout instanceof BoringLayout || fromexisting && des <= want);
            boolean maximumChanged = this.mMaxMode != this.mOldMaxMode || this.mMaximum != this.mOldMaximum;
            if (layoutChanged || maximumChanged) {
                if (!maximumChanged && widthChanged) {
                    this.mLayout.increaseWidthTo(want);
                } else {
                    this.makeNewLayout(want, want, boring, hintBoring, width - this.getCompoundPaddingLeft() - this.getCompoundPaddingRight(), false);
                }
            }
        }
        int height;
        if (heightMode == 1073741824) {
            height = heightSize;
            this.mDesiredHeightAtMeasure = -1;
        } else {
            int desired = this.getDesiredHeight();
            height = desired;
            this.mDesiredHeightAtMeasure = desired;
            if (heightMode == Integer.MIN_VALUE) {
                height = Math.min(desired, heightSize);
            }
        }
        int unpaddedHeight = height - this.getCompoundPaddingTop() - this.getCompoundPaddingBottom();
        if (this.mMaxMode == 1 && this.mLayout.getLineCount() > this.mMaximum) {
            unpaddedHeight = Math.min(unpaddedHeight, this.mLayout.getLineTop(this.mMaximum));
        }
        if (this.mMovement == null && this.mLayout.getWidth() <= unpaddedWidth && this.mLayout.getHeight() <= unpaddedHeight) {
            this.scrollTo(0, 0);
        } else {
            this.registerForPreDraw();
        }
        this.setMeasuredDimension(width, height);
    }

    private int getDesiredHeight() {
        return Math.max(this.getDesiredHeight(this.mLayout, true), this.getDesiredHeight(this.mHintLayout, this.mEllipsize != null));
    }

    private int getDesiredHeight(Layout layout, boolean cap) {
        if (layout == null) {
            return 0;
        } else {
            int desired = layout.getHeight(cap);
            TextView.Drawables dr = this.mDrawables;
            if (dr != null) {
                desired = Math.max(desired, dr.mDrawableHeightLeft);
                desired = Math.max(desired, dr.mDrawableHeightRight);
            }
            int linecount = layout.getLineCount();
            int padding = this.getCompoundPaddingTop() + this.getCompoundPaddingBottom();
            desired += padding;
            if (this.mMaxMode != 1) {
                desired = Math.min(desired, this.mMaximum);
            } else if (cap && linecount > this.mMaximum && (layout instanceof DynamicLayout || layout instanceof BoringLayout)) {
                desired = layout.getLineTop(this.mMaximum);
                if (dr != null) {
                    desired = Math.max(desired, dr.mDrawableHeightLeft);
                    desired = Math.max(desired, dr.mDrawableHeightRight);
                }
                desired += padding;
                linecount = this.mMaximum;
            }
            if (this.mMinMode == 1) {
                if (linecount < this.mMinimum) {
                    desired += this.getLineHeight() * (this.mMinimum - linecount);
                }
            } else {
                desired = Math.max(desired, this.mMinimum);
            }
            return Math.max(desired, this.getSuggestedMinimumHeight());
        }
    }

    private void checkForResize() {
        boolean sizeChanged = false;
        if (this.mLayout != null) {
            ViewGroup.LayoutParams params = this.getLayoutParams();
            if (params.width == -2) {
                sizeChanged = true;
                this.invalidate();
            }
            if (params.height == -2) {
                int desiredHeight = this.getDesiredHeight();
                if (desiredHeight != this.getHeight()) {
                    sizeChanged = true;
                }
            } else if (params.height == -1 && this.mDesiredHeightAtMeasure >= 0) {
                int desiredHeight = this.getDesiredHeight();
                if (desiredHeight != this.mDesiredHeightAtMeasure) {
                    sizeChanged = true;
                }
            }
        }
        if (sizeChanged) {
            this.requestLayout();
        }
    }

    private void checkForRelayout() {
        ViewGroup.LayoutParams params = this.getLayoutParams();
        if ((params.width != -2 || this.mMaxWidth == this.mMinWidth) && (this.mHint == null || this.mHintLayout != null) && this.getWidth() - this.getCompoundPaddingLeft() - this.getCompoundPaddingRight() > 0) {
            int oldHeight = this.mLayout.getHeight();
            int want = this.mLayout.getWidth();
            int hintWant = this.mHintLayout == null ? 0 : this.mHintLayout.getWidth();
            this.makeNewLayout(want, hintWant, UNKNOWN_BORING, UNKNOWN_BORING, this.getWidth() - this.getCompoundPaddingLeft() - this.getCompoundPaddingRight(), false);
            if (params.height != -2 && params.height != -1) {
                this.invalidate();
                return;
            }
            if (this.mLayout.getHeight() == oldHeight && (this.mHintLayout == null || this.mHintLayout.getHeight() == oldHeight)) {
                this.invalidate();
                return;
            }
        } else {
            this.nullLayouts();
        }
        this.requestLayout();
        this.invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mDeferScroll >= 0) {
            int curs = this.mDeferScroll;
            this.mDeferScroll = -1;
            this.bringPointIntoView(Math.min(curs, this.mText.length()));
        }
    }

    private boolean isShowingHint() {
        return TextUtils.isEmpty(this.mText) && !TextUtils.isEmpty(this.mHint);
    }

    private void bringTextIntoView() {
        Layout layout = this.isShowingHint() ? this.mHintLayout : this.mLayout;
        int line = 0;
        if ((this.mGravity & 112) == 80) {
            line = layout.getLineCount() - 1;
        }
        Layout.Alignment a = layout.getParagraphAlignment(line);
        int dir = layout.getParagraphDirection(line);
        int hspace = this.getWidth() - this.getCompoundPaddingLeft() - this.getCompoundPaddingRight();
        int vspace = this.getHeight() - this.getExtendedPaddingTop() - this.getExtendedPaddingBottom();
        int ht = layout.getHeight();
        if (a == Layout.Alignment.ALIGN_NORMAL) {
            a = dir == 1 ? Layout.Alignment.ALIGN_LEFT : Layout.Alignment.ALIGN_RIGHT;
        } else if (a == Layout.Alignment.ALIGN_OPPOSITE) {
            a = dir == 1 ? Layout.Alignment.ALIGN_RIGHT : Layout.Alignment.ALIGN_LEFT;
        }
        int scrollx;
        if (a == Layout.Alignment.ALIGN_CENTER) {
            int left = (int) Math.floor((double) layout.getLineLeft(line));
            int right = (int) Math.ceil((double) layout.getLineRight(line));
            if (right - left < hspace) {
                scrollx = (right + left) / 2 - hspace / 2;
            } else if (dir < 0) {
                scrollx = right - hspace;
            } else {
                scrollx = left;
            }
        } else if (a == Layout.Alignment.ALIGN_RIGHT) {
            int right = (int) Math.ceil((double) layout.getLineRight(line));
            scrollx = right - hspace;
        } else {
            scrollx = (int) Math.floor((double) layout.getLineLeft(line));
        }
        int scrolly;
        if (ht < vspace) {
            scrolly = 0;
        } else if ((this.mGravity & 112) == 80) {
            scrolly = ht - vspace;
        } else {
            scrolly = 0;
        }
        if (scrollx != this.mScrollX || scrolly != this.mScrollY) {
            this.scrollTo(scrollx, scrolly);
        }
    }

    public boolean bringPointIntoView(int offset) {
        if (this.isLayoutRequested()) {
            this.mDeferScroll = offset;
            return false;
        } else {
            Layout layout = this.isShowingHint() ? this.mHintLayout : this.mLayout;
            if (layout == null) {
                return false;
            } else {
                boolean changed = false;
                int line = layout.getLineForOffset(offset);
                int grav = switch(layout.getParagraphAlignment(line)) {
                    case ALIGN_LEFT ->
                        1;
                    case ALIGN_RIGHT ->
                        -1;
                    case ALIGN_NORMAL ->
                        layout.getParagraphDirection(line);
                    case ALIGN_OPPOSITE ->
                        -layout.getParagraphDirection(line);
                    default ->
                        0;
                };
                boolean clamped = grav > 0;
                int x = (int) layout.getPrimaryHorizontal(offset, clamped);
                int top = layout.getLineTop(line);
                int bottom = layout.getLineTop(line + 1);
                int left = (int) Math.floor((double) layout.getLineLeft(line));
                int right = (int) Math.ceil((double) layout.getLineRight(line));
                int ht = layout.getHeight();
                int hspace = this.getWidth() - this.getCompoundPaddingLeft() - this.getCompoundPaddingRight();
                int vspace = this.getHeight() - this.getExtendedPaddingTop() - this.getExtendedPaddingBottom();
                if (!this.mHorizontallyScrolling && right - left > hspace && right > x) {
                    right = Math.max(x, left + hspace);
                }
                int hslack = (bottom - top) / 2;
                int vslack = hslack;
                if (hslack > vspace / 4) {
                    vslack = vspace / 4;
                }
                if (hslack > hspace / 4) {
                    hslack = hspace / 4;
                }
                int hs = this.mScrollX;
                int vs = this.mScrollY;
                if (top - vs < vslack) {
                    vs = top - vslack;
                }
                if (bottom - vs > vspace - vslack) {
                    vs = bottom - (vspace - vslack);
                }
                if (ht - vs < vspace) {
                    vs = ht - vspace;
                }
                if (vs < 0) {
                    vs = 0;
                }
                if (grav != 0) {
                    if (x - hs < hslack) {
                        hs = x - hslack;
                    }
                    if (x - hs > hspace - hslack) {
                        hs = x - (hspace - hslack);
                    }
                }
                if (grav < 0) {
                    if (left - hs > 0) {
                        hs = left;
                    }
                    if (right - hs < hspace) {
                        hs = right - hspace;
                    }
                } else if (grav > 0) {
                    if (right - hs < hspace) {
                        hs = right - hspace;
                    }
                    if (left - hs > 0) {
                        hs = left;
                    }
                } else if (right - left <= hspace) {
                    hs = left - (hspace - (right - left)) / 2;
                } else if (x > right - hslack) {
                    hs = right - hspace;
                } else if (x < left + hslack) {
                    hs = left;
                } else if (left > hs) {
                    hs = left;
                } else if (right < hs + hspace) {
                    hs = right - hspace;
                } else {
                    if (x - hs < hslack) {
                        hs = x - hslack;
                    }
                    if (x - hs > hspace - hslack) {
                        hs = x - (hspace - hslack);
                    }
                }
                if (hs != this.mScrollX || vs != this.mScrollY) {
                    this.scrollTo(hs, vs);
                    changed = true;
                }
                if (this.isFocused()) {
                    if (this.mTempRect == null) {
                        this.mTempRect = new Rect();
                    }
                    this.mTempRect.set(x - 2, top, x + 2, bottom);
                    this.getInterestingRect(this.mTempRect, line);
                    this.mTempRect.offset(this.mScrollX, this.mScrollY);
                    if (this.requestRectangleOnScreen(this.mTempRect)) {
                        changed = true;
                    }
                }
                return changed;
            }
        }
    }

    public boolean moveCursorToVisibleOffset() {
        if (this.mSpannable == null) {
            return false;
        } else {
            int start = this.getSelectionStart();
            int end = this.getSelectionEnd();
            if (start != end) {
                return false;
            } else {
                int line = this.mLayout.getLineForOffset(start);
                int top = this.mLayout.getLineTop(line);
                int bottom = this.mLayout.getLineTop(line + 1);
                int vspace = this.getHeight() - this.getExtendedPaddingTop() - this.getExtendedPaddingBottom();
                int vslack = (bottom - top) / 2;
                if (vslack > vspace / 4) {
                    vslack = vspace / 4;
                }
                int vs = this.mScrollY;
                if (top < vs + vslack) {
                    line = this.mLayout.getLineForVertical(vs + vslack + (bottom - top));
                } else if (bottom > vspace + vs - vslack) {
                    line = this.mLayout.getLineForVertical(vspace + vs - vslack - (bottom - top));
                }
                int hspace = this.getWidth() - this.getCompoundPaddingLeft() - this.getCompoundPaddingRight();
                int hs = this.mScrollX;
                int leftChar = this.mLayout.getOffsetForHorizontal(line, (float) hs);
                int rightChar = this.mLayout.getOffsetForHorizontal(line, (float) (hspace + hs));
                int lowChar = Math.min(leftChar, rightChar);
                int highChar = Math.max(leftChar, rightChar);
                int newStart = start;
                if (start < lowChar) {
                    newStart = lowChar;
                } else if (start > highChar) {
                    newStart = highChar;
                }
                if (newStart != start) {
                    Selection.setSelection(this.mSpannable, newStart);
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    private void getInterestingRect(@NonNull Rect r, int line) {
        this.convertFromViewportToContentCoordinates(r);
        if (line == 0) {
            r.top = r.top - this.getExtendedPaddingTop();
        }
        if (line == this.mLayout.getLineCount() - 1) {
            r.bottom = r.bottom + this.getExtendedPaddingBottom();
        }
    }

    private void convertFromViewportToContentCoordinates(@NonNull Rect r) {
        int horizontalOffset = this.viewportToContentHorizontalOffset();
        r.left += horizontalOffset;
        r.right += horizontalOffset;
        int verticalOffset = this.viewportToContentVerticalOffset();
        r.top += verticalOffset;
        r.bottom += verticalOffset;
    }

    int viewportToContentHorizontalOffset() {
        return this.getCompoundPaddingLeft() - this.mScrollX;
    }

    int viewportToContentVerticalOffset() {
        int offset = this.getExtendedPaddingTop() - this.mScrollY;
        if ((this.mGravity & 112) != 48) {
            offset += this.getVerticalOffset(false);
        }
        return offset;
    }

    public int getSelectionStart() {
        return Selection.getSelectionStart(this.getText());
    }

    public int getSelectionEnd() {
        return Selection.getSelectionEnd(this.getText());
    }

    public boolean hasSelection() {
        int selectionStart = this.getSelectionStart();
        int selectionEnd = this.getSelectionEnd();
        return selectionStart >= 0 && selectionEnd >= 0 && selectionStart != selectionEnd;
    }

    public void setSingleLine() {
        this.setSingleLine(true);
    }

    public void setSingleLine(boolean singleLine) {
        if (this.mSingleLine != singleLine) {
            this.applySingleLine(singleLine, true, true, true);
        }
    }

    private void applySingleLine(boolean singleLine, boolean applyTransformation, boolean changeMaxLines, boolean changeMaxLength) {
        this.mSingleLine = singleLine;
        if (singleLine) {
            this.setLines(1);
            this.setHorizontallyScrolling(true);
            if (applyTransformation) {
                this.setTransformationMethod(SingleLineTransformationMethod.getInstance());
            }
            if (!changeMaxLength) {
                return;
            }
            if (this.mBufferType != TextView.BufferType.EDITABLE) {
                return;
            }
            InputFilter[] prevFilters = this.getFilters();
            for (InputFilter filter : this.getFilters()) {
                if (filter instanceof InputFilter.LengthFilter) {
                    return;
                }
            }
            if (this.mSingleLineLengthFilter == null) {
                this.mSingleLineLengthFilter = new InputFilter.LengthFilter(5000);
            }
            InputFilter[] newFilters = new InputFilter[prevFilters.length + 1];
            System.arraycopy(prevFilters, 0, newFilters, 0, prevFilters.length);
            newFilters[prevFilters.length] = this.mSingleLineLengthFilter;
            this.setFilters(newFilters);
            this.setText(this.getText());
        } else {
            if (changeMaxLines) {
                this.setMaxLines(Integer.MAX_VALUE);
            }
            this.setHorizontallyScrolling(false);
            if (applyTransformation) {
                this.setTransformationMethod(null);
            }
            if (!changeMaxLength) {
                return;
            }
            if (this.mBufferType != TextView.BufferType.EDITABLE) {
                return;
            }
            InputFilter[] prevFilters = this.getFilters();
            if (prevFilters.length == 0) {
                return;
            }
            if (this.mSingleLineLengthFilter == null) {
                return;
            }
            int targetIndex = -1;
            for (int i = 0; i < prevFilters.length; i++) {
                if (prevFilters[i] == this.mSingleLineLengthFilter) {
                    targetIndex = i;
                    break;
                }
            }
            if (targetIndex == -1) {
                return;
            }
            if (prevFilters.length == 1) {
                this.setFilters(NO_FILTERS);
                return;
            }
            InputFilter[] newFilters = new InputFilter[prevFilters.length - 1];
            System.arraycopy(prevFilters, 0, newFilters, 0, targetIndex);
            System.arraycopy(prevFilters, targetIndex + 1, newFilters, targetIndex, prevFilters.length - targetIndex - 1);
            this.setFilters(newFilters);
            this.mSingleLineLengthFilter = null;
        }
    }

    public void setEllipsize(@Nullable TextUtils.TruncateAt where) {
        if (this.mEllipsize != where) {
            this.mEllipsize = where;
            if (this.mLayout != null) {
                this.nullLayouts();
                this.requestLayout();
                this.invalidate();
            }
        }
    }

    @Nullable
    public TextUtils.TruncateAt getEllipsize() {
        return this.mEllipsize;
    }

    public void setSelectAllOnFocus(boolean selectAllOnFocus) {
        this.createEditorIfNeeded();
        this.mEditor.mSelectAllOnFocus = selectAllOnFocus;
        if (selectAllOnFocus && !(this.mText instanceof Spannable)) {
            this.setText(this.mText, TextView.BufferType.SPANNABLE);
        }
    }

    public void setCursorVisible(boolean visible) {
        if (!visible || this.mEditor != null) {
            this.createEditorIfNeeded();
            if (this.mEditor.mCursorVisible != visible) {
                this.mEditor.mCursorVisible = visible;
                this.invalidate();
                this.mEditor.makeBlink();
            }
        }
    }

    public boolean isCursorVisible() {
        return this.mEditor == null || this.mEditor.mCursorVisible;
    }

    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
    }

    public void addTextChangedListener(@NonNull TextWatcher watcher) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(watcher);
    }

    public void removeTextChangedListener(@NonNull TextWatcher watcher) {
        if (this.mListeners != null) {
            this.mListeners.remove(watcher);
        }
    }

    private void sendBeforeTextChanged(CharSequence text, int start, int before, int after) {
        if (this.mListeners != null && this.mListeners.size() > 0) {
            for (TextWatcher textWatcher : this.mListeners) {
                textWatcher.beforeTextChanged(text, start, before, after);
            }
        }
    }

    private void sendOnTextChanged(CharSequence text, int start, int before, int after) {
        if (this.mListeners != null && this.mListeners.size() > 0) {
            for (TextWatcher textWatcher : this.mListeners) {
                textWatcher.onTextChanged(text, start, before, after);
            }
        }
        if (this.mEditor != null) {
            this.mEditor.sendOnTextChanged(start, before, after);
        }
    }

    private void sendAfterTextChanged(Editable text) {
        if (this.mListeners != null && this.mListeners.size() > 0) {
            for (TextWatcher textWatcher : this.mListeners) {
                textWatcher.afterTextChanged(text);
            }
        }
    }

    void handleTextChanged(CharSequence buffer, int start, int before, int after) {
        this.invalidate();
        int curs = this.getSelectionStart();
        if (curs >= 0 || (this.mGravity & 112) == 80) {
            this.registerForPreDraw();
        }
        this.checkForResize();
        if (curs >= 0) {
            this.mHighlightPathBogus = true;
            if (this.mEditor != null) {
                this.mEditor.makeBlink();
            }
            this.bringPointIntoView(curs);
        }
        this.sendOnTextChanged(buffer, start, before, after);
        this.onTextChanged(buffer, start, before, after);
    }

    private void spanChange(Spanned buf, Object what, int oldStart, int newStart, int oldEnd, int newEnd) {
        boolean selChanged = false;
        if (what == Selection.SELECTION_END) {
            selChanged = true;
            if (oldStart >= 0 || newStart >= 0) {
                this.invalidate();
                this.checkForResize();
                this.registerForPreDraw();
                if (this.mEditor != null) {
                    this.mEditor.makeBlink();
                }
            }
        }
        if (what == Selection.SELECTION_START) {
            selChanged = true;
            if (oldStart >= 0 || newStart >= 0) {
                this.invalidate();
            }
        }
        if (selChanged) {
            this.mHighlightPathBogus = true;
            if (this.mEditor != null && !this.isFocused()) {
                this.mEditor.mSelectionMoved = true;
            }
        }
        if (what instanceof UpdateAppearance || what instanceof ParagraphStyle || what instanceof CharacterStyle) {
            this.invalidate();
            this.mHighlightPathBogus = true;
            this.checkForResize();
        }
        if (TextKeyListener.isMetaTracker(what)) {
            this.mHighlightPathBogus = true;
            if (Selection.getSelectionStart(buf) >= 0 && this.getSelectionEnd() >= 0) {
                this.invalidate();
            }
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, @Nullable Rect previouslyFocusedRect) {
        if (this.isTemporarilyDetached()) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        } else {
            if (this.mEditor != null) {
                this.mEditor.onFocusChanged(focused, direction);
            }
            if (focused && this.mSpannable != null) {
                TextKeyListener.resetMetaState(this.mSpannable);
            }
            if (this.mTransformation != null) {
                this.mTransformation.onFocusChanged(this, this.mText, focused, direction, previouslyFocusedRect);
            }
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        int action = event.getAction();
        if (this.mEditor != null) {
            this.mEditor.onTouchEvent(event);
        }
        boolean superResult = super.onTouchEvent(event);
        if (this.mEditor != null && this.mEditor.mDiscardNextActionUp && action == 1) {
            this.mEditor.mDiscardNextActionUp = false;
            return superResult;
        } else {
            boolean touchIsFinished = action == 1 && (this.mEditor == null || !this.mEditor.mIgnoreActionUpEvent) && this.isFocused();
            if ((this.mMovement != null || this.mEditor != null) && this.isEnabled() && this.mSpannable != null && this.mLayout != null) {
                boolean handled = false;
                if (this.mMovement != null) {
                    handled = this.mMovement.onTouchEvent(this, this.mSpannable, event);
                }
                boolean textIsSelectable = this.isTextSelectable();
                if (touchIsFinished && this.mLinksClickable && textIsSelectable) {
                    List<ClickableSpan> links = this.mSpannable.getSpans(this.getSelectionStart(), this.getSelectionEnd(), ClickableSpan.class);
                    if (!links.isEmpty()) {
                        ((ClickableSpan) links.get(0)).onClick(this);
                        handled = true;
                    }
                }
                if (touchIsFinished && (this.isTextEditable() || textIsSelectable)) {
                    this.mEditor.onTouchUpEvent(event);
                    handled = true;
                }
                if (handled) {
                    return true;
                }
            }
            return superResult;
        }
    }

    @Override
    public boolean onGenericMotionEvent(@NonNull MotionEvent event) {
        return this.mMovement != null && this.mSpannable != null && this.mLayout != null && this.mMovement.onGenericMotionEvent(this, this.mSpannable, event) ? true : super.onGenericMotionEvent(event);
    }

    @Override
    protected void onCreateContextMenu(@NonNull ContextMenu menu) {
        if (this.mEditor != null) {
            this.mEditor.onCreateContextMenu(menu);
        }
    }

    @Override
    public boolean showContextMenu(float x, float y) {
        if (this.mEditor != null) {
            this.mEditor.setContextMenuAnchor(x, y);
        }
        return super.showContextMenu(x, y);
    }

    boolean isTextEditable() {
        return this.mText instanceof Editable && this.mEditor != null && this.isEnabled();
    }

    public boolean didTouchFocusSelect() {
        return this.mEditor != null && this.mEditor.mTouchFocusSelected;
    }

    @Override
    public void cancelLongPress() {
        super.cancelLongPress();
        if (this.mEditor != null) {
            this.mEditor.mIgnoreActionUpEvent = true;
        }
    }

    @Override
    protected int computeHorizontalScrollRange() {
        if (this.mLayout == null) {
            return super.computeHorizontalScrollRange();
        } else {
            return this.mSingleLine && (this.mGravity & 7) == 3 ? (int) this.mLayout.getLineWidth(0) : this.mLayout.getWidth();
        }
    }

    @Override
    protected int computeVerticalScrollRange() {
        return this.mLayout != null ? this.mLayout.getHeight() : super.computeVerticalScrollRange();
    }

    @Override
    protected int computeVerticalScrollExtent() {
        return this.getHeight() - this.getCompoundPaddingTop() - this.getCompoundPaddingBottom();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (!this.isEnabled()) {
            return super.onKeyDown(keyCode, event);
        } else {
            if (event.getRepeatCount() == 0 && !KeyEvent.isModifierKey(keyCode)) {
                this.mPreventDefaultMovement = false;
            }
            switch(keyCode) {
                case 257:
                case 335:
                    if (this.mSingleLine && this.mBufferType == TextView.BufferType.EDITABLE && event.hasNoModifiers()) {
                        if (this.hasOnClickListeners()) {
                            return super.onKeyDown(keyCode, event);
                        }
                        return true;
                    }
                    break;
                case 258:
                    if (event.hasNoModifiers() || event.isShiftPressed()) {
                        return super.onKeyDown(keyCode, event);
                    }
            }
            if (this.mEditor != null && this.mBufferType == TextView.BufferType.EDITABLE) {
                if (TextKeyListener.getInstance().onKeyDown(this, (Editable) this.mText, keyCode, event)) {
                    return true;
                }
                if ((keyCode == 257 || keyCode == 335) && !this.mSingleLine && (event.hasNoModifiers() || event.isShiftPressed())) {
                    int selStart = this.getSelectionStart();
                    int selEnd = this.getSelectionEnd();
                    ((Editable) this.mText).replace(Math.min(selStart, selEnd), Math.max(selStart, selEnd), "\n");
                    return true;
                }
                if (keyCode == 32 && (event.hasNoModifiers() || event.isShiftPressed())) {
                    return true;
                }
            }
            if (this.mMovement == null || this.mLayout == null) {
                return this.mPreventDefaultMovement && !KeyEvent.isModifierKey(keyCode) || super.onKeyDown(keyCode, event);
            } else if (this.mSpannable != null && this.mMovement.onKeyDown(this, this.mSpannable, keyCode, event)) {
                if (event.getRepeatCount() == 0 && !KeyEvent.isModifierKey(keyCode)) {
                    this.mPreventDefaultMovement = true;
                }
                return true;
            } else {
                switch(keyCode) {
                    case 262:
                    case 263:
                    case 264:
                    case 265:
                        return true;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (!this.isEnabled()) {
            return super.onKeyUp(keyCode, event);
        } else {
            if (!KeyEvent.isModifierKey(keyCode)) {
                this.mPreventDefaultMovement = false;
            }
            switch(keyCode) {
                case 257:
                case 335:
                    if (event.hasNoModifiers()) {
                        if (this.mSingleLine && this.mBufferType == TextView.BufferType.EDITABLE && !this.hasOnClickListeners()) {
                            View v = this.focusSearch(130);
                            if (v != null) {
                                if (!v.requestFocus(130)) {
                                    throw new IllegalStateException("focus search returned a view that wasn't able to take focus!");
                                }
                                super.onKeyUp(keyCode, event);
                                return true;
                            }
                        }
                        return super.onKeyUp(keyCode, event);
                    }
                default:
                    if (this.mEditor != null && this.mBufferType == TextView.BufferType.EDITABLE && TextKeyListener.getInstance().onKeyUp(this, (Editable) this.mText, keyCode, event)) {
                        return true;
                    } else {
                        return this.mMovement != null && this.mLayout != null && this.mSpannable != null && this.mMovement.onKeyUp(this, this.mSpannable, keyCode, event) ? true : super.onKeyUp(keyCode, event);
                    }
            }
        }
    }

    @Override
    public boolean onKeyShortcut(int keyCode, @NonNull KeyEvent event) {
        if (event.hasModifiers(KeyEvent.META_CTRL_ON)) {
            switch(keyCode) {
                case 65:
                    if (this.canSelectAllText()) {
                        return this.onTextContextMenuItem(16908319);
                    }
                    break;
                case 67:
                    if (this.canCopy()) {
                        return this.onTextContextMenuItem(16908321);
                    }
                    break;
                case 86:
                    if (this.canPaste()) {
                        return this.onTextContextMenuItem(16908322);
                    }
                    break;
                case 88:
                    if (this.canCut()) {
                        return this.onTextContextMenuItem(16908320);
                    }
                    break;
                case 89:
                    if (this.canRedo()) {
                        return this.onTextContextMenuItem(16908339);
                    }
                    break;
                case 90:
                    if (this.canUndo()) {
                        return this.onTextContextMenuItem(16908338);
                    }
            }
        }
        return super.onKeyShortcut(keyCode, event);
    }

    boolean textCanBeSelected() {
        return this.mMovement != null && this.mMovement.canSelectArbitrarily() ? this.isTextEditable() || this.isTextSelectable() && this.mSpannable != null && this.isEnabled() : false;
    }

    public WordIterator getWordIterator() {
        return this.mEditor != null ? this.mEditor.getWordIterator() : null;
    }

    public boolean onTextContextMenuItem(int id) {
        if (!this.isFocused()) {
            this.requestFocus();
        }
        int selStart = this.getSelectionStart();
        int selEnd = this.getSelectionEnd();
        int min = Math.max(0, Math.min(selStart, selEnd));
        int max = Math.max(0, Math.max(selStart, selEnd));
        switch(id) {
            case 16908319:
                this.selectAllText();
                return true;
            case 16908320:
                CharSequence cut = this.mTransformed.subSequence(min, max);
                Core.executeOnMainThread(() -> Clipboard.setText(cut));
                this.getEditableText().delete(min, max);
                return true;
            case 16908321:
                CharSequence copy = this.mTransformed.subSequence(min, max);
                Core.executeOnMainThread(() -> Clipboard.setText(copy));
                return true;
            case 16908322:
                Core.executeOnMainThread(() -> {
                    String replacement = Clipboard.getText();
                    if (replacement != null) {
                        this.post(() -> {
                            Editable editable = this.getEditableText();
                            Selection.setSelection(editable, max);
                            editable.replace(min, max, replacement);
                        });
                    }
                });
                return true;
            case 16908338:
                if (this.mEditor != null) {
                    this.mEditor.undo();
                }
                return true;
            case 16908339:
                if (this.mEditor != null) {
                    this.mEditor.redo();
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        TextDirectionHeuristic newTextDir = this.getTextDirectionHeuristic();
        if (this.mTextDir != newTextDir) {
            this.mTextDir = newTextDir;
            if (this.mLayout != null) {
                this.checkForRelayout();
            }
        }
    }

    @NonNull
    public TextDirectionHeuristic getTextDirectionHeuristic() {
        if (this.hasPasswordTransformationMethod()) {
            return TextDirectionHeuristics.LTR;
        } else {
            return switch(this.getTextDirection()) {
                case 2 ->
                    TextDirectionHeuristics.ANYRTL_LTR;
                case 3 ->
                    TextDirectionHeuristics.LTR;
                case 4 ->
                    TextDirectionHeuristics.RTL;
                case 5 ->
                    TextDirectionHeuristics.LOCALE;
                case 6 ->
                    TextDirectionHeuristics.FIRSTSTRONG_LTR;
                case 7 ->
                    TextDirectionHeuristics.FIRSTSTRONG_RTL;
                default ->
                    this.isLayoutRtl() ? TextDirectionHeuristics.FIRSTSTRONG_RTL : TextDirectionHeuristics.FIRSTSTRONG_LTR;
            };
        }
    }

    @Override
    public void onResolveDrawables(int layoutDirection) {
        if (this.mLastLayoutDirection != layoutDirection) {
            this.mLastLayoutDirection = layoutDirection;
            if (this.mDrawables != null && this.mDrawables.resolveWithLayoutDirection(layoutDirection)) {
                this.prepareDrawableForDisplay(this.mDrawables.mShowing[0]);
                this.prepareDrawableForDisplay(this.mDrawables.mShowing[2]);
            }
        }
    }

    private void prepareDrawableForDisplay(@Nullable Drawable dr) {
        if (dr != null) {
            dr.setLayoutDirection(this.getLayoutDirection());
        }
    }

    @Override
    public PointerIcon onResolvePointerIcon(@NonNull MotionEvent event) {
        if (this.mSpannable != null && this.mLinksClickable) {
            float x = event.getX();
            float y = event.getY();
            int offset = this.getOffsetForPosition(x, y);
            List<ClickableSpan> clickables = this.mSpannable.getSpans(offset, offset, ClickableSpan.class);
            if (!clickables.isEmpty()) {
                return PointerIcon.getSystemIcon(1002);
            }
        }
        return !this.isTextSelectable() && !this.isTextEditable() ? super.onResolvePointerIcon(event) : PointerIcon.getSystemIcon(1008);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (this.mEditor != null) {
            this.mEditor.makeBlink();
        }
    }

    public int getOffsetForPosition(float x, float y) {
        if (this.getLayout() == null) {
            return -1;
        } else {
            int line = this.getLineAtCoordinate(y);
            return this.getOffsetAtCoordinate(line, x);
        }
    }

    int getLineAtCoordinate(float y) {
        y -= (float) this.getTotalPaddingTop();
        y = Math.max(0.0F, y);
        y = Math.min((float) (this.getHeight() - this.getTotalPaddingBottom() - 1), y);
        y += (float) this.getScrollY();
        return this.getLayout().getLineForVertical((int) y);
    }

    float convertToLocalHorizontalCoordinate(float x) {
        x -= (float) this.getTotalPaddingLeft();
        x = Math.max(0.0F, x);
        x = Math.min((float) (this.getWidth() - this.getTotalPaddingRight() - 1), x);
        return x + (float) this.getScrollX();
    }

    int getOffsetAtCoordinate(int line, float x) {
        x = this.convertToLocalHorizontalCoordinate(x);
        return this.getLayout().getOffsetForHorizontal(line, x);
    }

    boolean canUndo() {
        return this.mEditor != null && this.mEditor.canUndo();
    }

    boolean canRedo() {
        return this.mEditor != null && this.mEditor.canRedo();
    }

    boolean canCut() {
        return this.hasPasswordTransformationMethod() ? false : this.mText.length() > 0 && this.hasSelection() && this.mText instanceof Editable && this.mEditor != null && this.mBufferType == TextView.BufferType.EDITABLE;
    }

    boolean canCopy() {
        return this.hasPasswordTransformationMethod() ? false : this.mText.length() > 0 && this.hasSelection() && this.mEditor != null;
    }

    boolean canPaste() {
        return this.mText instanceof Editable && this.mEditor != null && this.mBufferType == TextView.BufferType.EDITABLE && this.getSelectionStart() >= 0 && this.getSelectionEnd() >= 0;
    }

    boolean canSelectAllText() {
        return this.mText.length() != 0 && this.mEditor != null && this.mLayout != null && !this.hasPasswordTransformationMethod() && (this.getSelectionStart() != 0 || this.getSelectionEnd() != this.mText.length());
    }

    boolean selectAllText() {
        if (this.mSpannable == null) {
            return false;
        } else {
            int length = this.mText.length();
            Selection.setSelection(this.mSpannable, 0, length);
            return length > 0;
        }
    }

    private void createEditorIfNeeded() {
        if (this.mEditor == null) {
            this.mEditor = new Editor(this);
        }
    }

    public static enum BufferType {

        NORMAL, SPANNABLE, EDITABLE
    }

    private class ChangeWatcher implements TextWatcher, SpanWatcher {

        @Override
        public void onSpanAdded(Spannable text, Object what, int start, int end) {
            TextView.this.spanChange(text, what, -1, start, -1, end);
        }

        @Override
        public void onSpanRemoved(Spannable text, Object what, int start, int end) {
            TextView.this.spanChange(text, what, start, -1, end, -1);
        }

        @Override
        public void onSpanChanged(Spannable text, Object what, int ost, int oen, int nst, int nen) {
            TextView.this.spanChange(text, what, ost, nst, oen, nen);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            TextView.this.sendBeforeTextChanged(s, start, count, after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            TextView.this.handleTextChanged(s, start, before, count);
        }

        @Override
        public void afterTextChanged(Editable s) {
            TextView.this.sendAfterTextChanged(s);
        }
    }

    static class Drawables {

        static final int LEFT = 0;

        static final int TOP = 1;

        static final int RIGHT = 2;

        static final int BOTTOM = 3;

        static final int DRAWABLE_NONE = -1;

        static final int DRAWABLE_RIGHT = 0;

        static final int DRAWABLE_LEFT = 1;

        final Rect mCompoundRect = new Rect();

        final Drawable[] mShowing = new Drawable[4];

        boolean mHasTint;

        boolean mHasTintMode;

        Drawable mDrawableStart;

        Drawable mDrawableEnd;

        Drawable mDrawableTemp;

        Drawable mDrawableLeftInitial;

        Drawable mDrawableRightInitial;

        boolean mOverride;

        int mDrawableSizeTop;

        int mDrawableSizeBottom;

        int mDrawableSizeLeft;

        int mDrawableSizeRight;

        int mDrawableSizeStart;

        int mDrawableSizeEnd;

        int mDrawableSizeTemp;

        int mDrawableWidthTop;

        int mDrawableWidthBottom;

        int mDrawableHeightLeft;

        int mDrawableHeightRight;

        int mDrawableHeightStart;

        int mDrawableHeightEnd;

        int mDrawableHeightTemp;

        int mDrawablePadding;

        int mDrawableSaved = -1;

        public Drawables() {
            this.mOverride = false;
        }

        public boolean hasMetadata() {
            return this.mDrawablePadding != 0 || this.mHasTintMode || this.mHasTint;
        }

        public boolean resolveWithLayoutDirection(int layoutDirection) {
            Drawable previousLeft = this.mShowing[0];
            Drawable previousRight = this.mShowing[2];
            this.mShowing[0] = this.mDrawableLeftInitial;
            this.mShowing[2] = this.mDrawableRightInitial;
            if (!ModernUI.getInstance().hasRtlSupport()) {
                if (this.mDrawableStart != null && this.mShowing[0] == null) {
                    this.mShowing[0] = this.mDrawableStart;
                    this.mDrawableSizeLeft = this.mDrawableSizeStart;
                    this.mDrawableHeightLeft = this.mDrawableHeightStart;
                }
                if (this.mDrawableEnd != null && this.mShowing[2] == null) {
                    this.mShowing[2] = this.mDrawableEnd;
                    this.mDrawableSizeRight = this.mDrawableSizeEnd;
                    this.mDrawableHeightRight = this.mDrawableHeightEnd;
                }
            } else {
                switch(layoutDirection) {
                    case 0:
                    default:
                        if (this.mOverride) {
                            this.mShowing[0] = this.mDrawableStart;
                            this.mDrawableSizeLeft = this.mDrawableSizeStart;
                            this.mDrawableHeightLeft = this.mDrawableHeightStart;
                            this.mShowing[2] = this.mDrawableEnd;
                            this.mDrawableSizeRight = this.mDrawableSizeEnd;
                            this.mDrawableHeightRight = this.mDrawableHeightEnd;
                        }
                        break;
                    case 1:
                        if (this.mOverride) {
                            this.mShowing[2] = this.mDrawableStart;
                            this.mDrawableSizeRight = this.mDrawableSizeStart;
                            this.mDrawableHeightRight = this.mDrawableHeightStart;
                            this.mShowing[0] = this.mDrawableEnd;
                            this.mDrawableSizeLeft = this.mDrawableSizeEnd;
                            this.mDrawableHeightLeft = this.mDrawableHeightEnd;
                        }
                }
            }
            this.applyErrorDrawableIfNeeded(layoutDirection);
            return this.mShowing[0] != previousLeft || this.mShowing[2] != previousRight;
        }

        private void applyErrorDrawableIfNeeded(int layoutDirection) {
            switch(this.mDrawableSaved) {
                case 0:
                    this.mShowing[2] = this.mDrawableTemp;
                    this.mDrawableSizeRight = this.mDrawableSizeTemp;
                    this.mDrawableHeightRight = this.mDrawableHeightTemp;
                    break;
                case 1:
                    this.mShowing[0] = this.mDrawableTemp;
                    this.mDrawableSizeLeft = this.mDrawableSizeTemp;
                    this.mDrawableHeightLeft = this.mDrawableHeightTemp;
            }
        }
    }
}