package org.embeddedt.modernfix.forge.mixin.perf.dynamic_resources.ctm;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.modernfix.ModernFixClient;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.embeddedt.modernfix.annotation.RequiresMod;
import org.embeddedt.modernfix.api.entrypoint.ModernFixClientIntegration;
import org.embeddedt.modernfix.forge.dynresources.IModelBakerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.chisel.ctm.CTM;
import team.chisel.ctm.client.mixin.ModelBakerImplAccessor;
import team.chisel.ctm.client.model.AbstractCTMBakedModel;
import team.chisel.ctm.client.model.ModelCTM;
import team.chisel.ctm.client.texture.IMetadataSectionCTM;
import team.chisel.ctm.client.util.ResourceUtil;
import team.chisel.ctm.client.util.TextureMetadataHandler;

@Mixin({ TextureMetadataHandler.class })
@RequiresMod("ctm")
@ClientOnlyMixin
public abstract class TextureMetadataHandlerMixin implements ModernFixClientIntegration {

    @Shadow(remap = false)
    @Final
    public static Multimap<ResourceLocation, Material> TEXTURES_SCRAPED;

    @Shadow(remap = false)
    @Nonnull
    protected abstract BakedModel wrap(UnbakedModel var1, BakedModel var2) throws IOException;

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void subscribeDynamic(CallbackInfo ci) {
        ModernFixClient.CLIENT_INTEGRATIONS.add(this);
    }

    @Inject(method = { "onModelBake(Lnet/minecraftforge/client/event/ModelEvent$ModifyBakingResult;)V", "onModelBake(Lnet/minecraftforge/client/event/ModelEvent$BakingCompleted;)V" }, at = { @At("HEAD") }, cancellable = true, remap = false)
    private void noIteration(CallbackInfo ci) {
        ci.cancel();
    }

    @Override
    public BakedModel onBakedModelLoad(ResourceLocation rl, UnbakedModel rootModel, BakedModel baked, ModelState state, ModelBakery bakery) {
        if (rl instanceof ModelResourceLocation && !(baked instanceof AbstractCTMBakedModel) && !baked.isCustomRenderer()) {
            Deque<ResourceLocation> dependencies = new ArrayDeque();
            Set<ResourceLocation> seenModels = new HashSet();
            dependencies.push(rl);
            seenModels.add(rl);
            boolean shouldWrap = false;
            new HashSet();
            while (true) {
                ResourceLocation dep;
                UnbakedModel model;
                while (true) {
                    if (shouldWrap || dependencies.isEmpty()) {
                        if (shouldWrap) {
                            try {
                                baked = this.wrap(rootModel, baked);
                                this.handleInit(rl, baked, bakery);
                                dependencies.clear();
                            } catch (IOException var18) {
                                CTM.logger.error("Could not wrap model " + rl + ". Aborting...", var18);
                            }
                            return baked;
                        }
                        return baked;
                    }
                    dep = (ResourceLocation) dependencies.pop();
                    try {
                        model = dep == rl ? rootModel : bakery.getModel(dep);
                        break;
                    } catch (Exception var20) {
                    }
                }
                Collection<Material> textures = Sets.newHashSet(TEXTURES_SCRAPED.get(dep));
                Collection<ResourceLocation> newDependencies = model.getDependencies();
                for (Material tex : textures) {
                    IMetadataSectionCTM meta = null;
                    try {
                        meta = (IMetadataSectionCTM) ResourceUtil.getMetadata(ResourceUtil.spriteToAbsolute(tex.texture())).orElse(null);
                    } catch (IOException var19) {
                    }
                    if (meta != null) {
                        shouldWrap = true;
                    }
                }
                for (ResourceLocation newDep : newDependencies) {
                    if (seenModels.add(newDep)) {
                        dependencies.push(newDep);
                    }
                }
            }
        } else {
            return baked;
        }
    }

    private void handleInit(ResourceLocation key, BakedModel wrappedModel, ModelBakery bakery) {
        if (wrappedModel instanceof AbstractCTMBakedModel baked && baked.getModel() instanceof ModelCTM ctmModel && !ctmModel.isInitialized()) {
            Function<Material, TextureAtlasSprite> spriteGetter = m -> Minecraft.getInstance().getModelManager().getAtlas(m.atlasLocation()).getSprite(m.texture());
            ModelBakery.ModelBakerImpl baker = ModelBakerImplAccessor.createImpl(bakery, ($, m) -> (TextureAtlasSprite) spriteGetter.apply(m), key);
            ((IModelBakerImpl) baker).mfix$ignoreCache();
            ctmModel.bake(baker, spriteGetter, BlockModelRotation.X0_Y0, key);
        }
    }
}