package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.effect.ItemEffect;

@ParametersAreNonnullByDefault
public class StatGetterSpread extends StatGetterEffectEfficiency {

    public StatGetterSpread(ItemEffect effect) {
        super(effect, 1.0);
    }

    protected double wrapEfficiency(double eff) {
        return Math.max(0.0, 100.0 - eff);
    }

    private double offsetToAngle(double eff) {
        return Math.atan(Math.sqrt(2.0 * Math.pow(this.wrapEfficiency(eff) * 0.0172275, 2.0))) * 180.0 / Math.PI;
    }

    @Override
    public boolean shouldShow(Player player, ItemStack currentStack, ItemStack previewStack) {
        return super.getValue(player, currentStack) > 0.0 || super.getValue(player, previewStack) > 0.0;
    }

    @Override
    public double getValue(Player player, ItemStack itemStack) {
        return this.offsetToAngle(super.getValue(player, itemStack));
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot) {
        return this.offsetToAngle(super.getValue(player, itemStack, slot));
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot, String improvement) {
        return this.offsetToAngle(super.getValue(player, itemStack, slot, improvement));
    }
}