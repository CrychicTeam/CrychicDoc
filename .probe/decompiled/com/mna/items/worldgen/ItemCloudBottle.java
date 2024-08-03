package com.mna.items.worldgen;

import com.mna.blocks.BlockInit;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;

public class ItemCloudBottle extends Item {

    public ItemCloudBottle() {
        super(new Item.Properties().stacksTo(16).setNoRepair());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide()) {
            return InteractionResult.PASS;
        } else {
            Direction face = context.getClickedFace();
            BlockPos pos = context.getClickedPos();
            if (face == Direction.UP) {
                pos = pos.above();
                int count = 0;
                while (context.getLevel().getBlockState(pos).m_60795_() && count++ < 5) {
                    pos = pos.above();
                }
                pos = pos.below();
            } else {
                pos = pos.offset(face.getNormal());
            }
            if (context.getLevel().getBlockState(pos).m_60795_()) {
                BlockSnapshot blocksnapshot = BlockSnapshot.create(context.getLevel().dimension(), context.getLevel(), pos);
                context.getLevel().setBlock(pos, BlockInit.STORM_CLOUD.get().m_49966_(), 11);
                if (ForgeEventFactory.onBlockPlace(context.getPlayer(), blocksnapshot, Direction.UP)) {
                    blocksnapshot.restore(true, false);
                    return InteractionResult.FAIL;
                } else {
                    if (context.getPlayer() instanceof ServerPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) context.getPlayer(), pos, context.getItemInHand());
                    }
                    if (!context.getPlayer().getAbilities().instabuild) {
                        context.getItemInHand().shrink(1);
                    }
                    context.getLevel().playSound(context.getPlayer(), pos, SoundEvents.POINTED_DRIPSTONE_DRIP_WATER, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            } else {
                return InteractionResult.FAIL;
            }
        }
    }
}