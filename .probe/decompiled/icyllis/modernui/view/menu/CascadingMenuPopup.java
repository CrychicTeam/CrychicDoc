package icyllis.modernui.view.menu;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.core.Core;
import icyllis.modernui.core.Handler;
import icyllis.modernui.core.Looper;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.resources.Resources;
import icyllis.modernui.transition.AutoTransition;
import icyllis.modernui.util.TypedValue;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.MenuItem;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.view.ViewTreeObserver;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.HeaderViewListAdapter;
import icyllis.modernui.widget.ListAdapter;
import icyllis.modernui.widget.ListView;
import icyllis.modernui.widget.MenuItemHoverListener;
import icyllis.modernui.widget.MenuPopupWindow;
import icyllis.modernui.widget.PopupWindow;
import icyllis.modernui.widget.TextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class CascadingMenuPopup extends MenuPopup implements MenuPresenter, View.OnKeyListener, PopupWindow.OnDismissListener {

    private static final int HORIZ_POSITION_LEFT = 0;

    private static final int HORIZ_POSITION_RIGHT = 1;

    private static final int SUBMENU_TIMEOUT_MS = 200;

    private final Context mContext;

    private final int mMenuMaxWidth;

    private final boolean mOverflowOnly;

    private final Handler mSubMenuHoverHandler;

    private final List<MenuBuilder> mPendingMenus = new LinkedList();

    private final List<CascadingMenuPopup.CascadingMenuInfo> mShowingMenus = new ArrayList();

    private final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            if (CascadingMenuPopup.this.isShowing() && CascadingMenuPopup.this.mShowingMenus.size() > 0 && !((CascadingMenuPopup.CascadingMenuInfo) CascadingMenuPopup.this.mShowingMenus.get(0)).window.isModal()) {
                View anchor = CascadingMenuPopup.this.mShownAnchorView;
                if (anchor != null && anchor.isShown()) {
                    for (CascadingMenuPopup.CascadingMenuInfo info : CascadingMenuPopup.this.mShowingMenus) {
                        info.window.show();
                    }
                } else {
                    CascadingMenuPopup.this.dismiss();
                }
            }
        }
    };

    private final View.OnAttachStateChangeListener mAttachStateChangeListener = new View.OnAttachStateChangeListener() {

        @Override
        public void onViewAttachedToWindow(View v) {
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            if (CascadingMenuPopup.this.mTreeObserver != null) {
                if (!CascadingMenuPopup.this.mTreeObserver.isAlive()) {
                    CascadingMenuPopup.this.mTreeObserver = v.getViewTreeObserver();
                }
                CascadingMenuPopup.this.mTreeObserver.removeOnGlobalLayoutListener(CascadingMenuPopup.this.mGlobalLayoutListener);
            }
            v.removeOnAttachStateChangeListener(this);
        }
    };

    private final MenuItemHoverListener mMenuItemHoverListener = new MenuItemHoverListener() {

        @Override
        public void onItemHoverExit(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
            CascadingMenuPopup.this.mSubMenuHoverHandler.removeCallbacksAndMessages(menu);
        }

        @Override
        public void onItemHoverEnter(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
            CascadingMenuPopup.this.mSubMenuHoverHandler.removeCallbacksAndMessages(null);
            int menuIndex = -1;
            int i = 0;
            for (int count = CascadingMenuPopup.this.mShowingMenus.size(); i < count; i++) {
                if (menu == ((CascadingMenuPopup.CascadingMenuInfo) CascadingMenuPopup.this.mShowingMenus.get(i)).menu) {
                    menuIndex = i;
                    break;
                }
            }
            if (menuIndex != -1) {
                int nextIndex = menuIndex + 1;
                final CascadingMenuPopup.CascadingMenuInfo nextInfo;
                if (nextIndex < CascadingMenuPopup.this.mShowingMenus.size()) {
                    nextInfo = (CascadingMenuPopup.CascadingMenuInfo) CascadingMenuPopup.this.mShowingMenus.get(nextIndex);
                } else {
                    nextInfo = null;
                }
                Runnable runnable = new Runnable() {

                    public void run() {
                        if (nextInfo != null) {
                            CascadingMenuPopup.this.mShouldCloseImmediately = true;
                            nextInfo.menu.close(false);
                            CascadingMenuPopup.this.mShouldCloseImmediately = false;
                        }
                        if (item.isEnabled() && item.hasSubMenu()) {
                            menu.performItemAction(item, 0);
                        }
                    }
                };
                long timeMillis = Core.timeMillis() + 200L;
                CascadingMenuPopup.this.mSubMenuHoverHandler.postAtTime(runnable, menu, timeMillis);
            }
        }
    };

    private int mRawDropDownGravity = 0;

    private int mDropDownGravity = 0;

    private View mAnchorView;

    private View mShownAnchorView;

    private int mLastPosition;

    private boolean mHasXOffset;

    private boolean mHasYOffset;

    private int mXOffset;

    private int mYOffset;

    private boolean mForceShowIcon;

    private boolean mShowTitle;

    private MenuPresenter.Callback mPresenterCallback;

    private ViewTreeObserver mTreeObserver;

    private PopupWindow.OnDismissListener mOnDismissListener;

    private boolean mShouldCloseImmediately;

    public CascadingMenuPopup(@NonNull Context context, @NonNull View anchorView, boolean overflowOnly) {
        this.mContext = (Context) Objects.requireNonNull(context);
        this.mAnchorView = (View) Objects.requireNonNull(anchorView);
        this.mOverflowOnly = overflowOnly;
        this.mForceShowIcon = false;
        this.mLastPosition = this.getInitialMenuPosition();
        Resources res = context.getResources();
        this.mMenuMaxWidth = Math.max(res.getDisplayMetrics().widthPixels / 2, (int) TypedValue.applyDimension(1, 320.0F, res.getDisplayMetrics()));
        this.mSubMenuHoverHandler = new Handler(Looper.myLooper());
    }

    @Override
    public void setForceShowIcon(boolean forceShow) {
        this.mForceShowIcon = forceShow;
    }

    private MenuPopupWindow createPopupWindow() {
        MenuPopupWindow popupWindow = new MenuPopupWindow(this.mContext);
        popupWindow.setBackgroundDrawable(new Drawable() {

            private final float mRadius = TypedValue.applyDimension(1, 8.0F, CascadingMenuPopup.this.mContext.getResources().getDisplayMetrics());

            @Override
            public void draw(@NonNull Canvas canvas) {
                Paint paint = Paint.obtain();
                paint.setColor(-332386256);
                Rect b = this.getBounds();
                canvas.drawRoundRect((float) b.left, (float) b.top, (float) b.right, (float) b.bottom, this.mRadius, paint);
                paint.recycle();
            }

            @Override
            public boolean getPadding(@NonNull Rect padding) {
                int r = (int) Math.ceil((double) (this.mRadius / 2.0F));
                padding.set(r, r, r, r);
                return true;
            }
        });
        popupWindow.setEnterTransition(new AutoTransition());
        popupWindow.setExitTransition(new AutoTransition());
        popupWindow.setOverlapAnchor(true);
        popupWindow.setHoverListener(this.mMenuItemHoverListener);
        popupWindow.setOnItemClickListener(this);
        popupWindow.setOnDismissListener(this);
        popupWindow.setAnchorView(this.mAnchorView);
        popupWindow.setDropDownGravity(this.mDropDownGravity);
        popupWindow.setModal(true);
        return popupWindow;
    }

    @Override
    public void show() {
        if (!this.isShowing()) {
            for (MenuBuilder menu : this.mPendingMenus) {
                this.showMenu(menu);
            }
            this.mPendingMenus.clear();
            this.mShownAnchorView = this.mAnchorView;
            if (this.mShownAnchorView != null) {
                boolean addGlobalListener = this.mTreeObserver == null;
                this.mTreeObserver = this.mShownAnchorView.getViewTreeObserver();
                if (addGlobalListener) {
                    this.mTreeObserver.addOnGlobalLayoutListener(this.mGlobalLayoutListener);
                }
                this.mShownAnchorView.addOnAttachStateChangeListener(this.mAttachStateChangeListener);
            }
        }
    }

    @Override
    public void dismiss() {
        int length = this.mShowingMenus.size();
        if (length > 0) {
            CascadingMenuPopup.CascadingMenuInfo[] addedMenus = (CascadingMenuPopup.CascadingMenuInfo[]) this.mShowingMenus.toArray(new CascadingMenuPopup.CascadingMenuInfo[length]);
            for (int i = length - 1; i >= 0; i--) {
                CascadingMenuPopup.CascadingMenuInfo info = addedMenus[i];
                if (info.window.isShowing()) {
                    info.window.dismiss();
                }
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    private int getInitialMenuPosition() {
        int layoutDirection = this.mAnchorView.getLayoutDirection();
        return layoutDirection == 1 ? 0 : 1;
    }

    private int getNextMenuPosition(int nextMenuWidth) {
        ListView lastListView = ((CascadingMenuPopup.CascadingMenuInfo) this.mShowingMenus.get(this.mShowingMenus.size() - 1)).getListView();
        int[] screenLocation = new int[2];
        lastListView.getLocationOnScreen(screenLocation);
        if (this.mLastPosition == 1) {
            int right = screenLocation[0] + lastListView.getWidth() + nextMenuWidth;
            return right > this.mShownAnchorView.getRootView().getWidth() ? 0 : 1;
        } else {
            int left = screenLocation[0] - nextMenuWidth;
            return left < 0 ? 1 : 0;
        }
    }

    @Override
    public void addMenu(MenuBuilder menu) {
        menu.addMenuPresenter(this, this.mContext);
        if (this.isShowing()) {
            this.showMenu(menu);
        } else {
            this.mPendingMenus.add(menu);
        }
    }

    private void showMenu(@NonNull MenuBuilder menu) {
        MenuAdapter adapter = new MenuAdapter(this.mContext, menu, this.mOverflowOnly);
        if (!this.isShowing() && this.mForceShowIcon) {
            adapter.setForceShowIcon(true);
        } else if (this.isShowing()) {
            adapter.setForceShowIcon(MenuPopup.shouldPreserveIconSpacing(menu));
        }
        int menuWidth = measureIndividualMenuWidth(adapter, null, this.mContext, this.mMenuMaxWidth);
        MenuPopupWindow popupWindow = this.createPopupWindow();
        popupWindow.setAdapter(adapter);
        popupWindow.setContentWidth(menuWidth);
        popupWindow.setDropDownGravity(this.mDropDownGravity);
        CascadingMenuPopup.CascadingMenuInfo parentInfo;
        View parentView;
        if (this.mShowingMenus.size() > 0) {
            parentInfo = (CascadingMenuPopup.CascadingMenuInfo) this.mShowingMenus.get(this.mShowingMenus.size() - 1);
            parentView = this.findParentViewForSubmenu(parentInfo, menu);
        } else {
            parentInfo = null;
            parentView = null;
        }
        if (parentView != null) {
            popupWindow.setAnchorView(parentView);
            popupWindow.setTouchModal(false);
            popupWindow.setEnterTransition(null);
            int nextMenuPosition = this.getNextMenuPosition(menuWidth);
            boolean showOnRight = nextMenuPosition == 1;
            this.mLastPosition = nextMenuPosition;
            int x;
            if ((this.mDropDownGravity & 5) == 5) {
                if (showOnRight) {
                    x = menuWidth;
                } else {
                    x = -parentView.getWidth();
                }
            } else if (showOnRight) {
                x = parentView.getWidth();
            } else {
                x = -menuWidth;
            }
            popupWindow.setHorizontalOffset(x);
            popupWindow.setOverlapAnchor(true);
            popupWindow.setVerticalOffset(0);
        } else {
            if (this.mHasXOffset) {
                popupWindow.setHorizontalOffset(this.mXOffset);
            }
            if (this.mHasYOffset) {
                popupWindow.setVerticalOffset(this.mYOffset);
            }
            Rect epicenterBounds = this.getEpicenterBounds();
            popupWindow.setEpicenterBounds(epicenterBounds);
        }
        CascadingMenuPopup.CascadingMenuInfo menuInfo = new CascadingMenuPopup.CascadingMenuInfo(popupWindow, menu, this.mLastPosition);
        this.mShowingMenus.add(menuInfo);
        popupWindow.show();
        ListView listView = popupWindow.getListView();
        listView.setOnKeyListener(this);
        if (parentInfo == null && this.mShowTitle && menu.getHeaderTitle() != null) {
            FrameLayout titleItemView = new FrameLayout(this.mContext);
            titleItemView.setMinimumWidth(titleItemView.dp(196.0F));
            titleItemView.setPadding(titleItemView.dp(16.0F), 0, titleItemView.dp(16.0F), 0);
            titleItemView.setLayoutParams(new ViewGroup.LayoutParams(-1, titleItemView.dp(48.0F)));
            TextView titleView = new TextView(this.mContext);
            titleView.setText(menu.getHeaderTitle());
            titleView.setGravity(16);
            titleView.setSingleLine();
            titleView.setTextAlignment(5);
            titleItemView.addView(titleView, -1, -2);
            titleItemView.setEnabled(false);
            listView.addHeaderView(titleItemView, null, false);
            popupWindow.show();
        }
    }

    private MenuItem findMenuItemForSubmenu(@NonNull MenuBuilder parent, @NonNull MenuBuilder submenu) {
        int i = 0;
        for (int count = parent.size(); i < count; i++) {
            MenuItem item = parent.getItem(i);
            if (item.hasSubMenu() && submenu == item.getSubMenu()) {
                return item;
            }
        }
        return null;
    }

    @Nullable
    private View findParentViewForSubmenu(@NonNull CascadingMenuPopup.CascadingMenuInfo parentInfo, @NonNull MenuBuilder submenu) {
        MenuItem owner = this.findMenuItemForSubmenu(parentInfo.menu, submenu);
        if (owner == null) {
            return null;
        } else {
            ListView listView = parentInfo.getListView();
            ListAdapter listAdapter = listView.getAdapter();
            int headersCount;
            MenuAdapter menuAdapter;
            if (listAdapter instanceof HeaderViewListAdapter headerAdapter) {
                headersCount = headerAdapter.getHeadersCount();
                menuAdapter = (MenuAdapter) headerAdapter.getWrappedAdapter();
            } else {
                headersCount = 0;
                menuAdapter = (MenuAdapter) listAdapter;
            }
            int ownerPosition = -1;
            int i = 0;
            for (int count = menuAdapter.getCount(); i < count; i++) {
                if (owner == menuAdapter.getItem(i)) {
                    ownerPosition = i;
                    break;
                }
            }
            if (ownerPosition == -1) {
                return null;
            } else {
                ownerPosition += headersCount;
                i = ownerPosition - listView.getFirstVisiblePosition();
                return i >= 0 && i < listView.getChildCount() ? listView.getChildAt(i) : null;
            }
        }
    }

    @Override
    public boolean isShowing() {
        return this.mShowingMenus.size() > 0 && ((CascadingMenuPopup.CascadingMenuInfo) this.mShowingMenus.get(0)).window.isShowing();
    }

    @Override
    public void onDismiss() {
        CascadingMenuPopup.CascadingMenuInfo dismissedInfo = null;
        int i = 0;
        for (int count = this.mShowingMenus.size(); i < count; i++) {
            CascadingMenuPopup.CascadingMenuInfo info = (CascadingMenuPopup.CascadingMenuInfo) this.mShowingMenus.get(i);
            if (!info.window.isShowing()) {
                dismissedInfo = info;
                break;
            }
        }
        if (dismissedInfo != null) {
            dismissedInfo.menu.close(false);
        }
    }

    @Override
    public void updateMenuView(boolean cleared) {
        for (CascadingMenuPopup.CascadingMenuInfo info : this.mShowingMenus) {
            toMenuAdapter(info.getListView().getAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public void setCallback(MenuPresenter.Callback cb) {
        this.mPresenterCallback = cb;
    }

    @Override
    public boolean onSubMenuSelected(SubMenuBuilder subMenu) {
        for (CascadingMenuPopup.CascadingMenuInfo info : this.mShowingMenus) {
            if (subMenu == info.menu) {
                info.getListView().requestFocus();
                return true;
            }
        }
        if (subMenu.hasVisibleItems()) {
            this.addMenu(subMenu);
            if (this.mPresenterCallback != null) {
                this.mPresenterCallback.onOpenSubMenu(subMenu);
            }
            return true;
        } else {
            return false;
        }
    }

    private int findIndexOfAddedMenu(@NonNull MenuBuilder menu) {
        int i = 0;
        for (int count = this.mShowingMenus.size(); i < count; i++) {
            CascadingMenuPopup.CascadingMenuInfo info = (CascadingMenuPopup.CascadingMenuInfo) this.mShowingMenus.get(i);
            if (menu == info.menu) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        int menuIndex = this.findIndexOfAddedMenu(menu);
        if (menuIndex >= 0) {
            int nextMenuIndex = menuIndex + 1;
            if (nextMenuIndex < this.mShowingMenus.size()) {
                CascadingMenuPopup.CascadingMenuInfo childInfo = (CascadingMenuPopup.CascadingMenuInfo) this.mShowingMenus.get(nextMenuIndex);
                childInfo.menu.close(false);
            }
            CascadingMenuPopup.CascadingMenuInfo info = (CascadingMenuPopup.CascadingMenuInfo) this.mShowingMenus.remove(menuIndex);
            info.menu.removeMenuPresenter(this);
            if (this.mShouldCloseImmediately) {
                info.window.setExitTransition(null);
            }
            info.window.dismiss();
            int count = this.mShowingMenus.size();
            if (count > 0) {
                this.mLastPosition = ((CascadingMenuPopup.CascadingMenuInfo) this.mShowingMenus.get(count - 1)).position;
            } else {
                this.mLastPosition = this.getInitialMenuPosition();
            }
            if (count == 0) {
                this.dismiss();
                if (this.mPresenterCallback != null) {
                    this.mPresenterCallback.onCloseMenu(menu, true);
                }
                if (this.mTreeObserver != null) {
                    if (this.mTreeObserver.isAlive()) {
                        this.mTreeObserver.removeOnGlobalLayoutListener(this.mGlobalLayoutListener);
                    }
                    this.mTreeObserver = null;
                }
                this.mShownAnchorView.removeOnAttachStateChangeListener(this.mAttachStateChangeListener);
                this.mOnDismissListener.onDismiss();
            } else if (allMenusAreClosing) {
                CascadingMenuPopup.CascadingMenuInfo rootInfo = (CascadingMenuPopup.CascadingMenuInfo) this.mShowingMenus.get(0);
                rootInfo.menu.close(false);
            }
        }
    }

    @Override
    public boolean flagActionItems() {
        return false;
    }

    @Override
    public void setGravity(int dropDownGravity) {
        if (this.mRawDropDownGravity != dropDownGravity) {
            this.mRawDropDownGravity = dropDownGravity;
            this.mDropDownGravity = Gravity.getAbsoluteGravity(dropDownGravity, this.mAnchorView.getLayoutDirection());
        }
    }

    @Override
    public void setAnchorView(@NonNull View anchor) {
        if (this.mAnchorView != anchor) {
            this.mAnchorView = anchor;
            this.mDropDownGravity = Gravity.getAbsoluteGravity(this.mRawDropDownGravity, this.mAnchorView.getLayoutDirection());
        }
    }

    @Override
    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        this.mOnDismissListener = listener;
    }

    @Override
    public ListView getListView() {
        return this.mShowingMenus.isEmpty() ? null : ((CascadingMenuPopup.CascadingMenuInfo) this.mShowingMenus.get(this.mShowingMenus.size() - 1)).getListView();
    }

    @Override
    public void setHorizontalOffset(int x) {
        this.mHasXOffset = true;
        this.mXOffset = x;
    }

    @Override
    public void setVerticalOffset(int y) {
        this.mHasYOffset = true;
        this.mYOffset = y;
    }

    @Override
    public void setShowTitle(boolean showTitle) {
        this.mShowTitle = showTitle;
    }

    private static class CascadingMenuInfo {

        public final MenuPopupWindow window;

        public final MenuBuilder menu;

        public final int position;

        public CascadingMenuInfo(@NonNull MenuPopupWindow window, @NonNull MenuBuilder menu, int position) {
            this.window = window;
            this.menu = menu;
            this.position = position;
        }

        public ListView getListView() {
            return this.window.getListView();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface HorizPosition {
    }
}