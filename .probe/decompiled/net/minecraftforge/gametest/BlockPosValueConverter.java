package net.minecraftforge.gametest;

import joptsimple.ValueConverter;
import net.minecraft.core.BlockPos;

public class BlockPosValueConverter implements ValueConverter<BlockPos> {

    public BlockPos convert(String value) {
        String[] split = value.split(",");
        return BlockPos.containing(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
    }

    public Class<BlockPos> valueType() {
        return BlockPos.class;
    }

    public String valuePattern() {
        return null;
    }
}