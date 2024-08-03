package dev.xkmc.l2backpack.content.capability;

import dev.xkmc.l2library.util.nbt.ItemCompoundTag;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record PickupConfig(PickupMode pickup, DestroyMode destroy) {

    private static final String KEY_ROOT = "backpack_pickup_config";

    private static final String KEY_MODE = "mode";

    private static final String KEY_VOID = "void";

    public static PickupConfig getPickupMode(ItemStack stack) {
        ItemCompoundTag tag = ItemCompoundTag.of(stack).getSubTag("backpack_pickup_config");
        PickupMode mode = PickupMode.NONE;
        DestroyMode destroy = DestroyMode.NONE;
        if (tag.isPresent()) {
            CompoundTag ctag = tag.getOrCreate();
            if (ctag.contains("mode")) {
                String str = ctag.getString("mode");
                mode = PickupMode.valueOf(str);
            }
            if (ctag.contains("void")) {
                String str = ctag.getString("void");
                destroy = DestroyMode.valueOf(str);
            }
        }
        return new PickupConfig(mode, destroy);
    }

    public static CompoundTag getConfig(ItemStack stack) {
        return ItemCompoundTag.of(stack).getSubTag("backpack_pickup_config").getOrCreate();
    }

    public static void setConfig(ItemStack stack, @Nullable CompoundTag config) {
        if (config != null) {
            ItemCompoundTag.of(stack).getSubTag("backpack_pickup_config").setTag(config);
        }
    }

    public static void setConfig(ItemStack stack, @Nullable PickupConfig config) {
        if (config != null) {
            CompoundTag root = ItemCompoundTag.of(stack).getSubTag("backpack_pickup_config").getOrCreate();
            root.putString("mode", config.pickup.name());
            root.putString("void", config.destroy.name());
        }
    }

    public static void addText(ItemStack stack, List<Component> list) {
        PickupConfig mode = getPickupMode(stack);
        list.add(mode.pickup().getTooltip());
        list.add(mode.destroy().getTooltip());
    }

    public static void iterateMode(ItemStack stack) {
        PickupConfig mode = getPickupMode(stack);
        PickupMode next = PickupMode.values()[(mode.pickup().ordinal() + 1) % PickupMode.values().length];
        getConfig(stack).putString("mode", next.name());
    }

    public static void iterateDestroy(ItemStack stack) {
        PickupConfig mode = getPickupMode(stack);
        DestroyMode next = DestroyMode.values()[(mode.destroy().ordinal() + 1) % DestroyMode.values().length];
        getConfig(stack).putString("void", next.name());
    }

    public static PickupConfig iterateMode(PickupConfig mode) {
        PickupMode next = PickupMode.values()[(mode.pickup().ordinal() + 1) % PickupMode.values().length];
        return new PickupConfig(next, mode.destroy);
    }

    public static PickupConfig iterateDestroy(PickupConfig mode) {
        DestroyMode next = DestroyMode.values()[(mode.destroy().ordinal() + 1) % DestroyMode.values().length];
        return new PickupConfig(mode.pickup, next);
    }
}