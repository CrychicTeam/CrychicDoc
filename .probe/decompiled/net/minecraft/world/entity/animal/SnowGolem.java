package net.minecraft.world.entity.animal;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class SnowGolem extends AbstractGolem implements Shearable, RangedAttackMob {

    private static final EntityDataAccessor<Byte> DATA_PUMPKIN_ID = SynchedEntityData.defineId(SnowGolem.class, EntityDataSerializers.BYTE);

    private static final byte PUMPKIN_FLAG = 16;

    private static final float EYE_HEIGHT = 1.7F;

    public SnowGolem(EntityType<? extends SnowGolem> entityTypeExtendsSnowGolem0, Level level1) {
        super(entityTypeExtendsSnowGolem0, level1);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new RangedAttackGoal(this, 1.25, 20, 10.0F));
        this.f_21345_.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0, 1.0000001E-5F));
        this.f_21345_.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(4, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, p_29932_ -> p_29932_ instanceof Enemy));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_PUMPKIN_ID, (byte) 16);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        compoundTag0.putBoolean("Pumpkin", this.hasPumpkin());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        if (compoundTag0.contains("Pumpkin")) {
            this.setPumpkin(compoundTag0.getBoolean("Pumpkin"));
        }
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (!this.m_9236_().isClientSide) {
            if (this.m_9236_().m_204166_(this.m_20183_()).is(BiomeTags.SNOW_GOLEM_MELTS)) {
                this.m_6469_(this.m_269291_().onFire(), 1.0F);
            }
            if (!this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                return;
            }
            BlockState $$0 = Blocks.SNOW.defaultBlockState();
            for (int $$1 = 0; $$1 < 4; $$1++) {
                int $$2 = Mth.floor(this.m_20185_() + (double) ((float) ($$1 % 2 * 2 - 1) * 0.25F));
                int $$3 = Mth.floor(this.m_20186_());
                int $$4 = Mth.floor(this.m_20189_() + (double) ((float) ($$1 / 2 % 2 * 2 - 1) * 0.25F));
                BlockPos $$5 = new BlockPos($$2, $$3, $$4);
                if (this.m_9236_().getBlockState($$5).m_60795_() && $$0.m_60710_(this.m_9236_(), $$5)) {
                    this.m_9236_().setBlockAndUpdate($$5, $$0);
                    this.m_9236_().m_220407_(GameEvent.BLOCK_PLACE, $$5, GameEvent.Context.of(this, $$0));
                }
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity0, float float1) {
        Snowball $$2 = new Snowball(this.m_9236_(), this);
        double $$3 = livingEntity0.m_20188_() - 1.1F;
        double $$4 = livingEntity0.m_20185_() - this.m_20185_();
        double $$5 = $$3 - $$2.m_20186_();
        double $$6 = livingEntity0.m_20189_() - this.m_20189_();
        double $$7 = Math.sqrt($$4 * $$4 + $$6 * $$6) * 0.2F;
        $$2.m_6686_($$4, $$5 + $$7, $$6, 1.6F, 12.0F);
        this.m_5496_(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.m_217043_().nextFloat() * 0.4F + 0.8F));
        this.m_9236_().m_7967_($$2);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 1.7F;
    }

    @Override
    protected InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        if ($$2.is(Items.SHEARS) && this.readyForShearing()) {
            this.shear(SoundSource.PLAYERS);
            this.m_146852_(GameEvent.SHEAR, player0);
            if (!this.m_9236_().isClientSide) {
                $$2.hurtAndBreak(1, player0, p_29910_ -> p_29910_.m_21190_(interactionHand1));
            }
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void shear(SoundSource soundSource0) {
        this.m_9236_().playSound(null, this, SoundEvents.SNOW_GOLEM_SHEAR, soundSource0, 1.0F, 1.0F);
        if (!this.m_9236_().isClientSide()) {
            this.setPumpkin(false);
            this.m_5552_(new ItemStack(Items.CARVED_PUMPKIN), 1.7F);
        }
    }

    @Override
    public boolean readyForShearing() {
        return this.m_6084_() && this.hasPumpkin();
    }

    public boolean hasPumpkin() {
        return (this.f_19804_.get(DATA_PUMPKIN_ID) & 16) != 0;
    }

    public void setPumpkin(boolean boolean0) {
        byte $$1 = this.f_19804_.get(DATA_PUMPKIN_ID);
        if (boolean0) {
            this.f_19804_.set(DATA_PUMPKIN_ID, (byte) ($$1 | 16));
        } else {
            this.f_19804_.set(DATA_PUMPKIN_ID, (byte) ($$1 & -17));
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SNOW_GOLEM_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.SNOW_GOLEM_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SNOW_GOLEM_DEATH;
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, (double) (0.75F * this.m_20192_()), (double) (this.m_20205_() * 0.4F));
    }
}