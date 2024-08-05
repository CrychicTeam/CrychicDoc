package net.minecraft.network.protocol.game;

import com.google.common.base.MoreObjects;
import com.mojang.authlib.GameProfile;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.Optionull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.RemoteChatSession;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

public class ClientboundPlayerInfoUpdatePacket implements Packet<ClientGamePacketListener> {

    private final EnumSet<ClientboundPlayerInfoUpdatePacket.Action> actions;

    private final List<ClientboundPlayerInfoUpdatePacket.Entry> entries;

    public ClientboundPlayerInfoUpdatePacket(EnumSet<ClientboundPlayerInfoUpdatePacket.Action> enumSetClientboundPlayerInfoUpdatePacketAction0, Collection<ServerPlayer> collectionServerPlayer1) {
        this.actions = enumSetClientboundPlayerInfoUpdatePacketAction0;
        this.entries = collectionServerPlayer1.stream().map(ClientboundPlayerInfoUpdatePacket.Entry::new).toList();
    }

    public ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action clientboundPlayerInfoUpdatePacketAction0, ServerPlayer serverPlayer1) {
        this.actions = EnumSet.of(clientboundPlayerInfoUpdatePacketAction0);
        this.entries = List.of(new ClientboundPlayerInfoUpdatePacket.Entry(serverPlayer1));
    }

    public static ClientboundPlayerInfoUpdatePacket createPlayerInitializing(Collection<ServerPlayer> collectionServerPlayer0) {
        EnumSet<ClientboundPlayerInfoUpdatePacket.Action> $$1 = EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, ClientboundPlayerInfoUpdatePacket.Action.INITIALIZE_CHAT, ClientboundPlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE, ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED, ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY, ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME);
        return new ClientboundPlayerInfoUpdatePacket($$1, collectionServerPlayer0);
    }

    public ClientboundPlayerInfoUpdatePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.actions = friendlyByteBuf0.readEnumSet(ClientboundPlayerInfoUpdatePacket.Action.class);
        this.entries = friendlyByteBuf0.readList(p_249950_ -> {
            ClientboundPlayerInfoUpdatePacket.EntryBuilder $$1 = new ClientboundPlayerInfoUpdatePacket.EntryBuilder(p_249950_.readUUID());
            for (ClientboundPlayerInfoUpdatePacket.Action $$2 : this.actions) {
                $$2.reader.read($$1, p_249950_);
            }
            return $$1.build();
        });
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeEnumSet(this.actions, ClientboundPlayerInfoUpdatePacket.Action.class);
        friendlyByteBuf0.writeCollection(this.entries, (p_251434_, p_252303_) -> {
            p_251434_.writeUUID(p_252303_.profileId());
            for (ClientboundPlayerInfoUpdatePacket.Action $$2 : this.actions) {
                $$2.writer.write(p_251434_, p_252303_);
            }
        });
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handlePlayerInfoUpdate(this);
    }

    public EnumSet<ClientboundPlayerInfoUpdatePacket.Action> actions() {
        return this.actions;
    }

    public List<ClientboundPlayerInfoUpdatePacket.Entry> entries() {
        return this.entries;
    }

    public List<ClientboundPlayerInfoUpdatePacket.Entry> newEntries() {
        return this.actions.contains(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER) ? this.entries : List.of();
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("actions", this.actions).add("entries", this.entries).toString();
    }

    public static enum Action {

        ADD_PLAYER((p_251116_, p_251884_) -> {
            GameProfile $$2 = new GameProfile(p_251116_.profileId, p_251884_.readUtf(16));
            $$2.getProperties().putAll(p_251884_.readGameProfileProperties());
            p_251116_.profile = $$2;
        }, (p_252022_, p_250357_) -> {
            p_252022_.writeUtf(p_250357_.profile().getName(), 16);
            p_252022_.writeGameProfileProperties(p_250357_.profile().getProperties());
        }),
        INITIALIZE_CHAT((p_253468_, p_253469_) -> p_253468_.chatSession = p_253469_.readNullable(RemoteChatSession.Data::m_246364_), (p_253470_, p_253471_) -> p_253470_.writeNullable(p_253471_.chatSession, RemoteChatSession.Data::m_247658_)),
        UPDATE_GAME_MODE((p_251118_, p_248955_) -> p_251118_.gameMode = GameType.byId(p_248955_.readVarInt()), (p_249222_, p_250996_) -> p_249222_.writeVarInt(p_250996_.gameMode().getId())),
        UPDATE_LISTED((p_248777_, p_248837_) -> p_248777_.listed = p_248837_.readBoolean(), (p_249355_, p_251658_) -> p_249355_.writeBoolean(p_251658_.listed())),
        UPDATE_LATENCY((p_252263_, p_248964_) -> p_252263_.latency = p_248964_.readVarInt(), (p_248830_, p_251312_) -> p_248830_.writeVarInt(p_251312_.latency())),
        UPDATE_DISPLAY_NAME((p_248840_, p_251000_) -> p_248840_.displayName = p_251000_.readNullable(FriendlyByteBuf::m_130238_), (p_251723_, p_251870_) -> p_251723_.writeNullable(p_251870_.displayName(), FriendlyByteBuf::m_130083_));

        final ClientboundPlayerInfoUpdatePacket.Action.Reader reader;

        final ClientboundPlayerInfoUpdatePacket.Action.Writer writer;

        private Action(ClientboundPlayerInfoUpdatePacket.Action.Reader p_249392_, ClientboundPlayerInfoUpdatePacket.Action.Writer p_250487_) {
            this.reader = p_249392_;
            this.writer = p_250487_;
        }

        public interface Reader {

            void read(ClientboundPlayerInfoUpdatePacket.EntryBuilder var1, FriendlyByteBuf var2);
        }

        public interface Writer {

            void write(FriendlyByteBuf var1, ClientboundPlayerInfoUpdatePacket.Entry var2);
        }
    }

    public static record Entry(UUID f_244142_, GameProfile f_243688_, boolean f_243700_, int f_244322_, GameType f_244162_, @Nullable Component f_244512_, @Nullable RemoteChatSession.Data f_244153_) {

        private final UUID profileId;

        private final GameProfile profile;

        private final boolean listed;

        private final int latency;

        private final GameType gameMode;

        @Nullable
        private final Component displayName;

        @Nullable
        private final RemoteChatSession.Data chatSession;

        Entry(ServerPlayer p_252094_) {
            this(p_252094_.m_20148_(), p_252094_.m_36316_(), true, p_252094_.latency, p_252094_.gameMode.getGameModeForPlayer(), p_252094_.getTabListDisplayName(), Optionull.map(p_252094_.getChatSession(), RemoteChatSession::m_245986_));
        }

        public Entry(UUID f_244142_, GameProfile f_243688_, boolean f_243700_, int f_244322_, GameType f_244162_, @Nullable Component f_244512_, @Nullable RemoteChatSession.Data f_244153_) {
            this.profileId = f_244142_;
            this.profile = f_243688_;
            this.listed = f_243700_;
            this.latency = f_244322_;
            this.gameMode = f_244162_;
            this.displayName = f_244512_;
            this.chatSession = f_244153_;
        }
    }

    static class EntryBuilder {

        final UUID profileId;

        GameProfile profile;

        boolean listed;

        int latency;

        GameType gameMode = GameType.DEFAULT_MODE;

        @Nullable
        Component displayName;

        @Nullable
        RemoteChatSession.Data chatSession;

        EntryBuilder(UUID uUID0) {
            this.profileId = uUID0;
            this.profile = new GameProfile(uUID0, null);
        }

        ClientboundPlayerInfoUpdatePacket.Entry build() {
            return new ClientboundPlayerInfoUpdatePacket.Entry(this.profileId, this.profile, this.listed, this.latency, this.gameMode, this.displayName, this.chatSession);
        }
    }
}