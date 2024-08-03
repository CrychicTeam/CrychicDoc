package net.minecraft.world.level.block;

import java.util.List;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DecoratedPotBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final ResourceLocation SHERDS_DYNAMIC_DROP_ID = new ResourceLocation("sherds");

    private static final VoxelShape BOUNDING_BOX = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    private static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final BooleanProperty CRACKED = BlockStateProperties.CRACKED;

    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected DecoratedPotBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(HORIZONTAL_FACING, Direction.NORTH)).m_61124_(WATERLOGGED, false)).m_61124_(CRACKED, false));
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        FluidState $$1 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
        return (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(HORIZONTAL_FACING, blockPlaceContext0.m_8125_())).m_61124_(WATERLOGGED, $$1.getType() == Fluids.WATER)).m_61124_(CRACKED, false);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return BOUNDING_BOX;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(HORIZONTAL_FACING, WATERLOGGED, CRACKED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new DecoratedPotBlockEntity(blockPos0, blockState1);
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState0, LootParams.Builder lootParamsBuilder1) {
        BlockEntity $$2 = lootParamsBuilder1.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if ($$2 instanceof DecoratedPotBlockEntity $$3) {
            lootParamsBuilder1.withDynamicDrop(SHERDS_DYNAMIC_DROP_ID, p_284876_ -> $$3.getDecorations().sorted().map(Item::m_7968_).forEach(p_284876_));
        }
        return super.m_49635_(blockState0, lootParamsBuilder1);
    }

    @Override
    public void playerWillDestroy(Level level0, BlockPos blockPos1, BlockState blockState2, Player player3) {
        ItemStack $$4 = player3.m_21205_();
        BlockState $$5 = blockState2;
        if ($$4.is(ItemTags.BREAKS_DECORATED_POTS) && !EnchantmentHelper.hasSilkTouch($$4)) {
            $$5 = (BlockState) blockState2.m_61124_(CRACKED, true);
            level0.setBlock(blockPos1, $$5, 4);
        }
        super.m_5707_(level0, blockPos1, $$5, player3);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public SoundType getSoundType(BlockState blockState0) {
        return blockState0.m_61143_(CRACKED) ? SoundType.DECORATED_POT_CRACKED : SoundType.DECORATED_POT;
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable BlockGetter blockGetter1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        super.m_5871_(itemStack0, blockGetter1, listComponent2, tooltipFlag3);
        DecoratedPotBlockEntity.Decorations $$4 = DecoratedPotBlockEntity.Decorations.load(BlockItem.getBlockEntityData(itemStack0));
        if (!$$4.equals(DecoratedPotBlockEntity.Decorations.EMPTY)) {
            listComponent2.add(CommonComponents.EMPTY);
            Stream.of($$4.front(), $$4.left(), $$4.right(), $$4.back()).forEach(p_284873_ -> listComponent2.add(new ItemStack(p_284873_, 1).getHoverName().plainCopy().withStyle(ChatFormatting.GRAY)));
        }
    }
}