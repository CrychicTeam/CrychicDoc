package net.blay09.mods.waystones.block;

import java.util.List;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.block.entity.SharestoneBlockEntity;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntityBase;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SharestoneBlock extends WaystoneBlockBase {

    private static final VoxelShape LOWER_SHAPE = Shapes.or(m_49796_(0.0, 0.0, 0.0, 16.0, 3.0, 16.0), m_49796_(1.0, 3.0, 1.0, 15.0, 7.0, 15.0), m_49796_(2.0, 7.0, 2.0, 14.0, 9.0, 14.0), m_49796_(3.0, 9.0, 3.0, 13.0, 16.0, 13.0)).optimize();

    private static final VoxelShape UPPER_SHAPE = Shapes.or(m_49796_(3.0, 0.0, 3.0, 13.0, 7.0, 13.0), m_49796_(2.0, 7.0, 2.0, 14.0, 9.0, 14.0), m_49796_(1.0, 9.0, 1.0, 15.0, 13.0, 15.0), m_49796_(0.0, 13.0, 0.0, 16.0, 16.0, 16.0)).optimize();

    @Nullable
    private final DyeColor color;

    public SharestoneBlock(BlockBehaviour.Properties properties, @Nullable DyeColor color) {
        super(properties);
        this.color = color;
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(HALF, DoubleBlockHalf.LOWER)).m_61124_(WATERLOGGED, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SharestoneBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.m_61143_(HALF) == DoubleBlockHalf.UPPER ? UPPER_SHAPE : LOWER_SHAPE;
    }

    @Override
    protected InteractionResult handleActivation(Level world, BlockPos pos, Player player, WaystoneBlockEntityBase tileEntity, IWaystone waystone) {
        if (!world.isClientSide) {
            Balm.getNetworking().openGui(player, tileEntity.getMenuProvider());
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> list, TooltipFlag flag) {
        MutableComponent component = Component.translatable(this.color != null ? "tooltip.waystones." + this.color.getSerializedName() + "_sharestone" : "tooltip.waystones.sharestone");
        component.withStyle(ChatFormatting.GRAY);
        list.add(component);
        super.appendHoverText(stack, world, list, flag);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    @Nullable
    public DyeColor getColor() {
        return this.color;
    }
}