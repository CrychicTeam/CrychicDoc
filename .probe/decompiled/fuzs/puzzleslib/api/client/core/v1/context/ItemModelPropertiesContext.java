package fuzs.puzzleslib.api.client.core.v1.context;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public interface ItemModelPropertiesContext {

    void registerGlobalProperty(ResourceLocation var1, ClampedItemPropertyFunction var2);

    void registerItemProperty(ResourceLocation var1, ClampedItemPropertyFunction var2, ItemLike... var3);
}