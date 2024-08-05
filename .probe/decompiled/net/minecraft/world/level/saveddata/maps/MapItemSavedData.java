package net.minecraft.world.level.saveddata.maps;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.saveddata.SavedData;
import org.slf4j.Logger;

public class MapItemSavedData extends SavedData {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int MAP_SIZE = 128;

    private static final int HALF_MAP_SIZE = 64;

    public static final int MAX_SCALE = 4;

    public static final int TRACKED_DECORATION_LIMIT = 256;

    public final int centerX;

    public final int centerZ;

    public final ResourceKey<Level> dimension;

    private final boolean trackingPosition;

    private final boolean unlimitedTracking;

    public final byte scale;

    public byte[] colors = new byte[16384];

    public final boolean locked;

    private final List<MapItemSavedData.HoldingPlayer> carriedBy = Lists.newArrayList();

    private final Map<Player, MapItemSavedData.HoldingPlayer> carriedByPlayers = Maps.newHashMap();

    private final Map<String, MapBanner> bannerMarkers = Maps.newHashMap();

    final Map<String, MapDecoration> decorations = Maps.newLinkedHashMap();

    private final Map<String, MapFrame> frameMarkers = Maps.newHashMap();

    private int trackedDecorationCount;

    private MapItemSavedData(int int0, int int1, byte byte2, boolean boolean3, boolean boolean4, boolean boolean5, ResourceKey<Level> resourceKeyLevel6) {
        this.scale = byte2;
        this.centerX = int0;
        this.centerZ = int1;
        this.dimension = resourceKeyLevel6;
        this.trackingPosition = boolean3;
        this.unlimitedTracking = boolean4;
        this.locked = boolean5;
        this.m_77762_();
    }

    public static MapItemSavedData createFresh(double double0, double double1, byte byte2, boolean boolean3, boolean boolean4, ResourceKey<Level> resourceKeyLevel5) {
        int $$6 = 128 * (1 << byte2);
        int $$7 = Mth.floor((double0 + 64.0) / (double) $$6);
        int $$8 = Mth.floor((double1 + 64.0) / (double) $$6);
        int $$9 = $$7 * $$6 + $$6 / 2 - 64;
        int $$10 = $$8 * $$6 + $$6 / 2 - 64;
        return new MapItemSavedData($$9, $$10, byte2, boolean3, boolean4, false, resourceKeyLevel5);
    }

    public static MapItemSavedData createForClient(byte byte0, boolean boolean1, ResourceKey<Level> resourceKeyLevel2) {
        return new MapItemSavedData(0, 0, byte0, false, false, boolean1, resourceKeyLevel2);
    }

    public static MapItemSavedData load(CompoundTag compoundTag0) {
        ResourceKey<Level> $$1 = (ResourceKey<Level>) DimensionType.parseLegacy(new Dynamic(NbtOps.INSTANCE, compoundTag0.get("dimension"))).resultOrPartial(LOGGER::error).orElseThrow(() -> new IllegalArgumentException("Invalid map dimension: " + compoundTag0.get("dimension")));
        int $$2 = compoundTag0.getInt("xCenter");
        int $$3 = compoundTag0.getInt("zCenter");
        byte $$4 = (byte) Mth.clamp(compoundTag0.getByte("scale"), 0, 4);
        boolean $$5 = !compoundTag0.contains("trackingPosition", 1) || compoundTag0.getBoolean("trackingPosition");
        boolean $$6 = compoundTag0.getBoolean("unlimitedTracking");
        boolean $$7 = compoundTag0.getBoolean("locked");
        MapItemSavedData $$8 = new MapItemSavedData($$2, $$3, $$4, $$5, $$6, $$7, $$1);
        byte[] $$9 = compoundTag0.getByteArray("colors");
        if ($$9.length == 16384) {
            $$8.colors = $$9;
        }
        ListTag $$10 = compoundTag0.getList("banners", 10);
        for (int $$11 = 0; $$11 < $$10.size(); $$11++) {
            MapBanner $$12 = MapBanner.load($$10.getCompound($$11));
            $$8.bannerMarkers.put($$12.getId(), $$12);
            $$8.addDecoration($$12.getDecoration(), null, $$12.getId(), (double) $$12.getPos().m_123341_(), (double) $$12.getPos().m_123343_(), 180.0, $$12.getName());
        }
        ListTag $$13 = compoundTag0.getList("frames", 10);
        for (int $$14 = 0; $$14 < $$13.size(); $$14++) {
            MapFrame $$15 = MapFrame.load($$13.getCompound($$14));
            $$8.frameMarkers.put($$15.getId(), $$15);
            $$8.addDecoration(MapDecoration.Type.FRAME, null, "frame-" + $$15.getEntityId(), (double) $$15.getPos().m_123341_(), (double) $$15.getPos().m_123343_(), (double) $$15.getRotation(), null);
        }
        return $$8;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag0) {
        ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, this.dimension.location()).resultOrPartial(LOGGER::error).ifPresent(p_77954_ -> compoundTag0.put("dimension", p_77954_));
        compoundTag0.putInt("xCenter", this.centerX);
        compoundTag0.putInt("zCenter", this.centerZ);
        compoundTag0.putByte("scale", this.scale);
        compoundTag0.putByteArray("colors", this.colors);
        compoundTag0.putBoolean("trackingPosition", this.trackingPosition);
        compoundTag0.putBoolean("unlimitedTracking", this.unlimitedTracking);
        compoundTag0.putBoolean("locked", this.locked);
        ListTag $$1 = new ListTag();
        for (MapBanner $$2 : this.bannerMarkers.values()) {
            $$1.add($$2.save());
        }
        compoundTag0.put("banners", $$1);
        ListTag $$3 = new ListTag();
        for (MapFrame $$4 : this.frameMarkers.values()) {
            $$3.add($$4.save());
        }
        compoundTag0.put("frames", $$3);
        return compoundTag0;
    }

    public MapItemSavedData locked() {
        MapItemSavedData $$0 = new MapItemSavedData(this.centerX, this.centerZ, this.scale, this.trackingPosition, this.unlimitedTracking, true, this.dimension);
        $$0.bannerMarkers.putAll(this.bannerMarkers);
        $$0.decorations.putAll(this.decorations);
        $$0.trackedDecorationCount = this.trackedDecorationCount;
        System.arraycopy(this.colors, 0, $$0.colors, 0, this.colors.length);
        $$0.m_77762_();
        return $$0;
    }

    public MapItemSavedData scaled(int int0) {
        return createFresh((double) this.centerX, (double) this.centerZ, (byte) Mth.clamp(this.scale + int0, 0, 4), this.trackingPosition, this.unlimitedTracking, this.dimension);
    }

    public void tickCarriedBy(Player player0, ItemStack itemStack1) {
        if (!this.carriedByPlayers.containsKey(player0)) {
            MapItemSavedData.HoldingPlayer $$2 = new MapItemSavedData.HoldingPlayer(player0);
            this.carriedByPlayers.put(player0, $$2);
            this.carriedBy.add($$2);
        }
        if (!player0.getInventory().contains(itemStack1)) {
            this.removeDecoration(player0.getName().getString());
        }
        for (int $$3 = 0; $$3 < this.carriedBy.size(); $$3++) {
            MapItemSavedData.HoldingPlayer $$4 = (MapItemSavedData.HoldingPlayer) this.carriedBy.get($$3);
            String $$5 = $$4.player.getName().getString();
            if (!$$4.player.m_213877_() && ($$4.player.getInventory().contains(itemStack1) || itemStack1.isFramed())) {
                if (!itemStack1.isFramed() && $$4.player.m_9236_().dimension() == this.dimension && this.trackingPosition) {
                    this.addDecoration(MapDecoration.Type.PLAYER, $$4.player.m_9236_(), $$5, $$4.player.m_20185_(), $$4.player.m_20189_(), (double) $$4.player.m_146908_(), null);
                }
            } else {
                this.carriedByPlayers.remove($$4.player);
                this.carriedBy.remove($$4);
                this.removeDecoration($$5);
            }
        }
        if (itemStack1.isFramed() && this.trackingPosition) {
            ItemFrame $$6 = itemStack1.getFrame();
            BlockPos $$7 = $$6.m_31748_();
            MapFrame $$8 = (MapFrame) this.frameMarkers.get(MapFrame.frameId($$7));
            if ($$8 != null && $$6.m_19879_() != $$8.getEntityId() && this.frameMarkers.containsKey($$8.getId())) {
                this.removeDecoration("frame-" + $$8.getEntityId());
            }
            MapFrame $$9 = new MapFrame($$7, $$6.m_6350_().get2DDataValue() * 90, $$6.m_19879_());
            this.addDecoration(MapDecoration.Type.FRAME, player0.m_9236_(), "frame-" + $$6.m_19879_(), (double) $$7.m_123341_(), (double) $$7.m_123343_(), (double) ($$6.m_6350_().get2DDataValue() * 90), null);
            this.frameMarkers.put($$9.getId(), $$9);
        }
        CompoundTag $$10 = itemStack1.getTag();
        if ($$10 != null && $$10.contains("Decorations", 9)) {
            ListTag $$11 = $$10.getList("Decorations", 10);
            for (int $$12 = 0; $$12 < $$11.size(); $$12++) {
                CompoundTag $$13 = $$11.getCompound($$12);
                if (!this.decorations.containsKey($$13.getString("id"))) {
                    this.addDecoration(MapDecoration.Type.byIcon($$13.getByte("type")), player0.m_9236_(), $$13.getString("id"), $$13.getDouble("x"), $$13.getDouble("z"), $$13.getDouble("rot"), null);
                }
            }
        }
    }

    private void removeDecoration(String string0) {
        MapDecoration $$1 = (MapDecoration) this.decorations.remove(string0);
        if ($$1 != null && $$1.getType().shouldTrackCount()) {
            this.trackedDecorationCount--;
        }
        this.setDecorationsDirty();
    }

    public static void addTargetDecoration(ItemStack itemStack0, BlockPos blockPos1, String string2, MapDecoration.Type mapDecorationType3) {
        ListTag $$4;
        if (itemStack0.hasTag() && itemStack0.getTag().contains("Decorations", 9)) {
            $$4 = itemStack0.getTag().getList("Decorations", 10);
        } else {
            $$4 = new ListTag();
            itemStack0.addTagElement("Decorations", $$4);
        }
        CompoundTag $$6 = new CompoundTag();
        $$6.putByte("type", mapDecorationType3.getIcon());
        $$6.putString("id", string2);
        $$6.putDouble("x", (double) blockPos1.m_123341_());
        $$6.putDouble("z", (double) blockPos1.m_123343_());
        $$6.putDouble("rot", 180.0);
        $$4.add($$6);
        if (mapDecorationType3.hasMapColor()) {
            CompoundTag $$7 = itemStack0.getOrCreateTagElement("display");
            $$7.putInt("MapColor", mapDecorationType3.getMapColor());
        }
    }

    private void addDecoration(MapDecoration.Type mapDecorationType0, @Nullable LevelAccessor levelAccessor1, String string2, double double3, double double4, double double5, @Nullable Component component6) {
        int $$7 = 1 << this.scale;
        float $$8 = (float) (double3 - (double) this.centerX) / (float) $$7;
        float $$9 = (float) (double4 - (double) this.centerZ) / (float) $$7;
        byte $$10 = (byte) ((int) ((double) ($$8 * 2.0F) + 0.5));
        byte $$11 = (byte) ((int) ((double) ($$9 * 2.0F) + 0.5));
        int $$12 = 63;
        byte $$13;
        if ($$8 >= -63.0F && $$9 >= -63.0F && $$8 <= 63.0F && $$9 <= 63.0F) {
            double5 += double5 < 0.0 ? -8.0 : 8.0;
            $$13 = (byte) ((int) (double5 * 16.0 / 360.0));
            if (this.dimension == Level.NETHER && levelAccessor1 != null) {
                int $$14 = (int) (levelAccessor1.getLevelData().getDayTime() / 10L);
                $$13 = (byte) ($$14 * $$14 * 34187121 + $$14 * 121 >> 15 & 15);
            }
        } else {
            if (mapDecorationType0 != MapDecoration.Type.PLAYER) {
                this.removeDecoration(string2);
                return;
            }
            int $$15 = 320;
            if (Math.abs($$8) < 320.0F && Math.abs($$9) < 320.0F) {
                mapDecorationType0 = MapDecoration.Type.PLAYER_OFF_MAP;
            } else {
                if (!this.unlimitedTracking) {
                    this.removeDecoration(string2);
                    return;
                }
                mapDecorationType0 = MapDecoration.Type.PLAYER_OFF_LIMITS;
            }
            $$13 = 0;
            if ($$8 <= -63.0F) {
                $$10 = -128;
            }
            if ($$9 <= -63.0F) {
                $$11 = -128;
            }
            if ($$8 >= 63.0F) {
                $$10 = 127;
            }
            if ($$9 >= 63.0F) {
                $$11 = 127;
            }
        }
        MapDecoration $$18 = new MapDecoration(mapDecorationType0, $$10, $$11, $$13, component6);
        MapDecoration $$19 = (MapDecoration) this.decorations.put(string2, $$18);
        if (!$$18.equals($$19)) {
            if ($$19 != null && $$19.getType().shouldTrackCount()) {
                this.trackedDecorationCount--;
            }
            if (mapDecorationType0.shouldTrackCount()) {
                this.trackedDecorationCount++;
            }
            this.setDecorationsDirty();
        }
    }

    @Nullable
    public Packet<?> getUpdatePacket(int int0, Player player1) {
        MapItemSavedData.HoldingPlayer $$2 = (MapItemSavedData.HoldingPlayer) this.carriedByPlayers.get(player1);
        return $$2 == null ? null : $$2.nextUpdatePacket(int0);
    }

    private void setColorsDirty(int int0, int int1) {
        this.m_77762_();
        for (MapItemSavedData.HoldingPlayer $$2 : this.carriedBy) {
            $$2.markColorsDirty(int0, int1);
        }
    }

    private void setDecorationsDirty() {
        this.m_77762_();
        this.carriedBy.forEach(MapItemSavedData.HoldingPlayer::m_164820_);
    }

    public MapItemSavedData.HoldingPlayer getHoldingPlayer(Player player0) {
        MapItemSavedData.HoldingPlayer $$1 = (MapItemSavedData.HoldingPlayer) this.carriedByPlayers.get(player0);
        if ($$1 == null) {
            $$1 = new MapItemSavedData.HoldingPlayer(player0);
            this.carriedByPlayers.put(player0, $$1);
            this.carriedBy.add($$1);
        }
        return $$1;
    }

    public boolean toggleBanner(LevelAccessor levelAccessor0, BlockPos blockPos1) {
        double $$2 = (double) blockPos1.m_123341_() + 0.5;
        double $$3 = (double) blockPos1.m_123343_() + 0.5;
        int $$4 = 1 << this.scale;
        double $$5 = ($$2 - (double) this.centerX) / (double) $$4;
        double $$6 = ($$3 - (double) this.centerZ) / (double) $$4;
        int $$7 = 63;
        if ($$5 >= -63.0 && $$6 >= -63.0 && $$5 <= 63.0 && $$6 <= 63.0) {
            MapBanner $$8 = MapBanner.fromWorld(levelAccessor0, blockPos1);
            if ($$8 == null) {
                return false;
            }
            if (this.bannerMarkers.remove($$8.getId(), $$8)) {
                this.removeDecoration($$8.getId());
                return true;
            }
            if (!this.isTrackedCountOverLimit(256)) {
                this.bannerMarkers.put($$8.getId(), $$8);
                this.addDecoration($$8.getDecoration(), levelAccessor0, $$8.getId(), $$2, $$3, 180.0, $$8.getName());
                return true;
            }
        }
        return false;
    }

    public void checkBanners(BlockGetter blockGetter0, int int1, int int2) {
        Iterator<MapBanner> $$3 = this.bannerMarkers.values().iterator();
        while ($$3.hasNext()) {
            MapBanner $$4 = (MapBanner) $$3.next();
            if ($$4.getPos().m_123341_() == int1 && $$4.getPos().m_123343_() == int2) {
                MapBanner $$5 = MapBanner.fromWorld(blockGetter0, $$4.getPos());
                if (!$$4.equals($$5)) {
                    $$3.remove();
                    this.removeDecoration($$4.getId());
                }
            }
        }
    }

    public Collection<MapBanner> getBanners() {
        return this.bannerMarkers.values();
    }

    public void removedFromFrame(BlockPos blockPos0, int int1) {
        this.removeDecoration("frame-" + int1);
        this.frameMarkers.remove(MapFrame.frameId(blockPos0));
    }

    public boolean updateColor(int int0, int int1, byte byte2) {
        byte $$3 = this.colors[int0 + int1 * 128];
        if ($$3 != byte2) {
            this.setColor(int0, int1, byte2);
            return true;
        } else {
            return false;
        }
    }

    public void setColor(int int0, int int1, byte byte2) {
        this.colors[int0 + int1 * 128] = byte2;
        this.setColorsDirty(int0, int1);
    }

    public boolean isExplorationMap() {
        for (MapDecoration $$0 : this.decorations.values()) {
            if ($$0.getType() == MapDecoration.Type.MANSION || $$0.getType() == MapDecoration.Type.MONUMENT) {
                return true;
            }
        }
        return false;
    }

    public void addClientSideDecorations(List<MapDecoration> listMapDecoration0) {
        this.decorations.clear();
        this.trackedDecorationCount = 0;
        for (int $$1 = 0; $$1 < listMapDecoration0.size(); $$1++) {
            MapDecoration $$2 = (MapDecoration) listMapDecoration0.get($$1);
            this.decorations.put("icon-" + $$1, $$2);
            if ($$2.getType().shouldTrackCount()) {
                this.trackedDecorationCount++;
            }
        }
    }

    public Iterable<MapDecoration> getDecorations() {
        return this.decorations.values();
    }

    public boolean isTrackedCountOverLimit(int int0) {
        return this.trackedDecorationCount >= int0;
    }

    public class HoldingPlayer {

        public final Player player;

        private boolean dirtyData = true;

        private int minDirtyX;

        private int minDirtyY;

        private int maxDirtyX = 127;

        private int maxDirtyY = 127;

        private boolean dirtyDecorations = true;

        private int tick;

        public int step;

        HoldingPlayer(Player player0) {
            this.player = player0;
        }

        private MapItemSavedData.MapPatch createPatch() {
            int $$0 = this.minDirtyX;
            int $$1 = this.minDirtyY;
            int $$2 = this.maxDirtyX + 1 - this.minDirtyX;
            int $$3 = this.maxDirtyY + 1 - this.minDirtyY;
            byte[] $$4 = new byte[$$2 * $$3];
            for (int $$5 = 0; $$5 < $$2; $$5++) {
                for (int $$6 = 0; $$6 < $$3; $$6++) {
                    $$4[$$5 + $$6 * $$2] = MapItemSavedData.this.colors[$$0 + $$5 + ($$1 + $$6) * 128];
                }
            }
            return new MapItemSavedData.MapPatch($$0, $$1, $$2, $$3, $$4);
        }

        @Nullable
        Packet<?> nextUpdatePacket(int int0) {
            MapItemSavedData.MapPatch $$1;
            if (this.dirtyData) {
                this.dirtyData = false;
                $$1 = this.createPatch();
            } else {
                $$1 = null;
            }
            Collection<MapDecoration> $$3;
            if (this.dirtyDecorations && this.tick++ % 5 == 0) {
                this.dirtyDecorations = false;
                $$3 = MapItemSavedData.this.decorations.values();
            } else {
                $$3 = null;
            }
            return $$3 == null && $$1 == null ? null : new ClientboundMapItemDataPacket(int0, MapItemSavedData.this.scale, MapItemSavedData.this.locked, $$3, $$1);
        }

        void markColorsDirty(int int0, int int1) {
            if (this.dirtyData) {
                this.minDirtyX = Math.min(this.minDirtyX, int0);
                this.minDirtyY = Math.min(this.minDirtyY, int1);
                this.maxDirtyX = Math.max(this.maxDirtyX, int0);
                this.maxDirtyY = Math.max(this.maxDirtyY, int1);
            } else {
                this.dirtyData = true;
                this.minDirtyX = int0;
                this.minDirtyY = int1;
                this.maxDirtyX = int0;
                this.maxDirtyY = int1;
            }
        }

        private void markDecorationsDirty() {
            this.dirtyDecorations = true;
        }
    }

    public static class MapPatch {

        public final int startX;

        public final int startY;

        public final int width;

        public final int height;

        public final byte[] mapColors;

        public MapPatch(int int0, int int1, int int2, int int3, byte[] byte4) {
            this.startX = int0;
            this.startY = int1;
            this.width = int2;
            this.height = int3;
            this.mapColors = byte4;
        }

        public void applyToMap(MapItemSavedData mapItemSavedData0) {
            for (int $$1 = 0; $$1 < this.width; $$1++) {
                for (int $$2 = 0; $$2 < this.height; $$2++) {
                    mapItemSavedData0.setColor(this.startX + $$1, this.startY + $$2, this.mapColors[$$1 + $$2 * this.width]);
                }
            }
        }
    }
}