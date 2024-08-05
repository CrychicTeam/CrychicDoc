package dev.latvian.mods.kubejs.core.mixin.common;

import dev.latvian.mods.kubejs.core.LazyComponentKJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import java.util.function.Supplier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@RemapPrefixForJS("kjs$")
@Mixin({ CommandSourceStack.class })
public abstract class CommandSourceStackMixin {

    @Shadow
    @HideFromJS
    public abstract void sendSuccess(Supplier<Component> var1, boolean var2);

    @Unique
    public void kjs$sendSuccess(Component component, boolean broadcastToAdmins) {
        this.kjs$sendSuccessLazy(() -> component, broadcastToAdmins);
    }

    @Unique
    public void kjs$sendSuccessLazy(LazyComponentKJS component, boolean broadcastToAdmins) {
        this.sendSuccess(component, broadcastToAdmins);
    }
}