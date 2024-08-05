package com.mna.api.timing;

import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "mna", bus = Bus.FORGE)
public class DelayedEventQueue {

    private static HashMap<ResourceKey<Level>, ArrayList<IDelayedEvent>> _events = new HashMap();

    @SubscribeEvent
    public static void worldTick(TickEvent.LevelTickEvent event) {
        if (!event.level.isClientSide()) {
            if (_events.containsKey(event.level.dimension())) {
                ArrayList<IDelayedEvent> worldDelayedEvents = (ArrayList<IDelayedEvent>) _events.get(event.level.dimension());
                for (int i = 0; i < worldDelayedEvents.size(); i++) {
                    if (((IDelayedEvent) worldDelayedEvents.get(i)).tick()) {
                        worldDelayedEvents.remove(i);
                        i--;
                    }
                }
            }
        }
    }

    public static void pushEvent(Level world, IDelayedEvent event) {
        if (event.getID() != null) {
            if (!_events.containsKey(world.dimension())) {
                _events.put(world.dimension(), new ArrayList());
            }
            ((ArrayList) _events.get(world.dimension())).add(event);
        }
    }

    public static boolean hasEvent(Level world, String identifier) {
        return !_events.containsKey(world.dimension()) ? false : ((ArrayList) _events.get(world.dimension())).stream().anyMatch(e -> e.getID() != null && e.getID().equals(identifier));
    }
}