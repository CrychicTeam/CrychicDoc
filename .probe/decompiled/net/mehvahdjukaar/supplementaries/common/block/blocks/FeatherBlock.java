package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.TreeMap;
import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FeatherBlock extends Block {

    protected static final VoxelShape COLLISION_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 11.0, 16.0);

    private static final TreeMap<Float, VoxelShape> COLLISIONS = new TreeMap();

    private final VoxelShape COLLISION_CHECK_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.1, 16.0);

    public FeatherBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float height) {
        if (!world.isClientSide) {
            if (height > 2.0F) {
                world.playSound(null, pos, SoundEvents.WOOL_FALL, SoundSource.BLOCKS, 1.0F, 0.9F);
            }
        } else {
            for (int i = 0; (double) i < Math.min(6.0, (double) height * 0.8); i++) {
                RandomSource random = world.getRandom();
                double dy = Mth.clamp(0.03 * (double) height / 7.0, 0.03, 0.055);
                world.addParticle((ParticleOptions) ModParticles.FEATHER_PARTICLE.get(), entity.getX() + this.r(random, 0.35), entity.getY(), entity.getZ() + this.r(random, 0.35), this.r(random, 0.007), dy * 0.5, this.r(random, 0.007));
            }
        }
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter reader, Entity entity) {
        entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, 0.4, 1.0));
    }

    private boolean isColliding(Entity e, BlockPos pos) {
        if (e == null) {
            return false;
        } else {
            VoxelShape voxelshape = this.COLLISION_CHECK_SHAPE.move((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_());
            return Shapes.joinIsNotEmpty(voxelshape, Shapes.create(e.getBoundingBox()), BooleanOp.AND);
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos blockPos, Entity entity) {
        if (level.isClientSide && (!(entity instanceof LivingEntity) || entity.getFeetBlockState().m_60713_(this))) {
            RandomSource random = level.getRandom();
            boolean isMoving = entity.xOld != entity.getX() || entity.zOld != entity.getZ();
            if (isMoving && random.nextInt(10) == 0) {
                double dy = 0.005;
                level.addParticle((ParticleOptions) ModParticles.FEATHER_PARTICLE.get(), entity.getX() + this.r(random, 0.15), entity.getY(), entity.getZ() + this.r(random, 0.15), 0.0, dy, 0.0);
            }
        }
    }

    private double r(RandomSource random, double a) {
        return a * (double) (random.nextFloat() + random.nextFloat() - 1.0F);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (collisionContext instanceof EntityCollisionContext entityCollisionContext && entityCollisionContext.getEntity() instanceof LivingEntity entity) {
            float dy = (float) (entity.m_20186_() - (double) blockPos.m_123342_());
            if (dy > 0.0F) {
                Float key = (Float) COLLISIONS.lowerKey(dy);
                if (key != null) {
                    return (VoxelShape) COLLISIONS.getOrDefault(key, COLLISION_SHAPE);
                }
            }
        }
        return Shapes.block();
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
        SoundType soundtype = state.m_60827_();
        level.playSound(null, pos, soundtype.getBreakSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        if (level.isClientSide) {
            RandomSource r = level.random;
            for (int i = 0; i < 10; i++) {
                level.addParticle((ParticleOptions) ModParticles.FEATHER_PARTICLE.get(), (double) ((float) pos.m_123341_() + r.nextFloat()), (double) ((float) pos.m_123342_() + r.nextFloat()), (double) ((float) pos.m_123343_() + r.nextFloat()), (0.5 - (double) r.nextFloat()) * 0.02, (0.5 - (double) r.nextFloat()) * 0.02, (0.5 - (double) r.nextFloat()) * 0.02);
            }
        }
    }

    static {
        float y = (float) COLLISION_SHAPE.max(Direction.Axis.Y);
        float i = 0.0015F;
        COLLISIONS.put(y - i, Shapes.box(0.0, 0.0, 0.0, 1.0, (double) y, 1.0));
        while (y < 1.0F) {
            COLLISIONS.put(y, Shapes.box(0.0, 0.0, 0.0, 1.0, (double) y, 1.0));
            i = (float) ((double) i * 1.131);
            y += i;
        }
        COLLISIONS.put(1.0F, Shapes.block());
        COLLISIONS.put(0.0F, Shapes.block());
    }
}