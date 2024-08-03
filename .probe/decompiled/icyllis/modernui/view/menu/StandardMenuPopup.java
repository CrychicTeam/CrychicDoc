package icyllis.modernui.view.menu;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.transition.AutoTransition;
import icyllis.modernui.util.TypedValue;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.view.ViewTreeObserver;
import icyllis.modernui.widget.AdapterView;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.ListView;
import icyllis.modernui.widget.MenuPopupWindow;
import icyllis.modernui.widget.PopupWindow;
import icyllis.modernui.widget.TextView;

public final class StandardMenuPopup extends MenuPopup implements PopupWindow.OnDismissListener, AdapterView.OnItemClickListener, MenuPresenter, View.OnKeyListener {

    private final Context mContext;

    private final MenuBuilder mMenu;

    private final MenuAdapter mAdapter;

    private final boolean mOverflowOnly;

    private final int mPopupMaxWidth;

    private final MenuPopupWindow mPopup;

    private final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            if (StandardMenuPopup.this.isShowing() && !StandardMenuPopup.this.mPopup.isModal()) {
                View anchor = StandardMenuPopup.this.mShownAnchorView;
                if (anchor != null && anchor.isShown()) {
                    StandardMenuPopup.this.mPopup.show();
                } else {
                    StandardMenuPopup.this.dismiss();
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
            if (StandardMenuPopup.this.mTreeObserver != null) {
                if (!StandardMenuPopup.this.mTreeObserver.isAlive()) {
                    StandardMenuPopup.this.mTreeObserver = v.getViewTreeObserver();
                }
                StandardMenuPopup.this.mTreeObserver.removeOnGlobalLayoutListener(StandardMenuPopup.this.mGlobalLayoutListener);
            }
            v.removeOnAttachStateChangeListener(this);
        }
    };

    private PopupWindow.OnDismissListener mOnDismissListener;

    private View mAnchorView;

    private View mShownAnchorView;

    private MenuPresenter.Callback mPresenterCallback;

    private ViewTreeObserver mTreeObserver;

    private boolean mWasDismissed;

    private boolean mHasContentWidth;

    private int mContentWidth;

    private int mDropDownGravity = 0;

    private boolean mShowTitle;

    public StandardMenuPopup(Context context, @NonNull MenuBuilder menu, @NonNull View anchorView, boolean overflowOnly) {
        this.mContext = context;
        this.mMenu = menu;
        this.mOverflowOnly = overflowOnly;
        this.mAdapter = new MenuAdapter(context, menu, this.mOverflowOnly);
        this.mPopupMaxWidth = anchorView.getRootView().getMeasuredWidth() / 2;
        this.mAnchorView = anchorView;
        this.mPopup = new MenuPopupWindow(context);
        this.mPopup.setBackgroundDrawable(new Drawable() {

            private final float mRadius = TypedValue.applyDimension(1, 8.0F, context.getResources().getDisplayMetrics());

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
        this.mPopup.setEnterTransition(new AutoTransition());
        this.mPopup.setExitTransition(new AutoTransition());
        this.mPopup.setOverlapAnchor(true);
        menu.addMenuPresenter(this);
    }

    @Override
    public void setForceShowIcon(boolean forceShow) {
        this.mAdapter.setForceShowIcon(forceShow);
    }

    @Override
    public void setGravity(int gravity) {
        this.mDropDownGravity = gravity;
    }

    private boolean tryShow() {
        if (this.isShowing()) {
            return true;
        } else if (!this.mWasDismissed && this.mAnchorView != null) {
            this.mShownAnchorView = this.mAnchorView;
            this.mPopup.setOnDismissListener(this);
            this.mPopup.setOnItemClickListener(this);
            this.mPopup.setAdapter(this.mAdapter);
            this.mPopup.setModal(true);
            View anchor = this.mShownAnchorView;
            boolean addGlobalListener = this.mTreeObserver == null;
            this.mTreeObserver = anchor.getViewTreeObserver();
            if (addGlobalListener) {
                this.mTreeObserver.addOnGlobalLayoutListener(this.mGlobalLayoutListener);
            }
            anchor.addOnAttachStateChangeListener(this.mAttachStateChangeListener);
            this.mPopup.setAnchorView(anchor);
            this.mPopup.setDropDownGravity(this.mDropDownGravity);
            if (!this.mHasContentWidth) {
                this.mContentWidth = measureIndividualMenuWidth(this.mAdapter, null, this.mContext, this.mPopupMaxWidth);
                this.mHasContentWidth = true;
            }
            this.mPopup.setContentWidth(this.mContentWidth);
            this.mPopup.setEpicenterBounds(this.getEpicenterBounds());
            this.mPopup.show();
            ListView listView = this.mPopup.getListView();
            assert listView != null;
            listView.setOnKeyListener(this);
            if (this.mShowTitle && this.mMenu.getHeaderTitle() != null) {
                FrameLayout titleItemView = new FrameLayout(this.mContext);
                titleItemView.setMinimumWidth(titleItemView.dp(196.0F));
                titleItemView.setPadding(titleItemView.dp(16.0F), 0, titleItemView.dp(16.0F), 0);
                titleItemView.setLayoutParams(new ViewGroup.LayoutParams(-1, titleItemView.dp(48.0F)));
                TextView titleView = new TextView(this.mContext);
                titleView.setText(this.mMenu.getHeaderTitle());
                titleView.setGravity(16);
                titleView.setSingleLine();
                titleView.setTextAlignment(5);
                titleItemView.addView(titleView, -1, -2);
                titleItemView.setEnabled(false);
                listView.addHeaderView(titleItemView, null, false);
                this.mPopup.show();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void show() {
        if (!this.tryShow()) {
            throw new IllegalStateException("StandardMenuPopup cannot be used without an anchor");
        }
    }

    @Override
    public void dismiss() {
        if (this.isShowing()) {
            this.mPopup.dismiss();
        }
    }

    @Override
    public void addMenu(MenuBuilder menu) {
    }

    @Override
    public boolean isShowing() {
        return !this.mWasDismissed && this.mPopup.isShowing();
    }

    @Override
    public void onDismiss() {
        this.mWasDismissed = true;
        this.mMenu.close();
        if (this.mTreeObserver != null) {
            if (!this.mTreeObserver.isAlive()) {
                this.mTreeObserver = this.mShownAnchorView.getViewTreeObserver();
            }
            this.mTreeObserver.removeOnGlobalLayoutListener(this.mGlobalLayoutListener);
            this.mTreeObserver = null;
        }
        this.mShownAnchorView.removeOnAttachStateChangeListener(this.mAttachStateChangeListener);
        if (this.mOnDismissListener != null) {
            this.mOnDismissListener.onDismiss();
        }
    }

    @Override
    public void updateMenuView(boolean cleared) {
        this.mHasContentWidth = false;
        if (this.mAdapter != null) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setCallback(MenuPresenter.Callback cb) {
        this.mPresenterCallback = cb;
    }

    @Override
    public boolean onSubMenuSelected(@NonNull SubMenuBuilder subMenu) {
        if (subMenu.hasVisibleItems()) {
            MenuPopupHelper subPopup = new MenuPopupHelper(this.mContext, subMenu, this.mShownAnchorView, this.mOverflowOnly);
            subPopup.setPresenterCallback(this.mPresenterCallback);
            subPopup.setForceShowIcon(MenuPopup.shouldPreserveIconSpacing(subMenu));
            subPopup.setOnDismissListener(this.mOnDismissListener);
            this.mOnDismissListener = null;
            this.mMenu.close(false);
            int horizontalOffset = this.mPopup.getHorizontalOffset();
            int verticalOffset = this.mPopup.getVerticalOffset();
            int hgrav = Gravity.getAbsoluteGravity(this.mDropDownGravity, this.mAnchorView.getLayoutDirection()) & 7;
            if (hgrav == 5) {
                horizontalOffset += this.mAnchorView.getWidth();
            }
            if (subPopup.tryShow(horizontalOffset, verticalOffset)) {
                if (this.mPresenterCallback != null) {
                    this.mPresenterCallback.onOpenSubMenu(subMenu);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        if (menu == this.mMenu) {
            this.dismiss();
            if (this.mPresenterCallback != null) {
                this.mPresenterCallback.onCloseMenu(menu, allMenusAreClosing);
            }
        }
    }

    @Override
    public boolean flagActionItems() {
        return false;
    }

    @Override
    public void setAnchorView(View anchor) {
        this.mAnchorView = anchor;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        this.mOnDismissListener = listener;
    }

    @Override
    public ListView getListView() {
        return this.mPopup.getListView();
    }

    @Override
    public void setHorizontalOffset(int x) {
        this.mPopup.setHorizontalOffset(x);
    }

    @Override
    public void setVerticalOffset(int y) {
        this.mPopup.setVerticalOffset(y);
    }

    @Override
    public void setShowTitle(boolean showTitle) {
        this.mShowTitle = showTitle;
    }
}