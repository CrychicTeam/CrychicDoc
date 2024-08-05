package fuzs.puzzleslib.api.client.init.v1;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;

@Deprecated(forRemoval = true)
public interface ItemModelOverrides {

    ItemModelOverrides INSTANCE = (item, itemModel, itemModelOverride, defaultContexts) -> ItemModelDisplayOverrides.INSTANCE.register(itemModel, itemModelOverride, defaultContexts);

    void register(Item var1, ModelResourceLocation var2, ModelResourceLocation var3, ItemDisplayContext... var4);
}