package io.github.lightman314.lightmanscurrency.api.traders;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class InteractionSlotData {

    public static final String FLUID_TYPE = "BUCKET_SLOT";

    public static final String ENERGY_TYPE = "ENERGY_SLOT";

    public final String type;

    protected InteractionSlotData(String type) {
        this.type = type;
    }

    public abstract boolean allowItemInSlot(ItemStack var1);

    @Nullable
    public Pair<ResourceLocation, ResourceLocation> emptySlotBG() {
        return null;
    }

    public static boolean allowItemInSlot(List<InteractionSlotData> slots, ItemStack item) {
        for (InteractionSlotData slot : slots) {
            if (slot.allowItemInSlot(item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasInteractionSlot(List<InteractionSlotData> slots, String type) {
        for (InteractionSlotData slot : slots) {
            if (slot.type.contentEquals(type)) {
                return true;
            }
        }
        return false;
    }
}