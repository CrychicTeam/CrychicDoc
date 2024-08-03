package net.minecraft.network.protocol.game;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.properties.StructureMode;

public class ServerboundSetStructureBlockPacket implements Packet<ServerGamePacketListener> {

    private static final int FLAG_IGNORE_ENTITIES = 1;

    private static final int FLAG_SHOW_AIR = 2;

    private static final int FLAG_SHOW_BOUNDING_BOX = 4;

    private final BlockPos pos;

    private final StructureBlockEntity.UpdateType updateType;

    private final StructureMode mode;

    private final String name;

    private final BlockPos offset;

    private final Vec3i size;

    private final Mirror mirror;

    private final Rotation rotation;

    private final String data;

    private final boolean ignoreEntities;

    private final boolean showAir;

    private final boolean showBoundingBox;

    private final float integrity;

    private final long seed;

    public ServerboundSetStructureBlockPacket(BlockPos blockPos0, StructureBlockEntity.UpdateType structureBlockEntityUpdateType1, StructureMode structureMode2, String string3, BlockPos blockPos4, Vec3i vecI5, Mirror mirror6, Rotation rotation7, String string8, boolean boolean9, boolean boolean10, boolean boolean11, float float12, long long13) {
        this.pos = blockPos0;
        this.updateType = structureBlockEntityUpdateType1;
        this.mode = structureMode2;
        this.name = string3;
        this.offset = blockPos4;
        this.size = vecI5;
        this.mirror = mirror6;
        this.rotation = rotation7;
        this.data = string8;
        this.ignoreEntities = boolean9;
        this.showAir = boolean10;
        this.showBoundingBox = boolean11;
        this.integrity = float12;
        this.seed = long13;
    }

    public ServerboundSetStructureBlockPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.pos = friendlyByteBuf0.readBlockPos();
        this.updateType = friendlyByteBuf0.readEnum(StructureBlockEntity.UpdateType.class);
        this.mode = friendlyByteBuf0.readEnum(StructureMode.class);
        this.name = friendlyByteBuf0.readUtf();
        int $$1 = 48;
        this.offset = new BlockPos(Mth.clamp(friendlyByteBuf0.readByte(), -48, 48), Mth.clamp(friendlyByteBuf0.readByte(), -48, 48), Mth.clamp(friendlyByteBuf0.readByte(), -48, 48));
        int $$2 = 48;
        this.size = new Vec3i(Mth.clamp(friendlyByteBuf0.readByte(), 0, 48), Mth.clamp(friendlyByteBuf0.readByte(), 0, 48), Mth.clamp(friendlyByteBuf0.readByte(), 0, 48));
        this.mirror = friendlyByteBuf0.readEnum(Mirror.class);
        this.rotation = friendlyByteBuf0.readEnum(Rotation.class);
        this.data = friendlyByteBuf0.readUtf(128);
        this.integrity = Mth.clamp(friendlyByteBuf0.readFloat(), 0.0F, 1.0F);
        this.seed = friendlyByteBuf0.readVarLong();
        int $$3 = friendlyByteBuf0.readByte();
        this.ignoreEntities = ($$3 & 1) != 0;
        this.showAir = ($$3 & 2) != 0;
        this.showBoundingBox = ($$3 & 4) != 0;
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeBlockPos(this.pos);
        friendlyByteBuf0.writeEnum(this.updateType);
        friendlyByteBuf0.writeEnum(this.mode);
        friendlyByteBuf0.writeUtf(this.name);
        friendlyByteBuf0.writeByte(this.offset.m_123341_());
        friendlyByteBuf0.writeByte(this.offset.m_123342_());
        friendlyByteBuf0.writeByte(this.offset.m_123343_());
        friendlyByteBuf0.writeByte(this.size.getX());
        friendlyByteBuf0.writeByte(this.size.getY());
        friendlyByteBuf0.writeByte(this.size.getZ());
        friendlyByteBuf0.writeEnum(this.mirror);
        friendlyByteBuf0.writeEnum(this.rotation);
        friendlyByteBuf0.writeUtf(this.data);
        friendlyByteBuf0.writeFloat(this.integrity);
        friendlyByteBuf0.writeVarLong(this.seed);
        int $$1 = 0;
        if (this.ignoreEntities) {
            $$1 |= 1;
        }
        if (this.showAir) {
            $$1 |= 2;
        }
        if (this.showBoundingBox) {
            $$1 |= 4;
        }
        friendlyByteBuf0.writeByte($$1);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleSetStructureBlock(this);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public StructureBlockEntity.UpdateType getUpdateType() {
        return this.updateType;
    }

    public StructureMode getMode() {
        return this.mode;
    }

    public String getName() {
        return this.name;
    }

    public BlockPos getOffset() {
        return this.offset;
    }

    public Vec3i getSize() {
        return this.size;
    }

    public Mirror getMirror() {
        return this.mirror;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public String getData() {
        return this.data;
    }

    public boolean isIgnoreEntities() {
        return this.ignoreEntities;
    }

    public boolean isShowAir() {
        return this.showAir;
    }

    public boolean isShowBoundingBox() {
        return this.showBoundingBox;
    }

    public float getIntegrity() {
        return this.integrity;
    }

    public long getSeed() {
        return this.seed;
    }
}