package fuzs.puzzleslib.api.client.core.v1.context;

import fuzs.puzzleslib.api.client.init.v1.DynamicBuiltinItemRenderer;
import net.minecraft.world.level.ItemLike;

@FunctionalInterface
public interface BuiltinModelItemRendererContext {

    void registerItemRenderer(DynamicBuiltinItemRenderer var1, ItemLike... var2);
}