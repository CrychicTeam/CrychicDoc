package net.minecraft.network.protocol.game;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.scores.PlayerTeam;

public class ClientboundSetPlayerTeamPacket implements Packet<ClientGamePacketListener> {

    private static final int METHOD_ADD = 0;

    private static final int METHOD_REMOVE = 1;

    private static final int METHOD_CHANGE = 2;

    private static final int METHOD_JOIN = 3;

    private static final int METHOD_LEAVE = 4;

    private static final int MAX_VISIBILITY_LENGTH = 40;

    private static final int MAX_COLLISION_LENGTH = 40;

    private final int method;

    private final String name;

    private final Collection<String> players;

    private final Optional<ClientboundSetPlayerTeamPacket.Parameters> parameters;

    private ClientboundSetPlayerTeamPacket(String string0, int int1, Optional<ClientboundSetPlayerTeamPacket.Parameters> optionalClientboundSetPlayerTeamPacketParameters2, Collection<String> collectionString3) {
        this.name = string0;
        this.method = int1;
        this.parameters = optionalClientboundSetPlayerTeamPacketParameters2;
        this.players = ImmutableList.copyOf(collectionString3);
    }

    public static ClientboundSetPlayerTeamPacket createAddOrModifyPacket(PlayerTeam playerTeam0, boolean boolean1) {
        return new ClientboundSetPlayerTeamPacket(playerTeam0.getName(), boolean1 ? 0 : 2, Optional.of(new ClientboundSetPlayerTeamPacket.Parameters(playerTeam0)), (Collection<String>) (boolean1 ? playerTeam0.getPlayers() : ImmutableList.of()));
    }

    public static ClientboundSetPlayerTeamPacket createRemovePacket(PlayerTeam playerTeam0) {
        return new ClientboundSetPlayerTeamPacket(playerTeam0.getName(), 1, Optional.empty(), ImmutableList.of());
    }

    public static ClientboundSetPlayerTeamPacket createPlayerPacket(PlayerTeam playerTeam0, String string1, ClientboundSetPlayerTeamPacket.Action clientboundSetPlayerTeamPacketAction2) {
        return new ClientboundSetPlayerTeamPacket(playerTeam0.getName(), clientboundSetPlayerTeamPacketAction2 == ClientboundSetPlayerTeamPacket.Action.ADD ? 3 : 4, Optional.empty(), ImmutableList.of(string1));
    }

    public ClientboundSetPlayerTeamPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.name = friendlyByteBuf0.readUtf();
        this.method = friendlyByteBuf0.readByte();
        if (shouldHaveParameters(this.method)) {
            this.parameters = Optional.of(new ClientboundSetPlayerTeamPacket.Parameters(friendlyByteBuf0));
        } else {
            this.parameters = Optional.empty();
        }
        if (shouldHavePlayerList(this.method)) {
            this.players = friendlyByteBuf0.<String>readList(FriendlyByteBuf::m_130277_);
        } else {
            this.players = ImmutableList.of();
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeUtf(this.name);
        friendlyByteBuf0.writeByte(this.method);
        if (shouldHaveParameters(this.method)) {
            ((ClientboundSetPlayerTeamPacket.Parameters) this.parameters.orElseThrow(() -> new IllegalStateException("Parameters not present, but method is" + this.method))).write(friendlyByteBuf0);
        }
        if (shouldHavePlayerList(this.method)) {
            friendlyByteBuf0.writeCollection(this.players, FriendlyByteBuf::m_130070_);
        }
    }

    private static boolean shouldHavePlayerList(int int0) {
        return int0 == 0 || int0 == 3 || int0 == 4;
    }

    private static boolean shouldHaveParameters(int int0) {
        return int0 == 0 || int0 == 2;
    }

    @Nullable
    public ClientboundSetPlayerTeamPacket.Action getPlayerAction() {
        switch(this.method) {
            case 0:
            case 3:
                return ClientboundSetPlayerTeamPacket.Action.ADD;
            case 1:
            case 2:
            default:
                return null;
            case 4:
                return ClientboundSetPlayerTeamPacket.Action.REMOVE;
        }
    }

    @Nullable
    public ClientboundSetPlayerTeamPacket.Action getTeamAction() {
        switch(this.method) {
            case 0:
                return ClientboundSetPlayerTeamPacket.Action.ADD;
            case 1:
                return ClientboundSetPlayerTeamPacket.Action.REMOVE;
            default:
                return null;
        }
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetPlayerTeamPacket(this);
    }

    public String getName() {
        return this.name;
    }

    public Collection<String> getPlayers() {
        return this.players;
    }

    public Optional<ClientboundSetPlayerTeamPacket.Parameters> getParameters() {
        return this.parameters;
    }

    public static enum Action {

        ADD, REMOVE
    }

    public static class Parameters {

        private final Component displayName;

        private final Component playerPrefix;

        private final Component playerSuffix;

        private final String nametagVisibility;

        private final String collisionRule;

        private final ChatFormatting color;

        private final int options;

        public Parameters(PlayerTeam playerTeam0) {
            this.displayName = playerTeam0.getDisplayName();
            this.options = playerTeam0.packOptions();
            this.nametagVisibility = playerTeam0.getNameTagVisibility().name;
            this.collisionRule = playerTeam0.getCollisionRule().name;
            this.color = playerTeam0.getColor();
            this.playerPrefix = playerTeam0.getPlayerPrefix();
            this.playerSuffix = playerTeam0.getPlayerSuffix();
        }

        public Parameters(FriendlyByteBuf friendlyByteBuf0) {
            this.displayName = friendlyByteBuf0.readComponent();
            this.options = friendlyByteBuf0.readByte();
            this.nametagVisibility = friendlyByteBuf0.readUtf(40);
            this.collisionRule = friendlyByteBuf0.readUtf(40);
            this.color = friendlyByteBuf0.readEnum(ChatFormatting.class);
            this.playerPrefix = friendlyByteBuf0.readComponent();
            this.playerSuffix = friendlyByteBuf0.readComponent();
        }

        public Component getDisplayName() {
            return this.displayName;
        }

        public int getOptions() {
            return this.options;
        }

        public ChatFormatting getColor() {
            return this.color;
        }

        public String getNametagVisibility() {
            return this.nametagVisibility;
        }

        public String getCollisionRule() {
            return this.collisionRule;
        }

        public Component getPlayerPrefix() {
            return this.playerPrefix;
        }

        public Component getPlayerSuffix() {
            return this.playerSuffix;
        }

        public void write(FriendlyByteBuf friendlyByteBuf0) {
            friendlyByteBuf0.writeComponent(this.displayName);
            friendlyByteBuf0.writeByte(this.options);
            friendlyByteBuf0.writeUtf(this.nametagVisibility);
            friendlyByteBuf0.writeUtf(this.collisionRule);
            friendlyByteBuf0.writeEnum(this.color);
            friendlyByteBuf0.writeComponent(this.playerPrefix);
            friendlyByteBuf0.writeComponent(this.playerSuffix);
        }
    }
}