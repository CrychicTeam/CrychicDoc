package dev.latvian.mods.kubejs.core.mixin.common;

import dev.latvian.mods.kubejs.core.TagLoaderKJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagLoader;
import net.minecraft.tags.TagManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ TagManager.class })
public abstract class TagManagerMixin {

    @Inject(method = { "createLoader" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/tags/TagLoader;<init>(Ljava/util/function/Function;Ljava/lang/String;)V", shift = Shift.BY, by = 2) }, locals = LocalCapture.CAPTURE_FAILHARD)
    private <T> void kjs$saveRegistryToTagLoader(ResourceManager rm, Executor executor, RegistryAccess.RegistryEntry<T> reg, CallbackInfoReturnable<CompletableFuture<TagManager.LoadResult<T>>> cir, ResourceKey<? extends Registry<T>> key, Registry<T> registry, TagLoader<Holder<T>> loader) {
        ((TagLoaderKJS) loader).kjs$setRegistry(registry);
    }

    @Inject(method = { "reload" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/core/RegistryAccess;registries()Ljava/util/stream/Stream;") })
    private void kjs$reload(PreparableReloadListener.PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        ItemStackJS.clearAllCaches();
    }
}