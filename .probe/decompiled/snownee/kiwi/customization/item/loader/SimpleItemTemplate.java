package snownee.kiwi.customization.item.loader;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public final class SimpleItemTemplate extends KItemTemplate {

    private final String clazz;

    private Function<Item.Properties, Item> constructor;

    public SimpleItemTemplate(Optional<ItemDefinitionProperties> properties, String clazz) {
        super(properties);
        this.clazz = clazz;
    }

    public static Codec<SimpleItemTemplate> directCodec() {
        return RecordCodecBuilder.create(instance -> instance.group(ItemDefinitionProperties.mapCodecField().forGetter(KItemTemplate::properties), Codec.STRING.optionalFieldOf("class", "").forGetter(SimpleItemTemplate::clazz)).apply(instance, SimpleItemTemplate::new));
    }

    @Override
    public KItemTemplate.Type<?> type() {
        return KItemTemplates.SIMPLE.getOrCreate();
    }

    @Override
    public void resolve(ResourceLocation key) {
        if (this.clazz.isEmpty()) {
            this.constructor = ItemCodecs.SIMPLE_ITEM_FACTORY;
        } else {
            try {
                Class<?> clazz = Class.forName(this.clazz);
                this.constructor = $ -> {
                    try {
                        return (Item) clazz.getConstructor(Item.Properties.class).newInstance($);
                    } catch (Throwable var3x) {
                        throw new RuntimeException(var3x);
                    }
                };
            } catch (Throwable var3) {
                throw new IllegalStateException(var3);
            }
        }
    }

    @Override
    public Item createItem(ResourceLocation id, Item.Properties settings, JsonObject input) {
        return (Item) this.constructor.apply(settings);
    }

    public String clazz() {
        return this.clazz;
    }

    public String toString() {
        return "SimpleItemTemplate[properties=" + this.properties + ", clazz=" + this.clazz + ", constructor=" + this.constructor + "]";
    }
}