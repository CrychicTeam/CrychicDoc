package se.mickelus.tetra.effect;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.network.AbstractPacket;

@ParametersAreNonnullByDefault
public class TruesweepPacket extends AbstractPacket {

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
    }

    @Override
    public void handle(Player player) {
        ItemStack itemStack = player.m_21205_();
        if (player.getAttackStrengthScale(0.5F) > 0.9F && EffectHelper.getEffectLevel(itemStack, ItemEffect.truesweep) > 0) {
            boolean hasSweepingStrike = EffectHelper.getEffectLevel(itemStack, ItemEffect.sweepingStrike) > 0;
            if (player.m_20096_() && !player.m_20142_()) {
                SweepingEffect.truesweep(itemStack, player, !hasSweepingStrike);
            }
            if (hasSweepingStrike) {
                SweepingStrikeEffect.causeTruesweepEffect(player, itemStack);
            }
        }
    }
}