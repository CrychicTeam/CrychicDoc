package icyllis.modernui.view.menu;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.widget.PopupWindow;

public class MenuPopupHelper implements MenuHelper {

    private static final int TOUCH_EPICENTER_SIZE_DP = 48;

    private final Context mContext;

    private final MenuBuilder mMenu;

    private final boolean mOverflowOnly;

    private View mAnchorView;

    private int mDropDownGravity = 8388611;

    private boolean mForceShowIcon;

    private MenuPresenter.Callback mPresenterCallback;

    private MenuPopup mPopup;

    private PopupWindow.OnDismissListener mOnDismissListener;

    private final PopupWindow.OnDismissListener mInternalOnDismissListener = this::onDismiss;

    public MenuPopupHelper(@NonNull Context context, @NonNull MenuBuilder menu) {
        this(context, menu, null, false);
    }

    public MenuPopupHelper(@NonNull Context context, @NonNull MenuBuilder menu, View anchorView) {
        this(context, menu, anchorView, false);
    }

    public MenuPopupHelper(@NonNull Context context, @NonNull MenuBuilder menu, View anchorView, boolean overflowOnly) {
        this.mContext = context;
        this.mMenu = menu;
        this.mAnchorView = anchorView;
        this.mOverflowOnly = overflowOnly;
    }

    public void setOnDismissListener(@Nullable PopupWindow.OnDismissListener listener) {
        this.mOnDismissListener = listener;
    }

    public void setAnchorView(@NonNull View anchor) {
        this.mAnchorView = anchor;
    }

    public void setForceShowIcon(boolean forceShowIcon) {
        this.mForceShowIcon = forceShowIcon;
        if (this.mPopup != null) {
            this.mPopup.setForceShowIcon(forceShowIcon);
        }
    }

    public void setGravity(int gravity) {
        this.mDropDownGravity = gravity;
    }

    public int getGravity() {
        return this.mDropDownGravity;
    }

    public void show() {
        if (!this.tryShow()) {
            throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
        }
    }

    public void show(int x, int y) {
        if (!this.tryShow(x, y)) {
            throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
        }
    }

    @NonNull
    public MenuPopup getPopup() {
        if (this.mPopup == null) {
            this.mPopup = this.createPopup();
        }
        return this.mPopup;
    }

    public boolean tryShow() {
        if (this.isShowing()) {
            return true;
        } else if (this.mAnchorView == null) {
            return false;
        } else {
            this.showPopup(0, 0, false, false);
            return true;
        }
    }

    public boolean tryShow(int x, int y) {
        if (this.isShowing()) {
            return true;
        } else if (this.mAnchorView == null) {
            return false;
        } else {
            this.showPopup(x, y, true, true);
            return true;
        }
    }

    @NonNull
    private MenuPopup createPopup() {
        boolean enableCascadingSubmenus = true;
        MenuPopup popup = new CascadingMenuPopup(this.mContext, this.mAnchorView, this.mOverflowOnly);
        popup.addMenu(this.mMenu);
        popup.setOnDismissListener(this.mInternalOnDismissListener);
        popup.setAnchorView(this.mAnchorView);
        popup.setCallback(this.mPresenterCallback);
        popup.setForceShowIcon(this.mForceShowIcon);
        popup.setGravity(this.mDropDownGravity);
        return popup;
    }

    private void showPopup(int xOffset, int yOffset, boolean useOffsets, boolean showTitle) {
        MenuPopup popup = this.getPopup();
        popup.setShowTitle(showTitle);
        if (useOffsets) {
            int hgrav = Gravity.getAbsoluteGravity(this.mDropDownGravity, this.mAnchorView.getLayoutDirection()) & 7;
            if (hgrav == 5) {
                xOffset -= this.mAnchorView.getWidth();
            }
            popup.setHorizontalOffset(xOffset);
            popup.setVerticalOffset(yOffset);
            float density = this.mContext.getResources().getDisplayMetrics().density;
            int halfSize = (int) (48.0F * density / 2.0F);
            Rect epicenter = new Rect(xOffset - halfSize, yOffset - halfSize, xOffset + halfSize, yOffset + halfSize);
            popup.setEpicenterBounds(epicenter);
        }
        popup.show();
    }

    @Override
    public void dismiss() {
        if (this.isShowing()) {
            this.mPopup.dismiss();
        }
    }

    protected void onDismiss() {
        this.mPopup = null;
        if (this.mOnDismissListener != null) {
            this.mOnDismissListener.onDismiss();
        }
    }

    public boolean isShowing() {
        return this.mPopup != null && this.mPopup.isShowing();
    }

    @Override
    public void setPresenterCallback(@Nullable MenuPresenter.Callback cb) {
        this.mPresenterCallback = cb;
        if (this.mPopup != null) {
            this.mPopup.setCallback(cb);
        }
    }
}