package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.EntityRaccoon;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class AnimalAILootChests extends MoveToBlockGoal {

    private final Animal entity;

    private final ILootsChests chestLooter;

    private boolean hasOpenedChest = false;

    public AnimalAILootChests(Animal entity, int range) {
        super(entity, 1.0, range);
        this.entity = entity;
        this.chestLooter = (ILootsChests) entity;
    }

    public boolean isChestRaidable(LevelReader world, BlockPos pos) {
        if (world.m_8055_(pos).m_60734_() instanceof BaseEntityBlock) {
            Block block = world.m_8055_(pos).m_60734_();
            boolean listed = false;
            BlockEntity entity = world.m_7702_(pos);
            if (entity instanceof Container inventory) {
                try {
                    if (!inventory.isEmpty() && this.chestLooter.isLootable(inventory)) {
                        return true;
                    }
                } catch (Exception var8) {
                    AlexsMobs.LOGGER.warn("Alex's Mobs stopped a " + entity.getClass().getSimpleName() + " from causing a crash during access");
                    var8.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean canUse() {
        if (this.entity instanceof TamableAnimal && ((TamableAnimal) this.entity).isTame()) {
            return false;
        } else if (!AMConfig.raccoonsStealFromChests) {
            return false;
        } else if (!this.entity.m_21120_(InteractionHand.MAIN_HAND).isEmpty()) {
            return false;
        } else {
            return this.f_25600_ <= 0 && !ForgeEventFactory.getMobGriefingEvent(this.entity.m_9236_(), this.entity) ? false : super.canUse();
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
            if (this.chestLooter.shouldLootItem(stack)) {
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
    public void tick() {
        super.tick();
        if (this.f_25602_ != null) {
            if (this.entity.m_9236_().getBlockEntity(this.f_25602_) instanceof Container feeder) {
                double distance = this.entity.m_20275_((double) ((float) this.f_25602_.m_123341_() + 0.5F), (double) ((float) this.f_25602_.m_123342_() + 0.5F), (double) ((float) this.f_25602_.m_123343_() + 0.5F));
                if (this.hasLineOfSightChest()) {
                    if (this.m_25625_() && distance <= 3.0) {
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
                            if (this.entity instanceof EntityRaccoon) {
                                ((EntityRaccoon) this.entity).lookForWaterBeforeEatingTimer = 10;
                            }
                            stack.shrink(1);
                            this.stop();
                        }
                    } else if (distance < 5.0 && !this.hasOpenedChest) {
                        this.hasOpenedChest = true;
                        this.toggleChest(feeder, true);
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