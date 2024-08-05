package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

class FlytrapBlock extends BushBlock implements BonemealableBlock {

    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    public static final VoxelShape SHAPE = Block.box(3.5, 0.0, 3.5, 12.5, 21.0, 12.5);

    public FlytrapBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).instabreak().sound(SoundType.ROOTS).randomTicks().offsetType(BlockBehaviour.OffsetType.XZ).noOcclusion().noCollission());
        this.m_49959_((BlockState) this.m_49966_().m_61124_(OPEN, true));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.m_60824_(getter, pos);
        return SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        if ((Boolean) state.m_61143_(OPEN)) {
            level.m_7731_(pos, (BlockState) state.m_61124_(OPEN, false), 2);
            level.m_186460_(pos, this, 100 + randomSource.nextInt(100));
        } else {
            level.m_7731_(pos, (BlockState) state.m_61124_(OPEN, true), 2);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        level.m_7731_(pos, (BlockState) state.m_61124_(OPEN, true), 2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPEN);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        if ((Boolean) state.m_61143_(OPEN) && randomSource.nextInt(3) == 0) {
            Vec3 center = Vec3.upFromBottomCenterOf(pos, 1.0).add(state.m_60824_(level, pos));
            level.addParticle(ACParticleRegistry.FLY.get(), center.x, center.y, center.z, center.x, center.y, center.z);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos blockPos, BlockState blockState, boolean idk) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource randomSource, BlockPos blockPos, BlockState state) {
        m_49840_(level, blockPos, new ItemStack(this));
    }
}