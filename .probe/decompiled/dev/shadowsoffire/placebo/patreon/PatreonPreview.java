package dev.shadowsoffire.placebo.patreon;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "placebo")
public class PatreonPreview {

    public static final boolean PARTICLES = false;

    public static final boolean WINGS = false;

    private static int counter = 0;

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent e) {
        if (e.phase == TickEvent.Phase.END && e.player.m_9236_().isClientSide && e.player.f_19797_ >= 200 && e.player.f_19797_ % 150 == 0) {
            Minecraft var1 = Minecraft.getInstance();
        }
    }
}