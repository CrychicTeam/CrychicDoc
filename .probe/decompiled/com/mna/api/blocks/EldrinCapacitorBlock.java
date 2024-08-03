package com.mna.api.blocks;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.tile.EldrinCapacitorTile;
import com.mna.api.blocks.tile.IEldrinCapacitorTile;
import com.mna.api.gui.EldrinCapacitorPermissionsContainer;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public abstract class EldrinCapacitorBlock extends Block implements EntityBlock {

    protected Affinity[] affinities;

    public EldrinCapacitorBlock(BlockBehaviour.Properties pProperties, Affinity... affinities) {
        super(pProperties);
        this.affinities = affinities;
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
        if (be != null && be instanceof EldrinCapacitorTile cap) {
            return cap.getPlacedBy() != player.getGameProfile().getId() ? null : new SimpleMenuProvider((id, playerInv, user) -> new EldrinCapacitorPermissionsContainer(id, playerInv, cap), Component.empty());
        } else {
            return null;
        }
    }

    protected Consumer<FriendlyByteBuf> getContainerBufferWriter(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity workbench = level.getBlockEntity(pos);
        return workbench != null && workbench instanceof EldrinCapacitorTile ? (EldrinCapacitorTile) workbench : null;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        if (pPlacer instanceof Player player && pLevel.getBlockEntity(pPos) instanceof IEldrinCapacitorTile capTile) {
            capTile.setPlacedBy(player);
            pLevel.getChunkAt(pPos).getCapability(ManaAndArtificeMod.getChunkMagicCapability()).ifPresent(magic -> magic.pushKnownEldrinSupplier(pPos));
        }
    }

    @Nullable
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public void onRemove(BlockState state, Level pLevel, BlockPos pPos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            pLevel.getChunkAt(pPos).getCapability(ManaAndArtificeMod.getChunkMagicCapability()).ifPresent(magic -> magic.popKnownEldrinSupplier(pPos));
        }
        super.m_6810_(state, pLevel, pPos, newState, isMoving);
    }
}