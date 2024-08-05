package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModuleMajor;

@ParametersAreNonnullByDefault
public class StatGetterEffectLevel implements IStatGetter {

    protected final ItemEffect effect;

    protected final double multiplier;

    protected final double base;

    public StatGetterEffectLevel(ItemEffect effect, double multiplier) {
        this(effect, multiplier, 0.0);
    }

    public StatGetterEffectLevel(ItemEffect effect, double multiplier, double base) {
        this.effect = effect;
        this.multiplier = multiplier;
        this.base = base;
    }

    @Override
    public double getValue(Player player, ItemStack itemStack) {
        return this.base + (Double) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> (double) item.getEffectLevel(itemStack, this.effect) * this.multiplier).orElse(0.0);
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot) {
        return (Double) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, slot)).map(module -> (double) module.getEffectLevel(itemStack, this.effect) * this.multiplier).orElse(0.0);
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot, String improvement) {
        return this.base + (Double) CastOptional.cast(itemStack.getItem(), IModularItem.class).flatMap(item -> CastOptional.cast(item.getModuleFromSlot(itemStack, slot), ItemModuleMajor.class)).map(module -> module.getImprovement(itemStack, improvement)).map(improvementData -> improvementData.effects).map(effects -> (double) effects.getLevel(this.effect) * this.multiplier).orElse(0.0);
    }
}