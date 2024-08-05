package net.mehvahdjukaar.amendments.common.block;

import java.util.Arrays;
import java.util.List;
import net.mehvahdjukaar.amendments.Amendments;
import net.mehvahdjukaar.amendments.common.FlowerPotHandler;
import net.mehvahdjukaar.amendments.common.tile.HangingFlowerPotBlockTile;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.integration.SuppCompat;
import net.mehvahdjukaar.amendments.reg.ModBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class HangingFlowerPotBlock extends Block implements EntityBlock {

    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);

    public static final IntegerProperty LIGHT_LEVEL = ModBlockProperties.LIGHT_LEVEL;

    public HangingFlowerPotBlock(BlockBehaviour.Properties properties) {
        super(properties.lightLevel(state -> (Integer) state.m_61143_(LIGHT_LEVEL)));
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LIGHT_LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIGHT_LEVEL);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        Item i = stack.getItem();
        if (world.getBlockEntity(pos) instanceof HangingFlowerPotBlockTile tile) {
            if (i instanceof BlockItem blockItem) {
                BlockState mimic = blockItem.getBlock().defaultBlockState();
                tile.setHeldBlock(mimic);
            }
            if (CompatHandler.SUPPLEMENTARIES) {
                SuppCompat.addOptionalOwnership(tile.m_58904_(), tile.m_58899_(), entity);
            }
        }
    }

    @Override
    public MutableComponent getName() {
        return Component.translatable("block.minecraft.flower_pot");
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return context.m_43719_() == Direction.DOWN ? super.getStateForPlacement(context) : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof HangingFlowerPotBlockTile tile && tile.isAccessibleBy(player)) {
            Block pot = tile.getHeldBlock().m_60734_();
            if (pot instanceof FlowerPotBlock flowerPot) {
                ItemStack itemstack = player.m_21120_(handIn);
                Block newPot = itemstack.getItem() instanceof BlockItem bi ? FlowerPotHandler.getFullPot(flowerPot, bi.getBlock()) : Blocks.AIR;
                boolean isEmptyFlower = newPot == Blocks.AIR;
                boolean isPotEmpty = FlowerPotHandler.isEmptyPot(pot);
                if (isEmptyFlower != isPotEmpty) {
                    if (isPotEmpty) {
                        if (!level.isClientSide) {
                            tile.setHeldBlock(newPot.defaultBlockState());
                            level.sendBlockUpdated(pos, state, state, 2);
                            tile.m_6596_();
                        }
                        playPlantSound(level, pos, player);
                        player.awardStat(Stats.POT_FLOWER);
                        if (!player.getAbilities().instabuild) {
                            itemstack.shrink(1);
                        }
                    } else {
                        ItemStack flowerItem = pot.getCloneItemStack(level, pos, state);
                        if (!flowerItem.equals(new ItemStack(this))) {
                            if (itemstack.isEmpty()) {
                                player.m_21008_(handIn, flowerItem);
                            } else if (!player.addItem(flowerItem)) {
                                player.drop(flowerItem, false);
                            }
                        }
                        if (!level.isClientSide) {
                            tile.setHeldBlock(FlowerPotHandler.getEmptyPot(flowerPot).defaultBlockState());
                            level.sendBlockUpdated(pos, state, state, 2);
                            tile.m_6596_();
                        }
                    }
                    level.m_142346_(player, GameEvent.BLOCK_CHANGE, pos);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    public static void playPlantSound(Level level, BlockPos pos, Player player) {
        level.playSound(player, pos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.1F + 0.95F);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new HangingFlowerPotBlockTile(pPos, pState);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof HangingFlowerPotBlockTile te && te.getHeldBlock().m_60734_() instanceof FlowerPotBlock b) {
            Block flower = b.getContent();
            if (flower == Blocks.AIR) {
                return new ItemStack(Blocks.FLOWER_POT, 1);
            }
            return new ItemStack(flower);
        }
        return new ItemStack(Blocks.FLOWER_POT, 1);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof HangingFlowerPotBlockTile tile && tile.getHeldBlock().m_60734_() instanceof FlowerPotBlock flowerPotBlock) {
            return Arrays.asList(new ItemStack(flowerPotBlock.getContent()), new ItemStack(Items.FLOWER_POT));
        }
        return super.m_49635_(state, builder);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.UP && !this.canSurvive(stateIn, worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return Amendments.isSupportingCeiling(pos.relative(Direction.UP), worldIn);
    }
}