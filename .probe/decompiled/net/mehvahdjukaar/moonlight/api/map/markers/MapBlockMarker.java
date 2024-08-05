package net.mehvahdjukaar.moonlight.api.map.markers;

import java.util.Objects;
import net.mehvahdjukaar.moonlight.api.map.CustomMapDecoration;
import net.mehvahdjukaar.moonlight.api.map.type.MapDecorationType;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MapBlockMarker<D extends CustomMapDecoration> {

    protected final MapDecorationType<D, ?> type;

    @Nullable
    private BlockPos pos;

    private int rot = 0;

    @Nullable
    private Component name;

    private boolean persistent;

    public static final int HAS_SMALL_TEXTURE_FLAG = 1;

    protected MapBlockMarker(MapDecorationType<D, ?> type) {
        this.type = type;
    }

    public void loadFromNBT(CompoundTag compound) {
        this.pos = NbtUtils.readBlockPos(compound.getCompound("Pos"));
        this.name = compound.contains("Name") ? Component.Serializer.fromJson(compound.getString("Name")) : null;
        this.persistent = compound.getBoolean("Persistent");
    }

    public CompoundTag saveToNBT() {
        CompoundTag compound = new CompoundTag();
        return this.saveToNBT(compound);
    }

    @Deprecated(forRemoval = true)
    public CompoundTag saveToNBT(CompoundTag compound) {
        if (this.pos != null) {
            compound.put("Pos", NbtUtils.writeBlockPos(this.pos));
        }
        if (this.name != null) {
            compound.putString("Name", Component.Serializer.toJson(this.name));
        }
        if (this.persistent) {
            compound.putBoolean("Persistent", true);
        }
        return compound;
    }

    public boolean shouldRefresh() {
        return this.persistent ? false : this.type.isFromWorld();
    }

    public boolean shouldSave() {
        return this.persistent || this.type.isFromWorld();
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            MapBlockMarker<?> that = (MapBlockMarker<?>) o;
            return Objects.equals(this.type, that.type) && Objects.equals(this.pos, that.pos) && Objects.equals(this.name, that.name);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.type, this.pos, this.name });
    }

    private String getPosSuffix() {
        return this.pos == null ? "" : this.pos.m_123341_() + "," + this.pos.m_123342_() + "," + this.pos.m_123343_();
    }

    public MapDecorationType<D, ?> getType() {
        return this.type;
    }

    public String getTypeId() {
        return Utils.getID(this.type).toString();
    }

    public String getMarkerId() {
        return this.getTypeId() + "-" + this.getPosSuffix();
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public void setRotation(int rot) {
        this.rot = rot;
    }

    public float getRotation() {
        return (float) this.rot;
    }

    public Component getName() {
        return this.name;
    }

    public void setName(Component name) {
        this.name = name;
    }

    @NotNull
    protected abstract D doCreateDecoration(byte var1, byte var2, byte var3);

    @Nullable
    public D createDecorationFromMarker(MapItemSavedData data) {
        BlockPos pos = this.getPos();
        if (pos == null) {
            return null;
        } else {
            double worldX = (double) pos.m_123341_();
            double worldZ = (double) pos.m_123343_();
            double rotation = (double) this.getRotation();
            int i = 1 << data.scale;
            float f = (float) (worldX - (double) data.centerX) / (float) i;
            float f1 = (float) (worldZ - (double) data.centerZ) / (float) i;
            byte mapX = (byte) ((int) ((double) (f * 2.0F) + 0.5));
            byte mapY = (byte) ((int) ((double) (f1 * 2.0F) + 0.5));
            if (f >= -64.0F && f1 >= -64.0F && f <= 64.0F && f1 <= 64.0F) {
                rotation += rotation < 0.0 ? -8.0 : 8.0;
                byte rot = (byte) ((int) (rotation * 16.0 / 360.0));
                return this.doCreateDecoration(mapX, mapY, rot);
            } else {
                return null;
            }
        }
    }

    public int getFlags() {
        return 0;
    }

    public boolean hasFlag(int flag) {
        return (this.getFlags() & flag) != 0;
    }
}