package net.mehvahdjukaar.supplementaries.common.block.tiles;

import java.util.EnumSet;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.client.util.ParticleUtil;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BellowsBlock;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.WetSpongeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BellowsBlockTile extends BlockEntity {

    private static final float MAX_COMPRESSION = 0.125F;

    private float height = 0.0F;

    private float prevHeight = 0.0F;

    private int manualPress = 0;

    private long startTime = 0L;

    private boolean isPressed = false;

    private boolean lastBlowing = false;

    public BellowsBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.BELLOWS_TILE.get(), pos, state);
    }

    public float getHeight(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevHeight, this.height);
    }

    public void setManualPress() {
        this.manualPress = 10;
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_);
    }

    private AABB getProgressDeltaAabb(Direction dir) {
        AABB bb = new AABB(BlockPos.ZERO);
        float max = Math.max(this.height, this.prevHeight);
        float min = Math.min(this.height, this.prevHeight);
        return (switch(dir) {
            case UP ->
                bb.setMaxY((double) (1.0F + max)).setMinY((double) (1.0F + min));
            case DOWN ->
                bb.setMaxY((double) max).setMinY((double) min);
            case NORTH ->
                bb.setMaxZ((double) max).setMinZ((double) min);
            case SOUTH ->
                bb.setMaxZ((double) (1.0F + max)).setMinZ((double) (1.0F + min));
            case EAST ->
                bb.setMaxX((double) (1.0F + max)).setMinX((double) (1.0F + min));
            case WEST ->
                bb.setMaxX((double) max).setMinX((double) min);
        }).move(this.f_58858_);
    }

    private void moveCollidedEntities(Level level) {
        Direction dir = this.getDirection().getAxis() == Direction.Axis.Y ? Direction.SOUTH : Direction.UP;
        for (int j = 0; j < 2; j++) {
            AABB progressDelta = this.getProgressDeltaAabb(dir);
            List<Entity> list = level.m_45933_(null, progressDelta);
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
                        double f = 0.0;
                        entity.move(MoverType.SHULKER_BOX, new Vec3((progressDelta.getXsize() + f) * (double) dir.getStepX(), (progressDelta.getYsize() + f) * (double) dir.getStepY(), (progressDelta.getZsize() + f) * (double) dir.getStepZ()));
                        entity.setOnGround(true);
                    }
                }
            }
            dir = dir.getOpposite();
        }
    }

    private void pushEntities(Direction facing, float period, float range, Level level) {
        double velocity = (Double) CommonConfigs.Redstone.BELLOWS_BASE_VEL_SCALING.get() / (double) period;
        double maxVelocity = (Double) CommonConfigs.Redstone.BELLOWS_MAX_VEL.get();
        AABB facingBox = MiscUtils.getDirectionBB(this.f_58858_, facing, (int) range);
        for (Entity entity : level.m_45976_(Entity.class, facingBox)) {
            if (this.inLineOfSight(entity, facing, level)) {
                if (facing == Direction.UP) {
                    maxVelocity *= 0.5;
                }
                AABB entityBB = entity.getBoundingBox();
                double dist;
                switch(facing) {
                    case UP:
                        double b = (double) this.f_58858_.m_123342_() + 1.0;
                        if (entityBB.minY < b) {
                            continue;
                        }
                        dist = entity.getY() - b;
                        break;
                    case DOWN:
                        double b = (double) this.f_58858_.m_123342_();
                        if (entityBB.maxY > b) {
                            continue;
                        }
                        dist = b - entity.getY();
                        break;
                    case NORTH:
                        double b = (double) this.f_58858_.m_123343_();
                        if (entityBB.maxZ > b) {
                            continue;
                        }
                        dist = b - entity.getZ();
                        break;
                    case SOUTH:
                    default:
                        double b = (double) this.f_58858_.m_123343_() + 1.0;
                        if (entityBB.minZ < b) {
                            continue;
                        }
                        dist = entity.getZ() - b;
                        break;
                    case EAST:
                        double b = (double) this.f_58858_.m_123341_() + 1.0;
                        if (entityBB.minX < b) {
                            continue;
                        }
                        dist = entity.getX() - b;
                        break;
                    case WEST:
                        double b = (double) this.f_58858_.m_123341_();
                        if (entityBB.maxX > b) {
                            continue;
                        }
                        dist = b - entity.getX();
                }
                velocity *= ((double) range - dist) / (double) range;
                if (Math.abs(entity.getDeltaMovement().get(facing.getAxis())) < maxVelocity) {
                    entity.push((double) facing.getStepX() * velocity, (double) facing.getStepY() * velocity, (double) facing.getStepZ() * velocity);
                    entity.hurtMarked = true;
                }
            }
        }
    }

    private void blowParticles(float air, Direction facing, Level level, boolean waterInFront) {
        if (level.random.nextFloat() < air) {
            BellowsBlockTile.AirType type = BellowsBlockTile.AirType.BUBBLE;
            BlockPos facingPos = this.f_58858_.relative(facing);
            BlockPos frontPos = facingPos;
            boolean hasSponge = false;
            if (!waterInFront) {
                BlockState frontState = level.getBlockState(facingPos);
                if (frontState.m_60734_() instanceof WetSpongeBlock) {
                    hasSponge = true;
                    frontPos = facingPos.relative(facing);
                }
                type = BellowsBlockTile.AirType.AIR;
            }
            if (!Block.canSupportCenter(level, frontPos, facing.getOpposite())) {
                BlockPos p = this.f_58858_;
                if (hasSponge) {
                    EnumSet<Direction> directions = EnumSet.allOf(Direction.class);
                    directions.remove(facing.getOpposite());
                    directions.remove(facing);
                    for (Direction d : directions) {
                        if (level.getBlockState(facingPos.relative(d)).m_60713_((Block) ModRegistry.SOAP_BLOCK.get())) {
                            type = BellowsBlockTile.AirType.SOAP;
                            p = facingPos;
                            break;
                        }
                    }
                    if (type != BellowsBlockTile.AirType.SOAP) {
                        return;
                    }
                }
                this.spawnParticle(level, p, facing, type);
            }
        }
    }

    private <T extends BlockEntity> void tickFurnaces(BlockPos frontPos, BlockState frontState, Level level, T tile) {
        if (tile != null) {
            BlockEntityTicker<T> ticker = frontState.m_155944_(level, tile.getType());
            if (ticker != null) {
                ticker.tick(level, frontPos, frontState, tile);
            }
        }
    }

    private void tickFurnaces(BlockPos pos, Level level) {
        BlockState state = level.getBlockState(pos);
        if (state.m_204336_(ModTags.BELLOWS_TICKABLE_TAG)) {
            BlockEntity te = level.getBlockEntity(pos);
            this.tickFurnaces(pos, state, level, te);
        } else if (state.m_60734_() instanceof ChangeOverTimeBlock && level instanceof ServerLevel serverLevel) {
            state.m_222972_(serverLevel, pos, level.random);
        }
    }

    private void refreshFire(int n, Direction facing, BlockPos frontPos, Level level) {
        for (int i = 0; i < n; i++) {
            BlockState fb = level.getBlockState(frontPos);
            if (fb.m_60734_() instanceof FireBlock) {
                int age = (Integer) fb.m_61143_(FireBlock.AGE);
                if (age != 0) {
                    level.setBlock(frontPos, (BlockState) fb.m_61124_(FireBlock.AGE, Mth.clamp(age - 7, 0, 15)), 4);
                }
            }
            frontPos = frontPos.relative(facing);
        }
    }

    private float getPeriodForPower(int power) {
        return (float) ((Integer) CommonConfigs.Redstone.BELLOWS_PERIOD.get()).intValue() - (float) (power - 1) * (float) ((Integer) CommonConfigs.Redstone.BELLOWS_POWER_SCALING.get()).intValue();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BellowsBlockTile tile) {
        int power = (Integer) state.m_61143_(BellowsBlock.POWER);
        tile.prevHeight = tile.height;
        if (power != 0 && (tile.startTime != 0L || tile.height == 0.0F)) {
            long time = level.getGameTime();
            if (tile.startTime == 0L) {
                tile.startTime = time;
            }
            float period = tile.getPeriodForPower(power);
            float arg = (float) (Math.PI * 2) * ((float) (time - tile.startTime) / period % 1.0F);
            float sin = Mth.sin(arg);
            float cos = Mth.cos(arg);
            float half = 0.0625F;
            tile.height = half * cos - half;
            tile.pushAir(level, pos, state, power, time, period, sin);
            boolean blowing = Mth.sin(arg - 0.8F) > 0.0F;
            if (tile.lastBlowing != blowing) {
                level.playSound(null, pos, blowing ? (SoundEvent) ModSounds.BELLOWS_BLOW.get() : (SoundEvent) ModSounds.BELLOWS_RETRACT.get(), SoundSource.BLOCKS, 0.1F, MthUtils.nextWeighted(level.random, 0.1F) + 0.85F + 0.6F * (float) power / 15.0F);
            }
            tile.lastBlowing = blowing;
        } else if (tile.isPressed) {
            float minH = -0.125F;
            tile.height = Math.max(tile.height - 0.01F, minH);
            if (tile.height > minH) {
                long timex = level.getGameTime();
                int p = 7;
                float period = tile.getPeriodForPower(p);
                tile.pushAir(level, pos, state, p, timex, period, 0.8F);
            }
        } else {
            tile.startTime = 0L;
            if (tile.height < 0.0F) {
                tile.height = Math.min(tile.height + 0.01F, 0.0F);
            }
        }
        if (tile.prevHeight != 0.0F && tile.height != 0.0F) {
            tile.moveCollidedEntities(level);
        }
        if (tile.manualPress > 0) {
            tile.manualPress--;
            tile.isPressed = true;
        } else {
            tile.isPressed = false;
        }
    }

    private void pushAir(Level level, BlockPos pos, BlockState state, int power, long time, float period, float airIntensity) {
        Direction facing = (Direction) state.m_61143_(BellowsBlock.FACING);
        BlockPos frontPos = pos.relative(facing);
        FluidState fluid = level.getFluidState(frontPos);
        if (level.isClientSide) {
            this.blowParticles(airIntensity, facing, level, fluid.getType().is(FluidTags.WATER));
        } else if (fluid.isEmpty()) {
            float range = (float) ((Integer) CommonConfigs.Redstone.BELLOWS_RANGE.get()).intValue();
            if (airIntensity > 0.0F) {
                this.pushEntities(facing, period, range, level);
            }
            if (time % (long) (10 - power / 2) == 0L) {
                this.tickFurnaces(frontPos, level);
            }
            int n = 0;
            for (int a = 0; (float) a <= range; a++) {
                if (time % (15L * (long) (a + 1)) != 0L) {
                    n = a;
                    break;
                }
            }
            this.refreshFire(n, facing, frontPos, level);
        }
    }

    public boolean inLineOfSight(Entity entity, Direction facing, Level level) {
        int x = facing.getStepX() * (Mth.floor(entity.getX()) - this.f_58858_.m_123341_());
        int y = facing.getStepY() * (Mth.floor(entity.getY()) - this.f_58858_.m_123342_());
        int z = facing.getStepZ() * (Mth.floor(entity.getZ()) - this.f_58858_.m_123343_());
        boolean flag = true;
        for (int i = 1; i < Math.abs(x + y + z); i++) {
            if (Block.canSupportCenter(level, this.f_58858_.relative(facing, i), facing.getOpposite())) {
                flag = false;
            }
        }
        return flag;
    }

    public void spawnParticle(Level world, BlockPos pos, Direction dir, BellowsBlockTile.AirType airType) {
        if (airType == BellowsBlockTile.AirType.SOAP) {
            for (int m = 0; m < 1 + world.random.nextInt(3); m++) {
                ParticleUtil.spawnParticleOnFace(world, pos, dir, (ParticleOptions) ModParticles.SUDS_PARTICLE.get(), 0.3F, 0.5F, true);
            }
        } else {
            double xo = (double) dir.getStepX();
            double yo = (double) dir.getStepY();
            double zo = (double) dir.getStepZ();
            double x = xo * 0.5 + (double) pos.m_123341_() + 0.5 + ((double) world.random.nextFloat() - 0.5) / 3.0;
            double y = yo * 0.5 + (double) pos.m_123342_() + 0.5 + ((double) world.random.nextFloat() - 0.5) / 3.0;
            double z = zo * 0.5 + (double) pos.m_123343_() + 0.5 + ((double) world.random.nextFloat() - 0.5) / 3.0;
            double vel = (double) (0.125F + world.random.nextFloat() * 0.2F);
            double velX = xo * vel;
            double velY = yo * vel;
            double velZ = zo * vel;
            if (airType == BellowsBlockTile.AirType.BUBBLE) {
                world.addParticle(ParticleTypes.BUBBLE, x, y, z, velX * 0.8, velY * 0.8, velZ * 0.8);
            } else {
                world.addParticle(ParticleTypes.SMOKE, x, y, z, velX, velY, velZ);
            }
        }
    }

    public Direction getDirection() {
        return (Direction) this.m_58900_().m_61143_(BellowsBlock.FACING);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.startTime = compound.getLong("Offset");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putLong("Offset", this.startTime);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public void onSteppedOn(Entity entityIn) {
        if (!this.isPressed) {
            double b = entityIn.getBoundingBox().getSize();
            if (b > 0.8 && ((Direction) this.m_58900_().m_61143_(BellowsBlock.FACING)).getAxis() != Direction.Axis.Y) {
                this.isPressed = true;
            }
        }
    }

    private static enum AirType {

        AIR, BUBBLE, SOAP
    }
}