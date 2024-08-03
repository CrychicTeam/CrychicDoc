package dev.latvian.mods.kubejs.core.mixin.common.components;

import dev.latvian.mods.kubejs.core.ComponentKJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@RemapPrefixForJS("kjs$")
@Mixin({ MutableComponent.class })
public abstract class MutableComponentMixin implements ComponentKJS {

    @HideFromJS
    @Shadow
    public abstract MutableComponent append(String var1);
}