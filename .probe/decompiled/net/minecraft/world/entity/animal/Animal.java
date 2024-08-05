package net.minecraft.world.entity.animal;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public abstract class Animal extends AgeableMob {

    protected static final int PARENT_AGE_AFTER_BREEDING = 6000;

    private int inLove;

    @Nullable
    private UUID loveCause;

    protected Animal(EntityType<? extends Animal> entityTypeExtendsAnimal0, Level level1) {
        super(entityTypeExtendsAnimal0, level1);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, 16.0F);
        this.m_21441_(BlockPathTypes.DAMAGE_FIRE, -1.0F);
    }

    @Override
    protected void customServerAiStep() {
        if (this.m_146764_() != 0) {
            this.inLove = 0;
        }
        super.m_8024_();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.m_146764_() != 0) {
            this.inLove = 0;
        }
        if (this.inLove > 0) {
            this.inLove--;
            if (this.inLove % 10 == 0) {
                double $$0 = this.f_19796_.nextGaussian() * 0.02;
                double $$1 = this.f_19796_.nextGaussian() * 0.02;
                double $$2 = this.f_19796_.nextGaussian() * 0.02;
                this.m_9236_().addParticle(ParticleTypes.HEART, this.m_20208_(1.0), this.m_20187_() + 0.5, this.m_20262_(1.0), $$0, $$1, $$2);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.m_6673_(damageSource0)) {
            return false;
        } else {
            this.inLove = 0;
            return super.m_6469_(damageSource0, float1);
        }
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos0, LevelReader levelReader1) {
        return levelReader1.m_8055_(blockPos0.below()).m_60713_(Blocks.GRASS_BLOCK) ? 10.0F : levelReader1.getPathfindingCostFromLightLevels(blockPos0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putInt("InLove", this.inLove);
        if (this.loveCause != null) {
            compoundTag0.putUUID("LoveCause", this.loveCause);
        }
    }

    @Override
    public double getMyRidingOffset() {
        return 0.14;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.inLove = compoundTag0.getInt("InLove");
        this.loveCause = compoundTag0.hasUUID("LoveCause") ? compoundTag0.getUUID("LoveCause") : null;
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> entityTypeExtendsAnimal0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return levelAccessor1.m_8055_(blockPos3.below()).m_204336_(BlockTags.ANIMALS_SPAWNABLE_ON) && isBrightEnoughToSpawn(levelAccessor1, blockPos3);
    }

    protected static boolean isBrightEnoughToSpawn(BlockAndTintGetter blockAndTintGetter0, BlockPos blockPos1) {
        return blockAndTintGetter0.getRawBrightness(blockPos1, 0) > 8;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }

    @Override
    public boolean removeWhenFarAway(double double0) {
        return false;
    }

    @Override
    public int getExperienceReward() {
        return 1 + this.m_9236_().random.nextInt(3);
    }

    public boolean isFood(ItemStack itemStack0) {
        return itemStack0.is(Items.WHEAT);
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        if (this.isFood($$2)) {
            int $$3 = this.m_146764_();
            if (!this.m_9236_().isClientSide && $$3 == 0 && this.canFallInLove()) {
                this.usePlayerItem(player0, interactionHand1, $$2);
                this.setInLove(player0);
                return InteractionResult.SUCCESS;
            }
            if (this.m_6162_()) {
                this.usePlayerItem(player0, interactionHand1, $$2);
                this.m_146740_(m_216967_(-$$3), true);
                return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
            }
            if (this.m_9236_().isClientSide) {
                return InteractionResult.CONSUME;
            }
        }
        return super.m_6071_(player0, interactionHand1);
    }

    protected void usePlayerItem(Player player0, InteractionHand interactionHand1, ItemStack itemStack2) {
        if (!player0.getAbilities().instabuild) {
            itemStack2.shrink(1);
        }
    }

    public boolean canFallInLove() {
        return this.inLove <= 0;
    }

    public void setInLove(@Nullable Player player0) {
        this.inLove = 600;
        if (player0 != null) {
            this.loveCause = player0.m_20148_();
        }
        this.m_9236_().broadcastEntityEvent(this, (byte) 18);
    }

    public void setInLoveTime(int int0) {
        this.inLove = int0;
    }

    public int getInLoveTime() {
        return this.inLove;
    }

    @Nullable
    public ServerPlayer getLoveCause() {
        if (this.loveCause == null) {
            return null;
        } else {
            Player $$0 = this.m_9236_().m_46003_(this.loveCause);
            return $$0 instanceof ServerPlayer ? (ServerPlayer) $$0 : null;
        }
    }

    public boolean isInLove() {
        return this.inLove > 0;
    }

    public void resetLove() {
        this.inLove = 0;
    }

    public boolean canMate(Animal animal0) {
        if (animal0 == this) {
            return false;
        } else {
            return animal0.getClass() != this.getClass() ? false : this.isInLove() && animal0.isInLove();
        }
    }

    public void spawnChildFromBreeding(ServerLevel serverLevel0, Animal animal1) {
        AgeableMob $$2 = this.m_142606_(serverLevel0, animal1);
        if ($$2 != null) {
            $$2.setBaby(true);
            $$2.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0F, 0.0F);
            this.finalizeSpawnChildFromBreeding(serverLevel0, animal1, $$2);
            serverLevel0.m_47205_($$2);
        }
    }

    public void finalizeSpawnChildFromBreeding(ServerLevel serverLevel0, Animal animal1, @Nullable AgeableMob ageableMob2) {
        Optional.ofNullable(this.getLoveCause()).or(() -> Optional.ofNullable(animal1.getLoveCause())).ifPresent(p_277486_ -> {
            p_277486_.m_36220_(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(p_277486_, this, animal1, ageableMob2);
        });
        this.m_146762_(6000);
        animal1.m_146762_(6000);
        this.resetLove();
        animal1.resetLove();
        serverLevel0.broadcastEntityEvent(this, (byte) 18);
        if (serverLevel0.m_46469_().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            serverLevel0.addFreshEntity(new ExperienceOrb(serverLevel0, this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_217043_().nextInt(7) + 1));
        }
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 18) {
            for (int $$1 = 0; $$1 < 7; $$1++) {
                double $$2 = this.f_19796_.nextGaussian() * 0.02;
                double $$3 = this.f_19796_.nextGaussian() * 0.02;
                double $$4 = this.f_19796_.nextGaussian() * 0.02;
                this.m_9236_().addParticle(ParticleTypes.HEART, this.m_20208_(1.0), this.m_20187_() + 0.5, this.m_20262_(1.0), $$2, $$3, $$4);
            }
        } else {
            super.m_7822_(byte0);
        }
    }
}