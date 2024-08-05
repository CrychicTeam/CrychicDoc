package noppes.npcs.blocks;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import noppes.npcs.CustomBlocks;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.blocks.tiles.TileRedstoneBlock;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.packets.server.SPacketGuiOpen;

public class BlockNpcRedstone extends BlockInterface {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockNpcRedstone() {
        super(BlockBehaviour.Properties.copy(Blocks.STONE).lightLevel(state -> 12).strength(50.0F, 2000.0F));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack currentItem = player.getInventory().getSelected();
            if (currentItem != null && currentItem.getItem() == CustomItems.wand && CustomNpcsPermissions.hasPermission((ServerPlayer) player, CustomNpcsPermissions.EDIT_BLOCKS)) {
                SPacketGuiOpen.sendOpenGui(player, EnumGuiType.RedstoneBlock, null, pos);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }
    }

    @Override
    public void onPlace(BlockState state, Level par1Level, BlockPos pos, BlockState stateNew, boolean bo) {
        par1Level.updateNeighborsAt(pos, this);
        par1Level.updateNeighborsAt(pos.below(), this);
        par1Level.updateNeighborsAt(pos.above(), this);
        par1Level.updateNeighborsAt(pos.west(), this);
        par1Level.updateNeighborsAt(pos.east(), this);
        par1Level.updateNeighborsAt(pos.south(), this);
        par1Level.updateNeighborsAt(pos.north(), this);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack item) {
        if (!level.isClientSide && entity instanceof Player) {
            SPacketGuiOpen.sendOpenGui((Player) entity, EnumGuiType.RedstoneBlock, null, pos);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        this.onPlace(state, level, pos, state, isMoving);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter worldIn, BlockPos pos, Direction side) {
        return this.isActivated(state);
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
        return this.isActivated(state);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    public int isActivated(BlockState state) {
        return state.m_61143_(ACTIVE) ? 15 : 0;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileRedstoneBlock(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return m_152132_(type, CustomBlocks.tile_redstoneblock, TileRedstoneBlock::tick);
    }
}