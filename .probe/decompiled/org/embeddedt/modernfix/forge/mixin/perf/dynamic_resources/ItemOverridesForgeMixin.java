package org.embeddedt.modernfix.forge.mixin.perf.dynamic_resources;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.function.Function;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.embeddedt.modernfix.duck.IExtendedModelBaker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ ItemOverrides.class })
@ClientOnlyMixin
public class ItemOverridesForgeMixin {

    @WrapOperation(method = { "bakeModel" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelBaker;bake(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/resources/model/ModelState;Ljava/util/function/Function;)Lnet/minecraft/client/resources/model/BakedModel;") }, remap = false)
    private BakedModel bake(ModelBaker instance, ResourceLocation resourceLocation, ModelState modelState, Function<ResourceLocation, TextureAtlasSprite> spriteGetter, Operation<BakedModel> original) {
        boolean prevState = ((IExtendedModelBaker) instance).throwOnMissingModel(false);
        BakedModel var7;
        try {
            var7 = (BakedModel) original.call(new Object[] { instance, resourceLocation, modelState, spriteGetter });
        } finally {
            ((IExtendedModelBaker) instance).throwOnMissingModel(prevState);
        }
        return var7;
    }
}