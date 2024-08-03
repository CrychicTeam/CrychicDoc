package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.View;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class DropDownListView extends ListView {

    private boolean mListSelectionHidden;

    private boolean mHijackFocus;

    private boolean mDrawsInPressedState;

    private DropDownListView.ResolveHoverRunnable mResolveHoverRunnable;

    public DropDownListView(Context context, boolean hijackFocus) {
        super(context);
        this.mHijackFocus = hijackFocus;
    }

    @Override
    boolean shouldShowSelector() {
        return this.isHovered() || super.shouldShowSelector();
    }

    @Override
    public boolean onTouchEvent(@Nonnull MotionEvent ev) {
        if (this.mResolveHoverRunnable != null) {
            this.mResolveHoverRunnable.cancel();
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onHoverEvent(@Nonnull MotionEvent ev) {
        int action = ev.getAction();
        if (action == 10 && this.mResolveHoverRunnable == null) {
            this.mResolveHoverRunnable = new DropDownListView.ResolveHoverRunnable();
            this.mResolveHoverRunnable.post();
        }
        boolean handled = super.onHoverEvent(ev);
        if (action == 9 || action == 7) {
            int position = this.pointToPosition((int) ev.getX(), (int) ev.getY());
            if (position != -1 && position != this.mSelectedPosition) {
                View hoveredItem = this.getChildAt(position - this.getFirstVisiblePosition());
                if (hoveredItem.isEnabled()) {
                    this.requestFocus();
                    this.positionSelector(position, hoveredItem);
                    this.setSelectedPositionInt(position);
                    this.setNextSelectedPositionInt(position);
                }
                this.updateSelectorState();
            }
        } else if (!super.shouldShowSelector()) {
            this.setSelectedPositionInt(-1);
            this.setNextSelectedPositionInt(-1);
        }
        return handled;
    }

    @Override
    protected void drawableStateChanged() {
        if (this.mResolveHoverRunnable == null) {
            super.drawableStateChanged();
        }
    }

    public boolean onForwardedEvent(@Nonnull MotionEvent event) {
        boolean handledEvent = true;
        boolean clearPressedItem = false;
        int action = event.getAction();
        switch(action) {
            case 1:
                handledEvent = false;
            case 2:
                int x = (int) event.getX();
                int y = (int) event.getY();
                int position = this.pointToPosition(x, y);
                if (position == -1) {
                    clearPressedItem = true;
                } else {
                    View child = this.getChildAt(position - this.getFirstVisiblePosition());
                    this.setPressedItem(child, position, (float) x, (float) y);
                    handledEvent = true;
                    if (action == 1) {
                        long id = this.getItemIdAtPosition(position);
                        this.performItemClick(child, position, id);
                    }
                }
                break;
            case 3:
                handledEvent = false;
        }
        if (!handledEvent || clearPressedItem) {
            this.clearPressedItem();
        }
        return handledEvent;
    }

    public void setListSelectionHidden(boolean hideListSelection) {
        this.mListSelectionHidden = hideListSelection;
    }

    private void clearPressedItem() {
        this.mDrawsInPressedState = false;
        this.setPressed(false);
        this.updateSelectorState();
        View motionView = this.getChildAt(this.mMotionPosition - this.mFirstPosition);
        if (motionView != null) {
            motionView.setPressed(false);
        }
    }

    private void setPressedItem(@Nonnull View child, int position, float x, float y) {
        this.mDrawsInPressedState = true;
        this.drawableHotspotChanged(x, y);
        if (!this.isPressed()) {
            this.setPressed(true);
        }
        if (this.mDataChanged) {
            this.layoutChildren();
        }
        View motionView = this.getChildAt(this.mMotionPosition - this.mFirstPosition);
        if (motionView != null && motionView != child && motionView.isPressed()) {
            motionView.setPressed(false);
        }
        this.mMotionPosition = position;
        float childX = x - (float) child.getLeft();
        float childY = y - (float) child.getTop();
        child.drawableHotspotChanged(childX, childY);
        if (!child.isPressed()) {
            child.setPressed(true);
        }
        this.setSelectedPositionInt(position);
        this.positionSelectorLikeTouch(position, child, x, y);
        this.refreshDrawableState();
    }

    @Override
    boolean touchModeDrawsInPressedState() {
        return this.mDrawsInPressedState || super.touchModeDrawsInPressedState();
    }

    @Override
    View obtainView(int position, boolean[] isScrap) {
        View view = super.obtainView(position, isScrap);
        if (view instanceof TextView) {
            ((TextView) view).setHorizontallyScrolling(true);
        }
        return view;
    }

    @Override
    public boolean isInTouchMode() {
        return this.mHijackFocus && this.mListSelectionHidden || super.isInTouchMode();
    }

    @Override
    public boolean hasWindowFocus() {
        return this.mHijackFocus || super.hasWindowFocus();
    }

    @Override
    public boolean isFocused() {
        return this.mHijackFocus || super.isFocused();
    }

    @Override
    public boolean hasFocus() {
        return this.mHijackFocus || super.hasFocus();
    }

    private class ResolveHoverRunnable implements Runnable {

        public void run() {
            DropDownListView.this.mResolveHoverRunnable = null;
            DropDownListView.this.drawableStateChanged();
        }

        public void cancel() {
            DropDownListView.this.mResolveHoverRunnable = null;
            DropDownListView.this.removeCallbacks(this);
        }

        public void post() {
            DropDownListView.this.post(this);
        }
    }
}