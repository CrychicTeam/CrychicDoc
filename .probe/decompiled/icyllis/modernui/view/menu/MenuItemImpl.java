package icyllis.modernui.view.menu;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.view.ActionProvider;
import icyllis.modernui.view.ContextMenu;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.MenuItem;
import icyllis.modernui.view.SubMenu;
import icyllis.modernui.view.View;

public final class MenuItemImpl implements MenuItem {

    private static final int SHOW_AS_ACTION_MASK = 3;

    private final int mId;

    private final int mGroup;

    private final int mCategoryOrder;

    private final int mOrdering;

    private CharSequence mTitle;

    private CharSequence mTitleCondensed;

    private char mShortcutNumericChar;

    private int mShortcutNumericModifiers = KeyEvent.META_CTRL_ON;

    private char mShortcutAlphabeticChar;

    private int mShortcutAlphabeticModifiers = KeyEvent.META_CTRL_ON;

    private Drawable mIconDrawable;

    private ColorStateList mIconTintList = null;

    private boolean mHasIconTint = false;

    private boolean mNeedToApplyIconTint = false;

    private final MenuBuilder mMenu;

    private SubMenuBuilder mSubMenu;

    private Runnable mItemCallback;

    private MenuItem.OnMenuItemClickListener mClickListener;

    private int mFlags = 16;

    private static final int CHECKABLE = 1;

    private static final int CHECKED = 2;

    private static final int EXCLUSIVE = 4;

    private static final int HIDDEN = 8;

    private static final int ENABLED = 16;

    private static final int IS_ACTION = 32;

    private int mShowAsAction;

    private View mActionView;

    private ActionProvider mActionProvider;

    private MenuItem.OnActionExpandListener mOnActionExpandListener;

    private boolean mIsActionViewExpanded = false;

    private ContextMenu.ContextMenuInfo mMenuInfo;

    private CharSequence mContentDescription;

    private CharSequence mTooltipText;

    MenuItemImpl(@NonNull MenuBuilder menu, int group, int id, int categoryOrder, int ordering, @Nullable CharSequence title, int showAsAction) {
        this.mMenu = menu;
        this.mId = id;
        this.mGroup = group;
        this.mCategoryOrder = categoryOrder;
        this.mOrdering = ordering;
        this.mTitle = title;
        this.mShowAsAction = showAsAction;
    }

    public boolean invoke() {
        if (this.mClickListener != null && this.mClickListener.onMenuItemClick(this)) {
            return true;
        } else if (this.mMenu.dispatchMenuItemSelected(this.mMenu, this)) {
            return true;
        } else if (this.mItemCallback != null) {
            this.mItemCallback.run();
            return true;
        } else {
            return this.mActionProvider != null && this.mActionProvider.onPerformDefaultAction();
        }
    }

    @Override
    public boolean isEnabled() {
        return (this.mFlags & 16) != 0;
    }

    @Override
    public MenuItem setEnabled(boolean enabled) {
        if (enabled) {
            this.mFlags |= 16;
        } else {
            this.mFlags &= -17;
        }
        this.mMenu.onItemsChanged(false);
        return this;
    }

    @Override
    public int getGroupId() {
        return this.mGroup;
    }

    @Override
    public int getItemId() {
        return this.mId;
    }

    @Override
    public int getOrder() {
        return this.mCategoryOrder;
    }

    public int getOrdering() {
        return this.mOrdering;
    }

    Runnable getCallback() {
        return this.mItemCallback;
    }

    public MenuItem setCallback(Runnable callback) {
        this.mItemCallback = callback;
        return this;
    }

    @Override
    public char getAlphabeticShortcut() {
        return this.mShortcutAlphabeticChar;
    }

    @Override
    public int getAlphabeticModifiers() {
        return this.mShortcutAlphabeticModifiers;
    }

    @Override
    public MenuItem setAlphabeticShortcut(char alphaChar) {
        if (this.mShortcutAlphabeticChar == alphaChar) {
            return this;
        } else {
            this.mShortcutAlphabeticChar = Character.toLowerCase(alphaChar);
            this.mMenu.onItemsChanged(false);
            return this;
        }
    }

    @Override
    public MenuItem setAlphabeticShortcut(char alphaChar, int alphaModifiers) {
        if (this.mShortcutAlphabeticChar == alphaChar && this.mShortcutAlphabeticModifiers == alphaModifiers) {
            return this;
        } else {
            this.mShortcutAlphabeticChar = Character.toLowerCase(alphaChar);
            this.mShortcutAlphabeticModifiers = alphaModifiers;
            this.mMenu.onItemsChanged(false);
            return this;
        }
    }

    @Override
    public char getNumericShortcut() {
        return this.mShortcutNumericChar;
    }

    @Override
    public int getNumericModifiers() {
        return this.mShortcutNumericModifiers;
    }

    @Override
    public MenuItem setNumericShortcut(char numericChar) {
        if (this.mShortcutNumericChar == numericChar) {
            return this;
        } else {
            this.mShortcutNumericChar = numericChar;
            this.mMenu.onItemsChanged(false);
            return this;
        }
    }

    @Override
    public MenuItem setNumericShortcut(char numericChar, int numericModifiers) {
        if (this.mShortcutNumericChar == numericChar && this.mShortcutNumericModifiers == numericModifiers) {
            return this;
        } else {
            this.mShortcutNumericChar = numericChar;
            this.mShortcutNumericModifiers = numericModifiers;
            this.mMenu.onItemsChanged(false);
            return this;
        }
    }

    @Override
    public MenuItem setShortcut(char numericChar, char alphaChar) {
        this.mShortcutNumericChar = numericChar;
        this.mShortcutAlphabeticChar = Character.toLowerCase(alphaChar);
        this.mMenu.onItemsChanged(false);
        return this;
    }

    @Override
    public MenuItem setShortcut(char numericChar, char alphaChar, int numericModifiers, int alphaModifiers) {
        this.mShortcutNumericChar = numericChar;
        this.mShortcutNumericModifiers = numericModifiers;
        this.mShortcutAlphabeticChar = Character.toLowerCase(alphaChar);
        this.mShortcutAlphabeticModifiers = alphaModifiers;
        this.mMenu.onItemsChanged(false);
        return this;
    }

    char getShortcut() {
        return this.mMenu.isQwertyMode() ? this.mShortcutAlphabeticChar : this.mShortcutNumericChar;
    }

    @NonNull
    String getShortcutLabel() {
        char shortcut = this.getShortcut();
        if (shortcut == 0) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            int modifiers = this.mMenu.isQwertyMode() ? this.mShortcutAlphabeticModifiers : this.mShortcutNumericModifiers;
            appendModifier(sb, modifiers, KeyEvent.META_CTRL_ON, "Ctrl + ");
            appendModifier(sb, modifiers, 4, "Alt + ");
            appendModifier(sb, modifiers, 1, "Shift + ");
            sb.append(Character.toUpperCase(shortcut));
            return sb.toString();
        }
    }

    private static void appendModifier(StringBuilder sb, int mask, int modifier, String label) {
        if ((mask & modifier) == modifier) {
            sb.append(label);
        }
    }

    boolean shouldShowShortcut() {
        return this.mMenu.isShortcutsVisible() && this.getShortcut() != 0;
    }

    @Override
    public SubMenu getSubMenu() {
        return this.mSubMenu;
    }

    @Override
    public boolean hasSubMenu() {
        return this.mSubMenu != null;
    }

    void setSubMenu(@NonNull SubMenuBuilder subMenu) {
        this.mSubMenu = subMenu;
        subMenu.setHeaderTitle(this.getTitle());
    }

    @Override
    public CharSequence getTitle() {
        return this.mTitle;
    }

    CharSequence getTitleForItemView(MenuView.ItemView itemView) {
        return itemView != null && itemView.prefersCondensedTitle() ? this.getTitleCondensed() : this.getTitle();
    }

    @Override
    public MenuItem setTitle(CharSequence title) {
        this.mTitle = title;
        this.mMenu.onItemsChanged(false);
        if (this.mSubMenu != null) {
            this.mSubMenu.setHeaderTitle(title);
        }
        return this;
    }

    @Override
    public CharSequence getTitleCondensed() {
        return this.mTitleCondensed != null ? this.mTitleCondensed : this.mTitle;
    }

    @Override
    public MenuItem setTitleCondensed(CharSequence title) {
        this.mTitleCondensed = title;
        this.mMenu.onItemsChanged(false);
        return this;
    }

    @Nullable
    @Override
    public Drawable getIcon() {
        return this.mIconDrawable != null ? this.applyIconTintIfNecessary(this.mIconDrawable) : null;
    }

    @Override
    public MenuItem setIcon(Drawable icon) {
        this.mIconDrawable = icon;
        this.mNeedToApplyIconTint = true;
        this.mMenu.onItemsChanged(false);
        return this;
    }

    @Override
    public MenuItem setIconTintList(@Nullable ColorStateList iconTintList) {
        this.mIconTintList = iconTintList;
        this.mHasIconTint = true;
        this.mNeedToApplyIconTint = true;
        this.mMenu.onItemsChanged(false);
        return this;
    }

    @Nullable
    @Override
    public ColorStateList getIconTintList() {
        return this.mIconTintList;
    }

    private Drawable applyIconTintIfNecessary(Drawable icon) {
        if (icon != null && this.mNeedToApplyIconTint && this.mHasIconTint) {
            icon = icon.mutate();
            if (this.mHasIconTint) {
                icon.setTintList(this.mIconTintList);
            }
            this.mNeedToApplyIconTint = false;
        }
        return icon;
    }

    @Override
    public boolean isCheckable() {
        return (this.mFlags & 1) == 1;
    }

    @Override
    public MenuItem setCheckable(boolean checkable) {
        int oldFlags = this.mFlags;
        this.mFlags = this.mFlags & -2 | (checkable ? 1 : 0);
        if (oldFlags != this.mFlags) {
            this.mMenu.onItemsChanged(false);
        }
        return this;
    }

    public void setExclusiveCheckable(boolean exclusive) {
        this.mFlags = this.mFlags & -5 | (exclusive ? 4 : 0);
    }

    public boolean isExclusiveCheckable() {
        return (this.mFlags & 4) != 0;
    }

    @Override
    public boolean isChecked() {
        return (this.mFlags & 2) == 2;
    }

    @Override
    public MenuItem setChecked(boolean checked) {
        if ((this.mFlags & 4) != 0) {
            this.mMenu.setExclusiveItemChecked(this);
        } else {
            this.setCheckedInt(checked);
        }
        return this;
    }

    void setCheckedInt(boolean checked) {
        int oldFlags = this.mFlags;
        this.mFlags = this.mFlags & -3 | (checked ? 2 : 0);
        if (oldFlags != this.mFlags) {
            this.mMenu.onItemsChanged(false);
        }
    }

    @Override
    public boolean isVisible() {
        return this.mActionProvider != null && this.mActionProvider.overridesItemVisibility() ? (this.mFlags & 8) == 0 && this.mActionProvider.isVisible() : (this.mFlags & 8) == 0;
    }

    boolean setVisibleInt(boolean shown) {
        int oldFlags = this.mFlags;
        this.mFlags = this.mFlags & -9 | (shown ? 0 : 8);
        return oldFlags != this.mFlags;
    }

    @Override
    public MenuItem setVisible(boolean shown) {
        if (this.setVisibleInt(shown)) {
            this.mMenu.onItemVisibleChanged(this);
        }
        return this;
    }

    @Override
    public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener clickListener) {
        this.mClickListener = clickListener;
        return this;
    }

    @NonNull
    public String toString() {
        return this.mTitle != null ? this.mTitle.toString() : "";
    }

    void setMenuInfo(ContextMenu.ContextMenuInfo menuInfo) {
        this.mMenuInfo = menuInfo;
    }

    @Override
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return this.mMenuInfo;
    }

    public void actionFormatChanged() {
        this.mMenu.onItemActionRequestChanged(this);
    }

    public boolean shouldShowIcon() {
        return this.mMenu.getOptionalIconsVisible();
    }

    public boolean isActionButton() {
        return (this.mFlags & 32) == 32;
    }

    public boolean requestsActionButton() {
        return (this.mShowAsAction & 1) == 1;
    }

    @Override
    public boolean requiresActionButton() {
        return (this.mShowAsAction & 2) == 2;
    }

    @Override
    public boolean requiresOverflow() {
        return !this.requiresActionButton() && !this.requestsActionButton();
    }

    public void setIsActionButton(boolean isActionButton) {
        if (isActionButton) {
            this.mFlags |= 32;
        } else {
            this.mFlags &= -33;
        }
    }

    public boolean showsTextAsAction() {
        return (this.mShowAsAction & 4) == 4;
    }

    @Override
    public void setShowAsAction(int actionEnum) {
        switch(actionEnum & 3) {
            case 0:
            case 1:
            case 2:
                this.mShowAsAction = actionEnum;
                this.mMenu.onItemActionRequestChanged(this);
                return;
            default:
                throw new IllegalArgumentException("SHOW_AS_ACTION_ALWAYS, SHOW_AS_ACTION_IF_ROOM, and SHOW_AS_ACTION_NEVER are mutually exclusive.");
        }
    }

    @Override
    public MenuItem setActionView(View view) {
        this.mActionView = view;
        this.mActionProvider = null;
        if (view != null && view.getId() == -1 && this.mId > 0) {
            view.setId(this.mId);
        }
        this.mMenu.onItemActionRequestChanged(this);
        return this;
    }

    @Nullable
    @Override
    public View getActionView() {
        if (this.mActionView != null) {
            return this.mActionView;
        } else if (this.mActionProvider != null) {
            this.mActionView = this.mActionProvider.onCreateActionView(this);
            return this.mActionView;
        } else {
            return null;
        }
    }

    @Override
    public ActionProvider getActionProvider() {
        return this.mActionProvider;
    }

    @Override
    public MenuItem setActionProvider(ActionProvider actionProvider) {
        if (this.mActionProvider != null) {
            this.mActionProvider.reset();
        }
        this.mActionView = null;
        this.mActionProvider = actionProvider;
        this.mMenu.onItemsChanged(true);
        if (this.mActionProvider != null) {
            this.mActionProvider.setVisibilityListener(isVisible -> this.mMenu.onItemVisibleChanged(this));
        }
        return this;
    }

    @Override
    public MenuItem setShowAsActionFlags(int actionEnum) {
        this.setShowAsAction(actionEnum);
        return this;
    }

    @Override
    public boolean expandActionView() {
        if (!this.hasCollapsibleActionView()) {
            return false;
        } else {
            return this.mOnActionExpandListener != null && !this.mOnActionExpandListener.onMenuItemActionExpand(this) ? false : this.mMenu.expandItemActionView(this);
        }
    }

    @Override
    public boolean collapseActionView() {
        if ((this.mShowAsAction & 8) == 0) {
            return false;
        } else if (this.mActionView == null) {
            return true;
        } else {
            return this.mOnActionExpandListener != null && !this.mOnActionExpandListener.onMenuItemActionCollapse(this) ? false : this.mMenu.collapseItemActionView(this);
        }
    }

    @Override
    public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener listener) {
        this.mOnActionExpandListener = listener;
        return this;
    }

    public boolean hasCollapsibleActionView() {
        if ((this.mShowAsAction & 8) != 0) {
            if (this.mActionView == null && this.mActionProvider != null) {
                this.mActionView = this.mActionProvider.onCreateActionView(this);
            }
            return this.mActionView != null;
        } else {
            return false;
        }
    }

    public void setActionViewExpanded(boolean isExpanded) {
        this.mIsActionViewExpanded = isExpanded;
        this.mMenu.onItemsChanged(false);
    }

    @Override
    public boolean isActionViewExpanded() {
        return this.mIsActionViewExpanded;
    }

    @Override
    public MenuItem setContentDescription(CharSequence contentDescription) {
        this.mContentDescription = contentDescription;
        this.mMenu.onItemsChanged(false);
        return this;
    }

    @Override
    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }

    @Override
    public MenuItem setTooltipText(CharSequence tooltipText) {
        this.mTooltipText = tooltipText;
        this.mMenu.onItemsChanged(false);
        return this;
    }

    @Override
    public CharSequence getTooltipText() {
        return this.mTooltipText;
    }
}