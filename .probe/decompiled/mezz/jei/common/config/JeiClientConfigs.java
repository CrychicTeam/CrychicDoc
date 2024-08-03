package mezz.jei.common.config;

import java.nio.file.Path;
import mezz.jei.common.config.file.ConfigSchemaBuilder;
import mezz.jei.common.config.file.FileWatcher;
import mezz.jei.common.config.file.IConfigSchema;
import mezz.jei.common.config.file.IConfigSchemaBuilder;
import mezz.jei.common.util.HorizontalAlignment;

public class JeiClientConfigs implements IJeiClientConfigs {

    private final IClientConfig clientConfig;

    private final IIngredientFilterConfig ingredientFilterConfig;

    private final IIngredientGridConfig ingredientListConfig;

    private final IIngredientGridConfig bookmarkListConfig;

    private final IConfigSchema schema;

    public JeiClientConfigs(Path configFile) {
        IConfigSchemaBuilder builder = new ConfigSchemaBuilder(configFile);
        this.clientConfig = new ClientConfig(builder);
        this.ingredientFilterConfig = new IngredientFilterConfig(builder);
        this.ingredientListConfig = new IngredientGridConfig("IngredientList", builder, HorizontalAlignment.RIGHT);
        this.bookmarkListConfig = new IngredientGridConfig("BookmarkList", builder, HorizontalAlignment.LEFT);
        this.schema = builder.build();
    }

    public void register(FileWatcher fileWatcher, ConfigManager configManager) {
        this.schema.register(fileWatcher, configManager);
    }

    @Override
    public IClientConfig getClientConfig() {
        return this.clientConfig;
    }

    @Override
    public IIngredientFilterConfig getIngredientFilterConfig() {
        return this.ingredientFilterConfig;
    }

    @Override
    public IIngredientGridConfig getIngredientListConfig() {
        return this.ingredientListConfig;
    }

    @Override
    public IIngredientGridConfig getBookmarkListConfig() {
        return this.bookmarkListConfig;
    }
}