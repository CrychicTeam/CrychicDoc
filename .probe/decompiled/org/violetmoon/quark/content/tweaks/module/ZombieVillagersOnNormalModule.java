package org.violetmoon.quark.content.tweaks.module;

import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.ForgeEventFactory;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.play.entity.living.ZLivingConversion;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "tweaks")
public class ZombieVillagersOnNormalModule extends ZetaModule {

    @PlayEvent
    public void onConversion(ZLivingConversion.Pre event) {
        if (event.getEntity().m_6095_() == EntityType.VILLAGER && event.getOutcome() == EntityType.ZOMBIE_VILLAGER) {
            Villager villager = (Villager) event.getEntity();
            Level level = villager.m_9236_();
            if (level instanceof ServerLevelAccessor serverLevel) {
                ZombieVillager zombievillager = (ZombieVillager) villager.m_21406_(EntityType.ZOMBIE_VILLAGER, false);
                if (zombievillager == null) {
                    return;
                }
                zombievillager.finalizeSpawn(serverLevel, level.getCurrentDifficultyAt(zombievillager.m_20183_()), MobSpawnType.CONVERSION, new Zombie.ZombieGroupData(false, true), null);
                zombievillager.setVillagerData(villager.getVillagerData());
                zombievillager.setGossips(villager.getGossips().store(NbtOps.INSTANCE));
                zombievillager.setTradeOffers(villager.m_6616_().createTag());
                zombievillager.setVillagerXp(villager.getVillagerXp());
                ForgeEventFactory.onLivingConvert(villager, zombievillager);
                level.m_5898_(null, 1026, villager.m_20183_(), 0);
                event.setCanceled(true);
            }
        }
    }
}