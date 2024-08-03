package dev.shadowsoffire.attributeslib.api.client;

import java.util.List;
import java.util.ListIterator;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;

public class AddAttributeTooltipsEvent extends PlayerEvent {

    protected final ItemStack stack;

    protected final List<Component> tooltip;

    protected final ListIterator<Component> attributeTooltipIterator;

    protected final TooltipFlag flag;

    public AddAttributeTooltipsEvent(ItemStack stack, @Nullable Player player, List<Component> tooltip, ListIterator<Component> attributeTooltipIterator, TooltipFlag flag) {
        super(player);
        this.stack = stack;
        this.tooltip = tooltip;
        this.attributeTooltipIterator = attributeTooltipIterator;
        this.flag = flag;
    }

    public TooltipFlag getFlags() {
        return this.flag;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public List<Component> getTooltip() {
        return this.tooltip;
    }

    public ListIterator<Component> getAttributeTooltipIterator() {
        return this.attributeTooltipIterator;
    }

    @Nullable
    @Override
    public Player getEntity() {
        return super.getEntity();
    }
}