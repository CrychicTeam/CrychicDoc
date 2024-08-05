package dev.ftb.mods.ftblibrary.core.mixin.common;

import dev.ftb.mods.ftblibrary.core.CompoundContainerFTBL;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ CompoundContainer.class })
public abstract class CompoundContainerMixin implements CompoundContainerFTBL {

    @Accessor("container1")
    @Override
    public abstract Container getContainer1FTBL();

    @Accessor("container2")
    @Override
    public abstract Container getContainer2FTBL();
}