package com.github.alexthe666.alexsmobs.tileentity;

import com.github.alexthe666.alexsmobs.block.BlockEndPirateAnchor;
import com.github.alexthe666.alexsmobs.block.BlockEndPirateAnchorWinch;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityEndPirateAnchorWinch extends BlockEntity {

    public float clientRoll;

    public int windCounter = 0;

    private int prevTargetChainLength;

    private int targetChainLength = 0;

    private float prevMaximumChainLength;

    private float chainLength;

    private float prevChainLength;

    private int windTime = 0;

    private int ticksExisted = 0;

    private float windProgress;

    private float prevWindProgress;

    private boolean draggingAnchor;

    private boolean anchorEW;

    private boolean pullingUp;

    private boolean hasPower;

    private int anchorPlaceCooldown = 0;

    public TileEntityEndPirateAnchorWinch(BlockPos pos, BlockState state) {
        super(AMTileEntityRegistry.END_PIRATE_ANCHOR_WINCH.get(), pos, state);
        this.prevTargetChainLength = this.targetChainLength;
    }

    public static void commonTick(Level level, BlockPos pos, BlockState state, TileEntityEndPirateAnchorWinch entity) {
        entity.tick();
    }

    private int calcChainLength(boolean goBelowAnchor) {
        BlockPos down = this.m_58899_().below();
        while (this.f_58857_ != null && down.m_123342_() > this.f_58857_.m_141937_() && !this.isAnchorTop(this.f_58857_, down) && (this.isEmptyBlock(down) || this.isAnchorChain(this.f_58857_, down))) {
            down = down.below();
        }
        int i = 0;
        if (this.isAnchorTop(this.f_58857_, down) || goBelowAnchor) {
            if (goBelowAnchor) {
                i = this.m_58899_().m_123342_() - 1 - this.keepMovingBelowAnchor(down.below(2));
            } else {
                i = this.m_58899_().m_123342_() - 1 - down.m_123342_();
            }
        }
        return this.draggingAnchor ? i - 3 : i;
    }

    private int keepMovingBelowAnchor(BlockPos below) {
        while (below.m_123342_() > this.f_58857_.m_141937_() && this.isEmptyBlock(below)) {
            below = below.below();
        }
        return below.m_123342_();
    }

    private boolean isEmptyBlock(BlockPos pos) {
        return this.f_58857_.m_46859_(pos) || this.isAnchorChain(this.f_58857_, pos) || this.f_58857_.getBlockState(pos).m_247087_();
    }

    private boolean isAnchorChain(Level level, BlockPos pos) {
        return level.getBlockState(pos).m_60734_() instanceof BlockEndPirateAnchor && level.getBlockState(pos).m_61143_(BlockEndPirateAnchor.PIECE) == BlockEndPirateAnchor.PieceType.CHAIN;
    }

    private boolean isAnchorTop(Level level, BlockPos pos) {
        return level.getBlockState(pos).m_60734_() instanceof BlockEndPirateAnchor && level.getBlockState(pos.below(2)).m_60734_() instanceof BlockEndPirateAnchor && level.getBlockState(pos.below(2)).m_61143_(BlockEndPirateAnchor.PIECE) == BlockEndPirateAnchor.PieceType.ANCHOR;
    }

    private void tick() {
        this.prevChainLength = this.chainLength;
        this.prevWindProgress = this.windProgress;
        this.prevTargetChainLength = this.targetChainLength;
        this.ticksExisted++;
        boolean powered = false;
        if (this.m_58900_().m_60734_() instanceof BlockEndPirateAnchorWinch) {
            powered = (Boolean) this.m_58900_().m_61143_(BlockEndPirateAnchorWinch.POWERED);
        }
        if (powered && this.pullingUp) {
            this.sendDownChains();
        }
        if (!powered && !this.pullingUp) {
            this.pullUpChains();
        }
        if (this.chainLength < (float) this.targetChainLength) {
            this.chainLength = Math.min(this.chainLength + 0.1F, (float) this.targetChainLength);
        }
        if (this.chainLength > (float) this.targetChainLength) {
            this.chainLength = Math.max(this.chainLength - 0.1F, (float) this.targetChainLength);
        }
        if (Math.abs((float) this.targetChainLength - this.chainLength) > 0.2F) {
            this.windTime = 5;
        }
        if (this.windTime > 0) {
            this.windCounter++;
            this.windTime--;
            if (this.windProgress < 1.0F) {
                this.windProgress += 0.25F;
            }
        } else {
            this.windCounter = 0;
            if (this.windProgress > 0.0F) {
                this.windProgress -= 0.25F;
            }
        }
        if (this.anchorPlaceCooldown > 0) {
            this.anchorPlaceCooldown--;
        }
        if (this.chainLength != (float) this.targetChainLength && this.isWindingUp() && !this.draggingAnchor) {
            BlockPos down = this.m_58899_();
            if (this.anchorPlaceCooldown == 0 && (this.checkAndBreakAnchor(down.below()) || this.checkAndBreakAnchor(down.below(1 + (int) Math.ceil((double) this.chainLength))))) {
                this.draggingAnchor = true;
            }
        }
        if (this.chainLength == (float) this.targetChainLength && this.draggingAnchor) {
            int offset = this.isWindingUp() ? 0 : this.targetChainLength;
            if (this.anchorPlaceCooldown == 0 && this.tryPlaceAnchor(offset)) {
                this.draggingAnchor = false;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public boolean checkAndBreakAnchor(BlockPos down) {
        if (this.f_58857_.getBlockState(down).m_60734_() instanceof BlockEndPirateAnchor) {
            this.anchorEW = (Boolean) this.f_58857_.getBlockState(down).m_61143_(BlockEndPirateAnchor.EASTORWEST);
            BlockPos actualAnchorPos = down.below(2);
            if (this.f_58857_.getBlockState(actualAnchorPos).m_60734_() instanceof BlockEndPirateAnchor) {
                BlockEndPirateAnchor.removeAnchor(this.f_58857_, actualAnchorPos, this.f_58857_.getBlockState(actualAnchorPos));
                this.removeChainBlocks();
                return true;
            }
        }
        return false;
    }

    public boolean tryPlaceAnchor(int offset) {
        BlockPos at = this.m_58899_().below(3 + offset);
        if (BlockEndPirateAnchor.isClearForPlacement(this.f_58857_, at, this.anchorEW)) {
            BlockState anchorState = null;
            this.f_58857_.setBlock(at, anchorState, 2);
            BlockEndPirateAnchor.placeAnchor(this.f_58857_, at, anchorState);
            this.placeChainBlocks(offset);
            return true;
        } else {
            return false;
        }
    }

    private void placeChainBlocks(int offset) {
        BlockPos at = this.m_58899_().below(3 + offset);
        BlockPos chainPos = at.above(3);
        while (chainPos.m_123342_() < this.m_58899_().m_123342_() - 1 && this.isEmptyBlock(chainPos)) {
            chainPos = chainPos.above();
        }
    }

    private void removeChainBlocks() {
        for (BlockPos chainPos = this.m_58899_().below(1 + (int) Math.ceil((double) this.chainLength)); chainPos.m_123342_() < this.m_58899_().m_123342_(); chainPos = chainPos.above()) {
            if (this.isAnchorChain(this.f_58857_, chainPos)) {
                this.f_58857_.setBlock(chainPos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    public void recalculateChains() {
        if (this.targetChainLength != 0) {
            this.prevMaximumChainLength = (float) this.targetChainLength;
        }
        BlockPos at = this.m_58899_().below(1);
        if (this.isAnchorTop(this.f_58857_, at) && this.anchorPlaceCooldown == 0 && this.checkAndBreakAnchor(at)) {
            this.draggingAnchor = true;
        }
        this.targetChainLength = this.calcChainLength(this.draggingAnchor);
    }

    public void sendDownChains() {
        this.recalculateChains();
        this.pullingUp = false;
    }

    public void pullUpChains() {
        if (this.targetChainLength != 0) {
            this.prevMaximumChainLength = (float) this.targetChainLength;
        }
        this.targetChainLength = 0;
        this.pullingUp = true;
    }

    public void onInteract() {
    }

    public float getChainLengthForRender() {
        return Math.max((float) this.targetChainLength, this.prevMaximumChainLength);
    }

    public float getChainLength(float partialTick) {
        return this.prevChainLength + (this.chainLength - this.prevChainLength) * partialTick;
    }

    public float getWindProgress(float partialTick) {
        return this.prevWindProgress + (this.windProgress - this.prevWindProgress) * partialTick;
    }

    public boolean isAnchorEW() {
        return this.anchorEW;
    }

    public boolean isWinching() {
        return this.windTime > 0;
    }

    public boolean isWindingUp() {
        return this.pullingUp;
    }

    public boolean hasAnchor() {
        return this.draggingAnchor;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.pullingUp = compound.getBoolean("PullingUp");
        this.draggingAnchor = compound.getBoolean("DraggingAnchor");
        this.anchorEW = compound.getBoolean("EWAnchor");
        this.prevChainLength = this.chainLength = compound.getFloat("ChainLength");
        this.targetChainLength = compound.getInt("TargetChainLength");
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("PullingUp", this.pullingUp);
        compound.putBoolean("DraggingAnchor", this.draggingAnchor);
        compound.putBoolean("EWAnchor", this.anchorEW);
        compound.putFloat("ChainLength", this.chainLength);
        compound.putInt("TargetChainLength", this.targetChainLength);
    }
}