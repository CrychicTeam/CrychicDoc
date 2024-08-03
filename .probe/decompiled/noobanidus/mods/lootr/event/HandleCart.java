package noobanidus.mods.lootr.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.entity.EntityTicker;
import noobanidus.mods.lootr.entity.LootrChestMinecartEntity;

@EventBusSubscriber(modid = "lootr")
public class HandleCart {

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (!ConfigManager.isDimensionBlocked(event.getLevel().dimension()) && !ConfigManager.DISABLE.get()) {
            if (event.getEntity().getType() == EntityType.CHEST_MINECART && event.getEntity() instanceof MinecartChest chest && !chest.m_9236_().isClientSide && chest.f_38204_ != null && ConfigManager.CONVERT_MINESHAFTS.get() && !ConfigManager.getLootBlacklist().contains(chest.f_38204_) && chest.m_9236_() instanceof ServerLevel level) {
                LootrChestMinecartEntity lootr = new LootrChestMinecartEntity(chest.m_9236_(), chest.m_20185_(), chest.m_20186_(), chest.m_20189_());
                lootr.m_38236_(chest.f_38204_, chest.f_38205_);
                lootr.getPersistentData().merge(chest.getPersistentData());
                event.setCanceled(true);
                if (!level.getServer().m_18695_()) {
                    LootrAPI.LOG.error("Minecart with Loot table created off main thread. Falling back on EntityTicker.");
                    EntityTicker.addEntity(lootr);
                } else {
                    level.addFreshEntity(lootr);
                }
            }
        }
    }
}