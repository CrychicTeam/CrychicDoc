package icyllis.modernui.widget;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.core.Core;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.util.DataSetObserver;
import icyllis.modernui.view.MeasureSpec;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.view.ViewParent;
import icyllis.modernui.view.menu.ShowableListMenu;

public class ListPopupWindow implements ShowableListMenu {

    private static final int EXPAND_LIST_TIMEOUT = 250;

    public static final int POSITION_PROMPT_ABOVE = 0;

    public static final int POSITION_PROMPT_BELOW = 1;

    private Context mContext;

    private ListAdapter mAdapter;

    private DropDownListView mDropDownList;

    private int mDropDownHeight = -2;

    private int mDropDownWidth = -2;

    private int mDropDownHorizontalOffset;

    private int mDropDownVerticalOffset;

    private boolean mDropDownVerticalOffsetSet;

    private boolean mOverlapAnchor;

    private int mDropDownGravity = 0;

    private boolean mDropDownAlwaysVisible = false;

    private boolean mForceIgnoreOutsideTouch = false;

    int mListItemExpandMaximum = Integer.MAX_VALUE;

    private View mPromptView;

    private int mPromptPosition = 0;

    private DataSetObserver mObserver;

    private View mDropDownAnchorView;

    private Drawable mDropDownListHighlight;

    private AdapterView.OnItemClickListener mItemClickListener;

    private AdapterView.OnItemSelectedListener mItemSelectedListener;

    private final ListPopupWindow.ResizePopupRunnable mResizePopupRunnable = new ListPopupWindow.ResizePopupRunnable();

    private final ListPopupWindow.PopupTouchInterceptor mTouchInterceptor = new ListPopupWindow.PopupTouchInterceptor();

    private final ListPopupWindow.ListSelectorHider mHideSelector = new ListPopupWindow.ListSelectorHider();

    private Runnable mShowDropDownRunnable;

    private final Rect mTempRect = new Rect();

    private Rect mEpicenterBounds;

    private boolean mModal;

    PopupWindow mPopup;

    public ListPopupWindow(@NonNull Context context) {
        this.mContext = context;
        this.mPopup = new PopupWindow();
    }

    public void setAdapter(@Nullable ListAdapter adapter) {
        if (this.mObserver == null) {
            this.mObserver = new ListPopupWindow.PopupDataSetObserver();
        } else if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
        }
        this.mAdapter = adapter;
        if (this.mAdapter != null) {
            adapter.registerDataSetObserver(this.mObserver);
        }
        if (this.mDropDownList != null) {
            this.mDropDownList.setAdapter(this.mAdapter);
        }
    }

    public void setPromptPosition(int position) {
        this.mPromptPosition = position;
    }

    public int getPromptPosition() {
        return this.mPromptPosition;
    }

    public void setModal(boolean modal) {
        this.mModal = modal;
        this.mPopup.setFocusable(modal);
    }

    public boolean isModal() {
        return this.mModal;
    }

    public void setForceIgnoreOutsideTouch(boolean forceIgnoreOutsideTouch) {
        this.mForceIgnoreOutsideTouch = forceIgnoreOutsideTouch;
    }

    public void setDropDownAlwaysVisible(boolean dropDownAlwaysVisible) {
        this.mDropDownAlwaysVisible = dropDownAlwaysVisible;
    }

    public boolean isDropDownAlwaysVisible() {
        return this.mDropDownAlwaysVisible;
    }

    public void setListSelector(Drawable selector) {
        this.mDropDownListHighlight = selector;
    }

    @Nullable
    public Drawable getBackground() {
        return this.mPopup.getBackground();
    }

    public void setBackgroundDrawable(@Nullable Drawable d) {
        this.mPopup.setBackgroundDrawable(d);
    }

    @Nullable
    public View getAnchorView() {
        return this.mDropDownAnchorView;
    }

    public void setAnchorView(@Nullable View anchor) {
        this.mDropDownAnchorView = anchor;
    }

    public int getHorizontalOffset() {
        return this.mDropDownHorizontalOffset;
    }

    public void setHorizontalOffset(int offset) {
        this.mDropDownHorizontalOffset = offset;
    }

    public int getVerticalOffset() {
        return !this.mDropDownVerticalOffsetSet ? 0 : this.mDropDownVerticalOffset;
    }

    public void setVerticalOffset(int offset) {
        this.mDropDownVerticalOffset = offset;
        this.mDropDownVerticalOffsetSet = true;
    }

    public void setEpicenterBounds(@Nullable Rect bounds) {
        this.mEpicenterBounds = bounds != null ? bounds.copy() : null;
    }

    @Nullable
    public Rect getEpicenterBounds() {
        return this.mEpicenterBounds != null ? this.mEpicenterBounds.copy() : null;
    }

    public void setDropDownGravity(int gravity) {
        this.mDropDownGravity = gravity;
    }

    public int getWidth() {
        return this.mDropDownWidth;
    }

    public void setWidth(int width) {
        this.mDropDownWidth = width;
    }

    public void setContentWidth(int width) {
        Drawable popupBackground = this.mPopup.getBackground();
        if (popupBackground != null) {
            popupBackground.getPadding(this.mTempRect);
            this.mDropDownWidth = this.mTempRect.left + this.mTempRect.right + width;
        } else {
            this.setWidth(width);
        }
    }

    public int getHeight() {
        return this.mDropDownHeight;
    }

    public void setHeight(int height) {
        this.mDropDownHeight = height;
    }

    public void setOnItemClickListener(@Nullable AdapterView.OnItemClickListener clickListener) {
        this.mItemClickListener = clickListener;
    }

    public void setOnItemSelectedListener(@Nullable AdapterView.OnItemSelectedListener selectedListener) {
        this.mItemSelectedListener = selectedListener;
    }

    public void setPromptView(@Nullable View prompt) {
        boolean showing = this.isShowing();
        if (showing) {
            this.removePromptView();
        }
        this.mPromptView = prompt;
        if (showing) {
            this.show();
        }
    }

    public void postShow() {
        Core.getUiHandler().post(this.mShowDropDownRunnable);
    }

    @Override
    public void show() {
        this.mPopup.setOverlapAnchor(this.mOverlapAnchor);
        int height = this.buildDropDown();
        if (this.mPopup.isShowing()) {
            if (!this.mDropDownAnchorView.isAttachedToWindow()) {
                return;
            }
            int widthSpec;
            if (this.mDropDownWidth == -1) {
                widthSpec = -1;
            } else if (this.mDropDownWidth == -2) {
                widthSpec = this.mDropDownAnchorView.getWidth();
            } else {
                widthSpec = this.mDropDownWidth;
            }
            int heightSpec;
            if (this.mDropDownHeight == -1) {
                heightSpec = height;
                this.mPopup.setWidth(this.mDropDownWidth == -1 ? -1 : 0);
                this.mPopup.setHeight(0);
            } else if (this.mDropDownHeight == -2) {
                heightSpec = height;
            } else {
                heightSpec = this.mDropDownHeight;
            }
            this.mPopup.setOutsideTouchable(!this.mForceIgnoreOutsideTouch && !this.mDropDownAlwaysVisible);
            this.mPopup.update(this.mDropDownAnchorView, this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset, widthSpec < 0 ? -1 : widthSpec, heightSpec < 0 ? -1 : heightSpec);
            this.mPopup.getContentView().restoreDefaultFocus();
        } else {
            int widthSpecx;
            if (this.mDropDownWidth == -1) {
                widthSpecx = -1;
            } else if (this.mDropDownWidth == -2) {
                widthSpecx = this.mDropDownAnchorView.getWidth();
            } else {
                widthSpecx = this.mDropDownWidth;
            }
            int heightSpec;
            if (this.mDropDownHeight == -1) {
                heightSpec = -1;
            } else if (this.mDropDownHeight == -2) {
                heightSpec = height;
            } else {
                heightSpec = this.mDropDownHeight;
            }
            this.mPopup.setWidth(widthSpecx);
            this.mPopup.setHeight(heightSpec);
            this.mPopup.setIsClippedToScreen(true);
            this.mPopup.setOutsideTouchable(!this.mForceIgnoreOutsideTouch && !this.mDropDownAlwaysVisible);
            this.mPopup.setTouchInterceptor(this.mTouchInterceptor);
            this.mPopup.setEpicenterBounds(this.mEpicenterBounds);
            this.mPopup.showAsDropDown(this.mDropDownAnchorView, this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset, this.mDropDownGravity);
            this.mDropDownList.setSelection(-1);
            this.mPopup.getContentView().restoreDefaultFocus();
            if (!this.mModal || this.mDropDownList.isInTouchMode()) {
                this.clearListSelection();
            }
            if (!this.mModal) {
                Core.getUiHandler().post(this.mHideSelector);
            }
        }
    }

    @Override
    public void dismiss() {
        this.mPopup.dismiss();
        this.removePromptView();
        this.mPopup.setContentView(null);
        this.mDropDownList = null;
        Core.getUiHandler().removeCallbacks(this.mResizePopupRunnable);
    }

    public void dismissImmediate() {
        this.mPopup.setExitTransition(null);
        this.dismiss();
    }

    public void setOnDismissListener(@Nullable PopupWindow.OnDismissListener listener) {
        this.mPopup.setOnDismissListener(listener);
    }

    private void removePromptView() {
        if (this.mPromptView != null) {
            ViewParent parent = this.mPromptView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this.mPromptView);
            }
        }
    }

    public void setSelection(int position) {
        DropDownListView list = this.mDropDownList;
        if (this.isShowing() && list != null) {
            list.setListSelectionHidden(false);
            list.setSelection(position);
            if (list.getChoiceMode() != 0) {
                list.setItemChecked(position, true);
            }
        }
    }

    public void clearListSelection() {
        DropDownListView list = this.mDropDownList;
        if (list != null) {
            list.setListSelectionHidden(true);
            list.hideSelector();
            list.requestLayout();
        }
    }

    @Override
    public boolean isShowing() {
        return this.mPopup.isShowing();
    }

    public boolean performItemClick(int position) {
        if (this.isShowing()) {
            if (this.mItemClickListener != null) {
                DropDownListView list = this.mDropDownList;
                View child = list.getChildAt(position - list.getFirstVisiblePosition());
                ListAdapter adapter = list.getAdapter();
                this.mItemClickListener.onItemClick(list, child, position, adapter.getItemId(position));
            }
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    public Object getSelectedItem() {
        return !this.isShowing() ? null : this.mDropDownList.getSelectedItem();
    }

    public int getSelectedItemPosition() {
        return !this.isShowing() ? -1 : this.mDropDownList.getSelectedItemPosition();
    }

    public long getSelectedItemId() {
        return !this.isShowing() ? Long.MIN_VALUE : this.mDropDownList.getSelectedItemId();
    }

    @Nullable
    public View getSelectedView() {
        return !this.isShowing() ? null : this.mDropDownList.getSelectedView();
    }

    @Nullable
    @Override
    public ListView getListView() {
        return this.mDropDownList;
    }

    @NonNull
    DropDownListView createDropDownListView(Context context, boolean hijackFocus) {
        return new DropDownListView(context, hijackFocus);
    }

    private int buildDropDown() {
        int otherHeights = 0;
        if (this.mDropDownList == null) {
            this.mShowDropDownRunnable = () -> {
                View view = this.getAnchorView();
                if (view != null && view.isAttachedToWindow()) {
                    this.show();
                }
            };
            this.mDropDownList = this.createDropDownListView(this.mContext, !this.mModal);
            if (this.mDropDownListHighlight != null) {
                this.mDropDownList.setSelector(this.mDropDownListHighlight);
            }
            this.mDropDownList.setAdapter(this.mAdapter);
            this.mDropDownList.setOnItemClickListener(this.mItemClickListener);
            this.mDropDownList.setFocusable(true);
            this.mDropDownList.setFocusableInTouchMode(true);
            this.mDropDownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != -1) {
                        DropDownListView dropDownList = ListPopupWindow.this.mDropDownList;
                        if (dropDownList != null) {
                            dropDownList.setListSelectionHidden(false);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            if (this.mItemSelectedListener != null) {
                this.mDropDownList.setOnItemSelectedListener(this.mItemSelectedListener);
            }
            ViewGroup dropDownView = this.mDropDownList;
            View hintView = this.mPromptView;
            if (hintView != null) {
                LinearLayout hintContainer = new LinearLayout(this.mContext);
                hintContainer.setOrientation(1);
                LinearLayout.LayoutParams hintParams = new LinearLayout.LayoutParams(-1, 0, 1.0F);
                switch(this.mPromptPosition) {
                    case 0:
                        hintContainer.addView(hintView);
                        hintContainer.addView(dropDownView, hintParams);
                        break;
                    case 1:
                        hintContainer.addView(dropDownView, hintParams);
                        hintContainer.addView(hintView);
                        break;
                    default:
                        throw new IllegalStateException();
                }
                int widthSize;
                int widthMode;
                if (this.mDropDownWidth >= 0) {
                    widthMode = Integer.MIN_VALUE;
                    widthSize = this.mDropDownWidth;
                } else {
                    widthMode = 0;
                    widthSize = 0;
                }
                int widthSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
                int heightSpec = 0;
                hintView.measure(widthSpec, 0);
                hintParams = (LinearLayout.LayoutParams) hintView.getLayoutParams();
                otherHeights = hintView.getMeasuredHeight() + hintParams.topMargin + hintParams.bottomMargin;
                dropDownView = hintContainer;
            }
            this.mPopup.setContentView(dropDownView);
        } else {
            View view = this.mPromptView;
            if (view != null) {
                LinearLayout.LayoutParams hintParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                otherHeights = view.getMeasuredHeight() + hintParams.topMargin + hintParams.bottomMargin;
            }
        }
        Drawable background = this.mPopup.getBackground();
        int padding;
        if (background != null) {
            background.getPadding(this.mTempRect);
            padding = this.mTempRect.top + this.mTempRect.bottom;
            if (!this.mDropDownVerticalOffsetSet) {
                this.mDropDownVerticalOffset = -this.mTempRect.top;
            }
        } else {
            this.mTempRect.setEmpty();
            padding = 0;
        }
        int maxHeight = this.mPopup.getMaxAvailableHeight(this.mDropDownAnchorView, this.mDropDownVerticalOffset);
        if (!this.mDropDownAlwaysVisible && this.mDropDownHeight != -1) {
            int childWidthSpec = switch(this.mDropDownWidth) {
                case -2 ->
                    MeasureSpec.makeMeasureSpec(this.mDropDownAnchorView.getRootView().getMeasuredWidth() - (this.mTempRect.left + this.mTempRect.right), Integer.MIN_VALUE);
                case -1 ->
                    MeasureSpec.makeMeasureSpec(this.mDropDownAnchorView.getRootView().getMeasuredWidth() - (this.mTempRect.left + this.mTempRect.right), 1073741824);
                default ->
                    MeasureSpec.makeMeasureSpec(this.mDropDownWidth, 1073741824);
            };
            int listContent = this.mDropDownList.measureHeightOfChildren(childWidthSpec, 0, -1, maxHeight - otherHeights, -1);
            if (listContent > 0) {
                int listPadding = this.mDropDownList.getPaddingTop() + this.mDropDownList.getPaddingBottom();
                otherHeights += padding + listPadding;
            }
            return listContent + otherHeights;
        } else {
            return maxHeight + padding;
        }
    }

    public void setOverlapAnchor(boolean overlap) {
        this.mOverlapAnchor = overlap;
    }

    private class ListSelectorHider implements Runnable {

        public void run() {
            ListPopupWindow.this.clearListSelection();
        }
    }

    private class PopupDataSetObserver implements DataSetObserver {

        @Override
        public void onChanged() {
            if (ListPopupWindow.this.isShowing()) {
                ListPopupWindow.this.show();
            }
        }

        @Override
        public void onInvalidated() {
            ListPopupWindow.this.dismiss();
        }
    }

    private class PopupTouchInterceptor implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (action == 0 && ListPopupWindow.this.mPopup != null && ListPopupWindow.this.mPopup.isShowing() && x >= 0 && x < ListPopupWindow.this.mPopup.getWidth() && y >= 0 && y < ListPopupWindow.this.mPopup.getHeight()) {
                Core.getUiHandler().postDelayed(ListPopupWindow.this.mResizePopupRunnable, 250L);
            } else if (action == 1) {
                Core.getUiHandler().removeCallbacks(ListPopupWindow.this.mResizePopupRunnable);
            }
            return false;
        }
    }

    private class ResizePopupRunnable implements Runnable {

        public void run() {
            if (ListPopupWindow.this.mDropDownList != null && ListPopupWindow.this.mDropDownList.isAttachedToWindow() && ListPopupWindow.this.mDropDownList.getCount() > ListPopupWindow.this.mDropDownList.getChildCount() && ListPopupWindow.this.mDropDownList.getChildCount() <= ListPopupWindow.this.mListItemExpandMaximum) {
                ListPopupWindow.this.show();
            }
        }
    }
}