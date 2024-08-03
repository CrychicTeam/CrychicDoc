package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityLeafcutterAnt;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityLeafcutterAnthill;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class ItemLeafcutterPupa extends Item {

    public ItemLeafcutterPupa(Item.Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (blockstate.m_204336_(AMTagRegistry.LEAFCUTTER_PUPA_USABLE_ON) && world.getBlockState(blockpos.below()).m_204336_(AMTagRegistry.LEAFCUTTER_PUPA_USABLE_ON)) {
            Player playerentity = context.getPlayer();
            if (playerentity != null) {
                playerentity.m_146850_(GameEvent.BLOCK_PLACE);
            }
            world.playSound(playerentity, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!world.isClientSide) {
                world.setBlock(blockpos, AMBlockRegistry.LEAFCUTTER_ANTHILL.get().defaultBlockState(), 11);
                world.setBlock(blockpos.below(), AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get().defaultBlockState(), 11);
                if (world.getBlockEntity(blockpos) instanceof TileEntityLeafcutterAnthill beehivetileentity) {
                    int j = Math.min(3, AMConfig.leafcutterAntColonySize);
                    for (int k = 0; k < j; k++) {
                        EntityLeafcutterAnt beeentity = new EntityLeafcutterAnt(AMEntityRegistry.LEAFCUTTER_ANT.get(), world);
                        beeentity.setQueen(k == 0);
                        beehivetileentity.tryEnterHive(beeentity, false, 100);
                    }
                }
                if (playerentity != null && !playerentity.isCreative()) {
                    context.getItemInHand().shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }
}