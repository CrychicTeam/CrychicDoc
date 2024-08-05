package noobanidus.mods.lootr.entity;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.data.DataStorage;

@EventBusSubscriber(modid = "lootr")
public class EntityTicker {

    private static final List<LootrChestMinecartEntity> entities = new ArrayList();

    private static final List<LootrChestMinecartEntity> pendingEntities = new ArrayList();

    private static final Object listLock = new Object();

    private static final Object worldLock = new Object();

    private static boolean tickingList = false;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (!ConfigManager.DISABLE.get()) {
                List<LootrChestMinecartEntity> completed = new ArrayList();
                List<LootrChestMinecartEntity> copy;
                synchronized (listLock) {
                    tickingList = true;
                    copy = new ArrayList(entities);
                    tickingList = false;
                }
                synchronized (worldLock) {
                    for (LootrChestMinecartEntity entity : copy) {
                        if (!entity.isAddedToWorld()) {
                            ServerLevel world = (ServerLevel) entity.m_9236_();
                            ServerChunkCache provider = world.getChunkSource();
                            if (provider.hasChunk(Mth.floor(entity.m_20185_() / 16.0), Mth.floor(entity.m_20189_() / 16.0))) {
                                world.addFreshEntity(entity);
                                completed.add(entity);
                            }
                        }
                    }
                }
                synchronized (listLock) {
                    tickingList = true;
                    entities.removeAll(completed);
                    entities.addAll(pendingEntities);
                    tickingList = false;
                    pendingEntities.clear();
                }
            }
            DataStorage.doDecay();
            DataStorage.doRefresh();
        }
    }

    public static void addEntity(LootrChestMinecartEntity entity) {
        if (!ConfigManager.DISABLE.get()) {
            synchronized (listLock) {
                if (tickingList) {
                    pendingEntities.add(entity);
                } else {
                    entities.add(entity);
                }
            }
        }
    }
}