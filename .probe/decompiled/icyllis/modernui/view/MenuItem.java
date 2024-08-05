package icyllis.modernui.view;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.util.ColorStateList;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface MenuItem {

    int SHOW_AS_ACTION_NEVER = 0;

    int SHOW_AS_ACTION_IF_ROOM = 1;

    int SHOW_AS_ACTION_ALWAYS = 2;

    int SHOW_AS_ACTION_WITH_TEXT = 4;

    int SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW = 8;

    int getItemId();

    int getGroupId();

    int getOrder();

    MenuItem setTitle(CharSequence var1);

    CharSequence getTitle();

    MenuItem setTitleCondensed(CharSequence var1);

    CharSequence getTitleCondensed();

    MenuItem setIcon(Drawable var1);

    Drawable getIcon();

    default MenuItem setIconTintList(@Nullable ColorStateList tint) {
        return this;
    }

    @Nullable
    default ColorStateList getIconTintList() {
        return null;
    }

    MenuItem setShortcut(char var1, char var2);

    default MenuItem setShortcut(char numericChar, char alphaChar, int numericModifiers, int alphaModifiers) {
        return (alphaModifiers & Menu.SUPPORTED_MODIFIERS_MASK) == KeyEvent.META_CTRL_ON && (numericModifiers & Menu.SUPPORTED_MODIFIERS_MASK) == KeyEvent.META_CTRL_ON ? this.setShortcut(numericChar, alphaChar) : this;
    }

    MenuItem setNumericShortcut(char var1);

    default MenuItem setNumericShortcut(char numericChar, int numericModifiers) {
        return (numericModifiers & Menu.SUPPORTED_MODIFIERS_MASK) == KeyEvent.META_CTRL_ON ? this.setNumericShortcut(numericChar) : this;
    }

    char getNumericShortcut();

    default int getNumericModifiers() {
        return KeyEvent.META_CTRL_ON;
    }

    MenuItem setAlphabeticShortcut(char var1);

    default MenuItem setAlphabeticShortcut(char alphaChar, int alphaModifiers) {
        return (alphaModifiers & Menu.SUPPORTED_MODIFIERS_MASK) == KeyEvent.META_CTRL_ON ? this.setAlphabeticShortcut(alphaChar) : this;
    }

    char getAlphabeticShortcut();

    default int getAlphabeticModifiers() {
        return KeyEvent.META_CTRL_ON;
    }

    MenuItem setCheckable(boolean var1);

    boolean isCheckable();

    MenuItem setChecked(boolean var1);

    boolean isChecked();

    MenuItem setVisible(boolean var1);

    boolean isVisible();

    MenuItem setEnabled(boolean var1);

    boolean isEnabled();

    boolean hasSubMenu();

    SubMenu getSubMenu();

    MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener var1);

    @Nullable
    ContextMenu.ContextMenuInfo getMenuInfo();

    void setShowAsAction(int var1);

    MenuItem setShowAsActionFlags(int var1);

    MenuItem setActionView(View var1);

    View getActionView();

    MenuItem setActionProvider(ActionProvider var1);

    ActionProvider getActionProvider();

    boolean expandActionView();

    boolean collapseActionView();

    boolean isActionViewExpanded();

    MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener var1);

    default MenuItem setContentDescription(CharSequence contentDescription) {
        return this;
    }

    default CharSequence getContentDescription() {
        return null;
    }

    default MenuItem setTooltipText(CharSequence tooltipText) {
        return this;
    }

    default CharSequence getTooltipText() {
        return null;
    }

    @Internal
    default boolean requiresActionButton() {
        return false;
    }

    @Internal
    default boolean requiresOverflow() {
        return true;
    }

    public interface OnActionExpandListener {

        boolean onMenuItemActionExpand(@NonNull MenuItem var1);

        boolean onMenuItemActionCollapse(@NonNull MenuItem var1);
    }

    @FunctionalInterface
    public interface OnMenuItemClickListener {

        boolean onMenuItemClick(@NonNull MenuItem var1);
    }
}