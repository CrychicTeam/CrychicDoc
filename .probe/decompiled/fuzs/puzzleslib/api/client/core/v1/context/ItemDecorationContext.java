package fuzs.puzzleslib.api.client.core.v1.context;

import fuzs.puzzleslib.api.client.init.v1.DynamicItemDecorator;
import net.minecraft.world.level.ItemLike;

@FunctionalInterface
public interface ItemDecorationContext {

    void registerItemDecorator(DynamicItemDecorator var1, ItemLike... var2);
}