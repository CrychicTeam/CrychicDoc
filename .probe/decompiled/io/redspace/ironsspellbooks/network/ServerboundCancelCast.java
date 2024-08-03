package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.MagicHelper;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.item.Scroll;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ServerboundCancelCast {

    private final boolean triggerCooldown;

    public ServerboundCancelCast(boolean triggerCooldown) {
        this.triggerCooldown = triggerCooldown;
    }

    public ServerboundCancelCast(FriendlyByteBuf buf) {
        this.triggerCooldown = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(this.triggerCooldown);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer serverPlayer = ctx.getSender();
            cancelCast(serverPlayer, this.triggerCooldown);
        });
        return true;
    }

    public static void cancelCast(ServerPlayer serverPlayer, boolean triggerCooldown) {
        if (serverPlayer != null) {
            MagicData playerMagicData = MagicData.getPlayerMagicData(serverPlayer);
            if (playerMagicData.isCasting()) {
                SpellData spellData = playerMagicData.getCastingSpell();
                if (triggerCooldown) {
                    MagicHelper.MAGIC_MANAGER.addCooldown(serverPlayer, spellData.getSpell(), playerMagicData.getCastSource());
                }
                if (playerMagicData.getCastSource() == CastSource.SCROLL && spellData.getSpell().getCastType() == CastType.CONTINUOUS) {
                    Scroll.attemptRemoveScrollAfterCast(serverPlayer);
                }
                playerMagicData.getCastingSpell().getSpell().onServerCastComplete(serverPlayer.f_19853_, spellData.getLevel(), serverPlayer, playerMagicData, true);
            }
        }
    }
}