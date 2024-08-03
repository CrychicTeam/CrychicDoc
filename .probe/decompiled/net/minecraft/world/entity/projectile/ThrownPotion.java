package net.minecraft.world.entity.projectile;

import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownPotion extends ThrowableItemProjectile implements ItemSupplier {

    public static final double SPLASH_RANGE = 4.0;

    private static final double SPLASH_RANGE_SQ = 16.0;

    public static final Predicate<LivingEntity> WATER_SENSITIVE_OR_ON_FIRE = p_287524_ -> p_287524_.isSensitiveToWater() || p_287524_.m_6060_();

    public ThrownPotion(EntityType<? extends ThrownPotion> entityTypeExtendsThrownPotion0, Level level1) {
        super(entityTypeExtendsThrownPotion0, level1);
    }

    public ThrownPotion(Level level0, LivingEntity livingEntity1) {
        super(EntityType.POTION, livingEntity1, level0);
    }

    public ThrownPotion(Level level0, double double1, double double2, double double3) {
        super(EntityType.POTION, double1, double2, double3, level0);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SPLASH_POTION;
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult0) {
        super.m_8060_(blockHitResult0);
        if (!this.m_9236_().isClientSide) {
            ItemStack $$1 = this.m_7846_();
            Potion $$2 = PotionUtils.getPotion($$1);
            List<MobEffectInstance> $$3 = PotionUtils.getMobEffects($$1);
            boolean $$4 = $$2 == Potions.WATER && $$3.isEmpty();
            Direction $$5 = blockHitResult0.getDirection();
            BlockPos $$6 = blockHitResult0.getBlockPos();
            BlockPos $$7 = $$6.relative($$5);
            if ($$4) {
                this.dowseFire($$7);
                this.dowseFire($$7.relative($$5.getOpposite()));
                for (Direction $$8 : Direction.Plane.HORIZONTAL) {
                    this.dowseFire($$7.relative($$8));
                }
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult0) {
        super.m_6532_(hitResult0);
        if (!this.m_9236_().isClientSide) {
            ItemStack $$1 = this.m_7846_();
            Potion $$2 = PotionUtils.getPotion($$1);
            List<MobEffectInstance> $$3 = PotionUtils.getMobEffects($$1);
            boolean $$4 = $$2 == Potions.WATER && $$3.isEmpty();
            if ($$4) {
                this.applyWater();
            } else if (!$$3.isEmpty()) {
                if (this.isLingering()) {
                    this.makeAreaOfEffectCloud($$1, $$2);
                } else {
                    this.applySplash($$3, hitResult0.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) hitResult0).getEntity() : null);
                }
            }
            int $$5 = $$2.hasInstantEffects() ? 2007 : 2002;
            this.m_9236_().m_46796_($$5, this.m_20183_(), PotionUtils.getColor($$1));
            this.m_146870_();
        }
    }

    private void applyWater() {
        AABB $$0 = this.m_20191_().inflate(4.0, 2.0, 4.0);
        for (LivingEntity $$2 : this.m_9236_().m_6443_(LivingEntity.class, $$0, WATER_SENSITIVE_OR_ON_FIRE)) {
            double $$3 = this.m_20280_($$2);
            if ($$3 < 16.0) {
                if ($$2.isSensitiveToWater()) {
                    $$2.hurt(this.m_269291_().indirectMagic(this, this.m_19749_()), 1.0F);
                }
                if ($$2.m_6060_() && $$2.isAlive()) {
                    $$2.m_252836_();
                }
            }
        }
        for (Axolotl $$5 : this.m_9236_().m_45976_(Axolotl.class, $$0)) {
            $$5.rehydrate();
        }
    }

    private void applySplash(List<MobEffectInstance> listMobEffectInstance0, @Nullable Entity entity1) {
        AABB $$2 = this.m_20191_().inflate(4.0, 2.0, 4.0);
        List<LivingEntity> $$3 = this.m_9236_().m_45976_(LivingEntity.class, $$2);
        if (!$$3.isEmpty()) {
            Entity $$4 = this.m_150173_();
            for (LivingEntity $$5 : $$3) {
                if ($$5.isAffectedByPotions()) {
                    double $$6 = this.m_20280_($$5);
                    if ($$6 < 16.0) {
                        double $$7;
                        if ($$5 == entity1) {
                            $$7 = 1.0;
                        } else {
                            $$7 = 1.0 - Math.sqrt($$6) / 4.0;
                        }
                        for (MobEffectInstance $$9 : listMobEffectInstance0) {
                            MobEffect $$10 = $$9.getEffect();
                            if ($$10.isInstantenous()) {
                                $$10.applyInstantenousEffect(this, this.m_19749_(), $$5, $$9.getAmplifier(), $$7);
                            } else {
                                int $$11 = $$9.mapDuration(p_267930_ -> (int) ($$7 * (double) p_267930_ + 0.5));
                                MobEffectInstance $$12 = new MobEffectInstance($$10, $$11, $$9.getAmplifier(), $$9.isAmbient(), $$9.isVisible());
                                if (!$$12.endsWithin(20)) {
                                    $$5.addEffect($$12, $$4);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void makeAreaOfEffectCloud(ItemStack itemStack0, Potion potion1) {
        AreaEffectCloud $$2 = new AreaEffectCloud(this.m_9236_(), this.m_20185_(), this.m_20186_(), this.m_20189_());
        Entity $$3 = this.m_19749_();
        if ($$3 instanceof LivingEntity) {
            $$2.setOwner((LivingEntity) $$3);
        }
        $$2.setRadius(3.0F);
        $$2.setRadiusOnUse(-0.5F);
        $$2.setWaitTime(10);
        $$2.setRadiusPerTick(-$$2.getRadius() / (float) $$2.getDuration());
        $$2.setPotion(potion1);
        for (MobEffectInstance $$4 : PotionUtils.getCustomEffects(itemStack0)) {
            $$2.addEffect(new MobEffectInstance($$4));
        }
        CompoundTag $$5 = itemStack0.getTag();
        if ($$5 != null && $$5.contains("CustomPotionColor", 99)) {
            $$2.setFixedColor($$5.getInt("CustomPotionColor"));
        }
        this.m_9236_().m_7967_($$2);
    }

    private boolean isLingering() {
        return this.m_7846_().is(Items.LINGERING_POTION);
    }

    private void dowseFire(BlockPos blockPos0) {
        BlockState $$1 = this.m_9236_().getBlockState(blockPos0);
        if ($$1.m_204336_(BlockTags.FIRE)) {
            this.m_9236_().removeBlock(blockPos0, false);
        } else if (AbstractCandleBlock.isLit($$1)) {
            AbstractCandleBlock.extinguish(null, $$1, this.m_9236_(), blockPos0);
        } else if (CampfireBlock.isLitCampfire($$1)) {
            this.m_9236_().m_5898_(null, 1009, blockPos0, 0);
            CampfireBlock.dowse(this.m_19749_(), this.m_9236_(), blockPos0, $$1);
            this.m_9236_().setBlockAndUpdate(blockPos0, (BlockState) $$1.m_61124_(CampfireBlock.LIT, false));
        }
    }
}