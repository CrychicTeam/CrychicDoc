package net.minecraft.world.level.block;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FlowerPotBlock extends Block {

    private static final Map<Block, Block> POTTED_BY_CONTENT = Maps.newHashMap();

    public static final float AABB_SIZE = 3.0F;

    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);

    private final Block content;

    public FlowerPotBlock(Block block0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.content = block0;
        POTTED_BY_CONTENT.put(block0, this);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        ItemStack $$6 = player3.m_21120_(interactionHand4);
        Item $$7 = $$6.getItem();
        BlockState $$8 = ($$7 instanceof BlockItem ? (Block) POTTED_BY_CONTENT.getOrDefault(((BlockItem) $$7).getBlock(), Blocks.AIR) : Blocks.AIR).defaultBlockState();
        boolean $$9 = $$8.m_60713_(Blocks.AIR);
        boolean $$10 = this.isEmpty();
        if ($$9 != $$10) {
            if ($$10) {
                level1.setBlock(blockPos2, $$8, 3);
                player3.awardStat(Stats.POT_FLOWER);
                if (!player3.getAbilities().instabuild) {
                    $$6.shrink(1);
                }
            } else {
                ItemStack $$11 = new ItemStack(this.content);
                if ($$6.isEmpty()) {
                    player3.m_21008_(interactionHand4, $$11);
                } else if (!player3.addItem($$11)) {
                    player3.drop($$11, false);
                }
                level1.setBlock(blockPos2, Blocks.FLOWER_POT.defaultBlockState(), 3);
            }
            level1.m_142346_(player3, GameEvent.BLOCK_CHANGE, blockPos2);
            return InteractionResult.sidedSuccess(level1.isClientSide);
        } else {
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return this.isEmpty() ? super.getCloneItemStack(blockGetter0, blockPos1, blockState2) : new ItemStack(this.content);
    }

    private boolean isEmpty() {
        return this.content == Blocks.AIR;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return direction1 == Direction.DOWN && !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    public Block getContent() {
        return this.content;
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}