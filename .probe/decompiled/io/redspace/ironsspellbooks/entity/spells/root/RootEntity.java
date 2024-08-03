package io.redspace.ironsspellbooks.entity.spells.root;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Collections;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RootEntity extends LivingEntity implements GeoEntity, PreventDismount, AntiMagicSusceptible {

    @Nullable
    private LivingEntity owner;

    @Nullable
    private UUID ownerUUID;

    private int duration;

    private boolean playSound = true;

    private LivingEntity target;

    private boolean played = false;

    private final RawAnimation ANIMATION = RawAnimation.begin().thenPlay("emerge");

    private final AnimationController controller = new AnimationController<>(this, "root_controller", 0, this::animationPredicate);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public float getScale() {
        return this.target == null ? 1.0F : this.target.getScale();
    }

    public RootEntity(EntityType<? extends RootEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RootEntity(Level level, LivingEntity owner) {
        this(EntityRegistry.ROOT.get(), level);
        this.setOwner(owner);
    }

    public LivingEntity getTarget() {
        return this.target;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    @Override
    public boolean canCollideWith(@NotNull Entity pEntity) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected void doPush(@NotNull Entity pEntity) {
    }

    @Override
    public void push(@NotNull Entity pEntity) {
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public boolean dismountsUnderwater() {
        return false;
    }

    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.0;
    }

    public boolean shouldRiderFaceForward(@NotNull Player player) {
        return false;
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        Entity rooted = this.m_146895_();
        return rooted != null ? EntityDimensions.fixed(rooted.getBbWidth() * 1.25F, 0.75F) : super.getDimensions(pPose);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.playSound) {
            this.m_6210_();
            this.m_5496_(SoundRegistry.ROOT_EMERGE.get(), 2.0F, 1.0F);
            this.playSound = false;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.f_19797_ > this.duration || this.target != null && this.target.isDeadOrDying() || !this.m_20160_()) {
                this.removeRoot();
            }
        } else if (this.f_19797_ < 20) {
            this.clientDiggingParticles(this);
        }
    }

    protected void clientDiggingParticles(LivingEntity livingEntity) {
        RandomSource randomsource = livingEntity.getRandom();
        BlockState blockstate = livingEntity.m_20075_();
        if (blockstate.m_60799_() != RenderShape.INVISIBLE) {
            for (int i = 0; i < 15; i++) {
                double d0 = livingEntity.m_20185_() + (double) Mth.randomBetween(randomsource, -0.5F, 0.5F);
                double d1 = livingEntity.m_20186_();
                double d2 = livingEntity.m_20189_() + (double) Mth.randomBetween(randomsource, -0.5F, 0.5F);
                livingEntity.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), d0, d1, d2, 0.0, 0.0, 0.0);
            }
        }
    }

    public void setOwner(@Nullable LivingEntity pOwner) {
        this.owner = pOwner;
        this.ownerUUID = pOwner == null ? null : pOwner.m_20148_();
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.m_9236_() instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.m_9236_()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
            }
        }
        return this.owner;
    }

    public void removeRoot() {
        if (this.m_9236_().isClientSide) {
            for (int i = 0; i < 5; i++) {
                this.m_9236_().addParticle(ParticleHelper.ROOT_FOG, this.m_20185_() + Utils.getRandomScaled(0.1F), this.m_20186_() + Utils.getRandomScaled(0.1F), this.m_20189_() + Utils.getRandomScaled(0.1F), Utils.getRandomScaled(2.0), (double) (-this.f_19796_.nextFloat() * 0.5F), Utils.getRandomScaled(2.0));
            }
        }
        this.m_20153_();
        this.m_146870_();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Age", this.f_19797_);
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
        pCompound.putInt("Duration", this.duration);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.f_19797_ = pCompound.getInt("Age");
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
        this.duration = pCompound.getInt("Duration");
    }

    @Override
    public boolean hasIndirectPassenger(Entity pEntity) {
        return true;
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        this.removeRoot();
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isDamageSourceBlocked(DamageSource pDamageSource) {
        return true;
    }

    @Override
    public boolean showVehicleHealth() {
        return false;
    }

    @Override
    public void knockback(double pStrength, double pX, double pZ) {
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction entityMoveFunction0) {
        int x = (int) (this.m_20185_() - passenger.getX());
        int y = (int) (this.m_20186_() - passenger.getY());
        int z = (int) (this.m_20189_() - passenger.getZ());
        x *= x;
        y *= y;
        z *= z;
        if (x + y + z > 25) {
            this.removeRoot();
        } else {
            passenger.setPos(this.m_20185_(), this.m_20186_(), this.m_20189_());
        }
    }

    @Override
    protected boolean isImmobile() {
        return true;
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            this.removeRoot();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    private PlayState animationPredicate(AnimationState event) {
        AnimationController controller = event.getController();
        if (!this.played && controller.getAnimationState() == AnimationController.State.STOPPED) {
            controller.forceAnimationReset();
            controller.setAnimation(this.ANIMATION);
            this.played = true;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(this.controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}