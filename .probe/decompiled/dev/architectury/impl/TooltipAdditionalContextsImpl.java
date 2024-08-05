package dev.architectury.impl;

import dev.architectury.event.events.client.ClientTooltipEvent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TooltipAdditionalContextsImpl implements ClientTooltipEvent.AdditionalContexts {

    private static final ThreadLocal<TooltipAdditionalContextsImpl> INSTANCE_LOCAL = ThreadLocal.withInitial(TooltipAdditionalContextsImpl::new);

    @Nullable
    private ItemStack item;

    public static ClientTooltipEvent.AdditionalContexts get() {
        return (ClientTooltipEvent.AdditionalContexts) INSTANCE_LOCAL.get();
    }

    @Nullable
    @Override
    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public void setItem(@Nullable ItemStack item) {
        this.item = item;
    }
}