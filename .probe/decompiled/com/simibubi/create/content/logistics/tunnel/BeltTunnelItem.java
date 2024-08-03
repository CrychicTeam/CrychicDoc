package com.simibubi.create.content.logistics.tunnel;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class BeltTunnelItem extends BlockItem {

    public BeltTunnelItem(Block p_i48527_1_, Item.Properties p_i48527_2_) {
        super(p_i48527_1_, p_i48527_2_);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext ctx, BlockState state) {
        Player playerentity = ctx.m_43723_();
        CollisionContext iselectioncontext = playerentity == null ? CollisionContext.empty() : CollisionContext.of(playerentity);
        Level world = ctx.m_43725_();
        BlockPos pos = ctx.getClickedPos();
        return (!this.m_6652_() || ((BeltTunnelBlock) AllBlocks.ANDESITE_TUNNEL.get()).isValidPositionForPlacement(state, world, pos)) && world.m_45752_(state, pos, iselectioncontext);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level world, Player p_195943_3_, ItemStack p_195943_4_, BlockState state) {
        boolean flag = super.updateCustomBlockEntityTag(pos, world, p_195943_3_, p_195943_4_, state);
        if (!world.isClientSide) {
            BeltBlockEntity belt = BeltHelper.getSegmentBE(world, pos.below());
            if (belt != null && belt.casing == BeltBlockEntity.CasingType.NONE) {
                belt.setCasingType(AllBlocks.ANDESITE_TUNNEL.has(state) ? BeltBlockEntity.CasingType.ANDESITE : BeltBlockEntity.CasingType.BRASS);
            }
        }
        return flag;
    }
}