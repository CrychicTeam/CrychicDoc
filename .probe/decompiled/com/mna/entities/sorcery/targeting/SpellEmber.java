package com.mna.entities.sorcery.targeting;

import com.mna.ManaAndArtifice;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.entities.EntityInit;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.SummonUtils;
import com.mna.tools.math.MathUtils;
import com.mojang.math.Axis;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SpellEmber extends Entity {

    private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(SpellEmber.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<CompoundTag> SPELL_RECIPE = SynchedEntityData.defineId(SpellEmber.class, EntityDataSerializers.COMPOUND_TAG);

    private static final EntityDataAccessor<Integer> ANGLE = SynchedEntityData.defineId(SpellEmber.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(SpellEmber.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Byte> TARGET_DISTANCE = SynchedEntityData.defineId(SpellEmber.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Float> HOMING_STRENGTH = SynchedEntityData.defineId(SpellEmber.class, EntityDataSerializers.FLOAT);

    private ISpellDefinition cachedRecipe;

    private LivingEntity cachedOwner;

    private LivingEntity target;

    private Vec3 movement;

    public SpellEmber(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SpellEmber(Level pLevel, LivingEntity owner, ISpellDefinition recipe, int angle) {
        super(EntityInit.SPELL_EMBER.get(), pLevel);
        this.m_146884_(owner.m_20182_());
        this.f_19804_.set(OWNER_ID, owner.m_19879_());
        CompoundTag recipeCompound = new CompoundTag();
        recipe.writeToNBT(recipeCompound);
        this.f_19804_.set(SPELL_RECIPE, recipeCompound);
        this.f_19804_.set(ANGLE, angle);
        this.m_20242_(true);
        SummonUtils.addTrackedEntity(owner, this, "mna:ember_ids");
        SummonUtils.tagAsSummon(this, owner);
    }

    public void setHomingStrength(float strength) {
        this.f_19804_.set(HOMING_STRENGTH, MathUtils.clamp01(strength));
    }

    public void setTargetDistance(byte distance) {
        this.f_19804_.set(TARGET_DISTANCE, distance);
    }

    @Override
    public void tick() {
        if (!this.m_9236_().isClientSide()) {
            if (this.getOwner() == null || !this.getOwner().isAlive() || this.getOwner().m_9236_() != this.m_9236_() || this.getSpell() == null || this.getTarget() != null && this.f_19797_ >= 60) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
                return;
            }
            if (this.getTarget() == null) {
                if (!this.m_9236_().isClientSide() && !SummonUtils.isTracked(this.getOwner(), this, "mna:ember_ids")) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                    return;
                }
                Vec3 desiredPos = this.getPositionAroundOwnerBasedOnLocation();
                this.m_146884_(desiredPos);
                if (this.m_9236_().getGameTime() % 20L == 0L && SummonUtils.isFirstTrackedEntity(this.getOwner(), "mna:ember_ids", this)) {
                    LivingEntity owner = this.getOwner();
                    if (owner instanceof Player) {
                        owner.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                            if (m.getEmberCooldown() == 0) {
                                this.target();
                            }
                            if (this.getTarget() != null) {
                                m.setEmberCooldown(20);
                            }
                        });
                    } else {
                        long last_ember = owner.getPersistentData().getLong("mna_last_ember_time");
                        long cur_time = this.m_9236_().getGameTime();
                        long last_ember_time = cur_time - last_ember;
                        if (last_ember_time >= 20L) {
                            this.target();
                            owner.getPersistentData().putLong("mna_last_ember_time", cur_time);
                        }
                    }
                }
            } else {
                if (!this.getTarget().isAlive()) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                    return;
                }
                Vec3 myPos = this.m_20182_();
                Vec3 theirPos = this.getTarget().m_20182_().add(0.0, (double) (this.getTarget().m_20206_() / 2.0F), 0.0);
                if (this.movement == null) {
                    this.movement = theirPos.subtract(myPos).normalize();
                } else {
                    float tickTheta = 7.5F * MathUtils.clamp01(this.f_19804_.get(HOMING_STRENGTH));
                    if (tickTheta > 0.0F) {
                        Vec3 desiredHeading = theirPos.subtract(myPos).normalize();
                        this.movement = MathUtils.rotateTowards(this.movement, desiredHeading, tickTheta).normalize().scale(0.5);
                    }
                }
                this.m_146884_(this.m_20182_().add(this.movement));
                if (!this.m_9236_().isClientSide()) {
                    SpellContext context = new SpellContext(this.m_9236_(), this.getSpell(), this);
                    SpellSource source = new SpellSource(this.getOwner(), InteractionHand.MAIN_HAND);
                    if (myPos.distanceTo(theirPos) < 0.5) {
                        this.m_142687_(Entity.RemovalReason.DISCARDED);
                        if (this.getSpell() != null && this.getOwner() != null) {
                            this.getSpell().iterateComponents(c -> SpellCaster.ApplyComponents(this.getSpell(), source, new SpellTarget(this.getTarget()), context));
                        }
                    }
                }
            }
        }
        if (this.m_9236_().isClientSide()) {
            for (int i = 0; i < 2; i++) {
                switch(this.getSpell().getHighestAffinity()) {
                    case EARTH:
                        this.m_9236_().addParticle(new MAParticleType(ParticleInit.DUST.get()), this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
                        break;
                    case ENDER:
                        this.m_9236_().addParticle(new MAParticleType(ParticleInit.ENDER_VELOCITY.get()), this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
                        break;
                    case FIRE:
                        this.m_9236_().addParticle(new MAParticleType(ParticleInit.FLAME.get()), this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
                        break;
                    case HELLFIRE:
                        this.m_9236_().addParticle(new MAParticleType(ParticleInit.HELLFIRE.get()), this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
                        break;
                    case ICE:
                        this.m_9236_().addParticle(new MAParticleType(ParticleInit.FROST.get()), this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
                        break;
                    case LIGHTNING:
                        this.m_9236_().addParticle(new MAParticleType(ParticleInit.LIGHTNING_BOLT.get()), this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_20185_() - 0.25 + Math.random() * 0.5, this.m_20186_() - 0.25 + Math.random() * 0.5, this.m_20189_() - 0.25 + Math.random() * 0.5);
                        break;
                    case WATER:
                        this.m_9236_().addParticle(new MAParticleType(ParticleInit.WATER.get()), this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
                        break;
                    case WIND:
                        this.m_9236_().addParticle(new MAParticleType(ParticleInit.AIR_ORBIT.get()).setScale(0.05F).setColor(30, 30, 30), this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.5, 0.001F, 0.05);
                        break;
                    case ARCANE:
                    default:
                        this.m_9236_().addParticle(new MAParticleType(ParticleInit.ARCANE_MAGELIGHT.get()), this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
                }
            }
        }
        super.tick();
    }

    private void target() {
        this.target = (LivingEntity) this.m_9236_().getEntities(this, this.m_20191_().inflate((double) this.f_19804_.get(TARGET_DISTANCE).byteValue()), e -> {
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(e)) {
                return false;
            } else {
                ClipContext ctx = new ClipContext(this.m_20182_(), e.position().add(0.0, (double) (e.getBbHeight() / 2.0F), 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this);
                return this.m_9236_().m_45547_(ctx).getType() == HitResult.Type.BLOCK ? false : e.isAlive() && e instanceof LivingEntity && !SummonUtils.isTargetFriendly(e, this.getOwner());
            }
        }).stream().map(e -> (LivingEntity) e).findFirst().orElse(null);
        if (this.target != null) {
            this.f_19804_.set(TARGET_ID, this.target.m_19879_());
            this.f_19797_ = 0;
        }
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return this.getTarget() == pEntity;
    }

    private Vec3 getPositionAroundOwnerBasedOnLocation() {
        float angle = (float) (ManaAndArtifice.instance.proxy.getGameTicks() * 3L + (long) this.f_19804_.get(ANGLE).intValue());
        Vec3 targetPos = this.getOwner().m_20182_().add(0.0, (double) (this.getOwner().m_20206_() / 2.0F), 0.0);
        Vector3f offset = new Vector3f(1.0F, 0.0F, 0.0F);
        Quaternionf rotation = Axis.YP.rotationDegrees(angle);
        offset.rotate(rotation);
        offset.normalize();
        return targetPos.add((double) offset.x(), (double) offset.y(), (double) offset.z());
    }

    @Nullable
    private LivingEntity getOwner() {
        if (this.cachedOwner == null) {
            int ownerID = this.f_19804_.get(OWNER_ID);
            if (ownerID > -1) {
                Entity e = this.m_9236_().getEntity(ownerID);
                if (e == null || !(e instanceof LivingEntity)) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                    return null;
                }
                this.cachedOwner = (LivingEntity) e;
            } else {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
        return this.cachedOwner;
    }

    @Nullable
    private LivingEntity getTarget() {
        if (this.target == null) {
            Entity e = this.m_9236_().getEntity(this.f_19804_.get(TARGET_ID));
            if (e instanceof LivingEntity) {
                this.target = (LivingEntity) e;
            }
        }
        return this.target;
    }

    public void setAngle(int angle) {
        this.f_19804_.set(ANGLE, angle);
    }

    private ISpellDefinition getSpell() {
        if (this.cachedRecipe == null) {
            CompoundTag spellTag = this.f_19804_.get(SPELL_RECIPE);
            if (spellTag == null) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            } else {
                this.cachedRecipe = SpellRecipe.fromNBT(spellTag);
                if (!this.cachedRecipe.isValid()) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            }
        }
        return this.cachedRecipe;
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(OWNER_ID, -1);
        this.f_19804_.define(SPELL_RECIPE, new CompoundTag());
        this.f_19804_.define(ANGLE, 0);
        this.f_19804_.define(TARGET_ID, -1);
        this.f_19804_.define(HOMING_STRENGTH, 0.0F);
        this.f_19804_.define(TARGET_DISTANCE, (byte) 16);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("owner")) {
            this.f_19804_.set(OWNER_ID, pCompound.getInt("owner"));
        }
        if (pCompound.contains("spell", 10)) {
            this.f_19804_.set(SPELL_RECIPE, (CompoundTag) pCompound.get("spell"));
        }
        if (pCompound.contains("angle")) {
            this.f_19804_.set(ANGLE, pCompound.getInt("angle"));
        }
        if (pCompound.contains("homingFactor")) {
            this.f_19804_.set(HOMING_STRENGTH, pCompound.getFloat("homingFactor"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        int ownerID = this.f_19804_.get(OWNER_ID);
        if (ownerID > -1) {
            pCompound.putInt("owner", ownerID);
        }
        if (!this.f_19804_.get(SPELL_RECIPE).isEmpty()) {
            pCompound.put("spell", this.f_19804_.get(SPELL_RECIPE));
        }
        pCompound.putInt("angle", this.f_19804_.get(ANGLE));
        pCompound.putFloat("homingFactor", this.f_19804_.get(HOMING_STRENGTH));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}