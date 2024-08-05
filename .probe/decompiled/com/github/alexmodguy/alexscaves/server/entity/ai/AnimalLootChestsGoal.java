package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.util.ChestThief;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class AnimalLootChestsGoal extends MoveToBlockGoal {

    private final Animal entity;

    private final ChestThief chestLooter;

    private boolean hasOpenedChest = false;

    public AnimalLootChestsGoal(Animal entity, int range) {
        super(entity, 1.0, range, 6);
        this.entity = entity;
        this.chestLooter = (ChestThief) entity;
    }

    @Override
    protected int nextStartTick(PathfinderMob mob) {
        return m_186073_(100 + this.entity.m_217043_().nextInt(100));
    }

    public boolean isChestRaidable(LevelReader world, BlockPos pos) {
        if (world.m_8055_(pos).m_60734_() instanceof BaseEntityBlock) {
            BlockEntity entity = world.m_7702_(pos);
            if (entity instanceof Container inventory) {
                try {
                    if (!inventory.isEmpty() && this.chestLooter.isLootable(inventory)) {
                        return true;
                    }
                } catch (Exception var6) {
                    AlexsCaves.LOGGER.warn("Alex's Caves stopped a " + entity.getClass().getSimpleName() + " from causing a crash during access");
                    var6.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    protected BlockPos getMoveToTarget() {
        return this.f_25602_;
    }

    @Override
    protected void moveMobToBlock() {
        BlockPos pos = this.getMoveToTarget();
        this.f_25598_.m_21573_().moveTo((double) ((float) pos.m_123341_()) + 0.5, (double) (pos.m_123342_() + 1), (double) ((float) pos.m_123343_()) + 0.5, this.f_25599_);
    }

    @Override
    public boolean canUse() {
        if (this.entity instanceof TamableAnimal && ((TamableAnimal) this.entity).isTame()) {
            return false;
        } else {
            return !this.entity.m_21120_(InteractionHand.MAIN_HAND).isEmpty() ? false : super.canUse();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.entity.m_21120_(InteractionHand.MAIN_HAND).isEmpty();
    }

    public boolean hasLineOfSightChest() {
        HitResult raytraceresult = this.entity.m_9236_().m_45547_(new ClipContext(this.entity.m_20299_(1.0F), new Vec3((double) this.f_25602_.m_123341_() + 0.5, (double) this.f_25602_.m_123342_() + 0.5, (double) this.f_25602_.m_123343_() + 0.5), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.entity));
        if (!(raytraceresult instanceof BlockHitResult blockRayTraceResult)) {
            return true;
        } else {
            BlockPos pos = blockRayTraceResult.getBlockPos();
            return pos.equals(this.f_25602_) || this.entity.m_9236_().m_46859_(pos) || this.entity.m_9236_().getBlockEntity(pos) == this.entity.m_9236_().getBlockEntity(this.f_25602_);
        }
    }

    public ItemStack getFoodFromInventory(Container inventory, RandomSource random) {
        List<ItemStack> items = new ArrayList();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && this.chestLooter.shouldLootItem(stack)) {
                items.add(stack);
            }
        }
        if (items.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            return items.size() == 1 ? (ItemStack) items.get(0) : (ItemStack) items.get(random.nextInt(items.size() - 1));
        }
    }

    @Override
    public double acceptedDistance() {
        return Math.pow((double) this.entity.m_20205_(), 2.0) + 3.0;
    }

    @Override
    public boolean shouldRecalculatePath() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.entity.m_9236_().getBlockEntity(this.f_25602_) instanceof Container feeder) {
            double distance = this.entity.m_20275_((double) ((float) this.f_25602_.m_123341_() + 0.5F), (double) ((float) this.f_25602_.m_123342_() + 0.5F), (double) ((float) this.f_25602_.m_123343_() + 0.5F));
            this.entity.m_21573_().moveTo((double) ((float) this.f_25602_.m_123341_() + 0.5F), (double) (this.f_25602_.m_123342_() - 1), (double) ((float) this.f_25602_.m_123343_() + 0.5F), 1.0);
            if (this.hasLineOfSightChest()) {
                this.entity.m_7618_(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(this.f_25602_));
                if (distance <= this.acceptedDistance()) {
                    this.entity.m_21573_().stop();
                    this.chestLooter.startOpeningChest();
                    if (!this.hasOpenedChest) {
                        this.hasOpenedChest = true;
                        this.toggleChest(feeder, true);
                    }
                    if (this.hasOpenedChest && this.chestLooter.didOpeningChest()) {
                        this.toggleChest(feeder, false);
                        ItemStack stack = this.getFoodFromInventory(feeder, this.entity.m_9236_().random);
                        if (stack == ItemStack.EMPTY) {
                            this.stop();
                        } else {
                            ItemStack duplicate = stack.copy();
                            duplicate.setCount(1);
                            if (!this.entity.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && !this.entity.m_9236_().isClientSide) {
                                this.entity.m_5552_(this.entity.m_21120_(InteractionHand.MAIN_HAND), 0.0F);
                            }
                            this.entity.m_21008_(InteractionHand.MAIN_HAND, duplicate);
                            stack.shrink(1);
                            this.chestLooter.afterSteal(this.f_25602_);
                            this.stop();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void stop() {
        super.m_8041_();
        if (this.f_25602_ != null) {
            BlockEntity te = this.entity.m_9236_().getBlockEntity(this.f_25602_);
            if (te instanceof Container) {
                this.toggleChest((Container) te, false);
            }
        }
        this.f_25602_ = BlockPos.ZERO;
        this.hasOpenedChest = false;
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        return pos != null && this.isChestRaidable(worldIn, pos);
    }

    public void toggleChest(Container te, boolean open) {
        if (te instanceof ChestBlockEntity chest) {
            if (open) {
                this.entity.m_9236_().blockEvent(this.f_25602_, chest.m_58900_().m_60734_(), 1, 1);
            } else {
                this.entity.m_9236_().blockEvent(this.f_25602_, chest.m_58900_().m_60734_(), 1, 0);
            }
            this.entity.m_9236_().updateNeighborsAt(this.f_25602_, chest.m_58900_().m_60734_());
            this.entity.m_9236_().updateNeighborsAt(this.f_25602_.below(), chest.m_58900_().m_60734_());
        }
    }
}