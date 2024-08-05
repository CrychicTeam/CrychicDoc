package org.embeddedt.modernfix.forge.mixin.perf.dynamic_resources;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.function.Function;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.ModernFixClient;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.embeddedt.modernfix.api.entrypoint.ModernFixClientIntegration;
import org.embeddedt.modernfix.duck.IExtendedModelBaker;
import org.embeddedt.modernfix.duck.IExtendedModelBakery;
import org.embeddedt.modernfix.dynamicresources.ModelMissingException;
import org.embeddedt.modernfix.forge.dynresources.IModelBakerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { ModelBakery.ModelBakerImpl.class }, priority = 600)
@ClientOnlyMixin
public abstract class ModelBakerImplMixin implements IModelBakerImpl, IExtendedModelBaker {

    private static final boolean debugDynamicModelLoading = Boolean.getBoolean("modernfix.debugDynamicModelLoading");

    @Shadow
    @Final
    private ModelBakery f_243927_;

    private boolean mfix$ignoreCache = false;

    @Shadow
    @Final
    private Function<Material, TextureAtlasSprite> modelTextureGetter;

    private boolean throwIfMissing;

    @Override
    public void mfix$ignoreCache() {
        this.mfix$ignoreCache = true;
    }

    @Override
    public boolean throwOnMissingModel(boolean flag) {
        boolean old = this.throwIfMissing;
        this.throwIfMissing = flag;
        return old;
    }

    @Inject(method = { "getModel" }, at = { @At("HEAD") }, cancellable = true)
    private void obtainModel(ResourceLocation arg, CallbackInfoReturnable<UnbakedModel> cir) {
        if (debugDynamicModelLoading) {
            ModernFix.LOGGER.info("Baking {}", arg);
        }
        IExtendedModelBakery extendedBakery = (IExtendedModelBakery) this.f_243927_;
        if (arg instanceof ModelResourceLocation && arg != ModelBakery.MISSING_MODEL_LOCATION) {
            synchronized (this.f_243927_) {
                this.f_243927_.loadTopLevel((ModelResourceLocation) arg);
                cir.setReturnValue((UnbakedModel) this.f_243927_.topLevelModels.getOrDefault(arg, extendedBakery.mfix$getUnbakedMissingModel()));
                this.f_243927_.topLevelModels.clear();
            }
        } else {
            cir.setReturnValue(this.f_243927_.getModel(arg));
        }
        UnbakedModel toReplace = (UnbakedModel) cir.getReturnValue();
        for (ModernFixClientIntegration integration : ModernFixClient.CLIENT_INTEGRATIONS) {
            try {
                toReplace = integration.onUnbakedModelPreBake(arg, toReplace, this.f_243927_);
            } catch (RuntimeException var8) {
                ModernFix.LOGGER.error("Exception firing model pre-bake event for {}", arg, var8);
            }
        }
        cir.setReturnValue(toReplace);
        ((UnbakedModel) cir.getReturnValue()).resolveParents(this.f_243927_::m_119341_);
        if (cir.getReturnValue() == extendedBakery.mfix$getUnbakedMissingModel() && arg != ModelBakery.MISSING_MODEL_LOCATION) {
            if (debugDynamicModelLoading) {
                ModernFix.LOGGER.warn("Model {} not present", arg);
            }
            if (this.throwIfMissing) {
                throw new ModelMissingException();
            }
        }
    }

    @ModifyExpressionValue(method = { "bake(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/resources/model/ModelState;Ljava/util/function/Function;)Lnet/minecraft/client/resources/model/BakedModel;" }, at = { @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0) }, remap = false)
    private Object ignoreCacheIfRequested(Object o) {
        return this.mfix$ignoreCache ? null : o;
    }

    @WrapOperation(method = { "bake(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/resources/model/ModelState;Ljava/util/function/Function;)Lnet/minecraft/client/resources/model/BakedModel;" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/UnbakedModel;bake(Lnet/minecraft/client/resources/model/ModelBaker;Ljava/util/function/Function;Lnet/minecraft/client/resources/model/ModelState;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/resources/model/BakedModel;") })
    private BakedModel callBakedModelIntegration(UnbakedModel unbakedModel, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState state, ResourceLocation location, Operation<BakedModel> operation) {
        BakedModel model = (BakedModel) operation.call(new Object[] { unbakedModel, baker, spriteGetter, state, location });
        for (ModernFixClientIntegration integration : ModernFixClient.CLIENT_INTEGRATIONS) {
            model = integration.onBakedModelLoad(location, unbakedModel, model, state, this.f_243927_, spriteGetter);
        }
        return model;
    }
}