package io.redspace.ironsspellbooks.api.spells;

import com.google.common.util.concurrent.AtomicDouble;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.IForgeRegistry;

public enum SpellRarity {

    COMMON(0), UNCOMMON(1), RARE(2), EPIC(3), LEGENDARY(4);

    private final int value;

    private static final LazyOptional<List<Double>> rawRarityConfig = LazyOptional.of(SpellRarity::getRawRarityConfigInternal);

    private static List<Double> rarityConfig = null;

    private final MutableComponent[] DISPLAYS = new MutableComponent[] { Component.translatable("rarity.irons_spellbooks.common").withStyle(ChatFormatting.GRAY), Component.translatable("rarity.irons_spellbooks.uncommon").withStyle(ChatFormatting.GREEN), Component.translatable("rarity.irons_spellbooks.rare").withStyle(ChatFormatting.AQUA), Component.translatable("rarity.irons_spellbooks.epic").withStyle(ChatFormatting.LIGHT_PURPLE), Component.translatable("rarity.irons_spellbooks.legendary").withStyle(ChatFormatting.GOLD), Component.translatable("rarity.irons_spellbooks.mythic").withStyle(ChatFormatting.GOLD), Component.translatable("rarity.irons_spellbooks.ancient").withStyle(ChatFormatting.GOLD) };

    private SpellRarity(int newValue) {
        this.value = newValue;
    }

    public int getValue() {
        return this.value;
    }

    public MutableComponent getDisplayName() {
        return this.DISPLAYS[this.getValue()];
    }

    public static List<Double> getRawRarityConfig() {
        return (List<Double>) rawRarityConfig.resolve().get();
    }

    private static List<Double> getRawRarityConfigInternal() {
        List<Double> fromConfig = ServerConfigs.RARITY_CONFIG.get();
        if (fromConfig.size() != 5) {
            List<Double> configDefault = ServerConfigs.RARITY_CONFIG.getDefault();
            IronsSpellbooks.LOGGER.info("INVALID RARITY CONFIG FOUND (Size != 5): {} FALLING BACK TO DEFAULT: {}", fromConfig, configDefault);
            return configDefault;
        } else if (fromConfig.stream().mapToDouble(a -> a).sum() != 1.0) {
            List<Double> configDefault = ServerConfigs.RARITY_CONFIG.getDefault();
            IronsSpellbooks.LOGGER.info("INVALID RARITY CONFIG FOUND (Values must add up to 1): {} FALLING BACK TO DEFAULT: {}", fromConfig, configDefault);
            return configDefault;
        } else {
            return fromConfig;
        }
    }

    public static List<Double> getRarityConfig() {
        if (rarityConfig == null) {
            AtomicDouble counter = new AtomicDouble();
            rarityConfig = new ArrayList();
            getRawRarityConfig().forEach(item -> rarityConfig.add(counter.addAndGet(item)));
        }
        return rarityConfig;
    }

    public int compareRarity(SpellRarity other) {
        return Integer.compare(this.getValue(), other.getValue());
    }

    public static void rarityTest() {
        StringBuilder sb = new StringBuilder();
        ((IForgeRegistry) SpellRegistry.REGISTRY.get()).getValues().forEach(s -> {
            sb.append(String.format("\nSpellType:%s\n", s));
            sb.append(String.format("\tMinRarity:%s, MaxRarity:%s\n", s.getMinRarity(), s.getMaxRarity()));
            sb.append(String.format("\tMinLevel:%s, MaxLevel:%s\n", s.getMinLevel(), s.getMaxLevel()));
            sb.append(String.format("\tRawRarityConfig:%s\n", getRawRarityConfig().stream().map(Object::toString).collect(Collectors.joining(","))));
            sb.append(String.format("\tRarityConfig:%s\n", getRarityConfig().stream().map(Object::toString).collect(Collectors.joining(","))));
            for (int i = s.getMinLevel(); i <= s.getMaxLevel(); i++) {
                sb.append(String.format("\t\tLevel %s -> %s\n", i, s.getRarity(i)));
            }
            sb.append("\n");
            for (int i = s.getMinRarity(); i <= s.getMaxRarity(); i++) {
                sb.append(String.format("\t\t%s -> Level %s\n", values()[i], s.getMinLevelForRarity(values()[i])));
            }
        });
    }

    public ChatFormatting getChatFormatting() {
        return switch(this) {
            case COMMON ->
                ChatFormatting.GRAY;
            case UNCOMMON ->
                ChatFormatting.GREEN;
            case RARE ->
                ChatFormatting.AQUA;
            case EPIC ->
                ChatFormatting.LIGHT_PURPLE;
            case LEGENDARY ->
                ChatFormatting.GOLD;
        };
    }
}