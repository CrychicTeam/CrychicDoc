package io.redspace.ironsspellbooks.entity.spells.wall_of_fire;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import io.redspace.ironsspellbooks.entity.spells.ShieldPart;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.network.NetworkHooks;

public class WallOfFireEntity extends AbstractShieldEntity implements IEntityAdditionalSpawnData {

    protected ShieldPart[] subEntities;

    protected List<Vec3> partPositions = new ArrayList();

    protected List<Vec3> anchorPoints = new ArrayList();

    @Nullable
    private UUID ownerUUID;

    @Nullable
    private Entity cachedOwner;

    protected float damage;

    protected int lifetime = 240;

    public WallOfFireEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.subEntities = new ShieldPart[0];
    }

    @Override
    public void takeDamage(DamageSource source, float amount, @Nullable Vec3 location) {
    }

    public WallOfFireEntity(Level level, Entity owner, List<Vec3> anchors, float damage) {
        this(EntityRegistry.WALL_OF_FIRE_ENTITY.get(), level);
        this.anchorPoints = anchors;
        this.createShield();
        this.damage = damage;
        this.setOwner(owner);
    }

    @Override
    public void tick() {
        if (this.anchorPoints.size() > 1 && this.subEntities.length > 1) {
            int i = 0;
            for (int subEntitiesLength = this.subEntities.length; i < subEntitiesLength; i++) {
                PartEntity<?> subEntity = this.subEntities[i];
                Vec3 pos = (Vec3) this.partPositions.get(i);
                subEntity.m_146884_(pos);
                subEntity.f_19854_ = pos.x;
                subEntity.f_19855_ = pos.y;
                subEntity.f_19856_ = pos.z;
                subEntity.f_19790_ = pos.x;
                subEntity.f_19791_ = pos.y;
                subEntity.f_19792_ = pos.z;
                if (this.f_19853_.isClientSide && i < subEntitiesLength - 1) {
                    for (int j = 0; j < 1; j++) {
                        Vec3 offset = ((Vec3) this.partPositions.get(i + 1)).subtract(pos).scale((double) Utils.random.nextFloat()).add(Utils.getRandomVec3(0.1));
                        this.f_19853_.addParticle(ParticleHelper.FIRE, pos.x + offset.x, pos.y + (double) Utils.random.nextFloat() * 0.25, pos.z + offset.z, 0.0, Math.random() * 0.3, 0.0);
                    }
                } else {
                    for (LivingEntity livingentity : this.f_19853_.m_45976_(LivingEntity.class, subEntity.m_20191_().inflate(0.2, 0.0, 0.2))) {
                        if (livingentity != this.getOwner()) {
                            DamageSources.applyDamage(livingentity, this.damage, SpellRegistry.WALL_OF_FIRE_SPELL.get().getDamageSource(this, this.getOwner()));
                        }
                    }
                }
            }
            if (!this.f_19853_.isClientSide && --this.lifetime < 0) {
                this.m_146870_();
            }
        } else {
            this.m_146870_();
        }
    }

    @Override
    public void createShield() {
        float height = 3.0F;
        float step = 0.8F;
        List<ShieldPart> entitiesList = new ArrayList();
        for (int i = 0; i < this.anchorPoints.size() - 1; i++) {
            Vec3 start = (Vec3) this.anchorPoints.get(i);
            Vec3 end = (Vec3) this.anchorPoints.get(i + 1);
            Vec3 dirVec = end.subtract(start).normalize().scale((double) step);
            int steps = (int) ((start.distanceTo(end) + 0.5) / (double) step);
            for (int currentStep = 0; currentStep < steps; currentStep++) {
                ShieldPart part = new ShieldPart(this, "part" + i * steps + currentStep, 0.55F, height, false);
                double x = start.x + dirVec.x * (double) currentStep;
                double y = start.y + dirVec.y * (double) currentStep;
                double z = start.z + dirVec.z * (double) currentStep;
                double groundY = Utils.moveToRelativeGroundLevel(this.f_19853_, new Vec3(x, y, z), 4, 4).y;
                Vec3 pos = new Vec3(x, groundY, z);
                this.partPositions.add(pos);
                entitiesList.add(part);
            }
        }
        this.subEntities = (ShieldPart[]) entitiesList.toArray(this.subEntities);
    }

    public void setOwner(@Nullable Entity pOwner) {
        if (pOwner != null) {
            this.ownerUUID = pOwner.getUUID();
            this.cachedOwner = pOwner;
        }
    }

    @Nullable
    public Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        } else if (this.ownerUUID != null && this.f_19853_ instanceof ServerLevel) {
            this.cachedOwner = ((ServerLevel) this.f_19853_).getEntity(this.ownerUUID);
            return this.cachedOwner;
        } else {
            return null;
        }
    }

    @Override
    public PartEntity<?>[] getParts() {
        return this.subEntities;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        if (this.ownerUUID != null) {
            compoundTag.putUUID("Owner", this.ownerUUID);
        }
        compoundTag.putInt("lifetime", this.lifetime);
        ListTag anchors = new ListTag();
        for (Vec3 vec : this.anchorPoints) {
            CompoundTag anchor = new CompoundTag();
            anchor.putFloat("x", (float) vec.x);
            anchor.putFloat("y", (float) vec.y);
            anchor.putFloat("z", (float) vec.z);
            anchors.add(anchor);
        }
        compoundTag.put("Anchors", anchors);
        super.addAdditionalSaveData(compoundTag);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.hasUUID("Owner")) {
            this.ownerUUID = compoundTag.getUUID("Owner");
        }
        if (compoundTag.contains("lifetime")) {
            this.lifetime = compoundTag.getInt("lifetime");
        }
        this.anchorPoints = new ArrayList();
        if (compoundTag.contains("Anchors", 9)) {
            for (Tag tag : (ListTag) compoundTag.get("Anchors")) {
                if (tag instanceof CompoundTag anchor) {
                    this.anchorPoints.add(new Vec3(anchor.getDouble("x"), anchor.getDouble("y"), anchor.getDouble("z")));
                }
            }
        }
        super.readAdditionalSaveData(compoundTag);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.anchorPoints.size());
        for (Vec3 vec : this.anchorPoints) {
            buffer.writeFloat((float) vec.x);
            buffer.writeFloat((float) vec.y);
            buffer.writeFloat((float) vec.z);
        }
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.anchorPoints = new ArrayList();
        int length = additionalData.readInt();
        for (int i = 0; i < length; i++) {
            this.anchorPoints.add(new Vec3((double) additionalData.readFloat(), (double) additionalData.readFloat(), (double) additionalData.readFloat()));
        }
        this.createShield();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}