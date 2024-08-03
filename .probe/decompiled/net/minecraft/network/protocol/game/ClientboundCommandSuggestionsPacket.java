package net.minecraft.network.protocol.game;

import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.protocol.Packet;

public class ClientboundCommandSuggestionsPacket implements Packet<ClientGamePacketListener> {

    private final int id;

    private final Suggestions suggestions;

    public ClientboundCommandSuggestionsPacket(int int0, Suggestions suggestions1) {
        this.id = int0;
        this.suggestions = suggestions1;
    }

    public ClientboundCommandSuggestionsPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.id = friendlyByteBuf0.readVarInt();
        int $$1 = friendlyByteBuf0.readVarInt();
        int $$2 = friendlyByteBuf0.readVarInt();
        StringRange $$3 = StringRange.between($$1, $$1 + $$2);
        List<Suggestion> $$4 = friendlyByteBuf0.readList(p_178793_ -> {
            String $$2x = p_178793_.readUtf();
            Component $$3x = p_178793_.readNullable(FriendlyByteBuf::m_130238_);
            return new Suggestion($$3, $$2x, $$3x);
        });
        this.suggestions = new Suggestions($$3, $$4);
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.id);
        friendlyByteBuf0.writeVarInt(this.suggestions.getRange().getStart());
        friendlyByteBuf0.writeVarInt(this.suggestions.getRange().getLength());
        friendlyByteBuf0.writeCollection(this.suggestions.getList(), (p_237617_, p_237618_) -> {
            p_237617_.writeUtf(p_237618_.getText());
            p_237617_.writeNullable(p_237618_.getTooltip(), (p_237614_, p_237615_) -> p_237614_.writeComponent(ComponentUtils.fromMessage(p_237615_)));
        });
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleCommandSuggestions(this);
    }

    public int getId() {
        return this.id;
    }

    public Suggestions getSuggestions() {
        return this.suggestions;
    }
}