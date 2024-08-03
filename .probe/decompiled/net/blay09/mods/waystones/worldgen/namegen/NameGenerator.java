package net.blay09.mods.waystones.worldgen.namegen;

import com.google.common.collect.Sets;
import java.util.Objects;
import java.util.Set;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.GenerateWaystoneNameEvent;
import net.blay09.mods.waystones.api.IWaystone;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

public class NameGenerator extends SavedData {

    private static final String DATA_NAME = "waystones_NameGenerator";

    private static final String USED_NAMES = "UsedNames";

    private static final NameGenerator clientStorageCopy = new NameGenerator();

    private final Set<String> usedNames = Sets.newHashSet();

    private INameGenerator getNameGenerator(NameGenerationMode nameGenerationMode) {
        switch(nameGenerationMode) {
            case MIXED:
                return new MixedNameGenerator(new MrPorkNameGenerator(), new CustomNameGenerator(false, this.usedNames));
            case RANDOM_ONLY:
                return new MrPorkNameGenerator();
            case PRESET_ONLY:
                return new CustomNameGenerator(true, this.usedNames);
            case PRESET_FIRST:
            default:
                return new SequencedNameGenerator(new CustomNameGenerator(false, this.usedNames), new MrPorkNameGenerator());
        }
    }

    public synchronized String getName(IWaystone waystone, RandomSource rand, NameGenerationMode nameGenerationMode) {
        INameGenerator nameGenerator = this.getNameGenerator(nameGenerationMode);
        String originalName = nameGenerator.randomName(rand);
        if (originalName == null) {
            originalName = (String) Objects.requireNonNull(new MrPorkNameGenerator().randomName(rand));
        }
        String name = this.resolveDuplicate(originalName);
        GenerateWaystoneNameEvent event = new GenerateWaystoneNameEvent(waystone, name);
        Balm.getEvents().fireEvent(event);
        name = event.getName();
        this.usedNames.add(name);
        this.m_77762_();
        return name;
    }

    private String resolveDuplicate(String name) {
        String tryName = name;
        for (int i = 1; this.usedNames.contains(tryName); i++) {
            tryName = name + " " + RomanNumber.toRoman(i);
        }
        return tryName;
    }

    public static NameGenerator load(CompoundTag compound) {
        NameGenerator nameGenerator = new NameGenerator();
        for (Tag tag : compound.getList("UsedNames", 8)) {
            nameGenerator.usedNames.add(tag.getAsString());
        }
        return nameGenerator;
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        ListTag tagList = new ListTag();
        for (String entry : this.usedNames) {
            tagList.add(StringTag.valueOf(entry));
        }
        compound.put("UsedNames", tagList);
        return compound;
    }

    public static NameGenerator get(@Nullable MinecraftServer server) {
        if (server != null) {
            ServerLevel overworld = server.getLevel(Level.OVERWORLD);
            return ((ServerLevel) Objects.requireNonNull(overworld)).getDataStorage().computeIfAbsent(NameGenerator::load, NameGenerator::new, "waystones_NameGenerator");
        } else {
            return clientStorageCopy;
        }
    }
}