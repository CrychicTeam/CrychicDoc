package fuzs.puzzleslib.impl.client.init;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.puzzleslib.api.core.v1.ModContainerHelper;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.BakedModelWrapper;

public final class ForgeItemDisplayOverrides extends ItemDisplayOverridesImpl {

    public ForgeItemDisplayOverrides() {
        ModContainerHelper.getOptionalModEventBus("puzzleslib").ifPresent(eventBus -> eventBus.addListener(evt -> {
            final Map<ResourceLocation, BakedModel> models = evt.getModels();
            for (final Entry<ModelResourceLocation, Map<ItemDisplayContext, ModelResourceLocation>> entry : this.overrideLocations.entrySet()) {
                final BakedModel itemModel = (BakedModel) models.get(entry.getKey());
                Objects.requireNonNull(itemModel, "item model is null");
                models.put((ResourceLocation) entry.getKey(), new BakedModelWrapper<BakedModel>(itemModel) {

                    private final Map<ItemDisplayContext, BakedModel> overrides = (Map<ItemDisplayContext, BakedModel>) Stream.of(ItemDisplayContext.values()).collect(Maps.toImmutableEnumMap(Function.identity(), context -> {
                        if (((Map) entry.getValue()).containsKey(context)) {
                            BakedModel itemModelOverride = (BakedModel) models.get(((Map) entry.getValue()).get(context));
                            Objects.requireNonNull(itemModelOverride, "item model override is null");
                            return itemModelOverride;
                        } else {
                            return itemModel;
                        }
                    }));

                    @Override
                    public BakedModel applyTransform(ItemDisplayContext itemDisplayContext, PoseStack poseStack, boolean applyLeftHandTransform) {
                        return ((BakedModel) this.overrides.get(itemDisplayContext)).applyTransform(itemDisplayContext, poseStack, applyLeftHandTransform);
                    }
                });
            }
        }));
    }
}