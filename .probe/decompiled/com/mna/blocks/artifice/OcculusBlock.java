package com.mna.blocks.artifice;

import com.mna.api.blocks.interfaces.ITranslucentBlock;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.blocks.WaterloggableBlock;
import com.mna.blocks.tileentities.OcculusTile;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.gui.containers.providers.NamedOcculus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

public class OcculusBlock extends WaterloggableBlock implements ITranslucentBlock, EntityBlock {

    public OcculusBlock() {
        super(BlockBehaviour.Properties.of().pushReaction(PushReaction.BLOCK).noOcclusion().strength(3.0F), false);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            IPlayerMagic magic = (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            if (magic != null && magic.isMagicUnlocked()) {
                player.openMenu(new NamedOcculus());
            } else {
                player.m_213846_(Component.translatable("block.mna.occulus.confusion"));
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OcculusTile(pos, state);
    }
}