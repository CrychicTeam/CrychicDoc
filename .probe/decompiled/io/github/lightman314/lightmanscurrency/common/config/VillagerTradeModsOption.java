package io.github.lightman314.lightmanscurrency.common.config;

import io.github.lightman314.lightmanscurrency.api.config.options.ConfigOption;
import io.github.lightman314.lightmanscurrency.api.config.options.ListOption;
import io.github.lightman314.lightmanscurrency.api.config.options.basic.StringOption;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParser;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParsingException;
import io.github.lightman314.lightmanscurrency.common.villager_merchant.listings.mods.VillagerTradeMods;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraftforge.common.util.NonNullSupplier;

public class VillagerTradeModsOption extends ConfigOption<VillagerTradeMods> {

    public static final ConfigParser<VillagerTradeMods> PARSER = new VillagerTradeModsOption.Parser();

    private VillagerTradeModsOption(@Nonnull NonNullSupplier<VillagerTradeMods> defaultValue) {
        super(defaultValue);
    }

    @Nonnull
    public static VillagerTradeModsOption create(@Nonnull NonNullSupplier<VillagerTradeMods> mods) {
        return new VillagerTradeModsOption(mods);
    }

    @Nonnull
    @Override
    protected ConfigParser<VillagerTradeMods> getParser() {
        return PARSER;
    }

    private static final class Parser implements ConfigParser<VillagerTradeMods> {

        private final ConfigParser<List<String>> PARSER;

        private Parser() {
            this.PARSER = ListOption.makeParser(StringOption.PARSER);
        }

        @Nonnull
        public VillagerTradeMods tryParse(@Nonnull String cleanLine) throws ConfigParsingException {
            return new VillagerTradeMods(this.PARSER.tryParse(cleanLine));
        }

        @Nonnull
        public String write(@Nonnull VillagerTradeMods value) {
            return this.PARSER.write(value.writeToConfig());
        }
    }
}