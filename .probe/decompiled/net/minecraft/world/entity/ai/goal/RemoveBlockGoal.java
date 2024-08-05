package net.minecraft.world.entity.ai.goal;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.phys.Vec3;

public class RemoveBlockGoal extends MoveToBlockGoal {

    private final Block blockToRemove;

    private final Mob removerMob;

    private int ticksSinceReachedGoal;

    private static final int WAIT_AFTER_BLOCK_FOUND = 20;

    public RemoveBlockGoal(Block block0, PathfinderMob pathfinderMob1, double double2, int int3) {
        super(pathfinderMob1, double2, 24, int3);
        this.blockToRemove = block0;
        this.removerMob = pathfinderMob1;
    }

    @Override
    public boolean canUse() {
        if (!this.removerMob.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return false;
        } else if (this.f_25600_ > 0) {
            this.f_25600_--;
            return false;
        } else if (this.m_25626_()) {
            this.f_25600_ = m_186073_(20);
            return true;
        } else {
            this.f_25600_ = this.m_6099_(this.f_25598_);
            return false;
        }
    }

    @Override
    public void stop() {
        super.m_8041_();
        this.removerMob.f_19789_ = 1.0F;
    }

    @Override
    public void start() {
        super.start();
        this.ticksSinceReachedGoal = 0;
    }

    public void playDestroyProgressSound(LevelAccessor levelAccessor0, BlockPos blockPos1) {
    }

    public void playBreakSound(Level level0, BlockPos blockPos1) {
    }

    @Override
    public void tick() {
        super.tick();
        Level $$0 = this.removerMob.m_9236_();
        BlockPos $$1 = this.removerMob.m_20183_();
        BlockPos $$2 = this.getPosWithBlock($$1, $$0);
        RandomSource $$3 = this.removerMob.m_217043_();
        if (this.m_25625_() && $$2 != null) {
            if (this.ticksSinceReachedGoal > 0) {
                Vec3 $$4 = this.removerMob.m_20184_();
                this.removerMob.m_20334_($$4.x, 0.3, $$4.z);
                if (!$$0.isClientSide) {
                    double $$5 = 0.08;
                    ((ServerLevel) $$0).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.EGG)), (double) $$2.m_123341_() + 0.5, (double) $$2.m_123342_() + 0.7, (double) $$2.m_123343_() + 0.5, 3, ((double) $$3.nextFloat() - 0.5) * 0.08, ((double) $$3.nextFloat() - 0.5) * 0.08, ((double) $$3.nextFloat() - 0.5) * 0.08, 0.15F);
                }
            }
            if (this.ticksSinceReachedGoal % 2 == 0) {
                Vec3 $$6 = this.removerMob.m_20184_();
                this.removerMob.m_20334_($$6.x, -0.3, $$6.z);
                if (this.ticksSinceReachedGoal % 6 == 0) {
                    this.playDestroyProgressSound($$0, this.f_25602_);
                }
            }
            if (this.ticksSinceReachedGoal > 60) {
                $$0.removeBlock($$2, false);
                if (!$$0.isClientSide) {
                    for (int $$7 = 0; $$7 < 20; $$7++) {
                        double $$8 = $$3.nextGaussian() * 0.02;
                        double $$9 = $$3.nextGaussian() * 0.02;
                        double $$10 = $$3.nextGaussian() * 0.02;
                        ((ServerLevel) $$0).sendParticles(ParticleTypes.POOF, (double) $$2.m_123341_() + 0.5, (double) $$2.m_123342_(), (double) $$2.m_123343_() + 0.5, 1, $$8, $$9, $$10, 0.15F);
                    }
                    this.playBreakSound($$0, $$2);
                }
            }
            this.ticksSinceReachedGoal++;
        }
    }

    @Nullable
    private BlockPos getPosWithBlock(BlockPos blockPos0, BlockGetter blockGetter1) {
        if (blockGetter1.getBlockState(blockPos0).m_60713_(this.blockToRemove)) {
            return blockPos0;
        } else {
            BlockPos[] $$2 = new BlockPos[] { blockPos0.below(), blockPos0.west(), blockPos0.east(), blockPos0.north(), blockPos0.south(), blockPos0.below().below() };
            for (BlockPos $$3 : $$2) {
                if (blockGetter1.getBlockState($$3).m_60713_(this.blockToRemove)) {
                    return $$3;
                }
            }
            return null;
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader0, BlockPos blockPos1) {
        ChunkAccess $$2 = levelReader0.getChunk(SectionPos.blockToSectionCoord(blockPos1.m_123341_()), SectionPos.blockToSectionCoord(blockPos1.m_123343_()), ChunkStatus.FULL, false);
        return $$2 == null ? false : $$2.m_8055_(blockPos1).m_60713_(this.blockToRemove) && $$2.m_8055_(blockPos1.above()).m_60795_() && $$2.m_8055_(blockPos1.above(2)).m_60795_();
    }
}