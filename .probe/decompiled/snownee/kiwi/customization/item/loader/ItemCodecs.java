package snownee.kiwi.customization.item.loader;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import snownee.kiwi.customization.item.MultipleBlockItem;

public class ItemCodecs {

    private static final Map<ResourceLocation, MapCodec<Item>> CODECS = Maps.newHashMap();

    public static final String ITEM_PROPERTIES_KEY = "properties";

    private static final Codec<Item.Properties> ITEM_PROPERTIES = Codec.unit(Item.Properties::new);

    public static final Function<Item.Properties, Item> SIMPLE_ITEM_FACTORY = Item::new;

    public static final MapCodec<Item> ITEM = simpleCodec(SIMPLE_ITEM_FACTORY);

    public static <I extends Item> RecordCodecBuilder<I, Item.Properties> propertiesCodec() {
        return ITEM_PROPERTIES.fieldOf("properties").forGetter(item -> {
            throw new UnsupportedOperationException();
        });
    }

    public static <I extends Item> MapCodec<I> simpleCodec(Function<Item.Properties, I> function) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(propertiesCodec()).apply(instance, function));
    }

    public static void register(ResourceLocation key, MapCodec<? extends Item> codec) {
        CODECS.put(key, codec);
    }

    public static MapCodec<Item> get(ResourceLocation key) {
        return (MapCodec<Item>) Objects.requireNonNull((MapCodec) CODECS.get(key), key::toString);
    }

    static {
        register(new ResourceLocation("item"), ITEM);
        register(new ResourceLocation("blocks"), MultipleBlockItem.CODEC);
    }
}