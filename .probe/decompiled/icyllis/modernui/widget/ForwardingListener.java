package icyllis.modernui.widget;

import icyllis.modernui.core.Core;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewConfiguration;
import icyllis.modernui.view.ViewParent;
import icyllis.modernui.view.menu.ShowableListMenu;
import javax.annotation.Nonnull;

public abstract class ForwardingListener implements View.OnTouchListener, View.OnAttachStateChangeListener {

    private final float mScaledTouchSlop;

    private final int mTapTimeout;

    private final int mLongPressTimeout;

    private final View mView;

    private Runnable mDisallowIntercept;

    private Runnable mTriggerLongPress;

    private boolean mForwarding;

    public ForwardingListener(@Nonnull View view) {
        this.mView = view;
        view.setLongClickable(true);
        view.addOnAttachStateChangeListener(this);
        this.mScaledTouchSlop = (float) ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
        this.mTapTimeout = ViewConfiguration.getTapTimeout();
        this.mLongPressTimeout = (this.mTapTimeout + ViewConfiguration.getLongPressTimeout()) / 2;
    }

    public abstract ShowableListMenu getPopup();

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean wasForwarding = this.mForwarding;
        boolean forwarding;
        if (wasForwarding) {
            forwarding = this.onTouchForwarded(event) || !this.onForwardingStopped();
        } else {
            forwarding = this.onTouchObserved(event) && this.onForwardingStarted();
            if (forwarding) {
                long now = Core.timeNanos();
                MotionEvent e = MotionEvent.obtain(now, 3, 0.0F, 0.0F, 0);
                this.mView.onTouchEvent(e);
                e.recycle();
            }
        }
        this.mForwarding = forwarding;
        return forwarding || wasForwarding;
    }

    @Override
    public void onViewAttachedToWindow(View v) {
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        this.mForwarding = false;
        if (this.mDisallowIntercept != null) {
            this.mView.removeCallbacks(this.mDisallowIntercept);
        }
    }

    protected boolean onForwardingStarted() {
        ShowableListMenu popup = this.getPopup();
        if (popup != null && !popup.isShowing()) {
            popup.show();
        }
        return true;
    }

    protected boolean onForwardingStopped() {
        ShowableListMenu popup = this.getPopup();
        if (popup != null && popup.isShowing()) {
            popup.dismiss();
        }
        return true;
    }

    private boolean onTouchObserved(MotionEvent event) {
        View view = this.mView;
        if (!view.isEnabled()) {
            return false;
        } else {
            switch(event.getAction()) {
                case 0:
                    if (this.mDisallowIntercept == null) {
                        this.mDisallowIntercept = new ForwardingListener.DisallowIntercept();
                    }
                    view.postDelayed(this.mDisallowIntercept, (long) this.mTapTimeout);
                    if (this.mTriggerLongPress == null) {
                        this.mTriggerLongPress = new ForwardingListener.TriggerLongPress();
                    }
                    view.postDelayed(this.mTriggerLongPress, (long) this.mLongPressTimeout);
                    break;
                case 1:
                case 3:
                    this.clearCallbacks();
                    break;
                case 2:
                    float x = event.getX();
                    float y = event.getY();
                    if (!view.pointInView(x, y, this.mScaledTouchSlop)) {
                        this.clearCallbacks();
                        ViewParent parent = view.getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                        return true;
                    }
            }
            return false;
        }
    }

    private void clearCallbacks() {
        if (this.mTriggerLongPress != null) {
            this.mView.removeCallbacks(this.mTriggerLongPress);
        }
        if (this.mDisallowIntercept != null) {
            this.mView.removeCallbacks(this.mDisallowIntercept);
        }
    }

    private void onLongPress() {
        this.clearCallbacks();
        View view = this.mView;
        if (view.isEnabled() && !view.isLongClickable()) {
            if (this.onForwardingStarted()) {
                ViewParent parent = view.getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                long now = Core.timeNanos();
                MotionEvent e = MotionEvent.obtain(now, 3, 0.0F, 0.0F, 0);
                view.onTouchEvent(e);
                e.recycle();
                this.mForwarding = true;
            }
        }
    }

    private boolean onTouchForwarded(MotionEvent event) {
        ShowableListMenu popup = this.getPopup();
        if (popup != null && popup.isShowing()) {
            DropDownListView target = (DropDownListView) popup.getListView();
            if (target != null && target.isShown()) {
                MotionEvent targetEvent = event.copy();
                this.mView.toGlobalMotionEvent(targetEvent);
                target.toLocalMotionEvent(targetEvent);
                boolean handled = target.onForwardedEvent(targetEvent);
                targetEvent.recycle();
                int action = event.getAction();
                boolean keepForwarding = action != 1 && action != 3;
                return handled && keepForwarding;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private class DisallowIntercept implements Runnable {

        public void run() {
            ViewParent parent = ForwardingListener.this.mView.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
    }

    private class TriggerLongPress implements Runnable {

        public void run() {
            ForwardingListener.this.onLongPress();
        }
    }
}