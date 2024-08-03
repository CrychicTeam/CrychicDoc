package net.mehvahdjukaar.supplementaries.common.misc;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Objects;
import net.mehvahdjukaar.moonlight.api.map.CustomMapData;
import net.mehvahdjukaar.moonlight.api.map.MapDataRegistry;
import net.mehvahdjukaar.moonlight.api.util.math.colors.RGBColor;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.network.ClientBoundSyncAmbientLightPacket;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class MapLightHandler {

    private static boolean enabled = false;

    public static final CustomMapData.Type<MapLightHandler.LightData> LIGHT_DATA = MapDataRegistry.registerCustomMapSavedData(Supplementaries.res("light_data"), MapLightHandler.LightData::new);

    private static final Object2IntMap<ResourceKey<Level>> LIGHT_PER_WORLD = new Object2IntArrayMap();

    @Nullable
    private static Object lightMap = null;

    public static void init() {
    }

    public static void setActive(boolean on) {
        enabled = on;
    }

    public static MapLightHandler.LightData getLightData(MapItemSavedData data) {
        return LIGHT_DATA.get(data);
    }

    public static boolean isActive() {
        return enabled;
    }

    @OnlyIn(Dist.CLIENT)
    public static void setLightMap(@Nullable NativeImage map) {
        if (map != null) {
            Preconditions.checkArgument(map.getWidth() != 16 || map.getHeight() != 6, "Lightmap must be 16x16");
        }
        lightMap = map;
    }

    @Internal
    public static void setAmbientLight(Object2IntMap<ResourceKey<Level>> ambientLight) {
        LIGHT_PER_WORLD.clear();
        LIGHT_PER_WORLD.putAll(ambientLight);
    }

    @Internal
    public static void sendDataToClient(ServerPlayer player) {
        if (enabled) {
            ModNetwork.CHANNEL.sendToClientPlayer(player, new ClientBoundSyncAmbientLightPacket(player.m_9236_().registryAccess()));
        }
    }

    private static class Counter implements CustomMapData.DirtyCounter {

        private int minDirtyX = 0;

        private int maxDirtyX = 127;

        private int minDirtyZ = 0;

        private int maxDirtyZ = 127;

        private boolean posDirty = true;

        public void markDirty(int x, int z) {
            if (this.posDirty) {
                this.minDirtyX = Math.min(this.minDirtyX, x);
                this.minDirtyZ = Math.min(this.minDirtyZ, z);
                this.maxDirtyX = Math.max(this.maxDirtyX, x);
                this.maxDirtyZ = Math.max(this.maxDirtyZ, z);
            } else {
                this.posDirty = true;
                this.minDirtyX = x;
                this.minDirtyZ = z;
                this.maxDirtyX = x;
                this.maxDirtyZ = z;
            }
        }

        @Override
        public boolean isDirty() {
            return this.posDirty;
        }

        @Override
        public void clearDirty() {
            this.posDirty = false;
            this.minDirtyX = 0;
            this.minDirtyZ = 0;
            this.maxDirtyX = 0;
            this.maxDirtyZ = 0;
        }
    }

    public static class LightData implements CustomMapData<MapLightHandler.Counter> {

        private static final String LIGHTMAP_TAG = "lightmap";

        public static final String MIN_X = "min_x";

        public static final String MAX_X = "max_x";

        public static final String MIN_Z = "min_z";

        private byte[][] data = null;

        private int getEntry(int x, int z) {
            if (this.data == null) {
                return 0;
            } else if (x < 0 || x >= 128 || z < 0 || z >= 128) {
                return 0;
            } else {
                return this.data[x] != null ? Byte.toUnsignedInt(this.data[x][z]) : 0;
            }
        }

        private void addEntry(MapItemSavedData md, int x, int z, int packedLight) {
            if (this.data == null) {
                this.data = new byte[128][];
            }
            if (this.data[x] == null) {
                this.data[x] = new byte[128];
            }
            this.data[x][z] = (byte) packedLight;
            this.setDirty(md, counter -> counter.markDirty(x, z));
        }

        @Override
        public void load(CompoundTag tag) {
            if (tag.contains("lightmap")) {
                CompoundTag t = tag.getCompound("lightmap");
                int minX = 0;
                if (t.contains("min_x")) {
                    minX = t.getInt("min_x");
                }
                int maxX = 127;
                if (t.contains("max_x")) {
                    maxX = t.getInt("max_x");
                }
                int minZ = 0;
                if (t.contains("min_z")) {
                    minZ = t.getInt("min_z");
                }
                for (int x = minX; x <= maxX; x++) {
                    byte[] rowData = t.getByteArray("pos_" + x);
                    if (this.data == null) {
                        this.data = new byte[128][];
                    }
                    if (this.data[x] == null) {
                        this.data[x] = new byte[128];
                    }
                    System.arraycopy(rowData, 0, this.data[x], minZ, rowData.length);
                }
            }
        }

        private void savePatch(CompoundTag tag, int minX, int maxX, int minZ, int maxZ, boolean pos) {
            if (pos && this.data != null) {
                CompoundTag t = new CompoundTag();
                if (minX != 0) {
                    t.putInt("min_x", minX);
                }
                if (maxX != 127) {
                    t.putInt("max_x", maxX);
                }
                if (minZ != 0) {
                    t.putInt("min_z", minZ);
                }
                for (int x = minX; x <= maxX; x++) {
                    if (this.data[x] != null) {
                        byte[] rowData = new byte[maxZ - minZ + 1];
                        System.arraycopy(this.data[x], minZ, rowData, 0, rowData.length);
                        t.putByteArray("pos_" + x, rowData);
                    }
                }
                tag.put("lightmap", t);
            }
        }

        @Override
        public void save(CompoundTag tag) {
            this.savePatch(tag, 0, 127, 0, 127, true);
        }

        public void saveToUpdateTag(CompoundTag tag, MapLightHandler.Counter dc) {
            this.savePatch(tag, dc.minDirtyX, dc.maxDirtyX, dc.minDirtyZ, dc.maxDirtyZ, dc.posDirty);
        }

        @Override
        public void loadUpdateTag(CompoundTag tag) {
            this.load(tag);
        }

        @Override
        public boolean persistOnCopyOrLock() {
            return false;
        }

        @Override
        public CustomMapData.Type<?> getType() {
            return MapLightHandler.LIGHT_DATA;
        }

        public MapLightHandler.Counter createDirtyCounter() {
            return new MapLightHandler.Counter();
        }

        public void setLightLevel(int x, int z, int blockLight, int skyLight, MapItemSavedData data) {
            int packed = blockLight << 4 | 15 - skyLight;
            if (packed != 0) {
                if (!Objects.equals(this.getEntry(x, z), packed)) {
                    this.addEntry(data, x, z, packed);
                }
            } else if (this.data != null && this.data[x] != null && this.data[x][z] != 0) {
                this.data[x][z] = 0;
                this.setDirty(data, counter -> counter.markDirty(x, z));
                for (byte b : this.data[x]) {
                    if (b != 0) {
                        return;
                    }
                }
                this.data[x] = null;
            }
        }

        @OnlyIn(Dist.CLIENT)
        public void processTexture(NativeImage texture, int startX, int startY, ResourceKey<Level> levelKey) {
            if (MapLightHandler.lightMap != null) {
                int minL = MapLightHandler.LIGHT_PER_WORLD.getOrDefault(levelKey, 0);
                for (int x = 0; x < 128; x++) {
                    for (int z = 0; z < 128; z++) {
                        int light = this.getEntry(x, z);
                        int skyDarkness = light & 15;
                        int blockLight = Math.max(minL, light >> 4 & 15);
                        int pX = startX + x;
                        int pY = startY + z;
                        int originalColor = texture.getPixelRGBA(pX, pY);
                        int skyLight = 15 - skyDarkness;
                        RGBColor lightColor = new RGBColor(((NativeImage) MapLightHandler.lightMap).getPixelRGBA(blockLight, skyLight));
                        float intensity = 1.0F;
                        int newColor = new RGBColor(originalColor).multiply(lightColor.red() * intensity, lightColor.green() * intensity, lightColor.green() * intensity, 1.0F).toInt();
                        texture.setPixelRGBA(pX, pY, newColor);
                    }
                }
            }
        }

        public void clear() {
            this.data = null;
        }
    }
}