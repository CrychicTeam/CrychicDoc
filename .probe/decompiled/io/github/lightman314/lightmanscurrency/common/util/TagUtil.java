package io.github.lightman314.lightmanscurrency.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import org.lwjgl.system.NonnullDefault;

@NonnullDefault
public class TagUtil {

    public static CompoundTag saveBlockPos(BlockPos pos) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", pos.m_123341_());
        tag.putInt("y", pos.m_123342_());
        tag.putInt("z", pos.m_123343_());
        return tag;
    }

    public static BlockPos loadBlockPos(CompoundTag tag) {
        return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
    }
}