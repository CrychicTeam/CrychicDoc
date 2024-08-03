package com.simibubi.create.content.logistics.funnel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class FunnelItem extends BlockItem {

    public FunnelItem(Block p_i48527_1_, Item.Properties p_i48527_2_) {
        super(p_i48527_1_, p_i48527_2_);
    }

    @SubscribeEvent
    public static void funnelItemAlwaysPlacesWhenUsed(PlayerInteractEvent.RightClickBlock event) {
        if (event.getItemStack().getItem() instanceof FunnelItem) {
            event.setUseBlock(Result.DENY);
        }
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext ctx) {
        Level world = ctx.m_43725_();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = super.getPlacementState(ctx);
        if (state == null) {
            return state;
        } else if (!(state.m_60734_() instanceof FunnelBlock)) {
            return state;
        } else if (((Direction) state.m_61143_(FunnelBlock.FACING)).getAxis().isVertical()) {
            return state;
        } else {
            Direction direction = (Direction) state.m_61143_(FunnelBlock.FACING);
            FunnelBlock block = (FunnelBlock) this.m_40614_();
            Block beltFunnelBlock = block.getEquivalentBeltFunnel(world, pos, state).m_60734_();
            BlockState equivalentBeltFunnel = (BlockState) beltFunnelBlock.getStateForPlacement(ctx).m_61124_(BeltFunnelBlock.HORIZONTAL_FACING, direction);
            return BeltFunnelBlock.isOnValidBelt(equivalentBeltFunnel, world, pos) ? equivalentBeltFunnel : state;
        }
    }
}