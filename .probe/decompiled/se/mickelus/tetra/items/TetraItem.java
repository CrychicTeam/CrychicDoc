package se.mickelus.tetra.items;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.Item;

@ParametersAreNonnullByDefault
public class TetraItem extends Item implements InitializableItem {

    public TetraItem(Item.Properties properties) {
        super(properties);
    }
}