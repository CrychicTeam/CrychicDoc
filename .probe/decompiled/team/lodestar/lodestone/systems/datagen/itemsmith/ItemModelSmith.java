package team.lodestar.lodestone.systems.datagen.itemsmith;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import team.lodestar.lodestone.systems.datagen.providers.LodestoneItemModelProvider;

public class ItemModelSmith extends AbstractItemModelSmith {

    public final ItemModelSmith.ItemModelSupplier modelSupplier;

    public ItemModelSmith(ItemModelSmith.ItemModelSupplier modelSupplier) {
        this.modelSupplier = modelSupplier;
    }

    @SafeVarargs
    public final void act(AbstractItemModelSmith.ItemModelSmithData data, Supplier<? extends Item>... items) {
        for (Supplier<? extends Item> item : items) {
            this.act(data, item);
        }
        List.of(items).forEach(data.consumer);
    }

    public void act(AbstractItemModelSmith.ItemModelSmithData data, Collection<Supplier<? extends Item>> items) {
        items.forEach(r -> this.act(data, r));
        new ArrayList(items).forEach(data.consumer);
    }

    private void act(AbstractItemModelSmith.ItemModelSmithData data, Supplier<? extends Item> registryObject) {
        Item item = (Item) registryObject.get();
        this.modelSupplier.act(item, data.provider);
    }

    public void act(Supplier<? extends Item> registryObject, LodestoneItemModelProvider provider) {
        Item item = (Item) registryObject.get();
        this.modelSupplier.act(item, provider);
    }

    public interface ItemModelSupplier {

        void act(Item var1, LodestoneItemModelProvider var2);
    }
}