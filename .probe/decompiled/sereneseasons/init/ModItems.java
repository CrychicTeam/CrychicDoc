package sereneseasons.init;

import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import sereneseasons.api.SSItems;
import sereneseasons.core.SereneSeasons;
import sereneseasons.item.CalendarItem;

public class ModItems {

    public static void setup() {
        registerItems();
    }

    private static void registerItems() {
        SSItems.SS_ICON = registerItem(() -> new Item(new Item.Properties()), "ss_icon");
        SSItems.CALENDAR = registerItem(() -> new CalendarItem(new Item.Properties().stacksTo(1)), "calendar");
    }

    public static RegistryObject<Item> registerItem(Supplier<Item> itemSupplier, String name) {
        return SereneSeasons.ITEM_REGISTER.register(name, itemSupplier);
    }
}