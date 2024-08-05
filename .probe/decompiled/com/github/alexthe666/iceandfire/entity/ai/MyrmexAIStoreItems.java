package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.block.BlockMyrmexCocoon;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityMyrmexCocoon;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathFindingStatus;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathResult;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MyrmexAIStoreItems extends Goal {

    private final EntityMyrmexBase myrmex;

    private final double movementSpeed;

    private BlockPos nextRoom = null;

    private BlockPos nextCocoon = null;

    private BlockPos mainRoom = null;

    private boolean first = true;

    private PathResult path;

    public MyrmexAIStoreItems(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.myrmex.canMove() || this.myrmex instanceof EntityMyrmexWorker && ((EntityMyrmexWorker) this.myrmex).holdingBaby() || !this.myrmex.shouldEnterHive() && !this.myrmex.m_21573_().isDone() || this.myrmex.m_21120_(InteractionHand.MAIN_HAND).isEmpty()) {
            return false;
        } else if (!(this.myrmex.m_21573_() instanceof AdvancedPathNavigate) || this.myrmex.m_20159_()) {
            return false;
        } else if (this.myrmex.getWaitTicks() > 0) {
            return false;
        } else {
            MyrmexHive village = this.myrmex.getHive();
            if (village == null) {
                return false;
            } else if (!this.myrmex.isInHive()) {
                return false;
            } else {
                this.first = true;
                this.mainRoom = MyrmexHive.getGroundedPos(this.myrmex.m_9236_(), village.getCenter());
                this.nextRoom = MyrmexHive.getGroundedPos(this.myrmex.m_9236_(), village.getRandomRoom(WorldGenMyrmexHive.RoomType.FOOD, this.myrmex.m_217043_(), this.myrmex.m_20183_()));
                this.nextCocoon = this.getNearbyCocoon(this.nextRoom);
                if (this.nextCocoon == null) {
                    this.myrmex.setWaitTicks(20 + ThreadLocalRandom.current().nextInt(40));
                }
                this.path = ((AdvancedPathNavigate) this.myrmex.m_21573_()).moveToXYZ((double) this.mainRoom.m_123341_() + 0.5, (double) this.mainRoom.m_123342_() + 0.5, (double) this.mainRoom.m_123343_() + 0.5, this.movementSpeed);
                return this.nextCocoon != null;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.myrmex.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && this.nextCocoon != null && this.isUseableCocoon(this.nextCocoon) && !this.myrmex.isCloseEnoughToTarget(this.nextCocoon, 3.0) && this.myrmex.shouldEnterHive();
    }

    @Override
    public void tick() {
        if (this.first && this.mainRoom != null) {
            if (this.myrmex.isCloseEnoughToTarget(this.mainRoom, 10.0)) {
                this.path = ((AdvancedPathNavigate) this.myrmex.m_21573_()).moveToXYZ((double) this.nextCocoon.m_123341_() + 0.5, (double) this.nextCocoon.m_123342_() + 0.5, (double) this.nextCocoon.m_123343_() + 0.5, this.movementSpeed);
                this.first = false;
            } else if (!this.myrmex.pathReachesTarget(this.path, this.mainRoom, 9.0)) {
                this.nextCocoon = null;
            }
        }
        if (!this.first && this.nextCocoon != null) {
            double dist = 9.0;
            if (this.myrmex.isCloseEnoughToTarget(this.nextCocoon, 9.0) && !this.myrmex.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && this.isUseableCocoon(this.nextCocoon)) {
                TileEntityMyrmexCocoon cocoon = (TileEntityMyrmexCocoon) this.myrmex.m_9236_().getBlockEntity(this.nextCocoon);
                ItemStack itemstack = this.myrmex.m_21120_(InteractionHand.MAIN_HAND);
                if (!itemstack.isEmpty()) {
                    for (int i = 0; i < cocoon.getContainerSize(); i++) {
                        if (!itemstack.isEmpty()) {
                            ItemStack cocoonStack = cocoon.m_8020_(i);
                            if (cocoonStack.isEmpty()) {
                                cocoon.m_6836_(i, itemstack.copy());
                                cocoon.m_6596_();
                                this.myrmex.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                                this.myrmex.isEnteringHive = false;
                                return;
                            }
                            if (cocoonStack.getItem() == itemstack.getItem()) {
                                int j = Math.min(cocoon.getMaxStackSize(), cocoonStack.getMaxStackSize());
                                int k = Math.min(itemstack.getCount(), j - cocoonStack.getCount());
                                if (k > 0) {
                                    cocoonStack.grow(k);
                                    itemstack.shrink(k);
                                    if (itemstack.isEmpty()) {
                                        cocoon.m_6596_();
                                    }
                                    this.myrmex.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                                    this.myrmex.isEnteringHive = false;
                                    return;
                                }
                            }
                        }
                    }
                }
            } else if (!this.myrmex.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && this.path.getStatus() == PathFindingStatus.COMPLETE && !this.myrmex.pathReachesTarget(this.path, this.nextCocoon, 9.0)) {
                this.nextCocoon = this.getNearbyCocoon(this.nextRoom);
                if (this.nextCocoon != null) {
                    this.path = ((AdvancedPathNavigate) this.myrmex.m_21573_()).moveToXYZ((double) this.nextCocoon.m_123341_() + 0.5, (double) this.nextCocoon.m_123342_() + 0.5, (double) this.nextCocoon.m_123343_() + 0.5, this.movementSpeed);
                }
            } else if (this.myrmex.pathReachesTarget(this.path, this.nextCocoon, 9.0) && this.path.isCancelled()) {
                this.stop();
            }
        }
    }

    @Override
    public void stop() {
        this.nextRoom = null;
        this.nextCocoon = null;
        this.mainRoom = null;
        this.first = true;
    }

    public BlockPos getNearbyCocoon(BlockPos roomCenter) {
        int RADIUS_XZ = 15;
        int RADIUS_Y = 7;
        List<BlockPos> closeCocoons = new ArrayList();
        BlockPos.betweenClosedStream(roomCenter.offset(-RADIUS_XZ, -RADIUS_Y, -RADIUS_XZ), roomCenter.offset(RADIUS_XZ, RADIUS_Y, RADIUS_XZ)).forEach(blockpos -> {
            BlockEntity te = this.myrmex.m_9236_().getBlockEntity(blockpos);
            if (te instanceof TileEntityMyrmexCocoon && !((TileEntityMyrmexCocoon) te).isFull(this.myrmex.m_21120_(InteractionHand.MAIN_HAND))) {
                closeCocoons.add(te.getBlockPos());
            }
        });
        return closeCocoons.isEmpty() ? null : (BlockPos) closeCocoons.get(this.myrmex.m_217043_().nextInt(Math.max(closeCocoons.size() - 1, 1)));
    }

    public boolean isUseableCocoon(BlockPos blockpos) {
        return this.myrmex.m_9236_().getBlockState(blockpos).m_60734_() instanceof BlockMyrmexCocoon && this.myrmex.m_9236_().getBlockEntity(blockpos) != null && this.myrmex.m_9236_().getBlockEntity(blockpos) instanceof TileEntityMyrmexCocoon ? !((TileEntityMyrmexCocoon) this.myrmex.m_9236_().getBlockEntity(blockpos)).isFull(this.myrmex.m_21120_(InteractionHand.MAIN_HAND)) : false;
    }
}