package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModuleMajor;

@ParametersAreNonnullByDefault
public class StatGetterDurability implements IStatGetter {

    @Override
    public double getValue(Player player, ItemStack itemStack) {
        return (double) itemStack.getMaxDamage();
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot) {
        return (double) ((Integer) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, slot)).map(module -> module.getProperties(itemStack)).map(data -> data.durability + (data.durabilityMultiplier != 0.0F ? (int) ((data.durabilityMultiplier - 1.0F) * (float) itemStack.getMaxDamage()) : 0)).orElse(0)).intValue();
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot, String improvement) {
        return (double) ((Integer) CastOptional.cast(itemStack.getItem(), IModularItem.class).flatMap(item -> CastOptional.cast(item.getModuleFromSlot(itemStack, slot), ItemModuleMajor.class)).map(module -> module.getImprovement(itemStack, improvement)).map(data -> data.durability + (data.durabilityMultiplier != 0.0F ? (int) ((data.durabilityMultiplier - 1.0F) * (float) itemStack.getMaxDamage()) : 0)).orElse(0)).intValue();
    }
}