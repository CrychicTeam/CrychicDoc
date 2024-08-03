package dev.latvian.mods.kubejs.core.mixin.common;

import dev.latvian.mods.kubejs.core.ItemFrameEntityKJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.spongepowered.asm.mixin.Mixin;

@RemapPrefixForJS("kjs$")
@Mixin({ ItemFrame.class })
public abstract class ItemFrameEntityMixin implements ItemFrameEntityKJS {
}