package net.mehvahdjukaar.moonlight.api.item;

import dev.architectury.injectables.annotations.PlatformOnly;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class FuelBlockItem extends BlockItem {

    private final Supplier<Integer> burnTime;

    public FuelBlockItem(Block pBlock, Item.Properties pProperties, Supplier<Integer> burnTime) {
        super(pBlock, pProperties);
        this.burnTime = burnTime;
        PlatHelper.getPlatform().ifFabric(() -> {
            int b = (Integer) burnTime.get();
            if (b != 0) {
                RegHelper.registerItemBurnTime(this, b);
            }
        });
    }

    @PlatformOnly({ "forge" })
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return (Integer) this.burnTime.get();
    }
}