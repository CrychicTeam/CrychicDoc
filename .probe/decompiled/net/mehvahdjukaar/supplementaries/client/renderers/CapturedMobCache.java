package net.mehvahdjukaar.supplementaries.client.renderers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.mehvahdjukaar.supplementaries.common.misc.mob_container.MobContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CapturedMobCache {

    private static final LoadingCache<UUID, Entity> MOB_CACHE = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.MINUTES).build(new CacheLoader<UUID, Entity>() {

        public Entity load(UUID key) {
            return null;
        }
    });

    private static UUID crystalID = UUID.randomUUID();

    private static boolean updateCrystal = false;

    public static void addMob(Entity e) {
        if (e != null) {
            MOB_CACHE.put(e.getUUID(), e);
        }
    }

    @Nullable
    public static Entity getOrCreateCachedMob(UUID id, CompoundTag tag) {
        Entity e = (Entity) MOB_CACHE.getIfPresent(id);
        if (e == null) {
            Level world = Minecraft.getInstance().level;
            if (world != null) {
                CompoundTag mobData = tag.getCompound("EntityData");
                e = MobContainer.createEntityFromNBT(mobData, id, world);
                addMob(e);
            }
        }
        return e;
    }

    public static void tickCrystal() {
        if (updateCrystal) {
            Entity e = (Entity) MOB_CACHE.getIfPresent(crystalID);
            if (e instanceof EndCrystal c) {
                c.time++;
                if (e.level() != Minecraft.getInstance().level) {
                    crystalID = UUID.randomUUID();
                }
            }
            updateCrystal = false;
        }
    }

    public static EndCrystal getEndCrystal(Level level) {
        updateCrystal = true;
        Entity e = (Entity) MOB_CACHE.getIfPresent(crystalID);
        if (e instanceof EndCrystal) {
            return (EndCrystal) e;
        } else {
            EndCrystal entity = new EndCrystal(EntityType.END_CRYSTAL, level);
            entity.setShowBottom(false);
            entity.m_20084_(crystalID);
            addMob(entity);
            return entity;
        }
    }

    public static void clear() {
        MOB_CACHE.invalidateAll();
    }
}