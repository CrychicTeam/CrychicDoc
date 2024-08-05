package fuzs.puzzleslib.api.client.init.v1;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface DynamicBuiltinItemRenderer extends ResourceManagerReloadListener {

    void renderByItem(ItemStack var1, ItemDisplayContext var2, PoseStack var3, MultiBufferSource var4, int var5, int var6);

    @Override
    default void onResourceManagerReload(ResourceManager resourceManager) {
    }
}