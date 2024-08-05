package noppes.npcs.schematics;

import com.mojang.serialization.Dynamic;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.CustomBlocks;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.shared.common.CommonUtil;

public class SpongeSchem implements ISchematic {

    public static final int latestDataVersion = 2586;

    public String name;

    public short width;

    public short height;

    public short length;

    public long timestamp = System.currentTimeMillis();

    public int[] data;

    public Map<Integer, BlockState> palette = new HashMap();

    public List<CompoundTag> tileData = new ArrayList();

    public SpongeSchem(String name) {
        this.name = name;
    }

    @Override
    public short getWidth() {
        return this.width;
    }

    @Override
    public short getHeight() {
        return this.height;
    }

    @Override
    public short getLength() {
        return this.length;
    }

    @Override
    public int getBlockEntityDimensions() {
        return this.tileData.size();
    }

    @Override
    public CompoundTag getBlockEntity(int i) {
        return (CompoundTag) this.tileData.get(i);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return this.getBlockState(this.xyzToIndex(x, y, z));
    }

    public int xyzToIndex(int x, int y, int z) {
        return (y * this.length + z) * this.width + x;
    }

    @Override
    public BlockState getBlockState(int i) {
        return (BlockState) this.palette.get(this.data[i]);
    }

    @Override
    public CompoundTag getNBT() {
        CompoundTag root = new CompoundTag();
        CompoundTag compound = new CompoundTag();
        root.put("", compound);
        CompoundTag data = new CompoundTag();
        compound.put("Schematic", data);
        data.putInt("Width", this.width);
        data.putInt("Height", this.height);
        data.putInt("Length", this.length);
        data.putInt("Version", 3);
        data.putInt("DataVersion", 2586);
        CompoundTag metadata = new CompoundTag();
        metadata.putLong("Date", this.timestamp);
        data.put("Metadata", metadata);
        CompoundTag blockData = new CompoundTag();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(this.data.length);
        for (int i = 0; i < this.data.length; i++) {
            int blockId;
            for (blockId = this.data[i]; (blockId & -128) != 0; blockId >>>= 7) {
                buffer.write(blockId & 127 | 128);
            }
            buffer.write(blockId);
        }
        blockData.putByteArray("Data", buffer.toByteArray());
        CompoundTag palletteNBT = new CompoundTag();
        for (Entry<Integer, BlockState> en : this.palette.entrySet()) {
            palletteNBT.putInt(BlockStateParser.serialize((BlockState) en.getValue()), (Integer) en.getKey());
        }
        blockData.put("Palette", palletteNBT);
        ListTag tileNBT = new ListTag();
        for (CompoundTag tile : this.tileData) {
            tile = tile.copy();
            tile.putIntArray("Pos", new int[] { tile.getInt("x"), tile.getInt("y"), tile.getInt("z") });
            tile.putString("Id", tile.getString("id"));
            tile.remove("x");
            tile.remove("y");
            tile.remove("z");
            tile.remove("id");
            tileNBT.add(tile);
        }
        blockData.put("BlockEntities", tileNBT);
        data.put("Blocks", blockData);
        return root;
    }

    public void load(CompoundTag compound) {
        if (compound.size() == 1) {
            compound = compound.getCompound("").getCompound("Schematic");
        }
        this.width = compound.getShort("Width");
        this.height = compound.getShort("Height");
        this.length = compound.getShort("Length");
        CompoundTag metadata = compound.getCompound("Metadata");
        this.timestamp = 0L;
        if (!metadata.isEmpty()) {
            this.timestamp = metadata.getLong("Date");
        }
        int dataVersion = 1631;
        if (compound.contains("DataVersion")) {
            dataVersion = compound.getInt("DataVersion");
            if (dataVersion > 2586) {
            }
            if (dataVersion < 2586) {
            }
        }
        int version = compound.getInt("Version");
        if (version < 3) {
            this.palette = this.readPalette(compound.getCompound("Palette"), dataVersion);
            ListTag tileEntities = compound.getList("BlockEntities", 10);
            if (tileEntities.isEmpty()) {
                tileEntities = compound.getList("TileEntities", 10);
            }
            this.tileData = this.readTileData(tileEntities, dataVersion);
            this.data = this.readBlockData(compound.getByteArray("BlockData"));
        } else {
            CompoundTag blocks = compound.getCompound("Blocks");
            this.palette = this.readPalette(blocks.getCompound("Palette"), dataVersion);
            this.tileData = this.readTileData(blocks.getList("BlockEntities", 10), dataVersion);
            this.data = this.readBlockData(blocks.getByteArray("Data"));
        }
    }

    private int[] readBlockData(byte[] bytes) {
        int[] data = new int[this.width * this.length * this.height];
        int index = 0;
        for (int i = 0; i < bytes.length; index++) {
            int value = 0;
            int varintLength = 0;
            while (true) {
                value |= (bytes[i] & 127) << varintLength++ * 7;
                if (varintLength > 5) {
                    throw new CustomNPCsException("VarInt too big (probably corrupted data)");
                }
                if ((bytes[i] & 128) != 128) {
                    i++;
                    data[index] = value;
                    break;
                }
                i++;
            }
        }
        return data;
    }

    private Map<Integer, BlockState> readPalette(CompoundTag comp, int dataVersion) {
        Map<String, Integer> map = new HashMap();
        Map<Integer, BlockState> palette = new HashMap();
        for (String blockState : comp.getAllKeys()) {
            int id = comp.getInt(blockState);
            if (dataVersion < 2586) {
                CompoundTag stateNBT = stateToNBT(blockState);
                Dynamic<Tag> dynamic = new Dynamic(NbtOps.INSTANCE, stateNBT);
                stateNBT = (CompoundTag) DataFixers.getDataFixer().update(References.BLOCK_STATE, dynamic, dataVersion, 2586).getValue();
                blockState = this.nbtToState(stateNBT);
            }
            map.put(blockState, id);
        }
        for (Block block : ForgeRegistries.BLOCKS) {
            block.getStateDefinition().getPossibleStates().forEach(state -> {
                String name = BlockStateParser.serialize(state);
                if (map.containsKey(name)) {
                    int idx = (Integer) map.remove(name);
                    palette.put(idx, state);
                }
            });
        }
        for (int id : map.values()) {
            palette.put(id, Blocks.AIR.defaultBlockState());
        }
        return palette;
    }

    private List<CompoundTag> readTileData(ListTag list, int dataVersion) {
        List<CompoundTag> tileData = new ArrayList();
        if (list.isEmpty()) {
            return tileData;
        } else {
            for (int i = 0; i < list.size(); i++) {
                CompoundTag data = list.getCompound(i);
                int[] posArr = data.getIntArray("Pos");
                BlockPos pos = new BlockPos(posArr[0], posArr[1], posArr[2]);
                data.putInt("x", pos.m_123341_());
                data.putInt("y", pos.m_123342_());
                data.putInt("z", pos.m_123343_());
                data.put("id", data.get("Id"));
                data.remove("Id");
                data.remove("Pos");
                if (dataVersion < 2586) {
                    Dynamic<Tag> dynamic = new Dynamic(NbtOps.INSTANCE, data);
                    data = (CompoundTag) DataFixers.getDataFixer().update(References.BLOCK_ENTITY, dynamic, dataVersion, 2586).getValue();
                } else {
                    data = data.copy();
                }
                tileData.add(data);
            }
            return tileData;
        }
    }

    private String nbtToState(CompoundTag tagCompound) {
        StringBuilder sb = new StringBuilder();
        sb.append(tagCompound.getString("Name"));
        if (tagCompound.contains("Properties", 10)) {
            sb.append('[');
            CompoundTag props = tagCompound.getCompound("Properties");
            sb.append((String) props.getAllKeys().stream().map(k -> k + "=" + props.getString(k).replace("\"", "")).collect(Collectors.joining(",")));
            sb.append(']');
        }
        return sb.toString();
    }

    private static CompoundTag stateToNBT(String blockState) {
        int propIdx = blockState.indexOf(91);
        CompoundTag tag = new CompoundTag();
        if (propIdx < 0) {
            tag.putString("Name", blockState);
        } else {
            tag.putString("Name", blockState.substring(0, propIdx));
            CompoundTag propTag = new CompoundTag();
            String props = blockState.substring(propIdx + 1, blockState.length() - 1);
            String[] propArr = props.split(",");
            for (String pair : propArr) {
                String[] split = pair.split("=");
                propTag.putString(split[0], split[1]);
            }
            tag.put("Properties", propTag);
        }
        return tag;
    }

    public static SpongeSchem Create(Level level, String name, BlockPos pos, short height, short width, short length) {
        SpongeSchem schema = new SpongeSchem(name);
        schema.height = height;
        schema.width = width;
        schema.length = length;
        int size = height * width * length;
        CommonUtil.NotifyOPs(level.getServer(), "Creating schematic at: " + pos + " might lag slightly");
        Map<String, Integer> map = new HashMap();
        schema.data = new int[size];
        int uniqueBlockId = 0;
        for (int i = 0; i < size; i++) {
            int x = i % width;
            int z = (i - x) / width % length;
            int y = ((i - x) / width - z) / length;
            BlockState state = level.getBlockState(pos.offset(x, y, z));
            if (state.m_60734_() != CustomBlocks.copy) {
                String stateName = BlockStateParser.serialize(state);
                Integer blockId = (Integer) map.get(stateName);
                if (!map.containsKey(stateName)) {
                    map.put(stateName, blockId = uniqueBlockId++);
                }
                schema.palette.put(blockId, state);
                schema.data[i] = blockId;
                if (state.m_60734_() instanceof EntityBlock) {
                    BlockEntity tile = level.getBlockEntity(pos.offset(x, y, z));
                    CompoundTag compound = tile.saveWithFullMetadata();
                    compound.putInt("x", x);
                    compound.putInt("y", y);
                    compound.putInt("z", z);
                    schema.tileData.add(compound);
                }
            }
        }
        return schema;
    }
}