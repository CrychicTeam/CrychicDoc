package io.github.lightman314.lightmanscurrency.api.config.options.builtin;

import io.github.lightman314.lightmanscurrency.api.config.options.ConfigOption;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParser;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParsingException;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemOption extends ConfigOption<Item> {

    public static final ConfigParser<Item> PARSER = new ItemOption.Parser(true);

    public static final ConfigParser<Item> PARSER_NO_AIR = new ItemOption.Parser(false);

    private final boolean allowAir;

    protected ItemOption(@Nonnull NonNullSupplier<Item> defaultValue, boolean allowAir) {
        super(defaultValue);
        this.allowAir = allowAir;
    }

    @Nonnull
    @Override
    protected ConfigParser<Item> getParser() {
        return this.allowAir ? PARSER : PARSER_NO_AIR;
    }

    public static ItemOption create(@Nonnull RegistryObject<? extends ItemLike> defaultValue) {
        return new ItemOption(() -> defaultValue.get().asItem(), true);
    }

    public static ItemOption create(@Nonnull RegistryObject<? extends ItemLike> defaultValue, boolean allowAir) {
        return new ItemOption(() -> defaultValue.get().asItem(), allowAir);
    }

    public static ItemOption create(@Nonnull NonNullSupplier<Item> defaultValue) {
        return new ItemOption(defaultValue, true);
    }

    public static ItemOption create(@Nonnull NonNullSupplier<Item> defaultValue, boolean allowAir) {
        return new ItemOption(defaultValue, allowAir);
    }

    private static class Parser implements ConfigParser<Item> {

        private final boolean allowAir;

        private Parser(boolean allowAir) {
            this.allowAir = allowAir;
        }

        @Nonnull
        public Item tryParse(@Nonnull String cleanLine) throws ConfigParsingException {
            if (cleanLine.isBlank() && this.allowAir) {
                return Items.AIR;
            } else {
                ResourceLocation itemID = ResourceOption.PARSER.tryParse(cleanLine);
                if (!ForgeRegistries.ITEMS.containsKey(itemID)) {
                    throw new ConfigParsingException("No item found with id " + itemID + "!");
                } else {
                    Item item = ForgeRegistries.ITEMS.getValue(itemID);
                    if (item == Items.AIR && !this.allowAir) {
                        throw new ConfigParsingException("Air is not an allowed item!");
                    } else {
                        return item;
                    }
                }
            }
        }

        @Nonnull
        public String write(@Nonnull Item value) {
            return ResourceOption.PARSER.write(ForgeRegistries.ITEMS.getKey(value));
        }
    }
}