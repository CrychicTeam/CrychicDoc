package io.github.lightman314.lightmanscurrency.common.villager_merchant.listings.mods;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParsingException;
import io.github.lightman314.lightmanscurrency.common.villager_merchant.listings.configured.ConfiguredTradeModOption;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public class ConfiguredTradeMod extends VillagerTradeMod {

    private final Pair<Item, Item> defaultReplacements;

    private final Map<String, Pair<Item, Item>> regionalReplacements;

    public ConfiguredTradeMod(@Nonnull Pair<Item, Item> defaultReplacements, @Nonnull Map<String, Pair<Item, Item>> regionalReplacements) {
        this.defaultReplacements = defaultReplacements;
        this.regionalReplacements = ImmutableMap.copyOf(regionalReplacements);
    }

    @Nonnull
    public static ConfiguredTradeMod tryParse(@Nonnull String entries, boolean forceDefaults) throws ConfigParsingException {
        Pair<Item, Item> defaultReplacements = Pair.of(null, null);
        Map<String, Pair<Item, Item>> regionalReplacements = new HashMap();
        String[] split = entries.split("-");
        for (String subEntry : split) {
            String region = null;
            String[] subSplits = subEntry.split(";");
            Item costReplacement;
            Item resultReplacement;
            if (!subEntry.startsWith("r;") && !subEntry.startsWith("R;")) {
                LightmansCurrency.LogDebug("Parsing default items:");
                costReplacement = tryParseItem(subSplits[0]);
                if (subSplits.length > 1) {
                    resultReplacement = tryParseItem(subSplits[1]);
                } else {
                    resultReplacement = costReplacement;
                }
            } else {
                if (subSplits.length < 3) {
                    throw new ConfigParsingException("Entry starts with 'r;' but is missing either the villager type or the defined replacement entry.");
                }
                if (subSplits.length > 4) {
                    throw new ConfigParsingException("Entry contains too many ';' splits!");
                }
                region = subSplits[1];
                LightmansCurrency.LogDebug("Parsing items for '" + region + "' region:");
                if (subSplits.length > 3) {
                    costReplacement = tryParseItem(subSplits[2]);
                    resultReplacement = tryParseItem(subSplits[3]);
                } else {
                    costReplacement = resultReplacement = tryParseItem(subSplits[2]);
                }
            }
            if (region == null && shouldWrite(defaultReplacements)) {
                throw new ConfigParsingException("Entry cannot have multiple default entries! All non-default entries should start with 'r;VILLAGER_TYPE; to define their subsequent region.");
            }
            if (region != null && regionalReplacements.containsKey(region)) {
                throw new ConfigParsingException("Entry has duplicate villager type entry for the '" + region + "' villager type!");
            }
            if (region == null) {
                defaultReplacements = Pair.of(costReplacement, resultReplacement);
            } else {
                regionalReplacements.put(region, Pair.of(costReplacement, resultReplacement));
            }
        }
        if (forceDefaults && (defaultReplacements.getFirst() == null || defaultReplacements.getSecond() == null)) {
            throw new ConfigParsingException("Missing default cost and/or result entry!");
        } else if (defaultReplacements.getFirst() == null && defaultReplacements.getSecond() == null && regionalReplacements.isEmpty()) {
            throw new ConfigParsingException("No valid sub-entries were parsed!");
        } else {
            return new ConfiguredTradeMod(defaultReplacements, regionalReplacements);
        }
    }

    @Nullable
    private static Item tryParseItem(@Nonnull String item) throws ConfigParsingException {
        LightmansCurrency.LogDebug("Attempting to parse '" + item + "' as an item!");
        if (!item.isBlank() && !item.equals("minecraft:air")) {
            try {
                return ForgeRegistries.ITEMS.getValue(new ResourceLocation(item));
            } catch (ResourceLocationException var2) {
                throw new ConfigParsingException(item + " is not a valid ResourceLocation!", var2);
            }
        } else {
            return null;
        }
    }

    private static boolean shouldWrite(@Nonnull Pair<Item, Item> pair) {
        return pair.getFirst() != null || pair.getSecond() != null;
    }

    public final void write(@Nonnull StringBuilder builder) {
        AtomicBoolean addDash = new AtomicBoolean(false);
        if (shouldWrite(this.defaultReplacements)) {
            if (addDash.get()) {
                builder.append("-");
            } else {
                addDash.set(true);
            }
            writePair(this.defaultReplacements, builder);
        }
        this.regionalReplacements.forEach((key, pair) -> {
            if (shouldWrite(pair)) {
                if (addDash.get()) {
                    builder.append("-");
                } else {
                    addDash.set(true);
                }
                builder.append("r;").append(key).append(";");
                writePair(pair, builder);
            }
        });
    }

    private static void writePair(@Nonnull Pair<Item, Item> pair, @Nonnull StringBuilder builder) {
        if (pair.getFirst() != null && pair.getFirst() == pair.getSecond()) {
            builder.append(getID((Item) pair.getFirst()));
        } else {
            builder.append(getID((Item) pair.getFirst())).append(";").append(getID((Item) pair.getSecond()));
        }
    }

    @Nonnull
    private static String getID(@Nullable Item item) {
        return item == null ? "" : ForgeRegistries.ITEMS.getKey(item).toString();
    }

    @Nullable
    private String getType(@Nullable Entity villager) {
        if (villager instanceof Villager v) {
            VillagerData d = v.getVillagerData();
            if (d != null) {
                VillagerType t = d.getType();
                if (t != null) {
                    return BuiltInRegistries.VILLAGER_TYPE.getKey(t).toString();
                }
            }
        }
        return null;
    }

    private Pair<Item, Item> getPair(@Nonnull Entity villager) {
        String type = this.getType(villager);
        return type == null ? this.defaultReplacements : (Pair) this.regionalReplacements.getOrDefault(type, this.defaultReplacements);
    }

    @Nullable
    private Item getCost(@Nonnull Entity villager) {
        Pair<Item, Item> pair = this.getPair(villager);
        Item first = (Item) pair.getFirst();
        return first == null && this != LCConfig.COMMON.defaultEmeraldReplacementMod.get() ? LCConfig.COMMON.defaultEmeraldReplacementMod.get().getCost(villager) : first;
    }

    @Nonnull
    private Item getResult(@Nonnull Entity villager) {
        Pair<Item, Item> pair = this.getPair(villager);
        Item second = (Item) pair.getSecond();
        return second == null && this != LCConfig.COMMON.defaultEmeraldReplacementMod.get() ? LCConfig.COMMON.defaultEmeraldReplacementMod.get().getResult(villager) : second;
    }

    @Nonnull
    @Override
    public ItemStack modifyCost(@Nullable Entity villager, @Nonnull ItemStack cost) {
        return cost.getItem() == Items.EMERALD ? this.copyWithNewItem(cost, this.getCost(villager)) : cost;
    }

    @Nonnull
    @Override
    public ItemStack modifyResult(@Nullable Entity villager, @Nonnull ItemStack result) {
        return result.getItem() == Items.EMERALD ? this.copyWithNewItem(result, this.getResult(villager)) : result;
    }

    public static ConfiguredTradeMod.ModBuilder builder() {
        return new ConfiguredTradeMod.ModBuilder(null);
    }

    public static ConfiguredTradeMod.ModBuilder builder(@Nonnull VillagerTradeMods.Builder parent) {
        return new ConfiguredTradeMod.ModBuilder(parent);
    }

    @Nonnull
    public static Pair<Item, Item> buildPair(@Nonnull Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> pair) {
        return Pair.of(safeGet((Supplier<? extends ItemLike>) pair.getFirst()), safeGet((Supplier<? extends ItemLike>) pair.getSecond()));
    }

    @Nullable
    private static Item safeGet(@Nullable Supplier<? extends ItemLike> supplier) {
        return supplier != null && supplier.get() != null ? ((ItemLike) supplier.get()).asItem() : null;
    }

    public static final class ModBuilder {

        private final VillagerTradeMods.Builder parent;

        private Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> defaultReplacement = Pair.of(null, null);

        private final Map<String, Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>>> regionalReplacements = new HashMap();

        private ModBuilder(@Nullable VillagerTradeMods.Builder parent) {
            this.parent = parent;
        }

        @Nullable
        public VillagerTradeMods.Builder back() {
            return this.parent;
        }

        @Nonnull
        private Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> replaceCost(@Nonnull Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> pair, Supplier<? extends ItemLike> newCost) {
            return Pair.of(newCost, (Supplier) pair.getSecond());
        }

        private Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> replaceResult(@Nonnull Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> pair, Supplier<? extends ItemLike> newResult) {
            return Pair.of((Supplier) pair.getFirst(), newResult);
        }

        @Nonnull
        public ConfiguredTradeMod.ModBuilder defaultCost(@Nonnull Supplier<? extends ItemLike> costReplacement) {
            this.defaultReplacement = this.replaceCost(this.defaultReplacement, costReplacement);
            return this;
        }

        @Nonnull
        public ConfiguredTradeMod.ModBuilder defaultResult(@Nonnull Supplier<? extends ItemLike> resultReplacement) {
            this.defaultReplacement = this.replaceResult(this.defaultReplacement, resultReplacement);
            return this;
        }

        @Nonnull
        public ConfiguredTradeMod.ModBuilder defaults(@Nonnull Supplier<? extends ItemLike> replacement) {
            this.defaultReplacement = Pair.of(replacement, replacement);
            return this;
        }

        @Nonnull
        public ConfiguredTradeMod.ModBuilder costForRegion(@Nonnull VillagerType type, @Nonnull Supplier<? extends ItemLike> costReplacement) {
            return this.costForRegion(BuiltInRegistries.VILLAGER_TYPE.getKey(type), costReplacement);
        }

        public ConfiguredTradeMod.ModBuilder costForRegion(@Nonnull ResourceLocation type, @Nonnull Supplier<? extends ItemLike> costReplacement) {
            return this.costForRegion(type.toString(), costReplacement);
        }

        @Nonnull
        public ConfiguredTradeMod.ModBuilder costForRegion(@Nonnull String type, @Nonnull Supplier<? extends ItemLike> costReplacement) {
            Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> pair = (Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>>) this.regionalReplacements.getOrDefault(type, Pair.of(null, null));
            this.regionalReplacements.put(type, this.replaceCost(pair, costReplacement));
            return this;
        }

        @Nonnull
        public ConfiguredTradeMod.ModBuilder resultForRegion(@Nonnull VillagerType type, @Nonnull Supplier<? extends ItemLike> resultReplacement) {
            return this.resultForRegion(BuiltInRegistries.VILLAGER_TYPE.getKey(type), resultReplacement);
        }

        @Nonnull
        public ConfiguredTradeMod.ModBuilder resultForRegion(@Nonnull ResourceLocation type, @Nonnull Supplier<? extends ItemLike> resultReplacement) {
            return this.resultForRegion(type.toString(), resultReplacement);
        }

        @Nonnull
        public ConfiguredTradeMod.ModBuilder resultForRegion(@Nonnull String type, @Nonnull Supplier<? extends ItemLike> resultReplacement) {
            Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> pair = (Pair<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>>) this.regionalReplacements.getOrDefault(type, Pair.of(null, null));
            this.regionalReplacements.put(type, this.replaceResult(pair, resultReplacement));
            return this;
        }

        @Nonnull
        public ConfiguredTradeMod.ModBuilder bothForRegion(@Nonnull VillagerType type, @Nonnull Supplier<? extends ItemLike> replacement) {
            return this.bothForRegion(BuiltInRegistries.VILLAGER_TYPE.getKey(type), replacement);
        }

        @Nonnull
        public ConfiguredTradeMod.ModBuilder bothForRegion(@Nonnull ResourceLocation type, @Nonnull Supplier<? extends ItemLike> replacement) {
            return this.bothForRegion(type.toString(), replacement);
        }

        @Nonnull
        public ConfiguredTradeMod.ModBuilder bothForRegion(@Nonnull String type, @Nonnull Supplier<? extends ItemLike> replacement) {
            this.regionalReplacements.put(type, Pair.of(replacement, replacement));
            return this;
        }

        @Nonnull
        public ConfiguredTradeMod build() {
            Map<String, Pair<Item, Item>> temp = new HashMap();
            this.regionalReplacements.forEach((key, pair) -> temp.put(key, ConfiguredTradeMod.buildPair(pair)));
            return new ConfiguredTradeMod(ConfiguredTradeMod.buildPair(this.defaultReplacement), temp);
        }

        @Nonnull
        public ConfiguredTradeModOption buildOption() {
            return ConfiguredTradeModOption.create(this::build);
        }
    }
}