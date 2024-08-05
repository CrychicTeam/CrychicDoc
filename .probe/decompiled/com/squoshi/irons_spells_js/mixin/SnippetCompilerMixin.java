package com.squoshi.irons_spells_js.mixin;

import com.probejs.ProbeCommands;
import com.probejs.specials.SnippetCompiler.KubeDump;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = { KubeDump.class }, remap = false)
public class SnippetCompilerMixin {

    @Inject(method = { "putRegistry" }, at = { @At("HEAD") }, cancellable = true)
    private static <T> void kjs_irons_spells$useForge(Map<String, List<String>> registries, String type, ResourceKey<Registry<T>> registry, CallbackInfo ci) {
        Registry<?> builtinRegistry = RegistryInfo.of(registry).getVanillaRegistry();
        if (builtinRegistry == null) {
            builtinRegistry = (Registry<?>) ProbeCommands.COMMAND_LEVEL.m_9598_().registry(registry).orElse(null);
        }
        IForgeRegistry<?> forgeRegistry = null;
        if (builtinRegistry == null) {
            forgeRegistry = RegistryManager.ACTIVE.getRegistry(registry);
        }
        if (builtinRegistry != null) {
            registries.put(type, ProbeCommands.getRegistry(registry).keySet().stream().map(ResourceLocation::toString).toList());
        } else {
            registries.put(type, forgeRegistry.getKeys().stream().map(ResourceLocation::toString).toList());
        }
        ci.cancel();
    }
}