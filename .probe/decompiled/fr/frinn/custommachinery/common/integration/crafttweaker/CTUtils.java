package fr.frinn.custommachinery.common.integration.crafttweaker;

import com.blamejared.crafttweaker.api.data.IData;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.platform.Services;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

public class CTUtils {

    @Nullable
    public static CompoundTag getNBT(@Nullable IData data) {
        return data != null && data.getInternal() instanceof CompoundTag ? (CompoundTag) data.getInternal() : null;
    }

    @Nullable
    public static CompoundTag nbtFromStack(IItemStack stack) {
        return nbtFromStack(stack.getInternal());
    }

    @Nullable
    public static CompoundTag nbtFromStack(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        if (nbt != null && !nbt.isEmpty()) {
            if (nbt.contains("Damage", 3) && nbt.getInt("Damage") == 0) {
                nbt.remove("Damage");
            }
            return nbt.isEmpty() ? null : nbt;
        } else {
            return null;
        }
    }

    public static ResourceLocation biomeID(Biome biome) {
        return Services.REGISTRY.biomes().getKey(biome);
    }

    public static void resetRecipesIDs() {
        CustomMachineRecipeCTBuilder.IDS.clear();
        CustomCraftRecipeCTBuilder.IDS.clear();
    }
}