package org.violetmoon.zetaimplforge.event.play;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import org.violetmoon.zeta.event.play.ZAnvilRepair;

public class ForgeZAnvilRepair implements ZAnvilRepair {

    private final AnvilRepairEvent e;

    public ForgeZAnvilRepair(AnvilRepairEvent e) {
        this.e = e;
    }

    public Player getEntity() {
        return this.e.getEntity();
    }

    @Override
    public ItemStack getOutput() {
        return this.e.getOutput();
    }

    @Override
    public ItemStack getLeft() {
        return this.e.getLeft();
    }

    @Override
    public ItemStack getRight() {
        return this.e.getRight();
    }

    @Override
    public float getBreakChance() {
        return this.e.getBreakChance();
    }

    @Override
    public void setBreakChance(float breakChance) {
        this.e.setBreakChance(breakChance);
    }
}