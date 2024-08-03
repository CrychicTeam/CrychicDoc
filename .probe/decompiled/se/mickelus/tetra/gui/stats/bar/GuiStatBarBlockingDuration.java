package se.mickelus.tetra.gui.stats.bar;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectEfficiency;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterBlockingDuration;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterBlockingReflect;

@ParametersAreNonnullByDefault
public class GuiStatBarBlockingDuration extends GuiStatBar {

    private static final IStatGetter durationGetter = new StatGetterEffectLevel(ItemEffect.blocking, 1.0);

    private static final IStatGetter cooldownGetter = new StatGetterEffectEfficiency(ItemEffect.blocking, 1.0);

    public GuiStatBarBlockingDuration(int x, int y, int width) {
        super(x, y, width, I18n.get("tetra.stats.blocking"), 0.0, 16.0, false, durationGetter, LabelGetterBasic.integerLabel, new TooltipGetterBlockingDuration(durationGetter, cooldownGetter));
        this.setIndicators(new GuiStatIndicator[] { new GuiStatIndicator(0, 0, "tetra.stats.blocking_reflect", 2, new StatGetterEffectLevel(ItemEffect.blockingReflect, 1.0), new TooltipGetterBlockingReflect()) });
    }

    @Override
    public void update(Player player, ItemStack currentStack, ItemStack previewStack, String slot, String improvement) {
        super.update(player, currentStack, previewStack, slot, improvement);
        if (durationGetter.getValue(player, currentStack) >= 16.0 || durationGetter.getValue(player, previewStack) >= 16.0) {
            this.valueString.setString("");
        }
    }
}