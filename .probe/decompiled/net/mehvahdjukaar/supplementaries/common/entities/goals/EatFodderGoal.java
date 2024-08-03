package net.mehvahdjukaar.supplementaries.common.entities.goals;

import java.util.EnumSet;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FodderBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.phys.Vec3;

public class EatFodderGoal extends MoveToBlockGoal {

    private final Animal animal;

    private final int blockBreakingTime;

    private int ticksSinceReachedGoal;

    protected int lastBreakProgress = -1;

    private static final BlockState FODDER_STATE = ((Block) ModRegistry.FODDER.get()).defaultBlockState();

    public EatFodderGoal(Animal entity, double speedModifier, int searchRange, int verticalSearchRange, int breakTime) {
        super(entity, speedModifier, searchRange, verticalSearchRange);
        this.animal = entity;
        this.blockBreakingTime = breakTime;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!this.animal.canFallInLove() || this.animal.m_146764_() > 0) {
            return false;
        } else if (this.f_25600_ > 0) {
            this.f_25600_--;
            return false;
        } else if (this.f_25602_ != BlockPos.ZERO && !ForgeHelper.canEntityDestroy(this.animal.m_9236_(), this.f_25602_, this.animal)) {
            return false;
        } else if (this.tryFindBlock()) {
            this.f_25600_ = 600;
            return true;
        } else {
            this.f_25600_ = this.nextStartTick(this.f_25598_);
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.f_25601_ >= -100 && this.f_25601_ <= 200 && this.isValidTarget(this.f_25598_.m_9236_(), this.f_25602_);
    }

    private boolean tryFindBlock() {
        return this.f_25602_ != BlockPos.ZERO && this.isValidTarget(this.f_25598_.m_9236_(), this.f_25602_) || this.m_25626_();
    }

    @Override
    protected int nextStartTick(PathfinderMob pCreature) {
        return 800 + pCreature.m_217043_().nextInt(400);
    }

    @Override
    public void stop() {
        super.m_8041_();
        this.animal.f_19789_ = 1.0F;
        this.f_25602_ = BlockPos.ZERO;
    }

    @Override
    public void start() {
        super.start();
        this.ticksSinceReachedGoal = 0;
    }

    @Override
    public double acceptedDistance() {
        return 1.5;
    }

    @Override
    public void tick() {
        super.tick();
        Level level = this.animal.m_9236_();
        RandomSource random = this.animal.m_217043_();
        if (this.m_25625_()) {
            this.f_25601_--;
            BlockPos targetPos = this.m_6669_().below();
            Vec3 vector3d = Vec3.atBottomCenterOf(targetPos);
            this.f_25598_.m_21563_().setLookAt(vector3d.x(), vector3d.y(), vector3d.z());
            if (this.ticksSinceReachedGoal > 0 && !level.isClientSide && this.ticksSinceReachedGoal % 2 == 0) {
                ((ServerLevel) level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, FODDER_STATE), (double) targetPos.m_123341_() + 0.5, (double) targetPos.m_123342_() + 0.7, (double) targetPos.m_123343_() + 0.5, 3, ((double) random.nextFloat() - 0.5) * 0.08, ((double) random.nextFloat() - 0.5) * 0.08, ((double) random.nextFloat() - 0.5) * 0.08, 0.15F);
            }
            if (this.ticksSinceReachedGoal == 1 && this.animal instanceof Sheep) {
                level.broadcastEntityEvent(this.f_25598_, (byte) 10);
            }
            int k = (int) ((float) this.ticksSinceReachedGoal / (float) this.blockBreakingTime * 10.0F);
            if (k != this.lastBreakProgress) {
                level.destroyBlockProgress(this.f_25598_.m_19879_(), this.f_25602_, k);
                this.lastBreakProgress = k;
            }
            if (this.ticksSinceReachedGoal > this.blockBreakingTime) {
                BlockState state = level.getBlockState(targetPos);
                if (state.m_60713_((Block) ModRegistry.FODDER.get())) {
                    int layers = (Integer) state.m_61143_(FodderBlock.LAYERS);
                    if (layers > 1) {
                        level.m_46796_(2001, targetPos, Block.getId(FODDER_STATE));
                        level.setBlock(targetPos, (BlockState) FODDER_STATE.m_61124_(FodderBlock.LAYERS, layers - 1), 2);
                    } else {
                        level.m_46961_(targetPos, false);
                    }
                    if (!level.isClientSide) {
                        ((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER, this.animal.m_20185_(), this.animal.m_20186_(), this.animal.m_20189_(), 5, (double) (this.animal.m_20205_() / 2.0F), (double) (this.animal.m_20206_() / 2.0F), (double) (this.animal.m_20205_() / 2.0F), 0.0);
                    }
                    if (!this.animal.m_6162_()) {
                        this.animal.setInLove(null);
                    }
                    this.animal.m_8035_();
                }
                this.f_25600_ = this.nextStartTick(this.f_25598_);
                this.f_25601_ = 800;
            }
            this.ticksSinceReachedGoal++;
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader world, BlockPos pos) {
        ChunkAccess chunk = world.getChunk(SectionPos.blockToSectionCoord(pos.m_123341_()), SectionPos.blockToSectionCoord(pos.m_123343_()), ChunkStatus.FULL, false);
        return chunk == null ? false : chunk.m_8055_(pos).m_60713_((Block) ModRegistry.FODDER.get()) && chunk.m_8055_(pos.above()).m_60795_();
    }
}