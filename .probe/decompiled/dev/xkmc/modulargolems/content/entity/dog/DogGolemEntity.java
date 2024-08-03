package dev.xkmc.modulargolems.content.entity.dog;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.goals.GolemMeleeGoal;
import dev.xkmc.modulargolems.init.data.MGConfig;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class DogGolemEntity extends AbstractGolemEntity<DogGolemEntity, DogGolemPartType> {

    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(DogGolemEntity.class, EntityDataSerializers.BYTE);

    public float getJumpStrength() {
        float ans = (float) this.m_21133_((Attribute) GolemTypes.GOLEM_JUMP.get());
        MobEffectInstance ins = this.m_21124_(MobEffects.JUMP);
        if (ins != null) {
            int lv = ins.getAmplifier() + 1;
            ans *= 1.0F + (float) lv * 0.625F;
        }
        return ans;
    }

    public DogGolemEntity(EntityType<DogGolemEntity> type, Level level) {
        super(type, level);
        this.m_274367_(1.0F);
    }

    public float getTailAngle() {
        if (this.m_21660_()) {
            return 1.5393804F;
        } else {
            float percentage = 1.0F - this.m_21223_() / this.m_21233_();
            return (0.55F - percentage * 0.16F) * (float) Math.PI;
        }
    }

    @Override
    protected void tickRidden(Player player, Vec3 vec3) {
        super.m_274498_(player, vec3);
        Vec2 vec2 = this.getRiddenRotation(player);
        this.m_19915_(vec2.y, vec2.x);
        this.f_19859_ = this.f_20883_ = this.f_20885_ = this.m_146908_();
        if (this.m_6109_() && (this.m_20096_() || this.m_20072_()) && player.f_20899_) {
            this.executeRidersJump(vec3);
        }
    }

    protected Vec2 getRiddenRotation(LivingEntity rider) {
        return new Vec2(rider.m_146909_() * 0.5F, rider.m_146908_());
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 input) {
        float f = player.f_20900_ * 0.5F;
        float f1 = player.f_20902_;
        if (f1 <= 0.0F) {
            f1 *= 0.25F;
        }
        Vec3 ans = new Vec3((double) f, 0.0, (double) f1);
        if (player.m_6144_()) {
            ans = ans.add(0.0, -1.0, 0.0);
        }
        return ans;
    }

    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.m_146895_();
        if (entity instanceof Player) {
            return (Player) entity;
        } else {
            return entity instanceof AbstractGolemEntity ? (AbstractGolemEntity) entity : null;
        }
    }

    @Override
    protected float getRiddenSpeed(Player rider) {
        return (float) (this.m_21133_(Attributes.MOVEMENT_SPEED) * MGConfig.COMMON.riddenSpeedFactor.get());
    }

    protected void executeRidersJump(Vec3 action) {
        Vec3 vec3 = this.m_20184_();
        float jump = this.getJumpStrength();
        this.m_20334_(vec3.x, (double) jump, vec3.z);
        this.f_19812_ = true;
        ForgeHooks.onLivingJump(this);
        if (action.z > 0.0) {
            float x0 = Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0));
            float z0 = Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0));
            this.m_20256_(this.m_20184_().add((double) (-0.4F * x0 * jump), 0.0, (double) (0.4F * z0 * jump)));
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.m_20206_() * 1.4 - 0.35;
    }

    @Override
    protected void positionRider(Entity rider, Entity.MoveFunction setPos) {
        int index = this.m_20197_().indexOf(rider);
        int total = this.m_20197_().size();
        if (index >= 0) {
            float width = this.m_20205_();
            float offset = index == 0 ? (float) index + 0.7F : (float) index + (this.getControllingPassenger() instanceof Player ? 1.7F : 1.2F);
            float pos = width / 2.0F - width / (float) total * offset;
            double dy = rider.getMyRidingOffset() + this.getPassengersRidingOffset();
            Vec3 vec3 = new Vec3(0.0, 0.0, (double) pos).yRot(-this.f_20883_ * (float) (Math.PI / 180.0));
            setPos.accept(rider, this.m_20185_() + vec3.x, this.m_20186_() + dy, this.m_20189_() + vec3.z);
            if (index > 0) {
                this.clampRotation(rider);
            }
        }
    }

    @Override
    public void onPassengerTurned(Entity rider) {
        if (this.getControllingPassenger() != rider) {
            this.clampRotation(rider);
        }
    }

    private void clampRotation(Entity rider) {
        rider.setYBodyRot(this.m_146908_());
        float yr0 = rider.getYRot();
        float dyr = Mth.wrapDegrees(yr0 - this.m_146908_());
        float yr1 = Mth.clamp(dyr, -160.0F, 160.0F);
        rider.yRotO += yr1 - dyr;
        float yr2 = yr0 + yr1 - dyr;
        rider.setYRot(yr2);
        rider.setYHeadRot(yr2);
    }

    @Override
    protected boolean canAddPassenger(Entity entity) {
        return this.m_20197_().size() <= (Integer) this.getModifiers().getOrDefault(GolemModifiers.SIZE_UPGRADE.get(), 0);
    }

    @Override
    protected void addPassenger(Entity rider) {
        this.setInSittingPose(false);
        super.m_20348_(rider);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DATA_FLAGS_ID, (byte) 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putBoolean("Sitting", this.isInSittingPose());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.setInSittingPose(compoundTag0.getBoolean("Sitting"));
    }

    @Override
    public boolean isInSittingPose() {
        return (this.f_19804_.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setInSittingPose(boolean sit) {
        byte b0 = this.f_19804_.get(DATA_FLAGS_ID);
        this.m_21573_().stop();
        this.setTarget(null);
        if (sit) {
            this.f_19804_.set(DATA_FLAGS_ID, (byte) (b0 | 1));
        } else {
            this.f_19804_.set(DATA_FLAGS_ID, (byte) (b0 & -2));
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(2, new GolemMeleeGoal(this));
        super.registerGoals();
    }

    @Override
    protected boolean predicatePriorityTarget(LivingEntity e) {
        return !this.isInSittingPose() && super.predicatePriorityTarget(e);
    }

    @Override
    protected boolean predicateSecondaryTarget(LivingEntity e) {
        return !this.isInSittingPose() && super.predicateSecondaryTarget(e);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.m_9236_().isClientSide) {
            this.setInSittingPose(false);
        }
        return super.m_6469_(source, amount);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.WOLF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(SoundEvents.WOLF_STEP, 1.0F, 1.0F);
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, (double) (0.6F * this.m_20192_()), (double) (this.m_20205_() * 0.4F));
    }

    @Override
    protected InteractionResult mobInteractImpl(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (MGConfig.COMMON.strictInteract.get() && !itemstack.isEmpty()) {
            return InteractionResult.PASS;
        } else if (!player.m_6144_() && itemstack.isEmpty()) {
            return super.mobInteractImpl(player, hand);
        } else {
            if (!this.m_9236_().isClientSide() && this.canModify(player)) {
                this.setInSittingPose(!this.isInSittingPose());
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target == null || !this.isInSittingPose()) {
            super.setTarget(target);
        }
    }
}