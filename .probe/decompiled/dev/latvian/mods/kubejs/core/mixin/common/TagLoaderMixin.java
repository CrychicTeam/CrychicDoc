package dev.latvian.mods.kubejs.core.mixin.common;

import dev.latvian.mods.kubejs.core.TagLoaderKJS;
import dev.latvian.mods.kubejs.server.ServerScriptManager;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagLoader;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ TagLoader.class })
public abstract class TagLoaderMixin<T> implements TagLoaderKJS<T> {

    @Unique
    @Nullable
    private Registry<T> kjs$storedRegistry;

    @Inject(method = { "load" }, at = { @At("RETURN") })
    private void customTags(ResourceManager resourceManager, CallbackInfoReturnable<Map<ResourceLocation, List<TagLoader.EntryWithSource>>> cir) {
        ServerScriptManager ssm = ServerScriptManager.instance;
        if (ssm != null) {
            this.kjs$customTags(ssm, (Map<ResourceLocation, List<TagLoader.EntryWithSource>>) cir.getReturnValue());
        }
    }

    @Override
    public void kjs$setRegistry(Registry<T> registry) {
        this.kjs$storedRegistry = registry;
    }

    @Nullable
    @Override
    public Registry<T> kjs$getRegistry() {
        return this.kjs$storedRegistry;
    }
}