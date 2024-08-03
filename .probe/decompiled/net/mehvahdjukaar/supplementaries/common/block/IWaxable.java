package net.mehvahdjukaar.supplementaries.common.block;

import net.mehvahdjukaar.supplementaries.common.network.ClientBoundParticlePacket;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IWaxable {

    boolean isWaxed();

    void setWaxed(boolean var1);

    default InteractionResult tryWaxing(Level level, BlockPos pos, Player player, InteractionHand hand) {
        if (this.isWaxed()) {
            level.m_247517_(null, pos, SoundEvents.WAXED_SIGN_INTERACT_FAIL, SoundSource.BLOCKS);
            return InteractionResult.FAIL;
        } else {
            ItemStack stack = player.m_21120_(hand);
            if (stack.getItem() instanceof HoneycombItem) {
                level.m_247517_(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                if (player instanceof ServerPlayer serverPlayer) {
                    this.setWaxed(true);
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    ModNetwork.CHANNEL.sendToAllClientPlayersInRange(level, pos, 64.0, new ClientBoundParticlePacket(pos, ClientBoundParticlePacket.EventType.WAX_ON));
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }
}