package dev.latvian.mods.rhino.mod.core.mixin.common;

import dev.latvian.mods.rhino.mod.util.color.Color;
import net.minecraft.ChatFormatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ ChatFormatting.class })
public abstract class ChatFormattingMixin implements Color {

    @Shadow
    @Final
    private Integer color;

    @Override
    public int getArgbJS() {
        return this.color == null ? -16777216 : 0xFF000000 | this.color;
    }

    @Override
    public int getRgbJS() {
        return this.color == null ? 0 : this.color;
    }
}