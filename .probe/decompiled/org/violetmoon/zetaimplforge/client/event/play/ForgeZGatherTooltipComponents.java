package org.violetmoon.zetaimplforge.client.event.play;

import com.mojang.datafixers.util.Either;
import java.util.List;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import org.violetmoon.zeta.client.event.play.ZGatherTooltipComponents;

public record ForgeZGatherTooltipComponents(RenderTooltipEvent.GatherComponents e) implements ZGatherTooltipComponents {

    @Override
    public ItemStack getItemStack() {
        return this.e.getItemStack();
    }

    @Override
    public int getScreenWidth() {
        return this.e.getScreenWidth();
    }

    @Override
    public int getScreenHeight() {
        return this.e.getScreenHeight();
    }

    @Override
    public List<Either<FormattedText, TooltipComponent>> getTooltipElements() {
        return this.e.getTooltipElements();
    }

    @Override
    public int getMaxWidth() {
        return this.e.getMaxWidth();
    }

    @Override
    public void setMaxWidth(int maxWidth) {
        this.e.setMaxWidth(maxWidth);
    }
}