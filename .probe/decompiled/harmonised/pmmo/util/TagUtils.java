package harmonised.pmmo.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TagUtils {

    public static CompoundTag mergeTags(CompoundTag tag1, CompoundTag tag2) {
        CompoundTag output = new CompoundTag();
        List<String> allKeys = new ArrayList();
        tag1.getAllKeys().forEach(s -> allKeys.add(s));
        for (String key : tag2.getAllKeys()) {
            if (!allKeys.contains(key) && key != null) {
                allKeys.add(key);
            }
        }
        for (String keyx : allKeys) {
            if (tag1.contains(keyx) && tag2.contains(keyx)) {
                Tag var7 = tag1.get(keyx);
                if (var7 instanceof NumericTag) {
                    NumericTag numTag = (NumericTag) var7;
                    if (numTag instanceof DoubleTag) {
                        output.putDouble(keyx, tag1.getDouble(keyx) + tag2.getDouble(keyx));
                    } else if (numTag instanceof FloatTag) {
                        output.putFloat(keyx, tag1.getFloat(keyx) + tag2.getFloat(keyx));
                    } else if (numTag instanceof IntTag) {
                        output.putInt(keyx, tag1.getInt(keyx) + tag2.getInt(keyx));
                    } else if (numTag instanceof LongTag) {
                        output.putLong(keyx, tag1.getLong(keyx) + tag2.getLong(keyx));
                    } else if (numTag instanceof ShortTag) {
                        output.putShort(keyx, (short) (tag1.getShort(keyx) + tag2.getShort(keyx)));
                    } else {
                        output.put(keyx, tag1.get(keyx));
                    }
                } else {
                    output.put(keyx, tag1.get(keyx));
                }
            } else if (tag1.contains(keyx) && !tag2.contains(keyx)) {
                output.put(keyx, tag1.get(keyx));
            } else if (!tag1.contains(keyx) && tag2.contains(keyx)) {
                output.put(keyx, tag2.get(keyx));
            }
        }
        return output;
    }

    public static CompoundTag stackTag(ItemStack stack) {
        return stack != null && stack.getTag() != null ? stack.getTag() : new CompoundTag();
    }

    public static CompoundTag entityTag(Entity entity) {
        CompoundTag data = new CompoundTag();
        return entity == null ? data : entity.saveWithoutId(data);
    }

    public static CompoundTag tileTag(BlockEntity tile) {
        return tile == null ? new CompoundTag() : tile.saveWithFullMetadata();
    }

    public static CompoundTag stateTag(BlockState state) {
        CompoundTag dataOut = new CompoundTag();
        state.m_61147_().forEach(prop -> dataOut.putString(prop.getName(), state.m_61143_(prop).toString()));
        return dataOut;
    }

    public static float getFloat(CompoundTag nbt, String key, float ifAbsent) {
        return nbt.contains(key) ? nbt.getFloat(key) : ifAbsent;
    }

    public static BlockPos getBlockPos(CompoundTag nbt, String key, BlockPos ifAbsent) {
        return nbt.contains(key) ? BlockPos.of(nbt.getLong(key)) : ifAbsent;
    }
}