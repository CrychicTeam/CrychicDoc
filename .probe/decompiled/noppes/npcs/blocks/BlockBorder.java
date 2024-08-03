package noppes.npcs.blocks;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import noppes.npcs.CustomBlocks;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileBorder;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.packets.server.SPacketGuiOpen;

public class BlockBorder extends BlockInterface {

    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 3);

    public BlockBorder() {
        super(BlockBehaviour.Properties.copy(Blocks.BARRIER).sound(SoundType.STONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        ItemStack currentItem = player.getInventory().getSelected();
        if (!level.isClientSide && currentItem.getItem() == CustomItems.wand) {
            SPacketGuiOpen.sendOpenGui(player, EnumGuiType.Border, null, pos);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.m_43723_() != null) {
            int l = Mth.floor((double) (context.m_43723_().m_146908_() * 4.0F / 360.0F) + 0.5) & 3;
            l %= 4;
            return (BlockState) this.m_49966_().m_61124_(ROTATION, l);
        } else {
            return super.m_5573_(context);
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack item) {
        TileBorder tile = (TileBorder) level.getBlockEntity(pos);
        TileBorder adjacent = this.getTile(level, pos.west());
        if (adjacent == null) {
            adjacent = this.getTile(level, pos.south());
        }
        if (adjacent == null) {
            adjacent = this.getTile(level, pos.north());
        }
        if (adjacent == null) {
            adjacent = this.getTile(level, pos.east());
        }
        if (adjacent != null) {
            CompoundTag compound = new CompoundTag();
            adjacent.writeExtraNBT(compound);
            tile.readExtraNBT(compound);
        }
        tile.rotation = (Integer) state.m_61143_(ROTATION);
        if (!level.isClientSide && entity instanceof Player) {
            SPacketGuiOpen.sendOpenGui((Player) entity, EnumGuiType.Border, null, pos);
        }
    }

    private TileBorder getTile(Level level, BlockPos pos) {
        BlockEntity tile = level.getBlockEntity(pos);
        return tile != null && tile instanceof TileBorder ? (TileBorder) tile : null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileBorder(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return m_152132_(type, CustomBlocks.tile_border, TileBorder::tick);
    }
}