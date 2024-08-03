package net.mehvahdjukaar.dummmmmmy.forge;

import net.mehvahdjukaar.dummmmmmy.Dummmmmmy;
import net.mehvahdjukaar.dummmmmmy.common.ModEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("dummmmmmy")
public class DummmmmmyForge {

    public DummmmmmyForge() {
        Dummmmmmy.init();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityCriticalHit(CriticalHitEvent event) {
        if (!event.isCanceled()) {
            float mod = event.getDamageModifier();
            if (mod > 1.0F) {
                ModEvents.onEntityCriticalHit(event.getEntity(), event.getTarget(), event.getDamageModifier());
            }
        }
    }

    @SubscribeEvent
    public void onCheckSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (ModEvents.onCheckSpawn(event.getEntity(), event.getLevel())) {
            event.setSpawnCancelled(true);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinLevelEvent event) {
        ModEvents.onEntityJoinWorld(event.getEntity());
    }
}