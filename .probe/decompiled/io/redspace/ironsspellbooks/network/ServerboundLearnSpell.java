package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class ServerboundLearnSpell {

    private final byte hand;

    private final String spell;

    public ServerboundLearnSpell(InteractionHand interactionHand, String spell) {
        this.hand = handToByte(interactionHand);
        this.spell = spell;
    }

    public ServerboundLearnSpell(FriendlyByteBuf buf) {
        this.hand = buf.readByte();
        this.spell = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeByte(this.hand);
        buf.writeUtf(this.spell);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer serverPlayer = ctx.getSender();
            ItemStack itemStack = serverPlayer.m_21120_(byteToHand(this.hand));
            AbstractSpell spell = SpellRegistry.getSpell(this.spell);
            SyncedSpellData data = MagicData.getPlayerMagicData(serverPlayer).getSyncedData();
            if (spell != SpellRegistry.none() && !data.isSpellLearned(spell) && itemStack.is(ItemRegistry.ELDRITCH_PAGE.get()) && itemStack.getCount() > 0) {
                data.learnSpell(spell);
                if (!serverPlayer.m_150110_().instabuild) {
                    itemStack.shrink(1);
                }
            }
        });
        return true;
    }

    public static byte handToByte(InteractionHand hand) {
        return (byte) (hand == InteractionHand.MAIN_HAND ? 1 : 0);
    }

    public static InteractionHand byteToHand(byte b) {
        return b > 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }
}