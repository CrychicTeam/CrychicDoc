package net.minecraft.client.renderer.block;

import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class BlockModelShaper {

    private Map<BlockState, BakedModel> modelByStateCache = Map.of();

    private final ModelManager modelManager;

    public BlockModelShaper(ModelManager modelManager0) {
        this.modelManager = modelManager0;
    }

    public TextureAtlasSprite getParticleIcon(BlockState blockState0) {
        return this.getBlockModel(blockState0).getParticleIcon();
    }

    public BakedModel getBlockModel(BlockState blockState0) {
        BakedModel $$1 = (BakedModel) this.modelByStateCache.get(blockState0);
        if ($$1 == null) {
            $$1 = this.modelManager.getMissingModel();
        }
        return $$1;
    }

    public ModelManager getModelManager() {
        return this.modelManager;
    }

    public void replaceCache(Map<BlockState, BakedModel> mapBlockStateBakedModel0) {
        this.modelByStateCache = mapBlockStateBakedModel0;
    }

    public static ModelResourceLocation stateToModelLocation(BlockState blockState0) {
        return stateToModelLocation(BuiltInRegistries.BLOCK.getKey(blockState0.m_60734_()), blockState0);
    }

    public static ModelResourceLocation stateToModelLocation(ResourceLocation resourceLocation0, BlockState blockState1) {
        return new ModelResourceLocation(resourceLocation0, statePropertiesToString(blockState1.m_61148_()));
    }

    public static String statePropertiesToString(Map<Property<?>, Comparable<?>> mapPropertyComparable0) {
        StringBuilder $$1 = new StringBuilder();
        for (Entry<Property<?>, Comparable<?>> $$2 : mapPropertyComparable0.entrySet()) {
            if ($$1.length() != 0) {
                $$1.append(',');
            }
            Property<?> $$3 = (Property<?>) $$2.getKey();
            $$1.append($$3.getName());
            $$1.append('=');
            $$1.append(getValue($$3, (Comparable<?>) $$2.getValue()));
        }
        return $$1.toString();
    }

    private static <T extends Comparable<T>> String getValue(Property<T> propertyT0, Comparable<?> comparable1) {
        return propertyT0.getName((T) comparable1);
    }
}