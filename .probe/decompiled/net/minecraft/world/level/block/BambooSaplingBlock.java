package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BambooSaplingBlock extends Block implements BonemealableBlock {

    protected static final float SAPLING_AABB_OFFSET = 4.0F;

    protected static final VoxelShape SAPLING_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 12.0, 12.0);

    public BambooSaplingBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        Vec3 $$4 = blockState0.m_60824_(blockGetter1, blockPos2);
        return SAPLING_SHAPE.move($$4.x, $$4.y, $$4.z);
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (randomSource3.nextInt(3) == 0 && serverLevel1.m_46859_(blockPos2.above()) && serverLevel1.m_45524_(blockPos2.above(), 0) >= 9) {
            this.growBamboo(serverLevel1, blockPos2);
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return levelReader1.m_8055_(blockPos2.below()).m_204336_(BlockTags.BAMBOO_PLANTABLE_ON);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (!blockState0.m_60710_(levelAccessor3, blockPos4)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            if (direction1 == Direction.UP && blockState2.m_60713_(Blocks.BAMBOO)) {
                levelAccessor3.m_7731_(blockPos4, Blocks.BAMBOO.defaultBlockState(), 2);
            }
            return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return new ItemStack(Items.BAMBOO);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        return levelReader0.m_8055_(blockPos1.above()).m_60795_();
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        this.growBamboo(serverLevel0, blockPos2);
    }

    @Override
    public float getDestroyProgress(BlockState blockState0, Player player1, BlockGetter blockGetter2, BlockPos blockPos3) {
        return player1.m_21205_().getItem() instanceof SwordItem ? 1.0F : super.m_5880_(blockState0, player1, blockGetter2, blockPos3);
    }

    protected void growBamboo(Level level0, BlockPos blockPos1) {
        level0.setBlock(blockPos1.above(), (BlockState) Blocks.BAMBOO.defaultBlockState().m_61124_(BambooStalkBlock.LEAVES, BambooLeaves.SMALL), 3);
    }
}