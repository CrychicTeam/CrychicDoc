package fuzs.puzzleslib.api.client.init.v1;

import fuzs.puzzleslib.impl.client.core.ClientFactories;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

public interface ItemModelDisplayOverrides {

    ItemModelDisplayOverrides INSTANCE = ClientFactories.INSTANCE.getItemModelDisplayOverrides();

    void register(ModelResourceLocation var1, ModelResourceLocation var2, ItemDisplayContext... var3);
}