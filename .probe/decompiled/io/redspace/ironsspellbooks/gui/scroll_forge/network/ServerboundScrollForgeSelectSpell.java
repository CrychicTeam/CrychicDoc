package io.redspace.ironsspellbooks.gui.scroll_forge.network;

import io.redspace.ironsspellbooks.block.scroll_forge.ScrollForgeTile;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ServerboundScrollForgeSelectSpell {

    private final BlockPos pos;

    private final String spellId;

    public ServerboundScrollForgeSelectSpell(BlockPos pos, String spellId) {
        this.pos = pos;
        this.spellId = spellId;
    }

    public ServerboundScrollForgeSelectSpell(FriendlyByteBuf buf) {
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        this.pos = new BlockPos(x, y, z);
        this.spellId = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.pos.m_123341_());
        buf.writeInt(this.pos.m_123342_());
        buf.writeInt(this.pos.m_123343_());
        buf.writeUtf(this.spellId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> {
            ScrollForgeTile scrollForgeTile = (ScrollForgeTile) ctx.getSender().m_9236_().getBlockEntity(this.pos);
            if (scrollForgeTile != null) {
                scrollForgeTile.setRecipeSpell(this.spellId);
            }
        });
        return true;
    }
}