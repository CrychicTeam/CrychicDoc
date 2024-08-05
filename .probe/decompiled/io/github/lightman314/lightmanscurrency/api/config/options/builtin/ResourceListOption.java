package io.github.lightman314.lightmanscurrency.api.config.options.builtin;

import io.github.lightman314.lightmanscurrency.api.config.options.ListOption;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParser;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.NonNullSupplier;

public class ResourceListOption extends ListOption<ResourceLocation> {

    protected ResourceListOption(@Nonnull NonNullSupplier<List<ResourceLocation>> defaultValue) {
        super(defaultValue);
    }

    @Override
    protected ConfigParser<ResourceLocation> getPartialParser() {
        return ResourceOption.PARSER;
    }

    public static ResourceListOption create(@Nonnull List<ResourceLocation> defaultValue) {
        return new ResourceListOption(() -> defaultValue);
    }

    public static ResourceListOption create(@Nonnull NonNullSupplier<List<ResourceLocation>> defaultValue) {
        return new ResourceListOption(defaultValue);
    }
}