package io.redspace.ironsspellbooks.gui.inscription_table.network;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ServerboundInscriptionTableSelectSpell {

    private final BlockPos pos;

    private final int selectedIndex;

    public ServerboundInscriptionTableSelectSpell(BlockPos pos, int selectedIndex) {
        this.pos = pos;
        this.selectedIndex = selectedIndex;
    }

    public ServerboundInscriptionTableSelectSpell(FriendlyByteBuf buf) {
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        this.pos = new BlockPos(x, y, z);
        this.selectedIndex = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.pos.m_123341_());
        buf.writeInt(this.pos.m_123342_());
        buf.writeInt(this.pos.m_123343_());
        buf.writeInt(this.selectedIndex);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> {
        });
        return true;
    }
}