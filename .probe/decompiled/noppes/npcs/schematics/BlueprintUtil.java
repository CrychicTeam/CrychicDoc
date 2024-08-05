package noppes.npcs.schematics;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;

public class BlueprintUtil {

    public static CompoundTag writeBlueprintToNBT(Blueprint schem) {
        CompoundTag compound = new CompoundTag();
        compound.putByte("version", (byte) 1);
        compound.putShort("size_x", schem.getSizeX());
        compound.putShort("size_y", schem.getSizeY());
        compound.putShort("size_z", schem.getSizeZ());
        BlockState[] palette = schem.getPallete();
        ListTag paletteTag = new ListTag();
        for (short i = 0; i < schem.getPalleteSize(); i++) {
            paletteTag.add(NbtUtils.writeBlockState(palette[i]));
        }
        compound.put("palette", paletteTag);
        int[] blockInt = convertBlocksToSaveData(schem.getStructure(), schem.getSizeX(), schem.getSizeY(), schem.getSizeZ());
        compound.putIntArray("blocks", blockInt);
        ListTag finishedTes = new ListTag();
        CompoundTag[] tes = schem.getTileEntities();
        for (int i = 0; i < tes.length; i++) {
            finishedTes.add(tes[i]);
        }
        compound.put("tile_entities", finishedTes);
        List<String> requiredMods = schem.getRequiredMods();
        ListTag modsList = new ListTag();
        for (int i = 0; i < requiredMods.size(); i++) {
            modsList.add(StringTag.valueOf((String) requiredMods.get(i)));
        }
        compound.put("required_mods", modsList);
        String name = schem.getName();
        String[] architects = schem.getArchitects();
        if (name != null) {
            compound.putString("name", name);
        }
        if (architects != null) {
            ListTag architectsTag = new ListTag();
            for (String architect : architects) {
                architectsTag.add(StringTag.valueOf(architect));
            }
            compound.put("architects", architectsTag);
        }
        return compound;
    }

    public static Blueprint readBlueprintFromNBT(CompoundTag tag) {
        byte version = tag.getByte("version");
        if (version != 1) {
            return null;
        } else {
            short sizeX = tag.getShort("size_x");
            short sizeY = tag.getShort("size_y");
            short sizeZ = tag.getShort("size_z");
            List<String> requiredMods = new ArrayList();
            ListTag modsList = tag.getList("required_mods", 8);
            short modListSize = (short) modsList.size();
            for (int i = 0; i < modListSize; i++) {
                requiredMods.add(((StringTag) modsList.get(i)).getAsString());
                if (!ModList.get().isLoaded((String) requiredMods.get(i))) {
                    Logger.getGlobal().log(Level.WARNING, "Couldn't load Blueprint, the following mod is missing: " + (String) requiredMods.get(i));
                    return null;
                }
            }
            ListTag paletteTag = tag.getList("palette", 10);
            short paletteSize = (short) paletteTag.size();
            BlockState[] palette = new BlockState[paletteSize];
            for (short ix = 0; ix < palette.length; ix++) {
                palette[ix] = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.m_255303_(), paletteTag.getCompound(ix));
            }
            short[][][] blocks = convertSaveDataToBlocks(tag.getIntArray("blocks"), sizeX, sizeY, sizeZ);
            ListTag teTag = tag.getList("tile_entities", 10);
            CompoundTag[] tileEntities = new CompoundTag[teTag.size()];
            for (short ix = 0; ix < tileEntities.length; ix++) {
                tileEntities[ix] = teTag.getCompound(ix);
            }
            Blueprint schem = new Blueprint(sizeX, sizeY, sizeZ, paletteSize, palette, blocks, tileEntities, requiredMods);
            if (tag.contains("name")) {
                schem.setName(tag.getString("name"));
            }
            if (tag.contains("architects")) {
                ListTag architectsTag = tag.getList("architects", 8);
                String[] architects = new String[architectsTag.size()];
                for (int ix = 0; ix < architectsTag.size(); ix++) {
                    architects[ix] = architectsTag.getString(ix);
                }
                schem.setArchitects(architects);
            }
            return schem;
        }
    }

    private static int[] convertBlocksToSaveData(short[][][] multDimArray, short sizeX, short sizeY, short sizeZ) {
        short[] oneDimArray = new short[sizeX * sizeY * sizeZ];
        int j = 0;
        for (short y = 0; y < sizeY; y++) {
            for (short z = 0; z < sizeZ; z++) {
                for (short x = 0; x < sizeX; x++) {
                    oneDimArray[j++] = multDimArray[y][z][x];
                }
            }
        }
        int[] ints = new int[(int) Math.ceil((double) ((float) oneDimArray.length / 2.0F))];
        int currentInt = 0;
        for (int i = 1; i < oneDimArray.length; i += 2) {
            int var11 = oneDimArray[i - 1];
            var11 = var11 << 16 | oneDimArray[i];
            ints[(int) Math.ceil((double) ((float) i / 2.0F)) - 1] = var11;
            int var13 = false;
        }
        if (oneDimArray.length % 2 == 1) {
            currentInt = oneDimArray[oneDimArray.length - 1] << 16;
            ints[ints.length - 1] = currentInt;
        }
        return ints;
    }

    public static short[][][] convertSaveDataToBlocks(int[] ints, short sizeX, short sizeY, short sizeZ) {
        short[] oneDimArray = new short[ints.length * 2];
        for (int i = 0; i < ints.length; i++) {
            oneDimArray[i * 2] = (short) (ints[i] >> 16);
            oneDimArray[i * 2 + 1] = (short) ints[i];
        }
        short[][][] multDimArray = new short[sizeY][sizeZ][sizeX];
        int i = 0;
        for (short y = 0; y < sizeY; y++) {
            for (short z = 0; z < sizeZ; z++) {
                for (short x = 0; x < sizeX; x++) {
                    multDimArray[y][z][x] = oneDimArray[i++];
                }
            }
        }
        return multDimArray;
    }
}