package org.violetmoon.zetaimplforge.event.play;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.zeta.event.play.ZFurnaceFuelBurnTime;

public class ForgeZFurnaceFuelBurnTime implements ZFurnaceFuelBurnTime {

    private final FurnaceFuelBurnTimeEvent e;

    public ForgeZFurnaceFuelBurnTime(FurnaceFuelBurnTimeEvent e) {
        this.e = e;
    }

    @NotNull
    @Override
    public ItemStack getItemStack() {
        return this.e.getItemStack();
    }

    @Override
    public void setBurnTime(int burnTime) {
        this.e.setBurnTime(burnTime);
    }
}