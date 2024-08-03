package net.mehvahdjukaar.supplementaries.common.block.tiles;

import java.util.List;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SpringLauncherArmBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SpringLauncherBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SpringLauncherHeadBlock;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SpringLauncherArmBlockTile extends BlockEntity {

    private int age;

    private double increment;

    private double offset;

    private double prevOffset;

    private int dx;

    private int dy;

    private int dz;

    public SpringLauncherArmBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.SPRING_LAUNCHER_ARM_TILE.get(), pos, state);
        boolean extending = (Boolean) state.m_61143_(ModBlockProperties.EXTENDING);
        Direction dir = (Direction) state.m_61143_(BlockStateProperties.FACING);
        this.age = 0;
        if (extending) {
            this.increment = 0.5;
            this.offset = -1.0;
            this.prevOffset = -1.0;
        } else {
            this.increment = -0.5;
            this.offset = 0.0;
            this.prevOffset = 0.0;
        }
        Vec3i v = dir.getNormal();
        this.dx = v.getX();
        this.dy = v.getY();
        this.dz = v.getZ();
    }

    public int getAge() {
        return this.age;
    }

    public double getRenderOffset(float partialTicks) {
        return Mth.lerp((double) partialTicks, this.prevOffset, this.offset);
    }

    public AABB getAdjustedBoundingBox() {
        return new AABB(this.f_58858_).move((double) this.dx * this.offset, (double) this.dy * this.offset, (double) this.dz * this.offset);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SpringLauncherArmBlockTile tile) {
        boolean extending = (Boolean) state.m_61143_(SpringLauncherArmBlock.EXTENDING);
        if (level.isClientSide && extending) {
            double x = (double) pos.m_123341_() + 0.5 + (double) tile.dx * tile.offset;
            double y = (double) pos.m_123342_() + (double) tile.dy * tile.offset;
            double z = (double) pos.m_123343_() + 0.5 + (double) tile.dz * tile.offset;
            RandomSource random = level.random;
            for (int l = 0; l < 2; l++) {
                double d0 = x + (double) random.nextFloat() - 0.5;
                double d1 = y + (double) random.nextFloat() + 0.5;
                double d2 = z + (double) random.nextFloat() - 0.5;
                double d3 = ((double) random.nextFloat() - 0.5) * 0.05;
                double d4 = ((double) random.nextFloat() - 0.5) * 0.05;
                double d5 = ((double) random.nextFloat() - 0.5) * 0.05;
                level.addParticle(ParticleTypes.CLOUD, d0, d1, d2, d3, d4, d5);
            }
        }
        if (tile.age > 1) {
            tile.prevOffset = tile.offset;
            if (!level.isClientSide) {
                Direction dir = (Direction) state.m_61143_(SpringLauncherArmBlock.FACING);
                if (extending) {
                    BlockState state1 = ((Block) ModRegistry.SPRING_LAUNCHER_HEAD.get()).defaultBlockState();
                    level.setBlock(pos, (BlockState) state1.m_61124_(SpringLauncherHeadBlock.FACING, dir), 3);
                } else {
                    BlockState _bs = ((Block) ModRegistry.SPRING_LAUNCHER.get()).defaultBlockState();
                    BlockPos behindPos = pos.relative(tile.getDirection().getOpposite());
                    BlockState oldState = level.getBlockState(behindPos);
                    if (((BlockState) _bs.m_61124_(SpringLauncherBlock.FACING, dir)).m_61124_(SpringLauncherBlock.EXTENDED, true) == oldState) {
                        level.setBlock(behindPos, (BlockState) oldState.m_61124_(SpringLauncherBlock.EXTENDED, false), 3);
                    }
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                }
            }
        } else {
            tile.age++;
            tile.prevOffset = tile.offset;
            tile.offset = tile.offset + tile.increment;
            if (extending) {
                AABB p_bb = tile.getAdjustedBoundingBox();
                List<Entity> list1 = level.m_45933_(null, p_bb);
                if (!list1.isEmpty()) {
                    for (Entity entity : list1) {
                        if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
                            Vec3 vec3d = entity.getDeltaMovement();
                            double d1 = vec3d.x;
                            double d2 = vec3d.y;
                            double d3 = vec3d.z;
                            double speed = (Double) CommonConfigs.Redstone.LAUNCHER_VEL.get();
                            if (tile.dx != 0) {
                                d1 = (double) tile.dx * speed;
                            }
                            if (tile.dy != 0) {
                                d2 = (double) tile.dy * speed;
                            }
                            if (tile.dz != 0) {
                                d3 = (double) tile.dz * speed;
                            }
                            entity.setDeltaMovement(d1, d2, d3);
                            entity.hurtMarked = true;
                            tile.moveCollidedEntity(entity, p_bb);
                        }
                    }
                }
            }
        }
    }

    private void moveCollidedEntity(Entity entity, AABB aabb) {
        AABB boundingBox = entity.getBoundingBox();
        double dx = 0.0;
        double dy = 0.0;
        double dz = 0.0;
        switch(this.getDirection()) {
            case UP:
                dy = aabb.maxY - boundingBox.minY;
                break;
            case DOWN:
                dy = aabb.minY - boundingBox.maxY;
                break;
            case NORTH:
                dz = aabb.minZ - boundingBox.maxZ;
                break;
            case SOUTH:
                dz = aabb.maxZ - boundingBox.minZ;
                break;
            case WEST:
                dx = aabb.minX - boundingBox.maxX;
                break;
            case EAST:
                dx = aabb.maxX - boundingBox.minX;
                break;
            default:
                dy = 0.0;
        }
        entity.move(MoverType.PISTON, new Vec3(dx, dy, dz));
    }

    public Direction getDirection() {
        return (Direction) this.m_58900_().m_61143_(SpringLauncherArmBlock.FACING);
    }

    public boolean getExtending() {
        return (Boolean) this.m_58900_().m_61143_(SpringLauncherArmBlock.EXTENDING);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.age = compound.getInt("Age");
        this.offset = compound.getDouble("Offset");
        this.prevOffset = compound.getDouble("PrevOffset");
        this.increment = compound.getDouble("Increment");
        this.dx = compound.getInt("Dx");
        this.dy = compound.getInt("Dy");
        this.dz = compound.getInt("Dz");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("Age", this.age);
        compound.putDouble("Offset", this.offset);
        compound.putDouble("PrevOffset", this.prevOffset);
        compound.putDouble("Increment", this.increment);
        compound.putInt("Dx", this.dx);
        compound.putInt("Dy", this.dy);
        compound.putInt("Dz", this.dz);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public void retractOnFallOn() {
        this.age = 1;
        this.offset = -0.5;
    }
}