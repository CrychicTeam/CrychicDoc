package net.minecraft.data.structures;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.slf4j.Logger;

public class StructureUpdater implements SnbtToNbt.Filter {

    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public CompoundTag apply(String string0, CompoundTag compoundTag1) {
        return string0.startsWith("data/minecraft/structures/") ? update(string0, compoundTag1) : compoundTag1;
    }

    public static CompoundTag update(String string0, CompoundTag compoundTag1) {
        StructureTemplate $$2 = new StructureTemplate();
        int $$3 = NbtUtils.getDataVersion(compoundTag1, 500);
        int $$4 = 3437;
        if ($$3 < 3437) {
            LOGGER.warn("SNBT Too old, do not forget to update: {} < {}: {}", new Object[] { $$3, 3437, string0 });
        }
        CompoundTag $$5 = DataFixTypes.STRUCTURE.updateToCurrentVersion(DataFixers.getDataFixer(), compoundTag1, $$3);
        $$2.load(BuiltInRegistries.BLOCK.m_255303_(), $$5);
        return $$2.save(new CompoundTag());
    }
}