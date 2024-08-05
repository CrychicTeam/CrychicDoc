package dev.latvian.mods.kubejs.core.mixin.common;

import dev.latvian.mods.kubejs.core.BlockStateKJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@RemapPrefixForJS("kjs$")
@Mixin({ BlockBehaviour.BlockStateBase.class })
public abstract class BlockStateBaseMixin implements BlockStateKJS {

    @Accessor("destroySpeed")
    @Mutable
    @Override
    public abstract void kjs$setDestroySpeed(float var1);

    @Accessor("requiresCorrectToolForDrops")
    @Mutable
    @Override
    public abstract void kjs$setRequiresTool(boolean var1);

    @Accessor("lightEmission")
    @Mutable
    @Override
    public abstract void kjs$setLightEmission(int var1);
}