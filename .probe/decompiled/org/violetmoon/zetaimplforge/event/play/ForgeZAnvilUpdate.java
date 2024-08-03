package org.violetmoon.zetaimplforge.event.play;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import org.violetmoon.zeta.event.play.ZAnvilUpdate;

public class ForgeZAnvilUpdate implements ZAnvilUpdate {

    private final AnvilUpdateEvent e;

    public ForgeZAnvilUpdate(AnvilUpdateEvent e) {
        this.e = e;
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
    public ItemStack getOutput() {
        return this.e.getOutput();
    }

    @Override
    public String getName() {
        return this.e.getName();
    }

    @Override
    public void setOutput(ItemStack output) {
        this.e.setOutput(output);
    }

    @Override
    public void setCost(int cost) {
        this.e.setCost(cost);
    }

    @Override
    public int getMaterialCost() {
        return this.e.getMaterialCost();
    }

    @Override
    public void setMaterialCost(int materialCost) {
        this.e.setMaterialCost(materialCost);
    }

    @Override
    public Player getPlayer() {
        return this.e.getPlayer();
    }

    public static class Highest extends ForgeZAnvilUpdate implements ZAnvilUpdate.Highest {

        public Highest(AnvilUpdateEvent e) {
            super(e);
        }
    }

    public static class Lowest extends ForgeZAnvilUpdate implements ZAnvilUpdate.Lowest {

        public Lowest(AnvilUpdateEvent e) {
            super(e);
        }
    }
}