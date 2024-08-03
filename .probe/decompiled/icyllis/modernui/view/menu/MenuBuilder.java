package icyllis.modernui.view.menu;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.view.ActionProvider;
import icyllis.modernui.view.ContextMenu;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.Menu;
import icyllis.modernui.view.MenuItem;
import icyllis.modernui.view.SubMenu;
import icyllis.modernui.view.View;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MenuBuilder implements Menu {

    private static final int[] sCategoryToOrder = new int[] { 1, 4, 5, 3, 2, 0 };

    private final Context mContext;

    private boolean mQwertyMode;

    private boolean mShortcutsVisible;

    private MenuBuilder.Callback mCallback;

    private final ArrayList<MenuItemImpl> mItems;

    private final ArrayList<MenuItemImpl> mVisibleItems;

    private boolean mIsVisibleItemsStale;

    private final ArrayList<MenuItemImpl> mActionItems;

    private final ArrayList<MenuItemImpl> mNonActionItems;

    private boolean mIsActionItemsStale;

    private int mDefaultShowAsAction = 0;

    private ContextMenu.ContextMenuInfo mCurrentMenuInfo;

    CharSequence mHeaderTitle;

    Drawable mHeaderIcon;

    View mHeaderView;

    private boolean mPreventDispatchingItemsChanged = false;

    private boolean mItemsChangedWhileDispatchPrevented = false;

    private boolean mOptionalIconsVisible = false;

    private boolean mIsClosing = false;

    private final ArrayList<MenuItemImpl> mTempShortcutItemList = new ArrayList();

    private final CopyOnWriteArrayList<WeakReference<MenuPresenter>> mPresenters = new CopyOnWriteArrayList();

    private MenuItemImpl mExpandedItem;

    private boolean mGroupDividerEnabled = false;

    public MenuBuilder(Context context) {
        this.mContext = context;
        this.mItems = new ArrayList();
        this.mVisibleItems = new ArrayList();
        this.mIsVisibleItemsStale = true;
        this.mActionItems = new ArrayList();
        this.mNonActionItems = new ArrayList();
        this.mIsActionItemsStale = true;
        this.setShortcutsVisibleInner(true);
    }

    public MenuBuilder setDefaultShowAsAction(int defaultShowAsAction) {
        this.mDefaultShowAsAction = defaultShowAsAction;
        return this;
    }

    public void addMenuPresenter(@NonNull MenuPresenter presenter) {
        this.addMenuPresenter(presenter, this.mContext);
    }

    public void addMenuPresenter(MenuPresenter presenter, Context menuContext) {
        this.mPresenters.add(new WeakReference(presenter));
        presenter.initForMenu(menuContext, this);
        this.mIsActionItemsStale = true;
    }

    public void removeMenuPresenter(MenuPresenter presenter) {
        for (WeakReference<MenuPresenter> ref : this.mPresenters) {
            MenuPresenter item = (MenuPresenter) ref.get();
            if (item == null || item == presenter) {
                this.mPresenters.remove(ref);
            }
        }
    }

    private void dispatchPresenterUpdate(boolean cleared) {
        if (!this.mPresenters.isEmpty()) {
            this.stopDispatchingItemsChanged();
            for (WeakReference<MenuPresenter> ref : this.mPresenters) {
                MenuPresenter presenter = (MenuPresenter) ref.get();
                if (presenter == null) {
                    this.mPresenters.remove(ref);
                } else {
                    presenter.updateMenuView(cleared);
                }
            }
            this.startDispatchingItemsChanged();
        }
    }

    private boolean dispatchSubMenuSelected(SubMenuBuilder subMenu, MenuPresenter preferredPresenter) {
        if (this.mPresenters.isEmpty()) {
            return false;
        } else {
            boolean result = false;
            if (preferredPresenter != null) {
                result = preferredPresenter.onSubMenuSelected(subMenu);
            }
            for (WeakReference<MenuPresenter> ref : this.mPresenters) {
                MenuPresenter presenter = (MenuPresenter) ref.get();
                if (presenter == null) {
                    this.mPresenters.remove(ref);
                } else if (!result) {
                    result = presenter.onSubMenuSelected(subMenu);
                }
            }
            return result;
        }
    }

    public void setCallback(MenuBuilder.Callback cb) {
        this.mCallback = cb;
    }

    @NonNull
    private MenuItemImpl addInternal(int group, int id, int categoryOrder, @Nullable CharSequence title) {
        int ordering = getOrdering(categoryOrder);
        MenuItemImpl item = new MenuItemImpl(this, group, id, categoryOrder, ordering, title, this.mDefaultShowAsAction);
        if (this.mCurrentMenuInfo != null) {
            item.setMenuInfo(this.mCurrentMenuInfo);
        }
        this.mItems.add(findInsertIndex(this.mItems, ordering), item);
        this.onItemsChanged(true);
        return item;
    }

    @NonNull
    @Override
    public MenuItem add(@Nullable CharSequence title) {
        return this.addInternal(0, 0, 0, title);
    }

    @NonNull
    @Override
    public MenuItem add(int group, int id, int categoryOrder, @Nullable CharSequence title) {
        return this.addInternal(group, id, categoryOrder, title);
    }

    @NonNull
    @Override
    public SubMenu addSubMenu(@Nullable CharSequence title) {
        return this.addSubMenu(0, 0, 0, title);
    }

    @NonNull
    @Override
    public SubMenu addSubMenu(int group, int id, int categoryOrder, @Nullable CharSequence title) {
        MenuItemImpl item = this.addInternal(group, id, categoryOrder, title);
        SubMenuBuilder subMenu = new SubMenuBuilder(this.mContext, this, item);
        item.setSubMenu(subMenu);
        return subMenu;
    }

    @Override
    public void setGroupDividerEnabled(boolean groupDividerEnabled) {
        this.mGroupDividerEnabled = groupDividerEnabled;
    }

    public boolean isGroupDividerEnabled() {
        return this.mGroupDividerEnabled;
    }

    @Override
    public void removeItem(int id) {
        this.removeItemAtInt(this.findItemIndex(id), true);
    }

    @Override
    public void removeGroup(int group) {
        int i = this.findGroupIndex(group);
        if (i >= 0) {
            int maxRemovable = this.mItems.size() - i;
            int numRemoved = 0;
            while (numRemoved++ < maxRemovable && ((MenuItemImpl) this.mItems.get(i)).getGroupId() == group) {
                this.removeItemAtInt(i, false);
            }
            this.onItemsChanged(true);
        }
    }

    private void removeItemAtInt(int index, boolean updateChildrenOnMenuViews) {
        if (index >= 0 && index < this.mItems.size()) {
            this.mItems.remove(index);
            if (updateChildrenOnMenuViews) {
                this.onItemsChanged(true);
            }
        }
    }

    public void removeItemAt(int index) {
        this.removeItemAtInt(index, true);
    }

    public void clearAll() {
        this.mPreventDispatchingItemsChanged = true;
        this.clear();
        this.clearHeader();
        this.mPresenters.clear();
        this.mPreventDispatchingItemsChanged = false;
        this.mItemsChangedWhileDispatchPrevented = false;
        this.onItemsChanged(true);
    }

    @Override
    public void clear() {
        if (this.mExpandedItem != null) {
            this.collapseItemActionView(this.mExpandedItem);
        }
        this.mItems.clear();
        this.onItemsChanged(true);
    }

    void setExclusiveItemChecked(@NonNull MenuItem item) {
        int group = item.getGroupId();
        for (MenuItemImpl curItem : this.mItems) {
            if (curItem.getGroupId() == group && curItem.isExclusiveCheckable() && curItem.isCheckable()) {
                curItem.setCheckedInt(curItem == item);
            }
        }
    }

    @Override
    public void setGroupCheckable(int group, boolean checkable, boolean exclusive) {
        for (MenuItemImpl item : this.mItems) {
            if (item.getGroupId() == group) {
                item.setExclusiveCheckable(exclusive);
                item.setCheckable(checkable);
            }
        }
    }

    @Override
    public void setGroupVisible(int group, boolean visible) {
        boolean changedAtLeastOneItem = false;
        for (MenuItemImpl item : this.mItems) {
            if (item.getGroupId() == group && item.setVisibleInt(visible)) {
                changedAtLeastOneItem = true;
            }
        }
        if (changedAtLeastOneItem) {
            this.onItemsChanged(true);
        }
    }

    @Override
    public void setGroupEnabled(int group, boolean enabled) {
        for (MenuItemImpl item : this.mItems) {
            if (item.getGroupId() == group) {
                item.setEnabled(enabled);
            }
        }
    }

    @Override
    public boolean hasVisibleItems() {
        int size = this.size();
        for (int i = 0; i < size; i++) {
            MenuItemImpl item = (MenuItemImpl) this.mItems.get(i);
            if (item.isVisible()) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public MenuItem findItem(int id) {
        int size = this.size();
        for (int i = 0; i < size; i++) {
            MenuItemImpl item = (MenuItemImpl) this.mItems.get(i);
            if (item.getItemId() == id) {
                return item;
            }
            if (item.hasSubMenu()) {
                MenuItem possibleItem = item.getSubMenu().findItem(id);
                if (possibleItem != null) {
                    return possibleItem;
                }
            }
        }
        return null;
    }

    public int findItemIndex(int id) {
        int size = this.size();
        for (int i = 0; i < size; i++) {
            MenuItemImpl item = (MenuItemImpl) this.mItems.get(i);
            if (item.getItemId() == id) {
                return i;
            }
        }
        return -1;
    }

    public int findGroupIndex(int group) {
        return this.findGroupIndex(group, 0);
    }

    public int findGroupIndex(int group, int start) {
        int size = this.size();
        if (start < 0) {
            start = 0;
        }
        for (int i = start; i < size; i++) {
            MenuItemImpl item = (MenuItemImpl) this.mItems.get(i);
            if (item.getGroupId() == group) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return this.mItems.size();
    }

    @NonNull
    @Override
    public MenuItem getItem(int index) {
        return (MenuItem) this.mItems.get(index);
    }

    @Override
    public boolean isShortcutKey(int keyCode, @NonNull KeyEvent event) {
        return this.findItemWithShortcutForKey(event) != null;
    }

    @Override
    public void setQwertyMode(boolean isQwerty) {
        this.mQwertyMode = isQwerty;
        this.onItemsChanged(false);
    }

    private static int getOrdering(int categoryOrder) {
        int index = (categoryOrder & -65536) >> 16;
        if (index >= 0 && index < sCategoryToOrder.length) {
            return sCategoryToOrder[index] << 16 | categoryOrder & 65535;
        } else {
            throw new IllegalArgumentException("order does not contain a valid category.");
        }
    }

    boolean isQwertyMode() {
        return this.mQwertyMode;
    }

    public void setShortcutsVisible(boolean shortcutsVisible) {
        if (this.mShortcutsVisible != shortcutsVisible) {
            this.setShortcutsVisibleInner(shortcutsVisible);
            this.onItemsChanged(false);
        }
    }

    private void setShortcutsVisibleInner(boolean shortcutsVisible) {
        this.mShortcutsVisible = shortcutsVisible;
    }

    public boolean isShortcutsVisible() {
        return this.mShortcutsVisible;
    }

    boolean dispatchMenuItemSelected(MenuBuilder menu, MenuItem item) {
        return this.mCallback != null && this.mCallback.onMenuItemSelected(menu, item);
    }

    public Context getContext() {
        return this.mContext;
    }

    public void changeMenuMode() {
        if (this.mCallback != null) {
            this.mCallback.onMenuModeChange(this);
        }
    }

    private static int findInsertIndex(@NonNull ArrayList<MenuItemImpl> items, int ordering) {
        for (int i = items.size() - 1; i >= 0; i--) {
            MenuItemImpl item = (MenuItemImpl) items.get(i);
            if (item.getOrdering() <= ordering) {
                return i + 1;
            }
        }
        return 0;
    }

    @Override
    public boolean performShortcut(int keyCode, @NonNull KeyEvent event, int flags) {
        MenuItemImpl item = this.findItemWithShortcutForKey(event);
        boolean handled = false;
        if (item != null) {
            handled = this.performItemAction(item, flags);
        }
        if ((flags & 2) != 0) {
            this.close(true);
        }
        return handled;
    }

    void findItemsWithShortcutForKey(List<MenuItemImpl> items, @NonNull KeyEvent event) {
        boolean qwerty = this.isQwertyMode();
        int modifierState = event.getModifiers();
        char possibleChar = event.getMappedChar();
        if (possibleChar != 0) {
            for (MenuItemImpl item : this.mItems) {
                if (item.hasSubMenu()) {
                    ((MenuBuilder) item.getSubMenu()).findItemsWithShortcutForKey(items, event);
                }
                char shortcutChar = qwerty ? item.getAlphabeticShortcut() : item.getNumericShortcut();
                int shortcutModifiers = qwerty ? item.getAlphabeticModifiers() : item.getNumericModifiers();
                boolean isModifiersExactMatch = (modifierState & SUPPORTED_MODIFIERS_MASK) == (shortcutModifiers & SUPPORTED_MODIFIERS_MASK);
                if (isModifiersExactMatch && shortcutChar != 0 && shortcutChar == possibleChar && item.isEnabled()) {
                    items.add(item);
                }
            }
        }
    }

    MenuItemImpl findItemWithShortcutForKey(KeyEvent event) {
        ArrayList<MenuItemImpl> items = this.mTempShortcutItemList;
        items.clear();
        this.findItemsWithShortcutForKey(items, event);
        return items.isEmpty() ? null : (MenuItemImpl) items.get(0);
    }

    @Override
    public boolean performIdentifierAction(int id, int flags) {
        return this.performItemAction(this.findItem(id), flags);
    }

    public boolean performItemAction(MenuItem item, int flags) {
        return this.performItemAction(item, null, flags);
    }

    public boolean performItemAction(MenuItem item, MenuPresenter preferredPresenter, int flags) {
        MenuItemImpl itemImpl = (MenuItemImpl) item;
        if (itemImpl != null && itemImpl.isEnabled()) {
            boolean invoked = itemImpl.invoke();
            ActionProvider provider = item.getActionProvider();
            boolean providerHasSubMenu = provider != null && provider.hasSubMenu();
            if (itemImpl.hasCollapsibleActionView()) {
                invoked |= itemImpl.expandActionView();
                if (invoked) {
                    this.close(true);
                }
            } else if (!itemImpl.hasSubMenu() && !providerHasSubMenu) {
                if ((flags & 1) == 0) {
                    this.close(true);
                }
            } else {
                if (!itemImpl.hasSubMenu()) {
                    itemImpl.setSubMenu(new SubMenuBuilder(this.mContext, this, itemImpl));
                }
                SubMenuBuilder subMenu = (SubMenuBuilder) itemImpl.getSubMenu();
                if (providerHasSubMenu) {
                    provider.onPrepareSubMenu(subMenu);
                }
                invoked |= this.dispatchSubMenuSelected(subMenu, preferredPresenter);
                if (!invoked) {
                    this.close(true);
                }
            }
            return invoked;
        } else {
            return false;
        }
    }

    public final void close(boolean closeAllMenus) {
        if (!this.mIsClosing) {
            this.mIsClosing = true;
            for (WeakReference<MenuPresenter> ref : this.mPresenters) {
                MenuPresenter presenter = (MenuPresenter) ref.get();
                if (presenter == null) {
                    this.mPresenters.remove(ref);
                } else {
                    presenter.onCloseMenu(this, closeAllMenus);
                }
            }
            this.mIsClosing = false;
        }
    }

    @Override
    public void close() {
        this.close(true);
    }

    public void onItemsChanged(boolean structureChanged) {
        if (!this.mPreventDispatchingItemsChanged) {
            if (structureChanged) {
                this.mIsVisibleItemsStale = true;
                this.mIsActionItemsStale = true;
            }
            this.dispatchPresenterUpdate(structureChanged);
        } else {
            this.mItemsChangedWhileDispatchPrevented = true;
        }
    }

    public void stopDispatchingItemsChanged() {
        if (!this.mPreventDispatchingItemsChanged) {
            this.mPreventDispatchingItemsChanged = true;
            this.mItemsChangedWhileDispatchPrevented = false;
        }
    }

    public void startDispatchingItemsChanged() {
        this.mPreventDispatchingItemsChanged = false;
        if (this.mItemsChangedWhileDispatchPrevented) {
            this.mItemsChangedWhileDispatchPrevented = false;
            this.onItemsChanged(true);
        }
    }

    void onItemVisibleChanged(MenuItemImpl item) {
        this.mIsVisibleItemsStale = true;
        this.onItemsChanged(true);
    }

    void onItemActionRequestChanged(MenuItemImpl item) {
        this.mIsActionItemsStale = true;
        this.onItemsChanged(true);
    }

    @NonNull
    public ArrayList<MenuItemImpl> getVisibleItems() {
        if (!this.mIsVisibleItemsStale) {
            return this.mVisibleItems;
        } else {
            this.mVisibleItems.clear();
            for (MenuItemImpl item : this.mItems) {
                if (item.isVisible()) {
                    this.mVisibleItems.add(item);
                }
            }
            this.mIsVisibleItemsStale = false;
            this.mIsActionItemsStale = true;
            return this.mVisibleItems;
        }
    }

    public void flagActionItems() {
        ArrayList<MenuItemImpl> visibleItems = this.getVisibleItems();
        if (this.mIsActionItemsStale) {
            boolean flagged = false;
            for (WeakReference<MenuPresenter> ref : this.mPresenters) {
                MenuPresenter presenter = (MenuPresenter) ref.get();
                if (presenter == null) {
                    this.mPresenters.remove(ref);
                } else {
                    flagged |= presenter.flagActionItems();
                }
            }
            this.mActionItems.clear();
            this.mNonActionItems.clear();
            if (flagged) {
                for (MenuItemImpl item : visibleItems) {
                    if (item.isActionButton()) {
                        this.mActionItems.add(item);
                    } else {
                        this.mNonActionItems.add(item);
                    }
                }
            } else {
                this.mNonActionItems.addAll(this.getVisibleItems());
            }
            this.mIsActionItemsStale = false;
        }
    }

    public ArrayList<MenuItemImpl> getActionItems() {
        this.flagActionItems();
        return this.mActionItems;
    }

    public ArrayList<MenuItemImpl> getNonActionItems() {
        this.flagActionItems();
        return this.mNonActionItems;
    }

    public void clearHeader() {
        this.mHeaderIcon = null;
        this.mHeaderTitle = null;
        this.mHeaderView = null;
        this.onItemsChanged(false);
    }

    private void setHeaderInternal(@Nullable CharSequence title, @Nullable Drawable icon, @Nullable View view) {
        if (view != null) {
            this.mHeaderView = view;
            this.mHeaderTitle = null;
            this.mHeaderIcon = null;
        } else {
            if (title != null) {
                this.mHeaderTitle = title;
            }
            if (icon != null) {
                this.mHeaderIcon = icon;
            }
            this.mHeaderView = null;
        }
        this.onItemsChanged(false);
    }

    protected MenuBuilder setHeaderTitleInt(CharSequence title) {
        this.setHeaderInternal(title, null, null);
        return this;
    }

    protected MenuBuilder setHeaderIconInt(Drawable icon) {
        this.setHeaderInternal(null, icon, null);
        return this;
    }

    protected MenuBuilder setHeaderViewInt(View view) {
        this.setHeaderInternal(null, null, view);
        return this;
    }

    public CharSequence getHeaderTitle() {
        return this.mHeaderTitle;
    }

    public Drawable getHeaderIcon() {
        return this.mHeaderIcon;
    }

    public View getHeaderView() {
        return this.mHeaderView;
    }

    public MenuBuilder getRootMenu() {
        return this;
    }

    public void setCurrentMenuInfo(ContextMenu.ContextMenuInfo menuInfo) {
        this.mCurrentMenuInfo = menuInfo;
    }

    public void setOptionalIconsVisible(boolean visible) {
        this.mOptionalIconsVisible = visible;
    }

    boolean getOptionalIconsVisible() {
        return this.mOptionalIconsVisible;
    }

    public boolean expandItemActionView(MenuItemImpl item) {
        if (this.mPresenters.isEmpty()) {
            return false;
        } else {
            boolean expanded = false;
            this.stopDispatchingItemsChanged();
            for (WeakReference<MenuPresenter> ref : this.mPresenters) {
                MenuPresenter presenter = (MenuPresenter) ref.get();
                if (presenter == null) {
                    this.mPresenters.remove(ref);
                } else {
                    expanded = presenter.expandItemActionView(this, item);
                    if (expanded) {
                        break;
                    }
                }
            }
            this.startDispatchingItemsChanged();
            if (expanded) {
                this.mExpandedItem = item;
            }
            return expanded;
        }
    }

    public boolean collapseItemActionView(MenuItemImpl item) {
        if (!this.mPresenters.isEmpty() && this.mExpandedItem == item) {
            boolean collapsed = false;
            this.stopDispatchingItemsChanged();
            for (WeakReference<MenuPresenter> ref : this.mPresenters) {
                MenuPresenter presenter = (MenuPresenter) ref.get();
                if (presenter == null) {
                    this.mPresenters.remove(ref);
                } else {
                    collapsed = presenter.collapseItemActionView(this, item);
                    if (collapsed) {
                        break;
                    }
                }
            }
            this.startDispatchingItemsChanged();
            if (collapsed) {
                this.mExpandedItem = null;
            }
            return collapsed;
        } else {
            return false;
        }
    }

    public MenuItemImpl getExpandedItem() {
        return this.mExpandedItem;
    }

    public interface Callback {

        boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2);

        void onMenuModeChange(MenuBuilder var1);
    }

    public interface ItemInvoker {

        boolean invokeItem(MenuItemImpl var1);
    }
}