package net.mehvahdjukaar.moonlight.core.misc;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.events.IVillagerBrainEvent;
import net.mehvahdjukaar.moonlight.api.events.MoonlightEventsHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.mehvahdjukaar.moonlight.core.misc.forge.VillagerAIInternalImpl;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Schedule;

public class VillagerAIInternal {

    public static final Supplier<Schedule> CUSTOM_VILLAGER_SCHEDULE = RegHelper.register(Moonlight.res("custom_villager_schedule"), Schedule::new, Registries.SCHEDULE);

    public static void init() {
    }

    public static void onRegisterBrainGoals(Brain<Villager> brain, AbstractVillager villager) {
        if (villager instanceof Villager v) {
            IVillagerBrainEvent event = createEvent(brain, v);
            MoonlightEventsHelper.postEvent(event, IVillagerBrainEvent.class);
            VillagerBrainEventInternal internal = event.getInternal();
            if (internal.hasCustomSchedule()) {
                brain.setSchedule(internal.buildFinalizedSchedule());
                brain.updateActivityFromSchedule(villager.m_9236_().getDayTime(), villager.m_9236_().getGameTime());
            }
        }
    }

    @ExpectPlatform
    @Transformed
    public static IVillagerBrainEvent createEvent(Brain<Villager> brain, Villager villager) {
        return VillagerAIInternalImpl.createEvent(brain, villager);
    }
}