package io.redspace.ironsspellbooks.entity.spells.wisp;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.goals.WispAttackGoal;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.spells.holy.WispSpell;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Collections;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WispEntity extends PathfinderMob implements GeoEntity {

    @Nullable
    private UUID ownerUUID;

    @Nullable
    private Entity cachedOwner;

    private final RawAnimation animation = RawAnimation.begin().thenPlay("animation.wisp.flying");

    private Vec3 targetSearchStart;

    private Vec3 lastTickPos;

    private float damageAmount;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public WispEntity(EntityType<? extends WispEntity> entityType, Level level) {
        super(entityType, level);
        this.m_20242_(true);
    }

    public WispEntity(Level levelIn, LivingEntity owner, float damageAmount) {
        this(EntityRegistry.WISP.get(), levelIn);
        this.f_21342_ = new FlyingMoveControl(this, 20, true);
        this.damageAmount = damageAmount;
        this.setOwner(owner);
        float xRot = owner.m_146909_();
        float yRot = owner.m_146908_();
        float yHeadRot = owner.getYHeadRot();
        this.m_146922_(yRot);
        this.m_146926_(xRot);
        this.m_5618_(yRot);
        this.m_5616_(yHeadRot);
        this.lastTickPos = this.m_20182_();
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(2, new WispAttackGoal(this, 1.0));
    }

    public static boolean isValidTarget(@Nullable Entity entity) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.isAlive() && livingEntity instanceof Enemy) {
            return true;
        }
        return false;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public LivingEntity getTarget() {
        return super.m_5448_();
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.f_19853_.isClientSide) {
            this.spawnParticles();
        } else {
            LivingEntity target = this.getTarget();
            if (target != null && !target.m_213877_()) {
                if (this.m_20191_().intersects(target.m_20191_())) {
                    DamageSources.applyDamage(target, this.damageAmount, SpellRegistry.WISP_SPELL.get().getDamageSource(this, this.cachedOwner));
                    this.m_5496_(WispSpell.getImpactSound(), 1.0F, 1.0F);
                    Vec3 p = target.m_146892_();
                    MagicManager.spawnParticles(this.f_19853_, ParticleHelper.WISP, p.x, p.y, p.z, 25, 0.0, 0.0, 0.0, 0.18, true);
                    this.m_146870_();
                }
            } else if (this.f_19797_ > 10) {
                this.popAndDie();
            }
        }
        this.lastTickPos = this.m_20182_();
    }

    public void setOwner(@Nullable Entity pOwner) {
        if (pOwner != null) {
            this.ownerUUID = pOwner.getUUID();
            this.cachedOwner = pOwner;
        }
    }

    @NotNull
    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel) {

            @Override
            public boolean isStableDestination(BlockPos blockPos) {
                return !this.f_26495_.getBlockState(blockPos.below()).m_60795_();
            }

            @Override
            public void tick() {
                super.tick();
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.m_7008_(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return pDimensions.height * 0.6F;
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if (this.m_21515_() || this.m_6109_()) {
            if (this.m_20069_()) {
                this.m_19920_(0.02F, pTravelVector);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.8F));
            } else if (this.m_20077_()) {
                this.m_19920_(0.02F, pTravelVector);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.5));
            } else {
                this.m_19920_(this.m_6113_(), pTravelVector);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.91F));
            }
        }
        this.m_267651_(false);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void setTarget(@org.jetbrains.annotations.Nullable LivingEntity target) {
        super.m_6710_(target);
    }

    @Override
    protected void customServerAiStep() {
        if (this.cachedOwner == null || !this.cachedOwner.isAlive()) {
            this.m_146870_();
        }
    }

    private PlayState predicate(AnimationState event) {
        event.getController().setAnimation(this.animation);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.FLYING_SPEED, 0.2).add(Attributes.MOVEMENT_SPEED, 0.2);
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
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!this.f_19853_.isClientSide) {
            this.popAndDie();
        }
        return true;
    }

    private void popAndDie() {
        this.m_5496_(SoundEvents.SHULKER_BULLET_HURT, 1.0F, 1.0F);
        ((ServerLevel) this.f_19853_).sendParticles(ParticleTypes.CRIT, this.m_20185_(), this.m_20186_(), this.m_20189_(), 15, 0.2, 0.2, 0.2, 0.0);
        this.m_146870_();
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }

    public void spawnParticles() {
    }
}