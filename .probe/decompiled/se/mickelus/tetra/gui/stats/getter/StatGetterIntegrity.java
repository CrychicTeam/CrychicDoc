package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModuleMajor;

@ParametersAreNonnullByDefault
public class StatGetterIntegrity implements IStatGetter {

    @Override
    public boolean shouldShow(Player player, ItemStack currentStack, ItemStack previewStack) {
        return true;
    }

    @Override
    public double getValue(Player player, ItemStack itemStack) {
        return (double) (IModularItem.getIntegrityGain(itemStack) - IModularItem.getIntegrityCost(itemStack));
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot) {
        return (double) ((Integer) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, slot)).map(module -> module.getIntegrityGain(itemStack) - module.getIntegrityCost(itemStack)).orElse(0)).intValue();
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot, String improvement) {
        return (double) ((Integer) CastOptional.cast(itemStack.getItem(), IModularItem.class).flatMap(item -> CastOptional.cast(item.getModuleFromSlot(itemStack, slot), ItemModuleMajor.class)).map(module -> module.getImprovement(itemStack, improvement)).map(improvementData -> improvementData.integrity).orElse(0)).intValue();
    }
}