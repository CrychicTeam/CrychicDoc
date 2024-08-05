package net.minecraft.network.protocol.game;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundEditBookPacket implements Packet<ServerGamePacketListener> {

    public static final int MAX_BYTES_PER_CHAR = 4;

    private static final int TITLE_MAX_CHARS = 128;

    private static final int PAGE_MAX_CHARS = 8192;

    private static final int MAX_PAGES_COUNT = 200;

    private final int slot;

    private final List<String> pages;

    private final Optional<String> title;

    public ServerboundEditBookPacket(int int0, List<String> listString1, Optional<String> optionalString2) {
        this.slot = int0;
        this.pages = ImmutableList.copyOf(listString1);
        this.title = optionalString2;
    }

    public ServerboundEditBookPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.slot = friendlyByteBuf0.readVarInt();
        this.pages = friendlyByteBuf0.readCollection(FriendlyByteBuf.limitValue(Lists::newArrayListWithCapacity, 200), p_182763_ -> p_182763_.readUtf(8192));
        this.title = friendlyByteBuf0.readOptional(p_182757_ -> p_182757_.readUtf(128));
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.slot);
        friendlyByteBuf0.writeCollection(this.pages, (p_182759_, p_182760_) -> p_182759_.writeUtf(p_182760_, 8192));
        friendlyByteBuf0.writeOptional(this.title, (p_182753_, p_182754_) -> p_182753_.writeUtf(p_182754_, 128));
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleEditBook(this);
    }

    public List<String> getPages() {
        return this.pages;
    }

    public Optional<String> getTitle() {
        return this.title;
    }

    public int getSlot() {
        return this.slot;
    }
}