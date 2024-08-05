package io.github.lightman314.lightmanscurrency.common.villager_merchant.listings.mods;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParsingException;
import io.github.lightman314.lightmanscurrency.common.config.VillagerTradeModsOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.ForgeRegistries;

public class VillagerTradeMods {

    private final Map<String, ConfiguredTradeMod> modMap;

    private VillagerTradeMods(@Nonnull VillagerTradeMods.Builder builder) {
        Map<String, ConfiguredTradeMod> temp = new HashMap();
        builder.dataMap.forEach((key, subBuilder) -> temp.put(key, subBuilder.build()));
        this.modMap = ImmutableMap.copyOf(temp);
    }

    public VillagerTradeMods(@Nonnull List<String> parseableData) {
        Map<String, ConfiguredTradeMod> temp = new HashMap();
        for (String data : parseableData) {
            try {
                Pair<String, ConfiguredTradeMod> results = this.tryParseEntry(data);
                if (temp.containsKey(results.getFirst())) {
                    throw new ConfigParsingException("Duplicate profession type '" + (String) results.getFirst() + "' modded!");
                }
                temp.put((String) results.getFirst(), (ConfiguredTradeMod) results.getSecond());
            } catch (ConfigParsingException var6) {
                LightmansCurrency.LogError("Error parsing '" + data + "' as a villager trade modification.", var6);
            }
        }
        this.modMap = ImmutableMap.copyOf(temp);
    }

    public final List<String> writeToConfig() {
        List<String> data = new ArrayList();
        this.modMap.forEach((key, entry) -> {
            StringBuilder builder = new StringBuilder(key).append("-");
            entry.write(builder);
            data.add(builder.toString());
        });
        return data;
    }

    private Pair<String, ConfiguredTradeMod> tryParseEntry(@Nonnull String entry) throws ConfigParsingException {
        String[] split = entry.split("-", 2);
        if (split.length < 2) {
            throw new ConfigParsingException("Missing '-' dividers!");
        } else {
            String profession = split[0];
            LightmansCurrency.LogDebug("Attempting to parse entries for '" + profession + "' profession.");
            ConfiguredTradeMod mod = ConfiguredTradeMod.tryParse(split[1], false);
            return Pair.of(profession, mod);
        }
    }

    public final VillagerTradeMod getModFor(@Nonnull String trader) {
        return this.modMap.containsKey(trader) ? (VillagerTradeMod) this.modMap.get(trader) : LCConfig.COMMON.defaultEmeraldReplacementMod.get();
    }

    public static VillagerTradeMods.Builder builder() {
        return new VillagerTradeMods.Builder();
    }

    public static final class Builder {

        private final Map<String, ConfiguredTradeMod.ModBuilder> dataMap = new HashMap();

        private Builder() {
        }

        @Nonnull
        public ConfiguredTradeMod.ModBuilder forProfession(@Nonnull VillagerProfession profession) {
            return this.forProfession(ForgeRegistries.VILLAGER_PROFESSIONS.getKey(profession));
        }

        public ConfiguredTradeMod.ModBuilder forProfession(@Nonnull ResourceLocation profession) {
            return this.forProfession(profession.toString());
        }

        @Nonnull
        public ConfiguredTradeMod.ModBuilder forProfession(@Nonnull String profession) {
            if (!this.dataMap.containsKey(profession)) {
                this.dataMap.put(profession, ConfiguredTradeMod.builder(this));
            }
            return (ConfiguredTradeMod.ModBuilder) this.dataMap.get(profession);
        }

        @Nonnull
        public VillagerTradeMods build() {
            return new VillagerTradeMods(this);
        }

        @Nonnull
        public VillagerTradeModsOption buildOption() {
            return VillagerTradeModsOption.create(this::build);
        }
    }
}