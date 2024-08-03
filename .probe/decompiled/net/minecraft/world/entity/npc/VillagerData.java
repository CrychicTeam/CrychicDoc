package net.minecraft.world.entity.npc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;

public class VillagerData {

    public static final int MIN_VILLAGER_LEVEL = 1;

    public static final int MAX_VILLAGER_LEVEL = 5;

    private static final int[] NEXT_LEVEL_XP_THRESHOLDS = new int[] { 0, 10, 70, 150, 250 };

    public static final Codec<VillagerData> CODEC = RecordCodecBuilder.create(p_258961_ -> p_258961_.group(BuiltInRegistries.VILLAGER_TYPE.m_194605_().fieldOf("type").orElseGet(() -> VillagerType.PLAINS).forGetter(p_150024_ -> p_150024_.type), BuiltInRegistries.VILLAGER_PROFESSION.m_194605_().fieldOf("profession").orElseGet(() -> VillagerProfession.NONE).forGetter(p_150022_ -> p_150022_.profession), Codec.INT.fieldOf("level").orElse(1).forGetter(p_150020_ -> p_150020_.level)).apply(p_258961_, VillagerData::new));

    private final VillagerType type;

    private final VillagerProfession profession;

    private final int level;

    public VillagerData(VillagerType villagerType0, VillagerProfession villagerProfession1, int int2) {
        this.type = villagerType0;
        this.profession = villagerProfession1;
        this.level = Math.max(1, int2);
    }

    public VillagerType getType() {
        return this.type;
    }

    public VillagerProfession getProfession() {
        return this.profession;
    }

    public int getLevel() {
        return this.level;
    }

    public VillagerData setType(VillagerType villagerType0) {
        return new VillagerData(villagerType0, this.profession, this.level);
    }

    public VillagerData setProfession(VillagerProfession villagerProfession0) {
        return new VillagerData(this.type, villagerProfession0, this.level);
    }

    public VillagerData setLevel(int int0) {
        return new VillagerData(this.type, this.profession, int0);
    }

    public static int getMinXpPerLevel(int int0) {
        return canLevelUp(int0) ? NEXT_LEVEL_XP_THRESHOLDS[int0 - 1] : 0;
    }

    public static int getMaxXpPerLevel(int int0) {
        return canLevelUp(int0) ? NEXT_LEVEL_XP_THRESHOLDS[int0] : 0;
    }

    public static boolean canLevelUp(int int0) {
        return int0 >= 1 && int0 < 5;
    }
}