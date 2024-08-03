package org.embeddedt.modernfix.common.mixin.perf.dynamic_resources;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.embeddedt.modernfix.dynamicresources.DynamicModelCache;
import org.embeddedt.modernfix.dynamicresources.ModelLocationCache;
import org.embeddedt.modernfix.util.DynamicInt2ObjectMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ItemModelShaper.class })
@ClientOnlyMixin
public abstract class ItemModelShaperMixin {

    @Shadow
    @Final
    @Mutable
    private Int2ObjectMap<BakedModel> shapesCache;

    private Map<Item, ModelResourceLocation> overrideLocationsVanilla;

    private static final ModelResourceLocation SENTINEL_VANILLA = new ModelResourceLocation(new ResourceLocation("modernfix", "sentinel"), "sentinel");

    private final DynamicModelCache<Item> mfix$itemModelCache = new DynamicModelCache<>(k -> this.mfix$getModelForItem((Item) k), true);

    @Shadow
    public abstract ModelManager getModelManager();

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void replaceLocationMap(CallbackInfo ci) {
        this.overrideLocationsVanilla = new HashMap();
        this.shapesCache = new DynamicInt2ObjectMap<>(index -> this.getModelManager().getModel(ModelLocationCache.get(Item.byId(index))));
    }

    @Unique
    private ModelResourceLocation mfix$getLocation(Item item) {
        ModelResourceLocation map = (ModelResourceLocation) this.overrideLocationsVanilla.getOrDefault(item, SENTINEL_VANILLA);
        if (map == SENTINEL_VANILLA) {
            map = ModelLocationCache.get(item);
        }
        return map;
    }

    private BakedModel mfix$getModelForItem(Item item) {
        ModelResourceLocation map = this.mfix$getLocation(item);
        return map == null ? null : this.getModelManager().getModel(map);
    }

    @Overwrite
    public BakedModel getItemModel(Item item) {
        return this.mfix$itemModelCache.get(item);
    }

    @Overwrite
    public void register(Item item, ModelResourceLocation location) {
        this.overrideLocationsVanilla.put(item, location);
    }

    @Overwrite
    public void rebuildCache() {
        this.mfix$itemModelCache.clear();
    }
}