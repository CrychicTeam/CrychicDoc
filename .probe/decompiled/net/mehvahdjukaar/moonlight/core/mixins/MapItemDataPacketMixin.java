package net.mehvahdjukaar.moonlight.core.mixins;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.EncoderException;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import net.mehvahdjukaar.moonlight.api.map.CustomMapData;
import net.mehvahdjukaar.moonlight.api.map.CustomMapDecoration;
import net.mehvahdjukaar.moonlight.api.map.ExpandedMapData;
import net.mehvahdjukaar.moonlight.api.map.markers.MapBlockMarker;
import net.mehvahdjukaar.moonlight.api.map.type.MapDecorationType;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.mehvahdjukaar.moonlight.core.map.MapDataInternal;
import net.mehvahdjukaar.moonlight.core.misc.IMapDataPacketExtension;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ClientboundMapItemDataPacket.class })
public class MapItemDataPacketMixin implements IMapDataPacketExtension {

    @Shadow
    @Final
    @Nullable
    private MapItemSavedData.MapPatch colorPatch;

    @Shadow
    @Final
    private int mapId;

    @Unique
    private CustomMapDecoration[] moonlight$customDecorations = null;

    @Unique
    private CompoundTag moonlight$customData = null;

    @Unique
    private int moonlight$mapCenterX = 0;

    @Unique
    private int moonlight$mapCenterZ = 0;

    @Unique
    private ResourceLocation moonlight$dimension = Level.OVERWORLD.location();

    @Inject(method = { "<init>(IBZLjava/util/Collection;Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;)V" }, at = { @At("RETURN") })
    private void addExtraCenterAndDimension(int mapId, byte b, boolean bl, Collection collection, MapItemSavedData.MapPatch mapPatch, CallbackInfo ci) {
        ServerLevel level = PlatHelper.getCurrentServer().getLevel(Level.OVERWORLD);
        this.moonlight$dimension = null;
        if (level != null) {
            MapItemSavedData data = Moonlight.getMapDataFromKnownKeys(level, mapId);
            if (data != null) {
                this.moonlight$mapCenterX = data.centerX;
                this.moonlight$mapCenterZ = data.centerZ;
                this.moonlight$dimension = data.dimension.location();
            }
        }
    }

    @Inject(method = { "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V" }, at = { @At("RETURN") })
    private void readExtraData(FriendlyByteBuf buf, CallbackInfo ci) {
        if (buf.readBoolean()) {
            this.moonlight$dimension = buf.readResourceLocation();
            this.moonlight$mapCenterX = buf.readVarInt();
            this.moonlight$mapCenterZ = buf.readVarInt();
        }
        if (buf.readBoolean()) {
            this.moonlight$customDecorations = new CustomMapDecoration[buf.readVarInt()];
            for (int m = 0; m < this.moonlight$customDecorations.length; m++) {
                MapDecorationType<?, ?> type = MapDataInternal.get(buf.readResourceLocation());
                if (type != null) {
                    this.moonlight$customDecorations[m] = type.loadDecorationFromBuffer(buf);
                }
            }
        }
        if (buf.readBoolean()) {
            this.moonlight$customData = buf.readNbt();
        }
    }

    @Inject(method = { "write" }, at = { @At("RETURN") })
    private void writeExtraData(FriendlyByteBuf buf, CallbackInfo ci) {
        buf.writeBoolean(this.moonlight$dimension != null);
        if (this.moonlight$dimension != null) {
            buf.writeResourceLocation(this.moonlight$dimension);
            buf.writeVarInt(this.moonlight$mapCenterX);
            buf.writeVarInt(this.moonlight$mapCenterZ);
        }
        buf.writeBoolean(this.moonlight$customDecorations != null);
        if (this.moonlight$customDecorations != null) {
            buf.writeVarInt(this.moonlight$customDecorations.length);
            for (CustomMapDecoration decoration : this.moonlight$customDecorations) {
                buf.writeResourceLocation(Utils.getID(decoration.getType()));
                decoration.saveToBuffer(buf);
            }
        }
        buf.writeBoolean(this.moonlight$customData != null);
        if (this.moonlight$customData != null) {
            buf.writeNbt(this.moonlight$customData);
        }
    }

    @Override
    public void moonlight$sendCustomDecorations(Collection<CustomMapDecoration> decorations) {
        if (PlatHelper.getPhysicalSide().isClient()) {
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            decorations = decorations.stream().map(e -> {
                e.saveToBuffer(buffer);
                return e.getType().loadDecorationFromBuffer(buffer);
            }).toList();
        }
        this.moonlight$customDecorations = (CustomMapDecoration[]) decorations.toArray(CustomMapDecoration[]::new);
    }

    @Override
    public void moonlight$sendCustomMapDataTag(CompoundTag dataTag) {
        this.moonlight$customData = dataTag;
    }

    @Override
    public CompoundTag moonlight$getCustomMapDataTag() {
        return this.moonlight$customData;
    }

    @Override
    public MapItemSavedData.MapPatch moonlight$getColorPatch() {
        return this.colorPatch;
    }

    @Override
    public ResourceKey<Level> moonlight$getDimension() {
        return ResourceKey.create(Registries.DIMENSION, this.moonlight$dimension);
    }

    @Inject(method = { "applyToMap" }, at = { @At("HEAD") })
    private void handleExtraData(MapItemSavedData mapData, CallbackInfo ci) {
        CustomMapDecoration[] serverDeco = this.moonlight$customDecorations;
        CompoundTag serverData = this.moonlight$customData;
        mapData.centerX = this.moonlight$mapCenterX;
        mapData.centerZ = this.moonlight$mapCenterZ;
        mapData.dimension = this.moonlight$getDimension();
        if (mapData instanceof ExpandedMapData ed) {
            Map<String, CustomMapDecoration> decorations = ed.getCustomDecorations();
            if (serverDeco != null) {
                decorations.clear();
                for (int i = 0; i < serverDeco.length; i++) {
                    CustomMapDecoration customDecoration = serverDeco[i];
                    if (customDecoration != null) {
                        decorations.put("icon-" + i, customDecoration);
                    } else {
                        Moonlight.LOGGER.warn("Failed to load custom map decoration, skipping");
                    }
                }
            }
            if (serverData != null) {
                Map<ResourceLocation, CustomMapData<?>> customData = ed.getCustomData();
                for (CustomMapData<?> v : customData.values()) {
                    v.loadUpdateTag(this.moonlight$customData);
                }
            }
            for (MapBlockMarker<?> m : MapDataInternal.getDynamicClient(this.mapId, mapData)) {
                CustomMapDecoration d = m.createDecorationFromMarker(mapData);
                if (d != null) {
                    decorations.put(m.getMarkerId(), d);
                }
            }
        }
    }

    private static CompoundTag readCompressedNbt(FriendlyByteBuf buf) {
        int i = buf.readerIndex();
        byte b = buf.readByte();
        if (b == 0) {
            throw new EncoderException();
        } else {
            buf.readerIndex(i);
            try {
                return NbtIo.readCompressed(new ByteBufInputStream(buf));
            } catch (IOException var4) {
                throw new EncoderException(var4);
            }
        }
    }

    private static void writeCompressedNbt(FriendlyByteBuf buf, CompoundTag nbt) {
        if (nbt == null) {
            buf.writeByte(0);
        } else {
            try {
                NbtIo.writeCompressed(nbt, new ByteBufOutputStream(buf));
            } catch (IOException var3) {
                throw new EncoderException(var3);
            }
        }
    }
}