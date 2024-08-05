package dev.latvian.mods.kubejs.core.mixin.common;

import dev.latvian.mods.kubejs.core.ItemEntityKJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;

@RemapPrefixForJS("kjs$")
@Mixin({ ItemEntity.class })
public abstract class ItemEntityMixin implements ItemEntityKJS {
}