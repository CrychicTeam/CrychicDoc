package com.mna.recipes.multiblock;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mna.tools.BlockUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class MultiblockConfiguration {

    String identifier;

    HashMap<Long, Pair<Integer, MutableBoolean>> structureBlocks;

    private static final Pair<Integer, MutableBoolean> DEFAULT = new Pair(-1, new MutableBoolean(false));

    private Vec3i size;

    private boolean cachedMapped = false;

    private Rotation curRotation = Rotation.NONE;

    private boolean isValid = false;

    private MultiblockConfiguration() {
        this.structureBlocks = new HashMap();
    }

    public void rotate(Rotation rotation) {
        if (rotation != this.curRotation) {
            ;
        }
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public Set<Long> getOffsets() {
        return this.structureBlocks.keySet();
    }

    public Vec3i getSize() {
        return this.size;
    }

    public boolean getIsValid() {
        return this.isValid;
    }

    public int countBlocks() {
        return this.structureBlocks.size();
    }

    public void resetMatchData() {
        this.structureBlocks.values().forEach(p -> ((MutableBoolean) p.getSecond()).setFalse());
        this.cachedMapped = false;
    }

    public Integer getBlockAt(BlockPos offset) {
        return (Integer) ((Pair) this.structureBlocks.getOrDefault(offset.asLong(), DEFAULT)).getFirst();
    }

    public void setBlockAt(BlockPos offset, Integer blockStateIndex) {
        this.structureBlocks.put(offset.asLong(), new Pair(blockStateIndex, new MutableBoolean(false)));
        this.recalculateSize();
    }

    private void recalculateSize() {
        int minX = 0;
        int maxX = 0;
        int minY = 0;
        int maxY = 0;
        int minZ = 0;
        int maxZ = 0;
        for (Long l : this.structureBlocks.keySet()) {
            BlockPos cur = BlockPos.of(l);
            if (cur.m_123341_() < minX) {
                minX = cur.m_123341_();
            } else if (cur.m_123341_() > maxX) {
                maxX = cur.m_123341_();
            }
            if (cur.m_123342_() < minY) {
                minY = cur.m_123342_();
            } else if (cur.m_123342_() > maxY) {
                maxY = cur.m_123342_();
            }
            if (cur.m_123343_() < minZ) {
                minZ = cur.m_123343_();
            } else if (cur.m_123343_() > maxZ) {
                maxZ = cur.m_123343_();
            }
        }
        this.size = new BlockPos(maxX - minX, maxY - minY, maxZ - minZ);
    }

    public void validate() {
        if (this.size.getX() == 0 || this.size.getY() == 0 || this.size.getZ() == 0) {
            this.isValid = false;
        } else if (this.structureBlocks.size() == 0) {
            this.isValid = false;
        } else {
            this.isValid = true;
        }
    }

    public void markMatch(BlockPos offset) {
        Pair<Integer, MutableBoolean> pair = (Pair<Integer, MutableBoolean>) this.structureBlocks.get(offset.asLong());
        ((MutableBoolean) pair.getSecond()).setTrue();
    }

    private boolean offsetMatched(Long offset) {
        return ((MutableBoolean) ((Pair) this.structureBlocks.getOrDefault(offset, DEFAULT)).getSecond()).booleanValue();
    }

    public void computeMatch() {
        this.cachedMapped = this.structureBlocks.values().stream().allMatch(e -> ((MutableBoolean) e.getSecond()).isTrue());
    }

    public boolean matched() {
        return this.cachedMapped;
    }

    public Pair<Integer, Integer> matchCount(List<MultiblockConfiguration> variations) {
        int total = this.structureBlocks.size();
        int matched = (int) this.structureBlocks.entrySet().stream().filter(e -> ((MutableBoolean) ((Pair) e.getValue()).getSecond()).isTrue() || variations.stream().anyMatch(v -> v.offsetMatched((Long) e.getKey()))).count();
        return new Pair(matched, total);
    }

    private static int updateBlockStateIndex(BlockState state, List<BlockState> blockIndex, boolean allowAir) {
        if (state.m_60795_() && !allowAir) {
            return -1;
        } else {
            int index = blockIndex.indexOf(state);
            if (index == -1) {
                blockIndex.add(state);
                index = blockIndex.size() - 1;
            }
            return index;
        }
    }

    public static MultiblockConfiguration parseVariation(JsonObject variation, List<BlockState> blockIndex) {
        MultiblockConfiguration mbc = new MultiblockConfiguration();
        if (variation.has("id") && variation.has("data")) {
            mbc.identifier = variation.get("id").getAsString();
            mbc.isValid = true;
            JsonArray data = variation.getAsJsonArray("data");
            int[] min = new int[] { 0, 0, 0 };
            int[] max = new int[] { 0, 0, 0 };
            data.forEach(d -> {
                JsonObject elem = d.getAsJsonObject();
                CompoundTag nbt = null;
                try {
                    nbt = TagParser.parseTag(elem.toString());
                } catch (CommandSyntaxException var10) {
                    mbc.isValid = false;
                    return;
                }
                if (nbt.contains("offset", 10) && nbt.contains("state", 10)) {
                    BlockPos offset = NbtUtils.readBlockPos((CompoundTag) nbt.get("offset"));
                    BlockState state = BlockUtils.readBlockState((CompoundTag) nbt.get("state"));
                    int index = updateBlockStateIndex(state, blockIndex, true);
                    if (index == -1) {
                        mbc.isValid = false;
                        return;
                    }
                    if (offset.m_123341_() < min[0]) {
                        min[0] = offset.m_123341_();
                    }
                    if (offset.m_123342_() < min[1]) {
                        min[1] = offset.m_123342_();
                    }
                    if (offset.m_123343_() < min[2]) {
                        min[2] = offset.m_123343_();
                    }
                    if (offset.m_123341_() > max[0]) {
                        max[0] = offset.m_123341_();
                    }
                    if (offset.m_123342_() > max[1]) {
                        max[1] = offset.m_123342_();
                    }
                    if (offset.m_123343_() > max[2]) {
                        max[2] = offset.m_123343_();
                    }
                    mbc.structureBlocks.put(offset.asLong(), new Pair(index, new MutableBoolean(false)));
                } else {
                    mbc.isValid = false;
                }
            });
            mbc.size = new BlockPos(max[0] - min[0], max[1] - min[1], max[2] - min[2]);
        }
        return mbc;
    }

    public static MultiblockConfiguration loadStructure(StructureTemplateManager manager, ResourceLocation structureFile, List<BlockState> blockIndex) {
        MultiblockConfiguration mbc = new MultiblockConfiguration();
        manager.get(structureFile).ifPresent(template -> {
            mbc.size = template.getSize();
            template.palettes.forEach(p -> p.blocks().forEach(bi -> {
                int index = updateBlockStateIndex(bi.state(), blockIndex, false);
                if (index != -1) {
                    mbc.structureBlocks.put(bi.pos().asLong(), new Pair(index, new MutableBoolean(false)));
                }
            }));
            mbc.isValid = true;
        });
        return mbc;
    }

    public static MultiblockConfiguration createDummyStructure() {
        MultiblockConfiguration mbc = new MultiblockConfiguration();
        mbc.size = new BlockPos(0, 0, 0);
        mbc.isValid = false;
        return mbc;
    }

    public CompoundTag serialize() {
        CompoundTag data = new CompoundTag();
        if (!this.isValid) {
            return data;
        } else {
            ListTag list = new ListTag();
            this.structureBlocks.forEach((l, i) -> {
                CompoundTag entry = new CompoundTag();
                entry.putLong("offset", l);
                entry.putInt("index", (Integer) i.getFirst());
                list.add(entry);
            });
            data.put("list", list);
            if (this.size != null) {
                data.put("size", NbtUtils.writeBlockPos(new BlockPos(this.size)));
            }
            return data;
        }
    }

    public static MultiblockConfiguration deserialize(CompoundTag nbt) {
        MultiblockConfiguration conf = new MultiblockConfiguration();
        conf.isValid = true;
        if (nbt.contains("list")) {
            ListTag list = nbt.getList("list", 10);
            list.forEach(e -> {
                CompoundTag entry = (CompoundTag) e;
                if (entry.contains("offset") && entry.contains("index")) {
                    long offset = entry.getLong("offset");
                    int index = entry.getInt("index");
                    conf.structureBlocks.put(offset, new Pair(index, new MutableBoolean(false)));
                }
            });
        } else {
            conf.isValid = false;
        }
        if (nbt.contains("size")) {
            conf.size = NbtUtils.readBlockPos(nbt.getCompound("size"));
        } else {
            conf.isValid = false;
        }
        return conf;
    }

    public JsonObject computeRecipeDiff(MultiblockConfiguration other, ArrayList<BlockState> blockIndex) {
        HashMap<Long, Integer> changes = new HashMap();
        other.structureBlocks.keySet().stream().forEach(k -> {
            if (this.structureBlocks.containsKey(k)) {
                if (((Pair) this.structureBlocks.get(k)).getFirst() != ((Pair) other.structureBlocks.get(k)).getFirst()) {
                    changes.put(k, (Integer) ((Pair) other.structureBlocks.get(k)).getFirst());
                }
            } else {
                changes.put(k, (Integer) ((Pair) other.structureBlocks.get(k)).getFirst());
            }
        });
        ListTag diffData = new ListTag();
        changes.entrySet().stream().forEach(e -> {
            BlockPos offset = BlockPos.of((Long) e.getKey());
            BlockState state = (BlockState) blockIndex.get((Integer) e.getValue());
            CompoundTag diffItem = new CompoundTag();
            diffItem.put("offset", NbtUtils.writeBlockPos(offset));
            diffItem.put("state", NbtUtils.writeBlockState(state));
            diffData.add(diffItem);
        });
        CompoundTag nbt = new CompoundTag();
        nbt.putString("id", "variation_id_here_must_be_unique");
        nbt.put("data", diffData);
        return JsonParser.parseString(nbt.toString()).getAsJsonObject();
    }
}