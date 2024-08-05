package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.properties.IToolProvider;

@ParametersAreNonnullByDefault
public class StatGetterToolLevel implements IStatGetter {

    private final ToolAction tool;

    public StatGetterToolLevel(ToolAction tool) {
        this.tool = tool;
    }

    @Override
    public double getValue(Player player, ItemStack itemStack) {
        return (double) ((Integer) CastOptional.cast(itemStack.getItem(), IToolProvider.class).map(item -> item.getToolData(itemStack)).map(data -> data.getLevel(this.tool)).orElse(0)).intValue();
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot) {
        return (double) ((Integer) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, slot)).map(module -> module.getToolLevel(itemStack, this.tool)).orElse(0)).intValue();
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot, String improvement) {
        return (double) ((Integer) CastOptional.cast(itemStack.getItem(), IModularItem.class).flatMap(item -> CastOptional.cast(item.getModuleFromSlot(itemStack, slot), ItemModuleMajor.class)).map(module -> module.getImprovement(itemStack, improvement)).map(improvementData -> improvementData.tools).map(data -> data.getLevel(this.tool)).orElse(0)).intValue();
    }
}