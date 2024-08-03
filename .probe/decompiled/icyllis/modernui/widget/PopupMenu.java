package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.view.Menu;
import icyllis.modernui.view.MenuItem;
import icyllis.modernui.view.View;
import icyllis.modernui.view.menu.MenuBuilder;
import icyllis.modernui.view.menu.MenuPopupHelper;
import icyllis.modernui.view.menu.ShowableListMenu;

public class PopupMenu {

    private final Context mContext;

    private final MenuBuilder mMenu;

    private final View mAnchor;

    private final MenuPopupHelper mPopup;

    private PopupMenu.OnMenuItemClickListener mMenuItemClickListener;

    private PopupMenu.OnDismissListener mOnDismissListener;

    private View.OnTouchListener mDragListener;

    public PopupMenu(Context context, View anchor) {
        this(context, anchor, 0);
    }

    public PopupMenu(Context context, View anchor, int gravity) {
        this.mContext = context;
        this.mAnchor = anchor;
        this.mMenu = new MenuBuilder(context);
        this.mMenu.setCallback(new MenuBuilder.Callback() {

            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                return PopupMenu.this.mMenuItemClickListener != null ? PopupMenu.this.mMenuItemClickListener.onMenuItemClick(item) : false;
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {
            }
        });
        this.mPopup = new MenuPopupHelper(context, this.mMenu, anchor, false);
        this.mPopup.setGravity(gravity);
        this.mPopup.setOnDismissListener(() -> {
            if (this.mOnDismissListener != null) {
                this.mOnDismissListener.onDismiss(this);
            }
        });
    }

    public void setGravity(int gravity) {
        this.mPopup.setGravity(gravity);
    }

    public int getGravity() {
        return this.mPopup.getGravity();
    }

    public View.OnTouchListener getDragToOpenListener() {
        if (this.mDragListener == null) {
            this.mDragListener = new ForwardingListener(this.mAnchor) {

                @Override
                protected boolean onForwardingStarted() {
                    PopupMenu.this.show();
                    return true;
                }

                @Override
                protected boolean onForwardingStopped() {
                    PopupMenu.this.dismiss();
                    return true;
                }

                @Override
                public ShowableListMenu getPopup() {
                    return PopupMenu.this.mPopup.getPopup();
                }
            };
        }
        return this.mDragListener;
    }

    public Menu getMenu() {
        return this.mMenu;
    }

    public void show() {
        this.mPopup.show();
    }

    public void dismiss() {
        this.mPopup.dismiss();
    }

    public void setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener listener) {
        this.mMenuItemClickListener = listener;
    }

    public void setOnDismissListener(PopupMenu.OnDismissListener listener) {
        this.mOnDismissListener = listener;
    }

    public void setForceShowIcon(boolean forceShowIcon) {
        this.mPopup.setForceShowIcon(forceShowIcon);
    }

    public ListView getMenuListView() {
        return !this.mPopup.isShowing() ? null : this.mPopup.getPopup().getListView();
    }

    @FunctionalInterface
    public interface OnDismissListener {

        void onDismiss(PopupMenu var1);
    }

    @FunctionalInterface
    public interface OnMenuItemClickListener {

        boolean onMenuItemClick(MenuItem var1);
    }
}