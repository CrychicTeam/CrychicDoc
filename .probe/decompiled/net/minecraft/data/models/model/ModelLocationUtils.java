package net.minecraft.data.models.model;

import java.util.function.UnaryOperator;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModelLocationUtils {

    @Deprecated
    public static ResourceLocation decorateBlockModelLocation(String string0) {
        return new ResourceLocation("minecraft", "block/" + string0);
    }

    public static ResourceLocation decorateItemModelLocation(String string0) {
        return new ResourceLocation("minecraft", "item/" + string0);
    }

    public static ResourceLocation getModelLocation(Block block0, String string1) {
        ResourceLocation $$2 = BuiltInRegistries.BLOCK.getKey(block0);
        return $$2.withPath((UnaryOperator<String>) (p_251253_ -> "block/" + p_251253_ + string1));
    }

    public static ResourceLocation getModelLocation(Block block0) {
        ResourceLocation $$1 = BuiltInRegistries.BLOCK.getKey(block0);
        return $$1.withPrefix("block/");
    }

    public static ResourceLocation getModelLocation(Item item0) {
        ResourceLocation $$1 = BuiltInRegistries.ITEM.getKey(item0);
        return $$1.withPrefix("item/");
    }

    public static ResourceLocation getModelLocation(Item item0, String string1) {
        ResourceLocation $$2 = BuiltInRegistries.ITEM.getKey(item0);
        return $$2.withPath((UnaryOperator<String>) (p_251542_ -> "item/" + p_251542_ + string1));
    }
}