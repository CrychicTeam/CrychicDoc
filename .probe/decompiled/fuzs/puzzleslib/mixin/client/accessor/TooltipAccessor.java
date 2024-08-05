package fuzs.puzzleslib.mixin.client.accessor;

import java.util.List;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ Tooltip.class })
public interface TooltipAccessor {

    @Accessor("cachedTooltip")
    void puzzleslib$setCachedTooltip(@Nullable List<FormattedCharSequence> var1);
}