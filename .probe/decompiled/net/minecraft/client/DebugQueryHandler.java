package net.minecraft.client;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ServerboundBlockEntityTagQuery;
import net.minecraft.network.protocol.game.ServerboundEntityTagQuery;

public class DebugQueryHandler {

    private final ClientPacketListener connection;

    private int transactionId = -1;

    @Nullable
    private Consumer<CompoundTag> callback;

    public DebugQueryHandler(ClientPacketListener clientPacketListener0) {
        this.connection = clientPacketListener0;
    }

    public boolean handleResponse(int int0, @Nullable CompoundTag compoundTag1) {
        if (this.transactionId == int0 && this.callback != null) {
            this.callback.accept(compoundTag1);
            this.callback = null;
            return true;
        } else {
            return false;
        }
    }

    private int startTransaction(Consumer<CompoundTag> consumerCompoundTag0) {
        this.callback = consumerCompoundTag0;
        return ++this.transactionId;
    }

    public void queryEntityTag(int int0, Consumer<CompoundTag> consumerCompoundTag1) {
        int $$2 = this.startTransaction(consumerCompoundTag1);
        this.connection.send(new ServerboundEntityTagQuery($$2, int0));
    }

    public void queryBlockEntityTag(BlockPos blockPos0, Consumer<CompoundTag> consumerCompoundTag1) {
        int $$2 = this.startTransaction(consumerCompoundTag1);
        this.connection.send(new ServerboundBlockEntityTagQuery($$2, blockPos0));
    }
}