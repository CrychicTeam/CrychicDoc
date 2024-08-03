package io.github.lightman314.lightmanscurrency.common.blocks;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IEasyEntityBlock;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IOwnableBlock;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.RotatableBlock;
import io.github.lightman314.lightmanscurrency.common.blockentity.CoinChestBlockEntity;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class CoinChestBlock extends RotatableBlock implements IEasyEntityBlock, IOwnableBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);

    public CoinChestBlock(BlockBehaviour.Properties properties) {
        super(properties, SHAPE);
        this.m_49959_((BlockState) ((BlockState) this.m_49965_().any()).m_61124_(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    @Nonnull
    @Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        FluidState fluidstate = context.m_43725_().getFluidState(context.getClickedPos());
        return (BlockState) super.getStateForPlacement(context).m_61124_(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    public void setPlacedBy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity player, @Nonnull ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof CoinChestBlockEntity be && stack.hasCustomHoverName()) {
            be.setCustomName(stack.getHoverName());
        }
        super.m_6402_(level, pos, state, player, stack);
    }

    @Nonnull
    @Override
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult result) {
        if (level.getBlockEntity(pos) instanceof CoinChestBlockEntity be && player instanceof ServerPlayer sp) {
            if (be.allowAccess(player)) {
                NetworkHooks.openScreen(sp, CoinChestBlockEntity.getMenuProvider(be), pos);
                PiglinAi.angerNearbyPiglins(player, true);
            } else {
                player.m_213846_(LCText.MESSAGE_COIN_CHEST_PROTECTION_WARNING.get().withStyle(ChatFormatting.GOLD));
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void playerWillDestroy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
        if (level.getBlockEntity(pos) instanceof CoinChestBlockEntity be) {
            if (!be.allowAccess(player)) {
                return;
            }
            be.onValidBlockRemoval();
        }
        super.m_5707_(level, pos, state, player);
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean flag) {
        if (level.getBlockEntity(pos) instanceof CoinChestBlockEntity be) {
            be.onBlockRemoval();
            Containers.dropContents(level, pos, be.getStorage());
            Containers.dropContents(level, pos, be.getUpgrades());
        }
        super.m_6810_(state, level, pos, newState, flag);
    }

    @Nonnull
    @Override
    public Collection<BlockEntityType<?>> getAllowedTypes() {
        return Collections.singleton(ModBlockEntities.COIN_CHEST.get());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new CoinChestBlockEntity(pos, state);
    }

    @Override
    public void tick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        super.m_213897_(state, level, pos, random);
        if (level.m_7702_(pos) instanceof CoinChestBlockEntity be) {
            be.recheckOpen();
        }
    }

    @Override
    public boolean triggerEvent(@Nonnull BlockState blockState0, @Nonnull Level level1, @Nonnull BlockPos blockPos2, int int3, int int4) {
        super.m_8133_(blockState0, level1, blockPos2, int3, int4);
        BlockEntity blockentity = level1.getBlockEntity(blockPos2);
        return blockentity != null && blockentity.triggerEvent(int3, int4);
    }

    @Override
    public boolean canBreak(@Nonnull Player player, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return level.m_7702_(pos) instanceof CoinChestBlockEntity blockEntity ? blockEntity.allowAccess(player) : true;
    }
}