package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityShoebill;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.phys.Vec3;

public class ShoebillAIFish extends Goal {

    private final EntityShoebill bird;

    private BlockPos waterPos = null;

    private BlockPos targetPos = null;

    private int executionChance = 0;

    private final Direction[] HORIZONTALS = new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST };

    private int idleTime = 0;

    private int navigateTime = 0;

    public ShoebillAIFish(EntityShoebill bird) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.bird = bird;
    }

    @Override
    public void stop() {
        this.targetPos = null;
        this.waterPos = null;
        this.idleTime = 0;
        this.navigateTime = 0;
        this.bird.m_21573_().stop();
    }

    @Override
    public void tick() {
        if (this.targetPos != null && this.waterPos != null) {
            double dist = this.bird.m_20238_(Vec3.atCenterOf(this.waterPos));
            if (dist <= 1.0) {
                this.navigateTime = 0;
                double d0 = (double) this.waterPos.m_123341_() + 0.5 - this.bird.m_20185_();
                double d2 = (double) this.waterPos.m_123343_() + 0.5 - this.bird.m_20189_();
                float yaw = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
                this.bird.m_146922_(yaw);
                this.bird.f_20885_ = yaw;
                this.bird.f_20883_ = yaw;
                this.bird.m_21573_().stop();
                this.idleTime++;
                if (this.idleTime > 25) {
                    this.bird.setAnimation(EntityShoebill.ANIMATION_FISH);
                }
                if (this.idleTime > 45 && this.bird.getAnimation() == EntityShoebill.ANIMATION_FISH) {
                    this.bird.m_146850_(GameEvent.ITEM_INTERACT_START);
                    this.bird.m_5496_(SoundEvents.GENERIC_SPLASH, 0.7F, 0.5F + this.bird.m_217043_().nextFloat());
                    this.bird.resetFishingCooldown();
                    this.spawnFishingLoot();
                    this.stop();
                }
            } else {
                this.navigateTime++;
                this.bird.m_21573_().moveTo((double) this.waterPos.m_123341_(), (double) this.waterPos.m_123342_(), (double) this.waterPos.m_123343_(), 1.2);
            }
            if (this.navigateTime > 3600) {
                this.stop();
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.targetPos != null && this.bird.fishingCooldown == 0 && this.bird.revengeCooldown == 0 && !this.bird.isFlying();
    }

    public void spawnFishingLoot() {
        double luck = 0.0 + (double) ((float) this.bird.luckLevel * 0.5F);
        LootParams.Builder lootcontext$builder = new LootParams.Builder((ServerLevel) this.bird.m_9236_());
        lootcontext$builder.withLuck((float) luck);
        LootContextParamSet.Builder lootparameterset$builder = new LootContextParamSet.Builder();
        LootTable loottable = this.bird.m_9236_().getServer().getLootData().m_278676_(BuiltInLootTables.FISHING);
        for (ItemStack itemstack : loottable.getRandomItems(lootcontext$builder.create(lootparameterset$builder.build()))) {
            ItemEntity item = new ItemEntity(this.bird.m_9236_(), this.bird.m_20185_() + 0.5, this.bird.m_20186_(), this.bird.m_20189_(), itemstack);
            if (!this.bird.m_9236_().isClientSide) {
                this.bird.m_9236_().m_7967_(item);
            }
        }
    }

    @Override
    public boolean canUse() {
        if (!this.bird.isFlying() && this.bird.fishingCooldown == 0 && this.bird.m_217043_().nextInt(30) == 0) {
            if (this.bird.m_20069_()) {
                this.waterPos = this.bird.m_20183_();
                this.targetPos = this.waterPos;
                return true;
            }
            this.waterPos = this.generateTarget();
            if (this.waterPos != null) {
                this.targetPos = this.getLandPos(this.waterPos);
                return this.targetPos != null;
            }
        }
        return false;
    }

    public BlockPos generateTarget() {
        BlockPos blockpos = null;
        RandomSource random = this.bird.m_217043_();
        int range = 32;
        for (int i = 0; i < 15; i++) {
            BlockPos blockpos1 = this.bird.m_20183_().offset(random.nextInt(range) - range / 2, 3, random.nextInt(range) - range / 2);
            while (this.bird.m_9236_().m_46859_(blockpos1) && blockpos1.m_123342_() > 1) {
                blockpos1 = blockpos1.below();
            }
            if (this.isConnectedToLand(blockpos1)) {
                blockpos = blockpos1;
            }
        }
        return blockpos;
    }

    public boolean isConnectedToLand(BlockPos pos) {
        if (this.bird.m_9236_().getFluidState(pos).is(FluidTags.WATER)) {
            for (Direction dir : this.HORIZONTALS) {
                BlockPos offsetPos = pos.relative(dir);
                if (this.bird.m_9236_().getFluidState(offsetPos).isEmpty() && this.bird.m_9236_().getFluidState(offsetPos.above()).isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public BlockPos getLandPos(BlockPos pos) {
        if (this.bird.m_9236_().getFluidState(pos).is(FluidTags.WATER)) {
            for (Direction dir : this.HORIZONTALS) {
                BlockPos offsetPos = pos.relative(dir);
                if (this.bird.m_9236_().getFluidState(offsetPos).isEmpty() && this.bird.m_9236_().getFluidState(offsetPos.above()).isEmpty()) {
                    return offsetPos;
                }
            }
        }
        return null;
    }
}