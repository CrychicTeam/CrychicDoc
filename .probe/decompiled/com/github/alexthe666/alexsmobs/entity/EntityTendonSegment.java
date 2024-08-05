package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.entity.util.TendonWhipUtil;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class EntityTendonSegment extends Entity {

    private static final EntityDataAccessor<Optional<UUID>> CREATOR_ID = SynchedEntityData.defineId(EntityTendonSegment.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> FROM_ID = SynchedEntityData.defineId(EntityTendonSegment.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> TARGET_COUNT = SynchedEntityData.defineId(EntityTendonSegment.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> CURRENT_TARGET_ID = SynchedEntityData.defineId(EntityTendonSegment.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> PROGRESS = SynchedEntityData.defineId(EntityTendonSegment.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(EntityTendonSegment.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> RETRACTING = SynchedEntityData.defineId(EntityTendonSegment.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HAS_CLAW = SynchedEntityData.defineId(EntityTendonSegment.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HAS_GLINT = SynchedEntityData.defineId(EntityTendonSegment.class, EntityDataSerializers.BOOLEAN);

    private List<Entity> previouslyTouched = new ArrayList();

    private boolean hasTouched = false;

    private boolean hasChained = false;

    public float prevProgress = 0.0F;

    public static final float MAX_EXTEND_TIME = 3.0F;

    public EntityTendonSegment(EntityType<?> type, Level level) {
        super(type, level);
    }

    public EntityTendonSegment(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AMEntityRegistry.TENDON_SEGMENT.get(), world);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(CREATOR_ID, Optional.empty());
        this.f_19804_.define(FROM_ID, -1);
        this.f_19804_.define(TARGET_COUNT, 0);
        this.f_19804_.define(CURRENT_TARGET_ID, -1);
        this.f_19804_.define(PROGRESS, 0.0F);
        this.f_19804_.define(DAMAGE, 5.0F);
        this.f_19804_.define(RETRACTING, false);
        this.f_19804_.define(HAS_CLAW, true);
        this.f_19804_.define(HAS_GLINT, false);
    }

    @Override
    public void tick() {
        float progress = this.getProgress();
        this.prevProgress = progress;
        if (this.f_19797_ < 1) {
            this.onJoinWorld();
        } else if (this.f_19797_ == 1 && !this.m_9236_().isClientSide) {
            this.m_5496_(AMSoundRegistry.TENDON_WHIP.get(), 1.0F, 0.8F + this.f_19796_.nextFloat() * 0.4F);
        }
        super.tick();
        Entity creator = this.getCreatorEntity();
        Entity current = this.getToEntity();
        if (progress < 3.0F && !this.isRetracting()) {
            this.setProgress(progress + 1.0F);
        }
        if (progress > 0.0F && this.isRetracting()) {
            this.setProgress(progress - 1.0F);
        }
        if (progress == 0.0F && this.isRetracting()) {
            if (this.getFromEntity() instanceof EntityTendonSegment tendonSegment) {
                tendonSegment.setRetracting(true);
                this.updateLastTendon(tendonSegment);
            } else {
                this.updateLastTendon(null);
            }
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        if (creator instanceof LivingEntity && current != null) {
            Vec3 target = new Vec3(current.getX(), current.getY(0.4F), current.getZ());
            Vec3 lerp = target.subtract(this.m_20182_());
            this.m_20256_(lerp.scale(0.5));
            if (!this.m_9236_().isClientSide && !this.hasTouched && progress >= 3.0F) {
                this.hasTouched = true;
                Entity entity = this.getCreatorEntity();
                if (entity instanceof LivingEntity && current != creator && current.hurt(this.m_269291_().mobProjectile(this, (LivingEntity) entity), (float) this.getDamageFor((LivingEntity) creator, (LivingEntity) entity))) {
                    this.m_19970_((LivingEntity) creator, entity);
                }
            }
        }
        Vec3 vector3d = this.m_20184_();
        if (!this.m_9236_().isClientSide && !this.hasChained) {
            if (this.getTargetsHit() > 3) {
                this.setRetracting(true);
            } else if (creator instanceof LivingEntity && this.getProgress() >= 3.0F) {
                Entity closestValid = null;
                for (Entity entity : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(8.0))) {
                    if (!entity.equals(creator) && !this.previouslyTouched.contains(entity) && this.isValidTarget((LivingEntity) creator, entity) && this.hasLineOfSight(entity) && (closestValid == null || this.m_20270_(entity) < this.m_20270_(closestValid))) {
                        closestValid = entity;
                    }
                }
                if (closestValid != null) {
                    this.createChain(closestValid);
                    this.hasChained = true;
                } else {
                    this.setRetracting(true);
                }
            }
        }
        double d0 = this.m_20185_() + vector3d.x;
        double d1 = this.m_20186_() + vector3d.y;
        double d2 = this.m_20189_() + vector3d.z;
        this.m_20256_(vector3d.scale(0.99F));
        this.m_6034_(d0, d1, d2);
    }

    private boolean isValidTarget(LivingEntity creator, Entity entity) {
        return !creator.m_7307_(entity) && !entity.isAlliedTo(creator) && entity instanceof Mob ? true : creator.getLastHurtMob() != null && creator.getLastHurtMob().m_20148_().equals(entity.getUUID()) || creator.getLastHurtByMob() != null && creator.getLastHurtByMob().m_20148_().equals(entity.getUUID());
    }

    private double getDamageFor(LivingEntity creator, LivingEntity entity) {
        ItemStack stack = creator.getItemInHand(InteractionHand.MAIN_HAND).is(AMItemRegistry.TENDON_WHIP.get()) ? creator.getItemInHand(InteractionHand.MAIN_HAND) : creator.getItemInHand(InteractionHand.OFF_HAND);
        double dmg = (double) this.getBaseDamage();
        if (stack.is(AMItemRegistry.TENDON_WHIP.get())) {
            dmg += (double) EnchantmentHelper.getDamageBonus(stack, entity.getMobType());
        }
        return dmg;
    }

    private double getDamageForItem(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> map = itemStack.getAttributeModifiers(EquipmentSlot.MAINHAND);
        if (map.isEmpty()) {
            return 0.0;
        } else {
            double d = 0.0;
            for (AttributeModifier mod : map.get(Attributes.ATTACK_DAMAGE)) {
                d += mod.getAmount();
            }
            return d;
        }
    }

    private boolean hasLineOfSight(Entity entity) {
        if (entity.level() != this.m_9236_()) {
            return false;
        } else {
            Vec3 vec3 = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
            Vec3 vec31 = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
            return vec31.distanceTo(vec3) > 128.0 ? false : this.m_9236_().m_45547_(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
        }
    }

    private void updateLastTendon(EntityTendonSegment lastTendon) {
        Entity creator = this.getCreatorEntity();
        if (creator == null) {
            creator = this.m_9236_().m_46003_(this.getCreatorEntityUUID());
        }
        if (creator instanceof LivingEntity) {
            TendonWhipUtil.setLastTendon((LivingEntity) creator, lastTendon);
        }
    }

    private void createChain(Entity closestValid) {
        this.f_19804_.set(HAS_CLAW, false);
        EntityTendonSegment child = AMEntityRegistry.TENDON_SEGMENT.get().create(this.m_9236_());
        child.previouslyTouched = new ArrayList(this.previouslyTouched);
        child.previouslyTouched.add(closestValid);
        child.setCreatorEntityUUID(this.getCreatorEntityUUID());
        child.setFromEntityID(this.m_19879_());
        child.setToEntityID(closestValid.getId());
        child.m_6034_(closestValid.getX(), closestValid.getY(0.4F), closestValid.getZ());
        child.setTargetsHit(this.getTargetsHit() + 1);
        this.updateLastTendon(child);
        child.setHasGlint(this.hasGlint());
        this.m_9236_().m_7967_(child);
    }

    private void onJoinWorld() {
        Entity creator = this.getCreatorEntity();
        if (creator == null) {
            creator = this.m_9236_().m_46003_(this.getCreatorEntityUUID());
        }
        Entity prior = this.getFromEntity();
        if (creator instanceof Player player) {
            ItemStack stack = player.m_21120_(InteractionHand.MAIN_HAND).is(AMItemRegistry.TENDON_WHIP.get()) ? player.m_21120_(InteractionHand.MAIN_HAND) : player.m_21120_(InteractionHand.OFF_HAND);
            if (stack.is(AMItemRegistry.TENDON_WHIP.get())) {
                this.setHasGlint(stack.hasFoil());
            }
            float dmg = 2.0F;
            if (prior instanceof EntityTendonSegment) {
                dmg = Math.max(((EntityTendonSegment) prior).getBaseDamage() - 1.0F, 2.0F);
            } else {
                dmg = (float) this.getDamageForItem(stack);
            }
            this.f_19804_.set(DAMAGE, dmg);
        }
    }

    private float getBaseDamage() {
        return this.f_19804_.get(DAMAGE);
    }

    public UUID getCreatorEntityUUID() {
        return (UUID) this.f_19804_.get(CREATOR_ID).orElse(null);
    }

    public void setCreatorEntityUUID(UUID id) {
        this.f_19804_.set(CREATOR_ID, Optional.ofNullable(id));
    }

    public Entity getCreatorEntity() {
        UUID uuid = this.getCreatorEntityUUID();
        return uuid != null && !this.m_9236_().isClientSide ? ((ServerLevel) this.m_9236_()).getEntity(uuid) : null;
    }

    public int getFromEntityID() {
        return this.f_19804_.get(FROM_ID);
    }

    public void setFromEntityID(int id) {
        this.f_19804_.set(FROM_ID, id);
    }

    public Entity getFromEntity() {
        return this.getFromEntityID() == -1 ? null : this.m_9236_().getEntity(this.getFromEntityID());
    }

    public int getToEntityID() {
        return this.f_19804_.get(CURRENT_TARGET_ID);
    }

    public void setToEntityID(int id) {
        this.f_19804_.set(CURRENT_TARGET_ID, id);
    }

    public Entity getToEntity() {
        return this.getToEntityID() == -1 ? null : this.m_9236_().getEntity(this.getToEntityID());
    }

    public int getTargetsHit() {
        return this.f_19804_.get(TARGET_COUNT);
    }

    public void setTargetsHit(int i) {
        this.f_19804_.set(TARGET_COUNT, i);
    }

    public float getProgress() {
        return this.f_19804_.get(PROGRESS);
    }

    public void setProgress(float progress) {
        this.f_19804_.set(PROGRESS, progress);
    }

    public boolean isRetracting() {
        return this.f_19804_.get(RETRACTING);
    }

    public void setRetracting(boolean retract) {
        this.f_19804_.set(RETRACTING, retract);
    }

    public boolean hasGlint() {
        return this.f_19804_.get(HAS_GLINT);
    }

    public void setHasGlint(boolean glint) {
        this.f_19804_.set(HAS_GLINT, glint);
    }

    public boolean hasClaw() {
        return this.f_19804_.get(HAS_CLAW);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
    }

    public boolean isCreator(Entity mob) {
        return this.getCreatorEntityUUID() != null && mob.getUUID().equals(this.getCreatorEntityUUID());
    }
}