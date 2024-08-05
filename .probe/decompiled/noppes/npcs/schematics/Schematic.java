package noppes.npcs.schematics;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.CustomNpcs;

public class Schematic implements ISchematic {

    private static final HashMap<String, BlockState> staticBlockIds = new HashMap();

    public String name;

    public short width;

    public short height;

    public short length;

    private ListTag entityList;

    public ListTag tileList;

    public short[] blockArray;

    public byte[] blockDataArray;

    public HashMap<String, BlockState> blockIds = staticBlockIds;

    private static <T extends Comparable<T>> BlockState setValue(BlockState state, Property<T> prop, String val) {
        Optional<T> optional = prop.getValue(val);
        return optional.isPresent() ? (BlockState) state.m_61124_(prop, (Comparable) optional.get()) : state;
    }

    public Schematic(String name) {
        this.name = name;
    }

    public void load(CompoundTag compound) {
        this.width = compound.getShort("Width");
        this.height = compound.getShort("Height");
        this.length = compound.getShort("Length");
        byte[] addId = compound.contains("AddBlocks") ? compound.getByteArray("AddBlocks") : new byte[0];
        this.setBlockBytes(compound.getByteArray("Blocks"), addId);
        this.blockDataArray = compound.getByteArray("Data");
        this.entityList = compound.getList("Entities", 10);
        this.tileList = compound.getList("TileEntities", 10);
        if (compound.contains("BlockIDs", 10)) {
            CompoundTag comp = compound.getCompound("BlockIDs");
            this.blockIds = new HashMap();
            for (String idStr : comp.getAllKeys()) {
                String key = comp.getString(idStr);
                try {
                    int id = Integer.parseInt(idStr);
                    Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(key));
                    if (block != null) {
                        this.blockIds.put(id + ":0", block.defaultBlockState());
                    }
                } catch (NumberFormatException var9) {
                }
            }
        }
    }

    @Override
    public CompoundTag getNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putShort("Width", this.width);
        compound.putShort("Height", this.height);
        compound.putShort("Length", this.length);
        byte[][] arr = this.getBlockBytes();
        compound.putByteArray("Blocks", arr[0]);
        if (arr.length > 1) {
            compound.putByteArray("AddBlocks", arr[1]);
        }
        compound.putByteArray("Data", this.blockDataArray);
        compound.put("TileEntities", this.tileList);
        CompoundTag comp = new CompoundTag();
        for (Entry<String, BlockState> entry : this.blockIds.entrySet()) {
            comp.putString(Block.getId((BlockState) entry.getValue()) + "", ForgeRegistries.BLOCKS.getKey(((BlockState) entry.getValue()).m_60734_()).toString());
        }
        compound.put("BlockIDs", comp);
        return compound;
    }

    public void setBlockBytes(byte[] blockId, byte[] addId) {
        this.blockArray = new short[blockId.length];
        for (int index = 0; index < blockId.length; index++) {
            short id = (short) (blockId[index] & 255);
            if (index >> 1 < addId.length) {
                if ((index & 1) == 0) {
                    id += (short) ((addId[index >> 1] & 15) << 8);
                } else {
                    id += (short) ((addId[index >> 1] & 240) << 4);
                }
            }
            this.blockArray[index] = id;
        }
    }

    public byte[][] getBlockBytes() {
        byte[] blocks = new byte[this.blockArray.length];
        byte[] addBlocks = null;
        for (int i = 0; i < blocks.length; i++) {
            short id = this.blockArray[i];
            if (id > 255) {
                if (addBlocks == null) {
                    addBlocks = new byte[(blocks.length >> 1) + 1];
                }
                if ((i & 1) == 0) {
                    addBlocks[i >> 1] = (byte) (addBlocks[i >> 1] & 240 | id >> 8 & 15);
                } else {
                    addBlocks[i >> 1] = (byte) (addBlocks[i >> 1] & 15 | (id >> 8 & 15) << 4);
                }
            }
            blocks[i] = (byte) id;
        }
        return addBlocks == null ? new byte[][] { blocks } : new byte[][] { blocks, addBlocks };
    }

    public int xyzToIndex(int x, int y, int z) {
        return (y * this.length + z) * this.width + x;
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return this.getBlockState(this.xyzToIndex(x, y, z));
    }

    @Override
    public BlockState getBlockState(int i) {
        BlockState b = (BlockState) this.blockIds.get(this.blockArray[i] + ":" + this.blockDataArray[i]);
        return b == null ? Blocks.AIR.defaultBlockState() : b;
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
        return this.tileList == null ? 0 : this.tileList.size();
    }

    @Override
    public CompoundTag getBlockEntity(int i) {
        return this.tileList.getCompound(i);
    }

    @Override
    public String getName() {
        return this.name;
    }

    static {
        ResourceLocation resource = new ResourceLocation("customnpcs", "legacy_blockids.json");
        Resource ir = (Resource) CustomNpcs.Server.getServerResources().resourceManager().m_213713_(resource).orElse(null);
        if (ir != null) {
            try {
                InputStream stream = ir.open();
                Reader reader = new InputStreamReader(stream, "UTF-8");
                JsonObject result = ((JsonObject) new Gson().fromJson(reader, JsonObject.class)).getAsJsonObject("blocks");
                for (Entry<String, JsonElement> entry : result.entrySet()) {
                    String val = ((JsonElement) entry.getValue()).getAsString();
                    String[] properties = null;
                    if (val.indexOf(91) > 0) {
                        properties = val.substring(val.indexOf(91) + 1, val.length() - 1).split(",");
                        val = val.substring(0, val.indexOf(91));
                    }
                    Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(val));
                    if (block != null) {
                        BlockState state = block.defaultBlockState();
                        if (properties != null) {
                            for (Property<?> prop : state.m_61147_()) {
                                for (String r : properties) {
                                    if (r.startsWith(prop.getName() + "=")) {
                                        state = setValue(state, prop, r.split("=")[1]);
                                    }
                                }
                            }
                        }
                        staticBlockIds.put((String) entry.getKey(), state);
                    }
                }
            } catch (UnsupportedEncodingException var17) {
                var17.printStackTrace();
            } catch (IOException var18) {
            }
        }
    }
}