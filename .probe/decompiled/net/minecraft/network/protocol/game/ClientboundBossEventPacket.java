package net.minecraft.network.protocol.game;

import java.util.UUID;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.BossEvent;

public class ClientboundBossEventPacket implements Packet<ClientGamePacketListener> {

    private static final int FLAG_DARKEN = 1;

    private static final int FLAG_MUSIC = 2;

    private static final int FLAG_FOG = 4;

    private final UUID id;

    private final ClientboundBossEventPacket.Operation operation;

    static final ClientboundBossEventPacket.Operation REMOVE_OPERATION = new ClientboundBossEventPacket.Operation() {

        @Override
        public ClientboundBossEventPacket.OperationType getType() {
            return ClientboundBossEventPacket.OperationType.REMOVE;
        }

        @Override
        public void dispatch(UUID p_178660_, ClientboundBossEventPacket.Handler p_178661_) {
            p_178661_.remove(p_178660_);
        }

        @Override
        public void write(FriendlyByteBuf p_178663_) {
        }
    };

    private ClientboundBossEventPacket(UUID uUID0, ClientboundBossEventPacket.Operation clientboundBossEventPacketOperation1) {
        this.id = uUID0;
        this.operation = clientboundBossEventPacketOperation1;
    }

    public ClientboundBossEventPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.id = friendlyByteBuf0.readUUID();
        ClientboundBossEventPacket.OperationType $$1 = friendlyByteBuf0.readEnum(ClientboundBossEventPacket.OperationType.class);
        this.operation = (ClientboundBossEventPacket.Operation) $$1.reader.apply(friendlyByteBuf0);
    }

    public static ClientboundBossEventPacket createAddPacket(BossEvent bossEvent0) {
        return new ClientboundBossEventPacket(bossEvent0.getId(), new ClientboundBossEventPacket.AddOperation(bossEvent0));
    }

    public static ClientboundBossEventPacket createRemovePacket(UUID uUID0) {
        return new ClientboundBossEventPacket(uUID0, REMOVE_OPERATION);
    }

    public static ClientboundBossEventPacket createUpdateProgressPacket(BossEvent bossEvent0) {
        return new ClientboundBossEventPacket(bossEvent0.getId(), new ClientboundBossEventPacket.UpdateProgressOperation(bossEvent0.getProgress()));
    }

    public static ClientboundBossEventPacket createUpdateNamePacket(BossEvent bossEvent0) {
        return new ClientboundBossEventPacket(bossEvent0.getId(), new ClientboundBossEventPacket.UpdateNameOperation(bossEvent0.getName()));
    }

    public static ClientboundBossEventPacket createUpdateStylePacket(BossEvent bossEvent0) {
        return new ClientboundBossEventPacket(bossEvent0.getId(), new ClientboundBossEventPacket.UpdateStyleOperation(bossEvent0.getColor(), bossEvent0.getOverlay()));
    }

    public static ClientboundBossEventPacket createUpdatePropertiesPacket(BossEvent bossEvent0) {
        return new ClientboundBossEventPacket(bossEvent0.getId(), new ClientboundBossEventPacket.UpdatePropertiesOperation(bossEvent0.shouldDarkenScreen(), bossEvent0.shouldPlayBossMusic(), bossEvent0.shouldCreateWorldFog()));
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeUUID(this.id);
        friendlyByteBuf0.writeEnum(this.operation.getType());
        this.operation.write(friendlyByteBuf0);
    }

    static int encodeProperties(boolean boolean0, boolean boolean1, boolean boolean2) {
        int $$3 = 0;
        if (boolean0) {
            $$3 |= 1;
        }
        if (boolean1) {
            $$3 |= 2;
        }
        if (boolean2) {
            $$3 |= 4;
        }
        return $$3;
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleBossUpdate(this);
    }

    public void dispatch(ClientboundBossEventPacket.Handler clientboundBossEventPacketHandler0) {
        this.operation.dispatch(this.id, clientboundBossEventPacketHandler0);
    }

    static class AddOperation implements ClientboundBossEventPacket.Operation {

        private final Component name;

        private final float progress;

        private final BossEvent.BossBarColor color;

        private final BossEvent.BossBarOverlay overlay;

        private final boolean darkenScreen;

        private final boolean playMusic;

        private final boolean createWorldFog;

        AddOperation(BossEvent bossEvent0) {
            this.name = bossEvent0.getName();
            this.progress = bossEvent0.getProgress();
            this.color = bossEvent0.getColor();
            this.overlay = bossEvent0.getOverlay();
            this.darkenScreen = bossEvent0.shouldDarkenScreen();
            this.playMusic = bossEvent0.shouldPlayBossMusic();
            this.createWorldFog = bossEvent0.shouldCreateWorldFog();
        }

        private AddOperation(FriendlyByteBuf friendlyByteBuf0) {
            this.name = friendlyByteBuf0.readComponent();
            this.progress = friendlyByteBuf0.readFloat();
            this.color = friendlyByteBuf0.readEnum(BossEvent.BossBarColor.class);
            this.overlay = friendlyByteBuf0.readEnum(BossEvent.BossBarOverlay.class);
            int $$1 = friendlyByteBuf0.readUnsignedByte();
            this.darkenScreen = ($$1 & 1) > 0;
            this.playMusic = ($$1 & 2) > 0;
            this.createWorldFog = ($$1 & 4) > 0;
        }

        @Override
        public ClientboundBossEventPacket.OperationType getType() {
            return ClientboundBossEventPacket.OperationType.ADD;
        }

        @Override
        public void dispatch(UUID uUID0, ClientboundBossEventPacket.Handler clientboundBossEventPacketHandler1) {
            clientboundBossEventPacketHandler1.add(uUID0, this.name, this.progress, this.color, this.overlay, this.darkenScreen, this.playMusic, this.createWorldFog);
        }

        @Override
        public void write(FriendlyByteBuf friendlyByteBuf0) {
            friendlyByteBuf0.writeComponent(this.name);
            friendlyByteBuf0.writeFloat(this.progress);
            friendlyByteBuf0.writeEnum(this.color);
            friendlyByteBuf0.writeEnum(this.overlay);
            friendlyByteBuf0.writeByte(ClientboundBossEventPacket.encodeProperties(this.darkenScreen, this.playMusic, this.createWorldFog));
        }
    }

    public interface Handler {

        default void add(UUID uUID0, Component component1, float float2, BossEvent.BossBarColor bossEventBossBarColor3, BossEvent.BossBarOverlay bossEventBossBarOverlay4, boolean boolean5, boolean boolean6, boolean boolean7) {
        }

        default void remove(UUID uUID0) {
        }

        default void updateProgress(UUID uUID0, float float1) {
        }

        default void updateName(UUID uUID0, Component component1) {
        }

        default void updateStyle(UUID uUID0, BossEvent.BossBarColor bossEventBossBarColor1, BossEvent.BossBarOverlay bossEventBossBarOverlay2) {
        }

        default void updateProperties(UUID uUID0, boolean boolean1, boolean boolean2, boolean boolean3) {
        }
    }

    interface Operation {

        ClientboundBossEventPacket.OperationType getType();

        void dispatch(UUID var1, ClientboundBossEventPacket.Handler var2);

        void write(FriendlyByteBuf var1);
    }

    static enum OperationType {

        ADD(ClientboundBossEventPacket.AddOperation::new),
        REMOVE(p_178719_ -> ClientboundBossEventPacket.REMOVE_OPERATION),
        UPDATE_PROGRESS(ClientboundBossEventPacket.UpdateProgressOperation::new),
        UPDATE_NAME(ClientboundBossEventPacket.UpdateNameOperation::new),
        UPDATE_STYLE(ClientboundBossEventPacket.UpdateStyleOperation::new),
        UPDATE_PROPERTIES(ClientboundBossEventPacket.UpdatePropertiesOperation::new);

        final Function<FriendlyByteBuf, ClientboundBossEventPacket.Operation> reader;

        private OperationType(Function<FriendlyByteBuf, ClientboundBossEventPacket.Operation> p_178716_) {
            this.reader = p_178716_;
        }
    }

    static class UpdateNameOperation implements ClientboundBossEventPacket.Operation {

        private final Component name;

        UpdateNameOperation(Component component0) {
            this.name = component0;
        }

        private UpdateNameOperation(FriendlyByteBuf friendlyByteBuf0) {
            this.name = friendlyByteBuf0.readComponent();
        }

        @Override
        public ClientboundBossEventPacket.OperationType getType() {
            return ClientboundBossEventPacket.OperationType.UPDATE_NAME;
        }

        @Override
        public void dispatch(UUID uUID0, ClientboundBossEventPacket.Handler clientboundBossEventPacketHandler1) {
            clientboundBossEventPacketHandler1.updateName(uUID0, this.name);
        }

        @Override
        public void write(FriendlyByteBuf friendlyByteBuf0) {
            friendlyByteBuf0.writeComponent(this.name);
        }
    }

    static class UpdateProgressOperation implements ClientboundBossEventPacket.Operation {

        private final float progress;

        UpdateProgressOperation(float float0) {
            this.progress = float0;
        }

        private UpdateProgressOperation(FriendlyByteBuf friendlyByteBuf0) {
            this.progress = friendlyByteBuf0.readFloat();
        }

        @Override
        public ClientboundBossEventPacket.OperationType getType() {
            return ClientboundBossEventPacket.OperationType.UPDATE_PROGRESS;
        }

        @Override
        public void dispatch(UUID uUID0, ClientboundBossEventPacket.Handler clientboundBossEventPacketHandler1) {
            clientboundBossEventPacketHandler1.updateProgress(uUID0, this.progress);
        }

        @Override
        public void write(FriendlyByteBuf friendlyByteBuf0) {
            friendlyByteBuf0.writeFloat(this.progress);
        }
    }

    static class UpdatePropertiesOperation implements ClientboundBossEventPacket.Operation {

        private final boolean darkenScreen;

        private final boolean playMusic;

        private final boolean createWorldFog;

        UpdatePropertiesOperation(boolean boolean0, boolean boolean1, boolean boolean2) {
            this.darkenScreen = boolean0;
            this.playMusic = boolean1;
            this.createWorldFog = boolean2;
        }

        private UpdatePropertiesOperation(FriendlyByteBuf friendlyByteBuf0) {
            int $$1 = friendlyByteBuf0.readUnsignedByte();
            this.darkenScreen = ($$1 & 1) > 0;
            this.playMusic = ($$1 & 2) > 0;
            this.createWorldFog = ($$1 & 4) > 0;
        }

        @Override
        public ClientboundBossEventPacket.OperationType getType() {
            return ClientboundBossEventPacket.OperationType.UPDATE_PROPERTIES;
        }

        @Override
        public void dispatch(UUID uUID0, ClientboundBossEventPacket.Handler clientboundBossEventPacketHandler1) {
            clientboundBossEventPacketHandler1.updateProperties(uUID0, this.darkenScreen, this.playMusic, this.createWorldFog);
        }

        @Override
        public void write(FriendlyByteBuf friendlyByteBuf0) {
            friendlyByteBuf0.writeByte(ClientboundBossEventPacket.encodeProperties(this.darkenScreen, this.playMusic, this.createWorldFog));
        }
    }

    static class UpdateStyleOperation implements ClientboundBossEventPacket.Operation {

        private final BossEvent.BossBarColor color;

        private final BossEvent.BossBarOverlay overlay;

        UpdateStyleOperation(BossEvent.BossBarColor bossEventBossBarColor0, BossEvent.BossBarOverlay bossEventBossBarOverlay1) {
            this.color = bossEventBossBarColor0;
            this.overlay = bossEventBossBarOverlay1;
        }

        private UpdateStyleOperation(FriendlyByteBuf friendlyByteBuf0) {
            this.color = friendlyByteBuf0.readEnum(BossEvent.BossBarColor.class);
            this.overlay = friendlyByteBuf0.readEnum(BossEvent.BossBarOverlay.class);
        }

        @Override
        public ClientboundBossEventPacket.OperationType getType() {
            return ClientboundBossEventPacket.OperationType.UPDATE_STYLE;
        }

        @Override
        public void dispatch(UUID uUID0, ClientboundBossEventPacket.Handler clientboundBossEventPacketHandler1) {
            clientboundBossEventPacketHandler1.updateStyle(uUID0, this.color, this.overlay);
        }

        @Override
        public void write(FriendlyByteBuf friendlyByteBuf0) {
            friendlyByteBuf0.writeEnum(this.color);
            friendlyByteBuf0.writeEnum(this.overlay);
        }
    }
}