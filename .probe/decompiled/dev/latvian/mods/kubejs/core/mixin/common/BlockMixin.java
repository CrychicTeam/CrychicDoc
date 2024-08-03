package dev.latvian.mods.kubejs.core.mixin.common;

import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@RemapPrefixForJS("kjs$")
@Mixin({ Block.class })
public abstract class BlockMixin extends BlockBehaviourMixin {

    @Accessor("descriptionId")
    @Mutable
    @Override
    public abstract void kjs$setNameKey(String var1);
}