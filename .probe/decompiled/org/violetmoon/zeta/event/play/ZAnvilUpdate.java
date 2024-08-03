package org.violetmoon.zeta.event.play;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;

public interface ZAnvilUpdate extends IZetaPlayEvent {

    ItemStack getLeft();

    ItemStack getRight();

    String getName();

    ItemStack getOutput();

    void setOutput(ItemStack var1);

    void setCost(int var1);

    int getMaterialCost();

    void setMaterialCost(int var1);

    Player getPlayer();

    public interface Highest extends ZAnvilUpdate {
    }

    public interface Lowest extends ZAnvilUpdate {
    }
}