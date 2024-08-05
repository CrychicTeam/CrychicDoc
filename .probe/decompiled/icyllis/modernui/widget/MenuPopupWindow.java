package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.transition.Transition;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.MenuItem;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.menu.ListMenuItemView;
import icyllis.modernui.view.menu.MenuAdapter;
import icyllis.modernui.view.menu.MenuBuilder;
import javax.annotation.Nonnull;

public class MenuPopupWindow extends ListPopupWindow implements MenuItemHoverListener {

    private MenuItemHoverListener mHoverListener;

    public MenuPopupWindow(Context context) {
        super(context);
    }

    @Nonnull
    @Override
    DropDownListView createDropDownListView(Context context, boolean hijackFocus) {
        MenuPopupWindow.MenuDropDownListView view = new MenuPopupWindow.MenuDropDownListView(context, hijackFocus);
        view.setHoverListener(this);
        view.setPadding(0, view.dp(2.0F), 0, view.dp(2.0F));
        return view;
    }

    public void setEnterTransition(Transition enterTransition) {
        this.mPopup.setEnterTransition(enterTransition);
    }

    public void setExitTransition(Transition exitTransition) {
        this.mPopup.setExitTransition(exitTransition);
    }

    public void setHoverListener(MenuItemHoverListener hoverListener) {
        this.mHoverListener = hoverListener;
    }

    public void setTouchModal(boolean touchModal) {
        this.mPopup.setTouchModal(touchModal);
    }

    @Override
    public void onItemHoverEnter(@Nonnull MenuBuilder menu, @Nonnull MenuItem item) {
        if (this.mHoverListener != null) {
            this.mHoverListener.onItemHoverEnter(menu, item);
        }
    }

    @Override
    public void onItemHoverExit(@Nonnull MenuBuilder menu, @Nonnull MenuItem item) {
        if (this.mHoverListener != null) {
            this.mHoverListener.onItemHoverExit(menu, item);
        }
    }

    public static class MenuDropDownListView extends DropDownListView {

        final int mAdvanceKey = 262;

        final int mRetreatKey = 263;

        private MenuItemHoverListener mHoverListener;

        private MenuItem mHoveredMenuItem;

        public MenuDropDownListView(Context context, boolean hijackFocus) {
            super(context, hijackFocus);
        }

        public void setHoverListener(MenuItemHoverListener hoverListener) {
            this.mHoverListener = hoverListener;
        }

        public void clearSelection() {
            this.setSelectedPositionInt(-1);
            this.setNextSelectedPositionInt(-1);
        }

        @Override
        public boolean onKeyDown(int keyCode, @Nonnull KeyEvent event) {
            ListMenuItemView selectedItem = (ListMenuItemView) this.getSelectedView();
            if (selectedItem != null && keyCode == this.mAdvanceKey) {
                if (selectedItem.isEnabled() && selectedItem.getItemData().hasSubMenu()) {
                    this.performItemClick(selectedItem, this.getSelectedItemPosition(), this.getSelectedItemId());
                }
                return true;
            } else if (selectedItem != null && keyCode == this.mRetreatKey) {
                this.setSelectedPositionInt(-1);
                this.setNextSelectedPositionInt(-1);
                ((MenuAdapter) this.getAdapter()).getAdapterMenu().close(false);
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }

        @Override
        public boolean onHoverEvent(@Nonnull MotionEvent ev) {
            if (this.mHoverListener != null) {
                ListAdapter adapter = this.getAdapter();
                int headersCount;
                MenuAdapter menuAdapter;
                if (adapter instanceof HeaderViewListAdapter headerAdapter) {
                    headersCount = headerAdapter.getHeadersCount();
                    menuAdapter = (MenuAdapter) headerAdapter.getWrappedAdapter();
                } else {
                    headersCount = 0;
                    menuAdapter = (MenuAdapter) adapter;
                }
                MenuItem menuItem = null;
                if (ev.getAction() != 10) {
                    int position = this.pointToPosition((int) ev.getX(), (int) ev.getY());
                    if (position != -1) {
                        int itemPosition = position - headersCount;
                        if (itemPosition >= 0 && itemPosition < menuAdapter.getCount()) {
                            menuItem = menuAdapter.getItem(itemPosition);
                        }
                    }
                }
                MenuItem oldMenuItem = this.mHoveredMenuItem;
                if (oldMenuItem != menuItem) {
                    MenuBuilder menu = menuAdapter.getAdapterMenu();
                    if (oldMenuItem != null) {
                        this.mHoverListener.onItemHoverExit(menu, oldMenuItem);
                    }
                    this.mHoveredMenuItem = menuItem;
                    if (menuItem != null) {
                        this.mHoverListener.onItemHoverEnter(menu, menuItem);
                    }
                }
            }
            return super.onHoverEvent(ev);
        }
    }
}