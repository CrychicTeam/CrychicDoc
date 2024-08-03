package fuzs.puzzleslib.impl.client.core.context;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.puzzleslib.api.client.core.v1.context.BuiltinModelItemRendererContext;
import fuzs.puzzleslib.api.client.init.v1.DynamicBuiltinItemRenderer;
import fuzs.puzzleslib.api.core.v1.resources.ForwardingReloadListenerHelper;
import fuzs.puzzleslib.impl.client.core.ForwardingClientItemExtensions;
import fuzs.puzzleslib.mixin.client.accessor.ItemForgeAccessor;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.Nullable;

public record BuiltinModelItemRendererContextForgeImpl(String modId, List<ResourceManagerReloadListener> dynamicRenderers) implements BuiltinModelItemRendererContext {

    @Override
    public void registerItemRenderer(final DynamicBuiltinItemRenderer renderer, ItemLike... items) {
        if (!FMLLoader.getLaunchHandler().isData()) {
            Objects.requireNonNull(renderer, "renderer is null");
            Objects.requireNonNull(items, "items is null");
            Preconditions.checkPositionIndex(1, items.length, "items is empty");
            IClientItemExtensions itemExtensions = new IClientItemExtensions() {

                @Nullable
                private BlockEntityWithoutLevelRenderer blockEntityWithoutLevelRenderer;

                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    if (this.blockEntityWithoutLevelRenderer == null) {
                        this.blockEntityWithoutLevelRenderer = new BuiltinModelItemRendererContextForgeImpl.ForwardingBlockEntityWithoutLevelRenderer(Minecraft.getInstance(), renderer);
                    }
                    return this.blockEntityWithoutLevelRenderer;
                }
            };
            for (ItemLike item : items) {
                Objects.requireNonNull(item, "item is null");
                setClientItemExtensions(item.asItem(), itemExtensions);
            }
            String itemName = BuiltInRegistries.ITEM.getKey(items[0].asItem()).getPath();
            ResourceLocation identifier = new ResourceLocation(this.modId, itemName + "_built_in_model_renderer");
            this.dynamicRenderers.add(ForwardingReloadListenerHelper.fromResourceManagerReloadListener(identifier, renderer));
        }
    }

    private static void setClientItemExtensions(Item item, final IClientItemExtensions itemExtensions) {
        Object renderProperties = ((ItemForgeAccessor) item).puzzleslib$getRenderProperties();
        ((ItemForgeAccessor) item).puzzleslib$setRenderProperties(renderProperties != null ? new ForwardingClientItemExtensions((IClientItemExtensions) renderProperties) {

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return itemExtensions.getCustomRenderer();
            }
        } : itemExtensions);
    }

    private static class ForwardingBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {

        private final DynamicBuiltinItemRenderer renderer;

        public ForwardingBlockEntityWithoutLevelRenderer(Minecraft minecraft, DynamicBuiltinItemRenderer renderer) {
            super(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
            this.renderer = renderer;
        }

        @Override
        public void renderByItem(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
            this.renderer.renderByItem(stack, mode, matrices, vertexConsumers, light, overlay);
        }
    }
}