package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SnifferEggBlock extends Block {

    public static final int MAX_HATCH_LEVEL = 2;

    public static final IntegerProperty HATCH = BlockStateProperties.HATCH;

    private static final int REGULAR_HATCH_TIME_TICKS = 24000;

    private static final int BOOSTED_HATCH_TIME_TICKS = 12000;

    private static final int RANDOM_HATCH_OFFSET_TICKS = 300;

    private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 2.0, 15.0, 16.0, 14.0);

    public SnifferEggBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(HATCH, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(HATCH);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    public int getHatchLevel(BlockState blockState0) {
        return (Integer) blockState0.m_61143_(HATCH);
    }

    private boolean isReadyToHatch(BlockState blockState0) {
        return this.getHatchLevel(blockState0) == 2;
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (!this.isReadyToHatch(blockState0)) {
            serverLevel1.m_5594_(null, blockPos2, SoundEvents.SNIFFER_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + randomSource3.nextFloat() * 0.2F);
            serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(HATCH, this.getHatchLevel(blockState0) + 1), 2);
        } else {
            serverLevel1.m_5594_(null, blockPos2, SoundEvents.SNIFFER_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + randomSource3.nextFloat() * 0.2F);
            serverLevel1.m_46961_(blockPos2, false);
            Sniffer $$4 = EntityType.SNIFFER.create(serverLevel1);
            if ($$4 != null) {
                Vec3 $$5 = blockPos2.getCenter();
                $$4.setBaby(true);
                $$4.m_7678_($$5.x(), $$5.y(), $$5.z(), Mth.wrapDegrees(serverLevel1.f_46441_.nextFloat() * 360.0F), 0.0F);
                serverLevel1.addFreshEntity($$4);
            }
        }
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        boolean $$5 = hatchBoost(level1, blockPos2);
        if (!level1.isClientSide() && $$5) {
            level1.m_46796_(3009, blockPos2, 0);
        }
        int $$6 = $$5 ? 12000 : 24000;
        int $$7 = $$6 / 3;
        level1.m_220407_(GameEvent.BLOCK_PLACE, blockPos2, GameEvent.Context.of(blockState0));
        level1.m_186460_(blockPos2, this, $$7 + level1.random.nextInt(300));
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }

    public static boolean hatchBoost(BlockGetter blockGetter0, BlockPos blockPos1) {
        return blockGetter0.getBlockState(blockPos1.below()).m_204336_(BlockTags.SNIFFER_EGG_HATCH_BOOST);
    }
}