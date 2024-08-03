package io.redspace.ironsspellbooks.effect.guiding_bolt;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.data.IronsDataStorage;
import io.redspace.ironsspellbooks.network.ClientboundGuidingBoltManagerStartTracking;
import io.redspace.ironsspellbooks.network.ClientboundGuidingBoltManagerStopTracking;
import io.redspace.ironsspellbooks.setup.Messages;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class GuidingBoltManager implements INBTSerializable<CompoundTag> {

    public static final GuidingBoltManager INSTANCE = new GuidingBoltManager();

    private final HashMap<UUID, ArrayList<Projectile>> trackedEntities = new HashMap();

    private final HashMap<ResourceKey<Level>, List<Projectile>> dirtyProjectiles = new HashMap();

    private final int tickDelay = 3;

    public void startTracking(LivingEntity entity) {
        if (!entity.f_19853_.isClientSide && !this.trackedEntities.containsKey(entity.m_20148_())) {
            this.trackedEntities.put(entity.m_20148_(), new ArrayList());
            IronsDataStorage.INSTANCE.m_77762_();
        }
    }

    public void stopTracking(LivingEntity entity) {
        if (!entity.f_19853_.isClientSide) {
            this.trackedEntities.remove(entity.m_20148_());
            IronsDataStorage.INSTANCE.m_77762_();
            Messages.sendToPlayersTrackingEntity(new ClientboundGuidingBoltManagerStopTracking(entity), entity);
        }
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag uuids = new ListTag();
        for (UUID key : this.trackedEntities.keySet()) {
            uuids.add(NbtUtils.createUUID(key));
        }
        tag.put("TrackedEntities", uuids);
        return tag;
    }

    public void deserializeNBT(CompoundTag compoundTag) {
        for (Tag uuidTag : compoundTag.getList("TrackedEntities", 11)) {
            try {
                UUID uuid = NbtUtils.loadUUID(uuidTag);
                this.trackedEntities.put(uuid, new ArrayList());
            } catch (Exception var6) {
            }
        }
    }

    @SubscribeEvent
    public static void onProjectileShot(EntityJoinLevelEvent event) {
        if (event.getLevel() instanceof ServerLevel serverLevel && !INSTANCE.trackedEntities.isEmpty() && event.getEntity() instanceof Projectile projectile) {
            ((List) INSTANCE.dirtyProjectiles.computeIfAbsent(serverLevel.m_46472_(), key -> new ArrayList())).add(projectile);
        }
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.START && !INSTANCE.dirtyProjectiles.isEmpty()) {
            if (event.level instanceof ServerLevel serverLevel) {
                HashMap<Entity, List<Projectile>> toSync = new HashMap();
                List<Projectile> dirtyProjectiles = (List<Projectile>) INSTANCE.dirtyProjectiles.getOrDefault(serverLevel.m_46472_(), List.of());
                for (int i = dirtyProjectiles.size() - 1; i >= 0; i--) {
                    Projectile projectile = (Projectile) dirtyProjectiles.get(i);
                    if (projectile.isAddedToWorld()) {
                        Vec3 start = projectile.m_20182_();
                        int searchRange = 32;
                        Vec3 end = Utils.raycastForBlock(serverLevel, start, projectile.m_20184_().normalize().scale((double) searchRange).add(start), ClipContext.Fluid.NONE).m_82450_();
                        for (Entry<UUID, ArrayList<Projectile>> entityToTrackedProjectiles : INSTANCE.trackedEntities.entrySet()) {
                            Entity entity = serverLevel.getEntity((UUID) entityToTrackedProjectiles.getKey());
                            if (entity != null && !(Math.abs(entity.getX() - projectile.m_20185_()) > (double) searchRange) && !(Math.abs(entity.getY() - projectile.m_20186_()) > (double) searchRange) && !(Math.abs(entity.getZ() - projectile.m_20189_()) > (double) searchRange) && Utils.checkEntityIntersecting(entity, start, end, 3.0F).getType() == HitResult.Type.ENTITY) {
                                updateTrackedProjectiles((List<Projectile>) entityToTrackedProjectiles.getValue(), projectile);
                                ((List) toSync.computeIfAbsent(entity, key -> new ArrayList())).add(projectile);
                                break;
                            }
                        }
                        dirtyProjectiles.remove(i);
                    }
                }
                for (Entry<Entity, List<Projectile>> entry : toSync.entrySet()) {
                    Entity entity = (Entity) entry.getKey();
                    Messages.sendToPlayersTrackingEntity(new ClientboundGuidingBoltManagerStartTracking(entity, (List<Projectile>) entry.getValue()), entity);
                }
            }
        }
    }

    private static void updateTrackedProjectiles(List<Projectile> tracked, Projectile toTrack) {
        updateTrackedProjectiles(tracked, List.of(toTrack));
    }

    private static void updateTrackedProjectiles(List<Projectile> tracked, List<Projectile> toTrack) {
        tracked.removeIf(Entity::m_213877_);
        tracked.addAll(toTrack);
    }

    @SubscribeEvent
    public static void livingTick(LivingEvent.LivingTickEvent event) {
        if (!INSTANCE.trackedEntities.isEmpty()) {
            LivingEntity livingEntity = event.getEntity();
            if (livingEntity.f_19797_ % 3 == 0) {
                ArrayList<Projectile> projectiles = (ArrayList<Projectile>) INSTANCE.trackedEntities.get(event.getEntity().m_20148_());
                if (projectiles != null) {
                    if (livingEntity.m_213877_() || livingEntity.isDeadOrDying()) {
                        INSTANCE.stopTracking(livingEntity);
                        return;
                    }
                    List<Projectile> projectilesToRemove = new ArrayList();
                    for (Projectile projectile : projectiles) {
                        Vec3 motion = projectile.m_20184_();
                        float speed = (float) motion.length();
                        Vec3 home = livingEntity.m_20191_().getCenter().subtract(projectile.m_20182_()).normalize().scale((double) (speed * 0.3F));
                        if (home.dot(motion) < 0.0) {
                            projectilesToRemove.add(projectile);
                        } else {
                            Vec3 newMotion = motion.add(home).normalize().scale((double) speed);
                            projectile.m_20256_(newMotion);
                        }
                    }
                    projectiles.removeAll(projectilesToRemove);
                }
            }
        }
    }

    public static void handleClientboundStartTracking(UUID uuid, List<Integer> projectileIds) {
        ClientLevel level = Minecraft.getInstance().level;
        List<Projectile> projectiles = new ArrayList();
        for (Integer i : projectileIds) {
            if (level.getEntity(i) instanceof Projectile projectile) {
                updateTrackedProjectiles(projectiles, projectile);
            }
        }
        ((ArrayList) INSTANCE.trackedEntities.computeIfAbsent(uuid, key -> new ArrayList())).addAll(projectiles);
    }

    public static void handleClientboundStopTracking(UUID uuid) {
        INSTANCE.trackedEntities.remove(uuid);
    }

    public static void handleClientLogout() {
        INSTANCE.trackedEntities.clear();
    }
}