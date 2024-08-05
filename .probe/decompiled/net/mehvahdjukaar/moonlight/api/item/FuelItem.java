package net.mehvahdjukaar.moonlight.api.item;

import dev.architectury.injectables.annotations.PlatformOnly;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public class FuelItem extends Item {

    private final Supplier<Integer> burnTime;

    public FuelItem(Item.Properties pProperties, Supplier<Integer> burnTime) {
        super(pProperties);
        this.burnTime = burnTime;
        if (PlatHelper.getPlatform().isFabric()) {
            int b = (Integer) burnTime.get();
            if (b != 0) {
                RegHelper.registerItemBurnTime(this, b);
            }
        }
    }

    @PlatformOnly({ "forge" })
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return (Integer) this.burnTime.get();
    }
}