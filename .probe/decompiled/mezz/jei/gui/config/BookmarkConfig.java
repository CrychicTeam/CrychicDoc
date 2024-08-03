package mezz.jei.gui.config;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.util.ServerConfigPathUtil;
import mezz.jei.gui.bookmarks.BookmarkList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BookmarkConfig implements IBookmarkConfig {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String MARKER_OTHER = "O:";

    private static final String MARKER_STACK = "T:";

    private final Path jeiConfigurationDir;

    private static Optional<Path> getPath(Path jeiConfigurationDir) {
        return ServerConfigPathUtil.getWorldPath(jeiConfigurationDir).flatMap(configPath -> {
            try {
                configPath = Files.createDirectories(configPath);
            } catch (IOException var2) {
                LOGGER.error("Unable to create bookmark config folder: {}", configPath);
                return Optional.empty();
            }
            Path path = configPath.resolve("bookmarks.ini");
            return Optional.of(path);
        });
    }

    public BookmarkConfig(Path jeiConfigurationDir) {
        this.jeiConfigurationDir = jeiConfigurationDir;
    }

    @Override
    public void saveBookmarks(IIngredientManager ingredientManager, List<ITypedIngredient<?>> ingredientList) {
        getPath(this.jeiConfigurationDir).ifPresent(path -> {
            List<String> strings = new ArrayList();
            for (ITypedIngredient<?> typedIngredient : ingredientList) {
                if (typedIngredient.getIngredient() instanceof ItemStack stack) {
                    strings.add("T:" + stack.save(new CompoundTag()));
                } else {
                    strings.add("O:" + getUid(ingredientManager, typedIngredient));
                }
            }
            try {
                Files.write(path, strings);
            } catch (IOException var8) {
                LOGGER.error("Failed to save bookmarks list to file {}", path, var8);
            }
        });
    }

    @Override
    public void loadBookmarks(IIngredientManager ingredientManager, BookmarkList bookmarkList) {
        getPath(this.jeiConfigurationDir).ifPresent(path -> {
            if (Files.exists(path, new LinkOption[0])) {
                List<String> ingredientJsonStrings;
                try {
                    ingredientJsonStrings = Files.readAllLines(path);
                } catch (IOException var14) {
                    LOGGER.error("Failed to load bookmarks from file {}", path, var14);
                    return;
                }
                Collection<IIngredientType<?>> otherIngredientTypes = ingredientManager.getRegisteredIngredientTypes().stream().filter(i -> !i.equals(VanillaTypes.ITEM_STACK)).toList();
                IIngredientHelper<ItemStack> itemStackHelper = ingredientManager.getIngredientHelper(VanillaTypes.ITEM_STACK);
                for (String ingredientJsonString : ingredientJsonStrings) {
                    if (ingredientJsonString.startsWith("T:")) {
                        String itemStackAsJson = ingredientJsonString.substring("T:".length());
                        try {
                            CompoundTag itemStackAsNbt = TagParser.parseTag(itemStackAsJson);
                            ItemStack itemStack = ItemStack.of(itemStackAsNbt);
                            if (!itemStack.isEmpty()) {
                                ItemStack normalized = itemStackHelper.normalizeIngredient(itemStack);
                                Optional<ITypedIngredient<ItemStack>> typedIngredient = ingredientManager.createTypedIngredient(VanillaTypes.ITEM_STACK, normalized);
                                if (typedIngredient.isEmpty()) {
                                    LOGGER.warn("Failed to load bookmarked ItemStack from json string, the item no longer exists:\n{}", itemStackAsJson);
                                } else {
                                    bookmarkList.addToList((ITypedIngredient) typedIngredient.get(), false);
                                }
                            } else {
                                LOGGER.warn("Failed to load bookmarked ItemStack from json string, the item no longer exists:\n{}", itemStackAsJson);
                            }
                        } catch (CommandSyntaxException var13) {
                            LOGGER.error("Failed to load bookmarked ItemStack from json string:\n{}", itemStackAsJson, var13);
                        }
                    } else if (ingredientJsonString.startsWith("O:")) {
                        String uid = ingredientJsonString.substring("O:".length());
                        Optional<ITypedIngredient<?>> typedIngredient = getNormalizedIngredientByUid(ingredientManager, otherIngredientTypes, uid);
                        if (typedIngredient.isEmpty()) {
                            LOGGER.error("Failed to load unknown bookmarked ingredient:\n{}", ingredientJsonString);
                        } else {
                            bookmarkList.addToList((ITypedIngredient) typedIngredient.get(), false);
                        }
                    } else {
                        LOGGER.error("Failed to load unknown bookmarked ingredient:\n{}", ingredientJsonString);
                    }
                }
                bookmarkList.notifyListenersOfChange();
            }
        });
    }

    private static <T> String getUid(IIngredientManager ingredientManager, ITypedIngredient<T> typedIngredient) {
        IIngredientHelper<T> ingredientHelper = ingredientManager.getIngredientHelper(typedIngredient.getType());
        return ingredientHelper.getUniqueId(typedIngredient.getIngredient(), UidContext.Ingredient);
    }

    private static Optional<ITypedIngredient<?>> getNormalizedIngredientByUid(IIngredientManager ingredientManager, Collection<IIngredientType<?>> ingredientTypes, String uid) {
        return ingredientTypes.stream().map(t -> getNormalizedIngredientByUid(ingredientManager, t, uid)).flatMap(Optional::stream).findFirst();
    }

    private static <T> Optional<ITypedIngredient<?>> getNormalizedIngredientByUid(IIngredientManager ingredientManager, IIngredientType<T> ingredientType, String uid) {
        return ingredientManager.getIngredientByUid(ingredientType, uid).map(i -> {
            IIngredientHelper<T> ingredientHelper = ingredientManager.getIngredientHelper(ingredientType);
            return ingredientHelper.normalizeIngredient((T) i);
        }).flatMap(i -> ingredientManager.createTypedIngredient(ingredientType, (T) i));
    }
}