package org.embeddedt.modernfix.common.mixin.perf.dynamic_resources;

import com.google.common.collect.UnmodifiableIterator;
import java.util.Map;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.embeddedt.modernfix.duck.IModelHoldingBlockState;
import org.embeddedt.modernfix.dynamicresources.ModelLocationCache;
import org.embeddedt.modernfix.util.DynamicOverridableMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ BlockModelShaper.class })
@ClientOnlyMixin
public class BlockModelShaperMixin {

    @Shadow
    @Final
    private ModelManager modelManager;

    @Shadow
    private Map<BlockState, BakedModel> modelByStateCache;

    @Inject(method = { "<init>", "replaceCache" }, at = { @At("RETURN") })
    private void replaceModelMap(CallbackInfo ci) {
        this.modelByStateCache = new DynamicOverridableMap<>(statex -> this.modelManager.getModel(ModelLocationCache.get(statex)));
        for (Block block : BuiltInRegistries.BLOCK) {
            UnmodifiableIterator var4 = block.getStateDefinition().getPossibleStates().iterator();
            while (var4.hasNext()) {
                BlockState state = (BlockState) var4.next();
                if (state instanceof IModelHoldingBlockState modelHolder) {
                    modelHolder.mfix$setModel(null);
                }
            }
        }
    }

    private BakedModel cacheBlockModel(BlockState state) {
        ModelResourceLocation mrl = ModelLocationCache.get(state);
        BakedModel model = mrl == null ? null : this.modelManager.getModel(mrl);
        if (model == null) {
            model = this.modelManager.getMissingModel();
        }
        return model;
    }

    @Overwrite
    public BakedModel getBlockModel(BlockState state) {
        if (state instanceof IModelHoldingBlockState modelHolder) {
            BakedModel model = modelHolder.mfix$getModel();
            if (model != null) {
                return model;
            } else {
                model = this.cacheBlockModel(state);
                modelHolder.mfix$setModel(model);
                return model;
            }
        } else {
            return this.cacheBlockModel(state);
        }
    }
}