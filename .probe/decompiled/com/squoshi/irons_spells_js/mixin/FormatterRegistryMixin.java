package com.squoshi.irons_spells_js.mixin;

import com.probejs.ProbeCommands;
import com.probejs.ProbeConfig;
import com.probejs.ProbeJS;
import com.probejs.specials.special.FormatterRegistry;
import com.probejs.util.RLHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.RegistryManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = { FormatterRegistry.class }, remap = false)
public class FormatterRegistryMixin<T> {

    @Shadow
    @Final
    private ResourceKey<Registry<T>> registry;

    @Inject(method = { "format" }, at = { @At("HEAD") }, cancellable = true)
    private void kjs_irons_spells$useForge(Integer indent, Integer stepIndent, CallbackInfoReturnable<List<String>> cir) {
        List<String> items = new ArrayList();
        String typeName = RLHelper.finalComponentToTitle(this.registry.location().getPath());
        Registry<T> builtinRegistry = (Registry<T>) ProbeCommands.COMMAND_LEVEL.m_9598_().registry(this.registry).orElse(null);
        if (builtinRegistry != null) {
            builtinRegistry.keySet().forEach(rl -> {
                if (rl.getNamespace().equals("minecraft")) {
                    items.add(ProbeJS.GSON.toJson(rl.getPath()));
                }
                items.add(ProbeJS.GSON.toJson(rl.toString()));
            });
        } else {
            RegistryManager.ACTIVE.getRegistry(this.registry).getKeys().forEach(rl -> {
                if (rl.getNamespace().equals("minecraft")) {
                    items.add(ProbeJS.GSON.toJson(rl.getPath()));
                }
                items.add(ProbeJS.GSON.toJson(rl.toString()));
            });
        }
        String joined = String.join(" | ", items);
        if (items.isEmpty() || !ProbeConfig.INSTANCE.allowRegistryLiteralDumps) {
            joined = "string";
        }
        cir.setReturnValue(List.of("%stype %s = %s;".formatted(" ".repeat(indent), typeName, joined)));
    }
}