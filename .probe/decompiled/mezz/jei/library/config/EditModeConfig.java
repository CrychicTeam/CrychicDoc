package mezz.jei.library.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.runtime.IEditModeConfig;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.core.util.WeakList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EditModeConfig implements IEditModeConfig {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String[] defaultBlacklist = new String[0];

    private final Set<String> blacklist = new LinkedHashSet();

    private final EditModeConfig.ISerializer serializer;

    private final IIngredientManager ingredientManager;

    private final WeakList<EditModeConfig.IListener> listeners = new WeakList<>();

    public EditModeConfig(EditModeConfig.ISerializer serializer, IIngredientManager ingredientManager) {
        this.ingredientManager = ingredientManager;
        Collections.addAll(this.blacklist, defaultBlacklist);
        this.serializer = serializer;
        this.serializer.initialize(this);
        this.serializer.load(this);
    }

    public <V> void addIngredientToConfigBlacklist(ITypedIngredient<V> typedIngredient, IEditModeConfig.HideMode blacklistType, IIngredientHelper<V> ingredientHelper) {
        if (this.addIngredientToConfigBlacklistInternal(typedIngredient, blacklistType, ingredientHelper)) {
            this.serializer.save(this);
            this.notifyListenersOfVisibilityChange(typedIngredient, false);
        }
    }

    private <V> boolean addIngredientToConfigBlacklistInternal(ITypedIngredient<V> typedIngredient, IEditModeConfig.HideMode blacklistType, IIngredientHelper<V> ingredientHelper) {
        String wildcardUid = getIngredientUid(typedIngredient, IEditModeConfig.HideMode.WILDCARD, ingredientHelper);
        if (blacklistType == IEditModeConfig.HideMode.SINGLE) {
            String uid = getIngredientUid(typedIngredient, blacklistType, ingredientHelper);
            return wildcardUid.equals(uid) ? this.blacklist.add(wildcardUid) : this.blacklist.add(uid);
        } else {
            return blacklistType == IEditModeConfig.HideMode.WILDCARD ? this.blacklist.add(wildcardUid) : false;
        }
    }

    public <V> void removeIngredientFromConfigBlacklist(ITypedIngredient<V> typedIngredient, IEditModeConfig.HideMode blacklistType, IIngredientHelper<V> ingredientHelper) {
        String uid = getIngredientUid(typedIngredient, blacklistType, ingredientHelper);
        if (this.blacklist.remove(uid)) {
            this.serializer.save(this);
            this.notifyListenersOfVisibilityChange(typedIngredient, true);
        }
    }

    public <V> boolean isIngredientOnConfigBlacklist(ITypedIngredient<V> typedIngredient, IIngredientHelper<V> ingredientHelper) {
        for (IEditModeConfig.HideMode hideMode : IEditModeConfig.HideMode.values()) {
            if (this.isIngredientOnConfigBlacklist(typedIngredient, hideMode, ingredientHelper)) {
                return true;
            }
        }
        return false;
    }

    private <V> Set<IEditModeConfig.HideMode> getIngredientOnConfigBlacklist(ITypedIngredient<V> ingredient, IIngredientHelper<V> ingredientHelper) {
        return (Set<IEditModeConfig.HideMode>) Arrays.stream(IEditModeConfig.HideMode.values()).filter(hideMode -> this.isIngredientOnConfigBlacklist(ingredient, hideMode, ingredientHelper)).collect(Collectors.toUnmodifiableSet());
    }

    public <V> boolean isIngredientOnConfigBlacklist(ITypedIngredient<V> typedIngredient, IEditModeConfig.HideMode blacklistType, IIngredientHelper<V> ingredientHelper) {
        String uid = getIngredientUid(typedIngredient, blacklistType, ingredientHelper);
        return this.blacklist.contains(uid);
    }

    private static <V> String getIngredientUid(ITypedIngredient<V> typedIngredient, IEditModeConfig.HideMode blacklistType, IIngredientHelper<V> ingredientHelper) {
        V ingredient = typedIngredient.getIngredient();
        return switch(blacklistType) {
            case SINGLE ->
                ingredientHelper.getUniqueId(ingredient, UidContext.Ingredient);
            case WILDCARD ->
                ingredientHelper.getWildcardId(ingredient);
        };
    }

    @Override
    public <V> boolean isIngredientHiddenUsingConfigFile(ITypedIngredient<V> ingredient) {
        IIngredientType<V> type = ingredient.getType();
        IIngredientHelper<V> ingredientHelper = this.ingredientManager.getIngredientHelper(type);
        return this.isIngredientOnConfigBlacklist(ingredient, ingredientHelper);
    }

    @Override
    public <V> Set<IEditModeConfig.HideMode> getIngredientHiddenUsingConfigFile(ITypedIngredient<V> ingredient) {
        IIngredientType<V> type = ingredient.getType();
        IIngredientHelper<V> ingredientHelper = this.ingredientManager.getIngredientHelper(type);
        return this.getIngredientOnConfigBlacklist(ingredient, ingredientHelper);
    }

    @Override
    public <V> void hideIngredientUsingConfigFile(ITypedIngredient<V> ingredient, IEditModeConfig.HideMode hideMode) {
        IIngredientType<V> type = ingredient.getType();
        IIngredientHelper<V> ingredientHelper = this.ingredientManager.getIngredientHelper(type);
        this.addIngredientToConfigBlacklist(ingredient, hideMode, ingredientHelper);
    }

    @Override
    public <V> void showIngredientUsingConfigFile(ITypedIngredient<V> ingredient, IEditModeConfig.HideMode hideMode) {
        IIngredientType<V> type = ingredient.getType();
        IIngredientHelper<V> ingredientHelper = this.ingredientManager.getIngredientHelper(type);
        this.removeIngredientFromConfigBlacklist(ingredient, hideMode, ingredientHelper);
    }

    public void registerListener(EditModeConfig.IListener listener) {
        this.listeners.add(listener);
    }

    private <T> void notifyListenersOfVisibilityChange(ITypedIngredient<T> ingredient, boolean visible) {
        this.listeners.forEach(listener -> listener.onIngredientVisibilityChanged(ingredient, visible));
    }

    public static class FileSerializer implements EditModeConfig.ISerializer {

        private final Path path;

        public FileSerializer(Path path) {
            this.path = path;
        }

        @Override
        public void initialize(EditModeConfig config) {
            if (!Files.exists(this.path, new LinkOption[0])) {
                this.save(config);
            }
        }

        @Override
        public void save(EditModeConfig config) {
            try {
                Files.write(this.path, config.blacklist);
                EditModeConfig.LOGGER.debug("Saved blacklist config to file: {}", this.path);
            } catch (IOException var3) {
                EditModeConfig.LOGGER.error("Failed to save blacklist config to file {}", this.path, var3);
            }
        }

        @Override
        public void load(EditModeConfig config) {
            if (Files.exists(this.path, new LinkOption[0])) {
                try {
                    List<String> strings = Files.readAllLines(this.path);
                    config.blacklist.clear();
                    config.blacklist.addAll(strings);
                } catch (IOException var3) {
                    EditModeConfig.LOGGER.error("Failed to load blacklist from file {}", this.path, var3);
                }
            }
        }
    }

    public interface IListener {

        <V> void onIngredientVisibilityChanged(ITypedIngredient<V> var1, boolean var2);
    }

    public interface ISerializer {

        void initialize(EditModeConfig var1);

        void save(EditModeConfig var1);

        void load(EditModeConfig var1);
    }
}