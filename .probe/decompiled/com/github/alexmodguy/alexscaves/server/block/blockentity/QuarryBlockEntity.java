package com.github.alexmodguy.alexscaves.server.block.blockentity;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.QuarryBlock;
import com.github.alexmodguy.alexscaves.server.entity.item.QuarrySmasherEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class QuarryBlockEntity extends BlockEntity {

    private static int FURTHEST_TORCH_DISTANCE = 20;

    private float previousRotation;

    private float rotation;

    private BlockPos bottomLeftTorch;

    private BlockPos bottomRightTorch;

    private BlockPos topLeftTorch;

    private BlockPos topRightTorch;

    private boolean hasMiningArea;

    private int checkTimer;

    public int spinFor;

    private AABB miningBox;

    private QuarrySmasherEntity serverSmasher = null;

    private BlockPos lastMineablePos;

    public QuarryBlockEntity(BlockPos pos, BlockState state) {
        super(ACBlockEntityRegistry.QUARRY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, QuarryBlockEntity entity) {
        entity.previousRotation = entity.rotation;
        if (entity.checkTimer-- < 0) {
            entity.checkTimer = 20 + level.random.nextInt(20);
            if (entity.searchForTorches(level, blockPos, (Direction) state.m_61143_(QuarryBlock.FACING))) {
                entity.hasMiningArea = true;
                AABB aabb1 = new AABB(entity.bottomLeftTorch, entity.bottomRightTorch);
                AABB aabb2 = new AABB(entity.topLeftTorch, entity.topRightTorch);
                entity.miningBox = aabb1.minmax(aabb2);
                if (!level.isClientSide) {
                    entity.lastMineablePos = (BlockPos) entity.findMinableBlock(level, (double) (blockPos.m_123342_() + 3)).orElse(null);
                    if (entity.serverSmasher == null) {
                        QuarrySmasherEntity closest = null;
                        Vec3 center = Vec3.atCenterOf(blockPos);
                        for (QuarrySmasherEntity quarrySmasher : level.m_45976_(QuarrySmasherEntity.class, entity.miningBox.inflate(0.0, 100.0, 0.0))) {
                            if (closest == null || quarrySmasher.m_20238_(center) < closest.m_20238_(center)) {
                                closest = quarrySmasher;
                            }
                        }
                        entity.serverSmasher = closest;
                    }
                }
            } else {
                entity.hasMiningArea = false;
            }
        }
        if (level.isClientSide) {
            entity.spawnLightningBetween(level, entity.bottomLeftTorch, entity.bottomRightTorch);
            entity.spawnLightningBetween(level, entity.bottomLeftTorch, entity.topLeftTorch);
            entity.spawnLightningBetween(level, entity.bottomRightTorch, entity.topRightTorch);
            entity.spawnLightningBetween(level, entity.topLeftTorch, entity.topRightTorch);
        } else if (entity.serverSmasher != null) {
            entity.serverSmasher.setQuarryPos(blockPos);
            if (entity.serverSmasher.m_213877_()) {
                entity.serverSmasher = null;
            } else if (entity.hasMiningArea && entity.lastMineablePos != null) {
                entity.serverSmasher.setInactive(false);
            } else {
                entity.serverSmasher.setInactive(true);
                entity.serverSmasher = null;
            }
        }
        if (entity.spinFor > 0) {
            entity.spinFor--;
            entity.rotation = entity.rotation + (float) Math.min(10, entity.spinFor) * 0.1F;
        }
    }

    private boolean searchForTorches(Level level, BlockPos blockPos, Direction blockFacing) {
        BlockPos directlyBehind = blockPos.relative(blockFacing.getOpposite());
        BlockPos.MutableBlockPos mutableTorchPos = directlyBehind.mutable();
        int dist = 0;
        Direction leftTorchDir = blockFacing.getOpposite().getCounterClockWise();
        Direction rightTorchDir;
        for (rightTorchDir = blockFacing.getOpposite().getClockWise(); dist < FURTHEST_TORCH_DISTANCE && !isMinable(level, mutableTorchPos) && level.isLoaded(mutableTorchPos); dist++) {
            mutableTorchPos.move(leftTorchDir);
        }
        if (!level.getBlockState(mutableTorchPos).m_60713_(ACBlockRegistry.MAGNETIC_LIGHT.get())) {
            this.bottomLeftTorch = null;
            return false;
        } else {
            this.bottomLeftTorch = mutableTorchPos.immutable();
            dist = 0;
            mutableTorchPos.set(directlyBehind);
            mutableTorchPos.move(rightTorchDir);
            while (dist < FURTHEST_TORCH_DISTANCE && !isMinable(level, mutableTorchPos) && level.isLoaded(mutableTorchPos)) {
                mutableTorchPos.move(rightTorchDir);
                dist++;
            }
            if (!level.getBlockState(mutableTorchPos).m_60713_(ACBlockRegistry.MAGNETIC_LIGHT.get())) {
                this.bottomRightTorch = null;
                return false;
            } else {
                this.bottomRightTorch = mutableTorchPos.immutable();
                dist = 0;
                mutableTorchPos.set(this.bottomLeftTorch);
                mutableTorchPos.move(blockFacing.getOpposite());
                while (dist < FURTHEST_TORCH_DISTANCE && !isMinable(level, mutableTorchPos) && level.isLoaded(mutableTorchPos)) {
                    mutableTorchPos.move(blockFacing.getOpposite());
                    dist++;
                }
                if (!level.getBlockState(mutableTorchPos).m_60713_(ACBlockRegistry.MAGNETIC_LIGHT.get())) {
                    this.topLeftTorch = null;
                    return false;
                } else {
                    this.topLeftTorch = mutableTorchPos.immutable();
                    dist = 0;
                    mutableTorchPos.set(this.bottomRightTorch);
                    mutableTorchPos.move(blockFacing.getOpposite());
                    while (dist < FURTHEST_TORCH_DISTANCE && !isMinable(level, mutableTorchPos) && level.isLoaded(mutableTorchPos)) {
                        mutableTorchPos.move(blockFacing.getOpposite());
                        dist++;
                    }
                    if (level.getBlockState(mutableTorchPos).m_60713_(ACBlockRegistry.MAGNETIC_LIGHT.get())) {
                        this.topRightTorch = mutableTorchPos.immutable();
                        if (this.topRightTorch.m_123341_() - this.topLeftTorch.m_123341_() != 0 && this.topRightTorch.m_123343_() - this.topLeftTorch.m_123343_() != 0) {
                            return false;
                        } else if (this.topRightTorch.m_123341_() - this.bottomRightTorch.m_123341_() != 0 && this.topRightTorch.m_123343_() - this.bottomRightTorch.m_123343_() != 0) {
                            return false;
                        } else {
                            return this.topLeftTorch.m_123341_() - this.bottomLeftTorch.m_123341_() != 0 && this.topLeftTorch.m_123343_() - this.bottomLeftTorch.m_123343_() != 0 ? false : this.bottomRightTorch.m_123341_() - this.bottomLeftTorch.m_123341_() == 0 || this.bottomRightTorch.m_123343_() - this.bottomLeftTorch.m_123343_() == 0;
                        }
                    } else {
                        this.topRightTorch = null;
                        return false;
                    }
                }
            }
        }
    }

    private void spawnLightningBetween(Level level, BlockPos pos1, BlockPos pos2) {
        if (pos1 != null && pos2 != null && level.random.nextInt(4) == 0) {
            Vec3 particleFrom;
            Vec3 particleTo;
            if (level.random.nextBoolean()) {
                particleFrom = Vec3.upFromBottomCenterOf(pos1, 0.4F);
                particleTo = Vec3.upFromBottomCenterOf(pos2, 0.4F);
            } else {
                particleFrom = Vec3.upFromBottomCenterOf(pos2, 0.4F);
                particleTo = Vec3.upFromBottomCenterOf(pos1, 0.4F);
            }
            level.addParticle(ACParticleRegistry.QUARRY_BORDER_LIGHTING.get(), particleFrom.x, particleFrom.y, particleFrom.z, particleTo.x, particleTo.y, particleTo.z);
        }
    }

    public Optional<BlockPos> findMinableBlock(Level level, double yStart) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        BlockPos highest = null;
        for (int x = (int) (this.miningBox.minX + 1.0); x < (int) this.miningBox.maxX; x++) {
            for (int z = (int) (this.miningBox.minZ + 1.0); z < (int) this.miningBox.maxZ; z++) {
                mutableBlockPos.set((double) x, yStart, (double) z);
                while (mutableBlockPos.m_123342_() > level.m_141937_() + 1 && !isMinable(level, mutableBlockPos)) {
                    mutableBlockPos.move(0, -1, 0);
                }
                if (isMinable(level, mutableBlockPos) && (highest == null || highest.m_123342_() < mutableBlockPos.m_123342_())) {
                    highest = mutableBlockPos.immutable();
                }
            }
        }
        return Optional.ofNullable(highest);
    }

    public static boolean isMinable(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return !state.m_204336_(ACTagRegistry.UNMOVEABLE) && !state.m_60795_() && !state.m_247087_();
    }

    public AABB getMiningBox() {
        return this.miningBox;
    }

    public boolean hasMiningArea() {
        return this.hasMiningArea;
    }

    public float getGrindRotation(float partialTicks) {
        return this.previousRotation + (this.rotation - this.previousRotation) * partialTicks;
    }
}