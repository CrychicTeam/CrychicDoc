package org.violetmoon.quark.addons.oddities.block.be;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.violetmoon.quark.addons.oddities.block.MagnetBlock;
import org.violetmoon.quark.addons.oddities.magnetsystem.MagnetSystem;
import org.violetmoon.quark.addons.oddities.module.MagnetsModule;
import org.violetmoon.quark.api.IMagneticEntity;
import org.violetmoon.quark.mixin.mixins.accessor.AccessorServerGamePacketListener;
import org.violetmoon.zeta.api.ICollateralMover;

public class MagnetBlockEntity extends BlockEntity {

    public MagnetBlockEntity(BlockPos pos, BlockState state) {
        super(MagnetsModule.magnetType, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MagnetBlockEntity be) {
        boolean powered = (Boolean) state.m_61143_(MagnetBlock.POWERED);
        if (powered) {
            Direction dir = (Direction) state.m_61143_(MagnetBlock.FACING);
            int power = level.m_277086_(pos);
            be.magnetize(state, dir, dir, power);
            be.magnetize(state, dir.getOpposite(), dir, power);
        }
    }

    private void magnetize(BlockState state, Direction dir, Direction moveDir, int power) {
        if (this.f_58857_ != null) {
            double magnitude = (double) (dir == moveDir ? 1 : -1);
            int blockDist;
            for (blockDist = 1; blockDist <= power; blockDist++) {
                BlockPos targetPos = this.f_58858_.relative(dir, blockDist);
                BlockState targetState = this.f_58857_.getBlockState(targetPos);
                if (!this.f_58857_.isClientSide) {
                    ICollateralMover.MoveResult reaction = MagnetSystem.getPushAction(this, targetPos, targetState, moveDir);
                    if (reaction != ICollateralMover.MoveResult.MOVE && reaction != ICollateralMover.MoveResult.BREAK) {
                        if (reaction == ICollateralMover.MoveResult.PREVENT) {
                            break;
                        }
                    } else {
                        MagnetSystem.applyForce(this.f_58857_, targetPos, power - blockDist + 1, dir == moveDir, moveDir, blockDist, this.f_58858_);
                    }
                }
                if (targetState.m_60734_() == MagnetsModule.magnetized_block && this.f_58857_.getBlockEntity(targetPos) instanceof MagnetizedBlockBlockEntity mbe) {
                    targetState = mbe.f_58856_;
                }
                if (!this.canFluxPenetrate(targetPos, targetState)) {
                    break;
                }
            }
            if (MagnetsModule.affectEntities && blockDist > 1) {
                for (Entity e : this.f_58857_.getEntities((Entity) null, new AABB(this.f_58858_).expandTowards(new Vec3(dir.step().mul((float) blockDist))), this::canPullEntity)) {
                    this.pushEntity(dir, magnitude, e);
                }
            }
            if (this.f_58857_.isClientSide && !(Boolean) state.m_61143_(MagnetBlock.WAXED)) {
                double particleMotion = 0.06 * magnitude;
                double particleChance = 0.2;
                double xOff = (double) dir.getStepX() * particleMotion;
                double yOff = (double) dir.getStepY() * particleMotion;
                double zOff = (double) dir.getStepZ() * particleMotion;
                double particleOffset = moveDir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0 : -1.0;
                for (int j = 1; j < blockDist; j++) {
                    if ((double) this.f_58857_.random.nextFloat() <= particleChance) {
                        BlockPos targetPosx = this.f_58858_.relative(dir, j);
                        double x = (double) targetPosx.m_123341_() + this.getParticlePos(xOff, this.f_58857_.random, particleOffset);
                        double y = (double) targetPosx.m_123342_() + this.getParticlePos(yOff, this.f_58857_.random, particleOffset);
                        double z = (double) targetPosx.m_123343_() + this.getParticlePos(zOff, this.f_58857_.random, particleOffset);
                        SimpleParticleType p = dir == moveDir ? MagnetsModule.repulsorParticle : MagnetsModule.attractorParticle;
                        this.f_58857_.addParticle(p, x, y, z, xOff, yOff, zOff);
                    }
                }
            }
        }
    }

    private void pushEntity(Direction dir, double magnitude, Entity e) {
        double distanceFromMagnetSq = e.distanceToSqr(this.f_58858_.getCenter());
        double invSquared = 1.0 / distanceFromMagnetSq;
        Vec3 vec = new Vec3(dir.step().mul((float) (invSquared * magnitude * MagnetsModule.entitiesPullForce)));
        if (e instanceof IMagneticEntity me) {
            me.moveByMagnet(e, vec, this);
        } else {
            e.push(vec.x(), vec.y(), vec.z());
            if (e instanceof ServerPlayer player) {
                ((AccessorServerGamePacketListener) player.connection).setAboveGroundTickCount(0);
            } else {
                e.hurtMarked = true;
            }
            if (e instanceof FallingBlockEntity fb) {
                fb.time--;
            }
            e.fallDistance = 0.0F;
        }
    }

    private boolean canPullEntity(Entity e) {
        if (this.f_58857_.isClientSide) {
            if (MagnetsModule.affectsArmor && e instanceof Player) {
                for (ItemStack armor : e.getArmorSlots()) {
                    if (MagnetSystem.isItemMagnetic(armor.getItem())) {
                        return true;
                    }
                }
            }
            return false;
        } else if (e instanceof IMagneticEntity) {
            return true;
        } else if (e instanceof ItemEntity ie) {
            return MagnetSystem.isItemMagnetic(ie.getItem().getItem());
        } else if (e.getType().is(MagnetsModule.magneticEntities)) {
            return true;
        } else if (e instanceof FallingBlockEntity fb) {
            return MagnetSystem.isBlockMagnetic(fb.getBlockState());
        } else {
            if (MagnetsModule.affectsArmor) {
                for (ItemStack armorx : e.getArmorSlots()) {
                    if (MagnetSystem.isItemMagnetic(armorx.getItem())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private boolean canFluxPenetrate(BlockPos targetPos, BlockState targetState) {
        return targetState.m_60795_() || targetState.m_60812_(this.f_58857_, targetPos).isEmpty();
    }

    private double getParticlePos(double offset, RandomSource ran, double magnitude) {
        return offset == 0.0 ? (double) (0.5F + (ran.nextFloat() + ran.nextFloat() - 1.0F) / 2.0F) : 0.5 + magnitude * ((double) ran.nextFloat() - 1.25);
    }
}