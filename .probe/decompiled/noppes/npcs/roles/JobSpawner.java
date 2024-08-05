package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntityLiving;
import noppes.npcs.api.entity.data.role.IJobSpawner;
import noppes.npcs.controllers.data.CloneSpawnData;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.server.SPacketToolMobSpawner;
import org.apache.commons.lang3.RandomStringUtils;

public class JobSpawner extends JobInterface implements IJobSpawner {

    public Map<Integer, CloneSpawnData> data = new HashMap();

    private int number = 0;

    public List<LivingEntity> spawned = new ArrayList();

    private Map<String, Long> cooldown = new HashMap();

    private String id = RandomStringUtils.random(8, true, true);

    public boolean doesntDie = false;

    public int spawnType = 0;

    public int xOffset = 0;

    public int yOffset = 0;

    public int zOffset = 0;

    private LivingEntity target;

    public boolean despawnOnTargetLost = true;

    public JobSpawner(EntityNPCInterface npc) {
        super(npc);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.put("SpawnerData", CloneSpawnData.save(this.data));
        compound.putString("SpawnerId", this.id);
        compound.putBoolean("SpawnerDoesntDie", this.doesntDie);
        compound.putInt("SpawnerType", this.spawnType);
        compound.putInt("SpawnerXOffset", this.xOffset);
        compound.putInt("SpawnerYOffset", this.yOffset);
        compound.putInt("SpawnerZOffset", this.zOffset);
        compound.putBoolean("DespawnOnTargetLost", this.despawnOnTargetLost);
        return compound;
    }

    public String getTitle(int slot) {
        CloneSpawnData sd = (CloneSpawnData) this.data.get(slot);
        return sd == null ? "gui.selectnpc" : sd.tab + ": " + sd.name;
    }

    @Override
    public void load(CompoundTag compound) {
        this.data = CloneSpawnData.load(compound.getList("SpawnerData", 10));
        this.id = compound.getString("SpawnerId");
        this.doesntDie = compound.getBoolean("SpawnerDoesntDie");
        this.spawnType = compound.getInt("SpawnerType");
        this.xOffset = compound.getInt("SpawnerXOffset");
        this.yOffset = compound.getInt("SpawnerYOffset");
        this.zOffset = compound.getInt("SpawnerZOffset");
        this.despawnOnTargetLost = compound.getBoolean("DespawnOnTargetLost");
    }

    public void setJobCompound(int slot, int tab, String name) {
        this.data.put(slot, new CloneSpawnData(tab, name));
    }

    public void remove(int i) {
        this.data.remove(i);
    }

    @Override
    public void aiUpdateTask() {
        if (this.spawned.isEmpty()) {
            if (this.spawnType == 0 && this.spawnEntity(this.number) == null && !this.doesntDie) {
                this.npc.m_146870_();
            }
            if (this.spawnType == 1) {
                if (this.number >= 6 && !this.doesntDie) {
                    this.npc.m_146870_();
                } else {
                    this.spawnEntity(0);
                    this.spawnEntity(1);
                    this.spawnEntity(2);
                    this.spawnEntity(3);
                    this.spawnEntity(4);
                    this.spawnEntity(5);
                    this.number = 6;
                }
            }
            if (this.spawnType == 2) {
                ArrayList<CompoundTag> list = new ArrayList();
                for (CloneSpawnData d : this.data.values()) {
                    CompoundTag c = d.getCompound();
                    if (c != null && c.contains("id")) {
                        list.add(c);
                    }
                }
                if (!list.isEmpty()) {
                    CompoundTag compound = (CompoundTag) list.get(this.npc.m_217043_().nextInt(list.size()));
                    this.spawnEntity(compound);
                } else if (!this.doesntDie) {
                    this.npc.m_146870_();
                }
            }
        } else {
            this.checkSpawns();
        }
    }

    public void checkSpawns() {
        Iterator<LivingEntity> iterator = this.spawned.iterator();
        while (iterator.hasNext()) {
            LivingEntity spawn = (LivingEntity) iterator.next();
            if (this.shouldDelete(spawn)) {
                spawn.m_146870_();
                iterator.remove();
            } else {
                this.checkTarget(spawn);
            }
        }
    }

    public void checkTarget(LivingEntity entity) {
        if (entity instanceof Mob liv) {
            if (liv.getTarget() == null || this.npc.m_217043_().nextInt(100) == 1) {
                liv.setTarget(this.target);
            }
        } else if (entity.getLastHurtByMob() == null || this.npc.m_217043_().nextInt(100) == 1) {
            entity.setLastHurtByMob(this.target);
        }
    }

    public boolean shouldDelete(LivingEntity entity) {
        return !this.npc.isInRange(entity, 60.0) || entity.m_213877_() || entity.getHealth() <= 0.0F || this.despawnOnTargetLost && this.target == null;
    }

    private LivingEntity getTarget() {
        LivingEntity target = this.getTarget(this.npc);
        if (target != null) {
            return target;
        } else {
            for (LivingEntity entity : this.spawned) {
                target = this.getTarget(entity);
                if (target != null) {
                    return target;
                }
            }
            return null;
        }
    }

    private LivingEntity getTarget(LivingEntity entity) {
        if (entity instanceof Mob) {
            this.target = ((Mob) entity).getTarget();
            if (this.target != null && !this.target.m_213877_() && this.target.getHealth() > 0.0F) {
                return this.target;
            }
        }
        this.target = entity.getLastHurtByMob();
        return this.target != null && !this.target.m_213877_() && this.target.getHealth() > 0.0F ? this.target : null;
    }

    private void setTarget(LivingEntity base, LivingEntity target) {
        if (base instanceof Mob) {
            ((Mob) base).setTarget(target);
        } else {
            base.setLastHurtByMob(target);
        }
    }

    @Override
    public boolean aiShouldExecute() {
        if (!this.data.isEmpty() && !this.npc.isKilled()) {
            this.target = this.getTarget();
            if (this.npc.m_217043_().nextInt(30) == 1 && this.spawned.isEmpty()) {
                this.spawned = this.getNearbySpawned();
            }
            if (!this.spawned.isEmpty()) {
                this.checkSpawns();
            }
            return this.target != null;
        } else {
            return false;
        }
    }

    @Override
    public boolean aiContinueExecute() {
        return this.aiShouldExecute();
    }

    @Override
    public void stop() {
        this.reset();
    }

    @Override
    public void aiStartExecuting() {
        this.number = 0;
        for (LivingEntity entity : this.spawned) {
            int i = entity.getPersistentData().getInt("NpcSpawnerNr");
            if (i > this.number) {
                this.number = i;
            }
            this.setTarget(entity, this.npc.m_5448_());
        }
    }

    @Override
    public void reset() {
        this.number = 0;
        if (this.spawned.isEmpty()) {
            this.spawned = this.getNearbySpawned();
        }
        this.target = null;
        this.checkSpawns();
    }

    @Override
    public void killed() {
        this.reset();
    }

    private LivingEntity spawnEntity(CompoundTag compound) {
        if (compound != null && compound.contains("id")) {
            double x = this.npc.m_20185_() + (double) this.xOffset - 0.5 + (double) this.npc.m_217043_().nextFloat();
            double y = this.npc.m_20186_() + (double) this.yOffset;
            double z = this.npc.m_20189_() + (double) this.zOffset - 0.5 + (double) this.npc.m_217043_().nextFloat();
            Entity entity = SPacketToolMobSpawner.spawnClone(compound, x, y, z, this.npc.m_9236_());
            if (entity != null && entity instanceof LivingEntity living) {
                living.getPersistentData().putString("NpcSpawnerId", this.id);
                living.getPersistentData().putInt("NpcSpawnerNr", this.number);
                this.setTarget(living, this.npc.m_5448_());
                living.m_6034_(x, y, z);
                if (living instanceof EntityNPCInterface snpc) {
                    snpc.stats.spawnCycle = 4;
                    snpc.stats.respawnTime = 0;
                    snpc.ais.returnToStart = false;
                }
                this.spawned.add(living);
                return living;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private CompoundTag getCompound(int i) {
        for (Entry<Integer, CloneSpawnData> entry : this.data.entrySet()) {
            if (i <= (Integer) entry.getKey()) {
                CompoundTag compound = ((CloneSpawnData) entry.getValue()).getCompound();
                if (compound != null && compound.contains("id")) {
                    this.number = (Integer) entry.getKey() + 1;
                    return compound;
                }
            }
        }
        return null;
    }

    private List<LivingEntity> getNearbySpawned() {
        List<LivingEntity> spawnList = new ArrayList();
        for (LivingEntity entity : this.npc.m_9236_().m_45976_(LivingEntity.class, this.npc.m_20191_().inflate(40.0, 40.0, 40.0))) {
            if (entity.getPersistentData().getString("NpcSpawnerId").equals(this.id) && !entity.m_213877_()) {
                spawnList.add(entity);
            }
        }
        return spawnList;
    }

    public boolean isOnCooldown(String name) {
        if (!this.cooldown.containsKey(name)) {
            return false;
        } else {
            long time = (Long) this.cooldown.get(name);
            return System.currentTimeMillis() < time + 1200000L;
        }
    }

    @Override
    public IEntityLiving spawnEntity(int i) {
        CompoundTag compound = this.getCompound(i);
        if (compound == null) {
            return null;
        } else {
            LivingEntity base = this.spawnEntity(compound);
            return base == null ? null : (IEntityLiving) NpcAPI.Instance().getIEntity(base);
        }
    }

    @Override
    public void removeAllSpawned() {
        for (LivingEntity entity : this.spawned) {
            entity.m_146870_();
        }
        this.spawned = new ArrayList();
    }

    @Override
    public int getType() {
        return 6;
    }
}