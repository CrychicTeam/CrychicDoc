package net.minecraft.world.entity.monster;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;

public abstract class Monster extends PathfinderMob implements Enemy {

    protected Monster(EntityType<? extends Monster> entityTypeExtendsMonster0, Level level1) {
        super(entityTypeExtendsMonster0, level1);
        this.f_21364_ = 5;
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    public void aiStep() {
        this.m_21203_();
        this.updateNoActionTime();
        super.m_8107_();
    }

    protected void updateNoActionTime() {
        float $$0 = this.m_213856_();
        if ($$0 > 0.5F) {
            this.f_20891_ += 2;
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.HOSTILE_SWIM;
    }

    @Override
    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.HOSTILE_SPLASH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.HOSTILE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HOSTILE_DEATH;
    }

    @Override
    public LivingEntity.Fallsounds getFallSounds() {
        return new LivingEntity.Fallsounds(SoundEvents.HOSTILE_SMALL_FALL, SoundEvents.HOSTILE_BIG_FALL);
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos0, LevelReader levelReader1) {
        return -levelReader1.getPathfindingCostFromLightLevels(blockPos0);
    }

    public static boolean isDarkEnoughToSpawn(ServerLevelAccessor serverLevelAccessor0, BlockPos blockPos1, RandomSource randomSource2) {
        if (serverLevelAccessor0.m_45517_(LightLayer.SKY, blockPos1) > randomSource2.nextInt(32)) {
            return false;
        } else {
            DimensionType $$3 = serverLevelAccessor0.m_6042_();
            int $$4 = $$3.monsterSpawnBlockLightLimit();
            if ($$4 < 15 && serverLevelAccessor0.m_45517_(LightLayer.BLOCK, blockPos1) > $$4) {
                return false;
            } else {
                int $$5 = serverLevelAccessor0.getLevel().m_46470_() ? serverLevelAccessor0.m_46849_(blockPos1, 10) : serverLevelAccessor0.m_46803_(blockPos1);
                return $$5 <= $$3.monsterSpawnLightTest().sample(randomSource2);
            }
        }
    }

    public static boolean checkMonsterSpawnRules(EntityType<? extends Monster> entityTypeExtendsMonster0, ServerLevelAccessor serverLevelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return serverLevelAccessor1.m_46791_() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(serverLevelAccessor1, blockPos3, randomSource4) && m_217057_(entityTypeExtendsMonster0, serverLevelAccessor1, mobSpawnType2, blockPos3, randomSource4);
    }

    public static boolean checkAnyLightMonsterSpawnRules(EntityType<? extends Monster> entityTypeExtendsMonster0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return levelAccessor1.getDifficulty() != Difficulty.PEACEFUL && m_217057_(entityTypeExtendsMonster0, levelAccessor1, mobSpawnType2, blockPos3, randomSource4);
    }

    public static AttributeSupplier.Builder createMonsterAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE);
    }

    @Override
    public boolean shouldDropExperience() {
        return true;
    }

    @Override
    protected boolean shouldDropLoot() {
        return true;
    }

    public boolean isPreventingPlayerRest(Player player0) {
        return true;
    }

    @Override
    public ItemStack getProjectile(ItemStack itemStack0) {
        if (itemStack0.getItem() instanceof ProjectileWeaponItem) {
            Predicate<ItemStack> $$1 = ((ProjectileWeaponItem) itemStack0.getItem()).getSupportedHeldProjectiles();
            ItemStack $$2 = ProjectileWeaponItem.getHeldProjectile(this, $$1);
            return $$2.isEmpty() ? new ItemStack(Items.ARROW) : $$2;
        } else {
            return ItemStack.EMPTY;
        }
    }
}