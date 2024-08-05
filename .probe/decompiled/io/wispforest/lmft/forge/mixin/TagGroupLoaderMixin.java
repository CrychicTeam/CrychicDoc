package io.wispforest.lmft.forge.mixin;

import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import io.wispforest.lmft.LMFTCommon;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagLoader;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ TagLoader.class })
public class TagGroupLoaderMixin<T> {

    @Unique
    private static final Logger LOGGER = LogUtils.getLogger();

    @Unique
    private static final ThreadLocal<ResourceLocation> currentTagId = ThreadLocal.withInitial(() -> new ResourceLocation("", ""));

    @Inject(method = { "resolveAll(Lnet/minecraft/registry/tag/TagEntry$ValueGetter;Ljava/util/List;)Lcom/mojang/datafixers/util/Either;" }, at = { @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z") }, locals = LocalCapture.CAPTURE_FAILHARD)
    private void preventTagsFromFailingToLoad(TagEntry.Lookup<T> valueGetter, List<TagLoader.EntryWithSource> list, CallbackInfoReturnable<Either<Collection<TagLoader.EntryWithSource>, Collection<T>>> cir, LinkedHashSet builder, List<TagLoader.EntryWithSource> list2) {
        if (!list2.isEmpty()) {
            LOGGER.error("[Load My Fucking Tags] Couldn't load certain entries with the tag {}: {}", currentTagId.get(), list2.stream().map(Objects::toString).collect(Collectors.joining(", ")));
            list2.clear();
            LMFTCommon.areTagsCooked = true;
        }
    }

    @Inject(method = { "lambda$build$6" }, at = { @At("HEAD") }, remap = false)
    private void saveTagId(TagEntry.Lookup valueGetter, Map map, ResourceLocation id, TagLoader.SortingEntry dependencies, CallbackInfo ci) {
        currentTagId.set(id);
    }
}