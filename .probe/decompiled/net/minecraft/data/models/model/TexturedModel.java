package net.minecraft.data.models.model;

import com.google.gson.JsonElement;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class TexturedModel {

    public static final TexturedModel.Provider CUBE = createDefault(TextureMapping::m_125748_, ModelTemplates.CUBE_ALL);

    public static final TexturedModel.Provider CUBE_MIRRORED = createDefault(TextureMapping::m_125748_, ModelTemplates.CUBE_MIRRORED_ALL);

    public static final TexturedModel.Provider COLUMN = createDefault(TextureMapping::m_125818_, ModelTemplates.CUBE_COLUMN);

    public static final TexturedModel.Provider COLUMN_HORIZONTAL = createDefault(TextureMapping::m_125818_, ModelTemplates.CUBE_COLUMN_HORIZONTAL);

    public static final TexturedModel.Provider CUBE_TOP_BOTTOM = createDefault(TextureMapping::m_125826_, ModelTemplates.CUBE_BOTTOM_TOP);

    public static final TexturedModel.Provider CUBE_TOP = createDefault(TextureMapping::m_125822_, ModelTemplates.CUBE_TOP);

    public static final TexturedModel.Provider ORIENTABLE_ONLY_TOP = createDefault(TextureMapping::m_125848_, ModelTemplates.CUBE_ORIENTABLE);

    public static final TexturedModel.Provider ORIENTABLE = createDefault(TextureMapping::m_125846_, ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM);

    public static final TexturedModel.Provider CARPET = createDefault(TextureMapping::m_125804_, ModelTemplates.CARPET);

    public static final TexturedModel.Provider FLOWERBED_1 = createDefault(TextureMapping::m_272143_, ModelTemplates.FLOWERBED_1);

    public static final TexturedModel.Provider FLOWERBED_2 = createDefault(TextureMapping::m_272143_, ModelTemplates.FLOWERBED_2);

    public static final TexturedModel.Provider FLOWERBED_3 = createDefault(TextureMapping::m_272143_, ModelTemplates.FLOWERBED_3);

    public static final TexturedModel.Provider FLOWERBED_4 = createDefault(TextureMapping::m_272143_, ModelTemplates.FLOWERBED_4);

    public static final TexturedModel.Provider GLAZED_TERRACOTTA = createDefault(TextureMapping::m_125810_, ModelTemplates.GLAZED_TERRACOTTA);

    public static final TexturedModel.Provider CORAL_FAN = createDefault(TextureMapping::m_125814_, ModelTemplates.CORAL_FAN);

    public static final TexturedModel.Provider PARTICLE_ONLY = createDefault(TextureMapping::m_125834_, ModelTemplates.PARTICLE_ONLY);

    public static final TexturedModel.Provider ANVIL = createDefault(TextureMapping::m_125852_, ModelTemplates.ANVIL);

    public static final TexturedModel.Provider LEAVES = createDefault(TextureMapping::m_125748_, ModelTemplates.LEAVES);

    public static final TexturedModel.Provider LANTERN = createDefault(TextureMapping::m_125840_, ModelTemplates.LANTERN);

    public static final TexturedModel.Provider HANGING_LANTERN = createDefault(TextureMapping::m_125840_, ModelTemplates.HANGING_LANTERN);

    public static final TexturedModel.Provider SEAGRASS = createDefault(TextureMapping::m_125768_, ModelTemplates.SEAGRASS);

    public static final TexturedModel.Provider COLUMN_ALT = createDefault(TextureMapping::m_125824_, ModelTemplates.CUBE_COLUMN);

    public static final TexturedModel.Provider COLUMN_HORIZONTAL_ALT = createDefault(TextureMapping::m_125824_, ModelTemplates.CUBE_COLUMN_HORIZONTAL);

    public static final TexturedModel.Provider TOP_BOTTOM_WITH_WALL = createDefault(TextureMapping::m_125828_, ModelTemplates.CUBE_BOTTOM_TOP);

    public static final TexturedModel.Provider COLUMN_WITH_WALL = createDefault(TextureMapping::m_125830_, ModelTemplates.CUBE_COLUMN);

    private final TextureMapping mapping;

    private final ModelTemplate template;

    private TexturedModel(TextureMapping textureMapping0, ModelTemplate modelTemplate1) {
        this.mapping = textureMapping0;
        this.template = modelTemplate1;
    }

    public ModelTemplate getTemplate() {
        return this.template;
    }

    public TextureMapping getMapping() {
        return this.mapping;
    }

    public TexturedModel updateTextures(Consumer<TextureMapping> consumerTextureMapping0) {
        consumerTextureMapping0.accept(this.mapping);
        return this;
    }

    public ResourceLocation create(Block block0, BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumerResourceLocationSupplierJsonElement1) {
        return this.template.create(block0, this.mapping, biConsumerResourceLocationSupplierJsonElement1);
    }

    public ResourceLocation createWithSuffix(Block block0, String string1, BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumerResourceLocationSupplierJsonElement2) {
        return this.template.createWithSuffix(block0, string1, this.mapping, biConsumerResourceLocationSupplierJsonElement2);
    }

    private static TexturedModel.Provider createDefault(Function<Block, TextureMapping> functionBlockTextureMapping0, ModelTemplate modelTemplate1) {
        return p_125948_ -> new TexturedModel((TextureMapping) functionBlockTextureMapping0.apply(p_125948_), modelTemplate1);
    }

    public static TexturedModel createAllSame(ResourceLocation resourceLocation0) {
        return new TexturedModel(TextureMapping.cube(resourceLocation0), ModelTemplates.CUBE_ALL);
    }

    @FunctionalInterface
    public interface Provider {

        TexturedModel get(Block var1);

        default ResourceLocation create(Block block0, BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumerResourceLocationSupplierJsonElement1) {
            return this.get(block0).create(block0, biConsumerResourceLocationSupplierJsonElement1);
        }

        default ResourceLocation createWithSuffix(Block block0, String string1, BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumerResourceLocationSupplierJsonElement2) {
            return this.get(block0).createWithSuffix(block0, string1, biConsumerResourceLocationSupplierJsonElement2);
        }

        default TexturedModel.Provider updateTexture(Consumer<TextureMapping> consumerTextureMapping0) {
            return p_125963_ -> this.get(p_125963_).updateTextures(consumerTextureMapping0);
        }
    }
}