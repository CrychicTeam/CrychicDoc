package dev.xkmc.l2hostility.content.item.spawner;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.HashMap;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@SerialClass
public class TraitSpawnerData {

    @SerialField
    private final HashMap<UUID, TraitSpawnerData.TrackedEntity> list = new HashMap();

    private boolean init = false;

    protected void init(Level level) {
        if (!this.init) {
            for (TraitSpawnerData.TrackedEntity e : this.list.values()) {
                if (level instanceof ServerLevel sl) {
                    e.serverInit(sl);
                } else {
                    e.clientInit(level);
                }
            }
            this.init = true;
        }
    }

    protected void add(LivingEntity le) {
        TraitSpawnerData.TrackedEntity entry = new TraitSpawnerData.TrackedEntity();
        entry.uuid = le.m_20148_();
        entry.uid = le.m_19879_();
        entry.entity = le;
        entry.state = TraitSpawnerData.EntityState.ALIVE;
        this.list.put(entry.uuid, entry);
    }

    public void onDeath(LivingEntity mob) {
        TraitSpawnerData.TrackedEntity ans = (TraitSpawnerData.TrackedEntity) this.list.get(mob.m_20148_());
        if (ans != null) {
            ans.state = TraitSpawnerData.EntityState.DEAD;
        }
    }

    protected TraitSpawnerBlock.State tick() {
        boolean hasMissing = false;
        boolean hasAlive = false;
        for (TraitSpawnerData.TrackedEntity e : this.list.values()) {
            e.tick();
            hasMissing |= e.state == TraitSpawnerData.EntityState.MISSING;
            hasAlive |= e.state == TraitSpawnerData.EntityState.ALIVE;
        }
        return hasMissing ? TraitSpawnerBlock.State.FAILED : (hasAlive ? TraitSpawnerBlock.State.ACTIVATED : TraitSpawnerBlock.State.CLEAR);
    }

    protected void stop() {
        for (TraitSpawnerData.TrackedEntity e : this.list.values()) {
            if (e.getEntity() != null && e.state == TraitSpawnerData.EntityState.ALIVE) {
                e.getEntity().m_146870_();
            }
        }
        this.list.clear();
    }

    protected int getMax() {
        return this.list.size();
    }

    protected int getAlive() {
        int i = 0;
        for (TraitSpawnerData.TrackedEntity e : this.list.values()) {
            if (e.state == TraitSpawnerData.EntityState.ALIVE) {
                i++;
            }
        }
        return i;
    }

    public static enum EntityState {

        ALIVE, DEAD, MISSING
    }

    @SerialClass
    public static class TrackedEntity {

        @SerialField
        public UUID uuid;

        @SerialField(toClient = true)
        public int uid;

        @SerialField(toClient = true)
        public TraitSpawnerData.EntityState state;

        @Nullable
        private LivingEntity entity;

        private void serverInit(ServerLevel level) {
            this.uid = -1;
            this.entity = null;
            if (this.state == TraitSpawnerData.EntityState.ALIVE) {
                if (level.getEntity(this.uuid) instanceof LivingEntity le) {
                    this.entity = le;
                    this.uid = le.m_19879_();
                } else {
                    this.state = TraitSpawnerData.EntityState.MISSING;
                }
            }
        }

        private void clientInit(Level level) {
            if (this.uid > 0 && level.getEntity(this.uid) instanceof LivingEntity le) {
                this.entity = le;
            } else {
                this.entity = null;
            }
        }

        @Nullable
        public LivingEntity getEntity() {
            if (this.state != TraitSpawnerData.EntityState.ALIVE) {
                return null;
            } else {
                return this.entity == null ? null : (!this.entity.isAlive() ? null : this.entity);
            }
        }

        public void tick() {
            if (this.state == TraitSpawnerData.EntityState.ALIVE) {
                if (this.entity == null) {
                    this.state = TraitSpawnerData.EntityState.MISSING;
                } else {
                    if (this.entity.m_213877_() || !this.entity.isAlive()) {
                        this.state = TraitSpawnerData.EntityState.MISSING;
                    }
                }
            }
        }
    }
}