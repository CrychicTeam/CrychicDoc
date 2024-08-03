package net.blay09.mods.waystones.block;

import java.util.List;
import java.util.UUID;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.WaystoneOrigin;
import net.blay09.mods.waystones.block.entity.PortstoneBlockEntity;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PortstoneBlock extends WaystoneBlockBase {

    private static final VoxelShape[] LOWER_SHAPES = new VoxelShape[] { Shapes.or(m_49796_(0.0, 0.0, 0.0, 16.0, 3.0, 16.0), m_49796_(1.0, 3.0, 1.0, 15.0, 7.0, 15.0), m_49796_(2.0, 7.0, 2.0, 14.0, 9.0, 14.0), m_49796_(3.0, 9.0, 3.0, 13.0, 16.0, 7.0), m_49796_(4.0, 9.0, 7.0, 12.0, 16.0, 10.0), m_49796_(4.0, 9.0, 10.0, 12.0, 12.0, 12.0)).optimize(), Shapes.or(m_49796_(0.0, 0.0, 0.0, 16.0, 3.0, 16.0), m_49796_(1.0, 3.0, 1.0, 15.0, 7.0, 15.0), m_49796_(2.0, 7.0, 2.0, 14.0, 9.0, 14.0), m_49796_(9.0, 9.0, 3.0, 13.0, 16.0, 13.0), m_49796_(6.0, 9.0, 4.0, 9.0, 16.0, 12.0), m_49796_(4.0, 9.0, 4.0, 6.0, 12.0, 12.0)).optimize(), Shapes.or(m_49796_(0.0, 0.0, 0.0, 16.0, 3.0, 16.0), m_49796_(1.0, 3.0, 1.0, 15.0, 7.0, 15.0), m_49796_(2.0, 7.0, 2.0, 14.0, 9.0, 14.0), m_49796_(3.0, 9.0, 9.0, 13.0, 16.0, 13.0), m_49796_(4.0, 9.0, 6.0, 12.0, 16.0, 9.0), m_49796_(4.0, 9.0, 4.0, 12.0, 12.0, 6.0)).optimize(), Shapes.or(m_49796_(0.0, 0.0, 0.0, 16.0, 3.0, 16.0), m_49796_(1.0, 3.0, 1.0, 15.0, 7.0, 15.0), m_49796_(2.0, 7.0, 2.0, 14.0, 9.0, 14.0), m_49796_(3.0, 9.0, 3.0, 7.0, 16.0, 13.0), m_49796_(7.0, 9.0, 4.0, 10.0, 16.0, 12.0), m_49796_(10.0, 9.0, 4.0, 12.0, 12.0, 12.0)).optimize() };

    private static final VoxelShape[] UPPER_SHAPES = new VoxelShape[] { Shapes.or(m_49796_(3.0, 0.0, 3.0, 13.0, 7.0, 7.0), m_49796_(4.0, 0.0, 7.0, 12.0, 2.0, 9.0)).optimize(), Shapes.or(m_49796_(9.0, 0.0, 3.0, 13.0, 7.0, 13.0), m_49796_(7.0, 0.0, 4.0, 9.0, 2.0, 12.0)).optimize(), Shapes.or(m_49796_(3.0, 0.0, 9.0, 13.0, 7.0, 13.0), m_49796_(4.0, 0.0, 7.0, 12.0, 2.0, 9.0)).optimize(), Shapes.or(m_49796_(3.0, 0.0, 3.0, 7.0, 7.0, 13.0), m_49796_(7.0, 0.0, 4.0, 9.0, 2.0, 12.0)).optimize() };

    public PortstoneBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(HALF, DoubleBlockHalf.LOWER)).m_61124_(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = (Direction) state.m_61143_(FACING);
        return state.m_61143_(HALF) == DoubleBlockHalf.UPPER ? UPPER_SHAPES[direction.get2DDataValue()] : LOWER_SHAPES[direction.get2DDataValue()];
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PortstoneBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, final Level world, final BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (!world.isClientSide) {
            Balm.getNetworking().openGui(player, new BalmMenuProvider() {

                @Override
                public Component getDisplayName() {
                    return Component.translatable("block.waystones.portstone");
                }

                @Override
                public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                    IWaystone portstone = new Waystone(WaystoneTypes.PORTSTONE, UUID.randomUUID(), world.dimension(), pos, WaystoneOrigin.UNKNOWN, null);
                    return WaystoneSelectionMenu.createWaystoneSelection(i, player, WarpMode.PORTSTONE_TO_WAYSTONE, portstone);
                }

                @Override
                public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                    buf.writeByte(WarpMode.PORTSTONE_TO_WAYSTONE.ordinal());
                    buf.writeBlockPos(pos);
                }
            });
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> list, TooltipFlag flag) {
        MutableComponent component = Component.translatable("tooltip.waystones.portstone");
        component.withStyle(ChatFormatting.GRAY);
        list.add(component);
        super.appendHoverText(stack, world, list, flag);
    }
}