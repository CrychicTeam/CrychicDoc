package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import java.util.ArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

public class BlockSkunkSpray extends MultifaceBlock implements SimpleWaterloggedBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BlockSkunkSpray() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).noOcclusion().randomTicks().noCollission().instabreak().sound(SoundType.FROGSPAWN));
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false)).m_61124_(AGE, 0));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor levelAccessor, BlockPos pos, BlockPos pos2) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            levelAccessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        }
        return super.updateShape(state, direction, state2, levelAccessor, pos, pos2);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        this.tick(state, level, pos, randomSource);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(8) == 0) {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (Direction direction : Direction.values()) {
                blockpos$mutableblockpos.setWithOffset(pos, direction);
                BlockState blockstate = level.m_8055_(blockpos$mutableblockpos);
                if (blockstate.m_60713_(this) && !this.incrementAge(blockstate, level, blockpos$mutableblockpos)) {
                    level.m_186460_(blockpos$mutableblockpos, this, Mth.nextInt(random, 50, 100));
                }
            }
            this.incrementAge(state, level, pos);
        } else {
            level.m_186460_(pos, this, Mth.nextInt(random, 50, 100));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> definition) {
        super.createBlockStateDefinition(definition);
        definition.add(WATERLOGGED, AGE);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack itemStack = player.m_21120_(handIn);
        int setContent = -1;
        if (itemStack.is(Items.GLASS_BOTTLE)) {
            Direction dir = hit.getDirection().getOpposite();
            if (m_153900_(state, dir)) {
                worldIn.setBlockAndUpdate(pos, removeStinkFace(state, dir));
                ItemStack bottle = new ItemStack(AMItemRegistry.STINK_BOTTLE.get());
                if (!player.addItem(bottle)) {
                    player.drop(bottle, false);
                }
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.m_6227_(state, worldIn, pos, player, handIn, hit);
    }

    public static BlockState removeStinkFace(BlockState state, Direction faceProperty) {
        BlockState blockstate = (BlockState) state.m_61124_(m_153933_(faceProperty), false);
        return m_153960_(blockstate) ? blockstate : Blocks.AIR.defaultBlockState();
    }

    private boolean incrementAge(BlockState state, Level level, BlockPos pos) {
        int i = (Integer) state.m_61143_(AGE);
        if (i < 3) {
            level.setBlock(pos, (BlockState) state.m_61124_(AGE, i + 1), 2);
            return false;
        } else {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            return true;
        }
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return !context.m_43722_().is(AMBlockRegistry.SKUNK_SPRAY.get().asItem()) || super.canBeReplaced(state, context);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return null;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return state.m_60819_().isEmpty();
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos pos, RandomSource randomSource) {
        if (randomSource.nextInt(2) == 0) {
            ArrayList<Direction> faces = new ArrayList(m_221584_(blockState));
            Direction direction = null;
            if (faces.size() == 1) {
                direction = (Direction) faces.get(0);
            } else if (faces.size() > 1) {
                direction = Util.getRandom(faces, randomSource);
            }
            if (direction != null) {
                double d0 = direction.getStepX() == 0 ? randomSource.nextDouble() : 0.5 + (double) direction.getStepX() * 0.8;
                double d1 = direction.getStepY() == 0 ? randomSource.nextDouble() : 0.5 + (double) direction.getStepY() * 0.8;
                double d2 = direction.getStepZ() == 0 ? randomSource.nextDouble() : 0.5 + (double) direction.getStepZ() * 0.8;
                level.addParticle(AMParticleRegistry.SMELLY.get(), (double) pos.m_123341_() + d0, (double) pos.m_123342_() + d1, (double) pos.m_123343_() + d2, 0.0, 0.0, 0.0);
            }
        }
    }
}