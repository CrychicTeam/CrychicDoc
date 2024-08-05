package dev.xkmc.modulargolems.content.entity.metalgolem;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.entity.common.SweepGolemEntity;
import dev.xkmc.modulargolems.content.entity.goals.GolemMeleeGoal;
import dev.xkmc.modulargolems.init.advancement.GolemTriggers;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class MetalGolemEntity extends SweepGolemEntity<MetalGolemEntity, MetalGolemPartType> {

    private int attackAnimationTick;

    public MetalGolemEntity(EntityType<MetalGolemEntity> type, Level level) {
        super(type, level);
        this.m_274367_(1.0F);
    }

    @Override
    protected boolean performDamageTarget(Entity target, float damage, double kb) {
        if (target instanceof LivingEntity le) {
            le.setLastHurtByPlayer(this.getOwner());
            damage += EnchantmentHelper.getDamageBonus(this.m_21205_(), le.getMobType());
            kb += (double) ((float) EnchantmentHelper.getKnockbackBonus(this));
        }
        boolean succeed = target.hurt(this.m_9236_().damageSources().mobAttack(this), damage);
        if (succeed) {
            double d1 = Math.max(0.0, 1.0 - kb);
            double dokb = this.m_21133_(Attributes.ATTACK_KNOCKBACK) * 0.4;
            target.setDeltaMovement(target.getDeltaMovement().add(0.0, dokb * d1, 0.0));
            this.m_19970_(this, target);
        }
        return succeed;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(2, new GolemMeleeGoal(this));
        super.m_8099_();
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (this.attackAnimationTick > 0) {
            this.attackAnimationTick--;
        }
        if (this.m_20184_().horizontalDistanceSqr() > 2.5000003E-7F && this.f_19796_.nextInt(5) == 0) {
            int i = Mth.floor(this.m_20185_());
            int j = Mth.floor(this.m_20186_() - 0.2F);
            int k = Mth.floor(this.m_20189_());
            BlockPos pos = new BlockPos(i, j, k);
            BlockState blockstate = this.m_9236_().getBlockState(pos);
            if (!blockstate.m_60795_()) {
                this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate).setPos(pos), this.m_20185_() + ((double) this.f_19796_.nextFloat() - 0.5) * (double) this.m_20205_(), this.m_20186_() + 0.1, this.m_20189_() + ((double) this.f_19796_.nextFloat() - 0.5) * (double) this.m_20205_(), 4.0 * ((double) this.f_19796_.nextFloat() - 0.5), 0.5, ((double) this.f_19796_.nextFloat() - 0.5) * 4.0);
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        this.attackAnimationTick = 10;
        this.m_9236_().broadcastEntityEvent(this, (byte) 4);
        float damage = this.getAttackDamage();
        double kb;
        if (target instanceof LivingEntity livingentity) {
            kb = livingentity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        } else {
            kb = 0.0;
        }
        boolean flag = this.performRangedDamage(target, damage, kb);
        this.m_5496_(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        IronGolem.Crackiness crack = this.getCrackiness();
        boolean flag = super.m_6469_(source, amount);
        if (flag && this.getCrackiness() != crack) {
            this.m_5496_(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
        }
        return flag;
    }

    public IronGolem.Crackiness getCrackiness() {
        return IronGolem.Crackiness.byFraction(this.m_21223_() / this.m_21233_());
    }

    @Override
    public void handleEntityEvent(byte event) {
        if (event == 4) {
            this.attackAnimationTick = 10;
            this.m_5496_(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        } else {
            super.m_7822_(event);
        }
    }

    public int getAttackAnimationTick() {
        return this.attackAnimationTick;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader0) {
        BlockPos blockpos = this.m_20183_();
        BlockPos blockpos1 = blockpos.below();
        BlockState blockstate = levelReader0.m_8055_(blockpos1);
        if (!blockstate.m_60634_(levelReader0, blockpos1, this)) {
            return false;
        } else {
            for (int i = 1; i < 3; i++) {
                BlockPos blockpos2 = blockpos.above(i);
                BlockState blockstate1 = levelReader0.m_8055_(blockpos2);
                if (!NaturalSpawner.isValidEmptySpawnBlock(levelReader0, blockpos2, blockstate1, blockstate1.m_60819_(), EntityType.IRON_GOLEM)) {
                    return false;
                }
            }
            return NaturalSpawner.isValidEmptySpawnBlock(levelReader0, blockpos, levelReader0.m_8055_(blockpos), Fluids.EMPTY.defaultFluidState(), EntityType.IRON_GOLEM) && levelReader0.m_45784_(this);
        }
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, (double) (0.875F * this.m_20192_()), (double) (this.m_20205_() * 0.4F));
    }

    @Override
    protected InteractionResult mobInteractImpl(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (this.getMaterials().size() != MetalGolemPartType.values().length) {
            return super.mobInteractImpl(player, hand);
        } else {
            GolemMaterial mat = (GolemMaterial) this.getMaterials().get(MetalGolemPartType.BODY.ordinal());
            Ingredient ing = (Ingredient) GolemMaterialConfig.get().ingredients.get(mat.id());
            if (!ing.test(itemstack)) {
                return MGConfig.COMMON.strictInteract.get() && !itemstack.isEmpty() ? InteractionResult.PASS : super.mobInteractImpl(player, hand);
            } else {
                float f = this.m_21223_();
                this.m_5634_(this.m_21233_() / 4.0F);
                if (this.m_21223_() == f) {
                    return InteractionResult.PASS;
                } else {
                    float f1 = 1.0F + (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F;
                    this.m_5496_(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, f1);
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    if (!this.m_9236_().isClientSide()) {
                        GolemTriggers.HOT_FIX.trigger((ServerPlayer) player);
                    }
                    return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
                }
            }
        }
    }
}