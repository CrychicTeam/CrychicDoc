package net.mehvahdjukaar.supplementaries.common.block.blocks;

import com.google.common.base.Suppliers;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SconceBlock extends LightUpWaterBlock {

    protected static final VoxelShape SHAPE = m_49796_(6.0, 0.0, 6.0, 10.0, 11.0, 10.0);

    protected final Supplier<SimpleParticleType> particleData;

    public <T extends ParticleType<?>> SconceBlock(BlockBehaviour.Properties properties, Supplier<T> particleData) {
        super(properties);
        this.particleData = Suppliers.memoize(() -> {
            SimpleParticleType data = (SimpleParticleType) particleData.get();
            if (data == null) {
                data = ParticleTypes.FLAME;
            }
            return data;
        });
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false)).m_61124_(LIT, true));
    }

    public <T extends ParticleType<?>> SconceBlock(BlockBehaviour.Properties properties, int lightLevel, Supplier<T> particleData) {
        this(properties.lightLevel(state -> state.m_61143_(BlockStateProperties.LIT) ? lightLevel : 0), particleData);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        return facing == Direction.DOWN && !this.canSurvive(stateIn, worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return m_49863_(worldIn, pos.below(), Direction.UP);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Deprecated
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext ec && ec.getEntity() instanceof Projectile) {
            return state.m_60808_(level, pos);
        }
        return Shapes.empty();
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        if ((Boolean) stateIn.m_61143_(LIT)) {
            double d0 = (double) pos.m_123341_() + 0.5;
            double d1 = (double) pos.m_123342_() + 0.75;
            double d2 = (double) pos.m_123343_() + 0.5;
            worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
            worldIn.addParticle((ParticleOptions) this.particleData.get(), d0, d1, d2, 0.0, 0.0, 0.0);
        }
    }
}