package io.github.lightman314.lightmanscurrency.api.config.options.builtin;

import io.github.lightman314.lightmanscurrency.api.config.options.ConfigOption;
import io.github.lightman314.lightmanscurrency.api.config.options.basic.StringOption;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParser;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParsingException;
import javax.annotation.Nonnull;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.NonNullSupplier;

public class ResourceOption extends ConfigOption<ResourceLocation> {

    public static final ConfigParser<ResourceLocation> PARSER = new ResourceOption.Parser();

    protected ResourceOption(@Nonnull NonNullSupplier<ResourceLocation> defaultValue) {
        super(defaultValue);
    }

    public static ResourceOption create(@Nonnull ResourceLocation defaultValue) {
        return new ResourceOption(() -> defaultValue);
    }

    public static ResourceOption create(@Nonnull NonNullSupplier<ResourceLocation> defaultValue) {
        return new ResourceOption(defaultValue);
    }

    @Nonnull
    @Override
    protected ConfigParser<ResourceLocation> getParser() {
        return PARSER;
    }

    private static class Parser implements ConfigParser<ResourceLocation> {

        @Nonnull
        public ResourceLocation tryParse(@Nonnull String cleanLine) throws ConfigParsingException {
            String s = StringOption.PARSER.tryParse(cleanLine);
            try {
                return new ResourceLocation(s);
            } catch (ResourceLocationException var4) {
                throw new ConfigParsingException(s + " is not a valid Resource Location!", var4);
            }
        }

        @Nonnull
        public String write(@Nonnull ResourceLocation value) {
            return value.toString();
        }
    }
}