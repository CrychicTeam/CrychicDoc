package com.simibubi.create.content.redstone.displayLink;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.redstone.displayLink.source.DisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.RedstonePowerDisplaySource;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class DisplayLinkBlock extends WrenchableDirectionalBlock implements IBE<DisplayLinkBlockEntity> {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public DisplayLinkBlock(BlockBehaviour.Properties p_i48415_1_) {
        super(p_i48415_1_);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(POWERED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState placed = super.getStateForPlacement(context);
        placed = (BlockState) placed.m_61124_(f_52588_, context.m_43719_());
        return (BlockState) placed.m_61124_(POWERED, this.shouldBePowered(placed, context.m_43725_(), context.getClickedPos()));
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.m_6402_(pLevel, pPos, pState, pPlacer, pStack);
        AdvancementBehaviour.setPlacedBy(pLevel, pPos, pPlacer);
    }

    public static void notifyGatherers(LevelAccessor level, BlockPos pos) {
        forEachAttachedGatherer(level, pos, DisplayLinkBlockEntity::updateGatheredData);
    }

    public static <T extends DisplaySource> void sendToGatherers(LevelAccessor level, BlockPos pos, BiConsumer<DisplayLinkBlockEntity, T> callback, Class<T> type) {
        forEachAttachedGatherer(level, pos, dgte -> {
            if (type.isInstance(dgte.activeSource)) {
                callback.accept(dgte, dgte.activeSource);
            }
        });
    }

    private static void forEachAttachedGatherer(LevelAccessor level, BlockPos pos, Consumer<DisplayLinkBlockEntity> callback) {
        for (Direction d : Iterate.directions) {
            BlockPos offsetPos = pos.relative(d);
            BlockState blockState = level.m_8055_(offsetPos);
            if (AllBlocks.DISPLAY_LINK.has(blockState)) {
                BlockEntity blockEntity = level.m_7702_(offsetPos);
                if (blockEntity instanceof DisplayLinkBlockEntity) {
                    DisplayLinkBlockEntity dlbe = (DisplayLinkBlockEntity) blockEntity;
                    if (dlbe.activeSource != null && dlbe.getDirection() == d.getOpposite()) {
                        callback.accept(dlbe);
                    }
                }
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            if (fromPos.equals(pos.relative(((Direction) state.m_61143_(f_52588_)).getOpposite()))) {
                sendToGatherers(worldIn, fromPos, (dlte, p) -> dlte.tickSource(), RedstonePowerDisplaySource.class);
            }
            boolean powered = this.shouldBePowered(state, worldIn, pos);
            boolean previouslyPowered = (Boolean) state.m_61143_(POWERED);
            if (previouslyPowered != powered) {
                worldIn.setBlock(pos, (BlockState) state.m_61122_(POWERED), 2);
                if (!powered) {
                    this.withBlockEntityDo(worldIn, pos, DisplayLinkBlockEntity::onNoLongerPowered);
                }
            }
        }
    }

    private boolean shouldBePowered(BlockState state, Level worldIn, BlockPos pos) {
        boolean powered = false;
        for (Direction d : Iterate.directions) {
            if (d.getOpposite() != state.m_61143_(f_52588_) && worldIn.m_277185_(pos.relative(d), d) != 0) {
                powered = true;
                break;
            }
        }
        return powered;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POWERED));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer == null) {
            return InteractionResult.PASS;
        } else if (pPlayer.m_6144_()) {
            return InteractionResult.PASS;
        } else {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.withBlockEntityDo(pLevel, pPos, be -> this.displayScreen(be, pPlayer)));
            return InteractionResult.SUCCESS;
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void displayScreen(DisplayLinkBlockEntity be, Player player) {
        if (player instanceof LocalPlayer) {
            if (be.targetOffset.equals(BlockPos.ZERO)) {
                player.displayClientMessage(Lang.translateDirect("display_link.invalid"), true);
            } else {
                ScreenOpener.open(new DisplayLinkScreen(be));
            }
        }
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.DATA_GATHERER.get((Direction) pState.m_61143_(f_52588_));
    }

    @Override
    public Class<DisplayLinkBlockEntity> getBlockEntityClass() {
        return DisplayLinkBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DisplayLinkBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends DisplayLinkBlockEntity>) AllBlockEntityTypes.DISPLAY_LINK.get();
    }
}