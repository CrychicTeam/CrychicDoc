package com.mna.blocks.manaweaving;

import com.mna.api.blocks.interfaces.ICutoutBlock;
import com.mna.blocks.WaterloggableBlock;
import com.mna.blocks.tileentities.ManaweaveProjectorTile;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.items.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ManaweaveProjectorBlock extends WaterloggableBlock implements ICutoutBlock, EntityBlock {

    public static final IntegerProperty FILL_LEVEL = IntegerProperty.create("fill_level", 0, 4);

    public static final BooleanProperty PROJECTING = BooleanProperty.create("projecting");

    public static final int EMPTY = 0;

    public static final int QUARTER = 1;

    public static final int HALF = 2;

    public static final int THREE_QUARTERS = 3;

    public static final int FULL = 4;

    private static final VoxelShape FOOT = Block.box(4.0, 0.0, 4.0, 12.0, 2.0, 12.0);

    private static final VoxelShape PILLAR = Block.box(6.0, 2.0, 6.0, 10.0, 16.0, 10.0);

    private static final VoxelShape COMBINED_SHAPE = Shapes.or(FOOT, PILLAR);

    public ManaweaveProjectorBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F).noOcclusion(), false);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(FILL_LEVEL, 0)).m_61124_(PROJECTING, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return COMBINED_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FILL_LEVEL, PROJECTING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ManaweaveProjectorTile(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.MANAWEAVE_PROJECTOR.get() ? (lvl, pos, state1, be) -> ManaweaveProjectorTile.Tick(lvl, pos, state1, (ManaweaveProjectorTile) be) : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult p_225533_6_) {
        if (!worldIn.isClientSide) {
            ItemStack stack = player.m_21120_(handIn);
            BlockEntity te = worldIn.getBlockEntity(pos);
            if (te != null && te instanceof ManaweaveProjectorTile) {
                if (stack.getItem() == ItemInit.RECIPE_SCRAP_MANAWEAVING_PATTERN.get()) {
                    ResourceLocation rLoc = ItemInit.RECIPE_SCRAP_MANAWEAVING_PATTERN.get().getRecipe(stack, worldIn);
                    ((ManaweaveProjectorTile) te).setPattern(rLoc, player);
                    worldIn.sendBlockUpdated(pos, state, state, 2);
                } else {
                    ((ManaweaveProjectorTile) te).trySpawnManaweaveEntity(player);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        if (te != null && te instanceof ManaweaveProjectorTile) {
            float pct = ((ManaweaveProjectorTile) te).getMana() / 100.0F;
            return (int) (pct * 15.0F);
        } else {
            return 0;
        }
    }
}