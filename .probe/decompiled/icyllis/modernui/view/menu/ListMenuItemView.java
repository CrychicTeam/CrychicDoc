package icyllis.modernui.view.menu;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.material.MaterialCheckBox;
import icyllis.modernui.material.MaterialRadioButton;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.util.StateSet;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.AbsListView;
import icyllis.modernui.widget.CheckBox;
import icyllis.modernui.widget.CompoundButton;
import icyllis.modernui.widget.ImageView;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.RadioButton;
import icyllis.modernui.widget.TextView;

public class ListMenuItemView extends LinearLayout implements MenuView.ItemView, AbsListView.SelectionBoundsAdjuster {

    private static final ColorStateList TEXT_COLOR = new ColorStateList(new int[][] { StateSet.get(8), StateSet.WILD_CARD }, new int[] { -1, -8355712 });

    private MenuItemImpl mItemData;

    private ImageView mIconView;

    private RadioButton mRadioButton;

    private CheckBox mCheckBox;

    private final TextView mTitleView;

    private final TextView mShortcutView;

    private final ImageView mSubMenuArrowView;

    private final LinearLayout mContent;

    private boolean mForceShowIcon;

    public ListMenuItemView(Context context) {
        super(context);
        this.setMinimumWidth(this.dp(196.0F));
        this.setOrientation(1);
        this.setDividerDrawable(new Drawable() {

            @Override
            public void draw(@NonNull Canvas canvas) {
                Paint paint = Paint.obtain();
                paint.setRGBA(255, 255, 255, 32);
                canvas.drawRect(this.getBounds(), paint);
                paint.recycle();
            }

            @Override
            public int getIntrinsicHeight() {
                return ListMenuItemView.this.dp(1.0F);
            }
        });
        this.setDividerPadding(this.dp(2.0F));
        this.mContent = new LinearLayout(this.getContext());
        this.mContent.setDuplicateParentStateEnabled(true);
        this.mContent.setPaddingRelative(this.dp(4.0F), this.dp(2.0F), this.dp(16.0F), this.dp(2.0F));
        this.mTitleView = new TextView(this.getContext());
        this.mTitleView.setId(16908310);
        this.mTitleView.setTextSize(16.0F);
        this.mTitleView.setTextColor(TEXT_COLOR);
        this.mTitleView.setSingleLine();
        this.mTitleView.setDuplicateParentStateEnabled(true);
        this.mTitleView.setTextAlignment(5);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -2, 1.0F);
        params.gravity = 16;
        params.setMarginStart(this.dp(16.0F));
        this.mContent.addView(this.mTitleView, params);
        this.mShortcutView = new TextView(this.getContext());
        this.mShortcutView.setTextSize(14.0F);
        this.mShortcutView.setTextColor(-3223858);
        this.mShortcutView.setSingleLine();
        this.mShortcutView.setDuplicateParentStateEnabled(true);
        this.mShortcutView.setTextAlignment(5);
        params = new LinearLayout.LayoutParams(-2, -2);
        params.gravity = 16;
        params.setMarginStart(this.dp(16.0F));
        this.mContent.addView(this.mShortcutView, params);
        this.mSubMenuArrowView = new ImageView(this.getContext());
        this.mSubMenuArrowView.setScaleType(ImageView.ScaleType.CENTER);
        this.mSubMenuArrowView.setVisibility(8);
        this.mSubMenuArrowView.setImageDrawable(new SubMenuArrowDrawable(context));
        params = new LinearLayout.LayoutParams(-2, -2);
        params.gravity = 17;
        params.setMarginStart(this.dp(8.0F));
        this.mContent.addView(this.mSubMenuArrowView, params);
        this.addView(this.mContent, -1, -2);
        this.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    }

    @Override
    public void initialize(@NonNull MenuItemImpl itemData, int menuType) {
        this.mItemData = itemData;
        this.setVisibility(itemData.isVisible() ? 0 : 8);
        this.setTitle(itemData.getTitleForItemView(this));
        this.setCheckable(itemData.isCheckable());
        this.setShortcut(itemData.shouldShowShortcut(), itemData.getShortcut());
        this.setIcon(itemData.getIcon());
        this.setEnabled(itemData.isEnabled());
        this.setSubMenuArrowVisible(itemData.hasSubMenu());
    }

    private void addContentView(View v) {
        this.addContentView(v, -1);
    }

    private void addContentView(View v, int index) {
        if (this.mContent != null) {
            this.mContent.addView(v, index);
        } else {
            this.addView(v, index);
        }
    }

    public void setForceShowIcon(boolean forceShow) {
        this.mForceShowIcon = forceShow;
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
        if (title != null) {
            this.mTitleView.setText(title);
            if (this.mTitleView.getVisibility() != 0) {
                this.mTitleView.setVisibility(0);
            }
        } else if (this.mTitleView.getVisibility() != 8) {
            this.mTitleView.setVisibility(8);
        }
    }

    @Override
    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    @Override
    public void setCheckable(boolean checkable) {
        if (checkable || this.mRadioButton != null || this.mCheckBox != null) {
            CompoundButton compoundButton;
            CompoundButton otherCompoundButton;
            if (this.mItemData.isExclusiveCheckable()) {
                if (this.mRadioButton == null) {
                    this.insertRadioButton();
                }
                compoundButton = this.mRadioButton;
                otherCompoundButton = this.mCheckBox;
            } else {
                if (this.mCheckBox == null) {
                    this.insertCheckBox();
                }
                compoundButton = this.mCheckBox;
                otherCompoundButton = this.mRadioButton;
            }
            if (checkable) {
                compoundButton.setChecked(this.mItemData.isChecked());
                if (compoundButton.getVisibility() != 0) {
                    compoundButton.setVisibility(0);
                }
                if (otherCompoundButton != null && otherCompoundButton.getVisibility() != 8) {
                    otherCompoundButton.setVisibility(8);
                }
            } else {
                if (this.mCheckBox != null) {
                    this.mCheckBox.setVisibility(8);
                }
                if (this.mRadioButton != null) {
                    this.mRadioButton.setVisibility(8);
                }
            }
        }
    }

    @Override
    public void setChecked(boolean checked) {
        CompoundButton compoundButton;
        if (this.mItemData.isExclusiveCheckable()) {
            if (this.mRadioButton == null) {
                this.insertRadioButton();
            }
            compoundButton = this.mRadioButton;
        } else {
            if (this.mCheckBox == null) {
                this.insertCheckBox();
            }
            compoundButton = this.mCheckBox;
        }
        compoundButton.setChecked(checked);
    }

    private void setSubMenuArrowVisible(boolean hasSubmenu) {
        if (this.mSubMenuArrowView != null) {
            this.mSubMenuArrowView.setVisibility(hasSubmenu ? 0 : 8);
        }
    }

    @Override
    public void setShortcut(boolean showShortcut, char shortcutKey) {
        int newVisibility = showShortcut && this.mItemData.shouldShowShortcut() ? 0 : 8;
        if (newVisibility == 0) {
            this.mShortcutView.setText(this.mItemData.getShortcutLabel());
        }
        if (this.mShortcutView.getVisibility() != newVisibility) {
            this.mShortcutView.setVisibility(newVisibility);
        }
    }

    @Override
    public void setIcon(Drawable icon) {
        boolean showIcon = this.mItemData.shouldShowIcon() || this.mForceShowIcon;
        if (showIcon) {
            if (this.mIconView != null || icon != null || this.mForceShowIcon) {
                if (this.mIconView == null) {
                    this.insertIconView();
                }
                if (icon == null && !this.mForceShowIcon) {
                    this.mIconView.setVisibility(8);
                } else {
                    this.mIconView.setImageDrawable(icon);
                    if (this.mIconView.getVisibility() != 0) {
                        this.mIconView.setVisibility(0);
                    }
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mIconView != null && this.mForceShowIcon) {
            ViewGroup.LayoutParams lp = this.getLayoutParams();
            LinearLayout.LayoutParams iconLp = (LinearLayout.LayoutParams) this.mIconView.getLayoutParams();
            if (lp.height > 0 && iconLp.width <= 0) {
                iconLp.width = lp.height;
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void insertIconView() {
        this.mIconView = new ImageView(this.getContext());
        this.mIconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.mIconView.setDuplicateParentStateEnabled(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.gravity = 16;
        params.setMarginsRelative(this.dp(8.0F), this.dp(8.0F), this.dp(-8.0F), this.dp(8.0F));
        this.mIconView.setLayoutParams(params);
        if (this.mRadioButton == null && this.mCheckBox == null) {
            this.addContentView(this.mIconView, 0);
        } else {
            this.addContentView(this.mIconView, 1);
        }
    }

    private void insertRadioButton() {
        this.mRadioButton = new MaterialRadioButton(this.getContext());
        this.mRadioButton.setFocusable(false);
        this.mRadioButton.setClickable(false);
        this.mRadioButton.setDuplicateParentStateEnabled(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.gravity = 16;
        this.mRadioButton.setLayoutParams(params);
        this.addContentView(this.mRadioButton, 0);
    }

    private void insertCheckBox() {
        this.mCheckBox = new MaterialCheckBox(this.getContext());
        this.mCheckBox.setFocusable(false);
        this.mCheckBox.setClickable(false);
        this.mCheckBox.setDuplicateParentStateEnabled(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.gravity = 16;
        this.mCheckBox.setLayoutParams(params);
        this.addContentView(this.mCheckBox, 0);
    }

    @Override
    public boolean prefersCondensedTitle() {
        return false;
    }

    @Override
    public boolean showsIcon() {
        return this.mForceShowIcon;
    }

    public void setGroupDividerEnabled(boolean groupDividerEnabled) {
        if (this.getShowDividers() == 0 == groupDividerEnabled) {
            if (groupDividerEnabled) {
                this.setShowDividers(1);
                this.setPadding(0, this.dp(2.0F), 0, 0);
            } else {
                this.setShowDividers(0);
                this.setPadding(0, 0, 0, 0);
            }
        }
    }

    @Override
    public void adjustListItemSelectionBounds(@NonNull Rect rect) {
        rect.inset(this.dp(4.0F), this.dp(2.0F));
        if (this.getShowDividers() != 0) {
            rect.top = rect.top + this.dp(5.0F);
        }
    }
}