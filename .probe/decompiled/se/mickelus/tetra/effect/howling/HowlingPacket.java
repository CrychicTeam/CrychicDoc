package se.mickelus.tetra.effect.howling;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.network.AbstractPacket;
import se.mickelus.tetra.effect.EffectHelper;
import se.mickelus.tetra.effect.ItemEffect;

@ParametersAreNonnullByDefault
public class HowlingPacket extends AbstractPacket {

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
    }

    @Override
    public void handle(Player player) {
        ItemStack itemStack = player.m_21205_();
        if (player.getAttackStrengthScale(0.5F) > 0.9F) {
            int effectLevel = EffectHelper.getEffectLevel(itemStack, ItemEffect.howling);
            if (effectLevel > 0) {
                HowlingEffect.trigger(itemStack, player, effectLevel);
            }
        }
    }
}