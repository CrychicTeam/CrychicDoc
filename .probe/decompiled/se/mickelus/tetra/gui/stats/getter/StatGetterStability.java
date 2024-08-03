package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.effect.ItemEffect;

@ParametersAreNonnullByDefault
public class StatGetterStability implements IStatGetter {

    private final StatGetterEffectLevel stabilizingGetter = new StatGetterEffectLevel(ItemEffect.stabilizing, 1.0);

    private final StatGetterEffectLevel unstableGetter = new StatGetterEffectLevel(ItemEffect.unstable, 1.0);

    @Override
    public boolean shouldShow(Player player, ItemStack currentStack, ItemStack previewStack) {
        return this.getValue(player, currentStack) != 0.0 || this.getValue(player, previewStack) != 0.0;
    }

    @Override
    public double getValue(Player player, ItemStack itemStack) {
        return this.stabilizingGetter.getValue(player, itemStack) - this.unstableGetter.getValue(player, itemStack);
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot) {
        return this.stabilizingGetter.getValue(player, itemStack, slot) - this.unstableGetter.getValue(player, itemStack, slot);
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot, String improvement) {
        return this.stabilizingGetter.getValue(player, itemStack, slot, improvement) - this.unstableGetter.getValue(player, itemStack, slot, improvement);
    }
}