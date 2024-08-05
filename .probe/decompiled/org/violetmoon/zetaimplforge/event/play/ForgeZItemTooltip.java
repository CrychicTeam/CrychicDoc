package org.violetmoon.zetaimplforge.event.play;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.event.play.ZItemTooltip;

public class ForgeZItemTooltip implements ZItemTooltip {

    private final ItemTooltipEvent e;

    public ForgeZItemTooltip(ItemTooltipEvent e) {
        this.e = e;
    }

    @Override
    public TooltipFlag getFlags() {
        return this.e.getFlags();
    }

    @NotNull
    @Override
    public ItemStack getItemStack() {
        return this.e.getItemStack();
    }

    @Override
    public List<Component> getToolTip() {
        return this.e.getToolTip();
    }

    @Nullable
    @Override
    public Player getEntity() {
        return this.e.getEntity();
    }
}