package net.mehvahdjukaar.supplementaries.common.misc;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.mehvahdjukaar.moonlight.api.map.CustomMapData;
import net.mehvahdjukaar.moonlight.api.map.MapDataRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.api.util.math.ColorUtils;
import net.mehvahdjukaar.moonlight.api.util.math.colors.LABColor;
import net.mehvahdjukaar.moonlight.api.util.math.colors.RGBColor;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.reg.ClientRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class ColoredMapHandler {

    protected static int DITHERING = 1;

    public static final CustomMapData.Type<ColoredMapHandler.ColorData> COLOR_DATA = MapDataRegistry.registerCustomMapSavedData(Supplementaries.res("color_data"), ColoredMapHandler.ColorData::new);

    public static void init() {
    }

    public static ColoredMapHandler.ColorData getColorData(MapItemSavedData data) {
        return COLOR_DATA.get(data);
    }

    @Nullable
    public static Block getCustomColor(Block block) {
        Holder.Reference<Block> blockReference = block.builtInRegistryHolder();
        if (blockReference.is(ModTags.NOT_TINTED_ON_MAPS)) {
            return null;
        } else if (blockReference.is(ModTags.TINTED_ON_MAPS_GC)) {
            return block instanceof BushBlock ? Blocks.GRASS : Blocks.GRASS_BLOCK;
        } else if (blockReference.is(ModTags.TINTED_ON_MAPS_FC) || block instanceof LeavesBlock) {
            return Blocks.OAK_LEAVES;
        } else if (blockReference.is(ModTags.TINTED_ON_MAPS_WC)) {
            return Blocks.WATER;
        } else {
            return blockReference.is(ModTags.TINTED_ON_MAPS_GENERIC) ? block : null;
        }
    }

    public static class ColorData implements CustomMapData<ColoredMapHandler.Counter>, BlockAndTintGetter {

        public static final int BIOME_SIZE = 4;

        public static final String MIN_X = "min_x";

        public static final String MAX_X = "max_x";

        public static final String MIN_Z = "min_z";

        private static final Map<Pair<Pair<Block, ResourceLocation>, Integer>, Integer> GLOBAL_COLOR_CACHE = new Object2IntOpenHashMap();

        private static final int[] IND2COLOR_BUFFER = new int[1024];

        private byte[][] data = null;

        private final List<ResourceLocation> biomesIndexes = new ArrayList();

        private final List<Block> blockIndexes = new ArrayList();

        private int lastMinDirtyX = 0;

        private int lastMinDirtyZ = 0;

        private int lastMaxDirtyX = 0;

        private int lastMaxDirtyZ = 0;

        private Pair<Block, ResourceLocation> lastEntryHack;

        @Nullable
        private Pair<Block, ResourceLocation> getEntry(int x, int z) {
            if (this.data == null) {
                return null;
            } else if (x < 0 || x >= 128 || z < 0 || z >= 128) {
                return null;
            } else if (this.data[x] != null) {
                int packed = Byte.toUnsignedInt(this.data[x][z]);
                if (packed == 0) {
                    return null;
                } else {
                    int bi = --packed & 15;
                    int bli = packed >> 4;
                    return bi < this.blockIndexes.size() && bli < this.biomesIndexes.size() ? Pair.of((Block) this.blockIndexes.get(bi), (ResourceLocation) this.biomesIndexes.get(bli)) : null;
                }
            } else {
                return null;
            }
        }

        private int getIndex(int x, int z) {
            return this.data != null && this.data[x] != null ? Byte.toUnsignedInt(this.data[x][z]) : 0;
        }

        private void addEntry(MapItemSavedData md, int x, int z, Pair<Block, ResourceLocation> res) {
            Block block = (Block) res.getFirst();
            boolean changedBlock;
            if (!this.blockIndexes.contains(block)) {
                if (this.blockIndexes.size() >= 16) {
                    return;
                }
                this.blockIndexes.add(block);
                changedBlock = true;
            } else {
                changedBlock = false;
            }
            int blockIndex = this.blockIndexes.indexOf(block);
            ResourceLocation biome = (ResourceLocation) res.getSecond();
            boolean changedBiome;
            if (!this.biomesIndexes.contains(biome)) {
                if (this.biomesIndexes.size() >= 16) {
                    return;
                }
                this.biomesIndexes.add(biome);
                changedBiome = true;
            } else {
                changedBiome = false;
            }
            int biomeIndex = this.biomesIndexes.indexOf(biome);
            if (this.data == null) {
                this.data = new byte[128][];
            }
            if (this.data[x] == null) {
                this.data[x] = new byte[128];
            }
            this.data[x][z] = (byte) ((blockIndex & 15 | biomeIndex << 4) + 1);
            this.setDirty(md, counter -> counter.markDirty(x, z, changedBiome, changedBlock));
        }

        @Override
        public void load(CompoundTag tag) {
            this.lastMinDirtyX = 0;
            this.lastMinDirtyZ = 0;
            this.lastMaxDirtyX = 0;
            this.lastMaxDirtyZ = 0;
            if (tag.contains("positions")) {
                CompoundTag t = tag.getCompound("positions");
                int minX = 0;
                if (t.contains("min_x")) {
                    minX = t.getInt("min_x");
                }
                this.lastMinDirtyX = minX;
                int maxX = 127;
                if (t.contains("max_x")) {
                    maxX = t.getInt("max_x");
                }
                this.lastMaxDirtyX = maxX;
                int minZ = 0;
                if (t.contains("min_z")) {
                    minZ = t.getInt("min_z");
                }
                this.lastMinDirtyZ = minZ;
                for (int x = minX; x <= maxX; x++) {
                    byte[] rowData = t.getByteArray("pos_" + x);
                    this.lastMaxDirtyZ = minZ + rowData.length;
                    if (this.data == null) {
                        this.data = new byte[128][];
                    }
                    if (this.data[x] == null) {
                        this.data[x] = new byte[128];
                    }
                    System.arraycopy(rowData, 0, this.data[x], minZ, rowData.length);
                }
            }
            if (tag.contains("biomes")) {
                this.biomesIndexes.clear();
                ListTag biomes = tag.getList("biomes", 10);
                for (int j = 0; j < biomes.size(); j++) {
                    CompoundTag c = biomes.getCompound(j);
                    int i = c.getByte("index");
                    String id = c.getString("id");
                    this.biomesIndexes.add(i, new ResourceLocation(id));
                }
            }
            if (tag.contains("blocks")) {
                this.blockIndexes.clear();
                ListTag blocks = tag.getList("blocks", 10);
                for (int j = 0; j < blocks.size(); j++) {
                    CompoundTag c = blocks.getCompound(j);
                    int i = c.getByte("index");
                    String id = c.getString("id");
                    this.blockIndexes.add(i, BuiltInRegistries.BLOCK.get(new ResourceLocation(id)));
                }
            }
        }

        private void savePatch(CompoundTag tag, int minX, int maxX, int minZ, int maxZ, boolean pos, boolean block, boolean biome) {
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
                tag.put("positions", t);
            }
            if (biome && !this.biomesIndexes.isEmpty()) {
                ListTag biomesList = new ListTag();
                for (int i = 0; i < this.biomesIndexes.size(); i++) {
                    CompoundTag biomeTag = new CompoundTag();
                    biomeTag.putByte("index", (byte) i);
                    biomeTag.putString("id", ((ResourceLocation) this.biomesIndexes.get(i)).toString());
                    biomesList.add(biomeTag);
                }
                tag.put("biomes", biomesList);
            }
            if (block && !this.blockIndexes.isEmpty()) {
                ListTag blocksList = new ListTag();
                for (int i = 0; i < this.blockIndexes.size(); i++) {
                    CompoundTag blockTag = new CompoundTag();
                    blockTag.putByte("index", (byte) i);
                    blockTag.putString("id", Utils.getID((Block) this.blockIndexes.get(i)).toString());
                    blocksList.add(blockTag);
                }
                tag.put("blocks", blocksList);
            }
        }

        @Override
        public void save(CompoundTag tag) {
            this.savePatch(tag, 0, 127, 0, 127, true, true, true);
        }

        public void saveToUpdateTag(CompoundTag tag, ColoredMapHandler.Counter dc) {
            this.savePatch(tag, dc.minDirtyX, dc.maxDirtyX, dc.minDirtyZ, dc.maxDirtyZ, dc.posDirty, dc.blockDirty, dc.biomesDirty);
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
            return ColoredMapHandler.COLOR_DATA;
        }

        public ColoredMapHandler.Counter createDirtyCounter() {
            return new ColoredMapHandler.Counter();
        }

        public void markColored(int x, int z, Block block, Level level, BlockPos pos, MapItemSavedData data) {
            Block customColor = ColoredMapHandler.getCustomColor(block);
            if (customColor != null) {
                boolean odd = x % 2 == 0 ^ z % 2 == 1;
                pos = pos.offset(odd ? ColoredMapHandler.DITHERING : -ColoredMapHandler.DITHERING, 0, odd ? ColoredMapHandler.DITHERING : -ColoredMapHandler.DITHERING);
                ResourceKey<Biome> biome = (ResourceKey<Biome>) level.m_204166_(pos).unwrapKey().get();
                Pair<Block, ResourceLocation> pair = Pair.of(customColor, biome.location());
                if (!Objects.equals(this.getEntry(x, z), pair)) {
                    this.addEntry(data, x, z, pair);
                }
            } else if (this.data != null && this.data[x] != null && this.data[x][z] != 0) {
                this.data[x][z] = 0;
                this.setDirty(data, counter -> counter.markDirty(x, z, false, false));
                for (byte b : this.data[x]) {
                    if (b != 0) {
                        return;
                    }
                }
                this.data[x] = null;
            }
        }

        @Nullable
        @Override
        public BlockEntity getBlockEntity(BlockPos pos) {
            return null;
        }

        @Override
        public BlockState getBlockState(BlockPos pos) {
            Pair<Block, ResourceLocation> entry = this.getEntry(pos.m_123341_(), pos.m_123343_());
            return entry == null ? Blocks.AIR.defaultBlockState() : ((Block) entry.getFirst()).defaultBlockState();
        }

        @Override
        public FluidState getFluidState(BlockPos pos) {
            return this.getBlockState(pos).m_60819_();
        }

        @Override
        public int getHeight() {
            return 0;
        }

        @Override
        public int getMinBuildHeight() {
            return 0;
        }

        @OnlyIn(Dist.CLIENT)
        public void processTexture(NativeImage texture, int startX, int startY, byte[] colors) {
            if ((Boolean) ClientConfigs.Tweaks.COLORED_MAPS.get() && this.data != null) {
                boolean tallGrass = (Boolean) ClientConfigs.Tweaks.TALL_GRASS_COLOR_CHANGE.get();
                boolean accurateConfig = (Boolean) ClientConfigs.Tweaks.ACCURATE_COLORED_MAPS.get();
                if (!accurateConfig) {
                    Arrays.fill(IND2COLOR_BUFFER, 0);
                }
                BlockColors blockColors = Minecraft.getInstance().getBlockColors();
                for (int x = 0; x < 128; x++) {
                    for (int z = 0; z < 128; z++) {
                        int index = this.getIndex(x, z);
                        if (index != 0) {
                            int newTint = -1;
                            int k = x + z * 128;
                            byte packedId = colors[k];
                            int brightnessInd = packedId & 3;
                            if (!accurateConfig) {
                                int alreadyKnownColor = IND2COLOR_BUFFER[index + brightnessInd * 256];
                                if (alreadyKnownColor != 0) {
                                    newTint = alreadyKnownColor;
                                }
                            }
                            if (newTint == -1) {
                                Pair<Block, ResourceLocation> e = this.getEntry(x, z);
                                this.lastEntryHack = e;
                                if (e == null) {
                                    continue;
                                }
                                Block block = (Block) e.getFirst();
                                if (accurateConfig) {
                                    BlockPos pos = new BlockPos(x, 64, z);
                                    int tint = blockColors.getColor(block.defaultBlockState(), this, pos, 0);
                                    if (tint != -1) {
                                        newTint = postProcessTint(tallGrass, packedId, block, tint);
                                    }
                                } else {
                                    newTint = (Integer) GLOBAL_COLOR_CACHE.computeIfAbsent(Pair.of(e, brightnessInd), n -> {
                                        BlockPos posx = new BlockPos(0, 64, 0);
                                        int tintx = blockColors.getColor(block.defaultBlockState(), this, posx, 0);
                                        return postProcessTint(tallGrass, packedId, block, tintx);
                                    });
                                    IND2COLOR_BUFFER[index + brightnessInd * 256] = newTint;
                                }
                            }
                            if (newTint != -1) {
                                texture.setPixelRGBA(startX + x, startY + z, newTint);
                            }
                        }
                    }
                }
            }
        }

        private static int postProcessTint(boolean tg, byte packedId, Block block, int tint) {
            float lumIncrease = 1.3F;
            MapColor mapColor = MapColor.byId((packedId & 255) >> 2);
            if (mapColor == MapColor.WATER) {
                lumIncrease = 2.0F;
            } else if (mapColor == MapColor.PLANT && block instanceof BushBlock && tg) {
                packedId = MapColor.GRASS.getPackedId(MapColor.Brightness.byId(packedId & 3));
            }
            int color = MapColor.getColorFromPackedId(packedId);
            tint = ColorUtils.swapFormat(tint);
            RGBColor tintColor = new RGBColor(tint);
            LABColor c = new RGBColor(color).asLAB();
            RGBColor gray = c.multiply(lumIncrease, 0.0F, 0.0F, 1.0F).asRGB();
            return gray.multiply(tintColor.red(), tintColor.green(), tintColor.blue(), 1.0F).asHSL().multiply(1.0F, 1.3F, 1.0F, 1.0F).asRGB().toInt();
        }

        @Override
        public float getShade(Direction direction, boolean shade) {
            return 0.0F;
        }

        @Override
        public LevelLightEngine getLightEngine() {
            return ClientRegistry.getLightEngine();
        }

        @Override
        public int getBlockTint(BlockPos pos, ColorResolver colorResolver) {
            if (this.lastEntryHack != null) {
                if (pos == null || colorResolver == null) {
                    throw new IllegalStateException("Block position of Color resolvers were null. How? " + pos + colorResolver);
                }
                int x = pos.m_123341_();
                int z = pos.m_123343_();
                Biome b = Utils.hackyGetRegistry(Registries.BIOME).get((ResourceLocation) this.lastEntryHack.getSecond());
                if (b != null) {
                    boolean odd = x % 2 == 0 ^ z % 2 == 1;
                    pos = pos.offset(odd ? ColoredMapHandler.DITHERING : -ColoredMapHandler.DITHERING, 0, odd ? ColoredMapHandler.DITHERING : -ColoredMapHandler.DITHERING);
                    return colorResolver.getColor(b, (double) pos.m_123341_() + 0.5, (double) pos.m_123343_() + 0.5);
                }
            }
            return 0;
        }

        public void clear() {
            this.data = null;
            this.biomesIndexes.clear();
            this.blockIndexes.clear();
        }
    }

    private static class Counter implements CustomMapData.DirtyCounter {

        private int minDirtyX = 0;

        private int maxDirtyX = 127;

        private int minDirtyZ = 0;

        private int maxDirtyZ = 127;

        private boolean posDirty = true;

        private boolean blockDirty = true;

        private boolean biomesDirty = true;

        public void markDirty(int x, int z, boolean changedBiome, boolean changedBlock) {
            if (changedBiome) {
                this.biomesDirty = true;
            }
            if (changedBlock) {
                this.blockDirty = true;
            }
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
            return this.posDirty || this.biomesDirty || this.blockDirty;
        }

        @Override
        public void clearDirty() {
            this.biomesDirty = false;
            this.blockDirty = false;
            this.posDirty = false;
            this.minDirtyX = 0;
            this.minDirtyZ = 0;
            this.maxDirtyX = 0;
            this.maxDirtyZ = 0;
        }
    }
}