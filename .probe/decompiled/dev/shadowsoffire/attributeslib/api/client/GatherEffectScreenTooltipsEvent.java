package dev.shadowsoffire.attributeslib.api.client;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.eventbus.api.Event;

public class GatherEffectScreenTooltipsEvent extends Event {

    protected final EffectRenderingInventoryScreen<?> screen;

    protected final MobEffectInstance effectInst;

    protected final List<Component> tooltip;

    public GatherEffectScreenTooltipsEvent(EffectRenderingInventoryScreen<?> screen, MobEffectInstance effectInst, List<Component> tooltip) {
        this.screen = screen;
        this.effectInst = effectInst;
        this.tooltip = new ArrayList(tooltip);
    }

    public EffectRenderingInventoryScreen<?> getScreen() {
        return this.screen;
    }

    public MobEffectInstance getEffectInstance() {
        return this.effectInst;
    }

    public List<Component> getTooltip() {
        return this.tooltip;
    }
}