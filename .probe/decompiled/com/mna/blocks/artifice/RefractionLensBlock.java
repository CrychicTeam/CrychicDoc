package com.mna.blocks.artifice;

import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.interfaces.ICutoutBlock;
import com.mna.blocks.BlockInit;
import com.mna.blocks.tileentities.RefractionLensTile;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.blocks.utility.BlockWithOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RefractionLensBlock extends BlockWithOffset implements ICutoutBlock, EntityBlock {

    private final Affinity affinity;

    public static final BooleanProperty INVALID = BooleanProperty.create("invalid");

    public RefractionLensBlock(Affinity affinity) {
        super(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).noOcclusion().strength(3.0F).isViewBlocking(BlockInit.NEVER), new BlockPos(-1, 0, -1), new BlockPos(-1, 0, 0), new BlockPos(-1, 0, 1), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1), new BlockPos(1, 0, -1), new BlockPos(1, 0, 0), new BlockPos(1, 0, 1));
        this.affinity = affinity;
        this.m_49959_((BlockState) this.m_49966_().m_61124_(INVALID, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(INVALID);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RefractionLensTile(this.affinity, pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.REFRACTION_LENS.get() ? (lvl, pos, state1, be) -> RefractionLensTile.Tick(lvl, pos, state1, (RefractionLensTile) be) : null;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity living, ItemStack stack) {
        if (living instanceof Player) {
            RefractionLensTile tile = (RefractionLensTile) world.getBlockEntity(pos);
            tile.setPlacedBy((Player) living);
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, BlockGetter p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean doDrops) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile != null && tile instanceof RefractionLensTile) {
            ((RefractionLensTile) tile).onBlockBroken();
        }
        super.onRemove(state, world, pos, newState, doDrops);
    }

    @Override
    public VoxelShape getVisualShape(BlockState p_230322_1_, BlockGetter p_230322_2_, BlockPos p_230322_3_, CollisionContext p_230322_4_) {
        return Shapes.empty();
    }

    @Override
    public InteractionResult use(BlockState p_225533_1_, Level p_225533_2_, BlockPos p_225533_3_, Player p_225533_4_, InteractionHand p_225533_5_, BlockHitResult p_225533_6_) {
        if (p_225533_2_.isClientSide) {
            BlockEntity te = p_225533_2_.getBlockEntity(p_225533_3_);
            if (te instanceof RefractionLensTile) {
                ((RefractionLensTile) te).setGhostMultiblock();
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public BlockPlaceContext adjustPlacement(BlockPlaceContext attempted) {
        switch(attempted.m_43719_()) {
            case EAST:
            case NORTH:
            case SOUTH:
            case WEST:
                BlockPos newPos = attempted.getClickedPos().offset(attempted.m_43719_().getNormal());
                BlockHitResult bhr = new BlockHitResult(Vec3.atCenterOf(newPos), attempted.m_43719_(), newPos, true);
                return new BlockPlaceContext(attempted.m_43723_(), attempted.m_43724_(), attempted.m_43722_(), bhr);
            case UP:
            case DOWN:
            default:
                return attempted;
        }
    }
}