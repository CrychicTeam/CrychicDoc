package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.effect.ItemEffect;

@ParametersAreNonnullByDefault
public class StatGetterSweepingRange extends StatGetterEffectEfficiency {

    IStatGetter levelGetter = new StatGetterEffectLevel(ItemEffect.sweeping, 1.0);

    public StatGetterSweepingRange() {
        super(ItemEffect.sweeping, 1.0);
    }

    @Override
    public boolean shouldShow(Player player, ItemStack currentStack, ItemStack previewStack) {
        return !this.levelGetter.shouldShow(player, currentStack, previewStack) && super.shouldShow(player, currentStack, previewStack);
    }
}