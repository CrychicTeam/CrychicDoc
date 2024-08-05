package se.mickelus.tetra.gui.stats.getter;

import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModuleMajor;

@ParametersAreNonnullByDefault
public class StatGetterUnbreaking implements IStatGetter {

    private final IStatGetter levelGetter;

    public StatGetterUnbreaking(IStatGetter levelGetter) {
        this.levelGetter = levelGetter;
    }

    @Override
    public double getValue(Player player, ItemStack itemStack) {
        return 100.0 - 100.0 / (this.levelGetter.getValue(player, itemStack) + 1.0);
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot) {
        double levelItem = this.levelGetter.getValue(player, itemStack);
        return (Double) Optional.of(this.levelGetter.getValue(player, itemStack, slot)).map(level -> 100.0 / (levelItem - level + 1.0) - 100.0 / (levelItem + 1.0)).orElse(0.0);
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot, String improvement) {
        double levelItem = this.levelGetter.getValue(player, itemStack);
        return (Double) CastOptional.cast(itemStack.getItem(), IModularItem.class).flatMap(item -> CastOptional.cast(item.getModuleFromSlot(itemStack, slot), ItemModuleMajor.class)).map(module -> module.getImprovement(itemStack, improvement)).map(data -> data.effects.getLevel(ItemEffect.unbreaking)).map(level -> 100.0 / (levelItem - (double) level.intValue() + 1.0) - 100.0 / (levelItem + 1.0)).orElse(0.0);
    }
}