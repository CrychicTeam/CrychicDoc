package snownee.kiwi.customization.item;

import java.util.function.Consumer;
import net.minecraft.world.item.Item;

public class KItemSettings {

    private KItemSettings(KItemSettings.Builder builder) {
    }

    public static KItemSettings empty() {
        return new KItemSettings(builder());
    }

    public static KItemSettings.Builder builder() {
        return new KItemSettings.Builder(new Item.Properties());
    }

    public static class Builder {

        private final Item.Properties properties;

        private Builder(Item.Properties properties) {
            this.properties = properties;
        }

        public Item.Properties get() {
            return this.properties;
        }

        public KItemSettings.Builder configure(Consumer<Item.Properties> configurator) {
            configurator.accept(this.properties);
            return this;
        }
    }
}