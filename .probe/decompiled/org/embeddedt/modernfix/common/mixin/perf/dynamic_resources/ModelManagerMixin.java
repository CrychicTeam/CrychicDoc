package org.embeddedt.modernfix.common.mixin.perf.dynamic_resources;

import com.google.common.collect.ImmutableList;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.embeddedt.modernfix.util.LambdaMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ModelManager.class })
@ClientOnlyMixin
public class ModelManagerMixin {

    @Shadow
    private Map<ResourceLocation, BakedModel> bakedRegistry;

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void injectDummyBakedRegistry(CallbackInfo ci) {
        if (this.bakedRegistry == null) {
            this.bakedRegistry = new HashMap();
        }
    }

    @Redirect(method = { "reload" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelManager;loadBlockModels(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
    private CompletableFuture<Map<ResourceLocation, BlockModel>> deferBlockModelLoad(ResourceManager manager, Executor executor) {
        return CompletableFuture.completedFuture(new LambdaMap(location -> this.loadSingleBlockModel(manager, location)));
    }

    @Redirect(method = { "reload" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelManager;loadBlockStates(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
    private CompletableFuture<Map<ResourceLocation, List<ModelBakery.LoadedJson>>> deferBlockStateLoad(ResourceManager manager, Executor executor) {
        return CompletableFuture.completedFuture(new LambdaMap(location -> this.loadSingleBlockState(manager, location)));
    }

    @Redirect(method = { "loadModels" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/StateDefinition;getPossibleStates()Lcom/google/common/collect/ImmutableList;"))
    private ImmutableList<BlockState> skipCollection(StateDefinition<Block, BlockState> definition) {
        return ImmutableList.of();
    }

    private BlockModel loadSingleBlockModel(ResourceManager manager, ResourceLocation location) {
        return (BlockModel) manager.m_213713_(location).map(resource -> {
            try {
                BufferedReader reader = resource.openAsReader();
                BlockModel var2;
                try {
                    var2 = BlockModel.fromStream(reader);
                } catch (Throwable var5) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var4) {
                            var5.addSuppressed(var4);
                        }
                    }
                    throw var5;
                }
                if (reader != null) {
                    reader.close();
                }
                return var2;
            } catch (IOException var6) {
                ModernFix.LOGGER.error("Couldn't load model", var6);
                return null;
            }
        }).orElse(null);
    }

    private List<ModelBakery.LoadedJson> loadSingleBlockState(ResourceManager manager, ResourceLocation location) {
        return (List<ModelBakery.LoadedJson>) manager.getResourceStack(location).stream().map(resource -> {
            try {
                BufferedReader reader = resource.openAsReader();
                ModelBakery.LoadedJson var2;
                try {
                    var2 = new ModelBakery.LoadedJson(resource.sourcePackId(), GsonHelper.parse(reader));
                } catch (Throwable var5) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var4) {
                            var5.addSuppressed(var4);
                        }
                    }
                    throw var5;
                }
                if (reader != null) {
                    reader.close();
                }
                return var2;
            } catch (IOException var6) {
                ModernFix.LOGGER.error("Couldn't load blockstate", var6);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}