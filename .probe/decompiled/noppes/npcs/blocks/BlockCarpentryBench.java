package noppes.npcs.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileBlockAnvil;
import noppes.npcs.constants.EnumGuiType;

public class BlockCarpentryBench extends BlockInterface {

    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 3);

    public BlockCarpentryBench() {
        super(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE).sound(SoundType.WOOD).strength(5.0F, 10.0F));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (!level.isClientSide) {
            NoppesUtilServer.openContainerGui((ServerPlayer) player, EnumGuiType.PlayerAnvil, buffer -> buffer.writeBlockPos(pos));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState p_196247_1_, BlockGetter p_196247_2_, BlockPos p_196247_3_) {
        return Shapes.empty();
    }

    @Override
    public boolean isPathfindable(BlockState p_196266_1_, BlockGetter p_196266_2_, BlockPos p_196266_3_, PathComputationType p_196266_4_) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        int var6 = Mth.floor((double) (context.m_43723_().m_146908_() / 90.0F) + 0.5) & 3;
        return (BlockState) this.m_49966_().m_61124_(ROTATION, var6);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileBlockAnvil(pos, state);
    }
}