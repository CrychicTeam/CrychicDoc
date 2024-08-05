package mezz.jei.gui.config;

import java.util.List;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.gui.bookmarks.BookmarkList;

public interface IBookmarkConfig {

    void saveBookmarks(IIngredientManager var1, List<ITypedIngredient<?>> var2);

    void loadBookmarks(IIngredientManager var1, BookmarkList var2);
}