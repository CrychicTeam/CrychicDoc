package net.minecraft.network.protocol.game;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class ClientboundMapItemDataPacket implements Packet<ClientGamePacketListener> {

    private final int mapId;

    private final byte scale;

    private final boolean locked;

    @Nullable
    private final List<MapDecoration> decorations;

    @Nullable
    private final MapItemSavedData.MapPatch colorPatch;

    public ClientboundMapItemDataPacket(int int0, byte byte1, boolean boolean2, @Nullable Collection<MapDecoration> collectionMapDecoration3, @Nullable MapItemSavedData.MapPatch mapItemSavedDataMapPatch4) {
        this.mapId = int0;
        this.scale = byte1;
        this.locked = boolean2;
        this.decorations = collectionMapDecoration3 != null ? Lists.newArrayList(collectionMapDecoration3) : null;
        this.colorPatch = mapItemSavedDataMapPatch4;
    }

    public ClientboundMapItemDataPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.mapId = friendlyByteBuf0.readVarInt();
        this.scale = friendlyByteBuf0.readByte();
        this.locked = friendlyByteBuf0.readBoolean();
        this.decorations = friendlyByteBuf0.readNullable(p_237731_ -> p_237731_.readList(p_178981_ -> {
            MapDecoration.Type $$1x = p_178981_.readEnum(MapDecoration.Type.class);
            byte $$2x = p_178981_.readByte();
            byte $$3x = p_178981_.readByte();
            byte $$4x = (byte) (p_178981_.readByte() & 15);
            Component $$5x = p_178981_.readNullable(FriendlyByteBuf::m_130238_);
            return new MapDecoration($$1x, $$2x, $$3x, $$4x, $$5x);
        }));
        int $$1 = friendlyByteBuf0.readUnsignedByte();
        if ($$1 > 0) {
            int $$2 = friendlyByteBuf0.readUnsignedByte();
            int $$3 = friendlyByteBuf0.readUnsignedByte();
            int $$4 = friendlyByteBuf0.readUnsignedByte();
            byte[] $$5 = friendlyByteBuf0.readByteArray();
            this.colorPatch = new MapItemSavedData.MapPatch($$3, $$4, $$1, $$2, $$5);
        } else {
            this.colorPatch = null;
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.mapId);
        friendlyByteBuf0.writeByte(this.scale);
        friendlyByteBuf0.writeBoolean(this.locked);
        friendlyByteBuf0.writeNullable(this.decorations, (p_237728_, p_237729_) -> p_237728_.writeCollection(p_237729_, (p_237725_, p_237726_) -> {
            p_237725_.writeEnum(p_237726_.getType());
            p_237725_.writeByte(p_237726_.getX());
            p_237725_.writeByte(p_237726_.getY());
            p_237725_.writeByte(p_237726_.getRot() & 15);
            p_237725_.writeNullable(p_237726_.getName(), FriendlyByteBuf::m_130083_);
        }));
        if (this.colorPatch != null) {
            friendlyByteBuf0.writeByte(this.colorPatch.width);
            friendlyByteBuf0.writeByte(this.colorPatch.height);
            friendlyByteBuf0.writeByte(this.colorPatch.startX);
            friendlyByteBuf0.writeByte(this.colorPatch.startY);
            friendlyByteBuf0.writeByteArray(this.colorPatch.mapColors);
        } else {
            friendlyByteBuf0.writeByte(0);
        }
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleMapItemData(this);
    }

    public int getMapId() {
        return this.mapId;
    }

    public void applyToMap(MapItemSavedData mapItemSavedData0) {
        if (this.decorations != null) {
            mapItemSavedData0.addClientSideDecorations(this.decorations);
        }
        if (this.colorPatch != null) {
            this.colorPatch.applyToMap(mapItemSavedData0);
        }
    }

    public byte getScale() {
        return this.scale;
    }

    public boolean isLocked() {
        return this.locked;
    }
}