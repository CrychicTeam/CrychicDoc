package com.mna.blocks.artifice;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.tile.EldrinCapacitorTile;
import com.mna.api.blocks.tile.IEldrinCapacitorTile;
import com.mna.api.gui.EldrinCapacitorPermissionsContainer;
import com.mna.blocks.tileentities.EldrinConduitTile;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.blocks.utility.BlockWithOffset;
import com.mna.capabilities.chunkdata.ChunkMagicProvider;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class EldrinConduitBlock extends BlockWithOffset implements EntityBlock {

    private final Affinity affinity;

    private final boolean isLesser;

    protected static final VoxelShape LESSER_SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 24.0, 10.0);

    public EldrinConduitBlock(Affinity affinity, boolean isLesser) {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F).noOcclusion(), isLesser ? new BlockPos[0] : new BlockPos[] { new BlockPos(0, 1, 0) });
        this.affinity = affinity;
        this.isLesser = isLesser;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EldrinConduitTile(this.affinity, this.isLesser ? 50.0F : 250.0F, this.isLesser, pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            Consumer<FriendlyByteBuf> dataWriter = this.getContainerBufferWriter(state, level, pos, player, hand, hitResult);
            MenuProvider provider = this.getProvider(state, level, pos, player, hand, hitResult);
            if (provider != null && dataWriter != null) {
                NetworkHooks.openScreen((ServerPlayer) player, provider, dataWriter);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.SUCCESS;
            }
        }
    }

    protected MenuProvider getProvider(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);
        return be != null && be instanceof EldrinCapacitorTile ? new SimpleMenuProvider((id, playerInv, user) -> new EldrinCapacitorPermissionsContainer(id, playerInv, (EldrinCapacitorTile) be), Component.empty()) : null;
    }

    protected Consumer<FriendlyByteBuf> getContainerBufferWriter(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity workbench = level.getBlockEntity(pos);
        return workbench != null && workbench instanceof EldrinCapacitorTile ? (EldrinCapacitorTile) workbench : null;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.ELDRIN_CONDUIT_TILE.get() ? (lvl, pos, state1, be) -> EldrinConduitTile.Tick(lvl, pos, state1, (EldrinConduitTile) be) : null;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_149645_1_) {
        return this.isLesser ? RenderShape.MODEL : RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return this.isLesser ? LESSER_SHAPE : Shapes.block();
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            worldIn.getChunkAt(pos).getCapability(ChunkMagicProvider.MAGIC).ifPresent(magic -> magic.popKnownEldrinSupplier(pos));
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        if (pPlacer instanceof Player player && pLevel.getBlockEntity(pPos) instanceof IEldrinCapacitorTile capTile) {
            capTile.setPlacedBy(player);
            pLevel.getChunkAt(pPos).getCapability(ManaAndArtificeMod.getChunkMagicCapability()).ifPresent(magic -> magic.pushKnownEldrinSupplier(pPos));
        }
    }
}