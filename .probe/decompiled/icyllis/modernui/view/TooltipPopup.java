package icyllis.modernui.view;

import icyllis.modernui.app.Activity;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.drawable.ShapeDrawable;
import icyllis.modernui.widget.TextView;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class TooltipPopup {

    private final Context mContext;

    private final TextView mTextView;

    private final WindowManager.LayoutParams mParams;

    private final int[] mTmpAnchorPos = new int[2];

    public TooltipPopup(Context context) {
        this.mContext = context;
        this.mTextView = new TextView(context);
        this.mTextView.setTextColor(-1);
        this.mTextView.setTextSize(14.0F);
        this.mParams = new WindowManager.LayoutParams();
        this.mParams.width = -2;
        this.mParams.height = -2;
        this.mParams.flags = 8;
        this.mParams.type = 1005;
        ShapeDrawable background = new ShapeDrawable();
        background.setShape(0);
        background.setColor(-432983759);
        background.setCornerRadius((float) this.mTextView.dp(2.0F));
        this.mTextView.setBackground(background);
    }

    public void show(View anchorView, int anchorX, int anchorY, boolean fromTouch, CharSequence tooltipText) {
        if (this.isShowing()) {
            this.hide();
        }
        this.mTextView.setText(tooltipText);
        this.mTextView.setMaxWidth(Math.min(anchorView.getRootView().getMeasuredWidth() / 2, this.mTextView.dp(512.0F)));
        this.mTextView.setPadding(this.mTextView.dp(16.0F), this.mTextView.dp(6.5F), this.mTextView.dp(16.0F), this.mTextView.dp(6.5F));
        this.computePosition(anchorView, anchorX, anchorY, fromTouch, this.mParams);
        WindowManager wm = ((Activity) this.mContext).getWindowManager();
        wm.addView(this.mTextView, this.mParams);
    }

    public void hide() {
        if (this.isShowing()) {
            WindowManager wm = ((Activity) this.mContext).getWindowManager();
            wm.removeView(this.mTextView);
        }
    }

    public View getContentView() {
        return this.mTextView;
    }

    public boolean isShowing() {
        return this.mTextView.getParent() != null;
    }

    private void computePosition(View anchorView, int anchorX, int anchorY, boolean fromTouch, WindowManager.LayoutParams outParams) {
        int tooltipPreciseAnchorThreshold = this.mTextView.dp(96.0F);
        int offsetX;
        if (anchorView.getWidth() >= tooltipPreciseAnchorThreshold) {
            offsetX = anchorX;
        } else {
            offsetX = anchorView.getWidth() / 2;
        }
        int offsetBelow;
        int offsetAbove;
        if (anchorView.getHeight() >= tooltipPreciseAnchorThreshold) {
            int offsetExtra = this.mTextView.dp(8.0F);
            offsetBelow = anchorY + offsetExtra;
            offsetAbove = anchorY - offsetExtra;
        } else {
            offsetBelow = anchorView.getHeight();
            offsetAbove = 0;
        }
        outParams.gravity = 49;
        int tooltipOffset;
        if (fromTouch) {
            tooltipOffset = this.mTextView.dp(16.0F);
        } else {
            tooltipOffset = 0;
        }
        View appView = anchorView.getRootView();
        anchorView.getLocationOnScreen(this.mTmpAnchorPos);
        outParams.x = this.mTmpAnchorPos[0] + offsetX - appView.getMeasuredWidth() / 2;
        int spec = MeasureSpec.makeMeasureSpec(0, 0);
        this.mTextView.measure(spec, spec);
        int tooltipHeight = this.mTextView.getMeasuredHeight();
        int yAbove = this.mTmpAnchorPos[1] + offsetAbove - tooltipOffset - tooltipHeight;
        int yBelow = this.mTmpAnchorPos[1] + offsetBelow + tooltipOffset;
        if (fromTouch) {
            if (yAbove >= 0) {
                outParams.y = yAbove;
            } else {
                outParams.y = yBelow;
            }
        } else if (yBelow + tooltipHeight <= appView.getMeasuredHeight()) {
            outParams.y = yBelow;
        } else {
            outParams.y = yAbove;
        }
    }
}