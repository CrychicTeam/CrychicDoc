package mezz.jei.gui.bookmarks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.config.IClientConfig;
import mezz.jei.gui.config.IBookmarkConfig;
import mezz.jei.gui.overlay.IIngredientGridSource;
import net.minecraft.world.item.ItemStack;

public class BookmarkList implements IIngredientGridSource {

    private final List<ITypedIngredient<?>> list = new LinkedList();

    private final IIngredientManager ingredientManager;

    private final IBookmarkConfig bookmarkConfig;

    private final IClientConfig clientConfig;

    private final List<IIngredientGridSource.SourceListChangedListener> listeners = new ArrayList();

    public BookmarkList(IIngredientManager ingredientManager, IBookmarkConfig bookmarkConfig, IClientConfig clientConfig) {
        this.ingredientManager = ingredientManager;
        this.bookmarkConfig = bookmarkConfig;
        this.clientConfig = clientConfig;
    }

    public <T> boolean add(ITypedIngredient<T> value) {
        if (this.contains(value)) {
            return false;
        } else {
            this.addToList(value, this.clientConfig.isAddingBookmarksToFrontEnabled());
            this.notifyListenersOfChange();
            this.bookmarkConfig.saveBookmarks(this.ingredientManager, this.list);
            return true;
        }
    }

    private <T> boolean contains(ITypedIngredient<T> value) {
        return this.indexOf(value) >= 0;
    }

    private <T> int indexOf(ITypedIngredient<T> value) {
        Optional<ITypedIngredient<T>> normalized = normalize(this.ingredientManager, value);
        if (normalized.isEmpty()) {
            return -1;
        } else {
            value = (ITypedIngredient<T>) normalized.get();
            IIngredientHelper<T> ingredientHelper = this.ingredientManager.getIngredientHelper(value.getType());
            String uniqueId = ingredientHelper.getUniqueId(value.getIngredient(), UidContext.Ingredient);
            for (int i = 0; i < this.list.size(); i++) {
                ITypedIngredient<?> existing = (ITypedIngredient<?>) this.list.get(i);
                if (equal(ingredientHelper, value, uniqueId, existing)) {
                    return i;
                }
            }
            return -1;
        }
    }

    public static <T> Optional<ITypedIngredient<T>> normalize(IIngredientManager ingredientManager, ITypedIngredient<T> value) {
        IIngredientHelper<T> ingredientHelper = ingredientManager.getIngredientHelper(value.getType());
        T ingredient = ingredientHelper.normalizeIngredient(value.getIngredient());
        return ingredientManager.createTypedIngredient(value.getType(), ingredient);
    }

    private static <T> boolean equal(IIngredientHelper<T> ingredientHelper, ITypedIngredient<T> a, String uidA, ITypedIngredient<?> b) {
        if (a.getIngredient() == b.getIngredient()) {
            return true;
        } else {
            if (a.getIngredient() instanceof ItemStack itemStackA && b.getIngredient() instanceof ItemStack itemStackB) {
                return ItemStack.matches(itemStackA, itemStackB);
            }
            Optional<T> filteredB = b.getIngredient(a.getType());
            if (filteredB.isPresent()) {
                T ingredientB = (T) filteredB.get();
                String uidB = ingredientHelper.getUniqueId(ingredientB, UidContext.Ingredient);
                return uidA.equals(uidB);
            } else {
                return false;
            }
        }
    }

    public <T> boolean remove(ITypedIngredient<T> ingredient) {
        int index = this.indexOf(ingredient);
        if (index < 0) {
            return false;
        } else {
            this.list.remove(index);
            this.notifyListenersOfChange();
            this.bookmarkConfig.saveBookmarks(this.ingredientManager, this.list);
            return true;
        }
    }

    public <T> void addToList(ITypedIngredient<T> value, boolean addToFront) {
        Optional<ITypedIngredient<T>> result = normalize(this.ingredientManager, value);
        if (!result.isEmpty()) {
            value = (ITypedIngredient<T>) result.get();
            if (addToFront) {
                this.list.add(0, value);
            } else {
                this.list.add(value);
            }
        }
    }

    @Override
    public List<ITypedIngredient<?>> getIngredientList() {
        return this.list;
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public void addSourceListChangedListener(IIngredientGridSource.SourceListChangedListener listener) {
        this.listeners.add(listener);
    }

    public void notifyListenersOfChange() {
        for (IIngredientGridSource.SourceListChangedListener listener : this.listeners) {
            listener.onSourceListChanged();
        }
    }
}