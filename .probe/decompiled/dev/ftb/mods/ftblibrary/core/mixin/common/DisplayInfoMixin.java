package dev.ftb.mods.ftblibrary.core.mixin.common;

import dev.ftb.mods.ftblibrary.core.DisplayInfoFTBL;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ DisplayInfo.class })
public abstract class DisplayInfoMixin implements DisplayInfoFTBL {

    @Accessor("icon")
    @Override
    public abstract ItemStack getIconStackFTBL();
}