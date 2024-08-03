package snownee.kiwi.schedule.impl;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import snownee.kiwi.schedule.ITicker;
import snownee.kiwi.schedule.Scheduler;

public enum GlobalTicker implements ITicker {

    PRE_SERVER, POST_SERVER, PRE_CLIENT, POST_CLIENT;

    @SubscribeEvent
    public static void onTickServer(TickEvent.ServerTickEvent event) {
        Scheduler.tick(event.phase == TickEvent.Phase.START ? PRE_SERVER : POST_SERVER);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onTickClient(TickEvent.ClientTickEvent event) {
        Scheduler.tick(event.phase == TickEvent.Phase.START ? PRE_CLIENT : POST_CLIENT);
    }

    public static GlobalTicker get(LogicalSide side, TickEvent.Phase phase) {
        return side == LogicalSide.SERVER ? (phase == TickEvent.Phase.START ? PRE_SERVER : POST_SERVER) : (phase == TickEvent.Phase.START ? PRE_CLIENT : POST_CLIENT);
    }

    static {
        MinecraftForge.EVENT_BUS.register(GlobalTicker.class);
    }
}