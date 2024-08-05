package com.mna.entities.passive;

import com.mna.entities.EntityInit;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.shapes.CollisionContext;

public class Magmoo extends Cow {

    public Magmoo(EntityType<? extends Cow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.m_21120_(pHand);
        if (itemstack.is(Items.BUCKET) && !this.m_6162_()) {
            pPlayer.playSound(SoundEvents.BUCKET_FILL_LAVA, 1.0F, 1.0F);
            ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, pPlayer, Items.LAVA_BUCKET.getDefaultInstance());
            pPlayer.m_21008_(pHand, itemstack1);
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide());
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.floatMagmoo();
        this.m_20101_();
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader pLevel) {
        return pLevel.m_45784_(this);
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
        this.m_20101_();
        if (this.m_20077_()) {
            this.m_183634_();
        } else {
            super.m_7840_(pY, pOnGround, pState, pPos);
        }
    }

    private void floatMagmoo() {
        if (this.m_20077_()) {
            CollisionContext collisioncontext = CollisionContext.of(this);
            if (collisioncontext.isAbove(LiquidBlock.STABLE_SHAPE, this.m_20183_(), true) && !this.m_9236_().getFluidState(this.m_20183_().above()).is(FluidTags.LAVA)) {
                this.m_6853_(true);
            } else {
                this.m_20256_(this.m_20184_().scale(0.5).add(0.0, 0.05, 0.0));
            }
        }
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean canStandOnFluid(FluidState pFluidState) {
        return pFluidState.is(FluidTags.LAVA);
    }

    @Nullable
    @Override
    public Cow getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return EntityInit.MAGMOO.get().create(pLevel);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() != null && pSource.getEntity().distanceTo(this) < 16.0F) {
            pSource.getEntity().lavaHurt();
        }
        return super.m_6469_(pSource, pAmount);
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new Magmoo.MagmooPathNavigation(this, pLevel);
    }

    @Override
    public float getWalkTargetValue(BlockPos pPos, LevelReader pLevel) {
        if (pLevel.m_8055_(pPos).m_60819_().is(FluidTags.LAVA)) {
            return 10.0F;
        } else {
            return this.m_20077_() ? Float.NEGATIVE_INFINITY : 0.0F;
        }
    }

    static class MagmooPathNavigation extends GroundPathNavigation {

        MagmooPathNavigation(Magmoo pStrider, Level pLevel) {
            super(pStrider, pLevel);
        }

        @Override
        protected PathFinder createPathFinder(int pMaxVisitedNodes) {
            this.f_26508_ = new WalkNodeEvaluator();
            this.f_26508_.setCanPassDoors(true);
            return new PathFinder(this.f_26508_, pMaxVisitedNodes);
        }

        @Override
        protected boolean hasValidPathType(BlockPathTypes pPathType) {
            return pPathType != BlockPathTypes.LAVA && pPathType != BlockPathTypes.DAMAGE_FIRE && pPathType != BlockPathTypes.DANGER_FIRE ? super.hasValidPathType(pPathType) : true;
        }

        @Override
        public boolean isStableDestination(BlockPos pPos) {
            return this.f_26495_.getBlockState(pPos).m_60713_(Blocks.LAVA) || super.m_6342_(pPos);
        }
    }
}