package snownee.kiwi.customization.item.loader;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import snownee.kiwi.customization.CustomizationRegistries;

public abstract class KItemTemplate {

    protected final Optional<ItemDefinitionProperties> properties;

    public static Codec<KItemTemplate> codec() {
        return CustomizationRegistries.ITEM_TEMPLATE.byNameCodec().dispatch("type", KItemTemplate::type, type -> (Codec) type.codec().get());
    }

    protected KItemTemplate(Optional<ItemDefinitionProperties> properties) {
        this.properties = properties;
    }

    public abstract KItemTemplate.Type<?> type();

    public abstract void resolve(ResourceLocation var1);

    abstract Item createItem(ResourceLocation var1, Item.Properties var2, JsonObject var3);

    public final Optional<ItemDefinitionProperties> properties() {
        return this.properties;
    }

    public static record Type<T extends KItemTemplate>(Supplier<Codec<T>> codec) {
    }
}