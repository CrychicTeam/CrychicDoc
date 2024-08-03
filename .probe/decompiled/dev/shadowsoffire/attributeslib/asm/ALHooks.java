package dev.shadowsoffire.attributeslib.asm;

import dev.shadowsoffire.attributeslib.api.client.GatherEffectScreenTooltipsEvent;
import java.util.List;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.common.MinecraftForge;

public class ALHooks {

    public static List<Component> getEffectTooltip(EffectRenderingInventoryScreen<?> screen, MobEffectInstance effectInst, List<Component> tooltip) {
        GatherEffectScreenTooltipsEvent event = new GatherEffectScreenTooltipsEvent(screen, effectInst, tooltip);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getTooltip();
    }
}