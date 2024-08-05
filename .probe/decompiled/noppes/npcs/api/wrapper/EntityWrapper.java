package noppes.npcs.api.wrapper;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.INbt;
import noppes.npcs.api.IPos;
import noppes.npcs.api.IRayTrace;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IEntityItem;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.mixin.EntityIMixin;

public class EntityWrapper<T extends Entity> implements IEntity {

    protected T entity;

    private Map<String, Object> tempData = new HashMap();

    private IWorld levelWrapper;

    private final IData tempdata = new IData() {

        @Override
        public void put(String key, Object value) {
            EntityWrapper.this.tempData.put(key, value);
        }

        @Override
        public Object get(String key) {
            return EntityWrapper.this.tempData.get(key);
        }

        @Override
        public void remove(String key) {
            EntityWrapper.this.tempData.remove(key);
        }

        @Override
        public boolean has(String key) {
            return EntityWrapper.this.tempData.containsKey(key);
        }

        @Override
        public void clear() {
            EntityWrapper.this.tempData.clear();
        }

        @Override
        public String[] getKeys() {
            return (String[]) EntityWrapper.this.tempData.keySet().toArray(new String[EntityWrapper.this.tempData.size()]);
        }
    };

    private final IData storeddata = new IData() {

        @Override
        public void put(String key, Object value) {
            CompoundTag compound = this.getStoredCompound();
            if (value instanceof Number) {
                compound.putDouble(key, ((Number) value).doubleValue());
            } else if (value instanceof String) {
                compound.putString(key, (String) value);
            }
            this.saveStoredCompound(compound);
        }

        @Override
        public Object get(String key) {
            CompoundTag compound = this.getStoredCompound();
            if (!compound.contains(key)) {
                return null;
            } else {
                Tag base = compound.get(key);
                return base instanceof NumericTag ? ((NumericTag) base).getAsDouble() : base.getAsString();
            }
        }

        @Override
        public void remove(String key) {
            CompoundTag compound = this.getStoredCompound();
            compound.remove(key);
            this.saveStoredCompound(compound);
        }

        @Override
        public boolean has(String key) {
            return this.getStoredCompound().contains(key);
        }

        @Override
        public void clear() {
            EntityWrapper.this.entity.getPersistentData().remove("CNPCStoredData");
        }

        private CompoundTag getStoredCompound() {
            CompoundTag compound = EntityWrapper.this.entity.getPersistentData().getCompound("CNPCStoredData");
            if (compound == null) {
                EntityWrapper.this.entity.getPersistentData().put("CNPCStoredData", compound = new CompoundTag());
            }
            return compound;
        }

        private void saveStoredCompound(CompoundTag compound) {
            EntityWrapper.this.entity.getPersistentData().put("CNPCStoredData", compound);
        }

        @Override
        public String[] getKeys() {
            CompoundTag compound = this.getStoredCompound();
            return (String[]) compound.getAllKeys().toArray(new String[compound.getAllKeys().size()]);
        }
    };

    public EntityWrapper(T entity) {
        this.entity = entity;
        this.levelWrapper = NpcAPI.Instance().getIWorld((ServerLevel) entity.level());
    }

    @Override
    public double getX() {
        return this.entity.getX();
    }

    @Override
    public void setX(double x) {
        this.entity.setPos(x, this.entity.getY(), this.entity.getZ());
    }

    @Override
    public double getY() {
        return this.entity.getY();
    }

    @Override
    public void setY(double y) {
        this.entity.setPos(this.entity.getX(), y, this.entity.getZ());
    }

    @Override
    public double getZ() {
        return this.entity.getZ();
    }

    @Override
    public void setZ(double z) {
        this.entity.setPos(this.entity.getX(), this.entity.getY(), z);
    }

    @Override
    public int getBlockX() {
        return Mth.floor(this.entity.getX());
    }

    @Override
    public int getBlockY() {
        return Mth.floor(this.entity.getY());
    }

    @Override
    public int getBlockZ() {
        return Mth.floor(this.entity.getZ());
    }

    @Override
    public String getEntityName() {
        String s = this.entity.getType().getDescriptionId();
        return Language.getInstance().getOrDefault(s);
    }

    @Override
    public String getName() {
        return this.entity.getName().getString();
    }

    @Override
    public void setName(String name) {
        this.entity.setCustomName(Component.literal(name));
    }

    @Override
    public boolean hasCustomName() {
        return this.entity.hasCustomName();
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.entity.setPos(x, y, z);
    }

    @Override
    public IWorld getWorld() {
        if (this.entity.level() != this.levelWrapper.getMCLevel()) {
            this.levelWrapper = NpcAPI.Instance().getIWorld((ServerLevel) this.entity.level());
        }
        return this.levelWrapper;
    }

    @Override
    public boolean isAlive() {
        return this.entity.isAlive();
    }

    @Override
    public IData getTempdata() {
        return this.tempdata;
    }

    @Override
    public IData getStoreddata() {
        return this.storeddata;
    }

    @Override
    public long getAge() {
        return (long) this.entity.tickCount;
    }

    @Override
    public void damage(float amount) {
        this.entity.hurt(this.entity.damageSources().genericKill(), amount);
    }

    @Override
    public void despawn() {
        this.entity.discard();
    }

    @Override
    public void spawn() {
        if (this.levelWrapper.getMCLevel().getEntity(this.entity.getUUID()) != null) {
            throw new CustomNPCsException("Entity is already spawned");
        } else {
            ((EntityIMixin) this.entity).removal(null);
            this.levelWrapper.getMCLevel().addFreshEntity(this.entity);
        }
    }

    @Override
    public void kill() {
        this.entity.kill();
    }

    @Override
    public boolean inWater() {
        return this.entity.isInWater();
    }

    @Override
    public boolean inLava() {
        return this.entity.isInLava();
    }

    @Override
    public boolean inFire() {
        return this.entity.level().m_45556_(this.entity.getBoundingBox()).anyMatch(state -> state.m_204336_(BlockTags.FIRE));
    }

    @Override
    public boolean isBurning() {
        return this.entity.isOnFire();
    }

    @Override
    public void setBurning(int ticks) {
        this.entity.setRemainingFireTicks(ticks);
    }

    @Override
    public void extinguish() {
        this.entity.clearFire();
    }

    @Override
    public String getTypeName() {
        return this.entity.getEncodeId();
    }

    @Override
    public IEntityItem dropItem(IItemStack item) {
        return (IEntityItem) NpcAPI.Instance().getIEntity(this.entity.spawnAtLocation(item.getMCItemStack(), 0.0F));
    }

    @Override
    public IEntity[] getRiders() {
        List<Entity> list = this.entity.getPassengers();
        IEntity[] riders = new IEntity[list.size()];
        for (int i = 0; i < list.size(); i++) {
            riders[i] = NpcAPI.Instance().getIEntity((Entity) list.get(i));
        }
        return riders;
    }

    @Override
    public IRayTrace rayTraceBlock(double distance, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox) {
        Vec3 vec3d = this.entity.getEyePosition(1.0F);
        Vec3 vec3d1 = this.entity.getViewVector(1.0F);
        Vec3 vec3d2 = vec3d.add(vec3d1.x * distance, vec3d1.y * distance, vec3d1.z * distance);
        HitResult result = this.entity.level().m_45547_(new ClipContext(vec3d, vec3d2, ClipContext.Block.OUTLINE, stopOnLiquid ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, this.entity));
        if (result.getType() == HitResult.Type.MISS) {
            return null;
        } else {
            BlockHitResult br = (BlockHitResult) result;
            return new RayTraceWrapper(NpcAPI.Instance().getIBlock(this.entity.level(), br.getBlockPos()), br.getDirection().get3DDataValue());
        }
    }

    @Override
    public IEntity[] rayTraceEntities(double distance, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox) {
        Vec3 vec3d = this.entity.getEyePosition(1.0F);
        Vec3 vec3d1 = this.entity.getViewVector(1.0F);
        Vec3 vec3d2 = vec3d.add(vec3d1.x * distance, vec3d1.y * distance, vec3d1.z * distance);
        HitResult result = this.entity.level().m_45547_(new ClipContext(vec3d, vec3d2, ClipContext.Block.COLLIDER, stopOnLiquid ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, this.entity));
        if (result.getType() != HitResult.Type.MISS) {
            vec3d2 = result.getLocation();
        }
        return this.findEntityOnPath(distance, vec3d, vec3d2);
    }

    private IEntity[] findEntityOnPath(double distance, Vec3 vec3d, Vec3 vec3d1) {
        List<Entity> list = this.entity.level().m_45933_(this.entity, this.entity.getBoundingBox().inflate(distance));
        List<IEntity> result = new ArrayList();
        for (Entity entity1 : list) {
            if (entity1 != this.entity) {
                AABB axisalignedbb = entity1.getBoundingBox().inflate((double) entity1.getPickRadius());
                Optional<Vec3> optional = axisalignedbb.clip(vec3d, vec3d1);
                if (optional.isPresent()) {
                    result.add(NpcAPI.Instance().getIEntity(entity1));
                }
            }
        }
        result.sort((o1, o2) -> {
            double d1 = this.entity.distanceToSqr(o1.getMCEntity());
            double d2 = this.entity.distanceToSqr(o2.getMCEntity());
            if (d1 == d2) {
                return 0;
            } else {
                return d1 > d2 ? 1 : -1;
            }
        });
        return (IEntity[]) result.toArray(new IEntity[result.size()]);
    }

    @Override
    public IEntity[] getAllRiders() {
        List<Entity> list = ImmutableList.copyOf(this.entity.getIndirectPassengers());
        IEntity[] riders = new IEntity[list.size()];
        for (int i = 0; i < list.size(); i++) {
            riders[i] = NpcAPI.Instance().getIEntity((Entity) list.get(i));
        }
        return riders;
    }

    @Override
    public void addRider(IEntity entity) {
        if (entity != null) {
            entity.getMCEntity().startRiding(this.entity, true);
        }
    }

    @Override
    public void clearRiders() {
        this.entity.ejectPassengers();
    }

    @Override
    public IEntity getMount() {
        return NpcAPI.Instance().getIEntity(this.entity.getVehicle());
    }

    @Override
    public void setMount(IEntity entity) {
        if (entity == null) {
            this.entity.stopRiding();
        } else {
            this.entity.startRiding(entity.getMCEntity(), true);
        }
    }

    @Override
    public void setRotation(float rotation) {
        this.entity.setYRot(rotation);
    }

    @Override
    public float getRotation() {
        return this.entity.getYRot();
    }

    @Override
    public void setPitch(float rotation) {
        this.entity.setXRot(rotation);
    }

    @Override
    public float getPitch() {
        return this.entity.getXRot();
    }

    @Override
    public void knockback(int power, float direction) {
        float v = direction * (float) Math.PI / 180.0F;
        this.entity.push((double) (-Mth.sin(v) * (float) power), 0.1 + (double) ((float) power * 0.04F), (double) (Mth.cos(v) * (float) power));
        this.entity.setDeltaMovement(this.entity.getDeltaMovement().multiply(0.6, 1.0, 0.6));
        this.entity.hurtMarked = true;
    }

    @Override
    public boolean isSneaking() {
        return this.entity.isCrouching();
    }

    @Override
    public boolean isSprinting() {
        return this.entity.isSprinting();
    }

    @Override
    public T getMCEntity() {
        return this.entity;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public boolean typeOf(int type) {
        return type == this.getType();
    }

    @Override
    public String getUUID() {
        return this.entity.getUUID().toString();
    }

    @Override
    public String generateNewUUID() {
        UUID id = UUID.randomUUID();
        this.entity.setUUID(id);
        return id.toString();
    }

    @Override
    public INbt getNbt() {
        return NpcAPI.Instance().getINbt(this.entity.getPersistentData());
    }

    @Override
    public void storeAsClone(int tab, String name) {
        CompoundTag compound = new CompoundTag();
        if (!this.entity.saveAsPassenger(compound)) {
            throw new CustomNPCsException("Cannot store dead entities");
        } else {
            ServerCloneController.Instance.addClone(compound, name, tab);
        }
    }

    @Override
    public INbt getEntityNbt() {
        CompoundTag compound = new CompoundTag();
        this.entity.saveWithoutId(compound);
        ResourceLocation resourcelocation = EntityType.getKey(this.entity.getType());
        if (this.getType() == 1) {
            resourcelocation = new ResourceLocation("player");
        }
        if (resourcelocation != null) {
            compound.putString("id", resourcelocation.toString());
        }
        return NpcAPI.Instance().getINbt(compound);
    }

    @Override
    public void setEntityNbt(INbt nbt) {
        this.entity.load(nbt.getMCNBT());
    }

    @Override
    public void playAnimation(int type) {
        this.levelWrapper.getMCLevel().getChunkSource().broadcastAndSend(this.entity, new ClientboundAnimatePacket(this.entity, type));
    }

    @Override
    public float getHeight() {
        return this.entity.getBbHeight();
    }

    @Override
    public float getEyeHeight() {
        return this.entity.getEyeHeight();
    }

    @Override
    public float getWidth() {
        return this.entity.getBbWidth();
    }

    @Override
    public IPos getPos() {
        return new BlockPosWrapper(this.entity.blockPosition());
    }

    @Override
    public void setPos(IPos pos) {
        this.entity.setPos((double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F));
    }

    @Override
    public String[] getTags() {
        return (String[]) this.entity.getTags().toArray(new String[this.entity.getTags().size()]);
    }

    @Override
    public void addTag(String tag) {
        this.entity.addTag(tag);
    }

    @Override
    public boolean hasTag(String tag) {
        return this.entity.getTags().contains(tag);
    }

    @Override
    public void removeTag(String tag) {
        this.entity.removeTag(tag);
    }

    @Override
    public double getMotionX() {
        return this.entity.getDeltaMovement().x;
    }

    @Override
    public double getMotionY() {
        return this.entity.getDeltaMovement().y;
    }

    @Override
    public double getMotionZ() {
        return this.entity.getDeltaMovement().z;
    }

    @Override
    public void setMotionX(double motion) {
        Vec3 mo = this.entity.getDeltaMovement();
        if (mo.x != motion) {
            this.entity.setDeltaMovement(motion, mo.y, mo.z);
            this.entity.hurtMarked = true;
        }
    }

    @Override
    public void setMotionY(double motion) {
        Vec3 mo = this.entity.getDeltaMovement();
        if (mo.y != motion) {
            this.entity.setDeltaMovement(mo.x, motion, mo.z);
            this.entity.hurtMarked = true;
        }
    }

    @Override
    public void setMotionZ(double motion) {
        Vec3 mo = this.entity.getDeltaMovement();
        if (mo.z != motion) {
            this.entity.setDeltaMovement(mo.x, mo.y, motion);
            this.entity.hurtMarked = true;
        }
    }
}